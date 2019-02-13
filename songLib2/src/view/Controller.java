package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Optional;

import com.google.gson.*;

public class Controller {

	@FXML TextField currName;
	@FXML TextField currArtist;
	@FXML TextField currAlbum;
	@FXML TextField currYear;

	//variables involving listing all songs
	private ObservableList<Song> obsList;
	@FXML ListView<Song> listView;
	@FXML Button delete;
	@FXML Button edit;
	@FXML Button editConfirm;
	@FXML Button editCancel;

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

	//Miscellaneous variables for editting
	String tempName;
	String tempArtist;
	String tempAlbum;
	String tempYear;
	int tempIndex;




	public void start(Stage mainStage) throws  FileNotFoundException {

		obsList = FXCollections.observableArrayList();
	
		jsonParser = new JsonParser();
		jsonRoot = jsonParser.parse(new FileReader("src/savedSongs.json"));
		jsonObj = jsonRoot.getAsJsonObject();
		jsonArray = jsonObj.getAsJsonArray("songList");
		gson = new Gson();
		for(int i = 0; i < jsonArray.size();i++) {
			obsList.add(gson.fromJson(jsonArray.get(i), Song.class));
		}
		sort();
		listView.setItems(obsList);
		updateListView();

		listView.getSelectionModel().select(0);
		notEditing();

	    showItem(mainStage);
	    listView
	       .getSelectionModel()
	       .selectedIndexProperty()
	       .addListener(
	          (obs, oldVal, newVal) ->
	              showItem(mainStage));

	}

	public void deleteSong(ActionEvent e){
		Button b = (Button)e.getSource();
		if(b == delete){
			Song curr = listView.getSelectionModel().getSelectedItem();

			if(curr != null){

				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Confirmation");
				alert.setContentText("Confirm song deletion");
				Optional<ButtonType> choice = alert.showAndWait();

				if(choice.get() == ButtonType.OK){


					jsonArray.remove(getJsonIndex(curr.name,curr.artist));
					updateJson();
					obsList.remove(curr);
					displayAlert("Deletion", "Successfully deleted " + curr.name+ " by " + curr.artist);
				}

			}
		}
	}

