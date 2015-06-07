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
 * The interface for ByteCodeConfiguration class
 * 
 * @author Cihan Aydin
 * @version $Revision: 739 $
 * $Id: IByteCodeConfiguration.java 739 2015-05-17 21:22:23Z cihanaydin $
 */
public interface IByteCodeConfiguration {
	//TODO siehe IExport
	/**
	 * Getter for option show constant pool.
	 * @return boolean
	 */
	public boolean isShowConstantPool();
	
	/**
	 * Getter for option show line number table.
	 * @return boolean
	 */
	public boolean isShowLineNumberTable();
	
	/**
	 * Getter for option show local variable table.
	 * @return boolean
	 */
	public boolean isShowLocalVariableTable();
	
	/**
	 * Getter for option show exception table.
	 * @return boolean
	 */
	public boolean isShowExceptionTable();
	
	/**
	 * Getter for option show relative branch target offsets.
	 * @return boolean
	 */
	public boolean isShowRelativeBranchTargetOffsets();
	
	/**
	 * Getter for option show source line numbers.
	 * @return boolean
	 */
	public boolean isShowSourceLineNumbers();
	
	/**
	 * Getter for option show maxs.
	 * @return boolean
	 */
	public boolean isShowMaxs();
	
	
	public void setShowConstantPool(boolean showConstantPool);
	
	public void setShowLineNumberTable(boolean showLineNumberTable);
	
	public void setShowLocalVariableTable(boolean showLocalVariableTable);
	
	public void setShowExceptionTable(boolean showExceptionTable);
	
	public void setShowRelativeBranchTargetOffsets(boolean showRelativeBranchTargetOffsets);
	
	public void setShowSourceLineNumbers(boolean showSourceLineNumbers);
	
	public void setShowMaxs(boolean showMaxs);
}
