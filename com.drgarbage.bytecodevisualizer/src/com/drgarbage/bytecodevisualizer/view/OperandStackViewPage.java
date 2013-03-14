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

//import org.eclipse.jdt.ui.ISharedImages;
import org.eclipse.ui.ISharedImages;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.part.IPageSite;
import org.eclipse.ui.part.Page;

import com.drgarbage.asm.render.intf.IClassFileDocument;
import com.drgarbage.asm.render.intf.IInstructionLine;
import com.drgarbage.asm.render.intf.IMethodSection;
import com.drgarbage.bytecode.ByteCodeConstants;
import com.drgarbage.bytecodevisualizer.BytecodeVisualizerMessages;
import com.drgarbage.bytecodevisualizer.BytecodeVisualizerPlugin;
import com.drgarbage.bytecodevisualizer.editors.BytecodeDocumentProvider;
import com.drgarbage.bytecodevisualizer.editors.BytecodeEditor;
import com.drgarbage.bytecodevisualizer.editors.IClassFileEditorSelectionListener;
import com.drgarbage.bytecodevisualizer.operandstack.OperandStack;
import com.drgarbage.controlflowgraph.ControlFlowGraphUtils;
import com.drgarbage.controlflowgraph.intf.INodeExt;
import com.drgarbage.controlflowgraph.intf.INodeListExt;
import com.drgarbage.controlflowgraph.intf.INodeType;
import com.drgarbage.core.img.CoreImg;

/**
 * The abstract Operand Stack View Page.
 * 
 * @author Sergej Alekseev
 * @version $Revision$
 * $Id$
 */
public abstract class OperandStackViewPage extends Page {
	
	/**
	 * Tree Viewer of the OperandStack view Page.
	 */
	private TreeViewer treeViewer;
	
	/**
	 * Reference to the selected method instance.
	 */
	private IMethodSection methodInput;
	
	private OperandStack operandStack;
	
	/* Menu actions */
	private IAction showTreeViewAction, showBasicBlockViewAction, showInstructioneListViewAction;
	
	static enum OperandStackView_ID{
		TREE_VIEW,
		BASICBKLOCK_VIEW,
		INSTR_LIST_VIEW;
	};
	
	/**
	 * Kind of the view. The value is one of the  REE_VIEW, 
	 * BASICBKLOCK_VIEW or INSTR_LIST_VIEW.
	 */
	OperandStackView_ID view_ID = OperandStackView_ID.TREE_VIEW;

	/**
     * Map of the tree items to the byte code line numbers in the editor.
     */
	private Map<Integer, Node> treeMap;
    
    /**
     * Listener for synchronization of lines with the BCV.
     */
    private IClassFileEditorSelectionListener classFileEditorSelectionListener;
    
    /**
     * Use the mutex variable to avoid call backs from the editor view. 
     */
     private boolean treeViewerSelectionMutex = false; 
    
 	/**
 	 * Reference to the active byte code editor.
 	 */
 	private BytecodeEditor editor;
     
	private synchronized boolean isTreeViewerSelectionMutex() {
		return treeViewerSelectionMutex;
	}

	private synchronized void setTreeViewerSelectionMutex(boolean b) {
		treeViewerSelectionMutex = b;
	}

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
     * Returns the current view id, one of the REE_VIEW, BASICBKLOCK_VIEW, INSTR_LIST_VIEW. 
     * @return view_ID
     */
    public OperandStackView_ID getView_ID() {
		return view_ID;
	}

	/**
	 * Sets the view id, one of the REE_VIEW, BASICBKLOCK_VIEW, INSTR_LIST_VIEW.
	 * @param view_ID
	 */
	public void setView_ID(OperandStackView_ID view_ID) {
		this.view_ID = view_ID;
	}
	
