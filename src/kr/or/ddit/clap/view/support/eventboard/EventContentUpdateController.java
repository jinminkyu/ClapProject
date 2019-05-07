package kr.or.ddit.clap.view.support.eventboard;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import kr.or.ddit.clap.main.LoginSession;
import kr.or.ddit.clap.service.eventboard.IEventBoardService;
import kr.or.ddit.clap.vo.support.EventBoardVO;

public class EventContentUpdateController implements Initializable  {

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
	JFXButton btn_updateImg;
	@FXML
	JFXButton btn_update;
	@FXML
	JFXButton btn_delete;
	@FXML
	JFXTextArea text_Content;
	@FXML
	Label label_no;
	
	private FileChooser fileChooser;
	private File filePath;
	private String img_path;
	private Registry reg;
	private IEventBoardService ies;
	private static AnchorPane contents;
	public static String eventNo; // 파라미터로 받은 선택한 글 번호

	//String d = today;
	//public static AnchorPane contents;
	
	public void givePane(AnchorPane contents) {
		this.contents = contents;
		System.out.println("contents 적용 완료");
	}
	
	
	// 전 화면에 있는 데이터를 그대로 가져와  세팅해주는 메서드 // 
	public void initData(String eventNo, String eventImg, String eventTitle, String eventCont, String eventSdate, String eventEdate) {
		
		img_path = eventImg; //이미지경로를 전역에 저장
		Image img = new Image(img_path); //이미지 객체등록
		imgview_eventImg.setImage(img);
		label_no.setText(eventNo);
		text_Title.setText(eventTitle);
		date_Start.setPromptText(eventSdate);
		date_End.setPromptText(eventEdate);
		text_Content.setText(eventCont);
		label_Id.setText(LoginSession.session.getMem_id());
		
	}
	
	/*public void initData() {
		System.out.println("initData");
		
		img_path = eVO.getEvent_image(); //이미지경로를 전역에 저장
		Image img = new Image(img_path); //이미지 객체등록
		imgview_eventImg.setImage(img);
		
		text_Title.setText(eVO.getEvent_title());
		date_Start.setPromptText(eVO.getEvent_sdate());
		date_End.setPromptText(eVO.getEvent_edate());
		label_Id.setText(LoginSession.session.getMem_id());
		text_Content.setText(eVO.getEvent_content());
		
		
	}*/
	
	
	
	

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		//System.out.println("글 번호 : " + eventNo);
		
		try {
			reg = LocateRegistry.getRegistry("localhost", 8888);
			ies = (IEventBoardService) reg.lookup("eventboard");
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
		
		
		btn_updateImg.setOnAction(e -> {
			
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
		
		
		
		btn_update.setOnAction(e -> {
			
			if(text_Title.getText().isEmpty() || text_Content.getText().isEmpty()) {
				
				errMsg("작업오류", "빈 항목이 있습니다.");
				return;
			}
			
			EventBoardVO eVO = new EventBoardVO();
			eVO.setEvent_no(eventNo);
			eVO.setEvent_image(img_path);
			eVO.setEvent_title(text_Title.getText());
			eVO.setEvent_sdate(date_Start.getValue().toString());
			eVO.setEvent_edate(date_End.getValue().toString());
			eVO.setEvent_content(text_Content.getText());
			eVO.setMem_id(LoginSession.session.getMem_id());
			
			try {
				
				ies.updateEvent(eVO);
				System.out.println("update 완료");
				//업데이트 왜 안돼..
			} catch(RemoteException ee) {
				ee.printStackTrace();
			}
			
			infoMsg("update 완료", "이벤트 - update가 되었습니다.");
			
			Parent root1;
			try {
				root1 = FXMLLoader.load(getClass().getResource("EventClientShowList.fxml"));
				main.getChildren().removeAll();
				main.getChildren().setAll(root1);
				
			} catch(IOException eee) {
				eee.printStackTrace();
			}
			
		});
		
		btn_delete.setOnMouseClicked(e -> {
			
			//Alert창을 출력해 정말 삭제할 지 물어봄
			try {
				if(0>alertConfrimDelete()) {
					return;
				}
				int cnt = ies.deleteEvent(eventNo);
				
			} catch(RemoteException ee) {
				ee.printStackTrace();
			}
			
			
			//화면이동 
			Parent root1;
			try {
				root1 = FXMLLoader.load(getClass().getResource("EventShowList.fxml"));
				contents.getChildren().removeAll();
				contents.getChildren().setAll(root1);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
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
	
	//사용자가 확인을 누르면 1을 리턴 이외는 -1
	public int alertConfrimDelete() {
		Alert alertConfirm = new Alert(AlertType.CONFIRMATION);
	      
	      alertConfirm.setTitle("CONFIRMATION");
	      alertConfirm.setContentText("삭제하시면 복구가 불가능합니다.");
	      
	      // Alert창을 보여주고 사용자가 누른 버튼 값 읽어오기
	      ButtonType confirmResult = alertConfirm.showAndWait().get();
	      
	      if (confirmResult == ButtonType.OK) {
	         System.out.println("OK 버튼을 눌렀습니다.");
	         
	         Parent root1;
				try {
					root1 = FXMLLoader.load(getClass().getResource("EventClientShowList.fxml"));
					main.getChildren().removeAll();
					main.getChildren().setAll(root1);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
	         
	         return 1;
	      } else if (confirmResult == ButtonType.CANCEL) {
	         System.out.println("취소 버튼을 눌렀습니다.");
	         return -1;
	      }
	      return -1;
	}
	

}
