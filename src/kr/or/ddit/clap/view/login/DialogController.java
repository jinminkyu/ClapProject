package kr.or.ddit.clap.view.login;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class DialogController implements Initializable{

	@FXML Label lb_idSearch;
	@FXML Button btn_ok;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		SearchController sc = new SearchController();
//		lb_idSearch.setText(sc.idSearch);
	}


}
