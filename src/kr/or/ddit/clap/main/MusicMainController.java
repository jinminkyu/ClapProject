package kr.or.ddit.clap.main;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Pagination;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TabPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import kr.or.ddit.clap.service.album.IAlbumService;
import kr.or.ddit.clap.service.login.ILoginService;
import kr.or.ddit.clap.service.message.IMessageService;
import kr.or.ddit.clap.service.music.IMusicService;
import kr.or.ddit.clap.service.musichistory.IMusicHistoryService;
import kr.or.ddit.clap.service.playlist.IPlayListService;
import kr.or.ddit.clap.service.recommend.IRecommendService;
import kr.or.ddit.clap.view.chartmenu.dialog.MyAlbumDialogController;
import kr.or.ddit.clap.view.chartmenu.main.ChartMenuController;
import kr.or.ddit.clap.view.chartmenu.musiclist.MusicList;
import kr.or.ddit.clap.view.genremusic.main.GenreMusicMenuController;
import kr.or.ddit.clap.view.message.ShowMessageController;
import kr.or.ddit.clap.view.musicplayer.MusicPlayerController;
import kr.or.ddit.clap.view.newmusic.main.NewMusicMenuController;
import kr.or.ddit.clap.view.recommend.album.UserRcmDetailController;
import kr.or.ddit.clap.view.singer.main.NotfoundController;
import kr.or.ddit.clap.view.singer.main.SingerMainController;
import kr.or.ddit.clap.view.singer.main.SingerMenuController;
import kr.or.ddit.clap.view.singer.main.UnifiedSearchController;
import kr.or.ddit.clap.view.singer.singer.ShowSingerDetailController;
import kr.or.ddit.clap.vo.album.AlbumVO;
import kr.or.ddit.clap.vo.member.MemberVO;
import kr.or.ddit.clap.vo.music.PlayListVO;
import kr.or.ddit.clap.vo.recommend.RecommendAlbumVO;
import kr.or.ddit.clap.vo.search.NewSearchWordVO;
import kr.or.ddit.clap.vo.support.MessageVO;

/**
 * 메인화면의 fxml 컨트롤러.
 * 
 * @author Kyunghun
 *
 */
public class MusicMainController implements Initializable {

	@FXML
	ScrollPane all;
	@FXML
	AnchorPane menu;
	@FXML
	AnchorPane contents;
	@FXML
	HBox mem_menu;
	@FXML
	MenuBar bar;
	@FXML
	TabPane tabpane;
	private static boolean thread_flag;
	static int current_index;

	private ObservableList<NewSearchWordVO> bestSearchList;

	@FXML
	JFXTreeTableView<NewSearchWordVO> tbl_search;

	@FXML
	TreeTableColumn<NewSearchWordVO, String> col_word;

	@FXML
	public JFXButton btn_login, btn_rec1, btn_rec2, btn_rec3, btn_rec4, btn_rec5; // 추천 앨범의 버튼

	@FXML
	public JFXButton btn_goforward;
	@FXML
	public JFXButton btn_goback;
	@FXML
	JFXTextField txt_search;

	@FXML
	public JFXButton btn_logout;
	@FXML
	public JFXButton btn_join;
	@FXML
	public JFXButton btn_mem;
	@FXML
	public JFXButton btn_player;

	@FXML
	VBox vbox, editorBox1, editorBox2, editorBox3, editorBox4, editorBox5;
	@FXML
	HBox chart_box;
	@FXML
	Menu menu_admin;
	@FXML
	Label lb_id, lbb1, lbb2, lbb3, lbb4, lbb5, lbb6, lbb7, lbb8, lbb9, lbb10; // 추천음악 제목 라벨
	@FXML
	Label lb_bottom1, lb_bottom2, lb_bottom3, lb_bottom4, lb_bottom5, lb_bottom6, lb_bottom7, lb_bottom8, lb_bottom9,
			lb_bottom10, lb_bottom11, lb_bottom12, lb_bottom13; // 맨아래쪽 회사 라벨
	@FXML
	Label lbbb1, lbbb2, lbbb3, lbbb4, lbbb5, lbbb6, lbbb7, lbbb8, lbbb9, lbbb10; // 추천음악 좋아요/곡수 라벨
	@FXML
	Label la_messageCnt, lb_chart, lb_editor;
	@FXML
	ImageView mem_img;
	@FXML
	ImageView new1, new2, new3, new4, new5;
	@FXML
	ImageView new6, new7, new8, new9, new10;
	@FXML
	ImageView tabImg1, tabImg2, tabImg3, tabImg4, tabImg5;
	@FXML
	ImageView edit1, edit2, edit3, edit4, edit5, logo_imgView;
	@FXML
	JFXButton btn_new1, btn_new2, btn_new3, btn_new4, btn_new5;
	@FXML
	JFXButton btn_new6, btn_new7, btn_new8, btn_new9, btn_new10;
	@FXML
	AnchorPane tab_pane1, tab_pane2, tab_pane3, tab_pane4, tab_pane5, achorpane_game;
	@FXML
	JFXButton btn_msg;
	LoginSession ls = new LoginSession();
	public static Stage musicplayer = new Stage();
	private ILoginService ils;
	private IAlbumService ias;
	private IMusicHistoryService imhs;
	private IMessageService imsgs;
	public static FXMLLoader playerLoad;
	public static Stage movieStage = new Stage();
	public static AnchorPane secondPane;

	List<AlbumVO> albumList = new ArrayList<>();
	List<AlbumVO> newList = new ArrayList<>();

	@FXML
	StackPane mem_pane;
	@FXML
	FontAwesomeIcon icon_msg;
	@FXML
	VBox mainBox;
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

	// 검색창
	@FXML
	AnchorPane pane_search; // 전체페인

	@FXML
	JFXButton btn_bestWord;

