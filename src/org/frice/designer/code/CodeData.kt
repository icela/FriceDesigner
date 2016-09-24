package org.frice.designer.code

import org.frice.designer.controller.Controller
import java.awt.Color
import java.util.*

/**
 * Created by ice1000 on 2016/9/17.
 *
 * @author ice1000
 */
class CodeData() {
	var objectList = LinkedList<AnObject>()
	var objectChosen: AnObject? = null

	fun getCode(language: Int): String = when (language) {
		LANGUAGE_JAVA -> String.format(javaCode, buildFieldCodes(language), buildInitCodes(language))
		LANGUAGE_KOTLIN -> String.format(kotlinCode, buildFieldCodes(language), buildInitCodes(language))
		LANGUAGE_SCALA -> String.format(scalaCode, buildFieldCodes(language), buildInitCodes(language))
		LANGUAGE_GROOVY -> String.format(groovyCode, buildFieldCodes(language), buildInitCodes(language))
		else -> throw UnknownLanguageException()
	}

	private fun buildInitCodes(language: Int): String {
		val sb = StringBuffer()
		objectList.forEach { o ->
			when (language) {
				LANGUAGE_JAVA -> {
					when (o) {
						is AnOval ->
							sb.append("\t\t${o.fieldName} = new ${typeOf(o)}(new org.frice.game.resource.graphics.ColorResource(${o.color.rgb}), ",
									"new ${oval(o)}, ${o.x}, ${o.y});\n")
						is AnRectangle ->
							sb.append("\t\t${o.fieldName} = new ${typeOf(o)}(new org.frice.game.resource.graphics.ColorResource(${o.color.rgb}), ",
									"new ${rect(o)}, ${o.x}, ${o.y});\n")
						is AnText -> {
							sb.append("\t\t${o.fieldName} = new ${typeOf(o)}(\"${o.text}\", ${o.x}, ${o.y});\n")
							sb.append(buildColorCode(o.fieldName, o.color, language))
						}
						is AnButton -> {
							sb.append("\t\t${o.fieldName} = new ${typeOf(o)}(\"${o.text}\", ${o.x}, ${o.y}, ",
									"${o.width}, ${o.height});\n")
							sb.append(buildColorCode(o.fieldName, o.color, language))
						}
					}
					sb.append("\t\tsuper.addObject(${o.fieldName});\n")
				}
				LANGUAGE_SCALA, LANGUAGE_GROOVY -> {
					when (o) {
						is AnOval ->
							sb.append("\t\t${o.fieldName} = new ${typeOf(o)}(new org.frice.game.resource.graphics.ColorResource(${o.color.rgb}), ",
									"new ${oval(o)}, ${o.x}, ${o.y})\n")
						is AnRectangle ->
							sb.append("\t\t${o.fieldName} = new ${typeOf(o)}(new org.frice.game.resource.graphics.ColorResource(${o.color.rgb}), ",
									"new ${rect(o)}, ${o.x}, ${o.y})\n")
						is AnText -> {
							sb.append("\t\t${o.fieldName} = new ${typeOf(o)}(\"${o.text}\", ${o.x}, ${o.y})\n")
							sb.append(buildColorCode(o.fieldName, o.color, language))
						}
						is AnButton -> {
							sb.append("\t\t${o.fieldName} = new ${typeOf(o)}(\"${o.text}\", ${o.x}, ${o.y}, ",
									"${o.width}, ${o.height})\n")
							sb.append(buildColorCode(o.fieldName, o.color, language))
						}
					}
					sb.append("\t\tsuper.addObject(${o.fieldName})\n")
				}
				LANGUAGE_KOTLIN -> {
					when (o) {
						is AnOval ->
							sb.append("\t\t${o.fieldName} = ${typeOf(o)}(org.frice.game.resource.graphics.ColorResource(${o.color.rgb}), ",
									"${oval(o)}, ${o.x}, ${o.y})\n")
						is AnRectangle ->
							sb.append("\t\t${o.fieldName} = ${typeOf(o)}(org.frice.game.resource.graphics.ColorResource(${o.color.rgb}), ",
									"${rect(o)}, ${o.x}, ${o.y})\n")
						is AnText -> {
							sb.append("\t\t${o.fieldName} = ${typeOf(o)}(\"${o.text}\", ${o.x}, ${o.y})\n")
							sb.append(buildColorCode(o.fieldName, o.color, language))
						}
						is AnButton -> {
							sb.append("\t\t${o.fieldName} = ${typeOf(o)}(\"${o.text}\", ${o.x}, ${o.y}, ",
									"${o.width}, ${o.height})\n")
							sb.append(buildColorCode(o.fieldName, o.color, language))
						}
					}
					sb.append("\t\tsuper<org.frice.game.Game>.addObject(${o.fieldName})\n")
				}
			}
		}
		return sb.toString()
	}

	private fun buildColorCode(fieldName: String, color: Color, language: Int) = when (language) {
		LANGUAGE_KOTLIN -> "\t\t$fieldName.colorResource = org.frice.game.resource.graphics.ColorResource(${color.rgb})\n"
		LANGUAGE_SCALA, LANGUAGE_GROOVY -> "\t\t$fieldName.setColorResource(new org.frice.game.resource.graphics.ColorResource(${color.rgb}))\n"
		LANGUAGE_JAVA -> "\t\t$fieldName.setColorResource(new org.frice.game.resource.graphics.ColorResource(${color.rgb}));\n"
		else -> throw UnknownLanguageException()
	}

