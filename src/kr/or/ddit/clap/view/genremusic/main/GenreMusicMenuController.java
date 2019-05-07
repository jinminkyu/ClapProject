package kr.or.ddit.clap.view.genremusic.main;

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

public class GenreMusicMenuController implements Initializable{
	
	public static int menuCount = 0;
	
	@FXML AnchorPane main;
	@FXML JFXTabPane tabPane_main;
	@FXML Tab tab_song;
	@FXML Tab tab_pop;
	@FXML Tab tab_ost;
	@FXML Tab tab_other;
	@FXML StackPane stackPane_song;
	@FXML StackPane stackPane_pop;
	@FXML StackPane stackPane_ost;
	@FXML StackPane stackPane_other;
	

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		tabPane_main.getSelectionModel().select(menuCount);
		
		try {
			StackPane pane = FXMLLoader.load(getClass().getResource("../song/Song.fxml"));
			stackPane_song.getChildren().removeAll();
			stackPane_song.getChildren().setAll(pane);
			
			StackPane pane2 = FXMLLoader.load(getClass().getResource("../pop/Pop.fxml"));
			stackPane_pop.getChildren().removeAll();
			stackPane_pop.getChildren().setAll(pane2);
			
			StackPane pane3 = FXMLLoader.load(getClass().getResource("../ost/Ost.fxml"));
			stackPane_ost.getChildren().removeAll();
			stackPane_ost.getChildren().setAll(pane3);
			
			StackPane pane4 = FXMLLoader.load(getClass().getResource("../other/Other.fxml"));
			stackPane_other.getChildren().removeAll();
			stackPane_other.getChildren().setAll(pane4);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
