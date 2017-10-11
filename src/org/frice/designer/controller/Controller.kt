package org.frice.designer.controller

import javafx.scene.canvas.Canvas
import javafx.scene.control.*
import javafx.scene.input.*
import javafx.scene.paint.Color
import javafx.stage.FileChooser
import org.frice.designer.code.*
import org.frice.designer.controller.canvas.Drawer
import org.frice.designer.view.AlertStage
import org.frice.game.utils.data.string2File
import org.frice.game.utils.message.FDialog
import org.lice.compiler.parse.toHexInt
import java.io.File
import javax.swing.JOptionPane
import java.awt.Color as AwtColor

/**
 * Created by ice1000 on 2016/9/15.
 *
 * @author ice1000
 */
abstract class Controller : Drawer() {

	protected abstract val widgetsList: Accordion

	protected abstract val ovalObjectChoice: Label
	protected abstract val rectangleObjectChoice: Label
	protected abstract val webImageObjectChoice: Label
	protected abstract val pathImageObjectChoice: Label

	protected abstract val simpleTextChoice: Label
	protected abstract val simpleButtonChoice: Label

	protected abstract val boxX: TextField
	protected abstract val boxY: TextField
	protected abstract val boxWidth: TextField
	protected abstract val boxHeight: TextField
	protected abstract val boxSource: TextField
	protected abstract val boxFieldName: TextField
	protected abstract val boxColor: TextField

	protected abstract val messageBox: TextArea

	protected abstract val menuSave: MenuItem

	private lateinit var disabling: List<TextField>

	private var currentSelection: AnObject? = AnObject.new()

	protected abstract val mainCanvas: Canvas
	protected abstract val mainView: ScrollPane

	private var random = -1

	private var workingFile: File? = null

	override val width: Double
		get() = mainCanvas.width

	override val height: Double
		get() = mainCanvas.height

	private var isDragging = false

	protected fun initialize() {
		repaint()

		mainView.setOnDragOver { e ->
			e.acceptTransferModes(TransferMode.MOVE)
			mainView.requestFocus()
		}

		mainView.setOnDragDropped { e ->
			println(isDragging)
			if (isDragging) {
				objects[objectIndexChosen!!] setLocation e
				isDragging = false
			}
			messageBox.text = "position: (${e.x}, ${e.y}).\nobject added.\n\ntype:\n$currentSelection"
			val temp = currentSelection
			temp?.let {
				temp.x = e.x
				temp.y = e.y
				objects.add(temp)
				changeSelected(temp)
				repaint()
			}
		}

		mainView.setOnDragDetected { e ->
			objectIndexChosen?.let {
				isDragging = objects[objectIndexChosen!!] containsPoint e
			}
			mainView.startDragAndDrop(TransferMode.MOVE)
		}

		mainView.setOnMouseClicked { e ->
			var found = false
			findObject(e.x, e.y) { o, i ->
				changeSelected(o, i)
				disableBoxes(o)
				setBoxValues(o)
				messageBox.text = "position: (${o.x}, ${o.y})\nexisting object selected."
				found = true
			}
			if (!found) {
				objectChosen = null
				messageBox.text = "position: (${e.x}, ${e.y})\nno objects selected."
			}
			repaint()
		}

		mainView.setOnKeyPressed { e ->
			when (e.code) {
				KeyCode.DELETE -> onMenuDeleteClicked()
				KeyCode.N -> if (e.isControlDown) onMenuNew()
				KeyCode.S -> if (e.isControlDown) onMenuSave()
				KeyCode.W -> if (e.isControlDown) onMenuExit()
				else -> {
				}
			}
		}

		disabling = listOf(
				boxHeight,
				boxWidth,
				boxSource,
				boxColor
		)

		ovalObjectChoice.setupChoice(AnOval.new(++random))

		rectangleObjectChoice.setupChoice(AnRectangle.new(++random))

		webImageObjectChoice.setupChoice(AnWebImageObject.new(++random, ""))

		pathImageObjectChoice.setupChoice(AnPathImageObject.new(++random, ""))

		simpleTextChoice.setupChoice(AnText.new(++random))

		simpleButtonChoice.setupChoice(AnButton.new(++random))

		boxX.setupInput { v ->
			objectChosen?.x = v.toDouble()
		}

		boxY.setupInput { v ->
			objectChosen?.y = v.toDouble()
		}

		boxWidth.setupInput { v ->
			objectChosen?.width = v.toDouble()
		}

		boxHeight.setupInput { v ->
			objectChosen?.height = v.toDouble()
		}

		boxColor.setupInput { c ->
			(objectChosen as? ColorOwner)?.run {
				//				println("0x$c".toHexInt())
				this.color = AwtColor("0x$c".toHexInt())
			}
		}

		boxSource.setupInput { s ->
			when (objectChosen) {
				is UrlOwner -> (objectChosen as UrlOwner).url = s
				is PathOwner -> (objectChosen as PathOwner).path = s
				is TextOwner -> (objectChosen as TextOwner).text = s
			}
		}

		boxFieldName.setupInput { n ->
			objectChosen?.fieldName = n
		}
	}

	private fun findObject(x: Double, y: Double, contains: (AnObject, Int) -> Unit) {
		var index = 0
		for (o in objects) {
			if (o.containsPoint(x, y)) {
				contains(o, index)
				break
			}
			++index
		}
	}

