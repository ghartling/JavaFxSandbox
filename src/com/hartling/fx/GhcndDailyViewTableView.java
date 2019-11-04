package com.hartling.fx;

import org.apache.log4j.Logger;

import com.hartling.fx.scene.GhcndDailyStnScene;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GhcndDailyViewTableView extends Application {
	private static final Logger logger = Logger.getLogger(GhcndDailyViewTableView.class);

	@Override
	public void start(Stage primaryStage) {

		// scene
		GhcndDailyStnScene ghcndDailyStnScene = new GhcndDailyStnScene();
		Scene scene = ghcndDailyStnScene.build();

		// main window
		primaryStage.setScene(scene);
		primaryStage.setTitle("Weather");
		primaryStage.show();
	}

}
