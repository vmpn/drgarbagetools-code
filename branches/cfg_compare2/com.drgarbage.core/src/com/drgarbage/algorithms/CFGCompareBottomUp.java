package com.drgarbage.algorithms;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import com.drgarbage.controlflowgraph.intf.GraphUtils;
import com.drgarbage.controlflowgraph.intf.IDirectedGraphExt;
import com.drgarbage.controlflowgraph.intf.IEdgeExt;
import com.drgarbage.controlflowgraph.intf.IEdgeListExt;
import com.drgarbage.controlflowgraph.intf.INodeExt;
import com.drgarbage.controlflowgraph.intf.INodeListExt;
import com.drgarbage.controlflowgraph.intf.MarkEnum;


/**
 * Class to compare two ControlFlowGraphs using BottomUp algorithms
 * 
 * @author Artem Garishin, Adam Kajrys
 * 
 * @version $Revision$ $Id$
 */
public class CFGCompareBottomUp {
	
	/**
	 * transform the graphs into trees and call 
	 * bottomUpUnorderedSubtreeIsomorphism for further compare
	 * 
	 * @param graphLeft
	 * @param graphRight
	 */
	public static void compareGraphsBottomUp(
			IDirectedGraphExt graphLeft, IDirectedGraphExt graphRight) {

		IEdgeListExt backEdgesCfgLeft = removeBackEdges(graphLeft);
		IEdgeListExt backEdgesCfgRight = removeBackEdges(graphRight);

		IDirectedGraphExt cfgLeftSpanningTree = Algorithms.doSpanningTreeAlgorithm(graphLeft, false);
		IDirectedGraphExt cfgRightSpanningTree = Algorithms.doSpanningTreeAlgorithm(graphRight, false);
		
		/* clear visited flags in nodes and edges */
		GraphUtils.clearGraph(graphLeft);
		GraphUtils.clearGraph(graphRight);
		
		bottomUpUnorderedSubtreeIsomorphism(cfgLeftSpanningTree, cfgRightSpanningTree);
	}
	
	/**
	 * compares two trees using bottom-up subtree isomorphism
	 * 
	 * @param cfgLeftSpanningTree
	 * @param cfgRightSpanningTree
	 */
	public static void bottomUpUnorderedSubtreeIsomorphism(
			IDirectedGraphExt cfgLeftSpanningTree, IDirectedGraphExt cfgRightSpanningTree) {
		
		/* holds nodes in post order */
		INodeListExt l1 = TreeTraversal.doPostorderTreeListTraversal(cfgLeftSpanningTree);
		INodeListExt l2 = TreeTraversal.doPostorderTreeListTraversal(cfgRightSpanningTree);
		
		/* maps node to its equivalence class */
		HashMap<INodeExt, Integer> code1 = isomorphismEquivalenceClassPartition(l1);
		HashMap<INodeExt, Integer> code2 = isomorphismEquivalenceClassPartition(l2);

		INodeExt root1 = cfgLeftSpanningTree.getNodeList().getNodeExt(0);

		/* maps nodes in the left graph to nodes in the right graph */
		HashMap<INodeExt, INodeExt> map = new HashMap<INodeExt, INodeExt>();

		for (int i = 0; i < l2.size(); i++) {
			INodeExt v = l2.getNodeExt(i);
			
			if (code1.get(root1).equals(code2.get(v))) {
				map.put(root1, v);
				map.putAll(mapBottomUpUnorderedSubtree(root1, v, code1, code2));
			}
		}
		setMarksOfNodesInMap(map);
		if (map.size() == cfgLeftSpanningTree.getNodeList().size()) {
			System.out.println("trees are isomorph");
		}
		else {
			System.out.println("trees are not isomorph");
		}
		
		// TODO: check removed edges(do we need them?)
	}

