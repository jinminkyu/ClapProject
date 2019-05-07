/**
 * 앨범관리 수정화면 컨트롤러
 * @author Hansoo
 * 
 */
package kr.or.ddit.clap.view.music.music;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import kr.or.ddit.clap.service.music.IMusicService;
import kr.or.ddit.clap.vo.genre.GenreDetailVO;
import kr.or.ddit.clap.vo.genre.GenreVO;
import kr.or.ddit.clap.vo.music.MusicVO;

public class UpdateMusicController implements Initializable {

	@FXML
	ImageView imgview_albumImg;
	@FXML
	JFXTextField txt_name;
	@FXML
	JFXTextField txt_albName;
	@FXML
	JFXTextField txt_singerName;
	@FXML
	JFXTextField txt_write;
	@FXML
	JFXTextField txt_edit;
	@FXML
	JFXTextField txt_muswrite;
	@FXML
	JFXTextField txt_file;
	@FXML
	JFXTextField txt_fileVideo;
	@FXML
	JFXTextField txt_time;
	
	@FXML
	JFXComboBox<String> combo_genre;
	@FXML
	JFXComboBox<String> combo_genreDetail;
	
	@FXML
	Label label_albNO;
	@FXML
	Label genreDetail;
	@FXML
	Label label_musNO;
	@FXML
	JFXButton btn_insertImg;
	@FXML
	TextArea txt_lyrics;
	@FXML AnchorPane main;
	
	@FXML Label label_likeCnt;
	
	private FileChooser fileChooser;
	private File filePath;
	private String img_path;
	private Registry reg;
	private IMusicService ims;
	
	
	public static String musicNo; // PK값 받기

