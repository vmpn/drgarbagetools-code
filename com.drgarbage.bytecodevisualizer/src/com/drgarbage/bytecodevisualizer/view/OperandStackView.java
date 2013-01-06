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
import org.eclipse.jdt.internal.core.util.ConstantPoolEntry;
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

import com.drgarbage.bytecode.constant_pool.AbstractConstantPoolEntry;
import com.drgarbage.bytecode.instructions.Opcodes;
import com.drgarbage.asm.render.impl.ClassFileDocument;
import com.drgarbage.asm.render.intf.IInstructionLine;
import com.drgarbage.asm.render.intf.IMethodSection;
import com.drgarbage.bytecodevisualizer.BytecodeVisualizerMessages;
import com.drgarbage.bytecodevisualizer.editors.BytecodeDocumentProvider;
import com.drgarbage.bytecodevisualizer.editors.BytecodeEditor;
import com.drgarbage.bytecodevisualizer.operandstack.OperandStack;

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
	private AbstractConstantPoolEntry[] cPool;
	
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
	    	stack = new OperandStack(cPool, m.getInstructionLines());
	    	
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
        tc4.setText("stack instruction");
        
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
							//return stack.executeInstruction(i.getInstruction());
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
        	BytecodeDocumentProvider bdp = (BytecodeDocumentProvider)be.getDocumentProvider();
        	ClassFileDocument cfd = (ClassFileDocument)bdp.getClassFileDocument();
        	
        	cPool = cfd.getConstantPool();
        	
        	/*
        	System.out.println("creating new page");
        	
        		for(AbstractConstantPoolEntry en: cPool) {
        			if(en == null) System.out.println("null");
        			if(en != null) {
        				System.out.println(en.getInfo());
        				System.out.println();
        			}
        		}
        		
        	*/
        	
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
}
