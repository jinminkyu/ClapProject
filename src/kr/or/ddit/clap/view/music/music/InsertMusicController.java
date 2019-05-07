/**
 * 가수관리 수정화면 컨트롤러
 * @author Hansoo
 * 
 */
package kr.or.ddit.clap.view.music.music;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

import javafx.embed.swing.SwingFXUtils;
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

public class InsertMusicController implements Initializable {

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
	JFXButton btn_insertImg;
	
	@FXML
	JFXTextField txt_time;
	
	@FXML
	JFXComboBox<String> combo_genre;
	
	@FXML
	JFXComboBox<String> combo_genreDetail;
	
	@FXML
	TextArea txt_lyrics;

	@FXML
	AnchorPane main;
	
	@FXML
	Label genreDetail;
	
	@FXML
	Label label_albNO;
	
	@FXML
	Label label_musNO;

	@FXML JFXButton btn_insertvideo;
	
	
	
	private FileChooser fileChooser;
	private File filePath;
	private String img_path;
	private Registry reg;
	private IMusicService ims;
	public static AnchorPane contents;
	public static String singNo;
	public static String singName;

	

	// ShowSingerList.fxml는 VBOX를 포함한 전부이기 때문에
	// 현재 씬의 VBox까지 모두 제거 후 ShowSingerList를 불러야함.
	public void givePane(AnchorPane contents) {
		this.contents = contents;
		System.out.println("contents 적용완료");
	}

	public void initData() {
		System.out.println("initData");
		img_path = "file:\\\\\\\\Sem-pc\\\\공유폴더\\\\Clap\\\\img\\\\noImg.png";// 처음 아무것도 선택하지 않았을 떄의 이미지
		Image img = new Image(img_path);
		imgview_albumImg.setImage(img);

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			reg = LocateRegistry.getRegistry("localhost", 8888);
			ims =  (IMusicService) reg.lookup("music");
			initData();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}

