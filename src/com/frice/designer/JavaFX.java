package com.frice.designer;

import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;

public class JavaFX extends Controller {

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

	public void onMainViewClicked(MouseEvent event) {
		super.onMainViewClicked();
	}
}
