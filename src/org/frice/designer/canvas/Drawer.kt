package org.frice.designer.canvas

import javafx.scene.canvas.GraphicsContext
import org.frice.designer.code.AnObject
import org.frice.designer.code.AnShapeObject
import org.frice.designer.code.AnText
import org.frice.designer.code.CodeData
import java.util.*

/**
 * Created by ice1000 on 2016/9/17.
 *
 * @author ice1000
 */
open class Drawer() {
	protected val objects = LinkedList<AnObject>()

	protected fun paint(g: GraphicsContext) {
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
			}
		}
	}

	companion object {
		fun fromColor(c: java.awt.Color) = javafx.scene.paint.Color.rgb(c.red, c.green, c.blue)
	}
}