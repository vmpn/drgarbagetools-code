package com.drgarbage.commandlinetool.intf;


import static org.junit.Assert.*;
import org.junit.Test;
import com.drgarbage.commandlinetool.intf.IByteCodeConfiguration;

/**
 * Testing class for IByteCodeConfiguration
 * 
 * @author Baris Atas
 */
public class IByteCodeConfigurationTest {
	
	IByteCodeConfiguration bytecodeconfiguration= CommandLineToolFactory.createByteCodeConfigurationInterface();

	
	 /**
	  * Test the IsShowConstantPool method in {@link IByteCodeConfiguration}
	  * @param "c" 
	  */
	@Test
	public final void testIsShowConstantPool() {
		
	    bytecodeconfiguration.setShowConstantPool(true);
		assertTrue(bytecodeconfiguration.isShowConstantPool());
		assertFalse(bytecodeconfiguration.isShowExceptionTable());
		assertFalse(bytecodeconfiguration.isShowLineNumberTable());
		assertFalse(bytecodeconfiguration.isShowLocalVariableTable());
		assertFalse(bytecodeconfiguration.isShowMaxs());
		assertFalse(bytecodeconfiguration.isShowRelativeBranchTargetOffsets());
		assertFalse(bytecodeconfiguration.isShowSourceLineNumbers());				
	}

	 /**
	  * Test the IsShowLineNumber method in {@link IByteCodeConfiguration}
	  * @param "l" 
	  */
	@Test
	public final void testIsShowLineNumberTable() {

		bytecodeconfiguration.setShowLineNumberTable(true);
		assertFalse(bytecodeconfiguration.isShowConstantPool());
		assertFalse(bytecodeconfiguration.isShowExceptionTable());
		assertTrue(bytecodeconfiguration.isShowLineNumberTable());
		assertFalse(bytecodeconfiguration.isShowLocalVariableTable());
		assertFalse(bytecodeconfiguration.isShowMaxs());
		assertFalse(bytecodeconfiguration.isShowRelativeBranchTargetOffsets());
		assertFalse(bytecodeconfiguration.isShowSourceLineNumbers());
	}

	 /**
	  * Test the IsShowLocalVariableTable method in {@link IByteCodeConfiguration}
	  * @param "v" 
	  */
	@Test
	public final void testIsShowLocalVariableTable() {
		
		bytecodeconfiguration.setShowLocalVariableTable(true);
		assertFalse(bytecodeconfiguration.isShowConstantPool());
		assertFalse(bytecodeconfiguration.isShowExceptionTable());
		assertFalse(bytecodeconfiguration.isShowLineNumberTable());
		assertTrue(bytecodeconfiguration.isShowLocalVariableTable());
		assertFalse(bytecodeconfiguration.isShowMaxs());
		assertFalse(bytecodeconfiguration.isShowRelativeBranchTargetOffsets());
		assertFalse(bytecodeconfiguration.isShowSourceLineNumbers());
	}

	 /**
	  * Test the IsShowExceptionTable method in {@link IByteCodeConfiguration}
	  * @param "e" 
	  */
	@Test
	public final void testIsShowExceptionTable() {
		
		bytecodeconfiguration.setShowExceptionTable(true);
		assertFalse(bytecodeconfiguration.isShowConstantPool());
		assertTrue(bytecodeconfiguration.isShowExceptionTable());
		assertFalse(bytecodeconfiguration.isShowLineNumberTable());
		assertFalse(bytecodeconfiguration.isShowLocalVariableTable());
		assertFalse(bytecodeconfiguration.isShowMaxs());
		assertFalse(bytecodeconfiguration.isShowRelativeBranchTargetOffsets());
		assertFalse(bytecodeconfiguration.isShowSourceLineNumbers());
	
	}
    
