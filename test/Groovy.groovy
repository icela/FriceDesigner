// Generated codes

import org.frice.game.Game
import org.frice.game.obj.button.SimpleButton
import org.frice.game.obj.button.SimpleText
import org.frice.game.resource.graphics.ColorResource
/**
 * Frice engine class.
 * Auto-generated by Frice engine designer
 * Groovy
 */
class Groovy extends Game {

	public def simpleButton28545
	public def simpleText40442

	@Override
	void onInit() {
		simpleButton28545 = new SimpleButton("Click me!", 16.0, 166.0, 60.0, 30.0)
		simpleButton28545.setColorResource(new ColorResource(-16776961))
		super.addObject(simpleButton28545)
		simpleText40442 = new SimpleText("Hello World", 231.0, 74.0)
		simpleText40442.setColorResource(new ColorResource(-1))
		super.addObject(simpleText40442)

	}

	static void main(String[] args) {
		new Groovy()
	}
}

