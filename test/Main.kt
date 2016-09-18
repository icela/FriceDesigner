// Generated codes
import org.frice.game.Game
import org.frice.game.obj.sub.ShapeObject
import org.frice.game.resource.graphics.ColorResource
import org.frice.game.utils.graphics.shape.FCircle

/**
 * Frice engine class.
 * Kotlin
 */
class FriceProjectGame() : Game() {

	lateinit var shapeObject59441: ShapeObject


	override fun onInit() {
		shapeObject59441 = ShapeObject(ColorResource(-13948117), FCircle(100.0), 325.0, 48.0)
		super<Game>.addObject(shapeObject59441)

	}

	companion object {
		@JvmStatic fun main(args: Array<String>) {
			FriceProjectGame()
		}
	}
}