		//콤보박스 설정
		List<GenreVO> list;
		try {
			list = ims.showGenre();
			for(int i =0; i<list.size(); i++) {
				//콤보박스 값 추가
				combo_genre.getItems().add(list.get(i).getGen_name());
			}
			
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
		
		
		//장르콤보박스 값이 바뀔 때  장르상세도 그에 맞게 출력
		combo_genre.valueProperty().addListener((e->{
			combo_genreDetail.getItems().clear();
			String genreName = combo_genre.getValue();
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
				
				String genreDetailName = ims.selectGenreDetailNO(combo_genreDetail.getValue());
				System.out.println("combo_genreDetail.getValue():"+combo_genreDetail.getValue());
				genreDetail.setText(genreDetailName);
			} catch (RemoteException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		
	}

	@FXML
	public void btn_selectAlbum() {
		System.out.println("앨범조회 버튼클릭");
		try {
			//InsertMusicController(부모창)에서 SelectAlbumController(자식창)을 연다.
			FXMLLoader loader = new FXMLLoader(getClass().getResource("SelectAlbum.fxml"));
			Parent selectAlbum= loader.load(); 
			
			//SelectAlbumController를 받아온다.
			SelectAlbumController cotroller = loader.getController();
			
			//SelectAlbumController에 setcontroller메소드를 정의한다.
			//이 메소드는 this로 받은 자기자신(InsertMusicController)객체를 매개변수로  SelectAlbumController객체의 멤버변수로  set한다.
			cotroller.setcontroller(this);
			
			Stage stage = new Stage();
			Scene scene = new Scene(selectAlbum);
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
		
	//업로드 버튼 클릭시 
	@FXML
	public void btn_upload(ActionEvent event) {
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		fileChooser = new FileChooser();
		fileChooser.setTitle("Open MusicFile");

		// 사용자에 화면에 해당 디렉토리가 기본값으로 보여짐
		// String userDirectoryString = System.getProperty("user.home") + "\\Pictures";
		// 기본위치
		String userDirectoryString = "\\\\Sem-pc\\공유폴더\\Clap\\img\\music";

		File userDirectory = new File(userDirectoryString);

		if (!userDirectory.canRead()) { // 예외시?
			userDirectory = new File("c:/");
		}

		fileChooser.setInitialDirectory(userDirectory);
		;

		this.filePath = fileChooser.showOpenDialog(stage);

		String str_filePath =filePath+"";
		System.out.println("음악파일경로:" + str_filePath);
		
		txt_file.setText(str_filePath);
		
		}

	@FXML
	public void btn_uploadVideo(ActionEvent event) {
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		fileChooser = new FileChooser();
		fileChooser.setTitle("Open VideoFile");

		// 사용자에 화면에 해당 디렉토리가 기본값으로 보여짐
		// String userDirectoryString = System.getProperty("user.home") + "\\Pictures";
		// 기본위치
		String userDirectoryString = "\\\\\\\\Sem-pc\\\\공유폴더\\\\Clap\\\\img\\\\music";

		File userDirectory = new File(userDirectoryString);

		if (!userDirectory.canRead()) { // 예외시?
			userDirectory = new File("c:/");
		}

		fileChooser.setInitialDirectory(userDirectory);
		;

		this.filePath = fileChooser.showOpenDialog(stage);

		String str_filePath =filePath+"";
		System.out.println("뮤비파일경로:" + str_filePath);
		
		txt_fileVideo.setText(str_filePath);
		}	
	
	@FXML
	public void btn_chageImg(ActionEvent event) {
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		fileChooser = new FileChooser();
		fileChooser.setTitle("Open image");

		// 사용자에 화면에 해당 디렉토리가 기본값으로 보여짐
		// String userDirectoryString = System.getProperty("user.home") + "\\Pictures";
		// 기본위치
		String userDirectoryString = "\\\\Sem-pc\\공유폴더\\Clap\\img\\album";

		System.out.println("userDirectoryString:" + userDirectoryString);
		File userDirectory = new File(userDirectoryString);

		if (!userDirectory.canRead()) { // 예외시?
			userDirectory = new File("c:/");
		}

		fileChooser.setInitialDirectory(userDirectory);
		;

		this.filePath = fileChooser.showOpenDialog(stage);

		// 이미지를 새로운 이미지로 바꿈
		try {
			BufferedImage bufferedImage = ImageIO.read(filePath);
			Image image = SwingFXUtils.toFXImage(bufferedImage, null);
			imgview_albumImg.setImage(image);
			System.out.println("파일경로:" + filePath);
			String str_filePath = "file:" + filePath;
			// userDirectoryString = "file:\\\\Sem-pc\\공유폴더\\Clap\\img\\singer"; //화면 출력 시
			// 절대경로로 이미지를 읽기위해서
			img_path = str_filePath;
			System.out.println(img_path);

		} catch (Exception e) {
			// System.out.println(e.getMessage());
			// e.printStackTrace();
			System.out.println("이미지를 선택하지 않았습니다.");
		}
	}


	
	
	
	
	@FXML // 인서트 하는 버튼
	public void insertMusic() {

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

		mVO.setMus_title(txt_name.getText());
		mVO.setMus_time(txt_time.getText());
		mVO.setMus_file(txt_file.getText());
		mVO.setMus_mvfile(txt_fileVideo.getText());
		mVO.setMus_muswrite_son(txt_write.getText());
		mVO.setMus_edit_son(txt_edit.getText());
		mVO.setMus_write_son(txt_muswrite.getText());
		mVO.setMus_lyrics(txt_lyrics.getText());
		mVO.setAlb_no(label_albNO.getText());
		mVO.setGen_detail_no(genreDetail.getText());
		
		
		try {
			int flag = ims.insertMusic(mVO);
			if (flag == 1) {
				System.out.println("앨범등록 완료");
			} else {
				System.out.println("앨범등록 실패");
			}
		} catch (RemoteException e) {

			e.printStackTrace();
		}

		// 바뀔 화면(FXML)을 가져옴

		chagePage();

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

	// 화면을 조회창으로 이동해주는 메서드
	public void chagePage() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("MusicList.fxml"));// init실행됨
		Parent musicList;
		try {
			musicList = loader.load();
			contents.getChildren().removeAll();
			contents.getChildren().setAll(musicList);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
