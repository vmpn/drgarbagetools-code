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

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import com.drgarbage.asm.ClassReader;
import com.drgarbage.asm.render.impl.ClassFileDocument;
import com.drgarbage.asm.render.impl.ClassFileOutlineElement;
import com.drgarbage.javalang.JavaLangUtils;

/**
 * The configuration space of a class file editor.
 * 
 * @author Cihan Aydin
 * @version $Revision$
 * $Id$
 */
public class CommandLineTool {
	
	public static void main(String[] args) throws IOException{	
		PartsOfClassFiles pocf = new PartsOfClassFiles();

		String packageName = null;
		String className = null;
		String pathToFile = null;
		
		for (String arg : args) {
			if (arg.startsWith("-")){
				if (arg.contains("?"))
					showHelpAndExit();
				if (arg.contains("c"))
					pocf.setShowConstantPool(true);
				if (arg.contains("e"))
					pocf.setShowExceptionTable(true);
				if (arg.contains("l"))
					pocf.setShowLineNumberTable(true);
				if (arg.contains("v"))
					pocf.setShowLocalVariableTable(true);
				if (arg.contains("m"))
					pocf.setShowMaxs(true);
				if (arg.contains("r"))
					pocf.setShowRelativeBranchTargetOffsets(true);
				if (arg.contains("s"))
					pocf.setShowSourceLineNumbers(true);
			}
			else if (arg.endsWith(".jar"))
				pathToFile = arg;
			else if (arg.endsWith(".class"))
				pathToFile = arg;
			else if (arg.contains(".") && !arg.endsWith(".jar"))
				packageName = arg;
			else
				className = arg;
		}
		InputStream is;
		if (pathToFile.endsWith(".class"))
			is = getContentStreamWithClassFile(pathToFile);
		else
			is = getcontentStreamWithJarFile(pathToFile, packageName, className);
		
		startBCVCommandLine(is, pocf);
		
	}

	private static InputStream getcontentStreamWithJarFile(String pathToJarFile, String packageName, String className) throws IOException {
		return JavaLangUtils.findResource(new String[] {pathToJarFile}, packageName, className);
	}

	private static InputStream getContentStreamWithClassFile(String pathToJarFile) throws FileNotFoundException {
		File f = new File(pathToJarFile);
		return new FileInputStream(f);
	}

	private static void startBCVCommandLine(InputStream contentStream, PartsOfClassFiles pocf) {
		
		DataInputStream in = null;
		
		try {
			/* buffer only if necessary */
			if (contentStream instanceof BufferedInputStream) {
				in = new DataInputStream(contentStream);
			}
			else {
				in = new DataInputStream(new BufferedInputStream(contentStream));
			}
			ClassFileOutlineElement outlineElement = new ClassFileOutlineElement();
	        ClassFileDocument doc = new ClassFileDocument(outlineElement, pocf);
	        
	        outlineElement.setClassFileDocument(doc);
	        ClassReader cr = new ClassReader(in, doc);
	        cr.accept(doc, 0);
	        System.out.println(doc.toString());

		} catch (Exception e) {
			System.err.println("error while using");
			e.printStackTrace();
		}		
	}

	private static void showHelpAndExit() {
		System.out.println("usage.........");
		System.exit(0);
	}
}
