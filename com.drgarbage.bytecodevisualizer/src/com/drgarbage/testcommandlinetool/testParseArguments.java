package com.drgarbage.testcommandlinetool;

import static org.junit.Assert.*;

import java.io.IOException;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.drgarbage.commandlinetool.ByteCodeConfiguration;
import com.drgarbage.commandlinetool.ExportGraphConfiguration;
import com.drgarbage.commandlinetool.ParseArguments;
/**
 * This is a Testclass to test all the Parser funktions in CommandLineTool
 * 
 * @author Baris Atas
 * @version 
 * $Id: testParseArguments.java 2015-03-03 03:03:35Z Baris Atas $
 */

public class testParseArguments {
	
	private ParseArguments parser;
	private ByteCodeConfiguration byteCodeConfiguration = null;
	private ExportGraphConfiguration exportGraphConfiguration = null;
	private String methodNametest;
	
	
	
    // Test Parser-ByteCodeConfiguration with argument c
	@Test
	public void test_ParseArguments_case1() throws IOException{		
     String[] args = {"-BC:c","C:/Users/Barico/Desktop/Test.jar","-package:testjar","-class:main"};
	 parser = new ParseArguments(args);
	 byteCodeConfiguration = parser.getByteCodeConfiguration();
	 assertTrue(byteCodeConfiguration.isShowConstantPool());
	 
	}
	
	// Test Parser-ByteCodeConfiguration with argument d
	@Test
	public void test_ParseArguments_case2() throws IOException{		
     String[] args = {"-BC:e","C:/Users/Barico/Desktop/Test.jar","-package:testjar","-class:main"};
	 parser = new ParseArguments(args);
	 byteCodeConfiguration = parser.getByteCodeConfiguration();
	 assertTrue(byteCodeConfiguration.isShowExceptionTable());
	 
	}
	
	// Test Parser-ByteCodeConfiguration with  l
	@Test
	public void test_ParseArguments_case3() throws IOException{			
	 String[] args = {"-BC:l","C:/Users/Barico/Desktop/Test.jar","-package:testjar","-class:main"};
	 parser = new ParseArguments(args);
	 byteCodeConfiguration = parser.getByteCodeConfiguration();
	 assertTrue(byteCodeConfiguration.isShowLineNumberTable());
		 
	}
	
	// Test Parser-ByteCodeConfiguration with v
    @Test
	 public void test_ParseArguments_case4() throws IOException{			
	  String[] args = {"-BC:v","C:/Users/Barico/Desktop/Test.jar","-package:testjar","-class:main"};
	  parser = new ParseArguments(args);
	  byteCodeConfiguration = parser.getByteCodeConfiguration();
	  assertTrue(byteCodeConfiguration.isShowLocalVariableTable());
			 
	}
		
		// Test Parser-ByteCodeConfiguration with  m
     @Test
	   public void test_ParseArguments_case5() throws IOException{			
	    String[] args = {"-BC:m","C:/Users/Barico/Desktop/Test.jar","-package:testjar","-class:main"};
		parser = new ParseArguments(args);
		byteCodeConfiguration = parser.getByteCodeConfiguration();
		assertTrue(byteCodeConfiguration.isShowMaxs());
			 
	}
		
		// Test Parser-ByteCodeConfiguration with  r
	 @Test
	   public void test_ParseArguments_case6() throws IOException{			
		String[] args = {"-BC:r","C:/Users/Barico/Desktop/Test.jar","-package:testjar","-class:main"};
		parser = new ParseArguments(args);
		byteCodeConfiguration = parser.getByteCodeConfiguration();
		assertTrue(byteCodeConfiguration.isShowRelativeBranchTargetOffsets());
			 
		}
		
		// Test Parser-ByteCodeConfiguration with  s
	 @Test
	  public void test_ParseArguments_case7() throws IOException{			
	   String[] args = {"-BC:s","C:/Users/Barico/Desktop/Test.jar","-package:testjar","-class:main"};
	   parser = new ParseArguments(args);
	   byteCodeConfiguration = parser.getByteCodeConfiguration();
	   assertTrue(byteCodeConfiguration.isShowSourceLineNumbers());
			 
		}
	 