	@FXML
	JFXButton btn_newWord;
	@FXML
	AnchorPane footer;
	@FXML
	ImageView image_footer;
	@FXML
	ImageView image_logo;
	@FXML
	FontAwesomeIcon icon1, icon2, icon3, icon4, icon5;

	private Registry reg;
	private IMusicService ims;
	private IPlayListService ipls;
	private IRecommendService irs;
	private MusicList musicList;
	private ObservableList<Map> songRank;
	private ObservableList<Map> popRank;
	private ObservableList<Map> ostRank;
	private ObservableList<Map> otherRank;
	private ObservableList<JFXCheckBox> cbnList = FXCollections.observableArrayList();
	private ObservableList<JFXButton> btnPlayList = FXCollections.observableArrayList();
	private ObservableList<JFXButton> btnAddList = FXCollections.observableArrayList();
	private ObservableList<JFXButton> btnPutList = FXCollections.observableArrayList();
	private ObservableList<JFXButton> btnMovieList = FXCollections.observableArrayList();
	private MusicPlayerController mpc;
	private int itemsForPage;
	private Pagination p_page;

	private ObservableList<RecommendAlbumVO> recommendList;
	static Thread[] arr_thread = new Thread[1];

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		if (arr_thread[0] != null) {
			arr_thread[0].interrupt();
		}
		tabpane.getSelectionModel().select(0);
		
		Thread1 thread = new Thread1();
		thread.setDaemon(true);
		thread.start();
		arr_thread[0] = thread;

		try {
			reg = LocateRegistry.getRegistry("localhost", 8888);
			ils = (ILoginService) reg.lookup("login");
			ias = (IAlbumService) reg.lookup("album");
			imhs = (IMusicHistoryService) reg.lookup("history");
			imsgs = (IMessageService) reg.lookup("message");

			ims = (IMusicService) reg.lookup("music");
			ipls = (IPlayListService) reg.lookup("playlist");
			itemsForPage = 10;
			game();

			irs = (IRecommendService) reg.lookup("recommend");
			recommendList = FXCollections.observableArrayList(irs.selectAllRecommendAlbum());

			playerLoad = new FXMLLoader(getClass().getResource("../view/musicplayer/MusicPlayer.fxml"));
			secondPane = contents;
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}

		// 추천 앨범 출력하기

		Image[] tab_img = new Image[5];
		tab_img[0] = new Image(recommendList.get(0).getRcm_alb_image());
		tab_img[1] = new Image(recommendList.get(1).getRcm_alb_image());
		tab_img[2] = new Image(recommendList.get(2).getRcm_alb_image());
		tab_img[3] = new Image(recommendList.get(3).getRcm_alb_image());
		tab_img[4] = new Image(recommendList.get(4).getRcm_alb_image());

		tabImg1.setImage(tab_img[0]);
		tabImg2.setImage(tab_img[1]);
		tabImg3.setImage(tab_img[2]);
		tabImg4.setImage(tab_img[3]);
		tabImg5.setImage(tab_img[4]);

		ArrayList<String> titleList = cut_title(recommendList);
		lbb1.setPadding(new Insets(0, 0, 0, 0));
		lbb3.setPadding(new Insets(0, 0, 0, 0));
		lbb5.setPadding(new Insets(0, 0, 0, 0));
		lbb7.setPadding(new Insets(0, 0, 0, 0));
		lbb9.setPadding(new Insets(0, 0, 0, 0));
		if (recommendList.get(0).getRcm_alb_name().length() < 13) {
			lbb1.setPadding(new Insets(40, 0, 0, 0));
		}
		if (recommendList.get(1).getRcm_alb_name().length() < 13) {
			lbb3.setPadding(new Insets(40, 0, 0, 0));
		}
		if (recommendList.get(2).getRcm_alb_name().length() < 13) {
			lbb5.setPadding(new Insets(40, 0, 0, 0));
		}
		if (recommendList.get(3).getRcm_alb_name().length() < 13) {
			lbb7.setPadding(new Insets(40, 0, 0, 0));
		}
		if (recommendList.get(4).getRcm_alb_name().length() < 13) {
			lbb9.setPadding(new Insets(40, 0, 0, 0));
		}
		lbb1.setText(titleList.get(0));
		lbb2.setText(titleList.get(1));
		lbb3.setText(titleList.get(2));
		lbb4.setText(titleList.get(3));
		lbb5.setText(titleList.get(4));

		lbb6.setText(titleList.get(5));
		lbb7.setText(titleList.get(6));
		lbb8.setText(titleList.get(7));
		lbb9.setText(titleList.get(8));
		lbb10.setText(titleList.get(9));

		// 좋아요 카운트 쿼리
		int likeCnt1 = 0;
		int likeCnt3 = 0;
		int likeCnt5 = 0;
		int likeCnt7 = 0;
		int likeCnt9 = 0;
		int cntMusic2 = 0;
		int cntMusic4 = 0;
		int cntMusic6 = 0;
		int cntMusic8 = 0;
		int cntMusic10 = 0;
		try {
			likeCnt1 = irs.selectAlbumLikeCnt(recommendList.get(0).getRcm_alb_no());
			likeCnt3 = irs.selectAlbumLikeCnt(recommendList.get(1).getRcm_alb_no());
			likeCnt5 = irs.selectAlbumLikeCnt(recommendList.get(2).getRcm_alb_no());
			likeCnt7 = irs.selectAlbumLikeCnt(recommendList.get(3).getRcm_alb_no());
			likeCnt9 = irs.selectAlbumLikeCnt(recommendList.get(4).getRcm_alb_no());
			cntMusic2 = FXCollections.observableArrayList(irs.SelectRcmMusicList(recommendList.get(0).getRcm_alb_no()))
					.size();
			cntMusic4 = FXCollections.observableArrayList(irs.SelectRcmMusicList(recommendList.get(1).getRcm_alb_no()))
					.size();
			cntMusic6 = FXCollections.observableArrayList(irs.SelectRcmMusicList(recommendList.get(2).getRcm_alb_no()))
					.size();
			cntMusic8 = FXCollections.observableArrayList(irs.SelectRcmMusicList(recommendList.get(3).getRcm_alb_no()))
					.size();
			cntMusic10 = FXCollections.observableArrayList(irs.SelectRcmMusicList(recommendList.get(4).getRcm_alb_no()))
					.size();

		} catch (RemoteException e3) {
			e3.printStackTrace();
		}
		lbbb1.setText(likeCnt1 + "");
		lbbb3.setText(likeCnt3 + "");
		lbbb5.setText(likeCnt5 + "");
		lbbb7.setText(likeCnt7 + "");
		lbbb9.setText(likeCnt9 + "");
		lbbb2.setText(cntMusic2 + "곡");
		lbbb4.setText(cntMusic4 + "곡");
		lbbb6.setText(cntMusic6 + "곡");
		lbbb8.setText(cntMusic8 + "곡");
		lbbb10.setText(cntMusic10 + "곡");
		
