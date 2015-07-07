package com.drgarbage.commandlinetool.intf;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

/**
 * Testing class for IArgumentPaser
 * 
 * @author Baris Atas
 */
public class IArgumentParserTest {
	java.net.URL testClass = ICommandLineToolTest.class.getResource("Test.jar");
	File path = new File(testClass.getPath());

	String _classPath = testClass.getPath();

	/**
	 * Test the GraphConfigurationParsing method in {@link IArgumentParser}
	 * 
	 * @param "all variations"
	 */
	@Test
	public final void testGraphConfigurationParsing() throws IOException {

		String[][] array = { { "-XML:c" + _classPath },
				{ "-XML:d" + _classPath }, { "-XML:g" + _classPath },
				{ "-ML:l" + _classPath }, { "-DOT:e" + _classPath },
				{ "-DOT:m" + _classPath }, { "-NODES:v" + _classPath },
				{ "-NODES:s" + _classPath }, { "-NODES:b" + _classPath },
				{ "-BYTEGRAPH:M" + _classPath },
				{ "-BYTEGRAPH:X" + _classPath },
				{ "-BYTEGRAPH:D" + _classPath },
				{ "-BYTEGRAPH:N" + _classPath } };

		for (int i = 0; i < array.length; i++) {
			IArgumentParser argumentPars = CommandLineToolFactory
					.createArgumentParser(array[i]);
			assertNotNull(argumentPars.getGraphConfiguration());
		}
	}

	/**
	 * Test the ByteCodeParsing method in {@link IArgumentPaser}
	 * 
	 * @param "all variations"
	 */
	@Test
	public final void testByteCodeParsing() throws IOException {
		String[][] array = { { "-BC:c" + _classPath },
				{ "-BC:e" + _classPath }, { "-BC:l" + _classPath },
				{ "-BC:v" + _classPath }, { "-BC:m" + _classPath },
				{ "-BC:r" + _classPath }, { "-BC:s" + _classPath } };

		for (int i = 0; i < array.length; i++) {
			IArgumentParser argumentPars = CommandLineToolFactory
					.createArgumentParser(array[i]);
			assertNotNull(argumentPars.getByteCodeConfiguration());
		}
	}
}
