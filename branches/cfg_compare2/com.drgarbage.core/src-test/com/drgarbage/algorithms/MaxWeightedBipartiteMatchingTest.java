package com.drgarbage.algorithms;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.drgarbage.algorithms.MaxBipartiteMatchingTest.TestSet;
import com.drgarbage.controlflowgraph.intf.GraphExtentionFactory;
import com.drgarbage.controlflowgraph.intf.IDirectedGraphExt;
import com.drgarbage.controlflowgraph.intf.IEdgeExt;
import com.drgarbage.controlflowgraph.intf.INodeExt;

import junit.framework.TestCase;

/**
* @author Artem Garishin
* @version $Revision$
* $Id$
*/
public class MaxWeightedBipartiteMatchingTest extends TestCase{

	/**
	 * Matching algorithm to be tested.
	 */
	MaxWeightedBipartiteMatching m = new MaxWeightedBipartiteMatching();
	
	/**
	 * Test set consists of a bipartite graph and two partitions.
	 */
	class TestSet{
		 IDirectedGraphExt graph = GraphExtentionFactory.createDirectedGraphExtention();
		 List<INodeExt> partA = new ArrayList<INodeExt>(); 
		 List<INodeExt> partB = new ArrayList<INodeExt>();
	}
	
	/**
	 * Prints the graph.
	 * @param g the graph
	 */
	private static void printGraph(IDirectedGraphExt g) {

		System.out.println("Print Graph:");

		System.out.println("Nodes:");
		for (int i = 0; i < g.getNodeList().size(); i++) {
			System.out.println("  " + g.getNodeList().getNodeExt(i).getData());
		}

		System.out.println("Edges:");
		for (int i = 0; i < g.getEdgeList().size(); i++) {
			IEdgeExt e = g.getEdgeList().getEdgeExt(i);
			System.out.println("  " 
					+ e.getSource().getData() 
					+ " - "
					+ e.getTarget().getData()
					+ " : "
					+ e.getCounter());
		}
	}
	
	/**
	 * Prints the matched edges list.
	 * @param edges matching set
	 */
	private static void printMatchedEdges(Set<IEdgeExt> edges){
		System.out.println("Matched edges:");
		for(IEdgeExt e: edges){
			System.out.println("  " 
					+ e.getSource().getData() 
					+ " - "
					+ e.getTarget().getData()
					+ " : "
					+ e.getCounter());
		}
	}
	
	/**
	 * TODO: describe the graph
	 * 
	 * 
	 * @return the test set
	 */
	private TestSet createTestSet1(){
		TestSet t = new TestSet();
	   //PartA
       INodeExt a1 = GraphExtentionFactory.createNodeExtention("a1");
       t.graph.getNodeList().add(a1);
       t.partA.add(a1);
       
       INodeExt a2 = GraphExtentionFactory.createNodeExtention("a2");
       t.graph.getNodeList().add(a2);
       t.partA.add(a2);
       
       INodeExt a3 = GraphExtentionFactory.createNodeExtention("a3");
       t.graph.getNodeList().add(a3);
       t.partA.add(a3);
	   
       //PartB       
       INodeExt b1 = GraphExtentionFactory.createNodeExtention("b1");
       t.graph.getNodeList().add(b1);
       t.partB.add(b1);
       
       INodeExt b2 = GraphExtentionFactory.createNodeExtention("b2");
       t.graph.getNodeList().add(b2);
       t.partB.add(b2);
       
       INodeExt b3 = GraphExtentionFactory.createNodeExtention("b3");
       t.graph.getNodeList().add(b3);
       t.partB.add(b3);
       
       INodeExt b4 = GraphExtentionFactory.createNodeExtention("b4");
       t.graph.getNodeList().add(b4);
       t.partB.add(b4);
       
       
       //set edges and weights
       IEdgeExt edge = GraphExtentionFactory.createEdgeExtention(a3, b3);
       edge.setCounter(5);
       t.graph.getEdgeList().add(edge);
       
       edge = GraphExtentionFactory.createEdgeExtention(a1, b1);
       edge.setCounter(2);
       t.graph.getEdgeList().add(edge);
       
       edge = GraphExtentionFactory.createEdgeExtention(a2, b1);
       edge.setCounter(6);
       t.graph.getEdgeList().add(edge);
       
       edge = GraphExtentionFactory.createEdgeExtention(a1, b2);
       edge.setCounter(8);
       t.graph.getEdgeList().add(edge);
       
       edge = GraphExtentionFactory.createEdgeExtention(a2, b3);
       edge.setCounter(9);
       t.graph.getEdgeList().add(edge);

       edge = GraphExtentionFactory.createEdgeExtention(a2, b2);
       edge.setCounter(4);
       t.graph.getEdgeList().add(edge);
       
       edge = GraphExtentionFactory.createEdgeExtention(a1, b4);
       edge.setCounter(1);
       t.graph.getEdgeList().add(edge);
       
       return t;
	}
	
