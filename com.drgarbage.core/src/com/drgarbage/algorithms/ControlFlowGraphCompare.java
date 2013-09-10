package com.drgarbage.algorithms;

import java.util.ArrayList;

import com.drgarbage.controlflowgraph.intf.GraphUtils;
import com.drgarbage.controlflowgraph.intf.IDirectedGraphExt;
import com.drgarbage.controlflowgraph.intf.IEdgeExt;
import com.drgarbage.controlflowgraph.intf.IEdgeListExt;
import com.drgarbage.controlflowgraph.intf.INodeExt;

/**
 * Class to compare two ControlFlowGraphs
 * @author Andreas Karoly, Adam Kajrys
 *
 * @version $Revision$
 * $Id$
 */
public class ControlFlowGraphCompare {
	
	private IDirectedGraphExt cfgLeft = null;
	private IDirectedGraphExt cfgRight = null;
	private IEdgeListExt backEdgesCfgLeft = null, backEdgesCfgRight = null;
	
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

	public boolean isIsomorph() {
		
		backEdgesCfgLeft = removeBackEdges(cfgLeft);
		backEdgesCfgRight = removeBackEdges(cfgRight);
		
		if(!haveSameNodeCount()) return false;
		
		//if(!haveSameEdgeCount()) return false;
		
		if(!nodesHaveSameIncomingAndOutgoingEdgeCount()) return false;
		
		return true;
	}

	private boolean haveSameNodeCount(){
		return cfgLeft.getNodeList().size() == cfgRight.getNodeList().size();
	}
	
	private boolean haveSameEdgeCount(){
		return cfgLeft.getEdgeList().size() == cfgRight.getEdgeList().size();
	}
	
	private boolean nodesHaveSameIncomingAndOutgoingEdgeCount() {
		
		boolean isomorph = true;
		
		System.out.println("NEW COMPARE:");
		
		IDirectedGraphExt cfgLeftSpanningTree = Algorithms.doOrderedSpanningTreeAlgorithm(cfgLeft, false);
		IDirectedGraphExt cfgRightSpanningTree = Algorithms.doOrderedSpanningTreeAlgorithm(cfgRight, false);
		
		Algorithms.printGraph(cfgLeftSpanningTree);
		Algorithms.printGraph(cfgRightSpanningTree);
		
		for(int i = 0; i < cfgLeftSpanningTree.getNodeList().size(); i++){
			boolean tmp = true;
			
			INodeExt leftNode = cfgLeftSpanningTree.getNodeList().getNodeExt(i);
			INodeExt rightNode = cfgRightSpanningTree.getNodeList().getNodeExt(i);
			
			System.out.println("LeftNode: "+leftNode.getByteCodeString() +" , "+leftNode.getByteCodeOffset()+" , "+leftNode.getIncomingEdgeList().size()+"/"+leftNode.getOutgoingEdgeList().size());
			System.out.println("RightNode: "+rightNode.getByteCodeString() +" , "+rightNode.getByteCodeOffset()+" , "+rightNode.getIncomingEdgeList().size()+"/"+rightNode.getOutgoingEdgeList().size());

			if(leftNode.getByteCodeOffset() != rightNode.getByteCodeOffset()) {
				isomorph = false;
				tmp = false;
			}
			
			if(leftNode.getOutgoingEdgeList().size() != rightNode.getOutgoingEdgeList().size()) {
				isomorph = false;
				tmp = false;
			}
			
			if(tmp) {
				leftNode.setHighlighted(true);
				rightNode.setHighlighted(true);
			}
		}

		return isomorph;
	}
	
}
