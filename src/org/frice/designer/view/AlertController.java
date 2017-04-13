package org.frice.designer.view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by ice1000 on 2017/4/13.
 *
 * @author ice1000
 */
public class AlertController implements Initializable {
	@FXML
	private TextArea text;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		text.setText(AlertStage.TextHolder.getMessage());
	}
}
