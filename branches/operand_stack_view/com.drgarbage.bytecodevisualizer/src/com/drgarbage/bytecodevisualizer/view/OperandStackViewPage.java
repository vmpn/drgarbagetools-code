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
import java.util.Map;
import java.util.TreeMap;

import org.eclipse.jdt.ui.ISharedImages;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.ITreeViewerListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeExpansionEvent;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.TreeEvent;
import org.eclipse.swt.events.TreeListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.part.IPage;
import org.eclipse.ui.part.IPageSite;
import org.eclipse.ui.part.Page;

import com.drgarbage.algorithms.Algorithms;
import com.drgarbage.asm.render.intf.IInstructionLine;
import com.drgarbage.bytecode.instructions.Opcodes;
import com.drgarbage.bytecodevisualizer.BytecodeVisualizerMessages;
import com.drgarbage.bytecodevisualizer.editors.BytecodeEditor;
import com.drgarbage.bytecodevisualizer.editors.IClassFileEditorSelectionListener;
import com.drgarbage.controlflowgraph.ControlFlowGraphGenerator;
import com.drgarbage.controlflowgraph.ControlFlowGraphUtils;
import com.drgarbage.controlflowgraph.intf.IDirectedGraphExt;
import com.drgarbage.controlflowgraph.intf.IEdgeExt;
import com.drgarbage.controlflowgraph.intf.IEdgeListExt;
import com.drgarbage.controlflowgraph.intf.INodeExt;
import com.drgarbage.controlflowgraph.intf.INodeListExt;
import com.drgarbage.controlflowgraph.intf.INodeType;
import com.drgarbage.controlflowgraph.intf.MarkEnum;

/**
 * The implementation of the OPerand Stackh View Page.
 * 
 * @author Sergej Alekseev
 * @version $Revision$
 * $Id$
 */
public class OperandStackViewPage extends Page implements IPage, Opcodes{
	
	/**
	 * Tree Viewer of the OperandStack view Page.
	 */
	protected TreeViewer treeViewer;
	
    /**
     * Map of the tree items to the byte code line numbers in the editor.
     */
    private Map<Integer, TreeItem> treeMap;
    
	/**
	 * Reference to the active byte code editor.
	 */
	private BytecodeEditor editor;

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
	
