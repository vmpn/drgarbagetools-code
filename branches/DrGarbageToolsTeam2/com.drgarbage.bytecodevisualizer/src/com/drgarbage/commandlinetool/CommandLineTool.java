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

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import com.drgarbage.commandlinetool.intf.CommandLineToolFactory;
import com.drgarbage.commandlinetool.intf.GraphOutputTypes;
import com.drgarbage.commandlinetool.intf.IByteCodeConfiguration;
import com.drgarbage.commandlinetool.intf.ICommandLineTool;
import com.drgarbage.commandlinetool.intf.IGraphConfiguration;
import com.drgarbage.commandlinetool.intf.IMethodPair;
import com.drgarbage.controlflowgraph.ControlFlowGraphException;

/**
 * The configuration space of a class file editor.
 * 
 * @author Cihan Aydin
 * @version $Revision$
 * $Id$
 */
public class CommandLineTool {
	
	/**
	 * @param args
	 * @throws IOException
	 * @throws ControlFlowGraphException
	 */
	public static void main(String[] args) throws IOException, ControlFlowGraphException{	
		XmlGraph();
		MlGraph();
		printGraphs();
		printGraph();
	}

	private static void XmlGraph() throws IOException {
		String classPath = "CommandLineToolExample.jar";
		String packageName = "com.drgarbage.example";
		String className = "CommandLineToolExample";
		
		String methodName = "sqrt";
		String methodSignatur = "(I)D";
		
		ICommandLineTool meinInterface = CommandLineToolFactory.createCommandLineToolInterface();
		IGraphConfiguration configuration = CommandLineToolFactory.createGraphConfigurationInterface();
		
		InputStream inputStream;
		inputStream = meinInterface.getInputStream(classPath, packageName, className);
		
		configuration.setOutputType(GraphOutputTypes.ExportFormat_GraphXML_XML_Based);
		
		String visualizedClassFile = meinInterface.visualizeGraph(inputStream, methodName, methodSignatur, configuration);
		System.out.println(visualizedClassFile);
	}
	
	private static void MlGraph() throws IOException {
		String classPath = "CommandLineToolExample.jar";
		String packageName = "com.drgarbage.example";
		String className = "CommandLineToolExample";
		
		String methodName = "sqrt";
		String methodSignatur = "(I)D";
		
		ICommandLineTool meinInterface = CommandLineToolFactory.createCommandLineToolInterface();
		IGraphConfiguration configuration = CommandLineToolFactory.createGraphConfigurationInterface();
		
		InputStream inputStream;
		inputStream = meinInterface.getInputStream(classPath, packageName, className);
		
		configuration.setOutputType(GraphOutputTypes.ExportFormat_GraphML_XML_Based);
		
		String etwas = meinInterface.visualizeGraph(inputStream, methodName, methodSignatur, configuration);
		System.out.println(etwas);
		
	}

	private static void printGraphs() throws IOException {
		String classPath = "CommandLineToolExample.jar";
		String packageName = "com.drgarbage.example";
		String className = "CommandLineToolExample";
		
		ICommandLineTool meinInterface = CommandLineToolFactory.createCommandLineToolInterface();
		IGraphConfiguration configuration = CommandLineToolFactory.createGraphConfigurationInterface();
		
		configuration.setOutputType(GraphOutputTypes.ExportFormat_GraphXML_XML_Based);		
		
		Map<IMethodPair, String> myMap = meinInterface.visualizeGraphs(classPath, packageName, className, configuration);
		
		for (IMethodPair mp: myMap.keySet()) {
			System.out.println("Jetzt kommt der XML-Graph zur Methode: " + mp.getMethodName() + " mit der Signatur: " + mp.getMethodSignature() + "\n");
			System.out.println(myMap.get(mp));	
		}
	}
	
	private static void printGraph() throws IOException {
		String classPath = "CommandLineToolExample.jar";
		String packageName = "com.drgarbage.example";
		String className = "CommandLineToolExample";
		
		
		ICommandLineTool meinInterface = CommandLineToolFactory.createCommandLineToolInterface();
		
		InputStream inputStream = meinInterface.getInputStream(classPath, packageName, className);
		
		IByteCodeConfiguration configuration = CommandLineToolFactory.createByteCodeConfigurationInterface();
		
		String visualizedClassFile = meinInterface.visualizeClassFile(inputStream, configuration);
		System.out.println(visualizedClassFile);
	}
}
