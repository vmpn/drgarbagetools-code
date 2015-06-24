package com.drgarbage.commandlinetool.intf;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.drgarbage.commandlinetool.impl.ArgumentParser;

public class IArgumentParserTest {
	

	String[] array ={"-XML:","c ","C:/Users/Barico/Desktop/IT-Project/Test.jar"};
	IGraphConfiguration IGraphConf = CommandLineToolFactory.createGraphConfigurationInterface();
	
	IByteCodeConfiguration iByteCodeConf;
	
	

	@Test
	public final void testArgumentParser() {
		
		
	}

	@Test
	public final void testGetGraphConfiguration() throws IOException {
		IGraphConf.setExportComments(true);
		IArgumentParser argumentPars = CommandLineToolFactory.createArgumentParser(array);
		assertEquals(IGraphConf, argumentPars.getGraphConfiguration());
		
	}

	@Test
	public final void testGetByteCodeConfiguration() {
		fail("Not yet implemented"); // TODO
	}

}
