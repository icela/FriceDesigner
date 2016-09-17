package org.frice.designer.controller;

import com.eldath.alerts.InfoAlert;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import org.frice.designer.code.CodeData;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;

public class JavaFX extends Controller implements Initializable {

	public Accordion widgetsList;

	public Label shapeObjectChoice;
	public Label webImageObjectChoice;
	public Label pathImageObjectChoice;
	public Label projectName;
	public Canvas mainCanvas;
	public ScrollPane mainView;

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
		super.initialize();
	}

	@Override
	protected void setTitle(@NotNull String string) {
		projectName.setText(string);
	}

	public void onMenuJavaCodeClicked(ActionEvent event) {
		new InfoAlert(getCodeData().getCode(CodeData.LANGUAGE_JAVA, projectName.getText()));
	}

	public void onMenuKotlinCodeClicked(ActionEvent event) {
		new InfoAlert(getCodeData().getCode(CodeData.LANGUAGE_KOTLIN, projectName.getText()));
	}

	public void onMenuScalaCodeClicked(ActionEvent event) {
		new InfoAlert(getCodeData().getCode(CodeData.LANGUAGE_SCALA, projectName.getText()));
	}

	@NotNull
	public Label getShapeObjectChoice() {
		new Color(0xfffff);
		return shapeObjectChoice;
	}

	@NotNull
	public Label getWebImageObjectChoice() {
		return webImageObjectChoice;
	}

	@NotNull
	public Label getPathImageObjectChoice() {
		return pathImageObjectChoice;
	}

	@NotNull
	@Override
	protected Canvas getMainCanvas() {
		return mainCanvas;
	}

	@NotNull
	@Override
	protected ScrollPane getMainView() {
		return mainView;
	}
}