	private void setMesssageInStatusLine(){ //TODO: sample implementation
		if(methodInput == null){
			return;
		}
		
		IActionBars bars = getSite().getActionBars();
		
		IStatusLineManager slm = bars.getStatusLineManager();
		
		if(operandStack.getMaxStackSize() > methodInput.getMaxStack() || operandStack.getMaxStackSize() < methodInput.getMaxStack()){
			slm.setErrorMessage((JavaUI.getSharedImages()
					.getImage(ISharedImages.IMG_OBJS_ERROR_TSK)),
					"max_stack should be: " + String.valueOf(methodInput.getMaxStack()) +
					" is: " + String.valueOf(operandStack.getMaxStackSize()) +
					", max_locals: " + String.valueOf(methodInput.getMaxLocals()));
			
			IStatus status = BytecodeVisualizerPlugin.createErrorStatus("Operand Stack under- or overflow in Method: " + methodInput.getDescriptor() + " " + methodInput.getName() +" occurred", null);
			BytecodeVisualizerPlugin.log(status);
			
		} else {
			slm.setMessage(JavaUI.getSharedImages()
					.getImage(ISharedImages.IMG_OBJS_INFO_TSK),
					"max_stack: " + String.valueOf(methodInput.getMaxStack()) +
					", max_locals: " + String.valueOf(methodInput.getMaxLocals()));
		}

		slm.update(true);
	}

	private void configureToolBar(){

		IActionBars bars = getSite().getActionBars();
		IToolBarManager tbm = bars.getToolBarManager();
		
		showTreeViewAction = new Action() {
			public void run() {
				activateView(OperandStackView_ID.TREE_VIEW);
			}
		};
		showTreeViewAction
				.setImageDescriptor(CoreImg.bytecodeViewer_16x16); //$NON-NLS-1$ //TODO: make new icon
		tbm.add(showTreeViewAction);
		showTreeViewAction.setChecked(true);
		
		showBasicBlockViewAction = new Action() {
			public void run() {
				activateView(OperandStackView_ID.BASICBKLOCK_VIEW);
			}
		};
		showBasicBlockViewAction
				.setImageDescriptor(CoreImg.basicblockViewIcon_16x16); //$NON-NLS-1$ //TODO: make new icon
		tbm.add(showBasicBlockViewAction);
		showBasicBlockViewAction.setChecked(false);
		
		showInstructioneListViewAction = new Action() {
			public void run() {
				activateView(OperandStackView_ID.INSTR_LIST_VIEW);
			}
		};
		showInstructioneListViewAction
				.setImageDescriptor(CoreImg.hideMethodAction_16x16); //$NON-NLS-1$ //TODO: make new icon
		tbm.add(showInstructioneListViewAction);
		showInstructioneListViewAction.setChecked(false);
		
		enableActions(false);
		
		tbm.update(true);

	}
	
	/**
	 * Enables or disables the actions.
	 * @param b true or false
	 */
	private void enableActions(boolean b){
		showTreeViewAction.setEnabled(b);
		showBasicBlockViewAction.setEnabled(b);
		showInstructioneListViewAction.setEnabled(b);
	}
	
	/**
	 * Activates selected view
	 * @param id - id of the view
	 */
	private void activateView(OperandStackView_ID id) {

		if (id == OperandStackView_ID.TREE_VIEW) {
			showTreeViewAction.setChecked(true);
			showBasicBlockViewAction.setChecked(false);
			showInstructioneListViewAction.setChecked(false);
		} 
		else if (id == OperandStackView_ID.BASICBKLOCK_VIEW) {
			showTreeViewAction.setChecked(false);
			showBasicBlockViewAction.setChecked(true);
			showInstructioneListViewAction.setChecked(false);
		}
		else if( id == OperandStackView_ID.INSTR_LIST_VIEW){
			showTreeViewAction.setChecked(false);
			showBasicBlockViewAction.setChecked(false);
			showInstructioneListViewAction.setChecked(true);
		}
		
		/* update input */
		if(methodInput != null){
			setView_ID(id);
			setInput(methodInput.getInstructionLines());
		}
	}
	
	/**
	 * Sets the input - the list of the byte code instructions
	 * for the table Viewer. 
	 * @param methodSection
	 */
	public void setInput(IMethodSection m) {
		if(m == null && methodInput == null){
			return;
		}
		
		if(m!= null && m.equals(methodInput)){
			return;
		}
		
		methodInput = m;

		if(methodInput == null){
			enableActions(false);
			getTreeView().setInput(null);
		}
		else{
			setInput(methodInput.getInstructionLines());
			enableActions(true);
		}
		
		setMesssageInStatusLine();
	}
	
