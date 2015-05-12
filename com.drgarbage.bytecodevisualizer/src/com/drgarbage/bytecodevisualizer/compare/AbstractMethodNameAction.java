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

import java.util.ArrayList;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.CompareUI;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IClassFile;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.jdt.core.IType;

import com.drgarbage.bytecodevisualizer.BytecodeVisualizerPlugin;
import com.drgarbage.core.CoreMessages;
import com.drgarbage.core.ActionUtils;
import com.drgarbage.utils.Messages;

/**
 * The abstract action to implement action for comparing class files.
 * 
 * @author Lars Lewald
 * @version $Revision$
 * $Id$
 */
public abstract class AbstractMethodNameAction implements IObjectActionDelegate {
	private Shell shell;
	protected IStructuredSelection selection;

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction, org.eclipse.jface.viewers.ISelection)
	 */
	public void selectionChanged(IAction action, ISelection newSelection) {
		if (newSelection instanceof IStructuredSelection) {
			this.selection = (IStructuredSelection) newSelection;
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IObjectActionDelegate#setActivePart(org.eclipse.jface.action.IAction, org.eclipse.ui.IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		/* nothing to do */
	}

	/**
	 * Opens a compare editor.
	 * @param element1 The first element that has to be compared.
	 * @param element2 the second element that has to be compared.
	 * @throws Exception
	 */
	protected void run(IJavaElement element1, IJavaElement element2) throws Exception {
		String mMethodName1=null;
		String mMethodName2=null;
		String mMethodSignature1=null;
		String mMethodSignature2=null;
		String mClassName1=null;
		String mClassName2=null;
		String mPackage1=null;
		String mPackage2=null;
		/* java model elements */
		
		IMethod IMethod1 = (IMethod)element1;
		IMethod  IMethod2= (IMethod)element2;
		IType type1 = IMethod1.getDeclaringType();
		IType type2 = IMethod1.getDeclaringType();
		
		/* Methodname */
		if(IMethod1.isConstructor()){
			mMethodName1 = "<init>";
		}
		else{				
			mMethodName1 = IMethod1.getElementName();
		}
		
		if(IMethod2.isConstructor()){
			mMethodName2 = "<init>";
		}
		else{				
			mMethodName2 = IMethod2.getElementName();
		}
		
		/* Package and Classname */
		mClassName1 = type1.getFullyQualifiedName();
		mPackage1 = type1.getPackageFragment().getElementName();
		mClassName1 = mClassName1.replace(mPackage1 + ".", "");
	
		
		mClassName2 = type2.getFullyQualifiedName();
		mPackage2 = type2.getPackageFragment().getElementName();
		mClassName2 = mClassName1.replace(mPackage1 + ".", "");
		
		/* Method Signature */
		if(IMethod1.isBinary()){
			mMethodSignature1 = IMethod1.getSignature();
		}	
		else{
			try{
				/* resolve parameter signature */
				StringBuffer buf = new StringBuffer("(");
				String[] parameterTypes = IMethod1.getParameterTypes();
				String res = null;
				for(int i = 0; i < parameterTypes.length; i++){
					res = ActionUtils.getResolvedTypeName(parameterTypes[i], IMethod1.getDeclaringType());
					buf.append(res);
				}						
				buf.append(")");
				
				res = ActionUtils.getResolvedTypeName(IMethod1.getReturnType(), IMethod1.getDeclaringType());
				buf.append(res);
				
				mMethodSignature1=buf.toString();
				
			}catch(IllegalArgumentException e){
				BytecodeVisualizerPlugin.getDefault().getLog().log(new Status(IStatus.ERROR,BytecodeVisualizerPlugin.PLUGIN_ID, e.getMessage() , e));
				Messages.error(e.getMessage() + CoreMessages.ExceptionAdditionalMessage);
				return;
			}
		}
		
		
		
		if(IMethod2.isBinary()){
			mMethodSignature2 = IMethod2.getSignature();
		}	
		else{
			try{
				/* resolve parameter signature */
				StringBuffer buf = new StringBuffer("(");
				String[] parameterTypes = IMethod2.getParameterTypes();
				String res = null;
				for(int i = 0; i < parameterTypes.length; i++){
					res = ActionUtils.getResolvedTypeName(parameterTypes[i], IMethod2.getDeclaringType());
					buf.append(res);
				}						
				buf.append(")");
				
				res = ActionUtils.getResolvedTypeName(IMethod2.getReturnType(), IMethod2.getDeclaringType());
				buf.append(res);
				
				mMethodSignature2=buf.toString();
				
			}catch(IllegalArgumentException e){
				BytecodeVisualizerPlugin.getDefault().getLog().log(new Status(IStatus.ERROR,BytecodeVisualizerPlugin.PLUGIN_ID, e.getMessage() , e));
				Messages.error(e.getMessage() + CoreMessages.ExceptionAdditionalMessage);
				return;
			}
		}
		
		MessageDialog.openInformation(shell, "Methodname",mPackage1+"."+mClassName1+"."+mMethodName1+"."+mMethodSignature1+"\n"+mPackage2+"."+mClassName2+"."+mMethodName2+"."+mMethodSignature2);
		}
		
	

	/**
	 * Returns an array of selected resources.
	 * @return array of selected resources
	 */
	protected IJavaElement[] getSelectedResources() {
		ArrayList<Object> resources = null;
		if (!selection.isEmpty()) {
			resources = new ArrayList<Object>();

			Object[] elements = selection.toArray();
			for(Object o: elements){
				if (o instanceof IFile) {
					resources.add(JavaCore.create((IFile)o));
				} 
				else if (o instanceof IJavaElement) {
					resources.add(o);
				} 
				else if (o instanceof IAdaptable) {
					IAdaptable a = (IAdaptable) o;
					Object adapter = a.getAdapter(IFile.class);
					if (adapter instanceof IFile) {
						resources.add(JavaCore.create((IFile)adapter));
					}
					else {
						adapter = a.getAdapter(ICompilationUnit.class);
						if (adapter instanceof ICompilationUnit) {
							resources.add(adapter);
						}
						else{
							adapter = a.getAdapter(IClassFile.class);
							if (adapter instanceof IClassFile) {
								resources.add(adapter);
								
							}
						}
					}
				}
			}
		}

		if (resources != null && !resources.isEmpty()) {
			return resources.toArray(new IJavaElement[resources.size()]);
		}

		return new IJavaElement[0];
	}
}