	/**
	 * Set the reference to the active byte code editor.
	 * @param editor byte code editor
	 */
	public void setEditor(BytecodeEditor editor) {
		this.editor = editor;
		
		/* Synchronize tree selection with lines in the editor */
		editor.addtLineSelectionListener(new IClassFileEditorSelectionListener(){

			/* (non-Javadoc)
			 * @see com.drgarbage.bytecodevisualizer.editors.IClassFileEditorSelectionListener#lineSelectionChanged(int, java.lang.Object)
			 */
			@Override
			public void lineSelectionChanged(int newLine, Object o) {
				TreeItem item = treeMap.get(newLine);
				if(item != null){
					treeViewer.getTree().setSelection(item);
					treeViewer.refresh(true);
				}
			}
		});
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
		column.setText("Bytecode Instruction"); //TODO: define constant
		column.setWidth(200);
		
		column = new TreeColumn(tree, SWT.LEFT, 1);
		column.setMoveable(true);
		column.setText("Offset");//TODO: define constant
		column.setWidth(40);
		
		TreeColumn column3 = new TreeColumn(tree, SWT.RIGHT);
	    column3.setAlignment(SWT.LEFT);
	    column3.setText("Operand Stack");//TODO: define constant
	    column3.setWidth(100);
		
	    TreeColumn column4 = new TreeColumn(tree, SWT.RIGHT);
	    column4.setAlignment(SWT.LEFT);
	    column4.setText("Description");//TODO: define constant
	    column4.setWidth(100);
	    
		tree.setHeaderVisible(true);
    	tree.setLinesVisible(true);
    	
    	int order[] = {1, 0, 2, 3 };//tree.getColumnOrder();
    	tree.setColumnOrder(order);
    	
    	treeViewer.setContentProvider(new TreeViewContentProvider());
    	treeViewer.setLabelProvider(new TreeTableLabelProvider());
    	
        treeViewer.expandAll();
        
        /* selection listener for line synchronization */
        treeViewer.addSelectionChangedListener(new ISelectionChangedListener(){

			/* (non-Javadoc)
			 * @see org.eclipse.jface.viewers.ISelectionChangedListener#selectionChanged(org.eclipse.jface.viewers.SelectionChangedEvent)
			 */
			@Override
			public void selectionChanged(SelectionChangedEvent arg0) {
				ISelection sel =  arg0.getSelection();
				if(!sel.isEmpty()){
					TreeSelection treeSel = (TreeSelection) sel;
					Node n = (Node)treeSel.getFirstElement();
					Object o = n.getObject();
					if(o instanceof IInstructionLine){
						IInstructionLine i = (IInstructionLine)o;
						editor.selectLineAndRevaluate2(i.getLine());
					}
				}
			}
        });
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
        	if (columnIndex == 0) {
        		if(element instanceof Node){
        			Object o = ((Node)element).getObject();
        			if(o != null && o instanceof IInstructionLine){
        				IInstructionLine i = (IInstructionLine)o;
        				
        				//TODO: create new symbols
        				switch(ControlFlowGraphUtils.getInstructionNodeType(i.getInstruction().getOpcode())){
        					case INodeType.NODE_TYPE_SIMPLE:
        						return JavaUI.getSharedImages().getImage(ISharedImages.IMG_OBJS_LOCAL_VARIABLE);
        					case INodeType.NODE_TYPE_IF:
        						return JavaUI.getSharedImages().getImage(ISharedImages.IMG_OBJS_INTERFACE);
        					case INodeType.NODE_TYPE_RETURN:
        					case INodeType.NODE_TYPE_GOTO_JUMP:
        						return JavaUI.getSharedImages().getImage(ISharedImages.IMG_OBJS_PUBLIC);
        					case INodeType.NODE_TYPE_SWITCH:
        						return JavaUI.getSharedImages().getImage(ISharedImages.IMG_OBJS_INTERFACE);
        					case INodeType.NODE_TYPE_INVOKE:
        						return JavaUI.getSharedImages().getImage(ISharedImages.IMG_OBJS_PUBLIC);
        					case INodeType.NODE_TYPE_GET:
        						return JavaUI.getSharedImages().getImage(ISharedImages.IMG_OBJS_LOCAL_VARIABLE);
        					default:
        						return JavaUI.getSharedImages().getImage(ISharedImages.IMG_OBJS_LOCAL_VARIABLE);
        							
        				}
        			}
        			if(o instanceof String){
        				return JavaUI.getSharedImages().getImage(ISharedImages.IMG_OBJS_PROTECTED);
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
   
        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.IBaseLabelProvider#isLabelProperty(java.lang.Object, java.lang.String)
         */
        public boolean isLabelProperty(Object element, String property){
           return false;
        }
   
        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.IBaseLabelProvider#removeListener(org.eclipse.jface.viewers.ILabelProviderListener)
         */
        public void removeListener(ILabelProviderListener listener){
        }
     }		
    
    /**
     * Sets input of the tree viewer.
     * @param instructions list of byte code instructions
     */
    public void setInput(List<IInstructionLine> instructions){
    	Object input = generateInput(instructions);		
    	treeViewer.setInput(input);
    	treeViewer.expandAll();

    	treeMap = new TreeMap<Integer, TreeItem>();
    	fillTreeMap(treeViewer.getTree().getItems());
    	
    }
    
    /**
     * Fill the map for synchronization with lines in the editor.
     * @param treeItems
     */
    private void fillTreeMap(TreeItem treeItems[]){
		for(TreeItem item: treeItems){
			fillTreeMap(item.getItems());
			Object data = item.getData();

			if(data instanceof Node){
				Node node = (Node) data;
				Object nodeObj = node.getObject();
				if(nodeObj instanceof IInstructionLine){
					IInstructionLine i = (IInstructionLine) nodeObj;				
					treeMap.put(i.getLine(), item);
				}
			}
		}
    }
    
    /**
     * Creates the tree structure for the operand stack view.
     * @param instructions list of instructions
     * @return the tree structure
     */
    private  Object  generateInput(List<IInstructionLine> instructions)
    {
		IDirectedGraphExt graph = ControlFlowGraphGenerator.generateSynchronizedControlFlowGraphFrom(instructions, true);

		/* 
		 * Mark nodes and create spanning tree.
		 * Marking should be done before the spanning tree is created, 
		 * because some information gets to be missing after execution 
		 * of the spanning tree algorithm.
		 */
		markNodes(graph);
		Algorithms.resetVisitFlags(graph);
		
		/* remove back edges (loops) from the graph */
		removeBackEdges(graph);
		
		Algorithms.doSpanningTreeAlgorithm(graph, false);
		Algorithms.resetVisitFlags(graph);
				
		Node root = new Node();
		List<INodeExt> listOfStartNodes = getAllStartNodes(graph);
		for(INodeExt n: listOfStartNodes)
			parseGraph(root, n);
		
		
		return root;
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
     * Mark following nodes: if, switch and nodes with incoming degree
     * greater than 1 (end of block).
     * @param graph control flow graph
     */
    private void markNodes(IDirectedGraphExt graph){
    	INodeListExt graphNodelist = graph.getNodeList();
    	
    	for(int i = 0; i < graphNodelist.size(); i++){
    		INodeExt node = graphNodelist.getNodeExt(i);

    		if(node.isVisited()){
    			if(node.getOutgoingEdgeList().size() == 0){
    				/* last node has been reached */
    				return;
    			}
    			
    			IEdgeExt e = node.getOutgoingEdgeList().getEdgeExt(0);
    			e.setVisited(true);
    			node = e.getTarget();
    			continue;
    		}
    		
    		if(node.getIncomingEdgeList().size() > 1){
    			/* end of block */
    			node.setMark(MarkEnum.RED);
    		}
    		
    		int out = node.getOutgoingEdgeList().size();
    		if(out == 2){ /* if */
    			node.setMark(MarkEnum.GREEN);
    			IEdgeListExt outEdges = graphNodelist.getNodeExt(i).getOutgoingEdgeList();
    			outEdges.getEdgeExt(1).setMark(MarkEnum.BLACK); /* true */
    			outEdges.getEdgeExt(0).setMark(MarkEnum.WHITE); /* false */
    		}
    		if(out > 2){ /* switch */
    			node.setMark(MarkEnum.ORANGE);
    			markSwitchNodes(graphNodelist, i, node.getOutgoingEdgeList().size());
    		}
    		
    		node.setVisited(true);
    	}
    }
    
    /**
     * @param graphNodelist
     * @param nodeIndex
     * @param count
     * @return
     */
    private int markSwitchNodes(INodeListExt graphNodelist, int nodeIndex, int count){
    	for(int i = (nodeIndex + 1); i < graphNodelist.size(); i++){
    		INodeExt node = graphNodelist.getNodeExt(i);

    		if(node.isVisited()){
    			continue;
    		}
    		
    		int out = node.getOutgoingEdgeList().size();
    		if(out > 1){ /* new switch block found */
    			node.setMark(MarkEnum.ORANGE);
    			count -= markSwitchNodes(graphNodelist, i, node.getOutgoingEdgeList().size()) + 1;
    		}
    		
    		int inc = node.getIncomingEdgeList().size();
    		
    		int res = (count - inc);
    		if(res == 0){
    			/* end of block */
    			node.setMark(MarkEnum.RED);
    			node.setVisited(true);
    			return res;
    		}
    		
    		if(res < 0){		
    			return res;
    		}

    		if(inc > 1){
    			count -= inc -1;
    		}

    		node.setVisited(true);
    	}
    	
    	return 0;
    }

    
    /**
     * Creates a tree structure for operand stack view.
     * @param work parent node
     * @param node current node or start node
     */
	private void parseGraph(Node work, INodeExt node){

    	int out = 0;
    	do{
    		IEdgeExt e;
    		
    		if(node.isVisited()){
    			return;
    		}
    		
    		Node child = new Node();
    		child.setObject(node.getData());

    		/* end of the block (if or switch ) */
    		if(node.getMark() == MarkEnum.RED){
    			if(work.getParent() != null){
    				work = work.getParent().getParent();
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

	/**
	 * Element of the operand stack view structure.
	 */
	class Node {		  
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
}


