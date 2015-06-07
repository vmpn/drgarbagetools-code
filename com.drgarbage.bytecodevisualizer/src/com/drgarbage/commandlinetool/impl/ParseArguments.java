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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

import com.drgarbage.commandlinetool.intf.IByteCodeConfiguration;
import com.drgarbage.commandlinetool.intf.ICommandLineTool;
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
	private String methodSignature = null;
	private InputStream inputStream;
	private ByteCodeConfiguration byteCodeConfiguration;
	private GraphOutputTypes graphOutputType;
	private GraphConfiguration exportGraphConfiguration;
	
	/**
	 * Constructor, which is parsing the arguments
	 * @param args the program arguments
	 * @throws IOException
	 */
	public ParseArguments(String[] args) throws IOException {
		
		for (String arg : args) {
			if (arg.contains("?") || arg.contains("help")){
				showHelpAndExit();
			}
			else if (arg.startsWith("-BC:")){
				String[] argSplitted = arg.split(":");
				byteCodeConfiguration = new ByteCodeConfiguration(argSplitted[1]);
				
				setGraphOutputType(GraphOutputTypes.ExportFormat_ByteCode);
			}
			else if (arg.startsWith("-XML:")) {
				setGraphConfiguration("X"+arg);
				setGraphOutputType(GraphOutputTypes.ExportFormat_GraphXML_XML_Based);
			}
			else if (arg.startsWith("-ML:")) {
				setGraphConfiguration("M"+arg);
				setGraphOutputType(GraphOutputTypes.ExportFormat_GraphML_XML_Based);
			}
			else if (arg.startsWith("-DOT:")) {
				setGraphConfiguration("D"+arg);
				setGraphOutputType(GraphOutputTypes.ExportFormat_DOT_Graph_Language);
			}
			else if (arg.startsWith("-NODES:")) {
				setGraphConfiguration("N"+arg);
				setGraphOutputType(GraphOutputTypes.ExportFormat_PrintNodes);
			}
			else if (arg.startsWith("-BYTEGRAPH:")) {
				setGraphConfiguration(arg);
				setGraphOutputType(GraphOutputTypes.ExportFormat_ByteCodeGraph);
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
				methodSignature = argSplitted[1];
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
		
		if((byteCodeConfiguration == null && exportGraphConfiguration == null) || getInputStream() == null) {
			if (getInputStream() == null) {
				System.out.println("ERROR: class not found!!! ClassName: " + this.className);
			} else {
				System.out.println("ERROR while parsing arguments");
			}
			showHelpAndExit();
		}
		
		
	}
	
	/**
	 * This method sets the properties for the graph
	 * @param _arg
	 */
	private void setGraphConfiguration(String _arg) {
		String[] argSplitted = _arg.split("X");
		exportGraphConfiguration = new GraphConfiguration(argSplitted[1]);
		
		exportGraphConfiguration.setOutputType(graphOutputType);
	}
	/**
	 * This method returns the @link {@link InputStream} if the class is in a jar file
	 * @param pathToJarFile path to jar file
	 * @param packageName package name
	 * @param className class name
	 * @return InputStream
	 * @throws IOException
	 */
	private static InputStream getcontentStreamFromJarFile(String pathToJarFile, String packageName, String className) throws IOException {
		return JavaLangUtils.findResource(new String[] {pathToJarFile}, packageName, className);
	}

	/**
	 * This method returns the  @link {@link InputStream} if the class file is given in the arguments
	 * @param pathToFile path to class file
	 * @return InputStream
	 * @throws FileNotFoundException
	 */
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
	
	/**
	 * This method prints an usage for the parser, if while parsing an error appers or one of the arguments contains a 
	 * question mark.
	 * @throws IOException
	 */
	private static void showHelpAndExit() throws IOException {
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
		exportGraphConfiguration.setOutputType(gc);
	}
	public GraphConfiguration getExportGraphConfiguration() {
		return exportGraphConfiguration;
	}
	public void setExportGraphConfiguration(GraphConfiguration exportGraphConfiguration) {
		this.exportGraphConfiguration = exportGraphConfiguration;
	}
	
}
