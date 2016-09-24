import org.frice.designer.code.AutoCompiler

/**
 * testing the compiler
 * Created by ice1000 on 2016/9/24.
 *
 * @author ice1000
 */
fun main(args: Array<String>) {
	//language=JAVA
	val a = AutoCompiler("""
class ThisGame extends org.frice.game.Game {
	public org.frice.game.obj.button.SimpleButton bt;
	@Override
	protected void onInit() {
		bt = new org.frice.game.obj.button.SimpleButton("Start", 407.0, 251.0, 80.0, 40.0);
		bt.setColorResource(new org.frice.game.resource.graphics.ColorResource(-15728641));
		super.addObject(bt);
	}
	public static void main(String[] args) {
		new ThisGame();
	}
}""", "out/")
	println(a.autoInvoke("main"))
}