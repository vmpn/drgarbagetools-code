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


import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;


import com.drgarbage.core.ActionUtils;
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


public static String[] ClassPath(IJavaElement javaElement) throws CoreException{
	IJavaProject javaProject = javaElement.getJavaProject();
	return  JavaLangUtils.computeRuntimeClassPath(javaProject);
	
}
}
