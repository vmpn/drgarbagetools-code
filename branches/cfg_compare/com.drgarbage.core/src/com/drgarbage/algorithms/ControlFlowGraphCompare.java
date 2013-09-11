package com.drgarbage.algorithms;

import java.awt.font.ImageGraphicAttribute;
import java.util.ArrayList;

import com.drgarbage.controlflowgraph.ControlFlowGraphException;
import com.drgarbage.controlflowgraph.ControlFlowGraphGenerator;
import com.drgarbage.controlflowgraph.intf.GraphUtils;
import com.drgarbage.controlflowgraph.intf.IBasicBlock;
import com.drgarbage.controlflowgraph.intf.IDirectedGraphExt;
import com.drgarbage.controlflowgraph.intf.IEdgeExt;
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

	public boolean isIsomorphCFG() {
		
		backEdgesCfgLeft = removeBackEdges(cfgLeft);
		backEdgesCfgRight = removeBackEdges(cfgRight);
		
		cfgLeftSpanningTree = Algorithms.doOrderedSpanningTreeAlgorithm(cfgLeft, false);
		cfgRightSpanningTree = Algorithms.doOrderedSpanningTreeAlgorithm(cfgRight, false);
		
		INodeExt root1 = cfgLeftSpanningTree.getNodeList().getNodeExt(0);
		INodeExt root2 = cfgRightSpanningTree.getNodeList().getNodeExt(0);
		
		if(cfgLeftSpanningTree.getNodeList().size() > cfgRightSpanningTree.getNodeList().size()) {
			if(topDownOrderedSubtree(root1, root2)) return true;
		}
		
		else {
			if(topDownOrderedSubtree(root2, root1)) return true;
		}
		
		return false;
	}
	
	public boolean isIsomorphBasicBlock() {
		
		backEdgesCfgLeft = removeBackEdges(cfgLeft);
		backEdgesCfgRight = removeBackEdges(cfgRight);
		
		/* find basic blocks of graphs */
		GraphUtils.clearGraph(cfgLeft);
		BasicBlockGraphVisitor basicBlockVisitor1 = new BasicBlockGraphVisitor();
		try {
			basicBlockVisitor1.start(cfgLeft);
		} catch (ControlFlowGraphException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		IDirectedGraphExt basicBlockGraphLeft = basicBlockVisitor1.getBasicBlockGraph();
		
		GraphUtils.clearGraph(cfgRight);
		BasicBlockGraphVisitor basicBlockVisitor2 = new BasicBlockGraphVisitor();
		
		try {
			basicBlockVisitor2.start(cfgRight);
		} catch (ControlFlowGraphException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		IDirectedGraphExt basicBlockGraphRight = basicBlockVisitor2.getBasicBlockGraph();

		
		basicBlockGraphLeftSpanningTree = Algorithms.doOrderedSpanningTreeAlgorithm(basicBlockGraphLeft, false);
		basicBlockGraphRightSpanningTree = Algorithms.doOrderedSpanningTreeAlgorithm(basicBlockGraphRight, false);
		
		System.out.println("BB Spanning Trees");
		Algorithms.printGraph(basicBlockGraphLeftSpanningTree);
		System.out.println("--------------------");
		Algorithms.printGraph(basicBlockGraphRightSpanningTree);
		
		IBasicBlock root1 = (IBasicBlock) basicBlockGraphLeftSpanningTree.getNodeList().getNodeExt(0);
		IBasicBlock root2 = (IBasicBlock) basicBlockGraphRightSpanningTree.getNodeList().getNodeExt(0);
		
		if(basicBlockGraphLeftSpanningTree.getNodeList().size() > basicBlockGraphRightSpanningTree.getNodeList().size()) {
			if(topDownOrderedSubtreeBasicBlock(root1, root2)) return true;
		}
		
		else {
			if(topDownOrderedSubtreeBasicBlock(root2, root1)) return true;
		}
		
		return false;
	}
	
	private boolean topDownOrderedSubtreeBasicBlock(IBasicBlock node1, IBasicBlock node2) {
		boolean isIsomorph = true;
		System.out.println("comparing nodes: " +node1.getByteCodeOffset()+"/"+node1.getCounter() +" - "+ node2.getByteCodeOffset()+"/"+node2.getCounter());

		if(node1.getCounter() != node2.getCounter()){
			return false;
		}
		
		int childCount1 = node1.getOutgoingEdgeList().size();
		int childCount2 = node2.getOutgoingEdgeList().size();
		
		if(childCount1 > childCount2){
			return false;
		}

		IBasicBlock v1, v2;
		
		if(childCount1 > 0) {
			v1 = (IBasicBlock) node1.getOutgoingEdgeList().getEdgeExt(0).getTarget();
			v2 = (IBasicBlock) node2.getOutgoingEdgeList().getEdgeExt(0).getTarget();
			
			if(!topDownOrderedSubtreeBasicBlock(v1, v2))
				isIsomorph = false;
			
			for(int i = 1; i < childCount1; i++) {
				v1 = (IBasicBlock) node1.getOutgoingEdgeList().getEdgeExt(i).getTarget();
				v2 = (IBasicBlock) node2.getOutgoingEdgeList().getEdgeExt(i).getTarget();
				
				if(!topDownOrderedSubtreeBasicBlock(v1, v2));
					isIsomorph = false;
			}
		}
		
		INodeListExt basicBlockVertices1 = node1.getBasicBlockVertices();
		INodeListExt basicBlockVertices2 = node2.getBasicBlockVertices();
		
		for(int i = 0; i < basicBlockVertices1.size(); i++){
			basicBlockVertices1.getNodeExt(i).setHighlighted(true);
		}
		
		for(int i = 0; i < basicBlockVertices2.size(); i++){
			basicBlockVertices2.getNodeExt(i).setHighlighted(true);
		}
		
		return isIsomorph;
	}

	private boolean topDownOrderedSubtree(INodeExt node1, INodeExt node2) {
		boolean isIsomorph = true;
		System.out.println("comparing nodes: " +node1.getByteCodeOffset()+"/"+node1.getCounter() +" - "+ node2.getByteCodeOffset()+"/"+node2.getCounter());

		if(node1.getCounter() != node2.getCounter()){
			return false;
		}
		
		int childCount1 = node1.getOutgoingEdgeList().size();
		int childCount2 = node2.getOutgoingEdgeList().size();
		
		if(childCount1 > childCount2){
			return false;
		}

		INodeExt v1, v2;
		
		if(childCount1 > 0) {
			v1 = node1.getOutgoingEdgeList().getEdgeExt(0).getTarget();
			v2 = node2.getOutgoingEdgeList().getEdgeExt(0).getTarget();
			
			if(!topDownOrderedSubtree(v1, v2))
				isIsomorph = false;
			
			for(int i = 1; i < childCount1; i++) {
				v1 = node1.getOutgoingEdgeList().getEdgeExt(i).getTarget();
				v2 = node2.getOutgoingEdgeList().getEdgeExt(i).getTarget();
				
				if(!topDownOrderedSubtree(v1, v2));
					isIsomorph = false;
			}
		}
		
		node1.setHighlighted(true);
		node2.setHighlighted(true);
		
		return isIsomorph;
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
		
		
		Algorithms.printGraph(cfgLeftSpanningTree);
		Algorithms.printGraph(cfgRightSpanningTree);
		
		for(int i = 0; i < cfgLeftSpanningTree.getNodeList().size(); i++){
			boolean tmp = true;
			
			INodeExt leftNode = cfgLeftSpanningTree.getNodeList().getNodeExt(i);
			INodeExt rightNode = cfgRightSpanningTree.getNodeList().getNodeExt(i);
			
			System.out.println("LeftNode: "+leftNode.getByteCodeString() +" , "+leftNode.getByteCodeOffset()+" , "+leftNode.getIncomingEdgeList().size()+"/"+leftNode.getOutgoingEdgeList().size());
			System.out.println("RightNode: "+rightNode.getByteCodeString() +" , "+rightNode.getByteCodeOffset()+" , "+rightNode.getIncomingEdgeList().size()+"/"+rightNode.getOutgoingEdgeList().size());

//			if(leftNode.getByteCodeOffset() != rightNode.getByteCodeOffset()) {
//				isomorph = false;
//				tmp = false;
//			}
			
			if(leftNode.getOutgoingEdgeList().size() > rightNode.getOutgoingEdgeList().size()) {
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
