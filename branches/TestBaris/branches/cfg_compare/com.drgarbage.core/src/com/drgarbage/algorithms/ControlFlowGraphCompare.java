package com.drgarbage.algorithms;

import java.awt.font.ImageGraphicAttribute;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import org.eclipse.swt.widgets.Tree;

import com.drgarbage.controlflowgraph.ControlFlowGraphException;
import com.drgarbage.controlflowgraph.ControlFlowGraphGenerator;
import com.drgarbage.controlflowgraph.intf.GraphUtils;
import com.drgarbage.controlflowgraph.intf.IBasicBlock;
import com.drgarbage.controlflowgraph.intf.IDirectedGraphExt;
import com.drgarbage.controlflowgraph.intf.IEdgeExt;
import com.drgarbage.controlflowgraph.intf.IEdgeListExt;
import com.drgarbage.controlflowgraph.intf.INodeExt;
import com.drgarbage.controlflowgraph.intf.INodeListExt;
import com.drgarbage.controlflowgraph.intf.MarkEnum;

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
		
		System.out.println("CFG");
		Algorithms.printGraph(cfgLeft);
		System.out.println("--------------------");
		Algorithms.printGraph(cfgRight);
		
		cfgLeftSpanningTree = Algorithms.doOrderedSpanningTreeAlgorithm(cfgLeft, false);
		cfgRightSpanningTree = Algorithms.doOrderedSpanningTreeAlgorithm(cfgRight, false);
		
		System.out.println("CFG Spanning Trees");
		Algorithms.printGraph(cfgLeftSpanningTree);
		System.out.println("--------------------");
		Algorithms.printGraph(cfgRightSpanningTree);
		
		INodeExt root1 = cfgLeftSpanningTree.getNodeList().getNodeExt(0);
		INodeExt root2 = cfgRightSpanningTree.getNodeList().getNodeExt(0);
		
//		if(cfgLeftSpanningTree.getNodeList().size() > cfgRightSpanningTree.getNodeList().size()) {
//			if(topDownOrderedSubtree(root1, root2)) return true;
//		}
//		
//		else {
//			if(topDownOrderedSubtree(root2, root1)) return true;
//		}
		
		return topDownOrderedSubtree(root1, root2);

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
		
		System.out.println("BB");
		Algorithms.printGraph(basicBlockGraphLeft);
		System.out.println("--------------------");
		Algorithms.printGraph(basicBlockGraphRight);

		
		basicBlockGraphLeftSpanningTree = Algorithms.doOrderedSpanningTreeAlgorithm(basicBlockGraphLeft, false);
		basicBlockGraphRightSpanningTree = Algorithms.doOrderedSpanningTreeAlgorithm(basicBlockGraphRight, false);
		
		System.out.println("BB Spanning Trees");
		Algorithms.printGraph(basicBlockGraphLeftSpanningTree);
		System.out.println("--------------------");
		Algorithms.printGraph(basicBlockGraphRightSpanningTree);
		
		IBasicBlock root1 = (IBasicBlock) basicBlockGraphLeftSpanningTree.getNodeList().getNodeExt(0);
		IBasicBlock root2 = (IBasicBlock) basicBlockGraphRightSpanningTree.getNodeList().getNodeExt(0);
		
