package kr.or.ddit.clap.view.musicplayer;

import java.io.IOException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import kr.or.ddit.clap.main.LoginSession;
import kr.or.ddit.clap.main.MusicMainController;
import kr.or.ddit.clap.service.musichistory.IMusicHistoryService;
import kr.or.ddit.clap.service.playlist.IPlayListService;
import kr.or.ddit.clap.service.ticket.ITicketService;
import kr.or.ddit.clap.vo.music.MusicHistoryVO;
import kr.or.ddit.clap.vo.music.PlayListVO;
import kr.or.ddit.clap.vo.ticket.TicketBuyListVO;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXSlider;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer.Status;
import javafx.scene.paint.Color;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.ScrollPane;
import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.controls.JFXButton;

public class MusicPlayerController implements Initializable{

	@FXML JFXSlider slider_time;
	@FXML JFXSlider slider_volum;
	@FXML Label label_musicName;
	@FXML Label label_singerName;
	@FXML Label Label_nowTime;
	@FXML Label Label_finalTime;
	@FXML Label label_lyrics;
	@FXML TreeTableColumn<PlayListVO, JFXCheckBox> tcol_playListCheck;
	@FXML TreeTableColumn<PlayListVO, VBox> tcol_playListVbox;
	@FXML JFXTreeTableView<PlayListVO> t_playListTable;
	@FXML FontAwesomeIcon icon_retweet;
	@FXML FontAwesomeIcon icon_random;
	@FXML FontAwesomeIcon icon_play;
	@FXML FontAwesomeIcon icon_lyrics;
	@FXML ScrollPane scroll_lyrics;
	@FXML ImageView imgview_album;
	@FXML JFXTabPane tabpane_main;
	@FXML AnchorPane anchorpane_myalbum;
	@FXML JFXCheckBox btn_check;
	@FXML JFXButton btn_add;
	@FXML JFXButton btn_del;
	@FXML Label label_loginid;


	private Stage stage;
	public static MusicPlayer player;
	public ObservableList<PlayListVO> playList;
	private ObservableList<String> addMus_no;
	private ObservableList<String> delMus_no;
	private Registry reg;
	private IPlayListService ipls;
	private IMusicHistoryService imhs;
	private boolean retweenFlag = false;
	private boolean randomFlag = false;
	private boolean refreshFlag = false;
	private int[] randomIndex;
	private int count;
	private int mus_index;
	private TreeItem<PlayListVO> playListRoot;
	private MyAlbumListController mal;
	private ITicketService its;
	private List<TicketBuyListVO> buyticket;
	private String refreshMusNo;
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		try {
			reg = LocateRegistry.getRegistry("localhost", 8888);
			ipls = (IPlayListService) reg.lookup("playlist");
			imhs = (IMusicHistoryService) reg.lookup("history");
			its = (ITicketService) reg.lookup("ticket");
			buyticket = its.buyfind(LoginSession.session.getMem_id());
			playList = FXCollections.observableArrayList(ipls.playlistSelect(LoginSession.session.getMem_id()));
			player = new MusicPlayer();
			stage = MusicMainController.musicplayer;
			scroll_lyrics.setVisible(false);
			btn_add.setVisible(false);
			addMus_no = FXCollections.observableArrayList();
			delMus_no = FXCollections.observableArrayList();
			t_playListTable.setPlaceholder(new Label(""));
			String helloText = "님 환영합니다!!!";
			if (buyticket.size() == 0) {
				helloText = "님 이용권 결제를 하시면 전곡듣기가 가능합니다~";
			}
			label_loginid.setText(LoginSession.session.getMem_id() + helloText);
		} catch (RemoteException e2) {
			e2.printStackTrace();
		} catch (NotBoundException e1) {
			e1.printStackTrace();
		}

		tcol_playListCheck.setCellValueFactory( param -> 
		new SimpleObjectProperty<JFXCheckBox>(param.getValue().getValue().getCheckbox())
		);
		
