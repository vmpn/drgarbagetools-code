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

import com.drgarbage.commandlinetool.impl.ArgumentParser;
import com.drgarbage.commandlinetool.impl.ByteCodeConfiguration;
import com.drgarbage.commandlinetool.impl.CommandLineToolImplementation;
import com.drgarbage.commandlinetool.impl.GraphConfiguration;

/**
 * This class can create new CommandLineTool objects or configuration objects
 * @author cihanaydin
 *
 */
public class CommandLineToolFactory {
	
	/**
	 * This method creates a new CommandLineTool object.
	 * @return ICommandLineTool object to create graphs
	 */
	public static ICommandLineTool createCommandLineToolInterface(){
		return new CommandLineToolImplementation();
	}
	
	/**
	 * This method creates a new IByteCodeConfiguration object to specify detailed options.
	 * @return IByteCodeConfiguration
	 */
	public static IByteCodeConfiguration createByteCodeConfigurationInterface(){
		return new ByteCodeConfiguration();
	}
	
	/**
	 * This method creates a new IGraphConfiguration object to specify detailed options.
	 * @return IGraphConfiguration
	 */
	public static IGraphConfiguration createGraphConfigurationInterface(){
		return new GraphConfiguration();
	}
	
	/**
	 * Returns a IArgumentParser object to create an IByteCodeConfiguration or IGraphConfiguration object by parsing arguments.
	 * @param args String array with the options
	 * @return IArgumentParser
	 * @throws IOException
	 */
	public static IArgumentParser createArgumentParser(String[] args) throws IOException{
		return new ArgumentParser(args);
	}
}
