package com.drgarbage.core.algorithms;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.drgarbage.algorithms.ControlFlowGraphCompare;
import com.drgarbage.controlflowgraph.ControlFlowGraphException;
import com.drgarbage.controlflowgraph.ControlFlowGraphGenerator;
import com.drgarbage.controlflowgraph.intf.IDirectedGraphExt;
import com.drgarbage.controlflowgraph.intf.INodeExt;
import com.drgarbage.controlflowgraph.intf.INodeListExt;

public class ControlFlowGraphCompareBasicBlocksUnitTest {
	
	String testResources[] = new String[]{this.getClass().getResource("").getPath()};
	
	@Test
	public void differentNodeCountNotIsomorph() throws ControlFlowGraphException, IOException {
				
		IDirectedGraphExt graphThreeBasicBlocks = ControlFlowGraphGenerator.generateBasicBlockGraph(testResources, null, "TwoMethodsWithDifferentBasicBlockCount", "threeBasicBlocks", "()V", false, false, false);
		IDirectedGraphExt graphOneBasicBlock = ControlFlowGraphGenerator.generateBasicBlockGraph(testResources, null, "TwoMethodsWithDifferentBasicBlockCount", "oneBasicBlock", "()V", false, false, false);
		
		ControlFlowGraphCompare cfgCompare = new ControlFlowGraphCompare(graphThreeBasicBlocks, graphOneBasicBlock);
		
		assertFalse(cfgCompare.isIsomorph());
		
	}
	
	@Test
	public void sameNodeCountDifferentEdgeCountNotIsomorph() throws ControlFlowGraphException, IOException {
		
		IDirectedGraphExt graphFourBasicBlocksFiveEdges = ControlFlowGraphGenerator.generateBasicBlockGraph(testResources, null, "TwoMethodsWithSameBasicBlockCountDifferentEdgeCount", "fourBasicBlocksFiveEdges", "()V", false, false, false);
		IDirectedGraphExt graphFourBasicBlocksFourEdges = ControlFlowGraphGenerator.generateBasicBlockGraph(testResources, null, "TwoMethodsWithSameBasicBlockCountDifferentEdgeCount", "fourBasicBlocksFourEdges", "()V", false, false, false);
		
		ControlFlowGraphCompare cfgCompare = new ControlFlowGraphCompare(graphFourBasicBlocksFiveEdges, graphFourBasicBlocksFourEdges);
		
		assertFalse(cfgCompare.isIsomorph());
		
	}
	
	@Test
	public void differentNodeCountSameEdgeCountNotIsomorph() throws ControlFlowGraphException, IOException {
		
		IDirectedGraphExt graphFourBasicBlocksFiveEdges = ControlFlowGraphGenerator.generateBasicBlockGraph(testResources, null, "TwoMethodsWithDifferentBasicBlockCountSameEdgeCount", "threeBasicBlocksThreeEdges", "()V", false, false, false);
		IDirectedGraphExt graphFourBasicBlocksFourEdges = ControlFlowGraphGenerator.generateBasicBlockGraph(testResources, null, "TwoMethodsWithDifferentBasicBlockCountSameEdgeCount", "fourBasicBlocksThreeEdges", "()V", false, false, false);
		
		ControlFlowGraphCompare cfgCompare = new ControlFlowGraphCompare(graphFourBasicBlocksFiveEdges, graphFourBasicBlocksFourEdges);
		
		assertFalse(cfgCompare.isIsomorph());
		
	}
	
	@Test
	public void sameGraphsAreIsomorph() throws ControlFlowGraphException, IOException {
		
		IDirectedGraphExt graphThreeBasicBlocks = ControlFlowGraphGenerator.generateBasicBlockGraph(testResources, null, "TwoMethodsWithDifferentBasicBlockCount", "threeBasicBlocks", "()V", false, false, false);
		
		ControlFlowGraphCompare cfgCompare = new ControlFlowGraphCompare(graphThreeBasicBlocks, graphThreeBasicBlocks);
		
		assertTrue(cfgCompare.isIsomorph());
		
	}
	

}