		icon1.setIconName(recommendList.get(0).getRcm_icon());
		icon2.setIconName(recommendList.get(1).getRcm_icon());
		icon3.setIconName(recommendList.get(2).getRcm_icon());
		icon4.setIconName(recommendList.get(3).getRcm_icon());
		icon5.setIconName(recommendList.get(4).getRcm_icon());
		
		tbl_search.setPlaceholder(new Label(" "));

		btn_rec1.setOnAction(e -> {
			UserRcmDetailController.rcmAlbNo = recommendList.get(0).getRcm_alb_no();// 곡 번호를 변수로 넘겨줌
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/recommend/album/UserRcmDetail.fxml"));// init실행됨
			Parent recommendAlbumDetail;
			try {
				recommendAlbumDetail = loader.load();

				UserRcmDetailController cotroller = loader.getController();
				cotroller.givePane(contents);

				contents.getChildren().removeAll();
				contents.getChildren().setAll(recommendAlbumDetail);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});
		btn_rec2.setOnAction(e -> {
			UserRcmDetailController.rcmAlbNo = recommendList.get(1).getRcm_alb_no();// 곡 번호를 변수로 넘겨줌
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/recommend/album/UserRcmDetail.fxml"));// init실행됨
			Parent recommendAlbumDetail;
			try {
				recommendAlbumDetail = loader.load();

				UserRcmDetailController cotroller = loader.getController();
				cotroller.givePane(contents);

				contents.getChildren().removeAll();
				contents.getChildren().setAll(recommendAlbumDetail);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});
		btn_rec3.setOnAction(e -> {
			UserRcmDetailController.rcmAlbNo = recommendList.get(2).getRcm_alb_no();// 곡 번호를 변수로 넘겨줌
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/recommend/album/UserRcmDetail.fxml"));// init실행됨
			Parent recommendAlbumDetail;
			try {
				recommendAlbumDetail = loader.load();

				UserRcmDetailController cotroller = loader.getController();
				cotroller.givePane(contents);

				contents.getChildren().removeAll();
				contents.getChildren().setAll(recommendAlbumDetail);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});
		btn_rec4.setOnAction(e -> {
			UserRcmDetailController.rcmAlbNo = recommendList.get(3).getRcm_alb_no();// 곡 번호를 변수로 넘겨줌
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/recommend/album/UserRcmDetail.fxml"));// init실행됨
			Parent recommendAlbumDetail;
			try {
				recommendAlbumDetail = loader.load();

				UserRcmDetailController cotroller = loader.getController();
				cotroller.givePane(contents);

				contents.getChildren().removeAll();
				contents.getChildren().setAll(recommendAlbumDetail);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});
		btn_rec5.setOnAction(e -> {
			UserRcmDetailController.rcmAlbNo = recommendList.get(4).getRcm_alb_no();// 곡 번호를 변수로 넘겨줌
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/recommend/album/UserRcmDetail.fxml"));// init실행됨
			Parent recommendAlbumDetail;
			try {
				recommendAlbumDetail = loader.load();

				UserRcmDetailController cotroller = loader.getController();
				cotroller.givePane(contents);

				contents.getChildren().removeAll();
				contents.getChildren().setAll(recommendAlbumDetail);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});

		musicList = new MusicList(cbnList, btnPlayList, btnAddList, btnPutList, btnMovieList, mainBox, stackpane);

		// 실시간차트
		songChart();

		if (ls.session != null) {
			MessageVO msgvo = new MessageVO();
			msgvo.setMem_get_id(ls.session.getMem_id());
			msgvo.setMsg_read_tf("f");
			String cnt;
			try {
				cnt = imsgs.selectMessFCnt(msgvo);
				la_messageCnt.setText(cnt); // 갯수넣기
			} catch (RemoteException e2) {
				e2.printStackTrace();
			}
		}
		if (ls.session == null) {
			mem_menu.setVisible(false);
			btn_join.setVisible(true);
			btn_login.setVisible(true);
			btn_msg.setVisible(false);
			btn_logout.setVisible(false);
			mem_pane.setVisible(false);
			icon_msg.setVisible(false);
			la_messageCnt.setVisible(false);

		} else {
			if (ls.session.getMem_auth().equals("t")) { // 관리자 일 때 관리자모드 버튼 활성화
				menu_admin.setVisible(true);
			} else {
				menu_admin.setVisible(false); // 일반사용자일경우( 관리자로 로그인 후 사용자로 로그인 했을 경우를 대비해서만들었음
			}
			mem_menu.setVisible(true);
			btn_join.setVisible(false);
			btn_login.setVisible(false);
			btn_msg.setVisible(true);
			btn_logout.setVisible(true);
			mem_pane.setVisible(true);
			icon_msg.setVisible(true);
			la_messageCnt.setVisible(true);

			lb_id.setText(ls.session.getMem_id() + "님");

			Image img = null;
			if (ls.session.getMem_image() == null) {
				img = new Image("file:\\\\Sem-pc\\공유폴더\\Clap\\img\\userimg\\icons8-person-64.png");

			} else {
				img = new Image(ls.session.getMem_image());
			}
			mem_img.setImage(img);

			// 검색어
			try {

				col_word.setCellValueFactory(
						param -> new SimpleStringProperty(param.getValue().getValue().getNew_word()));
				bestSearchList = FXCollections.observableArrayList(ils.selecthotkeyword());


				TreeItem<NewSearchWordVO> root = new RecursiveTreeItem<>(bestSearchList,
						RecursiveTreeObject::getChildren);
				tbl_search.setRoot(root);
				tbl_search.setShowRoot(false);


			} catch (RemoteException e1) {
				e1.printStackTrace();
			}
			for (int i = 0; i < bestSearchList.size(); i++) {
				tbl_search.setOnMouseEntered(e -> {

				});
			}

		}

		// 최신음악에 앨범 목록에서 등록순으로 출력되도록.
		try {
			albumList = ias.selectListAll();
		} catch (RemoteException e1) {
			e1.printStackTrace();
		}
		// 최신순으로 10개 뽑는 메서드. -> 쿼리 이용하도록 수정.
		// setNewList();

		Image logo_img = new Image("file:\\\\Sem-pc\\공유폴더\\Clap\\img\\new logo170.png");
		Image footer_logo = new Image("file:\\\\Sem-pc\\공유폴더\\Clap\\img\\new logo170.png");
		Image footer = new Image("file:\\\\Sem-pc\\공유폴더\\Clap\\img\\footerImage.png");
		logo_imgView.setImage(logo_img);
		image_logo.setImage(footer_logo);
		image_footer.setImage(footer);

		Image[] images = new Image[albumList.size()];

		for (int i = 0; i < albumList.size(); i++) {
			images[i] = new Image(albumList.get(i).getAlb_image());
		}

		new1.setImage(images[0]);
		new2.setImage(images[1]);
		new3.setImage(images[2]);
		new4.setImage(images[3]);
		new5.setImage(images[4]);
		new6.setImage(images[5]);
		new7.setImage(images[6]);
		new8.setImage(images[7]);
		new9.setImage(images[8]);
		new10.setImage(images[9]);

		// 이미지 hover시 효과 -------------윤한수
		btn_new1.setOnMouseEntered(e -> { // 마우스가 들어갈 때
			new1.setOpacity(0.5);
		});

		btn_new1.setOnMouseExited(e -> {// 마우스가 나갈 때
			new1.setOpacity(1.0);
		});

		btn_new2.setOnMouseEntered(e -> {
			new2.setOpacity(0.5);
		});

		btn_new2.setOnMouseExited(e -> {
			new2.setOpacity(1.0);
		});

		btn_new3.setOnMouseEntered(e -> {
			new3.setOpacity(0.5);
		});

		btn_new3.setOnMouseExited(e -> {
			new3.setOpacity(1.0);
		});

		btn_new4.setOnMouseEntered(e -> {
			new4.setOpacity(0.5);
		});

		btn_new4.setOnMouseExited(e -> {
			new4.setOpacity(1.0);
		});

		btn_new5.setOnMouseEntered(e -> {
			new5.setOpacity(0.5);
		});

		btn_new5.setOnMouseExited(e -> {
			new5.setOpacity(1.0);
		});

		btn_new6.setOnMouseEntered(e -> {
			new6.setOpacity(0.5);
		});

		btn_new6.setOnMouseExited(e -> {
			new6.setOpacity(1.0);
		});

		btn_new7.setOnMouseEntered(e -> {
			new7.setOpacity(0.5);
		});

		btn_new7.setOnMouseExited(e -> {
			new7.setOpacity(1.0);
		});

		btn_new8.setOnMouseEntered(e -> {
			new8.setOpacity(0.5);
		});

		btn_new8.setOnMouseExited(e -> {
			new8.setOpacity(1.0);
		});

		btn_new9.setOnMouseEntered(e -> {
			new9.setOpacity(0.5);
		});

		btn_new9.setOnMouseExited(e -> {
			new9.setOpacity(1.0);
		});

		btn_new10.setOnMouseEntered(e -> {
			new10.setOpacity(0.5);
		});

		btn_new10.setOnMouseExited(e -> {
			new10.setOpacity(1.0);
		});
		/// 이미지 효과 끝
		/////////////////////////

		btn_new1.setOnAction(e -> {
			SingerMenuController.albumNo = albumList.get(0).getAlb_no(); // 앨범번호를 변수로 넘겨줌
			SingerMainController.singerNo = albumList.get(0).getSing_no(); // 가수번호를 변수로 넘겨줌
			SingerMenuController.menuCount = 1;
			singerMenu();
		});
		btn_new2.setOnAction(e -> {
			SingerMenuController.albumNo = albumList.get(1).getAlb_no();
			SingerMainController.singerNo = albumList.get(1).getSing_no();
			SingerMenuController.menuCount = 1;
			singerMenu();
		});
		btn_new3.setOnAction(e -> {
			SingerMenuController.albumNo = albumList.get(2).getAlb_no();
			SingerMainController.singerNo = albumList.get(2).getSing_no();
			SingerMenuController.menuCount = 1;
			singerMenu();
		});
		btn_new4.setOnAction(e -> {
			SingerMenuController.albumNo = albumList.get(3).getAlb_no();
			SingerMainController.singerNo = albumList.get(3).getSing_no();
			SingerMenuController.menuCount = 1;
			singerMenu();
		});
		btn_new5.setOnAction(e -> {
			SingerMenuController.albumNo = albumList.get(4).getAlb_no();
			SingerMainController.singerNo = albumList.get(4).getSing_no();
			SingerMenuController.menuCount = 1;
			singerMenu();
		});

		btn_new6.setOnAction(e -> {
			SingerMenuController.albumNo = albumList.get(5).getAlb_no();
			SingerMainController.singerNo = albumList.get(5).getSing_no();
			SingerMenuController.menuCount = 1;
			singerMenu();
		});
		btn_new7.setOnAction(e -> {
			SingerMenuController.albumNo = albumList.get(6).getAlb_no();
			SingerMainController.singerNo = albumList.get(6).getSing_no();
			SingerMenuController.menuCount = 1;
			singerMenu();
		});
		btn_new8.setOnAction(e -> {
			SingerMenuController.albumNo = albumList.get(7).getAlb_no();
			SingerMainController.singerNo = albumList.get(7).getSing_no();
			SingerMenuController.menuCount = 1;
			singerMenu();
		});
		btn_new9.setOnAction(e -> { // 수정중 - 곡 디테일
			SingerMenuController.albumNo = albumList.get(8).getAlb_no();
			SingerMainController.singerNo = albumList.get(8).getSing_no();
			SingerMenuController.menuCount = 1;
			singerMenu();
		});
		btn_new10.setOnAction(e -> {
			SingerMenuController.albumNo = albumList.get(9).getAlb_no();
			SingerMainController.singerNo = albumList.get(9).getSing_no();
			SingerMenuController.menuCount = 1;
			singerMenu();
		});

		// 검색창 정보 더블클릭
		tbl_search.setOnMouseClicked(e -> {

			// int index = tbl_singer.getSelectionModel().getSelectedIndex();
			String search_word = tbl_search.getSelectionModel().getSelectedItem().getValue().getNew_word();

			txt_search.setText(search_word);

		});

	}

	private ArrayList<String> cut_title(ObservableList<RecommendAlbumVO> list) {
		ArrayList<String> titleList = new ArrayList<>();
		String str1 = "";
		String str2 = "";
		for (int i = 0; i < 5; i++) {
			if (list.get(i).getRcm_alb_name().length() < 13) {
				str1 = list.get(i).getRcm_alb_name();
				str2 = "";
			} else {
				str1 = list.get(i).getRcm_alb_name().substring(0, 12);
				str2 = list.get(i).getRcm_alb_name().substring(12, list.get(i).getRcm_alb_name().length());
			}
			titleList.add(str1);
			titleList.add(str2);
		}
		return titleList;
	}
	
	//검색창 클릭
	public void searchClick() {
		try {
		String word= txt_search.getText();
		String id = LoginSession.session.getMem_id();
		
		Map<String,String> pMap = new HashMap<>();
		pMap.put("id", id);
		pMap.put("word", word);
		
			ils.insertSearchWord(pMap);
			
			String singPK = ils.selectSearchPK(word);
			
			
			if(singPK == null) {
			NotfoundController.word = word;
			Parent notfound = FXMLLoader.load(getClass().getResource("../view/singer/main/Notfound.fxml")); 
				
			String temp_path = (getClass().getResource("../view/singer/main/Notfound.fxml")).getPath();
			String path = temp_path.substring(1, temp_path.length()); // 현재화면 절대경로

			GobackStack.goURL(path);

			contents.getChildren().removeAll();
			contents.getChildren().setAll(notfound);
			return;
				//검색어가 없습니다 화면으로 전환
			}
		
			UnifiedSearchController.singerNo = singPK;//가수번호를 변수로 넘겨줌
		
				Parent unifiedSearch = FXMLLoader.load(getClass().getResource("../view/singer/main/UnifiedSearch.fxml")); // 바뀔
				// 화면을
				// 가져옴

				String temp_path = (getClass().getResource("../view/singer/main/UnifiedSearch.fxml")).getPath();
				String path = temp_path.substring(1, temp_path.length()); // 현재화면 절대경로

				GobackStack.goURL(path);

				contents.getChildren().removeAll();
				contents.getChildren().setAll(unifiedSearch);

			
			
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		
		
	}

	// 검색창에 마우스를 올렸을 경우 발생하는 메서드
	public void searchEnteredMouse() {
		pane_search.setVisible(true);

	}

	// 검색창에 마우스를 치웠을 경우 발생하는 메서드
	public void searchExitedMouse() {
		pane_search.setVisible(false);

	}

	// 검색창에 마우스 올렸을 경우 발생하는 메서드
	public void pane_searchMouseEnterd() {
		pane_search.setVisible(true);
	}

	// 검색창에 마우스 치웠을 경우 발생하는 메서드
	public void pane_searchMouseExited() {
		pane_search.setVisible(false);
	}

	// 인기검색어 버튼을 클릭했을 때
	public void btn_bestWord() {
		try {
		btn_bestWord.setStyle("-fx-background-color:#fff");
		btn_newWord.setStyle("-fx-background-color:#f0f0f0");
		
			bestSearchList = FXCollections.observableArrayList(ils.selecthotkeyword());
			
			TreeItem<NewSearchWordVO> root = new RecursiveTreeItem<>(bestSearchList,
					RecursiveTreeObject::getChildren);
			tbl_search.setRoot(root);
			tbl_search.setShowRoot(false);
			
		} catch (RemoteException e) {
			e.printStackTrace();
		}

	}

	// 최근검색어 버튼을 클릭했을 때
	public void btn_newWord() {
		try {
		btn_bestWord.setStyle("-fx-background-color:#f0f0f0");
		btn_newWord.setStyle("-fx-background-color:#fff");
		
		String id = LoginSession.session.getMem_id();
				
		bestSearchList = FXCollections.observableArrayList( ils.selectHistorykeyword(id));

		TreeItem<NewSearchWordVO> root = new RecursiveTreeItem<>(bestSearchList,
				RecursiveTreeObject::getChildren);
		tbl_search.setRoot(root);
		tbl_search.setShowRoot(false);
		
		} catch (RemoteException e) {
			System.out.println("로그인을 해주세요");
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
		try {
			songRank = FXCollections.observableArrayList(imhs.top10Select());
			cb_main.setSelected(false);

			musicList.musicList(songRank);

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

		if (mainBox.getChildren().size() == 8) {
			mainBox.getChildren().remove(7);
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

	private void setNewList() {
		newList = albumList;

		for (int j = 0; j < 10; j++) {
			int max = 0;
			int idx_max = -1;
			for (int i = j; i < newList.size(); i++) {
				if (max < Integer.valueOf(newList.get(i).getAlb_no())) {
					max = Integer.valueOf(newList.get(i).getAlb_no());
					idx_max = i;
				}
			}
			newList.add(j, newList.get(idx_max));
			newList.remove(idx_max + 1);
		}

	}

	@FXML
	public void goBack(ActionEvent event) throws IOException {

		GobackStack.goBack();
		String path = GobackStack.printPage();
		URL fxmlURL = Paths.get(path).toUri().toURL(); // Stirng 값을 URL로 변환

		Parent goback = FXMLLoader.load(fxmlURL); // 대입


		contents.getChildren().removeAll();
		contents.getChildren().setAll(goback);

	}

	@FXML
	public void goforward(ActionEvent event) throws IOException {
		GobackStack.goForward();
		String path = GobackStack.printPage();
		URL fxmlURL = Paths.get(path).toUri().toURL(); // Stirng 값을 URL로 변환

		Parent goback = FXMLLoader.load(fxmlURL); // 대입

		contents.getChildren().removeAll();
		contents.getChildren().setAll(goback);

	}

	@FXML
	public void login() throws IOException {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("../view/login/Login.fxml"));
			contents.getChildren().removeAll();
			contents.getChildren().setAll(root);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void logout() throws IOException {
		ls.session = null;
		menu_admin.setVisible(false);
		bar.setLayoutX(241);
		firstPage();
		if (musicplayer.isShowing()) {
			MusicPlayerController mpc = MusicMainController.playerLoad.getController();
			if (mpc.player.mediaPlayer != null) {
				mpc.player.stop();
				mpc.player.mediaPlayer = null;
			}
			musicplayer.close();
		}
	}

	@FXML
	public void join() throws IOException {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("../view/join/Join2.fxml"));
			String temp_path = (getClass().getResource("../view/join/Join2.fxml")).getPath();
			String path = temp_path.substring(1, temp_path.length()); // 현재화면 절대경로
			GobackStack.goURL(path);
			contents.getChildren().removeAll();
			contents.getChildren().setAll(root);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void mypage() {
		// 로그인 후 회원정보버튼(btn_mem) 눌렀을때 마이페이지로 이동.

		try {
			Parent root = FXMLLoader.load(getClass().getResource("../view/member/mypage/Mypage.fxml"));
			String temp_path = (getClass().getResource("../view/member/mypage/Mypage.fxml")).getPath();
			String path = temp_path.substring(1, temp_path.length()); // 현재화면 절대경로
			GobackStack.goURL(path);
			contents.getChildren().removeAll();
			contents.getChildren().setAll(root);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void buyTicket() throws IOException {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("../view/ticket/ticket/Ticket.fxml"));
			String temp_path = (getClass().getResource("../view/ticket/ticket/Ticket.fxml")).getPath();
			String path = temp_path.substring(1, temp_path.length()); // 현재화면 절대경로
			GobackStack.goURL(path);
			contents.getChildren().removeAll();
			contents.getChildren().setAll(root);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void qna() { // 고객센터 - 문의사항

		try {
			Parent qna = FXMLLoader.load(getClass().getResource("../view/support/qna/QnaMenuList.fxml"));

			String temp_path = (getClass().getResource("../view/support/qna/QnaMenuList.fxml")).getPath();
			String path = temp_path.substring(1, temp_path.length()); // 현재화면 절대경로

			GobackStack.goURL(path);

			contents.getChildren().removeAll();
			contents.getChildren().setAll(qna);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@FXML
	public void top50PageChange(ActionEvent event) { // 차트메뉴에서 Top50차트 클릭 했을때 페이지 전환 이벤트
		ChartMenuController.menuCount = 0;
		chartMenu_PageLoad();
	}

	@FXML
	public void genrePageChange(ActionEvent event) { // 차트메뉴에서 장르별차트 클릭 했을때 페이지 전환 이벤트
		ChartMenuController.menuCount = 1;
		chartMenu_PageLoad();
	}

	@FXML
	public void periodPageChange(ActionEvent event) { // 차트메뉴에서 시대별차트 클릭 했을때 페이지 전환 이벤트
		ChartMenuController.menuCount = 2;
		chartMenu_PageLoad();
	}

	@FXML
	public void musicPageChange(ActionEvent event) { // 최신음악메뉴에서 곡차트 클릭 했을때 페이지 전환 이벤트
		NewMusicMenuController.menuCount = 0;
		newMusicMenu_PageLoad();
	}

	@FXML
	public void albumPageChange(ActionEvent event) { // 최신은악메뉴에서 앨범차트 클릭 했을때 페이지 전환 이벤트
		NewMusicMenuController.menuCount = 1;
		newMusicMenu_PageLoad();
	}

	@FXML
	public void songPageChange(ActionEvent event) { // 장르음악메뉴에서 가요차트 클릭 했을때 페이지 전환 이벤트
		GenreMusicMenuController.menuCount = 0;
		genreMusicMenu_PageLoad();
	}

	@FXML
	public void popPageChange(ActionEvent event) { // 장르음악메뉴에서 POP차트 클릭 했을때 페이지 전환 이벤트
		GenreMusicMenuController.menuCount = 1;
		genreMusicMenu_PageLoad();
	}

	@FXML
	public void ostPageChange(ActionEvent event) { // 장르음악메뉴에서 OST차트 클릭 했을때 페이지 전환 이벤트
		GenreMusicMenuController.menuCount = 2;
		genreMusicMenu_PageLoad();
	}

	@FXML
	public void otherPageChange(ActionEvent event) { // 장르음악메뉴에서 그 외 장르 차트 클릭 했을때 페이지 전환 이벤트
		GenreMusicMenuController.menuCount = 3;
		genreMusicMenu_PageLoad();
	}

	public void chartMenu_PageLoad() {
		try {
			Parent page = FXMLLoader.load(getClass().getResource("../view/chartmenu/main/ChartMenu.fxml")); // 바뀔 화면을
			String temp_path = (getClass().getResource("../view/chartmenu/main/ChartMenu.fxml")).getPath();
			String path = temp_path.substring(1, temp_path.length()); // 현재화면 절대경로
			GobackStack.goURL(path); // 가져옴
			contents.getChildren().removeAll();
			contents.getChildren().setAll(page);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void newMusicMenu_PageLoad() {

		try {
			Parent page = FXMLLoader.load(getClass().getResource("../view/newmusic/main/NewMusicMenu.fxml")); // 바뀔 화면을
			String temp_path = (getClass().getResource("../view/newmusic/main/NewMusicMenu.fxml")).getPath();
			String path = temp_path.substring(1, temp_path.length()); // 현재화면 절대경로
			GobackStack.goURL(path); // 가져옴
			contents.getChildren().removeAll();
			contents.getChildren().setAll(page);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void genreMusicMenu_PageLoad() {

		try {
			Parent page = FXMLLoader.load(getClass().getResource("../view/genremusic/main/GenreMusicMenu.fxml")); // 바뀔
			String temp_path = (getClass().getResource("../view/genremusic/main/GenreMusicMenu.fxml")).getPath();
			String path = temp_path.substring(1, temp_path.length()); // 현재화면 절대경로
			GobackStack.goURL(path); // 화면을
			contents.getChildren().removeAll();
			contents.getChildren().setAll(page);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void singerMenu() {
		try {
			Parent page = FXMLLoader.load(getClass().getResource("../view/singer/main/SingerMenu.fxml")); // 바뀔 화면을
			String temp_path = (getClass().getResource("../view/singer/main/SingerMenu.fxml")).getPath();
			String path = temp_path.substring(1, temp_path.length()); // 현재화면 절대경로
			GobackStack.goURL(path); // 가져옴
			contents.getChildren().removeAll();
			contents.getChildren().setAll(page);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void firstPage() {
		List<MemberVO> list = new ArrayList<MemberVO>();
		try {
			if (ls.session != null) {
				list = ils.select(ls.session.getMem_id());
				ls.session = list.get(0);
			}
		} catch (RemoteException e1) {
			e1.printStackTrace();
		}

		FXMLLoader loader = new FXMLLoader(getClass().getResource("MusicMain.fxml"));
		ScrollPane root = null;
		try {
			root = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Scene scene = new Scene(root);
		Stage primaryStage = (Stage) btn_login.getScene().getWindow();
		primaryStage.setScene(scene);
	}

	// 추천
	@FXML
	public void hotRcmList(ActionEvent event) {
		try {
			Parent recommendManage = FXMLLoader.load(getClass().getResource("../view/recommend/album/HotRcmList.fxml")); // 바뀔
			// 화면을
			// 가져옴

			String temp_path = (getClass().getResource("../view/recommend/album/HotRcmList.fxml")).getPath();
			String path = temp_path.substring(1, temp_path.length()); // 현재화면 절대경로

			GobackStack.goURL(path);

			contents.getChildren().removeAll();
			contents.getChildren().setAll(recommendManage);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	// 베스트
	@FXML
	public void BestRcmList(ActionEvent event) {
		try {
			Parent recommendManage = FXMLLoader
					.load(getClass().getResource("../view/recommend/album/BestRcmList.fxml")); // 바뀔 화면을 가져옴

			String temp_path = (getClass().getResource("../view/recommend/album/BestRcmList.fxml")).getPath();
			String path = temp_path.substring(1, temp_path.length()); // 현재화면 절대경로

			GobackStack.goURL(path);

			contents.getChildren().removeAll();
			contents.getChildren().setAll(recommendManage);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@FXML
	public void singerManage(ActionEvent event) { // 가수관리를 클릭 했을 때.
		try {
			// 상대경로
			Parent singerManage = FXMLLoader.load(getClass().getResource("../view/singer/singer/ShowSingerList.fxml"));

			// 현재화면 절대경로
			String temp_path = (getClass().getResource("../view/singer/singer/ShowSingerList.fxml")).getPath();
			String path = temp_path.substring(1, temp_path.length());

			// 현재화면의 fxml절대경로를 저장해야함
			GobackStack.goURL(path);

			contents.getChildren().removeAll();
			contents.getChildren().setAll(singerManage);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void albumManage(ActionEvent event) { // 앨범관리를 클릭 했을 때.
		try {
			Parent albumManage = FXMLLoader.load(getClass().getResource("../view/album/album/ShowAlbumLIst.fxml")); // 바뀔
			// 화면을
			// 가져옴

			String temp_path = (getClass().getResource("../view/album/album/ShowAlbumLIst.fxml")).getPath();
			String path = temp_path.substring(1, temp_path.length()); // 현재화면 절대경로

			GobackStack.goURL(path);

			contents.getChildren().removeAll();
			contents.getChildren().setAll(albumManage);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void musicManage(ActionEvent event) { // 곡 관리를 클릭 했을 때.
		try {
			Parent albumManage = FXMLLoader.load(getClass().getResource("../view/music/music/MusicList.fxml")); // 바뀔
			// 화면을
			// 가져옴

			String temp_path = (getClass().getResource("../view/music/music/MusicList.fxml")).getPath();
			String path = temp_path.substring(1, temp_path.length()); // 현재화면 절대경로

			GobackStack.goURL(path);

			contents.getChildren().removeAll();
			contents.getChildren().setAll(albumManage);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void recommendManage(ActionEvent event) { // 추천앨범 관리를 클릭 했을 때.
		try {
			Parent recommendManage = FXMLLoader
					.load(getClass().getResource("../view/recommend/album/RecommendAlbumList.fxml")); // 바뀔 화면을 가져옴

			String temp_path = (getClass().getResource("../view/recommend/album/RecommendAlbumList.fxml")).getPath();
			String path = temp_path.substring(1, temp_path.length()); // 현재화면 절대경로

			GobackStack.goURL(path);

			contents.getChildren().removeAll();
			contents.getChildren().setAll(recommendManage);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@FXML
	public void musicPlayer(ActionEvent event) { // MusicPlayer를 클릭 했을 때.
		if (LoginSession.session == null) {
			return;
		} else {
			if (!musicplayer.isShowing()) {
				try {
					playerLoad = new FXMLLoader(getClass().getResource("../view/musicplayer/MusicPlayer.fxml"));
					AnchorPane root = playerLoad.load();
					Scene scene = new Scene(root);
					musicplayer.setTitle("MusicPlayer");
					musicplayer.setScene(scene);
					musicplayer.show();

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@FXML
	public void notice() { // 공지사항

		try {
			Parent notice = FXMLLoader.load(getClass().getResource("../view/support/noticeboard/NoticeMenuList.fxml"));
			String temp_path = (getClass().getResource("../view/support/noticeboard/NoticeMenuList.fxml")).getPath();
			String path = temp_path.substring(1, temp_path.length()); // 현재화면 절대경로
			GobackStack.goURL(path);
			contents.getChildren().removeAll();
			contents.getChildren().setAll(notice);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void eventManage(ActionEvent event) { // 이벤트관리를 클릭 했을 때.
		try {
			Parent eventManage = FXMLLoader
					.load(getClass().getResource("../view/support/eventboard/EventShowList.fxml")); // 바뀔 화면을 가져옴
			String temp_path = (getClass().getResource("../view/support/eventboard/EventShowList.fxml")).getPath();
			String path = temp_path.substring(1, temp_path.length()); // 현재화면 절대경로
			GobackStack.goURL(path);
			contents.getChildren().removeAll();
			contents.getChildren().setAll(eventManage);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void salesMange(ActionEvent event) { // 매출관리 클릭시
		try {
			Parent salesManage = FXMLLoader.load(getClass().getResource("../view/ticket/salemanage/salesmanage.fxml")); // 바뀔
			// 화면을
			String temp_path = (getClass().getResource("../view/ticket/salemanage/salesmanage.fxml")).getPath();
			String path = temp_path.substring(1, temp_path.length()); // 현재화면 절대경로
			GobackStack.goURL(path); // 가져옴
			contents.getChildren().removeAll();
			contents.getChildren().setAll(salesManage);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void memManage() {
		try {
			Parent salesManage = FXMLLoader.load(getClass().getResource("../view/member/manage/memmanage.fxml")); // 바뀔
			// 화면을
			String temp_path = (getClass().getResource("../view/member/manage/memmanage.fxml")).getPath();
			String path = temp_path.substring(1, temp_path.length()); // 현재화면 절대경로
			GobackStack.goURL(path); // 가져옴
			contents.getChildren().removeAll();
			contents.getChildren().setAll(salesManage);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void event() { // 이벤트

		try {
			Parent event = FXMLLoader
					.load(getClass().getResource("../view/support/eventboard/EventClientShowList.fxml"));
			String temp_path = (getClass().getResource("../view/support/eventboard/EventClientShowList.fxml"))
					.getPath();
			String path = temp_path.substring(1, temp_path.length()); // 현재화면 절대경로
			GobackStack.goURL(path);
			contents.getChildren().removeAll();
			contents.getChildren().setAll(event);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void btn_message() {// 메세지
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/message/mestable.fxml"));
			Parent messageCh = loader.load();

			ShowMessageController cotroller = loader.getController();

			cotroller.setController(this);
			Scene scene = new Scene(messageCh);
			musicplayer.setTitle("모여서 각잡고 코딩 - clap");
			musicplayer.setScene(scene);
			musicplayer.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void refreshmenu() {// 화면 전화을 위해
		FXMLLoader loader = new FXMLLoader(getClass().getResource("MusicMain.fxml"));
		Parent refmain;
		try {
			refmain = loader.load();
			menu.getChildren().removeAll();
			menu.getChildren().setAll(refmain);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void game() {
		try {
			AnchorPane pane = FXMLLoader.load(getClass().getResource("Game.fxml"));
			achorpane_game.getChildren().removeAll();
			achorpane_game.getChildren().setAll(pane);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	class Thread1 extends Thread {
		@Override
		public void run() {
			try {
				while (!Thread.interrupted()) {

					// System.out.println("인터럽트" + this.isInterrupted());
					if (Thread.interrupted()) { // interrupt()메서드가 호출되면 true
						break;
					}
					Thread.sleep(2500);
					current_index = tabpane.getSelectionModel().getSelectedIndex();
					if (current_index == 4) {
						tabpane.getSelectionModel().select(0);
					} else {
						tabpane.getSelectionModel().select(current_index + 1);
					}
					// System.out.println(current_index + 1);
				}
				arr_thread[0] = this;
			} catch (Exception e) {
			}
		}
	}

}
