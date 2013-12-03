package com.drgarbage.algorithms;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.eclipse.draw2d.graph.DirectedGraph;

import com.drgarbage.controlflowgraph.ControlFlowGraphException;
import com.drgarbage.controlflowgraph.intf.GraphUtils;
import com.drgarbage.controlflowgraph.intf.IDirectedGraphExt;
import com.drgarbage.controlflowgraph.intf.IEdgeExt;
import com.drgarbage.controlflowgraph.intf.IEdgeListExt;
import com.drgarbage.controlflowgraph.intf.INodeExt;
import com.drgarbage.controlflowgraph.intf.INodeListExt;

/**
 * Class to compare two ControlFlowGraphs
 * @author Artem Garishin, Adam Kajrys, Andreas Karoly
 *
 * @version $Revision$
 * $Id$
 */
public class ControlFlowGraphCompare {

	private IDirectedGraphExt cfgLeft = null;
	private IDirectedGraphExt cfgRight = null;
	private IEdgeListExt backEdgesCfgLeft = null, backEdgesCfgRight = null;
	private IDirectedGraphExt cfgLeftSpanningTree = null, cfgRightSpanningTree = null; 
	private IDirectedGraphExt basicBlockGraphLeftSpanningTree = null, basicBlockGraphRightSpanningTree = null;

	private int num;

	public ControlFlowGraphCompare(IDirectedGraphExt cfgLeft, IDirectedGraphExt cfgRight){
		this.cfgLeft = cfgLeft;
		this.cfgRight = cfgRight;
	}


