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
	
	public GraphConfiguration() {
		this(null);
	}
	public GraphConfiguration(String arg) {
		if (arg == null || arg.equals("")) {
			this.setOutputType(GraphOutputTypes.ExportFormat_PrintNodes);
		} else {
			for (Character c : arg.toCharArray()) {
				switch(c) {
					case 'c':
						this.setExportComments(true);
						break;
					case 'd':
						this.setExportDecorations(true);
						break;
					case 'g':
						this.setExportGeometry(true);
						break;
					case 'm':
						this.setSuppressMessages(true);
						break;
					case 's':
						this.setStartVertex(true);
						break;
					case 'e':
						this.setEndVertex(true);
						break;
					case 'b':
						this.setBackEdge(true);
						break;
					case 'D':
						this.setOutputType(GraphOutputTypes.ExportFormat_DOT_Graph_Language);
						break;
					case 'X':
						this.setOutputType(GraphOutputTypes.ExportFormat_GraphXML_XML_Based);
						break;
					case 'M':
						this.setOutputType(GraphOutputTypes.ExportFormat_GraphML_XML_Based);
						break;
					case 'N':
						this.setOutputType(GraphOutputTypes.ExportFormat_PrintNodes);
						break;
					default:
						break;
				}
			}
		}
	}

	public boolean isExportComments() {
		return exportComments;
	}
	public void setExportComments(boolean exportComments) {
		this.exportComments = exportComments;
	}
	public boolean isExportDecorations() {
		return exportDecorations;
	}
	public void setExportDecorations(boolean exportDecorations) {
		this.exportDecorations = exportDecorations;
	}
	public boolean isExportGeometry() {
		return exportGeometry;
	}
	public void setExportGeometry(boolean exportGeometry) {
		this.exportGeometry = exportGeometry;
	}
	public boolean isSuppressMessages() {
		return supressMessages;
	}
	public void setSuppressMessages(boolean supressMessages) {
		this.supressMessages = supressMessages;
	}
	public GraphOutputTypes getOutputType() {
		return outputType;
	}
	public void setOutputType(GraphOutputTypes outputType) {
		this.outputType = outputType;
	}
	public boolean isStartVertex() {
		return startvertex;
	}
	public void setStartVertex(boolean startvertex) {
		this.startvertex = startvertex;
	}
	public boolean isExitVertex() {
		return endvertex;
	}
	public void setEndVertex(boolean endvertex) {
		this.endvertex = endvertex;
	}
	public boolean isBackEdge() {
		return backedge;
	}
	public void setBackEdge(boolean backedge) {
		this.backedge = backedge;
	}
}
