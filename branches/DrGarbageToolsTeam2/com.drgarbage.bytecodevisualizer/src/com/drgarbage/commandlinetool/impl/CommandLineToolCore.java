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
package com.drgarbage.commandlinetool.impl;

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
import com.drgarbage.commandlinetool.intf.IByteCodeConfiguration;
import com.drgarbage.commandlinetool.intf.IGraphConfiguration;
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
	
	/**
	 * Starts the graph exporter and prints the graph.
	 * @param config config file for graph preferences
	 * @throws ControlFlowGraphException
	 * @throws IOException
	 * @return String object with the Graph of the ByteCode in the specified format
	 */
	public static String startGraphExporter(InputStream inputStream, String methodName,
			String methodSignatur, IGraphConfiguration config) throws ControlFlowGraphException, IOException{

		IDirectedGraphExt graph = null;
		AbstractExport2 exporter = null;
		ControlFlowGraphDiagram diagram = null;
		
		switch(config.getOutputType()){
		case ExportFormat_PrintNodes:
		case ExportFormat_SourceCodeGraph:	
			FilteringCodeVisitor codeVisitor = getInstructionList(inputStream, methodName, methodSignatur);
			graph = ControlFlowGraphGenerator.generateControlFlowGraph(codeVisitor.getInstructions(), codeVisitor.getLineNumberTable(), config.isStartVertex(), config.isEndVertex(), config.isBackEdge());			
			return printGraph(graph);
			
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
		FilteringCodeVisitor codeVisitor = getInstructionList(inputStream, methodName, methodSignatur);

		graph = ControlFlowGraphGenerator.generateControlFlowGraph(codeVisitor.getInstructions(), codeVisitor.getLineNumberTable(), config.isStartVertex(), config.isEndVertex(), config.isBackEdge());		diagram = ControlFlowGraphDiagramFactory.createControlFlowGraphDiagram(graph);
		diagram = ControlFlowGraphDiagramFactory.createControlFlowGraphDiagram(graph);

		IGraphSpecification graphSpecification = setConfiguredGraphSpecifications(config);
		
		exporter.setGraphSpecification(graphSpecification);
		
		StringWriter sb = new StringWriter();
		try {
			exporter.write(diagram, sb);
		} catch (ExportException e) {
			/* This will never happen as
			 * StringBuilder.append(*) does not throw IOException*/
			throw new RuntimeException(e);
		}
		
		return sb.toString();

	}
	
	/**
	 * Starts the byte code visualizer.
	 * @param contentStream as InputStream
	 * @param ByteCodeConfiguration
	 * @return String object with the visualized ByteCode
	 */
	public static String startByteCodeVisualizer(InputStream contentStream, IByteCodeConfiguration configuration) {
		
		if (configuration == null) {
			configuration = new ByteCodeConfiguration();
		}
		DataInputStream in = null;
		String byteCodeVisualizedString = null;
		try {
			/* buffer only if necessary */
			if (contentStream instanceof BufferedInputStream) {
				in = new DataInputStream(contentStream);
			}
			else {
				in = new DataInputStream(new BufferedInputStream(contentStream));
			}
			ClassFileOutlineElement outlineElement = new ClassFileOutlineElement();
	        ClassFileDocument doc = new ClassFileDocument(outlineElement, configuration);
	        outlineElement.setClassFileDocument(doc);
	        
	        ClassReader cr = new ClassReader(in, doc);
	        
	        cr.accept(doc, 0);
	        
	        byteCodeVisualizedString = doc.toString();

		} catch (Exception e) {
			System.err.println("error while using");
			e.printStackTrace();
		}
		return byteCodeVisualizedString;		
	}
	
	/**
	 * This method sets the export configurations for the graph
	 * @param config
	 * @return
	 */
	private static IGraphSpecification setConfiguredGraphSpecifications(IGraphConfiguration config) {
		IGraphSpecification graphSpecification = new DefaultGraphSpecification();
		graphSpecification.setExportComments(config.isExportComments());
		graphSpecification.setExportDecorations(config.isExportDecorations());
		graphSpecification.setExportGeometry(config.isExportGeometry());
		graphSpecification.setSupressMessages(config.isSuppressMessages());
		
		switch(config.getOutputType()){
		case ExportFormat_GraphXML_XML_Based:
			graphSpecification.setExportFormat(GraphConstants.EXPORT_FORMAT_GRAPHXML);
			break;
		case ExportFormat_DOT_Graph_Language:
			graphSpecification.setExportFormat(GraphConstants.EXPORT_FORMAT_DOT);
			break;
		case ExportFormat_GraphML_XML_Based:
			graphSpecification.setExportFormat(GraphConstants.EXPORT_FORMAT_GRAPHML);
			break;
		case ExportFormat_PrintNodes:
			graphSpecification.setExportFormat(GraphConstants.EXPORT_FORMAT_NONE);
			break;
		default:
			graphSpecification.setExportFormat(GraphConstants.EXPORT_FORMAT_NONE);
			break;
		}
		
		return graphSpecification;
	}

	/**
	 * This method prints the graph
	 * @param graph graph to print
	 */
	public static String printGraph(IDirectedGraphExt graph) {
		StringBuffer sb = new StringBuffer();
		sb.append("Print Graph:\n");
		sb.append("Nodes:\n");
		for (int i = 0; i < graph.getNodeList().size(); i++) {
			sb.append("  " + graph.getNodeList().getNodeExt(i).getByteCodeString() + "\n");
		}

		for (int i = 0; i < graph.getEdgeList().size(); i++) {
			IEdgeExt e = graph.getEdgeList().getEdgeExt(i);
			sb.append("  " 
					+ e.getSource().getByteCodeOffset()
					+ " - "
					+ e.getTarget().getByteCodeOffset() + "\n");
		}
		return sb.toString();
	}
	
	/**
	 * Returns instruction list. 
	 * @param classPath
	 * @param packageName
	 * @param className
	 * @param methodName
	 * @param methodSig
	 * @return instructions
	 * @throws ControlFlowGraphException
	 * @throws IOException
	 */
	private static FilteringCodeVisitor getInstructionList(InputStream in,
			String methodName,
			String methodSig) 
	throws ControlFlowGraphException, IOException
	{
		if (!(in instanceof BufferedInputStream)) {
			/* buffer only if necessary */
			in = new BufferedInputStream(in);
		}
		
		
        FilteringCodeVisitor codeVisitor = new FilteringCodeVisitor(methodName, methodSig);
        
        MethodFilteringVisitor classVisitor = new MethodFilteringVisitor(codeVisitor);
        ClassReader cr = new ClassReader(in, classVisitor);
        cr.accept(classVisitor, 0);
		if (codeVisitor.getInstructions() == null) {
			throw new ControlFlowGraphException(
					"ControlFlowGraphGenerator: can't get method info of the "
							+ methodName + methodSig);
		}
		return codeVisitor;
	}

	
}
