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



package com.drgarbage.bytecodevisualizer.compare;
/**
 *  Control Flow Graph Exceptions.
 *
 * @author Lars Lewald
 * @version $Revision$
 *  $Id$
 */

public class CompareException extends Exception {
	
	/** Default ID*/
	private static final long serialVersionUID = 1L;
	
	/** default constructor */
	public CompareException(){}
	
	/** default constructor with string*/
	public CompareException(String message){
		super(message);
	}

}


