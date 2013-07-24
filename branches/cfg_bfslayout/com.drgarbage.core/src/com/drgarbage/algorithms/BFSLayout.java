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

package com.drgarbage.algorithms;

import org.eclipse.draw2d.graph.DirectedGraph;
import org.eclipse.draw2d.graph.DirectedGraphLayout;
import org.eclipse.draw2d.graph.EdgeList;
import org.eclipse.draw2d.graph.NodeList;

import com.drgarbage.controlflowgraph.ControlFlowGraphException;
import com.drgarbage.controlflowgraph.intf.IDirectedGraphExt;
import com.drgarbage.controlflowgraph.intf.IEdgeExt;
import com.drgarbage.controlflowgraph.intf.IEdgeListExt;
import com.drgarbage.controlflowgraph.intf.INodeExt;
import com.drgarbage.controlflowgraph.intf.INodeListExt;
import com.drgarbage.core.CorePlugin;

/**
 * Breadth-First Search layout for placing of nodes.
 * 
 * @author Adam Kajrys
 *
 */
public class BFSLayout extends BFSBase{
	
	public BFSLayout() {
		
	}
	
	/**
	 * Start Method
	 */
	public void visit(IDirectedGraphExt graph) {
		//Convert graph
		DirectedGraph graph2 = new DirectedGraph();
		
		INodeListExt nodeList = graph.getNodeList();
		IEdgeListExt edgeList = graph.getEdgeList();
		graph2.nodes = (NodeList) nodeList;
		graph2.edges = (EdgeList) edgeList;
		
		//Set Layout
		try {
			start(graph);
		} catch (ControlFlowGraphException e) {
			CorePlugin.log(e);
		}
	}

	
	@Override
	protected void visitedNode(INodeExt node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void visitedEdge(IEdgeExt edge) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void enqueue(INodeExt node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void dequeue(INodeExt node) {
		// TODO Auto-generated method stub
		
	}

}
