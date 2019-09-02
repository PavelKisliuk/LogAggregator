package com.github.PavelKisliuk.controller;

import com.github.PavelKisliuk.util.LogStatistic.TimePeriodType;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class InputController {

	@FXML
	private DatePicker dateFromDatePicker;

	@FXML
	private ComboBox<String> hourFromComboBox;

	@FXML
	private ComboBox<String> minuteFromComboBox;

	@FXML
	private DatePicker dateTillDatePicker;

	@FXML
	private ComboBox<String> hourTillComboBox;

	@FXML
	private ComboBox<String> minuteTillComboBox;

	@FXML
	private TextField usernameTextField;

	@FXML
	private TextField messageTextField;

	@FXML
	private CheckBox nameStatisticCheckBox;

	@FXML
	private CheckBox timeStatisticCheckBox;

	@FXML
	private ComboBox<TimePeriodType> periodComboBox;

	@FXML
	private TextField threadAmountTextField;

	@FXML
	private TextField outputPathTextField;

	@FXML
	private TextField logPathTextField;

	@FXML
	private Label infoLabel;

	@FXML
	private Button startButton;

	private boolean isCancel;

	private LocalDate from;
	private LocalDate till;

	private String userName;
	private LocalDateTime dateBefore;
	private LocalDateTime dateAfter;
	private Pattern pattern;

	public boolean isCancel() {
		return isCancel;
	}

	public String getUserName() {
		return userName;
	}

	public LocalDateTime getDateBefore() {
		return dateBefore;
	}

	public LocalDateTime getDateAfter() {
		return dateAfter;
	}

	public Pattern getPattern() {
		return pattern;
	}

	public boolean isNameStatistic() {
		return nameStatisticCheckBox.isSelected();
	}

	public boolean isTimeStatistic() {
		return timeStatisticCheckBox.isSelected();
	}

	public TimePeriodType getTimePeriod() {
		return periodComboBox.getValue();
	}

	public int getAmountOfThread() {
		return Integer.parseInt(threadAmountTextField.getText());
	}

	public String getOutputPath() {
		return outputPathTextField.getText();
	}

	public String getPathToLog() {
		return logPathTextField.getText();
	}

	@FXML
	void initialize() {
		isCancel = true;
		List<String> hList = IntStream.range(0, 24).mapToObj(this::initHours).collect(Collectors.toList());
		hourFromComboBox.setItems(FXCollections.observableArrayList(hList));
		hourTillComboBox.setItems(FXCollections.observableArrayList(hList));

		List<String> mList = IntStream.range(0, 60).mapToObj(this::initMinutes).collect(Collectors.toList());
		minuteFromComboBox.setItems(FXCollections.observableArrayList(mList));
		minuteTillComboBox.setItems(FXCollections.observableArrayList(mList));

		dateFromDatePicker.valueProperty().addListener((ov, oldValue, newValue) -> {
			if (till != null &&
					newValue != null &&
					newValue.compareTo(till) > 0) {
				dateFromDatePicker.setValue(oldValue);
			}
			from = dateFromDatePicker.getValue();
			if (from != null) {
				setHourMinute(hourFromComboBox, minuteFromComboBox);
			} else {
				hourFromComboBox.setDisable(true);
				minuteFromComboBox.setDisable(true);
			}
		});

		dateTillDatePicker.valueProperty().addListener((ov, oldValue, newValue) -> {
			if (from != null &&
					newValue != null &&
					newValue.compareTo(from) < 0) {
				dateTillDatePicker.setValue(oldValue);
			}
			till = dateTillDatePicker.getValue();
			if (till != null) {
				setHourMinute(hourTillComboBox, minuteTillComboBox);
			} else {
				hourTillComboBox.setDisable(true);
				minuteTillComboBox.setDisable(true);
			}
		});

		periodComboBox.setItems(FXCollections.observableArrayList(TimePeriodType.values()));
		periodComboBox.setValue(TimePeriodType.DAY);

		timeStatisticCheckBox.setOnAction(actionEvent -> timeStatisticOnAction());

		threadAmountTextField.textProperty().addListener((obs, oldVal, newVal) -> {
			if (!isDigit(newVal)) {
				threadAmountTextField.setText(oldVal);
			}
			if (threadAmountTextField.getText().isBlank()) {
				threadAmountTextField.setText("1");
			}
			if (threadAmountTextField.getText().charAt(0) == '0') {
				threadAmountTextField.setText(oldVal);
			}
		});

		infoLabel.setTextFill(Color.RED);
		startButton.setOnAction(actionEvent -> startOnAction());
	}

	private String initHours(Integer hour) {
		return (hour >= 10) ? String.valueOf(hour) : "0" + hour;
	}

	private String initMinutes(Integer minute) {
		return (minute >= 10) ? String.valueOf(minute) : "0" + minute;
	}

	private void timeStatisticOnAction() {
		if (!timeStatisticCheckBox.isSelected()) {
			periodComboBox.setDisable(true);
			periodComboBox.setValue(TimePeriodType.DAY);
		} else {
			periodComboBox.setDisable(false);
		}
	}

	private void setHourMinute(ComboBox<String> hour, ComboBox<String> minute) {
		hour.setDisable(false);
		minute.setDisable(false);
		hour.setValue("00");
		minute.setValue("00");
	}

	private boolean isDigit(String s) {
		for (char ch : s.toCharArray()) {
			if (!Character.isDigit(ch)) {
				return false;
			}
		}
		return true;
	}

	private void startOnAction() {
		boolean flag = true;
		StringBuilder stringBuilder = new StringBuilder();
		if (isFilterParametersNotSpecify()) {
			stringBuilder.append("At least one filter parameter should be specified.\n");
			flag = false;
		}
		if (!nameStatisticCheckBox.isSelected() &&
				!timeStatisticCheckBox.isSelected()) {
			stringBuilder.append("At least one grouping parameter should be selected.\n");
			flag = false;
		}
		if (outputPathTextField.getText().isBlank()) {
			stringBuilder.append("Output path has to established.\n");
			flag = false;
		}
		if (logPathTextField.getText().isBlank()) {
			stringBuilder.append("Path to log's has to established.\n");
			flag = false;
		}
		if (!flag) {
			infoLabel.setText(stringBuilder.toString());
		} else {
			infoLabel.setText("");
			if (dateFromDatePicker.getValue() != null) {
				dateBefore =
						LocalDateTime.of(
								dateFromDatePicker.getValue(),
								LocalTime.of(
										Integer.parseInt(hourFromComboBox.getValue()),
										Integer.parseInt(minuteFromComboBox.getValue())));
			}
			if (dateTillDatePicker.getValue() != null) {
				dateAfter =
						LocalDateTime.of(
								dateTillDatePicker.getValue(),
								LocalTime.of(
										Integer.parseInt(hourTillComboBox.getValue()),
										Integer.parseInt(minuteTillComboBox.getValue())));
			}
			if (!usernameTextField.getText().isBlank()) {
				userName = usernameTextField.getText();
			}
			if (!messageTextField.getText().isBlank()) {
				pattern = Pattern.compile(messageTextField.getText());
			}
			isCancel = false;
			Stage stage = (Stage) startButton.getScene().getWindow();
			stage.close();
		}
	}

	private boolean isFilterParametersNotSpecify() {
		return dateFromDatePicker.getValue() == null &&
				dateTillDatePicker.getValue() == null &&
				usernameTextField.getText().isBlank() &&
				messageTextField.getText().isBlank();
	}
}