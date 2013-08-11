package com.drgarbage.bytecodevisualizer.actions;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.jdt.internal.debug.core.model.JDIDebugElement;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.FilteredList;

import com.sun.jdi.ReferenceType;

public class ClassFilteredList {

	FilteredList fl;
	Text fFilterText;
	String fFilter;
	final Shell shell = new Shell();
	Text pathText;
	Composite footerComp;
	GridData gd;
	Button bt;
	DirectoryDialog directoryDialog;
	String dir;
	private static String OS = System.getProperty("os.name").toLowerCase();

	public ClassFilteredList() {
		shell.setLayout(new GridLayout(1, false));
		init();
		shell.setSize(300, 400);
		shell.open();
	}

	@SuppressWarnings("restriction")
	private void init() {

		createFilterText(shell);
		fl = new FilteredList(shell, SWT.BORDER | SWT.MULTI,
				new ClassLabelProvider(), true, true, true);

		fl.setLayoutData(new GridData(GridData.FILL_BOTH));

		fillList(fl);

		footerComp = new Composite(shell, SWT.BORDER);
		footerComp.setLayout(new GridLayout(2, false));
		footerComp.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		pathText = new Text(footerComp, SWT.BORDER);
		gd = new GridData();
		gd.widthHint = 230;
		pathText.setLayoutData(gd);

		bt = new Button(footerComp, SWT.None);
		bt.setText("...");
		bt.setToolTipText("Choose where to save classes");
		bt.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				directoryDialog = new DirectoryDialog(shell);

				if (!isWindows())
					directoryDialog.setFilterPath("/");
				else
					directoryDialog.setFilterPath("C:\\");
				directoryDialog
						.setMessage("Please select a directory and click OK");

				dir = directoryDialog.open();
				if (dir != null) {
					pathText.setText(dir);
				}
			}
		});
	}

	@SuppressWarnings("restriction")
	public void fillList(FilteredList fl) {
		Object o = DebugUITools.getDebugContext();
		List<String> list = new ArrayList<String>();

		if (o instanceof JDIDebugElement) {
			JDIDebugElement jdiDebugElement = (JDIDebugElement) o;
			com.sun.jdi.VirtualMachine vm = jdiDebugElement
					.getJavaDebugTarget().getVM();

			List<ReferenceType> l = vm.allClasses();

			for (ReferenceType r : l) {
				list.add(r.name());
			}
		}
		fl.setElements(list.toArray());

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
				fl.setFilter(fFilterText.getText());
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

	public static boolean isWindows() {
		return (OS.indexOf("win") >= 0);
	}

}
