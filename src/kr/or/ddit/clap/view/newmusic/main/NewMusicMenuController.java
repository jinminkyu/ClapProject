package kr.or.ddit.clap.view.newmusic.main;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import com.jfoenix.controls.JFXTabPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

/**
 * 
 * @author 진민규
 *
 */

public class NewMusicMenuController implements Initializable{
	
	public static int menuCount = 0;
	
	@FXML JFXTabPane tabPane_main;
	@FXML Tab tab_album;
	@FXML Tab tab_music;
	@FXML StackPane stackPane_album;
	@FXML StackPane stackPane_music;
	@FXML AnchorPane main;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		tabPane_main.getSelectionModel().select(menuCount);
		
		try {
			StackPane pane = FXMLLoader.load(getClass().getResource("../album/NewAlbum.fxml"));
			stackPane_album.getChildren().removeAll();
			stackPane_album.getChildren().setAll(pane);
			
			StackPane pane2 = FXMLLoader.load(getClass().getResource("../music/NewMusic.fxml"));
			stackPane_music.getChildren().removeAll();
			stackPane_music.getChildren().setAll(pane2);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
