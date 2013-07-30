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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.TreeMap;

import com.drgarbage.controlflowgraph.ControlFlowGraphException;
import com.drgarbage.controlflowgraph.intf.IDirectedGraphExt;
import com.drgarbage.controlflowgraph.intf.IEdgeExt;
import com.drgarbage.controlflowgraph.intf.IEdgeListExt;
import com.drgarbage.controlflowgraph.intf.INodeExt;
import com.drgarbage.controlflowgraph.intf.INodeListExt;
import com.drgarbage.controlflowgraph.intf.INodeType;
import com.drgarbage.core.CorePlugin;

/**
 * Breadth-First Search layout for placing of nodes.
 * 
 * @author Adam Kajrys
 * @version $Revision: 230 $
 * $Id$
 * 
 */
public class BFSLayout extends BFSBase{
	
	private int offset = 68;
	
	private List<INodeExt> branchingNodes = new ArrayList<INodeExt>();
	
	IDirectedGraphExt graph = null;
	
	/**
	 * Constructor.
	 * @throws ControlFlowGraphException 
	 */
	public BFSLayout(IDirectedGraphExt graph) throws ControlFlowGraphException {
		this.graph = graph;

		initialize();
	}
	
	/**
	 * Constructor.
	 * @throws ControlFlowGraphException 
	 */
	public BFSLayout(IDirectedGraphExt graph, int offset) throws ControlFlowGraphException {
		this.graph = graph;
		this.offset = offset;

		initialize();
	}
	
	private void initialize() throws ControlFlowGraphException{

		/* init all start nodes */
		INodeListExt nodeList = graph.getNodeList();

		if(nodeList.size() == 0){
			throw new ControlFlowGraphException("The node list is empty.");
		}
		
		/* initialize nodes */
		/* first instruction */
		INodeExt node = nodeList.getNodeExt(0);
		node.setX(offset);
		node.setHeight(0); /* initialize height */
		
		int startOffset = offset * 4;
		for(int i = 1; i < nodeList.size(); i++){
			node = nodeList.getNodeExt(i);
			
			node.setHeight(0);/* initialize height */
			
			/* other start nodes */
			if(node.getIncomingEdgeList().size() == 0){
				node.setX(startOffset);
				startOffset = startOffset + offset * 2;
			 }
			else{
				node.setX(-1);
			}
		}
		
		/* get node Properties */
//		INodeListExt nodes = graph.getNodeList();
//		for(int i = 0; i< nodes.size(); i++){
//			node = nodes.getNodeExt(i);
//			
//			/* set size of the node */
//	  		node.setWidth(offset + offset/2);
//	  		if(node.getHeight() == 0){
//	  			node.setHeight(offset/2);	
//	  		}
//		}
		
	}
	
	/**
	 * Start Method
	 */
	public void visit() {
		try {
			start(graph);
		} catch (ControlFlowGraphException e) {
			CorePlugin.log(e);
		}	
	}
	
	/* (non-Javadoc)
	 * @see com.drgarbage.visualgraphic.controlflowgraph.algorithms.DepthFirstSearchBaseVisitor#visit(com.drgarbage.visualgraphic.controlflowgraph.intf.IDirectedGraphExt)
	 */
	@Override	
	public void start(IDirectedGraphExt graph) throws ControlFlowGraphException{
		INodeListExt nodeList = graph.getNodeList();
		IEdgeListExt edgeList = graph.getEdgeList();
		
		if(	nodeList == null || nodeList.size() < 1){
			throw new ControlFlowGraphException("Can't start BFS. Vertex List is empty.");
		}
		
		INodeExt node; 
		for(int i = 0; i < nodeList.size(); i++){
			node = nodeList.getNodeExt(i);

			/* get start nodes */
			if(node.getIncomingEdgeList().size() == 0)
				bfs(node);
		}
		
		
		/* optimize layout, expand in x dimension if tree width > 2 */
		optimizeLayout(nodeList, edgeList);
	}

