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
 */
public class %sGame extends Game {
	@Override
	protected void onInit() {
		super.onInit();
		%s
	}

	public static void main(String[] args) {
		new %sGame();
	}
}

"""

	private val kotlinCode = """
package org.frice.designer.code

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
 */
class %sGame() : Game() {
	override fun onInit() {
		super.onInit()
		%s
	}

	companion object {
		@JvmStatic fun main(args: Array<String>) {
			%sGame()
		}
	}
}

"""

	fun getCode(language: String, name: String) = String.format(when (language) {
		LANGUAGE_JAVA -> javaCode
		LANGUAGE_KOTLIN -> kotlinCode
		else -> throw UnknownLanguageException()
	}, name, buildCodes(), name)

	private fun buildCodes() = ""

	companion object {
		val LANGUAGE_JAVA = "LANGUAGE_JAVA"
		val LANGUAGE_KOTLIN = "LANGUAGE_KOTLIN"
	}
}

class AnObject(var x: Double, var y: Double, var width: Double, var height: Double)

class UnknownLanguageException() : Exception("Language given is unknown.")