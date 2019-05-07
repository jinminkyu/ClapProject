package kr.or.ddit.clap.view.chartmenu.main;

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

public class ChartMenuController implements Initializable{
	
	public static int menuCount = 0;
	
	@FXML JFXTabPane tabPane_main;
	@FXML Tab tab_top50;
	@FXML Tab tab_genre;
	@FXML Tab tab_period;
	@FXML AnchorPane main;
	@FXML StackPane stackPane_top50;
	@FXML StackPane stackPane_genre;
	@FXML StackPane stackPane_period;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		tabPane_main.getSelectionModel().select(menuCount);
		
		try {
			StackPane pane = FXMLLoader.load(getClass().getResource("../top50/Top50.fxml"));
			stackPane_top50.getChildren().removeAll();
			stackPane_top50.getChildren().setAll(pane);
			
			StackPane pane2 = FXMLLoader.load(getClass().getResource("../genre/Genre.fxml"));
			stackPane_genre.getChildren().removeAll();
			stackPane_genre.getChildren().setAll(pane2);
			
			StackPane pane3 = FXMLLoader.load(getClass().getResource("../period/Period.fxml"));
			stackPane_period.getChildren().removeAll();
			stackPane_period.getChildren().setAll(pane3);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