	/**
	 * The test set 2. <br>
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
	 * (a1 - b1)  3
	 * (a2 - b1)  5
	 * (a2 - b2)  3
	 * (a2 - b3)  1
	 * (a3 - b2)  1
	 * (a3 - b3)  3
	 * </pre>
	 * 
	 * Expected matching <code>M = (a1-b1, a2-b2, a3-b3)</code> 
	 * with |M|=3 and W = 3 + 3 + 3 = 9:
	 * <pre>
	 *   a1 --- b1
	 *   
	 *      
	 *   a2  -- b2
	 *      
	 *      
	 *   a3 --- b3
	 * 
	 * </pre>*/
	private TestSet createTestSet2(){
		TestSet t = new TestSet();
	   //PartA
       INodeExt a1 = GraphExtentionFactory.createNodeExtention("a1");
       t.graph.getNodeList().add(a1);
       t.partA.add(a1);
       INodeExt a2 = GraphExtentionFactory.createNodeExtention("a2");
       t.graph.getNodeList().add(a2);
       t.partA.add(a2);
       INodeExt a3 = GraphExtentionFactory.createNodeExtention("a3");
       t.graph.getNodeList().add(a3);
       t.partA.add(a3);
       
       //PartB       
       INodeExt b1 = GraphExtentionFactory.createNodeExtention("b1");
       t.graph.getNodeList().add(b1);
       t.partB.add(b1);
       INodeExt b2 = GraphExtentionFactory.createNodeExtention("b2");
       t.graph.getNodeList().add(b2);
       t.partB.add(b2);
       INodeExt b3 = GraphExtentionFactory.createNodeExtention("b3");
       t.graph.getNodeList().add(b3);
       t.partB.add(b3);
       
       
       //set edges and weights
       
       IEdgeExt edge = GraphExtentionFactory.createEdgeExtention(a1, b1);
       edge.setCounter(3);
       t.graph.getEdgeList().add(edge);
       
       edge = GraphExtentionFactory.createEdgeExtention(a2, b1);
       edge.setCounter(5);
       t.graph.getEdgeList().add(edge);
		       
       edge = GraphExtentionFactory.createEdgeExtention(a2, b2);
       edge.setCounter(3);
       t.graph.getEdgeList().add(edge);

       edge = GraphExtentionFactory.createEdgeExtention(a2, b3);
       edge.setCounter(1);
       t.graph.getEdgeList().add(edge);
       
       edge = GraphExtentionFactory.createEdgeExtention(a3, b2);
       edge.setCounter(1);
       t.graph.getEdgeList().add(edge);

       edge = GraphExtentionFactory.createEdgeExtention(a3, b3);
       edge.setCounter(3);
       t.graph.getEdgeList().add(edge);


		return t;
	}
	/**
	 * The test set 3. <br>
	 * The graph <code>G = (A + B, E)</code>:
	 * <pre>
	 * TODO: change the layout: 
	 *   a1 ----- b1
	 *       \ /
	 *       /\
	 *   a2 ---\-- b2
	 *          \
	 *           \
	 *   	      b3
	 * </pre>
	 * <code>A =(a1, a2)</code>, <code>B =(b1, b2, b3)</code>
	 * and <code>E =((a1 - b1), (a1 - b3), (a2 - b1), (a2 - b2)</code>
	 * <br>
	 * 
	 * The following weights are assigned to the edges:
	 * <pre>
	 * (a1 - b1)  10
	 * (a1 - b3)  5
	 * (a2 - b1)  11
	 * (a2 - b2)  9
	 * 
	 *  Expected matching <code>M = (a1-b1, a2-b2)</code> 
	 * with |M|=2 and W = 10 + 9 = 19:
	 * <pre>
	 *   a1 --10-- b1
	 *   
	 *      
	 *   a2 --9--- b2
	 *      
	 *      
	 *   		   b3
	 * </pre> 
	 * */
	private TestSet createTestSet3(){
		TestSet t = new TestSet();
	   //PartA
       INodeExt a1 = GraphExtentionFactory.createNodeExtention("a1");
       t.graph.getNodeList().add(a1);
       t.partA.add(a1);
       
       INodeExt a2 = GraphExtentionFactory.createNodeExtention("a2");
       t.graph.getNodeList().add(a2);
       t.partA.add(a2);
       
       //PartB       
       INodeExt b2 = GraphExtentionFactory.createNodeExtention("b2");
       t.graph.getNodeList().add(b2);
       t.partB.add(b2);
       INodeExt b3 = GraphExtentionFactory.createNodeExtention("b3");
       t.graph.getNodeList().add(b3);
       t.partB.add(b3);
       INodeExt b1 = GraphExtentionFactory.createNodeExtention("b1");
       t.graph.getNodeList().add(b1);
       t.partB.add(b1);
       
       
       //set edges and weights
       
       IEdgeExt edge = GraphExtentionFactory.createEdgeExtention(a1, b1);
       edge.setCounter(10);
       t.graph.getEdgeList().add(edge);
       
       edge = GraphExtentionFactory.createEdgeExtention(a1, b3);
       edge.setCounter(5);
       t.graph.getEdgeList().add(edge);
       
       edge = GraphExtentionFactory.createEdgeExtention(a2, b1);
       edge.setCounter(11);
       t.graph.getEdgeList().add(edge);
		       
       edge = GraphExtentionFactory.createEdgeExtention(a2, b2);
       edge.setCounter(9);
       t.graph.getEdgeList().add(edge);

       return t;
	}
	
