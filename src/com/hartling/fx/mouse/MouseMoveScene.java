package com.hartling.fx.mouse;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.security.SecureRandom;
import java.util.Random;

import org.apache.log4j.Logger;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;

public class MouseMoveScene {
	private static final Logger logger = Logger.getLogger(MouseMoveScene.class);

	private static boolean moveMouse = false;

	private SimpleStringProperty statusProperty = new SimpleStringProperty(getStatusText());

	public Scene build() {

		// scene
		HBox mainHbox = new HBox();
		Scene scene = new Scene(mainHbox);
		VBox stationHbox = buildFieldsInColumns(scene);

		VBox vbox = new VBox(stationHbox);
		vbox.setPadding(new Insets(10)); // Set all sides to 10
		vbox.setSpacing(10); // Gap between nodes
		mainHbox.setMinSize(270, 240);

		// add to main window
		mainHbox.getChildren().add(vbox);
		return scene;
	}

	public VBox buildVBox() {
		VBox v = new VBox();
		v.setPadding(new Insets(0, 10, 2, 0));
		v.setSpacing(10);
		return v;
	}

	public HBox buildHBox() {
		HBox hbox = new HBox();
		hbox.setPadding(new Insets(5, 10, 5, 10));
		hbox.setSpacing(10);
		// hbox.setStyle("-fx-background-color: #A0D9E9;");

		return hbox;
	}

	public VBox buildFieldsInColumns(Scene scene) {
		VBox containerVbox = buildVBox();
		HBox hbox = buildHBox();

		VBox leftHBox = buildVBox();
		leftHBox.setAlignment(Pos.CENTER_LEFT);
		VBox rightHBox = buildVBox();
		rightHBox.setAlignment(Pos.CENTER_RIGHT);

		// create X and Y label and field
		Label xLabel = new Label("X:");
		Label xField = new Label();
		leftHBox.getChildren().add(xLabel);
		rightHBox.getChildren().add(xField);

		Label yLabel = new Label("Y:");
		Label yField = new Label();
		leftHBox.getChildren().add(yLabel);
		rightHBox.getChildren().add(yField);

		// create screen width fields
		Rectangle2D screenBounds = Screen.getPrimary().getBounds();
		System.out.println(screenBounds);

		Label screenWidthLabel = new Label("Screen Width:");
		Label screenWidthField = new Label(String.format("%7.0f", screenBounds.getMaxX()));
		leftHBox.getChildren().add(screenWidthLabel);
		rightHBox.getChildren().add(screenWidthField);

		Label screenHeightLabel = new Label("Screen Height:");
		Label screenHeightField = new Label(String.format("%7.0f", screenBounds.getMaxY()));
		leftHBox.getChildren().add(screenHeightLabel);
		rightHBox.getChildren().add(screenHeightField);

		Label statusLabel = new Label("Status:");
		Label statusField = new Label(getStatusText());
		statusField.textProperty().bind(statusProperty);
		leftHBox.getChildren().add(statusLabel);
		rightHBox.getChildren().add(statusField);

		// button
		Button goButton = createGoButton();
		Button stopButton = createStopButton();
		HBox buttonHBox = buildHBox();
		buttonHBox.getChildren().addAll(goButton, stopButton);

		// add fields
		hbox.getChildren().addAll(leftHBox, rightHBox);
		containerVbox.getChildren().addAll(hbox, buttonHBox);

		EventHandler<MouseEvent> event = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				xField.setText(formatScreenPos(mouseEvent.getScreenX()));
				yField.setText(formatScreenPos(mouseEvent.getScreenY()));

				// create screen width fields
				Rectangle2D screenBounds = Screen.getPrimary().getBounds();
				screenWidthField.setText(formatScreenPos(screenBounds.getMaxX()));
				screenHeightField.setText(formatScreenPos(screenBounds.getMaxY()));
			}
		};
		scene.addEventFilter(MouseEvent.MOUSE_MOVED, event);

		/**
		 * Background task for moving the mouse
		 */
		Thread taskThread = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {

					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					if (moveMouse) {
						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								moveMouseRandomly(screenBounds.getMaxY(), screenBounds.getMaxY());
							}
						});
					}
				}
			}
		});

		taskThread.start();

		return containerVbox;
	}

	public String getStatusText() {
		return String.format("%s", moveMouse ? "running" : "not running");
	}

	public void moveMouseGradually() {
		try {
			Robot r = new Robot();
			int xi1, yi1, xi, yi;

			// get initial loction
			Point p = MouseInfo.getPointerInfo().getLocation();
			xi = p.x;
			yi = p.y;

			System.out.println("x,y: " + xi + "," + yi);

			// move to a hard coded x,y
			xi1 = 1000;
			yi1 = 1000;
			int i = xi, j = yi;

			// slowly move the mouse to detined location
			while (i != xi1 || j != yi1) {
				// move the mouse to the other point
				r.mouseMove(i, j);

				if (i < xi1)
					i++;
				if (j < yi1)
					j++;

				if (i > xi1)
					i--;
				if (j > yi1)
					j--;

				// wait
				// Thread.sleep(30);
			}
		} catch (Exception evt) {
			System.err.println(evt.getMessage());
		}

	}

	/**
	 * Randomly moves the mouse to an x,y point
	 * 
	 * @param maxWidth
	 * @param maxHeight
	 */
	public void moveMouseRandomly(double maxWidth, double maxHeight) {
		try {
			Robot r = new Robot();
			int xDest, yDest, xi, yi;

			// get initial loction
			Point p = MouseInfo.getPointerInfo().getLocation();
			xi = p.x;
			yi = p.y;

			System.out.println("start x,y: " + xi + "," + yi);

			Random random = new SecureRandom();
			xDest = random.nextInt((int) maxWidth);
			yDest = random.nextInt((int) maxHeight);
			System.out.println("end x,y: " + xDest + "," + yDest);

			r.mouseMove(xDest, yDest);
		} catch (Exception evt) {
			System.err.println(evt.getMessage());
		}

	}

	public String formatScreenPos(double value) {
		return String.format("%7.0f", value);
	}

	/**
	 * Create the button for starting the mouse movement
	 * 
	 * @return
	 */
	private Button createGoButton() {
		Button goButton = new Button("Go");
		goButton.setPrefSize(100, 20);

		// event handler for go button
		EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				moveMouse = true;
				statusProperty.set(getStatusText());
			}
		};

		goButton.setOnAction(event);

		return goButton;
	}

	private Button createStopButton() {
		Button goButton = new Button("Stop");
		goButton.setPrefSize(100, 20);

		// event handler for go button
		EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				moveMouse = false;
				statusProperty.set(getStatusText());
			}
		};

		goButton.setOnAction(event);

		return goButton;
	}
}
