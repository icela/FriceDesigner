package org.frice.designer.code.compile

import javax.tools.*
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.OutputStream
import java.net.URI
import java.net.URL
import java.net.URLClassLoader
import java.io.File
import java.util.ArrayList

class CompileEngine private constructor() {

	private val parentClassLoader: URLClassLoader
	private var classpath: String? = null

	init {
		this.parentClassLoader = this.javaClass.classLoader as URLClassLoader
		this.buildClassPath()
	}

	private fun buildClassPath() {
		this.classpath = null
		val sb = StringBuilder()
		for (url in this.parentClassLoader.urLs) {
			val p = url.file
			sb.append(p).append(File.pathSeparator)
		}
		this.classpath = sb.toString()
	}

	fun javaCodeToObject(fullClassName: String, javaCode: String): Any {
		val start = System.currentTimeMillis()
		var instance: Any? = null
		val compiler = ToolProvider.getSystemJavaCompiler()
		val diagnostics = DiagnosticCollector<JavaFileObject>()
		val fileManager = ClassFileManager(compiler.getStandardFileManager(diagnostics, null, null))

		val jFiles = ArrayList<JavaFileObject>()
		jFiles.add(CharSequenceJavaFileObject(fullClassName, javaCode))

		val options = ArrayList<String>()
		options.add("-encoding")
		options.add("UTF-8")
		options.add("-classpath")
		options.add(this.classpath)

		val task = compiler.getTask(null, fileManager, diagnostics, options, null, jFiles)

		if (task.call()!!) {
			val jco = fileManager.javaClassObject
			val dynamicClassLoader = DynamicClassLoader(this.parentClassLoader)
			val clazz = dynamicClassLoader.loadClass(fullClassName, jco)
			instance = clazz.newInstance()
		} else {
			var error = ""
			for (diagnostic in diagnostics.diagnostics) error += compilePrint(diagnostic)
			System.err.println(error)
		}
		val end = System.currentTimeMillis()
		println("javaCodeToObject use:" + (end - start) + "ms")
		return instance
	}

	private fun compilePrint(diagnostic: Diagnostic<*>): String {
		println("Code:" + diagnostic.code)
		println("Kind:" + diagnostic.kind)
		println("Position:" + diagnostic.position)
		println("Start Position:" + diagnostic.startPosition)
		println("End Position:" + diagnostic.endPosition)
		println("Source:" + diagnostic.source)
		println("Message:" + diagnostic.getMessage(null))
		println("LineNumber:" + diagnostic.lineNumber)
		println("ColumnNumber:" + diagnostic.columnNumber)
		return "Code:[" + diagnostic.code + "]\n" +
				"Kind:[" + diagnostic.kind + "]\n" +
				"Position:[" + diagnostic.position + "]\n" +
				"Start Position:[" + diagnostic.startPosition + "]\n" +
				"End Position:[" + diagnostic.endPosition + "]\n" +
				"Source:[" + diagnostic.source + "]\n" +
				"Message:[" + diagnostic.getMessage(null) + "]\n" +
				"LineNumber:[" + diagnostic.lineNumber + "]\n" +
				"ColumnNumber:[" + diagnostic.columnNumber + "]\n"
	}

	companion object {
		internal val instance = CompileEngine()
	}
}


internal class CharSequenceJavaFileObject(className: String, private val content: CharSequence) :
		SimpleJavaFileObject(URI.create("string:///" + className.replace('.', '/')
				+ JavaFileObject.Kind.SOURCE.extension), JavaFileObject.Kind.SOURCE) {
	override fun getCharContent(ignoreEncodingErrors: Boolean): CharSequence = content
}

internal class ClassFileManager(standardManager: StandardJavaFileManager) : ForwardingJavaFileManager<*>(standardManager) {

	var javaClassObject: JavaClassObject? = null
		private set

	override fun getJavaFileForOutput(
			location: JavaFileManager.Location,
			className: String,
			kind: JavaFileObject.Kind,
			sibling: FileObject): JavaFileObject {
		javaClassObject = JavaClassObject(className, kind)
		return javaClassObject
	}
}

internal class JavaClassObject(name: String, kind: JavaFileObject.Kind) :
		SimpleJavaFileObject(URI.create("string:///" + name.replace('.', '/') + kind.extension), kind) {

	private val bos = ByteArrayOutputStream()

	val bytes: ByteArray
		get() = bos.toByteArray()

	override fun openOutputStream(): OutputStream {
		return bos
	}
}

internal class DynamicClassLoader(parent: ClassLoader) : URLClassLoader(arrayOfNulls<URL>(0), parent) {

	@Throws(ClassNotFoundException::class)
	fun findClassByClassName(className: String): Class<*> {
		return this.findClass(className)
	}

	fun loadClass(fullName: String, jco: JavaClassObject): Class<*> {
		val classData = jco.bytes
		return this.defineClass(fullName, classData, 0, classData.size)
	}
}
