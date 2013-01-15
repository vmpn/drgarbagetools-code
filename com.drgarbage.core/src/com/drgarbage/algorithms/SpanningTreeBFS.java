package com.drgarbage.algorithms;

import com.drgarbage.controlflowgraph.ControlFlowGraphException;
import com.drgarbage.controlflowgraph.intf.GraphExtentionFactory;
import com.drgarbage.controlflowgraph.intf.IDirectedGraphExt;
import com.drgarbage.controlflowgraph.intf.IEdgeExt;
import com.drgarbage.controlflowgraph.intf.INodeExt;
import com.drgarbage.controlflowgraph.intf.INodeListExt;

public class SpanningTreeBFS extends BFSBase {
	
	IDirectedGraphExt spanningTree;
	
	public IDirectedGraphExt getSpanningTree(){
		return spanningTree;
	}

	@Override
	protected void visitedNode(INodeExt node) {
		
		
		if(node.isVisited()){
			spanningTree.getNodeList().add(node);
		}
		
	}
	
	@Override
	public void start(IDirectedGraphExt graph) throws ControlFlowGraphException{
		spanningTree = GraphExtentionFactory.createDirectedGraphExtention();
		
		super.start(graph);
	}

	@Override
	protected void visitedEdge(IEdgeExt edge) {
		
		if(!edge.getTarget().isVisited()){
			spanningTree.getEdgeList().add(edge);
		}
		
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