	public boolean topDownOrderedSubtreeIsomorphism(IDirectedGraphExt graphLeft, IDirectedGraphExt graphRight) {

		backEdgesCfgLeft = removeBackEdges(graphLeft);
		backEdgesCfgRight = removeBackEdges(graphRight);

		// question: operating on a spanning tree decreases complexity but then we'll also have to check the removed
		// edges. also we have to think about a strategy to remove the edges.
		// see Algorithms.doOrderedSpanningTreeAlgorithm() in old branch
		// for now this is sufficient because we order according to the storing sequence
		cfgLeftSpanningTree = Algorithms.doSpanningTreeAlgorithm(graphLeft, true);
		cfgRightSpanningTree = Algorithms.doSpanningTreeAlgorithm(graphRight, true);

		/* clear visited flags in nodes and edges */
		GraphUtils.clearGraph(graphLeft);
		GraphUtils.clearGraph(graphRight);

		// just for debug purposes, we should be using the spanning tree
		INodeExt graphRoot1 = graphLeft.getNodeList().getNodeExt(0);		
		INodeExt graphRoot2 = graphRight.getNodeList().getNodeExt(0);

		INodeExt root1 = cfgLeftSpanningTree.getNodeList().getNodeExt(0);		
		INodeExt root2 = cfgRightSpanningTree.getNodeList().getNodeExt(0);

		TopDownTreeTraversal tdtt = new TopDownTreeTraversal();

		try {
			tdtt.traverse(graphLeft, graphRoot1);
			tdtt.traverse(graphRight, graphRoot2);

			tdtt.traverse(cfgLeftSpanningTree, root1);
			tdtt.traverse(cfgRightSpanningTree, root2);


		} catch (ControlFlowGraphException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// print method for debugging
		printGraph(graphLeft, "graph left");
		printGraph(cfgLeftSpanningTree, "spanning tree left");
		printGraph(cfgRightSpanningTree, "spanning tree right");


		if(cfgLeftSpanningTree.getNodeList().size() > cfgRightSpanningTree.getNodeList().size())
			return false;

		INodeExt rootLeft = cfgLeftSpanningTree.getNodeList().getNodeExt(0);
		INodeExt rootRight = cfgRightSpanningTree.getNodeList().getNodeExt(0);


		if(mapOrderedSubtree(rootLeft, rootRight))
			return true;

		return false;

		// TODO: check removed edges
	}

	private boolean mapOrderedSubtree(INodeExt node1, INodeExt node2) {

		if(node1.getCounter() != node2.getCounter()) 
			return false;

		IEdgeListExt node1OutgoingEdges = node1.getOutgoingEdgeList();
		IEdgeListExt node2OutgoingEdges = node2.getOutgoingEdgeList();

		int node1ChildCount = node1OutgoingEdges.size();
		int node2ChildCount = node2OutgoingEdges.size();

		if(node1ChildCount > node2ChildCount) 
			return false;

		INodeExt v1, v2;

		if(node1ChildCount > 0) {

			ArrayList<IEdgeExt> node1SortedEdges = sortEdges(node1OutgoingEdges);
			ArrayList<IEdgeExt> node2SortedEdges = sortEdges(node2OutgoingEdges);

			v1 = node1SortedEdges.get(0).getTarget();
			v2 = node2SortedEdges.get(0).getTarget();


			// do we really need sortEdges() in this scenario? we traverse in the storing sequence anyway
			//			v1 = node1OutgoingEdges.getEdgeExt(0).getTarget();
			//			v2 = node2OutgoingEdges.getEdgeExt(0).getTarget();

			if(!mapOrderedSubtree(v1, v2))
				return false;

			for(int i = 1; i < node1ChildCount; i++) {
				v1 = node1SortedEdges.get(i).getTarget();
				v2 = node2SortedEdges.get(i).getTarget();

				//				v1 = node1OutgoingEdges.getEdgeExt(i).getTarget();
				//				v2 = node2OutgoingEdges.getEdgeExt(i).getTarget();

				if(!mapOrderedSubtree(v1, v2))
					return false;
			}
		}

		return true;
	}

	public boolean topDownUnorderedSubtreeIsomorphism(IDirectedGraphExt graphLeft, IDirectedGraphExt graphRight) {
		backEdgesCfgLeft = removeBackEdges(graphLeft);
		backEdgesCfgRight = removeBackEdges(graphRight);

		// question: operating on a spanning tree decreases complexity but then we'll also have to check the removed
		// edges. also we have to think about a strategy to remove the edges.
		// see Algorithms.doOrderedSpanningTreeAlgorithm() in old branch
		// for now this is sufficient because we order according to the storing sequence
		cfgLeftSpanningTree = Algorithms.doSpanningTreeAlgorithm(graphLeft, true);
		cfgRightSpanningTree = Algorithms.doSpanningTreeAlgorithm(graphRight, true);

		/* clear visited flags in nodes and edges */
		GraphUtils.clearGraph(graphLeft);
		GraphUtils.clearGraph(graphRight);

		// just for debug purposes, we should be using the spanning tree
		INodeExt graphRoot1 = graphLeft.getNodeList().getNodeExt(0);		
		INodeExt graphRoot2 = graphRight.getNodeList().getNodeExt(0);

		INodeExt root1 = cfgLeftSpanningTree.getNodeList().getNodeExt(0);		
		INodeExt root2 = cfgRightSpanningTree.getNodeList().getNodeExt(0);

		TopDownTreeTraversal tdtt = new TopDownTreeTraversal();

		try {
			tdtt.traverse(graphLeft, graphRoot1);
			tdtt.traverse(graphRight, graphRoot2);

			tdtt.traverse(cfgLeftSpanningTree, root1);
			tdtt.traverse(cfgRightSpanningTree, root2);


		} catch (ControlFlowGraphException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// print method for debugging
		printGraph(graphLeft, "graph left");
		printGraph(cfgLeftSpanningTree, "spanning tree left");
		printGraph(cfgRightSpanningTree, "spanning tree right");


		INodeExt r1 = cfgLeftSpanningTree.getNodeList().getNodeExt(0);
		INodeExt r2 = cfgRightSpanningTree.getNodeList().getNodeExt(0);

		HashMap<INodeExt, Integer> height1 = new HashMap<INodeExt, Integer>();
		HashMap<INodeExt, Integer> size1 = new HashMap<INodeExt, Integer>();

		HashMap<INodeExt, Integer> height2 = new HashMap<INodeExt, Integer>();
		HashMap<INodeExt, Integer> size2 = new HashMap<INodeExt, Integer>();

		// somekind of other hashmap needed

		computeHeightAndSizeOfNodesInTree(cfgLeftSpanningTree, height1, size1);

		boolean isIsomorph = topDownUnorderedSubtreeIsomorphismRec(r1, height1, size1, r2, height2, size2);

		//if(isIsomorph)
		// TODO: reconstruct unordered subtree isomorphism

		return isIsomorph;
	}

	private void computeHeightAndSizeOfNodesInTree(IDirectedGraphExt graph, HashMap<INodeExt, Integer> height, HashMap<INodeExt, Integer> size) {
		List<INodeExt> l = new ArrayList<INodeExt>();

		TreeTraversal.postOrderTreeListTraversal(graph, l);

		for(INodeExt v : l) {
			/* leaves have height equal to zero and size equal to one */
			height.put(v, 0);
			size.put(v, 1);

			int childCount = v.getOutgoingEdgeList().size();

			if(childCount != 0) {
				for(int i = 0; i < childCount; i++) {
					INodeExt child = v.getOutgoingEdgeList().getEdgeExt(i).getTarget();

					height.put(v, Math.max(height.get(v), height.get(child)));
					size.put(v, size.get(v) + size.get(child));
				}
				/* one plus the largest height among the children */
				height.put(v, height.get(v) + 1);
			}
		}

	}


	/**
	 * @param node1
	 * @param height1
	 * @param size1
	 * @param node2
	 * @param height2
	 * @param size2
	 * @return
	 */
	private boolean topDownUnorderedSubtreeIsomorphismRec(
			INodeExt node1, 
			HashMap<INodeExt, Integer> height1, 
			HashMap<INodeExt, Integer> size1, 
			INodeExt node2, 
			HashMap<INodeExt, Integer> height2, 
			HashMap<INodeExt, Integer> size2) {

		if(node1.getCounter() != node2.getCounter())
			return false;

		if(node1.getOutgoingEdgeList().size() == 0)
			return true;

		int node1ChildCount = node1.getOutgoingEdgeList().size();
		int node2ChildCount = node2.getOutgoingEdgeList().size();

		if(node1ChildCount > node2ChildCount || height1.get(node1) > height2.get(node2) || size1.get(node1) > size2.get(node2))
			return false;

		HashMap<INodeExt, INodeExt> nodeMapT1G = new HashMap<INodeExt, INodeExt>();
		HashMap<INodeExt, INodeExt> nodeMapT2G = new HashMap<INodeExt, INodeExt>();
		
		IDirectedGraphExt g;
		
		HashMap<INodeExt, INodeExt> nodeMapGT = new HashMap<INodeExt, INodeExt>();
		
		for(int i = 0; i < node1ChildCount; i++) {
			// TODO: p 183
		}

		for(int i = 0; i < node2ChildCount; i++) {
			// TODO: p 183
		}
		
		for(int i = 0; i < node1ChildCount; i++) {
			INodeExt v1 = node1.getOutgoingEdgeList().getEdgeExt(i).getTarget();
			
			for(int j = 0; j < node2ChildCount; j++) {
				INodeExt v2 = node2.getOutgoingEdgeList().getEdgeExt(i).getTarget();
				
				if(topDownUnorderedSubtreeIsomorphismRec(v1, height1, size1, v2, height2, size2)) {
					// TODO: add edge to g from T1G[v1] to T2G[v2]
				}
			}
			
		}
		
		// TODO: max card bipartite matching
		
		// TODO: p. 184

		return false;
	}


	public boolean bottomUpUnorderedSubtreeIsomorphism(IDirectedGraphExt graphLeft, IDirectedGraphExt graphRight) {
		backEdgesCfgLeft = removeBackEdges(graphLeft);
		backEdgesCfgRight = removeBackEdges(graphRight);

		cfgLeftSpanningTree = Algorithms.doSpanningTreeAlgorithm(graphLeft, true);
		cfgRightSpanningTree = Algorithms.doSpanningTreeAlgorithm(graphRight, true);

		/* clear visited flags in nodes and edges */
		GraphUtils.clearGraph(graphLeft);
		GraphUtils.clearGraph(graphRight);

		INodeExt root1 = cfgLeftSpanningTree.getNodeList().getNodeExt(0);		
		INodeExt root2 = cfgRightSpanningTree.getNodeList().getNodeExt(0);

		BottomUpTreeTraversal butt = new BottomUpTreeTraversal();

		try {
			butt.traverse(cfgLeftSpanningTree, root1);
			butt.traverse(cfgRightSpanningTree, root2);
		} catch (ControlFlowGraphException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		printGraph(cfgLeftSpanningTree, "spanning tree left");
		printGraph(cfgRightSpanningTree, "spanning tree right");

		/* maps node to its equivalence class */
		HashMap<INodeExt, Integer> code1 = new HashMap<INodeExt, Integer>();
		HashMap<INodeExt, Integer> code2 = new HashMap<INodeExt, Integer>();

		/* holds nodes in post order */
		List<INodeExt> l1 = new ArrayList<INodeExt>();
		List<INodeExt> l2 = new ArrayList<INodeExt>();

		TreeTraversal.postOrderTreeListTraversal(cfgLeftSpanningTree, l1);
		TreeTraversal.postOrderTreeListTraversal(cfgRightSpanningTree, l2);

		/* maps a equivalence class to its equivalence class number */
		HashMap<ArrayList<Integer>, Integer> CODE = new HashMap <ArrayList<Integer>, Integer>();

		ArrayList<Integer> l = new ArrayList<Integer>();
		num = 1; /* number of known equivalence classes */

		isomorphismEquivalenceClassPartition(code1, l1, CODE, l);
		isomorphismEquivalenceClassPartition(code2, l2, CODE, l);


		INodeExt r1 = cfgLeftSpanningTree.getNodeList().getNodeExt(0);
		l.clear();

		HashMap<INodeExt, INodeExt> map = new HashMap<INodeExt, INodeExt>();

		for(INodeExt v : l2) {
			if(code1.get(r1).equals(code2.get(v))) {
				map.put(r1, v);
				mapBottomUpUnorderedSubtree(r1, v, code1, code2, map);
			}
		}

		System.out.println("map print:");
		for (Entry<INodeExt, INodeExt> entry : map.entrySet()) {
			INodeExt key = entry.getKey();
			INodeExt value = entry.getValue();

			System.out.println(key.getCounter() + " -> " + value.getCounter());
		}

		if(map.size() == cfgLeftSpanningTree.getNodeList().size())
			return true;

		return false;
	}

	/**
	 * @param code
	 * @param list
	 * @param CODE
	 * @param l
	 * @return
	 */
	private int isomorphismEquivalenceClassPartition(
			HashMap<INodeExt, Integer> code, List<INodeExt> list,
			HashMap<ArrayList<Integer>, Integer> CODE, ArrayList<Integer> l) {
		for(INodeExt v : list) {
			if(v.getOutgoingEdgeList().size() == 0)
				code.put(v, 1);

			else {
				l.clear();

				for(int i = 0; i < v.getOutgoingEdgeList().size(); i++) {
					INodeExt w = v.getOutgoingEdgeList().getEdgeExt(i).getTarget();

					l.add(code.get(w));
				}

				Collections.sort(l);

				if(CODE.containsKey(l))
					code.put(v, CODE.get(l));

				else {
					CODE.put(l, ++num);
					code.put(v, num);
				}	
			}
		}
		return num;
	}

	private void mapBottomUpUnorderedSubtree(INodeExt r1, INodeExt r2,
			HashMap<INodeExt, Integer> code1, HashMap<INodeExt, Integer> code2, HashMap<INodeExt, INodeExt> m) {
		ArrayList<INodeExt> l2 = new ArrayList<INodeExt>();

		for(int i = 0; i < r2.getOutgoingEdgeList().size(); i++)
			l2.add(r2.getOutgoingEdgeList().getEdgeExt(i).getTarget());

		INodeExt v, w;

		for(int i = 0; i < r1.getOutgoingEdgeList().size(); i++) {
			v = r1.getOutgoingEdgeList().getEdgeExt(i).getTarget();

			Iterator<INodeExt> items = l2.iterator();
			while(items.hasNext()) {
				w = items.next();
				if(code1.get(v).equals(code2.get(w))) {
					m.put(v, w);
					items.remove();
					mapBottomUpUnorderedSubtree(v, w, code1, code2, m);
					break;
				}
			}
		}

	}



	private ArrayList<IEdgeExt> sortEdges(IEdgeListExt edgeList) {
		TreeMap<Integer, IEdgeExt> tmpEdgeList = new TreeMap<Integer, IEdgeExt>();

		for(int i = 0; i < edgeList.size(); i++){
			tmpEdgeList.put(edgeList.getEdgeExt(i).getTarget().getCounter(), edgeList.getEdgeExt(i));
		}

		return new ArrayList<IEdgeExt>(tmpEdgeList.values());
	}

	/**
	 * Removes all back edges from the edge list and 
	 * incidence lists of nodes.
	 * @param graph control flow graph
	 */
	private IEdgeListExt removeBackEdges(IDirectedGraphExt graph){

		IEdgeListExt backEdges = Algorithms.doFindBackEdgesAlgorithm(graph);
		GraphUtils.clearGraph(graph);
		GraphUtils.clearGraphColorMarks(graph);

		IEdgeListExt edges = graph.getEdgeList();
		for(int i = 0; i < backEdges.size(); i++){
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
	 * FOR DEBUG PURPOSES ONLY
	 * @param graph
	 */
	private void artemsDebugPrinter(IDirectedGraphExt graph) {
		//DEBUG PURPOSES
		PrintWriter writer;
		try {
			writer = new PrintWriter("E:/Programms/debug.txt", "UTF-8");
			writer.println("nodes:");
			INodeListExt nodes = graph.getNodeList();
			for(int i = 0; i < nodes.size(); i++){
				writer.println("  " + nodes.getNodeExt(i).getCounter());
				nodes.getNodeExt(i).setLongDescr(Integer.toString(nodes.getNodeExt(i).getCounter()));
				//nodes.getNodeExt(i).set
				writer.println(nodes.getNodeExt(i).getLongDescr());
			}

			writer.println("edges:");

			IEdgeListExt edges = graph.getEdgeList();
			for(int i = 0; i < edges.size(); i++ ){
				writer.println("  " + edges.getEdgeExt(i).getSource().getCounter() 
						+ "->" + edges.getEdgeExt(i).getTarget().getCounter());
			}
			writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//END DEBUG PURPOSES
	}

	/* debug only */
	private void printGraph(IDirectedGraphExt g, String s) {

		System.out.println("\n" + s);

		System.out.println("nodes:");
		for(int i = 0; i < g.getNodeList().size(); i++) {
			System.out.println(g.getNodeList().getNodeExt(i).getCounter());
		}

		System.out.println("edges:");
		for(int i = 0; i < g.getEdgeList().size(); i++) {
			IEdgeExt e = g.getEdgeList().getEdgeExt(i);		
			System.out.println(i + ": " + e.getSource().getCounter() + " -> " + e.getTarget().getCounter());
		}

	}
}
