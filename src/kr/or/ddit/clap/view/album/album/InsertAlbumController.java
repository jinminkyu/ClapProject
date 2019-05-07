/**
 * 가수관리 수정화면 컨트롤러
 * @author Hansoo
 * 
 */
package kr.or.ddit.clap.view.album.album;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
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
import kr.or.ddit.clap.service.album.IAlbumService;
import kr.or.ddit.clap.vo.album.AlbumVO;

public class InsertAlbumController implements Initializable {

	@FXML
	ImageView imgview_albumImg;
	@FXML
	JFXTextField txt_name;

	@FXML
	JFXTextField label_singName;
	@FXML
	Label label_singerNO;
	@FXML
	JFXDatePicker date_saledate;
	@FXML
	JFXTextField txt_saleEnter;
	@FXML
	JFXButton btn_chageImg;
	@FXML
	JFXTextField txt_entertain;
	@FXML
	TextArea txt_intro;

	@FXML
	AnchorPane main;

	private FileChooser fileChooser;
	private File filePath;
	private String img_path;
	private Registry reg;
	private IAlbumService ias;
	public static AnchorPane contents;
	public static String singNo;
	public static String singName;

	public void returnData(String singNo, String singName) {
		try {
			this.singNo = singNo;
			this.singName = singName;
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
	}

	// ShowSingerList.fxml는 VBOX를 포함한 전부이기 때문에
	// 현재 씬의 VBox까지 모두 제거 후 ShowSingerList를 불러야함.
	public void givePane(AnchorPane contents) {
		this.contents = contents;
		System.out.println("contents 적용완료");
	}

	public void initData() {
		img_path = "file:\\\\\\\\Sem-pc\\\\공유폴더\\\\Clap\\\\img\\\\noImg.png";// 처음 아무것도 선택하지 않았을 떄의 이미지
		Image img = new Image(img_path);
		imgview_albumImg.setImage(img);

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			reg = LocateRegistry.getRegistry("localhost", 8888);
			ias = (IAlbumService) reg.lookup("album");
			initData();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}

	}

	@FXML
	public void btn_selectSinger() {
		try {
			/*FXMLLoader loader = new FXMLLoader(getClass().getResource("SingerDetail.fxml"));// init실행됨
			Parent singerDetail= loader.load(); 
			
			ShowSingerDetailController cotroller = loader.getController();
			*/
			FXMLLoader loader = new FXMLLoader(getClass().getResource("selectSinger.fxml"));
			Parent selectSinger= loader.load(); 
			SelectSingerController cotroller = loader.getController();
			
			cotroller.setcontroller(this);
			
			
			Stage stage = new Stage();
			Scene scene = new Scene(selectSinger);
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			Stage primaryStage = (Stage)label_singName.getScene().getWindow();
			stage.initOwner(primaryStage);
			stage.setWidth(600);
			stage.setHeight(600);
			stage.show();
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
	}

	@FXML
	public void btn_chageImg(ActionEvent event) {
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		fileChooser = new FileChooser();
		fileChooser.setTitle("Open image");

		// 사용자에 화면에 해당 디렉토리가 기본값으로 보여짐
		// 기본위치
		String userDirectoryString = "\\\\Sem-pc\\공유폴더\\Clap\\img\\album";

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
			String str_filePath = "file:" + filePath;
			// userDirectoryString = "file:\\\\Sem-pc\\공유폴더\\Clap\\img\\singer"; //화면 출력 시
			// 절대경로로 이미지를 읽기위해서
			img_path = str_filePath;

		} catch (Exception e) {
			 e.printStackTrace();
		}
	}

	@FXML // 인서트 하는 버튼
	public void insertAlbum() {

		if (txt_name.getText().isEmpty()) {
			errMsg("앨범명은 필수 입력 사항입니다.");
			return;
		}

		if (label_singName.getText().isEmpty()) {
			errMsg("가수이름은 필수 입력 사항입니다.");
			return;
		}

		if ((date_saledate.getValue()).toString().isEmpty()) {
			errMsg("발매일은 필수 입력 사항입니다.");
			return;
		}

		if (txt_saleEnter.getText().isEmpty()) {
			errMsg("기획사는 필수 입력 사항입니다.");
			return;
		}

		AlbumVO aVO = new AlbumVO();

		aVO.setAlb_name(txt_name.getText());
		aVO.setSing_no(label_singerNO.getText());
		aVO.setAlb_saledate(date_saledate.getValue() + "");
		aVO.setAlb_sale_enter(txt_saleEnter.getText());
		aVO.setAlb_entertain(txt_entertain.getText());
		aVO.setAlb_image(img_path);
		aVO.setAlb_intro(txt_intro.getText());
		
		// indate 자동 입력되도록 추가.
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yy/MM/dd");
		aVO.setAlb_indate(sdf.format(now));

		try {
			int flag = ias.insertAlbum(aVO);
			if (flag == 1) {
			} else {
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
		FXMLLoader loader = new FXMLLoader(getClass().getResource("ShowAlbumLIst.fxml"));// init실행됨
		Parent singerList;
		try {
			singerList = loader.load();
			contents.getChildren().removeAll();
			contents.getChildren().setAll(singerList);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
