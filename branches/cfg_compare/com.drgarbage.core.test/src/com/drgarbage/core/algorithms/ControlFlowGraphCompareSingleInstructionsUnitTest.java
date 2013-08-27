package com.drgarbage.core.algorithms;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.drgarbage.algorithms.ControlFlowGraphCompare;
import com.drgarbage.controlflowgraph.ControlFlowGraphException;
import com.drgarbage.controlflowgraph.ControlFlowGraphGenerator;
import com.drgarbage.controlflowgraph.intf.IDirectedGraphExt;
import com.drgarbage.controlflowgraph.intf.INodeExt;
import com.drgarbage.controlflowgraph.intf.INodeListExt;

public class ControlFlowGraphCompareSingleInstructionsUnitTest {
	
	String testResources[] = new String[]{this.getClass().getResource("").getPath()};

	@Test
	public void differentNodeCountNotIsomorph() throws ControlFlowGraphException, IOException {
				
		IDirectedGraphExt graphFiveNodes = ControlFlowGraphGenerator.generateControlFlowGraph(testResources, null, "TwoMethodsWithDifferentNodeCount", "fiveNodes", "()V", true, true, true);
		IDirectedGraphExt graphSevenNodes = ControlFlowGraphGenerator.generateControlFlowGraph(testResources, null, "TwoMethodsWithDifferentNodeCount", "sevenNodes", "()V", true, true, true);
		
		ControlFlowGraphCompare cfgCompare = new ControlFlowGraphCompare(graphFiveNodes, graphSevenNodes);
		
		assertFalse(cfgCompare.isIsomorph());
		
	}
	
	@Test
	public void sameNodeCountDifferentEdgeCountNotIsomorph() throws ControlFlowGraphException, IOException {
		
		IDirectedGraphExt graphFourBasicBlocksFiveEdges = ControlFlowGraphGenerator.generateControlFlowGraph(testResources, null, "TwoMethodsWithSameBasicBlockCountDifferentEdgeCount", "fourBasicBlocksFiveEdges", "()V", false, false, false);
		IDirectedGraphExt graphFourBasicBlocksFourEdges = ControlFlowGraphGenerator.generateControlFlowGraph(testResources, null, "TwoMethodsWithSameBasicBlockCountDifferentEdgeCount", "fourBasicBlocksFourEdges", "()V", false, false, false);
		
		ControlFlowGraphCompare cfgCompare = new ControlFlowGraphCompare(graphFourBasicBlocksFiveEdges, graphFourBasicBlocksFourEdges);
		
		assertFalse(cfgCompare.isIsomorph());
		
	}
	
	@Test
	public void differentNodeCountSameEdgeCountNotIsomorph() throws ControlFlowGraphException, IOException {
		
		IDirectedGraphExt graphFourBasicBlocksFiveEdges = ControlFlowGraphGenerator.generateControlFlowGraph(testResources, null, "TwoMethodsWithDifferentBasicBlockCountSameEdgeCount", "threeBasicBlocksThreeEdges", "()V", false, false, false);
		IDirectedGraphExt graphFourBasicBlocksFourEdges = ControlFlowGraphGenerator.generateControlFlowGraph(testResources, null, "TwoMethodsWithDifferentBasicBlockCountSameEdgeCount", "fourBasicBlocksThreeEdges", "()V", false, false, false);
		
		ControlFlowGraphCompare cfgCompare = new ControlFlowGraphCompare(graphFourBasicBlocksFiveEdges, graphFourBasicBlocksFourEdges);
		
		assertFalse(cfgCompare.isIsomorph());
		
	}
	
	@Test
	@Ignore
	public void sameGraphsAreIsomorph() throws ControlFlowGraphException, IOException {
		
		IDirectedGraphExt graphThreeBasicBlocks = ControlFlowGraphGenerator.generateControlFlowGraph(testResources, null, "TwoMethodsWithDifferentBasicBlockCount", "threeBasicBlocks", "()V", false, false, false);
		
		ControlFlowGraphCompare cfgCompare = new ControlFlowGraphCompare(graphThreeBasicBlocks, graphThreeBasicBlocks);
		
		assertTrue(cfgCompare.isIsomorph());
		
	}
	
	@Test
	public void sameNodeCountSameEdgeCountNotIsomorph() throws ControlFlowGraphException, IOException {
		
		IDirectedGraphExt graphM1 = ControlFlowGraphGenerator.generateControlFlowGraph(testResources, null, "MethodsWithAnisomorphicControlFlowGraphs", "M1", "()V", true, true, false);
		IDirectedGraphExt graphM2 = ControlFlowGraphGenerator.generateControlFlowGraph(testResources, null, "MethodsWithAnisomorphicControlFlowGraphs", "M2", "()V", true, true, false);

		
		ControlFlowGraphCompare cfgCompare = new ControlFlowGraphCompare(graphM1, graphM2);
		
		assertFalse(cfgCompare.isIsomorph());
		
	}

}
