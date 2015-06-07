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
package com.drgarbage.commandlinetool.intf;

import com.drgarbage.commandlinetool.impl.GraphOutputTypes;

/**
 * The interface for ExportGraphConfiguration class alles false
 * 
 * @author Cihan Aydin
 * @version $Revision: 739 $
 * $Id: IGraphConfiguration.java 739 2015-05-17 21:22:23Z cihanaydin $
 */
public interface IGraphConfiguration {
	//TODO setter methoden
	//TODO return true or false
	/**
	 * Returns true if export comments option is set, otherwise false.
	 * Getter for option show export comments.
	 * @return <code> true </code> or <code>false</code>
	 */
	public boolean isExportComments();
	
	/**
	 * Getter for option show export decorations.
	 * @return <code> true </code> or <code>false</code>
	 */
	public boolean isExportDecorations();
	
	/**
	 * Getter for option show export geometry.
	 * @return <code> true </code> or <code>false</code>
	 */
	public boolean isExportGeometry();
	
	/**
	 * Getter for option show suppress messages.
	 * @return <code> true </code> or <code>false</code>
	 */
	public boolean isSuppressMessages();
	
	/**
	 * Getter for show start vertex.
	 * @return boolean
	 */
	public boolean isStartVertex();
	
	/**
	 * Getter for show end vertex.
	 * @return boolean
	 */
	public boolean isExitVertex();
	
	/**
	 * Getter for show back edge
	 * @return boolean
	 */
	public boolean isBackEdge();
	
	/**
	 * Getter graph output type
	 * @return GraphOutputTypes
	 */
	public GraphOutputTypes getOutputType();
	
	public void setExportComments(boolean exportComments);
	
	public void setExportDecorations(boolean exportDecorations);
	
	public void setExportGeometry(boolean exportGeometry);
	
	public void setSupressMessages(boolean supressMessages);
	
	public void setOutputType(GraphOutputTypes outputType);
	
	public void setStartvertex(boolean startvertex);
	
	public void setEndvertex(boolean endvertex);
	
	public void setBackedge(boolean backedge);
}
