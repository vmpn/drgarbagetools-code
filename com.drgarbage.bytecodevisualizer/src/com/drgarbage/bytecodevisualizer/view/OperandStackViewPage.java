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

import com.drgarbage.algorithms.Algorithms;
import com.drgarbage.asm.render.intf.IInstructionLine;
import com.drgarbage.bytecode.instructions.Opcodes;
import com.drgarbage.bytecodevisualizer.BytecodeVisualizerMessages;
import com.drgarbage.controlflowgraph.ControlFlowGraphGenerator;
import com.drgarbage.controlflowgraph.intf.IDirectedGraphExt;
import com.drgarbage.controlflowgraph.intf.IEdgeExt;
import com.drgarbage.controlflowgraph.intf.IEdgeListExt;
import com.drgarbage.controlflowgraph.intf.INodeExt;
import com.drgarbage.controlflowgraph.intf.INodeListExt;
import com.drgarbage.controlflowgraph.intf.MarkEnum;

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
		createTreeViewer(parent);
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
	
	/**
	 * Creates the tree viewer of the operand stack view.
	 * @param parent composite
	 */
	void createTreeViewer(Composite parent){
		treeViewer = new TreeViewer(parent, SWT.BORDER);
		
		Tree tree = treeViewer.getTree();
		TreeColumn column = new TreeColumn(tree, SWT.LEFT, 0);
		column.setMoveable(true);
		column.setText("C1");
		column.setWidth(200);
		
		
		column = new TreeColumn(tree, SWT.LEFT, 1);
		column.setMoveable(true);
		column.setText("C2");
		column.setWidth(40);
		
		
		TreeColumn column3 = new TreeColumn(tree, SWT.RIGHT);
	    column3.setAlignment(SWT.LEFT);
	    column3.setText("C3");
	    column3.setWidth(100);
		
		tree.setHeaderVisible(true);
    	tree.setLinesVisible(true);
    	
    	int order[] = {1, 0, 2 };//tree.getColumnOrder();
    	tree.setColumnOrder(order);
    	
    	treeViewer.setContentProvider(new TreeViewContentProvider());
    	treeViewer.setLabelProvider(new TreeTableLabelProvider());

        treeViewer.expandAll();
     }

	/**
	 * Element of the operand stack view structure.
	 */
	public  class Node {		  
		Node parent = null;
		List<Node> children = new ArrayList<Node>();
		Object obj;

		public Object getObject() {
			return obj;
		}

		public void setObject(Object obj) {
			this.obj = obj;
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
	
    /**
     * Content provider for the operand stack view.
     */
    class TreeViewContentProvider implements ITreeContentProvider{
    	
        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.Object)
         */
        public Object[] getChildren(Object parentElement){
           if (parentElement instanceof Node){
        	   return ((Node)parentElement).getChildren().toArray();
           }

           return new Object[0];
        }
   
        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.ITreeContentProvider#getParent(java.lang.Object)
         */
        public Object getParent(Object element){
        	if (element instanceof Node){
         	   return ((Node)element).getParent();
            }

           return null;
        }
   
        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang.Object)
         */
        public boolean hasChildren(Object element){
        	if (element instanceof Node){
          	   return ((Node)element).hasChildren();
             }
        	
           return false;
        }
   
        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.ITreeContentProvider#getElements(java.lang.Object)
         */
        public Object[] getElements(Object nodes){
        	return getChildren(nodes);
        }
   
        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.IContentProvider#dispose()
         */
        public void dispose(){
        }
   
        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
         */
        public void inputChanged(Viewer viewer, Object oldInput, Object newInput){
        }
     }
    
    /**
     * Label provider for the operand stack view
     */
    class TreeTableLabelProvider implements ITableLabelProvider{
    	   
        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.Object, int)
         */
        public Image getColumnImage(Object element, int columnIndex){
        	//TODO: this is just a sample implementation, has to be changed.
        	if (columnIndex == 0) {
        		if(element instanceof Node){
        			Object o = ((Node)element).getObject();
        			if(o != null && o instanceof IInstructionLine){
        				IInstructionLine i = (IInstructionLine)o;

        				if(i.getInstruction().getOpcode() == OPCODE_IF_ACMPEQ
        						|| i.getInstruction().getOpcode() == OPCODE_IF_ACMPNE
        						|| i.getInstruction().getOpcode() ==  OPCODE_IF_ICMPEQ
        						|| i.getInstruction().getOpcode() ==  OPCODE_IF_ICMPNE
        						|| i.getInstruction().getOpcode() ==  OPCODE_IF_ICMPLT
        						|| i.getInstruction().getOpcode() ==  OPCODE_IF_ICMPGE
        						|| i.getInstruction().getOpcode() ==  OPCODE_IF_ICMPGT
        						|| i.getInstruction().getOpcode() ==  OPCODE_IF_ICMPLE
        						|| i.getInstruction().getOpcode() ==  OPCODE_IFEQ
        						|| i.getInstruction().getOpcode() ==  OPCODE_IFNE
        						|| i.getInstruction().getOpcode() ==  OPCODE_IFLT
        						|| i.getInstruction().getOpcode() ==  OPCODE_IFGE
        						|| i.getInstruction().getOpcode() ==  OPCODE_IFGT
        						|| i.getInstruction().getOpcode() ==  OPCODE_IFLE
        						|| i.getInstruction().getOpcode() ==  OPCODE_IFNONNULL
        						){
        					return JavaUI.getSharedImages().getImage(ISharedImages.IMG_OBJS_CLASS);
        				}
        			}
        			if(o instanceof String){
        				return JavaUI.getSharedImages().getImage(ISharedImages.IMG_FIELD_PRIVATE);
        			}
        		}
        		
        		return JavaUI.getSharedImages().getImage(ISharedImages.IMG_OBJS_LOCAL_VARIABLE);
			}
			return null;
        }

        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object, int)
         */
        public String getColumnText(Object element, int columnIndex){

        	if(element instanceof Node){
        		Object o = ((Node)element).getObject();
        		if(o != null && o instanceof IInstructionLine){
        			IInstructionLine i = (IInstructionLine)o;

        			if (columnIndex == 0) {
        				return i.getInstruction().getOpcodeMnemonic();
        			}
        			else if (columnIndex == 1) {							
        				return String.valueOf(i.getInstruction().getOffset());
        			}
        			else if (columnIndex == 2) {
        				return String.valueOf(i.getInstruction().getOpcode());
        			}
        		}
        		else{
        			if (columnIndex == 0) {
        				return o.toString();
        			}
        			else{
        				return "";
        			}
        		}
        	}
        	
			return BytecodeVisualizerMessages.OperandStackView_Unknown;
        	
        }
   
        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.IBaseLabelProvider#addListener(org.eclipse.jface.viewers.ILabelProviderListener)
         */
        public void addListener(ILabelProviderListener listener){
        }
   
        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.IBaseLabelProvider#dispose()
         */
        public void dispose(){
        }
   
        public boolean isLabelProperty(Object element, String property){
           return false;
        }
   
        public void removeListener(ILabelProviderListener listener){
        }
     }		
    
    /**
     * Creates the tree structure for the operand stack view.
     * @param instructions list of instructions
     * @return the tree structure
     */
    @SuppressWarnings("restriction")
    public  Object  generateInput(List<IInstructionLine> instructions)
    {
		IDirectedGraphExt graph = ControlFlowGraphGenerator.generateSynchronizedControlFlowGraphFrom(instructions, true);
//		Algorithms.printGraph(graph);

		/* 
		 * Mark nodes and create spanning tree.
		 * Marking should be done before the spanning tree is created, 
		 * because some information gets to be missing after execution 
		 * of the spanning tree algorithm.
		 */
		markNodes(graph);
		Algorithms.doSpanningTreeAlgorithm(graph, false);
		Algorithms.resetVisitFlags(graph);
				
		Node root = new Node();
		parseGraph(root, graph.getNodeList().getNodeExt(0));
		return root;
    }
    
    /**
     * Mark following nodes: if, switch and nodes with incoming degree
     * greater than 1 (end of block).
     * @param graph control flow graph
     */
    @SuppressWarnings("restriction")
    private void markNodes(IDirectedGraphExt graph){
    	INodeListExt graphNodelist = graph.getNodeList();
    	
    	for(int i = 0; i < graphNodelist.size(); i++){
    		if(graphNodelist.getNodeExt(i).getIncomingEdgeList().size() > 1){
    			/* end of block */
    			graphNodelist.getNodeExt(i).setMark(MarkEnum.RED);
    		}
    		
    		int out = graphNodelist.getNodeExt(i).getOutgoingEdgeList().size();
    		if(out == 2){ /* if */
    			graphNodelist.getNodeExt(i).setMark(MarkEnum.GREEN);
    			IEdgeListExt outEdges = graphNodelist.getNodeExt(i).getOutgoingEdgeList();
    			outEdges.getEdgeExt(1).setMark(MarkEnum.BLACK); /* true */
    			outEdges.getEdgeExt(0).setMark(MarkEnum.WHITE); /* false */
    		}
    		if(out > 2){ /* switch */
    			graphNodelist.getNodeExt(i).setMark(MarkEnum.ORANGE);
    		}
    	}
    }

    
    /**
     * Creates a tree structure for operand stack view.
     * @param work parent node
     * @param node current node or start node
     */
    @SuppressWarnings("restriction")
	private void parseGraph(Node work, INodeExt node){

    	int out = 0;
    	do{
//    		System.out.println(node.getByteCodeOffset());
    		IEdgeExt e;
    		
    		if(node.isVisited()){
    			return;
    		}
    		
    		Node child = new Node();
    		child.setObject(node.getData());

    		/* end of the block (if or switch ) */
    		if(node.getMark() == MarkEnum.RED){
//    			System.out.println(node.getByteCodeOffset() + " RED -> return");
    			if(work.getParent() != null){
    				work = work.getParent().getParent();
    					System.out.println(node.getByteCodeOffset() + " ?" + work.getObject());
    			}
    		}
    		
    		child.setParent(work);
        	work.addhild(child);
    		node.setVisited(true);
    		
    		out = node.getOutgoingEdgeList().size();
    		if(out == 0){ /* last node */
    			return;
    		}

    		/* if block */
    		if(node.getMark() == MarkEnum.GREEN) {

    			/* create two pseudo nodes 'true' and 'false' */
    			Node child1 = new Node();
    			child1.setParent(child);
    			child1.setObject("true");
    			child.addhild(child1);
    			
    			Node child2 = new Node();
    			child2.setParent(child);
    			child2.setObject("false");
    			child.addhild(child2);

    			IEdgeListExt outEdges =  node.getOutgoingEdgeList();
    			
    			switch(outEdges.size()){
    			case 2: {
    				IEdgeExt e1 = outEdges.getEdgeExt(0);
    				IEdgeExt e2 = outEdges.getEdgeExt(1);

    				/*  start parsing with the 'true' edge */
    				if(e1.getMark() == MarkEnum.BLACK){ /* true */
    					e1.setVisited(true);
    					parseGraph(child1, e1.getTarget());

    					e2.setVisited(true);
    					parseGraph(child2, e2.getTarget());
    				}
    				else{
    					e2.setVisited(true);
    					parseGraph(child1, e2.getTarget());

    					e1.setVisited(true);
    					parseGraph(child2, e1.getTarget());
    				}
    			}
    			break;
    			
    			case 1: {
    				IEdgeExt e1 = outEdges.getEdgeExt(0);

    				if(e1.getMark() == MarkEnum.BLACK){ /* true */
    					e1.setVisited(true);
    					parseGraph(child1, e1.getTarget());   	    			
    				}
    				else{
    					e1.setVisited(true);
    					parseGraph(child2, e1.getTarget());
    				}
    			}
    			break;
    			}
    		}
    		
    		/* switch block */
    		if(node.getMark() == MarkEnum.ORANGE) { 
//    			System.out.println(node.getByteCodeOffset() + " SWITCH");
    			IEdgeListExt edgeList = node.getOutgoingEdgeList();
    			for(int j = 0; j < edgeList.size(); j++){
    				e = node.getOutgoingEdgeList().getEdgeExt(j);
    				
    				Node switchChild = new Node();
        			switchChild.setParent(child);
        			if(e.getData()!=null){
        				switchChild.setObject(e.getData().toString());
        			}
        			else{
        				switchChild.setObject("ERROR");
        			}
        			child.addhild(switchChild);
        			
        			e.setVisited(true);
        			parseGraph(switchChild, e.getTarget());
    			}
    		}

    		e = node.getOutgoingEdgeList().getEdgeExt(0);
    		e.setVisited(true);
    		node = e.getTarget();
    		
    	} while(out != 0);
    }

}
