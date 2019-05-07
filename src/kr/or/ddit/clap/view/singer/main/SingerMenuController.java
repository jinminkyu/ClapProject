package kr.or.ddit.clap.view.singer.main;

import java.io.IOException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.LongUnaryOperator;

import javax.swing.plaf.synth.SynthSpinnerUI;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.controls.JFXTextArea;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import kr.or.ddit.clap.main.LoginSession;
import kr.or.ddit.clap.main.MusicMainController;
import kr.or.ddit.clap.service.album.IAlbumService;
import kr.or.ddit.clap.service.music.IMusicService;
import kr.or.ddit.clap.service.playlist.IPlayListService;
import kr.or.ddit.clap.service.singer.ISingerService;
import kr.or.ddit.clap.view.chartmenu.dialog.MyAlbumDialogController;
import kr.or.ddit.clap.view.chartmenu.musiclist.MusicList;
import kr.or.ddit.clap.view.member.mypage.OtherMypageController;
import kr.or.ddit.clap.view.musicplayer.MusicPlayerController;
import kr.or.ddit.clap.vo.album.AlbumVO;
import kr.or.ddit.clap.vo.music.MusicVO;
import kr.or.ddit.clap.vo.music.PlayListVO;
import kr.or.ddit.clap.vo.singer.SingerVO;

/**
 * 
 * @author 박경훈
 *
 */

public class SingerMenuController implements Initializable {

	public static int menuCount = 0;
	public static boolean detailFlag = false; // true면 뮤직 디테일 화면으로.
	ObservableList<Map<String, String>> replyMap1; // 앨범의 리플을 담는 맵
	ObservableList<Map<String, String>> replyMap2; // 곡의 리플을 담는 맵
	Map<String, String> pMap = new HashMap<String, String>();
	Map<String, String> pMap_mus = new HashMap<String, String>();
	int yn = 0;
	int yn2 = 0;

	@FXML
	JFXTabPane tabPane;
	@FXML
	Tab tab_main;
	@FXML
	Tab tab_album;
	@FXML
	Tab tab_music;
	@FXML
	AnchorPane main;
	@FXML
	StackPane singerMain;
	@FXML
	StackPane singerAlbum;
	@FXML
	StackPane singerMusic;

	public static String albumNo; // 파라미터로 받은 선택한 가수의 PK
	private IAlbumService ias;
	private ISingerService iss;
	private String temp_img_path = "";

	// 파라미터로 넘기기 위해 전역으로 선언
	public AlbumVO aVO = null;
	public SingerVO sVO = null;
	public String str_like_cnt;
	public static AnchorPane contents;

	// @FXML Label label_singNo;
	@FXML
	Label label_albumName1;
	@FXML
	Label label_singerName;
	@FXML
	Label label_saledate;
	@FXML
	Label label_saleEnter;
	@FXML
	Label label_entertain;
	@FXML
	Label label_LikeCnt;
	@FXML
	ImageView imgview_albumImg;
	@FXML
	Label txt_intro;

	@FXML
	VBox box;
	@FXML
	Label lb_singer;

	@FXML
	VBox mainBox, musicBox;

	@FXML
	VBox reply_vbox1;
	@FXML
	VBox reply_vbox2;

	@FXML
	JFXCheckBox cb_main;
	@FXML
	JFXButton btn_Song;
	@FXML
	JFXButton btn_Pop;
	@FXML
	JFXButton btn_Ost;
	@FXML
	JFXButton btn_Other;
	@FXML
	StackPane stackpane;

	@FXML
	Label lb_total;
	@FXML
	Label lb_intro;
	@FXML
	Line line_intro;

	public static String musicNo = "1084";// 파라미터로 받은 선택한 가수의 PK
	private Registry reg;
	private IMusicService ims;
	private String temp_img_path2 = "";

	// 파라미터로 넘기기 위해 전역으로 선언
	public MusicVO mVO = null;
	public String str_like_cnt2;
	// public static AnchorPane contents;

	// @FXML Label label_singNo;

	// @FXML
	// AnchorPane main;
	@FXML
	Label label_musicTitle;
	// @FXML Label label_musicTitle2;
	@FXML
	ImageView imgview_albumImg2;
	@FXML
	Label txt_albName;
	@FXML
	Label txt_singerName;
	@FXML
	Label txt_write;
	@FXML
	Label txt_edit;
	@FXML
	Label txt_muswrite;
	@FXML
	Label txt_file;
	@FXML
	Label txt_fileVideo;
	@FXML
	Label lb_genre;
	@FXML
	Label lb_genreDetail;
	@FXML
	Label txt_time;
	@FXML
	Label label_LikeCnt2;

	@FXML
	JFXTextArea txt_lyrics;

	private IPlayListService ipls;
	private MusicList musicList;
	private ObservableList<Map> songRank;
	private ObservableList<JFXCheckBox> cbnList = FXCollections.observableArrayList();
	private ObservableList<JFXButton> btnPlayList = FXCollections.observableArrayList();
	private ObservableList<JFXButton> btnAddList = FXCollections.observableArrayList();
	private ObservableList<JFXButton> btnPutList = FXCollections.observableArrayList();
	private ObservableList<JFXButton> btnMovieList = FXCollections.observableArrayList();
	private MusicPlayerController mpc;
	private int itemsForPage;
	private Pagination p_page;
	private Pagination p_page1; // 앨범
	private Pagination p_page2; // 곡
	@FXML
	FontAwesomeIcon icon_heart;
	@FXML
	FontAwesomeIcon icon_heart2;

