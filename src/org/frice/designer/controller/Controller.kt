package org.frice.designer.controller

import com.eldath.alerts.InfoAlert
import javafx.scene.canvas.Canvas
import javafx.scene.control.*
import javafx.scene.input.ClipboardContent
import javafx.scene.input.KeyCode
import javafx.scene.input.TransferMode
import javafx.scene.paint.Color
import javafx.stage.FileChooser
import org.frice.designer.canvas.Drawer
import org.frice.designer.code.*
import org.frice.game.utils.data.FileUtils
import org.frice.game.utils.message.FDialog
import org.frice.game.utils.misc.forceRun
import java.io.File
import java.util.*
import javax.swing.JOptionPane

/**
 * Created by ice1000 on 2016/9/15.
 *
 * @author ice1000
 */
abstract class Controller() : Drawer() {

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

	private lateinit var disabling: List<TextField>

	private var currentSelection: AnObject? = AnObject.new()

	protected abstract val mainCanvas: Canvas
	protected abstract val mainView: ScrollPane

	private val random = Random()

	private var workingFile: File? = null

	override val width: Double
		get() = mainCanvas.width

	override val height: Double
		get() = mainCanvas.height

	protected fun initialize() {
		repaint()

		mainView.setOnDragOver { e ->
			e.acceptTransferModes(TransferMode.MOVE)
			mainView.requestFocus()
		}

		mainView.setOnDragDropped { e ->
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

		mainView.setOnMouseClicked { e ->
			var found = false
			var index = 0
			for (o in objects) {
				if (o.containsPoint(e.x, e.y)) {
					changeSelected(o, index)
					disableBoxes(o)
					messageBox.text = "position: (${o.x}, ${o.y})\nexisting object selected."
					found = true
					break
				}
				++index
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

		ovalObjectChoice.setupChoice(AnOval.new(random.nextInt(99999)))

		rectangleObjectChoice.setupChoice(AnRectangle.new(random.nextInt(99999)))

//		webImageObjectChoice.setupChoice(webImageObject)

//		pathImageObjectChoice.setupChoice(pathImageObject)

		simpleTextChoice.setupChoice(AnText.new(random.nextInt(99999)))

		simpleButtonChoice.setupChoice(AnButton.new(random.nextInt(99999)))

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
			if (objectChosen is ColorOwner) (objectChosen as ColorOwner).color = awtColor(c)
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
			if (e.code == KeyCode.ENTER) forceRun {
				set(text)
				messageBox.text = "property changed.\n\nnew value:\n$text"
			}
			repaint()
		}
	}

	private fun repaint() = paint(mainCanvas.graphicsContext2D)

	private fun awtColor(c: String) = java.awt.Color(c.toInt())
	private fun awtColor(c: Int) = java.awt.Color(c)

	protected fun onMenuExit() {
		if (FDialog(null).confirm("Are you sure to exit frice engine designer?", "Frice engine designer",
				JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) System.exit(0)
	}

	protected fun onMenuNew() {
		FileChooser().showOpenDialog(null)
		messageBox.text = "menu item: new\noperation detected."
	}

	protected fun onMenuToolsExportFileClicked() {
		val file = FileChooser().apply {
			if (workingFile != null) initialDirectory = workingFile!!.parentFile
			initialFileName = "ThisGame.java"
		}.showSaveDialog(null)
		messageBox.text = "menu item: export java.\noperation detected.\n\npath:\n$file\nclass name: ThisGame"
		FileUtils.string2File(codeData.getCode(CodeData.LANGUAGE_JAVA), file)
	}

	protected fun onMenuToolsJarClicked() {
	}

	protected fun onMenuToolsCompileClicked() {
	}

	protected fun onMenuSave() {
		if (workingFile == null)
			workingFile = FileChooser().apply {
				initialFileName = "save.txt"
			}.showSaveDialog(null)
		FileUtils.string2File(codeData.toString(), workingFile!!)
		messageBox.text = "menu item: save\noperation detected.\n\npath:\n$workingFile"

	}

	protected fun onMenuPreference() {
	}

	protected fun onMenuOpen() {
		workingFile = FileChooser().showOpenDialog(null)
		messageBox.text = "menu item: open\noperation detected.\n\npath:\n$workingFile"
		codeData = CodeData.fromString(workingFile!!.readText())
		repaint()
	}

	/**
	 * change the selected object
	 */
	private fun changeSelected(o: AnObject, index: Int = objects.lastIndex) {
		objectChosen = o
		objectIndexChosen = index
		boxFieldName.text = o.fieldName
		boxX.text = "${o.x}"
		boxY.text = "${o.y}"
		boxWidth.text = "${o.width}"
		boxHeight.text = "${o.height}"

		when (o) {
			is ColorOwner -> boxColor.text = "${o.color.rgb}"
			is TextOwner -> boxSource.text = o.text
			is PathOwner -> boxSource.text = o.path
			is UrlOwner -> boxSource.text = o.url
		}

		repaint()
	}

	protected fun showCode(language: Int) {
		InfoAlert(codeData.getCode(language))
	}

	protected fun compileAndRun() {
		FriceCompiler compile codeData.getCode(CodeData.LANGUAGE_JAVA)
	}

	protected fun onMenuAboutClicked() {
		InfoAlert("""Copyright(c) 2016 Frice Engine Designer
Under Apache 2.0 License.


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

	companion object {
		const val fObject = "org.frice.game.obj.FObject"
		const val shapeObject_ = "org.frice.game.obj.sub.ShapeObject"
		const val shapeObjectOval = "ShapeObjectOval"
		const val shapeObjectRectangle = "ShapeObjectRectangle"
		const val pathImageObject = "org.frice.game.obj.sub.ImageObject"
		const val webImageObject = "org.frice.game.obj.sub.ImageObject"
		const val simpleText = "org.frice.game.obj.button.SimpleText"
		const val simpleButton = "org.frice.game.obj.button.SimpleButton"
	}

}