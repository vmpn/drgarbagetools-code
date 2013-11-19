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
	/**
	 * 1 try to get TD traversal
	 * @param graph
	 * @return
	 */
	public boolean topDownTreeTraversal(IDirectedGraphExt graph) {
		
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
//		PrintWriter writer;
//		try {
//			writer = new PrintWriter("E:/Programms/debug.txt", "UTF-8");
//			writer.println("nodes:");
//			INodeListExt nodes = graph.getNodeList();
//			for(int i = 0; i < nodes.size(); i++){
//				writer.println("  " + nodes.getNodeExt(i).getCounter());
//				nodes.getNodeExt(i).setLongDescr(Integer.toString(nodes.getNodeExt(i).getCounter()));
//				//nodes.getNodeExt(i).set
//				writer.println(nodes.getNodeExt(i).getLongDescr());
//			}
//			
//			writer.println("edges:");
//			
//			IEdgeListExt edges = graph.getEdgeList();
//			for(int i = 0; i < edges.size(); i++ ){
//				writer.println("  " + edges.getEdgeExt(i).getSource().getCounter() 
//						+ "->" + edges.getEdgeExt(i).getTarget().getCounter());
//			}
//			writer.close();
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
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
	
	

}
