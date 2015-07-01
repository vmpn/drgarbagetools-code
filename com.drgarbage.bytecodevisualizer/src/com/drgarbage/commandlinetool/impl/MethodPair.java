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

import com.drgarbage.commandlinetool.intf.IMethodPair;

/**
 * A helper class to combine method name and method signature in a single object.
 * @author cihanaydin
 * @version $Revision: 777 $
 * $Id: MethodPair.java 723 2015-05-12 08:40:35Z cihanaydin $
 */
public class MethodPair implements IMethodPair{
	private String methodName;
	private String methodSignature;
	
	/**
	 * Creates an object.
	 * @param name Method name
	 * @param descriptor Method signature
	 */
	public MethodPair(String name, String descriptor) {
		this.methodName = name;
		this.methodSignature = descriptor;
	}
	
	/* (non-Javadoc)
	 * @see com.drgarbage.commandlinetool.intf.IMethodPair#getMethodName()
	 */
	public String getMethodName() {
		return methodName;
	}

	/* (non-Javadoc)
	 * @see com.drgarbage.commandlinetool.intf.IMethodPair#getMethodSignature()
	 */
	public String getMethodSignature() {
		return methodSignature;
	}
}
