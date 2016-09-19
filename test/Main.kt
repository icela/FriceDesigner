// Generated codes
import org.frice.game.Game
import org.frice.game.obj.button.SimpleText
import org.frice.game.obj.sub.ShapeObject
import org.frice.game.resource.graphics.ColorResource
import org.frice.game.utils.graphics.shape.FCircle
import org.frice.game.utils.graphics.shape.FRectangle

/**
 * Frice engine class.
 * Kotlin
 */
class FriceProjectGame() : Game() {

	lateinit var text: SimpleText
	lateinit var simpleText78618: SimpleText
	lateinit var simpleText76577: SimpleText
	lateinit var shapeObject29414: ShapeObject
	lateinit var shapeObject4058: ShapeObject


	override fun onInit() {
		text = SimpleText("Frice engine by ice1000", 100.0, 100.0)
		text.colorResource = ColorResource(-13948117)
		super<Game>.addObject(text)
		simpleText78618 = SimpleText("This is the designer for frice engine", 104.0, 126.0)
		simpleText78618.colorResource = ColorResource(-13948117)
		super<Game>.addObject(simpleText78618)
		simpleText76577 = SimpleText("These are SimpleText widgets", 300.0, 222.0)
		simpleText76577.colorResource = ColorResource(-13948117)
		super<Game>.addObject(simpleText76577)
		shapeObject29414 = ShapeObject(ColorResource(-13948117), FRectangle(100, 100), 142.0, 371.0)
		super.addObject(shapeObject29414)
		shapeObject4058 = ShapeObject(ColorResource(-13948117), FCircle(10.0), 151.0, 357.0)
		super<Game>.addObject(shapeObject4058)

	}

	companion object {
		@JvmStatic fun main(args: Array<String>) {
			FriceProjectGame()
		}
	}
}

