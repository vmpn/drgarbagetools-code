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

package com.drgarbage.controlflowgraphfactory.compare;

import java.io.IOException;
import java.io.InputStream;
import java.io.LineNumberReader;
import java.io.ObjectInputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.CompareUI;
import org.eclipse.compare.IStreamContentAccessor;
import org.eclipse.compare.contentmergeviewer.ContentMergeViewer;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;

import com.drgarbage.algorithms.Algorithms;
import com.drgarbage.algorithms.ControlFlowGraphCompare;
//import com.drgarbage.bytecodevisualizer.BytecodeVisualizerPlugin;
import com.drgarbage.controlflowgraph.intf.IDirectedGraphExt;
import com.drgarbage.controlflowgraph.intf.INodeExt;
import com.drgarbage.controlflowgraphfactory.ControlFlowFactoryMessages;
import com.drgarbage.controlflowgraphfactory.ControlFlowFactoryPlugin;
import com.drgarbage.controlflowgraphfactory.actions.LayoutAlgorithmsUtils;
import com.drgarbage.controlflowgraphfactory.compare.actions.CompareZoomInAction;
import com.drgarbage.controlflowgraphfactory.compare.actions.CompareZoomOutAction;
import com.drgarbage.visualgraphic.editparts.DiagramEditPartFactory;
import com.drgarbage.visualgraphic.model.Connection;
import com.drgarbage.visualgraphic.model.ControlFlowGraphDiagram;
import com.drgarbage.visualgraphic.model.VertexBase;




/**
 * The graph merge viewer.
 * 
 * @author Sergej Alekseev
 * @version $Revision$
 * $Id$
 */
public class GraphMergeViewer extends ContentMergeViewer {
	
	private static final String BUNDLE_NAME= "org.eclipse.compare.internal.ImageMergeViewerResources"; //$NON-NLS-1$
	
	private ControlFlowGraphDiagram diagramLeft;
	private ControlFlowGraphDiagram diagramRight;

	private GraphicalViewer fLeft;
	private GraphicalViewer fRight;
	
