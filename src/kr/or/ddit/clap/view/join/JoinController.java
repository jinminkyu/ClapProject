package kr.or.ddit.clap.view.join;

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
import java.time.LocalDate;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import kr.or.ddit.clap.main.MusicMainController;
import kr.or.ddit.clap.service.join.IJoinService;
import kr.or.ddit.clap.service.login.ILoginService;
import kr.or.ddit.clap.vo.member.MemberVO;

/**
 * 회원가입 창 컨트롤러.
 * @author Kyunghun
 *
 */
public class JoinController implements Initializable{
	
	@FXML JFXTextField txt_id;
	@FXML JFXPasswordField txt_pw;
	@FXML JFXPasswordField txt_pwCheck;
//	@FXML JFXTextField txt_bir;
	@FXML JFXDatePicker picker;
	@FXML JFXTextField txt_tel;
	@FXML JFXTextField txt_email;
	@FXML JFXTextField txt_emailCheck;
	@FXML JFXTextField txt_name;
	@FXML JFXTextField txt_captcha;
	
	@FXML JFXButton btn_ok;
	@FXML JFXRadioButton radio_m;
	@FXML JFXRadioButton radio_f;
	
	@FXML ImageView img_captcha;
	@FXML Label lb_id;
	@FXML Label lb_pw;
	@FXML Label lb_pwCheck;
	@FXML Label lb_bir;
	@FXML Label lb_tel;
	@FXML Label lb_email;
	@FXML Label lb_emailCheck;
	@FXML Label lb_ok;
	@FXML Label lb_captcha;
	@FXML Label lb_captcha2;
	
	@FXML Label lb_next;
	@FXML JFXTextArea txtArea1;
	@FXML JFXTextArea txtArea2;
	@FXML JFXCheckBox check1;
	@FXML JFXCheckBox check2;
	
	@FXML BorderPane pane;	
	@FXML StackPane stack1, stack2, box_txt1, box_txt2;	
	@FXML VBox box, boxbox;	
	@FXML JFXButton btn_next;	
	
	ToggleGroup group = new ToggleGroup();
	
	private IJoinService ijs;
	private ILoginService ils;
	private Registry reg;
	
	private boolean pwFlag;
	String code = null; // 이메일 인증코드
	private String captchaKey = "";
	File f = null; // captchaimg파일
	String captchaImg = ""; // captcha img 파일이름.
	SimpleDateFormat sdf = new SimpleDateFormat("yy/MM/dd");
	SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
	private boolean idFlag = false;
	private boolean pwFlag2 = false; // 비밀번호 확인까지 완료되었는지 확인.
	private boolean birFlag = false;
	private boolean telFlag = false;
	private boolean emailFlag = false;
	private boolean captchaFlag = false;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			reg = LocateRegistry.getRegistry("localhost", 8888);
			ijs = (IJoinService) reg.lookup("join");
			ils = (ILoginService) reg.lookup("login");
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
		
		// pane, box 체크 & Y좌표
		pane.setVisible(false);
		box.setVisible(true);
		pane.setLayoutY(650);
		box.setLayoutY(0);
		lb_next.setVisible(false);
		
		boxbox.setVisible(false);
		check1.setDisable(true);
		check2.setDisable(true);
		stack1.setVisible(false);
		stack2.setVisible(false);
		
		radio_m.setToggleGroup(group);
		radio_m.setUserData("m");
		radio_m.setSelected(true);
		
		radio_f.setToggleGroup(group);
		radio_f.setUserData("f");
		
		box_txt1.setVisible(true);
		box_txt2.setVisible(true);
		
		txtArea1.setEditable(false);
		txtArea2.setEditable(false);
		
		picker.setValue(LocalDate.of(1995, 1, 1));
		
