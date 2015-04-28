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
import java.util.Map;
import java.util.Queue;
import java.util.Map.Entry;

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
 * @version $Revision$
 * $Id$
 * 
 */
public class BFSLayout extends BFSBase{
	
	private int offset = 34;
	
	private List<INodeExt> branchingNodes = new ArrayList<INodeExt>();
	
	private IDirectedGraphExt graph = null;
	private IDirectedGraphExt spanningTree = null;
	Map<INodeExt, INodeExt> nodeMap;
	 
	
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
		SpanningTreeBFS st = new SpanningTreeBFS();
		st.setCreateNewGraph(true);
		st.start(graph);
		
		spanningTree = st.getSpanningTree();
		nodeMap = st.getMapNodeList();

		/* init all start nodes */
		INodeListExt nodeList = spanningTree.getNodeList();

		if(nodeList.size() == 0){
			throw new ControlFlowGraphException("The node list is empty.");
		}
		
		/* initialize nodes */
		/* first instruction */
		INodeExt node = nodeList.getNodeExt(0);
		node.setX(offset);
		
		int startOffset = offset * 4;
		for(int i = 1; i < nodeList.size(); i++){
			node = nodeList.getNodeExt(i);
			
			/* other start nodes */
			if(node.getIncomingEdgeList().size() == 0){
				node.setX(startOffset);
				startOffset = startOffset + offset * 2;
			 }
		}
	}
	
	/**
	 * Start Method
	 */
	public void visit() {
		try {
			start(spanningTree);
		} catch (ControlFlowGraphException e) {
			CorePlugin.log(e);
		}
		
		/* assign coordinates from nodes in spanning tree to the corresponding nodes in actual graph */
		for (Entry<INodeExt, INodeExt> entry : nodeMap.entrySet()) {
		    INodeExt graphNode = entry.getKey();
		    INodeExt spanningTreeNode = entry.getValue();
		    
		    graphNode.setX(spanningTreeNode.getX());
		    graphNode.setY(spanningTreeNode.getY());
		}
	}
	
	/* (non-Javadoc)
	 * @see com.drgarbage.algorithms.BFSBase#start(com.drgarbage.controlflowgraph.intf.IDirectedGraphExt)
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
		
		
		optimizeLayout(nodeList, edgeList);
	}
	
	/**
	 * Traverses the graph from the given node.
	 * @param start node
	 */
	@Override
	protected void bfs(INodeExt startNode){
		int treeLevel = 1;
		int nodesInNextTreeLevel = 0;
		
		int nodeHeightSum = 0;
		
		Queue<INodeExt> queue = new LinkedList<INodeExt>();
		enqueue(queue, startNode);
		
		visitNode(startNode);
		
		nodeHeightSum += startNode.getHeight();
		
		/* start bfs */
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
					
					targetNode.setY(offset * treeLevel + nodeHeightSum);
					nodesInNextTreeLevel++;
				}
			}
			
			if(queue.size() == nodesInNextTreeLevel) {
				treeLevel++;
				nodesInNextTreeLevel = 0;
				
				int maxHeight = 0;
				
				for(Object n : queue.toArray()) {
					if(((INodeExt)n).getHeight() > maxHeight)
						maxHeight = ((INodeExt)n).getHeight();
				}
				
				nodeHeightSum += maxHeight;	
			}
		}
	}
	
	private int getTreeWidth(INodeExt startnode) {
		int treeWidth = 0;
		int nodesInNextTreeLevel = 0;
		
		Queue<INodeExt> queue = new LinkedList<INodeExt>();
		enqueue(queue, startnode);
		
		startnode.setVisited(true);
		
		/* start bfs */
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

	/* (non-Javadoc)
	 * @see com.drgarbage.algorithms.BFSBase#visitedNode(com.drgarbage.controlflowgraph.intf.INodeExt)
	 */
	@Override
	protected void visitedNode(INodeExt currentNode) {
		/* set x coordinate */
		IEdgeListExt outList = currentNode.getOutgoingEdgeList();
		
		switch(currentNode.getVertexType()) {
			case INodeType.NODE_TYPE_IF:
				branchingNodes.add(currentNode);
				
				if(outList.size() == 2) {
					INodeExt n1 = outList.getEdgeExt(0).getTarget();
					INodeExt n2 = outList.getEdgeExt(1).getTarget();
					
					int maxWidth = n1.getWidth() > n2.getWidth() ? n1.getWidth() : n2.getWidth();
					
					n1.setX(currentNode.getX() + currentNode.getWidth()/2 + offset  + maxWidth/2);
					n2.setX(currentNode.getX() + currentNode.getWidth()/2 - offset - maxWidth/2 - n2.getWidth());
					
					break;
				}
			case INodeType.NODE_TYPE_SWITCH:
				branchingNodes.add(currentNode);
				
				if(outList.size() > 1) {
					positionCasesUnderSwitch(currentNode, outList);
					
					break;
				}
			default:
				if(outList.size() == 1) {
					positionSingleNodeUnderParent(currentNode, outList);
				}
		}
	}

	private void positionCasesUnderSwitch(INodeExt currentNode, IEdgeListExt outList) {
		int caseWidth = 0;
		
		for(int i = 0; i < outList.size(); i++) {
			INodeExt n = outList.getEdgeExt(i).getTarget();

			caseWidth += n.getWidth();
		}
		
		int positionX = currentNode.getX() + currentNode.getWidth()/2 - caseWidth/2 - ((outList.size()-1) * offset)/2;

		for(int i = 0; i < outList.size(); i++) {
			INodeExt n = outList.getEdgeExt(i).getTarget();
			
			n.setX(positionX);
			positionX += offset + n.getWidth();
		}
	}
	
	private void positionSingleNodeUnderParent(INodeExt currentNode,
			IEdgeListExt outList) {
		INodeExt n = outList.getEdgeExt(0).getTarget();
		
		n.setX(currentNode.getX() + currentNode.getWidth()/2 - n.getWidth()/2);
	}
	
	private void optimizeLayout(INodeListExt nodeList, IEdgeListExt edgeList) {
		List<INodeExt> bNodes = new ArrayList<INodeExt>(branchingNodes);
		
		for(INodeExt bNode : bNodes) {
			unvisitAllNodes(nodeList);
			unvisitAllEdges(edgeList);
			
			int treeWidth = getTreeWidth(bNode);
			
			relocateNodes(bNode, treeWidth);
		}
		
		int minX = getMinX(nodeList);
		int xShift = Math.abs(minX) + 10; 
		
		shiftGraphOnXAxis(nodeList, xShift);
	}

	private void shiftGraphOnXAxis(INodeListExt nodeList, int xShift) {
		for(int i = 0; i < nodeList.size(); i++) {
			 nodeList.getNodeExt(i).setX(nodeList.getNodeExt(i).getX() + xShift);
		}
	}

	private int getMinX(INodeListExt nodeList) {
		int minX = 0;
		
		for(int i = 0; i < nodeList.size(); i++) {
			 if(nodeList.getNodeExt(i).getX() < minX)
				 minX = nodeList.getNodeExt(i).getX();
		}
		return minX;
	}

	private void unvisitAllEdges(IEdgeListExt edgeList) {
		for(int i = 0; i < edgeList.size(); i++)
			edgeList.getEdgeExt(i).setVisited(false);
	}

	private void unvisitAllNodes(INodeListExt nodeList) {
		for(int i = 0; i < nodeList.size(); i++)
			nodeList.getNodeExt(i).setVisited(false);
	}

	private void relocateNodes(INodeExt currentNode, int treeWidth) {
		if(treeWidth > 2) {
			IEdgeListExt outList = currentNode.getOutgoingEdgeList();
			
			switch(currentNode.getVertexType()) {
				case INodeType.NODE_TYPE_IF:
					if(outList.size() == 2) {
						INodeExt n1 = outList.getEdgeExt(0).getTarget();
						INodeExt n2 = outList.getEdgeExt(1).getTarget();
						
						int stretch = offset > currentNode.getWidth()/4 ? offset : currentNode.getWidth()/4;
						
						if(n1.getX() < n2.getX()) {
							n1.setX(n1.getX() - stretch * (treeWidth - 2));
							n2.setX(n2.getX() + stretch * (treeWidth - 2));
							
							relocateNodes(n1, treeWidth, -1);
							relocateNodes(n2, treeWidth, 1);
						}
						
						else {
							n1.setX(n1.getX() + stretch * (treeWidth - 2));
							n2.setX(n2.getX() - stretch * (treeWidth - 2));
							
							relocateNodes(n1, treeWidth, 1);
							relocateNodes(n2, treeWidth, -1);
						}
						
						break;
					}
				
				case INodeType.NODE_TYPE_SWITCH:
					if(outList.size() > 1){
						positionCasesUnderSwitch(currentNode, outList);
						
						for(int i = 0; i < outList.size(); i++) {
							INodeExt n = outList.getEdgeExt(i).getTarget();
							relocateNodes(n, treeWidth, 1);
						}
						break;
					}
				default:
					if(outList.size() == 1) {
						positionSingleNodeUnderParent(currentNode, outList);
						
						INodeExt n = outList.getEdgeExt(0).getTarget();
						relocateNodes(n, treeWidth);
					}
			}
		}
	}
	
	private void relocateNodes(INodeExt currentNode, int treeWidth, int factor) {
		if(treeWidth > 2) {
			IEdgeListExt outList = currentNode.getOutgoingEdgeList();
			
			switch(currentNode.getVertexType()) {
				case INodeType.NODE_TYPE_IF:
					if(outList.size() == 2) {
						INodeExt n1 = outList.getEdgeExt(0).getTarget();
						INodeExt n2 = outList.getEdgeExt(1).getTarget();
						
						int stretch = offset > currentNode.getWidth()/4 ? offset : currentNode.getWidth()/4;
		
						n1.setX(n1.getX() + factor * stretch * (treeWidth - 2));
						n2.setX(n2.getX() + factor * stretch * (treeWidth - 2));
						
						relocateNodes(n1, treeWidth, factor);
						relocateNodes(n2, treeWidth, factor);
		
						break;
					}
				
				case INodeType.NODE_TYPE_SWITCH:
					if(outList.size() > 1) {
						positionCasesUnderSwitch(currentNode, outList);
						
						for(int i = 0; i < outList.size(); i++) {
							INodeExt n = outList.getEdgeExt(i).getTarget();	
							relocateNodes(n, treeWidth, 1);
						}
						break;
					}
			
				default:
					if(outList.size() == 1) {
						positionSingleNodeUnderParent(currentNode, outList);
						
						INodeExt n = outList.getEdgeExt(0).getTarget();
						relocateNodes(n, treeWidth, factor);
					}
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.drgarbage.algorithms.BFSBase#visitedEdge(com.drgarbage.controlflowgraph.intf.IEdgeExt)
	 */
	@Override
	protected void visitedEdge(IEdgeExt edge) {
		/* nothing to do */
	}
	
	/* (non-Javadoc)
	 * @see com.drgarbage.algorithms.BFSBase#enqueue(com.drgarbage.controlflowgraph.intf.INodeExt)
	 */
	@Override
	protected void enqueue(INodeExt node) {
		/* nothing to do */
	}
	
	/* (non-Javadoc)
	 * @see com.drgarbage.algorithms.BFSBase#dequeue(com.drgarbage.controlflowgraph.intf.INodeExt)
	 */
	@Override
	protected void dequeue(INodeExt node) {
		/* nothing to do */
	}

}
