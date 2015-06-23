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

/**
 * This interface defines the methods for the ArgumentParser.
 * @author cihanaydin
 * @version
 * $Id: IArgumentParser.java 723 2015-05-12 08:40:35Z cihanaydin $
 */
public interface IArgumentParser {
	
	/**
	 * Runs a parser on the object.
	 * @param args
	 */
	public void argumentParser(String[] args);
	
	/**
	 * Getter for GraphConfiguration object from IArgumentParser.
	 * @return IGraphConfiguration
	 */
	public IGraphConfiguration getGraphConfiguration();
	
	/**
	 * Getter for ByteCodeConfiguration object from IArgumentParser.
	 * @return IByteCodeConfiguration
	 */
	public IByteCodeConfiguration getByteCodeConfiguration();
}
