package com.drgarbage.bytecodevisualizer.compare;


import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IClassFile;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;

import com.drgarbage.javalang.JavaLangUtils;


/**
 * 
 * 
 * 
 * @author Lars Lewald 
 * @version $Revision$
 * $Id$
 */
public class Parameter {
	
	
	
public static String getpackageName(IJavaElement javaElement){
	String mPackage=null;
	IMethod m=(IMethod)javaElement;
	IType type = m.getDeclaringType();
	mPackage = type.getPackageFragment().getElementName();
	return  mPackage;
		
	}

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

public static String getmethodName(IJavaElement javaElement){
	return  javaElement.getElementName();
	
}

public static String getmethodSig(IJavaElement javaElement) throws JavaModelException{
	IMethod m=(IMethod)javaElement;
	return  m.getSignature() ;
	
}

public static String[] ClassPath(IJavaElement javaElement) throws CoreException{
	IJavaProject javaProject = javaElement.getJavaProject();
	return  JavaLangUtils.computeRuntimeClassPath(javaProject);
	
}
}
