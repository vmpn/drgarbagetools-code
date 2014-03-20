/**
 * Copyright (c) 2008-2013, Dr. Garbage Community
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Map.Entry;
import java.util.Set;

import com.drgarbage.algorithms.TopDownSubtreeIsomorphism.MatrixEntry;
import com.drgarbage.controlflowgraph.ControlFlowGraphException;
import com.drgarbage.controlflowgraph.intf.GraphExtentionFactory;
import com.drgarbage.controlflowgraph.intf.GraphUtils;
import com.drgarbage.controlflowgraph.intf.IDirectedGraphExt;
import com.drgarbage.controlflowgraph.intf.IEdgeExt;
import com.drgarbage.controlflowgraph.intf.IEdgeListExt;
import com.drgarbage.controlflowgraph.intf.INodeExt;
import com.drgarbage.controlflowgraph.intf.INodeListExt;

/**
 * The Top-Down-MAX Common Subtree Isomorphism algorithm. The implementation is based 
 * on the algorithm published by Gabriel Valiente in his book "Algorithms on Trees and Graphs". 
 * The following example from this book is used as a reference:
 * <pre>
 *   T_1                            T_2
  *       ____v12___                   _____ w18 ____________ 
 *       /          \                /        |               \
 *      v6          v11            w4        w12              w17
 *     /            /  \           /  \      /  \          /   |  \
 *    v5           v9   v10      w1    w3  w5   w11    w13   w14   w16
 *   /  \         /  \                  |       /  \                |
 * v1    v4      v7  v8                 w2     w9   w10            w15
 *     /  \                             |
 *    v2   v3                           w8
 *                                     / \
 *                                   w6   w7
 * </pre>  
 * 
 * @author Artem Garishin
 * 
 * @version $Revision:$ 
 * $Id:$
 */

public class TopDownMaxCommonSubTreeIsomorphism {
	
	Map<INodeExt, List<IEdgeExt>> B = null;
	
	protected class MatrixEntry{
		protected INodeExt v;
		protected INodeExt w;
		protected int result = -1;
	}
	
	/**
	 * Executes the Top Down Max Common Subtree isomorphism algorithm.
	 * 
	 * @param leftTree the tree <code>T_1</code>
	 * @param rightTree the tree <code>T_2</code>
	 * @return the map of matched nodes
	 * @throws ControlFlowGraphException 
	 */
	public Map<INodeExt, INodeExt> topDownMaxCommonUnorderedSubtreeIsomorphism(
			IDirectedGraphExt leftTree, IDirectedGraphExt rightTree) throws ControlFlowGraphException {
		
		/* get root nodes */
		INodeExt rootLeft = null;
		for(int i = 0; i < leftTree.getNodeList().size(); i++){
			INodeExt n = leftTree.getNodeList().getNodeExt(i);
			if(n.getIncomingEdgeList().size() == 0){
				rootLeft = n;
			}
		}
		
		if(rootLeft == null){
			throw new ControlFlowGraphException("The left tree has no root. The graph is propably not a tree.");
		}
		
		INodeExt rootRight = null;
		for(int i = 0; i < rightTree.getNodeList().size(); i++){
			INodeExt n = rightTree.getNodeList().getNodeExt(i);
			if(n.getIncomingEdgeList().size() == 0){
				rootRight = n;
			}
		}
		
		if(rootRight == null){
			throw new ControlFlowGraphException("The right tree has no root. The graph is propably not a tree.");
		}

		return topDownMaxCommonUnorderedSubtreeIsomorphism(leftTree, rootLeft, rightTree, rootRight);
		
	}
	
