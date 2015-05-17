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

/**
 * A helper class for parsing command line arguments
 * 
 * @author Cihan Aydin
 * @version $Revision: 723 $
 * $Id: PartsOfClassFile.java 723 2015-05-12 08:40:35Z cihanaydin $
 */
public class ByteCodeConfiguration {
	private boolean showConstantPool = false;
	private boolean showLineNumberTable = false;
	private boolean showLocalVariableTable = false;
	private boolean showExceptionTable = false;
	private boolean showRelativeBranchTargetOffsets = false;
	private boolean showSourceLineNumbers = false;
	private boolean showMaxs = false;
	private String className;
	
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
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	
	

}
