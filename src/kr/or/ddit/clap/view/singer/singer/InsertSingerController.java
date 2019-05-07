/**
 * 가수관리 수정화면 컨트롤러
 * @author Hansoo
 * 
 */
package kr.or.ddit.clap.view.singer.singer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import kr.or.ddit.clap.service.singer.ISingerService;
import kr.or.ddit.clap.vo.singer.SingerVO;
import javafx.scene.layout.AnchorPane;

public class InsertSingerController implements Initializable {

	@FXML
	ImageView imgview_singImg;
	@FXML
	JFXTextField txt_name;
	@FXML
	JFXComboBox<String> combo_actType;
	@FXML
	JFXComboBox<String> combo_actEra;
	@FXML
	JFXComboBox<String> combo_DebutEra;
	@FXML
	JFXTextField txt_debutMus;
	@FXML
	JFXTextField txt_nation;
	@FXML
	Label label_LikeCnt;
	@FXML
	JFXButton btn_chageImg;
	@FXML
	TextArea txt_intro;
	@FXML AnchorPane main;
	
	private FileChooser fileChooser;
	private File filePath;
	private String img_path;
	private Registry reg;
	private ISingerService iss;
	public static AnchorPane contents;
	@FXML ImageView imgview_eventImg;
	

	

	//ShowSingerList.fxml는 VBOX를 포함한 전부이기 때문에
	//현재 씬의 VBox까지 모두 제거 후   ShowSingerList를 불러야함.
	public void givePane(AnchorPane contents) {
		this.contents = contents;
		System.out.println("contents 적용완료");
	}
	
	// 전 화면에 있는 데이터를 그대로 가져와  세팅해주는 메서드
	public void initData() {
		System.out.println("initData");
		
		img_path = "file:\\\\\\\\Sem-pc\\\\공유폴더\\\\Clap\\\\img\\\\noImg.png";// 처음 아무것도 선택하지 않았을 떄의 이미지
		Image img = new Image(img_path); 
		imgview_singImg.setImage(img);
	
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			reg = LocateRegistry.getRegistry("localhost", 8888);
			iss = (ISingerService) reg.lookup("singer");
			initData();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
		
		combo_actType.getItems().add("여성/솔로");
		combo_actType.getItems().add("여성/그룹");
		combo_actType.getItems().add("남성/솔로");
		combo_actType.getItems().add("남성/그룹");
		combo_actType.getItems().add("혼성/그룹");
		combo_actEra.getItems().add("2010년대");
		combo_actEra.getItems().add("2000년대");
		combo_actEra.getItems().add("1990년대");
		combo_actEra.getItems().add("1980년대");
		combo_actEra.getItems().add("1970년대");
		combo_actEra.getItems().add("1960년대");

		for (int i = 2019; i > 1959; i--) {
			String year = i + "년";
			combo_DebutEra.getItems().add(year);
		}
	}

	@FXML
	public void btn_chageImg(ActionEvent event) {
		 Stage stage =  (Stage) ((Node)event.getSource()).getScene().getWindow();
		 fileChooser = new FileChooser();
		 fileChooser.setTitle("Open image");
		 
		 //사용자에 화면에 해당 디렉토리가 기본값으로 보여짐 
		 //String userDirectoryString = System.getProperty("user.home") + "\\Pictures"; 기본위치
		 String userDirectoryString = "\\\\Sem-pc\\공유폴더\\Clap\\img\\singer";
		 
		 System.out.println("userDirectoryString:" + userDirectoryString);
		 File userDirectory = new File(userDirectoryString); 
		 
		 if(!userDirectory.canRead()) { //예외시?
			 userDirectory = new File("c:/");
		 }
		 
		 fileChooser.setInitialDirectory(userDirectory);; 
		 
		 this.filePath = fileChooser.showOpenDialog(stage);
		 
		
		 //이미지를 새로운 이미지로 바꿈
		 try {
			 BufferedImage bufferedImage = ImageIO.read(filePath);
			 Image image =  SwingFXUtils.toFXImage(bufferedImage, null);
			 imgview_singImg.setImage(image);
			 System.out.println("파일경로:" + filePath);
			 String str_filePath = "file:"+filePath;
			// userDirectoryString = "file:\\\\Sem-pc\\공유폴더\\Clap\\img\\singer"; //화면 출력 시 절대경로로 이미지를 읽기위해서
			 img_path = str_filePath;
			 System.out.println(img_path);
			 
		 }catch (Exception e) {
			// System.out.println(e.getMessage());
			 //e.printStackTrace();
			 System.out.println("이미지를 선택하지 않았습니다.");
		 }
	}

	@FXML //업데이트 하는 버튼
	public void insertSinger() {
		
		if(txt_name.getText().isEmpty()) {
			errMsg("가수이름은 필수 입력 사항입니다.");
			return;
		}
		
		if(combo_actType.getValue().isEmpty()) {
			errMsg("활동유형은 필수 입력 사항입니다.");
			return;
		}
		
		if(combo_actEra.getValue().isEmpty()) {
			errMsg("활동연대은 필수 입력 사항입니다.");
			return;
		}
		
		if(combo_DebutEra.getValue().isEmpty()) {
			errMsg("데뷔년도는 필수 입력 사항입니다.");
			return;
		}
		
		if(txt_debutMus.getText().isEmpty()) {
			errMsg("데뷔곡은 필수 입력 사항입니다.");
			return;
		}
		if(txt_nation.getText().isEmpty()) {
			errMsg("국적은 필수 입력 사항입니다.");
			return;
		}
		
		
		
		
		
		SingerVO sVO = new SingerVO();
		
		sVO.setSing_name(txt_name.getText());
		sVO.setSing_nation(txt_nation.getText());
		
		//콤보박스 text값 가져오는법
		sVO.setSing_act_type(combo_actType.getValue()); 
		sVO.setSing_act_era(combo_actEra.getValue());
		sVO.setSing_debut_era(combo_DebutEra.getValue());
		sVO.setSing_debut_mus(txt_debutMus.getText());
		sVO.setSing_image(img_path);
		sVO.setSing_intro(txt_intro.getText());
		try {
			int flag=iss.insertSinger(sVO);
			if(flag==1) {
			System.out.println("가수등록 완료");
			}
			else {
				System.out.println("가수등록 실패");
			}
		} catch (RemoteException e) {
			
			e.printStackTrace();
		}
		
			//바뀔 화면(FXML)을 가져옴
		
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
	
	//화면을 조회창으로 이동해주는 메서드
	public void chagePage() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("ShowSingerList.fxml"));// init실행됨
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
