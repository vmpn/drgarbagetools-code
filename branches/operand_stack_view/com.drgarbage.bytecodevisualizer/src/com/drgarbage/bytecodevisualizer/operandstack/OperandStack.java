/**
 * Copyright (c) 2008-2012, Dr. Garbage Community
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

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Stack;

import com.drgarbage.asm.render.intf.IInstructionLine;
import com.drgarbage.bytecode.constant_pool.AbstractConstantPoolEntry;
import com.drgarbage.bytecode.instructions.AbstractInstruction;
import com.drgarbage.bytecode.instructions.Opcodes;
import com.drgarbage.controlflowgraph.ControlFlowGraphGenerator;
import com.drgarbage.controlflowgraph.intf.IDirectedGraphExt;
import com.drgarbage.controlflowgraph.intf.IEdgeExt;
import com.drgarbage.controlflowgraph.intf.IEdgeListExt;
import com.drgarbage.controlflowgraph.intf.INodeExt;
import com.drgarbage.controlflowgraph.intf.INodeListExt;

/**
 * Operand Stack View.
 * 
 * @author Andreas Karoly
 * @version $Revision: 26 $
 * $Id$
 */
public class OperandStack implements Opcodes{

//	private List<String> stack;
	private Stack<OperandStackEntry> stack2;
	private AbstractConstantPoolEntry[] classConstantPool;
	private IDirectedGraphExt graph;
	
	public IDirectedGraphExt getOperandStackGraph() {
		return graph;
	}


	/**
	 * Creates the operand stack object for the given method.
	 * @param cPool reference to the constant pool of the class
	 * @param instructions byte code instructions of the method 
	 */
	public OperandStack(AbstractConstantPoolEntry[] cPool, List<IInstructionLine> instructions){		
		stack2 = new Stack<OperandStackEntry>();
		classConstantPool = cPool;

		generateOperandStack(instructions);
	}
	
	/**
	 * Generates the operand stack.
	 * @param instructions list of instructions
	 */
	private void generateOperandStack(List<IInstructionLine> instructions){
		 graph = ControlFlowGraphGenerator.generateSynchronizedControlFlowGraphFrom(instructions, true);
		
		/* remove back edges (loops) from the graph */
		removeBackEdges(graph);
		
		/* parse the graph */
		List<INodeExt> listOfStartNodes = getAllStartNodes(graph);
		for(INodeExt n: listOfStartNodes)
			parseGraph(n);
		
	}
	
    /**
     * Removes all back edges from the edge list and 
     * incidence lists of nodes.
     * @param graph control flow graph
     */
    private void removeBackEdges(IDirectedGraphExt graph){
    	IEdgeListExt edges = graph.getEdgeList();
    	for(int i = 0; i < edges.size(); i++){
    		IEdgeExt e = edges.getEdgeExt(i);
    		INodeExt source = e.getSource(); 
    		INodeExt target = e.getTarget();
    		if(source.getByteCodeOffset() > target.getByteCodeOffset()){
    			source.getOutgoingEdgeList().remove(e);
    			target.getIncomingEdgeList().remove(e);
    			edges.remove(i);
    		}
    	}
    }

    /**
     * Returns the list of all nodes with incoming degree of 0.
     * @param graph control flow graph
     * @return list of start nodes
     */
    private List<INodeExt> getAllStartNodes(IDirectedGraphExt graph){
    	List<INodeExt> listOfStartNodes= new ArrayList<INodeExt>();
    	INodeListExt nodes = graph.getNodeList();
    	for(int i = 0; i < nodes.size(); i++){
    		INodeExt n = nodes.getNodeExt(i);
    		if(n.getIncomingEdgeList().size() == 0){
    			listOfStartNodes.add(n);
    		}
    	}
    	
    	return listOfStartNodes;
    }
    
