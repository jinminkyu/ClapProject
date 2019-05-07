package kr.or.ddit.clap.view.member.mypageCh;

import java.io.IOException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ResourceBundle;

import javafx.beans.property.BooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import kr.or.ddit.clap.main.LoginSession;
import kr.or.ddit.clap.service.mypage.IMypageService;
import kr.or.ddit.clap.vo.member.MemberVO;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MypageJoinOutController implements Initializable{
	
	static Stage mainDialog = new Stage(StageStyle.DECORATED);
	private Registry reg;
	private IMypageService ims;
	@FXML Button btn_Ok;
	@FXML Button btn_Cl;
	@FXML CheckBox combo_Check;
	LoginSession ls = new LoginSession();
	
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
		
		
		btn_Ok.setOnAction(e3->{
			Boolean b=(combo_Check.selectedProperty().getValue());
			if(b){
				String user_id=LoginSession.session.getMem_id();
				MemberVO vo = new MemberVO();
				vo.setMem_id(user_id);
				vo.setMem_del_tf("t");
				try {
					int res= ims.updateDelTF(vo);
				} catch (RemoteException e1) {
					
					e1.printStackTrace();
				}
				
				infoMsg("회원탈퇴가 완료 되었습니다.", "더 노력하는 CLAP가 되겠습니다.\n그동안 이용해 주셔서 감사합니다");
				ls.session = null;
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
				
			}else {
				infoMsg("위 내역에 동의하셔야 탈퇴하실 수 있습니다.", "");
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

}
