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
import java.util.Map;

import com.drgarbage.commandlinetool.impl.ByteCodeConfiguration;
import com.drgarbage.commandlinetool.impl.GraphConfiguration;
import com.drgarbage.commandlinetool.impl.MethodPair;
import com.drgarbage.commandlinetool.intf.CommandLineToolFactory;
import com.drgarbage.commandlinetool.intf.IByteCodeConfiguration;
import com.drgarbage.commandlinetool.intf.ICommandLineTool;
import com.drgarbage.commandlinetool.intf.IGraphConfiguration;
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

		String _classPath = "/Users/cihanaydin/Desktop/jxl.jar";
		String _packageName = "jxl.biff";
		String _className = "AutoFilter";
//		String _methodName = "write";
//		String _methodSignature = "(Ljxl/write/biff/File;)V";
		
		ICommandLineTool cc = CommandLineToolFactory.createCommandLineToolInterface();
						
		IByteCodeConfiguration byteCodeConfiguration = new ByteCodeConfiguration();
		IGraphConfiguration graphConfiguration = new GraphConfiguration("X");
		byteCodeConfiguration.setShowConstantPool(false);
		graphConfiguration.setBackEdge(true);
		
		Map<MethodPair, String> map = cc.visualizeGraphs(_classPath, _packageName, _className, graphConfiguration);
		
		for (MethodPair m : map.keySet()) {
			System.out.println(map.get(m));
		}
				
		
	}
}