	private void optimizeLayout(INodeListExt nodeList, IEdgeListExt edgeList) {
		List<INodeExt> bNodes = new ArrayList<INodeExt>(branchingNodes);
		for(INodeExt bNode : bNodes) {
			
			/* unvisit all nodes and edges due to getTreeWidth() uses bfs */
			for(int i = 0; i < nodeList.size(); i++)
				nodeList.getNodeExt(i).setVisited(false);
			
			for(int i = 0; i < edgeList.size(); i++)
				edgeList.getEdgeExt(i).setVisited(false);
			
			
			int treeWidth = getTreeWidth(bNode);
			
			/* unvisit all nodes to see which node was already touched in the relocate process */
			for(int i = 0; i < nodeList.size(); i++)
				nodeList.getNodeExt(i).setVisited(false);
			
			relocateNodes(bNode, treeWidth);
			
			/* move the graph to the right if some x coordinates got negative values in the process */
			int x = 0;
			
			for(int i = 0; i < nodeList.size(); i++) {
				 if(nodeList.getNodeExt(i).getX() < x)
					 x = nodeList.getNodeExt(i).getX();
			}
			
			x = Math.abs(x) + 5; 
			for(int i = 0; i < nodeList.size(); i++) {
				 nodeList.getNodeExt(i).setX(nodeList.getNodeExt(i).getX() + x);
			}
		}
	}

	private void relocateNodes(INodeExt bNode, int treeWidth) {
		if(treeWidth > 2) {
			if(bNode.getVertexType() == INodeType.NODE_TYPE_IF) {
				IEdgeListExt outList = bNode.getOutgoingEdgeList();
				
				IEdgeExt e1 = outList.getEdgeExt(0);
				IEdgeExt e2 = outList.getEdgeExt(1);
				
				if(e1.getTarget().getX() < e2.getTarget().getX()) {
					e1.getTarget().setX(e1.getTarget().getX() - offset * (treeWidth - 2));
					e2.getTarget().setX(e2.getTarget().getX() + offset * (treeWidth - 2));
					
					e1.getTarget().setVisited(true);
					e2.getTarget().setVisited(true);
					
					relocateNodes(e1.getTarget(), treeWidth, -1);
					relocateNodes(e2.getTarget(), treeWidth, 1);
				}
				
				else {
					e1.getTarget().setX(e1.getTarget().getX() + offset * (treeWidth - 2));
					e2.getTarget().setX(e2.getTarget().getX() - offset * (treeWidth - 2));
					
					e1.getTarget().setVisited(true);
					e2.getTarget().setVisited(true);
					
					relocateNodes(e1.getTarget(), treeWidth, 1);
					relocateNodes(e2.getTarget(), treeWidth, -1);
				}
			}
		}
	}
	
	private void relocateNodes(INodeExt bNode, int treeWidth, int factor) {
		if(treeWidth > 2) { System.out.println(factor);
			if(bNode.getVertexType() == INodeType.NODE_TYPE_IF) {
				IEdgeListExt outList = bNode.getOutgoingEdgeList();
				
				IEdgeExt e1 = outList.getEdgeExt(0);
				IEdgeExt e2 = outList.getEdgeExt(1);

				if(!e1.getTarget().isVisited())
					e1.getTarget().setX(e1.getTarget().getX() + factor * offset * (treeWidth - 2));
				if(!e2.getTarget().isVisited())
					e2.getTarget().setX(e2.getTarget().getX() + factor * offset * (treeWidth - 2));
				
				e1.getTarget().setVisited(true);
				e2.getTarget().setVisited(true);
				
				relocateNodes(e1.getTarget(), treeWidth, factor);
				relocateNodes(e2.getTarget(), treeWidth, factor);

			}
			
			else if(bNode.getOutgoingEdgeList().size() == 1) {
				IEdgeExt e = bNode.getOutgoingEdgeList().getEdgeExt(0);
				
				if(!e.getTarget().isVisited()) {
					e.getTarget().setX(bNode.getX());
					
					e.getTarget().setVisited(true);
					
					relocateNodes(e.getTarget(), treeWidth, factor);
				}
			}
		}
	}
	
	
	/**
	 * Traverses the graph from the given node.
	 * @param start node
	 */
	@Override
	protected void bfs(INodeExt startnode){
		int treeLevel = 1;
		int nodesInNextTreeLevel = 0;
		
		Queue<INodeExt> queue = new LinkedList<INodeExt>();
		enqueue(queue, startnode);
		
		visitNode(startnode);
		
		while(!queue.isEmpty()){
			INodeExt node = dequeue(queue);
			IEdgeListExt outgoingEdges = node.getOutgoingEdgeList();
			for(int i = 0; i < outgoingEdges.size(); i++){
				IEdgeExt edge = outgoingEdges.getEdgeExt(i);
				if(edge.isVisited()){
					continue;
				}
				
				visitEdge(edge);
				
				INodeExt targetNode = edge.getTarget();
				if(!targetNode.isVisited()){
					enqueue(queue, targetNode);
					visitNode(targetNode);
					
					targetNode.setY(offset * treeLevel);
					nodesInNextTreeLevel++;
				}
			}
			
			if(queue.size() == nodesInNextTreeLevel) {
				treeLevel++;
				nodesInNextTreeLevel = 0;
			}
		}
	}
	
