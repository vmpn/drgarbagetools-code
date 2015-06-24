package com.drgarbage.commandlinetool.intf;

import static org.junit.Assert.*;
import org.junit.Test;
import com.drgarbage.commandlinetool.impl.GraphConfiguration;
import com.drgarbage.commandlinetool.intf.IGraphConfiguration;

/**
 * Testing class for IGraphConfiguration
 * 
 * @author Baris Atas
 */
public class IGraphConfigurationTest {
	
	GraphOutputTypes outputtype;
	IGraphConfiguration graphconfiguration= CommandLineToolFactory.createGraphConfigurationInterface();

	 /**
	  * Test the IsExportComments method in {@link GraphConfiguration}
	  * @param "c" 
	  */
	@Test
	public final void testIsExportComments() {
		 
		graphconfiguration.setExportComments(true);
		assertTrue(graphconfiguration.isExportComments());
		assertFalse(graphconfiguration.isExportDecorations());
		assertFalse(graphconfiguration.isExportGeometry());
		assertFalse(graphconfiguration.isSuppressMessages());
		assertFalse(graphconfiguration.isStartVertex());
		assertFalse(graphconfiguration.isEndVertex());	
	}

	 /**
	  * Test the IsExportDecorations method in {@link GraphConfiguration}
	  * @param "d" 
	  */
	@Test
	public final void testIsExportDecorations() {
		
		graphconfiguration.setExportDecorations(true);
		assertFalse(graphconfiguration.isExportComments());
		assertTrue(graphconfiguration.isExportDecorations());
		assertFalse(graphconfiguration.isExportGeometry());
		assertFalse(graphconfiguration.isSuppressMessages());
		assertFalse(graphconfiguration.isStartVertex());
		assertFalse(graphconfiguration.isEndVertex());
	}

	 /**
	  * Test the IsExportGeometry method in {@link GraphConfiguration}
	  * @param "g" 
	  */
	@Test
	public final void testIsExportGeometry() {
		
        graphconfiguration.setExportGeometry(true);
		assertFalse(graphconfiguration.isExportComments());
		assertFalse(graphconfiguration.isExportDecorations());
		assertTrue(graphconfiguration.isExportGeometry());
		assertFalse(graphconfiguration.isSuppressMessages());
		assertFalse(graphconfiguration.isStartVertex());
		assertFalse(graphconfiguration.isEndVertex());
	}

	 /**
	  * Test the IsSuppressMessages method in {@link GraphConfiguration}
	  * @param "m" 
	  */
	@Test
	public final void testIsSuppressMessages() {
		
		graphconfiguration.setSuppressMessages(true);
		assertFalse(graphconfiguration.isExportComments());
		assertFalse(graphconfiguration.isExportDecorations());
		assertFalse(graphconfiguration.isExportGeometry());
		assertTrue(graphconfiguration.isSuppressMessages());
		assertFalse(graphconfiguration.isStartVertex());
		assertFalse(graphconfiguration.isEndVertex());
	}

	 /**
	  * Test the IsStartVertex method in {@link GraphConfiguration}
	  * @param "s" 
	  */
	@Test
	public final void testIsStartVertex() {
		
		graphconfiguration.setStartVertex(true);
		assertFalse(graphconfiguration.isExportComments());
		assertFalse(graphconfiguration.isExportDecorations());
		assertFalse(graphconfiguration.isExportGeometry());
		assertFalse(graphconfiguration.isSuppressMessages());
		assertTrue(graphconfiguration.isStartVertex());
		assertFalse(graphconfiguration.isEndVertex());
	}

	 /**
	  * Test the IsExitVertex method in {@link GraphConfiguration}
	  * @param "d" 
	  */
	@Test
	public final void testIsEndVertex() {
       
		graphconfiguration.setEndVertex(true);
		assertFalse(graphconfiguration.isExportComments());
		assertFalse(graphconfiguration.isExportDecorations());
		assertFalse(graphconfiguration.isExportGeometry());
		assertFalse(graphconfiguration.isSuppressMessages());
		assertFalse(graphconfiguration.isStartVertex());
		assertTrue(graphconfiguration.isEndVertex());
	}

