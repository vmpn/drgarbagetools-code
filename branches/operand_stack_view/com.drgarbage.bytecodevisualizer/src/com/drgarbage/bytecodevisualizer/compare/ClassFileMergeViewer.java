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
package com.drgarbage.bytecodevisualizer.compare;

import java.io.ByteArrayInputStream;
import java.text.ParseException;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.ResourceNode;
import org.eclipse.compare.contentmergeviewer.IMergeViewerContentProvider;
import org.eclipse.compare.contentmergeviewer.TextMergeViewer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.text.TextViewer;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.swt.widgets.Composite;

import com.drgarbage.bytecodevisualizer.BytecodeVisualizerPlugin;
import com.drgarbage.classfile.editors.ClassFileConfiguration;
import com.drgarbage.classfile.editors.ClassFileParser;
import com.drgarbage.classfile.editors.ColorManager;
import com.drgarbage.core.CoreMessages;
import com.drgarbage.utils.Messages;

/**
 * The compare and merge viewer with two side-by-side content areas.
 * The viewer modifies the input to represent the class file in 
 * a readable format by using the {@link ClassFileParser} and provides 
 * the syntax highlighting from the {@link ClassFileConfiguration}.
 *
 * @see IMergeViewerContentProvider
 * @see TextMergeViewer
 * 
 * @author Sergej Alekseev
 * @version $Revision$
 * $Id$
 */
public class ClassFileMergeViewer extends TextMergeViewer{

	public ClassFileMergeViewer(Composite parent,
			CompareConfiguration configuration) {
		super(parent, configuration);
		
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.compare.contentmergeviewer.TextMergeViewer#configureTextViewer(org.eclipse.jface.text.TextViewer)
	 */
	protected void configureTextViewer(TextViewer textViewer) {
		ColorManager colorManager = new ColorManager();
		ClassFileConfiguration conf = new ClassFileConfiguration(colorManager);
		
		if (textViewer instanceof SourceViewer) {
			SourceViewer sourceViewer = (SourceViewer) textViewer;
			sourceViewer.configure(conf);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.compare.contentmergeviewer.ContentMergeViewer#flushLeft(org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void flushLeft(IProgressMonitor monitor) {
		Object input = getInput();
		IMergeViewerContentProvider content = (IMergeViewerContentProvider) getContentProvider();

		boolean rightEmpty = content.getRightContent(input) == null;

		if (getCompareConfiguration().isLeftEditable() && isLeftDirty()) {
			byte[] bytes = getContents(true);
			if (rightEmpty && bytes != null && bytes.length == 0)
				bytes = null;
			setLeftDirty(false);
			
			doSaveContent(monitor, bytes, 
					(ResourceNode) content.getLeftContent(input));
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.compare.contentmergeviewer.ContentMergeViewer#flushRight(org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void flushRight(IProgressMonitor monitor) {
		Object input = getInput();
		IMergeViewerContentProvider content = (IMergeViewerContentProvider) getContentProvider();

		boolean leftEmpty = content.getLeftContent(input) == null;

		if (getCompareConfiguration().isRightEditable() && isRightDirty()) {
			byte[] bytes = getContents(false);
			if (leftEmpty && bytes != null && bytes.length == 0)
				bytes = null;
			setRightDirty(false);
						
			doSaveContent(monitor, bytes, 
					(ResourceNode) content.getRightContent(input));
		}
	}
	
	/**
	 * Flushes the modified content back to class file.
	 * @param monitor a progress monitor
	 * @param contentToSave new content
	 * @param resource the class file has to be saved
	 */
	private void doSaveContent(IProgressMonitor monitor, 
			byte[] contentToSave, 
			ResourceNode resource)
	{
		byte[] bytesToSave = null;
		try {
			bytesToSave = ClassFileParser.getClasFileBytesFromString(new String(contentToSave));

			IPath path = resource.getResource().getFullPath(); 
			final IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(path);

			file.setContents(new ByteArrayInputStream(bytesToSave), 
					true,   /* keep saving, even if IFile is out 
					         * of sync with the Workspace */ 
					false,  /* dont keep history */
					monitor /* progress monitor */
					);

		} catch (ParseException e) {
			handleException(ParseException.class.getName(), e);
			Messages.error(ParseException.class.getName() +
					CoreMessages.ExceptionAdditionalMessage);
		} catch (CoreException e) {
			handleException(ParseException.class.getName(), e);
			Messages.error(CoreException.class.getName() +
					CoreMessages.ExceptionAdditionalMessage);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.compare.contentmergeviewer.TextMergeViewer#setupDocument(org.eclipse.jface.text.IDocument)
	 */
	protected void setupDocument(org.eclipse.jface.text.IDocument document) {
		final byte[] bytes = document.get().getBytes();
		
		/* changing the content representation */
		ClassFileParser cfp = new ClassFileParser();
		try {
			String s = cfp.parseClassFile(bytes);
			document.set(s);
		} catch (ParseException e) {
			handleException(ParseException.class.getName(), e);
			Messages.error(CoreException.class.getName() +
					CoreMessages.ExceptionAdditionalMessage);
		}
		
		/* call setupDocument to make the new content visible */
		super.setupDocument(document);
		setLeftDirty(false);
		setRightDirty(false);
	}
	
	private void handleException(String message, Throwable t){
		IStatus status = BytecodeVisualizerPlugin.createErrorStatus(message, t);
		BytecodeVisualizerPlugin.log(status);
	}
	
}