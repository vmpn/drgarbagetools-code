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

	/* (non-Javadoc)
	 * @see com.drgarbage.commandlinetool.intf.IByteCodeConfiguration#isShowConstantPool()
	 */
	public boolean isShowConstantPool() {
		return showConstantPool;
	}
	/* (non-Javadoc)
	 * @see com.drgarbage.commandlinetool.intf.IByteCodeConfiguration#setShowConstantPool(boolean)
	 */
	public void setShowConstantPool(boolean showConstantPool) {
		this.showConstantPool = showConstantPool;
	}
	/* (non-Javadoc)
	 * @see com.drgarbage.commandlinetool.intf.IByteCodeConfiguration#isShowLineNumberTable()
	 */
	public boolean isShowLineNumberTable() {
		return showLineNumberTable;
	}
	/* (non-Javadoc)
	 * @see com.drgarbage.commandlinetool.intf.IByteCodeConfiguration#setShowLineNumberTable(boolean)
	 */
	public void setShowLineNumberTable(boolean showLineNumberTable) {
		this.showLineNumberTable = showLineNumberTable;
	}
	/* (non-Javadoc)
	 * @see com.drgarbage.commandlinetool.intf.IByteCodeConfiguration#isShowLocalVariableTable()
	 */
	public boolean isShowLocalVariableTable() {
		return showLocalVariableTable;
	}
	/* (non-Javadoc)
	 * @see com.drgarbage.commandlinetool.intf.IByteCodeConfiguration#setShowLocalVariableTable(boolean)
	 */
	public void setShowLocalVariableTable(boolean showLocalVariableTable) {
		this.showLocalVariableTable = showLocalVariableTable;
	}
	/* (non-Javadoc)
	 * @see com.drgarbage.commandlinetool.intf.IByteCodeConfiguration#isShowExceptionTable()
	 */
	public boolean isShowExceptionTable() {
		return showExceptionTable;
	}
	/* (non-Javadoc)
	 * @see com.drgarbage.commandlinetool.intf.IByteCodeConfiguration#setShowExceptionTable(boolean)
	 */
	public void setShowExceptionTable(boolean showExceptionTable) {
		this.showExceptionTable = showExceptionTable;
	}
	/* (non-Javadoc)
	 * @see com.drgarbage.commandlinetool.intf.IByteCodeConfiguration#isShowRelativeBranchTargetOffsets()
	 */
	public boolean isShowRelativeBranchTargetOffsets() {
		return showRelativeBranchTargetOffsets;
	}
	/* (non-Javadoc)
	 * @see com.drgarbage.commandlinetool.intf.IByteCodeConfiguration#setShowRelativeBranchTargetOffsets(boolean)
	 */
	public void setShowRelativeBranchTargetOffsets(
			boolean showRelativeBranchTargetOffsets) {
		this.showRelativeBranchTargetOffsets = showRelativeBranchTargetOffsets;
	}
	/* (non-Javadoc)
	 * @see com.drgarbage.commandlinetool.intf.IByteCodeConfiguration#isShowSourceLineNumbers()
	 */
	public boolean isShowSourceLineNumbers() {
		return showSourceLineNumbers;
	}
	/* (non-Javadoc)
	 * @see com.drgarbage.commandlinetool.intf.IByteCodeConfiguration#setShowSourceLineNumbers(boolean)
	 */
	public void setShowSourceLineNumbers(boolean showSourceLineNumbers) {
		this.showSourceLineNumbers = showSourceLineNumbers;
	}
	/* (non-Javadoc)
	 * @see com.drgarbage.commandlinetool.intf.IByteCodeConfiguration#isShowMaxs()
	 */
	public boolean isShowMaxs() {
		return showMaxs;
	}
	/* (non-Javadoc)
	 * @see com.drgarbage.commandlinetool.intf.IByteCodeConfiguration#setShowMaxs(boolean)
	 */
	public void setShowMaxs(boolean showMaxs) {
		this.showMaxs = showMaxs;
	}
	
}
