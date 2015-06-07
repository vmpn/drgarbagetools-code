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

import org.eclipse.jdt.core.IClassFile;

import com.drgarbage.commandlinetool.impl.ByteCodeConfiguration;
import com.drgarbage.commandlinetool.impl.GraphConfiguration;
import com.drgarbage.commandlinetool.impl.MethodPair;
import com.drgarbage.commandlinetool.impl.ParseArguments;

/**
 * This Interface represents the connection to the Command Line Tool - API.
 * @author cihanaydin
 */
public interface ICommandLineTool {
	
	/**
	 * method to receive the input stream with given parameters
	 * @param classPath
	 * @param packageName
	 * @param className
	 * @param methodName
	 * @param methodSig
	 * @return InputStream
	 * @throws IOException
	 */
	public InputStream getInputStream(String classPath, String packageName, String className) throws IOException;
	
	//TODO null configuration --> default übergeben
	/**
	 * Prints the class with the specified parameters in @link{@link IByteCodeConfiguration}
	 * @param configuration IByteCodeConfiguration configuration file
	 */
	public String visualizeClassFile(InputStream inputStream, IByteCodeConfiguration configuration);
	
	/**
	 * erweitern
	 * Prints the class with the specified parameters in @link{@link IExportGraphConfiguration}
	 * @param configuration IExportGraphConfiguration configuration file
	 * @see {IExportGraphConfiguration}
	 */
	/**
	 * @param inputStream
	 * @param methodName
	 * @param methodSignatur
	 * @param configuration
	 * @return
	 */
	public String visualizeGraph(InputStream inputStream, String methodName, String methodSignatur, IGraphConfiguration configuration);
	
	
	//Object: IMethod hilfsmethode mit zwei oder drei Strings --> klassenname, methodenname, methodensignatur --> nur getter
	public Map<MethodPair, String> visualizeGraphs(InputStream inputStream, IGraphConfiguration configuration);

	
	//TODO unix diff
	/**
	 * Compares two graphs and prints the difference
	 * @param graph
	 * @return
	 */
	public String compareClassFiles(InputStream is1, InputStream is2);

}