	private IDirectedGraphExt cfgLeft = null;
	private IDirectedGraphExt cfgRight = null;
	
			
	/**
	 * Creates a graph merge viewer.
	 * 
	 * @param parent parent composite
	 * @param styles SWT style
	 * @param mp configuration object
	 */
	public GraphMergeViewer(Composite parent, int styles, CompareConfiguration mp) {
		super(styles, ResourceBundle.getBundle(BUNDLE_NAME), mp); /* default actions */

		buildControl(parent);
		String title= "Graph Compare";  //Utilities.getString(getResourceBundle(), "title"); //$NON-NLS-1$
		getControl().setData(CompareUI.COMPARE_VIEWER_TITLE, title);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.compare.contentmergeviewer.ContentMergeViewer#updateContent(java.lang.Object, java.lang.Object, java.lang.Object)
	 */
	protected void updateContent(Object ancestor, Object left, Object right) {
		if(diagramLeft == null){
			diagramLeft = getControlFlowGraphDiagramFromInput(left);
		}		
		setInput(fLeft, diagramLeft);
		
		
		if(diagramRight == null){
			diagramRight = getControlFlowGraphDiagramFromInput(right);
		}		
		setInput(fRight, diagramRight);
		
		cfgLeft = LayoutAlgorithmsUtils.generateGraph(diagramLeft);		
		cfgRight = LayoutAlgorithmsUtils.generateGraph(diagramRight);

	}

	/**
	 * Reads the Input Object and returns the corresponding ControlFlowGraphDiagram
	 * 
	 * @param input
	 * @return a ControlFlowGraphDiagram representing the Input
	 */
	private ControlFlowGraphDiagram getControlFlowGraphDiagramFromInput(Object input) {
		if (input != null) {
			InputStream stream= null;
			ControlFlowGraphDiagram diagram = null;
			if (input instanceof IStreamContentAccessor) {
				IStreamContentAccessor sca= (IStreamContentAccessor) input;
				if (sca != null) {
					try {
						stream = sca.getContents();
						ObjectInputStream in = new ObjectInputStream(stream);
						diagram = (ControlFlowGraphDiagram) in.readObject();
						in.close();
						stream.close();

					} catch (CoreException ex) {
						// TODO: implement handling
						ex.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
			return diagram;
		}
		
		return null;
	}
	
	/**
	 * Sets the input for the viewer (left or right).
	 * 
	 * @param viewer the graphical viewer
	 * @param input the diagram object
	 */
	private void setInput(GraphicalViewer viewer, ControlFlowGraphDiagram diagram){
		if(viewer != null && diagram != null)
			viewer.setContents(diagram);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.compare.contentmergeviewer.ContentMergeViewer#getContents(boolean)
	 */
	protected byte[] getContents(boolean left) {
		/* We can't modify the contents of right or left side, just return null. */
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.compare.contentmergeviewer.ContentMergeViewer#createControls(org.eclipse.swt.widgets.Composite)
	 */
	public void createControls(Composite composite) {
		
		/* creates the left viewer */
		fLeft = new ScrollingGraphicalViewer();
		fLeft.createControl(composite);
		
		fLeft.getControl().setBackground(ColorConstants.listBackground);
		fLeft.setEditPartFactory(new DiagramEditPartFactory());

		ScalableFreeformRootEditPart root = new ScalableFreeformRootEditPart();
		fLeft.setRootEditPart(root);

		/* creates the right viewer */
		fRight = new ScrollingGraphicalViewer();
		fRight.createControl(composite);
		
		fRight.getControl().setBackground(ColorConstants.listBackground);
		fRight.setEditPartFactory(new DiagramEditPartFactory());

		ScalableFreeformRootEditPart root2 = new ScalableFreeformRootEditPart();
		fRight.setRootEditPart(root2);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.compare.contentmergeviewer.ContentMergeViewer#handleResizeAncestor(int, int, int, int)
	 */
	protected void handleResizeAncestor(int x, int y, int width, int height) {
		/* nothing to do */
	}

	/* (non-Javadoc)
	 * @see org.eclipse.compare.contentmergeviewer.ContentMergeViewer#handleResizeLeftRight(int, int, int, int, int, int)
	 */
	protected void handleResizeLeftRight(int x, int y, int width1, int centerWidth, int width2, int height) {
		fLeft.getControl().setBounds(x, y, width1, height);
		fRight.getControl().setBounds(x+width1+centerWidth, y, width2, height);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.compare.contentmergeviewer.ContentMergeViewer#copy(boolean)
	 */
	protected void copy(boolean leftToRight) {
		if (leftToRight) {			
			diagramRight = diagramLeft;
			setInput(fRight, diagramRight);
			
			setRightDirty(true);
		} else {			
			diagramLeft = diagramRight;
			setInput(fLeft, diagramLeft);
			
			setLeftDirty(true);
		}
	}
	
	private void swap(){
		
		ControlFlowGraphDiagram tmp = null;
		
		tmp = diagramRight;
		
		diagramRight = diagramLeft;
		setInput(fRight, diagramRight);
		setRightDirty(true);
		
		diagramLeft = tmp;
		setInput(fLeft, diagramLeft);
		
		setLeftDirty(true);
	}
	
	protected void clear(){
		
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.compare.contentmergeviewer.ContentMergeViewer#createToolItems(org.eclipse.jface.action.ToolBarManager)
	 */
	protected void createToolItems(ToolBarManager toolBarManager) {
		toolBarManager.add(new Separator());

		//TODO: implement actions for algorithms and move the action implementation into the separate java file.
		IAction a1 = new Action("TD"){ //TODO: define text and icon
			public void run() {
				
				ControlFlowGraphCompare comp = new ControlFlowGraphCompare(cfgLeft, cfgRight);	
				comp.topDownTreeTraversal(cfgLeft);
			}

		};

		IAction a2 = new Action("BBTD"){ //TODO: define text and icon
			public void run() {
				ControlFlowGraphCompare comp = new ControlFlowGraphCompare(cfgLeft, cfgRight);
			}

		};
		
		IAction a3 = new Action("SWAP"){ //TODO: define text and icon
			public void run() {
				swap();
			}
		};
		
		IAction a4 = new Action("CLEAR"){ //TODO: define text and icon
			public void run() {
				clear();
			}
		};
		
		toolBarManager.add(a1);
		toolBarManager.add(a2);
		toolBarManager.add(new Separator());
		toolBarManager.add(a3);
		toolBarManager.add(new Separator());
		toolBarManager.add(a4);
		/* zoom actions */
		toolBarManager.add(new Separator());
		
		ScalableFreeformRootEditPart rootLeft = (ScalableFreeformRootEditPart) fLeft.getRootEditPart();
		ScalableFreeformRootEditPart rootRight = (ScalableFreeformRootEditPart) fRight.getRootEditPart();
		
		CompareZoomInAction zoomIn = new CompareZoomInAction(rootLeft.getZoomManager(), rootRight.getZoomManager());
		zoomIn.setAccelerator(SWT.CTRL | 'I');
		toolBarManager.add(zoomIn);
		
		
		CompareZoomOutAction zoomOut = new CompareZoomOutAction(rootLeft.getZoomManager(), rootRight.getZoomManager());
		zoomOut.setAccelerator(SWT.CTRL | 'O');
		toolBarManager.add(zoomOut);
		
		
	}
}