	private int getTreeWidth(INodeExt startnode) {
		int treeWidth = 0;
		int nodesInNextTreeLevel = 0;
		
		Queue<INodeExt> queue = new LinkedList<INodeExt>();
		enqueue(queue, startnode);
		
		startnode.setVisited(true);
		
		while(!queue.isEmpty()){
			INodeExt node = dequeue(queue);
			IEdgeListExt outgoingEdges = node.getOutgoingEdgeList();
			for(int i = 0; i < outgoingEdges.size(); i++){
				IEdgeExt edge = outgoingEdges.getEdgeExt(i);
				if(edge.isVisited()){
					continue;
				}
				
				visitEdge(edge);
				
				INodeExt targetNode = edge.getTarget();
				if(!targetNode.isVisited()){
					enqueue(queue, targetNode);
					targetNode.setVisited(true);
					
					nodesInNextTreeLevel++;
				}
			}
			
			if(queue.size() == nodesInNextTreeLevel) {
				if(nodesInNextTreeLevel > treeWidth)
					treeWidth = nodesInNextTreeLevel;
				
				nodesInNextTreeLevel = 0;
			}
		}
		
		return treeWidth;
	}

	
	@Override
	protected void visitedNode(INodeExt node) {
		/* set x coordinate */
		IEdgeListExt outList = node.getOutgoingEdgeList();
		
		switch(node.getVertexType()) {
			case INodeType.NODE_TYPE_IF:
				branchingNodes.add(node);
				
				IEdgeExt e1 = outList.getEdgeExt(0);
				IEdgeExt e2 = outList.getEdgeExt(1);

				if(e1.getTarget().getX() == -1 && e2.getTarget().getX() == -1){
					e1.getTarget().setX(node.getX() + offset);
					e2.getTarget().setX(node.getX() - offset);
				}
				/* if the x coordinate is not -1, then the node was already visited
				 * the target node can be positioned right under the if type node */
				else if(e1.getTarget().getX() != -1 && e2.getTarget().getX() == -1){
						e2.getTarget().setX(node.getX());
				}
				else if(e1.getTarget().getX() == -1 && e2.getTarget().getX() != -1){
						e1.getTarget().setX(node.getX());
				}
				break;
			case INodeType.NODE_TYPE_SWITCH:
				branchingNodes.add(node);
				/* sort nodes by y coordinate */
             	TreeMap<Object, INodeExt> tm = new TreeMap<Object, INodeExt>();
        		IEdgeExt e = null;
				for (int j = 0; j < outList.size(); j++){			 
					 e = outList.getEdgeExt(j);
					 if(!e.getTarget().isVisited() && e.getTarget().getX() == -1){
						 tm.put(new Integer(e.getTarget().getByteCodeOffset()), e.getTarget()); 
					 }
				}

				Object[] array = tm.values().toArray();
				
				int positionX = node.getX() - (tm.size() * offset * 2)/2;
				
				for(int j = 0; j < array.length; j++){
					positionX = positionX + (offset * 2);
					((INodeExt)array[j]).setX(positionX);
				}	

			default:
				if( outList.size() > 0){
					e = outList.getEdgeExt(0);
					if(!e.getTarget().isVisited() && e.getTarget().getX() == -1){
						e.getTarget().setX(node.getX());
					}
				}
		}
	}
	

	@Override
	protected void visitedEdge(IEdgeExt edge) {
		/* nothing to do */
	}
	
	@Override
	protected void enqueue(INodeExt node) {
		/* nothing to do */
	}
	
	@Override
	protected void dequeue(INodeExt node) {
		/* nothing to do */
	}

}