		// Test XML Parser-ExportGraphConfiguration  with  c
	 @Test
	  public void test_ParseArguments_case8() throws IOException{			
	   String[] args = {"-XML:c","C:/Users/Barico/Desktop/Test.jar","-package:testjar","-class:main"};
	   parser = new ParseArguments(args);
	   exportGraphConfiguration = parser.getExportGraphConfiguration();
	   assertTrue(exportGraphConfiguration.isExportComments());
			 
		}
	
	// Test XML Parser-ExportGraphConfiguration  with  d
		 @Test
		  public void test_ParseArguments_case9() throws IOException{			
		   String[] args = {"-XML:d","C:/Users/Barico/Desktop/Test.jar","-package:testjar","-class:main"};
		   parser = new ParseArguments(args);
		   exportGraphConfiguration = parser.getExportGraphConfiguration();
		   assertTrue(exportGraphConfiguration.isExportDecorations());
				 
			}
		
	// Test XML Parser-ExportGraphConfiguration  with  g
		 @Test
		  public void test_ParseArguments_case10() throws IOException{			
		   String[] args = {"-XML:g","C:/Users/Barico/Desktop/Test.jar","-package:testjar","-class:main"};
		   parser = new ParseArguments(args);
		   exportGraphConfiguration = parser.getExportGraphConfiguration();
		   assertTrue(exportGraphConfiguration.isExportGeometry());
				 
			}
		
	// Test XML Parser-ExportGraphConfiguration  with  m
		 @Test
		  public void test_ParseArguments_case11() throws IOException{			
		   String[] args = {"-XML:m","C:/Users/Barico/Desktop/Test.jar","-package:testjar","-class:main"};
		   parser = new ParseArguments(args);
		   exportGraphConfiguration = parser.getExportGraphConfiguration();
		   assertTrue(exportGraphConfiguration.isSupressMessages());
						 
			}
		 
		 
		// Test ML Parser-ExportGraphConfiguration  with  c
		 @Test
		  public void test_ParseArguments_case12() throws IOException{			
		   String[] args = {"-ML:c","C:/Users/Barico/Desktop/Test.jar","-package:testjar","-class:main"};
		   parser = new ParseArguments(args);
		   exportGraphConfiguration = parser.getExportGraphConfiguration();
		   assertTrue(exportGraphConfiguration.isExportComments());
				 
			}
		
		// Test ML Parser-ExportGraphConfiguration  with  d
			 @Test
			  public void test_ParseArguments_case13() throws IOException{			
			   String[] args = {"-ML:d","C:/Users/Barico/Desktop/Test.jar","-package:testjar","-class:main"};
			   parser = new ParseArguments(args);
			   exportGraphConfiguration = parser.getExportGraphConfiguration();
			   assertTrue(exportGraphConfiguration.isExportDecorations());
					 
				}
			
		// Test ML Parser-ExportGraphConfiguration  with  g
			 @Test
			  public void test_ParseArguments_case14() throws IOException{			
			   String[] args = {"-ML:g","C:/Users/Barico/Desktop/Test.jar","-package:testjar","-class:main"};
			   parser = new ParseArguments(args);
			   exportGraphConfiguration = parser.getExportGraphConfiguration();
			   assertTrue(exportGraphConfiguration.isExportGeometry());
					 
				}
			
		// Test ML Parser-ExportGraphConfiguration  with  m
			 @Test
			  public void test_ParseArguments_case15() throws IOException{			
			   String[] args = {"-ML:m","C:/Users/Barico/Desktop/Test.jar","-package:testjar","-class:main"};
			   parser = new ParseArguments(args);
			   exportGraphConfiguration = parser.getExportGraphConfiguration();
			   assertTrue(exportGraphConfiguration.isSupressMessages());
							 
				}
		
			// Test DOT Parser-ExportGraphConfiguration  with  c
			 @Test
			  public void test_ParseArguments_case16() throws IOException{			
			   String[] args = {"-DOT:c","C:/Users/Barico/Desktop/Test.jar","-package:testjar","-class:main"};
			   parser = new ParseArguments(args);
			   exportGraphConfiguration = parser.getExportGraphConfiguration();
			   assertTrue(exportGraphConfiguration.isExportComments());
					 
				}
			
