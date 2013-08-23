package com.drgarbage.algorithms;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.drgarbage.controlflowgraph.NodeExt;
import com.drgarbage.controlflowgraph.intf.IBasicBlock;
import com.drgarbage.controlflowgraph.intf.IDirectedGraphExt;
import com.drgarbage.controlflowgraph.intf.IEdgeListExt;
import com.drgarbage.controlflowgraph.intf.INodeExt;
import com.drgarbage.controlflowgraph.intf.INodeListExt;

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
		
		orderGraphNodesAscending();
		
		if(!nodesHaveSameIncomingAndOutgoingEdgeCount()) return false;
		
		return true;
	}

	private void orderGraphNodesAscending() {
//		Collections.sort((List<NodeExt>) cfgLeft.getNodeList());
//		Collections.sort((List<NodeExt>) cfgRight.getNodeList());
	}

	private boolean haveSameNodeCount(){
		return cfgLeft.getNodeList().size() == cfgRight.getNodeList().size();
	}
	
	private boolean haveSameEdgeCount(){
		return cfgLeft.getEdgeList().size() == cfgRight.getEdgeList().size();
	}
	
	private boolean nodesHaveSameIncomingAndOutgoingEdgeCount() {
		// TODO Auto-generated method stub
		return false;
	}
	
}
