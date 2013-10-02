/**
 * Copyright (c) 2008-2013, Dr. Garbage Community
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
package com.drgarbage.bytecode.jdi.dialogs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.jdt.internal.debug.core.model.JDIDebugElement;
import org.eclipse.jdt.internal.ui.JavaPluginImages;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.FilteredTree;
import org.eclipse.ui.dialogs.PatternFilter;

//import com.drgarbage.bytecode.jdi.dialogs.FilteredCheckboxTree.FilterableCheckboxTreeViewer;
import com.drgarbage.bytecodevisualizer.BytecodeVisualizerMessages;
import com.sun.jdi.ReferenceType;

/**
 * Dialog for selection the classes from the JVM.
 * @author Than
 * @version $Revision$
 * $Id$
 */
@SuppressWarnings("restriction")
public class FilteredTypeDialog {

	/**
	 * The main shell of the dialog.
	 */
	private final Shell shell = new Shell();
	
	/**
	 * The dialog size.
	 */
	private Point dialogSize = new Point(500, 500);
	
	private int folderPromptWidth = 270;

	private static String homeDir = System.getProperty("user.dir").toLowerCase();
	

	/* buttons */
	private Button chooseDirBtn;
	private Button copyToPathBtn;
	private Button closeBtn;
	
	/** 
	 * The Checkbox viewer.
	 */
	private CheckboxTreeViewer viewer;
	
	/**
	 * Hint text of the filter rules.
	 */
	private Label hintLabel;

	/**
	 * Constructor for the Filtered Type dialog.
	 */
	public FilteredTypeDialog() {
		shell.setLayout(new GridLayout(1, false));
		createControls();
		shell.setSize(dialogSize);

		/* Set the dialog to display at center width of the screen */
		Monitor primary = shell.getDisplay().getPrimaryMonitor();

		Rectangle bounds = primary.getBounds();
		Rectangle rect = shell.getBounds();

		int x = bounds.x + (bounds.width - rect.width) / 2;
		int y = 0;

		shell.setLocation(x, y);
		shell.open();
	}