	/**
	 * Partitions a tree in isomorphism equivalence classes
	 * 
	 * @param code maps a node to its equivalence class
	 * @param nodeList list containing nodes of graph in post order
	 * @param CODE maps equivalence class of children nodes (list) to equivalence class of parent
	 * @return code equivalence class
	 */
	private static HashMap<INodeExt, Integer>  isomorphismEquivalenceClassPartition(INodeListExt nodeList) {
		
		/* maps node to its equivalence class */
		HashMap<INodeExt, Integer> code = new HashMap<INodeExt, Integer>();
		
		/* maps a equivalence class to its equivalence class number */
		HashMap<ArrayList<Integer>, Integer> CODE = new HashMap<ArrayList<Integer>, Integer>();
		
		int num = 1;
		for (int i = 0; i < nodeList.size(); i++) {
			INodeExt v = nodeList.getNodeExt(i);
			
			/* all leaves have equivalence class of 1 */
			if (v.getOutgoingEdgeList().size() == 0) {
				code.put(v, 1);
			}

			else {
				ArrayList<Integer> l = new ArrayList<Integer>();

				for (int j = 0; j < v.getOutgoingEdgeList().size(); j++) {
					INodeExt w = v.getOutgoingEdgeList().getEdgeExt(j).getTarget();

					/* fill list with equivalence classes of children nodes */
					l.add(code.get(w));
				}
				
				Collections.sort(l);
				
				/* if a node with the same equivalence classes of children nodes already exists
				 * assign the same equivalence class to that node */
				if (CODE.containsKey(l)) {
					code.put(v, CODE.get(l));
				}

				/* else: create new equivalence class */
				else {
					CODE.put(l, ++num);
					code.put(v, num);
				}
			}
		}
		
		return code;
	}

	
	/**
	 * Mapping the nodes of the left graph to equivalent nodes
	 * in the subtree of the right graph
	 * 
	 * @param node1 node in the left graph
	 * @param node2 node in the right graph
	 * @param code1 map containing the equivalence class of the nodes in the left graph
	 * @param code2 map containing the equivalence class of the nodes in the right graph
	 * @return key is a node in the left graph, value the equivalent node in the subtree of the right graph
	 */
	private static HashMap<INodeExt, INodeExt> mapBottomUpUnorderedSubtree(
			INodeExt node1, 
			INodeExt node2,
			HashMap<INodeExt, Integer> code1, 
			HashMap<INodeExt, Integer> code2) {
		
		HashMap<INodeExt, INodeExt> map = new HashMap<INodeExt, INodeExt>();
		ArrayList<INodeExt> l = new ArrayList<INodeExt>();

		for (int i = 0; i < node2.getOutgoingEdgeList().size(); i++) {
			l.add(node2.getOutgoingEdgeList().getEdgeExt(i).getTarget());
		}

		INodeExt v, w;

		for (int i = 0; i < node1.getOutgoingEdgeList().size(); i++) {
			v = node1.getOutgoingEdgeList().getEdgeExt(i).getTarget();

			Iterator<INodeExt> items = l.iterator();
			while (items.hasNext()) {
				w = items.next();
				if (code1.get(v) == code2.get(w)) {
					map.put(v, w);
					items.remove();
					
					map.putAll(mapBottomUpUnorderedSubtree(v, w, code1, code2));
										
					break;
				}
			}
		}
		return map;

	}	
	

	/**
	 * Removes all back edges from the edge list and incidence lists of nodes.
	 * 
	 * @param graph the graph
	 * @return backEdges
	 */
	private static IEdgeListExt removeBackEdges(IDirectedGraphExt graph) {

		IEdgeListExt backEdges = Algorithms.doFindBackEdgesAlgorithm(graph);
		GraphUtils.clearGraph(graph);

		IEdgeListExt edges = graph.getEdgeList();
		for (int i = 0; i < backEdges.size(); i++) {
			IEdgeExt e = backEdges.getEdgeExt(i);
			INodeExt source = e.getSource();
			INodeExt target = e.getTarget();

			source.getOutgoingEdgeList().remove(e);
			target.getIncomingEdgeList().remove(e);
			edges.remove(e);
		}

		return backEdges;
	}
	
	/**
	 * Sets properties SetMark as GREEN according to input parameter map
	 * The marked nodes will be in visualization green colored
	 * @param map map containing nodes
	 * @see com.drgarbage.controlflowgraph.intf.INodeExt#setMark(MarkEnum)
	 */
	private static void setMarksOfNodesInMap(HashMap<INodeExt, INodeExt> map) {
		for (Entry<INodeExt, INodeExt> entry : map.entrySet()) {
			INodeExt key = entry.getKey();
			INodeExt value = entry.getValue();
						
			key.setMark(MarkEnum.GREEN);
			value.setMark(MarkEnum.GREEN);
		}
	}

	
	/**
	 * prints graph: lists counter of nodes, and source and target of edges
	 * for debug purposes only.
	 * 
	 * @param g the graph
	 * @param s name of graph
	 */
	private void printGraph(IDirectedGraphExt g, String s) {

		System.out.println("\n" + s);

		System.out.println("nodes:");
		for (int i = 0; i < g.getNodeList().size(); i++) {
			System.out.println(g.getNodeList().getNodeExt(i).getCounter());
		}

		System.out.println("edges:");
		for (int i = 0; i < g.getEdgeList().size(); i++) {
			IEdgeExt e = g.getEdgeList().getEdgeExt(i);
			System.out.println(i + ": " + e.getSource().getCounter() + " -> "
					+ e.getTarget().getCounter());
		}

	}

}