		tcol_playListVbox.setCellValueFactory( param ->
			new SimpleObjectProperty<VBox>(param.getValue().getValue().getVbox())
		);
		
		playListRoot = new RecursiveTreeItem<>(playList, RecursiveTreeObject::getChildren);
		t_playListTable.setRoot(playListRoot);
		t_playListTable.setShowRoot(false);
		
	
		
		close();
		playListTableSelet();
		sliederMove();
		tapPaneSelete();
	}

	@FXML public void onPlay() {
		if (playList.size() != 0) {
			player.play(icon_play);
			endMusic();
		}
	}

	@FXML public void forWard() {
		
		selectIndex(true);
		
	}

	@FXML public void backWard() {
		
		selectIndex(false);
	}
	
	@FXML public void retweetClick() {
		if (!retweenFlag) {
			icon_retweet.setFill(Color.valueOf("#9c0000"));
			retweenFlag = true;
		} else {
			icon_retweet.setFill(Color.valueOf("#FFFFFF"));
			retweenFlag = false;
		}
	}
	
	@FXML public void randomClick() {
		if (!randomFlag) {
			icon_random.setFill(Color.valueOf("#9c0000"));
			randomFlag = true;
		} else {
			icon_random.setFill(Color.valueOf("#FFFFFF"));
			randomFlag = false;
		} 
	}
	
	

	@FXML public void lyricsClick() {
		if(!scroll_lyrics.isVisible()) {
			scroll_lyrics.setVisible(true);
			imgview_album.setVisible(false);
			icon_lyrics.setFill(Color.valueOf("#9c0000"));
		} else {
			scroll_lyrics.setVisible(false);
			imgview_album.setVisible(true);
			icon_lyrics.setFill(Color.valueOf("#FFFFFF"));
		}
	}
	
	@FXML public void playListAdd() {
		addMus_no.clear();
		mal = MyAlbumController.loader.getController();
		
		for (int i = 0; i < mal.myalbumList.size(); i++) {
			if (mal.t_table.getTreeItem(i).getValue().getChBox1().isSelected()) {
				addMus_no.add(mal.t_table.getTreeItem(i).getValue().getMus_no());
			}
		}
		
		PlayListVO vo = new PlayListVO();
		for (int i = 0; i < addMus_no.size(); i++) {
			vo.setMem_id(LoginSession.session.getMem_id());
			vo.setMus_no(addMus_no.get(i));
			try {
				ipls.playlistInsert(vo);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		btn_check.setSelected(false);
		allCheck();
		reFresh();
	}

	@FXML public void playListDelete() {
		delMus_no.clear();
		
		for (int i = 0; i < playList.size(); i++) {
			if (t_playListTable.getTreeItem(i).getValue().getCheckbox1().isSelected()) {
				delMus_no.add(t_playListTable.getTreeItem(i).getValue().getMus_no());
			}
		}
		
		PlayListVO vo = new PlayListVO();
		for (int i = 0; i < delMus_no.size(); i++) {
			vo.setMem_id(LoginSession.session.getMem_id());
			vo.setMus_no(delMus_no.get(i));
			try {
				ipls.playlistDelete(vo);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		if (player.mediaPlayer != null && playList.get(mus_index).getCheckbox1().isSelected()) {
			player.mediaPlayer.stop();
			player.mediaPlayer = null;
			label_musicName.setText("재생 목록이 없습니다");
			label_singerName.setText("듣고 싶은 곡을 선택해 보세요!");
			imgview_album.setImage(null);
			Label_nowTime.setText("00:00");
			Label_finalTime.setText("00:00");
			icon_play.setIconName("PLAY");
			slider_time.setValue(0);
		}else {
			btn_check.setSelected(false);
			allCheck();
			reFresh();
			return;
		}
		btn_check.setSelected(false);
		allCheck();
		try {
			playList = FXCollections.observableArrayList(ipls.playlistSelect(LoginSession.session.getMem_id()));
			playListRoot = new RecursiveTreeItem<>(playList, RecursiveTreeObject::getChildren);
			t_playListTable.setRoot(playListRoot);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
	}
	
	@FXML public void allCheck() {
		if (MyAlbumController.loader != null && tabpane_main.getSelectionModel().isSelected(1)) {
			mal = MyAlbumController.loader.getController();
			if (btn_check.isSelected()) {
				for (int i = 0; i < mal.myalbumList.size(); i++) {
					mal.t_table.getTreeItem(i).getValue().getChBox1().setSelected(true);
				}
				
			}else {
				for (int i = 0; i < mal.myalbumList.size(); i++) {
					mal.t_table.getTreeItem(i).getValue().getChBox1().setSelected(false);
				}
			}
		}
		
		if (tabpane_main.getSelectionModel().isSelected(0)) {
			if (btn_check.isSelected()) {
				for (int i = 0; i < playList.size(); i++) {
					t_playListTable.getTreeItem(i).getValue().getCheckbox1().setSelected(true);
				}
			}else {
				for (int i = 0; i < playList.size(); i++) {
					t_playListTable.getTreeItem(i).getValue().getCheckbox1().setSelected(false);
				}
			}
		}
	}
	
	public void ready(int index) {
		if (index >= 0) {
			player.setMedia(playList.get(index).getMus_file());
			label_musicName.setText(playList.get(index).getMus_title());
			label_singerName.setText(playList.get(index).getSing_name());
			imgview_album.setImage(new Image(playList.get(index).getAlb_image()));
			label_lyrics.setText(playList.get(index).getMus_lyrics());
			player.mediaPlayer.setVolume(slider_volum.getValue()/100);
			player.Ready(Label_nowTime, Label_finalTime, slider_time);	
		}
	}
	
	public void close() {
		stage.setOnCloseRequest(e -> {
			if (playList.size() != 0) {
				if(player.mediaPlayer != null && player.getStatus() == Status.PLAYING ) {
					player.stop();
					player.mediaPlayer = null;
				}
			}
		});
	}
	
	
	public void playListTableSelet() {
		
		t_playListTable.getSelectionModel().selectedIndexProperty().addListener((observable,oldValue,newValue) -> {
			if (refreshFlag && player.mediaPlayer != null) {
				for (int i = 0; i < playList.size(); i++) {
					if (playList.get(i).getMus_no().equals(refreshMusNo)) {
						t_playListTable.getSelectionModel().select(i);
					}
				}
				refreshFlag=false;
			}else {
				mus_index = t_playListTable.getSelectionModel().getSelectedIndex();
				if(mus_index == -1) {
					return;
				}
				refreshMusNo = t_playListTable.getSelectionModel().getModelItem(mus_index).getValue().getMus_no();
				if (mus_index >= 0) {
					ready(mus_index);
					MusicHistoryVO vo = new MusicHistoryVO();
					String mus_no = t_playListTable.getSelectionModel().getSelectedItem().getValue().getMus_no();
					vo.setMus_no(mus_no);
					vo.setMem_id(LoginSession.session.getMem_id());
					try {
						imhs.historyInsert(vo);
					} catch (RemoteException e) {
						e.printStackTrace();
					}
					onPlay();
				}
			}
		});
	}
	
	public void tapPaneSelete() {
		
		tabpane_main.getSelectionModel().selectedIndexProperty().addListener(new InvalidationListener() {
			@Override
			public void invalidated(Observable observable) {
				if (tabpane_main.getSelectionModel().isSelected(1)) {
					try {
						btn_del.setVisible(false);
						AnchorPane pane = FXMLLoader.load(getClass().getResource("MyAlbum.fxml"));
						anchorpane_myalbum.getChildren().removeAll();
						anchorpane_myalbum.getChildren().setAll(pane);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}else {
					btn_del.setVisible(true);
					btn_add.setVisible(false);
				}
			}
		});
	}
	
	public void sliederMove() {
		slider_time.setOnMouseClicked(e->{
			player.musicSync(slider_time.getValue());
		});
	}
	
	public void endMusic() {
		player.mediaPlayer.setOnEndOfMedia(()-> {
			selectIndex(true);
		});
	}
	
	public void setVolum() {
		if (player.mediaPlayer != null) {
			slider_volum.valueProperty().addListener(observable -> {
					player.mediaPlayer.setVolume(slider_volum.getValue()/100);
			});
		}
	}
	
	public void reFresh() {
		try {
			refreshFlag = true;
			playList = FXCollections.observableArrayList(ipls.playlistSelect(LoginSession.session.getMem_id()));
			playListRoot = new RecursiveTreeItem<>(playList, RecursiveTreeObject::getChildren);
			t_playListTable.setRoot(playListRoot);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	public void randomSuffle() {
		randomIndex = null;
		if (randomIndex == null || player.mediaPlayer.getStatus() == Status.STOPPED) {
			randomIndex = new int[playList.size()];
			int temp;
			
			for (int i = 0; i < randomIndex.length; i++) {
				randomIndex[i] = i;
			}
			
			for (int i = 0; i < randomIndex.length * 3; i++) {
				int math = (int)(Math.random() * randomIndex.length);
				temp = randomIndex[0];
				randomIndex[0] = randomIndex[math];
				randomIndex[math] = temp;
			}
			count = 0;
		}
	}
	
	public void selectIndex(String mus_no) {
		int index = -1;
		for (int i = 0; i < playList.size(); i++) {
			if (playList.get(i).getMus_no().equals(mus_no) ) {
				index = i;
			}
		}

		if (playList.size() == 0) {
			t_playListTable.getSelectionModel().select(0);
		}else if (index > -1) {
			t_playListTable.getSelectionModel().select(index);
		}else {
			t_playListTable.getSelectionModel().select(playList.size()-1);
		}
	}
	
	public void selectIndex() {
		
		if (playList.size() == 0) {
			t_playListTable.getSelectionModel().select(0);
		}else {
			t_playListTable.getSelectionModel().select(playList.size()-1);
		}
	}
	
	public void selectIndex(boolean forward) {
		int index;
		if (forward) {
			index = t_playListTable.getSelectionModel().getSelectedIndex() + 1;
		} else {
			index = t_playListTable.getSelectionModel().getSelectedIndex() - 1;
		}
		
		
		if (retweenFlag && !randomFlag && forward) {
			
			if (index == playList.size()) {
				t_playListTable.getSelectionModel().select(0);
			}else {
				t_playListTable.getSelectionModel().select(index);
			}
			
		}else if (retweenFlag && !randomFlag && !forward){
			
			if (index < 0) {
				t_playListTable.getSelectionModel().select(playList.size()-1);
			}else {
				t_playListTable.getSelectionModel().select(index);
			}
			
		}else if(randomFlag && !retweenFlag) {
			randomSuffle();
			if (!(count >= randomIndex.length)) {
				t_playListTable.getSelectionModel().select(randomIndex[count]);
				count++;
			}else {
				player.stop();
			}
			
		}else if(randomFlag && !retweenFlag && !forward) {
			randomSuffle();
			
		}else if (retweenFlag && randomFlag) {
			t_playListTable.getSelectionModel().select((int)(Math.random() * playList.size()));
		} else if(!retweenFlag && !randomFlag && index < playList.size()) {
			t_playListTable.getSelectionModel().select(index);
		}
	}

	@FXML public void MouseExit() {
		if (slider_volum.isVisible()) {
			slider_volum.setVisible(false);
		}
	}

	@FXML public void mouseEnter() {
		slider_volum.setVisible(true);
		setVolum();
	}
		
		
	


	
}
