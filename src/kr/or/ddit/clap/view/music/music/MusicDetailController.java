/**
 *가수 리스트 상세보기를 출력하는 화면 controller
 * 
 * 
 * @author Hansoo
 *
 */
package kr.or.ddit.clap.view.music.music;

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
import kr.or.ddit.clap.vo.music.MusicVO;

public class MusicDetailController implements Initializable {

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
	@FXML Label lb_genre;
	@FXML Label lb_genreDetail;
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
			mVO = ims.selectMusicDetailInfo(musicNo);
			// 파라미터로 받은 정보를 PK로 상세정보를 가져옴
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}

		
	
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
		lb_genre.setText(mVO.getGen_name());  
		
		lb_genreDetail.setText(mVO.getGen_detail_name()); 
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
			AnchorPane pane = FXMLLoader.load(getClass().getResource("AlbummgWiderDialog.fxml"));
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


	// 사용자가 확인을 누르면 1을 리턴 이외는 -1
	public int alertConfrimDelete() {
		Alert alertConfirm = new Alert(AlertType.CONFIRMATION);

		alertConfirm.setTitle("CONFIRMATION");
		alertConfirm.setContentText("정말로 삭제하시겠습니까?(해당 앨범 및 곡이 모두 삭제됩니다)");

		// Alert창을 보여주고 사용자가 누른 버튼 값 읽어오기
		ButtonType confirmResult = alertConfirm.showAndWait().get();

		if (confirmResult == ButtonType.OK) {
			System.out.println("OK 버튼을 눌렀습니다.");
			return 1;
		} else if (confirmResult == ButtonType.CANCEL) {
			System.out.println("취소 버튼을 눌렀습니다.");
			return -1;
		}
		return -1;
	}

	@FXML public void updateMusic() {
		try {
			// 바뀔 화면(FXML)을 가져옴
			UpdateMusicController.musicNo = musicNo;// 가수번호를 변수로 넘겨줌

			FXMLLoader loader = new FXMLLoader(getClass().getResource("UpdateMusic.fxml"));// initialize실행됨
			Parent UpdateMusic = loader.load();
			UpdateMusicController cotroller = loader.getController();
			cotroller.initData(mVO, str_like_cnt);
			main.getChildren().removeAll();
			main.getChildren().setAll(UpdateMusic);

		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
	}


	@FXML public void deleteMusic() {
		
		// Alert창을 출력해 정말 삭제할 지 물어봄
				try { if(0>alertConfrimDelete()) 
				{ return; }

				int cnt = ims.deleteMusic(musicNo);
				System.out.println("삭제 여부:" + cnt);
				if (cnt >= 1) {
					System.out.println("삭제성공");
				}

				else {
					System.out.println("삭제실패");

				}
			}
			catch(RemoteException e)
			{
				e.printStackTrace();
			}

			// 화면전환 
				FXMLLoader loader = new FXMLLoader(getClass().getResource("MusicList.fxml")); 
			Parent musicList;
				  try {
				  
					  musicList = loader.load(); //ShowSingerList.fxml는 VBOX를 포함한 전부이기 때문에 //현재 씬의
				//  VBox까지 모두 제거 후 ShowSingerList를 불러야함.
				  
				  contents.getChildren().removeAll(); // main.getChildren().removeAll();
				  contents.getChildren().setAll(musicList);
				  
				  } catch (IOException e) { e.printStackTrace(); }
				  
		
	}
}
