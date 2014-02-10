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
import java.util.List;

import junit.framework.TestCase;

import com.drgarbage.controlflowgraph.intf.GraphExtentionFactory;
import com.drgarbage.controlflowgraph.intf.IDirectedGraphExt;
import com.drgarbage.controlflowgraph.intf.IEdgeExt;
import com.drgarbage.controlflowgraph.intf.INodeExt;

/**
 * Test class for {@link com.drgarbage.algorithms.HungarianMethod}
 * 
 * @author Sergej Alekseev, Artem Garishin
 * @version $Revision$
 * $Id$
 */
public class HungarianMethodTest extends TestCase {
	
	/**
	 * Test set consists of a bipartite graph and two partitions.
	 */
	class TestSet{
		 IDirectedGraphExt graph = GraphExtentionFactory.createDirectedGraphExtention();
		 List<INodeExt> partA = new ArrayList<INodeExt>(); 
		 List<INodeExt> partB = new ArrayList<INodeExt>();
	}	
	
	/**
	 * The test set 1. <br>
	 * The graph <code>G = (A + B, E)</code>:
	 * <pre>
	 *   a1 --- b1
	 *      \ /
	 *      / \
	 *   a2 --- b2
	 *      \ /
	 *      / \
	 *   a3 --- b3
	 * </pre>
	 * <code>A =(a1, a2, a3)</code>, <code>B =(b1, b2, b3)</code>
	 * and
	 * <code>E = A X B</code>
	 * <br>
	 * 
	 * The weights, assigned to the edges are represented as a matrix:
	 * 	<pre>
	 *  	b1  b2  b3
	 *  a1	10  20  20
	 *  a2	 5  10  10
	 *  a3	20   6  10
	 *  </pre>
	 *  
	 * @param weights the weights to be assigned to the edges
	 * @return the test set
	 */
	private TestSet createTestSet2(int [][] weights){
		TestSet t = new TestSet();

        INodeExt a1 = GraphExtentionFactory.createNodeExtention("a1");
        t.graph.getNodeList().add(a1);
        t.partA.add(a1);
        INodeExt a2 = GraphExtentionFactory.createNodeExtention("a2");
        t.graph.getNodeList().add(a2);
        t.partA.add(a2);
        INodeExt a3 = GraphExtentionFactory.createNodeExtention("a3");
        t.graph.getNodeList().add(a3);
        t.partA.add(a3);
        
        INodeExt b1 = GraphExtentionFactory.createNodeExtention("b1");
        t.graph.getNodeList().add(b1);
        t.partB.add(b1);
        INodeExt b2 = GraphExtentionFactory.createNodeExtention("b2");
        t.graph.getNodeList().add(b2);
        t.partB.add(b2);
        INodeExt b3 = GraphExtentionFactory.createNodeExtention("b3");
        t.graph.getNodeList().add(b3);
        t.partB.add(b3);
        
        IEdgeExt edge = GraphExtentionFactory.createEdgeExtention(a1, b1);
        edge.setCounter(weights[0][0]);
        t.graph.getEdgeList().add(edge);
        
		edge = GraphExtentionFactory.createEdgeExtention(a1, b2);
		edge.setCounter(weights[0][1]);
		t.graph.getEdgeList().add(edge);
		
		edge = GraphExtentionFactory.createEdgeExtention(a1, b3);
		edge.setCounter(weights[0][2]);
		t.graph.getEdgeList().add(edge);
		
        edge = GraphExtentionFactory.createEdgeExtention(a2, b1);
        edge.setCounter(weights[1][0]);
		t.graph.getEdgeList().add(edge);
		
		edge = GraphExtentionFactory.createEdgeExtention(a2, b2);
		edge.setCounter(weights[1][1]);
		t.graph.getEdgeList().add(edge);
		
		edge = GraphExtentionFactory.createEdgeExtention(a2, b3);
		edge.setCounter(weights[1][2]);
		t.graph.getEdgeList().add(edge);

		edge = GraphExtentionFactory.createEdgeExtention(a3, b1);
        edge.setCounter(weights[2][0]);
		t.graph.getEdgeList().add(edge);
		
		edge = GraphExtentionFactory.createEdgeExtention(a3, b2);
        edge.setCounter(weights[2][1]);
		t.graph.getEdgeList().add(edge);
		
		edge = GraphExtentionFactory.createEdgeExtention(a3, b3);
        edge.setCounter(weights[2][2]);
		t.graph.getEdgeList().add(edge);
		
		return t;
	}
	