    /**
     * Topological sort.
     * @param n start node of the graph
     */
    private void parseGraph(INodeExt n){
    	INodeExt node = n;
    	while(node != null){
    		
    		if(node.isVisited()){
    			return;
    		}
    		
    		IEdgeListExt inList = node.getIncomingEdgeList();
    		for(int j = 0; j < inList.size(); j++){
    			if(!inList.getEdgeExt(j).isVisited()){
    				return;
    			}
    		}
    		
    		/* Calculate the operand stack and assign the it to the current node */
    		Object o = node.getData();
    		if(o instanceof IInstructionLine){
    			IInstructionLine iLine = (IInstructionLine) o;
    			node.setCounter(iLine.getLine()); /* use counter attribute to save line numbers */
    			calculateOperandStack(node, iLine.getInstruction());
    		}
    		
    		node.setVisited(true);

    		
    		IEdgeListExt outList = node.getOutgoingEdgeList();
    		if(outList.size() == 0){
    			return;
    		}
    		
    		if(outList.size() == 1){
    			IEdgeExt edge = outList.getEdgeExt(0);
    			edge.setVisited(true);
    			node = edge.getTarget();
    		}
    		else{ 
    			Stack<OperandStackEntry> localStack = new Stack<OperandStackEntry>();
    			for(int i = 0; i < outList.size(); i++){
    				stack2 = localStack;
    				IEdgeExt edge = outList.getEdgeExt(i);
    				edge.setVisited(true);
    				parseGraph(edge.getTarget());
    			}
    		}

    	} 
    }
    
    /**
     * Calculates the operand stack for the current byte code instruction.
     * @param node
     * @param i
     */
    private void calculateOperandStack(INodeExt node, AbstractInstruction i){

    	processInstruction(i);

    	/* convert stack to string */
    	/* TODO: adapt code to avoid conversion */
    	StringBuffer buf = new StringBuffer();
    	for (Enumeration<OperandStackEntry> en = stack2.elements(); en.hasMoreElements();){
    		OperandStackEntry ose = en.nextElement();
    		buf.append(ose.getVarName());
    		buf.append("=");
    		buf.append(ose.getValue());
    		buf.append(", ");
    	}
    	if(buf.length() > 2){ /* cut the last ' ,' ' ' */
    		buf.deleteCharAt(buf.length()-1);
    		buf.deleteCharAt(buf.length()-1);
    	}
    	
    	node.setData(buf.toString());
    }
    
    /**
     * The operand stack entry corresponding to the byte code instruction
     * which has to be pushed onto the stack or popped from the stack.
     */
    class OperandStackEntry{

		private int length;
    	private String varName;
    	private String value;

    	public OperandStackEntry(int length, String varName, String value) {
			super();
			this.length = length;
			this.varName = varName;
			this.value = value;
		}
    	
