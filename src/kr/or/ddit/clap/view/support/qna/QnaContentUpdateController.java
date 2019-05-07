package kr.or.ddit.clap.view.support.qna;

import java.io.IOException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import kr.or.ddit.clap.main.LoginSession;
import kr.or.ddit.clap.service.qna.IQnaService;
import kr.or.ddit.clap.vo.support.QnaVO;

public class QnaContentUpdateController implements Initializable {
	
	private Registry reg;
	private IQnaService iqs;
	private QnaVO qVO = new QnaVO();
	
	@FXML
	ComboBox<String> Com_Type;
	@FXML
	JFXTextField Text_QnaMemTel;
	@FXML
	JFXTextField Text_QnaMemEmail;
	@FXML
	JFXTextField Text_QnaTitle;
	@FXML
	JFXTextArea Text_QnaContent;
	@FXML
	Label Lb_MemId;
	@FXML
	JFXButton btn_Upd;
	@FXML
	AnchorPane main;
	
	public static String ContentNo; // PK값 받기
	
	// 전 화면에 있는 데이터를 그대로 가져와  세팅해주는 메서드
	public void initData(QnaVO qVO) {
		System.out.println("Qna-initData");
		
		Com_Type.setValue(qVO.getQna_type());
		Lb_MemId.setText(LoginSession.session.getMem_id()); //현재 로그인한 사용자
		Text_QnaMemTel.setText(LoginSession.session.getMem_tel());
		Text_QnaMemEmail.setText(LoginSession.session.getMem_email());
		Text_QnaTitle.setText(qVO.getQna_title());
		Text_QnaContent.setText(qVO.getQna_content());
	}
	
	
	
	
	

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		try {
			reg = LocateRegistry.getRegistry("localhost", 8888);
			iqs = (IQnaService) reg.lookup("qna");
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
		
		
		Com_Type.getItems().addAll("이용방법","벨/링","선물하기","이벤트","결제/요금제","음원오류",
				"14세 미만 가입/승인","인증 문의","회원/로그인","다운로드","오류문의","개선사항","기타");
		Com_Type.setValue("선택하세요");
		
		
		btn_Upd.setOnAction(e -> {
			
			if(Text_QnaTitle.getText().isEmpty() || Text_QnaContent.getText().isEmpty()) {
				errMsg("작업오류" , "빈 항목이 있습니다.");
				return;
			} else {
				
				qVO.setQna_no(ContentNo);
				qVO.setQna_type(Com_Type.getValue());
				qVO.setMem_tel(Text_QnaMemTel.getText());
				qVO.setMem_email(Text_QnaMemEmail.getText());
				qVO.setQna_title(Text_QnaTitle.getText());
				qVO.setQna_content(Text_QnaContent.getText());
				qVO.setMem_id(LoginSession.session.getMem_id());
				
				try {
					int flag=iqs.updateQna(qVO);
				} catch (RemoteException ee) {
					
					ee.printStackTrace();
				}
				
				infoMsg("수정 완료", "문의사항 - 글 수정이 완료되었습니다.");
				
				Parent root1;
				try {
					root1 = FXMLLoader.load(getClass().getResource("QnaDetailContent.fxml"));
					main.getChildren().removeAll();
					main.getChildren().setAll(root1);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
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
	
	

}
