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

	public static String executeAll(OperandStack opStack, IMethodSection method){
		StringBuffer buf = new StringBuffer();
		buf.append(sizeBasedAnalysis(opStack, method));
		buf.append(typeBasedAnalysis(opStack, method));
		buf.append(contentBasedAnalysis(opStack, method));
		
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
			buf.append('\t');
			buf.append(n.getByteCodeString());
			buf.append('\t'); //TODO: format the output columns
			buf.append('\t');
			
			
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
					buf.append(JavaLexicalConstants.NEWLINE);
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
					buf.append(JavaLexicalConstants.NEWLINE);
					buf.append(CoreMessages.Error);
					buf.append(JavaLexicalConstants.COLON);
					buf.append(JavaLexicalConstants.SPACE);
					buf.append(BytecodeVisualizerMessages.OperandStackAnalysis_Error_StackOverflow);
					buf.append(JavaLexicalConstants.DOT);
				}
				
				if(n.getVertexType() == INodeType.NODE_TYPE_RETURN){
					if(stackSize != 0){
						buf.append(JavaLexicalConstants.NEWLINE);
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
						buf.append(JavaLexicalConstants.NEWLINE);
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
								buf.append(JavaLexicalConstants.SPACE);
								buf.append(JavaLexicalConstants.COMMA);
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
		return buf.toString();
	}
	
	/**
	 * Returns a text representing the type based analysis 
	 * of the current operand stack object.
	 * @return string 
	 */
	public static String typeBasedAnalysis(OperandStack opStack, IMethodSection method){
		StringBuffer buf = new StringBuffer("=== Type based analysis: ");
		buf.append(method.getName());
		buf.append(method.getDescriptor());
		buf.append(JavaLexicalConstants.NEWLINE);
		
		INodeListExt nodeList = opStack.getOperandStackGraph().getNodeList();
		for(int i = 0; i < nodeList.size(); i++){
			INodeExt n = nodeList.getNodeExt(i);
			buf.append(n.getByteCodeOffset());
			buf.append('\t');
			buf.append(n.getByteCodeString());
			buf.append('\t'); //TODO: format the output columns
			buf.append('\t');
			
			
			Object obj = n.getData();
			if(obj instanceof NodeStackProperty){
				NodeStackProperty nsp = (NodeStackProperty)obj;
				
				buf.append('\t');
				buf.append(OperandStack.stackListToString(nsp.getStackAfter(), OpstackRepresenation.TYPES));
				buf.append('\t'); //TODO: format the output columns
			}
			
			buf.append(JavaLexicalConstants.NEWLINE);
		}
		
		
		buf.append(JavaLexicalConstants.NEWLINE);
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
		
		INodeListExt nodeList = opStack.getOperandStackGraph().getNodeList();
		for(int i = 0; i < nodeList.size(); i++){
			INodeExt n = nodeList.getNodeExt(i);
			buf.append(n.getByteCodeOffset());
			buf.append('\t');
			buf.append(n.getByteCodeString());
			buf.append('\t'); //TODO: format the output columns
			buf.append('\t');
			
			
			Object obj = n.getData();
			if(obj instanceof NodeStackProperty){
				NodeStackProperty nsp = (NodeStackProperty)obj;
				
				buf.append('\t');
				buf.append(OperandStack.stackListToString(nsp.getStackAfter(), OpstackRepresenation.ALL));
				buf.append('\t'); //TODO: format the output columns
			}
			
			buf.append(JavaLexicalConstants.NEWLINE);
		}
		
		buf.append(JavaLexicalConstants.NEWLINE);
		return buf.toString();
	}
}