	 /**
	  * Test the IsShowRelativeBranchTargetOffsets method in {@link IByteCodeConfiguration}
	  * @param "r" 
	  */
	@Test
	public final void testIsShowRelativeBranchTargetOffsets() {
		
		bytecodeconfiguration.setShowRelativeBranchTargetOffsets(true);
		assertFalse(bytecodeconfiguration.isShowConstantPool());
		assertFalse(bytecodeconfiguration.isShowExceptionTable());
		assertFalse(bytecodeconfiguration.isShowLineNumberTable());
		assertFalse(bytecodeconfiguration.isShowLocalVariableTable());
		assertFalse(bytecodeconfiguration.isShowMaxs());
		assertTrue(bytecodeconfiguration.isShowRelativeBranchTargetOffsets());
		assertFalse(bytecodeconfiguration.isShowSourceLineNumbers());
	
	}

	 /**
	  * Test the IsShowSourceLineNumbers method in {@link IByteCodeConfiguration}
	  * @param "s" 
	  */
	@Test
	public final void testIsShowSourceLineNumbers() {
		
		bytecodeconfiguration.setShowSourceLineNumbers(true);
		assertFalse(bytecodeconfiguration.isShowConstantPool());
		assertFalse(bytecodeconfiguration.isShowExceptionTable());
		assertFalse(bytecodeconfiguration.isShowLineNumberTable());
		assertFalse(bytecodeconfiguration.isShowLocalVariableTable());
		assertFalse(bytecodeconfiguration.isShowMaxs());
		assertFalse(bytecodeconfiguration.isShowRelativeBranchTargetOffsets());
		assertTrue(bytecodeconfiguration.isShowSourceLineNumbers());	
	}

	 /**
	  * Test the IsShowMaxs() method in {@link IByteCodeConfiguration}
	  * @param "m" 
	  */
	@Test
	public final void testIsShowMaxs() {
		
		bytecodeconfiguration.setShowMaxs(true);
		assertFalse(bytecodeconfiguration.isShowConstantPool());
		assertFalse(bytecodeconfiguration.isShowExceptionTable());
		assertFalse(bytecodeconfiguration.isShowLineNumberTable());
		assertFalse(bytecodeconfiguration.isShowLocalVariableTable());
		assertTrue(bytecodeconfiguration.isShowMaxs());
		assertFalse(bytecodeconfiguration.isShowRelativeBranchTargetOffsets());
		assertFalse(bytecodeconfiguration.isShowSourceLineNumbers());	
	}
	
	 /**
	  * Test with EmptyArguments in {@link IByteCodeConfiguration}
	  * @param "null" 
	  */
	@Test
	public final void testEmptyArg() {

		assertFalse(bytecodeconfiguration.isShowConstantPool());
		assertFalse(bytecodeconfiguration.isShowExceptionTable());
		assertFalse(bytecodeconfiguration.isShowLineNumberTable());
		assertFalse(bytecodeconfiguration.isShowLocalVariableTable());
		assertFalse(bytecodeconfiguration.isShowMaxs());
		assertFalse(bytecodeconfiguration.isShowRelativeBranchTargetOffsets());
		assertFalse(bytecodeconfiguration.isShowSourceLineNumbers());	
	}
	
	 /**
	  * Test with Multiple Arguments in {@link IByteCodeConfiguration}
	  * @param "mre" 
	  */
	@Test
	public final void testMultipleArgs() {

		bytecodeconfiguration.setShowExceptionTable(true);
		bytecodeconfiguration.setShowMaxs(true);
		bytecodeconfiguration.setShowRelativeBranchTargetOffsets(true);
		assertFalse(bytecodeconfiguration.isShowConstantPool());
		assertTrue(bytecodeconfiguration.isShowExceptionTable());
		assertFalse(bytecodeconfiguration.isShowLineNumberTable());
		assertFalse(bytecodeconfiguration.isShowLocalVariableTable());
		assertTrue(bytecodeconfiguration.isShowMaxs());
		assertTrue(bytecodeconfiguration.isShowRelativeBranchTargetOffsets());
		assertFalse(bytecodeconfiguration.isShowSourceLineNumbers());
	
	}
	


}
