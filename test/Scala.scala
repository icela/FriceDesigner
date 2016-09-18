import org.frice.game.Game
import org.frice.game.obj.button.SimpleText
import org.frice.game.obj.sub.ShapeObject
import org.frice.game.resource.graphics.ColorResource
import org.frice.game.utils.graphics.shape.FRectangle

class Scala extends Game {
	var a: ShapeObject = _

	override def onInit(): Unit = {
		a = new ShapeObject(new ColorResource(1), new FRectangle(1, 1), 0, 0)
		val b = new SimpleText("", 1, 1)
		b.setColorResource(new ColorResource(1))
	}

}