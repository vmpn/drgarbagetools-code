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

import com.drgarbage.commandlinetool.intf.GraphOutputTypes;
import com.drgarbage.commandlinetool.intf.IArgumentParser;
import com.drgarbage.commandlinetool.intf.IByteCodeConfiguration;
import com.drgarbage.commandlinetool.intf.IGraphConfiguration;
import com.drgarbage.javalang.JavaLangUtils;
/**
 * This class represents an argument parser for CommandLineTool
 * 
 * @author Cihan Aydin
 * @version $Revision: 723 $
 * $Id: ParseArguments.java 723 2015-05-12 08:40:35Z cihanaydin $
 */
public class ArgumentParser implements IArgumentParser {
	
	private ByteCodeConfiguration byteCodeConfiguration;
	private GraphOutputTypes graphOutputType;
	private GraphConfiguration exportGraphConfiguration;
	
	/**
	 * Constructor
	 * @param args the program arguments
	 * @throws IOException
	 */
	public ArgumentParser(String[] args) throws IOException {
		parse(args);
	}
	
	/**
	 * This method parses the string array and sets the options
	 * @param args String array with specified configurations 
	 * @throws IOException
	 */
	private void parse(String[] args) throws IOException {
		
		for (String arg : args) {
			if (arg.contains("?") || arg.contains("help")){
				showHelpAndExit();
			}
			else if (arg.startsWith("-BC:")){
				String[] argSplitted = arg.split(":");
				parseByteCodeConfiguration(argSplitted[1]);
			}
			else if (arg.startsWith("-XML:")) {
				setGraphConfiguration(arg);
			}
			else if (arg.startsWith("-ML:")) {
				setGraphConfiguration(arg);
			}
			else if (arg.startsWith("-DOT:")) {
				setGraphConfiguration(arg);
			}
			else if (arg.startsWith("-NODES:")) {
				setGraphConfiguration(arg);
			}
			else if (arg.startsWith("-BYTEGRAPH:")) {
				setGraphConfiguration(arg);
			}
		}
		
		if(byteCodeConfiguration == null && exportGraphConfiguration == null) {
			
			System.out.println("ERROR while parsing arguments");
			showHelpAndExit();
		}
		
		
	}

	/**
	 * This method parses the options for the byte code configuration object.
	 * @param arg String with specified configurations 
	 */
	private void parseByteCodeConfiguration(String arg) {
		byteCodeConfiguration = new ByteCodeConfiguration();
		if (arg != null) {
			for (Character c : arg.toCharArray()) {
				switch(c) {
				case 'c':
					byteCodeConfiguration.setShowConstantPool(true);
					break;
				case 'e':
					byteCodeConfiguration.setShowExceptionTable(true);
					break;
				case 'l':
					byteCodeConfiguration.setShowLineNumberTable(true);
					break;
				case 'v':
					byteCodeConfiguration.setShowLocalVariableTable(true);
					break;
				case 'm':
					byteCodeConfiguration.setShowMaxs(true);
					break;
				case 'r':
					byteCodeConfiguration.setShowRelativeBranchTargetOffsets(true);
					break;
				case 's':
					byteCodeConfiguration.setShowSourceLineNumbers(true);
					break;
				default:
					break;
				}
			}
		}
	}

	/**
	 * This method sets the properties for the graph.
	 * @param arg String with specified configurations 
	 */
	private void setGraphConfiguration(String _arg) {
		exportGraphConfiguration = new GraphConfiguration();
		String[] argSplitted = _arg.split(":");
		parseGraphConfiguration(argSplitted[1]);
		
		exportGraphConfiguration.setOutputType(graphOutputType);
	}
	
	/**
	 * This method sets the properties for the graph configuration.
	 * @param arg String array with specified configurations 
	 */
	private void parseGraphConfiguration(String arg) {

		if (arg == null || arg.equals("")) {
			exportGraphConfiguration.setOutputType(GraphOutputTypes.ExportFormat_PrintNodes);
		} else {
			for (Character c : arg.toCharArray()) {
				switch(c) {
					case 'c':
						exportGraphConfiguration.setExportComments(true);
						break;
					case 'd':
						exportGraphConfiguration.setExportDecorations(true);
						break;
					case 'g':
						exportGraphConfiguration.setExportGeometry(true);
						break;
					case 'm':
						exportGraphConfiguration.setSuppressMessages(true);
						break;
					case 's':
						exportGraphConfiguration.setStartVertex(true);
						break;
					case 'e':
						exportGraphConfiguration.setEndVertex(true);
						break;
					case 'b':
						exportGraphConfiguration.setBackEdge(true);
						break;
					case 'D':
						exportGraphConfiguration.setOutputType(GraphOutputTypes.ExportFormat_DOT_Graph_Language);
						break;
					case 'X':
						exportGraphConfiguration.setOutputType(GraphOutputTypes.ExportFormat_GraphXML_XML_Based);
						break;
					case 'M':
						exportGraphConfiguration.setOutputType(GraphOutputTypes.ExportFormat_GraphML_XML_Based);
						break;
					case 'N':
						exportGraphConfiguration.setOutputType(GraphOutputTypes.ExportFormat_PrintNodes);
						break;
					default:
						break;
				}
			}
		}		
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

	/* (non-Javadoc)
	 * @see com.drgarbage.commandlinetool.intf.IArgumentParser#argumentParser(java.lang.String[])
	 */
	public void argumentParser(String[] args) {
		try {
			parse(args);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see com.drgarbage.commandlinetool.intf.IArgumentParser#getGraphConfiguration()
	 */
	public IGraphConfiguration getGraphConfiguration() {
		return this.exportGraphConfiguration;
	}
	
	/* (non-Javadoc)
	 * @see com.drgarbage.commandlinetool.intf.IArgumentParser#getByteCodeConfiguration()
	 */
	public IByteCodeConfiguration getByteCodeConfiguration() {
		return byteCodeConfiguration;
	}
}
