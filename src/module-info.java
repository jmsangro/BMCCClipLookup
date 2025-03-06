module BMCCClipLookup {
	requires javafx.controls;
	requires javafx.fxml;
	requires com.opencsv;
	requires javafx.graphics;
	requires javafx.base;
	requires javafx.media;
	
	opens cliplookup to javafx.graphics, javafx.fxml;
}
