package com.drgarbage.commandlinetool.intf;

import static org.junit.Assert.*;
import java.io.IOException;
import org.junit.Test;

/**
 * Testing class for IArgumentPaser
 * 
 * @author Baris Atas
 */
public class IArgumentParserTest {
	
	/**
	  * Test the GraphConfigurationParsing method in {@link IArgumentParser}
	  * @param "all variations"
	  */
	@Test
	public final void testGraphConfigurationParsing() throws IOException {
		
		String[][] array ={{"-XML:c"," C:/Users/Barico/Desktop/IT-Project/Test.jar"},
		                   {"-XML:d"," C:/Users/Barico/Desktop/IT-Project/Test.jar"},
		                   {"-XML:g"," C:/Users/Barico/Desktop/IT-Project/Test.jar"},
		                   {"-ML:l"," C:/Users/Barico/Desktop/IT-Project/Test.jar"},
		                   {"-DOT:e"," C:/Users/Barico/Desktop/IT-Project/Test.jar"},
		                   {"-DOT:m"," C:/Users/Barico/Desktop/IT-Project/Test.jar"},
		                   {"-NODES:v"," C:/Users/Barico/Desktop/IT-Project/Test.jar"},
		                   {"-NODES:s"," C:/Users/Barico/Desktop/IT-Project/Test.jar"},
		                   {"-NODES:b"," C:/Users/Barico/Desktop/IT-Project/Test.jar"},
		                   {"-BYTEGRAPH:M"," C:/Users/Barico/Desktop/IT-Project/Test.jar"},
		                   {"-BYTEGRAPH:X"," C:/Users/Barico/Desktop/IT-Project/Test.jar"},
		                   {"-BYTEGRAPH:D"," C:/Users/Barico/Desktop/IT-Project/Test.jar"},
		                   {"-BYTEGRAPH:N"," C:/Users/Barico/Desktop/IT-Project/Test.jar"}};
		
		for(int i= 0;i<array.length;i++){            
		IArgumentParser argumentPars = CommandLineToolFactory.createArgumentParser(array[i]);
		assertNotNull( argumentPars.getGraphConfiguration());
		}            
	}
	
	/**
	  * Test the ByteCodeParsing method in {@link IArgumentPaser}
	  * @param "all variations"
	  */
	@Test
	public final void testByteCodeParsing() throws IOException {
		String[][] array ={{"-BC:c"," C:/Users/Barico/Desktop/IT-Project/Test.jar"},
				          {"-BC:e"," C:/Users/Barico/Desktop/IT-Project/Test.jar"},
				          {"-BC:l"," C:/Users/Barico/Desktop/IT-Project/Test.jar"},
				          {"-BC:v"," C:/Users/Barico/Desktop/IT-Project/Test.jar"},
				          {"-BC:m"," C:/Users/Barico/Desktop/IT-Project/Test.jar"},
				          {"-BC:r"," C:/Users/Barico/Desktop/IT-Project/Test.jar"},
				          {"-BC:s"," C:/Users/Barico/Desktop/IT-Project/Test.jar"}};
		
		for(int i = 0; i < array.length;i++){
		IArgumentParser argumentPars = CommandLineToolFactory.createArgumentParser(array[i]);
		assertNotNull( argumentPars.getByteCodeConfiguration());
	    }
    }    
}


