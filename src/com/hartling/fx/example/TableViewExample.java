package com.hartling.fx.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TableViewExample extends Application {

	@Override
	public void start(Stage primaryStage) {

		TableView<Person> tableView = new TableView<>();

//        TableColumn<String, Person> column1 = new TableColumn<>("First Name");
		TableColumn<Person, String> column1 = new TableColumn<>("First Name");
		column1.setCellValueFactory(new PropertyValueFactory<>("firstName"));

		TableColumn<Person, String> column2 = new TableColumn<>("Last Name");
		column2.setCellValueFactory(new PropertyValueFactory<>("lastName"));

		tableView.getColumns().add(column1);
		tableView.getColumns().add(column2);

		tableView.getItems().add(new Person("John", "Doe"));
		tableView.getItems().add(new Person("Jane", "Deer"));

		VBox vbox = new VBox(tableView);

		Scene scene = new Scene(vbox);

		primaryStage.setScene(scene);

		primaryStage.show();
	}

}
