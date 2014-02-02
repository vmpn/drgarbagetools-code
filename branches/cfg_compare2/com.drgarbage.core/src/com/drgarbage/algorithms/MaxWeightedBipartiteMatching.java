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

public class MaxWeightedBipartiteMatching {
	
	private boolean fromAtoB = true; 
	private List<IEdgeExt> path = null;
	private Set<IEdgeExt> matchedEdges = new HashSet<IEdgeExt>(); 
	
	public Set<IEdgeExt> getMatchedEdges() {
		return matchedEdges;
	}
	/**
	 * returns summarized weight of matched edges 
	 * @return
	 */
	public int getMaxWeightAll(){
		int count = 0;
		for(IEdgeExt edges: this.matchedEdges){
			count += edges.getCounter();
		}
		
		return count;
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
	 * get max weighted edges from list of edges
	 * @param list
	 * @return
	 */
	protected IEdgeExt getMaxEdge(IEdgeListExt list){
		
		IEdgeExt maxWeigtedEdge = null; 
		for(int i = 0; i < list.size(); i++){
			if(!list.getEdgeExt(i).isVisited()){
				maxWeigtedEdge = list.getEdgeExt(0);
			}
		}
		for(int i = 0; i < list.size(); i++)
		{
			if(!list.getEdgeExt(i).isVisited()){
				if(list.getEdgeExt(i).getCounter() > maxWeigtedEdge.getCounter())
				{
					maxWeigtedEdge = list.getEdgeExt(i);
				}
			}
		}
		
		return maxWeigtedEdge;
	}
	/**
	 * Depth first search.
	 * @param node the start node
	 */
	protected void dfs(INodeExt node){
		if(node.isVisited()){
			return;
		}
		node.setVisited(true);
		
		/* for all incoming edges */
		IEdgeListExt inList = node.getIncomingEdgeList();
		if(inList.size() != 0){	
			IEdgeExt e = getMaxEdge(inList);
			if(e == null){ 
				return;
			}
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
		
		if(outList.size() != 0){
			IEdgeExt e = getMaxEdge(outList);
			if(e == null){ 
				return;
			}
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
