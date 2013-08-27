package com.drgarbage.algorithms;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.TreeMap;

import com.drgarbage.controlflowgraph.ControlFlowGraphException;
import com.drgarbage.controlflowgraph.intf.IDirectedGraphExt;
import com.drgarbage.controlflowgraph.intf.IEdgeExt;
import com.drgarbage.controlflowgraph.intf.IEdgeListExt;
import com.drgarbage.controlflowgraph.intf.INodeExt;

public class BFSOrderedTraversal extends BFSBase {
	
	private ArrayList<INodeExt> orderedNodes;
	
	@Override
	/**
	 * Start bfs from the given node.
	 * @param graph the graph
	 * @param start the start node
	 * @throws ControlFlowGraphException
	 */
	protected void start(IDirectedGraphExt graph, INodeExt start) throws ControlFlowGraphException{
		if(!graph.getNodeList().contains(start)){
			throw new ControlFlowGraphException("Can't start BFS. Start Vertex '" + start.toString()+ "' not found." );
		}

		if(debug)log("Start node: " + start.toString() + " " + start.getData() + " " + start.getOutgoingEdgeList().toString());
		
		orderedNodes = new ArrayList<INodeExt>();
		
		bfs(start);
	}
	
	/**
	 * Traverses the graph from the given node.
	 * @param start node
	 */
	@Override
	protected void bfs(INodeExt startnode){
		Queue<INodeExt> queue = new LinkedList<INodeExt>();
		enqueue(queue, startnode);
		
		visitNode(startnode);
		
		while(!queue.isEmpty()){
			INodeExt node = dequeue(queue);
			IEdgeListExt outgoingEdges = node.getOutgoingEdgeList();
			
			TreeMap<Integer, IEdgeExt> orderedEdges = new TreeMap<Integer, IEdgeExt>();
			
			for(int i = 0; i < outgoingEdges.size(); i++){
				IEdgeExt edge = outgoingEdges.getEdgeExt(i);
				
				orderedEdges.put(edge.getTarget().getByteCodeOffset(), edge);
			}
			
			Iterator<IEdgeExt> it = orderedEdges.values().iterator();
			while(it.hasNext()){
				IEdgeExt edge = it.next();
				
				if(edge.isVisited()){
					continue;
				}
				visitEdge(edge);
				
				System.out.println("Visited Edge: " +edge.getSource().getByteCodeOffset()+" -> "+ edge.getTarget().getByteCodeOffset());
				
				INodeExt targetNode = edge.getTarget();
				if(!targetNode.isVisited()){
					enqueue(queue, targetNode);
					visitNode(targetNode);
				}
			}
		}
	}
	
	public ArrayList<INodeExt> getOrderedNodes(){
		return orderedNodes;
	}

	@Override
	protected void visitedNode(INodeExt node) {
		orderedNodes.add(node);
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
