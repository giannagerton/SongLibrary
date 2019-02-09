package application;
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;


public class Main extends Application {
	
	private static GridPane makeGridPane() {
		// all the widgets
		Text fText = new Text("Fahrenheit");
		Text cText = new Text("Celsius");
		TextField f = new TextField();
		TextField c = new TextField();
		Button f2c = new Button(">>>");
		Button c2f = new Button("<<<");
		GridPane gridPane = new GridPane();
		gridPane.add(fText, 0, 0);
		gridPane.add(f2c, 1, 0);
		gridPane.add(cText, 2, 0);
		gridPane.add(f, 0, 1);
		gridPane.add(c2f, 1, 1);
		gridPane.add(c, 2, 1);
		 return gridPane;
		} 
	@Override
	
	/*public void start(Stage primaryStage) {
		try {
			BorderPane root = new BorderPane();
			Scene scene = new Scene(root,400,400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}*/
	public void start(Stage primaryStage) {
		GridPane root = makeGridPane();
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show();
		}
	
	public static void main(String[] args) {
		launch(args);
	}
}