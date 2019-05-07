/**
 * 앨범관리 수정화면 컨트롤러
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
import java.time.LocalDate;
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

public class UpdateAlbumController implements Initializable {

	@FXML
	ImageView imgview_albumImg;
	@FXML
	JFXTextField txt_albumName;
	@FXML
	JFXTextField txt_singerName;
	@FXML
	JFXDatePicker datePicker_saledate;
	@FXML
	JFXTextField txt_saleEnter;
	@FXML
	JFXTextField txt_entertain;
	@FXML
	Label label_LikeCnt;
	@FXML
	JFXButton btn_chageImg;
	@FXML
	TextArea txt_intro;
	@FXML AnchorPane main;
	@FXML Label label_singerNO;
	
	private FileChooser fileChooser;
	private File filePath;
	private String img_path;
	private Registry reg;
	private IAlbumService ias;
	
	
	public static String albumNo; // PK값 받기

	// 전 화면에 있는 데이터를 그대로 가져와  세팅해주는 메서드
	public void initData(AlbumVO aVO, String str_like_cnt) {
		
		label_singerNO.setText(aVO.getSing_no()); 
		img_path = aVO.getAlb_image(); //이미지경로를 전역에 저장
		Image img = new Image(img_path); //이미지 객체등록
		imgview_albumImg.setImage(img);
		txt_albumName.setText(aVO.getAlb_name()); 
		txt_singerName.setText(aVO.getSing_name());
		
		String temp_date = aVO.getAlb_saledate().substring(0, 10);
		datePicker_saledate.setValue(LocalDate.parse(temp_date));
		
		label_LikeCnt.setText(str_like_cnt);
		txt_intro.setText(aVO.getAlb_intro());
		txt_saleEnter.setText(aVO.getAlb_sale_enter());
		txt_entertain.setText(aVO.getAlb_entertain());
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			reg = LocateRegistry.getRegistry("localhost", 8888);
			ias = (IAlbumService) reg.lookup("album");
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
		
		
	}

	@FXML
	public void btn_chageImg(ActionEvent event) {
		 Stage stage =  (Stage) ((Node)event.getSource()).getScene().getWindow();
		 fileChooser = new FileChooser();
		 fileChooser.setTitle("Open image");
		 
		 //사용자의 디렉토리 보여줌
		 String userDirectoryString = "\\\\Sem-pc\\공유폴더\\Clap\\img\\album";
		 
		 File userDirectory = new File(userDirectoryString); 
		 
		 if(!userDirectory.canRead()) {
			 userDirectory = new File("c:/");
		 }
		 
		 fileChooser.setInitialDirectory(userDirectory);; 
		 
		 this.filePath = fileChooser.showOpenDialog(stage);
		 
		
		 //이미지를 새로운 이미지로 바꿈
		 try {
			 BufferedImage bufferedImage = ImageIO.read(filePath);
			 Image image =  SwingFXUtils.toFXImage(bufferedImage, null);
			 imgview_albumImg.setImage(image);
			 String str_filePath = "file:"+filePath;
			// userDirectoryString = "file:\\\\Sem-pc\\공유폴더\\Clap\\img\\singer"; //화면 출력 시 절대경로로 이미지를 읽기위해서
			 img_path = str_filePath;
			 
		 }catch (Exception e) {
		 }
	}

	@FXML //업데이트 하는 버튼
	
	
	public void updateAlbum() {
		
		if(txt_albumName.getText().isEmpty()) {
			errMsg("앨범명은 필수 입력 사항입니다.");
			return;
		}
		
		if(txt_singerName.getText().isEmpty()) {
			errMsg("가수는 필수 입력 사항입니다.");
			return;
		}
		
		if((datePicker_saledate.getValue()+"").isEmpty()) {
			errMsg("발매일은 필수 입력 사항입니다.");
			return;
		}
		
		AlbumVO aVO = new AlbumVO();
		
		aVO.setAlb_no(albumNo);
		aVO.setAlb_name(txt_albumName.getText());
		aVO.setSing_no(label_singerNO.getText());
		aVO.setAlb_image(img_path);
		aVO.setAlb_saledate(datePicker_saledate.getValue()+"");
		
		aVO.setAlb_sale_enter(txt_saleEnter.getText());
		aVO.setAlb_entertain(txt_entertain.getText());
		aVO.setAlb_intro(txt_intro.getText());
		
		try {
			ias.updateAlbumInfo(aVO);
		} catch (RemoteException e) {
			
			e.printStackTrace();
		}
		
		chagePage();
		
	}
	
	@FXML
	public void btn_selectSinger() {
		try {
		
			FXMLLoader loader = new FXMLLoader(getClass().getResource("selectSingerUpd.fxml"));
			Parent selectSinger= loader.load(); 
			SelectSingerUpdController cotroller = loader.getController();
			
			cotroller.setcontroller(this);
			
			
			Stage stage = new Stage();
			Scene scene = new Scene(selectSinger);
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			Stage primaryStage = (Stage)label_singerNO.getScene().getWindow();
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
			AlbumDetailController.albumNo = albumNo;//앨범번호를 변수로 넘겨줌
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource("AlbumDetail.fxml"));// init실행됨
			Parent albumDetail= loader.load(); 
			main.getChildren().removeAll();
			main.getChildren().setAll(albumDetail);
			
			
		} catch (IOException e1) {
			e1.printStackTrace();
		} 
	}
}
