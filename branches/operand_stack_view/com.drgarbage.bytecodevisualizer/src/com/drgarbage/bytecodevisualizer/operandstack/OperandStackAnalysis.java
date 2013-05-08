/**
 * Copyright (c) 2008-2013, Dr. Garbage Community
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

package com.drgarbage.bytecodevisualizer.operandstack;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import com.drgarbage.asm.render.intf.IMethodSection;
import com.drgarbage.bytecode.ByteCodeConstants;
import com.drgarbage.bytecode.instructions.AbstractInstruction;
import com.drgarbage.bytecodevisualizer.BytecodeVisualizerMessages;
import com.drgarbage.bytecodevisualizer.operandstack.OperandStack.NodeStackProperty;
import com.drgarbage.bytecodevisualizer.operandstack.OperandStack.OperandStackEntry;
import com.drgarbage.bytecodevisualizer.operandstack.OperandStack.OpstackRepresenation;
import com.drgarbage.controlflowgraph.intf.INodeExt;
import com.drgarbage.controlflowgraph.intf.INodeListExt;
import com.drgarbage.controlflowgraph.intf.INodeType;
import com.drgarbage.core.CoreMessages;
import com.drgarbage.javasrc.JavaLexicalConstants;


/**
 * A collection of operand stack analysis methods.
 * 
 * @author Sergej Alekseev
 * @version $Revision$
 * $Id$
 */
public class OperandStackAnalysis {

	public static int offsetColumnWidth = 6;
	public static int byteCodeStringColumnWidth = 20;
	public static int numOfIf=0;
	public static int numOfSwitch=0;
	


	public static String executeAll(OperandStack opStack, IMethodSection method){
		StringBuffer buf = new StringBuffer();
		buf.append(sizeBasedAnalysis(opStack, method));
		buf.append(typeBasedAnalysis(opStack, method));
		buf.append(contentBasedAnalysis(opStack, method));
		buf.append(JavaLexicalConstants.NEWLINE);
		buf.append("Statistics:");
		buf.append(JavaLexicalConstants.NEWLINE);
		buf.append("Time to generate the operand Stack: "+ OperandStack.end/1000000+" ms");
		buf.append(JavaLexicalConstants.NEWLINE);
		buf.append("Memory consumption: "+ OperandStack.memoryConsumption/(1024L*1024L)+" Mbytes");
		buf.append(JavaLexicalConstants.NEWLINE);
		buf.append("Number of IF: ");
		buf.append(numOfIf);
		buf.append(JavaLexicalConstants.NEWLINE);
		buf.append("Number of switch: ");
		buf.append(numOfSwitch);
		
		
		return buf.toString();
	}

