package application;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class Controller {
	@FXML Button newSongSubmit;
	@FXML TextArea songCurrent;

	@FXML TableView songTable;
	@FXML TextField newSongName;
	@FXML TextField newSongArtist;
	@FXML TextField newSongAlbum;
	@FXML TextField newSongYear;
}
