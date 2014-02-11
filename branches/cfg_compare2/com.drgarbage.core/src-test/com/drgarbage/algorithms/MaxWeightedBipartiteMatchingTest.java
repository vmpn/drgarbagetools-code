package com.drgarbage.algorithms;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import com.drgarbage.controlflowgraph.intf.GraphExtentionFactory;
import com.drgarbage.controlflowgraph.intf.IDirectedGraphExt;
import com.drgarbage.controlflowgraph.intf.IEdgeExt;
import com.drgarbage.controlflowgraph.intf.INodeExt;

/**
* @author Artem Garishin
* @version $Revision$
* $Id$
*/
public class MaxWeightedBipartiteMatchingTest extends TestCase{

	/**
	 * Test set consists of a bipartite graph and two partitions.
	 */
	class TestSet{
		 IDirectedGraphExt graph = GraphExtentionFactory.createDirectedGraphExtention();
		 List<INodeExt> partA = new ArrayList<INodeExt>(); 
		 List<INodeExt> partB = new ArrayList<INodeExt>();
	}	
	
	/**
	 * The test set <br>
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
		
//		edge = GraphExtentionFactory.createEdgeExtention(a2, b3);
//		edge.setCounter(weights[1][2]);
//		t.graph.getEdgeList().add(edge);

		edge = GraphExtentionFactory.createEdgeExtention(a3, b1);
        edge.setCounter(weights[2][0]);
		t.graph.getEdgeList().add(edge);
		
		edge = GraphExtentionFactory.createEdgeExtention(a3, b2);
        edge.setCounter(weights[2][1]);
		t.graph.getEdgeList().add(edge);
		
//		edge = GraphExtentionFactory.createEdgeExtention(a3, b3);
//        edge.setCounter(weights[2][2]);
//		t.graph.getEdgeList().add(edge);
		
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

	
	public void testExecuteMax1() {
		
		int [][] weights = {
				{10, 1, 3},
				{ 5, 6, 2},
				{ 1, 4, 8}
		};
		
		printMatrix( weights);
		
		TestSet t = createTestSet2(weights);
		
		List<IEdgeExt> edges  = new MaxWeightedBipartiteMatching(true).execute(t.graph, t.partA, t.partB);
		assertEquals(2, edges.size());
		
		int weight = 0;
    	for(IEdgeExt e : edges){
    		weight += e.getCounter();
    		System.out.println(e.getSource().getData() + "-" + e.getTarget().getData() + " " + e.getCounter());
    	}
		
		assertEquals(4, weight);
		
		System.out.println("OK: sum = " + weight + "\n");
		
		
		
				
	}
	
	/**
	 * The test set 1. <br>
	 * The graph <code>G = (A + B, E)</code>:
	 * <pre>
	 *   a1 --- b1
	 *       /
	 *      /
	 *   a2  -- b2
	 *      \ /
	 *      / \
	 *   a3 --- b3
	 * </pre>
	 * <code>A =(a1, a2, a3)</code>, <code>B =(b1, b2, b3)</code>
	 * and <code>E =((a1 - b1), (a2 - b1), (a2 - b2), (a2 - b3), (a3 - b2), (a3 - b3))</code>
	 * <br>
	 * 
	 * The following weights are assigned to the edges:
	 * <pre>
	 * (a1 - b1)  1
	 * (a2 - b1)  5
	 * (a2 - b2)  1
	 * (a2 - b3)  1
	 * (a3 - b2)  6
	 * (a3 - b3)  1
	 * </pre>
	 * 
	 * Expected matching <code>M = (a2-b1, a3-b2)</code> 
	 * with |M|=2 and W = 5 + 6 = 11:
	 * <pre>
	 *   a1     b1
	 *       /
	 *      /
	 *   a2     b2
	 *       /
	 *      / 
	 *   a3     b3
	 * </pre>
	 * 
	 * ====== <br>
	 * 
	 * The steps of algorithm:
	 * <ol>
	 * 	<li> <b>Create a matrix</b><br> 
	 * 	Ensure that the matrix is square by the addition of dummy rows/columns if necessary.
	 * 	Assign zero value to the missing edges.
	 * 	<pre>
	 *  	b1  b2  b3
	 *  a1	1   0   0
	 *  a2	5   1   1
	 *  a3	0   6   1
	 *  </pre>
	 * 	</li>
	 * 
	 * <li> <b>Convert the matrix from min to max</b><br>
	 *   Multiply each value in the matrix by -1 and add the max value + 1 to each element
	 *   except the zero values.
	 * 	<pre>
	 *  	b1  b2  b3
	 *  a1	6   0   0
	 *  a2	2   6   6
	 *  a3	0   1   6
	 *  </pre>
	 * </li>
	 * 
	 * <li> <b>Reduce the matrix values</b><br>
	 * Reduce the rows by subtracting the minimum value of each row from that row.
	 *	<pre>
	 *  	b1  b2  b3
	 *  a1	6   0   0
	 *  a2	0   4   4
	 *  a3	0   1   6
	 *  </pre> 
	 *  Reduce the columns by subtracting the minimum value of each column from that column.
	 *	<pre>
	 *  	b1  b2  b3
	 *  a1	6   0   0
	 *  a2	0   4   4
	 *  a3	0   1   6
	 *  </pre> 
	 *  No changes in this example.  
	 * </li>
	 * 
	 * <li> <b>Start loop</b><br>
	 * Cover the zero elements with the minimum number of lines it is possible to cover them with.
	 * If the number of lines is equal to the number of rows then leave the loop and
	 * go to step 7
	 *	<pre>
	 *  	  b1  b2  b3
	 *  	  |
	 *  a1	--6---0---0--
	 *  	  |
	 *  a2	  0   4   4
	 *  	  |
	 *  a3	  0   1   6
	 *  	  |
	 *  </pre> 
	 * </li>
	 * 
	 * <li> 
	 *  Add the minimum uncovered element to every covered element. 
	 *  If an element is covered twice, add the minimum element to it twice.
	 *  <pre>
	 *  	b1  b2  b3
	 *  a1	8   1   1
	 *  a2	1   4   4
	 *  a3	1   1   6
	 *  </pre> 
	 * </li>
	 * <li> Subtract the minimum element from every element in the matrix.
	 * 	<pre>
	 *  	b1  b2  b3
	 *  a1	7   0   0
	 *  a2	0   3   3
	 *  a3	0   0   5
	 *  </pre> 
	 *  And now go to the beginning of the loop (step 4).
	 *  <br>
	 *  This example had to be reduced once more. 
	 *  The second iteration of the steps 4, 5 and 6 are described here:
	 *	<pre>
	 *  	  b1  b2  b3
	 *  	  |   |
	 *  a1	--7---0---0--
	 *  	  |   |
	 *  a2	  0   3   3
	 *  	  |   |
	 *  a3	  0   0   5
	 *  	  |   |
	 *  </pre>
	 * 	<pre>
	 *  	b1  b2  b3
	 *  a1	13  3   3
	 *  a2	3   6   3
	 *  a3	3   3   5
	 *  </pre>   
	 * 	<pre>
	 *  	b1  b2  b3
	 *  a1	10  0   0
	 *  a2	0   3   0
	 *  a3	0   0   2
	 *  </pre> 
	 *	<pre>
	 *  	b1  b2  b3
	 *  	|   |   |
	 *  a1	10  0   0
	 *  	|   |   |
	 *  a2	0   3   0
	 *  	|   |   |
	 *  a3	0   0   2
	 *  	|   |   |
	 *  </pre>
	 * </li>
	 * <li> <b>Select a matching</b><br>
	 * Select a matching by choosing a set of zeros so that each row or column has only one selected.
	 * 	<pre>
	 *  	b1  b2  b3
	 *  a1	10   0  (0)
	 *  a2	(0)  3   0
	 *  a3	 0  (0)  2
	 *  </pre> 
	 *  Apply the matching to the original matrix, disregarding dummy rows. 
	 * 	<pre>
	 *  	b1   b2  b3
	 *  a1	 1    0  (0)
	 *  a2	(5)   1   1
	 *  a3	 0   (6)  1
	 *  </pre>
	 *  Matching found.
	 * </li>
	 * </ol>
	 * 
	 */
	public final void testT1() {
		//TODO: to be done
	}

	
	//TODO: define test cases

