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

import com.drgarbage.commandlinetool.intf.IByteCodeConfiguration;

/**
 * A helper class for parsing command line arguments
 * 
 * @author Cihan Aydin
 * @version $Revision: 723 $
 * $Id: ByteCodeConfiguration.java 723 2015-05-12 08:40:35Z cihanaydin $
 */
public class ByteCodeConfiguration implements IByteCodeConfiguration{
	private boolean showConstantPool = false;
	private boolean showLineNumberTable = false;
	private boolean showLocalVariableTable = false;
	private boolean showExceptionTable = false;
	private boolean showRelativeBranchTargetOffsets = false;
	private boolean showSourceLineNumbers = false;
	private boolean showMaxs = false;
	
	public ByteCodeConfiguration(String arg){
		if (arg != null) {
			for (Character c : arg.toCharArray()) {
				switch(c) {
				case 'c':
					this.setShowConstantPool(true);
					break;
				case 'e':
					this.setShowExceptionTable(true);
					break;
				case 'l':
					this.setShowLineNumberTable(true);
					break;
				case 'v':
					this.setShowLocalVariableTable(true);
					break;
				case 'm':
					this.setShowMaxs(true);
					break;
				case 'r':
					this.setShowRelativeBranchTargetOffsets(true);
					break;
				case 's':
					this.setShowSourceLineNumbers(true);
					break;
				default:
					break;
				}
			}
		}
	}
	
	public ByteCodeConfiguration() {
		this(null);
	}

	public boolean isShowConstantPool() {
		return showConstantPool;
	}
	public void setShowConstantPool(boolean showConstantPool) {
		this.showConstantPool = showConstantPool;
	}
	public boolean isShowLineNumberTable() {
		return showLineNumberTable;
	}
	public void setShowLineNumberTable(boolean showLineNumberTable) {
		this.showLineNumberTable = showLineNumberTable;
	}
	public boolean isShowLocalVariableTable() {
		return showLocalVariableTable;
	}
	public void setShowLocalVariableTable(boolean showLocalVariableTable) {
		this.showLocalVariableTable = showLocalVariableTable;
	}
	public boolean isShowExceptionTable() {
		return showExceptionTable;
	}
	public void setShowExceptionTable(boolean showExceptionTable) {
		this.showExceptionTable = showExceptionTable;
	}
	public boolean isShowRelativeBranchTargetOffsets() {
		return showRelativeBranchTargetOffsets;
	}
	public void setShowRelativeBranchTargetOffsets(
			boolean showRelativeBranchTargetOffsets) {
		this.showRelativeBranchTargetOffsets = showRelativeBranchTargetOffsets;
	}
	public boolean isShowSourceLineNumbers() {
		return showSourceLineNumbers;
	}
	public void setShowSourceLineNumbers(boolean showSourceLineNumbers) {
		this.showSourceLineNumbers = showSourceLineNumbers;
	}
	public boolean isShowMaxs() {
		return showMaxs;
	}
	public void setShowMaxs(boolean showMaxs) {
		this.showMaxs = showMaxs;
	}
	
}
