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
package com.drgarbage.commandlinetool;

/**
 * A configuration class for creating graphs
 * 
 * @author Cihan Aydin
 * @version $Revision: 723 $
 * $Id: PartsOfClassFile.java 723 2015-05-12 08:40:35Z cihanaydin $
 */

public class ExportGraphConfiguration {
	private boolean exportComments = false;
	private boolean exportDecorations = false;
	private boolean exportGeometry = false;
	
	private boolean supressMessages = false;
	
	private GraphOutputTypes outputType;
	
	private String methodName;
	private String methodSignature;
	private String className;
	
	public String getMethodSignature() {
		return methodSignature;
	}
	public void setMethodSignature(String methodSignature) {
		this.methodSignature = methodSignature;
	}
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
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
	public boolean isSupressMessages() {
		return supressMessages;
	}
	public void setSupressMessages(boolean supressMessages) {
		this.supressMessages = supressMessages;
	}
	public GraphOutputTypes getOutputType() {
		return outputType;
	}
	public void setOutputType(GraphOutputTypes outputType) {
		this.outputType = outputType;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
}