	/*
	 * 3) MAXIMUM
	 * 
	 * <pre>
	 *   a1 --- b1
	 *       /
	 *      /
	 *   a2  -- b2
	 *      \ /
	 *      / \
	 *   a3 --- b3
	 * </pre>
	 * 
	 * Find maximum:
	 * <pre>
	 *  	b1  b2  b3
	 *  a1	 1   0   0
	 *  a2	 5   1   1
	 *  a3	 0   6   1
	 *  </pre>
	 *  
	 * <pre>
	 *  	  b1  b2   b3
	 *  a1	 -1    0    0
	 *  a2	 -5   -1   -1
	 *  a3	  0   -6   -1
	 *  </pre> 
	 *  
	 *  +7
	 *  <pre>
	 *  	  b1  b2   b3
	 *  a1	  6    7    7
	 *  a2	  2    6    6
	 *  a3	  7    1    6
	 *  </pre> 
	 *  
	 *  
	 *  
	 *  
	 * <li> <b>Reduce the matrix values</b><br>
	 * 	<pre>
	 *  	  b1  b2   b3
	 *  a1	  0    1    1
	 *  a2	  0    4    4
	 *  a3	  6    0    5
	 *  </pre>
	 *  Reduce the columns by subtracting the minimum value of each column from that column.
	 *	<pre>
	 *  	b1  b2  b3
	 *  a1	  0    1    0
	 *  a2	  0    4    3
	 *  a3	  6    0    4
	 *  </pre> 
	 * </li>
	 * 
	 * <li> <b>Start loop</b><br>
	 *	<pre>
	 *  	  b1  b2  b3
	 *  	  |
	 *  a1	--0---1---0--
	 *  	  |
	 *  a2	  0   4   3
	 *  	  |
	 *  a3	--6---0---4--
	 *  	  |
	 *  </pre> 
	 * </li>
	 *  
	 *  	  b1  b2  b3
	 *  a1	  0    1   (0)
	 *  a2	 (0)   4    3
	 *  a3	  6   (0)   4
	 *  
	 *  Matching:
	 * <pre>
	 *  	b1  b2  b3
	 *  a1	 1   0  (0)
	 *  a2	(5)  1   1
	 *  a3	 0  (6)   1
	 *  </pre>
	 *  
	 *  OK
	 *  
	 */
	
	
	/*
	 * 4) MAXIMUM
	 * 
	 * <pre>
	 *   a1 --- b1
	 *       /
	 *      /
	 *   a2     b2
	 *      \ /
	 *      / \
	 *   a3 --- b3
	 * </pre>
	 * 
	 * Find maximum:
	 * <pre>
	 *  	b1  b2  b3
	 *  a1	 1   0   0
	 *  a2	 5   0   1
	 *  a3	 0   6   1
	 *  </pre>
	 *  
	 * <pre>
	 *  	  b1  b2   b3
	 *  a1	 -1    0    0
	 *  a2	 -5    0   -1
	 *  a3	  0   -6   -1
	 *  </pre> 
	 *  
	 *  +7
	 *  <pre>
	 *  	  b1  b2   b3
	 *  a1	  6    7    7
	 *  a2	  2    7    6
	 *  a3	  7    1    6
	 *  </pre> 
	 *  
	 *  
	 *  
	 *  
	 * <li> <b>Reduce the matrix values</b><br>
	 * 	<pre>
	 *  	  b1  b2   b3
	 *  a1	  0    1    1
	 *  a2	  0    5    4
	 *  a3	  6    0    5
	 *  </pre>
	 *  Reduce the columns by subtracting the minimum value of each column from that column.
	 *	<pre>
	 *  	b1  b2  b3
	 *  a1	  0    1    0
	 *  a2	  0    5    3
	 *  a3	  6    0    4
	 *  </pre> 
	 * </li>
	 * 
	 * <li> <b>Start loop</b><br>
	 *	<pre>
	 *  	  b1  b2  b3
	 *  	  |
	 *  a1	--0---1---0--
	 *  	  |
	 *  a2	  0   5   3
	 *  	  |
	 *  a3	--6---0---4--
	 *  	  |
	 *  </pre> 
	 * </li>
	 *  
	 *  	  b1  b2  b3
	 *  a1	  0    1   (0)
	 *  a2	 (0)   5    3
	 *  a3	  6   (0)   4
	 *  
	 *  Matching:
	 * <pre>
	 *  	b1  b2  b3
	 *  a1	 1   0  (0)
	 *  a2	(5)  1   1
	 *  a3	 0  (6)   1
	 *  </pre>
	 *  
	 */
	
