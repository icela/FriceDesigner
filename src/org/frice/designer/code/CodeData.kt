package org.frice.designer.code

import javafx.scene.image.Image
import org.frice.designer.controller.Controller
import org.frice.game.anim.move.DoublePair
import org.frice.game.utils.graphics.shape.FPoint
import org.frice.game.utils.misc.forceRun
import java.awt.Color
import java.util.*

/**
 * Created by ice1000 on 2016/9/17.
 *
 * @author ice1000
 */
class CodeData() {
	val objectList = LinkedList<AnObject>()
	var objectChosen: AnObject? = null

	fun getCode(language: Int, name: String): String = when (language) {
		LANGUAGE_JAVA -> String.format(javaCode, name, buildFieldCodes(language),
				buildInitCodes(language), name)
		LANGUAGE_KOTLIN -> String.format(kotlinCode, name, buildFieldCodes(language),
				buildInitCodes(language), name)
		LANGUAGE_SCALA -> String.format(scalaCode, name, buildFieldCodes(language),
				buildInitCodes(language), name, name)
		else -> throw UnknownLanguageException()
	}

	private fun buildInitCodes(language: Int): String {
		val sb = StringBuffer()
		objectList.forEach { o ->
			when (language) {
				LANGUAGE_SCALA, LANGUAGE_JAVA -> {
					when (o) {
						is AnShapeObject ->
							sb.append("\t\t${o.fieldName} = new ${typeOf(o)}(new ColorResource(0x${o.color.rgb}), ",
									"new ${shapeOf(o)}, ${o.x}, ${o.y});\n")
						is AnText ->
							sb.append("\t\t${o.fieldName} = new ${typeOf(o)}(\"${o.text}\", ${o.x}, ${o.y})\n",
									"\t\t${o.fieldName}.setColorResource(new ColorResource(0x${o.color.rgb}))\n")
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
			s.append("${typeOf(o)} ${o.x} ${o.y} ${o.width} ${o.height} ${o.fieldName} ")
			///           0           1      2        3           4             5
			when (o) {
				is AnText -> s.append("${o.text} ${o.color}")
			///                            6         7
				is AnShapeObject -> s.append("${o.shape} ${o.color}")
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
		@JvmField val LANGUAGE_JAVA = 0x01
		@JvmField val LANGUAGE_KOTLIN = 0x02
		@JvmField val LANGUAGE_SCALA = 0x03

		@JvmField val SHAPE_OVAL = 0x04
		@JvmField val SHAPE_RECTANGLE = 0x05

		fun fromString(source: String): CodeData {
			val data = CodeData()
			source.split("\n").forEach { s ->
				val o = s.split(" ")
				when (o[0]) {
					Controller.simpleText -> data.objectList.add(AnText(
							Integer.parseInt(o[1]).toDouble(),
							Integer.parseInt(o[2]).toDouble(),
							o[5],
							Color.getColor(o[7]),
							o[6]
					))
					Controller.shapeObject -> data.objectList.add(AnShapeObject(
							Integer.parseInt(o[1]).toDouble(),
							Integer.parseInt(o[2]).toDouble(),
							Integer.parseInt(o[3]).toDouble(),
							Integer.parseInt(o[4]).toDouble(),
							o[5],
							Color.getColor(o[7]),
							Integer.parseInt(o[6])
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
public class Game%source extends Game {

%source

	@Override
	protected void onInit() {
%source
	}

	public static void main(String[] args) {
		new Game%source();
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
class Game%source() : Game() {

%source

	override fun onInit() {
%source
	}

	companion object {
		@JvmStatic fun main(args: Array<String>) {
			Game%source()
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
class Game%source extends Game {

%source

	override def onInit(): Unit = {
%source
	}

object %sGame {
	def main(args: Array[String]): Unit = {
		new Game%source
	}
}
"""
	}
}

open class AnObject(
		open var x: Double,
		open var y: Double,
		open var width: Double,
		open var height: Double,
		open var fieldName: String) {
	fun containsPoint(px: Int, py: Int) = px >= x && px <= x + width && py >= y && py <= y + height
	fun containsPoint(px: Double, py: Double) = containsPoint(px.toInt(), py.toInt())
	infix fun containsPoint(point: FPoint) = containsPoint(point.x, point.y)
	infix fun containsPoint(d: DoublePair) = containsPoint(d.x, d.y)
}

class AnShapeObject(
		x: Double,
		y: Double,
		width: Double,
		height: Double,
		fieldName: String,
		var color: Color,
		var shape: Int
) : AnObject(x, y, width, height, fieldName)

class AnText(
		x: Double,
		y: Double,
		fieldName: String,
		var color: Color,
		var text: String
) : AnObject(x, y, text.length * 8.0, 16.0, fieldName) {
	override var width: Double
		get() = text.length * 8.0
		set(value) {
		}

	override var height: Double
		get() = 16.0
		set(value) {
		}
}

class AnPathImageObject(
		x: Double,
		y: Double,
		fieldName: String,
		path: String
) : AnObject(x, y, -1.0, -1.0, fieldName) {
	var image = Image(path)
	var path: String = ""
		set(value) {
			forceRun { image = Image(value) }
			field = value
		}

	init {
		this.path = path
	}
}

class AnWebImageObject(
		x: Double,
		y: Double,
		fieldName: String,
		var url: String
) : AnObject(x, y, -1.0, -1.0, fieldName)

class UnknownLanguageException() : Exception("Language given is unknown.")

class UnknownShapeException() : Exception("Shape given is unknown")