	// 전 화면에 있는 데이터를 그대로 가져와  세팅해주는 메서드
	public void initData(MusicVO mVO, String str_like_cnt) {
		if(mVO.getMus_mvfile()==null) {
			mVO.setMus_mvfile("");
		}
		
		label_musNO.setText(musicNo);
		img_path = mVO.getAlb_image(); //이미지 객체 경로 받음
		Image img = new Image(img_path); //이미지 객체 생성
		imgview_albumImg.setImage(img); //이미지 등록 
		txt_name.setText(mVO.getMus_title());
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
		label_albNO.setText(mVO.getAlb_no());
		label_musNO.setText(mVO.getMus_no());
		
		genreDetail.setText(mVO.getGen_detail_no());
		txt_lyrics.setText(mVO.getMus_lyrics());
		label_likeCnt.setText(str_like_cnt);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		System.out.println("곡 번호:" + musicNo);
		
				List<GenreVO> list;
				try {
					reg = LocateRegistry.getRegistry("localhost", 8888);
					ims = (IMusicService) reg.lookup("music");
					
					//콤보박스 설정
					list = ims.showGenre();
					System.out.println("list size:"+ list.size());
					for(int i =0; i<list.size(); i++) {
						//콤보박스 값 추가
						combo_genre.getItems().add(list.get(i).getGen_name());
					}
					
				} catch (RemoteException e) {
					e.printStackTrace();
				} catch (NotBoundException e1) {
					e1.printStackTrace();
				}
				
				
				
				//장르콤보박스 값이 바뀔 때  장르상세도 그에 맞게 출력
				combo_genre.valueProperty().addListener((e->{
					combo_genreDetail.getItems().clear();
					String genreName = (String) combo_genre.getValue();
					System.out.println("장르:"+genreName);
					try {
						String genreNo= ims.selectGenreNO(genreName);
						System.out.println("장르번호:"+ genreNo);
						List<GenreDetailVO> list_genreDetail =ims.showGenreDetail(genreNo);
						System.out.println("리스트 사이즈"+list_genreDetail.size());
						
						for(int i=0; i<list_genreDetail.size(); i++) {
							combo_genreDetail.getItems().add(list_genreDetail.get(i).getGen_detail_name());
						}
					} catch (RemoteException e1) {
						e1.printStackTrace();
					}
				}));

				combo_genreDetail.valueProperty().addListener((e)->{
					try {
						
						String genreDetailName = ims.selectGenreDetailNO(combo_genreDetail.getValue()+"");
						System.out.println("combo_genreDetail.getValue():"+combo_genreDetail.getValue());
						genreDetail.setText(genreDetailName);
					} catch (RemoteException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				});
	}

	@FXML
	public void btn_upload(ActionEvent event) {
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		fileChooser = new FileChooser();
		fileChooser.setTitle("Open MusicFile");

		// 사용자에 화면에 해당 디렉토리가 기본값으로 보여짐
		// String userDirectoryString = System.getProperty("user.home") + "\\Pictures";
		// 기본위치
		String userDirectoryString = "\\\\Sem-pc\\공유폴더\\Clap\\mp3";

		File userDirectory = new File(userDirectoryString);

		if (!userDirectory.canRead()) { // 예외시?
			userDirectory = new File("c:/");
		}

		fileChooser.setInitialDirectory(userDirectory);
		;

		this.filePath = fileChooser.showOpenDialog(stage);

		String str_filePath = "file:" + filePath;
		System.out.println("파일경로:" + str_filePath);
		
		txt_file.setText(str_filePath);
	}
	@FXML
	public void btn_uploadVideo(ActionEvent event) {
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		fileChooser = new FileChooser();
		fileChooser.setTitle("Open MusicFile");
		
		// 사용자에 화면에 해당 디렉토리가 기본값으로 보여짐
		// String userDirectoryString = System.getProperty("user.home") + "\\Pictures";
		// 기본위치
		String userDirectoryString = "\\\\Sem-pc\\공유폴더\\Clap\\video";
		
		File userDirectory = new File(userDirectoryString);
		
		if (!userDirectory.canRead()) { // 예외시?
			userDirectory = new File("c:/");
		}
		
		fileChooser.setInitialDirectory(userDirectory);
		;
		
		this.filePath = fileChooser.showOpenDialog(stage);
		
		String str_filePath = "file:" + filePath;
		System.out.println("파일경로:" + str_filePath);
		
		txt_fileVideo.setText(str_filePath);
	}

	@FXML //업데이트 하는 버튼
	
	
	public void updateMusic() {
		
		  if (txt_name.getText().isEmpty()) {
			errMsg("곡은 필수 입력 사항입니다.");
			return;
		}

		if (txt_albName.getText().isEmpty()) {
			errMsg("앨범은 필수 입력 사항입니다.");
			return;
		}

		if (txt_file.getText().isEmpty()) {
			errMsg("Music File은 필수 입력 사항입니다.");
			return;
		}
		
		 
		
		
	
		MusicVO mVO = new MusicVO();
		System.out.println("곡번호:" + musicNo);
		mVO.setMus_no(musicNo);
		mVO.setMus_title(txt_name.getText());
		mVO.setMus_time(txt_time.getText());
		mVO.setMus_file(txt_file.getText());
		mVO.setMus_mvfile(txt_fileVideo.getText());
		mVO.setMus_edit_son(txt_edit.getText());
		mVO.setMus_lyrics(txt_lyrics.getText());
		mVO.setMus_write_son(txt_write.getText());
		mVO.setMus_muswrite_son(txt_muswrite.getText());
		mVO.setGen_detail_no(genreDetail.getText());
		mVO.setAlb_no(label_albNO.getText());
		
		try {
			ims.updateMusicInfo(mVO);
			System.out.println("앨범정보 변경 완료");
		} catch (RemoteException e) {
			
			e.printStackTrace();
		}
		
		chagePage();
		
	}
	
	@FXML
	public void btn_selectAlbum() {
		System.out.println("앨범조회 버튼클릭");
		try {
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource("SelectAlbumUpd.fxml"));
			Parent selectAlbumUpd= loader.load(); 
			SelectAlbumUpdController cotroller = loader.getController();
			
			cotroller.setcontroller(this);
			
			
			Stage stage = new Stage();
			Scene scene = new Scene(selectAlbumUpd);
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			Stage primaryStage = (Stage)txt_name.getScene().getWindow();
			stage.initOwner(primaryStage);
			stage.setWidth(600);
			stage.setHeight(600);
			stage.show();
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
		
	}
	
	
	public void errMsg(String msg) {
		Alert errAlert = new Alert(AlertType.ERROR);
		errAlert.setTitle("유효성 검사");
		errAlert.setHeaderText("유효성 검사");
		errAlert.setContentText(msg);
		errAlert.showAndWait();
	}
	@FXML
	public void cancel() {
		chagePage();
	}
	public void chagePage() {
		try {
			//바뀔 화면(FXML)을 가져옴
			//singerDetail
			MusicDetailController.musicNo = musicNo;//앨범번호를 변수로 넘겨줌
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource("MusicDetail.fxml"));// init실행됨
			Parent albumDetail= loader.load(); 
			main.getChildren().removeAll();
			main.getChildren().setAll(albumDetail);
			
			
		} catch (IOException e1) {
			e1.printStackTrace();
		} 
	}
}
