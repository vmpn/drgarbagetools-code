package com.drgarbage.algorithms;

import com.drgarbage.controlflowgraph.intf.IEdgeExt;
import com.drgarbage.controlflowgraph.intf.INodeExt;

public class TopDownTreeTraversal extends BFSBase {

	@Override
	protected void visitedNode(INodeExt node) {
		node.incrementCounter();
		
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
