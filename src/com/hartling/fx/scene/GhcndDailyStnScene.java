package com.hartling.fx.scene;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.hartling.app.weather.entity.GhcndDailyView;
import com.hartling.app.weather.entity.StationIdMap;
import com.hartling.app.weather.service.ObservedWeatherService;
import com.hartling.fx.controls.AutoCompleteTextField;

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

public class GhcndDailyStnScene {
	private static final Logger logger = Logger.getLogger(GhcndDailyStnScene.class);

	// create the table
	private TableView<GhcndDailyView> tableView = new TableView<>();
	private StationIdMap selectedStationId;

	public Scene build() {

		buildTableColumns();

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
		return new Scene(border);
	}

	private void buildTableColumns() {
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
		TextField stnField = createAutoCompleteTextField();

		Label startDateLabel = new Label("Start:");
		LocalDate yesterday = LocalDate.now().minusDays(1);
		DatePicker startDatePicker = createDatePicker(stnLabel, yesterday);
		Label endDateLabel = new Label("End:");
		DatePicker endDatePicker = createDatePicker(stnLabel, yesterday);

		Button goButton = createGoButton(startDatePicker, endDatePicker, stnField, testlabel);

		// add fields
		hbox.getChildren().addAll(stnLabel, stnField, startDateLabel, startDatePicker, endDateLabel, endDatePicker, goButton);

		return hbox;
	}

	/**
	 * Create the button for requesting the data
	 * 
	 * @param startDatePicker
	 * @param endDatePicker
	 * @param stnField
	 * @param testlabel
	 * @return
	 */
	private Button createGoButton(DatePicker startDatePicker, DatePicker endDatePicker, TextField stnField, Label testlabel) {
		Button goButton = new Button("Go");
		goButton.setPrefSize(100, 20);

		// event handler for go button
		EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				LocalDate start = startDatePicker.getValue();
				LocalDate end = endDatePicker.getValue();

				if (selectedStationId != null && StringUtils.isNotEmpty(selectedStationId.getIcaoId())) {
					String stnId = selectedStationId.getIcaoId();

					// verify that the start date is before the end date
					if (start != null && end != null && (start.isBefore(end) || start.isEqual(end))) {

						stnField.setText(stnId.toUpperCase());
						testlabel.setText(stnField.getText() + ", " + start + ", " + end);

						List<GhcndDailyView> data = new ArrayList<>();
						try {
							tableView.getItems().clear();
							data = getDailyData(stnField.getText());
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

	private DatePicker createDatePicker(Label l, LocalDate localDate) {
		// create a date picker
		DatePicker d = new DatePicker(localDate);

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
	private List<GhcndDailyView> getDailyData(String stnId) throws ClientProtocolException, IOException {
		ObservedWeatherService weatherService = new ObservedWeatherService();
		List<GhcndDailyView> ghcndMapped = weatherService.findUsinfoByStn(stnId);

		return ghcndMapped;
	}

	private SortedSet<StationIdMap> getStationList() {
		ObservedWeatherService weatherService = new ObservedWeatherService();
		List<StationIdMap> mapped = new ArrayList<>();
		SortedSet<StationIdMap> s = new TreeSet<>();
		try {
			mapped = weatherService.getStationList();

			for (StationIdMap id : mapped) {
				s.add(id);
			}
		} catch (JsonParseException e) {
			logger.error(e);
			displayError("unable to parse JSON for the station ID list");
		} catch (JsonMappingException e) {
			logger.error(e);
			displayError("unable to parse JSON for the station ID list");
		} catch (IOException e) {
			logger.error(e);
			displayError("unable to retrieve the station ID list");
		}

		return s;
	}

	private TextField createTextField(HBox hbox) {
		// create a textfield
		TextField b = new TextField("KBOS");

		return b;
	}

	private AutoCompleteTextField<StationIdMap> createAutoCompleteTextField() {
		// SortedSet<String> s = new TreeSet<>();
		// s.addAll(Arrays.asList("KBOS", "KMCO", "KMIA"));

		SortedSet<StationIdMap> stationList = getStationList();
		// s.addAll(stationList);

		// create a textfield
		AutoCompleteTextField<StationIdMap> tf = new AutoCompleteTextField<>(stationList);
		tf.getEntryMenu().setOnAction(e -> {
			((MenuItem) e.getTarget()).addEventHandler(Event.ANY, event -> {
				if (tf.getLastSelectedObject() != null) {
					tf.setText(tf.getLastSelectedObject().toString());
					selectedStationId = tf.getLastSelectedObject();
					System.out.println(tf.getLastSelectedObject().toString());
				}
			});
		});

		return tf;
	}

}
