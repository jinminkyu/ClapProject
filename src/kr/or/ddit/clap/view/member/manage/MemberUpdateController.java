package kr.or.ddit.clap.view.member.manage;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import kr.or.ddit.clap.service.mypage.IMypageService;
import kr.or.ddit.clap.view.join.AES256Util;
import kr.or.ddit.clap.vo.member.MemberVO;

public class MemberUpdateController implements Initializable{
	
	private Registry reg;
	private IMypageService ims;
	
	private FileChooser fileChooser;
	private File filePath;
	private String img_path;

	public static String memid;
	@FXML AnchorPane main;
	
	@FXML Label label_MemName1;
	@FXML ImageView imgview_MemImg;
	@FXML Label label_Memid;
	@FXML JFXPasswordField textF_Mempw;
	@FXML Label label_BlackCnt ;
	@FXML JFXTextField   textF_Memname2;
	@FXML JFXComboBox  combo_Memgender;
	@FXML JFXTextField   textF_MemTel;
	@FXML JFXDatePicker  Date_MemBir;
	@FXML JFXComboBox  combo_MemAuth;
	
	@FXML JFXComboBox  combo_MemGrade;
	@FXML JFXTextField  textF_MemEmail;
	@FXML JFXDatePicker  Date_Indate;
	@FXML JFXComboBox  combo_DelTF;
	@FXML JFXComboBox  combo_blackTF;
	@FXML TextArea  txt_intro;
	
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

		MemberVO vo = new MemberVO();

		vo.setMem_id(memid);
		MemberVO mvo = new MemberVO();
		try {
			mvo = ims.select(vo);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
		//원래 데이터값 넣기
		label_MemName1.setText(mvo.getMem_name());
		//imgview_MemImg 
		label_Memid .setText(memid);
		
		AES256Util aes = null;
		try {
			aes = new AES256Util();
		} catch (UnsupportedEncodingException e11) {
			e11.printStackTrace();
		}
		
		String decryptedPw = ""; // 복호화시킨  pw을 담음
		try {
			decryptedPw = aes.decrypt(mvo.getMem_pw());
		} catch (UnsupportedEncodingException | GeneralSecurityException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		txt_intro.setText(mvo.getMem_intro());
		textF_Mempw.setText(decryptedPw);
		textF_Memname2.setText(mvo.getMem_name());
		
			if(mvo.getMem_gender().equals("f")) {
				mvo.setMem_gender("여성");
			}else {
				mvo.setMem_gender("남성");
			}
		combo_Memgender.setValue(mvo.getMem_gender());
		textF_MemTel.setText(mvo.getMem_tel());
		
		String temp_bir = mvo.getMem_bir().substring(0, 10);
		Date_MemBir.setValue(LocalDate.parse(temp_bir));
		
		if(mvo.getMem_auth().equals("f")) {
			mvo.setMem_auth("사용자");
		}else {
			mvo.setMem_auth("관리자");
		}
		combo_MemAuth.setValue(mvo.getMem_auth());
	
		combo_MemGrade.setValue(mvo.getMem_grade());
		textF_MemEmail.setText(mvo.getMem_email());
		String temp_indate = mvo.getMem_indate().substring(0, 10);
		Date_Indate.setValue(LocalDate.parse(temp_indate));
		
		textF_Memname2.setText(mvo.getMem_name());
		
		
		if(mvo.getMem_del_tf().equals("f")) {
			mvo.setMem_del_tf("X");
		}else {
			mvo.setMem_del_tf("O");
		}
		combo_DelTF.setValue(mvo.getMem_del_tf());
		
		if(mvo.getMem_blacklist_tf().equals("f  ") ||mvo.getMem_blacklist_tf().equals("f")) {
			mvo.setMem_blacklist_tf("X");
		}else {
			mvo.setMem_blacklist_tf("O");
		}
		combo_blackTF.setValue(mvo.getMem_blacklist_tf());
		//txt_intro.setText(mvo.getMem_intro());
		
		label_BlackCnt.setText(mvo.getMem_black_cnt());
		
		
		if(mvo.getMem_image()==null) {
			mvo.setMem_image("file:\\\\Sem-pc\\공유폴더\\Clap\\img\\userimg\\icons8-person-64.png");
	   }
		
		img_path = mvo.getMem_image();
		Image img = new Image(img_path); //이미지 객체등록
		imgview_MemImg.setImage(img);
		
		//콤보박스에 값넣기
		combo_Memgender.getItems().add("여성");
		combo_Memgender.getItems().add("남성");
		combo_MemAuth.getItems().add("사용자");
		combo_MemAuth.getItems().add("관리자");
		combo_MemGrade.getItems().add("일반");
		combo_MemGrade.getItems().add("vip");
		combo_DelTF.getItems().add("O");
		combo_DelTF.getItems().add("X");
		combo_blackTF.getItems().add("O");
		combo_blackTF.getItems().add("X");

	}
	
	public void updateMem() throws RemoteException {
		if(textF_Mempw.getText().isEmpty()) {
			errMsg("비밀번호은 필수 입력 사항입니다.");
			return;
		}
		
		if(textF_Memname2.getText().isEmpty()) {
			errMsg("사용자이름은 필수 입력 사항입니다.");
			return;
		}
		
		if(textF_MemTel.getText().isEmpty()) {
			errMsg("전화번호는 필수 입력 사항입니다.");
			return;
		}
		
		if((Date_MemBir.getValue()+"").isEmpty()) {
			errMsg("생일은 필수 입력 사항입니다.");
			return;
		}
		
		if(textF_MemEmail.getText().isEmpty()) {
			errMsg("이메일은 필수 입력 사항입니다.");
			return;
		}
		
		if((Date_Indate.getValue()+"").isEmpty()) {
			errMsg("가입일자는 필수 입력 사항입니다.");
			return;
		}
		
		MemberVO vo  =new MemberVO();
		vo.setMem_name(textF_Memname2.getText());
		
		
		AES256Util aes = null;
		try {
			aes = new AES256Util();
		} catch (UnsupportedEncodingException e11) {
			e11.printStackTrace();
		}
		
		String NowencryptedPw = ""; // 암호화된 pw
		try {
			NowencryptedPw = aes.encrypt(textF_Mempw.getText());
		} catch (UnsupportedEncodingException | GeneralSecurityException e12) {
			e12.printStackTrace();
		}
		Pattern p = Pattern.compile("(^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[`~!@#$%^&*?]).{8,}$)");
		Matcher m = p.matcher(textF_Mempw.getText());
		if(!m.find()) {
			errMsg("8~20자리의 영문 대/소문자, 숫자, 특수문자를 사용하세요 / 공백 사용 불가");
			return;
		}
		vo.setMem_pw(NowencryptedPw);//암호화 시키기
		vo.setMem_tel(textF_MemTel.getText());
		vo.setMem_email(textF_MemEmail.getText());

		if(combo_Memgender.getValue().equals("여성")) {
			vo.setMem_gender("f");
		}else {
			vo.setMem_gender("m");
		}
		
		if(combo_MemAuth.getValue().equals("사용자")) {
			vo.setMem_auth("f");
		}else {
			vo.setMem_auth("t");
		}
	
		if(combo_MemGrade.getValue().equals("일반")) {
			vo.setMem_grade("일반");
		}else {
			vo.setMem_grade("vip");
		}
		
		if(combo_DelTF.getValue().equals("O")) {
			vo.setMem_del_tf("t");
		}else {
			vo.setMem_del_tf("f");
		}
		
		if(combo_blackTF.getValue().equals("O")) {
			vo.setMem_blacklist_tf("t");
		}else {
			vo.setMem_blacklist_tf("f");
		}
		vo.setMem_id(memid);
		vo.setMem_image(img_path);
		vo.setMem_indate(Date_Indate.getValue()+"");
		vo.setMem_bir(Date_MemBir.getValue()+"");
		vo.setMem_intro(txt_intro.getText());
		int d=ims.updateAll(vo);
		System.out.println(d);
		cancel();
		
		
	}
	public void cancel() {
		try {
			//바뀔 화면(FXML)을 가져옴
			//singerDetail
			MemberDetailController.memid = memid;//가수번호를 변수로 넘겨줌
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource("memditail.fxml"));// init실행됨
			Parent memDetail= loader.load(); 
			main.getChildren().removeAll();
			main.getChildren().setAll(memDetail);
			
			
		} catch (IOException e1) {
			e1.printStackTrace();
		} 
	}
	
