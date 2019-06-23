package com.samisari.common.util.compiler;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

public class CompilerJava {
	void compile(File[] sources ) throws IOException {
		JavaCompiler compiler=ToolProvider.getSystemJavaCompiler();
		StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
		Iterable<? extends JavaFileObject> compilationUnits1 =
		           fileManager.getJavaFileObjectsFromFiles(Arrays.asList(sources));
		compiler.getTask(null, fileManager, null, null, null, compilationUnits1).call();
		fileManager.close();
	}
}