	/*
	 * // ShowSingerList.fxml는 VBOX를 포함한 전부이기 때문에 // 현재 씬의 VBox까지 모두 제거 후
	 * ShowSingerList를 불러야함. public void givePane(AnchorPane contents) {
	 * this.contents = contents; System.out.println("contents 적용완료"); }
	 */
	public void setcontroller(AnchorPane main) {
		this.main = main;

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		System.out.println("화면시작");
		if (menuCount == 0) {
			musicBox.setVisible(false);

		} else if (menuCount == 1) {
			box.setVisible(true);
			reply_vbox1.setVisible(true);
			stackpane.setVisible(true);
			line_intro.setVisible(true);
			lb_intro.setVisible(true);
			txt_intro.setVisible(true);
			musicBox.setVisible(false);

		} else if (menuCount == 2) {
			box.setVisible(false);
			reply_vbox1.setVisible(false);

			stackpane.setVisible(false);
			line_intro.setVisible(false);
			lb_intro.setVisible(false);
			txt_intro.setVisible(false);
			musicBox.setVisible(true);
			singerMusic.setVisible(false);
		}

		try {
			// AnchorPane pane = FXMLLoader.load(getClass().getResource("SingerMain.fxml"));

			/////

			FXMLLoader loader = new FXMLLoader(getClass().getResource("SingerMain.fxml"));
			Parent SingerMain = loader.load();
			SingerMainController cotroller = loader.getController();

			cotroller.setcontroller(main);
			///

			singerMain.getChildren().removeAll();
			singerMain.getChildren().setAll(SingerMain);

			StackPane pane2 = FXMLLoader.load(getClass().getResource("SingerAlbum.fxml"));
			pane2.setVisible(false);
			singerAlbum.getChildren().removeAll();
			singerAlbum.getChildren().setAll(pane2);

			StackPane pane3 = FXMLLoader.load(getClass().getResource("SingerMusic.fxml"));
			singerMusic.getChildren().removeAll();
			singerMusic.getChildren().setAll(pane3);

			// if(SingerMenuController.detailFlag) {
			// Parent pane = null;
			// pane = FXMLLoader.load(getClass().getResource("SingerMusicDetail.fxml"));
			//
			// pane3.setVisible(false);
			// singerMusic.getChildren().setAll(pane);
			// }

			tabPane.getSelectionModel().select(menuCount);
			// selected tab 값을 가져와야됨.
			tabPane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
				@Override
				public void changed(ObservableValue<? extends Tab> ov, Tab t, Tab t1) {
					System.out.println(tabPane.getSelectionModel().getSelectedIndex());

					box.setVisible(false);
					reply_vbox1.setVisible(false);

					mainBox.setVisible(false);
					line_intro.setVisible(false);
					lb_intro.setVisible(false);
					txt_intro.setVisible(false);
					pane2.setVisible(true);
					pane3.setVisible(true);

					musicBox.setVisible(false);
					// singerMusic.setVisible(false);

					if (tabPane.getSelectionModel().getSelectedIndex() == 2) {
						// pane3.setVisible(true);
					}

				}
			});

			tabPane.setOnMouseClicked(e -> {
				// singerMusic.setVisible(false);
				// singerMusic.getChildren().removeAll();
				// singerMusic.getChildren().setAll(pane3);
				box.setVisible(false);
				reply_vbox1.setVisible(false);

				stackpane.setVisible(false);
				line_intro.setVisible(false);
				lb_intro.setVisible(false);
				txt_intro.setVisible(false);
				pane2.setVisible(true);
				musicBox.setVisible(false);
				singerMusic.setVisible(true);
			});

			// StackPane pane_main =
			// FXMLLoader.load(getClass().getResource("SingerMainMusic.fxml"));
			// pane_main.setLayoutY(350);
			// singerMain.getChildren().setAll(pane_main);

		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("앨범번호:" + albumNo);

		System.out.println("init: 곡 번호" + musicNo);
		try {
			// reg로 ISingerService객체를 받아옴
			reg = LocateRegistry.getRegistry("localhost", 8888);
			ias = (IAlbumService) reg.lookup("album");
			aVO = ias.albumDetailInfo(albumNo);

			iss = (ISingerService) reg.lookup("singer");
			sVO = iss.singerDetailInfo(aVO.getSing_no());

			ims = (IMusicService) reg.lookup("music");
			mVO = ims.selectMusicDetailInfo(musicNo);

			System.out.println(aVO.getSing_no());
			// 파라미터로 받은 정보를 PK로 상세정보를 가져옴
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}

		lb_singer.setText(sVO.getSing_name());
		lb_singer.setOnMouseClicked(e -> {
			box.setVisible(false);
			reply_vbox1.setVisible(false);
			tabPane.getSelectionModel().select(0);
		});

		label_albumName1.setText(aVO.getAlb_name());
		// label_albumName2.setText(aVO.getAlb_name());
		label_singerName.setText(aVO.getSing_name());
		String saledateEdit = aVO.getAlb_saledate().substring(0, 10);
		label_saledate.setText(saledateEdit);
		label_saleEnter.setText(aVO.getAlb_sale_enter());

		label_entertain.setText(aVO.getAlb_entertain());
		txt_intro.setText(aVO.getAlb_intro());

		Image img = new Image(aVO.getAlb_image());
		temp_img_path = aVO.getAlb_image(); // aVO.getSing_image()를 전역으로 쓰기위해
		imgview_albumImg.setImage(img);

		// 좋아요 수를 가져오는 쿼리
		int like_cnt = 0;
		try {
			like_cnt = ims.selectMusicLikeCnt(musicNo);

		} catch (RemoteException e) {
			e.printStackTrace();
		}

		// 좋아요는 다른 VO에서 가져와야함...
		str_like_cnt = like_cnt + "";
		label_LikeCnt.setText(str_like_cnt);

