package org.frice.designer.code

import java.util.*

/**
 * Created by ice1000 on 2016/9/17.
 *
 * @author ice1000
 */
class CodeData() {
	private val objectList = LinkedList<AnObject>()

	private val javaCode = """
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
	@Override
	protected void onInit() {
		%s
	}

	public static void main(String[] args) {
		new %sGame();
	}
}

"""

	private val kotlinCode = """
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
	private val scalaCode = """
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
	override def onInit(): Unit = {
		%s
	}

object %sGame {
	def main(args: Array[String]): Unit = {
		new %sGame
	}
}
"""

	fun getCode(language: String, name: String) = when (language) {
		LANGUAGE_JAVA -> String.format(javaCode, name, buildCodes(), name)
		LANGUAGE_KOTLIN -> String.format(kotlinCode, name, buildCodes(), name)
		LANGUAGE_SCALA -> String.format(scalaCode, name, buildCodes(), name, name)
		else -> throw UnknownLanguageException()
	}

	private fun buildCodes() = ""

	companion object {
		@JvmField val LANGUAGE_JAVA = "LANGUAGE_JAVA"
		@JvmField val LANGUAGE_KOTLIN = "LANGUAGE_KOTLIN"
		@JvmField val LANGUAGE_SCALA = "LANGUAGE_SCALA"
	}
}

class AnObject(var x: Double, var y: Double, var width: Double, var height: Double)

class UnknownLanguageException() : Exception("Language given is unknown.")