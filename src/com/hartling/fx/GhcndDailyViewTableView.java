package com.hartling.fx;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.log4j.Logger;

import com.hartling.app.weather.entity.GhcndDailyView;
import com.hartling.app.weather.service.ObservedWeatherService;
import com.hartling.fx.controls.AutoCompleteTextField;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
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

		// label for debugging
		Label testlabel = new Label("test");

		// input fields
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

		// main window
		primaryStage.setScene(scene);
		primaryStage.setTitle("Weather");
		primaryStage.show();
	}

	/**
	 * Builds the HBox which contains the stnId, start date and end date
	 * 
	 * @param testlabel
	 * @return
	 */
	public HBox addHBox(Label testlabel) {
		HBox hbox = new HBox();
		hbox.setPadding(new Insets(15, 12, 15, 12));
		hbox.setSpacing(10);
		hbox.setStyle("-fx-background-color: #A0D9E9;");

		// create stn label and field
		Label stnLabel = new Label("STN:");
//		TextField stnField = createTextField(hbox);
		TextField stnField = createAutoCompleteTextField();

		Label startDateLabel = new Label("Start:");
		DatePicker startDatePicker = createDatePicker(stnLabel);
		Label endDateLabel = new Label("End:");
		DatePicker endDatePicker = createDatePicker(stnLabel);

		Button goButton = createGoButton(startDatePicker, endDatePicker, stnField, testlabel);

		// add fields
		hbox.getChildren().addAll(stnLabel, stnField, startDateLabel, startDatePicker, endDateLabel, endDatePicker, goButton);

		return hbox;
	}

	private Button createGoButton(DatePicker startDatePicker, DatePicker endDatePicker, TextField stnField, Label testlabel) {
		Button goButton = new Button("Go");
		goButton.setPrefSize(100, 20);

		// event handler for go button
		EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				LocalDate start = startDatePicker.getValue();
				LocalDate end = endDatePicker.getValue();
				String stnId = stnField.getText();

				if (StringUtils.isNotEmpty(stnId) && stnId.length() >= 3) {
					// verify that the start date is before the end date
					if (start != null && end != null && start.isBefore(end)) {

						stnField.setText(stnId.toUpperCase());
						testlabel.setText(stnField.getText() + ", " + start + ", " + end);

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

					} else {
						displayError("Start Date isn't before the End Date");
					}
				} else {
					displayError("Station ID must be at least 3 characters");
				}

			}
		};

		goButton.setOnAction(event);

		return goButton;
	}

	/**
	 * display an error dialog
	 * 
	 * @param msg
	 */
	private void displayError(String msg) {
		var alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText("Input Error");
		alert.setContentText(msg);
		alert.showAndWait().ifPresent((btnType) -> {
		});
	}

	private DatePicker createDatePicker(Label l) {
		// create a date picker
		DatePicker d = new DatePicker();

		// show week numbers
		d.setShowWeekNumbers(true);

		return d;
	}

	/**
	 * Get the daily data with normals
	 * 
	 * @param stnId
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	private List<GhcndDailyView> getData(String stnId) throws ClientProtocolException, IOException {
		ObservedWeatherService weatherService = new ObservedWeatherService();
		List<GhcndDailyView> ghcndMapped = weatherService.findUsinfoByStn(stnId);

		return ghcndMapped;
	}

	private TextField createTextField(HBox hbox) {
		// create a textfield
		TextField b = new TextField("KBOS");

		return b;
	}

	private AutoCompleteTextField<String> createAutoCompleteTextField() {
		SortedSet<String> s = new TreeSet<>();
		s.addAll(Arrays.asList("KBOS", "KMCO", "KMIA"));

		// create a textfield
		AutoCompleteTextField<String> tf = new AutoCompleteTextField<>(s);
		tf.getEntryMenu().setOnAction(e -> {
			((MenuItem) e.getTarget()).addEventHandler(Event.ANY, event -> {
				if (tf.getLastSelectedObject() != null) {
					tf.setText(tf.getLastSelectedObject().toString());
					System.out.println(tf.getLastSelectedObject().toString());
				}
			});
		});

		return tf;
	}

}
