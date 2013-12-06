package com.drgarbage.algorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.drgarbage.controlflowgraph.ControlFlowGraphException;
import com.drgarbage.controlflowgraph.intf.GraphExtentionFactory;
import com.drgarbage.controlflowgraph.intf.GraphUtils;
import com.drgarbage.controlflowgraph.intf.IDirectedGraphExt;
import com.drgarbage.controlflowgraph.intf.IEdgeExt;
import com.drgarbage.controlflowgraph.intf.IEdgeListExt;
import com.drgarbage.controlflowgraph.intf.INodeExt;
import com.drgarbage.controlflowgraph.intf.INodeListExt;
import com.drgarbage.controlflowgraph.intf.MarkEnum;

/**
 * Class to compare two ControlFlowGraphs
 * 
 * @author Artem Garishin, Adam Kajrys, Andreas Karoly
 * 
 * @version $Revision$ $Id: ControlFlowGraphCompare.java 444 2013-12-03
 *          21:34:04Z artemgarishin77 $
 */
public class ControlFlowGraphCompare {

	private IDirectedGraphExt cfgLeft = null;
	private IDirectedGraphExt cfgRight = null;
	
	private IEdgeListExt backEdgesCfgLeft = null;
	private IEdgeListExt backEdgesCfgRight = null;
	
	private IDirectedGraphExt cfgLeftSpanningTree = null;
	private IDirectedGraphExt cfgRightSpanningTree = null;
	
	private IDirectedGraphExt basicBlockGraphLeftSpanningTree = null;
	private IDirectedGraphExt basicBlockGraphRightSpanningTree = null;
	
	private INodeExt root1;
	private INodeExt root2;
	
	private int num;

	/**
	 * Constructor
	 * 
	 * @param cfgLeft
	 * @param cfgRight
	 */
	public ControlFlowGraphCompare(IDirectedGraphExt cfgLeft,
			IDirectedGraphExt cfgRight) {
		this.cfgLeft = cfgLeft;
		this.cfgRight = cfgRight;
	}

