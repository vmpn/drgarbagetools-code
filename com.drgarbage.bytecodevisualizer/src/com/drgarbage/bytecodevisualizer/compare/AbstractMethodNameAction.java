package com.drgarbage.bytecodevisualizer.compare;

import java.util.ArrayList;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.CompareUI;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jdt.core.IClassFile;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

/**
 * The abstract action to implement action for comparing class files.
 * 
 * @author Sergej Alekseev edit by Lars Lewald
 * @version $Revision: 203 $
 * $Id: AbstractCompareClassFileAction.java 203 2013-06-07 09:27:41Z salekseev $
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
		MessageDialog.openInformation(shell, "Methodname",String.valueOf(element1)+String.valueOf(element2));
		System.out.println(element1.getPath());
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