	/**
	 * Executes the Top Down Subtree isomorphism algorithm.
	 * 
	 * @param leftTree the tree <code>T_1</code>
	 * @param rootLeft the root node of the left tree
	 * @param rightTree the tree <code>T_2</code>
	 * @param rootRight the root node of the right tree
	 * @return the map of matched nodes
	 */
	public Map<INodeExt, INodeExt> topDownMaxCommonUnorderedSubtreeIsomorphism(
			IDirectedGraphExt leftTree, 
			INodeExt rootLeft, 
			IDirectedGraphExt rightTree,
			INodeExt rootRight) {

		/* clear tree graphs */
		GraphUtils.clearGraph(leftTree);
		GraphUtils.clearGraphColorMarks(leftTree);
		GraphUtils.clearGraph(rightTree);
		GraphUtils.clearGraphColorMarks(rightTree);
		
		/*partial injection*/
		B = new HashMap<INodeExt, List<IEdgeExt>>();
		
		Map<INodeExt, INodeExt> M  = new HashMap<INodeExt, INodeExt>();
		traverseTopDown(rootLeft, rootRight);

		//DEBUG
		System.out.println("map B:");
			for(Entry<INodeExt, List<IEdgeExt>> entry: B.entrySet()){
				System.out.print(entry.getKey().getData() + ": ");
				
				for(IEdgeExt e: entry.getValue()){
					System.out.print(" " + ((INodeExt)e.getTarget().getData()).getData());
				}
				System.out.println();
			}	
		System.out.println();
		
		/* reconstruct the subtree */
		M.put(rootLeft, rootRight);
		reconstruct(rootLeft, M);

		return M;
	}
	
	/**
	 * traverseTopDown
	 * 
	 * This method goes recursively simultaneously through T1 and T2 till leafs of them are founded.
	 * The example is based on:
	 * {@link com.drgarbage.algorithms.TopDownMaxCommonSubTreeIsomorphism TopDownMaxCommonSubTreeIsomorphism}.
	 * </br>
	 * Reach recursively the leaves of T1 or T2 and construct the follow matrix:
	 * <pre>
	 *    ---- Matrix---
	 *		(v3 w8 1) 
	 *		(v2 w8 1) 
	 *
	 * <pre>
	 *  The weight of v2 and v3 is equal 1(v2 and w8 respectively one too)
	 *  Build a weighed bipartite graph and get the max weighed matching: v2->w8 
	 *  
	 *  Since the possible matching is found go one level upper (the nodes which located on the same level)
	 *  Build the following matrix <b>taking into account previous matched nodes(in this case children of v4)</b> 
	 *	
	 *		  ---- Matrix---
	 *		(v1 w9 1) (v1 w10 1) 
	 *	  	(v4 w9 2) (v4 w10 1) 	
	 *  The weight of v4 and w9 has a weight 2 because the child of 
	 * @param v node of the T_1
	 * @param w node of the T_2
	 * @return 0 or 1
	 */
	private int traverseTopDown(INodeExt v, INodeExt w){
		
		debug(v.getData().toString() + " <-> " + w.getData().toString());
		 
		/* p is number of children of v 
		 * q is number of children of w
		 */
		int p  = v.getOutgoingEdgeList().size();
		int q = w.getOutgoingEdgeList().size();

		/* v is a leaf*/
		if(p == 0){
			return 1;
		}
		
		/* w is a leaf*/
		if(q == 0){
			return 1;
		}

		IEdgeListExt out1 = v.getOutgoingEdgeList();
		IEdgeListExt out2 = w.getOutgoingEdgeList();

		MatrixEntry matrix[][] = new MatrixEntry[p][q];
		for(int i = 0; i < p; i++){
			for(int j = 0; j < q; j++){
				INodeExt child1 = out1.getEdgeExt(i).getTarget();
				INodeExt child2 = out2.getEdgeExt(j).getTarget();

				MatrixEntry me = new MatrixEntry();
				me.v = child1;
				me.w = child2;
				
				matrix[i][j] = me;
				me.result = traverseTopDown(child1, child2);
			}
		}
		
		/*once leaves are reached  start increase edges-weight of maximum matched nodes*/
		int res = 1;
		if(p != 0 && q != 0){
			int size[] = {p, q};
			
			/* DEBUG */
			printMatrix(matrix, size);
			
			/* create a bipartite graph from the matrix */
			List<INodeExt> part1 = new ArrayList<INodeExt>();
			List<INodeExt> part2 = new ArrayList<INodeExt>();
			IDirectedGraphExt graph = createBibartitGraph(matrix, size, part1, part2);
			
			/* an optimization to avoid the creation of an empty matrix */
			if(graph.getEdgeList().size() == 0){
				return 1;
			}

			/* find max bipartite matching in weighted bipartite graph */
			MaxWeightedBipartiteMatching mwbm = new MaxWeightedBipartiteMatching();
			List<IEdgeExt> MatchedEdges = mwbm.execute(graph, part1, part2);
			
			/*increase edges weight of matched nodes in bipartite graph*/
			for(IEdgeExt e: MatchedEdges){
				res += e.getCounter();
			}
			
			matching(MatchedEdges);
			
		}

		return res;
	}