	/**
	 * Starts the top down ordered subtree isomorphism algorithm
	 * 
	 * @param graphLeft
	 * @param graphRight
	 * @return true if isomorphic subtree was found
	 */
	public boolean topDownOrderedSubtreeIsomorphism(
			IDirectedGraphExt graphLeft, IDirectedGraphExt graphRight) {

		init(graphLeft, graphRight);

		TopDownTreeTraversal tdtt = new TopDownTreeTraversal();

		try {
			tdtt.traverse(cfgLeftSpanningTree, root1);
			tdtt.traverse(cfgRightSpanningTree, root2);

		} catch (ControlFlowGraphException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		if (cfgLeftSpanningTree.getNodeList().size() > cfgRightSpanningTree.getNodeList().size())
			return false;

		INodeExt rootLeft = cfgLeftSpanningTree.getNodeList().getNodeExt(0);
		INodeExt rootRight = cfgRightSpanningTree.getNodeList().getNodeExt(0);

		if (mapOrderedSubtree(rootLeft, rootRight))
			return true;

		return false;

		// TODO: check removed edges
	}

	private boolean mapOrderedSubtree(INodeExt node1, INodeExt node2) {

		if (node1.getCounter() != node2.getCounter())
			return false;

		IEdgeListExt node1OutgoingEdges = node1.getOutgoingEdgeList();
		IEdgeListExt node2OutgoingEdges = node2.getOutgoingEdgeList();

		int node1ChildCount = node1OutgoingEdges.size();
		int node2ChildCount = node2OutgoingEdges.size();

		if (node1ChildCount > node2ChildCount)
			return false;

		INodeExt v1, v2;

		if (node1ChildCount > 0) {

			ArrayList<IEdgeExt> node1SortedEdges = sortEdges(node1OutgoingEdges);
			ArrayList<IEdgeExt> node2SortedEdges = sortEdges(node2OutgoingEdges);

			v1 = node1SortedEdges.get(0).getTarget();
			v2 = node2SortedEdges.get(0).getTarget();

			if (!mapOrderedSubtree(v1, v2))
				return false;

			for (int i = 1; i < node1ChildCount; i++) {
				v1 = node1SortedEdges.get(i).getTarget();
				v2 = node2SortedEdges.get(i).getTarget();

				if (!mapOrderedSubtree(v1, v2))
					return false;
			}
		}

		return true;
	}

	
	/**
	 * Starts the top down unordered subtree isomorphism algorithm
	 * 
	 * @param graphLeft
	 * @param graphRight
	 * @return true if isomorphic subtree was found
	 */
	public boolean topDownUnorderedSubtreeIsomorphism(
			IDirectedGraphExt graphLeft, IDirectedGraphExt graphRight) {
		
		init(graphLeft, graphRight);

		TopDownTreeTraversal tdtt = new TopDownTreeTraversal();

		try {
			tdtt.traverse(cfgLeftSpanningTree, root1);
			tdtt.traverse(cfgRightSpanningTree, root2);

		} catch (ControlFlowGraphException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		/* print spanning trees for debugging */ // TODO remove after debugging is done
		printGraph(cfgLeftSpanningTree, "spanning tree left");
		printGraph(cfgRightSpanningTree, "spanning tree right");

		HashMap<INodeExt, Integer> height1 = new HashMap<INodeExt, Integer>();
		HashMap<INodeExt, Integer> size1 = new HashMap<INodeExt, Integer>();

		HashMap<INodeExt, Integer> height2 = new HashMap<INodeExt, Integer>();
		HashMap<INodeExt, Integer> size2 = new HashMap<INodeExt, Integer>();

		HashMap<INodeExt, HashSet<INodeExt>> b = new HashMap<INodeExt, HashSet<INodeExt>>(); 
		
		for(int i = 0; i < cfgLeftSpanningTree.getNodeList().size(); i++)
			b.put(cfgLeftSpanningTree.getNodeList().getNodeExt(i), new HashSet<INodeExt>());

		computeHeightAndSizeOfNodesInTree(cfgLeftSpanningTree, height1, size1);
		computeHeightAndSizeOfNodesInTree(cfgRightSpanningTree, height2, size2);

		boolean isIsomorph = topDownUnorderedSubtreeIsomorphismRec(
				root1, height1, size1, root2, height2, size2, b);

		HashMap<INodeExt, INodeExt> m = new HashMap<INodeExt, INodeExt>();

		if(isIsomorph) {
			/* reconstruct unordered subtree isomorphism */
			m.put(root1, root2);
			
			List<INodeExt> l = new ArrayList<INodeExt>();
			
			TreeTraversal.preorderTreeListTraversal(cfgLeftSpanningTree, l);
			
			for(INodeExt v : l){
				if(v.getIncomingEdgeList().size() != 0) {
					for(INodeExt w : b.get(v)) {
						INodeExt parentV = v.getIncomingEdgeList().getEdgeExt(0).getSource();
						INodeExt parentW = w.getIncomingEdgeList().getEdgeExt(0).getSource();
						
						if(m.get(parentV) == parentW) {
							m.put(v, w);
							break;
						}
					}
				}
			}
			
		}
		
		setMarksOfNodesInMap(m);

		// TODO: check removed edges
		
		return isIsomorph;
	}

	/**
	 * Computes height and sizes of nodes in tree
	 * 
	 * @param graph the graph
	 * @param height maps a node to its height
	 * @param size maps a node to its size
	 */
	private void computeHeightAndSizeOfNodesInTree(
			IDirectedGraphExt graph,
			HashMap<INodeExt, Integer> height, 
			HashMap<INodeExt, Integer> size) {
		
		List<INodeExt> l = new ArrayList<INodeExt>();

		TreeTraversal.postorderTreeListTraversal(graph, l);

		for (INodeExt v : l) {
			/* leaves have height equal to zero and size equal to one */
			height.put(v, 0);
			size.put(v, 1);

			int childCount = v.getOutgoingEdgeList().size();

			if (childCount != 0) {
				for (int i = 0; i < childCount; i++) {
					INodeExt child = v.getOutgoingEdgeList().getEdgeExt(i)
							.getTarget();

					height.put(v, Math.max(height.get(v), height.get(child)));
					size.put(v, size.get(v) + size.get(child));
				}
				/* one plus the largest height among the children */
				height.put(v, height.get(v) + 1);
			}
		}

	}

	/**
	 * topDownUnorderedSubtreeIsomorphism uses recursion
	 * 
	 * @param node1 node of left graph
	 * @param height1 map which maps nodes of left graph to their height
	 * @param size1 map which maps nodes of left graph to their size
	 * @param node2 node of right graph
	 * @param height2 map which maps nodes of right graph to their height
	 * @param size2 map which maps nodes of right graph to their size
	 * @param b map containing valid combinations of valid bipartite matching pairs
	 * @return true if isomorphic subtree was found
	 * 
	 * @see com.drgarbage.algorithms.ControlFlowGraphCompare#topDownUnorderedSubtreeIsomorphism(IDirectedGraphExt, IDirectedGraphExt)
	 */
	private boolean topDownUnorderedSubtreeIsomorphismRec(
			INodeExt node1,
			HashMap<INodeExt, Integer> height1,
			HashMap<INodeExt, Integer> size1,
			INodeExt node2,
			HashMap<INodeExt, Integer> height2,
			HashMap<INodeExt, Integer> size2,
			HashMap<INodeExt, HashSet<INodeExt>> b) {

//		if (node1.getCounter() != node2.getCounter()) // i don't know why this is in the book
//			return false;

		if (node1.getOutgoingEdgeList().size() == 0)
			return true;

		int node1ChildCount = node1.getOutgoingEdgeList().size();
		int node2ChildCount = node2.getOutgoingEdgeList().size();

		if (node1ChildCount > node2ChildCount
				|| height1.get(node1) > height2.get(node2)
				|| size1.get(node1) > size2.get(node2))
			return false;

		HashMap<INodeExt, INodeExt> nodeMapT1G = new HashMap<INodeExt, INodeExt>();
		HashMap<INodeExt, INodeExt> nodeMapT2G = new HashMap<INodeExt, INodeExt>();

		IDirectedGraphExt g = GraphExtentionFactory.createDirectedGraphExtention();

		HashMap<INodeExt, INodeExt> nodeMapGT = new HashMap<INodeExt, INodeExt>();
		
		List<INodeExt> partition1 = new ArrayList<INodeExt>();
		List<INodeExt> partition2 = new ArrayList<INodeExt>();
		
		
		// TODO remove if debugging is finished
		// ==============================================
//		System.out.println(" ==============================================");
//		IDirectedGraphExt g23 = GraphExtentionFactory.createDirectedGraphExtention();
//		
//		INodeExt v11 = GraphExtentionFactory.createNodeExtention(null); v11.setCounter(111);
//		INodeExt v22 = GraphExtentionFactory.createNodeExtention(null); v22.setCounter(122);
//		
//		INodeExt w11 = GraphExtentionFactory.createNodeExtention(null); w11.setCounter(211);
//		INodeExt w22 = GraphExtentionFactory.createNodeExtention(null); w22.setCounter(222);
//		
//		g23.getNodeList().add(v11);
//		g23.getNodeList().add(v22);
//		g23.getNodeList().add(w11);
//		g23.getNodeList().add(w22);
//		
//		//IEdgeExt e11 = GraphExtentionFactory.createEdgeExtention(v11,w11);
//		IEdgeExt e13 = GraphExtentionFactory.createEdgeExtention(v11,w22);
//		IEdgeExt e22 = GraphExtentionFactory.createEdgeExtention(v22,w22);
//		
//		//g23.getEdgeList().add(e11);
//		g23.getEdgeList().add(e13);
//		g23.getEdgeList().add(e22);
//		
//		partition1.add(v11);
//		partition1.add(v22);
//		
//		partition2.add(w11);
//		partition2.add(w22);
//		
//		MaxCardBipartiteMatching mcbm23 = new MaxCardBipartiteMatching();
//		mcbm23.start(g23, partition1, partition2);
//		
//		List<IEdgeExt> l23 = mcbm23.getMatchingEdgeList(); // = maxCardBipartiteMatching(g);
//		System.out.println("max matchings: " + mcbm23.getMatching() + " - " + l23.size());
//		for(IEdgeExt e : l23) {
//			System.out.println(e.getSource().getCounter() + " " + e.getTarget().getCounter());
//		}
//		System.out.println(" ==============================================");
//		if (true)
//			return false;
		// ==============================================

		for (int i = 0; i < node1ChildCount; i++) {
			INodeExt v = GraphExtentionFactory.createNodeExtention(null);
			INodeExt child = node1.getOutgoingEdgeList().getEdgeExt(i).getTarget();

			v.setCounter(1); /* for debug purposes to distinguish left graph nodes from right graph nodes */
			
			g.getNodeList().add(v);

			nodeMapGT.put(v, child);
			nodeMapT1G.put(child, v);
			
			partition1.add(v);
		}

		for (int i = 0; i < node2ChildCount; i++) {
			INodeExt w = GraphExtentionFactory.createNodeExtention(null);
			INodeExt child = node2.getOutgoingEdgeList().getEdgeExt(i).getTarget();

			w.setCounter(2); /* for debug purposes to distinguish left graph nodes from right graph nodes */
			
			g.getNodeList().add(w);

			nodeMapGT.put(w, child);
			nodeMapT2G.put(child, w);
			
			partition2.add(w);
		}
		
		for (int i = 0; i < node1ChildCount; i++) {
			INodeExt v1 = node1.getOutgoingEdgeList().getEdgeExt(i).getTarget();

			for (int j = 0; j < node2ChildCount; j++) {
				INodeExt v2 = node2.getOutgoingEdgeList().getEdgeExt(j).getTarget();
				
				if (topDownUnorderedSubtreeIsomorphismRec(v1, height1, size1, v2, height2, size2, b)) {
					IEdgeExt edge = GraphExtentionFactory.createEdgeExtention(nodeMapT1G.get(v1), nodeMapT2G.get(v2));
					
					g.getEdgeList().add(edge); 
				}
			}

		}

		MaxCardBipartiteMatching mcbm = new MaxCardBipartiteMatching();
		mcbm.start(g, partition1, partition2);
		
		List<IEdgeExt> l = mcbm.getMatchingEdgeList();
		
		if (l.size() == node1ChildCount) {
			for (IEdgeExt e : l) {
				b.get(nodeMapGT.get(e.getSource())).add(nodeMapGT.get(e.getTarget()));
			}
			return true;
		}

		return false;
	}

	/**
	 * Starts the bottom up unordered subtree isomorphism algorithm
	 * 
	 * @param graphLeft
	 * @param graphRight
	 * @return true if isomorphic subtree was found
	 */
	public boolean bottomUpUnorderedSubtreeIsomorphism(
			IDirectedGraphExt graphLeft, IDirectedGraphExt graphRight) {
		
		init(graphLeft, graphRight);

		BottomUpTreeTraversal butt = new BottomUpTreeTraversal();

		try {
			butt.traverse(cfgLeftSpanningTree, root1);
			butt.traverse(cfgRightSpanningTree, root2);
		} catch (ControlFlowGraphException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/* print spanning trees for debugging */ // TODO remove after debugging is done
		printGraph(cfgLeftSpanningTree, "spanning tree left");
		printGraph(cfgRightSpanningTree, "spanning tree right");
		
		/* maps node to its equivalence class */
		HashMap<INodeExt, Integer> code1 = new HashMap<INodeExt, Integer>();
		HashMap<INodeExt, Integer> code2 = new HashMap<INodeExt, Integer>();

		/* holds nodes in post order */
		List<INodeExt> l1 = new ArrayList<INodeExt>();
		List<INodeExt> l2 = new ArrayList<INodeExt>();

		TreeTraversal.postorderTreeListTraversal(cfgLeftSpanningTree, l1);
		TreeTraversal.postorderTreeListTraversal(cfgRightSpanningTree, l2);

		/* maps a equivalence class to its equivalence class number */
		HashMap<ArrayList<Integer>, Integer> CODE = new HashMap<ArrayList<Integer>, Integer>();

		ArrayList<Integer> l = new ArrayList<Integer>();
		
		/* number of known equivalence classes */
		num = 1;

		isomorphismEquivalenceClassPartition(code1, l1, CODE, l);
		isomorphismEquivalenceClassPartition(code2, l2, CODE, l);

		INodeExt r1 = cfgLeftSpanningTree.getNodeList().getNodeExt(0);
		l.clear();

		HashMap<INodeExt, INodeExt> map = new HashMap<INodeExt, INodeExt>();

		for (INodeExt v : l2) {
			if (code1.get(r1).equals(code2.get(v))) {
				map.put(r1, v);
				mapBottomUpUnorderedSubtree(r1, v, code1, code2, map);
			}
		}

		// TODO remove after debugging is finished 
		System.out.println("map print:");
		for (Entry<INodeExt, INodeExt> entry : map.entrySet()) {
			INodeExt key = entry.getKey();
			INodeExt value = entry.getValue();
			
			System.out.println(key.getCounter() + " -> " + value.getCounter());
		}
		
		setMarksOfNodesInMap(map);

		if (map.size() == cfgLeftSpanningTree.getNodeList().size())
			return true;
		
		// TODO: check removed edges

		return false;
	}

	/**
	 * Partitions a tree in isomorphism equivalence classes
	 * 
	 * @param code map containing the equivalence class of the nodes
	 * @param nodeList list containing nodes of graph in post order
	 * @param CODE maps a equivalence class to its equivalence class number
	 * @param l
	 * @return number of known equivalence classes
	 */
	private int isomorphismEquivalenceClassPartition(
			HashMap<INodeExt, Integer> code, 
			List<INodeExt> nodeList,
			HashMap<ArrayList<Integer>, Integer> CODE, 
			ArrayList<Integer> l) {
		
		for (INodeExt v : nodeList) {
			if (v.getOutgoingEdgeList().size() == 0)
				code.put(v, 1);

			else {
				l.clear();

				for (int i = 0; i < v.getOutgoingEdgeList().size(); i++) {
					INodeExt w = v.getOutgoingEdgeList().getEdgeExt(i)
							.getTarget();

					l.add(code.get(w));
				}

				Collections.sort(l);

				if (CODE.containsKey(l))
					code.put(v, CODE.get(l));

				else {
					CODE.put(l, ++num);
					code.put(v, num);
				}
			}
		}
		return num;
	}

	
	/**
	 * Mapping the nodes of the left graph to equivalent nodes
	 * in the subtree of the right graph
	 * 
	 * @param root1 root of left graph
	 * @param root2 root of right graph
	 * @param code1 map containing the equivalence class of the nodes in the left graph
	 * @param code2 map containing the equivalence class of the nodes in the right graph
	 * @param map map which will be filled in this method
	 */
	private void mapBottomUpUnorderedSubtree(
			INodeExt root1, 
			INodeExt root2,
			HashMap<INodeExt, Integer> code1, 
			HashMap<INodeExt, Integer> code2,
			HashMap<INodeExt, INodeExt> map) {
		
		ArrayList<INodeExt> l = new ArrayList<INodeExt>();

		for (int i = 0; i < root2.getOutgoingEdgeList().size(); i++)
			l.add(root2.getOutgoingEdgeList().getEdgeExt(i).getTarget());

		INodeExt v, w;

		for (int i = 0; i < root1.getOutgoingEdgeList().size(); i++) {
			v = root1.getOutgoingEdgeList().getEdgeExt(i).getTarget();

			Iterator<INodeExt> items = l.iterator();
			while (items.hasNext()) {
				w = items.next();
				if (code1.get(v) == code2.get(w)) {
					map.put(v, w);
					items.remove();
					
					mapBottomUpUnorderedSubtree(v, w, code1, code2, map);
					
					break;
				}
			}
		}

	}

	/**
	 * Initializes variables
	 * 
	 * @param graphLeft
	 * @param graphRight
	 */
	private void init(IDirectedGraphExt graphLeft, IDirectedGraphExt graphRight) {
		backEdgesCfgLeft = removeBackEdges(graphLeft);
		backEdgesCfgRight = removeBackEdges(graphRight);

		cfgLeftSpanningTree = Algorithms.doSpanningTreeAlgorithm(graphLeft, false);
		cfgRightSpanningTree = Algorithms.doSpanningTreeAlgorithm(graphRight, false);
		//TODO switch 2nd parameter to true if you don't want to corrupt cfgLeft and cfgRight graphs,
		// could require map

		/* clear visited flags in nodes and edges */
		GraphUtils.clearGraph(graphLeft);
		GraphUtils.clearGraph(graphRight);

		root1 = cfgLeftSpanningTree.getNodeList().getNodeExt(0);
		root2 = cfgRightSpanningTree.getNodeList().getNodeExt(0);
	}
	
	
	/**
	 * Sorts edges according to the counter of their target
	 * 
	 * @param edgeList list containing edges
	 * @return sorted edge list
	 */
	private ArrayList<IEdgeExt> sortEdges(IEdgeListExt edgeList) {
		TreeMap<Integer, IEdgeExt> tmpEdgeList = new TreeMap<Integer, IEdgeExt>();

		for (int i = 0; i < edgeList.size(); i++) {
			tmpEdgeList.put(edgeList.getEdgeExt(i).getTarget().getCounter(),
					edgeList.getEdgeExt(i));
		}

		return new ArrayList<IEdgeExt>(tmpEdgeList.values());
	}

	/**
	 * Removes all back edges from the edge list and incidence lists of nodes.
	 * 
	 * @param graph the graph
	 */
	private IEdgeListExt removeBackEdges(IDirectedGraphExt graph) {

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
	 * Marks all nodes in map as green
	 * 
	 * @param map map containing nodes
	 * 
	 * @see com.drgarbage.controlflowgraph.intf.INodeExt#setMark(MarkEnum)
	 */
	private void setMarksOfNodesInMap(HashMap<INodeExt, INodeExt> map) {
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
