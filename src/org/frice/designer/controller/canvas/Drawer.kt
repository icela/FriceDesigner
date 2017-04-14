package org.frice.designer.controller.canvas

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
			if (o is ColorOwner) g.fill = fromColor(o.color)
			when (o) {
				is AnOval -> g.fillOval(o.x, o.y, o.width, o.height)
				is AnRectangle -> g.fillRect(o.x, o.y, o.width, o.height)
				is AnText -> g.fillText(o.text, o.x + 2, o.y + 13)
				is AnButton -> {
					/// from Frice engine.
					g.fillRoundRect(o.x, o.y, o.width, o.height,
							Math.min(o.width * 0.5, 10.0),
							Math.min(o.height * 0.5, 10.0))
					g.fill = fromColor(ColorResource.DARK_GRAY.color)
					g.fillText(o.text, o.x + 10, o.y + o.height / 2)
				}
				is AnPathImageObject ->
					o.image?.let { g.drawImage(it, o.x, o.y, it.width, it.height) }
				is AnWebImageObject ->
					o.image?.let { g.drawImage(it, o.x, o.y, it.width, it.height) }
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