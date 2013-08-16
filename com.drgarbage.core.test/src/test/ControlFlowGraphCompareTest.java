package test;

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

public class ControlFlowGraphCompareTest {
	
	String testResources[] = new String[]{this.getClass().getResource("").getPath()};
	
	@Test
	public void differentNodeCountNotIsomorph() throws ControlFlowGraphException, IOException {
				
		IDirectedGraphExt graphThreeBasicBlocks = ControlFlowGraphGenerator.generateControlFlowGraph(testResources, null, "TwoMethodsWithDifferentBasicBlockCount", "threeBasicBlocks", "()V", true, true, false);
		IDirectedGraphExt graphOneBasicBlock = ControlFlowGraphGenerator.generateControlFlowGraph(testResources, null, "TwoMethodsWithDifferentBasicBlockCount", "oneBasicBlock", "()V", true, true, false);
		
		ControlFlowGraphCompare cfgCompare = new ControlFlowGraphCompare(graphThreeBasicBlocks, graphOneBasicBlock);
		
		assertFalse(cfgCompare.isIsomorph());
		
	}
	
	@Test
	public void sameGraphsAreIsomorph() throws ControlFlowGraphException, IOException {
		
		IDirectedGraphExt graphThreeBasicBlocks = ControlFlowGraphGenerator.generateControlFlowGraph(testResources, null, "TwoMethodsWithDifferentBasicBlockCount", "threeBasicBlocks", "()V", true, true, false);
		
		ControlFlowGraphCompare cfgCompare = new ControlFlowGraphCompare(graphThreeBasicBlocks, graphThreeBasicBlocks);
		
		assertTrue(cfgCompare.isIsomorph());
		
	}
	
	@Test
	public void sameNodeCountDifferentEdgeCountNotIsomorph() throws ControlFlowGraphException, IOException {
		
		IDirectedGraphExt graphFourBasicBlocksFiveEdges = ControlFlowGraphGenerator.generateControlFlowGraph(testResources, null, "TwoMethodsWithSameBasicBlockCountDifferentEdgeCount", "fourBasicBlocksFiveEdges", "()V", true, true, false);
		IDirectedGraphExt graphFourBasicBlocksFourEdges = ControlFlowGraphGenerator.generateControlFlowGraph(testResources, null, "TwoMethodsWithSameBasicBlockCountDifferentEdgeCount", "fourBasicBlocksFourEdges", "()V", true, true, false);
		
		ControlFlowGraphCompare cfgCompare = new ControlFlowGraphCompare(graphFourBasicBlocksFiveEdges, graphFourBasicBlocksFourEdges);
		
		assertFalse(cfgCompare.isIsomorph());
		
	}
	
	@Test
	public void differentNodeCountSameEdgeCountNotIsomorph() throws ControlFlowGraphException, IOException {
		
		IDirectedGraphExt graphFourBasicBlocksFiveEdges = ControlFlowGraphGenerator.generateControlFlowGraph(testResources, null, "TwoMethodsWithDifferentBasicBlockCountSameEdgeCount", "fourBasicBlocksFiveEdges", "()V", true, true, false);
		IDirectedGraphExt graphFourBasicBlocksFourEdges = ControlFlowGraphGenerator.generateControlFlowGraph(testResources, null, "TwoMethodsWithDifferentBasicBlockCountSameEdgeCount", "fourBasicBlocksFourEdges", "()V", true, true, false);
		
		ControlFlowGraphCompare cfgCompare = new ControlFlowGraphCompare(graphFourBasicBlocksFiveEdges, graphFourBasicBlocksFourEdges);
		
		assertFalse(cfgCompare.isIsomorph());
		
	}
	

}