		txtArea2.setText("회사는 회원 가입, 상담, 서비스 제공을 위하여 아래와 같이 최소한의 개인정보만을 수집하며,"
				+ " 사상·신념, 가족 및 친인척관계, 학력(學歷)·병력(病歷), 기타 사회활동 경력 등 고객의 "
				+ "권리·이익이나 사생활을 뚜렷하게 침해할 우려가 있는 개인정보를 수집하지 않습니다. 다만, 고객이 "
				+ "동의하거나 다른 법률에 따라 특별히 허용된 경우에는 필요한 범위에서 최소한으로 위 개인정보를 "
				+ "수집할 수 있습니다.  \r\n" + 
				"\r\n" + 
				"회사는 고객의 개인정보를 필수정보와 선택정보로 구분하여 수집하고 있습니다. 필수정보 제공에 "
				+ "동의하지 않는 경우 “지니” 상품∙서비스 제공을 받기 어렵다는 점을 충분히 설명해드리고 있으며, "
				+ "선택정보 제공에 동의하지 않더라도 서비스 이용에는 제한이 없음을 알려드립니다.\r\n" + 
				"\r\n" + 
				"1. 회원 가입 및 관리\r\n" + 
				"구분	회원 구분	처리 항목\r\n" + 
				"필수항목	일반 회원	아이디, 비밀번호, 휴대폰번호, 이메일, 통신사인증정보\r\n" + 
				"만14세미만회원	아이디, 비밀번호, 휴대폰번호, 이메일, 통신사인증정보, 법정대리인의 생년월일, "
				+ "법정대리인의 본인인증정보(CI/DI)\r\n" + 
				"SNS(카카오톡,페이스북,트위터) 회원	아이디, SNS키값, 이메일\r\n" + 
				"휴대폰 간편 가입회원 (KT, LGU+)	휴대폰번호, 통신사인증정보, 이메일\r\n" + 
				"지니홈 간편 가입회원	집 전화번호, 통신사인증정보, 이메일\r\n" + 
				"미디어팩 연동회원	휴대폰번호, 통신사인증정보\r\n" + 
				"2. 재화 또는 서비스 제공\r\n" + 
				"구분	처리 목적	처리 항목\r\n" + 
				"필수항목	· 유료서비스 제공\r\n" + 
				"· 일부 컨텐츠 이용 시 법령에 따른 나이 또는 본인 확인	이름, 생년월일, 성별, 본인인증정보(CI/DI)\r\n" + 
				"· 유료서비스 제공에 따른 요금 청구∙결제∙정산∙할인	※결제수단별\r\n" + 
				"신용카드 : 카드사명, Billkey\r\n" + 
				"상품권 : 상품권 사이트 아이디\r\n" + 
				"휴대폰결제 : 휴대폰번호, 가입통신사, Billkey\r\n" + 
				"할인수단 : (할인)카드번호, 휴대폰번호, 생년월일\r\n" + 
				"· 서비스 부정 이용 및 비인가 사용방지\r\n" + 
				"· 접속빈도파악 및 통계 활용	아이디, 서비스이용기록, 회원기기정보\r\n" + 
				"· 서비스사용빈도 파악 및 통계 활용\r\n" + 
				"· 맞춤형 서비스 제공	연령대, 성별, 서비스이용기록\r\n" + 
				"음악선물	기기에 저장된 이름, 휴대폰번호\r\n" + 
				"부가서비스(지니팩)	통신사인증정보\r\n" + 
				"3. 고객 상담\r\n" + 
				"구분	처리 목적	처리 항목\r\n" + 
				"필수항목	고객상담업무의 처리	이메일\r\n" + 
				"4. 마케팅 및 광고\r\n" + 
				"구분	처리 목적	처리 항목\r\n" + 
				"필수항목	이벤트 진행 및 당첨자 경품 배송	아이디, 본인인증정보(CI/DI), 이메일, 휴대폰번호, 주소,"
				+ " 사용상품, 당첨횟수\r\n" + 
				"- 서비스 이용과정에서 다음의 개인정보 항목이 자동으로 생성되어 수집될 수 있습니다.\r\n" + 
				" 서비스이용기록, 사이트방문기록, 불량 이용기록, 쿠키, 회원 기기정보, 브라우저 정보, 상품구매이력,"
				+ " IP주소, MAC주소");
		txtArea1.setText("제1장 총칙\r\n" + 
				"제1조 [목적]\r\n" + 
				"이 약관은 주식회사 모.각.코.(이하 “회사”라 합니다)과 이용자 간에 “회사”가 제공하는 콘텐츠 서비스인"
				+ " Clap 서비스(이하 “서비스”라 합니다) 및 제반 서비스를 유선 또는 무선 인터넷 등의 수단으로 "
				+ "이용함에 있어 “회사”와 이용자 간의 권리, 의무에 관한 사항과 기타 필요한 사항을 규정하는 것을 "
				+ "목적으로 합니다.\r\n" + 
				"\r\n" + 
				"제2조 [용어의 정의]\r\n" + 
				"① 이 약관에서 사용하는 용어의 정의는 다음 각 호와 같습니다.\r\n" + 
				"1. “이용자”라 함은 “회사”가 제공하는 “서비스”에 유선 또는 무선 인터넷 등의 수단으로 접속하여 이 "
				+ "약관에 따라 “회사”가 제공하는 “콘텐츠” 및 제반 서비스를 이용하는 “회원” 및 “비회원”을 말합니다.\r\n" + 
				"2. “회원”이라 함은 “회사”와 이용계약을 체결하고 아이디(ID)를 부여 받은 “이용자”로서 “회사”의 "
				+ "정보를 지속적으로 제공받으며 “회사”가 제공하는 서비스를 이용할 수 있는 자를 말합니다.\r\n" + 
				"3. “비회원”이라 함은 “회원”이 아니면서 “회사”가 제공하는 서비스를 이용할 수 있는 자를 말하며, "
				+ "“회사”는 “회원”과 “비회원”에게 제공하는 서비스에 차별을 두어 제공할 수 있습니다.\r\n" + 
				"4. “아이디(ID)”라 함은 “회원” 식별과 “회원”의 서비스 이용을 위하여 “회원”이 선정하고 “회사”가 "
				+ "승인하는 영문자와 숫자의 조합을 의미합니다.\r\n" + 
				"5. “비밀번호(Password)”라 함은 “회원”이 부여 받은 “아이디(ID)”와 일치되는 “회원”임을 확인하고 "
				+ "비밀보호를 위해 “회원”자신이 정한 문자 또는 숫자의 조합을 말합니다.\r\n" + 
				"6. “콘텐츠”라 함은 『정보통신망 이용촉진 및 정보 보호 등에 관한 법률』 제2조 제1항 제1호의 규정에 "
				+ "의한 정보통신망에서 사용되는 부호, 문자, 음성, 음향, 이미지 또는 영상 등으로 표현된 자료 또는 "
				+ "정보로서, 그 보존 및 이용에 있어서 효용을 높일 수 있도록 전자적 형태로 제작 또는 처리된 것을 "
				+ "말합니다.\r\n" + 
				"7. “유료서비스”라 함은 “회원”이 일정 금액을 “결제”하고 이용할 수 있는 “콘텐츠” 서비스 또는 상품을 "
				+ "의미합니다.\r\n" + 
				"8. “지니캐시“라 함은 “회사”가 제공하는 다양한 “유료서비스”를 이용하기 위해 특정 결제수단 중 "
				+ "원하는 지불수단을 선택하여 현금처럼 지불할 수 있는 사이버 머니를 말하며, 현금 1원당 “지니캐시“ "
				+ "1원의 비율로 충전됩니다.\r\n" + 
				"9. “게시물”이라 함은 “서비스”에 “회원”이 올린 글, 그림, 사진, 음악선곡리스트, 각종 파일과 링크, "
				+ "각종 댓글 등의 정보의 총칭을 말합니다.\r\n" + 
				"10. “상품권”이란 “회사”가 온∙오프라인에서 발행한 “유료서비스” 이용권을 말하며, 사용방법은 "
				+ "“상품권” 또는 이용안내 페이지 등에 별도로 표기합니다.\r\n" + 
				"② 본 조에서 정의된 용어 이외의 용어에 대해서는 관계 법령 및 서비스별 안내에서 정의하는 바에 "
				+ "따릅니다.\r\n" + 
				"제3조 [신원정보 등의 제공]\r\n" + 
				"“회사”는 이 약관의 내용, 상호, 대표자 성명, 영업소 소재지 주소(소비자의 불만을 처리할 수 있는 "
				+ "곳의 주소를 포함), 전화번호, 전자우편주소, 사업자등록번호, 통신판매업 신고번호 및 "
				+ "개인정보관리책임자 등을 이용자가 쉽게 알 수 있도록 서비스 초기화면에 게시합니다. 다만, 약관 "
				+ "및 개인정보처리방침은 이용자가 연결화면을 통하여 볼 수 있도록 할 수 있습니다.\r\n" + 
				"\r\n" + 
				"제4조 [약관의 게시 등]\r\n" + 
				"① “회사”는 이 약관을 “이용자”이 그 전부를 인쇄할 수 있고 이용과정에서 해당 약관의 내용을 "
				+ "확인할 수 있도록 기술 조치를 취합니다.\r\n" + 
				"② “회사”는 “이용자”가 “회사”와 이 약관의 내용에 관하여 질의 및 응답할 수 있도록 기술적 장치를"
				+ " 설치합니다.\r\n" + 
				"제5조 [약관의 효력 및 개정]\r\n" + 
				"① 이 약관의 내용은 “회원” 가입 시 게재되거나 공지된 내용에 “회원”이 동의함으로써 그 효력이 "
				+ "발생합니다.\r\n" + 
				"② “회사”는 『콘텐츠산업 진흥법』, 『약관의 규제에 관한 법률』, 『정보통신망 이용촉진 및 "
				+ "정보보호 등에 관한 법률』, 『전자상거래 등에서의 소비자보호에 관한 법률』 등 관계 법령을 "
				+ "위배하지 않는 범위 내에서 필요 시 이 약관을 개정할 수 있으며, 약관을 개정할 경우에는 적용일자"
				+ " 및 개정사유를 명시하여 현행약관과 함께 서비스 초기화면에 그 적용일자 10일(“회원”에게 불리한"
				+ " 변경 또는 중대한 사항의 변경은 30일) 이전부터 적용일자 이후 상당한 기간 동안 공지하고, 기존"
				+ " ”회원”에게는 개정약관, 적용일자 및 변경사유(중요 내용에 대한 변경인 경우 이에 대한 설명을 "
				+ "포함) 전자우편주소로 발송하고, 해당기간 동안 개정 약관의 적용에 대한 “회원”의 동의 여부를 "
				+ "확인합니다.\r\n" + 
				"③ “회사”가 제2항의 기간 동안 [거부의사를 표시하지 않으면 의사표시가 표명된 것으로 본다]는 "
				+ "뜻을 공지하였음에도 불구하고, “회원”이 명시적으로 거부의사를 표시하지 아니하는 경우 “회원”이"
				+ " 개정약관에 동의한 것으로 봅니다.\r\n" + 
				"④ 이 약관에 동의하지 않는 “회원”에게 “회사”는 약관을 적용하지 아니하고, “회원”은 “서비스” "
				+ "이용을 중단하고 이용계약을 해지할 수 있습니다. 또한 기존 약관을 적용할 수 없는 특별한 사정이"
				+ " 있는 경우 “회사”가 이용계약을 해지할 수 있습니다.\r\n" + 
				"제6조 [약관의 해석 및 약관 외 사항에 대한 규정]\r\n" + 
				"이 약관에 명시되지 않은 사항에 대해서는 『콘텐츠산업 진흥법』, 『전자문서 및 전자거래 "
				+ "기본법』, 『전자상거래 등에서의 소비자보호에 관한 법률』, 『약관의 규제에 관한 법률』, "
				+ "『콘텐츠 이용자보호지침』 등 관계 법령 및 “회사”가 정한 서비스 이용 정책 등에 의합니다.\r\n" + 
				"\r\n" + 
				"제2장 서비스 이용 계약\r\n" + 
				"제7조 [이용신청, 승낙 및 거절, 계약의 성립]\r\n" + 
				"① “회원”이 되고자 희망하는 “이용자”는 약관의 내용에 대하여 동의를 하고 “회원” 가입신청을 "
				+ "한 후 “회사”가 이러한 신청에 대하여 승낙함으로써 이용계약이 체결됩니다.\r\n" + 
				"② “회원”이 되고자 희망하는 “이용자”는 본 약관 동의와 함께 “회사”가 필요로 하는 정보를 "
				+ "정확히 작성하여 제출하여야 합니다.\r\n" + 
				"③ “회사”는 다음 각 호의 어느 하나에 해당하는 사유가 있는 경우, “회원” 가입신청을 거절할 "
				+ "수 있습니다.\r\n" + 
				"1. 이용자 등록 사항을 누락하거나 오기하여 신청하는 경우\r\n" + 
				"2. “회원” 자격을 상실한 자로서, “회사”가 정한 일정 가입보류 기간이 경과되지 아니한 자\r\n" + 
				"3. 만 14세 미만의 자가 법정대리인의 동의를 얻지 않은 경우\r\n" + 
				"④ “회사”는 서비스 관련 설비의 여유가 없거나 기술상 또는 업무상 문제가 있는 경우에는 "
				+ "가입신청의 승낙을 유보하거나 거절할 수 있습니다.\r\n" + 
				"⑤ 제3항 또는 제4항에 따라 회원가입 신청에 대하여 유보하거나 거절한 경우 “회사”는 이를 "
				+ "신청자에게 즉시 알려야 합니다. 다만, “회사”가 고의 또는 과실 없이 신청자에게 통지할 수 "
				+ "없는 경우에는 그러하지 않습니다.\r\n" + 
				"⑥ “회사”는 『정보통신망 이용촉진 및 정보보호 등에 관한 법률』, 『청소년보호법』, 『영화 "
				+ "및 비디오물의 진흥에 관한 법률』 등의 규정에 의하여 연령 및 등급에 제한을 둘 수 있으며, "
				+ "별도의 성인인증의 절차를 실시할 수 있습니다.\r\n" + 
				"제8조 [만 14세 미만 미성년자의 “회원” 가입에 관한 특칙]\r\n" + 
				"① 만 14세 미만의 “이용자”는 개인정보의 수집 및 이용목적에 대하여 충분히 숙지하고 부모 등 "
				+ "법정대리인의 동의를 얻은 후에 “회원”가입을 신청하고 본인의 개인정보를 제공하여야 합니다.\r\n" + 
				"② “회사”는 부모 등 법정대리인의 동의에 대한 확인절차를 거치지 않은 만 14세 미만 이용자에 "
				+ "대하여는 가입을 취소 또는 불허합니다.\r\n" + 
				"③ 만 14세 미만 “이용자”의 부모 등 법정대리인은 미성년자에 대한 개인정보의 열람, 정정, "
				+ "갱신을 요청하거나 “회원”가입에 대한 동의를 철회할 수 있으며, 이러한 경우에 “회사”는 "
				+ "지체 없이 필요한 조치를 취해야 합니다.\r\n" + 
				"제9조 [“회원” 정보의 수정 및 관리]\r\n" + 
				"① “회원”은 “회원” 정보확인 화면을 통해서 언제든지 개인정보를 열람하고 수정할 수 있으며, "
				+ "이때 서비스 관리를 위해 필요한 실명, 아이디 등은 수정이 불가합니다.\r\n" + 
				"② “회원”은 가입신청 시 기재한 사항이 변경되었을 경우, 직접 회원정보 수정 등의 방법으로 "
				+ "수정하여야 합니다.\r\n" + 
				"③ “회사”는 “회원”이 제2항에 따라 회원정보를 수정하지 않아 발생한 불이익에 대하여 책임을"
				+ " 지지 않습니다.\r\n" + 
				"④ “회원”은 자신의 “아이디”와 “비밀번호”에 관하여 관리할 책임이 있으며, 제3자에게 이를 "
				+ "이용하도록 하여서는 안됩니다\r\n" + 
				"⑤ “회사”는 “회원”의 “아이디”가 개인정보유출 우려가 있거나, 반사회적 행위 및 미풍양속을"
				+ " 저해하거나 “회사” 및 “회사”의 운영자로 오인하게 할 수 있도록 사용되는 경우, “아이디”의"
				+ " 이용을 제한 할 수 있습니다.\r\n" + 
				"⑥ “회원”은 자신의 “아이디”가 도용되거나 제3자가 사용하고 있는 사실을 안 때에는 지체 "
				+ "없이 이를 “회사”에 통지하여야 하며, 통지하지 아니하거나 통지한 경우에도 “회사”의 정책에"
				+ " 따르지 않은 경우 발생한 결과에 대하여 “회사”는 책임을 지지 않습니다.\r\n" + 
				"제10조 [“회원”에 대한 통지]\r\n" + 
				"① “회사”는 별도의 방법을 정하지 않는 한 “회원”에 대한 통지는 “회원”이 지정한 전자우편 "
				+ "또는 휴대폰 문자메세지의 방법에 의합니다.\r\n" + 
				"② “회사”는 “회원” 전체에 대하여 통지할 경우에는 10일 이상의 기간 동안 “서비스”의 "
				+ "홈페이지를 통해서 게시하거나 팝업화면 등을 제시함으로써 제1항의 통지에 갈음할 수 "
				+ "있습니다. 다만, 제5조에 따른 약관의 변경 또는 “회원” 본인과 관련된 중요한 사항에 "
				+ "대하여는 제1항에서 정한 통지를 합니다.\r\n" + 
				"제11조 [“회원”탈퇴 및 자격 상실 등]\r\n" + 
				"① “회원”은 “회사”에 언제든지 탈퇴를 요청할 수 있으며, “회사”는 즉시 “회원” 탈퇴 요청을 "
				+ "처리합니다.\r\n" + 
				"② “회원”이 다음 각 호의 사유에 해당하는 경우, “회사”는 “회원” 자격을 제한 및 정지시킬"
				+ " 수 있습니다. 이 경우 “회사”는 “회원”에게 해당 사유를 통지합니다.\r\n" + 
				"1. 가입신청 또는 “콘텐츠” 이용 신청 또는 변경 시 허위내용을 등록한 경우\r\n" + 
				"2. “회사”가 회원가입 후 또는 최종 사용일 이후 1년 동안 서비스 사용이력이 없는 회원에 "
				+ "대해 사용의사를 묻는 고지를 하고, “회사”가 정한 기한 내에 “회원”의 답변 없는 경우\r\n" + 
				"3. 다른 사람이 “회사”의 서비스를 이용하는 것을 방해하거나 그 정보를 도용하는 등 "
				+ "전자상거래 질서를 위협하는 경우\r\n" + 
				"4. “회사”가 금지한 정보(컴퓨터 프로그램 등)의 사용, 송신 또는 게시\r\n" + 
				"5. “회사” 또는 제3자의 저작권 등 지식 재산권에 대한 침해\r\n" + 
				"6. “회사” 또는 제3자의 명예를 손상시키거나 업무를 방해하는 행위\r\n" + 
				"7. “회사”를 이용하여 법령 또는 이 약관이 금지하거나 공서양속에 반하는 행위를 하는 "
				+ "경우\r\n" + 
				"8. “회사”의 기술적 보호조치를 회피 혹은 무력화 하는 행위\r\n" + 
				"9. “회사”가 제공하는 서비스를 정상적인 용도 이외 또는 부당한 방법으로 서비스를 "
				+ "이용하는 행위\r\n" + 
				"10. “회사”의 영업상 방해를 목적으로 서비스를 비정상적으로 악용하는 행위\r\n" + 
				"③ “회사”가 “회원” 자격을 제한, 정지 시킨 후, 동일한 행위가 2회 이상 반복되거나 30일 "
				+ "이내에 그 사유가 시정되지 아니하는 경우, “회사”는 “회원” 자격을 상실시킬 수 있습니다.\r\n" + 
				"④ “회사”가 “회원” 자격을 상실시키는 경우에는 “회원”등록을 말소합니다. 이 경우 “회원”에게"
				+ " 이를 통지하고, “회원” 등록 말소 전에 최소한 30일 이상의 기간을 정하여 소명하고, 그 "
				+ "내용이 정당한 경우 “회사”는 즉시 “회원”의 자격을 복구합니다. 단, “회사”가 정한 소명기간"
				+ " 동안 소명치 않은 경우 회원 등록 말소 동의로 간주합니다.\r\n" + 
				"⑤ “회사”는 “회원”이 탈퇴하거나 회원자격을 상실하는 경우 “회원”의 개인정보를 『정보통신망"
				+ " 이용촉진 및 정보보호 등에 관한 법률』 등 관계 법령에서 정한 범위와 절차에 따라 "
				+ "파기합니다.\r\n" + 
				"⑥ 회사는 “서비스”를 1년 동안 이용하지 아니한 “회원”의 개인정보를 보호하기 위하여 다른 "
				+ "“회원”의 개인정보와 분리하여 별도로 저장·관리하되, “회원”이 “서비스” 재이용을 원하는 "
				+ "경우 “회원”의 본인확인절차를 거쳐 재이용 할 수 있도록 합니다.\r\n" + 
				"제12조 [개인정보의 보호]\r\n" + 
				"“회사”는 관계 법령이 정하는 바에 따라서 “회원” 등록정보를 포함한 “회원”의 개인정보를 "
				+ "보호하기 위하여 노력합니다. “회원”의 개인정보보호에 관해서는 관계 법령 및 “회사”가 "
				+ "정하는 “개인정보처리방침”에 정한 바에 따릅니다. 다만, “회사”의 사이트 이외에 단순 "
				+ "링크사이트, 제휴된 사이트 등의 경우에는 “회사”의 개인정보처리방침이 적용되지 "
				+ "아니합니다.\r\n" + 
				"\r\n" + 
				"제3장 서비스 이용\r\n" + 
				"제13조 [서비스의 이용개시]\r\n" + 
				"① “회사”는 “회원”의 이용 신청을 승낙한 때부터 서비스를 개시합니다. 단, 유료서비스의"
				+ " 경우 “결제“가 완료된 후 이용 가능합니다.\r\n" + 
				"② “회사”의 업무상 또는 기술상의 장애로 인하여 서비스를 개시하지 못하는 경우에는 "
				+ "사이트에 공지하거나 “회원”에게 이를 통지합니다.\r\n" + 
				"③ 서비스 이용에 필요한 최소한의 기술사항은 아래 표와 같습니다. 단, 모바일 "
				+ "어플리케이션의 경우에는 각 모바일 운영체제(Android, IOS, Window Mobile 등) 제공자의"
				+ " 정책에 따라 달라질 수 있습니다.\r\n" + 
				"OS	CPU	RAM	사운드 카드	Direct X	web browser\r\n" + 
				"Window XP SP3	Core2Duo PB700	2GB	16bit	Direct X 9.0	internet explore 8.0, "
				+ "Chrome 38\r\n" + 
				"제14조 [유료서비스의 이용 및 계약의 성립]\r\n" + 
				"① “회원”이 “회사”에 대하여 “유료서비스” 이용 신청을 하고 “회사”가 이를 승낙함으로써"
				+ " “유료서비스”에 대한 이용계약이 성립됩니다.\r\n" + 
				"② “회사”는 “유료서비스” 결제 전에 다음 각 호의 사항에 관하여 “회원”이 정확하게 "
				+ "이해하고 실수 또는 착오 없이 거래할 수 있도록 정보를 제공합니다.\r\n" + 
				"1. 해당 “유료서비스”의 내용, 가격, 이용기간, 이용방법, 청약철회 및 해지 조건 및 그 "
				+ "방법, 환불에 관한 사항 및 유료서비스의 선택과 관련한 사항\r\n" + 
				"2. “콘텐츠” 목록의 열람 및 선택\r\n" + 
				"3. 청약철회가 불가능한 “콘텐츠”에 대해 “회사”가 취한 조치에 관련한 내용에 대한 "
				+ "확인\r\n" + 
				"4. “콘텐츠”의 이용신청에 관한 확인 또는 “회사”의 확인에 대한 동의\r\n" + 
				"5. 결제방법의 선택\r\n" + 
				"③“회원”은 “회사”가 지정하는 지불수단으로 “유료서비스”를 이용할 수 있으며, 각종 "
				+ "프로모션이나 이벤트 등을 통하여 발행된 쿠폰 또는 “회사”가 발행한 “상품권” 등을 "
				+ "이용하여 “유료서비스”를 이용할 수 있습니다.\r\n" + 
				"④ 해당 “유료서비스”에 대한 세부 이용조건은 상품 안내 및 구매 페이지와 상품 보관함 "
				+ "페이지에서 안내합니다.\r\n" + 
				"⑤ “회사”는 “회원”의 “유료서비스” 이용신청이 다음 각 호에 해당하는 경우에는 승낙하지"
				+ " 않거나 승낙을 유보할 수 있습니다.\r\n" + 
				"1. 허위의 정보를 기재하거나, “회사”가 제시하는 필수 내용을 기재하지 않은 경우\r\n" + 
				"2. 미성년자가 청소년보호법에 의해서 이용이 금지되는 “콘텐츠”를 이용하고자 하는 "
				+ "경우\r\n" + 
				"3. 서비스 관련 설비의 여유가 없거나, 기술상 또는 업무상 문제가 있는 경우\r\n" + 
				"⑥ “회사”의 승낙은 제10조 제1항의 수신확인통지형태로 “회원”에게 이용신청에 대한 "
				+ "확인 및 서비스제공 가능여부, 이용신청의 정정, 취소 등에 관한 정보 등을 포함하여 "
				+ "발송하며, 도달한 시점에 “유료서비스” 이용계약이 성립한 것으로 봅니다\r\n" + 
				"⑦ “회사”가 제공하는 상품 중 각 음원권리자들의 이용허락 여부에 따라 제공되는 시기가 "
				+ "달라질 수 있습니다.\r\n" + 
				"⑧ 무선으로 “유료서비스” 이용시의 데이터량에 따른 요금은 “회사”가 별도로 청구하지 "
				+ "않으며, “회원”이 가입한 이동통신사가 청구합니다.\r\n" + 
				"⑨ 일부 “유료서비스”는 중복사용이 불가할 수 있습니다.\r\n" + 
				"⑩ 일부 음원의 경우에는 권리자의 요청에 따라 음원의 이용금액(또는 차감 곡수)이 "
				+ "상이하거나 단일 음원 단위로 판매될 수 있습니다.\r\n" + 
				"제15조 [음악감상 상품]\r\n" + 
				"음악감상 상품은 결제 후, 이용 시작일로부터 “서비스”가 제공하는 음악듣기, 뮤직비디오"
				+ " 등의 음악감상서비스를 일정기간 동안 횟수 제한 없이 이용하실 수 있는 기간제형과 "
				+ "이용시작일부터 일정 기간 동안 정해진 재생 횟수만큼 음악듣기, 뮤직비디오 감상 "
				+ "등을 이용할 수 있는 차감형 및 일정기간 동안 감상한 곡에 대한 금액이 청구되는 "
				+ "후불제형으로 구분되며, 본 상품을 통해 제공되는 음악은 “회원”의 Device에 저장되지 "
				+ "않습니다.\r\n" + 
				"\r\n" + 
				"제16조 [다운로드 상품, 곡 다운로드]\r\n" + 
				"① 다운로드 상품은 회사에서 제공하는 DRM(Digital Right Management)이 적용되었거나"
				+ " 적용되어있지 않은 파일을 “회원”이 일정한 대가를 지급하고 다운받아 이용하실 수 "
				+ "있는 서비스를 의미합니다.\r\n" + 
				"② 다운로드 상품은 “결제“ 후, 이용시작일로부터 일정기간 동안 일정 수량으로 "
				+ "음악파일을 다운로드 할 수 있는 기간제형과 단일 곡을 다운로드 할 수 있는 개별형으로 구분되며,"
				+ " 기간제형의 경우, 해당기간 내 사용하지 않은 다운로드 수량에 대해서는 이월되지 않습니다.\r\n" + 
				"③ 기간제형과 개별형 다운로드를 통해 구매한 곡은 서비스 내 조회할 수 있는 특정 페이지에 "
				+ "구매일로부터 보관 및 재다운로드 할 수 있습니다. 단, 권리사의 요청이 있는 경우 일부 구매한 "
				+ "곡에 대한 보관기간 및 재 다운로드 가능기간은 1년 또는 사이트에 명기된 기간으로 제한되며 "
				+ "이후에는 자동으로 삭제되고 복구되지 않습니다. 다만, 이벤트성으로 무상 제공된 곡의 경우에는"
				+ " “회사”에서 정한 기간에 재다운로드가 가능합니다.\r\n" + 
				"④ “회원”을 탈퇴하는 경우에는 재다운로드가 불가합니다.\r\n" + 
				"⑤ 다운로드 가능한 PC는 3대를 등록할 수 있으며, 등록한 PC를 변경할 경우에는 월 1회에 한하여 "
				+ "1대의 PC를 변경 할 수 있습니다.\r\n" + 
				"⑥ 일부 권리자의 요청에 따라 다운로드 및 전송이 불가한 곡이 있을 수 있으며, 다운로드 상품을 "
				+ "이용 중이시더라도, 일부 곡은 소유형 지원기기에서 재생 가능한 파일로만 다운로드 하실 수 "
				+ "있습니다.\r\n" + 
				"⑦ 다운로드 상품 개별형의 경우에 영상파일, 이미지 파일 등의 묶음 형태로 판매가 가능하며, 이 "
				+ "경우 상품의 가격과 이용 가능한 Device를 달리할 수 있습니다.\r\n" + 
				"제17조 [기간 임대형 다운로드 상품]\r\n" + 
				"① 기간 임대형 다운로드 상품이란 일정기간 동안만 재생이 가능하도록 DRM이 적용된 음악 파일을 "
				+ "다운받아 이용하실 수 있는 서비스를 뜻합니다.\r\n" + 
				"② 상품 이용 중 다운로드 하신 음악 파일은 스마트폰 어플리케이션 및 기간 임대형 지원기기에서만 "
				+ "이용가능하며(지적재산권 보호), 재생 유효 기간은 상품 사용 기간으로 제한됩니다.\r\n" + 
				"③ 다운로드 이용 시 하나의 ID로 이용가능 Device수는 월 2대로 등록 제한되며 PC로의 다운로드 "
				+ "기능은 지원하지 않습니다.\r\n" + 
				"제18조 [복합 상품]\r\n" + 
				"① 복합 상품은 음악감상 상품 기간제형과 다운로드 상품(NON-DRM) 기간제형 또는 기간임대형 "
				+ "다운로드 상품이 결합된 형태의 상품입니다.\r\n" + 
				"② 복합 상품 이용방법은 제15조 음악감상 상품의 기간제형, 제16조 다운로드 상품의 기간제형 및 "
				+ "제17조 기간임대형 다운로드 상품과 동일합니다.\r\n" + 
				"제19조 [상품에 대한 이용기간의 약정]\r\n" + 
				"① “회원”은 제15조 내지 제18조의 상품에 대하여 일정기간 동안 각 상품을 이용하기로 약정 할 수 "
				+ "있습니다. “회사”는 약정한 기간 동안은 “회사”에서 정한 할인액을 적용하여 청구합니다.\r\n" + 
				"② 약정 상품의 이용 조건은 제15조 내지 제18조의 각 상품별 이용조건과 동일합니다.\r\n" + 
				"③ 약정 기간 중 “회원”의 사정으로 인하여 이용계약을 해지하는 경우, “회사”에서 정한 할인반환금이"
				+ " 청구될 수 있습니다.\r\n" + 
				"제20조 [DRM(Digital Right Management) 적용범위]\r\n" + 
				"① 기간 임대형 다운로드 상품으로 구매한 DRM 적용 파일은 상품 사용기간 동안 재생 및 반복 "
				+ "다운로드가 가능합니다.\r\n" + 
				"② DRM 파일의 이용권한은 타인에게 양도할 수 없습니다.\r\n" + 
				"③ “회원”이 정상적인 방법을 통해 구매하여 다운로드한 파일은 PC, 휴대폰 단말기, 기타 플레이어"
				+ " 등 Device 특성에 따라 DRM 적용 여부가 다를 수 있습니다.\r\n" + 
				"④ DRM이 적용되지 않은 파일은 모든 Device에서 기간제한 없이 재생 가능하지만, 일부 DRM 적용 "
				+ "파일의 다운로드 및 재생은 DRM이 적용된 Device(회사에서 지원하는 DRM을 지원하는 기기)에서만"
				+ " 가능합니다.\r\n" + 
				"⑤ DRM 적용 파일의 다운로드 및 재생은 이동전화 서비스가 제공되지 않은 경우에는 제한될 수 "
				+ "있습니다.\r\n" + 
				"제21조 [서비스의 변경 및 중지]\r\n" + 
				"① “회사”는 운영상, 법률상, 기술상의 상당한 이유가 있는 경우, 관계 법령을 위반하지 않는 범위 "
				+ "내에서 서비스의 내용, 이용 방법, 지원기기 범위 등 서비스의 전부 또는 일부를 변경할 수 "
				+ "있습니다.\r\n" + 
				"② “회사”는 제1항의 경우, 변경될 서비스의 내용 및 제공일자를 제5조에서 정한 방법으로 회원에게"
				+ " 통지하고, “회원”의 선택에 따라 환불 또는 다른 상품으로 전환할 수 있는 서비스를 제공합니다.\r\n" + 
				"③ “회사”는 무료로 제공하는 서비스의 전부 또는 일부를 “회사”의 정책 및 운영의 필요상 "
				+ "변경하거나 중단할 수 있으며, 이에 대하여 “회원”에게 별도의 보상을 하지 않습니다.\r\n" + 
				"④ “회사”는 다음 각 호에 해당하는 사유가 발생한 경우, 서비스의 전부 또는 일부를 일시적으로 "
				+ "제한하거나 중지할 수 있으며, 이 경우 해당 내용을 사전에 “회원”에게 고지하거나, 사전 "
				+ "고지가 어려운 사항일 경우 사후에 고지할 수 있습니다.\r\n" + 
				"1. 서비스용 설비의 보수 등 공사로 인한 부득이한 경우\r\n" + 
				"2. 정기점검, 서비스 업그레이드 및 사이트 유지보수 등을 위해 필요한 경우\r\n" + 
				"3. 정전, 제반 설비의 장애 또는 이용량의 폭주 등으로 정상적인 서비스 이용에 지장이 있는 경우\r\n" + 
				"4. 기타 천재지변, 국가비상사태 등 불가항력적 사유가 있는 경우\r\n" + 
				"제22조 [결제수단 등]\r\n" + 
				"① “회원”이 결제를 위하여 이용할 수 있는 지불수단은 다음 각 호와 같으며, 각 지불 수단마다 "
				+ "결제가 가능한 “유료서비스”가 제한 될 수 있습니다.\r\n" + 
				"1. 선불카드, 직불카드, 신용카드 등의 각종 카드\r\n" + 
				"2. 전화 또는 휴대전화 요금합산\r\n" + 
				"3. 폰 뱅킹, 인터넷 뱅킹 등의 각종 계좌이체\r\n" + 
				"4. 전자화폐\r\n" + 
				"5. “회사”와 계약을 체결한 자가 발행한 상품권 또는 포인트\r\n" + 
				"6. “지니캐시“\r\n" + 
				"7. 기타 전자적 지급방법에 의한 대금지급 등\r\n" + 
				"② 제1항의 지불수단은 \"상품\"의 특성에 따라 정기적으로 “결제“될 수 있고, 이 경우 회사는"
				+ " \"회원\"에게 관계 법령에 따른 동의 및 고지절차를 진행합니다.\r\n" + 
				"③ 제1항의 지불수단 중 “지니캐시”의 경우에는 충전시, 그 외 지불수단으로 결제하는 경우에는"
				+ " 부가가치세가 별도로 청구됩니다.\r\n" + 
				"④ “회사”의 사정에 따라 특정 지불수단이 추가되거나 서비스 제공이 중단될 수 있습니다.\r\n" + 
				"⑤ 지불수단과 함께 사용될 수 있는 포인트 결제의 경우, 회사의 사정에 따라 제공기간과 "
				+ "방식이 변경될 수 있습니다.\r\n" + 
				"제23조 [“지니캐시“의 충전 및 유효기간]\r\n" + 
				"① “회원”은 “지니캐시“ 충전 시 “회사”에서 지정하는 지불수단을 통해 충전요금을 지불하여야"
				+ " 합니다.\r\n" + 
				"② “회사”는 “회원”의 “지니캐시“에 대한 금융이자 지급 의무가 없습니다.\r\n" + 
				"③ “지니캐시“를 마지막으로 이용한 날로부터 5년이 경과하는 동안 “지니캐시“를 단 한번도 "
				+ "충전하거나 사용하지 않을 경우, 회원에게 충전된 “지니캐시“는 자동 소멸되며, “지니캐시“의"
				+ " 추가적인 충전 또는 사용이 있는 경우 다시 그 날짜를 기준으로 5년간 유효하게 됩니다. "
				+ "다만, 이벤트성으로 제공되는 “지니캐시”는 “회사”의 정책에 따라 자동으로 소멸됩니다.\r\n" + 
				"④ “회원”은 “지니캐시“를 타인에게 양도하거나 타인으로부터 양도 받을 수 없습니다.\r\n" + 
				"⑤ “지니캐시“를 충전하는 것만으로는 “유료서비스”를 이용할 수 없으며, “지니캐시“ 또는 “회사”가"
				+ " 지정하는 지불수단으로 구매하여야 “유료서비스” 이용이 가능합니다.\r\n" + 
				"⑥ 상품 보관함에 등록된 상품의 유효기간은 구입일 또는 지급일로부터 60일이며, 유효기간 이내 "
				+ "사용하지 않은 상품은 “지니캐시“로 자동 전환됩니다. 단, 무상으로 제공된 이벤트성 상품의 "
				+ "경우에는 “지니캐시”로 전환되지 않습니다.\r\n" + 
				"제24조 [콘텐츠 내용 등의 게시]\r\n" + 
				"“회사”는 다음 사항을 해당 “콘텐츠”의 특성에 따라 초기화면 또는 이용화면에 “이용자”가 알기 "
				+ "쉽게 표시합니다.\r\n" + 
				"\r\n" + 
				"1. “콘텐츠”의 명칭 또는 제호\r\n" + 
				"2. “콘텐츠”의 제작 및 표시 연월일\r\n" + 
				"3. “콘텐츠” 제작자의 성명(법인인 경우에는 법인의 명칭)\r\n" + 
				"4. “콘텐츠”의 내용, 이용방법, 이용료, 환불조건 등의 기타 이용조건\r\n" + 
				"제25조 [미성년자 이용계약에 관한 특칙]\r\n" + 
				"“회사”는 만 19세 미만의 미성년이용자가 유료서비스를 이용하고자 하는 경우에 부모 등 법정 "
				+ "대리인의 동의를 얻거나, 계약체결 후 추인을 얻지 않으면 미성년자 본인 또는 법정대리인이 그 "
				+ "계약을 취소할 수 있다는 내용을 계약체결 전에 고지하는 조치를 취합니다.\r\n" + 
				"\r\n" + 
				"제26조 [수신확인통지, 이용신청 변경 및 취소]\r\n" + 
				"① “회사”는 “회원”의 이용신청이 있는 경우 “회원”에게 수신확인통지를 합니다.\r\n" + 
				"② 수신확인통지를 받은 “회원”은 의사표시의 불일치 등이 있는 경우에는 수신확인통지를 받은 후 즉시"
				+ " 이용신청 변경 및 취소를 요청할 수 있고, “회사”는 “유료서비스” 제공 전에 “회원”의 요청이 있는"
				+ " 경우에는 지체 없이 그 요청에 따라 처리하여야 합니다. 다만, 이미 대금을 지불한 경우에는 "
				+ "청약철회 등에 관한 제35조의 규정에 따릅니다.\r\n" + 
				"제27조 [서비스의 이용시간]\r\n" + 
				"① 서비스의 이용은 연중무휴 1일 24시간을 원칙으로 합니다. 다만, “회사”의 업무상이나 기술상의 "
				+ "이유로 서비스가 일시 중지될 수 있고, 운영상의 목적으로 “회사”가 정한 기간에 “서비스”가 일시 "
				+ "중지될 수 있습니다. 이러한 경우 “회사”는 사전에 공지함이 원칙이나 불가피한 경우 사후에 공지할"
				+ " 수 있습니다.\r\n" + 
				"② “회사”는 “서비스”를 일정범위로 분할하여 각 범위 별로 이용 가능한 시간을 별도로 정할 수 "
				+ "있거나 서비스별 이용 가능시간을 “회사”의 정책으로 정할 수 있습니다.\r\n" + 
				"제28조 [서비스의 이용 및 저장]\r\n" + 
				"① “회사”는 “서비스”의 이용에 관하여 본 약관 외에 게시물 등의 관리정책을 별도로 둘 수 있으며, "
				+ "이를 통해 게시물 또는 기타 게재된 내용이 “서비스”에 의해 보유되는 최대 일수 및 용량, “서비스”"
				+ " 서버에 할당되는 최대 디스크 공간 및 정해진 기간 동안 “서비스”에 접속할 수 있는 최대한의 수"
				+ " (최대 기간) 등을 관리할 수 있습니다.\r\n" + 
				"② “회사”의 정책에도 불구하고 “회사”의 귀책사유 없이 제1항의 정책에 따라 “서비스”에 보관되는 "
				+ "게시물 또는 기타 게재된 내용 등을 삭제하거나 저장하지 못한데 대해 “회사”는 아무런 책임을 "
				+ "지지 않습니다.\r\n" + 
				"③ “회사”는 본 약관에 따른 “서비스”의 제공지역을 대한민국으로 한정할 수 있습니다.\r\n" + 
				"제29조 [정보 및 광고의 제공]\r\n" + 
				"① “회사”는 “회원”의 “서비스” 이용과 관련하여 필요한 정보를 “서비스” 홈페이지에 게재하거나 "
				+ "우편물, e-mail 및 어플리케이션, SMS, MMS 등을 통해 제공할 수 있습니다. 단, “회원”이 정보제공을"
				+ " 원치 않는다는 의사를 밝히는 경우 정보제공대상에서 해당 “회원”을 제외하며, 정보제공 대상에서 "
				+ "제외되어 정보를 제공받지 못해 불이익이 발생할 경우에는 “회사”는 이에 대한 책임을 지지 "
				+ "않습니다.\r\n" + 
				"② “회사”는 는 법령에 따른 의무가 있거나, “회원”이 명시 적으로 동의한 경우에 한하여 제1항과 "
				+ "동일한 방법으로 광고성 정보를 제공하며, “회원”이 본 항에 따른 회사의 광고성 정보의 제공에 "
				+ "동의하지 않는 경우에도 “서비스” 의 이용에는 아무런 제한이 없습니다.\r\n" + 
				"③ “회원”이 광고를 통한 제3의 서비스를 이용하는 것과 관련하여 발생하는 모든 문제에 대해서 "
				+ "“회사”는 어떠한 책임도 지지 않습니다.\r\n" + 
				"제4장 계약당사자의 의무\r\n" + 
				"제30조 [회사의 의무]\r\n" + 
				"① “회사”는 “회원”의 개인정보를 보호하고 보안시스템을 구비하여 “회원”이 안전하게 “서비스”를 "
				+ "이용할 수 있도록 제반 환경을 제공합니다.\r\n" + 
				"② “회사”는 제1항의 범위 내에서 업무와 관련하여 “회원”의 사전 동의 없이 “회원” 전체 또는 "
				+ "일부의 개인정보에 관한 통계자료를 작성하여 이를 사용할 수 있고, 이를 위하여 “회원”의 컴퓨터에"
				+ " 쿠키를 전송할 수 있습니다. 이 경우 “회원”은 쿠키의 수신을 거부하거나 쿠키의 수신에 대하여 "
				+ "경고하도록 사용하는 컴퓨터의 브라우저의 설정을 변경할 수 있습니다.\r\n" + 
				"③ “회사”는 “서비스”와 관련한 “회원”의 불만사항이 접수되는 경우 이를 신속하게 처리하여야 하며,"
				+ " 신속한 처리가 곤란한 경우 그 사유와 처리 일정을 “서비스” 화면에 게재하거나 전자우편 등을 "
				+ "통하여 동 “회원”에게 통지합니다.\r\n" + 
				"④ “회사”는 『정보통신망 이용촉진 및 정보보호에 관한 법률』, 『통신비밀보호법』, "
				+ "『전기통신사업법』 등 “서비스”의 운영, 유지와 관련 있는 법규를 준수합니다.\r\n" + 
				"제31조 [회원의 의무]\r\n" + 
				"① “회원”은 다음 각 호의 행위를 하여서는 아니 됩니다.\r\n" + 
				"1. 이용 신청 및 서비스를 제공받기 위해 등록 양식에서 요구되는 정보를 허위 또는 타인의 명의로"
				+ " 작성하거나, 다른 “회원”의 ID 및 비밀번호를 도용, 부정하게 사용하는 행위\r\n" + 
				"2. “회사”의 “서비스” 정보를 이용하여 얻은 정보를 “회사”의 사전 승낙 없이 복제 또는 "
				+ "유통시키거나 상업적으로 이용하는 행위\r\n" + 
				"3. 타인의 명예를 손상시키거나 불이익을 주는 행위\r\n" + 
				"4. 음란물을 게재, 공개하거나 음란사이트를 연결(링크)하는 행위\r\n" + 
				"5. “회사” 및 제3자의 지적재산권 등 기타 권리를 침해하는 행위\r\n" + 
				"6. 공공질서 및 미풍양속에 위반되는 내용의 정보, 문장, 도형, 음성 등을 타인에게 유포하는 "
				+ "행위\r\n" + 
				"7. “서비스”와 관련된 설비의 오 동작이나 정보 등의 파괴 및 혼란을 유발시키는 컴퓨터 "
				+ "바이러스 감염자료를 등록 또는 유포하는 행위\r\n" + 
				"제32조 [분쟁해결 등]\r\n" + 
				"① “회사”와 “회원”간 제기된 소송은 대한민국 법률을 준거법으로 합니다.\r\n" + 
				"② ”회사”와 “회원”간의 “콘텐츠” 이용계약에 관한 소의 관할은 제소 당시의 “회원”의 주소에 "
				+ "의하고, 주소가 없는 경우 거소를 관할하는 지방 법원의 전속관할로 합니다.\r\n" + 
				"③ 제소 당시 “회원”의 주소 또는 거소가 분명하지 아니한 경우에는 『민사소송법』에 따라 "
				+ "관할법원을 정합니다.\r\n" + 
				"부칙 (시행일)\r\n" + 
				"본 약관은 2018년 10월 16일부터 적용하고, 2017년 10월 19일부터 시행되던 종전의 약관은 본 "
				+ "약관으로 대체합니다.");
		