	/**
	 * Creates weighted bipartite graph
	 * @param matrix
	 * @param size
	 * @param part1
	 * @param part2
	 * @return
	 */
	private static IDirectedGraphExt createBibartitGraph(MatrixEntry[][] matrix,
			int[] size, 
			List<INodeExt> part1, 
			List<INodeExt> part2){
	
		Set<INodeExt> set1 = new HashSet<INodeExt>();
		Set<INodeExt> set2 = new HashSet<INodeExt>();
		
		IDirectedGraphExt graph = GraphExtentionFactory.createDirectedGraphExtention();
		Map<INodeExt, INodeExt> newGraphnodeMap = new HashMap<INodeExt, INodeExt>();
		
		for(int i = 0; i < size[0]; i++){
			for(int j = 0; j < size[1]; j++){
				MatrixEntry me = matrix[i][j];
				if(me.result != 0){
					INodeExt v = newGraphnodeMap.get(me.v);
					if(v == null){
						v = GraphExtentionFactory.createNodeExtention(me.v);
						newGraphnodeMap.put(me.v, v);
					}
					set1.add(v);
	
					INodeExt w = newGraphnodeMap.get(me.w);
					if(w == null){
						w = GraphExtentionFactory.createNodeExtention(me.w);
						newGraphnodeMap.put(me.w, w);
					}
					set2.add(w);
	
					IEdgeExt e = GraphExtentionFactory.createEdgeExtention(v, w);
					e.setCounter(me.result);
					
					/* for debugging purposes */
					if(DEBUG){
						String str = me.v.getData().toString() + "->" + me.w.getData().toString(); 
						e.setData(str);
					}
					graph.getEdgeList().add(e);
				}
			}
		}
	
		for(INodeExt n: newGraphnodeMap.values()){
			graph.getNodeList().add(n);
		}
	
		Iterator<INodeExt> it = set1.iterator();
		while(it.hasNext()){
			part1.add(it.next());
		}
		
		it = set2.iterator();
		while(it.hasNext()){
			part2.add(it.next());
		}
		
		/* print the graph if */
		if(!DEBUG){
			printGraph(graph);
			debug("PART1:");
			for(INodeExt n: part1){
				System.out.print(" " + ((INodeExt)n.getData()).getData());
			}
			debug("\n");
			debug("PART2:");
			for(INodeExt n: part2){
				System.out.print(" " + ((INodeExt)n.getData()).getData());
			}
			debug("\n");
		}
		
		return graph;
	}
	
	
	/**
	 * This method reconstructs the top-down unordered subtree isomorphism mapping
	 * <code>V_1 X V_2 subset M</code> included the solution 
	 * <code>V_1 X V_2 subset B</code> to all maximum cardinality bipartite
	 * matchings problems.
	 * <br>
	 * The root of <code>T_1</code> is mapped to the root of <code>T_2</code>
	 * in the previous step. Each nonroot node <code>v in V_1</code> is
	 * mapped to the unique node <code>v in V_2</code> with <code>(v, w) in B</code>
	 * and <code>(parent(v), parent(w)) in B</code>.
	 * <br>
	 * According the example above:
	 * <pre>
	 * TODO: new description
	 * v_7: !w_18!
	 * v_6: !w_17!
	 * v_5: !w_12!
	 * v_4:  w_1, !w5 !,  w13
	 * v_3:  w_3, !w11!, w16
	 * v_2:  w_2, !w9 !,  w15
	 * v_1: !w_4 !
	 * </pre>
	 * @see  TopDownSubtreeIsomorphism
	 * 
	 * @param root the root of the tree <code>T_1</code>
	 * @param M the map of the matched nodes
	 */
	private void reconstruct(INodeExt root, Map<INodeExt, INodeExt> M){
		
		Queue<INodeExt> queue = new LinkedList<INodeExt>();
		queue.add(root);
		
		/*start bfs from rootLeft*/
		while(!queue.isEmpty()){
			INodeExt node = queue.poll();
			IEdgeListExt outgoingEdges = node.getOutgoingEdgeList();
			for(int i = 0; i < outgoingEdges.size(); i++){
				IEdgeExt edge = outgoingEdges.getEdgeExt(i);
				
				/*get each node in TreeLeft */
				INodeExt targetNode = edge.getTarget();
				if(!targetNode.isVisited()){
					queue.add(targetNode);
					List<IEdgeExt> edges = B.get(targetNode);
					if(edges != null){
						for(IEdgeExt e: edges){
							/*start reconstruct*/
							INodeExt nodeV = (INodeExt)e.getSource().getData();
							INodeExt nodeW = (INodeExt)e.getTarget().getData();
							debug("       +-> match " 
									+ nodeV.getData().toString() 
									+ "->" 
									+ nodeW.getData().toString());
	
							INodeExt parentV = nodeV.getIncomingEdgeList().getEdgeExt(0).getSource();
							INodeExt parentW = nodeW.getIncomingEdgeList().getEdgeExt(0).getSource();
	
							debug("         +-> Parent of :" + nodeV.getData() + " is " + parentV.getData());
							debug("         +-> Parent of :" + nodeW.getData() + " is " + parentW.getData());
	
							INodeExt vW = M.get(parentV);
							if(vW != null){
								if(vW.equals(parentW)){
									debug("!!! FOUND " + nodeV.getData() +  " " + nodeW.getData());
									M.put(nodeV, nodeW);
								}
							}
							else{
								debug("ERROR ..." + nodeV.getData() +  " " + nodeW.getData());
							}
						}
					}
				}
			}
		}
		
	}
	