	/**
	 * Set the reference to the active byte code editor.
	 * @param editor byte code editor
	 */
	public void setEditor(BytecodeEditor editor) {
		this.editor = editor;
		
		/* Synchronize tree selection with lines in the editor */		
		classFileEditorSelectionListener = new IClassFileEditorSelectionListener(){

			/* (non-Javadoc)
			 * @see com.drgarbage.bytecodevisualizer.editors.IClassFileEditorSelectionListener#lineSelectionChanged(int, java.lang.Object)
			 */
			@Override
			public void lineSelectionChanged(int newLine, Object o) {
				if(isTreeViewerSelectionMutex()){
					setTreeViewerSelectionMutex(false);
					return;
				}
				
				if(treeMap == null){
					return;
				}
				
				Node node  = treeMap.get(newLine);
		    	if(node != null){
		    		treeViewer.expandToLevel(node, 1);
		    		Widget w = treeViewer.testFindItem(node);
		    		if(w != null){
		    			TreeItem t = (TreeItem)w;
		    			treeViewer.getTree().select(t);
		    			treeViewer.refresh(true);
		    		}
		    	}
			}
		};
		editor.addtLineSelectionListener(classFileEditorSelectionListener);

	}

	/* (non-Javadoc)
	 * Method declared on Page
	 */
	public void init(IPageSite pageSite) {
		super.init(pageSite);
		configureToolBar();
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
		editor.removeLineSelectionListener(classFileEditorSelectionListener);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.Page#getControl()
	 */
	public Control getControl() {
		if(treeViewer == null){
			return null;
		}
		return treeViewer.getControl();	
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.Page#setFocus()
	 */
	@Override
	public void setFocus() {
		treeViewer.getTree().setFocus();
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
	    column3.setText("Operand Stack before");//TODO: define constant
	    column3.setWidth(100);
	    
		TreeColumn column4 = new TreeColumn(tree, SWT.RIGHT);
	    column4.setAlignment(SWT.LEFT);
	    column4.setText("Operand Stack after");//TODO: define constant
	    column4.setWidth(100);
		
	    TreeColumn column5 = new TreeColumn(tree, SWT.RIGHT);
	    column5.setAlignment(SWT.LEFT);
	    column5.setText("Description");//TODO: define constant
	    column5.setWidth(100);
	    
		tree.setHeaderVisible(true);
    	tree.setLinesVisible(true);
    	
    	int order[] = {1, 0, 2, 3, 4 };//tree.getColumnOrder();
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
					/* set a mutex to avoid the call back from the editor */
					setTreeViewerSelectionMutex(true);
					
					TreeSelection treeSel = (TreeSelection) sel;
					Node n = (Node)treeSel.getFirstElement();
					Object o = n.getObject();
					
					if(o instanceof IInstructionLine){
						IInstructionLine i = (IInstructionLine)o;
						editor.selectLineAndRevaluate2(i.getLine());
					}
					
					if(o instanceof String){ /* use parent of the true, false or switch value nodes */
						IInstructionLine i = (IInstructionLine)n.getParent().getObject();
						if(i != null){
							editor.selectLineAndRevaluate2(i.getLine());
						}
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
        						return JavaUI.getSharedImages().getImage(org.eclipse.jdt.ui.ISharedImages.IMG_OBJS_LOCAL_VARIABLE);
        					case INodeType.NODE_TYPE_IF:
        						return JavaUI.getSharedImages().getImage(org.eclipse.jdt.ui.ISharedImages.IMG_OBJS_INTERFACE);
        					case INodeType.NODE_TYPE_RETURN:
        					case INodeType.NODE_TYPE_GOTO_JUMP:
        						return JavaUI.getSharedImages().getImage(org.eclipse.jdt.ui.ISharedImages.IMG_OBJS_PUBLIC);
        					case INodeType.NODE_TYPE_SWITCH:
        						return JavaUI.getSharedImages().getImage(org.eclipse.jdt.ui.ISharedImages.IMG_OBJS_INTERFACE);
        					case INodeType.NODE_TYPE_INVOKE:
        						return JavaUI.getSharedImages().getImage(org.eclipse.jdt.ui.ISharedImages.IMG_OBJS_PUBLIC);
        					case INodeType.NODE_TYPE_GET:
        						return CoreImg.get_instr_16x16.createImage();
        					default:
        						return JavaUI.getSharedImages().getImage(org.eclipse.jdt.ui.ISharedImages.IMG_OBJS_LOCAL_VARIABLE);
        							
        				}
        			}
        			if(o instanceof String){
        				return JavaUI.getSharedImages().getImage(org.eclipse.jdt.ui.ISharedImages.IMG_OBJS_PROTECTED);
        			}
        		}
        		
        		return JavaUI.getSharedImages().getImage(org.eclipse.jdt.ui.ISharedImages.IMG_OBJS_LOCAL_VARIABLE);
			}
			return null;
        }

        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object, int)
         */
        public String getColumnText(Object element, int columnIndex){

        	if(element instanceof Node){
        		Node node = (Node)element;
        		Object o = node.getObject();
        		if(o != null && o instanceof IInstructionLine){
        			IInstructionLine i = (IInstructionLine)o;

        			if (columnIndex == 0) {
        				return i.getInstruction().getOpcodeMnemonic();
        			}
        			else if (columnIndex == 1) {							
        				return String.valueOf(i.getInstruction().getOffset());
        			}
        			else if (columnIndex == 2) { /* operand stack before */
        				return node.getOperandStackBefore();
        			}
        			else if (columnIndex == 3) { /* operand stack after*/
        				return node.getOperandStackAfter();
        			}
        			else if (columnIndex == 4) { /* opcode description   */
        				return ByteCodeConstants.OPCODE_OPERANDSTACK_DESCR[i.getInstruction().getOpcode()];
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
     * @param id kind of the view
     */
    private void setInput(List<IInstructionLine> instructions){
    	Object input = generateInput(instructions, view_ID);

    	if(input == null){
    		return;
    	}
    	
    	/* fill tree map for synchronization */
    	treeMap = new TreeMap<Integer, Node>();
    	fillTreeMap((Node)input);
    	
		//TODO: implement OperandStack here    	
    	BytecodeDocumentProvider byteCodeDocumentProvider = (BytecodeDocumentProvider) editor.getDocumentProvider();
    	if(byteCodeDocumentProvider!= null){
    		IClassFileDocument ic = byteCodeDocumentProvider.getClassFileDocument();

    		/* when the methodInput changes, a new stack is generated */
    		/* later, we can add the reference to the previous stack to the new stack */
    		operandStack = new OperandStack(instructions, ic.getConstantPool(), methodInput.getLocalVariableTable());
    		INodeListExt nodeList = operandStack.getOperandStackGraph().getNodeList();
    		for(int i = 0; i < nodeList.size(); i++){
    			INodeExt n = nodeList.getNodeExt(i);
    			Node node = treeMap.get(n.getCounter()); /* counter attribute is used to store the line numbers */
    			if(node != null){
    				Object stackList = n.getData();
    				if(stackList instanceof ArrayList<?>){ //TODO: Optimize display options
    					node.setOperandStackBefore((String) ((ArrayList) stackList).get(0));
    					node.setOperandStackAfter((String) ((ArrayList) stackList).get(1));
    				}
    				else{
    					node.setOperandStackBefore(BytecodeVisualizerMessages.OperandStackView_Unknown);
    					node.setOperandStackAfter(BytecodeVisualizerMessages.OperandStackView_Unknown);
    				}
    			}
    		}
    	}
    	
    	treeViewer.setInput(input);
    	treeViewer.expandAll();

    	/* set current selection */
    	int newLine = editor.getSelectedLine();
    	Node node  = treeMap.get(newLine);
    	if(node != null){
    		Widget w = treeViewer.testFindItem(node);
    		if(w != null){
    			TreeItem t = (TreeItem)w;
    			treeViewer.getTree().select(t);
    			treeViewer.refresh(true);
    		}
    	}
    }
    
    private void fillTreeMap(Node root){
    	for(Node n: root.getChildren()){
    		fillTreeMap(n);
    		Object nodeObj = n.getObject();
			if(nodeObj instanceof IInstructionLine){
				IInstructionLine i = (IInstructionLine) nodeObj;				
				treeMap.put(i.getLine(), n);
			}
    	}
      }
    
    /**
     * Creates the tree structure for the operand stack view.
     * @param instructions list of instructions
     * @param id kind of the view
     * @return the tree structure
     */
    protected abstract  Object  generateInput(List<IInstructionLine> instructions, OperandStackView_ID id);

	/**
	 * Element of the operand stack view structure.
	 */
	class Node {		  
		Node parent = null;
		List<Node> children = new ArrayList<Node>();
		Object obj;
		String operandStackBefore, operandStackAfter;

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
		
		public String getOperandStackBefore() {
			return operandStackBefore;
		}
		
		public String getOperandStackAfter() {
			return operandStackAfter;
		}

		public void setOperandStackBefore(String operandStack) {
			this.operandStackBefore = operandStack;
		}
		
		public void setOperandStackAfter(String operandStack) {
			this.operandStackAfter = operandStack;
		}
	}
}

