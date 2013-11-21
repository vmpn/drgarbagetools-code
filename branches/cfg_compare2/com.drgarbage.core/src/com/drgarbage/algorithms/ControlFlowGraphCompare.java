package com.drgarbage.algorithms;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.TreeMap;

import com.drgarbage.controlflowgraph.ControlFlowGraphException;
import com.drgarbage.controlflowgraph.intf.GraphUtils;
import com.drgarbage.controlflowgraph.intf.IDirectedGraphExt;
import com.drgarbage.controlflowgraph.intf.IEdgeExt;
import com.drgarbage.controlflowgraph.intf.IEdgeListExt;
import com.drgarbage.controlflowgraph.intf.INodeExt;
import com.drgarbage.controlflowgraph.intf.INodeListExt;

/**
 * Class to compare two ControlFlowGraphs
 * @author Artem Garishin, Adam Kajrys, Andreas Karoly
 *
 * @version $Revision$
 * $Id$
 */
public class ControlFlowGraphCompare {
	
	private IDirectedGraphExt cfgLeft = null;
	private IDirectedGraphExt cfgRight = null;
	private IEdgeListExt backEdgesCfgLeft = null, backEdgesCfgRight = null;
	private IDirectedGraphExt cfgLeftSpanningTree = null, cfgRightSpanningTree = null; 
	private IDirectedGraphExt basicBlockGraphLeftSpanningTree = null, basicBlockGraphRightSpanningTree = null;
	
