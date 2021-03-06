package org.frice.designer.controller;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import org.frice.designer.code.CodeData;
import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * simply an interface of java and kotlin.
 * JavaFX works awful with kotlin but java.
 */
public class JavaFX extends Controller implements Initializable {

	public Accordion widgetsList;

	public Label ovalObjectChoice;

	public Label rectangleObjectChoice;
	public Label webImageObjectChoice;
	public Label pathImageObjectChoice;

	public Label simpleButtonChoice;
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
	public TextArea messageBox;
	public MenuItem menuSave;

	@NotNull
	@Override
	public Accordion getWidgetsList() {
		return widgetsList;
	}

	public void onMenuNewClicked(@NotNull ActionEvent event) {
		super.onMenuNew();
	}

	public void onMenuOpenClicked(@NotNull ActionEvent event) {
		super.onMenuOpen();
	}

	public void onMenuExitClicked(@NotNull ActionEvent event) {
		super.onMenuExit();
	}

	public void onMenuSaveClicked(@NotNull ActionEvent event) {
		super.onMenuSave();
	}

	public void onMenuCloseClicked(@NotNull ActionEvent event) {
	}

	public void onMenuSaveAsClicked(@NotNull ActionEvent event) {
		super.onMenuSaveAs();
	}

	public void onMenuPreferenceClicked(@NotNull ActionEvent event) {
		super.onMenuPreference();
	}

	public void onMenuAboutClicked(@NotNull ActionEvent event) {
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

	public void onMenuJavaCodeClicked(@NotNull ActionEvent event) {
		showCode(CodeData.LANGUAGE_JAVA);
	}

	public void onMenuKotlinCodeClicked(@NotNull ActionEvent event) {
		showCode(CodeData.LANGUAGE_KOTLIN);
	}

	public void onMenuScalaCodeClicked(@NotNull ActionEvent event) {
		showCode(CodeData.LANGUAGE_SCALA);
	}

	public void onMenuToolsCompileClicked(@NotNull ActionEvent event) {
		super.onMenuToolsCompileClicked();
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

	@NotNull
	@Override
	public TextArea getMessageBox() {
		return messageBox;
	}

	@NotNull
	@Override
	public MenuItem getMenuSave() {
		return menuSave;
	}

	@NotNull
	@Override
	public Label getSimpleButtonChoice() {
		return simpleButtonChoice;
	}

	public void onMenuDeleteClicked(@NotNull ActionEvent event) {
		super.onMenuDeleteClicked();
	}

	public void onMenuToolsExportFileClicked(@NotNull ActionEvent event) {
		super.onMenuToolsExportFileClicked();
	}

	public void onMenuToolsJarClicked(@NotNull ActionEvent event) {
		super.onMenuToolsJarClicked();
	}

	public void onMenuGroovyCodeClicked(@NotNull ActionEvent event) {
		showCode(CodeData.LANGUAGE_GROOVY);
	}

	public void onMenuToolsTestClicked(@NotNull ActionEvent event) {
		super.compileAndRun();
	}

	public void onMenuRefreshViewClicked(@NotNull ActionEvent event) {
	}
}
