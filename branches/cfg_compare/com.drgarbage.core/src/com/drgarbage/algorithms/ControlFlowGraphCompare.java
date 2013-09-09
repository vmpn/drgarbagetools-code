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
		
		if(!haveSameEdgeCount()) return false;
		
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
		return true;
		
//		System.out.println("NEW COMPARE:");
//		
//		ArrayList<INodeExt> cfgLeftOrderdNodes = Algorithms.doBFSOrderedTraversal(cfgLeft);
//		ArrayList<INodeExt> cfgRightOrderdNodes = Algorithms.doBFSOrderedTraversal(cfgRight);
//		System.out.println(cfgLeftOrderdNodes.size());
//		
//		for(int i = 0; i < cfgLeftOrderdNodes.size(); i++){
//			
//			INodeExt leftNode = cfgLeftOrderdNodes.get(i);
//			INodeExt rightNode = cfgRightOrderdNodes.get(i);
//			
//			System.out.println("LeftNode: "+leftNode.getByteCodeString() +" , "+leftNode.getByteCodeOffset()+" , "+leftNode.getIncomingEdgeList().size()+"/"+leftNode.getOutgoingEdgeList().size());
//			System.out.println("RightNode: "+rightNode.getByteCodeString() +" , "+rightNode.getByteCodeOffset()+" , "+rightNode.getIncomingEdgeList().size()+"/"+rightNode.getOutgoingEdgeList().size());
//
//			
//			if(leftNode.getIncomingEdgeList().size() != rightNode.getIncomingEdgeList().size() ||
//					leftNode.getOutgoingEdgeList().size() != rightNode.getOutgoingEdgeList().size())
//				return false;
//		}
//
//		return true;
	}
	
}
