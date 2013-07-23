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

package com.drgarbage.controlflowgraphfactory.actions;


import org.eclipse.gef.commands.Command;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.actions.RetargetAction;


//import com.drgarbage.algorithms.BFSLayout;
import com.drgarbage.controlflowgraph.intf.IDirectedGraphExt;
import com.drgarbage.controlflowgraphfactory.ControlFlowFactoryMessages;
import com.drgarbage.controlflowgraphfactory.editors.ControlFlowGraphEditor;
import com.drgarbage.controlflowgraphfactory.img.ControlFlowFactoryResource;
import com.drgarbage.utils.Messages;
import com.drgarbage.visualgraphic.commands.LayoutAlgorithmCommand;
import com.drgarbage.visualgraphic.model.ControlFlowGraphDiagram;

/**
 * Action for breadth-first search layouting of the graph.
 *
 * @author Adam Kajrys
 * @version 
 */
public class BFSLayoutAlgorithmAction extends RetargetAction {

	/** Active editor*/
	protected ControlFlowGraphEditor editor = null;

	public static String ID = "com.drgarbage.controlflowgraphfactory.actions.bfslayoutalgorithm";
	private static String text = ControlFlowFactoryMessages.BFSLayoutAlgorithmAction_Text;
	private static String toolTipText = ControlFlowFactoryMessages.BFSLayoutAlgorithmAction_ToolTipText;

	public BFSLayoutAlgorithmAction() {
		super(ID, text);
		setToolTipText(toolTipText);	
		setImageDescriptor(ControlFlowFactoryResource.positioning_bfs_16x16);
		setEnabled(true);
	}
	
	/**
	 * Sets active Editor.
	 * @param editor the editor to set
	 */
	public void setActiveEditor(ControlFlowGraphEditor editor) {
		this.editor = editor;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.actions.RetargetAction#runWithEvent(org.eclipse.swt.widgets.Event)
	 */
	public void runWithEvent(Event event) {
		run();	
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.actions.RetargetAction#run()
	 */
	public void run() {
		if(editor != null){
		/*
			ControlFlowGraphDiagram controlFlowGraphDiagram = editor.getModel();
			IDirectedGraphExt graph = LayoutAlgorithmsUtils.generateGraph(controlFlowGraphDiagram);
			
			new HierarchicalLayout().visit(graph);
			
			Command cmd = new LayoutAlgorithmCommand(graph);
			editor.getControlFlowGraphEditorEditDomain().getCommandStack().execute(cmd);
		*/
		}
		else{
			Messages.error(ControlFlowFactoryMessages.ExecutionFailure);
		}
	}
}
