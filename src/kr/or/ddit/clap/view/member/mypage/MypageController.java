package kr.or.ddit.clap.view.member.mypage;

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
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import kr.or.ddit.clap.main.LoginSession;
import kr.or.ddit.clap.service.musichistory.IMusicHistoryService;
import kr.or.ddit.clap.service.musicreview.IMusicReviewService;
import kr.or.ddit.clap.service.myalbum.IMyAlbumService;
import kr.or.ddit.clap.service.mypage.IMypageService;
import kr.or.ddit.clap.view.join.AES256Util;
import kr.or.ddit.clap.view.ticket.buylist.TicketBuyListController;
import kr.or.ddit.clap.vo.member.MemberVO;
import kr.or.ddit.clap.vo.music.MusicHistoryVO;
import kr.or.ddit.clap.vo.music.MusicReviewVO;
import kr.or.ddit.clap.vo.myalbum.MyAlbumVO;;

public class MypageController implements Initializable {

	static Stage mypageDialog = new Stage(StageStyle.DECORATED);
	static Stage pwok = new Stage(StageStyle.DECORATED);
	static Stage myalb = new Stage(StageStyle.DECORATED);
	
	public Stage primaryStage;

	private FileChooser fileChooser;
	private String img_path;
	private File filePath;
	private Registry reg;
	
	private IMypageService ims;
	private IMusicReviewService imrs;
	private IMusicHistoryService imhs;
	private IMyAlbumService imas;
	
	private ObservableList<MusicReviewVO> revList, currentrevList;
	private ObservableList<MusicHistoryVO> singList; // 최근음악 담기
	private ObservableList<MusicHistoryVO> newList;

	private String temp_img_path = "";

	@FXML Label label_Id;
	@FXML Image img_User;
	@FXML AnchorPane contents;
	@FXML Text text_UserInfo;
	@FXML AnchorPane InfoContents;
	@FXML AnchorPane main;

	@FXML JFXTreeTableView<MusicReviewVO> tbl_Review; // 리뷰
	@FXML TreeTableColumn<MusicReviewVO, String> col_ReviewCont;
	@FXML TreeTableColumn<MusicReviewVO, String> col_ReviewDate;

	@FXML JFXTreeTableView<MusicHistoryVO> tbl_ManySigner; // 많이들은 아티스트
	@FXML TreeTableColumn<MusicHistoryVO, String> col_MSits;

	@FXML JFXTreeTableView<MusicHistoryVO> tbl_ManyMusic; // 많이들은 곡
	@FXML TreeTableColumn<MusicHistoryVO, String> col_MMits;
	@FXML TreeTableColumn<MusicHistoryVO, String> col_MMtitle;

	@FXML JFXTreeTableView<MusicHistoryVO> tbl_NewMusic; // 최근들은곡
	@FXML TreeTableColumn<MusicHistoryVO, String> col_NMits;
	@FXML TreeTableColumn<MusicHistoryVO, String> col_NMtitle;
	@FXML TreeTableColumn<MusicHistoryVO, String> col_NMdate;
	@FXML ImageView img_UserImg;
	
	
	@FXML JFXTreeTableView<MyAlbumVO> tbl_Myalb; //마이앨범
	private ObservableList<MyAlbumVO> myAlbList, currentsingerList;
	@FXML TreeTableColumn<MyAlbumVO,String> col_Myalb;
	private int from, to, itemsForPage, totalPageCnt;
	@FXML Pagination p_Paging;
private MypageMyAlbListController iAC;
	
	public void setcontroller(MypageMyAlbListController iAC){
		this.iAC = iAC;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		try {
			reg = LocateRegistry.getRegistry("localhost", 8888);
			ims  = (IMypageService) reg.lookup("mypage");
			imrs = (IMusicReviewService) reg.lookup("musicreview");
			imhs = (IMusicHistoryService) reg.lookup("history");
			imas = (IMyAlbumService) reg.lookup("myalbum");
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}

		String user_id = LoginSession.session.getMem_id();
		label_Id.setText(user_id+" 님"); // 현재 로그인한 사용자 아이디 가져오기
		MemberVO vo = new MemberVO();
		vo.setMem_id(user_id);
		MemberVO memvo = new MemberVO();
		try {
			memvo = ims.select(vo);
			text_UserInfo.setText(memvo.getMem_intro());
			
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// 이미지 설정
		if(memvo.getMem_image()==null) {
				memvo.setMem_image("file:\\\\Sem-pc\\공유폴더\\Clap\\img\\userimg\\icons8-person-64.png");
			
			
		}
		Image img = new Image(memvo.getMem_image());
		temp_img_path = memvo.getMem_image(); // sVO.getSing_image()를 전역으로 쓰기위해
		img_UserImg.setImage(img);

		// 최근댓글테이블
		col_ReviewCont.setCellValueFactory(
				param -> new SimpleStringProperty(param.getValue().getValue().getMus_re_content()));
		col_ReviewDate.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getIndate().substring(0, 10)));