	private fun disableBoxes(o: AnObject) {
		disabling.forEach { b ->
			b.isDisable = true
		}

		if (o is ColorOwner) boxColor.isDisable = false
		if (o is TextOwner || o is PathOwner || o is UrlOwner) boxSource.isDisable = false
		if (o is EdgeOwner) {
			boxWidth.isDisable = false
			boxHeight.isDisable = false
		}
	}

	protected fun onMenuDeleteClicked() {
		if (objectIndexChosen != null) {
			messageBox.text = """menu item: delete
operation detected.
object at: (${objects[objectIndexChosen!!].x}, ${objects[objectIndexChosen!!].y})"""
			objects.removeAt(objectIndexChosen!!)
			objectChosen = null
			repaint()
		}
	}

	private fun Label.setupChoice(selection: AnObject) {
		setOnMouseEntered { textFill = Color.web("#0000FF") }
		setOnMouseExited { textFill = Color.web("#000000") }
		setOnDragDetected {
			currentSelection = selection
			disableBoxes(selection)
			startDragAndDrop(TransferMode.MOVE).run {
				setContent(ClipboardContent().apply {
					putString(selection.toString())
				})
			}
		}
	}

	private fun TextField.setupInput(set: (String) -> Unit) {
		setOnKeyPressed { e ->
			if (e.code == KeyCode.ENTER) try {
				set(text.trim())
				messageBox.text = "property changed.\n\nnew value:\n$text"
			} catch (e: Throwable) {
				messageBox.text = "some error occurred:\n${e.message}"
			}
			repaint()
		}
	}

	private fun repaint() = paint(mainCanvas.graphicsContext2D)

	protected fun onMenuExit() {
		if (FDialog(null).confirm("Are you sure to exit frice engine designer?", "Frice engine designer",
				JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION)
			System.exit(0)
	}

	protected fun onMenuNew() {
		FileChooser().showOpenDialog(null)
		messageBox.text = "menu item: new\noperation detected."
	}

	protected fun onMenuToolsExportFileClicked() {
		val file = FileChooser().apply {
			workingFile?.let { initialDirectory = it.parentFile }
			initialFileName = "ThisGame.java"
		}.showSaveDialog(null)
		messageBox.text = "menu item: export java.\noperation detected.\n\npath:\n$file\nclass name: ThisGame"
		codeData.getCode(CodeData.LANGUAGE_JAVA).string2File(file)
	}

	protected fun onMenuToolsJarClicked() {
		// TODO
	}

	protected fun onMenuToolsCompileClicked() {
		// TODO
	}

	protected fun onMenuRefreshViewClicked() = repaint()

	protected fun onMenuSave() {
		if (workingFile == null) showFileChoosingDialog()
		workingFile?.let { codeData.toString().string2File(it) }
		messageBox.text = "menu item: save\noperation detected.\n\npath:\n$workingFile"
		menuSave.isDisable = workingFile == null
	}

	private fun showFileChoosingDialog() {
		workingFile = FileChooser().apply {
			initialFileName = "save.txt"
		}.showSaveDialog(null)
	}

	protected fun onMenuSaveAs() {
		showFileChoosingDialog()
		onMenuSave()
	}

	protected fun onMenuPreference() {
		// TODO
	}

	protected fun onMenuOpen() {
		workingFile = FileChooser().showOpenDialog(null)
		messageBox.text = "menu item: open\noperation detected.\n\npath:\n$workingFile"
		workingFile?.let {
			codeData = CodeData.fromString(it.readText())
		}
		repaint()
	}

	/**
	 * change the selected object
	 */
	private fun changeSelected(o: AnObject, index: Int = objects.lastIndex) {
		objectChosen = o
		objectIndexChosen = index
		boxFieldName.text = o.fieldName

		setBoxValues(o)
		repaint()
	}

	private fun setBoxValues(o: AnObject) {
		boxX.text = "${o.x}"
		boxY.text = "${o.y}"
		boxWidth.text = "${o.width}"
		boxHeight.text = "${o.height}"

		if (o is ColorOwner) boxColor.text = Integer.toHexString(o.color.rgb)
		if (o is PathOwner) boxSource.text = o.path
		if (o is UrlOwner) boxSource.text = o.url
		if (o is TextOwner) boxSource.text = o.text
	}

	protected fun showCode(language: Int) {
		AlertStage(codeData.getCode(language))
	}

	protected fun compileAndRun() {
//		FriceCompiler compile codeData.getCode(CodeData.LANGUAGE_JAVA)
//		FriceCompiler.executeCompiled()
	}

	protected fun onMenuAboutClicked() {
		AlertStage("""Copyright(c) 2016 Frice Engine Designer
Under GNU General Public License v3.0.


English:
Author: ice1000
Website: https://github.com/icela/FriceEngine
with a wiki(full api reference) and lots of demos.


Chinese:
作者：千里冰封
网站：https://github.com/icela/FriceEngine
有一个wiki（完整的API参考）和很多Demo。""")
	}

	protected abstract fun setTitle(string: String)

	companion object Constants {
		const val fObject = "org.frice.game.obj.FObject"
		const val shapeObject_ = "org.frice.game.obj.sub.ShapeObject"
		const val shapeObjectOval = "ShapeObjectOval"
		const val shapeObjectRectangle = "ShapeObjectRectangle"
		const val pathImageObject = "org.frice.game.obj.sub.ImageObject-Path"
		const val webImageObject = "org.frice.game.obj.sub.ImageObject-Web"
		const val imageObject = "org.frice.game.obj.sub.ImageObject"
		const val simpleText = "org.frice.game.obj.button.SimpleText"
		const val simpleButton = "org.frice.game.obj.button.SimpleButton"
	}

}