	public ControlFlowGraphCompare(IDirectedGraphExt cfgLeft, IDirectedGraphExt cfgRight){
		this.cfgLeft = cfgLeft;
		this.cfgRight = cfgRight;
	}
	
	
	public boolean topDownOrderedSubtreeIsomorphism(IDirectedGraphExt graphLeft, IDirectedGraphExt graphRight) {
		
		backEdgesCfgLeft = removeBackEdges(graphLeft);
		backEdgesCfgRight = removeBackEdges(graphRight);
		
		// question: operating on a spanning tree decreases complexity but then we'll also have to check the removed
		// edges. also we have to think about a strategy to remove the edges.
		// see Algorithms.doOrderedSpanningTreeAlgorithm() in old branch
		// for now this is sufficient because we order according to the storing sequence
		cfgLeftSpanningTree = Algorithms.doSpanningTreeAlgorithm(graphLeft, true);
		cfgRightSpanningTree = Algorithms.doSpanningTreeAlgorithm(graphRight, true);
		
		/* clear visited flags in nodes and edges */
		GraphUtils.clearGraph(graphLeft);
		GraphUtils.clearGraph(graphRight);
		
		// just for debug purposes, we should be using the spanning tree
		INodeExt graphRoot1 = graphLeft.getNodeList().getNodeExt(0);		
		INodeExt graphRoot2 = graphRight.getNodeList().getNodeExt(0);
		
		INodeExt root1 = cfgLeftSpanningTree.getNodeList().getNodeExt(0);		
		INodeExt root2 = cfgRightSpanningTree.getNodeList().getNodeExt(0);
		
		TopDownTreeTraversal tdtt = new TopDownTreeTraversal();
		
		try {
			tdtt.traverse(graphLeft, graphRoot1);
			tdtt.traverse(graphRight, graphRoot2);
			
			tdtt.traverse(cfgLeftSpanningTree, root1);
			tdtt.traverse(cfgRightSpanningTree, root2);
			
		} catch (ControlFlowGraphException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		// print method for debugging
		printGraph(graphLeft, "graph left");
		printGraph(cfgLeftSpanningTree, "spanning tree left");
		printGraph(cfgRightSpanningTree, "spanning tree right");
		
		
		if(cfgLeftSpanningTree.getNodeList().size() > cfgRightSpanningTree.getNodeList().size())
			return false;
		
		INodeExt rootLeft = cfgLeftSpanningTree.getNodeList().getNodeExt(0);
		INodeExt rootRight = cfgRightSpanningTree.getNodeList().getNodeExt(0);
		
		
		if(mapOrderedSubtree(rootLeft, rootRight))
			return true;
		
		return false;
		
		// TODO: check removed edges
	}

	private boolean mapOrderedSubtree(INodeExt node1, INodeExt node2) {
		
		if(node1.getCounter() != node2.getCounter()) 
			return false;
		
		IEdgeListExt node1OutgoingEdges = node1.getOutgoingEdgeList();
		IEdgeListExt node2OutgoingEdges = node2.getOutgoingEdgeList();
		
		int node1ChildCount = node1OutgoingEdges.size();
		int node2ChildCount = node2OutgoingEdges.size();
		
		if(node1ChildCount > node2ChildCount) 
			return false;

		INodeExt v1, v2;
		
		if(node1ChildCount > 0) {
			
			ArrayList<IEdgeExt> node1SortedEdges = sortEdges(node1OutgoingEdges);
			ArrayList<IEdgeExt> node2SortedEdges = sortEdges(node2OutgoingEdges);
			
			v1 = node1SortedEdges.get(0).getTarget();
			v2 = node2SortedEdges.get(0).getTarget();
			
			
			// do we really need sortEdges() in this scenario? we traverse in the storing sequence anyway
//			v1 = node1OutgoingEdges.getEdgeExt(0).getTarget();
//			v2 = node2OutgoingEdges.getEdgeExt(0).getTarget();
			
			if(!mapOrderedSubtree(v1, v2))
				return false;
			
			for(int i = 1; i < node1ChildCount; i++) {
				v1 = node1SortedEdges.get(i).getTarget();
				v2 = node2SortedEdges.get(i).getTarget();
				
//				v1 = node1OutgoingEdges.getEdgeExt(i).getTarget();
//				v2 = node2OutgoingEdges.getEdgeExt(i).getTarget();
				
				if(!mapOrderedSubtree(v1, v2))
					return false;
			}
		}
		
		return true;
	}

	private ArrayList<IEdgeExt> sortEdges(IEdgeListExt edgeList) {
		TreeMap<Integer, IEdgeExt> tmpEdgeList = new TreeMap<Integer, IEdgeExt>();
		
		for(int i = 0; i < edgeList.size(); i++){
			tmpEdgeList.put(edgeList.getEdgeExt(i).getTarget().getCounter(), edgeList.getEdgeExt(i));
		}
		
		return new ArrayList<IEdgeExt>(tmpEdgeList.values());
	}
	
	/**
	 * Removes all back edges from the edge list and 
	 * incidence lists of nodes.
	 * @param graph control flow graph
	 */
	private IEdgeListExt removeBackEdges(IDirectedGraphExt graph){
		
		IEdgeListExt backEdges = Algorithms.doFindBackEdgesAlgorithm(graph);
		GraphUtils.clearGraph(graph);
		GraphUtils.clearGraphColorMarks(graph);
		
		IEdgeListExt edges = graph.getEdgeList();
		for(int i = 0; i < backEdges.size(); i++){
			IEdgeExt e = backEdges.getEdgeExt(i);
			INodeExt source = e.getSource(); 
			INodeExt target = e.getTarget();
			
			source.getOutgoingEdgeList().remove(e);
			target.getIncomingEdgeList().remove(e);
			edges.remove(e);
		}
		
		return backEdges;
	}
	
	/**
	 * FOR DEBUG PURPOSES ONLY
	 * @param graph
	 */
	private void artemsDebugPrinter(IDirectedGraphExt graph) {
		//DEBUG PURPOSES
		PrintWriter writer;
		try {
			writer = new PrintWriter("E:/Programms/debug.txt", "UTF-8");
			writer.println("nodes:");
			INodeListExt nodes = graph.getNodeList();
			for(int i = 0; i < nodes.size(); i++){
				writer.println("  " + nodes.getNodeExt(i).getCounter());
				nodes.getNodeExt(i).setLongDescr(Integer.toString(nodes.getNodeExt(i).getCounter()));
				//nodes.getNodeExt(i).set
				writer.println(nodes.getNodeExt(i).getLongDescr());
			}
			
			writer.println("edges:");
			
			IEdgeListExt edges = graph.getEdgeList();
			for(int i = 0; i < edges.size(); i++ ){
				writer.println("  " + edges.getEdgeExt(i).getSource().getCounter() 
						+ "->" + edges.getEdgeExt(i).getTarget().getCounter());
			}
			writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//END DEBUG PURPOSES
	}
	
	/* debug only */
	private void printGraph(IDirectedGraphExt g, String s) {
		
		System.out.println("\n" + s);
		
		System.out.println("nodes:");
		for(int i = 0; i < g.getNodeList().size(); i++) {
			System.out.println(g.getNodeList().getNodeExt(i).getCounter());
		}
		
		System.out.println("edges:");
		for(int i = 0; i < g.getEdgeList().size(); i++) {
			IEdgeExt e = g.getEdgeList().getEdgeExt(i);		
			System.out.println(i + ": " + e.getSource().getCounter() + " -> " + e.getTarget().getCounter());
		}
		
	}
}
