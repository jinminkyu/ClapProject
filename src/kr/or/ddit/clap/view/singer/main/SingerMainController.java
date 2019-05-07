/**
 * 
 * 윤한수
 */
package kr.or.ddit.clap.view.singer.main;

import java.io.IOException;
import java.net.URL;
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

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
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
import kr.or.ddit.clap.service.music.IMusicService;
import kr.or.ddit.clap.service.playlist.IPlayListService;
import kr.or.ddit.clap.service.singer.ISingerService;
import kr.or.ddit.clap.view.chartmenu.dialog.MyAlbumDialogController;
import kr.or.ddit.clap.view.chartmenu.musiclist.MusicList;
import kr.or.ddit.clap.view.member.mypage.OtherMypageController;
import kr.or.ddit.clap.view.musicplayer.MusicPlayerController;
import kr.or.ddit.clap.vo.music.PlayListVO;
import kr.or.ddit.clap.vo.singer.SingerVO;

public class SingerMainController implements Initializable {

	public static String singerNo;// 파라미터로 받은 선택한 가수의 PK
	private Registry reg;
	private ISingerService iss;
	private String temp_img_path = "";

	// 파라미터로 넘기기 위해 전역으로 선언
	public SingerVO sVO = null;
	public String str_like_cnt;
	ObservableList<Map<String, String>> replyMap;

	
	
	@FXML
	Label label_singNo;
	@FXML
	Label label_singerName1;
	@FXML
	Label label_ActType;
	@FXML
	Label label_ActEra;
	@FXML
	Label label_DebutEra;
	@FXML
	Label label_DebutMus;
	@FXML
	Label label_Nation;
	@FXML
	Label label_LikeCnt;
	@FXML
	ImageView imgview_singImg;
	@FXML
	Label txt_intro;
	@FXML
	AnchorPane main;

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
	@FXML
	Label lb_intro;
	@FXML
	Line line_intro;

	int yn = 0;
	Map<String, String> pMap = new HashMap<String, String>();

	private IMusicService ims;
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
	private Pagination p_page1;

	@FXML
	VBox reply_vbox;
	@FXML
	FontAwesomeIcon icon_heart;
	@FXML
	AnchorPane singerMain;

	public void setcontroller(AnchorPane main) {
		this.main = main;

	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		try {
			// reg로 ISingerService객체를 받아옴
			reg = LocateRegistry.getRegistry("localhost", 8888);
			iss = (ISingerService) reg.lookup("singer");
			sVO = iss.singerDetailInfo(singerNo);
			// 파라미터로 받은 정보를 PK로 상세정보를 가져옴

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

		label_singerName1.setText(sVO.getSing_name());
		label_ActType.setText(sVO.getSing_act_type());
		label_ActEra.setText(sVO.getSing_act_era());
		label_DebutEra.setText(sVO.getSing_debut_era());

		label_DebutMus.setText(sVO.getSing_debut_mus());
		label_Nation.setText(sVO.getSing_nation());
		txt_intro.setText(sVO.getSing_intro());

		Image img = new Image(sVO.getSing_image());

		temp_img_path = sVO.getSing_image(); // sVO.getSing_image()를 전역으로 쓰기위해
		imgview_singImg.setImage(img);

		// 좋아요 수를 가져오는 쿼리
		try {
			int likeCnt = iss.selectSingerLikeCnt(singerNo);
			label_LikeCnt.setText(likeCnt + "");

			// 세션아이디와 가수번호를 매개변수로 좋아요를 눌렀는 지 확인하는 메서드
			String id = LoginSession.session.getMem_id();
			pMap.put("singerNo", singerNo);
			pMap.put("id", id);

			yn = iss.checkHeartYN(pMap);
			icon_heart.setIconName("HEART_ALT"); // 초기화 빈하트
			if (yn > 0) {
				icon_heart.setIconName("HEART");
			}

		} catch (Exception e) {

		}

		// 댓글조회
		try {
			replyMap = FXCollections.observableArrayList(iss.selectReply(singerNo));
			int size = replyMap.size();

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
			input_reply.setId("input_reply");

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
				rmap.put("singerNo", singerNo);
				rmap.put("contents", contents);
				rmap.put("mem_id", mem_id);

				try {
					iss.insertReply(rmap);
					input_reply.setText("");

					// 화면새로고침
					for (int i = 0; i < 1000; i++) {
					}
					for (int i = 0; i < 2000000000; i++) {
					}
					for (int i = 0; i < 2000000000; i++) {

					}

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
			reply_vbox.getChildren().addAll(HboxReply, h_reply);

		} catch (Exception e) {
			e.printStackTrace();
		}


		pageing1(replyMap);

	}

	// 화면을 새로고침하는 메서드
	private void refesh() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("SingerMain.fxml"));
			Parent SingerMain;
			SingerMain = loader.load();
			SingerMainController cotroller = loader.getController();
			cotroller.setcontroller(main);
			singerMain.getChildren().removeAll();
			singerMain.getChildren().setAll(SingerMain);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 페이징처리

	public VBox createPage1(int pageIndex, ObservableList<Map<String, String>> list, int itemsForPage) {
		int page = pageIndex * itemsForPage;
		return pagenation(list, itemsForPage, page);
	}

	// 페이징 처리

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

		reply_vbox.getChildren().addAll(p_page1);
	}