		check1.selectedProperty().addListener((observable, oldValue, newValue) ->{
			if(newValue == true) {
			}else {
			}
			
		});

		

		txtArea1.scrollTopProperty().addListener(e->{
			if(txtArea1.scrollTopProperty().intValue()==2934) {
				check1.setDisable(false);
			}else {
				check1.setSelected(false);
				check1.setDisable(true);				
			}
		});
		txtArea2.scrollTopProperty().addListener(e->{
			if(txtArea2.scrollTopProperty().intValue()==399) {
				check2.setDisable(false);
			}else {
				check2.setSelected(false);
				check2.setDisable(true);				
			}
		});
		
		txt_pw.setOnAction(e->{
			pwCheck();
		});
		
		txt_pwCheck.setOnAction(e->{
			pwEqualCheck();
		});
		
//		txt_bir.setOnAction(e->{
//			birCheck();
//		});
		
		txt_tel.setOnAction(e->{
			telCheck();
		});
		
		captchaKey = captchaKey();
		captchaImage(captchaKey);
		
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
                // 랜덤한 이름으로 파일 생성
                captchaImg = Long.valueOf(new Date().getTime()).toString();
                // 파일 저장위치 변경해보자 (d:/D_Other/복사본_Tulips.jpg)
                // join패키지에 넣자 일단.
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