		// 최근많이 들은 아티스트이름넣기
		col_MSits.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getName()));

		// 최근많이 들은 곡
		col_MMits.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getName()));
		col_MMtitle.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getTitle()));

		// 최근 감상곡
		col_NMits.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getName()));
		col_NMtitle.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getTitle()));
		col_NMdate.setCellValueFactory(
				param -> new SimpleStringProperty(param.getValue().getValue().getHisto_indate().substring(0, 10)));

		// 데이터 삽입
		MusicReviewVO muvo = new MusicReviewVO();
		muvo.setMem_id(user_id);
		MusicHistoryVO muh = new MusicHistoryVO();
		muh.setMem_id(user_id);
		try {
			revList = FXCollections.observableArrayList(imrs.selectReview(muvo));
			singList = FXCollections.observableArrayList(imhs.selectMayIts(muh));
			newList = FXCollections.observableArrayList(imhs.selectMayIndate(muh));
		} catch (RemoteException e) {
			System.out.println("에러");
			e.printStackTrace();
		}

		TreeItem<MusicReviewVO> root = new RecursiveTreeItem<>(revList, RecursiveTreeObject::getChildren);
		tbl_Review.setRoot(root);
		tbl_Review.setShowRoot(false);

		TreeItem<MusicHistoryVO> root1 = new RecursiveTreeItem<>(singList, RecursiveTreeObject::getChildren);
		tbl_ManySigner.setRoot(root1);
		tbl_ManySigner.setShowRoot(false);
		tbl_ManyMusic.setRoot(root1);
		tbl_ManyMusic.setShowRoot(false);

		TreeItem<MusicHistoryVO> root2 = new RecursiveTreeItem<>(newList, RecursiveTreeObject::getChildren);
		tbl_NewMusic.setRoot(root2);
		tbl_NewMusic.setShowRoot(false);
		
		//마이앨범 
		try {
			myAlbList = FXCollections.observableArrayList(imas.myAlbumSelect(user_id));
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		col_Myalb.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getMyalb_name()));
		
		TreeItem<MyAlbumVO> root3 = new RecursiveTreeItem<>(myAlbList, RecursiveTreeObject::getChildren);
		tbl_Myalb.setRoot(root3);
		tbl_Myalb.setShowRoot(false);
		
		itemsForPage = 10; // 한페이지 보여줄 항목 수 설정

		paging();
		
		tbl_Myalb.setOnMouseClicked(e2 ->{
			if (e2.getClickCount()  > 1) {

				String myAlbNo = tbl_Myalb.getSelectionModel().getSelectedItem().getValue().getMyalb_no();
				String myAlbName = tbl_Myalb.getSelectionModel().getSelectedItem().getValue().getMyalb_name();
				
				try {
					// 바뀔 화면(FXML)을 가져옴
					MypageMyAlbListController.myAlbNo = myAlbNo;// 번호을 변수로 넘겨줌
					MypageMyAlbListController.myAlbName = myAlbName;// 번호을 변수로 넘겨줌
					
					FXMLLoader loader = new FXMLLoader(getClass().getResource("myalblist.fxml"));// init실행됨
					Parent myAlblists = loader.load();
					contents.getChildren().removeAll();
					contents.getChildren().setAll(myAlblists);

				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			
		});
		
	}
	
	private void paging() {
		totalPageCnt = myAlbList.size() % itemsForPage == 0 ? myAlbList.size() / itemsForPage
				: myAlbList.size() / itemsForPage + 1;

		p_Paging.setPageCount(totalPageCnt); // 전체 페이지 수 설정

		p_Paging.setPageFactory((Integer pageIndex) -> {

			from = pageIndex * itemsForPage;
			to = from + itemsForPage - 1;

			TreeItem<MyAlbumVO> root = new RecursiveTreeItem<>(getTableViewData(from, to),
					RecursiveTreeObject::getChildren);
			tbl_Myalb.setRoot(root);
			tbl_Myalb.setShowRoot(false);
			return tbl_Myalb;
		});

	}

	
	private ObservableList<MyAlbumVO> getTableViewData(int from, int to) {

		currentsingerList = FXCollections.observableArrayList(); //
		int totSize = myAlbList.size();
		for (int i = from; i <= to && i < totSize; i++) {

			currentsingerList.add(myAlbList.get(i));
		}

		return currentsingerList;
	}


	@FXML
	public void btn_profch() throws IOException {// 프로필 수정 클릭시
		Parent root = FXMLLoader.load(getClass().getResource("profile.fxml"));
		Scene scene = new Scene(root);
		mypageDialog.setTitle("모여서 각잡고 코딩 - clap");

		mypageDialog.setScene(scene);
		mypageDialog.show();

		Button btnCl = (Button) root.lookup("#btn_ProfileCl");
		btnCl.setOnAction(e -> {
			mypageDialog.close();
		});

		String user_id = LoginSession.session.getMem_id();
		MemberVO vo = new MemberVO();
		vo.setMem_id(user_id);
		MemberVO memvo = new MemberVO();
		try {
			memvo = ims.select(vo);			//사용자 자기소개 출력
			TextField textF_Info = (TextField) root.lookup("#textF_Info");
			textF_Info.setText(memvo.getMem_intro());
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		if(memvo.getMem_image()==null) {
			memvo.setMem_image("file:\\\\Sem-pc\\공유폴더\\Clap\\img\\userimg\\icons8-person-64.png");
	}
		Image img = new Image(memvo.getMem_image());
		img_path = memvo.getMem_image(); // sVO.getSing_image()를 전역으로 쓰기위해
		ImageView imgview_UserImg = (ImageView) root.lookup("#imgview_UserImg");
		imgview_UserImg.setImage(img);

		Button btn_Image = (Button) root.lookup("#btn_Image");//찾아보기 클릭시
		btn_Image.setOnAction(e1 -> {
			Stage stage = (Stage) ((Node) e1.getSource()).getScene().getWindow();
			fileChooser = new FileChooser();
			fileChooser.setTitle("Open image");
			String userDirectoryString = "\\\\Sem-pc\\공유폴더\\Clap\\img\\userimg";

			System.out.println("userDirectoryString:" + userDirectoryString);
			File userDirectory = new File(userDirectoryString);

			if (!userDirectory.canRead()) {
				userDirectory = new File("c:/");
			}

			fileChooser.setInitialDirectory(userDirectory);
			;

			this.filePath = fileChooser.showOpenDialog(stage);

			// 이미지를 새로운 이미지로 바꿈
			try {
				BufferedImage bufferedImage = ImageIO.read(filePath);
				Image image = SwingFXUtils.toFXImage(bufferedImage, null);
				imgview_UserImg.setImage(image);
				String str_filePath = "file:" + filePath;
				// 절대경로로 이미지를 읽기위해서
				img_path = str_filePath;
				TextField textF_File =  (TextField) root.lookup("#textF_File");
				textF_File.setText(str_filePath);
			} catch (Exception e) {
				// System.out.println(e.getMessage());
				// e.printStackTrace();
				System.out.println("이미지를 선택하지 않았습니다.");
			}
			
			
		});
		
			

		JFXCheckBox chBox_del = (JFXCheckBox) root.lookup("#chBox_del");//삭제 버튼 클릭시
		chBox_del.setOnAction(e1 -> {
			img_path = "file:\\\\Sem-pc\\공유폴더\\Clap\\img\\userimg\\icons8-person-64.png"; 
			Image img2 = new Image(img_path);
			imgview_UserImg.setImage(img2);
			
			// sVO.getSing_image()를 전역으로 쓰기위해
			
			
		});
		
		
		JFXButton btn_InfoDel = (JFXButton) root.lookup("#btn_InfoDel");// x표시 클릭시
		btn_InfoDel.setOnAction(e->{
			TextField textF_Info = (TextField) root.lookup("#textF_Info");
			textF_Info.setText("");
		});

		Button btn_ProfileOk = (Button) root.lookup("#btn_ProfileOk");// 설정 클릭시
		btn_ProfileOk.setOnAction(eee -> {
			
			TextField textF_Info = (TextField) root.lookup("#textF_Info");
			System.out.println(textF_Info.getText());
			System.out.println(img_path);
			MemberVO vo1 = new MemberVO();
			vo1.setMem_intro(textF_Info.getText());
			vo1.setMem_image(img_path);
			vo1.setMem_id(user_id);
			try {
				int c=ims.updateImage(vo1);
			} catch (RemoteException e) {
				e.printStackTrace();

			}
			mypageDialog.close();
			chagePage();
		});
		
		

	}

	@FXML
	public void btn_my() throws IOException { // 내정보 클릭시
		Parent root = FXMLLoader.load(getClass().getResource("pwcheck.fxml"));
		Scene scene = new Scene(root);
		pwok.setTitle("모여서 각잡고 코딩 - clap");

		pwok.setScene(scene);
		pwok.show();

		Button btn = (Button) root.lookup("#btn_Ok");

		btn.setOnAction(new EventHandler<ActionEvent>()  {

			@Override
			public void handle(ActionEvent event) {
				AES256Util aes = null;
				try {
					aes = new AES256Util();
				} catch (UnsupportedEncodingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				JFXPasswordField fild = (JFXPasswordField) root.lookup("#fild_ok");

				String encryptedPw = ""; // 암호화된 pw
				try {
					encryptedPw = aes.encrypt(fild.getText());
				} catch (UnsupportedEncodingException | GeneralSecurityException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				String decryptedPw = ""; // 복호화시킨 pw
				try {
					decryptedPw = aes.decrypt(encryptedPw);
				} catch (UnsupportedEncodingException | GeneralSecurityException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				String user_id = LoginSession.session.getMem_id();
				label_Id.setText(user_id); // 현재 로그인한 사용자 아이디 가져오기

				MemberVO vo = new MemberVO();
				vo.setMem_id(user_id);
				MemberVO vo2 = new MemberVO();

				try {
					vo2 = ims.select(vo);
				} catch (RemoteException e) {
					System.out.println("에러입니다");
					e.printStackTrace();
				}

				
				if (encryptedPw.equals(vo2.getMem_pw())) {
					pwok.close();

					Parent root1 = null;
					try {
						root1 = FXMLLoader.load(getClass().getResource("../mypageCh/mypageCh_Info.fxml"));
						contents.getChildren().removeAll();
						contents.getChildren().setAll(root1);
					} catch (IOException e) {
						e.printStackTrace();
					}

				} else {
					Text test_set = (Text) root.lookup("#text");
					test_set.setText("\t   잘못된 비밀번호를 입력하였습니다.");
					fild.clear();
				}
			}
		});// btn_Ok

		Button btn_no = (Button) root.lookup("#btn_Cl");
		btn_no.setOnMouseClicked(e -> {
			pwok.close();
		});

	}

	@FXML
	public void btn_Info() throws IOException { // 연필모양 (사용자 개인소개)수정

		Parent root = null;
		try {
			root = FXMLLoader.load(getClass().getResource("mypageSub.fxml"));
			InfoContents.getChildren().removeAll();
			InfoContents.getChildren().setAll(root);
		} catch (IOException e) {
			e.printStackTrace();
		}

		String user_id = LoginSession.session.getMem_id();
		MemberVO vo = new MemberVO();
		vo.setMem_id(user_id);
		MemberVO memvo = new MemberVO();
		try {
			memvo = ims.select(vo);
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		JFXButton btn_SubOk = (JFXButton) root.lookup("#btn_SubOk");
		JFXButton btn_SubCl = (JFXButton) root.lookup("#btn_SubCl");
		JFXTextField textF_Sub = (JFXTextField) root.lookup("#textF_Sub");
		textF_Sub.setText(memvo.getMem_intro());

		btn_SubOk.setOnAction(e1 -> {
			vo.setMem_intro(textF_Sub.getText());
			try {
				ims.updateInfo(vo);
			} catch (RemoteException e) {
				e.printStackTrace();
			}

			System.out.println("완료");

			try {
				Parent root2 = FXMLLoader.load(getClass().getResource("Mypage.fxml"));
				main.getChildren().removeAll();
				InfoContents.getChildren().removeAll();
				contents.getChildren().removeAll();
				main.getChildren().setAll(root2);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});

		btn_SubCl.setOnAction(e1 -> {
			try {
				Parent root2 = FXMLLoader.load(getClass().getResource("Mypage.fxml"));
				main.getChildren().removeAll();
				InfoContents.getChildren().removeAll();
				contents.getChildren().removeAll();
				main.getChildren().setAll(root2);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

	@FXML
	public void btn_like() throws IOException { // 좋아요클릭시

		try {
			Parent root = FXMLLoader.load(getClass().getResource("../profiles/like.fxml"));
			contents.getChildren().removeAll();
			contents.getChildren().setAll(root);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public void chagePage() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Mypage.fxml"));// init실행됨
		Parent mypage;
		try {
			mypage = loader.load();
			main.getChildren().removeAll();
			InfoContents.getChildren().removeAll();
			contents.getChildren().removeAll();
			contents.getChildren().removeAll();
			main.getChildren().setAll(mypage);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void btn_riew() throws IOException { // 리뷰클릭시

		try {
			Parent root = FXMLLoader.load(getClass().getResource("../profiles/review.fxml"));
			contents.getChildren().removeAll();
			contents.getChildren().setAll(root);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	@FXML public void btn_MyalbCh(ActionEvent event) throws IOException { //마이앨범 편집 클릭시
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("myalbCh.fxml"));
		Parent ChangeAlbMusic = loader.load();

		// Controller를 받아온다.
		MypageMyAlbController cotroller = loader.getController();
		
		// Controller에 setcontroller메소드를 정의한다.
		// 이 메소드는 this로 받은 자기자신객체를 Controller객체의 멤버변수로 set한다.
		cotroller.setcontroller(this);
		
		Scene scene = new Scene(ChangeAlbMusic);
		myalb.setTitle("모여서 각잡고 코딩 - clap");
		myalb.setScene(scene);
		myalb.show();
		
	
		
	}
	
	
	public void ref()  {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Mypage.fxml"));
		Parent ChangeAlbMusic;
		try {
			ChangeAlbMusic = loader.load();

			main.getChildren().removeAll();
			main.getChildren().setAll(ChangeAlbMusic);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

	@FXML public void btn_ThrReview() {
		try {
		Parent root = FXMLLoader.load(getClass().getResource("../profiles/review.fxml"));
		contents.getChildren().removeAll();
		contents.getChildren().setAll(root);

	} catch (IOException e) {
		e.printStackTrace();
	}

	}

	@FXML public void btn_TheMus() {
		try {
		Parent root = FXMLLoader.load(getClass().getResource("../profilesmus/thenewmus.fxml"));
		contents.getChildren().removeAll();
		contents.getChildren().setAll(root);

	} catch (IOException e) {
		e.printStackTrace();
	}
	}

	@FXML public void btn_NewLisMus() {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("../profilesmus/thenewmus.fxml"));
			contents.getChildren().removeAll();
			contents.getChildren().setAll(root);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML public void btn_ManyLisMus() {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("../profilesmus/themanymus.fxml"));
			contents.getChildren().removeAll();
			contents.getChildren().setAll(root);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML public void btn_ManyLisIts() {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("../profilesmus/themanyits.fxml"));
			contents.getChildren().removeAll();
			contents.getChildren().setAll(root);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@FXML public void btn_ticketList() { //구매내역 클릭
		

		try {
			FXMLLoader loader2 = new FXMLLoader(getClass().getResource("../../ticket/buylist/ticketbuylist.fxml"));
			Parent ticket = loader2.load();
			TicketBuyListController cotrollerti = loader2.getController();
			cotrollerti.setController(this);
			contents.getChildren().removeAll();
			contents.getChildren().setAll(ticket);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//구매내역에서 이용권구매창으로 이동
	public void chTicket() {
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("../../ticket/ticket/Ticket.fxml"));
		Parent ChangeAlbMusic;
		try {
			ChangeAlbMusic = loader.load();
			main.getChildren().removeAll();
			main.getChildren().setAll(ChangeAlbMusic);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
	}
	
	 public void chAlbname (MyAlbumVO vo) {
		 TreeItem<MyAlbumVO> root = new RecursiveTreeItem<>(myAlbList, RecursiveTreeObject::getChildren);
		 tbl_Myalb.setRoot(root);
		 tbl_Myalb.setShowRoot(false);
	 }

}
