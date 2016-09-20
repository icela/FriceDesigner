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
import org.frice.game.resource.graphics.ColorResource
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

	private lateinit var boxes: List<TextField>

	private var currentSelection = shapeObjectOval

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
			var temp: AnObject? = null
			when (currentSelection) {
				shapeObjectOval, shapeObjectRectangle -> {
					temp = AnShapeObject(e.x, e.y, 30.0, 30.0,
							"shapeObject${random.nextInt(99999)}",
							ColorResource.IntelliJ_IDEA黑.color,
							if (currentSelection == shapeObjectOval) CodeData.SHAPE_OVAL
							else CodeData.SHAPE_RECTANGLE)
				}
				simpleText -> {
					temp = AnText(e.x, e.y,
							"simpleText${random.nextInt(99999)}",
							ColorResource.WHITE.color,
							"Hello World")
				}
				simpleButton -> {
					temp = AnButton(e.x, e.y, 80.0, 40.0,
							"simpleButton${random.nextInt(99999)}",
							ColorResource.洵濑绘理.color,
							"Click me!")
				}
				pathImageObject -> {
					temp = AnPathImageObject(e.x, e.y,
							"pathImageObject${random.nextInt(99999)}", "")
				}
			}
			temp?.let {
				objects.add(temp!!)
				changeSelected(temp!!)
				repaint()
			}
		}

		mainView.setOnMouseClicked { e ->
			var found = false
			var index = 0
			for (o in objects) {
				if (o.containsPoint(e.x, e.y)) {
					boxes.forEach { b ->
						b.isDisable = false
					}
					changeSelected(o, index)
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

		boxes = listOf(
				boxFieldName,
				boxHeight,
				boxWidth,
				boxSource,
				boxX,
				boxY,
				boxColor
		)

		ovalObjectChoice.setupChoice(shapeObjectOval) {
			boxSource.isDisable = true
		}

		rectangleObjectChoice.setupChoice(shapeObjectRectangle) {
			boxSource.isDisable = true
		}

		webImageObjectChoice.setupChoice(webImageObject) {
			boxColor.isDisable = true
		}

		pathImageObjectChoice.setupChoice(pathImageObject) {
			boxColor.isDisable = true
		}

		simpleTextChoice.setupChoice(simpleText) {
			boxHeight.isDisable = true
			boxWidth.isDisable = true
		}

		simpleButtonChoice.setupChoice(simpleButton) {
			boxSource.isDisable = true
		}

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
			when (objectChosen) {
				is AnText -> (objectChosen as AnText).color = awtColor(c)
				is AnShapeObject -> (objectChosen as AnShapeObject).color = awtColor(c)
				is AnButton -> (objectChosen as AnButton).color = awtColor(c)
			}
		}

		boxSource.setupInput { s ->
			when (objectChosen) {
				is AnPathImageObject -> (objectChosen as AnPathImageObject).path = s
				is AnWebImageObject -> (objectChosen as AnWebImageObject).url = s
				is AnText -> (objectChosen as AnText).text = s
				is AnButton -> (objectChosen as AnButton).text = s
			}
		}

		boxFieldName.setupInput { n ->
			objectChosen?.fieldName = n
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

	private fun Label.setupChoice(selection: String, disable: () -> Unit) {
		setOnMouseEntered { textFill = Color.web("#0000FF") }
		setOnMouseExited { textFill = Color.web("#000000") }
		setOnDragDetected {
			currentSelection = selection
			boxes.forEach { b ->
				b.isDisable = false
			}
			disable()
			startDragAndDrop(TransferMode.MOVE).run {
				setContent(ClipboardContent().apply {
					putString(selection)
				})
			}
		}
	}

	private fun TextField.setupInput(set: (String) -> Unit) {
		setOnKeyPressed { e ->
			if (e.code == KeyCode.ENTER) forceRun {
				set(text)
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
			is AnText -> {
				boxSource.text = o.text
				boxColor.text = "${o.color.rgb}"
			}
			is AnButton -> {
				boxSource.text = o.text
				boxColor.text = "${o.color.rgb}"
			}
			is AnShapeObject -> boxColor.text = "${o.color.rgb}"
			is AnPathImageObject -> boxSource.text = o.path
			is AnWebImageObject -> boxSource.text = o.url
		}

		repaint()
	}

	protected fun showCode(language: Int) {
		InfoAlert(codeData.getCode(language))
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
		const val fObject = "FObject"
		const val shapeObject = "ShapeObject"
		const val shapeObjectOval = "ShapeObjectOval"
		const val shapeObjectRectangle = "ShapeObjectRectangle"
		const val pathImageObject = "PathImageObject"
		const val webImageObject = "WebImageObject"
		const val simpleText = "SimpleText"
		const val simpleButton = "SimpleButton"
	}

}