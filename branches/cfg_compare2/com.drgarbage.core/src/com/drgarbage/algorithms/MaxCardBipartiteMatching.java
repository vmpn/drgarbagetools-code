/**
 * Copyright (c) 2008-2014, Dr. Garbage Community
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.drgarbage.algorithms;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.drgarbage.controlflowgraph.intf.GraphUtils;
import com.drgarbage.controlflowgraph.intf.IDirectedGraphExt;
import com.drgarbage.controlflowgraph.intf.IEdgeExt;
import com.drgarbage.controlflowgraph.intf.IEdgeListExt;
import com.drgarbage.controlflowgraph.intf.INodeExt;
import com.drgarbage.controlflowgraph.intf.MarkEnum;

/**
 * The general idea of the algorithm for construction of a maximum matching 
 * <code>M</code> for the bipartite graph <code>G = (A + B, E)</code> 
 * is presented in the following figure:
 * <pre> 
 *  Maximum Bipartite Matching
 *    M = 0
 *    While ( exists an augmenting path p)
 *       M = M + (switch) p
 *    return M
 * </pre>
 *
 * A path <code>p</code> is called an augmenting path for the 
 * matching <code>M</code> if: <br>
 * 1. The two end points of <code>p</code> are unmatched by <code>M</code>. <br>
 * 2. The edges of <code>p</code> alternate between edges in <code>M</code> 
 * and edges not in <code>M</code>.<br>
 * 
 * If <code>M</code> has an augmenting path <code>p</code> then switching the edges
 * along the path <code>p</code> from in-to-out of M gives
 * a matching with one more edge.
 *
 * The current implementation of the algorithm is presented in the following figure:  
 * <pre>
 * Maximum Bipartite Matching
 * 1.  Start DFS for all vertices in A.
 * 2.  Visit in alternating order an edge e in M and e not in M 
 * 3.  If at any point the DFS visits an unmatched vertex from B 
 *     then augmenting path p is found.
 * 3.1   Stop DFS and switch the path p 
 * </pre>
 * 
 * Complexity of the algorithm is <code>O(mn)</code>, where <code>m</code> 
 * the number of edges and <code>n</code> the number of nodes 
 * in the graph <code>G</code>.
 * 
 * @author Sergej Alekseev
 * @version $Revision$
 * $Id$
 */
public class MaxCardBipartiteMatching {

	private boolean fromAtoB = true; 
	private List<IEdgeExt> path = null;
	private Set<IEdgeExt> matchedEdges = new HashSet<IEdgeExt>(); 

	/**
	 * @return the matchedEdges
	 */
	public Set<IEdgeExt> getMatchedEdges() {
		return matchedEdges;
	}


	/**
	 * Starts matching algorithm.
	 * @param graph the bipartite graph
	 * @param partA the first partition
	 * @param partB the second partition
	 */
	public void start(IDirectedGraphExt graph, List<INodeExt> partA, List<INodeExt> partB) {
		for(INodeExt n: partA){
			path = new ArrayList<IEdgeExt>();
			GraphUtils.clearGraph(graph);
			fromAtoB = true;
			
			dfs(n);
		}
	}

	/**
	 * Depth first search.
	 * @param node the start node
	 */
	protected void dfs(INodeExt node){
		if(node.isVisited())
			return;

		node.setVisited(true);

		/* for all incoming edges */
		IEdgeListExt inList = node.getIncomingEdgeList();
		for(int i = 0; i < inList.size(); i++){
			IEdgeExt e = inList.getEdgeExt(i);
			if(!e.isVisited()){
				e.setVisited(true);

				/* add the edge to path */
				path.add(e);
				
				/* edge visitor hook */
				if(visitEdge(e)){
					return;
				}

				dfs(e.getSource());

				/* 
				 * back from the recursion:
				 * remove the edge from path 
				 */
				path.remove(e);
			}
		}

		/* for all outgoing edges */
		IEdgeListExt outList = node.getOutgoingEdgeList();
		for(int i = 0; i < outList.size(); i++){		
			IEdgeExt e = outList.getEdgeExt(i);
			if(!e.isVisited()){
				e.setVisited(true);

				/* add the edge to path */
				path.add(e);
				
				/* edge visitor hook */
				if(visitEdge(e)){ 
					return;
				}

				dfs(e.getTarget());

				/* 
				 * back from the recursion:
				 * remove the edge from path 
				 */
				path.remove(e);
			}
		}
	}

	/**
	 * Processes an edge and returns <code>true</code>
	 * if the DFS has to be stopped, otherwise 
	 * <code>false</code>.
	 * @param edge the edge has to be processed
	 * @return <code>true</code> or <code>false</code>
	 */
	public boolean visitEdge(IEdgeExt edge) {
		if(fromAtoB){ /* A -> B */
			fromAtoB = false;
			INodeExt s = edge.getSource();
			INodeExt t = edge.getTarget();

			/* incoming edge */
			if(s.isVisited()){
				if(t.getMark() != MarkEnum.RED){
					/* an augmenting path found */
					t.setMark(MarkEnum.RED);
					
					switchAugementingPath();

					/* stop dfs */
					return true;
				}
			}
			/* outgoing edge */
			else{
				if(s.getMark() != MarkEnum.RED){
					/* an augmenting path found */
					s.setMark(MarkEnum.RED);

					switchAugementingPath();

					/* stop dfs */
					return true;
				}
			}
		}
		else{ /* A <- B */
			fromAtoB = true;
		}

		return false;
	}

	/**
	 * Alternates an augmenting path and actualizes 
	 * the list of matched edges.
	 */
	private void switchAugementingPath(){
		for(int i = path.size() - 1; i >= 0; i--){
			if(i % 2 == 0){
				matchedEdges.add(path.get(i));
			}
			else{
				matchedEdges.remove(path.get(i));
			}
		}
	}

}
