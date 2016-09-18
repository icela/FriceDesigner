package org.frice.designer.controller

import com.eldath.alerts.InfoAlert
import javafx.scene.canvas.Canvas
import javafx.scene.control.Accordion
import javafx.scene.control.Label
import javafx.scene.control.ScrollPane
import javafx.scene.control.TextField
import javafx.scene.input.ClipboardContent
import javafx.scene.input.TransferMode
import javafx.scene.paint.Color
import org.frice.designer.canvas.Drawer
import org.frice.designer.code.AnObject
import org.frice.designer.code.AnShapeObject
import org.frice.designer.code.CodeData
import org.frice.game.resource.graphics.ColorResource
import org.frice.game.utils.message.FDialog
import org.frice.game.utils.message.log.FLog
import java.util.*
import javax.swing.JOptionPane

/**
 * Created by ice1000 on 2016/9/15.
 *
 * @author ice1000
 */
abstract class Controller() : Drawer() {

	protected abstract val widgetsList: Accordion

	protected abstract val shapeObjectChoice: Label
	protected abstract val webImageObjectChoice: Label
	protected abstract val pathImageObjectChoice: Label
	protected abstract val simpleTextChoice: Label

	protected abstract val boxX: TextField
	protected abstract val boxY: TextField
	protected abstract val boxWidth: TextField
	protected abstract val boxHeight: TextField
	protected abstract val boxSource: TextField
	protected abstract val boxFieldName: TextField

	private val shapeObject = "ShapeObject"
	private val pathImageObject = "PathImageObject"
	private val webImageObject = "WebImageObject"
	private val simpleText = "SimpleText"

	private var currentSelection = shapeObject

	protected abstract val mainCanvas: Canvas
	protected abstract val mainView: ScrollPane

	protected val codeData = CodeData()

	private val random = Random()

	protected fun initialize() {
		mainView.setOnDragOver { e -> e.acceptTransferModes(TransferMode.MOVE) }
		mainView.setOnDragDropped { e ->
			when (currentSelection) {
				shapeObject -> {
					addObject(AnShapeObject(e.x, e.y, 10.0, 10.0,
							"shapeObject${random.nextInt(99999)}",
							ColorResource.IntelliJ_IDEA黑.color,
							CodeData.SHAPE_OVAL
					))
					paint(mainCanvas.graphicsContext2D)
				}
				pathImageObject -> FLog.d(currentSelection)
			}
		}
		mainView.setOnMouseClicked { e ->
			codeData.objectList.forEach { o ->
				if (o.containsPoint(e.x, e.y)) {
				}
			}
		}
		shapeObjectChoice.setupChoice(shapeObject)
		webImageObjectChoice.setupChoice(webImageObject)
		pathImageObjectChoice.setupChoice(pathImageObject)
		simpleTextChoice.setupChoice(simpleText)
	}

	private fun Label.setupChoice(selection: String) {
		setOnMouseEntered { textFill = Color.web("#0000FF") }
		setOnMouseExited { textFill = Color.web("#000000") }
		setOnDragDetected {
			currentSelection = selection
			startDragAndDrop(TransferMode.MOVE).run {
				setContent(ClipboardContent().apply { putString(selection) })
			}
		}
	}

	protected fun onMenuExit() {
		if (FDialog(null).confirm("Are you sure to exit frice engine designer?", "Frice engine designer",
				JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) System.exit(0)
	}

	private fun addObject(o: AnObject) {
		objects.add(o)
		codeData.objectList.add(o)
	}

	private fun removeObject(o: AnObject) {
		objects.add(o)
		codeData.objectList.add(o)
	}

	protected fun onMenuNew() {
		val s = FDialog(null).input()
	}

	protected fun onMenuSave() {
	}

	protected fun onMenuPreference() {
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

}