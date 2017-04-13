package org.frice.designer.view

import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage

/**
 * Created by ice1000 on 2017/4/13.

 * @author ice1000
 */
class AlertStage(text: String) : Stage() {
	init {
		message = text
		title = "Frice Designer Output"
		scene = Scene(FXMLLoader.load<Parent>(javaClass.getResource("alert.fxml")))
		show()
	}

	companion object TextHolder {
		var message = ""
	}
}
