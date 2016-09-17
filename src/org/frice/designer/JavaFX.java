package org.frice.designer;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Accordion;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.util.ResourceBundle;

public class JavaFX extends Controller implements Initializable {

	public Accordion widgetsList;
	public ScrollPane mainView;
	public Label shapeObjectChoice;
	public Label webImageObjectChoice;
	public Label pathImageObjectChoice;
	public Label projectName;

	@NotNull
	@Override
	public Accordion getWidgetsList() {
		return widgetsList;
	}

	public void onMenuNewClicked(ActionEvent actionEvent) {
		super.onMenuNew();
	}

	public void onMenuOpenClicked(ActionEvent event) {
	}

	public void onMenuExitClicked(ActionEvent event) {
		super.onMenuExit();
	}

	public void onMenuSaveClicked(ActionEvent event) {
	}

	public void onMenuCloseClicked(ActionEvent event) {
	}

	public void onMenuSaveAsClicked(ActionEvent event) {
	}

	public void onMenuPreferenceClicked(ActionEvent event) {
	}

	public void onMenuAboutClicked(ActionEvent event) {
		super.onMenuAboutClicked();
	}

	public void onMainViewClicked(@NotNull MouseEvent event) {
		super.onMainViewClicked(event);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initialize();
	}

	@Override
	protected void setTitle(@NotNull String string) {
		projectName.setText(string);
	}
}