	/**
	 * Returns a text representing the size based analysis 
	 * of the current operand stack object.
	 * @return string 
	 */
	public static String sizeBasedAnalysis(OperandStack opStack, IMethodSection method){
		
		StringBuffer buf = new StringBuffer("=== Size based analysis: ");
		buf.append(method.getName());
		buf.append(method.getDescriptor());
		buf.append(JavaLexicalConstants.NEWLINE);

		buf.append(JavaLexicalConstants.NEWLINE);
		
		buf.append(ByteCodeConstants.MAX_STACK);
		buf.append(JavaLexicalConstants.COLON);
		buf.append(method.getMaxStack());
		buf.append(JavaLexicalConstants.NEWLINE);

		buf.append(ByteCodeConstants.MAX_LOCALS);
		buf.append(JavaLexicalConstants.COLON);
		buf.append(method.getMaxLocals());
		buf.append(JavaLexicalConstants.NEWLINE);

		/* max stack overflow or underflow */
		if(opStack.getMaxStackSize() > method.getMaxStack()){
			buf.append(CoreMessages.Error);
			buf.append(JavaLexicalConstants.COLON);
			buf.append(JavaLexicalConstants.SPACE);
			buf.append(BytecodeVisualizerMessages.OperandStackAnalysis_Error_StackOverflow);
			buf.append(JavaLexicalConstants.COMMA);
			buf.append(JavaLexicalConstants.SPACE);
			String msg = MessageFormat.format(
					BytecodeVisualizerMessages.OperandStackAnalysis_MaxStackSize_Info, 
					new Object[]{
							String.valueOf(method.getMaxStack()),
							String.valueOf(opStack.getMaxStackSize())
					});
			buf.append(msg);
			buf.append(JavaLexicalConstants.NEWLINE);
		}
		else if(opStack.getMaxStackSize() < method.getMaxStack()){
			buf.append(CoreMessages.Warning);
			buf.append(JavaLexicalConstants.COLON);
			buf.append(JavaLexicalConstants.SPACE);
			buf.append(BytecodeVisualizerMessages.OperandStackAnalysis_Warning_StackUnderflow);
			buf.append(JavaLexicalConstants.COMMA);
			buf.append(JavaLexicalConstants.SPACE);
			String msg = MessageFormat.format(
					BytecodeVisualizerMessages.OperandStackAnalysis_CurrentStackSize_Info, 
					new Object[]{
							String.valueOf(opStack.getMaxStackSize())
					});
			buf.append(msg);

			buf.append(JavaLexicalConstants.DOT);
			buf.append(JavaLexicalConstants.NEWLINE);
		}
		buf.append(JavaLexicalConstants.NEWLINE);

		INodeListExt nodeList = opStack.getOperandStackGraph().getNodeList();
		for(int i = 0; i < nodeList.size(); i++){
			INodeExt n = nodeList.getNodeExt(i);

			buf.append(n.getByteCodeOffset());
			buf.append(formatOffsetCol(offsetColumnWidth, String.valueOf(n.getByteCodeOffset()).length()));

			buf.append(n.getByteCodeString());
			buf.append(formatStringColumn(byteCodeStringColumnWidth, n.getByteCodeString().length()));
			
			countIf(n);
			countSwitch(n);
			

			Object obj = n.getData();
			if(obj instanceof NodeStackProperty){
				NodeStackProperty nsp = (NodeStackProperty)obj;

				int depth[] = nsp.getStackSize();
				int stackSize = OperandStack.UNKNOWN_SIZE;

				if(depth.length == 1){
					stackSize= depth[0];
				}

				List<Integer> listOfStacksSizes = new ArrayList<Integer>();
				if(depth.length > 1){
					for(int s: depth){
						if(stackSize != s){
							if(s > stackSize){
								stackSize = s;
							}
							listOfStacksSizes.add(s);
						}
					}
				}

				if(listOfStacksSizes.size() > 1){
					buf.append(spacesGenerator(offsetColumnWidth+byteCodeStringColumnWidth));
					buf.append(CoreMessages.Error);
					buf.append(JavaLexicalConstants.COLON);
					buf.append(JavaLexicalConstants.SPACE);
					buf.append(BytecodeVisualizerMessages.OperandStackAnalysis_Error_Different_StackSizes);
					buf.append(JavaLexicalConstants.SPACE);

					Iterator<Integer> it = listOfStacksSizes.iterator();
					buf.append(it.next());
					while(it.hasNext()){
						buf.append(JavaLexicalConstants.PIPE);
						buf.append(it.next());	
					}

					buf.append(JavaLexicalConstants.SPACE);
					buf.append(JavaLexicalConstants.DOT);
				}
				else{
					buf.append(stackSize);
				}

				if(stackSize > method.getMaxStack()){
					buf.append(spacesGenerator(offsetColumnWidth+byteCodeStringColumnWidth));
					buf.append(CoreMessages.Error);
					buf.append(JavaLexicalConstants.COLON);
					buf.append(JavaLexicalConstants.SPACE);
					buf.append(BytecodeVisualizerMessages.OperandStackAnalysis_Error_StackOverflow);
					buf.append(JavaLexicalConstants.DOT);
				}

				if(n.getVertexType() == INodeType.NODE_TYPE_RETURN){
					if(stackSize != 0){
						buf.append(spacesGenerator(offsetColumnWidth+byteCodeStringColumnWidth));
						buf.append(CoreMessages.Warning);
						buf.append(JavaLexicalConstants.COLON);
						buf.append(JavaLexicalConstants.SPACE);
						String msg = MessageFormat.format(
								BytecodeVisualizerMessages.OperandStackAnalysis_Warning_StackNonEmpty, 
								new Object[]{
										String.valueOf(stackSize)
								});
						buf.append(msg);

						/* get reference to the corresponding byte code instruction */
						buf.append(spacesGenerator(offsetColumnWidth+byteCodeStringColumnWidth));
						buf.append(BytecodeVisualizerMessages.OperandStackAnalysis_Possible_unused_bytecodes);
						buf.append(JavaLexicalConstants.COLON);
						buf.append(JavaLexicalConstants.SPACE);
						Iterator<Stack<OperandStackEntry>> it = nsp.getStackAfter().iterator();
						while(it.hasNext()){
							Iterator<OperandStackEntry> opS =  it.next().iterator();
							if(opS.hasNext()){
								OperandStackEntry ose = opS.next();
								AbstractInstruction bi = ose.getBytecodeInstruction();
								if(bi != null){
									buf.append(bi.getOffset());
								}
								else{
									buf.append(ose.getValue());
								}
							}
							while(opS.hasNext()){
								buf.append(JavaLexicalConstants.COMMA);
								buf.append(JavaLexicalConstants.SPACE);
								OperandStackEntry ose = opS.next();
								AbstractInstruction bi = ose.getBytecodeInstruction();
								if(bi != null){
									buf.append(bi.getOffset());
								}
								else{
									buf.append(ose.getValue());
								}
							}

						}
					}
				}

			}	

			buf.append(JavaLexicalConstants.NEWLINE);

		}

		buf.append(JavaLexicalConstants.NEWLINE);
		if(buf.indexOf("Error")==-1&&buf.indexOf("Warning")==-1){
			buf.append(JavaLexicalConstants.NEWLINE);
			buf.append("Size based analysis SUCCESSFULLY PASSED.");
		}else{
			buf.append(JavaLexicalConstants.NEWLINE);
			buf.append("Size based analysis completed with Errors/Warnings.");
		}

		buf.append(JavaLexicalConstants.NEWLINE);
		buf.append(JavaLexicalConstants.NEWLINE);
		return buf.toString();

	}

