package org.frice.designer.code.compile;

import javax.tools.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CompileEngine {
	private static CompileEngine ourInstance = new CompileEngine();

	static CompileEngine getInstance() {
		return ourInstance;
	}

	private URLClassLoader parentClassLoader;
	private String classpath;

	private CompileEngine() {
		this.parentClassLoader = (URLClassLoader) this.getClass().getClassLoader();
		this.buildClassPath();
	}

	private void buildClassPath() {
		this.classpath = null;
		StringBuilder sb = new StringBuilder();
		for (URL url : this.parentClassLoader.getURLs()) {
			String p = url.getFile();
			sb.append(p).append(File.pathSeparator);
		}
		this.classpath = sb.toString();
	}

	public Object javaCodeToObject(String fullClassName, String javaCode) throws Exception {
		long start = System.currentTimeMillis();
		Object instance = null;
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
		ClassFileManager fileManager = new ClassFileManager(compiler.getStandardFileManager(diagnostics, null, null));

		List<JavaFileObject> jFiles = new ArrayList<>();
		jFiles.add(new CharSequenceJavaFileObject(fullClassName, javaCode));

		List<String> options = new ArrayList<>();
		options.add("-encoding");
		options.add("UTF-8");
		options.add("-classpath");
		options.add(this.classpath);

		JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnostics, options, null, jFiles);

		if (task.call()) {
			JavaClassObject jco = fileManager.getJavaClassObject();
			DynamicClassLoader dynamicClassLoader = new DynamicClassLoader(this.parentClassLoader);
			Class<?> clazz = dynamicClassLoader.loadClass(fullClassName, jco);
			instance = clazz.newInstance();
		} else {
			String error = "";
			for (Diagnostic diagnostic : diagnostics.getDiagnostics()) error += compilePrint(diagnostic);
			System.err.println(error);
		}
		long end = System.currentTimeMillis();
		System.out.println("javaCodeToObject use:" + (end - start) + "ms");
		return instance;
	}

	private String compilePrint(Diagnostic diagnostic) {
		System.out.println("Code:" + diagnostic.getCode());
		System.out.println("Kind:" + diagnostic.getKind());
		System.out.println("Position:" + diagnostic.getPosition());
		System.out.println("Start Position:" + diagnostic.getStartPosition());
		System.out.println("End Position:" + diagnostic.getEndPosition());
		System.out.println("Source:" + diagnostic.getSource());
		System.out.println("Message:" + diagnostic.getMessage(null));
		System.out.println("LineNumber:" + diagnostic.getLineNumber());
		System.out.println("ColumnNumber:" + diagnostic.getColumnNumber());
		return "Code:[" + diagnostic.getCode() + "]\n" +
				"Kind:[" + diagnostic.getKind() + "]\n" +
				"Position:[" + diagnostic.getPosition() + "]\n" +
				"Start Position:[" + diagnostic.getStartPosition() + "]\n" +
				"End Position:[" + diagnostic.getEndPosition() + "]\n" +
				"Source:[" + diagnostic.getSource() + "]\n" +
				"Message:[" + diagnostic.getMessage(null) + "]\n" +
				"LineNumber:[" + diagnostic.getLineNumber() + "]\n" +
				"ColumnNumber:[" + diagnostic.getColumnNumber() + "]\n";
	}
}


class CharSequenceJavaFileObject extends SimpleJavaFileObject {
	private CharSequence content;

	CharSequenceJavaFileObject(String className,
	                           CharSequence content) {
		super(URI.create("string:///" + className.replace('.', '/')
				+ JavaFileObject.Kind.SOURCE.extension), JavaFileObject.Kind.SOURCE);
		this.content = content;
	}

	@Override
	public CharSequence getCharContent(boolean ignoreEncodingErrors) {
		return content;
	}
}

class ClassFileManager extends ForwardingJavaFileManager {
	JavaClassObject getJavaClassObject() {
		return jClassObject;
	}

	private JavaClassObject jClassObject;

	ClassFileManager(StandardJavaFileManager standardManager) {
		super(standardManager);
	}


	@Override
	public JavaFileObject getJavaFileForOutput(
			Location location, String className, JavaFileObject.Kind kind, FileObject sibling)
			throws IOException {
		jClassObject = new JavaClassObject(className, kind);
		return jClassObject;
	}
}

class JavaClassObject extends SimpleJavaFileObject {

	private final ByteArrayOutputStream bos = new ByteArrayOutputStream();

	JavaClassObject(String name, JavaFileObject.Kind kind) {
		super(URI.create("string:///" + name.replace('.', '/')
				+ kind.extension), kind);
	}

	byte[] getBytes() {
		return bos.toByteArray();
	}

	@Override
	public OutputStream openOutputStream() throws IOException {
		return bos;
	}
}

class DynamicClassLoader extends URLClassLoader {
	DynamicClassLoader(ClassLoader parent) {
		super(new URL[0], parent);
	}

	public Class findClassByClassName(String className) throws ClassNotFoundException {
		return this.findClass(className);
	}

	Class<?> loadClass(String fullName, JavaClassObject jco) {
		byte[] classData = jco.getBytes();
		return this.defineClass(fullName, classData, 0, classData.length);
	}
}
