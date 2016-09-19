package org.frice.designer.canvas

import javafx.scene.canvas.GraphicsContext
import org.frice.designer.code.*
import org.frice.game.resource.graphics.ColorResource
import java.awt.Color
import java.util.*

/**
 * Created by ice1000 on 2016/9/17.
 *
 * @author ice1000
 */
abstract class Drawer() {
	protected var objects: LinkedList<AnObject>
		get () = codeData.objectList
		set(value) {
			codeData.objectList = value
		}

	protected var codeData = CodeData()

	protected abstract val width: Double
	protected abstract val height: Double

	protected var objectChosen: AnObject?
		get() = codeData.objectChosen
		set(value) {
			if (value == null) objectIndexChosen = null
			codeData.objectChosen = value
		}

	protected var objectIndexChosen: Int? = null

	protected fun paint(g: GraphicsContext) {
		g.fill = fromColor(ColorResource.LIGHT_GRAY.color)
		g.fillRect(0.0, 0.0, width, height)
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
					g.fillText(o.text, o.x + 2, o.y + 13)
				}
				is AnPathImageObject -> {
					o.image?.let {
						g.drawImage(o.image, o.x, o.y, o.image!!.width, o.image!!.height)
					}
				}
			}
		}
		paintObjectChosen(g)
	}

	protected infix fun paintObjectChosen(g: GraphicsContext) {
		if (objectChosen != null) {
			g.stroke = fromColor(Color.BLUE)
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