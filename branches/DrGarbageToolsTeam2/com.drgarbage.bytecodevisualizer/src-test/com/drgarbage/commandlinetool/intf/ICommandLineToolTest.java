package com.drgarbage.commandlinetool.intf;


import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import org.junit.Test;
import com.drgarbage.commandlinetool.impl.ByteCodeConfiguration;
import com.drgarbage.commandlinetool.impl.GraphConfiguration;
import com.drgarbage.commandlinetool.intf.CommandLineToolFactory;
import com.drgarbage.commandlinetool.intf.IByteCodeConfiguration;
import com.drgarbage.commandlinetool.intf.ICommandLineTool;
import com.drgarbage.commandlinetool.intf.IGraphConfiguration;

/**
 * TestClass for ICommandLineTool
 * 
 * @author Baris Atas
 */
public class ICommandLineToolTest {
	
	String _classPath = "C:/Users/Barico/Desktop/IT-Project/Test.jar";
	String _packageName = "testjar";
	String _className = "main";
    ICommandLineTool cc = CommandLineToolFactory.createCommandLineTool();
    IByteCodeConfiguration byteCodeConf = new ByteCodeConfiguration();
    IGraphConfiguration graphConf = new GraphConfiguration();
	
    /**
	 * Test the GetInputStream method {@link ICommandLineTool}
	 */
    @Test
	public final void testGetInputStream() throws IOException {
		
		InputStream output = cc.getInputStream(_classPath, _packageName, _className);
		assertNotNull(output);
		
	}
    
    /**
	 * Test the VisualizeClassFile method in {@link ICommandLineTool}
	 */
	@Test
	public final void testVisualizeClassFile() throws IOException  {
		InputStream output = cc.getInputStream(_classPath, _packageName, _className);
		String test = cc.visualizeClassFile(output, byteCodeConf);
		assertNotNull(test);
	}

	 /**
      * Test the VisualizeGraph method in {@link ICommandLineTool}
      */
	@Test
	public final void testVisualizeGraph() throws IOException {
		fail("Not yet implemented"); //TODO
		
	}

	@Test
	public final void testVisualizeGraphs() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testCompareClassFiles() {
		fail("Not yet implemented"); // TODO
	}

}
