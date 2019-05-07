package kr.or.ddit.clap.view.login;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import kr.or.ddit.clap.main.LoginSession;
import kr.or.ddit.clap.main.MusicMainController;
import kr.or.ddit.clap.service.login.ILoginService;
import kr.or.ddit.clap.view.join.AES256Util;
import kr.or.ddit.clap.view.ticket.ticket.TicketController;
import kr.or.ddit.clap.vo.member.MemberVO;

/**
 * 로그인창 컨트롤러.
 * @author Kyunghun
 *
 */
public class LoginController implements Initializable{
	
	MusicMainController mmc = new MusicMainController();
	private ILoginService ils;
	private Registry reg;
	
	LoginSession ls = new LoginSession();
	
	@FXML JFXTextField txt_id;
	@FXML JFXTextField txt_captcha;
	@FXML JFXPasswordField txt_pw;
	@FXML Button btn_login;
	@FXML VBox box;
	@FXML HBox box1;
	@FXML HBox box2;
	@FXML HBox box3;
	@FXML Label lb_check;
	@FXML Label lb_captcha;
	@FXML Label lb_ok;
	@FXML ImageView img_captcha;
	@FXML Button btnn;
	@FXML Button btnnn;
	
	List<MemberVO> list = new ArrayList<MemberVO>();
	
	private String captchaKey = "";
	File f = null; // captchaimg파일
	private boolean captchaFlag = false;
	private int errorCount = 0; // id/pw 불일치 횟수.
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			reg = LocateRegistry.getRegistry("localhost", 8888);
			ils = (ILoginService) reg.lookup("login");
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
		
		captchaKey = captchaKey();
		captchaImage(captchaKey);
		
