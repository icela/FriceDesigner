package org.frice.designer.canvas

import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import org.frice.game.obj.sub.ShapeObject
import org.frice.game.utils.graphics.shape.FOval
import org.frice.game.utils.graphics.shape.FRectangle
import java.util.*

/**
 * Created by ice1000 on 2016/9/17.
 *
 * @author ice1000
 */
open class Drawer() {
	protected val shapes = LinkedList<ShapeObject>()

	protected fun paint(g: GraphicsContext) {
		shapes.forEach { s ->
			g.fill = Color.rgb(s.res.color.red, s.res.color.green, s.res.color.blue)
			when (s.collideBox) {
				is FOval -> g.fillOval(s.x, s.y, s.width, s.height)
				is FRectangle -> g.fillRect(s.x, s.y, s.width, s.height)
			}
		}
	}
}