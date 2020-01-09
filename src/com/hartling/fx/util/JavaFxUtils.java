package com.hartling.fx.util;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class JavaFxUtils {

	/**
	 * display an error dialog
	 * 
	 * @param msg
	 */
	public static void displayError(String msg) {
		var alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText("Input Error");
		alert.setContentText(msg);
		alert.showAndWait().ifPresent((btnType) -> {
		});
	}

}
