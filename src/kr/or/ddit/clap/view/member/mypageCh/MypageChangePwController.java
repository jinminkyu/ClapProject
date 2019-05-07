package kr.or.ddit.clap.view.member.mypageCh;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.GeneralSecurityException;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jfoenix.controls.JFXPasswordField;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.control.Button;
import kr.or.ddit.clap.main.LoginSession;
import kr.or.ddit.clap.service.mypage.IMypageService;
import kr.or.ddit.clap.view.join.AES256Util;
import kr.or.ddit.clap.vo.member.MemberVO;

public class MypageChangePwController implements Initializable{
	
	static Stage mainDialog = new Stage(StageStyle.DECORATED);

	private Registry reg;
	private IMypageService ims;
	@FXML JFXPasswordField textF_NowPw;
    @FXML JFXPasswordField textF_NewPw;
	@FXML JFXPasswordField textF_NewPwCh;
	@FXML Button btn_Ok;
	@FXML Button btn_Cl;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		
		try {
			reg = LocateRegistry.getRegistry("localhost", 8888);
			ims = (IMypageService) reg.lookup("mypage");
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
		
		btn_Ok.setOnAction(e1->{
			AES256Util aes = null;
			try {
				aes = new AES256Util();
			} catch (UnsupportedEncodingException e11) {
				e11.printStackTrace();
			}
			
			String NowencryptedPw = ""; // 암호화된 pw
			try {
				NowencryptedPw = aes.encrypt(textF_NowPw.getText());
			} catch (UnsupportedEncodingException | GeneralSecurityException e12) {
				e12.printStackTrace();
			}
			
			String NowdecryptedPw = ""; // 복호화시킨 pw
			try {
				NowdecryptedPw = aes.decrypt(NowencryptedPw);
			} catch (UnsupportedEncodingException | GeneralSecurityException e13) {
				e13.printStackTrace();
			}
			
			String user_id = LoginSession.session.getMem_id();
			MemberVO vo = new MemberVO();
			vo.setMem_id(user_id);
			MemberVO vo2 = new MemberVO();

			try {
				vo2 = ims.select(vo);
				
			} catch (RemoteException e) {
				System.out.println("에러입니다");
				e.printStackTrace();
			} 
			
			
			
			
			//암호화하여 커밋
			
			if(NowencryptedPw.equals(vo2.getMem_pw())) {
				Pattern p = Pattern.compile("(^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[`~!@#$%^&*?]).{8,}$)");
				Matcher m = p.matcher(textF_NewPw.getText());
				if(!m.find()) {
					warning("변경하실 패스워드를 형식에 맞게 입력해주세요.", "");
					return;
				}
				
				if(textF_NewPw.getText().equals(textF_NewPwCh.getText())) {
					
					String encryptedPw = "";
					try {
						encryptedPw = aes.encrypt(textF_NewPwCh.getText());
					} catch (UnsupportedEncodingException | GeneralSecurityException e3) {
						// TODO Auto-generated catch block
						e3.printStackTrace();
					}
					try {
						String decryptedPw = aes.decrypt(encryptedPw);
					} catch (UnsupportedEncodingException | GeneralSecurityException e3) {
						// TODO Auto-generated catch block
						e3.printStackTrace();
					}
					
					String encryptedPwCheck = "";
					try {
						encryptedPwCheck = aes.encrypt(textF_NewPwCh.getText());
					} catch (UnsupportedEncodingException | GeneralSecurityException e3) {
						// TODO Auto-generated catch block
						e3.printStackTrace();
					}
					try {
						String decryptedPwCheck = aes.decrypt(encryptedPwCheck);
					} catch (UnsupportedEncodingException | GeneralSecurityException e3) {
						// TODO Auto-generated catch block
						e3.printStackTrace();
					}
					
					vo.setMem_pw(encryptedPw);
					try {
						int result = ims.updatePw(vo);
					} catch (RemoteException e) {
						e.printStackTrace();
					}
					infoMsg("비밀번호 변경 완료", "");
					textF_NowPw.clear();
					textF_NewPw.clear();
					textF_NewPwCh.clear();
				}
				
				
			}else if(!textF_NowPw.getText().equals(vo2.getMem_pw())) {
				warning("잘못된 패스워드를 입력하셨습니다.", "");
				return;
			}else if(textF_NowPw.getText().isEmpty() || textF_NewPw.getText().isEmpty() || textF_NewPwCh.getText().isEmpty()) {
				warning("작업 오류", "빈 항목이 있습니다.");
				return;
			}	else if(!textF_NewPw.getText().equals(textF_NewPwCh.getText())) {
				warning("변경하실 패스워드를 정확히 입력해주세요.", "");
				return;
			}
		});//btn_ok
		
	btn_Cl.setOnAction(e2->{
		try {
			Parent root = FXMLLoader.load(getClass().getResource("../../../main/MusicMain.fxml"));
			Scene scene = new Scene(root);
			mainDialog.setTitle("모여서 각잡고 코딩 - clap");
			
			mainDialog.setScene(scene);
			mainDialog.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		});
	}
	
	
	
	public void infoMsg(String headerText, String msg) {
		Alert infoAlert = new Alert(AlertType.INFORMATION);
		infoAlert.setTitle("정보 확인");
		infoAlert.setHeaderText(headerText);
		infoAlert.setContentText(msg);
		infoAlert.showAndWait();
	}
	
	public void warning(String headerText, String msg) {
		 Alert alertWarning = new Alert(AlertType.WARNING);
		 alertWarning.setTitle("warning");
		 alertWarning.setHeaderText(headerText);
		 alertWarning.setContentText(msg);
		 alertWarning.showAndWait();
	}

}
