package com.drgarbage.algorithms;

import java.util.ArrayList;
import com.drgarbage.controlflowgraph.intf.IDirectedGraphExt;
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
	
	public ControlFlowGraphCompare(IDirectedGraphExt cfgLeft, IDirectedGraphExt cfgRight){
		this.cfgLeft = cfgLeft;
		this.cfgRight = cfgRight;
	}

	public boolean isIsomorph() {
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
		
		System.out.println("NEW COMPARE:");
		
		ArrayList<INodeExt> cfgLeftOrderdNodes = Algorithms.doBFSOrderedTraversal(cfgLeft);
		ArrayList<INodeExt> cfgRightOrderdNodes = Algorithms.doBFSOrderedTraversal(cfgRight);
		System.out.println(cfgLeftOrderdNodes.size());
		
		for(int i = 0; i < cfgLeftOrderdNodes.size(); i++){
			
			INodeExt leftNode = cfgLeftOrderdNodes.get(i);
			INodeExt rightNode = cfgRightOrderdNodes.get(i);
			
			System.out.println("LeftNode: "+leftNode.getByteCodeString() +" , "+leftNode.getByteCodeOffset()+" , "+leftNode.getIncomingEdgeList().size()+"/"+leftNode.getOutgoingEdgeList().size());
			System.out.println("RightNode: "+rightNode.getByteCodeString() +" , "+rightNode.getByteCodeOffset()+" , "+rightNode.getIncomingEdgeList().size()+"/"+rightNode.getOutgoingEdgeList().size());

			
			if(leftNode.getIncomingEdgeList().size() != rightNode.getIncomingEdgeList().size() ||
					leftNode.getOutgoingEdgeList().size() != rightNode.getOutgoingEdgeList().size())
				return false;
		}

		return true;
	}
	
}
