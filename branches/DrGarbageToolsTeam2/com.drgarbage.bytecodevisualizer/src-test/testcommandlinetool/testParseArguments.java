package testcommandlinetool;

import static org.junit.Assert.*;

import java.io.IOException;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.drgarbage.commandlinetool.ParseArguments;

public class testParseArguments {
	
	
	private String[] args = new String[10];  
	private ParseArguments parser;

	
	
	@Before
	public void setUp() throws IOException{
	}

	@Test
	public void test_ParseArguments_case1() throws IOException {
	 args[0]="?";
	 parser = new ParseArguments(args);
	 
	}

	@Test
	public void testGetInputStream() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetInputStream() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetByteCodeConfiguration() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetByteCodeConfiguration() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetGraphOutputType() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetGraphOutputType() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetExportGraphConfiguration() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetExportGraphConfiguration() {
		fail("Not yet implemented");
	}

}
