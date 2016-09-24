package org.frice.designer.code.compile;

import javax.tools.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

public class CompileEngine {
	public static void main(String[] args) throws Exception {
//		DynamicEngine de = DynamicEngine.getInstance();
//		Object instance = de.javaCodeToObject(fullName, src.toString());
//		System.out.println(instance);
	}
}

class DynamicEngine {
	private static DynamicEngine ourInstance = new DynamicEngine();

	static DynamicEngine getInstance() {
		return ourInstance;
	}

	private URLClassLoader parentClassLoader;
	private String classpath;

	private DynamicEngine() {
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

	public Object javaCodeToObject(String fullClassName, String javaCode) throws IllegalAccessException, InstantiationException {
		long start = System.currentTimeMillis();
		Object instance = null;
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
		ClassFileManager fileManager = new ClassFileManager(compiler.getStandardFileManager(diagnostics, null, null));

		List<JavaFileObject> jfiles = new ArrayList<JavaFileObject>();
		jfiles.add(new CharSequenceJavaFileObject(fullClassName, javaCode));

		List<String> options = new ArrayList<String>();
		options.add("-encoding");
		options.add("UTF-8");
		options.add("-classpath");
		options.add(this.classpath);

		JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnostics, options, null, jfiles);
		boolean success = task.call();

		if (success) {
			JavaClassObject jco = fileManager.getJavaClassObject();
			DynamicClassLoader dynamicClassLoader = new DynamicClassLoader(this.parentClassLoader);
			Class<?> clazz = dynamicClassLoader.loadClass(fullClassName, jco);
			instance = clazz.newInstance();
		} else {
			String error = "";
			for (Diagnostic diagnostic : diagnostics.getDiagnostics()) {
				error = error + compilePrint(diagnostic);
			}
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
		StringBuffer res = new StringBuffer();
		res.append("Code:[").append(diagnostic.getCode()).append("]\n");
		res.append("Kind:[").append(diagnostic.getKind()).append("]\n");
		res.append("Position:[").append(diagnostic.getPosition()).append("]\n");
		res.append("Start Position:[").append(diagnostic.getStartPosition()).append("]\n");
		res.append("End Position:[").append(diagnostic.getEndPosition()).append("]\n");
		res.append("Source:[").append(diagnostic.getSource()).append("]\n");
		res.append("Message:[").append(diagnostic.getMessage(null)).append("]\n");
		res.append("LineNumber:[").append(diagnostic.getLineNumber()).append("]\n");
		res.append("ColumnNumber:[").append(diagnostic.getColumnNumber()).append("]\n");
		return res.toString();
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
	public CharSequence getCharContent(
			boolean ignoreEncodingErrors) {
		return content;
	}
}

class ClassFileManager extends
		ForwardingJavaFileManager {
	JavaClassObject getJavaClassObject() {
		return jclassObject;
	}

	private JavaClassObject jclassObject;

	ClassFileManager(StandardJavaFileManager
			                 standardManager) {
		super(standardManager);
	}


	@Override
	public JavaFileObject getJavaFileForOutput(
			Location location, String className, JavaFileObject.Kind kind, FileObject sibling)
			throws IOException {
		jclassObject = new JavaClassObject(className, kind);
		return jclassObject;
	}
}
class JavaClassObject extends SimpleJavaFileObject {

	protected final ByteArrayOutputStream bos =
			new ByteArrayOutputStream();


	public JavaClassObject(String name, JavaFileObject.Kind kind) {
		super(URI.create("string:///" + name.replace('.', '/')
				+ kind.extension), kind);
	}


	public byte[] getBytes() {
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
