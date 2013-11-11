package com.drgarbage.algorithms;

import java.awt.font.ImageGraphicAttribute;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import org.eclipse.swt.widgets.Tree;
import com.drgarbage.controlflowgraph.intf.GraphUtils;
import com.drgarbage.controlflowgraph.intf.IBasicBlock;
import com.drgarbage.controlflowgraph.intf.IDirectedGraphExt;
import com.drgarbage.controlflowgraph.intf.IEdgeExt;
import com.drgarbage.controlflowgraph.intf.IEdgeListExt;
import com.drgarbage.controlflowgraph.intf.INodeExt;

/**
 * Class to compare two ControlFlowGraphs
 * @author Sergej Alekseev, Artem Garishin
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
	
	private boolean topDownOrderedSubtreeBasicBlock(IBasicBlock node1, IBasicBlock node2) {
		boolean isIsomorph = true;
		return isIsomorph;
	}

	private boolean topDownOrderedSubtree(INodeExt node1, INodeExt node2) {
		boolean isIsomorph = true;
		return isIsomorph;
	}

}
