package kr.or.ddit.clap.view.ticket.ticket;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXRadioButton;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import kr.or.ddit.clap.main.LoginSession;
import kr.or.ddit.clap.main.MusicMainController;
import kr.or.ddit.clap.service.login.ILoginService;
import kr.or.ddit.clap.service.mypage.IMypageService;
import kr.or.ddit.clap.service.ticket.ITicketService;
import kr.or.ddit.clap.view.join.AES256Util;
import kr.or.ddit.clap.vo.member.MemberVO;
import kr.or.ddit.clap.vo.ticket.TicketBuyListVO;

public class BuyTicketController implements Initializable{

	@FXML Label lb1;
	@FXML Label lb2;
	@FXML Label lb3;
	@FXML Label lb4;
	@FXML Label lb_vip;
	@FXML ImageView imgView;
	@FXML JFXRadioButton rb1;
	@FXML JFXRadioButton rb2;
	@FXML JFXRadioButton rb3;
	@FXML JFXRadioButton rb4;
	@FXML JFXRadioButton rb_card1;
	@FXML JFXRadioButton rb_card2;
	@FXML JFXComboBox<String> combo1;
	@FXML JFXComboBox<String> combo2;
	@FXML Button btn_ok;
	@FXML BorderPane pane;
	@FXML VBox box;
	VBox childBox = null;
	JFXProgressBar progress = null;
	
	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	SimpleDateFormat sdf2 = new SimpleDateFormat("~ yyyy-MM-dd");
	SimpleDateFormat sdf3 = new SimpleDateFormat("yy/MM/dd");
	TicketController tc = new TicketController();
	TicketBuyListVO vo = new TicketBuyListVO();
	LoginSession ls = new LoginSession();
	private ITicketService its;
	private IMypageService ms;
	private Registry reg;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			reg = LocateRegistry.getRegistry("localhost", 8888);
			its = (ITicketService) reg.lookup("ticket");
			ms = (IMypageService) reg.lookup("mypage");
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
		
		box.setOpacity(0);
		box.setLayoutY(620);
		pane.setOpacity(1);
		
		
		vo = new TicketBuyListVO();
		vo.setMem_id(ls.session.getMem_id());
		vo.setTicket_no(String.valueOf(tc.ticketInfo[4]));
		System.out.println("ticket no : "+tc.ticketInfo[4]);
		vo.setAccount_holder(ls.session.getMem_name());
		vo.setRefund_tf("f");
		Date now = new Date();
		
		lb1.setText((String) tc.ticketInfo[0]+" 이용권");
		lb2.setText((String) tc.ticketInfo[2]);
		
		System.out.println(tc.ticketInfo[3]);
		String lb4_str = "";
		if((boolean) tc.ticketInfo[3]) {
			lb3.setText(tc.ticketDate[0]);
			lb4_str = check_lb4(tc.ticketDate[0]);
			
		}else if(!(boolean) tc.ticketInfo[3]) {
			lb3.setText(sdf2.format(now).substring(2, sdf2.format(now).length()));
			String today = sdf2.format(now);
			lb4_str = check_lb4(today);
			lb4_str = "~" + lb4_str.substring(1, lb4_str.length());
		}
		lb4.setText(lb4_str);
		
		Image img = new Image(getClass().getResourceAsStream("../../../../../../../ticket90.jpg"));
		imgView.setImage(img);
		
		System.out.println(tc.ticketInfo[0]);
		
		ToggleGroup group = new ToggleGroup();
		ToggleGroup group2 = new ToggleGroup();
		rb1.setToggleGroup(group);
		rb2.setToggleGroup(group);
		rb3.setToggleGroup(group);
		rb4.setToggleGroup(group);
		rb_card1.setToggleGroup(group2);
		rb_card2.setToggleGroup(group2);
		
		rb1.setUserData("카드");
		rb2.setUserData("계좌이체");
		rb3.setUserData("무통장입금");
		rb4.setUserData("휴대폰");
		rb_card1.setUserData("개인");
		rb_card2.setUserData("법인");
		rb1.setSelected(true);
		rb_card1.setSelected(true);
		
