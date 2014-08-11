package com.drgarbage.algorithms;

import com.drgarbage.controlflowgraph.ControlFlowGraphException;
import com.drgarbage.controlflowgraph.intf.GraphExtentionFactory;
import com.drgarbage.controlflowgraph.intf.IDirectedGraphExt;
import com.drgarbage.controlflowgraph.intf.INodeExt;
import com.drgarbage.controlflowgraph.intf.INodeListExt;


/**
 * Class for Tree Traversal.
 * Combines Post and Pre-order tree traversal methods
 * 
 * @author Artem Garishin, Adam Kajrys
 *
 * @version $Revision$
 * $Id$
 */
public class TreeTraversal {
	
	/**
	 * Traverses a graph in pre-order and returns a node list in that order.
	 * 
	 * @param graph the graph
	 * @return node list in pre-order
	 */
	public static INodeListExt doPreorderTreeListTraversal(IDirectedGraphExt graph) {
		INodeExt root = null;
		for (int i = 0; i < graph.getNodeList().size(); i++) {
			INodeExt n = graph.getNodeList().getNodeExt(i);
			if (n.getIncomingEdgeList().size() == 0) {
				root = n;
			}
		}

		if (root == null) {
			return null;
		}
		
		INodeListExt nodeList = GraphExtentionFactory.createNodeListExtention();
		
		recPreorderSubtreeTraversal(graph, root, nodeList);
		
		return nodeList;
	}
	
	/**
	 * Traverses a graph in pre-order with a given node as root and returns
	 * a node list in that order.
	 * 
	 * @param graph the graph
	 * @param root node where the pre-order traversal starts
	 * @return node list in pre-order
	 */
	public static INodeListExt doPreorderTreeListTraversal(IDirectedGraphExt graph, INodeExt root) {

		if (root == null) {
			return null;
		}
		
		INodeListExt nodeList = GraphExtentionFactory.createNodeListExtention();
		
		recPreorderSubtreeTraversal(graph, root, nodeList);
		
		return nodeList;
	}

	private static void recPreorderSubtreeTraversal(IDirectedGraphExt graph,
			INodeExt node, INodeListExt nodeList) {
		nodeList.add(node);
		
		for(int i = 0; i < node.getOutgoingEdgeList().size(); i++) {
			recPreorderSubtreeTraversal(graph, node.getOutgoingEdgeList().getEdgeExt(i).getTarget(), nodeList);
		}
	}

	/**
	 * Traverses a graph in post-order and returns a node list in that order.
	 * 
	 * @param graph the graph
	 * @return node list in post-order
	 */
	public static INodeListExt doPostorderTreeListTraversal(IDirectedGraphExt graph) {
		INodeExt root = null;
		for (int i = 0; i < graph.getNodeList().size(); i++) {
			INodeExt n = graph.getNodeList().getNodeExt(i);
			if (n.getIncomingEdgeList().size() == 0) {
				root = n;
			}
		}

		if (root == null) {
			return null;
		}
		
		INodeListExt nodeList = GraphExtentionFactory.createNodeListExtention();
		
		recPostorderSubtreeTraversal(graph, root, nodeList);
		
		return nodeList;
	}

	private static void recPostorderSubtreeTraversal(IDirectedGraphExt graph,
			INodeExt node, INodeListExt nodeList) {

		for(int i = 0; i < node.getOutgoingEdgeList().size(); i++) {
			recPostorderSubtreeTraversal(graph, node.getOutgoingEdgeList().getEdgeExt(i).getTarget(), nodeList);
		}
		
		nodeList.add(node);
		
	}
}