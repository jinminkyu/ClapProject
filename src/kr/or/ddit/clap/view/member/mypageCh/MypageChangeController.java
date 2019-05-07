package kr.or.ddit.clap.view.member.mypageCh;

import java.io.IOException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXTextField;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import kr.or.ddit.clap.main.LoginSession;
import kr.or.ddit.clap.service.mypage.IMypageService;
import kr.or.ddit.clap.vo.member.MemberVO;
import javafx.scene.layout.AnchorPane;

public class MypageChangeController implements Initializable {

	private Registry reg;
	private IMypageService ims;
	@FXML
	Label label_Id;
	@FXML
	Label label_Tel;
	@FXML
	JFXTextField em1;
	@FXML
	JFXTextField em2;
	@FXML
	ComboBox<String> com_em;
	static Stage tel = new Stage(StageStyle.DECORATED);
	String tell = "";
	@FXML
	Button ok;
	@FXML
	Button cl;
	@FXML
	Button btn_PwCh;
	@FXML
	AnchorPane contents;
	@FXML
	Button btn_JoinOut;
	@FXML
	AnchorPane head;
	@FXML Button btn_Info;

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

		String user_id = LoginSession.session.getMem_id();
		label_Id.setText(user_id);
		MemberVO vo = new MemberVO();
		vo.setMem_id(user_id);
		MemberVO vo2 = new MemberVO();

		try {
			vo2 = ims.select(vo);
		} catch (RemoteException e) {
			System.out.println("에러입니다");
			e.printStackTrace();
		}

		label_Tel.setText(vo2.getMem_tel().substring(0, 3) + " -    " + vo2.getMem_tel().substring(3, 7));
		// 이메일 뿌주기
		String[] str = vo2.getMem_email().split("@");
		em1.setText(str[0]);
		em2.setText(str[1]);
		// 이메일값 변경
		com_em.getItems().addAll("naver.com", "gmail.com", "nate.com", "hanmail.net", "chol.com", "empal.com",
				"yahoo.co.kr");
		com_em.setValue("----");
		com_em.setOnAction(e -> {
			em2.setText(com_em.getValue().toString());
		});

		ok.setOnAction(ee -> {

			if (em1.getText().isEmpty() || em2.getText().isEmpty()) {
				errMsg("작업 오류", "빈 항목이 있습니다.");
				return;

			} else {

				vo.setMem_id(user_id);
				vo.setMem_email(em1.getText() + "@" + em2.getText());

				try {
					int result = ims.updateEmail(vo);
				} catch (RemoteException e) {
					System.out.println("에러입니다");
					e.printStackTrace();
				}
				infoMsg("완료", "회원정보 수정이 완료되었습니다.");
				Parent root1;
				try {
					root1 = FXMLLoader.load(getClass().getResource("mypageCh_Info.fxml"));
					head.getChildren().removeAll();
					head.getChildren().setAll(root1);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		//기본정보 변겨 ㅇ클릭시
		btn_Info.setOnAction(e4->{
			Parent root;
			try {
				root = FXMLLoader.load(getClass().getResource("mypageCh_Info.fxml"));
				head.getChildren().removeAll();
				head.getChildren().setAll(root);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});

		// 비밀번호 변경 클릭시
		btn_PwCh.setOnAction(e -> {
			Parent root;
			try {
				root = FXMLLoader.load(getClass().getResource("pwCh.fxml"));
				contents.getChildren().removeAll();
				contents.getChildren().setAll(root);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		});

		// 회원탈퇴 클릭시
		btn_JoinOut.setOnAction(e3 -> {
			Parent root;
			try {
				root = FXMLLoader.load(getClass().getResource("joinout.fxml"));
				contents.getChildren().removeAll();
				contents.getChildren().setAll(root);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});

	}

	public void btn_telCh() throws IOException { // 변경하기 클릭시
		Parent root = FXMLLoader.load(getClass().getResource("telCh.fxml"));
		Scene scene = new Scene(root);
		tel.setTitle("모여서 각잡고 코딩 - clap");
		tel.setScene(scene);
		tel.show();
		ComboBox<String> combo = (ComboBox<String>) root.lookup("#combo_tel");

		combo.getItems().addAll("010", "016", "018", "011", "017", "019");
		combo.setValue(combo.getItems().get(0));
		combo.setOnAction(e -> {
			combo.getValue().toString();
		});
		TextField textF_Tel1 = (TextField) root.lookup("#textF_Tel1");
		TextField testF_Tel2 = (TextField) root.lookup("#testF_Tel2");

		Button btn_Ok = (Button) root.lookup("#btn_Ok");
		btn_Ok.setOnAction(ee -> {
			if (!textF_Tel1.getText().matches("^[0-9]*$") || !testF_Tel2.getText().matches("^[0-9]*$")
					|| textF_Tel1.getText().length() > 4 || testF_Tel2.getText().length() > 4) {
				warning("휴대폰 번호가 잘못되었습니다", "다시입력해주세요");
				combo.setValue("");
				textF_Tel1.clear();
				testF_Tel2.clear();
			}else if(textF_Tel1.getText().isEmpty() || testF_Tel2.getText().isEmpty() ) {
				errMsg("작업 오류", "빈 항목이 있습니다.");
				return;
			}

			else {
				tell += combo.getValue().toString();
				tell += textF_Tel1.getText();
				tell += testF_Tel2.getText();

				String user_id = LoginSession.session.getMem_id();
				MemberVO vo = new MemberVO();
				vo.setMem_id(user_id);
				vo.setMem_tel(tell);

				try {
					int result = ims.updateTel(vo);
					if (result > 0) {
						tel.close();
					}
				} catch (RemoteException e) {
					System.out.println("에러입니다");
					e.printStackTrace();
				}

				Parent root1;
				try {
					root1 = FXMLLoader.load(getClass().getResource("mypageCh_Info.fxml"));
					head.getChildren().removeAll();
					head.getChildren().setAll(root1);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}

		});
		Button btn_Cl = (Button) root.lookup("#btn_Cl");
		btn_Cl.setOnAction(ee -> tel.close());
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

	public void warning(String headerText, String msg) {
		Alert alertWarning = new Alert(AlertType.WARNING);
		alertWarning.setTitle("warning");
		alertWarning.setHeaderText(headerText);
		alertWarning.setContentText(msg);
		alertWarning.showAndWait();
	}
}
