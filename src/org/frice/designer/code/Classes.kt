/**
 * Created by ice1000 on 2016/9/19.
 *
 * @author ice1000
 */
package org.frice.designer.code

import javafx.scene.image.Image
import org.frice.game.anim.move.DoublePair
import org.frice.game.utils.graphics.shape.FPoint
import org.frice.game.utils.misc.forceRun
import java.awt.Color


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
