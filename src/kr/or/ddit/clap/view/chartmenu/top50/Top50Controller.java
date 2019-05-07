package kr.or.ddit.clap.view.chartmenu.top50;

import java.io.IOException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
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

public class Top50Controller implements Initializable{

	@FXML VBox mainBox;
	@FXML JFXCheckBox cb_main;
	@FXML JFXButton btn_ToDay;
	@FXML JFXButton btn_Week;
	@FXML JFXButton btn_Month;
	@FXML Label la_Date;
	@FXML StackPane stackpane;
	
	private Registry reg;
	private IMusicHistoryService imhs;
	private MusicList musicList;
	private IPlayListService ipls;
	private ObservableList<Map> toDayRank;
	private ObservableList<Map> weekRank;
	private ObservableList<Map> monthRank;
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
		} catch (IOException e) {
			e.printStackTrace();
		}
		musicList = new MusicList(cbnList, btnPlayList, btnAddList, btnPutList,
								  btnMovieList, mainBox, stackpane);
		
		// 일간 조회 차트 
		toDayChart();
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
	
	// 일간차트
	@FXML public void toDayChart() {
		try {
			toDayRank = FXCollections.observableArrayList(imhs.toDaySelect());
			btn_ToDay.setStyle("-fx-background-color:#9c0000;-fx-text-fill:#FFFFFF;");
			btn_Week.setStyle("-fx-background-color:#FFFFFF;");
			btn_Month.setStyle("-fx-background-color:#FFFFFF;");
			cb_main.setSelected(false);
			
			Calendar cal = Calendar.getInstance();
			String toDay = "";
			toDay = cal.get(Calendar.YEAR)+"." + (cal.get(Calendar.MONTH)+1) + 
					"." + cal.get(Calendar.DAY_OF_MONTH);
			la_Date.setText(toDay);
			
			musicList.musicList(toDayRank);
			
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	// 주간차트
	@FXML public void weekChart() {
		try {
			btn_ToDay.setStyle("-fx-background-color:#FFFFFF;");
			btn_Week.setStyle("-fx-background-color:#9c0000;-fx-text-fill:#FFFFFF;");
			btn_Month.setStyle("-fx-background-color:#FFFFFF;");
			cb_main.setSelected(false);
			
			Calendar cal = Calendar.getInstance();
			Calendar cal2 = Calendar.getInstance();
			cal.add(Calendar.DATE, -7);
			int temp = 0; 
			int beforeWeek = 0; // 지난주차
			
			// 주자 월요일 날짜 구하기
			if (cal.get(Calendar.DAY_OF_WEEK) > 2) {
				temp = cal.get(Calendar.DAY_OF_WEEK) - 2;
			}else if (cal.get(Calendar.DAY_OF_WEEK) < 2) {
				temp = 6;
			}
			cal.add(Calendar.DATE, -temp);
			
			// 몇주차 구하기
			int count = 1;
			cal2.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), 1);
			
			while(cal2.get(Calendar.DAY_OF_WEEK) != 2 ) {
				count++;
				cal2.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), count);
			}
			
			int first_week = cal2.get(Calendar.DAY_OF_MONTH);
			int last_week = cal.get(Calendar.DAY_OF_MONTH); 
			
			for(; first_week<=last_week; first_week+=7) {
				beforeWeek++;
			}
			
			String toDay = cal.get(Calendar.YEAR) + "." + (cal.get(Calendar.MONTH) +1) + "." + beforeWeek+"주차";
			
			la_Date.setText(toDay);
			
			Map day = new HashMap<String,String>();
			
			String startday = cal.get(Calendar.YEAR)+"/" + (cal.get(Calendar.MONTH)+1) + "/" + cal.get(Calendar.DAY_OF_MONTH);
			cal.add(Calendar.DATE, 6);
			String endday = cal.get(Calendar.YEAR)+"/" + (cal.get(Calendar.MONTH)+1) + "/" + cal.get(Calendar.DAY_OF_MONTH);
			day.put("startday", startday);
			
			day.put("endday", endday);
			
			weekRank = FXCollections.observableArrayList(imhs.periodSelect(day));
			musicList.musicList(weekRank);
			
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	// 월간 차트
	@FXML public void monthChart() {
		try {
			
			btn_ToDay.setStyle("-fx-background-color:#FFFFFF;");
			btn_Week.setStyle("-fx-background-color:#FFFFFF;");
			btn_Month.setStyle("-fx-background-color:#9c0000;-fx-text-fill:#FFFFFF;");
			cb_main.setSelected(false);
			
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.MONTH, -1);
			String monthText = cal.get(Calendar.YEAR)+"."+(cal.get(Calendar.MONTH)+1);
			la_Date.setText(monthText);
			
			Map month = new HashMap<String,String>();
			cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), 1);
			String startday = cal.get(Calendar.YEAR)+"/" + (cal.get(Calendar.MONTH)+1) + "/" + cal.get(Calendar.DAY_OF_MONTH);
			String endday = cal.get(Calendar.YEAR)+"/" + (cal.get(Calendar.MONTH)+1) + "/" + cal.getActualMaximum(Calendar.DAY_OF_MONTH);
			month.put("startday", startday);
			month.put("endday", endday);
			
			monthRank = FXCollections.observableArrayList(imhs.periodSelect(month)); // 맵에서 가져왓어요
			musicList.musicList(monthRank);
			
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
}