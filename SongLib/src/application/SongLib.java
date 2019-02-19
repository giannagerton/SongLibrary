// Bandy Wang and Gianna Gerton

package application;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import view.Controller;

public class SongLib extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader();


			loader.setLocation(getClass().getResource("/view/view.fxml"));


			AnchorPane root = (AnchorPane)loader.load();

			Controller controller = loader.getController();
			controller.start(primaryStage);
			Scene scene = new Scene(root);

			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {

		launch(args);


	}
}