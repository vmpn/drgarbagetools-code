package com.drgarbage.algorithms;

import java.awt.font.ImageGraphicAttribute;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.TreeMap;

import org.eclipse.swt.widgets.Tree;

import com.drgarbage.controlflowgraph.ControlFlowGraphException;
import com.drgarbage.controlflowgraph.intf.GraphUtils;
import com.drgarbage.controlflowgraph.intf.IBasicBlock;
import com.drgarbage.controlflowgraph.intf.IDirectedGraphExt;
import com.drgarbage.controlflowgraph.intf.IEdgeExt;
import com.drgarbage.controlflowgraph.intf.IEdgeListExt;
import com.drgarbage.controlflowgraph.intf.INodeExt;
import com.drgarbage.controlflowgraph.intf.INodeListExt;

/**
 * Class to compare two ControlFlowGraphs
 * @author Adam Kajrys, Andreas Karoly, Artem Garishin
 *
 * @version $Revision$
 * $Id$
 */
public class ControlFlowGraphCompare {
	
	private IDirectedGraphExt cfgLeft = null;
	private IDirectedGraphExt cfgRight = null;
	private IEdgeListExt backEdgesCfgLeft = null, backEdgesCfgRight = null;
	private IDirectedGraphExt cfgLeftSpanningTree = null, cfgRightSpanningTree = null, basicBlockGraphLeftSpanningTree = null, basicBlockGraphRightSpanningTree = null;
	
	public ControlFlowGraphCompare(IDirectedGraphExt cfgLeft, IDirectedGraphExt cfgRight){
		this.cfgLeft = cfgLeft;
		this.cfgRight = cfgRight;
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
	 * 1 try to get TD traversal
	 * @param graph
	 * @return
	 */
	private boolean topDownTreeTraversal(IDirectedGraphExt graph) {
		
		//cfgLeftSpanningTree = Algorithms.doOrderedSpanningTreeAlgorithm(graph, false);
		backEdgesCfgLeft = removeBackEdges(graph);
		
		INodeExt root = graph.getNodeList().getNodeExt(0);
		TopDownTreeTraversal tdtt = new TopDownTreeTraversal();
		
		try {
			tdtt.start(graph, root);
		} catch (ControlFlowGraphException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
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
		return true;	
	}
	
	
	private boolean topDownOrderedSubtreeBasicBlock(IBasicBlock node1, IBasicBlock node2) {
		boolean isIsomorph = true;
		return isIsomorph;
	}

	private boolean topDownOrderedSubtree(INodeExt node1, INodeExt node2) {
		boolean isIsomorph = true;
		return isIsomorph;
	}

	public boolean topDownOrderedSubtreeIsomorphism(IDirectedGraphExt graphLeft, IDirectedGraphExt graphRight) {
		
		backEdgesCfgLeft = removeBackEdges(graphLeft);
		backEdgesCfgRight = removeBackEdges(graphRight);
		
		INodeExt root1 = graphLeft.getNodeList().getNodeExt(0);		
		INodeExt root2 = graphRight.getNodeList().getNodeExt(0);
		
		TopDownTreeTraversal tdtt = new TopDownTreeTraversal();
		
		try {
			tdtt.start(graphLeft, root1);
			//tdtt.start(graphRight, root2);
		} catch (ControlFlowGraphException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		System.out.println("left graph:");
		for(int i = 0; i < graphLeft.getNodeList().size(); i++) {
			System.out.println(graphLeft.getNodeList().getNodeExt(i).getCounter());
		}
		
		System.out.println("left right:");
		for(int i = 0; i < graphRight.getNodeList().size(); i++) {
			System.out.println(graphRight.getNodeList().getNodeExt(i).getCounter());
		}
		
		if(graphLeft.getNodeList().size() > graphRight.getNodeList().size())
			return false;
		
		INodeExt rootLeft = graphLeft.getNodeList().getNodeExt(0);
		INodeExt rootRight = graphRight.getNodeList().getNodeExt(0);
		
		if(mapOrderedSubtree(rootLeft, rootRight))
			return true;
		
		return false;
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
			
//			ArrayList<IEdgeExt> node1SortedEdges = sortEdges(node1OutgoingEdges);
//			ArrayList<IEdgeExt> node2SortedEdges = sortEdges(node2OutgoingEdges);
			
//			v1 = node1SortedEdges.get(0).getTarget();
//			v2 = node2SortedEdges.get(0).getTarget();
			
			v1 = node1OutgoingEdges.getEdgeExt(0).getTarget();
			v2 = node2OutgoingEdges.getEdgeExt(0).getTarget();
			
			if(!mapOrderedSubtree(v1, v2))
				return false;
			
			for(int i = 1; i < node1ChildCount; i++) {
				System.out.println("node " + node1OutgoingEdges.getEdgeExt(i).getSource().getCounter());
				System.out.println("ololodasdas    " + i + " of " + node1ChildCount);
//				v1 = node1SortedEdges.get(i).getTarget();
//				v2 = node2SortedEdges.get(i).getTarget();
				
				v1 = node1OutgoingEdges.getEdgeExt(i).getTarget();
				v2 = node2OutgoingEdges.getEdgeExt(i).getTarget();
				
				System.out.println("passed");
				if(!mapOrderedSubtree(v1, v2));
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
	
	

}
