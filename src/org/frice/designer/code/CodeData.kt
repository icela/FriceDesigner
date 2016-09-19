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
		else -> throw UnknownLanguageException()
	}

	private fun buildInitCodes(language: Int): String {
		val sb = StringBuffer()
		objectList.forEach { o ->
			when (language) {
				LANGUAGE_SCALA, LANGUAGE_JAVA -> {
					when (o) {
						is AnShapeObject ->
							sb.append("\t\t${o.fieldName} = new ${typeOf(o)}(new ColorResource(${o.color.rgb}), ",
									"new ${shapeOf(o)}, ${o.x}, ${o.y});\n")
						is AnText ->
							sb.append("\t\t${o.fieldName} = new ${typeOf(o)}(\"${o.text}\", ${o.x}, ${o.y})\n",
									"\t\t${o.fieldName}.setColorResource(new ColorResource(${o.color.rgb}))\n")
					}
					sb.append("\t\tsuper.addObject(${o.fieldName});\n")
				}
				LANGUAGE_KOTLIN -> {
					when (o) {
						is AnShapeObject ->
							sb.append("\t\t${o.fieldName} = ${typeOf(o)}(ColorResource(${o.color.rgb}), ",
									"${shapeOf(o)}, ${o.x}, ${o.y})\n")
						is AnText ->
							sb.append("\t\t${o.fieldName} = ${typeOf(o)}(\"${o.text}\", ${o.x}, ${o.y})\n",
									"\t\t${o.fieldName}.colorResource = ColorResource(${o.color.rgb})\n")
					}
					sb.append("\t\tsuper<Game>.addObject(${o.fieldName})\n")
				}
			}
		}
		return sb.toString()
	}

	private fun buildFieldCodes(language: Int): String {
		val sb = StringBuffer()
		objectList.forEach { o ->
			when (language) {
				LANGUAGE_SCALA -> sb.append("\tval ${o.fieldName}: ${typeOf(o)} = _\n")
				LANGUAGE_JAVA -> sb.append("\tpublic ${typeOf(o)} ${o.fieldName};\n")
				LANGUAGE_KOTLIN -> sb.append("\tlateinit var ${o.fieldName}: ${typeOf(o)}\n")
			}
		}
		return sb.toString()
	}

	private fun typeOf(obj: AnObject) = when (obj) {
		is AnShapeObject -> Controller.shapeObject
		is AnText -> Controller.simpleText
		else -> Controller.fObject
	}

	private fun shapeOf(obj: AnShapeObject) = when (obj.shape) {
		SHAPE_OVAL -> if (obj.width == obj.height)
			"FCircle(${obj.width})" else "FOval(${obj.width / 2.0}, ${obj.height / 2.0})"
		SHAPE_RECTANGLE -> "FRectangle(${obj.width}, ${obj.height})"
		else -> throw UnknownShapeException()
	}

	override fun toString(): String {
		val s = StringBuffer()
		objectList.forEach { o ->
			s.append("${typeOf(o)} ${o.x.toInt()} ${o.y.toInt()} ${o.width.toInt()} ${o.height.toInt()} ",
					///     0             1             2               3                   4
					"${o.fieldName} ")
			///                5
			when (o) {
				is AnText -> s.append("${o.text} ${o.color.rgb}")
			///                            6         7
				is AnShapeObject -> s.append("${o.shape} ${o.color.rgb}")
			///                                    6          7
				is AnPathImageObject -> s.append("${o.path}")
			///                                        6
				is AnWebImageObject -> s.append("${o.url}")
			///                                      6
			}
			s.append("\n")
		}
		return s.toString()
	}

	companion object {
		const val LANGUAGE_JAVA = 0x01
		const val LANGUAGE_KOTLIN = 0x02
		const val LANGUAGE_SCALA = 0x03

		const val SHAPE_OVAL = 0x04
		const val SHAPE_RECTANGLE = 0x05

		fun fromString(source: String): CodeData {
			val data = CodeData()
			source.split("\n").forEach { s ->
				val o = s.split(" ")
				when (o[0]) {
					Controller.simpleText -> data.objectList.add(AnText(
							o[1].toDouble(),
							o[2].toDouble(),
							o[5],
							Color(o[7].toInt()),
							o[6]
					))
					Controller.shapeObject -> data.objectList.add(AnShapeObject(
							o[1].toDouble(),
							o[2].toDouble(),
							o[3].toDouble(),
							o[4].toDouble(),
							o[5],
							Color(o[7].toInt()),
							o[6].toInt()
					))
				}
			}
			return data
		}

		private val javaCode = """// Generated codes
import org.frice.game.*;
import org.frice.game.obj.*;
import org.frice.game.obj.sub.*;
import org.frice.game.obj.button.*;
import org.frice.game.obj.effects.*;
import org.frice.game.resource.*;
import org.frice.game.resource.graphics.*;
import org.frice.game.resource.image.*;
import org.frice.game.anim.*;
import org.frice.game.anim.move.*;
import org.frice.game.anim.scale.*;
import org.frice.game.event.*;
import org.frice.game.utils.audio.*;
import org.frice.game.utils.misc.*;
import org.frice.game.utils.graphics.shape.*;
import org.frice.game.utils.graphics.utils.*;
import org.frice.game.utils.data.*;
import org.frice.game.utils.message.*;
import org.frice.game.utils.message.error.*;
import org.frice.game.utils.message.log.*;
import org.frice.game.utils.time.*;
import org.frice.game.utils.web.*;

/**
 * Frice engine class.
 * Auto-generated by Frice engine designer
 * Java
 */
public class ThisGame extends Game {

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

		private val kotlinCode = """// Generated codes
import org.frice.game.*
import org.frice.game.obj.*
import org.frice.game.obj.sub.*
import org.frice.game.obj.button.*
import org.frice.game.obj.effects.*
import org.frice.game.resource.*
import org.frice.game.resource.image.*
import org.frice.game.resource.graphics.*
import org.frice.game.anim.*
import org.frice.game.anim.move.*
import org.frice.game.anim.scale.*
import org.frice.game.event.*
import org.frice.game.utils.audio.*
import org.frice.game.utils.misc.*
import org.frice.game.utils.graphics.shape.*
import org.frice.game.utils.graphics.utils.*
import org.frice.game.utils.data.*
import org.frice.game.utils.message.*
import org.frice.game.utils.message.error.*
import org.frice.game.utils.message.log.*
import org.frice.game.utils.time.*
import org.frice.game.utils.web.*

/**
 * Frice engine class.
 * Auto-generated by Frice engine designer
 * Kotlin
 */
class ThisGame() : Game() {

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

		private val scalaCode = """// Generated codes
import org.frice.game._
import org.frice.game.obj._
import org.frice.game.obj.sub._
import org.frice.game.obj.button._
import org.frice.game.obj.effects._
import org.frice.game.resource._
import org.frice.game.resource.image._
import org.frice.game.resource.graphics._
import org.frice.game.anim._
import org.frice.game.anim.move._
import org.frice.game.anim.scale._
import org.frice.game.event._
import org.frice.game.utils.audio._
import org.frice.game.utils.misc._
import org.frice.game.utils.graphics.shape._
import org.frice.game.utils.graphics.utils._
import org.frice.game.utils.data._
import org.frice.game.utils.message._
import org.frice.game.utils.message.error._
import org.frice.game.utils.message.log._
import org.frice.game.utils.time._
import org.frice.game.utils.web._

/**
 * Frice engine class.
 * Auto-generated by Frice engine designer
 * Scala
 */
class ThisGame extends Game {

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