	public void editSong(ActionEvent e){
		Button b = (Button)e.getSource();
		Song curr = listView.getSelectionModel().getSelectedItem();
		if(curr == null){
			displayAlert("Error","Please select a song first to edit!");
			return;
		}
		if(b == edit){
			
			tempName = curr.name;
			tempArtist = curr.artist;
			tempAlbum = curr.album;
			tempYear = curr.year;
			tempIndex = getJsonIndex(tempName,tempArtist);
			
			editing();
		}else if(b == editConfirm){
			
			String newName = currName.getText();
			String newArtist = currArtist.getText();
			String newYear = newSongYear.getText();
			String newAlbum = newSongAlbum.getText();
			if(newName.equals("")  || newArtist.equals("")){
				displayAlert("Error", "Song name and artist can not be empty!");
			}else if(!newYear.equals("") && !isNumber(newYear)){
				displayAlert("Error", "Invaild song year!");	
			}else{
				
				if(!(newName.toLowerCase().equals(tempName.toLowerCase()) && newArtist.toLowerCase().equals(tempArtist.toLowerCase())) && isDuplicate(newName,newArtist)){
					displayAlert("Error","Duplicate song exists!");
				}else{
					JsonObject jsonObj = (JsonObject) jsonArray.get(tempIndex);
					jsonObj.addProperty("name", newName);
					jsonObj.addProperty("artist", newArtist);
					jsonObj.addProperty("year", newYear);
					jsonObj.addProperty("album", newAlbum);
					
					curr.name = newName;
					curr.album = newAlbum;
					curr.year = newYear;
					curr.artist = newArtist;
					updateJson();
					updateListView();
					sort();
					displayAlert("Success","Edit successful!");
					notEditing();
				}
			}
			
		}else if (b == editCancel){
			currName.setText(tempName);
			currArtist.setText(tempArtist);
			currAlbum.setText(tempAlbum);
			currYear.setText(tempYear);
			notEditing();
		}
	}
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

			}else if(isDuplicate(name,artist)){
				displayAlert("Error","Duplicate song exists!");

			}else{

				JsonObject jsonTemp = new JsonObject();

				jsonTemp.addProperty("name", name);
				jsonTemp.addProperty("artist", artist);
				jsonTemp.addProperty("album", album);
				jsonTemp.addProperty("year", year);
				jsonArray.add(jsonTemp);
				
				updateJson();

				Song newSong = new Song(name,artist,album,year);
				obsList.add(newSong);
				sort();
				listView.getSelectionModel().select(newSong);

				updateListView();

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

	public void updateListView(){
		listView.setCellFactory(list-> new ListCell<Song>(){
			@Override
			protected void updateItem(Song song,boolean e){
				super.updateItem(song,e);
				if(song == null){
					setText("");
				}else{
					String album;
					String year;
					
					if(song.year.equals("")){
						year = "no year";
					}else {
						year = song.year;
					}
					
					if(song.album.equals("")){
						album = "no album";
					}else {
						album = song.album;
					}
					setText(song.name + " - " + song.artist + " - " + album + " - " + year);
				}
			}

		});

	}

	public boolean isNumber(String string){
		try{
		    int temp = Integer.parseInt(string);
		  }catch(NumberFormatException nfe)  {
			  return false;
		  }
		return true;
	}

	//searches through observableList and see if there's any duplicate
	//true = is duplicate || false = not duplicate
	public boolean isDuplicate(String name, String artist){

		name = name.toLowerCase();
		artist = artist.toLowerCase();

		for(int i = 0; i < obsList.size(); i ++){
		
				String tName = obsList.get(i).name.toLowerCase();
				String tArtist = obsList.get(i).artist.toLowerCase();
	
				if(name.equals(tName) && artist.equals(tArtist)){
					return true;
				}
			
		}
		return false;
	}

	public void showItem(Stage mainStage){
		Song curr = listView.getSelectionModel().getSelectedItem();
		if(curr != null){
			currName.setText(curr.name);
			currArtist.setText(curr.artist);
			currAlbum.setText(curr.album);
			currYear.setText(curr.year);
		}
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

	public int getJsonIndex(String name,String artist){

		for(int i = 0; i < jsonArray.size();i++){
			JsonObject temp = (JsonObject) jsonArray.get(i);
			String n = temp.get("name").getAsString();
			String a = temp.get("artist").getAsString();
			if(name.equals(n) && artist.equals(a)){
				return i;
			}
		}
		return -1;
	}

	public void sort() {
		Collections.sort(obsList, new Comparator<Song>() {
			@Override
			public int compare(Song s1, Song s2) {
				int result = s1.name.compareToIgnoreCase(s2.name);
				if(result == 0) {
					result = s1.artist.compareToIgnoreCase(s2.artist);
				}
				return result;
			}
		});
	}
	
	public void notEditing(){

		//currName.setStyle("-fx-focus-color: transparent;");
		currName.setEditable(false);
		currArtist.setEditable(false);
		currAlbum.setEditable(false);
		currYear.setEditable(false);
		currName.setOpacity(0.5);
		currArtist.setOpacity(0.5);
		currAlbum.setOpacity(0.5);
		currYear.setOpacity(0.5);
		editConfirm.setOpacity(0);
		editCancel.setOpacity(0);
		delete.setOpacity(1);
		edit.setOpacity(1);
		listView.setMouseTransparent(false);
		listView.setFocusTraversable(true);
	
	}
	
	public void editing(){
		currName.setEditable(true);
		currArtist.setEditable(true);
		currAlbum.setEditable(true);
		currYear.setEditable(true);
		currName.setOpacity(1);
		currArtist.setOpacity(1);
		currAlbum.setOpacity(1);
		currYear.setOpacity(1);
		editConfirm.setOpacity(1);
		editCancel.setOpacity(1);
		delete.setOpacity(0);
		edit.setOpacity(0);
		listView.setMouseTransparent(true);
		listView.setFocusTraversable(false);
	}
}