	/**
	 * Test method for {@link com.drgarbage.algorithms.MaxBipartiteMatching}
	 * @see #createTestSet1()
	 */
	public final void testMaxCardBipartiteMatching1() {	
		TestSet t = createTestSet1();
		//printGraph(t.graph);
		m.start(t.graph, t.partA, t.partB);
		
		printMatchedEdges(m.getMatchedEdges());
		assertEquals(19, m.getMaxWeightMatchedEdges());
	}
	
	/**
	 * Test method for {@link com.drgarbage.algorithms.MaxBipartiteMatching}
	 * @see #createTestSet2()
	 */
	public final void testMaxCardBipartiteMatching2() {	
		TestSet t = createTestSet2();
		//printGraph(t.graph);
		m.start(t.graph, t.partA, t.partB);
		
		printMatchedEdges(m.getMatchedEdges());
		assertEquals(9, m.getMaxWeightMatchedEdges());
	}
	
	/**
	 * Test method for {@link com.drgarbage.algorithms.MaxBipartiteMatching}
	 * @see #createTestSet2()
	 */
	public final void testMaxCardBipartiteMatching3() {	
		TestSet t = createTestSet3();
		//printGraph(t.graph);
		m.start(t.graph, t.partA, t.partB);
		
		printMatchedEdges(m.getMatchedEdges());
		assertEquals(19, m.getMaxWeightMatchedEdges());
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

}
