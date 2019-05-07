package kr.or.ddit.clap.view.chartmenu.genre;

import java.io.IOException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;
import java.util.ResourceBundle;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import javafx.fxml.Initializable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import kr.or.ddit.clap.main.LoginSession;
import kr.or.ddit.clap.main.MusicMainController;
import kr.or.ddit.clap.service.musichistory.IMusicHistoryService;
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

public class GenreController implements Initializable{

	@FXML VBox mainBox;
	@FXML JFXCheckBox cb_main;
	@FXML JFXButton btn_Song;
	@FXML JFXButton btn_Pop;
	@FXML JFXButton btn_Ost;
	@FXML JFXButton btn_Other;
	@FXML Label la_Date;
	@FXML StackPane stackpane;
	
	private Registry reg;
	private IMusicHistoryService imhs;
	private IPlayListService ipls;
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
	

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			reg = LocateRegistry.getRegistry("localhost", 8888);
			imhs = (IMusicHistoryService) reg.lookup("history");
			ipls = (IPlayListService) reg.lookup("playlist");
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
		musicList = new MusicList(cbnList, btnPlayList, btnAddList, btnPutList,
								  btnMovieList, mainBox, stackpane);
		
		// 일간 조회 차트 
		songChart();
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
	
	// 가요장르
	@FXML public void songChart() {
		try {
			songRank = FXCollections.observableArrayList(imhs.genreSelect("1"));
			btn_Song.setStyle("-fx-background-color:#9c0000;-fx-text-fill:#FFFFFF;");
			btn_Pop.setStyle("-fx-background-color:#FFFFFF;");
			btn_Ost.setStyle("-fx-background-color:#FFFFFF;");
			btn_Other.setStyle("-fx-background-color:#FFFFFF;");
			cb_main.setSelected(false);
			
			Calendar cal = Calendar.getInstance();
			String toDay = "";
			toDay = cal.get(Calendar.YEAR)+"." + (cal.get(Calendar.MONTH)+1) + 
					"." + cal.get(Calendar.DAY_OF_MONTH);
			la_Date.setText(toDay);
			
			musicList.musicList(songRank);
			
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	// POP장르
	@FXML public void popChart() {
		try {
			popRank = FXCollections.observableArrayList(imhs.genreSelect("2"));
			btn_Song.setStyle("-fx-background-color:#FFFFFF;");
			btn_Pop.setStyle("-fx-background-color:#9c0000;-fx-text-fill:#FFFFFF;");
			btn_Ost.setStyle("-fx-background-color:#FFFFFF;");
			btn_Other.setStyle("-fx-background-color:#FFFFFF;");
			cb_main.setSelected(false);
			
			Calendar cal = Calendar.getInstance();
			String toDay = "";
			toDay = cal.get(Calendar.YEAR)+"." + (cal.get(Calendar.MONTH)+1) + 
					"." + cal.get(Calendar.DAY_OF_MONTH);
			la_Date.setText(toDay);
			
			musicList.musicList(popRank);
			
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	// OST장르
	@FXML public void ostChart() {
		try {
			ostRank = FXCollections.observableArrayList(imhs.genreSelect("3"));
			btn_Song.setStyle("-fx-background-color:#FFFFFF;");
			btn_Pop.setStyle("-fx-background-color:#FFFFFF;");
			btn_Ost.setStyle("-fx-background-color:#9c0000;-fx-text-fill:#FFFFFF;");
			btn_Other.setStyle("-fx-background-color:#FFFFFF;");
			cb_main.setSelected(false);
			
			Calendar cal = Calendar.getInstance();
			String toDay = "";
			toDay = cal.get(Calendar.YEAR)+"." + (cal.get(Calendar.MONTH)+1) + 
					"." + cal.get(Calendar.DAY_OF_MONTH);
			la_Date.setText(toDay);
			
			musicList.musicList(ostRank);
			
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	// 그 외 장르
	@FXML public void otherChart() {
		try {
			otherRank = FXCollections.observableArrayList(imhs.genreSelect("4"));
			btn_Song.setStyle("-fx-background-color:#FFFFFF;");
			btn_Pop.setStyle("-fx-background-color:#FFFFFF;");
			btn_Ost.setStyle("-fx-background-color:#FFFFFF;");
			btn_Other.setStyle("-fx-background-color:#9c0000;-fx-text-fill:#FFFFFF;");
			cb_main.setSelected(false);
			
			Calendar cal = Calendar.getInstance();
			String toDay = "";
			toDay = cal.get(Calendar.YEAR)+"." + (cal.get(Calendar.MONTH)+1) + 
					"." + cal.get(Calendar.DAY_OF_MONTH);
			la_Date.setText(toDay);
			
			musicList.musicList(otherRank);
			
		} catch (RemoteException e) {
			e.printStackTrace();
		}
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
}