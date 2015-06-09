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
package com.drgarbage.commandlinetool.intf;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import com.drgarbage.commandlinetool.impl.MethodPair;

/**
 * This Interface represents the connection to the Command Line Tool - API.
 * @author cihanaydin
 */
public interface ICommandLineTool {
	
	/**
	 * method to receive the input stream with given parameters
	 * @param classPath Example: <code>/tmp/class/container.jar</code> or <code>/tmp/class/</code>
	 * @param packageName Example: <code>com.drgarbage.commandlinetool</code>
	 * @param className Example: <code>ICommandLineTool</code>
	 * @param methodName Example: <code>SomeClass</code>
	 * @param methodSig Example: <code>()V</code>
	 * @return InputStream
	 * @throws IOException
	 */
	public InputStream getInputStream(String classPath, String packageName, String className) throws IOException;
	
	/**
	 * Prints the class with the specified parameters in {@link IByteCodeConfiguration}
	 * @param configuration IByteCodeConfiguration configuration file
	 */
	public String visualizeClassFile(InputStream inputStream, IByteCodeConfiguration configuration);
	
	/**
	 * Prints the class with the specified parameters in {@link IGraphConfiguration}
	 * @param inputStream Object from {@link getInputStream()}
	 * @param methodName Example: <code>SomeClass</code>
	 * @param methodSig Example: <code>()V</code>
	 * @param configuration Object from {@link IGraphConfiguration}
	 * @return string with visualized graph 
	 */
	public String visualizeGraph(InputStream inputStream, String methodName, String methodSignatur, IGraphConfiguration configuration);	
	
	/**
	 * Returns all methods in class as string with the specified parameteres in {@link IGraphConfiguration}
	 * @param classPath Example: <code>/tmp/class/container.jar</code> or <code>/tmp/class/</code>
	 * @param packageName Example: <code>com.drgarbage.commandlinetool</code>
	 * @param className Example: <code>ICommandLineTool</code>
	 * @param configuration Object from {@link IGraphConfiguration}
	 * @return 
	 */
	public Map<MethodPair, String> visualizeGraphs(String classPath, String packageName, String className, IGraphConfiguration configuration);

	
	/**
	 * Compares two graphs and prints the difference
	 * @param graph
	 * @return
	 */
	public String compareClassFiles(InputStream is1, InputStream is2);

}
