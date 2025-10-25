module LibraProject {
	 exports Controllers;
	requires javafx.controls;
	requires javafx.graphics;
	requires javafx.fxml;
	requires java.sql;
	requires java.desktop;
	requires javafx.base;
	
	opens application to javafx.graphics, javafx.fxml;
	 opens Controllers to javafx.fxml;
	 opens Model;
	
}
