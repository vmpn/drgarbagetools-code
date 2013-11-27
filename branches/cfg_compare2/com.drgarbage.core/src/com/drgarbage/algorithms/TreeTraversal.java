package com.drgarbage.algorithms;

import java.util.List;

import com.drgarbage.controlflowgraph.intf.IDirectedGraphExt;
import com.drgarbage.controlflowgraph.intf.INodeExt;


/**
 * Class for Tree Traversal traversal of a tree.
 * Combines Post and Preorder tree traversal methods
 * @author Artem Garishin, Adam Kajrys
 *
 * @version $Revision$
 * $Id$
 */
public class TreeTraversal {

	public static void postOrderTreeListTraversal(IDirectedGraphExt graph, List<INodeExt> l) {
		INodeExt root = graph.getNodeList().getNodeExt(0);
		
		recPostorderSubtreeTraversal(graph, root, l);
	}

	private static void recPostorderSubtreeTraversal(IDirectedGraphExt graph,
			INodeExt node, List<INodeExt> l) {

		for(int i = 0; i < node.getOutgoingEdgeList().size(); i++) {
			recPostorderSubtreeTraversal(graph, node.getOutgoingEdgeList().getEdgeExt(i).getTarget(), l);
		}
		
		l.add(node);
		
	}
}
