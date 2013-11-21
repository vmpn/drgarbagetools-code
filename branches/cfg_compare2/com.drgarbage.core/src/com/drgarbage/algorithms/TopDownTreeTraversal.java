package com.drgarbage.algorithms;

import com.drgarbage.controlflowgraph.ControlFlowGraphException;
import com.drgarbage.controlflowgraph.intf.IDirectedGraphExt;
import com.drgarbage.controlflowgraph.intf.IEdgeExt;
import com.drgarbage.controlflowgraph.intf.INodeExt;

/**
 * Class for top-down traversal of a tree.
 * Uses the nodes counter for labeling. Label corresponds with the order in
 * which the nodes are visited.
 * @author Artem Garishin, Adam Kajrys
 *
 * @version $Revision$
 * $Id$
 */

public class TopDownTreeTraversal extends BFSBase {
	private int ctr = 0;
	
	public void traverse(IDirectedGraphExt graph, INodeExt node) throws ControlFlowGraphException {
		ctr = 0;
		start(graph, node);
	}

	/* (non-Javadoc)
	 * @see com.drgarbage.algorithms.BFSBase#visitedNode(com.drgarbage.controlflowgraph.intf.INodeExt)
	 */
	@Override
	protected void visitedNode(INodeExt node) {
		node.setCounter(ctr++);
		
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
