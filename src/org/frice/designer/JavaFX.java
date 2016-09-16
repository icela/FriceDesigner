package org.frice.designer;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.util.ResourceBundle;

public class JavaFX extends Controller implements Initializable {

	@FXML
	public ListView<String> widgetsList;

	@NotNull
	@Override
	public ListView<String> getWidgetsList() {
		return widgetsList;
	}

	@FXML
	public void onMenuNewClicked(ActionEvent actionEvent) {
		super.onMenuNew();
	}

	@FXML
	public void onMenuOpenClicked(ActionEvent event) {
	}

	@FXML
	public void onMenuExitClicked(ActionEvent event) {
		super.onMenuExit();
	}

	@FXML
	public void onMenuSaveClicked(ActionEvent event) {
	}

	@FXML
	public void onMenuCloseClicked(ActionEvent event) {
	}

	@FXML
	public void onMenuSaveAsClicked(ActionEvent event) {
	}

	@FXML
	public void onMenuPreferenceClicked(ActionEvent event) {
	}

	@FXML
	public void onMenuAboutClicked(ActionEvent event) {
		super.onMenuAboutClicked();
	}

	@FXML
	public void onMainViewClicked(@NotNull MouseEvent event) {
		super.onMainViewClicked(event);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initialize();
	}
}
