package com.drgarbage.commandlinetooltest;


import static org.junit.Assert.*;

import org.junit.Test;

import com.drgarbage.commandlinetool.impl.ByteCodeConfiguration;
import com.drgarbage.commandlinetool.intf.IByteCodeConfiguration;

/**
 * Testclass for IByteCodeConfiguration
 * 
 * @author Baris Atas
 */

public class IByteCodeConfigurationTest {

	@Test
	public final void testIsShowConstantPool() {
		IByteCodeConfiguration bytecodeconfiguration = new ByteCodeConfiguration("c");
		assertTrue(bytecodeconfiguration.isShowConstantPool());
		assertFalse(bytecodeconfiguration.isShowExceptionTable());
		assertFalse(bytecodeconfiguration.isShowLineNumberTable());
		assertFalse(bytecodeconfiguration.isShowLocalVariableTable());
		assertFalse(bytecodeconfiguration.isShowMaxs());
		assertFalse(bytecodeconfiguration.isShowRelativeBranchTargetOffsets());
		assertFalse(bytecodeconfiguration.isShowSourceLineNumbers());				
	}

	@Test
	public final void testIsShowLineNumberTable() {
		IByteCodeConfiguration bytecodeconfiguration = new ByteCodeConfiguration("l");
		assertFalse(bytecodeconfiguration.isShowConstantPool());
		assertFalse(bytecodeconfiguration.isShowExceptionTable());
		assertTrue(bytecodeconfiguration.isShowLineNumberTable());
		assertFalse(bytecodeconfiguration.isShowLocalVariableTable());
		assertFalse(bytecodeconfiguration.isShowMaxs());
		assertFalse(bytecodeconfiguration.isShowRelativeBranchTargetOffsets());
		assertFalse(bytecodeconfiguration.isShowSourceLineNumbers());
	}

	@Test
	public final void testIsShowLocalVariableTable() {
		IByteCodeConfiguration bytecodeconfiguration = new ByteCodeConfiguration("v");
		assertFalse(bytecodeconfiguration.isShowConstantPool());
		assertFalse(bytecodeconfiguration.isShowExceptionTable());
		assertFalse(bytecodeconfiguration.isShowLineNumberTable());
		assertTrue(bytecodeconfiguration.isShowLocalVariableTable());
		assertFalse(bytecodeconfiguration.isShowMaxs());
		assertFalse(bytecodeconfiguration.isShowRelativeBranchTargetOffsets());
		assertFalse(bytecodeconfiguration.isShowSourceLineNumbers());
	}

	@Test
	public final void testIsShowExceptionTable() {
		IByteCodeConfiguration bytecodeconfiguration = new ByteCodeConfiguration("e");
		assertFalse(bytecodeconfiguration.isShowConstantPool());
		assertTrue(bytecodeconfiguration.isShowExceptionTable());
		assertFalse(bytecodeconfiguration.isShowLineNumberTable());
		assertFalse(bytecodeconfiguration.isShowLocalVariableTable());
		assertFalse(bytecodeconfiguration.isShowMaxs());
		assertFalse(bytecodeconfiguration.isShowRelativeBranchTargetOffsets());
		assertFalse(bytecodeconfiguration.isShowSourceLineNumbers());
	
	}

	@Test
	public final void testIsShowRelativeBranchTargetOffsets() {
		IByteCodeConfiguration bytecodeconfiguration = new ByteCodeConfiguration("r");
		assertFalse(bytecodeconfiguration.isShowConstantPool());
		assertFalse(bytecodeconfiguration.isShowExceptionTable());
		assertFalse(bytecodeconfiguration.isShowLineNumberTable());
		assertFalse(bytecodeconfiguration.isShowLocalVariableTable());
		assertFalse(bytecodeconfiguration.isShowMaxs());
		assertTrue(bytecodeconfiguration.isShowRelativeBranchTargetOffsets());
		assertFalse(bytecodeconfiguration.isShowSourceLineNumbers());
	
	}

	@Test
	public final void testIsShowSourceLineNumbers() {
		IByteCodeConfiguration bytecodeconfiguration = new ByteCodeConfiguration("s");
		assertFalse(bytecodeconfiguration.isShowConstantPool());
		assertFalse(bytecodeconfiguration.isShowExceptionTable());
		assertFalse(bytecodeconfiguration.isShowLineNumberTable());
		assertFalse(bytecodeconfiguration.isShowLocalVariableTable());
		assertFalse(bytecodeconfiguration.isShowMaxs());
		assertFalse(bytecodeconfiguration.isShowRelativeBranchTargetOffsets());
		assertTrue(bytecodeconfiguration.isShowSourceLineNumbers());	
	}

	@Test
	public final void testIsShowMaxs() {
		IByteCodeConfiguration bytecodeconfiguration = new ByteCodeConfiguration("m");
		assertFalse(bytecodeconfiguration.isShowConstantPool());
		assertFalse(bytecodeconfiguration.isShowExceptionTable());
		assertFalse(bytecodeconfiguration.isShowLineNumberTable());
		assertFalse(bytecodeconfiguration.isShowLocalVariableTable());
		assertTrue(bytecodeconfiguration.isShowMaxs());
		assertFalse(bytecodeconfiguration.isShowRelativeBranchTargetOffsets());
		assertFalse(bytecodeconfiguration.isShowSourceLineNumbers());	
	}
	
	@Test
	public final void testEmptyArg() {
		IByteCodeConfiguration bytecodeconfiguration = new ByteCodeConfiguration("");
		assertFalse(bytecodeconfiguration.isShowConstantPool());
		assertFalse(bytecodeconfiguration.isShowExceptionTable());
		assertFalse(bytecodeconfiguration.isShowLineNumberTable());
		assertFalse(bytecodeconfiguration.isShowLocalVariableTable());
		assertFalse(bytecodeconfiguration.isShowMaxs());
		assertFalse(bytecodeconfiguration.isShowRelativeBranchTargetOffsets());
		assertFalse(bytecodeconfiguration.isShowSourceLineNumbers());	
	}
	
	@Test
	public final void testWrongArg() {
		IByteCodeConfiguration bytecodeconfiguration = new ByteCodeConfiguration("x");
		assertFalse(bytecodeconfiguration.isShowConstantPool());
		assertFalse(bytecodeconfiguration.isShowExceptionTable());
		assertFalse(bytecodeconfiguration.isShowLineNumberTable());
		assertFalse(bytecodeconfiguration.isShowLocalVariableTable());
		assertFalse(bytecodeconfiguration.isShowMaxs());
		assertFalse(bytecodeconfiguration.isShowRelativeBranchTargetOffsets());
		assertFalse(bytecodeconfiguration.isShowSourceLineNumbers());
	
	}

}
