package org.frice.designer.code.compile


import java.net.URI
import java.util.*
import java.util.regex.Pattern
import javax.tools.JavaFileObject.Kind
import javax.tools.SimpleJavaFileObject
import javax.tools.ToolProvider

/**
 * P.S. 所有注释除了P.S.开头的都是原作者写的，剩下的是我写的
 *
 *
 * Java字节码的操纵之动态编译

 * @author WangYanCheng
 * @author ice1000
 * @since 2011-2-17
 * @version 2016-9-23
 */
class FriceCompiler
/**
 * Constructor
 *
 * @param source_  源代码
 * @param outPath_ 动态编译文件的输出路径
 */
(source_: String, outPath_: String) {
	/** 源代码 */
	var source = source_
		private set(value) {
			field = value
			this.className = analyseClassName(value).trim { i -> i <= ' ' }
		}

	/** 类名 */
	var className = ""
		private set

	/** 编译输出路径 */
	val outPath = outPath_

	/** 提取包名称 */
	private val packagePattern = Pattern.compile("^package//s+([a-z0-9.]+);")

	/** 提取类名称 */
	private val classNamePattern = Pattern.compile("class//s+([^{]+)")

	/**
	 * 编译
	 * @return 编译结果 true/false
	 */
	private fun doCompile() = InnerCompiler(URI("$className"), Kind.SOURCE, this.source).compile()

	/**
	 * 调用
	 * @return result 调用结果
	 */
	fun doInvoke(methodName: String): Any = InnerCompiler::class.java.classLoader.loadClass(className)
			.getMethod(methodName, *arrayOf<Class<*>>())
			.invoke(null, *arrayOf<Any>())

	/**
	 * P.S.我没看懂这是干嘛的 ——ice1000
	 *
	 * 自动调用
	 * @return resultObj 调用结果
	 */
	fun autoInvoke(methodName: String): Any {
		val result = Any()
		if (this.doCompile()) return doInvoke(methodName)
		return result
	}

	/**
	 * 解析类名称
	 * @return className 类名称/空字符串
	 */
	fun analyseClassName(source: String): String {
		var tmpName = ""
		var matcher = packagePattern.matcher(source)
		if (matcher.find()) tmpName = matcher.group(1) + "."
		matcher = classNamePattern.matcher(source)
		if (matcher.find()) tmpName += matcher.group(1)
		return tmpName
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	override fun toString() = "FriceCompiler [className=$className, source=$source]"

	/**
	 * 负责自动编译
	 * @author WangYanCheng
	 * @version 2011-2-17
	 */
	private open inner class InnerCompiler
	/**
	 * @param uri  编译源文件路径
	 * @param kind 文件类型
	 */
	internal constructor(uri: URI, kind: Kind, contents: String) : SimpleJavaFileObject(uri, kind) {
		private val content = contents

		internal constructor(uri: URI, kind: Kind) : this(uri, kind, "")

		/*
		 * (non-Javadoc)
		 * @see javax.tools.SimpleJavaFileObject#getCharContent(boolean)
		 */
		override fun getCharContent(ignoreEncodingErrors: Boolean) = content

		/**
		 * 编译
		 * @return result 成功编译标记{true|false}
		 */
		internal fun compile(): Boolean = ToolProvider.getSystemJavaCompiler()
				.getTask(null, ToolProvider.getSystemJavaCompiler()
						.getStandardFileManager(null, null, null),
						null,
						Arrays.asList("-d", outPath),
						null,
						Arrays.asList(this)
				).call()
	}

	companion object {
//		const private val url = "file:compile"
//		private var path = "compile"
//		infix fun compile(code: String) {
//			val f = File(path)
//			unless(f.exists()) {
//				f.mkdir()
//			}
//			FriceCompiler(code, f.absolutePath).doCompile()
//			executeCompiled()
//		}
//
//		fun executeCompiled() {
////			URLClassLoader(Array(1, { URL(path) })).loadClass("ThisGame").newInstance()
//			Runtime.getRuntime().exec("java ${path}${File.separator}ThisGame.class")
//		}

		infix fun compile(src: String) {
			val engine = CompileEngine.getInstance()
			engine.javaCodeToObject("ThisGame", src)
		}
	}
}
