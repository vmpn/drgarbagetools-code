package com.drgarbage.commandlinetooltest;


import static org.junit.Assert.*;

import org.junit.Test;

import com.drgarbage.commandlinetool.impl.GraphConfiguration;
import com.drgarbage.commandlinetool.impl.GraphOutputTypes;
import com.drgarbage.commandlinetool.intf.IByteCodeConfiguration;
import com.drgarbage.commandlinetool.intf.IGraphConfiguration;
/**
 * Testing class for IGraphConfiguration
 * 
 * @author Baris Atas

 */

public class IGraphConfigurationTest {
	
	GraphOutputTypes outputtype;

	 /**
	  * Test the IsExportComments method in {@link GraphConfiguration}
	  * @param "c" 
	  */
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

	 /**
	  * Test the IsExportDecorations method in {@link GraphConfiguration}
	  * @param "d" 
	  */
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

	 /**
	  * Test the IsExportGeometry method in {@link GraphConfiguration}
	  * @param "g" 
	  */
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

	 /**
	  * Test the IsSuppressMessages method in {@link GraphConfiguration}
	  * @param "m" 
	  */
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

	 /**
	  * Test the IsStartVertex method in {@link GraphConfiguration}
	  * @param "s" 
	  */
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

	 /**
	  * Test the IsExitVertex method in {@link GraphConfiguration}
	  * @param "d" 
	  */
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

	 /**
	  * Test the IsBackEdge method in {@link GraphConfiguration}
	  * @param "b" 
	  */
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
	
	 /**
	  * Test wit Empty Arguments in {@link GraphConfiguration}
	  * @param "null" 
	  */
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
	
	 /**
	  * Test with Multiple Arguments in {@link GraphConfiguration}
	  * @param "cdg" 
	  */
	@Test
	public final void testMultipleArgs() {
		IGraphConfiguration graphconfiguration = new GraphConfiguration("cdg");
		assertTrue(graphconfiguration.isExportComments());
		assertTrue(graphconfiguration.isExportDecorations());
		assertTrue(graphconfiguration.isExportGeometry());
		assertFalse(graphconfiguration.isSuppressMessages());
		assertFalse(graphconfiguration.isStartVertex());
		assertFalse(graphconfiguration.isExitVertex());
		assertFalse(graphconfiguration.isBackEdge());
	}

	 /**
	  * Test the GetoutputType method in {@link GraphConfiguration}
	  * @param "D" 
	  */
	@Test
	public final void testGetOutputType_test1() {
		IGraphConfiguration graphconfiguration = new GraphConfiguration("D");
        assertEquals(outputtype.ExportFormat_DOT_Graph_Language,graphconfiguration.getOutputType());    
	}
	
	 /**
	  * Test the GetoutputType method in {@link GraphConfiguration}
	  * @param "X" 
	  */
	@Test
	public final void testGetOutputType_test2() {
		IGraphConfiguration graphconfiguration = new GraphConfiguration("X");
        assertEquals(outputtype.ExportFormat_GraphXML_XML_Based,graphconfiguration.getOutputType());	    
	}
	
	 /**
	  * Test the GetoutputType method in {@link GraphConfiguration}
	  * @param "M" 
	  */
	@Test
	public final void testGetOutputType_test3() {
		IGraphConfiguration graphconfiguration = new GraphConfiguration("M");
        assertEquals(outputtype.ExportFormat_GraphML_XML_Based,graphconfiguration.getOutputType());    
	}
	
	 /**
	  * Test the GetoutputType method in {@link GraphConfiguration}
	  * @param "N" 
	  */
	@Test
	public final void testGetOutputType_test4() {
		IGraphConfiguration graphconfiguration = new GraphConfiguration("N");
        assertEquals(outputtype.ExportFormat_PrintNodes,graphconfiguration.getOutputType());    
	}
	

	
	
	

}
