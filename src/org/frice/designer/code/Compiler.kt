package org.frice.designer.code


import java.net.URI
import java.util.*
import java.util.regex.Pattern
import javax.tools.JavaFileObject.Kind
import javax.tools.SimpleJavaFileObject
import javax.tools.ToolProvider

/**
 * Java字节码的操纵之动态编译

 * @author WangYanCheng
 * *
 * @version 2011-2-17
 */
class AutoCompiler
/**
 * Constructor
 *
 * @param source  源代码
 * @param outPath 动态编译文件的输出路径
 */
constructor(source: String, outPath: String) {
	/** 源代码 */
	var source = ""
		set(value) {
			val tmpName = analyseClassName(value)
			this.className = tmpName.trim { i -> i <= ' ' }
			field = value
		}
	/** 类名 */
	var className = ""

	/** 编译输出路径 */
	var outPath = "."

	/** 提取包名称 */

	private val packPattern = Pattern.compile("^package//s+([a-z0-9.]+);")

	/** 提取类名称 */
	private val classNamePattern = Pattern.compile("class//s+([^{]+)")

	init {
		this.outPath = outPath
		this.source = source
	}

	/**
	 * 编译
	 * @return 编译结果 true/false
	 */
	private fun doCompile() = InnerCompiler(URI(className), Kind.SOURCE, this.source).compile()

	/**
	 * 调用
	 * @return result 调用结果
	 */
	fun doInvoke(methodName: String): Any = InnerCompiler::class.java.classLoader.loadClass(className)
			.getMethod(methodName, *arrayOf<Class<*>>())
			.invoke(null, *arrayOf<Any>())

	/**
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
	private fun analyseClassName(source: String): String {
		var tmpName = ""
		var matcher = packPattern.matcher(source)
		if (matcher.find()) tmpName = matcher.group(1) + "."
		matcher = classNamePattern.matcher(source)
		if (matcher.find()) tmpName += matcher.group(1)
		return tmpName
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	override fun toString() = "AutoCompiler [className=$className, source=$source]"

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
	private constructor(uri: URI, kind: Kind) : SimpleJavaFileObject(uri, kind) {

		private var content = ""

		/**
		 * @param uri     uri
		 * @param kind    kind
		 * @param content content
		 */
		constructor(uri: URI, kind: Kind, content: String) : this(uri, kind) {
			this.content = content
		}

		/*
		 * (non-Javadoc)
		 * @see javax.tools.SimpleJavaFileObject#getCharContent(boolean)
		 */
		override fun getCharContent(ignoreEncodingErrors: Boolean) = content

		/**
		 * 编译
		 * @return result 成功编译标记{true|false}
		 */
		fun compile(): Boolean = ToolProvider.getSystemJavaCompiler()
				.getTask(null, ToolProvider.getSystemJavaCompiler()
						.getStandardFileManager(null, null, null),
						null,
						Arrays.asList("-d", outPath),
						null,
						Arrays.asList(this)
				).call()
	}

//	companion object {
//		@JvmStatic fun main(args: Array<String>) {
//			val methodName = "compilerTest"
//			val dctInst = AutoCompiler(
//					"package org.ybygjy.basic.jvm; class TestCompiler {public static String compilerTest(){return \"HelloWorld\";}}", "./webRoot/WEB-INF/classes"
//			)
//			println(dctInst.autoInvoke(methodName))
//			// if (dctInst.doCompile()) {
//			// Object obj = dctInst.doInvoke(methodName);
//			// System.out.println(obj);
//			// }
//		}
//	}
}