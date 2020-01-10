package com.hartling.fx.scene.tab;

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
import com.hartling.app.weather.service.ObservedWeatherService;
import com.hartling.fx.controls.AutoCompleteTextField;
import com.hartling.fx.util.JavaFxUtils;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class RegionTab {
	private static final Logger logger = Logger.getLogger(RegionTab.class);

	private SortedSet<String> regionList;
	private String selectedRegionId;
	private TableView<GhcndDailyNormalDetail> regionTableView = new TableView<>();

	public VBox buildRegionTab(Label testlabel) {
		// build the region list
		regionList = loadRegionList();
		logger.info("number of region = " + regionList.size());

		// station daily data tab
		VBox stationHbox = new VBox();

		// input fields
		HBox inputHbox = buildRegionInputHBox(testlabel);
		stationHbox.getChildren().add(inputHbox);

		// table of results
		stationHbox.getChildren().add(regionTableView);

		return stationHbox;
	}

	public void buildTableColumns() {
		String fields[] = { "stationId", "icaoId", "occurDate", "tmax", "tmin", "prcp", "snow", "normalMax", "normalMin" };

		// add the columns
		int i = 0;
		for (String s : fields) {
			TableColumn<GhcndDailyNormalDetail, String> column1 = new TableColumn<>(fields[i]);
			column1.setCellValueFactory(new PropertyValueFactory<>(fields[i]));

			regionTableView.getColumns().add(column1);
			++i;
		}

		regionTableView.setPrefWidth(840);
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

	private DatePicker createDatePicker(Label l, LocalDate localDate) {
		// create a date picker
		DatePicker d = new DatePicker(localDate);

		// show week numbers
		d.setShowWeekNumbers(true);

		return d;
	}

	public Button createRegionGoButton(DatePicker startDatePicker, TextField regionField, Label testlabel) {
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
						JavaFxUtils.displayError("Start Date required");
					}
				} else {
					JavaFxUtils.displayError("Region ID must be at least 3 characters");
				}

			}
		};

		goButton.setOnAction(event);

		return goButton;
	}

	private List<GhcndDailyNormalDetail> findByRegionIdAndOccurDate(String regionId, String occurDate) throws ClientProtocolException, IOException {
		ObservedWeatherService weatherService = new ObservedWeatherService();
		List<GhcndDailyNormalDetail> ghcndMapped = weatherService.findByRegionIdAndOccurDate(regionId, occurDate);

		return ghcndMapped;
	}

	public SortedSet<String> loadRegionList() {
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
			JavaFxUtils.displayError("unable to parse JSON for the region ID list");
		} catch (JsonMappingException e) {
			logger.error(e);
			JavaFxUtils.displayError("unable to parse JSON for the region ID list");
		} catch (IOException e) {
			logger.error(e);
			JavaFxUtils.displayError("unable to retrieve the region ID list");
		}

		return sortedSet;
	}

}
