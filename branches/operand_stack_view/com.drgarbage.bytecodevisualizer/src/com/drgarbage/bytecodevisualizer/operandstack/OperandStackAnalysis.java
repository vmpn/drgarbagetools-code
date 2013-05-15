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
import java.util.Map;
import java.util.Stack;

import com.drgarbage.asm.render.intf.IInstructionLine;
import com.drgarbage.asm.render.intf.IMethodSection;
import com.drgarbage.bytecode.ByteCodeConstants;
import com.drgarbage.bytecode.instructions.AbstractInstruction;
import com.drgarbage.bytecode.instructions.Opcodes;
import com.drgarbage.bytecodevisualizer.BytecodeVisualizerMessages;
import com.drgarbage.bytecodevisualizer.operandstack.OperandStack.NodeStackProperty;
import com.drgarbage.bytecodevisualizer.operandstack.OperandStack.OperandStackEntry;
import com.drgarbage.bytecodevisualizer.operandstack.OperandStack.OperandStackPropertyConstants;
import com.drgarbage.bytecodevisualizer.operandstack.OperandStack.OpstackRepresenation;
import com.drgarbage.controlflowgraph.ControlFlowGraphUtils;
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

	/*    
	 *     |  -8-  |      -16-     |          -24-         |           -24-        |        -24-   |         
	 *     Offset  Bytecode        Stack Before            Stack After             Type/Size/Content
	 *	   -----------------------------------------------------------------------------------------
	 */
	
	public static String OFFSET_COL_LABEL = "Offset";
	public static String BYTE_CODE_COL_LABEL = "Bytecode";
	public static String STACK_SIZE_COL_LABEL = "Stack Size";
	
	public static int OFFSET_COLWIDTH = 8;
	public static int BYTECODESTRING_COLWIDTH = 16;
	public static int OPSTACK_SIZE_COLWIDTH = STACK_SIZE_COL_LABEL.length();
	
	public static int OPSTACK_BEFORE_COLWIDTH = 24;
	public static int OPSTACK_AFTER_COLWIDTH = 24;
	public static int TSC_COLWIDTH = 24;

	public static String executeAll(OperandStack opStack, IMethodSection method){
		StringBuffer buf = new StringBuffer();
		buf.append(sizeBasedAnalysis(opStack, method));
		buf.append(JavaLexicalConstants.NEWLINE);
		
		buf.append(typeBasedAnalysis(opStack, method));
		buf.append(JavaLexicalConstants.NEWLINE);
		
		buf.append(contentBasedAnalysis(opStack, method));
		buf.append(JavaLexicalConstants.NEWLINE);
		
		buf.append(loopBasedAnalysis(opStack, method));
		buf.append(JavaLexicalConstants.NEWLINE);
		
		buf.append(statistics(opStack, method));
		buf.append(JavaLexicalConstants.NEWLINE);

		return buf.toString();
	}

	/**
	 * Returns a text representing the size based analysis 
	 * of the current operand stack object.
	 * @return string 
	 */
	public static String sizeBasedAnalysis(OperandStack opStack, IMethodSection method){

		StringBuffer buf = new StringBuffer("Size based analysis: "); //TODO: define constant
		buf.append(method.getName());
		buf.append(method.getDescriptor());
		buf.append(JavaLexicalConstants.NEWLINE);

		buf.append(ByteCodeConstants.MAX_STACK);
		buf.append(JavaLexicalConstants.COLON);
		buf.append(JavaLexicalConstants.SPACE);
		buf.append(JavaLexicalConstants.SPACE);
		buf.append(method.getMaxStack());
		buf.append(JavaLexicalConstants.NEWLINE);

		buf.append(ByteCodeConstants.MAX_LOCALS);
		buf.append(JavaLexicalConstants.COLON);
		buf.append(JavaLexicalConstants.SPACE);
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
		
		String header = createHeaderSizedBasedAnalysis();
		buf.append(header);
		buf.append(JavaLexicalConstants.NEWLINE);
		String headerLine = createHeaderLine(header.length()); 
		buf.append(headerLine);
		buf.append(JavaLexicalConstants.NEWLINE);
		
		INodeListExt nodeList = opStack.getOperandStackGraph().getNodeList();
		for(int i = 0; i < nodeList.size(); i++){
			INodeExt n = nodeList.getNodeExt(i);

			buf.append(n.getByteCodeOffset());
			buf.append(formatCol(OFFSET_COLWIDTH, String.valueOf(n.getByteCodeOffset()).length()));

			buf.append(n.getByteCodeString());
			buf.append(formatCol(BYTECODESTRING_COLWIDTH, n.getByteCodeString().length()));

			Object o = n.getData();
			if(o instanceof Map){
				@SuppressWarnings("unchecked")
				Map<OperandStackPropertyConstants, Object> nodeMap = (Map<OperandStackPropertyConstants, Object>) o;
				o = nodeMap.get(OperandStackPropertyConstants.NODE_STACK);

				if(o != null){
					NodeStackProperty nsp = (NodeStackProperty)o;

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
						buf.append(spacesErr(OFFSET_COLWIDTH + BYTECODESTRING_COLWIDTH));
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
						/* add size */
						buf.append(spaces(OPSTACK_SIZE_COLWIDTH / 2));
						buf.append(stackSize);
					}

					if(stackSize > method.getMaxStack()){
						buf.append(spacesErr(OFFSET_COLWIDTH + BYTECODESTRING_COLWIDTH));
						buf.append(CoreMessages.Error);
						buf.append(JavaLexicalConstants.COLON);
						buf.append(JavaLexicalConstants.SPACE);
						buf.append(BytecodeVisualizerMessages.OperandStackAnalysis_Error_StackOverflow);
						buf.append(JavaLexicalConstants.DOT);
					}

					if(n.getVertexType() == INodeType.NODE_TYPE_RETURN){
						if(stackSize != 0){

							buf.append(spacesErr(OFFSET_COLWIDTH + BYTECODESTRING_COLWIDTH));
							buf.append(CoreMessages.Warning);
							buf.append(JavaLexicalConstants.COLON);
							buf.append(JavaLexicalConstants.SPACE);
							String msg = MessageFormat.format(
									BytecodeVisualizerMessages.OperandStackAnalysis_Warning_StackNonEmpty, 
									new Object[]{
											String.valueOf(stackSize)
									});
							buf.append(msg);

							buf.append(spacesErr(OFFSET_COLWIDTH + BYTECODESTRING_COLWIDTH));
							/* get reference to the corresponding byte code instruction */
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
			}

			buf.append(JavaLexicalConstants.NEWLINE);

		}
		
		buf.append(headerLine);

		if(buf.indexOf("Error")==-1&&buf.indexOf("Warning")==-1){
			buf.append(JavaLexicalConstants.NEWLINE);
			buf.append("Size based analysis successfully passed.");
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

		StringBuffer buf = new StringBuffer("Type based analysis: "); //TODO: define constant
		buf.append(method.getName());
		buf.append(method.getDescriptor());
		buf.append(JavaLexicalConstants.NEWLINE);
		buf.append(JavaLexicalConstants.NEWLINE);
		buf.append(printHeader());
		INodeListExt nodeList = opStack.getOperandStackGraph().getNodeList();
		for(int i = 0; i < nodeList.size(); i++){
			INodeExt n = nodeList.getNodeExt(i);

			buf.append(n.getByteCodeOffset());
			buf.append(formatCol(OFFSET_COLWIDTH, String.valueOf(n.getByteCodeOffset()).length()));
			buf.append(n.getByteCodeString());
			buf.append(formatCol(BYTECODESTRING_COLWIDTH, n.getByteCodeString().length()));

			Object o = n.getData();
			if(o instanceof Map){
				@SuppressWarnings("unchecked")
				Map<OperandStackPropertyConstants, Object> nodeMap = (Map<OperandStackPropertyConstants, Object>) o;
				o = nodeMap.get(OperandStackPropertyConstants.NODE_STACK);
				if(o != null){
					NodeStackProperty nsp = (NodeStackProperty)o;

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
						buf.append(spacesErr(OFFSET_COLWIDTH+BYTECODESTRING_COLWIDTH));
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
						List<Stack<OperandStackEntry>> listOfStacks = nsp.getStackAfter();
						List<Stack<OperandStackEntry>> listOfStacksBefore =  opStack.getStackBefore(n);
						/* 
						 * print list of types for the current 
						 * byte code instruction. 
						 */
						
						
						buf.append(OperandStack.stackListToString(listOfStacksBefore, OpstackRepresenation.TYPES));
						buf.append(formatCol(OPSTACK_BEFORE_COLWIDTH,OperandStack.stackListToString(listOfStacksBefore, OpstackRepresenation.TYPES).length()));
						
						buf.append(OperandStack.stackListToString(listOfStacks, OpstackRepresenation.TYPES));
						buf.append(formatCol(OPSTACK_BEFORE_COLWIDTH,OperandStack.stackListToString(listOfStacks, OpstackRepresenation.TYPES).length()));
						/* 
						 * verify if the list of stack types equals to 
						 * invoke byte instruction arguments. 
						 */
						//TODO: Mismatched stack types

						/* 
						 * verify the the type on stack for store
						 *  byte code instruction.
						 */
						int flag = 0;
						if(n.getVertexType() == INodeType.NODE_TYPE_SIMPLE){
							o = nodeMap.get(OperandStackPropertyConstants.NODE_INSTR_OBJECT);
							
							IInstructionLine iLine;
							if(o != null){
								iLine = (IInstructionLine) o;
								AbstractInstruction instr = iLine.getInstruction();
								String storeType = "?";
								switch(instr.getOpcode()){
								case Opcodes.OPCODE_ISTORE:
								case Opcodes.OPCODE_ISTORE_0:
								case Opcodes.OPCODE_ISTORE_1:
								case Opcodes.OPCODE_ISTORE_2:
								case Opcodes.OPCODE_ISTORE_3:
								case Opcodes.OPCODE_IASTORE: 
									storeType = OperandStack.I_INT;
									flag = 1;
									break;
								
								case Opcodes.OPCODE_DSTORE:
								case Opcodes.OPCODE_DSTORE_0:
								case Opcodes.OPCODE_DSTORE_1:
								case Opcodes.OPCODE_DSTORE_2:
								case Opcodes.OPCODE_DSTORE_3:
								case Opcodes.OPCODE_DASTORE: 
									storeType = OperandStack.D_DOUBLE;
									flag = 1;
									break;
									
								case Opcodes.OPCODE_LSTORE:
								case Opcodes.OPCODE_LSTORE_0:
								case Opcodes.OPCODE_LSTORE_1:
								case Opcodes.OPCODE_LSTORE_2:
								case Opcodes.OPCODE_LSTORE_3:
								case Opcodes.OPCODE_LASTORE: 
									storeType = OperandStack.J_LONG;
									flag = 1;
									break;
									
								case Opcodes.OPCODE_FSTORE:
								case Opcodes.OPCODE_FSTORE_0:
								case Opcodes.OPCODE_FSTORE_1:
								case Opcodes.OPCODE_FSTORE_2:
								case Opcodes.OPCODE_FSTORE_3:
								case Opcodes.OPCODE_FASTORE: 
									storeType = OperandStack.F_FLOAT;
									flag = 1;
									break;
								
								case Opcodes.OPCODE_ASTORE:
								case Opcodes.OPCODE_ASTORE_0:
								case Opcodes.OPCODE_ASTORE_1:
								case Opcodes.OPCODE_ASTORE_2:
								case Opcodes.OPCODE_ASTORE_3:
								case Opcodes.OPCODE_AASTORE: 
									storeType = OperandStack.L_REFERENCE;
									flag = 1;
									break;
								
								case Opcodes.OPCODE_BASTORE:
									storeType = OperandStack.B_BYTE;
									flag = 1;
									break;
								case Opcodes.OPCODE_CASTORE:
									storeType = OperandStack.C_CHAR;
									flag = 1;
									break;
								case Opcodes.OPCODE_SASTORE:
									storeType = OperandStack.S_SHORT;
									flag = 1;
									break;
								default:
									flag = 0;
									break;
								}
								/* get return type from stack */
								Stack<OperandStackEntry> se = listOfStacksBefore.get(0);
								if(se.size() != 0 && flag == 1){
									String opStackType = se.lastElement().getVarType();
									if(!storeType.equals(opStackType)){
										buf.append(spacesErr(OFFSET_COLWIDTH+BYTECODESTRING_COLWIDTH));
										buf.append(CoreMessages.Error);
										buf.append(JavaLexicalConstants.COLON);
										buf.append(JavaLexicalConstants.SPACE);
										buf.append("Expecting to find " + rewriteType(storeType) + " on stack");
										buf.append(", type on stack: " + rewriteType(opStackType));
										buf.append(JavaLexicalConstants.DOT);
										buf.append(JavaLexicalConstants.SPACE);
									}
								}
								
							}
							
						}

						/* 
						 * verify the the type on stack for return
						 *  byte code instruction.
						 */
						if(n.getVertexType() == INodeType.NODE_TYPE_RETURN){
							o = nodeMap.get(OperandStackPropertyConstants.NODE_INSTR_OBJECT);

							IInstructionLine iLine;
							if(o != null){
								iLine = (IInstructionLine) o;
								AbstractInstruction instr = iLine.getInstruction();
								String returnType = "?";
								switch(instr.getOpcode()){
								case Opcodes.OPCODE_ARETURN:
									returnType = OperandStack.L_REFERENCE;
									break;
								case Opcodes.OPCODE_DRETURN:
									returnType = OperandStack.D_DOUBLE;
									break;
								case Opcodes.OPCODE_FRETURN:
									returnType = OperandStack.F_FLOAT;
									break;
								case Opcodes.OPCODE_IRETURN:
									returnType = OperandStack.I_INT;
									break;
								case Opcodes.OPCODE_LRETURN:
									returnType = OperandStack.J_LONG;
									break;
//								case Opcodes.OPCODE_RETURN:
								}

								/* get return type from stack */
								Stack<OperandStackEntry> se = listOfStacksBefore.get(0);
								if(se.size() != 0){
									String opStackType = se.lastElement().getVarType();
									if(!returnType.equals(opStackType)){
										buf.append(spacesErr(OFFSET_COLWIDTH+BYTECODESTRING_COLWIDTH));
										buf.append(CoreMessages.Error);
										buf.append(JavaLexicalConstants.COLON);
										buf.append(JavaLexicalConstants.SPACE);
										buf.append("Return type mismatched. ");
										buf.append("Expected type: " + rewriteType(returnType));
										buf.append(", type on stack: " + rewriteType(opStackType));
										buf.append(JavaLexicalConstants.DOT);
										buf.append(JavaLexicalConstants.SPACE);
									}
								}
							}
						}
					}				
				}
			}

			buf.append(JavaLexicalConstants.NEWLINE);
		}

		if(buf.indexOf("Error")==-1&&buf.indexOf("Warning")==-1){
			buf.append(JavaLexicalConstants.NEWLINE);
			buf.append("Type based analysis successfully passed.");
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
		StringBuffer buf = new StringBuffer("=== Content based analysis: ");
		buf.append(method.getName());
		buf.append(method.getDescriptor());
		buf.append(JavaLexicalConstants.NEWLINE);
		buf.append(JavaLexicalConstants.NEWLINE);
		buf.append(printHeader());
		INodeListExt nodeList = opStack.getOperandStackGraph().getNodeList();
		for(int i = 0; i < nodeList.size(); i++){
			INodeExt n = nodeList.getNodeExt(i);
			buf.append(n.getByteCodeOffset());
			buf.append(formatCol(OFFSET_COLWIDTH, String.valueOf(n.getByteCodeOffset()).length()));

			buf.append(n.getByteCodeString());
			buf.append(formatCol(BYTECODESTRING_COLWIDTH, n.getByteCodeString().length()));

			Object o = n.getData();
			if(o instanceof Map){
				@SuppressWarnings("unchecked")
				Map<OperandStackPropertyConstants, Object> nodeMap = (Map<OperandStackPropertyConstants, Object>) o;
				o = nodeMap.get(OperandStackPropertyConstants.NODE_STACK);
				if(o != null){
					NodeStackProperty nsp = (NodeStackProperty) o;
					buf.append(OperandStack.stackListToString(nsp.getStackAfter(), OpstackRepresenation.ALL));
				}
			}
			buf.append(JavaLexicalConstants.NEWLINE);
		}

		buf.append(JavaLexicalConstants.NEWLINE);
		return buf.toString();
	}


	/**
	 * Returns a text representing the loop based analysis 
	 * of the current operand stack object.
	 * @return string 
	 */
	public static String loopBasedAnalysis(OperandStack opStack, IMethodSection method){
		StringBuffer buf = new StringBuffer("=== Loop based analysis: ");
		buf.append(JavaLexicalConstants.NEWLINE);
		buf.append("TODO");
		//TODO: implement

		buf.append(JavaLexicalConstants.NEWLINE);
		return buf.toString();
	}

	/**
	 * Returns statistics of the current operand stack object.
	 * @return string 
	 */
	public static String statistics(OperandStack opStack, IMethodSection method){
		StringBuffer buf = new StringBuffer("=== Statistics: ");
		buf.append(JavaLexicalConstants.NEWLINE);

		buf.append("Elapsed time of the operand stack generation: "+ opStack.getElapsedTime() +" ms");
		buf.append(JavaLexicalConstants.NEWLINE);
		buf.append("Memory consumption:                           "+ opStack.getMemoryConsumption() +" Bytes");
		buf.append(JavaLexicalConstants.NEWLINE);

		buf.append("Number of generated stacks:                   "+ opStack.getNumberOfStacks());
		buf.append(JavaLexicalConstants.NEWLINE);
		buf.append("Number of generated stack etries:             "+ opStack.getNumberOfStackEntries());
		buf.append(JavaLexicalConstants.NEWLINE);

		buf.append("Number of method instructions:                " + method.getInstructionLines().size());
		buf.append(JavaLexicalConstants.NEWLINE);
		buf.append("Number of if instructions:                    ");
		buf.append(countIfInstrunctions(method.getInstructionLines()));

		buf.append(JavaLexicalConstants.NEWLINE);
		buf.append("Number of switch instructions:                ");
		buf.append(countSwitchInstrunctions(method.getInstructionLines()));

		buf.append(JavaLexicalConstants.NEWLINE);
		return buf.toString();
	}

	private static int countIfInstrunctions(List<IInstructionLine> instructionList){
		int counter = 0;
		for (IInstructionLine i: instructionList){
			int type = ControlFlowGraphUtils.getInstructionNodeType(i.getInstruction().getOpcode());
			if(type == INodeType.NODE_TYPE_IF){
				counter++;
			}
		}
		return counter;
	}

	private static int countSwitchInstrunctions(List<IInstructionLine> instructionList){
		int counter = 0;
		for (IInstructionLine i: instructionList){
			int type = ControlFlowGraphUtils.getInstructionNodeType(i.getInstruction().getOpcode());
			if(type == INodeType.NODE_TYPE_SWITCH){
				counter++;
			}
		}
		return counter;
	}
//
//	public static String formatOffsetCol(int colWidth, int dataLength){
//		String spaces = "";
//		for(int i = 0;i<colWidth-dataLength;i++){
//			spaces += JavaLexicalConstants.SPACE;
//		}
//		return spaces;
//	}

	/**
	 * Generate a number of spaces for formatting purposes in bytecode String column
	 * @param columnWidth
	 * @param dataLength
	 * @return spaces
	 */

	public static String formatCol(int colWidth, int dataLength){
		String spaces = "";
		for(int i = 0;i<colWidth - dataLength;i++){
			spaces += JavaLexicalConstants.SPACE;
		}
		return spaces;
	}

	/**
	 * Generate a number of spaces for formatting purposes
	 * @param numOfSpace
	 * @return String
	 */
	public static String spacesErr(int numOfSpace){
		String spaces = "";
		spaces += JavaLexicalConstants.NEWLINE;
		for(int i = 0;i < numOfSpace;i++){
			spaces += JavaLexicalConstants.SPACE;
		}
		return spaces;
	}
	public static String spaces(int numOfSpace){
		String spaces = "";
		for(int i = 0;i < numOfSpace;i++){
			spaces += JavaLexicalConstants.SPACE;
		}
		return spaces;
	}
	
	/**
	 * TODO: description
	 * @return text
	 */
	public static String createHeaderSizedBasedAnalysis(){
		StringBuffer buf = new StringBuffer();
		buf.append(OFFSET_COL_LABEL);
		buf.append(spaces(OFFSET_COLWIDTH - OFFSET_COL_LABEL.length()));
		buf.append(BYTE_CODE_COL_LABEL);
		buf.append(spaces( BYTECODESTRING_COLWIDTH - BYTE_CODE_COL_LABEL.length()));
		buf.append(STACK_SIZE_COL_LABEL);

		return buf.toString();
	}

	/**
	 * TODO: description
	 * @param lenght
	 * @return text
	 */
	public static String createHeaderLine(int lenght){
		StringBuffer buf = new StringBuffer();
		for (int i = 0;i <= lenght - 1 ;i++){
			buf.append( '-');
		}
		
		return buf.toString();
	}
	
	public static String printHeader(){
		String header = "";
		String line ="";
		String offset = "Offset";
		String byteCode = "Bytecode";
		String opStackBefore = "Stack Before";
		String opStackAfter = "Stack After";
		String TSC = "Type/Size/Content";

		header = offset + 
				spaces(OFFSET_COLWIDTH - offset.length()) + 
				byteCode + 
				spaces(BYTECODESTRING_COLWIDTH - byteCode.length()) + 
				opStackBefore + 
				spaces(OPSTACK_BEFORE_COLWIDTH - opStackBefore.length()) + 
				opStackAfter + 
				spaces(OPSTACK_AFTER_COLWIDTH - opStackAfter.length()) + 
				TSC +
				JavaLexicalConstants.NEWLINE;
		
		for (int i = 0;i < header.length() - 1 ;i++){
			line += "-";
		}
		header += line;
		header += JavaLexicalConstants.NEWLINE;
		return header;
	}
	
	public static String rewriteType(String type){
		String typeName = "?";
		
		if (type.equals(OperandStack.I_INT))
			typeName = "int";
		else if (type.equals(OperandStack.D_DOUBLE))
			typeName = "double";
		else if (type.equals(OperandStack.J_LONG))
			typeName = "long";
		else if (type.equals(OperandStack.B_BYTE))
			typeName = "byte";
		else if (type.equals(OperandStack.C_CHAR))
			typeName = "char";
		else if (type.equals(OperandStack.F_FLOAT))
			typeName = "float";
		else if (type.equals(OperandStack.S_SHORT))
			typeName = "short";
		else 
			typeName = "Object";
		
		
		return typeName;
	}
	
}
