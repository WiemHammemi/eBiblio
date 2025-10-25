package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;


public class Main extends Application {
	@Override
	public void start(Stage stage) {
		try {
			ConnexionDb.Connect();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/Scenes/Dashboard.fxml"));
			
			Parent root = loader.load();
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/CSS/application.css").toExternalForm());
			Image icon = new Image(getClass().getResourceAsStream("/images/book.png"));
			stage.setResizable(false);
			stage.getIcons().add(icon);
			stage.setTitle("eBiblio");
			stage.setScene(scene);
			stage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) {
		launch(args);
	}
}
