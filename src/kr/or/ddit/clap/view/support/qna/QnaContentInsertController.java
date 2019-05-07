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
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Alert.AlertType;
import kr.or.ddit.clap.main.LoginSession;
import kr.or.ddit.clap.service.qna.IQnaService;
import kr.or.ddit.clap.vo.member.MemberVO;
import kr.or.ddit.clap.vo.support.QnaVO;

public class QnaContentInsertController implements Initializable {
	
	private Registry reg;
	private IQnaService iqs;
	public QnaVO qVO = new QnaVO();
	//public MemberVO mVO;
	
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
	JFXButton btn_Add;
	@FXML
	AnchorPane main;
	
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
		
		
		Com_Type.getItems().addAll("이용방법","벨/링","선물하기","이벤트","결제/요금제","음원오류","14세 미만 가입/승인","인증 문의","회원/로그인","다운로드","오류문의","개선사항","기타");
		Com_Type.setValue("선택하세요");
		
		//MemberVO mVO = new MemberVO();
		
		Lb_MemId.setText(LoginSession.session.getMem_id()); //현재 로그인한 사용자
		Text_QnaMemTel.setText(LoginSession.session.getMem_tel());
		Text_QnaMemEmail.setText(LoginSession.session.getMem_email());
		//Text_QnaMemTel.setText(mVO.getMem_tel());
		//Text_QnaMemEmail.setText(mVO.getMem_email());
		//연락처 , 이메일 db에서 가져오기
		
		btn_Add.setOnAction(e -> {
			
			if(Text_QnaTitle.getText().isEmpty() || Text_QnaContent.getText().isEmpty()) {
				errMsg("작업오류" , "빈 항목이 있습니다.");
				return;
			} else {
				qVO.setQna_type(Com_Type.getValue());
				qVO.setMem_tel(Text_QnaMemTel.getText());
				qVO.setMem_email(Text_QnaMemEmail.getText());
				qVO.setQna_title(Text_QnaTitle.getText());
				qVO.setQna_content(Text_QnaContent.getText());
				qVO.setMem_id(LoginSession.session.getMem_id());
				qVO.setQna_view_cnt("0");
				
				try {
					int flag=iqs.insertQna(qVO);
				} catch (RemoteException ee) {
					
					ee.printStackTrace();
				}
				
				infoMsg("등록 완료", "문의사항 - 글 작성이 완료되었습니다.");
				
				Parent root1;
				try {
					root1 = FXMLLoader.load(getClass().getResource("QnaMenuList.fxml"));
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
