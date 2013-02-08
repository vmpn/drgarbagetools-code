/**
 * Copyright (c) 2008-2012, Dr. Garbage Community
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
package com.drgarbage.bytecodevisualizer.view;

import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.IPage;
import org.eclipse.ui.part.IPageBookViewPage;
import org.eclipse.ui.part.MessagePage;
import org.eclipse.ui.part.PageBook;
import org.eclipse.ui.part.PageBookView;

import com.drgarbage.asm.render.intf.IMethodSection;
import com.drgarbage.bytecodevisualizer.editors.BytecodeEditor;


/**
 * Operand Stack View.
 * 
 * @author Sergej Alekseev
 * @version $Revision: 26 $
 * $Id$
 */
public class OperandStackView extends PageBookView {

	/**
	 * Sets the input - the list of the byte code instructions
	 * for the table Viewer of the current page.
	 * @param methodSection
	 */
	public void setInput(IMethodSection m) {
		OperandStackViewPage page;
		IWorkbenchPart part = getBootstrapPart();
		if(part instanceof BytecodeEditor){
			page = (OperandStackViewPage) getPageRec(part).page;
			page.setInput(m);
		}		
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.PageBookView#doCreatePage(org.eclipse.ui.IWorkbenchPart)
	 */
	protected PageRec doCreatePage(IWorkbenchPart part) {
		
		/* set reference in the editor */
        if(part instanceof BytecodeEditor){
        	BytecodeEditor be = (BytecodeEditor) part;
        	be.setOperandStackView(this);
        	
        	OperandStackViewPage page = new OperandStackViewPageIml();        	
			initPage((IPageBookViewPage) page);
            page.createControl(getPageBook());
            page.setEditor(be);
            
            return new PageRec(part, page);
        }

        return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.PageBookView#getBootstrapPart()
	 */
	@Override
	protected IWorkbenchPart getBootstrapPart() {
        IWorkbenchPage page = getSite().getPage();
        if (page != null) {
			return page.getActiveEditor();
		}

        return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.debug.ui.AbstractDebugView#isImportant(org.eclipse.ui.IWorkbenchPart)
	 */
	@Override
	protected boolean isImportant(IWorkbenchPart part) {
		/* we are interested only on an editor */
		return part instanceof IEditorPart ? true : false;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.PageBookView#createDefaultPage(org.eclipse.ui.part.PageBook)
	 */
	protected IPage createDefaultPage(PageBook book) {
        MessagePage page = new MessagePage();
		page = new MessagePage();
        initPage(page);
        page.createControl(book);
        page.setMessage("OperandStack View is not avaliable");//TODO: define constant for default page
        
        return page;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.PageBookView#doDestroyPage(org.eclipse.ui.IWorkbenchPart, org.eclipse.ui.part.PageBookView.PageRec)
	 */
	@Override
	protected void doDestroyPage(IWorkbenchPart part, PageRec rec) {
        rec.page.dispose();
	}
}
