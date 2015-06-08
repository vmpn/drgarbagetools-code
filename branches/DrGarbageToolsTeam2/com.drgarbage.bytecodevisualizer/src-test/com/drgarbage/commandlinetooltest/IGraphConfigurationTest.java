package com.drgarbage.commandlinetooltest;


import static org.junit.Assert.*;

import org.junit.Test;

import com.drgarbage.commandlinetool.impl.GraphConfiguration;
import com.drgarbage.commandlinetool.impl.GraphOutputTypes;
import com.drgarbage.commandlinetool.intf.IGraphConfiguration;
/**
 * Testclass for IGraphConfiguration
 * 
 * @author Baris Atas

 */

public class IGraphConfigurationTest {
	
	GraphOutputTypes outputtype;

	@Test
	public final void testIsExportComments() {
		IGraphConfiguration graphconfiguration = new GraphConfiguration("c");
		assertTrue(graphconfiguration.isExportComments());
		assertFalse(graphconfiguration.isExportDecorations());
		assertFalse(graphconfiguration.isExportGeometry());
		assertFalse(graphconfiguration.isSuppressMessages());
		assertFalse(graphconfiguration.isStartVertex());
		assertFalse(graphconfiguration.isExitVertex());	
	}

	@Test
	public final void testIsExportDecorations() {
		IGraphConfiguration graphconfiguration = new GraphConfiguration("d");
		assertFalse(graphconfiguration.isExportComments());
		assertTrue(graphconfiguration.isExportDecorations());
		assertFalse(graphconfiguration.isExportGeometry());
		assertFalse(graphconfiguration.isSuppressMessages());
		assertFalse(graphconfiguration.isStartVertex());
		assertFalse(graphconfiguration.isExitVertex());
	}

	@Test
	public final void testIsExportGeometry() {
		IGraphConfiguration graphconfiguration = new GraphConfiguration("g");
		assertFalse(graphconfiguration.isExportComments());
		assertFalse(graphconfiguration.isExportDecorations());
		assertTrue(graphconfiguration.isExportGeometry());
		assertFalse(graphconfiguration.isSuppressMessages());
		assertFalse(graphconfiguration.isStartVertex());
		assertFalse(graphconfiguration.isExitVertex());
	}

	@Test
	public final void testIsSuppressMessages() {
		IGraphConfiguration graphconfiguration = new GraphConfiguration("m");
		assertFalse(graphconfiguration.isExportComments());
		assertFalse(graphconfiguration.isExportDecorations());
		assertFalse(graphconfiguration.isExportGeometry());
		assertTrue(graphconfiguration.isSuppressMessages());
		assertFalse(graphconfiguration.isStartVertex());
		assertFalse(graphconfiguration.isExitVertex());
	}

	@Test
	public final void testIsStartVertex() {
		IGraphConfiguration graphconfiguration = new GraphConfiguration("s");
		assertFalse(graphconfiguration.isExportComments());
		assertFalse(graphconfiguration.isExportDecorations());
		assertFalse(graphconfiguration.isExportGeometry());
		assertFalse(graphconfiguration.isSuppressMessages());
		assertTrue(graphconfiguration.isStartVertex());
		assertFalse(graphconfiguration.isExitVertex());
	}

	@Test
	public final void testIsExitVertex() {
		IGraphConfiguration graphconfiguration = new GraphConfiguration("e");
		assertFalse(graphconfiguration.isExportComments());
		assertFalse(graphconfiguration.isExportDecorations());
		assertFalse(graphconfiguration.isExportGeometry());
		assertFalse(graphconfiguration.isSuppressMessages());
		assertFalse(graphconfiguration.isStartVertex());
		assertTrue(graphconfiguration.isExitVertex());
	}

	@Test
	public final void testIsBackEdge() {
		IGraphConfiguration graphconfiguration = new GraphConfiguration("b");
		assertFalse(graphconfiguration.isExportComments());
		assertFalse(graphconfiguration.isExportDecorations());
		assertFalse(graphconfiguration.isExportGeometry());
		assertFalse(graphconfiguration.isSuppressMessages());
		assertFalse(graphconfiguration.isExportDecorations());
		assertFalse(graphconfiguration.isStartVertex());
		assertFalse(graphconfiguration.isExitVertex());
		assertTrue(graphconfiguration.isBackEdge());
	}
	
	@Test
	public final void testEmptyArgs() {
		IGraphConfiguration graphconfiguration = new GraphConfiguration("");
		assertFalse(graphconfiguration.isExportComments());
		assertFalse(graphconfiguration.isExportDecorations());
		assertFalse(graphconfiguration.isExportGeometry());
		assertFalse(graphconfiguration.isSuppressMessages());
		assertFalse(graphconfiguration.isExportDecorations());
		assertFalse(graphconfiguration.isStartVertex());
		assertFalse(graphconfiguration.isExitVertex());
		assertFalse(graphconfiguration.isBackEdge());
	}

	@Test
	public final void testGetOutputType_test1() {
		IGraphConfiguration graphconfiguration = new GraphConfiguration("D");
        assertEquals(outputtype.ExportFormat_DOT_Graph_Language,graphconfiguration.getOutputType());    
	}
	
	@Test
	public final void testGetOutputType_test2() {
		IGraphConfiguration graphconfiguration = new GraphConfiguration("X");
        assertEquals(outputtype.ExportFormat_GraphXML_XML_Based,graphconfiguration.getOutputType());	    
	}
	
	@Test
	public final void testGetOutputType_test3() {
		IGraphConfiguration graphconfiguration = new GraphConfiguration("M");
        assertEquals(outputtype.ExportFormat_GraphML_XML_Based,graphconfiguration.getOutputType());    
	}
	
	@Test
	public final void testGetOutputType_test4() {
		IGraphConfiguration graphconfiguration = new GraphConfiguration("N");
        assertEquals(outputtype.ExportFormat_PrintNodes,graphconfiguration.getOutputType());    
	}
	

}
