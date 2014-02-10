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
	private int[][] numberedMatrix;
	private int matrixSize;
	
	private Map<INodeExt, Integer> mapPartA = new HashMap<INodeExt, Integer>();
	private Map<INodeExt, Integer> mapPartB = new HashMap<INodeExt, Integer>();
	private int[] matchedRows;
	
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
		HungarianAlgorithm alg = new HungarianAlgorithm(numberedMatrix);
		matchedRows = alg.execute();
		collectMatchedEdges(matchedRows);
	}
	/**
	 * 
	 * adds matched edges from matrix into Set matchedEdges
	 * @param matchedRows
	 */
	private void collectMatchedEdges(int []matchedRows){
		
		for(int i = 0; i < matrixSize; i++){
			if(matrix[i][matchedRows[i]] != null){
				matchedEdges.add(matrix[i][matchedRows[i]]);
			}
		}
	}
	
	/**
	 * build a simple squared matrix of weights
	 * @param int matrix
	 * @return int[][] numberedMatrix
	 */
	private int[][] getNumberedMatrix(IEdgeExt[][] matrix){
		numberedMatrix = new int [matrixSize][matrixSize];
		for (int[] row : numberedMatrix){
		    Arrays.fill(row, 0);
		}
		
		for(int i = 0; i < matrixSize; i++){
			for(int j = 0; j < matrixSize; j++){
				if(matrix[i][j] != null){
					numberedMatrix[i][j] = matrix[i][j].getCounter();
				}
			}
		}
		return numberedMatrix;
 	}
	
	/**
	 * Constructs a matrix of edges
	 * @param partA
	 * @param partB
	 */
	private void buildCostMatrix(List<INodeExt> partA, List<INodeExt> partB){
		
		/* declare matrix with highest number of vertices of B*/
		matrixSize = partB.size(); 
		int n = partB.size();
		this.matrix = new IEdgeExt[n][n];
		
		/* define sequence of A-vertices in the matrix*/
		int index = 0;
		for(INodeExt node: partA){
			mapPartA.put(node, index);
			index++;
		}

		/* define sequence of B-vertices in the matrix*/
		index = 0;
		for(INodeExt node: partB){
			mapPartB.put(node, index);
			index++;
		}
		    
		/*map vertices to graph properly according to the sequence*/
		for(int i = 0; i < partA.size(); i++){
			
			INodeExt node = partA.get(i); 
			for(int j = 0; j <node.getOutgoingEdgeList().size(); j++){	 
			
				INodeExt target = node.getOutgoingEdgeList().getEdgeExt(j).getTarget();
				int indexA = mapPartA.get(node);
				int indexB = mapPartB.get(target);
				this.matrix[indexA][indexB] = node.getOutgoingEdgeList().getEdgeExt(j);
			}
		}
		
		/*get integer matrix with weights*/
		numberedMatrix = getNumberedMatrix(matrix);
		printWeighedtMatrix(matrix);
	}
	
	/*-------------------------------------DEBUG-------------------------------------------*/
	/**
	 * calculate weights of matched edges 
	 * @return
	 */
	public int getMaxWeightMatchedEdges(){
		int count = 0;
		for(IEdgeExt edges: this.matchedEdges){
			count += edges.getCounter();
		}
		
		return count;
	}
	
	/**
	 * prints the matrix of edges
	 * @param matrix
	 */
	public void printWeighedtMatrix(IEdgeExt[][] matrix){
		System.out.println();
		System.out.println("===Matrix===");
		
		/* output B headers */
		String headerB = "  ";
		for(int i = 0; i < mapPartB.size(); i++){
		for(Entry<INodeExt, Integer> entry : mapPartB.entrySet()){
				if(entry.getValue().equals((Integer)i)){
					headerB += entry.getKey().getData()+ " ";
				}				
			}
		}
		System.out.print(headerB);
		System.out.println();
		
		/* output content of matrix */
		boolean noA = true;
		for(int i = 0; i < matrix.length; i++){
			/* output for A headers */
			for(Entry<INodeExt, Integer> entry : mapPartA.entrySet()){
				if(entry.getValue().equals((Integer)i)){
					System.out.print(entry.getKey().getData()+ " ");
					noA = true;
					break;
				}
				else{
					noA = false;
				}
			}
			
			/* when there are no real A, dummy A nodes are added to the matrix output(the matrix must be NxN) */
			if(!noA){
				System.out.print("a"+(i+1)+" ");
				noA = true;
			}
			/* output cost matrix (null edges are output as zero)*/
			for(int j = 0; j < matrix.length; j++){
				if(matrix[i][j] != null){
					System.out.print(matrix[i][j].getCounter()+"  ");
				}
				else{
					System.out.print("0  ");
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