	/**
	 * puts into list B matched edges for further reconstruction
	 * @param MatchedEdges
	 */
	protected  void matching(List<IEdgeExt> MatchedEdges){
		
		debug(" === Matching ");
		for(IEdgeExt e: MatchedEdges){

			debug(((INodeExt)e.getSource().getData()).getData()
					+ "->" 
					+ ((INodeExt)e.getTarget().getData()).getData());

			
			List<IEdgeExt> list = B.get(e.getSource().getData());
			if(list == null){
				list = new ArrayList<IEdgeExt>();
				B.put((INodeExt) e.getSource().getData(), list);
			}

			for(IEdgeExt ee: MatchedEdges){
				if(ee.getSource().getData().equals(e.getSource().getData())){
					list.add(ee);
				}
			}
		}
		debug(" =======");

	}
		/* 
		 * The Methods in this section are used for purely debugging purposes 
		 */
		
		/**
		 * Debugging flag. Set <code>true</code> to enable printing the
		 * debugging messages.
		 */
		protected static boolean DEBUG = true;
		
		/**
		 * Prints a message for debugging purposes.
		 * <br> 
		 * NOTE: The method is disabled if the debugging flag set to false.
		 * 
		 * @param msg the text message 
		 * @see #DEBUG
		 */
		private static void debug(String msg){
			if(!DEBUG) return;
			
			System.out.println(msg);
		}
		
		/**
		 * Prints the matrix in the following format:
		 * <pre>
		 *   ---- Matrix---
		 *   (v6 w17 1) (v6 w12 1) (v6 w4 1) 
		 *   (v5 w17 1) (v5 w12 1) (v5 w4 1) 
		 *   (v1 w17 1) (v1 w12 1) (v1 w4 1) 
		 *   --------------
		 * </pre>
		 * 
		 * NOTE: The method is disabled if the debugging flag set to false.
		 * 
		 * @param matrix the matrix
		 * @param size the matrix size <code>new int [][] = {10, 4}</code>
		 * @see #DEBUG
		 */
		private static void printMatrix(MatrixEntry[][] matrix, int[] size){	
			if(!DEBUG) return;
			
			System.out.println("---- Matrix---");
			for(int i = 0; i < size[0]; i++){
				for(int j = 0; j < size[1]; j++){
					MatrixEntry me = matrix[i][j];
					System.out.print("(" + me.v.getData().toString() 
							+ " " + me.w.getData().toString() 
							+ " " + me.result
							+ ") ");
				}
				System.out.println();
			}
			System.out.println("--------------");
		}
		
		private static void printGraph(IDirectedGraphExt g) {
			if(!DEBUG) return;
			
		}



}