	/**
	 * Returns a text representing the type based analysis 
	 * of the current operand stack object.
	 * @return string 
	 */
	public static String typeBasedAnalysis(OperandStack opStack, IMethodSection method){
		
//		opStack.getOperandStackGraph().getNodeList().getNodeExt(1)
		
		StringBuffer buf = new StringBuffer("=== Type based analysis: ");
		buf.append(method.getName());
		buf.append(method.getDescriptor());
		buf.append(JavaLexicalConstants.NEWLINE);
		buf.append(JavaLexicalConstants.NEWLINE);

		INodeListExt nodeList = opStack.getOperandStackGraph().getNodeList();
		for(int i = 0; i < nodeList.size(); i++){
			INodeExt n = nodeList.getNodeExt(i);
			
			buf.append(n.getByteCodeOffset());
			buf.append(formatOffsetCol(offsetColumnWidth, String.valueOf(n.getByteCodeOffset()).length()));
			buf.append(n.getByteCodeString());
			buf.append(formatStringColumn(byteCodeStringColumnWidth, n.getByteCodeString().length()));

			Object obj = n.getData();
			if(obj instanceof NodeStackProperty){
				NodeStackProperty nsp = (NodeStackProperty)obj;

				/* 
				 * verify if all stack entries contains 
				 * the same list of types. 
				 */
				List<String> listOfTypes = new ArrayList<String>();
				if(nsp.getStackAfter().size() > 1){
					Iterator<Stack<OperandStackEntry>> it = nsp.getStackAfter().iterator();
					String tmpTypeList = "";
					while(it.hasNext()){
						String typeList = getStackTypes(it.next());
						if(!tmpTypeList.equals(typeList)){
							if(typeList.equals("")){
								tmpTypeList = "<empty>";
							}
							else{
								tmpTypeList = typeList;
							}
							listOfTypes.add(tmpTypeList);
						}
					}
				}

				if(listOfTypes.size() > 1){
					buf.append(spacesGenerator(offsetColumnWidth+byteCodeStringColumnWidth));
					buf.append(CoreMessages.Error);
					buf.append(JavaLexicalConstants.COLON);
					buf.append(JavaLexicalConstants.SPACE);
					buf.append(BytecodeVisualizerMessages.OperandStackAnalysis_Error_Different_StackTypes);
					buf.append(JavaLexicalConstants.SPACE);

					Iterator<String> it = listOfTypes.iterator();
					buf.append(it.next());
					while(it.hasNext()){
						buf.append(JavaLexicalConstants.SPACE);
						buf.append(JavaLexicalConstants.PIPE);
						buf.append(JavaLexicalConstants.SPACE);
						buf.append(it.next());	
					}

					buf.append(JavaLexicalConstants.SPACE);
					buf.append(JavaLexicalConstants.DOT);
				}
				else{
					/* 
					 * print list of types for the current 
					 * byte code instruction. 
					 */

					buf.append(OperandStack.stackListToString(nsp.getStackAfter(), OpstackRepresenation.TYPES));
				}

				/* 
				 * verify if the list of stack types equals to 
				 * invoke byte instruction arguments. 
				 */
				//TODO: Mismatched stack types

				buf.append(spacesGenerator(byteCodeStringColumnWidth+offsetColumnWidth));
				buf.append(verifyTypes(n,opStack));
				

				/* 
				 * verify the the type on stack for store
				 *  byte code instruction.
				 */
				//TODO: Expecting to find integer on stack

			}

			buf.append(JavaLexicalConstants.NEWLINE);
		}

		if(buf.indexOf("Error")==-1&&buf.indexOf("Warning")==-1){
			buf.append(JavaLexicalConstants.NEWLINE);
			buf.append("Type based analysis SUCCESSFULLY PASSED.");
		}else{
			buf.append(JavaLexicalConstants.NEWLINE);
			buf.append("Type based analysis completed with Errors/Warning.");
		}
		buf.append(JavaLexicalConstants.NEWLINE);
		buf.append(JavaLexicalConstants.NEWLINE);
		return buf.toString();
	}