	// 댓글창을 그려주는 부분
	///////////////////////////////////////////////////////////////////////
	public VBox pagenation(ObservableList<Map<String, String>> list, int itemsForPage, int page) {
		// 임시로 담아주는 객체
		VBox temp_vbox = new VBox();

		int size = Math.min(page + itemsForPage, list.size());
		for (int i = page; i < size; i++) {
			HBox hbox = new HBox();
			hbox.setPrefWidth(731);
			hbox.setPrefHeight(73);

			reply_vbox.setMargin(hbox, new Insets(15, 0, 0, 0));

			ImageView imgView = new ImageView();
			Image image = new Image(replyMap.get(i).get("MEM_IMAGE").toString());
			imgView.setImage(image);
			imgView.setFitWidth(40);
			imgView.setFitHeight(40);
			imgView.setId(replyMap.get(i).get("SING_RE_NO").toString());
			hbox.setMargin(imgView, new Insets(0, 15, 0, 0));
			// 이미지 클릭했을 때
			imgView.setOnMouseClicked(e -> {
				ImageView temp_imgView = (ImageView) e.getSource();
				for (int j = 0; j < replyMap.size(); j++) {
					if (temp_imgView.getId().equals(replyMap.get(j).get("SING_RE_NO").toString())) {

						String id = replyMap.get(j).get("MEM_ID").toString();

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

			HBox small_hbox = new HBox();
			small_hbox.setPrefWidth(533);
			small_hbox.setPrefHeight(30);

			Label label_id = new Label();
			label_id.setPrefWidth(40);
			label_id.setPrefHeight(15);
			label_id.setText(replyMap.get(i).get("MEM_ID").toString());

			Label label_date = new Label();
			label_id.setPrefWidth(75);
			label_id.setPrefHeight(15);
			label_date.setText(replyMap.get(i).get("SING_RE_INDATE").toString());

			JFXButton btn_report = new JFXButton();
			btn_report.setPrefWidth(40);
			btn_report.setPrefHeight(15);
			btn_report.setText("신고");
			btn_report.setId(replyMap.get(i).get("SING_RE_NO").toString());
			btn_report.setTextFill(Color.valueOf("#fff"));
			btn_report.setStyle("-fx-background-color: #9c0000;");
			small_hbox.setMargin(btn_report, new Insets(0, 0, 0, 5));

			// 신고버튼 클릭했을 때
			btn_report.setOnMouseClicked(e -> {
				JFXButton temp_btn_report = (JFXButton) e.getSource();
				for (int j = 0; j < replyMap.size(); j++) {
					if (temp_btn_report.getId().equals(replyMap.get(j).get("SING_RE_NO").toString())) {

						String id = replyMap.get(j).get("MEM_ID").toString();

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
			btn_delete.setId(replyMap.get(i).get("SING_RE_NO").toString());
			btn_delete.setTextFill(Color.valueOf("#fff"));
			btn_delete.setStyle("-fx-background-color: #9c0000;");

			btn_delete.setId(replyMap.get(i).get("SING_RE_NO").toString());
			small_hbox.setMargin(btn_delete, new Insets(0, 0, 0, 5));

			btn_delete.setVisible(false); // 기본적으로 안보이게 설정

			// 관리자 이거나 리플의 작성자와 세션의 값이 같으면 버튼을 보여준다.
			if (LoginSession.session.getMem_auth().equals("t")
					|| LoginSession.session.getMem_id().equals(replyMap.get(i).get("MEM_ID").toString())) {
				btn_delete.setVisible(true);
			}

			// 댓글삭제로직
			btn_delete.setOnMouseClicked(e -> {
				JFXButton temp_btn_delete = (JFXButton) e.getSource();
				for (int j = 0; j < replyMap.size(); j++) {
					if (temp_btn_delete.getId().equals(replyMap.get(j).get("SING_RE_NO").toString())) {

						String id = replyMap.get(j).get("SING_RE_NO").toString();

						int check = alertConfrimDelete();
						// No
						if (check == -1) {
							return;
						}

						// Yes
						try {
							iss.deleteSigerReply(id);

							refesh();
						} catch (RemoteException e1) {
							e1.printStackTrace();
						}

					}
				}
			});

			Label label_contents = new Label();
			label_contents.setPrefWidth(598);
			label_contents.setPrefHeight(43);
			label_contents.setText(replyMap.get(i).get("SING_RE_CONTENT").toString());

			HBox h_Line = new HBox();
			// vbox.setMargin(h_Line, new Insets(0,0,0,0));
			h_Line.setPrefWidth(710);
			h_Line.setPrefHeight(1);
			h_Line.setStyle("-fx-background-color:#090948;");

			small_hbox.getChildren().addAll(label_id, label_date, btn_report, btn_delete);
			vbox.getChildren().addAll(small_hbox, label_contents);
			hbox.getChildren().addAll(imgView, vbox);
			// reply_vbox.getChildren().addAll(hbox, h_Line);

			temp_vbox.getChildren().addAll(hbox, h_Line);
		}

		return temp_vbox;

	}
	// 보여주는 거 끝
	///////////////////////////////////////////////////////////////////////

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
			songRank = FXCollections.observableArrayList(ims.selectSinger(SingerMainController.singerNo));
			cb_main.setSelected(false);
			lb_total.setText("발매곡 (총 " + songRank.size() + "개)");

			pageing(songRank);

			// 아티스트 소개 y좌표 설정
			line_intro.setLayoutY(560 + (songRank.size() - 1) * 73);
			lb_intro.setLayoutY(565 + (songRank.size() - 1) * 73);
			txt_intro.setLayoutY(620 + (songRank.size() - 1) * 73);
			reply_vbox.setLayoutY(916 + (songRank.size() - 1) * 73);

		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void wideView() {
		// img_wideimg
		try {
			AnchorPane pane = FXMLLoader.load(getClass().getResource("../singer/SingerImgWiderDialog.fxml"));
			Stage stage = new Stage(StageStyle.UTILITY);
			Scene scene = new Scene(pane);
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			Stage primaryStage = (Stage) label_singerName1.getScene().getWindow();
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

	@FXML
	public void btn_heart() throws RemoteException {
		if (yn > 0) // 좋아요 취소일 때
		{
			// 취소 메서드
			iss.deleteSingerLike(pMap);
			resetCnt();
			icon_heart.setIconName("HEART_ALT");

		} else {
			// 추가 메서드
			iss.insertSingerLike(pMap);
			resetCnt();
			icon_heart.setIconName("HEART");
		}

	}

	// 경고 알림창
	public int alertConfrimDelete(String id) {
		Alert alertConfirm = new Alert(AlertType.CONFIRMATION);

		alertConfirm.setTitle("CONFIRMATION");
		alertConfirm.setContentText(id + "님을 신고하시겠습니까?");

		// Alert창을 보여주고 사용자가 누른 버튼 값 읽어오기
		ButtonType confirmResult = alertConfirm.showAndWait().get();

		if (confirmResult == ButtonType.OK) {
			return 1;
		} else if (confirmResult == ButtonType.CANCEL) {
			return -1;
		}
		return -1;
	}

	public int alertConfrimDelete() {
		Alert alertConfirm = new Alert(AlertType.CONFIRMATION);

		alertConfirm.setTitle("CONFIRMATION");
		alertConfirm.setContentText("댓글을  삭제하시겠습니까?");

		// Alert창을 보여주고 사용자가 누른 버튼 값 읽어오기
		ButtonType confirmResult = alertConfirm.showAndWait().get();

		if (confirmResult == ButtonType.OK) {
			return 1;
		} else if (confirmResult == ButtonType.CANCEL) {
			return -1;
		}
		return -1;
	}

	private void resetCnt() throws RemoteException {
		// 좋아요 카운트 쿼리

		int likeCnt = iss.selectSingerLikeCnt(singerNo);
		label_LikeCnt.setText(likeCnt + "");

		yn = iss.checkHeartYN(pMap);
	}
}