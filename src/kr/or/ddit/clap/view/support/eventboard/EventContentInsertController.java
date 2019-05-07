/**
 * 이벤트관리 등록 화면 컨트롤러
 * @author Hanhwa
 * 
 */
package kr.or.ddit.clap.view.support.eventboard;

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
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import kr.or.ddit.clap.main.LoginSession;
import kr.or.ddit.clap.service.eventboard.IEventBoardService;
import kr.or.ddit.clap.vo.support.EventBoardVO;




public class EventContentInsertController implements Initializable {

	@FXML
	AnchorPane main;
	@FXML
	ImageView imgview_eventImg;
	@FXML
	JFXTextField text_Title;
	@FXML
	JFXDatePicker date_Start;
	@FXML
	JFXDatePicker date_End;
	@FXML
	Label label_Id;
	@FXML
	JFXButton btn_insertImg;
	@FXML
	JFXButton btn_Insert;
	@FXML
	JFXButton btn_cancel;
	@FXML
	JFXTextArea text_Content;
	
	public static AnchorPane contents;
	private FileChooser fileChooser;
	private File filePath;
	private String img_path;
	private Registry reg;
	private IEventBoardService ies;
	
	//ShowSingerList.fxml는 VBOX를 포함한 전부이기 때문에
	//현재 씬의 VBox까지 모두 제거 후   ShowSingerList를 불러야함.
	public void givePane(AnchorPane contents) {
		this.contents = contents;
		System.out.println("contents 적용완료");
		
	}
	
	// 전 화면에 있는 데이터를 그대로 가져와  세팅해주는 메서드
	public void initData() {
		System.out.println("initData");
		
		img_path = "file:\\\\\\\\Sem-pc\\\\공유폴더\\\\Clap\\\\img\\\\noImg.png";
		Image img = new Image(img_path); 
		imgview_eventImg.setImage(img);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		try {
			reg = LocateRegistry.getRegistry("localhost", 8888);
			ies = (IEventBoardService) reg.lookup("eventboard");
			initData();
		} catch(RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
		
		label_Id.setText(LoginSession.session.getMem_id()); //현재 로그인한 사용자
		
		
		btn_insertImg.setOnAction(e -> {
			
			 //에러 해결하기
			Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
			fileChooser = new FileChooser();
			fileChooser.setTitle("Open image");
			
			//사용자에 화면에 해당 디렉토리가 기본값으로 보여짐 
			String userDirectoryString = "\\\\Sem-pc\\공유폴더\\Clap\\img\\event";
			
			System.out.println("userDirectoryString:" + userDirectoryString);
			File userDirectory = new File(userDirectoryString); 
			
			if(!userDirectory.canRead()) { //예외시?
				 userDirectory = new File("c:/");
			 }
			
			fileChooser.setInitialDirectory(userDirectory);
			
			this.filePath = fileChooser.showOpenDialog(stage);
			
			//이미지를 새로운 이미지로 바꿈
			 try {
				 BufferedImage bufferedImage = ImageIO.read(filePath);
				 Image image =  SwingFXUtils.toFXImage(bufferedImage, null);
				 imgview_eventImg.setImage(image);
				 System.out.println("파일경로 : " + filePath);
				 String str_filePath = "file:"+filePath;				 
				 img_path = str_filePath;
				 System.out.println(img_path);
			 } catch(Exception e1) {
				 System.out.println("이미지를 선택하지 않았습니다.");
			 }
			
		});
		
		
		btn_Insert.setOnAction(e -> {
			
			if(text_Title.getText().isEmpty() || text_Content.getText().isEmpty() ) {
				errMsg("작업오류" , "빈 항목이 있습니다.");
				return;
			}
			
			EventBoardVO eVO = new EventBoardVO();
			
			eVO.setEvent_image(img_path);
			eVO.setEvent_title(text_Title.getText());
			eVO.setEvent_sdate(date_Start .getValue().toString());
			eVO.setEvent_edate(date_End .getValue().toString());
			eVO.setMem_id(LoginSession.session.getMem_id());
			eVO.setEvent_content(text_Content.getText());
			// 글 등록 시 조회수를 0으로 맞춤
			eVO.setEvent_view_cnt("0");
			
			try {
				int flag = ies.insertEvent(eVO);
				
			} catch(RemoteException ee) {
				ee.printStackTrace();
			}
			
			infoMsg("등록 완료", "문의사항 - 글 작성이 완료되었습니다.");
			
			Parent root1;
			
			try {
				root1 = FXMLLoader.load(getClass().getResource("EventShowList.fxml"));
				contents.getChildren().removeAll();
				contents.getChildren().setAll(root1);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
		});
		
	}
		
		
		public void errMsg(String headerText, String msg) {
			Alert errAlert = new Alert(AlertType.ERROR);
			errAlert.setTitle("오류");
			errAlert.setHeaderText(headerText);
			errAlert.setContentText(msg);
			errAlert.showAndWait();
		}
		
		public void infoMsg(String headerText, String msg) {
			Alert infoAlert = new Alert(AlertType.INFORMATION);
			infoAlert.setTitle("정보 확인");
			infoAlert.setHeaderText(headerText);
			infoAlert.setContentText(msg);
			infoAlert.showAndWait();
		}
		
		/*//화면을 조회창으로 이동해주는 메서드
		public void chagePage() {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("EventShowList.fxml"));// init실행됨
			Parent eventList;
			try {
				eventList = loader.load();
				contents.getChildren().removeAll();
				contents.getChildren().setAll(eventList);
				
			} catch (IOException e) {
				e.printStackTrace();
			} 
		}*/

}