	/**
	 * For debugging purposes only.
	 * @param matrix the matrix
	 */
	private void printMatrix(int[][] matrix){
		for(int i = 0; i < matrix.length; i++){
			for(int j = 0; j < matrix.length; j++){
				System.out.print(matrix[i][j]);
				System.out.print(' ');
			}
			System.out.println();
		}
	}
	
	/**
	 * Test 1
	 * <br>
	 * Input:
	 *  <pre>
	 *  	10 9 3
	 *  	 5 6 7
	 *  	 1 4 8
	 *  </pre>
	 * 
	 * Result:
	 *  <pre>
	 *  	- - 3
	 *  	- 6 -
	 *  	1 - -
	 *  </pre>
	 * 
	 * Sum 3 + 6 + 1 = 10
	 * 
	 */
	public void testExecuteHungarianMethod1() {
		
		int [][] weights = {
				{10, 9, 3},
				{ 5, 6, 7},
				{ 1, 4, 8}
		};
		
		printMatrix( weights);
		
		TestSet t = createTestSet2(weights);
		
		List<IEdgeExt> edges = new HungarianMethod(false).execute(t.graph, t.partA, t.partB);
		assertEquals(3, edges.size());
		
		int weight = 0;
    	for(IEdgeExt e : edges){
    		weight += e.getCounter();
    	}
		
		assertEquals(10, weight);
		
		System.out.println("OK: sum = " + weight + "\n");
	}
	
	/**
	 * Test 1
	 * <br>
	 * Input:
	 *  <pre>
	 *  	 1 9 3
	 *  	 5 1 7
	 *  	10 4 1
	 *  </pre>
	 * 
	 * Result:
	 *  <pre>
	 *  	1 - -
	 *  	- 1 -
	 *  	- - 1
	 *  </pre>
	 * 
	 * Sum 1 + 1 + 1 = 3
	 * 
	 */
	public void testExecuteHungarianMethod2() {
		
		int [][] weights = {
				{ 1, 9, 3},
				{ 5, 1, 7},
				{ 10, 4, 1}
		};
		
		printMatrix( weights);
		
		TestSet t = createTestSet2(weights);
		
		List<IEdgeExt> edges = new HungarianMethod(false).execute(t.graph, t.partA, t.partB);
		assertEquals(3, edges.size());
		
		int weight = 0;
    	for(IEdgeExt e : edges){
    		weight += e.getCounter();
    	}
		
		assertEquals(3, weight);
		
		System.out.println("OK: sum = " + weight + "\n");
	}
	
	
	/**
	 * Test 1
	 * <br>
	 * Input:
	 *  <pre>
	 *  	10 1 3
	 *  	 5 6 2
	 *  	 1 4 8
	 *  </pre>
	 * 
	 * Result:
	 *  <pre>
	 *  	- 1 -
	 *  	- - 2
	 *  	1 - -
	 *  </pre>
	 * 
	 * Sum 1 + 2 + 1 = 4
	 * 
	 */
	public void testExecuteHungarianMethod3() {
		
		int [][] weights = {
				{10, 1, 3},
				{ 5, 6, 2},
				{ 1, 4, 8}
		};
		
		printMatrix( weights);
		
		TestSet t = createTestSet2(weights);
		
		List<IEdgeExt> edges = new HungarianMethod(false).execute(t.graph, t.partA, t.partB);
		assertEquals(3, edges.size());
		
		int weight = 0;
    	for(IEdgeExt e : edges){
    		weight += e.getCounter();
    	}
		
		assertEquals(4, weight);
		
		System.out.println("OK: sum = " + weight + "\n");
	}	
	
}
