package com.drgarbage.bytecodevisualizer.actions;

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.jdt.internal.debug.core.model.JDIDebugElement;

import com.drgarbage.bytecodevisualizer.BytecodeVisualizerMessages;
import com.sun.jdi.ReferenceType;

import org.eclipse.ui.dialogs.FilteredList;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
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

/**
 * @author thanhnguyen
 * 
 */
@SuppressWarnings("restriction")
public class ClassFilteredList {

	private Point dialogSize = new Point(500, 500);
	private boolean fAllowDuplicates;
	private boolean fIgnoreCase;
	private boolean fMatchEmptyString = false;
	private String fFilter = "";
	private static String OS = System.getProperty("os.name").toLowerCase();
	private static String homeDir = System.getProperty("user.dir").toLowerCase();

	private Text pathText;
	private FilteredList fl;
	private Text fFilterText;
	private Composite dirPromptComposite;
	private Composite footerBtnComposite;
	final Shell shell = new Shell();
	private GridData gd;
	private int folderPromptWidth = 270;
	private String splitPattern = "\\.";

	private Button chooseDirBtn;
	private Button copyToPathBtn;
	private Button closeBtn;
	private DirectoryDialog directoryDialog;

	private Label hintLabel;
	private Label matchingClassLabel;

	private String dir;

	Set<Object> classSet = new HashSet<Object>();
	List<String> listOfClasses;

	public ClassFilteredList() {
		shell.setLayout(new GridLayout(1, false));
		init();
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

	private void init() {
		hintLabel = new Label(shell, SWT.NONE);
		hintLabel.setText(BytecodeVisualizerMessages.CFL_hint_text);

		createFilterText(shell);

		matchingClassLabel = new Label(shell, SWT.NONE);
		matchingClassLabel
				.setText(BytecodeVisualizerMessages.CFL_matching_class_name_text);

		/**
		 * Create a new FilteredList
		 */
		fl = new FilteredList(shell, SWT.BORDER | SWT.MULTI,
				new ClassLabelProvider(), fIgnoreCase, fAllowDuplicates,
				fMatchEmptyString);

		fl.setLayoutData(new GridData(GridData.FILL_BOTH));

		fillList(fl);

		dirPromptComposite = new Composite(shell, SWT.BORDER);
		dirPromptComposite.setLayout(new GridLayout(2, false));
		dirPromptComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		pathText = new Text(dirPromptComposite, SWT.SINGLE | SWT.BORDER);
		gd = new GridData();
		gd.widthHint = folderPromptWidth;
		pathText.setLayoutData(gd);

		chooseDirBtn = new Button(dirPromptComposite, SWT.PUSH);
		chooseDirBtn.setText(BytecodeVisualizerMessages.CFL_browse_btn_label);
		chooseDirBtn.setToolTipText(BytecodeVisualizerMessages.CFL_tooltip_browse_folder);

		chooseDirBtn.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				directoryDialog = new DirectoryDialog(shell);
				directoryDialog.setFilterPath(homeDir);
//				if (!isWindows())
////					directoryDialog.setFilterPath("/");
//					directoryDialog.setFilterPath(homeDir);
//				else
//					directoryDialog.setFilterPath("C:\\");
				directoryDialog.setMessage(BytecodeVisualizerMessages.CFL_directory_prompt_text);

				dir = directoryDialog.open();
				if (dir != null) {
					pathText.setText(dir);
				}
			}
		});
		footerBtnComposite = new Composite(shell, SWT.RIGHT_TO_LEFT);
		footerBtnComposite.setLayout(new GridLayout(2, false));
		
		copyToPathBtn = new Button(footerBtnComposite, SWT.NONE);
		copyToPathBtn.setText(BytecodeVisualizerMessages.CFL_copy_to_btn_text);
		copyToPathBtn.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				@SuppressWarnings("rawtypes")
				Iterator it = classSet.iterator();
				while (it.hasNext()) {

					String className = it.next().toString();
					String[] classShortName = className.split(splitPattern);

					String fullPathName = pathText.getText() + "/"
										+ classShortName[classShortName.length - 1]
										+ ".class";

					File file = new File(fullPathName);
					try {
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

		fl.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				classSet.clear();
				try {
					Object[] elements = getSelectedElements();
					for (Object element : elements) {
						classSet.add(element);
					}
				} catch (NullPointerException npe) {
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}

		});
	}

	public Object getFirstSelectedElement() {
		final Object[] selection = this.fl.getSelection();
		if (selection.length > 0) {
			return selection[0];
		}
		return null;
	}

	public Object[] getSelectedElements() {
		Object[] selection = this.fl.getSelection();
		if (selection.length == 0) {
			return null;
		}
		return selection;
	}

	/**
	 * Fill the FilteredList with classes from the JVM
	 * 
	 * @param fl
	 */
	public void fillList(FilteredList fl) {
		Object o = DebugUITools.getDebugContext();
		listOfClasses = new ArrayList<String>();

		if (o instanceof JDIDebugElement) {
			JDIDebugElement jdiDebugElement = (JDIDebugElement) o;
			com.sun.jdi.VirtualMachine vm = jdiDebugElement
					.getJavaDebugTarget().getVM();

			List<ReferenceType> l = vm.allClasses();

			for (ReferenceType r : l) {
				listOfClasses.add(r.name());
			}
		}
		fl.setElements(listOfClasses.toArray());
	}

	protected Text createFilterText(Composite parent) {
		Text text = new Text(parent, SWT.BORDER | SWT.SEARCH | SWT.ICON_CANCEL
				| SWT.ICON_SEARCH);

		GridData data = new GridData();
		data.grabExcessVerticalSpace = false;
		data.grabExcessHorizontalSpace = true;
		data.horizontalAlignment = GridData.FILL;
		data.verticalAlignment = GridData.BEGINNING;

		text.setLayoutData(data);
		text.setFont(parent.getFont());

		text.setText((fFilter == null ? "" : fFilter)); //$NON-NLS-1$

		Listener listener = new Listener() {

			public void handleEvent(Event e) {
				String key = fFilterText.getText();
				fl.setFilter(key);
			}
		};
		text.addListener(SWT.Modify, listener);

		text.addKeyListener(new KeyListener() {

			public void keyPressed(KeyEvent e) {
				if (e.keyCode == SWT.ARROW_DOWN) {
					fl.setFocus();
				}
			}

			public void keyReleased(KeyEvent e) {
			}
		});

		fFilterText = text;

		return text;
	}

	/**
	 * Check if the host system is Windows or not to define root path
	 * 
	 * @return boolean - true if Windows
	 */
	public static boolean isWindows() {
		return (OS.indexOf("win") >= 0);
	}
}
