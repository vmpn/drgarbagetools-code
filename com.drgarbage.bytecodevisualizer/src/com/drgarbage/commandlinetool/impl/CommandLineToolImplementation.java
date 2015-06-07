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

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.eclipse.jdt.core.IClassFile;

import com.drgarbage.commandlinetool.intf.IByteCodeConfiguration;
import com.drgarbage.commandlinetool.intf.ICommandLineTool;
import com.drgarbage.commandlinetool.intf.IGraphConfiguration;
import com.drgarbage.controlflowgraph.ControlFlowGraphException;
import com.drgarbage.javalang.JavaLangUtils;

/**
 * CommandLineToolFactory
 * 
 * @author Cihan Aydin
 * @version $Revision: 739 $
 * $Id: CommandLineToolFactory.java 739 2015-06-02 21:22:23Z cihanaydin $
 */
public class CommandLineToolImplementation implements ICommandLineTool {
	
	/**
	 * Constructor to create an object and set the main values for class path, package name, class name, method name and signature
	 * 
	 * @param _classPath
	 * @param _packageName
	 * @param _className
	 * @param _methodName
	 * @param _methodSignature
	 */
	public CommandLineToolImplementation() {

	}
	
	public InputStream getInputStream(String classPath, String packageName, 
			String className) throws IOException{
		
		return JavaLangUtils.findResource(new String[] {classPath}, packageName, className);
	}


	public String visualizeClassFile(InputStream inputStream,
			IByteCodeConfiguration configuration) {
	
		return CommandLineToolCore.startByteCodeVisualizer(inputStream, configuration);
	}

	public String visualizeGraph(InputStream inputStream, String methodName, String methodSignatur, IGraphConfiguration configuration) {
		
		try {
			return CommandLineToolCore.startGraphExporter(inputStream, methodName, methodSignatur, configuration);
		} catch (ControlFlowGraphException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	public Map<MethodPair, String> visualizeGraphs(InputStream inputStream, IGraphConfiguration configuration){
		
		return null;
	}
	
	/*not yet implemented*/
	public String compareClassFiles(InputStream is1, InputStream is2) {
		return null;
	}

	public ByteCodeConfiguration parseArgumentsForByteCodeConfiguration(String arguments) {
		return new ByteCodeConfiguration(arguments);
	}

	public GraphConfiguration parseArgumentsForExportGraphConfiguration(String arguments) {
		return new GraphConfiguration(arguments);
	}
	
	public static ByteCodeConfiguration createParser(String[] args) {
		
		return new ByteCodeConfiguration(null);
	}

}
