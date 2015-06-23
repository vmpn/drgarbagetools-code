/**
 * Copyright (c) 2008-2015, Dr. Garbage Community
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



package com.drgarbage.bytecodevisualizer.compare;


import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;




import org.eclipse.jface.preference.IPreferenceStore;

import com.drgarbage.bytecode.instructions.AbstractInstruction;
import com.drgarbage.bytecode.instructions.BranchInstruction;
import com.drgarbage.bytecode.instructions.ImmediateByteInstruction;
import com.drgarbage.bytecode.instructions.ImmediateIntInstruction;
import com.drgarbage.bytecode.instructions.ImmediateShortInstruction;
import com.drgarbage.bytecode.instructions.IncrementInstruction;
import com.drgarbage.bytecode.instructions.InvokeInterfaceInstruction;
import com.drgarbage.bytecode.instructions.LookupSwitchInstruction;
import com.drgarbage.bytecode.instructions.MultianewarrayInstruction;
import com.drgarbage.bytecode.instructions.TableSwitchInstruction;
import com.drgarbage.bytecode.instructions.LookupSwitchInstruction.MatchOffsetEntry;
import com.drgarbage.bytecodevisualizer.BytecodeVisualizerPlugin;
import com.drgarbage.bytecodevisualizer.preferences.BytecodeVisualizerPreferenceConstats;
import com.drgarbage.core.ActionUtils;
import com.drgarbage.javalang.JavaLangUtils;
import com.drgarbage.javasrc.JavaLexicalConstants;


/**
 * 
 * 
 * 
 * @author Lars Lewald 
 * @version $Revision$
 * $Id$
 */
public class Parameter {

