package com.hartling.fx.mouse;

import org.apache.log4j.Logger;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MouseMoveApp extends Application {
	private static final Logger logger = Logger.getLogger(MouseMoveApp.class);

	@Override
	public void start(Stage primaryStage) {

		// scene
		MouseMoveScene mouseMoveScene = new MouseMoveScene();
		Scene scene = mouseMoveScene.build();

		// main window
		primaryStage.setScene(scene);
		primaryStage.setTitle("Mouse Move");
		primaryStage.show();
	}

}
