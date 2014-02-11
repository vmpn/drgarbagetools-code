package com.drgarbage.algorithms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import com.drgarbage.controlflowgraph.intf.GraphExtentionFactory;
import com.drgarbage.controlflowgraph.intf.IDirectedGraphExt;
import com.drgarbage.controlflowgraph.intf.IEdgeExt;
import com.drgarbage.controlflowgraph.intf.INodeExt;

/**
 * Based on the Hopcroft–Karp algorithm.
 * Provides a list containing edges according to the maximum cardinality
 * matching of a bipartite graph
 * 
 * @author Adam Kajrys
 * @version $Revision$
 * 			$Id$
 */
public class HopcroftKarpBipartiteMatching {
	private List<INodeExt> g1;
	private List<INodeExt> g2;
	
	private List<IEdgeExt> matchingEdgeList;
	
	private HashMap<INodeExt, INodeExt> pairG1;
	private HashMap<INodeExt, INodeExt> pairG2;
	
	private HashMap<INodeExt, Integer> dist;
	
	private static final INodeExt NIL = GraphExtentionFactory.createNodeExtention(null);
	private static final int INF = Integer.MAX_VALUE;
	
	private int matching;

	/**
	 * Starts the production of a maximum cardinality matching.
	 * g is the union of partition1 and partition2
	 * 
	 * @param g The graph
	 * @param partition1 The first partition
	 * @param partition2 The second partition
	 */
	public void start(IDirectedGraphExt g, List<INodeExt> partition1, List<INodeExt> partition2) {
		g1 = partition1;
		g2 = partition2;
		
		matchingEdgeList = new ArrayList<IEdgeExt>();
		
		pairG1 = new HashMap<INodeExt, INodeExt>();
		pairG2 = new HashMap<INodeExt, INodeExt>();
		
		dist = new HashMap<INodeExt, Integer>();
		
		for(int i = 0; i < g.getNodeList().size(); i++) {
			INodeExt v = g.getNodeList().getNodeExt(i);
			
			pairG1.put(v, NIL);
			pairG2.put(v, NIL);
		}
		
		System.out.println("g1");
		for(INodeExt n : g1)
			System.out.println(n.getCounter());
		
		System.out.println("g2");
		for(INodeExt n : g2)
			System.out.println(n.getCounter());
		
		matching = 0;
		
		while(BFS()) {
			for(INodeExt v : g1) {
				if(pairG1.get(v) == NIL) {
					if(DFS(v)) {
						matching++;
						
					}
				}
			}
		}
	}

	/**
	 * BFS to set up pairing
	 * 
	 * @return Returns true if the distance value of INF was not changed
	 */
	private boolean BFS() {
		
		Queue<INodeExt> q = new LinkedList<INodeExt>();

		for(INodeExt v : g1) {
			if(pairG1.get(v) == NIL) {
				dist.put(v, 0);
				q.add(v);
			}
			else
				dist.put(v, -1);
		}
		
		dist.put(NIL, INF);
		
		while(!q.isEmpty()) {
			INodeExt v = q.poll();
			
			if(dist.get(v) < dist.get(NIL)) {
				for(int i = 0; i < v.getOutgoingEdgeList().size(); i++) {
					INodeExt u = v.getOutgoingEdgeList().getEdgeExt(i).getTarget();
					
					if(dist.get(pairG2.get(u)) == INF) {
						dist.put(pairG2.get(u), dist.get(v) + 1);
						q.add(pairG2.get(u));
					}
				}
			}
		}
		
		return dist.get(NIL) != INF;
	}
	
	/**
	 * DFS which pairs matching nodes
	 * 
	 * @param v Startnode
	 * @return
	 */
	private boolean DFS(INodeExt v) {

		if(v != NIL) {
			for(int i = 0; i < v.getOutgoingEdgeList().size(); i++) {
				IEdgeExt e = v.getOutgoingEdgeList().getEdgeExt(i);
				INodeExt u = e.getTarget();
				
				if(dist.get(pairG2.get(u)) == dist.get(v)+1) {
					if(DFS(pairG2.get(u))) {
						pairG2.put(u, v);
						pairG1.put(v, u);
						
						matchingEdgeList.add(e);
						
						return true;
					}
				}
			}
			dist.put(v, INF);
			return false;
		}
		return true;
	}

	public List<IEdgeExt> getMatchingEdgeList() {
		return matchingEdgeList;
	}

	public int getMatching() {
		return matching;
	}
}
