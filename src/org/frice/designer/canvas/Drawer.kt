package org.frice.designer.canvas

import javafx.scene.canvas.GraphicsContext
import org.frice.designer.code.*
import java.awt.Color
import java.util.*

/**
 * Created by ice1000 on 2016/9/17.
 *
 * @author ice1000
 */
abstract class Drawer() {
	protected val objects = object : LinkedList<AnObject>() {
		override fun add(element: AnObject): Boolean {
			objectChosen = element
			codeData.objectList.add(element)
			return super.add(element)
		}

		override fun remove(element: AnObject): Boolean {
			objectChosen = null
			codeData.objectList.remove(element)
			return super.remove(element)
		}
	}

	protected val codeData = CodeData()

	protected abstract val width: Double
	protected abstract val height: Double

	protected var objectChosen: AnObject?
		get() = codeData.objectChosen
		set(value) {
			codeData.objectChosen = value
		}

	protected fun paint(g: GraphicsContext) {
		g.clearRect(0.0, 0.0, width, height)
		objects.forEach { o ->
			when (o) {
				is AnShapeObject -> {
					g.fill = fromColor(o.color)
					when (o.shape) {
						CodeData.SHAPE_OVAL -> g.fillOval(o.x, o.y, o.width, o.height)
						CodeData.SHAPE_RECTANGLE -> g.fillRect(o.x, o.y, o.width, o.height)
					}
				}
				is AnText -> {
					g.fill = fromColor(o.color)
					g.fillText(o.text, o.x, o.y)
				}
				is AnPathImageObject -> g.drawImage(o.image, o.x, o.y, o.image.width, o.image.height)
			}
		}
		if (objectChosen != null) {
			println("2333")
			g.fill = fromColor(Color.BLUE)
			g.strokeRect(objectChosen?.x!!,
					objectChosen?.y!!,
					objectChosen?.width!!,
					objectChosen?.height!!)
		}
	}

	companion object {
		infix fun fromColor(c: java.awt.Color) = javafx.scene.paint.Color.rgb(c.red, c.green, c.blue)!!
	}
}