		System.out.println(mVO.getMus_title());
		label_musicTitle.setText(mVO.getMus_title());
		// label_musicTitle2.setText(mVO.getMus_title());
		Image img2 = new Image(mVO.getAlb_image());
		temp_img_path2 = mVO.getAlb_image(); // 전역으로 쓰기위해서
		imgview_albumImg2.setImage(img2);
		txt_albName.setText(mVO.getAlb_name());
		txt_singerName.setText(mVO.getSing_name());

		txt_write.setText(mVO.getMus_write_son());
		txt_edit.setText(mVO.getMus_edit_son());
		txt_muswrite.setText(mVO.getMus_muswrite_son());
		// txt_file.setText(mVO.getMus_file());
		// txt_fileVideo.setText(mVO.getMus_mvfile());
		lb_genre.setText(mVO.getGen_name());

		lb_genreDetail.setText(mVO.getGen_detail_name());
		txt_time.setText(mVO.getMus_time());
		txt_lyrics.setEditable(false);
		txt_lyrics.setText(mVO.getMus_lyrics());

		// 앨범 좋아요 수를 가져오는 쿼리
		int like_cnt2 = 0;
		try {
			like_cnt2 = ias.selectAlbumLikeCnt(albumNo);
			// 좋아요는 다른 VO에서 가져와야함...
			str_like_cnt2 = like_cnt2 + "";
			label_LikeCnt.setText(str_like_cnt2);

			// 세션아이디와 앨범번호를 매개변수로 좋아요를 눌렀는 지 확인하는 메서드
			String id = LoginSession.session.getMem_id();
			pMap.put("albNo", albumNo);
			pMap.put("id", id);

			yn = ias.checkHeartYN(pMap);
			icon_heart.setIconName("HEART_ALT"); // 초기화 빈하트

			if (yn > 0) {
				icon_heart.setIconName("HEART");
			}
			
				// 곡 상세 좋아요 기능
				int like_cnt3 = 0;
				like_cnt3 = ims.selectMusicLikeCnt(musicNo);
				System.out.println("곡 좋아요 상세쿼리갯수@@@@@" + like_cnt3);
				String str_like_cnt3 = like_cnt3 + "";
				label_LikeCnt2.setText(str_like_cnt3);
				pMap_mus.put("musNo", musicNo);
				pMap_mus.put("id", id);

				yn2 = ims.checkHeartYN(pMap_mus);
				icon_heart2.setIconName("HEART_ALT"); // 초기화 빈하트
				if (yn2 > 0) {
					icon_heart2.setIconName("HEART");
				}
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		try {
			// reg로 ISingerService객체를 받아옴
			reg = LocateRegistry.getRegistry("localhost", 8888);

			ims = (IMusicService) reg.lookup("music");
			ipls = (IPlayListService) reg.lookup("playlist");
			itemsForPage = 5;
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}

		musicList = new MusicList(cbnList, btnPlayList, btnAddList, btnPutList, btnMovieList, mainBox, stackpane);

		// 일간 조회 차트
		songChart();

		// 앨범 상세보기의 댓글창생성 및 조회 앨범은 1로 곡 상세보기는 2로 표시
		// 댓글조회
		try {
			System.out.println("albumNo:" + albumNo);
			replyMap1 = FXCollections.observableArrayList(ias.selectAlbReply(albumNo));
			System.out.println("리플사이즈:" + replyMap1.size());
			int size = replyMap1.size(); // ?

			// 댓글 창 생성
			HBox HboxReply = new HBox();
			HboxReply.setPrefWidth(100);
			HboxReply.setPrefHeight(20);
			HboxReply.setPadding(new Insets(0, 0, 10, 0));

			// 댓글 라벨생성
			Label reply = new Label();
			reply.setFont(Font.font("-윤고딕350", 14));
			reply.setTextFill(Color.valueOf("#000"));
			reply.setPrefWidth(40);
			reply.setPrefHeight(40);
			reply.setText("댓글");

			// 갯수라벨 생성
			Label replyCnt = new Label();
			replyCnt.setFont(Font.font("-윤고딕350", 14));
			replyCnt.setTextFill(Color.valueOf("#9c0000"));
			replyCnt.setPrefWidth(40);
			replyCnt.setPrefHeight(40);
			replyCnt.setText(size + "개");

			// 댓글창과 버튼을 담는 hbox 테두리
			HBox h_reply = new HBox();
			h_reply.setPrefWidth(770);
			h_reply.setPrefHeight(60);
			h_reply.setStyle("-fx-border-color: #090948");
			h_reply.setStyle("-fx-background-color: #f0f0f0");

			// TextArea
			TextArea input_reply = new TextArea();
			input_reply.setPrefWidth(660);
			input_reply.setPromptText(
					"명예회손, 개인정보 유출, 인격권 침해, 허위사실 유포 등은 이용약관 및 관련법률에 의해 제재를 받을 수 있습니다. 건전한 댓글문화 정착을 위해 이용에 주의를 부탁드립니다.");
			input_reply.setWrapText(true);
			input_reply.setEditable(true);
			// input_reply.setId("input_reply1");

			// 댓글등록버튼
			JFXButton btnReplyInsert = new JFXButton();
			// vbox.setMargin(temp_hbox, new Insets(50, 0, 0, 0));
			HBox.setMargin(btnReplyInsert, new Insets(0, 0, 0, 10));
			btnReplyInsert.setPrefWidth(130);
			btnReplyInsert.setPrefHeight(60);
			btnReplyInsert.setTextFill(Color.valueOf("#fff"));
			btnReplyInsert.setStyle("-fx-background-color: #090948 ;");
			btnReplyInsert.setText("댓글등록");

			btnReplyInsert.setOnAction(e -> {

				Map<String, String> rmap = new HashMap<>();
				String contents = input_reply.getText();
				String mem_id = LoginSession.session.getMem_id();
				rmap.put("albNo", albumNo);
				rmap.put("contents", contents);
				rmap.put("mem_id", mem_id);

				try {
					ias.insertAlbReply(rmap);
					System.out.println("앨범댓글작성성공");
					input_reply.setText("");

					// 화면새로고침
					/*
					 * for (int i = 0; i < 1000; i++) { System.out.println("화면전환"); } for (int i =
					 * 0; i < 2000000000; i++) { } for (int i = 0; i < 2000000000; i++) {
					 * 
					 * }
					 */

					// 화면새로고침

					refesh();

				} catch (RemoteException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			});

			// 세팅

			HboxReply.getChildren().addAll(reply, replyCnt);
			h_reply.getChildren().addAll(input_reply, btnReplyInsert);
			reply_vbox1.getChildren().addAll(HboxReply, h_reply); // 박스고침

		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("댓글생성 for문 시작");

		pageing1(replyMap1);

		///////////////////////////////////////////////////////////////////////////////////////////////
		// 곡 상세 보기 댓글출력부분
		try {
			System.out.println("musNo" + musicNo);
			System.out.println("곡상세보기  곡 번호" + musicNo);
			replyMap2 = FXCollections.observableArrayList(ims.selectMusReply(musicNo));
			System.out.println("리플사이즈:" + replyMap2.size());
			int size = replyMap2.size(); // ?

			// 댓글 창 생성
			HBox HboxReply = new HBox();
			HboxReply.setPrefWidth(100);
			HboxReply.setPrefHeight(20);
			HboxReply.setPadding(new Insets(0, 0, 10, 0));

			// 댓글 라벨생성
			Label reply = new Label();
			reply.setFont(Font.font("-윤고딕350", 14));
			reply.setTextFill(Color.valueOf("#000"));
			reply.setPrefWidth(40);
			reply.setPrefHeight(40);
			reply.setText("댓글");

			// 갯수라벨 생성
			Label replyCnt = new Label();
			replyCnt.setFont(Font.font("-윤고딕350", 14));
			replyCnt.setTextFill(Color.valueOf("#9c0000"));
			replyCnt.setPrefWidth(40);
			replyCnt.setPrefHeight(40);
			replyCnt.setText(size + "개");

			// 댓글창과 버튼을 담는 hbox 테두리
			HBox h_reply = new HBox();
			h_reply.setPrefWidth(770);
			h_reply.setPrefHeight(60);
			h_reply.setStyle("-fx-border-color: #090948");
			h_reply.setStyle("-fx-background-color: #f0f0f0");

			// TextArea
			TextArea input_reply = new TextArea();
			input_reply.setPrefWidth(660);
			input_reply.setPromptText(
					"명예회손, 개인정보 유출, 인격권 침해, 허위사실 유포 등은 이용약관 및 관련법률에 의해 제재를 받을 수 있습니다. 건전한 댓글문화 정착을 위해 이용에 주의를 부탁드립니다.");
			input_reply.setWrapText(true);
			input_reply.setEditable(true);
			// input_reply.setId("input_reply1");

			// 댓글등록버튼
			JFXButton btnReplyInsert = new JFXButton();
			// vbox.setMargin(temp_hbox, new Insets(50, 0, 0, 0));
			HBox.setMargin(btnReplyInsert, new Insets(0, 0, 0, 10));
			btnReplyInsert.setPrefWidth(130);
			btnReplyInsert.setPrefHeight(60);
			btnReplyInsert.setTextFill(Color.valueOf("#fff"));
			btnReplyInsert.setStyle("-fx-background-color: #090948 ;");
			btnReplyInsert.setText("댓글등록");

			btnReplyInsert.setOnAction(e -> {

				Map<String, String> rmap = new HashMap<>();
				String contents = input_reply.getText();
				String mem_id = LoginSession.session.getMem_id();
				rmap.put("musNo", musicNo);
				rmap.put("contents", contents);
				rmap.put("mem_id", mem_id);

				try {
					ims.insertMusReply(rmap);
					System.out.println("앨범댓글작성성공");
					input_reply.setText("");

					// 화면새로고침
					/*
					 * for (int i = 0; i < 1000; i++) { System.out.println("화면전환"); } for (int i =
					 * 0; i < 2000000000; i++) { } for (int i = 0; i < 2000000000; i++) {
					 * 
					 * }
					 */

					// 화면새로고침

					refesh();

				} catch (RemoteException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			});

			// 세팅

			HboxReply.getChildren().addAll(reply, replyCnt);
			h_reply.getChildren().addAll(input_reply, btnReplyInsert);
			reply_vbox2.getChildren().addAll(HboxReply, h_reply); // 박스고침

		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("댓글생성 for문 시작");

		pageing2(replyMap2);

	}

	@FXML
	public void wideView() {
		// img_wideimg
		System.out.println("크게보기 버튼클릭");
		try {
			AnchorPane pane = FXMLLoader.load(getClass().getResource("../../album/album/AlbummgWiderDialog.fxml"));
			Stage stage = new Stage(StageStyle.UTILITY);
			Scene scene = new Scene(pane);
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			Stage primaryStage = (Stage) label_albumName1.getScene().getWindow();
			stage.initOwner(primaryStage);
			stage.setResizable(false);

			ImageView img_wideimg = (ImageView) pane.lookup("#img_wideimg");
			Image temp_img = new Image(temp_img_path);
			img_wideimg.setImage(temp_img);

			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 메인 재생 버튼 이벤트
	@FXML
	public void btnMainPlay() {
		if (LoginSession.session == null) {
			return;
		}

		ArrayList<String> list = musicCheckList();
		playListInsert(list, true);
		if (!MusicMainController.musicplayer.isShowing()) {
			try {
				MusicMainController.playerLoad = new FXMLLoader(
						getClass().getResource("../../musicplayer/MusicPlayer.fxml"));
				AnchorPane root = MusicMainController.playerLoad.load();
				Scene scene = new Scene(root);
				MusicMainController.musicplayer.setTitle("MusicPlayer");
				MusicMainController.musicplayer.setScene(scene);
				MusicMainController.musicplayer.show();
				mpc = MusicMainController.playerLoad.getController();
				mpc.reFresh();
				mpc.selectIndex();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		cb_main.setSelected(false);
		mainCheck();
	}

	// 페이징 처리 앨범 2
	private void pageing2(ObservableList<Map<String, String>> list) {

		if (list.size() == 0)
			return;

		int totalPage = (list.size() / itemsForPage) + (list.size() % itemsForPage > 0 ? 1 : 0);

		p_page2 = new Pagination(totalPage, 0);
		p_page2.setPageFactory(new Callback<Integer, Node>() {
			@Override
			public Node call(Integer pageIndex) {
				return createPage2(pageIndex, list, itemsForPage);
			}
		});

		reply_vbox2.getChildren().addAll(p_page2);
	}

	// 페이징 처리 앨범 1
	private void pageing1(ObservableList<Map<String, String>> list) {

		if (list.size() == 0)
			return;

		int totalPage = (list.size() / itemsForPage) + (list.size() % itemsForPage > 0 ? 1 : 0);

		p_page1 = new Pagination(totalPage, 0);
		p_page1.setPageFactory(new Callback<Integer, Node>() {
			@Override
			public Node call(Integer pageIndex) {
				return createPage1(pageIndex, list, itemsForPage);
			}
		});

		reply_vbox1.getChildren().addAll(p_page1);
	}

	// 페이징 처리 앨범 1

	public VBox createPage1(int pageIndex, ObservableList<Map<String, String>> list, int itemsForPage) {
		int page = pageIndex * itemsForPage;
		return pagenation1(list, itemsForPage, page);
	}

	// 댓글을 그려주는 부분
	public VBox pagenation1(ObservableList<Map<String, String>> list, int itemsForPage, int page) {
		// 임시로 담아주는 객체
		VBox temp_vbox = new VBox();

		int size = Math.min(page + itemsForPage, list.size());
		for (int i = page; i < size; i++) {
			HBox hbox = new HBox();
			hbox.setPrefWidth(731);
			hbox.setPrefHeight(73);
			reply_vbox1.setMargin(hbox, new Insets(20, 0, 0, 0));
			System.out.println(i + "번 째 for문");

			ImageView imgView = new ImageView();
			Image image = new Image(replyMap1.get(i).get("MEM_IMAGE").toString());
			imgView.setImage(image);
			imgView.setFitWidth(40);
			imgView.setFitHeight(40);
			imgView.setId(replyMap1.get(i).get("ALB_RE_NO").toString());
			hbox.setMargin(imgView, new Insets(0, 15, 0, 0));
			// 이미지 클릭했을 때
			imgView.setOnMouseClicked(e -> {
				ImageView temp_imgView = (ImageView) e.getSource();
				for (int j = 0; j < replyMap1.size(); j++) {
					if (temp_imgView.getId().equals(replyMap1.get(j).get("ALB_RE_NO").toString())) {

						String id = replyMap1.get(j).get("MEM_ID").toString();
						System.out.println("화면전환");

						OtherMypageController.othermemid = id;
						FXMLLoader loader = new FXMLLoader(
								getClass().getResource("../../member/mypage/otherMember.fxml"));// initialize실행됨
						Parent otherMember;
						try {
							otherMember = loader.load();
							main.getChildren().removeAll();
							main.getChildren().setAll(otherMember);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				}
			});

			VBox vbox = new VBox();
			vbox.setPrefWidth(653);
			vbox.setPrefHeight(73);
			System.out.println(i + "번 째 for문에 v박스");

			HBox small_hbox = new HBox();
			small_hbox.setPrefWidth(533);
			small_hbox.setPrefHeight(30);

			Label label_id = new Label();
			label_id.setPrefWidth(40);
			label_id.setPrefHeight(15);
			label_id.setText(replyMap1.get(i).get("MEM_ID").toString());
			System.out.println(i + "번 째 for문에 lable박스 멤버아이디" + replyMap1.get(i).get("MEM_ID").toString());

			Label label_date = new Label();
			label_id.setPrefWidth(75);
			label_id.setPrefHeight(15);
			label_date.setText(replyMap1.get(i).get("ALB_RE_INDATE").toString());
			System.out.println(i + "번 째 for문에 lable박스 날짜" + replyMap1.get(i).get("ALB_RE_INDATE").toString());

			JFXButton btn_report = new JFXButton();
			btn_report.setPrefWidth(40);
			btn_report.setPrefHeight(15);
			btn_report.setText("신고");
			btn_report.setId(replyMap1.get(i).get("ALB_RE_NO").toString());
			btn_report.setTextFill(Color.valueOf("#fff"));
			btn_report.setStyle("-fx-background-color: #9c0000;");
			small_hbox.setMargin(btn_report, new Insets(0, 0, 0, 5));
			System.out.println(i + "번 째 for문에 버튼");

			// 신고버튼 클릭했을 때
			btn_report.setOnMouseClicked(e -> {
				JFXButton temp_btn_report = (JFXButton) e.getSource();
				for (int j = 0; j < replyMap1.size(); j++) {
					if (temp_btn_report.getId().equals(replyMap1.get(j).get("ALB_RE_NO").toString())) {

						System.out.println("아이디값" + replyMap1.get(j).get("MEM_ID").toString());
						String id = replyMap1.get(j).get("MEM_ID").toString();
						System.out.println("alert 창");

						int check = alertConfrimDelete(id);
						// No
						if (check == -1) {
							return;
						}

						// Yes
						try {
							iss.insertBlackCnt(id);
						} catch (RemoteException e1) {
						}

					}
				}
			});

			// 삭제버튼 생성
			JFXButton btn_delete = new JFXButton();
			btn_delete.setPrefWidth(40);
			btn_delete.setPrefHeight(15);
			btn_delete.setText("삭제");
			btn_delete.setId(replyMap1.get(i).get("ALB_RE_NO").toString());
			btn_delete.setTextFill(Color.valueOf("#fff"));
			btn_delete.setStyle("-fx-background-color: #9c0000;");

			btn_delete.setId(replyMap1.get(i).get("ALB_RE_NO").toString());
			small_hbox.setMargin(btn_delete, new Insets(0, 0, 0, 5));

			btn_delete.setVisible(false); // 기본적으로 안보이게 설정

			// 관리자 이거나 리플의 작성자와 세션의 값이 같으면 버튼을 보여준다.
			if (LoginSession.session.getMem_auth().equals("t")
					|| LoginSession.session.getMem_id().equals(replyMap1.get(i).get("MEM_ID").toString())) {
				btn_delete.setVisible(true);
			}

			// 댓글삭제로직
			btn_delete.setOnMouseClicked(e -> {
				System.out.println("댓글삭제버튼클릭");
				JFXButton temp_btn_delete = (JFXButton) e.getSource();
				for (int j = 0; j < replyMap1.size(); j++) {
					if (temp_btn_delete.getId().equals(replyMap1.get(j).get("ALB_RE_NO").toString())) {

						String id = replyMap1.get(j).get("ALB_RE_NO").toString();

						int check = alertConfrimDelete();
						// No
						if (check == -1) {
							return;
						}

						// Yes
						try {
							ias.deleteAlbReply(id);
							System.out.println("댓글삭제 성공");

							refesh();
						} catch (RemoteException e1) {
							e1.printStackTrace();
							System.out.println("댓글삭제 실패");
						}

					}
				}
			});

			Label label_contents = new Label();
			label_contents.setPrefWidth(598);
			label_contents.setPrefHeight(43);
			label_contents.setText(replyMap1.get(i).get("ALB_RE_CONTENT").toString());
			System.out.println(i + "번 째 for문에 label_contents" + replyMap1.get(i).get("ALB_RE_CONTENT").toString());

			HBox h_Line = new HBox();
			// vbox.setMargin(h_Line, new Insets(0,0,0,0));
			h_Line.setPrefWidth(710);
			h_Line.setPrefHeight(1);
			h_Line.setStyle("-fx-background-color:#090948;");
			System.out.println("h_Line");

			small_hbox.getChildren().addAll(label_id, label_date, btn_report, btn_delete);
			vbox.getChildren().addAll(small_hbox, label_contents);
			hbox.getChildren().addAll(imgView, vbox);
			temp_vbox.getChildren().addAll(hbox, h_Line);
		}

		return temp_vbox;

	}

	// 페이징 처리 앨범 2

	public VBox createPage2(int pageIndex, ObservableList<Map<String, String>> list, int itemsForPage) {
		int page = pageIndex * itemsForPage;
		return pagenation2(list, itemsForPage, page);
	}

	// 곡 상세 보기 댓글을 그려주는 부분
	public VBox pagenation2(ObservableList<Map<String, String>> list, int itemsForPage, int page) {
		// 임시로 담아주는 객체
		VBox temp_vbox = new VBox();

		int size = Math.min(page + itemsForPage, list.size());
		for (int i = page; i < size; i++) {
			HBox hbox = new HBox();
			hbox.setPrefWidth(731);
			hbox.setPrefHeight(73);
			reply_vbox2.setMargin(hbox, new Insets(20, 0, 0, 0));
			System.out.println(i + "번 째 for문");

			ImageView imgView = new ImageView();
			Image image = new Image(replyMap2.get(i).get("MEM_IMAGE").toString());
			imgView.setImage(image);
			imgView.setFitWidth(40);
			imgView.setFitHeight(40);
			imgView.setId(replyMap2.get(i).get("MUS_RE_NO").toString());
			hbox.setMargin(imgView, new Insets(0, 15, 0, 0));
			// 이미지 클릭했을 때
			imgView.setOnMouseClicked(e -> {
				ImageView temp_imgView = (ImageView) e.getSource();
				for (int j = 0; j < replyMap2.size(); j++) {
					if (temp_imgView.getId().equals(replyMap2.get(j).get("MUS_RE_NO").toString())) {

						String id = replyMap2.get(j).get("MEM_ID").toString();
						System.out.println("화면전환");

						OtherMypageController.othermemid = id;
						FXMLLoader loader = new FXMLLoader(
								getClass().getResource("../../member/mypage/otherMember.fxml"));// initialize실행됨
						Parent otherMember;
						try {
							otherMember = loader.load();
							main.getChildren().removeAll();
							main.getChildren().setAll(otherMember);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				}
			});

			VBox vbox = new VBox();
			vbox.setPrefWidth(653);
			vbox.setPrefHeight(73);
			System.out.println(i + "번 째 for문에 v박스");

			HBox small_hbox = new HBox();
			small_hbox.setPrefWidth(533);
			small_hbox.setPrefHeight(30);

			Label label_id = new Label();
			label_id.setPrefWidth(40);
			label_id.setPrefHeight(15);
			label_id.setText(replyMap2.get(i).get("MEM_ID").toString());
			System.out.println(i + "번 째 for문에 lable박스 멤버아이디" + replyMap2.get(i).get("MEM_ID").toString());

			Label label_date = new Label();
			label_id.setPrefWidth(75);
			label_id.setPrefHeight(15);
			label_date.setText(replyMap2.get(i).get("MUS_RE_INDATE").toString());
			System.out.println(i + "번 째 for문에 lable박스 날짜" + replyMap2.get(i).get("MUS_RE_INDATE").toString());

			JFXButton btn_report = new JFXButton();
			btn_report.setPrefWidth(40);
			btn_report.setPrefHeight(15);
			btn_report.setText("신고");
			btn_report.setId(replyMap2.get(i).get("MUS_RE_NO").toString());
			btn_report.setTextFill(Color.valueOf("#fff"));
			btn_report.setStyle("-fx-background-color: #9c0000;");
			small_hbox.setMargin(btn_report, new Insets(0, 0, 0, 5));
			System.out.println(i + "번 째 for문에 버튼");

			// 신고버튼 클릭했을 때
			btn_report.setOnMouseClicked(e -> {
				JFXButton temp_btn_report = (JFXButton) e.getSource();
				for (int j = 0; j < replyMap2.size(); j++) {
					if (temp_btn_report.getId().equals(replyMap2.get(j).get("MUS_RE_NO").toString())) {

						System.out.println("아이디값" + replyMap2.get(j).get("MEM_ID").toString());
						String id = replyMap2.get(j).get("MEM_ID").toString();
						System.out.println("alert 창");

						int check = alertConfrimDelete(id);
						// No
						if (check == -1) {
							return;
						}

						// Yes
						try {
							iss.insertBlackCnt(id);
						} catch (RemoteException e1) {
						}

					}
				}
			});

			// 삭제버튼 생성
			JFXButton btn_delete = new JFXButton();
			btn_delete.setPrefWidth(40);
			btn_delete.setPrefHeight(15);
			btn_delete.setText("삭제");
			btn_delete.setId(replyMap2.get(i).get("MUS_RE_NO").toString());
			btn_delete.setTextFill(Color.valueOf("#fff"));
			btn_delete.setStyle("-fx-background-color: #9c0000;");

			btn_delete.setId(replyMap2.get(i).get("MUS_RE_NO").toString());
			small_hbox.setMargin(btn_delete, new Insets(0, 0, 0, 5));

			btn_delete.setVisible(false); // 기본적으로 안보이게 설정

			// 관리자 이거나 리플의 작성자와 세션의 값이 같으면 버튼을 보여준다.
			if (LoginSession.session.getMem_auth().equals("t")
					|| LoginSession.session.getMem_id().equals(replyMap2.get(i).get("MEM_ID").toString())) {
				btn_delete.setVisible(true);
			}

			// 댓글삭제로직
			btn_delete.setOnMouseClicked(e -> {
				System.out.println("댓글삭제버튼클릭");
				JFXButton temp_btn_delete = (JFXButton) e.getSource();
				for (int j = 0; j < replyMap2.size(); j++) {
					if (temp_btn_delete.getId().equals(replyMap2.get(j).get("MUS_RE_NO").toString())) {

						String id = replyMap2.get(j).get("MUS_RE_NO").toString();

						int check = alertConfrimDelete();
						// No
						if (check == -1) {
							return;
						}

						// Yes
						try {
							ims.deleteMusReply(id);
							System.out.println("댓글삭제 성공");

							refesh();
						} catch (RemoteException e1) {
							e1.printStackTrace();
							System.out.println("댓글삭제 실패");
						}

					}
				}
			});

			Label label_contents = new Label();
			label_contents.setPrefWidth(598);
			label_contents.setPrefHeight(43);
			label_contents.setText(replyMap2.get(i).get("MUS_RE_CONTENT").toString());
			System.out.println(i + "번 째 for문에 label_contents" + replyMap2.get(i).get("MUS_RE_CONTENT").toString());

			HBox h_Line = new HBox();
			// vbox.setMargin(h_Line, new Insets(0,0,0,0));
			h_Line.setPrefWidth(710);
			h_Line.setPrefHeight(1);
			h_Line.setStyle("-fx-background-color:#090948;");
			System.out.println("h_Line");

			small_hbox.getChildren().addAll(label_id, label_date, btn_report, btn_delete);
			vbox.getChildren().addAll(small_hbox, label_contents);
			hbox.getChildren().addAll(imgView, vbox);
			temp_vbox.getChildren().addAll(hbox, h_Line);
		}

		return temp_vbox;

	}

	// 메인 추가 버튼 이벤트
	@FXML
	public void btnMainAdd() {
		if (LoginSession.session == null) {
			return;
		}

		ArrayList<String> list = musicCheckList();
		playListInsert(list, false);
		cb_main.setSelected(false);
		mainCheck();
	}

	// 메인 담기 버튼 이벤트
	@FXML
	public void btnMainPut() {
		if (LoginSession.session == null) {
			return;
		}

		ArrayList<String> list = musicCheckList();
		MyAlbumDialogController.mus_no.clear();
		MyAlbumDialogController.mus_no = list;
		musicList.myAlbumdialog();
		cb_main.setSelected(false);
		mainCheck();
	}

	// 삭제 알림창
	public int alertConfrimDelete() {
		Alert alertConfirm = new Alert(AlertType.CONFIRMATION);

		alertConfirm.setTitle("CONFIRMATION");
		alertConfirm.setContentText("댓글을  삭제하시겠습니까?");

		// Alert창을 보여주고 사용자가 누른 버튼 값 읽어오기
		ButtonType confirmResult = alertConfirm.showAndWait().get();

		if (confirmResult == ButtonType.OK) {
			System.out.println("OK 버튼을 눌렀습니다.");
			return 1;
		} else if (confirmResult == ButtonType.CANCEL) {
			System.out.println("취소 버튼을 눌렀습니다.");
			return -1;
		}
		return -1;
	}

	// 경고 알림창
	public int alertConfrimDelete(String id) {
		Alert alertConfirm = new Alert(AlertType.CONFIRMATION);

		alertConfirm.setTitle("CONFIRMATION");
		alertConfirm.setContentText(id + "님을 신고하시겠습니까?");

		// Alert창을 보여주고 사용자가 누른 버튼 값 읽어오기
		ButtonType confirmResult = alertConfirm.showAndWait().get();

		if (confirmResult == ButtonType.OK) {
			System.out.println("OK 버튼을 눌렀습니다.");
			return 1;
		} else if (confirmResult == ButtonType.CANCEL) {
			System.out.println("취소 버튼을 눌렀습니다.");
			return -1;
		}
		return -1;
	}

	// 화면을 새로고침하는 메서드
	private void refesh() {
		try {
			System.out.println("화면 새로고침");
			System.out.println("화면 새로고침");
			FXMLLoader loader = new FXMLLoader(getClass().getResource("SingerMenu.fxml"));
			Parent singerMenu;
			singerMenu = loader.load();
			SingerMenuController cotroller = loader.getController();
			cotroller.setcontroller(main);
			main.getChildren().removeAll();
			main.getChildren().setAll(singerMenu);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 전체 선택 및 해제 메서드
	@FXML
	public void mainCheck() {
		if (cb_main.isSelected()) {
			for (int i = 0; i < cbnList.size(); i++) {
				cbnList.get(i).setSelected(true);
			}
		} else {
			for (int i = 0; i < cbnList.size(); i++) {
				cbnList.get(i).setSelected(false);
			}
		}
	}

	// 체크 박스 선택한 곡넘버 보내기
	private ArrayList<String> musicCheckList() {
		ArrayList<String> list = new ArrayList<>();
		for (int i = 0; i < cbnList.size(); i++) {
			if (cbnList.get(i).isSelected()) {
				list.add(cbnList.get(i).getId());
			}
		}
		return list;
	}

	// 가요장르
	@FXML
	public void songChart() {
		System.out.println("앨범번호로찾기");
		try { // 앨범 번호로 찾기
			songRank = FXCollections.observableArrayList(ims.selectAlbum(SingerMenuController.albumNo));
			cb_main.setSelected(false);
			lb_total.setText("수록곡");

			pageing(songRank);

			// 아티스트 소개 y좌표 설정
			line_intro.setLayoutY(621 + (songRank.size() - 1) * 73);
			lb_intro.setLayoutY(626 + (songRank.size() - 1) * 73);
			txt_intro.setLayoutY(671 + (songRank.size() - 1) * 73);
			reply_vbox1.setLayoutY(976 + (songRank.size() - 1) * 73);

		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	public VBox createPage(int pageIndex, ObservableList<Map> list, int itemsForPage) {
		int page = pageIndex * itemsForPage;
		return musicList.pagenation(list, itemsForPage, page);
	}

	private void playListInsert(ArrayList<String> list, boolean play) {
		for (int i = 0; i < list.size(); i++) {
			PlayListVO vo = new PlayListVO();
			vo.setMus_no(list.get(i));
			vo.setMem_id(LoginSession.session.getMem_id());
			try {
				ipls.playlistInsert(vo);
			} catch (RemoteException e) {
				e.printStackTrace();
			}

			if (MusicMainController.musicplayer.isShowing()) {
				mpc = MusicMainController.playerLoad.getController();
				mpc.reFresh();
				if (play) {
					mpc.selectIndex();
				}
			}
		}
	}

	private void pageing(ObservableList<Map> list) {

		if (mainBox.getChildren().size() == 5) {
			mainBox.getChildren().remove(4);
		}

		if (list.size() == 0)
			return;
		int totalPage = (list.size() / itemsForPage) + (list.size() % itemsForPage > 0 ? 1 : 0);

		p_page = new Pagination(totalPage, 0);
		p_page.setPageFactory(new Callback<Integer, Node>() {
			@Override
			public Node call(Integer pageIndex) {
				return createPage(pageIndex, list, itemsForPage);
			}
		});

		mainBox.getChildren().addAll(p_page);
	}

	public void btn_heart() throws RemoteException {
		if (yn > 0) // 좋아요 취소일 때
		{
			// 취소 메서드
			System.out.println("취소메서드 클릭");
			ias.deleteAlbLike(pMap);
			resetCnt();
			icon_heart.setIconName("HEART_ALT");

		} else {
			// 추가 메서드
			System.out.println(yn);
			System.out.println("추가메서드 클릭");
			ias.insertAlbLike(pMap);
			resetCnt();
			icon_heart.setIconName("HEART");
		}

	}

	private void resetCnt() throws RemoteException {
		// 좋아요 카운트 쿼리

		int likeCnt = ias.selectAlbumLikeCnt(albumNo);
		System.out.println("likecnt :" + likeCnt);
		label_LikeCnt.setText(likeCnt + "");

		yn = ias.checkHeartYN(pMap);
	}

	private void resetCnt2() throws RemoteException {
		// 좋아요 카운트 쿼리

		int likeCnt = ims.selectMusicLikeCnt(musicNo);
		System.out.println("likecnt  곡 좋아요 카운트 갯수1 :" + likeCnt);
		String str_likeCnt = likeCnt + "";
		label_LikeCnt2.setText(str_likeCnt);

		yn2 = ims.checkHeartYN(pMap_mus);
	}

	// 곡 상세용
	@FXML
	public void btn_heart2() {
		if (yn2 > 0) // 좋아요 취소일 때
		{
			// 취소 메서드
			try {
				System.out.println("취소메서드 클릭");
				ims.deleteMusicLike(pMap_mus);
				resetCnt2();
				icon_heart2.setIconName("HEART_ALT");
			} catch (RemoteException e) {
				e.printStackTrace();
			}

		} else {
			// 추가 메서드
			System.out.println(yn2);
			System.out.println("추가메서드 클릭");
			try {
				ims.insertMusicLike(pMap_mus);
				resetCnt2();
				icon_heart2.setIconName("HEART");
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}

	}
}