	public void join() throws UnsupportedEncodingException, NoSuchAlgorithmException, GeneralSecurityException {
		AES256Util aes = new AES256Util();
		MemberVO vo = new MemberVO();
		
		String encryptedPw = aes.encrypt(txt_pw.getText());
		String decryptedPw = aes.decrypt(encryptedPw);
		
		String encryptedPwCheck = aes.encrypt(txt_pwCheck.getText());
		String decryptedPwCheck = aes.decrypt(encryptedPwCheck);
		
		String gender = group.getSelectedToggle().getUserData().toString();
		
		Date now = new Date();
		
		
		// 아이디 유효성 검사
		if(txt_id.getText().equals("")) {
			lb_ok.setVisible(true);
			lb_ok.setText("아이디를 입력해주세요.");
			lb_ok.setTextFill(Color.RED);
			txt_id.requestFocus();	
			return;
		}else if(!idFlag) {
			lb_ok.setVisible(true);
			lb_ok.setText("아이디 중복확인을 해주세요.");
			lb_ok.setTextFill(Color.RED);
			txt_id.requestFocus();
			return;
		}
		
		
		if(txt_pw.getText().equals("")) {
			lb_ok.setVisible(true);
			lb_ok.setText("비밀번호를 입력해주세요.");
			lb_ok.setTextFill(Color.RED);
			txt_pw.requestFocus();	
			return;
		}
		
		pwCheck();
		if(!pwFlag) {
        	lb_pw.setVisible(true);
        	lb_pw.setText("규칙에 맞게 입력해주세요.");
        	lb_pw.setTextFill(Color.RED);
        	txt_pw.requestFocus();
        	return;
		}
		
		if(txt_pwCheck.getText().equals("")) {
			lb_ok.setVisible(true);
			lb_ok.setText("비밀번호를 한번 더 입력해주세요.");
			lb_ok.setTextFill(Color.RED);
			txt_pwCheck.requestFocus();			
		}
		
		pwEqualCheck();
		
		if(!pwFlag2) {
			lb_ok.setVisible(true);
			lb_ok.setText("비밀번호가 일치하지 않습니다.");
			lb_ok.setTextFill(Color.RED);
			txt_pwCheck.requestFocus();		
			return;
		}
		
		if(txt_name.getText().equals("")) {
			lb_ok.setVisible(true);
			lb_ok.setText("이름을 입력해주세요.");
			lb_ok.setTextFill(Color.RED);
			txt_name.requestFocus();
			return;
		}
		
//		if(txt_bir.getText().equals("")) {
//			lb_ok.setVisible(true);
//			lb_ok.setText("생년월일을 입력해주세요.");
//			lb_ok.setTextFill(Color.RED);
//			txt_bir.requestFocus();
//			return;
//		}
		
		birCheck();
		
		if(!birFlag) {
			lb_ok.setVisible(true);
			lb_ok.setText("생년월일을 확인해주세요.");
			lb_ok.setTextFill(Color.RED);
			return;
		}
		
		if (txt_tel.getText().equals("")) {
			lb_ok.setVisible(true);
			lb_ok.setText("전화번호를 입력해주세요.");
			lb_ok.setTextFill(Color.RED);
			txt_tel.requestFocus();
			return;
		}
		
		telCheck();
			
		if (!telFlag) {
			lb_ok.setVisible(true);
			lb_ok.setText("전화번호를 확인해주세요.");
			lb_ok.setTextFill(Color.RED);
			txt_tel.requestFocus();
			return;
		}
		
		if (txt_email.getText().equals("")) {
			lb_ok.setVisible(true);
			lb_ok.setText("메일주소를 입력해주세요.");
			lb_ok.setTextFill(Color.RED);
			txt_email.requestFocus();
			return;
		}
		
		if(!emailFlag) {
			emailCheck();			
		}
		
		if (!emailFlag) {
			lb_ok.setVisible(true);
			lb_ok.setText("메일 인증을 완료해주세요.");
			lb_ok.setTextFill(Color.RED);
			txt_emailCheck.requestFocus();
			return;
		}
		
		if (txt_captcha.getText().equals("")) {
			lb_ok.setVisible(true);
			lb_ok.setText("보안문자를 입력해주세요.");
			lb_ok.setTextFill(Color.RED);
			txt_captcha.requestFocus();
		} else if (!captchaFlag) {
			lb_ok.setVisible(true);
			lb_ok.setText("보안문자를 확인을 완료해주세요.");
			lb_ok.setTextFill(Color.RED);
			txt_captcha.requestFocus();
		}else {
			vo.setMem_id(txt_id.getText());
			vo.setMem_pw(encryptedPw);
			vo.setMem_name(txt_name.getText());
			vo.setMem_email(txt_email.getText());
			
			String str = String.valueOf(picker.getValue());
			String str1 = str.substring(2, 4);
			String str2 = str.substring(5, 7);
			String str3 = str.substring(8, 10);
			String edit_str = str1+"/"+str2+"/"+str3;
			System.out.println(edit_str);
			vo.setMem_bir(edit_str);

			vo.setMem_gender(gender);
			vo.setMem_tel(txt_tel.getText());
			vo.setMem_grade("일반");
			vo.setMem_auth("f");
			vo.setMem_indate(sdf.format(now));

			vo.setMem_blacklist_tf("f");
			vo.setMem_del_tf("f");
			vo.setMem_black_cnt("0");
			vo.setMem_game("0");
			
			// insert vo
			try {
				int cnt = ijs.insert(vo);
				
				if(cnt>0){
					System.out.println(txt_id.getText() + "-회원 가입 성공");
				}else{
					System.out.println(txt_id.getText() + "-회원 가입 실패");
					return;
				}
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			
			System.out.println(vo.getMem_id());
//			FXMLLoader loader = new FXMLLoader(getClass().getResource("../../main/MusicMain.fxml"));
//			ScrollPane root = null;
//			try {
//				root = loader.load();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			
//			Scene scene = new Scene(root);
//			Stage primaryStage = (Stage) txt_id.getScene().getWindow();
//			primaryStage.setScene(scene);
			boxbox.setVisible(true);
			box.setVisible(false);
			pane.setVisible(false);
			
		}

	}
	
	public void idCheck() {
		// test
		System.out.println(picker.getValue());
		
		System.out.println("아이디중복확인");
		// 유효성 검사 후 아이디 확인
		Pattern p = Pattern.compile("(^[a-zA-Z0-9]{6,}$)");
		Matcher m = p.matcher(txt_id.getText());
		
        if(m.find()) { // m.find() -> true or false.
        	System.out.println("ok");
        	lb_id.setVisible(false);
        	
        	MemberVO vo = new MemberVO();
        	vo.setMem_id(txt_id.getText());
        	
    		Boolean idCheck = false;
    		try {
    			idCheck = ils.idCheck(txt_id.getText());
    			// DB에 id가 없을경우 -> false
    		} catch (RemoteException e) {
    			e.printStackTrace();
    		}
    		
    		if(!idCheck) {
    			// id 사용가능.
    			lb_id.setVisible(true);
    			lb_id.setText("사용가능합니다.");
    			idFlag = true;
    			lb_ok.setVisible(false);
    			lb_id.setTextFill(Color.valueOf("#00cc00"));
    			idFlag  = true;
    		}else {
    			lb_id.setVisible(true);
    			lb_id.setText("사용중인 아이디입니다.");
    			lb_id.setTextFill(Color.RED);
    	        txt_id.requestFocus();
    			
    		}
        }
        else{
        	System.out.println("no");
        	lb_id.setVisible(true);
        	lb_id.setText("규칙에 맞게 입력해주세요.");
        	lb_id.setTextFill(Color.RED);
        	txt_id.requestFocus();
        }  

	}
	
	public void pwCheck() {
		Pattern p = Pattern.compile("(^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[`~!@#$%^&*?]).{8,}$)");
		Matcher m = p.matcher(txt_pw.getText());
		
        if(m.find()) { // m.find() -> true or false.
        	System.out.println("ok");
        	pwFlag = true;
        	lb_ok.setVisible(false);
        	lb_ok.setVisible(false);
        	lb_pw.setVisible(false);
        }else{
        	System.out.println("no");
        	lb_pw.setVisible(true);
        	lb_pw.setText("규칙에 맞게 입력해주세요.");
        	lb_pw.setTextFill(Color.RED);
        	txt_pw.requestFocus();
        	
        }  
	}
	
	public void pwEqualCheck() {
		if(!pwFlag) {
			lb_pwCheck.setVisible(true);
			lb_pwCheck.setText("비밀번호를 먼저 입력해주세요.");			
			lb_pwCheck.setTextFill(Color.RED);
			txt_pw.requestFocus();
		}else if(!txt_pw.getText().equals(txt_pwCheck.getText())) {
			lb_pwCheck.setVisible(true);
			lb_pwCheck.setText("비밀번호가 일치하지 않습니다.");
			lb_pwCheck.setTextFill(Color.RED);
			txt_pwCheck.requestFocus();
		}else {
			lb_pwCheck.setVisible(true);
			lb_pwCheck.setText("확인되었습니다.");
			pwFlag2 = true;
			lb_pwCheck.setTextFill(Color.valueOf("#00cc00"));
		}
	}
	
	public void birCheck() {
		// 생년월일. 오늘 날짜보다 클 경우 birFlag -> false.
		boolean dateFlag = false;
		LocalDate currentDate = LocalDate.now();
		dateFlag = picker.getValue().isBefore(currentDate);
		
		if(!dateFlag) {
			lb_bir.setVisible(true);
			lb_bir.setText("생년월일을 확인해주세요.");
			lb_bir.setTextFill(Color.RED);
		}else {
			lb_bir.setVisible(false);
			birFlag = true;
		}
	}
	
	public boolean dateCheck(String date, String format) {
		SimpleDateFormat dateFormatParser = new SimpleDateFormat(format, Locale.KOREA);
		dateFormatParser.setLenient(false);
		try {
			dateFormatParser.parse(date);
			return true;
		} catch (Exception Ex) {
			return false;
		}
	}
	
	public void telCheck() {
		Pattern p = Pattern.compile("(^01\\d{9}$)");
		Matcher m = p.matcher(txt_tel.getText());
		
		if(!m.find()) {
			lb_tel.setVisible(true);
			lb_tel.setText("형식에 맞게 입력해주세요.");
			lb_tel.setTextFill(Color.RED);
			txt_tel.requestFocus();
		}else {
			telFlag = true;
			lb_ok.setVisible(false);
			lb_tel.setVisible(false);
		}
	}
	
	public void emailCheck() throws UnsupportedEncodingException, NoSuchAlgorithmException, GeneralSecurityException {
		AES256Util aes = new AES256Util();
		Pattern p = Pattern.compile("(^[a-zA-Z0-9]+@[a-zA-Z0-9.]+$)");
		Matcher m = p.matcher(txt_email.getText());
		
		if(txt_email.getText().equals("")) {
			lb_email.setVisible(true);
			lb_email.setText("메일주소을 입력해주세요.");
			lb_email.setTextFill(Color.RED);
			txt_email.requestFocus();
			return;
		}else if(!m.find()) {
			lb_email.setVisible(true);
			lb_email.setText("주소가 유효하지 않습니다.");
			lb_email.setTextFill(Color.RED);
			txt_email.requestFocus();
			return;
		}
		
		System.out.println(txt_email.getText());
		// java mail 로직. 인증코드 6자. 난수발생후 코드를..
		String ran = String.valueOf((int)(Math.random()*100)+1);
		code = aes.encrypt(ran).substring(0, 6);
		
		String host = "smtp.naver.com";
		final String user = "ykh1762@naver.com";
		final String password = "q1w2e3r4";

		System.out.println(txt_email.getText()+"로 이메일 발송");
		String to = txt_email.getText();
		
		Properties props = new Properties();
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.auth", "true");

		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(user, password);
			}
		});
		
		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(user));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

			message.setSubject("[JavaMail 인증코드] clap:음악, 그리고 설레임.");

			message.setText("clap:음악, 그리고 설레임. 다음의 인증코드를 입력하고 회원가입을 진행하세요. 인증코드 : "+code);

			// send the message
			Transport.send(message);
			System.out.println("message sent successfully...");

		} catch (MessagingException e) {
			e.printStackTrace();
		}
		
		lb_email.setVisible(true);
		lb_email.setText("인증코드가 메일로 발송되었습니다.");
		lb_email.setTextFill(Color.valueOf("#00cc00"));
		txt_emailCheck.requestFocus();
	}
	
	public void codeCheck() {
		if(txt_emailCheck.getText().equals("")) {
			lb_emailCheck.setVisible(true);
			lb_emailCheck.setText("인증코드를 입력해주세요.");
			lb_emailCheck.setTextFill(Color.RED);
			txt_emailCheck.requestFocus();			
		}else if(code == null) {
			lb_emailCheck.setVisible(true);
			lb_emailCheck.setText("인증코드 발송을 먼저 해주세요.");
			lb_emailCheck.setTextFill(Color.RED);
		}else if(!code.equals(txt_emailCheck.getText())) {
			lb_emailCheck.setVisible(true);
			lb_emailCheck.setText("인증코드가 일치하지 않습니다.");
			lb_emailCheck.setTextFill(Color.RED);
			txt_emailCheck.requestFocus();
		}else if(code.equals(txt_emailCheck.getText()) && code != null){
			lb_emailCheck.setVisible(true);
			lb_emailCheck.setText("확인되었습니다.");
			emailFlag = true;
			lb_ok.setVisible(false);
			lb_emailCheck.setTextFill(Color.valueOf("#00cc00"));
		}
	}
	
	public void imgRefresh() {
		captchaKey = captchaKey();
		captchaImage(captchaKey);
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
			lb_captcha2.setVisible(true);
			lb_captcha.setPadding(new Insets(0));
			lb_captcha.setText("보안문자가 일치하지 않습");
			lb_captcha2.setText("니다. 새로 입력해주세요.");
			captchaFlag = false;
			lb_captcha.setTextFill(Color.RED);
			lb_captcha2.setTextFill(Color.RED);
			txt_captcha.requestFocus();	
		}else if(result.equals("true,")) {
			lb_captcha.setVisible(true);
			lb_captcha2.setVisible(false);
			lb_captcha.setPadding(new Insets(3));
			lb_captcha.setText("확인되었습니다.");
			captchaFlag = true;
			lb_ok.setVisible(false);
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
	
	public void nextPage() {
		if(check1.isSelected() && check2.isSelected()) {
			// 둘 다 체크되어있을때 진행.
			pane.setVisible(true);
			box.setVisible(false);
			box_txt1.setVisible(false);
			box_txt2.setVisible(false);
			box.setLayoutY(650);
			pane.setLayoutY(0);
			
		}else {
			lb_next.setVisible(true);
			lb_next.setTextFill(Color.RED);
			
		}
	}
}
