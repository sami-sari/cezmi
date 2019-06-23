package com.samisari.java2d.textwriter;

import com.samisari.java2d.textwriter.commands.Indent;

public interface JavaCodeWriter {
	
	public String getJavaImportDeclaration(Indent indent) throws Exception;
	public String getJavaFieldDeclaration(Indent indent) throws Exception;
	public String getJavaBuildMethodStatements(Indent indent) throws Exception;
	//public String getJavaListenerStatements();
	
}
