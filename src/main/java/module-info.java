module LogAggregator {
	requires javafx.fxml;
	requires javafx.controls;


	opens com.github.PavelKisliuk.controller to javafx.fxml;
	exports com.github.PavelKisliuk.controller to javafx.fxml;

	opens fxml to javafx.fxml;
	exports com.github.PavelKisliuk;
}