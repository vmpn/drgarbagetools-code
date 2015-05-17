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

import java.io.IOException;

import com.drgarbage.controlflowgraph.ControlFlowGraphException;

/**
 * The configuration space of a class file editor.
 * 
 * @author Cihan Aydin
 * @version $Revision$
 * $Id$
 */
public class CommandLineTool {
	
	public static void main(String[] args) throws IOException, ControlFlowGraphException{	
		
		ParseArguments parser = new ParseArguments(args);
		
		switch (parser.getGraphOutputType()) {
		case ExportFormat_DOT_Graph_Language:
		case ExportFormat_GraphXML_XML_Based:
		case ExportFormat_GraphML_XML_Based:
		case ExportFormat_PrintNodes:
			CommandLineToolCore.startExportGraph(parser.getInputStream(), parser.getExportGraphConfiguration());
			break;
		case ExportFormat_ByteCode:
			CommandLineToolCore.startBCV(parser.getInputStream(), parser.getByteCodeConfiguration());
			break;
		default:
			break;
		}
	}
}
