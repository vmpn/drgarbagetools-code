package com.drgarbage.commandlinetool;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import com.drgarbage.asm.ClassReader;
import com.drgarbage.asm.render.impl.ClassFileDocument;
import com.drgarbage.asm.render.impl.ClassFileOutlineElement;

public class CommandLineTool {
	public static void main(String[] args) throws FileNotFoundException{
		DataInputStream in = null;
		
		File f = new File("//Users//cihanaydin//Documents//WISE1415//Projekt-neustesvomneusten//bin//CatConnectSameBanknotes.class");
		InputStream contentStream = new FileInputStream(f);
		
		try {
			/* buffer only if necessary */
			if (contentStream instanceof BufferedInputStream) {
				in = new DataInputStream(contentStream);
			}
			else {
				in = new DataInputStream(new BufferedInputStream(contentStream));
			}
			ClassFileOutlineElement outlineElement = new ClassFileOutlineElement();
	        ClassFileDocument doc = new ClassFileDocument(outlineElement);
	        outlineElement.setClassFileDocument(doc);
	        ClassReader cr = new ClassReader(in, doc);
	        cr.accept(doc, 0);
	        System.out.println(doc.toString());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