	/**
	 * Return the list of types for the given stack in 
	 * Java class file format.
	 * @param stack
	 * @return
	 */
	public static String getStackTypes(Stack<OperandStackEntry> stack){
		StringBuffer buf = new StringBuffer();
		for (Enumeration<OperandStackEntry> en = stack.elements(); en.hasMoreElements();){
			OperandStackEntry ose = en.nextElement();
			buf.append(ose.getVarType());
		}
		return buf.toString();
	}

	/**
	 * Returns a text representing the content based analysis 
	 * of the current operand stack object.
	 * @return string 
	 */
	public static String contentBasedAnalysis(OperandStack opStack, IMethodSection method){
		
//		opStack.getOperandStackGraph().getNodeList().getNodeExt(0).ge
		
		StringBuffer buf = new StringBuffer("=== Content based analysis: ");
		buf.append(method.getName());
		buf.append(method.getDescriptor());
		buf.append(JavaLexicalConstants.NEWLINE);
		buf.append(JavaLexicalConstants.NEWLINE);

		INodeListExt nodeList = opStack.getOperandStackGraph().getNodeList();
		for(int i = 0; i < nodeList.size(); i++){
			INodeExt n = nodeList.getNodeExt(i);
			buf.append(n.getByteCodeOffset());
			buf.append(formatOffsetCol(offsetColumnWidth, String.valueOf(n.getByteCodeOffset()).length()));

			buf.append(n.getByteCodeString());
			buf.append(formatStringColumn(byteCodeStringColumnWidth, n.getByteCodeString().length()));

			Object obj = n.getData();
			if(obj instanceof NodeStackProperty){
				NodeStackProperty nsp = (NodeStackProperty)obj;
				buf.append(OperandStack.stackListToString(nsp.getStackAfter(), OpstackRepresenation.ALL));
			}

			buf.append(JavaLexicalConstants.NEWLINE);
		}

		buf.append(JavaLexicalConstants.NEWLINE);
		return buf.toString();
	}
	



