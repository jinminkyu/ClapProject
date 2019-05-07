package kr.or.ddit.clap.view.singer.main;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Label;

public class NotfoundController implements Initializable {

	@FXML AnchorPane contents;
public static String word;
@FXML Label label_word;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		System.out.println("검색결과가 없습니다.");
		
		label_word.setText("'"+word+"'");
		
	}

}
