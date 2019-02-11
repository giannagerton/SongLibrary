package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.*;

public class Controller {

	@FXML TextArea songCurrent;

	//variables involving listing all songs
	private ObservableList<Song> songList;
	@FXML TableView<Song> table;
	@FXML TableColumn<Song,String> tableName;
	@FXML TableColumn<Song,String> tableArtist;
	@FXML TableColumn<Song,String> tableAlbum;
	@FXML TableColumn<Song,String> tableYear;

	//variables involving add new songs
	@FXML TextField newSongName;
	@FXML TextField newSongArtist;
	@FXML TextField newSongAlbum;
	@FXML TextField newSongYear;
	@FXML Button newSongSubmit;

	//GSON library-related variables for saving songs
	Gson gson;
	JsonParser jsonParser;
	JsonElement jsonRoot;
	JsonObject jsonObj;
	JsonArray jsonArray;

	//Miscellaneous variables
	String tempName;
	String tempArtist;
	String tempAlbum;
	String tempYear;




	public void start(Stage mainStage) throws  FileNotFoundException {

		songList = FXCollections.observableArrayList();

		gson = new Gson();
		jsonParser = new JsonParser();
		jsonRoot = jsonParser.parse(new FileReader("src/savedSongs.json"));
		jsonObj = jsonRoot.getAsJsonObject();
		jsonArray = jsonObj.getAsJsonArray("songList");

		/*for(int i = 0; i < jsonArray.size();i++) {
			songList.add(gson.fromJson(jsonArray.get(i), Song.class));
		}
		*/
		//table.setItems(songList);


		//table.getSelectionModel().select(0);


	    /*showItem(mainStage);
	    table
	       .getSelectionModel()
	       .selectedIndexProperty()
	       .addListener(
	          (obs, oldVal, newVal) ->
	              showItem(mainStage));
	     */

	}


	//INCOMPLETE
	//occurs when the submit button for a new song is pressed
	public void addNewSong(ActionEvent e){
		Button b = (Button)e.getSource();
		if(b == newSongSubmit){
			String name = newSongName.getText();
			String artist = newSongArtist.getText();
			String year = newSongYear.getText();
			String album = newSongAlbum.getText();
			
			if(name.equals("")  || artist.equals("")){
				 displayAlert("Error", "Song name and artist can not be empty!");
			}else if(!year.equals("") && !isNumber(year)){
				displayAlert("Error", "Invaild song year!");

			}else{
			
				Song newSong = new Song(name,artist,year,album);
				JsonObject jsonTemp = new JsonObject();

				jsonTemp.addProperty("name", name);
				jsonTemp.addProperty("artist", artist);
				jsonTemp.addProperty("album", album);
				jsonTemp.addProperty("artist", year);
				jsonArray.add(jsonTemp);

				updateJson();
				displayAlert("line 117","song added to json");
				
			}
		}
	}
	public  void displayAlert(String title, String content){
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(content);
		alert.showAndWait();
		
	}
	public boolean isNumber(String string){
		try{  
		    int temp = Integer.parseInt(string);  
		  }catch(NumberFormatException nfe)  {  
			  return false;  
		  }  
		return true; 
	}
	
	
	public void updateJson() {
		try(FileWriter writer = new FileWriter("src/savedSongs.json")) {
			Gson gsonBuilder = new GsonBuilder().setPrettyPrinting().create();
            gsonBuilder.toJson(jsonRoot, writer);
            writer.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
	}
	//list all songs
	public ObservableList<Song> listSongs(){

		return null;

	}
}