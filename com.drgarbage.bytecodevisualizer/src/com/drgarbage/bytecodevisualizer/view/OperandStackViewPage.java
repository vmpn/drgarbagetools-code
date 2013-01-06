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

package com.drgarbage.bytecodevisualizer.view;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.ui.ISharedImages;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.part.IPage;
import org.eclipse.ui.part.IPageSite;
import org.eclipse.ui.part.Page;

import com.drgarbage.asm.render.intf.IInstructionLine;
import com.drgarbage.bytecode.instructions.Opcodes;
import com.drgarbage.bytecodevisualizer.BytecodeVisualizerMessages;

/**
 * The implementation of the OPerand Stackh View Page.
 * 
 * @author Sergej Alekseev
 * @version $Revision$
 * $Id$
 */
public class OperandStackViewPage extends Page implements IPage, Opcodes{
	
	protected TreeViewer treeViewer;
	
	/**
	 * Constructs an outline.
	 * @param editor
	 */
	public OperandStackViewPage() {
		super();
	}

	/**
	 * Returns the TreeTable view object.
	 * @return treeView
	 */
	public TreeViewer getTreeView() {
		return treeViewer;
		
	}
	
	/* (non-Javadoc)
	 * Method declared on Page
	 */
	public void init(IPageSite pageSite) {
		super.init(pageSite);
	}

