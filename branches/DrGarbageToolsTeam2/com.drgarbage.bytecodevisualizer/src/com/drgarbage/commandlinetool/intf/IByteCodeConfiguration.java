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
	 * @return <code> true </code> or <code>false</code>
	 */
	public boolean isShowConstantPool();
	
	/**
	 * Getter for option show line number table.
	 * @return <code> true </code> or <code>false</code>
	 */
	public boolean isShowLineNumberTable();
	
	/**
	 * Getter for option show local variable table.
	 * @return <code> true </code> or <code>false</code>
	 */
	public boolean isShowLocalVariableTable();
	
	/**
	 * Getter for option show exception table.
	 * @return <code> true </code> or <code>false</code>
	 */
	public boolean isShowExceptionTable();
	
	/**
	 * Getter for option show relative branch target offsets.
	 * @return <code> true </code> or <code>false</code>
	 */
	public boolean isShowRelativeBranchTargetOffsets();
	
	/**
	 * Getter for option show source line numbers.
	 * @return <code> true </code> or <code>false</code>
	 */
	public boolean isShowSourceLineNumbers();
	
	/**
	 * Getter for option show maxs.
	 * @return <code> true </code> or <code>false</code>
	 */
	public boolean isShowMaxs();
	
	/**
	 * Setter for option show constant pool.
	 * @param showConstantPool <code> true </code> or <code>false</code>
	 */
	public void setShowConstantPool(boolean showConstantPool);
	
	/**
	 * Setter for option show line number table.
	 * @param showLineNumberTable <code> true </code> or <code>false</code>
	 */
	public void setShowLineNumberTable(boolean showLineNumberTable);
	
	/**
	 * Setter for option show local variable table.
	 * @param showLocalVariableTable <code> true </code> or <code>false</code>
	 */
	public void setShowLocalVariableTable(boolean showLocalVariableTable);
	
	/**
	 * Setter for option show exception table.
	 * @param showExceptionTable <code> true </code> or <code>false</code>
	 */
	public void setShowExceptionTable(boolean showExceptionTable);
	
	/**
	 * Setter for option show relative branch target offsets.
	 * @param showRelativeBranchTargetOffsets <code> true </code> or <code>false</code>
	 */
	public void setShowRelativeBranchTargetOffsets(boolean showRelativeBranchTargetOffsets);
	
	/**
	 * Setter for option show source line numbers.
	 * @param showSourceLineNumbers <code> true </code> or <code>false</code>
	 */
	public void setShowSourceLineNumbers(boolean showSourceLineNumbers);
	
	/**
	 * Setter for option show maxs.
	 * @param showMaxs <code> true </code> or <code>false</code>
	 */
	public void setShowMaxs(boolean showMaxs);
}
