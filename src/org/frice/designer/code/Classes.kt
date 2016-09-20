/**
 * Created by ice1000 on 2016/9/19.
 *
 * @author ice1000
 */
package org.frice.designer.code

import javafx.scene.image.Image
import org.frice.designer.controller.Controller
import org.frice.game.anim.move.DoublePair
import org.frice.game.resource.graphics.ColorResource
import org.frice.game.utils.graphics.shape.FPoint
import org.frice.game.utils.misc.forceRun
import java.awt.Color

interface EdgeOwner {
	var width: Double
	var height: Double
}

interface ColorOwner {
	var color: Color
}

interface TextOwner {
	var text: String
}

interface PathOwner {
	var path: String
}

interface UrlOwner {
	var url: String
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

	override fun toString() = Controller.fObject

	companion object {
		fun new(): AnObject? = null
	}
}

class AnOval(
		x: Double,
		y: Double,
		width: Double,
		height: Double,
		fieldName: String,
		override var color: Color
) : AnObject(x, y, width, height, fieldName), ColorOwner, EdgeOwner {
	override fun toString() = Controller.shapeObjectOval

	companion object {
		fun new(random: Int, x: Double = -100.0, y: Double = -100.0) = AnOval(x, y, 30.0, 30.0,
				"shapeObject$random", ColorResource.IntelliJ_IDEA黑.color)
	}
}

class AnRectangle(
		x: Double,
		y: Double,
		width: Double,
		height: Double,
		fieldName: String,
		override var color: Color
) : AnObject(x, y, width, height, fieldName), ColorOwner, EdgeOwner {
	override fun toString() = Controller.shapeObjectRectangle

	companion object {
		fun new(random: Int, x: Double = -100.0, y: Double = -100.0) = AnRectangle(x, y, 30.0, 30.0,
				"shapeObject$random", ColorResource.IntelliJ_IDEA黑.color)
	}
}

class AnText(
		x: Double,
		y: Double,
		fieldName: String,
		override var color: Color,
		override var text: String
) : AnObject(x, y, text.length * 8.0, 16.0, fieldName), TextOwner, ColorOwner {
	override var width: Double
		get() = text.length * 8.0
		set(value) {
		}

	override var height: Double
		get() = 16.0
		set(value) {
		}

	override fun toString() = Controller.simpleText

	companion object {
		fun new(random: Int, x: Double = -100.0, y: Double = -100.0) = AnText(x, y,
				"simpleText$random", ColorResource.WHITE.color, "HelloWorld")
	}
}

class AnPathImageObject(
		x: Double,
		y: Double,
		fieldName: String,
		path: String
) : AnObject(x, y, -1.0, -1.0, fieldName), PathOwner {
	var image: Image? = null
	override var path: String = ""
		set(value) {
			forceRun { image = Image(value) }
			field = value
		}

	init {
		this.path = path
	}

	override fun toString() = Controller.pathImageObject
}

class AnWebImageObject(
		x: Double,
		y: Double,
		fieldName: String,
		url: String
) : AnObject(x, y, -1.0, -1.0, fieldName), UrlOwner {
	var image: Image? = null
	override var url: String = ""
		set(value) {
			field = value
			image = Image(value)
		}

	init {
		this.url = url
	}

	override fun toString() = Controller.webImageObject

	companion object {
		fun new(random: Int, x: Double = -100.0, y: Double = -100.0) = AnPathImageObject(x, y, "pathImageObject$random", "")
	}
}

class AnButton(
		x: Double,
		y: Double,
		width: Double,
		height: Double,
		fieldName: String,
		override var color: Color,
		override var text: String
) : AnObject(x, y, width, height, fieldName), ColorOwner, TextOwner, EdgeOwner {
	override fun toString() = Controller.simpleButton

	companion object {
		fun new(random: Int, x: Double = -100.0, y: Double = -100.0) = AnButton(x, y, 80.0, 40.0,
				"simpleButton$random", ColorResource.洵濑绘理.color, "Start")
	}
}

class UnknownLanguageException() : Exception("Language given is unknown.")

class UnknownObjectException() : Exception("Object given is unknown.")

class UnknownShapeException() : Exception("Shape given is unknown")