	/*
	 * @see IPage#createControl
	 */
	public void createControl(Composite parent) {		
		createTreeViewer2(parent);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.Page#dispose()
	 */
	public void dispose() {
		super.dispose();
		treeViewer = null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.Page#getControl()
	 */
	public Control getControl() {
		return treeViewer.getControl();	
	}

	@Override
	public void setFocus() {
		/* nothing to do */		
	}

	public void addSelectionChangedListener(ISelectionChangedListener listener) {
		/* nothing to do */
	}

	public ISelection getSelection() {
		/* nothing to do */
		return null;
	}

	public void removeSelectionChangedListener(	ISelectionChangedListener listener) {
		/* nothing to do */
		
	}

	public void setSelection(ISelection selection) {
		/* nothing to do */
	}
	
	
	void createTreeViewer2(Composite parent){
		treeViewer = new TreeViewer(parent, SWT.BORDER);
		
		Tree tree = treeViewer.getTree();
		TreeColumn column = new TreeColumn(tree, SWT.LEFT, 0);
		column.setMoveable(true);
		column.setText("C1");
		column.setWidth(100);
		
		
		column = new TreeColumn(tree, SWT.LEFT, 1);
		column.setMoveable(true);
		column.setText("C2");
		column.setWidth(200);
		
		
		TreeColumn column3 = new TreeColumn(tree, SWT.RIGHT);
	      column3.setAlignment(SWT.LEFT);
	      column3.setText("m/w");
	      column3.setWidth(35);
		
		tree.setHeaderVisible(true);
    	tree.setLinesVisible(true);
    	
    	
    	treeViewer.setContentProvider(new TreeViewContentProvider());
    	treeViewer.setLabelProvider(new TreeTableLabelProvider());
        
    	
//    	List<City> cities = new ArrayList<City>();
//        cities.add(new City());
//        treeViewer.setInput(cities);
        
        treeViewer.expandAll();
     }

	//TODO: The class is not needed id the input will be adapted to OperandStack
	 public  class Node {		  
		Node parent = null;
		List<Node> children = new ArrayList<Node>();
		IInstructionLine instructionObj;

		public IInstructionLine getInstructionObj() {
			return instructionObj;
		}

		public void setInstructionObj(IInstructionLine instructionObj) {
			this.instructionObj = instructionObj;
		}

		public Node getParent() {
			return parent;
		}
		
		public void setParent(Node parent) {
			this.parent = parent;
		}
		
		public boolean hasParent() {
			return parent != null ;
		}
		
		public List<Node> getChildren() {
			return children;
		}

		public void setChildren(List<Node> children) {
			this.children = children;
		}
		
		public void addhild(Node child) {
			children.add(child);
		}
		
		public boolean hasChildren() {
			return children.size() > 0 ;
		}
	  }
	
    class TreeViewContentProvider implements ITreeContentProvider{
        public Object[] getChildren(Object parentElement){
           if (parentElement instanceof Node){
        	   return ((Node)parentElement).getChildren().toArray();
           }
           
           return new Object[0];
        }
   
        public Object getParent(Object element){
        	if (element instanceof Node){
         	   return ((Node)element).getParent();
            }
           return null;
        }
   
        public boolean hasChildren(Object element){
        	if (element instanceof Node){
          	   return ((Node)element).hasChildren();
             }
           return false;
        }
   
        public Object[] getElements(Object cities){
           return getChildren(cities);
        }
   
        public void dispose(){
        }
   
        public void inputChanged(Viewer viewer, Object oldInput, Object newInput){
        }
     }
    
    class TreeTableLabelProvider implements ITableLabelProvider{
    	   
        public Image getColumnImage(Object element, int columnIndex){
        	//TODO: this is just a sample implementation, has to be changed.
        	if (columnIndex == 0) {
        		
        		if(element instanceof Node){
    				IInstructionLine i = ((Node)element).getInstructionObj();
    				if(i.getInstruction().getOpcode() == OPCODE_IF_ACMPEQ
    				|| i.getInstruction().getOpcode() == OPCODE_IF_ACMPNE
    				|| i.getInstruction().getOpcode() ==  OPCODE_IF_ICMPEQ
    				|| i.getInstruction().getOpcode() ==  OPCODE_IF_ICMPNE
    				|| i.getInstruction().getOpcode() ==  OPCODE_IF_ICMPLT
    				|| i.getInstruction().getOpcode() ==  OPCODE_IF_ICMPGE
    				|| i.getInstruction().getOpcode() ==  OPCODE_IF_ICMPGT
    				|| i.getInstruction().getOpcode() ==  OPCODE_IF_ICMPLE
    				|| i.getInstruction().getOpcode() ==  OPCODE_IFNONNULL
    				){
    					return JavaUI.getSharedImages().getImage(ISharedImages.IMG_OBJS_CLASS);
    				}
        		}
        		
				return JavaUI.getSharedImages().getImage(ISharedImages.IMG_OBJS_LOCAL_VARIABLE);
			}
			return null;
        }
   
        public String getColumnText(Object element, int columnIndex){
        	
        	if(element instanceof Node){
				IInstructionLine i = ((Node)element).getInstructionObj();
				
				if (columnIndex == 0) {
					return i.getInstruction().getOpcodeMnemonic();
				}
				else if (columnIndex == 1) {							
					return String.valueOf(i.getInstruction().getOpcode());
				}
				else if (columnIndex == 2) {
					return String.valueOf(i.getInstruction().getOffset());
				}
//				else if (columnIndex == 3) {
//					stack.executeInstruction(i.getInstruction());
//					return stack.toString();
//				}
        	}
        	
			return BytecodeVisualizerMessages.OperandStackView_Unknown;
        }
   
        public void addListener(ILabelProviderListener listener){
        }
   
        public void dispose(){
        }
   
        public boolean isLabelProperty(Object element, String property){
           return false;
        }
   
        public void removeListener(ILabelProviderListener listener){
        }
     }		
	
    
    /**
     * TODO: the inpout has to be adpted for OperandStack
     * @param instructions
     * @return input for the tree view 
     */
    public  Object  generateInput(List<IInstructionLine> instructions){
    	Node root = new Node();
		Node work = root;
		for (IInstructionLine i : instructions){
			Node child = new Node();
			child.setParent(work);
			child.setInstructionObj(i);
			
			work.addhild(child);
			
			if(i.getInstruction().getOpcode() == OPCODE_IF_ACMPEQ
			|| i.getInstruction().getOpcode() == OPCODE_IF_ACMPNE
			|| i.getInstruction().getOpcode() ==  OPCODE_IF_ICMPEQ
			|| i.getInstruction().getOpcode() ==  OPCODE_IF_ICMPNE
			|| i.getInstruction().getOpcode() ==  OPCODE_IF_ICMPLT
			|| i.getInstruction().getOpcode() ==  OPCODE_IF_ICMPGE
			|| i.getInstruction().getOpcode() ==  OPCODE_IF_ICMPGT
			|| i.getInstruction().getOpcode() ==  OPCODE_IF_ICMPLE
			|| i.getInstruction().getOpcode() ==  OPCODE_IFNONNULL
			){
				work = child;
			}
			
			if(i.getInstruction().getOpcode() == OPCODE_GOTO){
						work = root;
			}
			
		}
		
		
		return root;
	}

}
