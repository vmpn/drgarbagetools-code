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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

import com.drgarbage.javalang.JavaLangUtils;
/**
 * This class represents an argument parser for CommandLineTool
 * 
 * @author Cihan Aydin
 * @version $Revision: 723 $
 * $Id: ParseArguments.java 723 2015-05-12 08:40:35Z cihanaydin $
 */
public class ParseArguments {
	private String packageName = null;
	private String className = null;
	private String methodName = null;
	private String pathToFile = null;
	private InputStream inputStream;
	private ByteCodeConfiguration byteCodeConfiguration;
	private GraphOutputTypes graphOutputType;
	private ExportGraphConfiguration exportGraphConfiguration;
	
	public ParseArguments(String[] args) throws IOException {
		
		for (String arg : args) {
			if (arg.contains("?") || arg.contains("help")){
				showHelpAndExit();
			}
			else if (arg.startsWith("-BC:")){
				byteCodeConfiguration = new ByteCodeConfiguration();
				String[] argSplitted = arg.split(":");
				if (argSplitted[1].contains("c")){
					byteCodeConfiguration.setShowConstantPool(true);
				}
				if (argSplitted[1].contains("e")){
					byteCodeConfiguration.setShowExceptionTable(true);
				}
				if (argSplitted[1].contains("l")){
					byteCodeConfiguration.setShowLineNumberTable(true);
				}
				if (argSplitted[1].contains("v")){
					byteCodeConfiguration.setShowLocalVariableTable(true);
				}
				if (argSplitted[1].contains("m")){
					byteCodeConfiguration.setShowMaxs(true);
				}
				if (argSplitted[1].contains("r")){
					byteCodeConfiguration.setShowRelativeBranchTargetOffsets(true);
				}
				if (argSplitted[1].contains("s")){
					byteCodeConfiguration.setShowSourceLineNumbers(true);
				}
				setGraphOutputType(GraphOutputTypes.ExportFormat_ByteCode);
			}
			else if (arg.startsWith("-XML:")) {
				setGraphOutputType(GraphOutputTypes.ExportFormat_GraphXML_XML_Based);
				setGraphConfiguration(arg);
			}
			else if (arg.startsWith("-ML:")) {
				setGraphOutputType(GraphOutputTypes.ExportFormat_GraphML_XML_Based);
				setGraphConfiguration(arg);
			}
			else if (arg.startsWith("-DOT:")) {
				setGraphOutputType(GraphOutputTypes.ExportFormat_DOT_Graph_Language);
				setGraphConfiguration(arg);
			}
			else if (arg.startsWith("-NODES:")) {
				setGraphOutputType(GraphOutputTypes.ExportFormat_PrintNodes);
				setGraphConfiguration(arg);
			}
			else if (arg.startsWith("-method:")){
				String[] argSplitted = arg.split(":");
				methodName=argSplitted[1];
			}
			else if (arg.startsWith("-class:")){
				String[] argSplitted = arg.split(":");
				className=argSplitted[1];
			}
			else if (arg.startsWith("-signature:")){
				String[] argSplitted = arg.split(":");
				exportGraphConfiguration.setMethodSignature(argSplitted[1]);
			}
			else if (arg.endsWith(".jar")) {
				pathToFile = arg;
			}
			else if (arg.endsWith(".class")) {
				pathToFile = arg;
			}
			else if (arg.startsWith("-package:")){
				String[] argSplitted = arg.split(":");
				packageName = argSplitted[1];
			}
		}
		
		if (pathToFile.endsWith(".class")) {
			setInputStream(getContentStreamFromClassFile(pathToFile));
		}
		else {
			setInputStream(getcontentStreamFromJarFile(pathToFile, packageName, className));
		}
		
		if((byteCodeConfiguration == null && exportGraphConfiguration == null) || getInputStream() == null) {
			if (getInputStream() == null) {
				System.out.println("ERROR: class not found!!!");
			} else {
				System.out.println("ERROR while parsing arguments");
			}
			showHelpAndExit();
		}
		
		switch (this.getGraphOutputType()) {
		case ExportFormat_DOT_Graph_Language:
		case ExportFormat_GraphXML_XML_Based:
		case ExportFormat_GraphML_XML_Based:
		case ExportFormat_PrintNodes:
			prepareGraphExportObject();
			break;
		case ExportFormat_ByteCode:
			prepareByteCodeConfigurationObject();
			break;
		default:
			break;
		}
		
		
	}
	private void prepareByteCodeConfigurationObject() throws IOException {
		byteCodeConfiguration.setClassName(className);
	}
	private void prepareGraphExportObject() throws IOException {
		exportGraphConfiguration.setMethodName(methodName);
		exportGraphConfiguration.setClassName(className);
		
	}
	private void setGraphConfiguration(String _arg) {
		exportGraphConfiguration = new ExportGraphConfiguration();
		String[] args = _arg.split(":");
		String arg = args[1];
		
		if (arg.contains("c")) {
			exportGraphConfiguration.setExportComments(true);
		}
		if (arg.contains("d")) {
			exportGraphConfiguration.setExportDecorations(true);
		}
		if (arg.contains("g")) {
			exportGraphConfiguration.setExportGeometry(true);
		}
		if (arg.contains("m")) {
			exportGraphConfiguration.setSupressMessages(true);
		}
		exportGraphConfiguration.setOutputType(graphOutputType);
	}
	private static InputStream getcontentStreamFromJarFile(String pathToJarFile, String packageName, String className) throws IOException {
		return JavaLangUtils.findResource(new String[] {pathToJarFile}, packageName, className);
	}

	private static InputStream getContentStreamFromClassFile(String pathToFile) throws FileNotFoundException {
		File f = new File(pathToFile);
		return new FileInputStream(f);
	}
	public InputStream getInputStream() {
		return inputStream;
	}
	public void setInputStream(InputStream is) {
		this.inputStream = is;
	}
	public ByteCodeConfiguration getByteCodeConfiguration() {
		return byteCodeConfiguration;
	}
	public void setByteCodeConfiguration(ByteCodeConfiguration byteCodeConfiguration) {
		this.byteCodeConfiguration = byteCodeConfiguration;
	}
	
	private void showHelpAndExit() throws IOException {
	    BufferedReader br = new BufferedReader(new FileReader("arguments.txt"));
	    try {
	        StringBuilder sb = new StringBuilder();
	        String line = br.readLine();

	        while (line != null) {
	            sb.append(line);
	            sb.append(System.lineSeparator());
	            line = br.readLine();
	        }
	        String everything = sb.toString();
	        System.out.println(everything);
	    } finally {
	        br.close();
	    }
	    

		System.exit(0);
	}
	public GraphOutputTypes getGraphOutputType() {
		return graphOutputType;
	}
	public void setGraphOutputType(GraphOutputTypes gc) {
		this.graphOutputType = gc;
	}
	public ExportGraphConfiguration getExportGraphConfiguration() {
		return exportGraphConfiguration;
	}
	public void setExportGraphConfiguration(ExportGraphConfiguration exportGraphConfiguration) {
		this.exportGraphConfiguration = exportGraphConfiguration;
	}
	
}