	private fun buildFieldCodes(language: Int): String {
		val sb = StringBuffer()
		objectList.forEach { o ->
			when (language) {
				LANGUAGE_SCALA -> sb.append("\tval ${o.fieldName}: ${typeOf(o)} = _\n")
				LANGUAGE_JAVA -> sb.append("\tpublic ${typeOf(o)} ${o.fieldName};\n")
				LANGUAGE_KOTLIN -> sb.append("\tlateinit var ${o.fieldName}: ${typeOf(o)}\n")
				LANGUAGE_GROOVY -> sb.append("\tpublic def ${o.fieldName}\n")
			}
		}
		return sb.toString()
	}

	/**
	 * already put full reference to the returned string.
	 * @return the full reference of the class name
	 */
	private fun typeOf(obj: AnObject) = when (obj) {
		is AnOval, is AnRectangle -> Controller.shapeObject_
		is AnText -> Controller.simpleText
		is AnButton -> Controller.simpleButton
		else -> Controller.fObject
	}

	private fun oval(obj: AnOval) =
			if (obj.width == obj.height)
				"org.frice.game.utils.graphics.shape.FCircle(${obj.width / 2.0})"
			else
				"org.frice.game.utils.graphics.shape.FOval(${obj.width / 2.0}, ${obj.height / 2.0})"

	private fun rect(obj: AnRectangle) = "org.frice.game.utils.graphics.shape.FRectangle(${obj.width.toInt()}, ${obj.height.toInt()})"

	override fun toString(): String {
		val s = StringBuffer()
		objectList.forEach { o ->
			s.append("${typeOf(o)} ${o.x.toInt()} ${o.y.toInt()} ${o.width.toInt()} ${o.height.toInt()} ",
					///     0             1             2               3                   4
					"${o.fieldName} ")
			///                5
			when (o) {
				is ColorOwner -> s.append("${o.color} ")
			///                          6
				is TextOwner -> s.append("${o.text} ")
			///                          6 or 7
				is PathOwner -> s.append("${o.path}")
			///                          6, 7 or 8
				is UrlOwner -> s.append("${o.url}")
			///                          6, 7, 8 or 9
			}
			s.append("\n")
		}
		return s.toString()
	}

	companion object {
		const val LANGUAGE_JAVA = 0x01
		const val LANGUAGE_KOTLIN = 0x02
		const val LANGUAGE_SCALA = 0x03
		const val LANGUAGE_GROOVY = 0x04

		fun fromString(source: String): CodeData {
			val data = CodeData()
			source.split("\n").forEach { s ->
				val o = s.split(" ")
				when (o[0]) {
					Controller.simpleText -> data.objectList.add(AnText(
							o[1].toDouble(),
							o[2].toDouble(),
							o[5],
							Color(o[6].toInt()),
							o[7]
					))
					Controller.shapeObjectOval -> data.objectList.add(AnOval(
							o[1].toDouble(),
							o[2].toDouble(),
							o[3].toDouble(),
							o[4].toDouble(),
							o[5],
							Color(o[6].toInt())
					))
					Controller.shapeObjectRectangle -> data.objectList.add(AnRectangle(
							o[1].toDouble(),
							o[2].toDouble(),
							o[3].toDouble(),
							o[4].toDouble(),
							o[5],
							Color(o[6].toInt())
					))
					Controller.simpleButton -> data.objectList.add(AnButton(
							o[1].toDouble(),
							o[2].toDouble(),
							o[3].toDouble(),
							o[4].toDouble(),
							o[5],
							Color(o[6].toInt()),
							o[7]
					))
				}
			}
			return data
		}

		private const val javaCode = """// Generated codes

/**
 * Frice engine class.
 * Auto-generated by Frice engine designer
 * Java
 */
class ThisGame extends org.frice.game.Game {

%s

	@Override
	protected void onInit() {
%s
	}

	public static void main(String[] args) {
		new ThisGame();
	}
}

"""

		private const val groovyCode = """// Generated codes

/**
 * Frice engine class.
 * Auto-generated by Frice engine designer
 * Groovy
 */
public class ThisGame extends org.frice.game.Game {

%s
	@Override
	protected void onInit() {
%s
	}

	public static void main(String[] args) {
		new ThisGame()
	}
}

"""

		private const val kotlinCode = """// Generated codes

/**
 * Frice engine class.
 * Auto-generated by Frice engine designer
 * Kotlin
 */
class ThisGame() : org.frice.game.Game() {

%s
	override fun onInit() {
%s
	}

	companion object {
		@JvmStatic fun main(args: Array<String>) {
			ThisGame()
		}
	}
}

"""

		private const val scalaCode = """// Generated codes

/**
 * Frice engine class.
 * Auto-generated by Frice engine designer
 * Scala
 */
class ThisGame extends org.frice.game.Game {

%s
	override def onInit(): Unit = {
%s
	}

object ThisGame {
	def main(args: Array[String]): Unit = {
		new ThisGame
	}
}
"""
	}
}