			// Test DOT Parser-ExportGraphConfiguration  with  d
				 @Test
				  public void test_ParseArguments_case17() throws IOException{			
				   String[] args = {"-DOT:d","C:/Users/Barico/Desktop/Test.jar","-package:testjar","-class:main"};
				   parser = new ParseArguments(args);
				   exportGraphConfiguration = parser.getExportGraphConfiguration();
				   assertTrue(exportGraphConfiguration.isExportDecorations());
						 
					}
				
			// Test DOT Parser-ExportGraphConfiguration  with  g
				 @Test
				  public void test_ParseArguments_case18() throws IOException{			
				   String[] args = {"-DOT:g","C:/Users/Barico/Desktop/Test.jar","-package:testjar","-class:main"};
				   parser = new ParseArguments(args);
				   exportGraphConfiguration = parser.getExportGraphConfiguration();
				   assertTrue(exportGraphConfiguration.isExportGeometry());
						 
					}
				
			// Test DOT Parser-ExportGraphConfiguration  with  m
				 @Test
				  public void test_ParseArguments_case19() throws IOException{			
				   String[] args = {"-DOT:m","C:/Users/Barico/Desktop/Test.jar","-package:testjar","-class:main"};
				   parser = new ParseArguments(args);
				   exportGraphConfiguration = parser.getExportGraphConfiguration();
				   assertTrue(exportGraphConfiguration.isSupressMessages());
								 
					}
			
				// Test XML Parser-ExportGraphConfiguration  with  c
				 @Test
				  public void test_ParseArguments_case20() throws IOException{			
				   String[] args = {"-NODES:c","C:/Users/Barico/Desktop/Test.jar","-package:testjar","-class:main"};
				   parser = new ParseArguments(args);
				   exportGraphConfiguration = parser.getExportGraphConfiguration();
				   assertTrue(exportGraphConfiguration.isExportComments());
						 
					}
				
				// Test NODES Parser-ExportGraphConfiguration  with  d
					 @Test
					  public void test_ParseArguments_case21() throws IOException{			
					   String[] args = {"-NODES:d","C:/Users/Barico/Desktop/Test.jar","-package:testjar","-class:main"};
					   parser = new ParseArguments(args);
					   exportGraphConfiguration = parser.getExportGraphConfiguration();
					   assertTrue(exportGraphConfiguration.isExportDecorations());
							 
						}
					
				// Test NODES Parser-ExportGraphConfiguration  with  g
					 @Test
					  public void test_ParseArguments_case22() throws IOException{			
					   String[] args = {"-NODES:g","C:/Users/Barico/Desktop/Test.jar","-package:testjar","-class:main"};
					   parser = new ParseArguments(args);
					   exportGraphConfiguration = parser.getExportGraphConfiguration();
					   assertTrue(exportGraphConfiguration.isExportGeometry());
							 
						}
					
				// Test NODES Parser-ExportGraphConfiguration  with  m
					 @Test
					  public void test_ParseArguments_case23() throws IOException{			
					   String[] args = {"-NODES:m","C:/Users/Barico/Desktop/Test.jar","-package:testjar","-class:main"};
					   parser = new ParseArguments(args);
					   exportGraphConfiguration = parser.getExportGraphConfiguration();
					   assertTrue(exportGraphConfiguration.isSupressMessages());
									 
						}
				
					// Test method-argument Parser-ExportGraphConfiguration  with  m
					 @Test
					  public void test_ParseArguments_case24() throws IOException{			
					   String[] args = {"-XML:m","C:/Users/Barico/Desktop/Test.jar","-package:testjar","-class:main"," -method:main"};
					   parser = new ParseArguments(args);
					   exportGraphConfiguration = parser.getExportGraphConfiguration();
					   methodNametest = "main";
					   assertEquals(methodNametest, exportGraphConfiguration.getMethodName());
									 
						}
		
	

}
