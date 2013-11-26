package com.drgarbage.algorithms;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import com.drgarbage.controlflowgraph.ControlFlowGraphException;
import com.drgarbage.controlflowgraph.intf.IDirectedGraphExt;
import com.drgarbage.controlflowgraph.intf.IEdgeExt;
import com.drgarbage.controlflowgraph.intf.INodeExt;

/**
 * Class for bottom-up traversal of a tree.
 * Uses the nodes counter for labeling. Label corresponds with the order in
 * which the nodes are visited.
 * @author Artem Garishin, Adam Kajrys
 *
 * @version $Revision$
 * $Id$
 */

public class BottomUpTreeTraversal extends BFSBase {
	private Queue<INodeExt> leafQueue = new LinkedList<INodeExt>();
	
	public void traverse(IDirectedGraphExt graph, INodeExt root) throws ControlFlowGraphException {
		int ctr = 0;
		HashMap<INodeExt, Integer> children = new HashMap<INodeExt, Integer>();
		
		for(int i = 0; i < graph.getNodeList().size(); i++) {
			INodeExt node = graph.getNodeList().getNodeExt(i);
			int childcount = node.getOutgoingEdgeList().size();
			
			children.put(node, childcount);
		}
		
		start(graph, root);
		
		while(!leafQueue.isEmpty()) {
			INodeExt node = leafQueue.poll();
			
			node.setCounter(ctr++);
			
			if(!node.equals(root)) {
				INodeExt parent = node.getIncomingEdgeList().getEdgeExt(0).getSource();
				
				children.put(parent, children.get(parent) - 1);
				
				if(children.get(parent).intValue() == 0)
					leafQueue.add(parent);
			}	
		}
	}

	@Override
	protected void visitedNode(INodeExt node) {
		if(node.getOutgoingEdgeList().size() == 0)
			leafQueue.add(node);
		
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
