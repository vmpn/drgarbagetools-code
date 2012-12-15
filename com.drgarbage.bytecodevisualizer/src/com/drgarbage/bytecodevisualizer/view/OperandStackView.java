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
package com.drgarbage.bytecodevisualizer.view;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.debug.internal.ui.views.expression.ExpressionView;
import org.eclipse.jdt.ui.ISharedImages;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;

import com.drgarbage.bytecode.instructions.Opcodes;
import com.drgarbage.asm.render.impl.ClassFileDocument;
import com.drgarbage.asm.render.intf.IInstructionLine;
import com.drgarbage.asm.render.intf.IMethodSection;
import com.drgarbage.bytecode.instructions.AbstractInstruction;
import com.drgarbage.bytecodevisualizer.BytecodeVisualizerMessages;
import com.drgarbage.bytecodevisualizer.editors.BytecodeDocumentProvider;
import com.drgarbage.bytecodevisualizer.editors.BytecodeEditor;
import com.sun.org.apache.bcel.internal.generic.ATHROW;
import com.sun.org.apache.bcel.internal.generic.ICONST;
import com.sun.org.apache.bcel.internal.generic.ISTORE;

/**
 * Operand Stack View.
 * 
 * @author Sergej Alekseev
 * @version $Revision: 26 $
 * $Id$
 */
public class OperandStackView extends /*VariablesView */ ExpressionView implements Opcodes{

	protected TableViewer tableViewer;
	private IMethodSection methodInput;
	private OperandStack stack;
	