	/**
	 * Creates dialog specific controls. 
	 */
	protected void createControls() {
		
		/* hint text */
		hintLabel = new Label(shell, SWT.NONE);
		hintLabel.setText(BytecodeVisualizerMessages.CFL_hint_text);

		/* Filtered list */
		PatternFilter pf = new PatternFilter();
		pf.setPattern("");
		FilteredCheckboxList ft = new FilteredCheckboxList(shell, SWT.BORDER | SWT.MULTI, pf);
		ft.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		/* initialize the viewer before filling the list */
		viewer = (CheckboxTreeViewer)ft.getViewer();
		viewer.setContentProvider(new TreeContentProvider());
		viewer.setLabelProvider(new LabelProvider());
		
		fillList(ft);
	
		/* buttons */
		Composite dirPromptComposite = new Composite(shell, SWT.BORDER);
		dirPromptComposite.setLayout(new GridLayout(2, false));
		dirPromptComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		final Text pathText = new Text(dirPromptComposite, SWT.SINGLE | SWT.BORDER);
		GridData gd = new GridData();
		gd.widthHint = folderPromptWidth;
		pathText.setLayoutData(gd);

		chooseDirBtn = new Button(dirPromptComposite, SWT.PUSH);
		chooseDirBtn.setText(BytecodeVisualizerMessages.CFL_browse_btn_label);
		chooseDirBtn.setToolTipText(BytecodeVisualizerMessages.CFL_tooltip_browse_folder);

		chooseDirBtn.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				DirectoryDialog directoryDialog = new DirectoryDialog(shell);
				
				Object o = DebugUITools.getDebugContext();
				if (o instanceof JDIDebugElement) {
					JDIDebugElement jdiDebugElement = (JDIDebugElement) o;
					jdiDebugElement.getLaunch().getLaunchConfiguration();
					//TODO: Initialize build path
				}
				
				directoryDialog.setFilterPath(homeDir);
				directoryDialog.setMessage(BytecodeVisualizerMessages.CFL_directory_prompt_text);

				String dir = directoryDialog.open();
				if (dir != null) {
					pathText.setText(dir);
				}
			}
		});
		
		Composite footerBtnComposite = new Composite(shell, SWT.RIGHT_TO_LEFT);
		footerBtnComposite.setLayout(new GridLayout(2, false));
		
		copyToPathBtn = new Button(footerBtnComposite, SWT.NONE);
		copyToPathBtn.setText(BytecodeVisualizerMessages.CFL_copy_to_btn_text);
		copyToPathBtn.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				
				Object[] selection = viewer.getCheckedElements();
					
				String searchString = Pattern.quote(".");
				String replacement = Matcher.quoteReplacement(File.separator);
				for(Object o: selection){
					String className = o.toString();
					String fullPathName = pathText.getText() + "/"
										+ className.replaceAll(searchString,  replacement)
										+ ".class";

					File file = new File(fullPathName);
					try {
						File directory = new File(file.getParentFile().getAbsolutePath());
						directory.mkdirs();
						
						file.createNewFile();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					
					
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});
		closeBtn = new Button(footerBtnComposite,SWT.NONE);
		closeBtn.setText(BytecodeVisualizerMessages.CFL_close_text);
		closeBtn.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.close();
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

	}


	/**
	 * Fill the {@link FilteredTree FilteredList} with classes from the JVM
	 * 
	 * @param fl the filtered checkbox list
	 */
	public void fillList(FilteredTree fl) {
		Object o = DebugUITools.getDebugContext();
		List<String> listOfClasses = new ArrayList<String>();

		if (o instanceof JDIDebugElement) {
			JDIDebugElement jdiDebugElement = (JDIDebugElement) o;
			com.sun.jdi.VirtualMachine vm = jdiDebugElement
					.getJavaDebugTarget().getVM();

			List<ReferenceType> l = vm.allClasses();

			for (ReferenceType r : l) {
				listOfClasses.add(r.name());
			}
		}
		
		viewer.setInput(listOfClasses.toArray());
	}

	/**
	 * Simple implementation of the Tree content provider.
	 * @see {@link ITreeContentProvider}
	 */
	public class TreeContentProvider implements ITreeContentProvider {
		
		/* (non-Javadoc)
		 * @see org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.Object)
		 */
		@Override
		public Object[] getChildren(Object arg0) {
			return null;
		}

		/* (non-Javadoc)
		 * @see org.eclipse.jface.viewers.ITreeContentProvider#getParent(java.lang.Object)
		 */
		@Override
		public Object getParent(Object arg0) {
			return null;
		}

		/* (non-Javadoc)
		 * @see org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang.Object)
		 */
		@Override
		public boolean hasChildren(Object arg0) {
			return false;
		}

		/* (non-Javadoc)
		 * @see org.eclipse.jface.viewers.ITreeContentProvider#getElements(java.lang.Object)
		 */
		@Override
		public Object[] getElements(Object arg0) {
			if(arg0 != null){
				return (Object[])arg0;
			}
			return null;
		}

		/* (non-Javadoc)
		 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
		 */
		@Override
		public void dispose() {
		}

		/* (non-Javadoc)
		 * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
		 */
		@Override
		public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
		}

	}
	
	/**
	 * Simple implementation of the LabelProvider.
	 * @see {@link ILabelProvider}
	 */
	public class LabelProvider implements ILabelProvider {

		/* (non-Javadoc)
		 * @see org.eclipse.jface.viewers.IBaseLabelProvider#addListener(org.eclipse.jface.viewers.ILabelProviderListener)
		 */
		public void addListener(ILabelProviderListener listener)  {
		}

		/* (non-Javadoc)
		 * @see org.eclipse.jface.viewers.IBaseLabelProvider#dispose()
		 */
		public void dispose() {
		}

		/* (non-Javadoc)
		 * @see org.eclipse.jface.viewers.IBaseLabelProvider#isLabelProperty(java.lang.Object, java.lang.String)
		 */
		public boolean isLabelProperty(Object element, String property) {
			return false;
		}

		/* (non-Javadoc)
		 * @see org.eclipse.jface.viewers.IBaseLabelProvider#removeListener(org.eclipse.jface.viewers.ILabelProviderListener)
		 */
		public void removeListener(ILabelProviderListener listener) {
		}

		/* (non-Javadoc)
		 * @see org.eclipse.jface.viewers.ILabelProvider#getImage(java.lang.Object)
		 */
		public Image getImage(Object element) {
			return JavaPluginImages.DESC_OBJS_CLASS.createImage();
		}

		/* (non-Javadoc)
		 * @see org.eclipse.jface.viewers.ILabelProvider#getText(java.lang.Object)
		 */
		public String getText(Object element) {
			return element.toString();
		}
	}
	
}
