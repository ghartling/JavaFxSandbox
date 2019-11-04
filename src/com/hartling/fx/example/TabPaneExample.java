package com.hartling.fx.example;

import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

public class TabPaneExample extends Application {
	// counter of tabs
	int counter = 0;

	// launch the application
	public void start(Stage stage) {

		// set title for the stage
		stage.setTitle("Creating Tab");

		// create a tabpane
		TabPane tabpane = new TabPane();

		for (int i = 0; i < 5; i++) {

			// create Tab
			Tab tab = new Tab("Tab_" + (int) (counter + 1));

			// create a label
			Label label = new Label("This is Tab: " + (int) (counter + 1));

			counter++;

			// add label to the tab
			tab.setContent(label);

			// add tab
			tabpane.getTabs().add(tab);
		}

		// create a tab which
		// when pressed creates a new tab
		Tab newtab = new Tab();

		// action event
		EventHandler<Event> event = new EventHandler<Event>() {

			public void handle(Event e) {
				System.out.println("handle: " + e.getSource().getClass().getSimpleName());
				System.out.println("handle: " + e.getTarget().getClass().getSimpleName());

				if (newtab.isSelected()) {

					// create Tab
					Tab tab = new Tab("Tab_" + (int) (counter + 1));

					// create a label
					Label label = new Label("This is Tab: " + (int) (counter + 1));

					counter++;

					// add label to the tab
					tab.setContent(label);

					// add tab
					tabpane.getTabs().add(tabpane.getTabs().size() - 1, tab);

					// select the last tab
					tabpane.getSelectionModel().select(tabpane.getTabs().size() - 2);
				}
			}
		};

		// set event handler to the tab
		newtab.setOnSelectionChanged(event);

		// add newtab
		tabpane.getTabs().add(newtab);

		// create a scene
		Scene scene = new Scene(tabpane, 600, 500);

		// set the scene
		stage.setScene(scene);

		stage.show();
	}

	// Main Method
	public static void main(String args[]) {

		// launch the application
		launch(args);
	}
}
