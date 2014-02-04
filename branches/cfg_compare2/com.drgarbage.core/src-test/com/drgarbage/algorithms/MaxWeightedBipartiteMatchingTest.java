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
       edge.setCounter(10);
       t.graph.getEdgeList().add(edge);

       edge = GraphExtentionFactory.createEdgeExtention(a2, b2);
       edge.setCounter(4);
       t.graph.getEdgeList().add(edge);
       
       edge = GraphExtentionFactory.createEdgeExtention(a1, b4);
       edge.setCounter(1);
       t.graph.getEdgeList().add(edge);
       
		       
       
		return t;
	}
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
	 * Test method for {@link com.drgarbage.algorithms.MaxBipartiteMatching}
	 * @see #createTestSet1()
	 */
	public final void testMaxCardBipartiteMatching1() {	
		TestSet t = createTestSet1();
		//printGraph(t.graph);
		m.start(t.graph, t.partA, t.partB);
		
		//printMatchedEdges(m.getMatchedEdges());
		//assertEquals(19, m.getMaxWeightAll());
	}
	/**
	 * Test method for {@link com.drgarbage.algorithms.MaxBipartiteMatching}
	 * @see #createTestSet2()
	 */
	public final void testMaxCardBipartiteMatching2() {	
		TestSet t = createTestSet2();
		//printGraph(t.graph);
		m.start(t.graph, t.partA, t.partB);
		
		//printMatchedEdges(m.getMatchedEdges());
		//assertEquals(19, m.getMaxWeightAll());
	}

}