	/* (non-Javadoc)
	 * @see org.eclipse.debug.ui.AbstractDebugView#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createPartControl(Composite parent) {	
		SashForm fSashForm = new SashForm(parent, SWT.NONE);		
		createTableView(fSashForm);	
		
		/* use the parent class VariablesView or ExpressionView */
		super.createPartControl(fSashForm);
		//TODO: implement own class
	}
	
	/**
	 * Sets the input - the list of the bytecode instructions
	 * for the table Viewer. 
	 * @param methodSection
	 */
	public void setInput(IMethodSection m) {		
		if(m!= null && m.equals(methodInput)){
			return;
		}
		else{
			methodInput = m;
			/* when the methodInput changes, a new stack is generated */
			/* later, we can add the reference to the previous stack to the new stack */
	    	stack = new OperandStack();
		}
		
		if(m == null){
			tableViewer.setInput(null);
			return;
		}
		
    	List<IInstructionLine> instructions = m.getInstructionLines();    	
    	tableViewer.setInput(instructions);
	}
	
	/**
	 * TODO: implement, use constants
	 * Creates a table for bytecode instructions
	 * @param parent
	 */
	private void createTableView(Composite parent){
		
		tableViewer = new TableViewer(parent, SWT.BORDER);
        
        Table t = tableViewer.getTable();
        t.setHeaderVisible(true);
        t.setLinesVisible(true);
        
        TableColumn tc1 = new TableColumn(t, SWT.NONE);
        tc1.setText(BytecodeVisualizerMessages.OperandStackView_Opcode_Mnemonic);
        TableColumn tc2 = new TableColumn(t, SWT.NONE);
        tc2.setText(BytecodeVisualizerMessages.OperandStackView_Opcode);
        TableColumn tc3 = new TableColumn(t, SWT.NONE);
        tc3.setText(BytecodeVisualizerMessages.OperandStackView_Offset);
        TableColumn tc4 = new TableColumn(t, SWT.NONE);
        tc4.setText("actual stack");
        
        //tableViewer.setInput(displayCandidates);
        GridData data = new GridData(GridData.FILL_BOTH);
        data.horizontalSpan = 2;
        data.widthHint = IDialogConstants.ENTRY_FIELD_WIDTH;
        data.heightHint = IDialogConstants.ENTRY_FIELD_WIDTH;
        t.setLayoutData(data);
        
        tc1.pack();
        tc2.pack();
        tc3.pack();
        tc4.pack();
        
        tableViewer.setContentProvider(new ArrayContentProvider());
        tableViewer.setLabelProvider(new ITableLabelProvider() {

			public void addListener(ILabelProviderListener listener) {
			}
			public void dispose() {
			}
			
			public Image getColumnImage(Object element, int columnIndex) {
				if (columnIndex == 0) {
					return JavaUI.getSharedImages().getImage(ISharedImages.IMG_OBJS_CLASS);
				}
				return null;
			}

			public String getColumnText(Object element, int columnIndex) {
				
				IInstructionLine i = (IInstructionLine) element;
				
						if (columnIndex == 0) {
							return i.getInstruction().getOpcodeMnemonic();
						}
						else if (columnIndex == 1) {							
							return String.valueOf(i.getInstruction().getOpcode());
						}
						else if (columnIndex == 2) {
							return String.valueOf(i.getInstruction().getOffset());
						}
						else if (columnIndex == 3) {
							stack.executeInstruction(i.getInstruction());
							return stack.toString();
						}
						
						return BytecodeVisualizerMessages.OperandStackView_Unknown;
					
			}

			public boolean isLabelProperty(Object element, String property) {
				return false;
			}

			public void removeListener(ILabelProviderListener listener) {
			}
        	
        });
        
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.PageBookView#doCreatePage(org.eclipse.ui.IWorkbenchPart)
	 */
	protected PageRec doCreatePage(IWorkbenchPart part) {

		/* set reference in the editor */
        if(part instanceof BytecodeEditor){
        	BytecodeEditor be = (BytecodeEditor) part;
        	be.setOperandStackView(this);
        	
        	tableViewer.setInput(null);
        	        	
        	//TODO: Adapt interface IClassFileDocument, 
        	//TODO: Make the constantPool accessible
        	//BytecodeDocumentProvider bdp = (BytecodeDocumentProvider)be.getDocumentProvider();
        	//ClassFileDocument cfd = (ClassFileDocument)bdp.getClassFileDocument();
        	//cfd.constantPool;
        	
        }
        
        return null;
	}

	@Override
	protected void doDestroyPage(IWorkbenchPart arg0, PageRec arg1) {
		super.doDestroyPage(arg0, arg1);
		
	}

	@Override
	protected IWorkbenchPart getBootstrapPart() {
        IWorkbenchPage page = getSite().getPage();
        if (page != null) {
			return page.getActiveEditor();
		}

        return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.debug.ui.AbstractDebugView#isImportant(org.eclipse.ui.IWorkbenchPart)
	 */
	@Override
	protected boolean isImportant(IWorkbenchPart part) {
		boolean b = false;
		
		/* we are interested only on an editor */
		if(part instanceof IEditorPart){
			b = true;
		}
		
        return b;
	}
	
	protected class OperandStack{
		
		private ArrayList<String> stack;
		private OperandStack previousStack;
		
		/* for the future 
		 * private StackElement returnPreviousStack;
		 * private StackElement returnCurrentStack;
		 * 
		 * a stack element should contain the actual name and value of a value or reference
		 * 
		 * in the case of a return, the stack saves it in a private StackElement to be retrieved when needed by a 
		 * new method.
		 * 
		 * for now i only implement a static, not really informal, view of the stack
		 */
		
		public OperandStack(){
			stack = new ArrayList<String>();
		}

		private void executeInstruction(AbstractInstruction i){
			
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
				
				stack.remove(stack.size()-1);
				stack.remove(stack.size()-1);
				stack.add("V");
				
				break;
				
			/* -> objectref */
			case OPCODE_ALOAD:
			case OPCODE_ALOAD_0:
			case OPCODE_ALOAD_1:
			case OPCODE_ALOAD_2:
			case OPCODE_ALOAD_3:
			case OPCODE_NEW:
				
				stack.add("R");
				break;
				
			/* -> value */
			case OPCODE_DLOAD:
			case OPCODE_DLOAD_0:
			case OPCODE_DLOAD_1:
			case OPCODE_DLOAD_2:
			case OPCODE_DLOAD_3:
				
			case OPCODE_FLOAD:
			case OPCODE_FLOAD_0:
			case OPCODE_FLOAD_1:
			case OPCODE_FLOAD_2:
			case OPCODE_FLOAD_3:
				
				stack.add("V");
				break;
				
			/* arrayref, index, value-> [] */
			case OPCODE_AASTORE:
			case OPCODE_BASTORE:
			case OPCODE_CASTORE:
			case OPCODE_DASTORE:
			case OPCODE_FASTORE:
			case OPCODE_IASTORE:
			case OPCODE_LASTORE:
			case OPCODE_SASTORE:
				
				stack.remove(stack.size()-1);
				stack.remove(stack.size()-1);
				stack.remove(stack.size()-1);
				
				break;
			
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
			case OPCODE_POP2:
				
				stack.remove(stack.size()-1);
				
				break;
				
	
			/* 	count -> arrayref */
			case OPCODE_ANEWARRAY:
				stack.remove(stack.size()-1);
				stack.add("R");
				break;
			
			/* arrayref -> length */
			case OPCODE_ARRAYLENGTH:
				stack.remove(stack.size()-1);
				stack.add("V");
				break;
				
			/* value -> [] */
			case OPCODE_ARETURN:
			case OPCODE_DRETURN:
			case OPCODE_FRETURN:
			case OPCODE_IRETURN:
			case OPCODE_LRETURN:
			case OPCODE_RETURN:
				// TODO special return case
				stack.clear();
				break;
				
			/* -> null */
			case OPCODE_ACONST_NULL:
				stack.add("N");
				break;
			
			/* -> const */
			case OPCODE_ICONST_0:
				stack.add("0");
				break;
			case OPCODE_ICONST_1:
				stack.add("1");
				break;
			case OPCODE_ICONST_2:
				stack.add("2");
				break;
			case OPCODE_ICONST_3:
				stack.add("3");
				break;
			case OPCODE_ICONST_4:
				stack.add("4");
				break;
			case OPCODE_ICONST_5:
				stack.add("5");
				break;
			case OPCODE_ICONST_M1:
				stack.add("-1");
				break;
			case OPCODE_DCONST_0:
				stack.add("0.0");
				break;
			case OPCODE_DCONST_1:
				stack.add("1.0");
				break;
			case OPCODE_FCONST_0:
				stack.add("0.0f");
				break;
			case OPCODE_FCONST_1:
				stack.add("1.0f");
				break;
			case OPCODE_FCONST_2:
				stack.add("2.0f");
				break;
			
			
			/* objectref -> [empty], objectref */
			case OPCODE_ATHROW:
				stack.clear();
				stack.add("R");
				break;
			/* -> value */
			case OPCODE_BIPUSH:
			case OPCODE_SIPUSH:
				stack.add("V");
				break;
			/* objectref -> objectref */
			case OPCODE_CHECKCAST:
				stack.remove(stack.size()-1);
				stack.add("R");
				break;
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

				stack.remove(stack.size()-1);
				stack.add("V");
				break;
				
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
				stack.remove(stack.size()-1);
				stack.remove(stack.size()-1);
				stack.add("V");
				break;
				
			/* value1 -> result */				
			case OPCODE_DNEG:
			case OPCODE_INEG:
			case OPCODE_FNEG:
			case OPCODE_LNEG:
				stack.remove(stack.size()-1);
				stack.add("V");
				break;
				
			/* value1, value2 -> result */
			case OPCODE_DCMPG:
			case OPCODE_DCMPL:
			case OPCODE_FCMPG:
			case OPCODE_FCMPL:
				stack.remove(stack.size()-1);
				stack.remove(stack.size()-1);
				stack.add("V");
				break;
				
			/* value -> value, value */
			case OPCODE_DUP:
			case OPCODE_DUP2:
				stack.remove(stack.size()-1);
				stack.add("V");
				stack.add("V");
				break;
			
			case OPCODE_DUP_X1:
			case OPCODE_DUP2_X1:
				stack.remove(stack.size()-1);
				stack.remove(stack.size()-1);
				stack.add("V");
				stack.add("V");
				stack.add("V");
				break;
			
			case OPCODE_DUP_X2:
			case OPCODE_DUP2_X2:
				stack.remove(stack.size()-1);
				stack.remove(stack.size()-1);
				stack.remove(stack.size()-1);
				stack.add("V");
				stack.add("V");
				stack.add("V");
				stack.add("V");
				break;
				
			/* objectref -> value */
			case OPCODE_GETFIELD:
				stack.remove(stack.size()-1);
				stack.add("V");
				break;
				
			/* -> value */
			case OPCODE_GETSTATIC:
				stack.add("V");
				break;
				
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
				stack.remove(stack.size()-1);
				stack.remove(stack.size()-1);
				stack.add("V");
				break;
				
			/* value1, value2 -> [] */
			case OPCODE_IF_ACMPEQ:
			case OPCODE_IF_ACMPNE:
			case OPCODE_IF_ICMPEQ:
			case OPCODE_IF_ICMPNE:
			case OPCODE_IF_ICMPLT:
			case OPCODE_IF_ICMPGE:
			case OPCODE_IF_ICMPGT:
			case OPCODE_IF_ICMPLE:
				stack.remove(stack.size()-1);
				stack.remove(stack.size()-1);
				break;
				
			/* value -> [] */
			case OPCODE_IFEQ:
			case OPCODE_IFNE:
			case OPCODE_IFGE:
			case OPCODE_IFLT:
			case OPCODE_IFGT:
			case OPCODE_IFLE:
			case OPCODE_IFNONNULL:
			case OPCODE_IFNULL:
				stack.remove(stack.size()-1);
				break;
				
			/* objectref -> result */
			case OPCODE_INSTANCEOF:
				stack.remove(stack.size()-1);
				stack.add("V");
				break;
			/* [arg1, [arg2 ...]] -> [] */
			case OPCODE_INVOKEDYNAMIC:
			case OPCODE_INVOKESTATIC:
				stack.remove(stack.size()-1);
				break;
			
			/* objectref, [arg1, arg2, ...] -> [] */
			case OPCODE_INVOKEINTERFACE:
			case OPCODE_INVOKESPECIAL:
			case OPCODE_INVOKEVIRTUAL:
				stack.remove(stack.size()-1);
				stack.remove(stack.size()-1);
				break;
			/* -> address */
			case OPCODE_JSR:
			case OPCODE_JSR_W:
				stack.add("ADDR");
				break;
				
			/* key -> [] */
			case OPCODE_LOOKUPSWITCH:
				stack.remove(stack.size()-1);
				break;
				
			/* objectref -> [] */
			case OPCODE_MONITORENTER:
			case OPCODE_MONITOREXIT:
				stack.remove(stack.size()-1);
				break;

			/* count1, [count2,...] -> arrayref */
			case OPCODE_MULTIANEWARRAY:
				stack.remove(stack.size()-1);
				stack.remove(stack.size()-1);
				stack.add("AR");
				break;
			/* count -> arrayref */
			case OPCODE_NEWARRAY:
				stack.remove(stack.size()-1);
				stack.add("AR");
				break;
				
			/* objectref,value -> [] */
			case OPCODE_PUTFIELD:
				stack.remove(stack.size()-1);
				stack.remove(stack.size()-1);
				break;
				
			/* value -> [] */
			case OPCODE_PUTSTATIC:
			case OPCODE_TABLESWITCH:
				stack.remove(stack.size()-1);
				break;
			
			/* value2, value1 -> value1, value2 */
			case OPCODE_SWAP:
				String tmp = stack.get(stack.size()-2);
				stack.set(stack.size()-1, tmp);
				stack.set(stack.size()-2, stack.get(stack.size()-1));
				break;
				
			default:
				break;
			}
		}
		
		
		@Override
		public String toString(){
			return stack.toString();
		}
		
	}

	
}
