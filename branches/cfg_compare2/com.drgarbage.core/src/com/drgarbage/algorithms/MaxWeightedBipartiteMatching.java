package com.drgarbage.algorithms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.drgarbage.controlflowgraph.intf.GraphUtils;
import com.drgarbage.controlflowgraph.intf.IDirectedGraphExt;
import com.drgarbage.controlflowgraph.intf.IEdgeExt;
import com.drgarbage.controlflowgraph.intf.IEdgeListExt;
import com.drgarbage.controlflowgraph.intf.INodeExt;
import com.drgarbage.controlflowgraph.intf.MarkEnum;
/**
* @author Artem Garishin
* @version $Revision$
* $Id$
*/
public class MaxWeightedBipartiteMatching {
	
	private Set<IEdgeExt> matchedEdges = new HashSet<IEdgeExt>(); 
	private IEdgeExt[][] matrix;
	private Map<INodeExt, Integer> mapPartA = new HashMap<INodeExt, Integer>();
	private Map<INodeExt, Integer> mapPartB = new HashMap<INodeExt, Integer>();
	
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
	
		buildCostMatrix(partA, partB);
		//TODO: attach hungarian method here
		
	}
	
	private void buildCostMatrix(List<INodeExt> partA, List<INodeExt> partB){
		
		/* declare matrix with highest number of vertices of B*/
		int n = partB.size();
		this.matrix = new IEdgeExt[n][n];
		
		/* define sequence of vertices in the matrix*/
		int index = 0;
		for(INodeExt node: partA){
			mapPartA.put(node, index);
			index++;
		}

		index = 0;
		for(INodeExt node: partB){
			mapPartB.put(node, index);
			index++;
		}
		
		/* debug positions*/
		printMap(mapPartA);
		printMap(mapPartB);
		    
		/*map vertices to graph properly*/
		for(int i = 0; i < partA.size(); i++){
			
			INodeExt node = partA.get(i); 
			for(int j = 0; j <node.getOutgoingEdgeList().size(); j++){	 
			
				INodeExt target = node.getOutgoingEdgeList().getEdgeExt(j).getTarget();
				int indexA = mapPartA.get(node);
				int indexB = mapPartB.get(target);
				this.matrix[indexA][indexB] = node.getOutgoingEdgeList().getEdgeExt(j);
			}
		}
		printWeighedtMatrix(matrix);
	}
	
	/*-------------------------------------DEBUG-------------------------------------------*/
	/**
	 * prints the matrix of edges
	 * @param matrix
	 */
	public void printWeighedtMatrix(IEdgeExt[][] matrix){
		System.out.println("===Matrix===");
		for(int i = 0; i < matrix.length; i++){
			for(int j = 0; j < matrix.length; j++){
				if(matrix[i][j] != null){
					System.out.print(matrix[i][j].getCounter()+" ");
				}
				else{
					System.out.print("0 ");
				}
			}
			System.out.print("\n");
		}	
	}
	
	/**
	 * prints map
	 * @param Map<INodeExt, Integer>
	 */
	public void printMap(Map<INodeExt, Integer> map){
		for(Entry<INodeExt, Integer> entry : map.entrySet()){
			INodeExt node = entry.getKey();
            Integer i = entry.getValue();
            System.out.println(node.getData().toString() + "-" + i);
		}
	}
}
