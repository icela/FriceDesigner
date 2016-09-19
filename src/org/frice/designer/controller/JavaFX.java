package org.frice.designer.controller;

import com.eldath.alerts.InfoAlert;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Accordion;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import org.frice.designer.code.CodeData;
import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.util.ResourceBundle;

public class JavaFX extends Controller implements Initializable {

	public Accordion widgetsList;

	public Label ovalObjectChoice;

	public Label rectangleObjectChoice;
	public Label webImageObjectChoice;
	public Label pathImageObjectChoice;

	public Label simpleTextChoice;

	public Label projectName;

	public Canvas mainCanvas;
	public ScrollPane mainView;

	public TextField boxX;
	public TextField boxY;
	public TextField boxWidth;
	public TextField boxHeight;
	public TextField boxSource;
	public TextField boxFieldName;
	public TextField boxColor;

	@NotNull
	@Override
	public Accordion getWidgetsList() {
		return widgetsList;
	}

	public void onMenuNewClicked(ActionEvent event) {
		super.onMenuNew();
	}

	public void onMenuOpenClicked(ActionEvent event) {
		super.onMenuOpen();
	}

	public void onMenuExitClicked(ActionEvent event) {
		super.onMenuExit();
	}

	public void onMenuSaveClicked(ActionEvent event) {
		super.onMenuSave();
	}

	public void onMenuCloseClicked(ActionEvent event) {
	}

	public void onMenuSaveAsClicked(ActionEvent event) {
		super.onMenuSave();
	}

	public void onMenuPreferenceClicked(ActionEvent event) {
	}

	public void onMenuAboutClicked(ActionEvent event) {
		super.onMenuAboutClicked();
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


	public void onMenuToolsCompileClicked(ActionEvent event) {
	}

	@NotNull
	public Label getOvalObjectChoice() {
		return ovalObjectChoice;
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

	@NotNull
	@Override
	public TextField getBoxX() {
		return boxX;
	}

	@NotNull
	@Override
	public TextField getBoxY() {
		return boxY;
	}

	@NotNull
	@Override
	public TextField getBoxWidth() {
		return boxWidth;
	}

	@NotNull
	@Override
	public TextField getBoxHeight() {
		return boxHeight;
	}

	@NotNull
	@Override
	public TextField getBoxSource() {
		return boxSource;
	}

	@NotNull
	@Override
	public TextField getBoxFieldName() {
		return boxFieldName;
	}

	@NotNull
	@Override
	public Label getSimpleTextChoice() {
		return simpleTextChoice;
	}

	@NotNull
	@Override
	public TextField getBoxColor() {
		return boxColor;
	}

	@NotNull
	@Override
	public Label getRectangleObjectChoice() {
		return rectangleObjectChoice;
	}

}
