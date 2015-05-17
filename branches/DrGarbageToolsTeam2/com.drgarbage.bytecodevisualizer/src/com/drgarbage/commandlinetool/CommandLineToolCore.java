/**
 * Copyright (c) 2008-2015, Dr. Garbage Community
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.drgarbage.commandlinetool;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import com.drgarbage.asm.ClassReader;
import com.drgarbage.asm.render.impl.ClassFileDocument;
import com.drgarbage.asm.render.impl.ClassFileOutlineElement;
import com.drgarbage.asm.visitor.FilteringCodeVisitor;
import com.drgarbage.asm.visitor.MethodFilteringVisitor;
import com.drgarbage.controlflowgraph.ControlFlowGraphException;
import com.drgarbage.controlflowgraph.ControlFlowGraphGenerator;
import com.drgarbage.controlflowgraph.intf.IDirectedGraphExt;
import com.drgarbage.controlflowgraph.intf.IEdgeExt;
import com.drgarbage.controlflowgraphfactory.export.AbstractExport2;
import com.drgarbage.controlflowgraphfactory.export.ExportException;
import com.drgarbage.controlflowgraphfactory.export.GraphDOTExport;
import com.drgarbage.controlflowgraphfactory.export.GraphMlExport;
import com.drgarbage.controlflowgraphfactory.export.GraphXMLExport;
import com.drgarbage.graph.DefaultGraphSpecification;
import com.drgarbage.graph.GraphConstants;
import com.drgarbage.graph.IGraphSpecification;
import com.drgarbage.visualgraphic.model.ControlFlowGraphDiagram;
import com.drgarbage.visualgraphic.model.ControlFlowGraphDiagramFactory;

/**
 * This class represents the core functionalities of CommandLineTool
 * 
 * @author Cihan Aydin
 * @version $Revision: 723 $
 * $Id: CommandLineToolCore.java 723 2015-05-12 08:40:35Z cihanaydin $
 */

public class CommandLineToolCore {
	public static void startBCV(InputStream contentStream, ByteCodeConfiguration pocf) {
		
		DataInputStream in = null;
		
		try {
			/* buffer only if necessary */
			if (contentStream instanceof BufferedInputStream) {
				in = new DataInputStream(contentStream);
			}
			else {
				in = new DataInputStream(new BufferedInputStream(contentStream));
			}
			ClassFileOutlineElement outlineElement = new ClassFileOutlineElement();
	        ClassFileDocument doc = new ClassFileDocument(outlineElement, pocf);
	        
	        outlineElement.setClassFileDocument(doc);
	        ClassReader cr = new ClassReader(in, doc);
	        cr.accept(doc, 0);
	        System.out.println(doc.toString());

		} catch (Exception e) {
			System.err.println("error while using");
			e.printStackTrace();
		}		
	}
	
	public static void startExportGraph(InputStream in, ExportGraphConfiguration config) throws IOException, ControlFlowGraphException {
		FilteringCodeVisitor codeVisitor;
		
		if (!(in instanceof BufferedInputStream)) {
			/* buffer only if necessary */
			in = new BufferedInputStream(in);
		}
		
        codeVisitor = new FilteringCodeVisitor(config.getMethodName(), config.getMethodSignature());

        MethodFilteringVisitor classVisitor = new MethodFilteringVisitor(codeVisitor);
        ClassReader cr = new ClassReader(in, classVisitor);
        cr.accept(classVisitor, 0);
		if (codeVisitor.getInstructions() == null) {
			throw new ControlFlowGraphException(
					"ControlFlowGraphGenerator: can't get method info of the "
							+ config.getClassName() + config.getMethodSignature());
		}
		IDirectedGraphExt graph = ControlFlowGraphGenerator.generateControlFlowGraph(codeVisitor.getInstructions(), codeVisitor.getLineNumberTable(), true, true, true);
		
		if(config.getOutputType() != null && config.getOutputType().equals(GraphOutputTypes.ExportFormat_PrintNodes)) {
			printGraph(graph);
		} else if (config.getOutputType() != null) {	
			ControlFlowGraphDiagram g = ControlFlowGraphDiagramFactory.createControlFlowGraphDiagram(graph);

			IGraphSpecification graphSpecification = setConfiguredGraphSpecifications(config);
			
			AbstractExport2 exporter = null;
			switch(config.getOutputType()){
			case ExportFormat_GraphML_XML_Based:
				exporter = new GraphMlExport();
				break;
			case ExportFormat_DOT_Graph_Language:
				exporter = new GraphDOTExport();
				break;
			case ExportFormat_GraphXML_XML_Based:
				exporter = new GraphXMLExport();
				break;
			default:
				break;
			}
			
			exporter.setGraphSpecification(graphSpecification);
			StringWriter sb = new StringWriter();
			try {
				exporter.write(g, sb);
			} catch (ExportException e) {
				/* This will never happen as
				 * StringBuilder.append(*) does not throw IOException*/
				throw new RuntimeException(e);
			}
			System.out.println(sb.toString());
		}
	}
	
	private static IGraphSpecification setConfiguredGraphSpecifications(ExportGraphConfiguration config) {
		IGraphSpecification graphSpecification = new DefaultGraphSpecification();
		graphSpecification.setExportComments(config.isExportComments());
		graphSpecification.setExportDecorations(config.isExportDecorations());
		graphSpecification.setExportGeometry(config.isExportGeometry());
		graphSpecification.setSupressMessages(config.isSupressMessages());
		switch(config.getOutputType()){
		case ExportFormat_GraphML_XML_Based:
			graphSpecification.setExportFormat(GraphConstants.EXPORT_FORMAT_GRAPHML);
			break;
		case ExportFormat_DOT_Graph_Language:
			graphSpecification.setExportFormat(GraphConstants.EXPORT_FORMAT_DOT);
			break;
		case ExportFormat_GraphXML_XML_Based:
			graphSpecification.setExportFormat(GraphConstants.EXPORT_FORMAT_GRAPHXML);
			break;
		case ExportFormat_PrintNodes:
			graphSpecification.setExportFormat(GraphConstants.EXPORT_FORMAT_NONE);
			break;
		default:
			break;
		}
		return graphSpecification;
	}

	public static void printGraph(IDirectedGraphExt g) {
		
		System.out.println("Print Graph:");

		System.out.println("Nodes:");
		for (int i = 0; i < g.getNodeList().size(); i++) {
			System.out.println("  " + g.getNodeList().getNodeExt(i).getByteCodeString());
		}

		System.out.println("Edges:");
		for (int i = 0; i < g.getEdgeList().size(); i++) {
			IEdgeExt e = g.getEdgeList().getEdgeExt(i);
			System.out.println("  " 
					+ e.getSource().getByteCodeOffset()
					+ " - "
					+ e.getTarget().getByteCodeOffset());
		}
	}
}