	/*
	 * 5) MAXIMUM
	 * 
	 * <pre>
	 *   a1 --- b1
	 *       /
	 *      /
	 *   a2     b2
	 *       /
	 *      /  
	 *   a3 --- b3
	 * </pre>
	 * 
	 * Find maximum:
	 * <pre>
	 *  	b1  b2  b3
	 *  a1	 1   0   0
	 *  a2	 5   0   0
	 *  a3	 0   6   1
	 *  </pre>
	 *  
	 * <pre>
	 *  	  b1  b2   b3
	 *  a1	 -1    0    0
	 *  a2	 -5    0    0
	 *  a3	  0   -6   -1
	 *  </pre> 
	 *  
	 *  +7
	 *  <pre>
	 *  	  b1  b2   b3
	 *  a1	  6    7    7
	 *  a2	  2    7    7
	 *  a3	  7    1    6
	 *  </pre> 
	 *  
	 *  
	 *  
	 *  
	 * <li> <b>Reduce the matrix values</b><br>
	 * 	<pre>
	 *  	  b1  b2   b3
	 *  a1	  0    1    1
	 *  a2	  0    5    5
	 *  a3	  6    0    5
	 *  </pre>
	 *  Reduce the columns by subtracting the minimum value of each column from that column.
	 *	<pre>
	 *  	b1  b2  b3
	 *  a1	  0    1    0
	 *  a2	  0    5    4
	 *  a3	  6    0    4
	 *  </pre> 
	 * </li>
	 * 
	 * <li> <b>Start loop</b><br>
	 *	<pre>
	 *  	  b1  b2  b3
	 *  	  |
	 *  a1	--0---1---0--
	 *  	  |
	 *  a2	  0   5   4
	 *  	  |
	 *  a3	--6---0---4--
	 *  	  |
	 *  </pre> 
	 * </li>
	 *  
	 *  	  b1  b2  b3
	 *  a1	  0    1   (0)
	 *  a2	 (0)   5    4
	 *  a3	  6   (0)   4
	 *  
	 *  Matching:
	 * <pre>
	 *  	b1  b2  b3
	 *  a1	 1   0  (0)
	 *  a2	(5)  1   1
	 *  a3	 0  (6)   1
	 *  </pre>
	 *  
	 */
	
