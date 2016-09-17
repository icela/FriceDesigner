package org.frice.designer

import com.eldath.alerts.InfoAlert
import javafx.scene.control.Accordion
import javafx.scene.control.Label
import javafx.scene.control.ScrollPane
import javafx.scene.input.MouseEvent
import javafx.scene.input.TransferMode
import javafx.scene.paint.Color
import org.frice.designer.code.CodeData
import org.frice.game.utils.message.FDialog
import javax.swing.JOptionPane

/**
 * Created by ice1000 on 2016/9/15.
 *
 * @author ice1000
 */
abstract class Controller {

	protected abstract val widgetsList: Accordion

	protected abstract val shapeObjectChoice: Label
	protected abstract val webImageObjectChoice: Label
	protected abstract val pathImageObjectChoice: Label

	private val shapeObject = "ShapeObject"
	private val pathImageObject = "PathImageObject"
	private val webImageObject = "WebImageObject"

	private var currentSelection = shapeObject

	protected abstract val mainView: ScrollPane

	protected val codeData = CodeData()

	protected fun initialize() {
		mainView.setOnDragDone { e ->
			e.acceptTransferModes(TransferMode.MOVE)
			when (currentSelection) {
				shapeObject -> {
				}
			}
		}
		shapeObjectChoice.setOnMousePressed {
			shapeObjectChoice.textFill = Color.web("#0000FF")
			currentSelection = shapeObject
		}
		shapeObjectChoice.setOnMouseReleased {
			shapeObjectChoice.textFill = Color.web("#000000")
		}
		webImageObjectChoice.setOnMousePressed {
			webImageObjectChoice.textFill = Color.web("#0000FF")
			currentSelection = webImageObject
		}
		webImageObjectChoice.setOnMouseReleased {
			webImageObjectChoice.textFill = Color.web("#000000")
		}
		pathImageObjectChoice.setOnMousePressed {
			pathImageObjectChoice.textFill = Color.web("#0000FF")
			currentSelection = pathImageObject
		}
		pathImageObjectChoice.setOnMouseReleased {
			pathImageObjectChoice.textFill = Color.web("#000000")
		}
	}

	protected fun onMenuExit() {
		if (FDialog(null).confirm("Are you sure to exit frice engine designer?", "Frice engine designer",
				JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) System.exit(0)
	}

	protected fun onMenuNew() {
		val s = FDialog(null).input()
	}

	protected fun onMenuSave() {
	}

	protected fun onMenuPreference() {
	}

	protected open fun onMainViewClicked(event: MouseEvent) {
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