		txt_pw.setOnAction(e->{
			try {
				login();
			} catch (UnsupportedEncodingException | GeneralSecurityException e1) {
				e1.printStackTrace();
			}
		});
		
	}

	public void login() throws UnsupportedEncodingException, NoSuchAlgorithmException, GeneralSecurityException{
		AES256Util aes = new AES256Util();
		
		//test
		TicketController tc = new TicketController();
		String[] arr = tc.ticketCheck("유저 아이디");
		System.out.println(arr[0]);
		System.out.println(arr[1]);
		System.out.println(arr[2]);
		
		
		String encryptedPw = ""; // 암호화된 pw
		encryptedPw = aes.encrypt(txt_pw.getText());
		
		String decryptedPw = ""; // 복호화시킨 pw
		decryptedPw = aes.decrypt(encryptedPw);
		
		MemberVO vo = new MemberVO();
		
		box1.setVisible(false);
		box2.setVisible(false);
		lb_captcha.setVisible(false);
		txt_captcha.setText("");
		if(errorCount==1) {
			imgRefresh();
		}
	
		if(txt_id.getText().equals("")) {
			lb_check.setVisible(true);
			lb_check.setText("아이디를 입력하세요.");
			return;
		}else if(txt_pw.getText().equals("")) {
			lb_check.setVisible(true);
			lb_check.setText("비밀번호를 입력하세요.");
			return;
		}
		
		// 아이디 확인
		vo.setMem_id(txt_id.getText());
		
		Boolean idCheck = false;
		try {
			idCheck = ils.idCheck(txt_id.getText());
			// DB에 id가 없을경우 -> false
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
		if(!idCheck) {
			lb_check.setVisible(true);
			lb_check.setText("존재하지 않는 아이디입니다.");
			return;
		}
		
		// 비밀번호 확인
		// dao로 비밀번호 가져오기.
		try {
			list = ils.select(txt_id.getText());
		} catch (RemoteException e1) {
			e1.printStackTrace();
		}
		System.out.println("가져온 비밀번호 "+list.get(0).getMem_pw());
		
		if(list.get(0).getMem_del_tf().equals("t")) { // 탈퇴한 회원인 경우.
			lb_check.setVisible(true);
			lb_check.setText("탈퇴한 회원입니다.");
			return;
		}
		
		if(encryptedPw.equals(list.get(0).getMem_pw())) {
			System.out.println("로그인 진행");
			
			// session에 vo넘기기
			vo.setMem_id(txt_id.getText());
			ls.session = list.get(0);
			System.out.println("확인 "+ls.session.getMem_id());
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../../main/MusicMain.fxml"));
			ScrollPane root = null;
			try {
				root = loader.load();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			Scene scene = new Scene(root);
			Stage primaryStage = (Stage) txt_id.getScene().getWindow();
			primaryStage.setScene(scene);
		}else {
			System.out.println("비밀번호 불일치");
			if(!txt_id.getText().equals("") && !txt_pw.getText().equals("")) {
				errorCount++;				
			}
			if(errorCount==3) {
				lb_check.setVisible(true);
				lb_check.setText("로그인 정보 3회 오류. 보안문자를 입력해주세요.");
				btn_login.setDisable(true);
				box1.setVisible(true);
				box2.setVisible(true);
				return;
			}
			lb_check.setVisible(true);
			lb_check.setText("아이디 또는 비밀번호를 확인해주세요. ("+errorCount+"/3회 오류)");
		}
		
		
	}

	public void imgRefresh() {
		captchaKey = captchaKey();
		captchaImage(captchaKey);
	}
	
	public String captchaKey() {
        String clientId = "klxHW7Dv5xl3eCyGm4My";//애플리케이션 클라이언트 아이디값";
        String clientSecret = "FLAfQWcfxj";//애플리케이션 클라이언트 시크릿값";
        String result = "";
        try {
            String code = "0"; // 키 발급시 0,  캡차 이미지 비교시 1로 세팅
            String apiURL = "https://openapi.naver.com/v1/captcha/nkey?code=" + code;
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("X-Naver-Client-Id", clientId);
            con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
            int responseCode = con.getResponseCode();
            BufferedReader br;
            if(responseCode==200) { // 정상 호출
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {  // 에러 발생
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();
            System.out.println(response.toString());
            result = response.toString().split("\"")[3];
        } catch (Exception e) {
            System.out.println(e);
        }

		return result;
	}
	
	public void captchaImage(String CaptchaKey) {
        String clientId = "klxHW7Dv5xl3eCyGm4My";//애플리케이션 클라이언트 아이디값";
        String clientSecret = "FLAfQWcfxj";//애플리케이션 클라이언트 시크릿값";
        try {
            String key = CaptchaKey; // https://openapi.naver.com/v1/captcha/nkey 호출로 받은 키값
            String apiURL = "https://openapi.naver.com/v1/captcha/ncaptcha.bin?key=" + key;
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("X-Naver-Client-Id", clientId);
            con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
            int responseCode = con.getResponseCode();
            BufferedReader br;
            if(responseCode==200) { // 정상 호출
                InputStream is = con.getInputStream();
                int read = 0;
                byte[] bytes = new byte[1024];
                f = new File("captchaImg.jpg");
                f.createNewFile();
                OutputStream outputStream = new FileOutputStream(f);
                while ((read =is.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }
                is.close();
            } else {  // 에러 발생
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();
                while ((inputLine = br.readLine()) != null) {
                    response.append(inputLine);
                }
                br.close();
                System.out.println(response.toString());
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        
		Image captcha = new Image("file:"+f.getAbsolutePath());
		img_captcha.setImage(captcha);
	}
	
	public void captchaCheck() {
		String result = captchaResult(captchaKey, txt_captcha.getText());
		if(txt_captcha.getText().equals("")) {
			lb_captcha.setVisible(true);
			lb_captcha.setText("보안문자를 입력해주세요.");
			captchaFlag = false;
			lb_captcha.setTextFill(Color.RED);
			txt_captcha.requestFocus();	
		}else if(result.equals("false")) {
			imgRefresh();
			lb_captcha.setVisible(true);
			lb_captcha.setPadding(new Insets(0));
			lb_captcha.setText("보안문자가 일치하지 않습니다. 새로 입력해주세요.");
			captchaFlag = false;
			lb_captcha.setTextFill(Color.RED);
			txt_captcha.requestFocus();	
		}else if(result.equals("true,")) {
			lb_captcha.setVisible(true);
			lb_captcha.setPadding(new Insets(3));
			lb_captcha.setText("확인되었습니다.");
			btn_login.setDisable(false);
			errorCount = 0;
			lb_check.setVisible(false);
			captchaFlag = true;
			lb_captcha.setTextFill(Color.valueOf("#00cc00"));
		}
	}
	
	public String captchaResult(String CaptchaKey, String input) {
        String clientId = "klxHW7Dv5xl3eCyGm4My";//애플리케이션 클라이언트 아이디값";
        String clientSecret = "FLAfQWcfxj";//애플리케이션 클라이언트 시크릿값";
        String result = "";
        try {
            String code = "1"; // 키 발급시 0,  캡차 이미지 비교시 1로 세팅
            String key = CaptchaKey; // 캡차 키 발급시 받은 키값
            String value = input; // 사용자가 입력한 캡차 이미지 글자값
            String apiURL = "https://openapi.naver.com/v1/captcha/nkey?code=" + code +"&key="+ key + "&value="+ value;

            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("X-Naver-Client-Id", clientId);
            con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
            int responseCode = con.getResponseCode();
            BufferedReader br;
            if(responseCode==200) { // 정상 호출
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {  // 에러 발생
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();
            System.out.println(response.toString());
            result = response.toString().substring(10, 15);
        } catch (Exception e) {
            System.out.println(e);
        }
		return result;
	}
	
	public void search() {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("Search.fxml"));
			box.getChildren().removeAll();
			box.getChildren().setAll(root);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