	protected static boolean showRelativeBranchTargetOffsets = true;
	

	
	
/**
 * @param javaElement
 * @return String Packagename(mPackage)
 */
public static String getpackageName(IJavaElement javaElement){
	String mPackage=null;
	IMethod m=(IMethod)javaElement;
	IType type = m.getDeclaringType();
	mPackage = type.getPackageFragment().getElementName();
	return  mPackage;
		
	}

/**
 * @param javaElement
 * @return String Classname(mClassName)
 */
public static String getclassName(IJavaElement javaElement){
	String mClassName=null;
	String mPackage=null;
	IMethod m=(IMethod)javaElement;
	IType type = m.getDeclaringType();
	mClassName = type.getFullyQualifiedName();
	mPackage = type.getPackageFragment().getElementName();
	mClassName = mClassName.replace(mPackage + ".", "");
	return  mClassName;
	
}

/**
 * @param javaElement
 * @return String methodname or <init> when method is Constructor
 */
public static String getmethodName(IJavaElement javaElement){
	IMethod m=(IMethod)javaElement;
	try {
		if(m.isConstructor()){
			return "<init>";
		}
		else{				
		return  javaElement.getElementName();
		
}
	} catch (JavaModelException e) {
		e.printStackTrace();
	}
	return null;
	}

/**
 * @param javaElement
 * @return method Signature
 * @throws JavaModelException
 */
public static String getmethodSig(IJavaElement javaElement) throws JavaModelException{
	IMethod m=(IMethod)javaElement;
	if(m.isBinary()){
		return m.getSignature();
	}
	else{
		try{
			/* resolve parameter signature */
			StringBuffer buf = new StringBuffer("(");
			String[] parameterTypes = m.getParameterTypes();
			String res = null;
			for(int i = 0; i < parameterTypes.length; i++){
				res = ActionUtils.getResolvedTypeName(parameterTypes[i], m.getDeclaringType());
				buf.append(res);
			}						
			buf.append(")");
			
			res = ActionUtils.getResolvedTypeName(m.getReturnType(), m.getDeclaringType());
			buf.append(res);
			
			return buf.toString();
			
		}catch(IllegalArgumentException e){
			return null;
		}
	}

}


/**
 * @param javaElement
 * @return String[] ClassPath
 * @throws CoreException
 */
public static String[] ClassPath(IJavaElement javaElement) throws CoreException{
	IJavaProject javaProject = javaElement.getJavaProject();
	return  JavaLangUtils.computeRuntimeClassPath(javaProject);
	
}




/**
 * @param instruction
 * @return operands of the method
 */
public static String appendOperands(AbstractInstruction instruction) {
	IPreferenceStore store = BytecodeVisualizerPlugin.getDefault().getPreferenceStore(); 
	if (BytecodeVisualizerPreferenceConstats.BRANCH_TARGET_ADDRESS_ABSOLUTE.equals(
			store.getString(BytecodeVisualizerPreferenceConstats.BRANCH_TARGET_ADDRESS_RENDERING)
			)) {
		showRelativeBranchTargetOffsets = false;
	}
	
	StringBuffer s = new StringBuffer();

	if (instruction instanceof MultianewarrayInstruction) {
		s.append(((MultianewarrayInstruction) instruction).getImmediateShort());
		s.append(" ");
		s.append(((MultianewarrayInstruction) instruction).getDimensions());
	}
	else if (instruction instanceof IncrementInstruction) {
		s.append(((IncrementInstruction) instruction).getImmediateByte());
		s.append(" ");
		s.append(((IncrementInstruction) instruction).getIncrementConst());
	}
	else if (instruction instanceof InvokeInterfaceInstruction) {
		s.append(((InvokeInterfaceInstruction) instruction).getImmediateShort());
		s.append(" ");
		s.append(((InvokeInterfaceInstruction) instruction).getCount());
	}
	else if (instruction instanceof ImmediateByteInstruction) {
		s.append(((ImmediateByteInstruction) instruction).getImmediateByte());
	}
	else if (instruction instanceof ImmediateShortInstruction) {
		s.append(((ImmediateShortInstruction) instruction).getImmediateShort());
	}
	else if (instruction instanceof ImmediateIntInstruction) {
		s.append(((ImmediateIntInstruction) instruction).getImmediateInt());
	}
	else if (instruction instanceof BranchInstruction) {                         
		BranchInstruction bi = (BranchInstruction) instruction;
		int val = bi.getBranchOffset();
		if (!showRelativeBranchTargetOffsets) {
			/* compute absolute offset */
			val += bi.getOffset();
		}
		s.append(val);                       
	}
	else if (instruction instanceof TableSwitchInstruction) {
		TableSwitchInstruction tsi = (TableSwitchInstruction) instruction;
		s.append(tsi.getDefaultOffset());
		s.append(tsi.getLow());
		s.append(tsi.getHigh());

		s.append(" ");
		s.append(JavaLexicalConstants.LEFT_SQUARE_BRACKET);
		int[] offs = tsi.getJumpOffsets();
		for (int i = 0; i < offs.length; i++) {
			if (i != 0) {
				s.append(",");
				s.append(" ");
			}
			int val = offs[i];
			if (!showRelativeBranchTargetOffsets) {
				/* compute absolute offset */
				val += tsi.getOffset();
			}
			s.append(String.valueOf(val));
		}
		s.append(JavaLexicalConstants.RIGHT_SQUARE_BRACKET);
	}
	else if (instruction instanceof LookupSwitchInstruction) {
		LookupSwitchInstruction lsi = (LookupSwitchInstruction) instruction;
		s.append(lsi.getDefaultOffset());

		List<MatchOffsetEntry> ens = lsi.getMatchOffsetPairs();
		s.append(ens.size());

		s.append(" ");
		s.append(JavaLexicalConstants.LEFT_SQUARE_BRACKET);
		for (int i = 0; i < ens.size(); i++) {
			if (i != 0) {
				s.append(",");
				s.append(" ");
			}
			MatchOffsetEntry en = ens.get(i);
			s.append(en.getMatch());
			s.append(" => ");
			
			int val = en.getOffset();
			if (!showRelativeBranchTargetOffsets) {
				/* compute absolute offset */
				val += lsi.getOffset();
			}
			s.append(String.valueOf(val));
		}
		s.append(JavaLexicalConstants.RIGHT_SQUARE_BRACKET);
	}
	
	
	
	return s.toString();



}



}
