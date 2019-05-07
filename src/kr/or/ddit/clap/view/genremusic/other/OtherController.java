package kr.or.ddit.clap.view.genremusic.other;

import java.io.IOException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import javafx.fxml.Initializable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Pagination;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import kr.or.ddit.clap.main.LoginSession;
import kr.or.ddit.clap.main.MusicMainController;
import kr.or.ddit.clap.service.music.IMusicService;
import kr.or.ddit.clap.service.playlist.IPlayListService;
import kr.or.ddit.clap.view.chartmenu.dialog.MyAlbumDialogController;
import kr.or.ddit.clap.view.chartmenu.musiclist.MusicList;
import kr.or.ddit.clap.view.musicplayer.MusicPlayerController;
import kr.or.ddit.clap.vo.music.PlayListVO;

/**
 * 
 * @author 진민규
 *
 */

public class OtherController implements Initializable{

	@FXML VBox mainBox;
	@FXML JFXCheckBox cb_main;
	@FXML JFXButton btn_world;
	@FXML JFXButton btn_china;
	@FXML JFXButton btn_children;
	@FXML JFXButton btn_taegyo;
	@FXML StackPane stackpane;
	
	private Registry reg;
	private IMusicService ims;
	private IPlayListService ipls;
	private MusicList musicList;
	private ObservableList<Map> worldRank;
	private ObservableList<Map> chinaRank;
	private ObservableList<Map> childrenRank;
	private ObservableList<Map> taegyoRank;
	private ObservableList<JFXCheckBox> cbnList = FXCollections.observableArrayList();
	private ObservableList<JFXButton> btnPlayList = FXCollections.observableArrayList();
	private ObservableList<JFXButton> btnAddList = FXCollections.observableArrayList();
	private ObservableList<JFXButton> btnPutList = FXCollections.observableArrayList();
	private ObservableList<JFXButton> btnMovieList = FXCollections.observableArrayList();
	private MusicPlayerController mpc;
	private int itemsForPage;
	private Pagination p_page;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			reg = LocateRegistry.getRegistry("localhost", 8888);
			ims = (IMusicService) reg.lookup("music");
			ipls = (IPlayListService) reg.lookup("playlist");
			itemsForPage = 5;
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
		musicList = new MusicList(cbnList, btnPlayList, btnAddList, btnPutList,
								  btnMovieList, mainBox, stackpane);
		
		// 일간 조회 차트 
		worldChart();
	}
	
	// 메인 재생 버튼 이벤트
	@FXML public void btnMainPlay() {
		if (LoginSession.session == null) {
			return;
		}
		
		ArrayList<String> list = musicCheckList();
		playListInsert(list,true);
		if (!MusicMainController.musicplayer.isShowing()) {
			try {
				MusicMainController.playerLoad = new FXMLLoader(getClass().getResource("../../musicplayer/MusicPlayer.fxml"));
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
	@FXML public void btnMainAdd() {
		if (LoginSession.session == null) {
			return;
		}
		
		ArrayList<String> list = musicCheckList();
		playListInsert(list,false);
		cb_main.setSelected(false);
		mainCheck();
	}

	// 메인 담기 버튼 이벤트
	@FXML public void btnMainPut() {
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
	@FXML public void mainCheck() {
		if (cb_main.isSelected()) {
			for(int i = 0; i < cbnList.size(); i++) {
				cbnList.get(i).setSelected(true);
			}
		} else {
			for(int i = 0; i < cbnList.size(); i++) {
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
	
	// 월드뮤직장르
	@FXML public void worldChart() {
		try {
			worldRank = FXCollections.observableArrayList(ims.genreMusicSelete("13"));
			btn_world.setStyle("-fx-background-color:#9c0000;-fx-text-fill:#FFFFFF;");
			btn_china.setStyle("-fx-background-color:#FFFFFF;");
			btn_children.setStyle("-fx-background-color:#FFFFFF;");
			btn_taegyo.setStyle("-fx-background-color:#FFFFFF;");
			cb_main.setSelected(false);
			
			pageing(worldRank);
			
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	// 중국음악장르
	@FXML public void chinaChart() {
		try {
			chinaRank = FXCollections.observableArrayList(ims.genreMusicSelete("14"));
			btn_world.setStyle("-fx-background-color:#FFFFFF;");
			btn_china.setStyle("-fx-background-color:#9c0000;-fx-text-fill:#FFFFFF;");
			btn_children.setStyle("-fx-background-color:#FFFFFF;");
			btn_taegyo.setStyle("-fx-background-color:#FFFFFF;");
			
			cb_main.setSelected(false);
			
			pageing(chinaRank);
			
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	// 동요장르
	@FXML public void childrenChart() {
		try {
			childrenRank = FXCollections.observableArrayList(ims.genreMusicSelete("15"));
			btn_world.setStyle("-fx-background-color:#FFFFFF;");
			btn_china.setStyle("-fx-background-color:#FFFFFF;");
			btn_children.setStyle("-fx-background-color:#9c0000;-fx-text-fill:#FFFFFF;");
			btn_taegyo.setStyle("-fx-background-color:#FFFFFF;");
			cb_main.setSelected(false);
			
			pageing(childrenRank);
			
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	// 태교음악 장르
	@FXML public void taegyoChart() {
		try {
			taegyoRank = FXCollections.observableArrayList(ims.genreMusicSelete("16"));
			btn_world.setStyle("-fx-background-color:#FFFFFF;");
			btn_china.setStyle("-fx-background-color:#FFFFFF;");
			btn_children.setStyle("-fx-background-color:#FFFFFF;");
			btn_taegyo.setStyle("-fx-background-color:#9c0000;-fx-text-fill:#FFFFFF;");
			cb_main.setSelected(false);
			
			pageing(taegyoRank);
			
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	public VBox createPage(int pageIndex, ObservableList<Map> list, int itemsForPage) {
        int page = pageIndex * itemsForPage;
        return musicList.pagenation(list,itemsForPage,page);
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
				if(play) {
					mpc.selectIndex();
				}
			}
		}
	}
	
	private void pageing(ObservableList<Map> list) {
		
		if (mainBox.getChildren().size() == 4) {
			mainBox.getChildren().remove(3);
		}
		
		if (list.size() == 0) return;
		int totalPage = (list.size() / itemsForPage) + (list.size() % itemsForPage > 0 ? 1 : 0);
		
		p_page = new Pagination(totalPage, 0);
		p_page.setPageFactory(new Callback<Integer, Node>() {
            @Override
            public Node call(Integer pageIndex) {
                return createPage(pageIndex,list,itemsForPage);
            }
	    });
		
		mainBox.getChildren().addAll(p_page);
	}
	
	
}