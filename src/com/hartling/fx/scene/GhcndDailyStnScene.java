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
import com.hartling.app.common.util.CommonUtils;
import com.hartling.app.weather.json.GhcndDailyNormalDetail;
import com.hartling.app.weather.json.GhcndDailyView;
import com.hartling.app.weather.json.StationIdMap;
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
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class GhcndDailyStnScene {
	private static final Logger logger = Logger.getLogger(GhcndDailyStnScene.class);

	// create the table
	private TableView<GhcndDailyNormalDetail> stationTableView = new TableView<>();
	private TableView<GhcndDailyNormalDetail> regionTableView = new TableView<>();
	private StationIdMap selectedStationId;
	private SortedSet<StationIdMap> stationList;
	private SortedSet<String> regionList;
	private String selectedRegionId;

	private ObservedWeatherService weatherService = new ObservedWeatherService();

	public Scene build() {
		// load the station list
		stationList = loadStationList();

		// build the region list
		regionList = loadRegionList();
		logger.info("number of region = " + regionList.size());

		// init tables
		buildTableColumns(stationTableView);
		buildTableColumns(regionTableView);

		// tabbed pane
		TabPane tabPane = new TabPane();

		Tab tab1 = new Tab("Station", new Label("Show data for a single station"));
		Tab tab2 = new Tab("Region", new Label("Show data for a single region (state)"));
		Tab tab3 = new Tab("Station Stats", new Label("Show Detailed Stats for a station"));

		tabPane.getTabs().add(tab1);
		tabPane.getTabs().add(tab2);
		tabPane.getTabs().add(tab3);

		// label for debugging
		Label testlabel = new Label("test");

		// station daily data tab
		VBox stationHbox = buildStationTab(testlabel);
		tab1.setContent(stationHbox);

		// region daily data tab
		VBox regionHbox = buildRegionTab(testlabel);
		tab2.setContent(regionHbox);

		// add tabbed pane and label for debugging
		VBox vbox = new VBox(tabPane);
		vbox.getChildren().add(testlabel);
		vbox.setPadding(new Insets(10)); // Set all sides to 10
		vbox.setSpacing(10); // Gap between nodes

		// add to main window
		HBox mainHbox = new HBox(vbox);

		// scene
		return new Scene(mainHbox);
	}

	/**
	 * Creates the UI components for the station tab
	 * 
	 * @param testlabel
	 * @return
	 */
	private VBox buildStationTab(Label testlabel) {

		// station daily data tab
		VBox stationHbox = new VBox();

		// input fields
		HBox inputHbox = buildStationInputHBox(testlabel);
		stationHbox.getChildren().add(inputHbox);

		// table of results
		stationHbox.getChildren().add(stationTableView);

		return stationHbox;
	}

	private VBox buildRegionTab(Label testlabel) {

		// station daily data tab
		VBox stationHbox = new VBox();

		// input fields
		HBox inputHbox = buildRegionInputHBox(testlabel);
		stationHbox.getChildren().add(inputHbox);

		// table of results
		stationHbox.getChildren().add(regionTableView);

		return stationHbox;
	}

	private void buildTableColumns(TableView<GhcndDailyNormalDetail> tableView) {
		String fields[] = { "stationId", "icaoId", "occurDate", "tmax", "tmin", "prcp", "snow", "normalMax", "normalMin" };

		// add the columns
		int i = 0;
		for (String s : fields) {
			TableColumn<GhcndDailyNormalDetail, String> column1 = new TableColumn<>(fields[i]);
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
	public HBox buildStationInputHBox(Label testlabel) {
		HBox hbox = new HBox();
		hbox.setPadding(new Insets(15, 12, 15, 12));
		hbox.setSpacing(10);
		hbox.setStyle("-fx-background-color: #A0D9E9;");

		// create stn label and field
		Label stnLabel = new Label("STN:");
		TextField stnField = createStationAutoCompleteTextField();

		Label startDateLabel = new Label("Start:");
		LocalDate yesterday = LocalDate.now().minusDays(1);
		DatePicker startDatePicker = createDatePicker(stnLabel, yesterday);
		Label endDateLabel = new Label("End:");
		DatePicker endDatePicker = createDatePicker(stnLabel, yesterday);

		Button goButton = createStationGoButton(startDatePicker, endDatePicker, stnField, testlabel);

		// add fields
		hbox.getChildren().addAll(stnLabel, stnField, startDateLabel, startDatePicker, endDateLabel, endDatePicker, goButton);

		return hbox;
	}

	public HBox buildRegionInputHBox(Label testlabel) {
		HBox hbox = new HBox();
		hbox.setPadding(new Insets(15, 12, 15, 12));
		hbox.setSpacing(10);
		hbox.setStyle("-fx-background-color: #A0D9E9;");

		// create stn label and field
		Label stnLabel = new Label("Region:");
		TextField stnField = createRegionAutoCompleteTextField();

		Label occurDateLabel = new Label("Date:");
		LocalDate yesterday = LocalDate.now().minusDays(1);
		DatePicker occurDatePicker = createDatePicker(stnLabel, yesterday);

		Button goButton = createRegionGoButton(occurDatePicker, stnField, testlabel);

		// add fields
		hbox.getChildren().addAll(stnLabel, stnField, occurDateLabel, occurDatePicker, goButton);

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
	private Button createStationGoButton(DatePicker startDatePicker, DatePicker endDatePicker, TextField stnField, Label testlabel) {
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

						String startOccurDate = CommonUtils.DATE_TIME_FORMAT_YYYYMMDD.format(start);
						String endOccurDate = CommonUtils.DATE_TIME_FORMAT_YYYYMMDD.format(end);
						String stationId = selectedStationId.getStationId();

						List<GhcndDailyNormalDetail> data = new ArrayList<>();
						try {
							stationTableView.getItems().clear();
							// data = getDailyData(stnField.getText());
							data = findByStationIdAndOccurDateBetween(stationId, startOccurDate, endOccurDate);
						} catch (ClientProtocolException e1) {
							logger.error(e1);
						} catch (IOException e1) {
							logger.error(e1);
						}
						stationTableView.getItems().addAll(data);

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

	private Button createRegionGoButton(DatePicker startDatePicker, TextField regionField, Label testlabel) {
		Button goButton = new Button("Go");
		goButton.setPrefSize(100, 20);

		// event handler for go button
		EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				LocalDate start = startDatePicker.getValue();

				if (selectedRegionId != null && StringUtils.isNotEmpty(selectedRegionId)) {
					// verify that the start date is before the end date
					if (start != null) {

						regionField.setText(selectedRegionId);
						testlabel.setText(regionField.getText() + ", " + start);

						String startOccurDate = CommonUtils.DATE_TIME_FORMAT_YYYYMMDD.format(start);

						List<GhcndDailyNormalDetail> data = new ArrayList<>();
						try {
							regionTableView.getItems().clear();
							data = findByRegionIdAndOccurDate(selectedRegionId, startOccurDate);
						} catch (ClientProtocolException e1) {
							logger.error(e1);
						} catch (IOException e1) {
							logger.error(e1);
						}
						regionTableView.getItems().addAll(data);

					} else {
						displayError("Start Date required");
					}
				} else {
					displayError("Region ID must be at least 3 characters");
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
	private List<GhcndDailyView> getDailyData(String icaoId) throws ClientProtocolException, IOException {
		List<GhcndDailyView> ghcndMapped = weatherService.findUsinfoByStn(icaoId);

		return ghcndMapped;
	}

	private List<GhcndDailyNormalDetail> findByStationIdAndOccurDateBetween(String stnId, String occurDateStart, String occurDateEnd) throws ClientProtocolException, IOException {
		ObservedWeatherService weatherService = new ObservedWeatherService();
		List<GhcndDailyNormalDetail> ghcndMapped = weatherService.findByStationIdAndOccurDateBetween(stnId, occurDateStart, occurDateEnd);

		return ghcndMapped;
	}

	private List<GhcndDailyNormalDetail> findByRegionIdAndOccurDate(String regionId, String occurDate) throws ClientProtocolException, IOException {
		ObservedWeatherService weatherService = new ObservedWeatherService();
		List<GhcndDailyNormalDetail> ghcndMapped = weatherService.findByRegionIdAndOccurDate(regionId, occurDate);

		return ghcndMapped;
	}

	private SortedSet<StationIdMap> loadStationList() {
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

	private SortedSet<String> loadRegionList() {
		ObservedWeatherService weatherService = new ObservedWeatherService();
		List<String> mapped = new ArrayList<>();
		SortedSet<String> sortedSet = new TreeSet<>();
		try {
			mapped = weatherService.getRegionList();

			for (String id : mapped) {
				sortedSet.add(id);
			}
		} catch (JsonParseException e) {
			logger.error(e);
			displayError("unable to parse JSON for the region ID list");
		} catch (JsonMappingException e) {
			logger.error(e);
			displayError("unable to parse JSON for the region ID list");
		} catch (IOException e) {
			logger.error(e);
			displayError("unable to retrieve the region ID list");
		}

		return sortedSet;
	}

	private TextField createTextField(HBox hbox) {
		// create a textfield
		TextField b = new TextField("KBOS");

		return b;
	}

	private AutoCompleteTextField<StationIdMap> createStationAutoCompleteTextField() {

		// create a textfield
		AutoCompleteTextField<StationIdMap> tf = new AutoCompleteTextField<>(stationList);
		tf.getEntryMenu().setOnAction(e -> {
			((MenuItem) e.getTarget()).addEventHandler(Event.ANY, event -> {
				if (tf.getLastSelectedObject() != null) {
					tf.setText(tf.getLastSelectedObject().toString());
					selectedStationId = tf.getLastSelectedObject();
					logger.info(tf.getLastSelectedObject().toString());
				}
			});
		});

		return tf;
	}

	private AutoCompleteTextField<String> createRegionAutoCompleteTextField() {

		// create a textfield
		AutoCompleteTextField<String> tf = new AutoCompleteTextField<>(regionList);
		tf.getEntryMenu().setOnAction(e -> {
			((MenuItem) e.getTarget()).addEventHandler(Event.ANY, event -> {
				if (tf.getLastSelectedObject() != null) {
					tf.setText(tf.getLastSelectedObject().toString());
					selectedRegionId = tf.getLastSelectedObject();
					logger.info(tf.getLastSelectedObject().toString());
				}
			});
		});

		return tf;
	}

	public TableView<GhcndDailyNormalDetail> getStationTableView() {
		return stationTableView;
	}

	public void setStationTableView(TableView<GhcndDailyNormalDetail> stationTableView) {
		this.stationTableView = stationTableView;
	}

	public TableView<GhcndDailyNormalDetail> getRegionTableView() {
		return regionTableView;
	}

	public void setRegionTableView(TableView<GhcndDailyNormalDetail> regionTableView) {
		this.regionTableView = regionTableView;
	}

	public StationIdMap getSelectedStationId() {
		return selectedStationId;
	}

	public void setSelectedStationId(StationIdMap selectedStationId) {
		this.selectedStationId = selectedStationId;
	}

	public void setStationList(SortedSet<StationIdMap> stationList) {
		this.stationList = stationList;
	}

}
