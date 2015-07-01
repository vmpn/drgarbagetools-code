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
package com.drgarbage.commandlinetool.impl;

import com.drgarbage.commandlinetool.intf.GraphOutputTypes;
import com.drgarbage.commandlinetool.intf.IGraphConfiguration;

/**
 * A configuration class for creating graphs
 * 
 * @author Cihan Aydin
 * @version $Revision: 723 $
 * $Id: ExportGraphConfiguration.java 723 2015-05-12 08:40:35Z cihanaydin $
 */
public class GraphConfiguration implements IGraphConfiguration {
	private boolean exportComments = false;
	private boolean exportDecorations = false;
	private boolean exportGeometry = false;
	private boolean supressMessages = false;
	private boolean startvertex = false;
	private boolean endvertex = false;
	private boolean backedge = false;

	private GraphOutputTypes outputType;
	
	/* (non-Javadoc)
	 * @see com.drgarbage.commandlinetool.intf.IGraphConfiguration#isExportComments()
	 */
	public boolean isExportComments() {
		return exportComments;
	}
	
	/* (non-Javadoc)
	 * @see com.drgarbage.commandlinetool.intf.IGraphConfiguration#setExportComments(boolean)
	 */
	public void setExportComments(boolean exportComments) {
		this.exportComments = exportComments;
	}
	
	/* (non-Javadoc)
	 * @see com.drgarbage.commandlinetool.intf.IGraphConfiguration#isExportDecorations()
	 */
	public boolean isExportDecorations() {
		return exportDecorations;
	}
	
	/* (non-Javadoc)
	 * @see com.drgarbage.commandlinetool.intf.IGraphConfiguration#setExportDecorations(boolean)
	 */
	public void setExportDecorations(boolean exportDecorations) {
		this.exportDecorations = exportDecorations;
	}
	
	/* (non-Javadoc)
	 * @see com.drgarbage.commandlinetool.intf.IGraphConfiguration#isExportGeometry()
	 */
	public boolean isExportGeometry() {
		return exportGeometry;
	}
	
	/* (non-Javadoc)
	 * @see com.drgarbage.commandlinetool.intf.IGraphConfiguration#setExportGeometry(boolean)
	 */
	public void setExportGeometry(boolean exportGeometry) {
		this.exportGeometry = exportGeometry;
	}
	
	/* (non-Javadoc)
	 * @see com.drgarbage.commandlinetool.intf.IGraphConfiguration#isSuppressMessages()
	 */
	public boolean isSuppressMessages() {
		return supressMessages;
	}
	
	/* (non-Javadoc)
	 * @see com.drgarbage.commandlinetool.intf.IGraphConfiguration#setSuppressMessages(boolean)
	 */
	public void setSuppressMessages(boolean supressMessages) {
		this.supressMessages = supressMessages;
	}
	
	/* (non-Javadoc)
	 * @see com.drgarbage.commandlinetool.intf.IGraphConfiguration#getOutputType()
	 */
	public GraphOutputTypes getOutputType() {
		return outputType;
	}
	
	/* (non-Javadoc)
	 * @see com.drgarbage.commandlinetool.intf.IGraphConfiguration#setOutputType(com.drgarbage.commandlinetool.impl.GraphOutputTypes)
	 */
	public void setOutputType(GraphOutputTypes outputType) {
		this.outputType = outputType;
	}
	
	/* (non-Javadoc)
	 * @see com.drgarbage.commandlinetool.intf.IGraphConfiguration#isStartVertex()
	 */
	public boolean isStartVertex() {
		return startvertex;
	}
	
	/* (non-Javadoc)
	 * @see com.drgarbage.commandlinetool.intf.IGraphConfiguration#setStartVertex(boolean)
	 */
	public void setStartVertex(boolean startvertex) {
		this.startvertex = startvertex;
	}
	
	/* (non-Javadoc)
	 * @see com.drgarbage.commandlinetool.intf.IGraphConfiguration#isEndVertex()
	 */
	public boolean isEndVertex() {
		return endvertex;
	}
	
	/* (non-Javadoc)
	 * @see com.drgarbage.commandlinetool.intf.IGraphConfiguration#setEndVertex(boolean)
	 */
	public void setEndVertex(boolean endvertex) {
		this.endvertex = endvertex;
	}
	
	/* (non-Javadoc)
	 * @see com.drgarbage.commandlinetool.intf.IGraphConfiguration#isBackEdge()
	 */
	public boolean isBackEdge() {
		return backedge;
	}
	
	/* (non-Javadoc)
	 * @see com.drgarbage.commandlinetool.intf.IGraphConfiguration#setBackEdge(boolean)
	 */
	public void setBackEdge(boolean backedge) {
		this.backedge = backedge;
	}
}