		group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
			@Override
			public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
				if(group.getSelectedToggle().getUserData()!=null) {
					System.out.println(group.getSelectedToggle().getUserData());
				}
			}
		});
		group2.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
			@Override
			public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
				if(group2.getSelectedToggle().getUserData()!=null) {
					System.out.println(group2.getSelectedToggle().getUserData());
				}
			}
		});
		
		combo1.getItems().addAll("삼성카드", "비씨카드", "신한은행", "현대카드", "KB국민은행", 
				"롯데카드", "외환은행", "NH농협은행", "하나은행", "카카오뱅크", "케이뱅크");
		combo1.setValue("삼성카드");
		combo2.getItems().add("일시불");
		combo2.setValue("일시불");
		
		btn_ok.setOnAction(e->{
			Stage dialog = new Stage(StageStyle.DECORATED);
			if(dialog.getModality()==null) {
				dialog.initModality(Modality.APPLICATION_MODAL);				
			}
			if(dialog.getOwner()==null) {
				dialog.initOwner(btn_ok.getScene().getWindow());				
			}
			
			AnchorPane parent = null;
			try {
				parent = FXMLLoader.load(getClass().getResource("BuyTicketCard.fxml"));
			}catch(IOException ee) {
				ee.printStackTrace();
			}
			
			// 부모창에서 FXML로 만든 자식창의 컨트롤객체 얻기 ★★ "lookup()"	
			Label lblb1 = (Label) parent.lookup("#lblb1");
			Label lblb2 = (Label) parent.lookup("#lblb2");
			Label lb_card = (Label) parent.lookup("#lb_card");
			Label lb_error = (Label) parent.lookup("#lb_error");
			TextField txt1 = (TextField) parent.lookup("#txt1");
			TextField txt2 = (TextField) parent.lookup("#txt2");
			PasswordField txt3 = (PasswordField) parent.lookup("#txt3");
			PasswordField txt4 = (PasswordField) parent.lookup("#txt4");
			Button btn_okok = (Button) parent.lookup("#btn_okok");
			Button btn_cancel = (Button) parent.lookup("#btn_cancel");
			childBox = (VBox) parent.lookup("#childBox"); 
			progress = (JFXProgressBar) parent.lookup("#progress"); 
			
			lblb1.setText((String) tc.ticketInfo[2]);
			Date time = new Date();
			lblb2.setText(sdf1.format(time));
			lb_card.setText(combo1.getValue());
			
			vo.setCard_bank_name(combo1.getValue());
			vo.setTicket_buy_type(String.valueOf(group.getSelectedToggle().getUserData()));
			
			btn_okok.setOnAction(event->{
				if(txt1.getText().length()<4) {
					lb_error.setVisible(true);
					txt1.requestFocus();
					return;
				}
				if(txt2.getText().length()<4) {
					lb_error.setVisible(true);
					txt2.requestFocus();
					return;
				}
				if(txt3.getText().length()<4) {
					lb_error.setVisible(true);
					txt3.requestFocus();
					return;
				}
				if(txt4.getText().length()<4) {
					lb_error.setVisible(true);
					txt4.requestFocus();
					return;
				}
				lb_error.setVisible(false);				
				
				progress.setOpacity(0);
				childBox.setOpacity(1);
				
				// 카드번호 뒷부분 암호화.
				String encryptedTxt3 = "";
				String encryptedTxt4 = "";
				try {
					AES256Util aes = new AES256Util();
					encryptedTxt3 = aes.encrypt(txt3.getText());
					encryptedTxt4 = aes.encrypt(txt4.getText());
					System.out.println(encryptedTxt4);
				} catch (UnsupportedEncodingException | GeneralSecurityException e1) {
					e1.printStackTrace();
				}
				
				vo.setCard_account_no(txt1.getText()+txt2.getText()+encryptedTxt3+encryptedTxt4);
				vo.setTicket_buydate(time.toString());
				try {
					System.out.println(vo.getTicket_buydate());
					int cnt = its.insertTicketBuy(vo);
					System.out.println("insert성공은 1 -> "+cnt);
					// date 집어넣기.. 일단 DB에서 sysdate로 넣기.
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
				
//				waitingNext(e); // 프로그레스바 띄우고 2초기다리기.
				
				// 1년권 구매한 경우 vip로 등급 바꾸기
				if(Integer.valueOf(String.valueOf(tc.ticketInfo[4]))==5 || Integer.valueOf(String.valueOf(tc.ticketInfo[4]))==6) {
					MemberVO mVO = ls.session;
					mVO.setMem_grade("vip");
					try {
						ms.updateGrade(mVO);
					} catch (RemoteException e1) {
						e1.printStackTrace();
					}
				}
				
				
				dialog.close();
				
				box.setOpacity(1);
				box.setLayoutY(1);
				pane.setOpacity(0);
			});
			
			btn_cancel.setOnAction(event->{
				dialog.close();
			});
			
			dialog.setOnCloseRequest(e2->{
				lb_error.setVisible(false);
			});
			
			// 5. Scene 객체 생성해서 컨테이너 객체 추가하기
			Scene scene = new Scene(parent);
			
			// 6. Stage 객체에 Scene 객체 추가
			dialog.setScene(scene);
			dialog.setResizable(false); // 크기 고정
			dialog.show();
			
			txt1.setOnKeyReleased(e2->{
				System.out.println(txt1.getText().length());
				if(txt1.getText().length()==4) {
					txt2.requestFocus();
				}
			});
			txt2.setOnKeyReleased(e2->{
				if(txt2.getText().length()==4) {
					txt3.requestFocus();
				}
			});
			txt3.setOnKeyReleased(e2->{
				if(txt3.getText().length()==4) {
					txt4.requestFocus();
				}
			});

		});
		
	}
	
	private boolean isStoped;
	private void waitingNext(ActionEvent e) {
		Thread thread = new Thread(new Runnable() { // 익명클래스.
			@Override
			public void run() {
				isStoped = false;
				childBox.setOpacity(0.5);
				progress.setOpacity(1);
				
				while(!isStoped) {
					try {
						System.out.println("쓰레드");
						Thread.sleep(20000);
						Platform.runLater(new Runnable() { 
							@Override
							public void run() {
								childBox.setOpacity(1);
								progress.setOpacity(0.5);
								isStoped = true;
							}
						});
						
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
		
		thread.setDaemon(true); // 데몬쓰레드로 설정 true.
		thread.start();
	}

	public String check_lb4(String compareDate) {
		// lb4 값 구하기.
		// 날짜 + 일수 ( ~ 2019-02-20 (+31일) )
		Date parseDate = null;
		try {
			parseDate = sdf2.parse(compareDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(parseDate);
		
		String lb4_str = "";
		if(String.valueOf(tc.ticketInfo[0]).equals("1개월")) {
			cal1.add(Calendar.DAY_OF_MONTH, 31);
			lb4_str = sdf2.format(cal1.getTime()) + " (+31일)";
			lb_vip.setVisible(false);
		}else if(String.valueOf(tc.ticketInfo[0]).equals("6개월")) {
			cal1.add(Calendar.DAY_OF_MONTH, 183);
			lb4_str = sdf2.format(cal1.getTime()) + " (+183일)";
			lb_vip.setVisible(false);
		}else if(String.valueOf(tc.ticketInfo[0]).equals("1년")) {
			cal1.add(Calendar.DAY_OF_MONTH, 365);
			lb4_str = sdf2.format(cal1.getTime()) + " (+365일)";
			lb_vip.setVisible(true);
		}
		
		lb4_str = "→" + lb4_str.substring(1, lb4_str.length());
		return lb4_str;
	}
	
	public void setVO() throws UnsupportedEncodingException, NoSuchAlgorithmException, GeneralSecurityException {
		AES256Util aes = new AES256Util();
//		String output = aes.encrypt();

	}

}