	 /**
	  * Test the IsBackEdge method in {@link GraphConfiguration}
	  * @param "b" 
	  */
	@Test
	public final void testIsBackEdge() {
		
		graphconfiguration.setBackEdge(true);
		assertFalse(graphconfiguration.isExportComments());
		assertFalse(graphconfiguration.isExportDecorations());
		assertFalse(graphconfiguration.isExportGeometry());
		assertFalse(graphconfiguration.isSuppressMessages());
		assertFalse(graphconfiguration.isExportDecorations());
		assertFalse(graphconfiguration.isStartVertex());
		assertFalse(graphconfiguration.isEndVertex());
		assertTrue(graphconfiguration.isBackEdge());
	}
	
	 /**
	  * Test wit Empty Arguments in {@link GraphConfiguration}
	  * @param "null" 
	  */
	@Test
	public final void testEmptyArgs() {
		
		assertFalse(graphconfiguration.isExportDecorations());
		assertFalse(graphconfiguration.isExportGeometry());
		assertFalse(graphconfiguration.isSuppressMessages());
		assertFalse(graphconfiguration.isExportDecorations());
		assertFalse(graphconfiguration.isStartVertex());
		assertFalse(graphconfiguration.isEndVertex());
		assertFalse(graphconfiguration.isBackEdge());
	}
	
	 /**
	  * Test with Multiple Arguments in {@link GraphConfiguration}
	  * @param "cdg" 
	  */
	@Test
	public final void testMultipleArgs() {
		
		graphconfiguration.setExportDecorations(true);
		graphconfiguration.setExportComments(true);
		graphconfiguration.setExportGeometry(true);
		assertTrue(graphconfiguration.isExportComments());
		assertTrue(graphconfiguration.isExportDecorations());
		assertTrue(graphconfiguration.isExportGeometry());
		assertFalse(graphconfiguration.isSuppressMessages());
		assertFalse(graphconfiguration.isStartVertex());
		assertFalse(graphconfiguration.isEndVertex());
		assertFalse(graphconfiguration.isBackEdge());
	}

	 /**
	  * Test the GetoutputType method in {@link GraphConfiguration}
	  * @param "D" 
	  */
	@Test
	public final void testGetOutputType_test1() {
		
		graphconfiguration.setOutputType(outputtype.ExportFormat_DOT_Graph_Language);
        assertEquals(outputtype.ExportFormat_DOT_Graph_Language,graphconfiguration.getOutputType());    
	}
	
	 /**
	  * Test the GetoutputType method in {@link GraphConfiguration}
	  * @param "X" 
	  */
	@Test
	public final void testGetOutputType_test2() {
		
		graphconfiguration.setOutputType(outputtype.ExportFormat_GraphXML_XML_Based);
        assertEquals(outputtype.ExportFormat_GraphXML_XML_Based,graphconfiguration.getOutputType());	    
	}
	
	 /**
	  * Test the GetoutputType method in {@link GraphConfiguration}
	  * @param "M" 
	  */
	@Test
	public final void testGetOutputType_test3() {
		
		graphconfiguration.setOutputType(outputtype.ExportFormat_GraphML_XML_Based);
        assertEquals(outputtype.ExportFormat_GraphML_XML_Based,graphconfiguration.getOutputType());    
	}
	
	 /**
	  * Test the GetoutputType method in {@link GraphConfiguration}
	  * @param "N" 
	  */
	@Test
	public final void testGetOutputType_test4() {
		
		graphconfiguration.setOutputType(outputtype.ExportFormat_PrintNodes);
        assertEquals(outputtype.ExportFormat_PrintNodes,graphconfiguration.getOutputType());    
	}
	

	
	
	

}
