package org.frice.designer.code

import java.awt.Color
import java.util.*

/**
 * Created by ice1000 on 2016/9/17.
 *
 * @author ice1000
 */
class CodeData() {
	private val objectList = LinkedList<AnObject>()

	fun getCode(language: Int, name: String) = when (language) {
		LANGUAGE_JAVA -> String.format(javaCode, name, buildFieldCodes(language),
				buildInitCodes(language), name)
		LANGUAGE_KOTLIN -> String.format(kotlinCode, name, buildFieldCodes(language),
				buildInitCodes(language), name)
		LANGUAGE_SCALA -> String.format(scalaCode, name, buildFieldCodes(language),
				buildInitCodes(language), name, name)
		else -> throw UnknownLanguageException()
	}

	private fun buildInitCodes(type: Int): String {
		val s = StringBuilder()
		objectList.forEach { o ->
			when (type) {
				LANGUAGE_SCALA -> {
				}
				LANGUAGE_JAVA -> {
				}
				LANGUAGE_KOTLIN -> {
				}
			}
		}
		return s.toString()
	}

	private fun buildFieldCodes(type: Int): String {
		val s = StringBuilder()
		objectList.forEach { o ->
			when (type) {
				LANGUAGE_SCALA -> {
				}
				LANGUAGE_JAVA -> {
				}
				LANGUAGE_KOTLIN -> {
					s.append("")
				}
			}
		}
		return s.toString()
	}

	companion object {
		@JvmField val LANGUAGE_JAVA = 0x01
		@JvmField val LANGUAGE_KOTLIN = 0x02
		@JvmField val LANGUAGE_SCALA = 0x03

		@JvmField val SHAPE_OVAL = 0x05
		@JvmField val SHAPE_RECTANGLE = 0x06

		private val javaCode = """// Generated codes
import org.frice.game.*;
import org.frice.game.obj.*;
import org.frice.game.resource.*;
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
 * Java
 */
public class %sGame extends Game {

	%s

	@Override
	protected void onInit() {
		%s
	}

	public static void main(String[] args) {
		new %sGame();
	}
}

"""

		private val kotlinCode = """// Generated codes
import org.frice.game.*
import org.frice.game.obj.*
import org.frice.game.resource.*
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
 * Kotlin
 */
class %sGame() : Game() {

	%s

	override fun onInit() {
		%s
	}

	companion object {
		@JvmStatic fun main(args: Array<String>) {
			%sGame()
		}
	}
}

"""

		private val kotlinField = """lateinit var %s """

		private val scalaCode = """// Generated codes
import org.frice.game._
import org.frice.game.obj._
import org.frice.game.resource._
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
 * Scala
 */
class %sGame extends Game {

	%s

	override def onInit(): Unit = {
		%s
	}

object %sGame {
	def main(args: Array[String]): Unit = {
		new %sGame
	}
}
"""
	}
}

open class AnObject(
		var x: Double,
		var y: Double,
		var width: Double,
		var height: Double,
		var fieldName: String
)

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
) : AnObject(x, y, 0.0, 0.0, fieldName)

class AnPathImageObject(
		x: Double,
		y: Double,
		width: Double,
		height: Double,
		fieldName: String,
		var path: String
) : AnObject(x, y, width, height, fieldName)

class AnWebImageObject(
		x: Double,
		y: Double,
		width: Double,
		height: Double,
		fieldName: String,
		var url: String
) : AnObject(x, y, width, height, fieldName)

class UnknownLanguageException() : Exception("Language given is unknown.")