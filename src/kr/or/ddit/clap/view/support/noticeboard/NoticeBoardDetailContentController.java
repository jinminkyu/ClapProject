/**
 *  문의한 내용의 상세 내용을 나타내는 페이지를 출력하는 화면 controller
 *  
 *  @author hanhwa
 */
package kr.or.ddit.clap.view.support.noticeboard;

import java.io.IOException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import kr.or.ddit.clap.main.LoginSession;
import kr.or.ddit.clap.service.noticeboard.INoticeBoardService;
import kr.or.ddit.clap.vo.support.NoticeBoardVO;
import com.jfoenix.controls.JFXButton;
import javafx.scene.control.TextArea;

public class NoticeBoardDetailContentController implements Initializable {
	
	public static String NoticeNo;
	private Registry reg;
	private INoticeBoardService ins;
	
	@FXML
	Text Text_NtcNo;
	@FXML
	Text Text_NtcTitle;
	@FXML
	Text Text_NtcDate;
	@FXML
	Text Text_NtcContent;
	@FXML
	AnchorPane n_main;
	@FXML
	JFXButton btn_del;
	@FXML
	JFXButton btn_upd;
	@FXML
	Text Text_NtcId;
	@FXML
	TextArea text_cont;
	
	
	public NoticeBoardVO nVO;
	
	

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		text_cont.setDisable(true);
		
		if(LoginSession.session!= null) {
			if(LoginSession.session.getMem_auth().equals("t")){
				btn_del.setVisible(true);
				btn_upd.setVisible(true);
			} else {
				btn_del.setVisible(false);
				btn_upd.setVisible(false);
			}
			
		}else {
			btn_del.setVisible(false);
			btn_upd.setVisible(false);
		}
		
		
		
		
		
		
		try {
			System.out.println("noticeNO??" + NoticeNo);
			//reg로 IQnaService객체를 받아옴
			reg = LocateRegistry.getRegistry("localhost", 8888);  
			ins = (INoticeBoardService) reg.lookup("notice");
			nVO = ins.NoticeBoardDetailContent(NoticeNo);
			System.out.println("noticeNO??ff>>" + nVO.getNotice_no());
			//파라미터로 받은 정보를 PK로 상세정보를 가져옴
		} catch (RemoteException e) {
			System.out.println("Detail 1");
			e.printStackTrace();
		} catch (NotBoundException e) {
			System.out.println(2);
			e.printStackTrace();
		}
		
		Text_NtcNo.setText(nVO.getNotice_no());
		Text_NtcTitle.setText(nVO.getNotice_title());
		Text_NtcDate.setText(nVO.getNotice_indate());
		Text_NtcContent.setText(nVO.getNotice_content());
		Text_NtcId.setText(nVO.getMem_id());
		
		
		btn_del.setOnMouseClicked(e -> {
			
			//Alert창을 출력해 정말 삭제할 지 물어봄
			try {
				if(0>alertConfrimDelete()) {
					return;
				}
				
				int cnt = ins.deleteNoticeContent(NoticeNo);
				
				
				
			} catch(RemoteException ee) {
				ee.printStackTrace();
			}
			
			
		});
		
		
		btn_upd.setOnAction(e -> {
			try {
				System.out.println("수 정");
				// 바뀔 화면(FXML)을 가져옴
				NoticeBoardUpdateController.NoticeNo = NoticeNo;
				FXMLLoader loader = new FXMLLoader(getClass().getResource("NoticeBoardUpdate.fxml"));
				Parent UpdateNotice = loader.load();
				NoticeBoardUpdateController controller = loader.getController();
				controller.initData(nVO );
				n_main.getChildren().removeAll();
				n_main.getChildren().setAll(UpdateNotice);
				
				
			} catch(IOException ee) {
				ee.printStackTrace();
			}
		});
		
		
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
							root1 = FXMLLoader.load(getClass().getResource("NoticeMenuList.fxml"));
							n_main.getChildren().removeAll();
							n_main.getChildren().setAll(root1);
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