//		if(basicBlockGraphLeftSpanningTree.getNodeList().size() > basicBlockGraphRightSpanningTree.getNodeList().size()) {
//			if(topDownOrderedSubtreeBasicBlock(root1, root2)) return true;
//		}
//		
//		else {
//			if(topDownOrderedSubtreeBasicBlock(root2, root1)) return true;
//		}
		
		return topDownOrderedSubtreeBasicBlock(root1, root2);
	}
	
	private boolean topDownOrderedSubtreeBasicBlock(IBasicBlock node1, IBasicBlock node2) {
		boolean isIsomorph = true;
		System.out.println("comparing nodes: " +node1.getByteCodeOffset()+"/"+node1.getCounter() +" - "+ node2.getByteCodeOffset()+"/"+node2.getCounter());

		if(node1.getCounter() != node2.getCounter()){
			return false;
		}
		
		int childCount1 = node1.getOutgoingEdgeList().size();
		int childCount2 = node2.getOutgoingEdgeList().size();
		
		if(childCount1 > childCount2) {
			isIsomorph = false;
		}

		else {
			IBasicBlock v1, v2;
			
			if(childCount1 > 0 && childCount1 < 3) {
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
			
			/* for switch cases */
			if(childCount1 > 2) {
				List<IBasicBlock> edgeList1 = new ArrayList<IBasicBlock>();
				List<IBasicBlock> edgeList2 = new ArrayList<IBasicBlock>();
				
				/* quickfix to reverse order of edges */
				for(int i = childCount1; i > 0; i--) {
					edgeList1.add((IBasicBlock) node1.getOutgoingEdgeList().getEdgeExt(i-1).getTarget());
				}
				
				for(int i = childCount2; i > 0; i--) {
					edgeList2.add((IBasicBlock) node2.getOutgoingEdgeList().getEdgeExt(i-1).getTarget());
				}
				
				v1 = edgeList1.get(0);
				v2 = edgeList2.get(0);
				
				if(!topDownOrderedSubtreeBasicBlock(v1, v2))
					isIsomorph = false;
				
				for(int i = 1; i < childCount1; i++) {
					v1 = edgeList1.get(i);
					v2 = edgeList2.get(i);
					
					if(!topDownOrderedSubtreeBasicBlock(v1, v2));
						isIsomorph = false;
				}
			}
		}
		
		INodeListExt basicBlockVertices1 = node1.getBasicBlockVertices();
		INodeListExt basicBlockVertices2 = node2.getBasicBlockVertices();
		
//		for(int i = 0; i < basicBlockVertices1.size(); i++){
//			basicBlockVertices1.getNodeExt(i).setHighlighted(true);
//			basicBlockVertices1.getNodeExt(i).setMark(MarkEnum.GREEN);
//		}
//		
//		for(int i = 0; i < basicBlockVertices2.size(); i++){
//			basicBlockVertices2.getNodeExt(i).setHighlighted(true);
//			basicBlockVertices2.getNodeExt(i).setMark(MarkEnum.GREEN);
//		}
		
		if(basicBlockVertices1.size() > basicBlockVertices2.size()) {
			for(int i = 0; i < basicBlockVertices1.size(); i++){
				basicBlockVertices1.getNodeExt(i).setHighlighted(true);
				basicBlockVertices1.getNodeExt(i).setMark(MarkEnum.GREEN);
				
				if(i < basicBlockVertices2.size()) {
					basicBlockVertices2.getNodeExt(i).setHighlighted(true);
					basicBlockVertices2.getNodeExt(i).setMark(MarkEnum.GREEN);
				}
				
				else basicBlockVertices1.getNodeExt(i).setMark(MarkEnum.RED);
			}
		}
		
		else {
			for(int i = 0; i < basicBlockVertices2.size(); i++){
				basicBlockVertices2.getNodeExt(i).setHighlighted(true);
				basicBlockVertices2.getNodeExt(i).setMark(MarkEnum.GREEN);
				
				if(i < basicBlockVertices1.size()) {
					basicBlockVertices1.getNodeExt(i).setHighlighted(true);
					basicBlockVertices1.getNodeExt(i).setMark(MarkEnum.GREEN);
				}
				
				else basicBlockVertices2.getNodeExt(i).setMark(MarkEnum.RED);
			}
		}
		
		return isIsomorph;
	}

	private boolean topDownOrderedSubtree(INodeExt node1, INodeExt node2) {
		boolean isIsomorph = true;
		System.out.println("comparing nodes: " +node1.getByteCodeOffset()+"/"+node1.getCounter() +" - "+ node2.getByteCodeOffset()+"/"+node2.getCounter());

		if(node1.getCounter() != node2.getCounter()){
			isIsomorph = false;
		}
		
		if(isIsomorph) {
			
			IEdgeListExt node1Children = node1.getOutgoingEdgeList();
			IEdgeListExt node2Children = node2.getOutgoingEdgeList();
			
			int childCount1 = node1Children.size();
			int childCount2 = node2Children.size();
			
			if(childCount1 > childCount2) {
				isIsomorph = false;
			}
	
			else {
				INodeExt v1, v2;
								
				if(childCount1 > 0) {
					
					ArrayList<IEdgeExt> node1SortedEdges = sortEdges(node1Children);
					ArrayList<IEdgeExt> node2SortedEdges = sortEdges(node2Children);

					v1 = node1SortedEdges.get(0).getTarget();
					v2 = node2SortedEdges.get(0).getTarget();
					
					if(node1.getCounter() == 3){
						for(int i = 0; i < node1.getOutgoingEdgeList().size(); i++){
							System.out.println("NOOOODES");
							System.out.println(node1.getOutgoingEdgeList().getEdgeExt(i).getSource().getByteCodeOffset() +"->"+node1.getOutgoingEdgeList().getEdgeExt(i).getTarget().getByteCodeOffset());
						}
					}

					
					if(!topDownOrderedSubtree(v1, v2))
						isIsomorph = false;
					
					for(int i = 1; i < childCount1; i++) {
						v1 = node1SortedEdges.get(i).getTarget();
						v2 = node2SortedEdges.get(i).getTarget();
						
						if(!topDownOrderedSubtree(v1, v2));
							isIsomorph = false;
					}
				}
			}
			node1.setHighlighted(true);
			node1.setMark(MarkEnum.GREEN);
			
			node2.setHighlighted(true);
			node2.setMark(MarkEnum.GREEN);
		}
		return isIsomorph;
	}
	
	private ArrayList<IEdgeExt> sortEdges(IEdgeListExt edgeList){
		
		TreeMap<Integer, IEdgeExt> tmpEdgeList = new TreeMap<Integer, IEdgeExt>();
		
		for(int i = 0; i < edgeList.size(); i++){
			tmpEdgeList.put(edgeList.getEdgeExt(i).getTarget().getByteCodeOffset(), edgeList.getEdgeExt(i));
		}
		
		return new ArrayList<IEdgeExt>(tmpEdgeList.values());
		
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