	public static String formatOffsetCol(int colWidth, int dataLength){
		String spaces = "";
		if(dataLength < 4){
			colWidth = 6;
		}
		else if(dataLength >=4 && dataLength<7){
			colWidth = 8;
		}
		for(int i = 0;i<colWidth-dataLength;i++){
			spaces+= String.valueOf(JavaLexicalConstants.SPACE);
		}
		return spaces;
	}

	/**
	 * Generate a number of spaces for formatting purposes in bytecode String column
	 * @param columnWidth
	 * @param dataLength
	 * @return spaces
	 */

	public static String formatStringColumn(int columnWidth, int dataLength){
		String spaces = "";
		for(int i = 0;i<columnWidth - dataLength;i++){
			spaces+= String.valueOf(JavaLexicalConstants.SPACE);
		}
		return spaces;
	}

	/**
	 * Generate a number of spaces for formatting purposes
	 * @param numOfSpace
	 * @return String
	 */
	public static String spacesGenerator(int numOfSpace){
		String spaces="\n";
		for(int i = 0;i < numOfSpace;i++){
			spaces+=String.valueOf(JavaLexicalConstants.SPACE);
		}
		return spaces;
	}

	public static int countIf(INodeExt n){
		String patternIF = "^if(?!(eq|ne|lt|ge|le|gt))\\w*";
			if(n.getByteCodeString().matches(patternIF)) numOfIf++;
		return numOfIf;
	}
	public static int countSwitch(INodeExt n){
		
			if(n.getByteCodeString().contains("switch")) ++numOfSwitch;
		return numOfSwitch;
	}
	
	public static String verifyTypes(INodeExt n,OperandStack opStack){
		//		Match all byteCodeString begin with [BCDFIJSZL] and follow by any word
		String pattern [] = new String [8];
		pattern [0] = "^f\\w*";    								/* F */
		pattern [1] = "^i(?!f)(?!ns)(?!nv)(?!mp)\\w*";			/* I */
		pattern [2] = "^d(?!up)\\w*";							/* D */
		pattern [3] = "^l(?!dc)(?!oo)\\w*";						/* J */
		pattern [4] = "^b(?!re)\\w*";							/* B */
		pattern [5] = "^s(?!(i|w))\\w*";						/* S */	
		pattern [6] ="^c(?!h)\\w*";								/* C */
		pattern [7] = "^a\\w*";									/* L */

		String types [] = new String [8];
		types [0] = String.valueOf(ByteCodeConstants.F_FLOAT);
		types [1] = String.valueOf(ByteCodeConstants.I_INT);
		types [2] = String.valueOf(ByteCodeConstants.D_DOUBLE);
		types [3] = String.valueOf(ByteCodeConstants.J_LONG);
		types [4] = String.valueOf(ByteCodeConstants.B_BYTE);
		types [5] = String.valueOf(ByteCodeConstants.S_SHORT);
		types [6] = String.valueOf(ByteCodeConstants.C_CHAR);
		types [7] = String.valueOf(ByteCodeConstants.L_REFERENCE);

		String result="";

		if(n.getByteCodeOffset()!=0){
			for(int i = 0;i<pattern.length;i++){
				if(n.getByteCodeString().matches(pattern[i])){
					if(!OperandStack.stackToString(opStack.getStackBefore(n).get(0),OpstackRepresenation.TYPES).equalsIgnoreCase(types[i])){
						if(n.getByteCodeString().contains("store")){
							result=CoreMessages.Error+": Expecting to find "+types[i]+" on stack";
						}else{
							result=CoreMessages.Error+": Type mismatch";
						}
					}else{

					}
				}
			}
		}
		return result;
	}
}
