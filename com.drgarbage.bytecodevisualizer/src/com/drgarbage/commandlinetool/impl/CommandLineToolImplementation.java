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
import java.util.HashMap;
import java.util.Map;

import com.drgarbage.asm.render.impl.ClassFileDocument;
import com.drgarbage.asm.render.intf.IClassFileDocument;
import com.drgarbage.asm.render.intf.IMethodSection;
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
	
	public Map<MethodPair, String> visualizeGraphs(String _classPath, String _packageName, String _className, IGraphConfiguration configuration){
		Map<MethodPair, String> map = new HashMap<MethodPair, String>();
		
		try {
			InputStream in = null;
			in = this.getInputStream(_classPath, _packageName, _className);
			
			IClassFileDocument classFileDoc = ClassFileDocument.readClass(in);
			java.util.List<IMethodSection> methodSelectionList = classFileDoc.getMethodSections();

			for (IMethodSection methodSelection : methodSelectionList) {
				in = this.getInputStream(_classPath, _packageName, _className);
				MethodPair methodPair = new MethodPair(methodSelection.getName(), methodSelection.getDescriptor());
				map.put(methodPair, CommandLineToolCore.startGraphExporter(in, methodSelection.getName(), methodSelection.getDescriptor(), configuration));
			}
			return map;
			
			
		} catch (ControlFlowGraphException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return map;
	}
	
	/*not yet implemented*/
	public String compareClassFiles(InputStream is1, InputStream is2) {
		return null;
	}

}
