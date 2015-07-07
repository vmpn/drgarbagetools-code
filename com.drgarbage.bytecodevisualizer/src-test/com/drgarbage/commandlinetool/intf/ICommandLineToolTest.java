package com.drgarbage.commandlinetool.intf;


import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import javax.print.DocFlavor.URL;

import org.junit.Test;

import com.drgarbage.asm.render.impl.ClassFileDocument;
import com.drgarbage.asm.render.intf.IClassFileDocument;
import com.drgarbage.asm.render.intf.IMethodSection;
import com.drgarbage.commandlinetool.impl.MethodPair;
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
	
	java.net.URL testClass = ICommandLineToolTest.class.getResource("Test.jar");
	File path = new File(testClass.getPath());
	
	String _classPath = testClass.getPath();
	String _packageName = "testjar";
	String _className = "main";
    ICommandLineTool cc = CommandLineToolFactory.createCommandLineToolInterface();
    IByteCodeConfiguration byteCodeConf = CommandLineToolFactory.createByteCodeConfigurationInterface();
    IGraphConfiguration graphConf = CommandLineToolFactory.createGraphConfigurationInterface();
    GraphOutputTypes outputType;

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
        graphConf.setOutputType(outputType.ExportFormat_GraphXML_XML_Based);
		Map<IMethodPair, String> result = cc.visualizeGraphs(_classPath, _packageName, _className, graphConf);
		assertNotNull(result);
	}

	/**
     * Test the VisualizeGraphs method and IMethodpair in {@link ICommandLineTool}
     */
	@Test
	public final void testVisualizeGraphsAndMethodPair() throws IOException {
		graphConf.setOutputType(outputType.ExportFormat_SourceCodeGraph);
		InputStream in = cc.getInputStream(_classPath, _packageName, _className);	 

		IClassFileDocument classFileDoc = ClassFileDocument.readClass(in);
		java.util.List<IMethodSection> methodSelectionList = classFileDoc.getMethodSections();

		for (IMethodSection methodSelection : methodSelectionList) {
			in = cc.getInputStream(_classPath, _packageName, _className);
			String result = cc.visualizeGraph(in, methodSelection.getName(), methodSelection.getDescriptor(), graphConf);
			MethodPair methodPair = new MethodPair(methodSelection.getName(), methodSelection.getDescriptor());
			assertNotNull(result);
			assertNotNull(methodPair.getMethodName());
			assertNotNull(methodPair.getMethodSignature());
		}		
	}
}


