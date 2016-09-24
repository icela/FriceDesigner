package org.frice.designer.code.compile

import org.frice.game.utils.message.log.FLog
import javax.tools.*
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.OutputStream
import java.net.URI
import java.net.URL
import java.net.URLClassLoader
import java.io.File
import java.util.ArrayList

object CompileEngine private constructor() {

	private val parentClassLoader: URLClassLoader
	private var classpath: String? = null

	init {
		parentClassLoader = this.javaClass.classLoader as URLClassLoader
		buildClassPath()
	}

	private fun buildClassPath() {
		classpath = null
		val sb = StringBuilder()
		for (url in this.parentClassLoader.urLs) {
			val p = url.file
			sb.append(p).append(File.pathSeparator)
		}
		this.classpath = sb.toString()
	}

	fun javaCodeToObject(fullClassName: String, javaCode: String): Any? {
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
		options.add(classpath)

		if (compiler.getTask(null, fileManager, diagnostics, options, null, jFiles).call()!!) {
			val jco = fileManager.javaClassObject
			val dynamicClassLoader = DynamicClassLoader(parentClassLoader)
			val clazz = dynamicClassLoader.loadClass(fullClassName, jco)
			instance = clazz.newInstance()
		} else {
			var error = ""
			for (diagnostic in diagnostics.diagnostics) error += compilePrint(diagnostic)
			FLog.e(error)
		}
		println("javaCodeToObject use:" + (System.currentTimeMillis() - start) + "ms")
		return instance
	}

	private fun compilePrint(diagnostic: Diagnostic<*>): String {
		FLog.i("Code:" + diagnostic.code)
		FLog.i("Kind:" + diagnostic.kind)
		FLog.i("Position:" + diagnostic.position)
		FLog.i("Start Position:" + diagnostic.startPosition)
		FLog.i("End Position:" + diagnostic.endPosition)
		FLog.i("Source:" + diagnostic.source)
		FLog.i("Message:" + diagnostic.getMessage(null))
		FLog.i("LineNumber:" + diagnostic.lineNumber)
		FLog.i("ColumnNumber:" + diagnostic.columnNumber)
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
}

internal class CharSequenceJavaFileObject(className: String, val content: CharSequence) :
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

	override fun openOutputStream(): OutputStream = bos
}

internal class DynamicClassLoader(parent: ClassLoader) : URLClassLoader(arrayOfNulls<URL>(0), parent) {

	fun findClassByClassName(className: String): Class<*> = this.findClass(className)

	fun loadClass(fullName: String, jco: JavaClassObject): Class<*> {
		val classData = jco.bytes
		return this.defineClass(fullName, classData, 0, classData.size)
	}
}