	public void ChangeView(ActionEvent event) {
		Stage stage =  (Stage) ((Node)event.getSource()).getScene().getWindow();
		 fileChooser = new FileChooser();
		 fileChooser.setTitle("Open image");
		 
		 //사용자의 디렉토리 보여줌
		 //String userDirectoryString = System.getProperty("user.home") + "\\Pictures"; 기본위치
		 String userDirectoryString = "\\\\Sem-pc\\공유폴더\\Clap\\img\\userimg";
		 
		 System.out.println("userDirectoryString:" + userDirectoryString);
		 File userDirectory = new File(userDirectoryString); 
		 
		 if(!userDirectory.canRead()) {
			 userDirectory = new File("c:/");
		 }
		 
		 fileChooser.setInitialDirectory(userDirectory);; 
		 
		 this.filePath = fileChooser.showOpenDialog(stage);
		 
		
		 //이미지를 새로운 이미지로 바꿈
		 try {
			 BufferedImage bufferedImage = ImageIO.read(filePath);
			 Image image =  SwingFXUtils.toFXImage(bufferedImage, null);
			 imgview_MemImg.setImage(image);
			 String str_filePath = "file:"+filePath;
			// userDirectoryString = "file:\\\\Sem-pc\\공유폴더\\Clap\\img\\singer"; //화면 출력 시 절대경로로 이미지를 읽기위해서
			 img_path = str_filePath;
			 System.out.println(img_path);
			 
		 }catch (Exception e) {
			 System.out.println("이미지를 선택하지 않았습니다.");
		 }
	}
	
	public void errMsg(String msg) {
		Alert errAlert = new Alert(AlertType.ERROR);
		errAlert.setTitle("유효성 검사");
		errAlert.setHeaderText("유효성 검사");
		errAlert.setContentText(msg);
		errAlert.showAndWait();
	}

}
