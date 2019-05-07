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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
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
import kr.or.ddit.clap.vo.music.PlayListVO;
import kr.or.ddit.clap.vo.singer.SingerVO;

public class UnifiedSearchController implements Initializable {

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
	private MusicList musicList2;
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
	private Pagination p_page2;
	private IAlbumService ias;
	private ObservableList<Map> list;
	private int itemsForPage2;
	@FXML
	VBox reply_vbox;
	@FXML
	FontAwesomeIcon icon_heart;
	@FXML
	AnchorPane singerMain;
	
	
	@FXML VBox mainBox1;
	@FXML StackPane stackpane1;
	@FXML AnchorPane contents;

	public void setcontroller(AnchorPane main) {
		this.main = main;

	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		System.out.println("가수번호:" + singerNo);

		try {
			
			
			// reg로 ISingerService객체를 받아옴
			reg = LocateRegistry.getRegistry("localhost", 8888);
			iss = (ISingerService) reg.lookup("singer");
			sVO = iss.singerDetailInfo(singerNo);
			ias = (IAlbumService) reg.lookup("album");
			System.out.println(sVO.getSing_no());
			// 파라미터로 받은 정보를 PK로 상세정보를 가져옴

			//앨범
			AlbumVO vo = new AlbumVO();
			vo.setSing_no(UnifiedSearchController.singerNo);
			list = FXCollections.observableArrayList(ias.singerAlbumSelect(vo));
			System.out.println("앨범리스트 사이즈 " + list.size());
			itemsForPage2 = 3;
			
			
			ims = (IMusicService) reg.lookup("music");
			ipls = (IPlayListService) reg.lookup("playlist");
			itemsForPage = 8;
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}

		musicList = new MusicList(cbnList, btnPlayList, btnAddList, btnPutList, btnMovieList, mainBox, stackpane);
		musicList2 = new MusicList( btnAddList,mainBox1,stackpane1);
		
		pageing2(list);
		
		// 일간 조회 차트
		songChart();

		label_singerName1.setText(sVO.getSing_name());
		label_ActType.setText(sVO.getSing_act_type());
		label_ActEra.setText(sVO.getSing_act_era());
		label_DebutEra.setText(sVO.getSing_debut_era());

		label_DebutMus.setText(sVO.getSing_debut_mus());
		label_Nation.setText(sVO.getSing_nation());
		//txt_intro.setText(sVO.getSing_intro());

		Image img = new Image(sVO.getSing_image());
		System.out.println("이미지경로:" + sVO.getSing_image());

		temp_img_path = sVO.getSing_image(); // sVO.getSing_image()를 전역으로 쓰기위해
		imgview_singImg.setImage(img);

		// 좋아요 수를 가져오는 쿼리
		try {
			int likeCnt = iss.selectSingerLikeCnt(singerNo);
			System.out.println("likecnt :" + likeCnt);
			label_LikeCnt.setText(likeCnt + "");

			// 세션아이디와 가수번호를 매개변수로 좋아요를 눌렀는 지 확인하는 메서드
			String id = LoginSession.session.getMem_id();
			pMap.put("singerNo", singerNo);
			pMap.put("id", id);
			System.out.println("singerNo:" + singerNo);
			System.out.println("id:" + id);
			System.out.println("첫번쨰" + yn);

			yn = iss.checkHeartYN(pMap);
			icon_heart.setIconName("HEART_ALT"); // 초기화 빈하트
			if (yn > 0) {
				icon_heart.setIconName("HEART");
			}

		} catch (Exception e) {

		}

		// 댓글조회
	
	}

private void pageing2(ObservableList<Map> list) {
		
		if (mainBox1.getChildren().size() == 4) {
			mainBox1.getChildren().remove(3);
		}
		
		if (list.size() == 0) return;
		int size = (list.size() / 2) + (list.size() % 2 > 0 ? 1 : 0);
		int totalPage = size / itemsForPage + (size % itemsForPage > 0 ? 1 : 0);
		
		p_page2 = new Pagination(totalPage, 0);
		p_page2.setPageFactory(new Callback<Integer, Node>() {
            @Override
            public Node call(Integer pageIndex) {
                return createPage2(pageIndex,list,itemsForPage);
            }
	    });
		
		mainBox1.getChildren().addAll(p_page2);
	}
	
public VBox createPage2(int pageIndex, ObservableList<Map> list, int itemsForPage) {
    int page = pageIndex * itemsForPage;
    return musicList2.albumList(list, itemsForPage, page);
}
	
	
	// 화면을 새로고침하는 메서드
	private void refesh() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("SingerMain.fxml"));
			Parent SingerMain;
			SingerMain = loader.load();
			UnifiedSearchController cotroller = loader.getController();
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
			System.out.println(i + "번 째 for문");

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
			label_id.setText(replyMap.get(i).get("MEM_ID").toString());
			System.out.println(i + "번 째 for문에 lable박스 멤버아이디" + replyMap.get(i).get("MEM_ID").toString());

			Label label_date = new Label();
			label_id.setPrefWidth(75);
			label_id.setPrefHeight(15);
			label_date.setText(replyMap.get(i).get("SING_RE_INDATE").toString());
			System.out.println(i + "번 째 for문에 lable박스 날짜" + replyMap.get(i).get("SING_RE_INDATE").toString());

			JFXButton btn_report = new JFXButton();
			btn_report.setPrefWidth(40);
			btn_report.setPrefHeight(15);
			btn_report.setText("신고");
			btn_report.setId(replyMap.get(i).get("SING_RE_NO").toString());
			btn_report.setTextFill(Color.valueOf("#fff"));
			btn_report.setStyle("-fx-background-color: #9c0000;");
			small_hbox.setMargin(btn_report, new Insets(0, 0, 0, 5));
			System.out.println(i + "번 째 for문에 버튼");

			// 신고버튼 클릭했을 때
			btn_report.setOnMouseClicked(e -> {
				JFXButton temp_btn_report = (JFXButton) e.getSource();
				for (int j = 0; j < replyMap.size(); j++) {
					if (temp_btn_report.getId().equals(replyMap.get(j).get("SING_RE_NO").toString())) {

						System.out.println("아이디값" + replyMap.get(j).get("MEM_ID").toString());
						String id = replyMap.get(j).get("MEM_ID").toString();
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
			label_contents.setText(replyMap.get(i).get("SING_RE_CONTENT").toString());
			System.out.println(i + "번 째 for문에 label_contents" + replyMap.get(i).get("SING_RE_CONTENT").toString());

			HBox h_Line = new HBox();
			// vbox.setMargin(h_Line, new Insets(0,0,0,0));
			h_Line.setPrefWidth(710);
			h_Line.setPrefHeight(1);
			h_Line.setStyle("-fx-background-color:#090948;");
			System.out.println("h_Line");

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
			songRank = FXCollections.observableArrayList(ims.selectSinger(UnifiedSearchController.singerNo));
			cb_main.setSelected(false);
			lb_total.setText("발매곡 (총 " + songRank.size() + "개)");

			pageing(songRank);

			// 아티스트 소개 y좌표 설정
			line_intro.setLayoutY(560 + (songRank.size() - 1) * 73);
			lb_intro.setLayoutY(565 + (songRank.size() - 1) * 73);
			//txt_intro.setLayoutY(620 + (songRank.size() - 1) * 73);
			reply_vbox.setLayoutY(916 + (songRank.size() - 1) * 73);

		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void wideView() {
		// img_wideimg
		System.out.println("크게보기 버튼클릭");
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

	@FXML
	public void btn_heart() throws RemoteException {
		if (yn > 0) // 좋아요 취소일 때
		{
			// 취소 메서드
			System.out.println("취소메서드 클릭");
			iss.deleteSingerLike(pMap);
			resetCnt();
			icon_heart.setIconName("HEART_ALT");

		} else {
			// 추가 메서드
			System.out.println(yn);
			System.out.println("추가메서드 클릭");
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
			System.out.println("OK 버튼을 눌렀습니다.");
			return 1;
		} else if (confirmResult == ButtonType.CANCEL) {
			System.out.println("취소 버튼을 눌렀습니다.");
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
			System.out.println("OK 버튼을 눌렀습니다.");
			return 1;
		} else if (confirmResult == ButtonType.CANCEL) {
			System.out.println("취소 버튼을 눌렀습니다.");
			return -1;
		}
		return -1;
	}

	private void resetCnt() throws RemoteException {
		// 좋아요 카운트 쿼리

		int likeCnt = iss.selectSingerLikeCnt(singerNo);
		System.out.println("likecnt :" + likeCnt);
		label_LikeCnt.setText(likeCnt + "");

		yn = iss.checkHeartYN(pMap);
	}
}