		public int getLength() {
			return length;
		}
		public String getVarName() {
			return varName;
		}
		public String getValue() {
			return value;
		}
    }
    
    
	private void processInstruction(AbstractInstruction i){
		
		switch (i.getOpcode()) {
		/* arrayref, index -> value */
		case OPCODE_AALOAD:
		case OPCODE_BALOAD:
		case OPCODE_CALOAD:
		case OPCODE_DALOAD:
		case OPCODE_FALOAD:
		case OPCODE_IALOAD:
		case OPCODE_LALOAD:
		case OPCODE_SALOAD:
			
			stack2.pop();
			stack2.pop();
			stack2.push(new OperandStackEntry(2, "V", "?"));
			return;
			
			
		/* -> objectref */
		case OPCODE_ALOAD:
		case OPCODE_ALOAD_0:
		case OPCODE_ALOAD_1:
		case OPCODE_ALOAD_2:
		case OPCODE_ALOAD_3:
		case OPCODE_NEW:
			
			stack2.push(new OperandStackEntry(4, "R", "?"));
			return;
			
		/* -> value */
		case OPCODE_ILOAD:
		case OPCODE_ILOAD_0:
		case OPCODE_ILOAD_1:
		case OPCODE_ILOAD_2:
		case OPCODE_ILOAD_3:
			stack2.push(new OperandStackEntry(4, "I", "?"));
			return;
					
		case OPCODE_DLOAD:
		case OPCODE_DLOAD_0:
		case OPCODE_DLOAD_1:
		case OPCODE_DLOAD_2:
		case OPCODE_DLOAD_3:
			stack2.push(new OperandStackEntry(4, "D", "?"));
			return;
			
		case OPCODE_FLOAD:
		case OPCODE_FLOAD_0:
		case OPCODE_FLOAD_1:
		case OPCODE_FLOAD_2:
		case OPCODE_FLOAD_3:
			
			stack2.push(new OperandStackEntry(4, "F", "?"));
			return;
			
		/* arrayref, index, value-> [] */
		case OPCODE_AASTORE:
		case OPCODE_BASTORE:
		case OPCODE_CASTORE:
		case OPCODE_DASTORE:
		case OPCODE_FASTORE:
		case OPCODE_IASTORE:
		case OPCODE_LASTORE:
		case OPCODE_SASTORE:
			
			stack2.pop();
			stack2.pop();
			stack2.pop();
			return;
		
		/* value -> */
		case OPCODE_ASTORE:
		case OPCODE_ASTORE_0:
		case OPCODE_ASTORE_1:
		case OPCODE_ASTORE_2:
		case OPCODE_ASTORE_3:
			
		case OPCODE_DSTORE:
		case OPCODE_DSTORE_0:
		case OPCODE_DSTORE_1:
		case OPCODE_DSTORE_2:
		case OPCODE_DSTORE_3:
			
		case OPCODE_FSTORE:
		case OPCODE_FSTORE_0:
		case OPCODE_FSTORE_1:
		case OPCODE_FSTORE_2:
		case OPCODE_FSTORE_3:
			
		case OPCODE_ISTORE:
		case OPCODE_ISTORE_0:
		case OPCODE_ISTORE_1:
		case OPCODE_ISTORE_2:
		case OPCODE_ISTORE_3:

		case OPCODE_POP:
			
			stack2.pop();
			
		case OPCODE_POP2:
			
//			stack2.pop();//TODO: check length			
			return;
			

		/* 	count -> arrayref */
		case OPCODE_ANEWARRAY:
			stack2.pop();
			stack2.push(new OperandStackEntry(4, "arrayref", "?"));
			return;
		
		/* arrayref -> length */
		case OPCODE_ARRAYLENGTH:
			stack2.pop();
			stack2.push(new OperandStackEntry(4, "length", "?"));
			return;
			
		/* value -> [] */
		case OPCODE_ARETURN:
		case OPCODE_DRETURN:
		case OPCODE_FRETURN:
		case OPCODE_IRETURN:
		case OPCODE_LRETURN:
		case OPCODE_RETURN:
			// TODO special return case
			stack2.clear();
			
			//TODO: check if the stack empty
			return;
			
		/* -> null */
		case OPCODE_ACONST_NULL:
			stack2.push(new OperandStackEntry(1, "const", "null"));
			return;
		
		/* -> const */
		case OPCODE_ICONST_0:
			stack2.push(new OperandStackEntry(1, "const", "0"));
			return;
		case OPCODE_ICONST_1:
			stack2.push(new OperandStackEntry(1, "const", "1"));
			return;
		case OPCODE_ICONST_2:
			stack2.push(new OperandStackEntry(1, "const", "2"));
			return;
		case OPCODE_ICONST_3:
			stack2.push(new OperandStackEntry(1, "const", "3"));
			return;
		case OPCODE_ICONST_4:
			stack2.push(new OperandStackEntry(1, "const", "4"));
			return;
		case OPCODE_ICONST_5:
			stack2.push(new OperandStackEntry(1, "const", "5"));
			return;
		case OPCODE_ICONST_M1:
			stack2.push(new OperandStackEntry(1, "const", "-1"));
			return;
		case OPCODE_DCONST_0:
			stack2.push(new OperandStackEntry(4, "const", "0.0"));
			return;
		case OPCODE_DCONST_1:
			stack2.push(new OperandStackEntry(4, "const", "1.0"));
			return;
		case OPCODE_FCONST_0:
			stack2.push(new OperandStackEntry(4, "const", "0.0F"));
			return;
		case OPCODE_FCONST_1:
			stack2.push(new OperandStackEntry(4, "const", "1.0F"));
			return;
		case OPCODE_FCONST_2:
			stack2.push(new OperandStackEntry(4, "const", "2.0F"));
			return;
		
		
		/* objectref -> [empty], objectref */
		case OPCODE_ATHROW:
			stack2.clear();
			stack2.push(new OperandStackEntry(4, "R", "?"));
			return;
			
		/* -> value */
		case OPCODE_BIPUSH:
		case OPCODE_SIPUSH:
			stack2.push(new OperandStackEntry(1, "V", "?"));
			return;
			
		/* objectref -> objectref */
		case OPCODE_CHECKCAST:
			stack2.pop();
			stack2.push(new OperandStackEntry(4, "R", "?"));
			return;
			
		/* value -> result */
		case OPCODE_D2F:
		case OPCODE_D2I:
		case OPCODE_D2L:
		case OPCODE_F2I:
		case OPCODE_F2L:
		case OPCODE_F2D:
		case OPCODE_I2D:
		case OPCODE_I2C:
		case OPCODE_I2F:
		case OPCODE_I2L:
		case OPCODE_I2S:
		case OPCODE_L2D:
		case OPCODE_L2F:
		case OPCODE_L2I:

			stack2.pop();
			stack2.push(new OperandStackEntry(4, "V", "?"));
			return;
			
		/* value1, value2 -> result */
		case OPCODE_DADD:
		case OPCODE_IADD:
		case OPCODE_FADD:
		case OPCODE_LADD:
			
		case OPCODE_DDIV:
		case OPCODE_IDIV:
		case OPCODE_FDIV:
		case OPCODE_LDIV:
			
		case OPCODE_DMUL:
		case OPCODE_IMUL:
		case OPCODE_FMUL:
		case OPCODE_LMUL:
			
		case OPCODE_DREM:
		case OPCODE_IREM:
		case OPCODE_FREM:
		case OPCODE_LREM:
			
		case OPCODE_DSUB:
		case OPCODE_ISUB:
		case OPCODE_FSUB:
		case OPCODE_LSUB:
			
			stack2.pop();
			stack2.pop();
			stack2.push(new OperandStackEntry(4, "V", "?"));
			return;
			
		/* value1 -> result */				
		case OPCODE_DNEG:
		case OPCODE_INEG:
		case OPCODE_FNEG:
		case OPCODE_LNEG:
			stack2.pop();
			stack2.push(new OperandStackEntry(4, "V", "?"));
			return;
			
		/* value1, value2 -> result */
		case OPCODE_DCMPG:
		case OPCODE_DCMPL:
		case OPCODE_FCMPG:
		case OPCODE_FCMPL:
			stack2.pop();
			stack2.pop();
			stack2.push(new OperandStackEntry(4, "V", "?"));
			return;
			
			
		/* value -> value, value */
		case OPCODE_DUP:
		case OPCODE_DUP2:
			stack2.pop();
			stack2.push(new OperandStackEntry(4, "V", "?"));
			stack2.push(new OperandStackEntry(4, "V", "?"));
			return;
		
		case OPCODE_DUP_X1:
		case OPCODE_DUP2_X1:
			stack2.pop();
			stack2.pop();
			stack2.push(new OperandStackEntry(4, "V", "?"));
			stack2.push(new OperandStackEntry(4, "V", "?"));
			stack2.push(new OperandStackEntry(4, "V", "?"));
			return;
		
		case OPCODE_DUP_X2:
		case OPCODE_DUP2_X2:
			stack2.pop();
			stack2.pop();
			stack2.pop();
			stack2.push(new OperandStackEntry(4, "V", "?"));
			stack2.push(new OperandStackEntry(4, "V", "?"));
			stack2.push(new OperandStackEntry(4, "V", "?"));
			stack2.push(new OperandStackEntry(4, "V", "?"));
			return;
			
		/* objectref -> value */
		case OPCODE_GETFIELD:
			stack2.pop();
			stack2.push(new OperandStackEntry(4, "V", "?"));
			return;
			
		/* -> value */
		case OPCODE_GETSTATIC:
			stack2.push(new OperandStackEntry(4, "V", "?"));
			return;
			
		/* value1, value2 -> result */
		case OPCODE_IAND:
		case OPCODE_IOR:
		case OPCODE_ISHL:
		case OPCODE_ISHR:
		case OPCODE_IUSHR:
		case OPCODE_IXOR:
			
		case OPCODE_LAND:
		case OPCODE_LOR:
		case OPCODE_LXOR:
		case OPCODE_LSHL:
		case OPCODE_LSHR:
		case OPCODE_LUSHR:
			stack2.pop();
			stack2.pop();
			stack2.push(new OperandStackEntry(4, "V", "?"));
			return;
			
		/* value1, value2 -> [] */
		case OPCODE_IF_ACMPEQ:
		case OPCODE_IF_ACMPNE:
		case OPCODE_IF_ICMPEQ:
		case OPCODE_IF_ICMPNE:
		case OPCODE_IF_ICMPLT:
		case OPCODE_IF_ICMPGE:
		case OPCODE_IF_ICMPGT:
		case OPCODE_IF_ICMPLE:
			stack2.pop();
			stack2.pop();
			return;
			
		/* value -> [] */
		case OPCODE_IFEQ:
		case OPCODE_IFNE:
		case OPCODE_IFGE:
		case OPCODE_IFLT:
		case OPCODE_IFGT:
		case OPCODE_IFLE:
		case OPCODE_IFNONNULL:
		case OPCODE_IFNULL:
			stack2.pop();
			return;
			
		/* objectref -> result */
		case OPCODE_INSTANCEOF:
			stack2.pop();
			stack2.push(new OperandStackEntry(4, "V", "?"));
			return;
			
		/* [arg1, [arg2 ...]] -> [] */
		case OPCODE_INVOKEDYNAMIC:
		case OPCODE_INVOKESTATIC:
			stack2.pop();
			//TODO: get number of arguments
			return;
		
		/* objectref, [arg1, arg2, ...] -> [] */
		case OPCODE_INVOKEINTERFACE:
		case OPCODE_INVOKESPECIAL:
		case OPCODE_INVOKEVIRTUAL:
			stack2.pop();
			//TODO: get number of arguments
			return;
			
		/* -> address */
		case OPCODE_JSR:
		case OPCODE_JSR_W:
			stack2.push(new OperandStackEntry(4, "ADDR", "?"));
			return;
			
		/* key -> [] */
		case OPCODE_LOOKUPSWITCH:
			stack2.pop();
			return;
			
		/* objectref -> [] */
		case OPCODE_MONITORENTER:
		case OPCODE_MONITOREXIT:
			stack2.pop();
			return;

		/* count1, [count2,...] -> arrayref */
		case OPCODE_MULTIANEWARRAY:
			stack2.pop();
			stack2.pop();
			stack2.push(new OperandStackEntry(4, "AR", "?"));
			return;
			
			
		/* count -> arrayref */
		case OPCODE_NEWARRAY:
			stack2.pop();
			stack2.push(new OperandStackEntry(4, "AR", "?"));
			return;
			
		/* objectref,value -> [] */
		case OPCODE_PUTFIELD:
			stack2.pop();
			stack2.pop();
			return;
			
		/* value -> [] */
		case OPCODE_PUTSTATIC:
		case OPCODE_TABLESWITCH:
			stack2.pop();
			return;
		
		/* value2, value1 -> value1, value2 */
		case OPCODE_SWAP:
			OperandStackEntry tmp = stack2.get(stack2.size()-2);
			stack2.set(stack2.size()-1, tmp);
			stack2.set(stack2.size()-2, stack2.get(stack2.size()-1));
			return;		
		}
		
	
	}
	
}
