package com.hartling.fx;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.apache.log4j.Logger;

import com.hartling.app.weather.entity.GhcndDailyView;
import com.hartling.app.weather.service.ObservedWeatherService;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GhcndDailyViewTableView extends Application {
	private static final Logger logger = Logger.getLogger(GhcndDailyViewTableView.class);

	// create the table
	private TableView<GhcndDailyView> tableView = new TableView<>();

	public static void main(String[] args) {
		Application.launch(GhcndDailyViewTableView.class);
	}

	@Override
	public void start(Stage primaryStage) {

		String fields[] = { "stationId", "icaoId", "occurDate", "tmax", "tmin", "prcp", "snow", "normalMax", "normalMin" };

		// add the columns
		int i = 0;
		for (String s : fields) {
			TableColumn<GhcndDailyView, String> column1 = new TableColumn<>(fields[i]);
			column1.setCellValueFactory(new PropertyValueFactory<>(fields[i]));

			tableView.getColumns().add(column1);
			++i;
		}

		tableView.setPrefWidth(840);

		// layout
		BorderPane border = new BorderPane();

		// fields at top
		Label testlabel = new Label("test");
		HBox hbox = addHBox(testlabel);

		// table
		VBox vbox = new VBox(tableView);
		vbox.getChildren().add(testlabel);
		vbox.setPadding(new Insets(10)); // Set all sides to 10
		vbox.setSpacing(10); // Gap between nodes
		border.setTop(hbox);
		border.setLeft(vbox);

		// scene
//		Scene scene = new Scene(border, 900, 600);
		Scene scene = new Scene(border);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Weather");

		primaryStage.show();
	}

	public HBox addHBox(Label label) {
		HBox hbox = new HBox();
		hbox.setPadding(new Insets(15, 12, 15, 12));
		hbox.setSpacing(10);
		hbox.setStyle("-fx-background-color: #A0D9E9;");

		// create a label
		Label stnLabel = new Label("STN:");
		TextField stnField = createTextField(hbox, stnLabel);
		Label startDateLabel = new Label("Start:");
		DatePicker startDatePicker = createDatePicker(stnLabel);
		Label endDateLabel = new Label("End:");
		DatePicker endDatePicker = createDatePicker(stnLabel);

		Button goButton = new Button("Go");
		goButton.setPrefSize(100, 20);

		// action event
		EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				LocalDate start = startDatePicker.getValue();
				LocalDate end = endDatePicker.getValue();

				label.setText(stnField.getText() + ", " + start + ", " + end);

				List<GhcndDailyView> data = new ArrayList<>();
				try {
					tableView.getItems().clear();
					data = getData(stnField.getText());
				} catch (ClientProtocolException e1) {
					logger.error(e1);
				} catch (IOException e1) {
					logger.error(e1);
				}
				tableView.getItems().addAll(data);

			}
		};

		goButton.setOnAction(event);

		// add fields
		hbox.getChildren().addAll(stnLabel, stnField, startDateLabel, startDatePicker, endDateLabel, endDatePicker, goButton);

		return hbox;
	}

	private DatePicker createDatePicker(Label l) {
		// create a date picker
		DatePicker d = new DatePicker();

		// action event
		EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				// get the date picker value
				LocalDate i = d.getValue();

				// get the selected date
				l.setText("Date :" + i);
			}
		};

		// show week numbers
		d.setShowWeekNumbers(true);

		// when datePicker is pressed
		d.setOnAction(event);

		return d;
	}

	private List<GhcndDailyView> getData(String stnId) throws ClientProtocolException, IOException {
		ObservedWeatherService weatherService = new ObservedWeatherService();
		List<GhcndDailyView> ghcndMapped = weatherService.findUsinfoByStn(stnId);

		return ghcndMapped;
	}

	private TextField createTextField(HBox hbox, Label l) {
		// create a textfield
		TextField b = new TextField("KBOS");

		// action event
		EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				l.setText(b.getText());
			}
		};

		// when enter is pressed
		b.setOnAction(event);

		return b;
	}

}
