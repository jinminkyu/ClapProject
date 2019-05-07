package kr.or.ddit.clap.view.message;

import java.io.IOException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXTextField;

import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import kr.or.ddit.clap.main.LoginSession;
import kr.or.ddit.clap.service.message.IMessageService;
import kr.or.ddit.clap.vo.support.MessageVO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

public class MessageTextController implements Initializable{

	private Registry reg;
	private IMessageService imsgs;
	
	public static String msgno;
	private AnchorPane contents;
	private String  user_id =LoginSession.session.getMem_id();

	public static Stage mes = new Stage();
	
	@FXML AnchorPane main;
	@FXML Label textF_Userid;
	@FXML Label textF_Title;
	@FXML Label la_SendDate;
	@FXML Label textA_content;
	
	private ObservableList<MessageVO> msgList;
	
	public void givePane(AnchorPane contents) {
		this.contents = contents;
	}	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			reg = LocateRegistry.getRegistry("localhost", 8888);
			imsgs = (IMessageService) reg.lookup("message");
			
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
		MessageVO vo = new MessageVO();
		vo.setMsg_no(msgno);
		try {
			msgList=FXCollections.observableArrayList(imsgs.selectMessage(vo));
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		//값셋팅
		textF_Userid.setText(user_id);
		textA_content.setText(msgList.get(0).getMsg_content());
		textF_Title.setText(msgList.get(0).getMsg_title());
		la_SendDate.setText(msgList.get(0).getMsg_send_date().substring(0, 10));
	}
	public void btn_Ok() throws IOException {
		Stage dialogStage = (Stage) la_SendDate.getScene().getWindow();
		dialogStage.close();
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("mestable.fxml"));// init실행됨
		Parent messageText= loader.load(); 
		Scene scene = new Scene(messageText);
		
		mes.setTitle("Meaage");
		mes.setScene(scene);
		mes.show();
		
		
		}


}