	/*
	 * 6) MAXIMUM
	 * 
	 * <pre>
	 *   a1 --- b1
	 *       /
	 *      /
	 *   a2  -- b2
	 *      \ /
	 *      / \
	 *   a3 --- b3
	 * </pre>
	 * 
	 * Find maximum:
	 * <pre>
	 *  	b1  b2  b3
	 *  a1	 3   0   0
	 *  a2	 1   3   1
	 *  a3	 0   1   3
	 *  </pre>
	 *  
	 * <pre>
	 *  	  b1  b2   b3
	 *  a1	 -3    0    0
	 *  a2	 -1   -3   -1
	 *  a3	  0   -1   -3
	 *  </pre> 
	 *  
	 *  +4
	 *  <pre>
	 *  	  b1  b2   b3
	 *  a1	  1    4    4
	 *  a2	  3    1    3
	 *  a3	  4    3    1
	 *  </pre> 
	 *  
	 *  
	 *  
	 *  
	 * <li> <b>Reduce the matrix values</b><br>
	 * 	<pre>
	 *  	  b1  b2   b3
	 *  a1	  0    3    3
	 *  a2	  2    0    2
	 *  a3	  3    2    0
	 *  </pre>
	 *  Reduce the columns by subtracting the minimum value of each column from that column.
	 *	<pre>
	 *  	b1  b2  b3
	 *  a1	  0    3    3
	 *  a2	  2    0    2
	 *  a3	  3    2    0
	 *  </pre> 
	 *  NO changes
	 * </li>
	 * 
	 * <li> <b>Start loop</b><br>
	 *	<pre>
	 *  	  b1  b2  b3
	 *  a1	--0---3---3--
	 *  a2	--2---0---2--
	 *  a3	--3---2---0--
	 *  </pre> 
	 * </li>
	 *  
	 *  	  b1  b2  b3
	 *  a1	 (0)   3    3
	 *  a2	  2   (0)   2
	 *  a3	  3    2   (0)
	 *  
	 *  Matching:
	 * <pre>
	 *  	b1  b2  b3
	 *  a1	(3)  0   0
	 *  a2	 1  (3)  1
	 *  a3	 0   1  (3)
	 *  </pre>
	 *  
	 *  OK
	 *  
	 */



	
}
