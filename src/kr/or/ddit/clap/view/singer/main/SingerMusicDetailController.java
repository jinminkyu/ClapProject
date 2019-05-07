/**
 *가수 리스트 상세보기를 출력하는 화면 controller
 * 
 * 
 * @author Hansoo
 *
 */
package kr.or.ddit.clap.view.singer.main;

import java.io.IOException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import kr.or.ddit.clap.service.music.IMusicService;
import kr.or.ddit.clap.view.music.music.UpdateMusicController;
import kr.or.ddit.clap.vo.music.MusicVO;

public class SingerMusicDetailController implements Initializable {

	public static String musicNo;// 파라미터로 받은 선택한 가수의 PK
	private Registry reg;
	private IMusicService ims;
	private String temp_img_path = "";

	// 파라미터로 넘기기 위해 전역으로 선언
	public MusicVO mVO = null;
	public String str_like_cnt;
	public static AnchorPane contents;

	// @FXML Label label_singNo;
	
	@FXML
	AnchorPane main;
	@FXML Label label_musicTitle;
	@FXML Label label_musicTitle2;
	@FXML ImageView imgview_albumImg;
	@FXML Label txt_albName;
	@FXML Label txt_singerName;
	@FXML Label txt_write;
	@FXML Label txt_edit;
	@FXML Label txt_muswrite;
	@FXML Label txt_file;
	@FXML Label txt_fileVideo;
	@FXML JFXComboBox<String> combo_genre;
	@FXML JFXComboBox<String> combo_genreDetail;
	@FXML Label txt_time;
	@FXML Label label_LikeCnt;
	
	@FXML JFXTextArea txt_lyrics;
	@FXML Label genreDetail;
	@FXML Label label_albNO;
	@FXML Label label_musNO;
	

	// ShowSingerList.fxml는 VBOX를 포함한 전부이기 때문에
	// 현재 씬의 VBox까지 모두 제거 후 ShowSingerList를 불러야함.
	public void givePane(AnchorPane contents) {
		this.contents = contents;
		System.out.println("contents 적용완료");
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		System.out.println("init: 곡 번호" + musicNo);

		try {
			// reg로 ISingerService객체를 받아옴
			reg = LocateRegistry.getRegistry("localhost", 8888);
			ims = (IMusicService) reg.lookup("music");
			mVO = ims.selectMusicDetailInfo("17");
			// 파라미터로 받은 정보를 PK로 상세정보를 가져옴
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}

		
		System.out.println(mVO.getMus_title());
		label_musicTitle.setText(mVO.getMus_title());
//		label_musicTitle2.setText(mVO.getMus_title());
		Image img = new Image(mVO.getAlb_image());
		temp_img_path = mVO.getAlb_image(); //전역으로 쓰기위해서
		imgview_albumImg.setImage(img);
		txt_albName.setText(mVO.getAlb_name());
		txt_singerName.setText(mVO.getSing_name());

		txt_write.setText(mVO.getMus_write_son());
		txt_edit.setText(mVO.getMus_edit_son());
		txt_muswrite.setText(mVO.getMus_muswrite_son());
		txt_file.setText(mVO.getMus_file()); 
		txt_fileVideo.setText(mVO.getMus_mvfile());
		combo_genre.setValue(mVO.getGen_name());  
		
		combo_genreDetail.setValue(mVO.getGen_detail_name()); 
		txt_time.setText(mVO.getMus_time());  
		txt_lyrics.setEditable(false);
		txt_lyrics.setText(mVO.getMus_lyrics());
		genreDetail.setText(mVO.getGen_detail_no()); 
		label_albNO.setText(mVO.getAlb_no());  
		label_musNO.setText(musicNo);

		// 좋아요 수를 가져오는 쿼리
		int like_cnt = 0;
		try {
			like_cnt = ims.selectMusicLikeCnt(musicNo);
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		// 좋아요는 다른 VO에서 가져와야함...
		str_like_cnt = like_cnt + "";
		label_LikeCnt.setText(str_like_cnt);

	}

	@FXML
	public void wideView() {
		// img_wideimg
		System.out.println("크게보기 버튼클릭");
		try {
			AnchorPane pane = FXMLLoader.load(getClass().getResource("../../album/album/AlbummgWiderDialog.fxml"));
			Stage stage = new Stage(StageStyle.UTILITY);
			Scene scene = new Scene(pane);
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			Stage primaryStage = (Stage) label_musicTitle.getScene().getWindow();
			stage.initOwner(primaryStage);
			stage.setResizable(false);

			ImageView img_wideimg = (ImageView) pane.lookup("#img_wideimg");
			Image temp_img = new Image(temp_img_path);
			img_wideimg.setImage(temp_img);

			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML public void updateMusic() {}

	@FXML public void deleteMusic() {}

}
