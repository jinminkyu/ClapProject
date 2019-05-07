package kr.or.ddit.clap.view.chartmenu.musiclist;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Map;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXDialog;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer.Status;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import kr.or.ddit.clap.main.GobackStack;
import kr.or.ddit.clap.main.LoginSession;
import kr.or.ddit.clap.main.MusicMainController;
import kr.or.ddit.clap.service.music.IMusicService;
import kr.or.ddit.clap.service.playlist.IPlayListService;
import kr.or.ddit.clap.view.chartmenu.dialog.MyAlbumDialogController;
import kr.or.ddit.clap.view.musicplayer.MusicPlayerController;
import kr.or.ddit.clap.view.musicvideo.MusicVideoController;
import kr.or.ddit.clap.view.singer.main.SingerMainController;
import kr.or.ddit.clap.view.singer.main.SingerMenuController;
import kr.or.ddit.clap.vo.music.MusicVO;
import kr.or.ddit.clap.vo.music.PlayListVO;

public class MusicList {
	private Registry reg;
	private IPlayListService ipls;
	private IMusicService ims;
	private ObservableList<JFXCheckBox> cbnList;
	private ObservableList<JFXButton> btnPlayList;
	private ObservableList<JFXButton> btnAddList;
	private ObservableList<JFXButton> btnPutList;
	private ObservableList<JFXButton> btnMovieList;
	private StackPane stackpane;
	private VBox mainBox;
	private MusicPlayerController mpc;
	private Stage stage = MusicMainController.movieStage;
	private double layoutX;
	private double layoutY;
	
	public MusicList() {
		
	}
	
	public MusicList( ObservableList<JFXCheckBox> cbnList, ObservableList<JFXButton> btnPlayList,
			 		  ObservableList<JFXButton> btnAddList, ObservableList<JFXButton> btnPutList,
			 		  ObservableList<JFXButton> btnMovieList, VBox mainBox , StackPane stackpane) {

		super();
		this.cbnList = cbnList;
		this.btnPlayList = btnPlayList;
		this.btnAddList = btnAddList;
		this.btnPutList = btnPutList;
		this.btnMovieList = btnMovieList;
		this.stackpane = stackpane;
		this.mainBox = mainBox;
		
		try {
			reg = LocateRegistry.getRegistry("localhost", 8888);
			ipls = (IPlayListService) reg.lookup("playlist");
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	
	public MusicList( ObservableList<JFXButton> btnAddList, VBox mainBox, StackPane stackpane) {

		super();
		this.btnAddList = btnAddList;
		this.mainBox = mainBox;
		this.stackpane = stackpane;
		
		try {
			reg = LocateRegistry.getRegistry("localhost", 8888);
			ipls = (IPlayListService) reg.lookup("playlist");
			ims = (IMusicService) reg.lookup("music");
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
	
	}
	
	// 재생 버튼 클릭시 이벤트
	private void btnPlayClick() {
		if (LoginSession.session == null) {
			return;
		}
		for (int i = 0; i < btnPlayList.size(); i++) {
			btnPlayList.get(i).setOnAction(e->{
				JFXButton btn_PlayMy = (JFXButton) e.getSource();
				
				PlayListVO vo = new PlayListVO();
				vo.setMus_no(btn_PlayMy.getId());
				vo.setMem_id(LoginSession.session.getMem_id());
				playListInsert(vo);
				
				if (!MusicMainController.musicplayer.isShowing()) {
					try {
						MusicMainController.playerLoad = new FXMLLoader(getClass().getResource("../../musicplayer/MusicPlayer.fxml"));
						AnchorPane root = MusicMainController.playerLoad.load();
						Scene scene = new Scene(root);
						MusicMainController.musicplayer.setTitle("MusicPlayer");
						MusicMainController.musicplayer.setScene(scene);
						MusicMainController.musicplayer.show();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
				mpc = MusicMainController.playerLoad.getController();
				mpc.reFresh();
				mpc.selectIndex(vo.getMus_no());
			});
		}
	}
	
	// 추가 버튼 클릭시 이벤트
	private void btnAddClick() {
		if (LoginSession.session == null) {
			return;
		}
		for (int i = 0; i < btnAddList.size(); i++) {
			btnAddList.get(i).setOnAction(e->{
				JFXButton btn_AddMy = (JFXButton) e.getSource();
				
				if (btn_AddMy.getAccessibleText() != null) {
					try {
						ObservableList<String> list = 
								FXCollections.observableArrayList(ims.albumMusNoSelect(btn_AddMy.getAccessibleText()));
						for (int j = 0; j < list.size(); j++) {
							PlayListVO vo = new PlayListVO();
							vo.setMus_no(list.get(j));
							vo.setMem_id(LoginSession.session.getMem_id());
							playListInsert(vo);
						}
					} catch (RemoteException e1) {
						e1.printStackTrace();
					}
				}else {
					PlayListVO vo = new PlayListVO();
					vo.setMus_no(btn_AddMy.getId());
					vo.setMem_id(LoginSession.session.getMem_id());
					playListInsert(vo);
				}
				
				
				
				if (MusicMainController.musicplayer.isShowing()) {
					mpc = MusicMainController.playerLoad.getController();
					mpc.reFresh();
				}
			});
		}
	}
	
	private void playListInsert(PlayListVO vo) {
		try {
			ipls.playlistInsert(vo);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	// 담기 버튼 클릭시 이벤트
	private void btnPutClick() {
		
		EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() { 
	         @Override 
	         public void handle(MouseEvent e) { 
	        	 
	        	layoutX =  e.getX() - (1005/2 - 150);
	        	layoutY = e.getY() - (mainBox.getHeight() / 2);
	        	if (layoutY >= mainBox.getHeight() - (mainBox.getHeight() / 1.1)) { 
	        		layoutY += -100;
	        	}else {
	        		layoutY += 110;
	        	}
	         } 
	     };  
	     this.stackpane.addEventFilter(MouseEvent.MOUSE_MOVED, eventHandler); 
		
		if (LoginSession.session == null) {
			return;
		}
		for (int i = 0; i < btnPutList.size(); i++) {
			btnPutList.get(i).setOnAction(e->{
				
				JFXButton btn_PutMy = (JFXButton) e.getSource();
				MyAlbumDialogController.mus_no.clear();
				MyAlbumDialogController.mus_no.add(btn_PutMy.getId());
				
				myAlbumdialog();
			});
		}
	}
	
	// 뮤비 버튼 클릭시 이벤트
	private void btnMovieClick() {
		if (LoginSession.session == null) {
			return;
		}
		for (int i = 0; i < btnMovieList.size(); i++) {
			btnMovieList.get(i).setOnAction(e->{
				JFXButton btn_MovieMy = (JFXButton) e.getSource();
				try {
					if (stage.isShowing()) {
						if (MusicVideoController.mediaPlayer.getStatus() == Status.PLAYING) {
							MusicVideoController.mediaPlayer.stop();
						}
						stage.close();
					}
					MusicVideoController.path = btn_MovieMy.getId();
					MusicVideoController.musName = btn_MovieMy.getAccessibleText();
					MusicVideoController.singerName = btn_MovieMy.getAccessibleHelp();
					AnchorPane pane = FXMLLoader.load(getClass().getResource("../../musicvideo/MusicVideo.fxml"));
					Scene scene = new Scene(pane);
					stage.setScene(scene);
					stage.setTitle("MusicVideo");
					stage.show();
					
					stage.setOnCloseRequest(e2->{
						if (MusicVideoController.mediaPlayer.getStatus() == Status.PLAYING) {
							MusicVideoController.mediaPlayer.stop();
						}
					});
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			});
		}
	}
	
	public void myAlbumdialog() {
		StackPane content;
		try {
			content = FXMLLoader.load(getClass().getResource("../dialog/MyAlbumDialog.fxml"));
			JFXDialog dialog = new JFXDialog(stackpane, content, JFXDialog.DialogTransition.CENTER);
			dialog.setTranslateX(layoutX);
			dialog.setTranslateY(layoutY);
			dialog.setBackground(Background.EMPTY);
			dialog.show();
			
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	public void musicList(ObservableList<Map> list) {
		
		VBox vbox = new VBox();
		
		cbnList.clear();
		btnPlayList.clear();
		btnAddList.clear();
		btnPutList.clear();
		btnMovieList.clear();
		
		for (int i = 0; i < list.size(); i++) {
			// 파란색 라인 HBox 
			HBox h_Line = new HBox();
			vbox.setMargin(h_Line, new Insets(0,0,0,90));
			h_Line.setMaxWidth(710);
			h_Line.setPrefHeight(3);
			if (i == 0) {
				h_Line.setStyle("-fx-background-color: #090948;");
			}else {
				h_Line.setStyle("-fx-background-color: #e1e1ef;");
			}
			//h_Line.alignmentProperty("")
			
			
			// 전체 HBox 
			HBox h_Table = new HBox();
			vbox.setMargin(h_Table, new Insets(0,0,0,80));
			h_Table.setAlignment(Pos.CENTER_LEFT);
			h_Table.setPadding(new Insets(10,10,10,10));
			h_Table.setSpacing(10);
			
				JFXCheckBox chb_Check = new JFXCheckBox();
				chb_Check.setPrefWidth(30);
				chb_Check.setPrefHeight(15);
				chb_Check.setCheckedColor(Color.valueOf("#9c0000"));
				chb_Check.setId(list.get(i).get("MUS_NO").toString()); //키값: 컬럼명->대문자로
				cbnList.add(chb_Check);
				
				// 곡제목 및 아티스트명을 담는 VBox
				VBox v_rank = new VBox();
				v_rank.setAlignment(Pos.CENTER_LEFT);
				v_rank.setSpacing(1);

					// 순위를 나타내는 Label
					Label la_Rank = new Label();
					la_Rank.setFont(Font.font("-윤고딕350", 18));
					la_Rank.setMinWidth(23);
					la_Rank.setMinHeight(23);
					la_Rank.setText("" + (i+1));
					
					// 전날 순위에 대한 변동순위를 나타내는 Label
					Label la_PreRank = new Label();
					la_PreRank.setFont(Font.font("-윤고딕350", 12));
					la_PreRank.setMinWidth(30);
					la_PreRank.setMinHeight(23);
					int beforRank = 0;
					if(list.get(i).get("MUS_BEFOR_RANK") != null) {
						beforRank = Integer.parseInt(list.get(i).get("MUS_BEFOR_RANK").toString());
					}
					int rank = i+1;
					String str_beforRank = "";
					FontAwesomeIcon rankImg = new FontAwesomeIcon();
					rankImg.setSize("15");
					
					
					if (rank == beforRank) {
						rankImg.setIconName("MINUS");
						rankImg.setFill(Color.valueOf("#A4A4A4"));
						la_PreRank.setGraphic(rankImg);
						
					} else if (rank <= beforRank) {
						beforRank -= rank;
						str_beforRank = Integer.toString(beforRank);
						la_PreRank.setTextFill(Color.valueOf("#FE5C62"));
						rankImg.setIconName("CARET_UP");
						rankImg.setFill(Color.valueOf("#FE5C62"));
						la_PreRank.setGraphic(rankImg);
						
					} else if (rank >= beforRank && beforRank != 0){
						rank -= beforRank;
						str_beforRank = Integer.toString(rank);
						la_PreRank.setTextFill(Color.valueOf("#609ACF"));
						rankImg.setIconName("CARET_DOWN");
						rankImg.setFill(Color.valueOf("#609ACF"));
						la_PreRank.setGraphic(rankImg);
			
					} else if (beforRank == 0){
						str_beforRank = "new";
						la_PreRank.setTextFill(Color.valueOf("#609ACF"));
						
					}
					la_PreRank.setText(str_beforRank);
				
				// 앨범이미지를 표시하는 ImageView
				ImageView iv_Album = new ImageView();
				Image img_Path = new Image(list.get(i).get("ALB_IMAGE").toString());
				iv_Album.setImage(img_Path);
				iv_Album.setFitWidth(50);
				iv_Album.setFitHeight(50);
				iv_Album.setId(list.get(i).get("ALB_NO").toString());
				iv_Album.setAccessibleText(list.get(i).get("SING_NO").toString());
				iv_Album.setOnMouseClicked(e -> {
					ImageView iv = (ImageView)e.getSource();
					SingerMenuController.menuCount = 1;
					SingerMenuController.albumNo = iv.getId();
					SingerMainController.singerNo = iv.getAccessibleText();
				  try {
				         Parent page = FXMLLoader.load(getClass().getResource("../../singer/main/SingerMenu.fxml")); 
				                                    
				         String temp_path = (getClass().getResource("../../singer/main/SingerMenu.fxml")).getPath();
						 String path = temp_path.substring(1, temp_path.length()); // 현재화면 절대경로

						 GobackStack.goURL(path);
				         
				         MusicMainController.secondPane.getChildren().removeAll();
				         MusicMainController.secondPane.getChildren().setAll(page);
				      } catch (IOException e1) {
				         e1.printStackTrace();
				      }
				});
					
				// 곡제목 및 아티스트명을 담는 VBox
				VBox v_MusicInfo = new VBox();
				v_MusicInfo.setAlignment(Pos.CENTER_LEFT);
				v_MusicInfo.setSpacing(5);
				v_MusicInfo.setPrefWidth(300);

					// 곡제목을 담당하는 라벨
					Label la_MusicName = new Label();
					la_MusicName.setFont(Font.font("-윤고딕350", 12));
					la_MusicName.setText(list.get(i).get("MUS_TITLE").toString());
					la_MusicName.setPrefWidth(300);
					la_MusicName.setId(list.get(i).get("ALB_NO").toString());
					la_MusicName.setAccessibleText(list.get(i).get("SING_NO").toString());
					la_MusicName.setAccessibleHelp((list.get(i).get("MUS_NO").toString()));
					la_MusicName.setOnMouseClicked(e -> {
						Label la = (Label)e.getSource();
						SingerMenuController.menuCount = 2;
						SingerMenuController.albumNo = la.getId();
						SingerMainController.singerNo = la.getAccessibleText();
						SingerMenuController.musicNo = la.getAccessibleHelp();
						  try {
						         Parent pane = FXMLLoader.load(getClass().getResource("../../singer/main/SingerMenu.fxml")); 
						         
						         String temp_path = (getClass().getResource("../../singer/main/SingerMenu.fxml")).getPath();
								 String path = temp_path.substring(1, temp_path.length()); // 현재화면 절대경로

								 GobackStack.goURL(path);
						         
						         MusicMainController.secondPane.getChildren().removeAll();
						         MusicMainController.secondPane.getChildren().setAll(pane);
						      } catch (IOException e1) {
						         e1.printStackTrace();
						      }
					});
					
					// 가수이름을 담당하는 라벨
					Label la_SingerName = new Label();
					la_SingerName.setFont(Font.font("-윤고딕330", 12));
					la_SingerName.setText(list.get(i).get("SING_NAME").toString());
					la_SingerName.setPrefWidth(300);
					la_SingerName.setId(list.get(i).get("ALB_NO").toString());
					la_SingerName.setAccessibleText(list.get(i).get("SING_NO").toString());
					la_SingerName.setOnMouseClicked(e -> {
						Label la = (Label)e.getSource();
						SingerMenuController.menuCount = 0;
						SingerMenuController.albumNo = la.getId();
						SingerMainController.singerNo = la.getAccessibleText();
						  try {
						         Parent pane = FXMLLoader.load(getClass().getResource("../../singer/main/SingerMenu.fxml")); 
		         
						         String temp_path = (getClass().getResource("../../singer/main/SingerMenu.fxml")).getPath();
								 String path = temp_path.substring(1, temp_path.length()); // 현재화면 절대경로

								 GobackStack.goURL(path);
						         

						         MusicMainController.secondPane.getChildren().removeAll();
						         MusicMainController.secondPane.getChildren().setAll(pane);
						      } catch (IOException e1) {
						         e1.printStackTrace();
						      }
					});
						
				// 듣기 버튼
				JFXButton btn_Play = new JFXButton();
				h_Table.setMargin(btn_Play, new Insets(0,0,0,40));
				btn_Play.setRipplerFill(Color.valueOf("#9c0000"));
				btn_Play.setAlignment(Pos.CENTER_LEFT);
				btn_Play.setPrefWidth(30);
				btn_Play.setPrefHeight(46);
				btn_Play.setId(list.get(i).get("MUS_NO").toString());
					
					// 듣기 아이콘
					FontAwesomeIcon icon_Play = new FontAwesomeIcon();
					icon_Play.setIconName("PLAY_CIRCLE_ALT");
					icon_Play.setFill(Color.valueOf("#9c0000"));
					icon_Play.setSize("30");
					btn_Play.setGraphic(icon_Play);
					btnPlayList.add(btn_Play);
					
				// 추가 버튼
				JFXButton btn_Add = new JFXButton();
				btn_Add.setRipplerFill(Color.valueOf("#9c0000"));
				btn_Add.setAlignment(Pos.CENTER_LEFT);
				btn_Add.setPrefWidth(30);
				btn_Add.setPrefHeight(46);
				btn_Add.setId(list.get(i).get("MUS_NO").toString());
					
					// 추가 아이콘
					FontAwesomeIcon icon_Add = new FontAwesomeIcon();
					icon_Add.setIconName("PLUS");
					icon_Add.setFill(Color.valueOf("#9c0000"));
					icon_Add.setSize("30");
					btn_Add.setGraphic(icon_Add);
					btnAddList.add(btn_Add);
					
				// 담기 버튼
				JFXButton btn_Put = new JFXButton();
				btn_Put.setRipplerFill(Color.valueOf("#9c0000"));
				btn_Put.setAlignment(Pos.CENTER_LEFT);
				btn_Put.setPrefWidth(30);
				btn_Put.setPrefHeight(46);
				btn_Put.setId(list.get(i).get("MUS_NO").toString());
					
					// 담기 아이콘
					FontAwesomeIcon icon_Put = new FontAwesomeIcon();
					icon_Put.setIconName("FOLDER_ALT");
					icon_Put.setFill(Color.valueOf("#9c0000"));
					icon_Put.setSize("30");
					btn_Put.setGraphic(icon_Put);
					btnPutList.add(btn_Put);
				
				// 뮤비 버튼
				JFXButton btn_Movie = new JFXButton();
				btn_Movie.setRipplerFill(Color.valueOf("#9c0000"));
				btn_Movie.setAlignment(Pos.CENTER_LEFT);
				btn_Movie.setPrefWidth(30);
				btn_Movie.setPrefHeight(46);
				
				if (list.get(i).get("MUS_MVFILE") == null) {
					btn_Movie.setDisable(true);
				}else {
					btn_Movie.setId(list.get(i).get("MUS_MVFILE").toString());
					btn_Movie.setAccessibleText(list.get(i).get("MUS_TITLE").toString());
					btn_Movie.setAccessibleHelp(list.get(i).get("SING_NAME").toString());
				}
					// 뮤비 아이콘
					FontAwesomeIcon icon_Movie = new FontAwesomeIcon();
					icon_Movie.setIconName("VIMEO_SQUARE");
					icon_Movie.setFill(Color.valueOf("#9c0000"));
					icon_Movie.setSize("30");
					btn_Movie.setGraphic(icon_Movie);
					btnMovieList.add(btn_Movie);
					
				v_rank.getChildren().addAll(la_Rank,la_PreRank);
				v_MusicInfo.getChildren().addAll(la_MusicName,la_SingerName);
			h_Table.getChildren().addAll(chb_Check,v_rank,iv_Album,v_MusicInfo,btn_Play,btn_Add,btn_Put,btn_Movie);
			vbox.getChildren().addAll(h_Line,h_Table);
			
		}
		
		if (mainBox.getChildren().size() == 4) {
			mainBox.getChildren().remove(3);
		}
		//-----------------------
		mainBox.getChildren().add(vbox);
		
		btnPlayClick();
		btnAddClick();
		btnPutClick();
		btnMovieClick();
		
	}
	
	//윤한수 VO용
	public void musicList_VO(ObservableList<MusicVO> list) {
		
		VBox vbox = new VBox();
		
		cbnList.clear();
		btnPlayList.clear();
		btnAddList.clear();
		btnPutList.clear();
		btnMovieList.clear();
		
		for (int i = 0; i < list.size(); i++) {
			// 파란색 라인 HBox 
			HBox h_Line = new HBox();
			vbox.setMargin(h_Line, new Insets(0,0,0,0));
			h_Line.setMaxWidth(710);
			h_Line.setPrefHeight(3);
			if (i == 0) {
				h_Line.setStyle("-fx-background-color: #090948;");
			}else {
				h_Line.setStyle("-fx-background-color: #e1e1ef;");
			}
			//h_Line.alignmentProperty("")
			
			
			// 전체 HBox 
			HBox h_Table = new HBox();
			vbox.setMargin(h_Table, new Insets(0,0,0,0));
			h_Table.setAlignment(Pos.CENTER_LEFT);
			h_Table.setPadding(new Insets(10,10,10,10));
			h_Table.setSpacing(10);
			
			JFXCheckBox chb_Check = new JFXCheckBox();
			chb_Check.setPrefWidth(30);
			chb_Check.setPrefHeight(15);
			chb_Check.setCheckedColor(Color.valueOf("#9c0000"));
			chb_Check.setId(list.get(i).getMus_no()); //키값: 컬럼명->대문자로
			cbnList.add(chb_Check);
			
			// 곡제목 및 아티스트명을 담는 VBox
			VBox v_rank = new VBox();
			v_rank.setAlignment(Pos.CENTER_LEFT);
			v_rank.setSpacing(1);
			
			// 순위를 나타내는 Label
			Label la_Rank = new Label();
			la_Rank.setFont(Font.font("-윤고딕350", 18));
			la_Rank.setPrefWidth(23);
			la_Rank.setPrefHeight(23);
			la_Rank.setText("" + (i+1));
			
			/*// 전날 순위에 대한 변동순위를 나타내는 Label  --> 순위 안나타냄
			Label la_PreRank = new Label();
			la_PreRank.setFont(Font.font("-윤고딕350", 12));
			la_PreRank.setPrefWidth(30);
			la_PreRank.setPrefHeight(23);
			int beforRank = 0;
			if(list.get(i).get("MUS_BEFOR_RANK") != null) {
				beforRank = Integer.parseInt(list.get(i).get("MUS_BEFOR_RANK").toString());
			}
			int rank = i+1;
			String str_beforRank = "";
			FontAwesomeIcon rankImg = new FontAwesomeIcon();
			rankImg.setSize("15");
			
			
			if (rank == beforRank) {
				rankImg.setIconName("MINUS");
				rankImg.setFill(Color.valueOf("#A4A4A4"));
				la_PreRank.setGraphic(rankImg);
				
			} else if (rank <= beforRank) {
				beforRank -= rank;
				str_beforRank = Integer.toString(beforRank);
				la_PreRank.setTextFill(Color.valueOf("#FE5C62"));
				rankImg.setIconName("CARET_UP");
				rankImg.setFill(Color.valueOf("#FE5C62"));
				la_PreRank.setGraphic(rankImg);
				
			} else if (rank >= beforRank && beforRank != 0){
				rank -= beforRank;
				str_beforRank = Integer.toString(rank);
				la_PreRank.setTextFill(Color.valueOf("#609ACF"));
				rankImg.setIconName("CARET_DOWN");
				rankImg.setFill(Color.valueOf("#609ACF"));
				la_PreRank.setGraphic(rankImg);
				
			} else if (beforRank == 0){
				str_beforRank = "new";
				la_PreRank.setTextFill(Color.valueOf("#609ACF"));
				
			}
			la_PreRank.setText(str_beforRank);
			*/
			// 앨범이미지를 표시하는 ImageView
			ImageView iv_Album = new ImageView();
			Image img_Path = new Image(list.get(i).getAlb_image());
			iv_Album.setImage(img_Path);
			iv_Album.setFitWidth(50);
			iv_Album.setFitHeight(50);
			
			// 곡제목 및 아티스트명을 담는 VBox
			VBox v_MusicInfo = new VBox();
			v_MusicInfo.setAlignment(Pos.CENTER_LEFT);
			v_MusicInfo.setSpacing(5);
			v_MusicInfo.setPrefWidth(300);
			
			// 곡제목을 담당하는 라벨
			Label la_MusicName = new Label();
			la_MusicName.setFont(Font.font("-윤고딕350", 12));
			la_MusicName.setText(list.get(i).getMus_title());
			la_MusicName.setPrefWidth(300);
			
			// 가수이름을 담당하는 라벨
			Label la_SingerName = new Label();
			la_SingerName.setFont(Font.font("-윤고딕330", 12));
			la_SingerName.setText(list.get(i).getSing_name());
			la_SingerName.setPrefWidth(300);
			
			// 듣기 버튼
			JFXButton btn_Play = new JFXButton();
			h_Table.setMargin(btn_Play, new Insets(0,0,0,40));
			btn_Play.setRipplerFill(Color.valueOf("#9c0000"));
			btn_Play.setAlignment(Pos.CENTER_LEFT);
			btn_Play.setPrefWidth(30);
			btn_Play.setPrefHeight(46);
			btn_Play.setId(list.get(i).getMus_no());
			
			// 듣기 아이콘
			FontAwesomeIcon icon_Play = new FontAwesomeIcon();
			icon_Play.setIconName("PLAY_CIRCLE_ALT");
			icon_Play.setFill(Color.valueOf("#9c0000"));
			icon_Play.setSize("30");
			btn_Play.setGraphic(icon_Play);
			btnPlayList.add(btn_Play);
			
			// 추가 버튼
			JFXButton btn_Add = new JFXButton();
			btn_Add.setRipplerFill(Color.valueOf("#9c0000"));
			btn_Add.setAlignment(Pos.CENTER_LEFT);
			btn_Add.setPrefWidth(30);
			btn_Add.setPrefHeight(46);
			btn_Add.setId(list.get(i).getMus_no());
			
			// 추가 아이콘
			FontAwesomeIcon icon_Add = new FontAwesomeIcon();
			icon_Add.setIconName("PLUS");
			icon_Add.setFill(Color.valueOf("#9c0000"));
			icon_Add.setSize("30");
			btn_Add.setGraphic(icon_Add);
			btnAddList.add(btn_Add);
			
			// 담기 버튼
			JFXButton btn_Put = new JFXButton();
			btn_Put.setRipplerFill(Color.valueOf("#9c0000"));
			btn_Put.setAlignment(Pos.CENTER_LEFT);
			btn_Put.setPrefWidth(30);
			btn_Put.setPrefHeight(46);
			btn_Put.setId(list.get(i).getMus_no());
			
			// 담기 아이콘
			FontAwesomeIcon icon_Put = new FontAwesomeIcon();
			icon_Put.setIconName("FOLDER_ALT");
			icon_Put.setFill(Color.valueOf("#9c0000"));
			icon_Put.setSize("30");
			btn_Put.setGraphic(icon_Put);
			btnPutList.add(btn_Put);
			
			// 뮤비 버튼
			JFXButton btn_Movie = new JFXButton();
			btn_Movie.setRipplerFill(Color.valueOf("#9c0000"));
			btn_Movie.setAlignment(Pos.CENTER_LEFT);
			btn_Movie.setPrefWidth(30);
			btn_Movie.setPrefHeight(46);
			
			if (list.get(i).getMus_mvfile() == null) {
				btn_Movie.setDisable(true);
			}else {
				btn_Movie.setId(list.get(i).getMus_mvfile());
				btn_Movie.setAccessibleText(list.get(i).getMus_title());
				btn_Movie.setAccessibleHelp(list.get(i).getSing_name());
			}
			// 뮤비 아이콘
			FontAwesomeIcon icon_Movie = new FontAwesomeIcon();
			icon_Movie.setIconName("VIMEO_SQUARE");
			icon_Movie.setFill(Color.valueOf("#9c0000"));
			icon_Movie.setSize("30");
			btn_Movie.setGraphic(icon_Movie);
			btnMovieList.add(btn_Movie);
			
			v_rank.getChildren().addAll(la_Rank);
			v_MusicInfo.getChildren().addAll(la_MusicName,la_SingerName);
			h_Table.getChildren().addAll(chb_Check,v_rank,iv_Album,v_MusicInfo,btn_Play,btn_Add,btn_Put,btn_Movie);
			vbox.getChildren().addAll(h_Line,h_Table);
			
		}
		
		if (mainBox.getChildren().size() == 4) {
			mainBox.getChildren().remove(3);
		}
		//-----------------------
		mainBox.getChildren().add(vbox);
		
		btnPlayClick();
		btnAddClick();
		btnPutClick();
		btnMovieClick();
		
	}
	
public VBox pagenation(ObservableList<Map> list, int itemsForPage, int page) {
		
		VBox vbox = new VBox();
		
		cbnList.clear();
		btnPlayList.clear();
		btnAddList.clear();
		btnPutList.clear();
		btnMovieList.clear();
		
		int size = Math.min(page + itemsForPage, list.size());
		
		for (int i = page; i < size; i++) {
			// 파란색 라인 HBox 
			HBox h_Line = new HBox();
			vbox.setMargin(h_Line, new Insets(0,0,0,90));
			h_Line.setMaxWidth(710);
			h_Line.setPrefHeight(3);
			if (i%5 == 0) {
				h_Line.setStyle("-fx-background-color: #090948;");
			}else {
				h_Line.setStyle("-fx-background-color: #e1e1ef;");
			}
			//h_Line.alignmentProperty("")
			
			
			// 전체 HBox 
			HBox h_Table = new HBox();
			vbox.setMargin(h_Table, new Insets(0,0,0,80));
			h_Table.setAlignment(Pos.CENTER_LEFT);
			h_Table.setPadding(new Insets(10,10,10,10));
			h_Table.setSpacing(6);
			
				JFXCheckBox chb_Check = new JFXCheckBox();
				chb_Check.setPrefWidth(30);
				chb_Check.setPrefHeight(15);
				chb_Check.setCheckedColor(Color.valueOf("#9c0000"));
				chb_Check.setId(list.get(i).get("MUS_NO").toString()); //키값: 컬럼명->대문자로
				cbnList.add(chb_Check);
				
				// 곡제목 및 아티스트명을 담는 VBox
				VBox v_rank = new VBox();
				v_rank.setAlignment(Pos.CENTER_LEFT);
				v_rank.setSpacing(1);

					// 순위를 나타내는 Label
					Label la_Rank = new Label();
					la_Rank.setFont(Font.font("-윤고딕350", 18));
					la_Rank.setPrefWidth(23);
					la_Rank.setPrefHeight(23);
					la_Rank.setText("" + (i+1));
					
					// 전날 순위에 대한 변동순위를 나타내는 Label
					Label la_PreRank = new Label();
					la_PreRank.setFont(Font.font("-윤고딕350", 12));
					la_PreRank.setPrefWidth(30);
					la_PreRank.setPrefHeight(23);
					
					String str_beforRank = "";
					FontAwesomeIcon rankImg = new FontAwesomeIcon();
					rankImg.setSize("15");
					rankImg.setIconName("MINUS");
					rankImg.setFill(Color.valueOf("#A4A4A4"));
					la_PreRank.setGraphic(rankImg);
					la_PreRank.setText(str_beforRank);
				
				// 앨범이미지를 표시하는 ImageView
				ImageView iv_Album = new ImageView();
				Image img_Path = new Image(list.get(i).get("ALB_IMAGE").toString());
				iv_Album.setImage(img_Path);
				iv_Album.setFitWidth(50);
				iv_Album.setFitHeight(50);
				iv_Album.setId(list.get(i).get("ALB_NO").toString());
				iv_Album.setAccessibleText(list.get(i).get("SING_NO").toString());
				iv_Album.setOnMouseClicked(e -> {
					ImageView iv = (ImageView)e.getSource();
					SingerMenuController.menuCount = 1;
					SingerMenuController.albumNo = iv.getId();
					SingerMainController.singerNo = iv.getAccessibleText();
					  try {
					         Parent pane = FXMLLoader.load(getClass().getResource("../../singer/main/SingerMenu.fxml")); 
					         
					         String temp_path = (getClass().getResource("../../singer/main/SingerMenu.fxml")).getPath();
							 String path = temp_path.substring(1, temp_path.length()); // 현재화면 절대경로

							 GobackStack.goURL(path);
					         
					         MusicMainController.secondPane.getChildren().removeAll();
					         MusicMainController.secondPane.getChildren().setAll(pane);
					      } catch (IOException e1) {
					         e1.printStackTrace();
					      }
				});
					
				// 곡제목 및 아티스트명을 담는 VBox
				VBox v_MusicInfo = new VBox();
				v_MusicInfo.setAlignment(Pos.CENTER_LEFT);
				v_MusicInfo.setSpacing(5);
				v_MusicInfo.setPrefWidth(300);

					// 곡제목을 담당하는 라벨
					Label la_MusicName = new Label();
					la_MusicName.setFont(Font.font("-윤고딕350", 12));
					la_MusicName.setText(list.get(i).get("MUS_TITLE").toString());
					la_MusicName.setPrefWidth(300);
					la_MusicName.setId(list.get(i).get("ALB_NO").toString());
					la_MusicName.setAccessibleText(list.get(i).get("SING_NO").toString());
					la_MusicName.setAccessibleHelp((list.get(i).get("MUS_NO").toString()));
					la_MusicName.setOnMouseClicked(e -> {
						Label la = (Label)e.getSource();
						SingerMenuController.menuCount = 2;
						SingerMenuController.albumNo = la.getId();
						SingerMainController.singerNo = la.getAccessibleText();
						SingerMenuController.musicNo = la_MusicName.getAccessibleHelp();
						  try {
						         Parent pane = FXMLLoader.load(getClass().getResource("../../singer/main/SingerMenu.fxml")); 
						         
						         String temp_path = (getClass().getResource("../../singer/main/SingerMenu.fxml")).getPath();
								 String path = temp_path.substring(1, temp_path.length()); // 현재화면 절대경로

								 GobackStack.goURL(path);
						         
						         MusicMainController.secondPane.getChildren().removeAll();
						         MusicMainController.secondPane.getChildren().setAll(pane);
						      } catch (IOException e1) {
						         e1.printStackTrace();
						      }
					});
					
					
					// 가수이름을 담당하는 라벨
					Label la_SingerName = new Label();
					la_SingerName.setFont(Font.font("-윤고딕330", 12));
					la_SingerName.setText(list.get(i).get("SING_NAME").toString());
					la_SingerName.setPrefWidth(300);
					la_SingerName.setId(list.get(i).get("ALB_NO").toString());
					la_SingerName.setAccessibleText(list.get(i).get("SING_NO").toString());
					la_SingerName.setOnMouseClicked(e -> {
						Label la = (Label)e.getSource();
						SingerMenuController.menuCount = 0;
						SingerMenuController.albumNo = la.getId();
						SingerMainController.singerNo = la.getAccessibleText();
						  try {
						         Parent pane = FXMLLoader.load(getClass().getResource("../../singer/main/SingerMenu.fxml")); 
						         
						         String temp_path = (getClass().getResource("../../singer/main/SingerMenu.fxml")).getPath();
								 String path = temp_path.substring(1, temp_path.length()); // 현재화면 절대경로

								 GobackStack.goURL(path);
						         
						         MusicMainController.secondPane.getChildren().removeAll();
						         MusicMainController.secondPane.getChildren().setAll(pane);
						      } catch (IOException e1) {
						         e1.printStackTrace();
						      }
					});
						
				// 듣기 버튼
				JFXButton btn_Play = new JFXButton();
				h_Table.setMargin(btn_Play, new Insets(0,0,0,54));
				btn_Play.setRipplerFill(Color.valueOf("#9c0000"));
				btn_Play.setAlignment(Pos.CENTER_LEFT);
				btn_Play.setPrefWidth(30);
				btn_Play.setPrefHeight(46);
				btn_Play.setId(list.get(i).get("MUS_NO").toString());
					
					// 듣기 아이콘
					FontAwesomeIcon icon_Play = new FontAwesomeIcon();
					icon_Play.setIconName("PLAY_CIRCLE_ALT");
					icon_Play.setFill(Color.valueOf("#9c0000"));
					icon_Play.setSize("30");
					btn_Play.setGraphic(icon_Play);
					btnPlayList.add(btn_Play);
					
				// 추가 버튼
				JFXButton btn_Add = new JFXButton();
				btn_Add.setRipplerFill(Color.valueOf("#9c0000"));
				btn_Add.setAlignment(Pos.CENTER_LEFT);
				btn_Add.setPrefWidth(30);
				btn_Add.setPrefHeight(46);
				btn_Add.setId(list.get(i).get("MUS_NO").toString());
					
					// 추가 아이콘
					FontAwesomeIcon icon_Add = new FontAwesomeIcon();
					icon_Add.setIconName("PLUS");
					icon_Add.setFill(Color.valueOf("#9c0000"));
					icon_Add.setSize("30");
					btn_Add.setGraphic(icon_Add);
					btnAddList.add(btn_Add);
					
				// 담기 버튼
				JFXButton btn_Put = new JFXButton();
				btn_Put.setRipplerFill(Color.valueOf("#9c0000"));
				btn_Put.setAlignment(Pos.CENTER_LEFT);
				btn_Put.setPrefWidth(30);
				btn_Put.setPrefHeight(46);
				btn_Put.setId(list.get(i).get("MUS_NO").toString());
					
					// 담기 아이콘
					FontAwesomeIcon icon_Put = new FontAwesomeIcon();
					icon_Put.setIconName("FOLDER_ALT");
					icon_Put.setFill(Color.valueOf("#9c0000"));
					icon_Put.setSize("30");
					btn_Put.setGraphic(icon_Put);
					btnPutList.add(btn_Put);
				
				// 뮤비 버튼
				JFXButton btn_Movie = new JFXButton();
				btn_Movie.setRipplerFill(Color.valueOf("#9c0000"));
				btn_Movie.setAlignment(Pos.CENTER_LEFT);
				btn_Movie.setPrefWidth(30);
				btn_Movie.setPrefHeight(46);
				if (list.get(i).get("MUS_MVFILE") == null) {
					btn_Movie.setDisable(true);
				}else {
					btn_Movie.setId(list.get(i).get("MUS_MVFILE").toString());
					btn_Movie.setAccessibleText(list.get(i).get("MUS_TITLE").toString());
					btn_Movie.setAccessibleHelp(list.get(i).get("SING_NAME").toString());
				}
					
					// 뮤비 아이콘
					FontAwesomeIcon icon_Movie = new FontAwesomeIcon();
					icon_Movie.setIconName("VIMEO_SQUARE");
					icon_Movie.setFill(Color.valueOf("#9c0000"));
					icon_Movie.setSize("30");
					btn_Movie.setGraphic(icon_Movie);
					btnMovieList.add(btn_Movie);
					
				v_rank.getChildren().addAll(la_Rank,la_PreRank);
				v_MusicInfo.getChildren().addAll(la_MusicName,la_SingerName);
			h_Table.getChildren().addAll(chb_Check,v_rank,iv_Album,v_MusicInfo,btn_Play,btn_Add,btn_Put,btn_Movie);
			vbox.getChildren().addAll(h_Line,h_Table);
			
		}
		
		btnPlayClick();
		btnAddClick();
		btnPutClick();
		btnMovieClick();
		
		return vbox;
		
	}

	public VBox albumList(ObservableList<Map> list, int itemsForPage, int page) {
		VBox vbox = new VBox();
		btnAddList.clear();
		
		int size = Math.min(page + itemsForPage, ((list.size() / 2) + (list.size() % 2 > 0 ? 1 : 0)));
		for (int i = page; i < size; i++) {
			
	
			// 파란색 라인 HBox 
			HBox h_Line = new HBox();
			vbox.setMargin(h_Line, new Insets(0,0,0,120));
			h_Line.setMaxWidth(710);
			h_Line.setPrefHeight(3);
			if (i%5 == 0) {
				h_Line.setStyle("-fx-background-color: white;");
			}else {
				h_Line.setStyle("-fx-background-color: #e1e1ef;");
			}	
			
			// 전체 HBox
			HBox hbox = new HBox();
			vbox.setMargin(hbox, new Insets(0,0,0,120));
			hbox.setPadding(new Insets(10,10,10,10));
			hbox.setSpacing(10);
			
			
			// 앨범이미지를 표시하는 ImageView
			ImageView iv_Album = new ImageView();
			Image img_Path = new Image(list.get(i*2).get("ALB_IMAGE").toString());
			iv_Album.setImage(img_Path);
			iv_Album.setFitWidth(130);
			iv_Album.setFitHeight(130);
			iv_Album.setId(list.get(i).get("ALB_NO").toString());
			iv_Album.setAccessibleText(list.get(i*2).get("SING_NO").toString());
			iv_Album.setOnMouseClicked(e -> {
				ImageView iv = (ImageView)e.getSource();
				SingerMenuController.menuCount = 1;
				SingerMenuController.albumNo = iv.getId();
				SingerMainController.singerNo = iv.getAccessibleText();
				  try {
				         Parent pane = FXMLLoader.load(getClass().getResource("../../singer/main/SingerMenu.fxml")); 
				         
				         String temp_path = (getClass().getResource("../../singer/main/SingerMenu.fxml")).getPath();
						 String path = temp_path.substring(1, temp_path.length()); // 현재화면 절대경로

						 GobackStack.goURL(path);
				         
				         MusicMainController.secondPane.getChildren().removeAll();
				         MusicMainController.secondPane.getChildren().setAll(pane);
				      } catch (IOException e1) {
				         e1.printStackTrace();
				      }
			});
			
			// Vbox (앨범 상세 정보)
			VBox v_album = new VBox();
			v_album.setMinWidth(200);
			v_album.setMinHeight(130);
			v_album.setSpacing(5);
				
				Label label_albumName = new Label(list.get(i*2).get("ALB_NAME").toString());
				label_albumName.setFont(Font.font("-윤고딕350", 18));
				label_albumName.setMaxWidth(200);
				label_albumName.setId(list.get(i*2).get("ALB_NO").toString());
				label_albumName.setAccessibleText(list.get(i*2).get("SING_NO").toString());
				label_albumName.setOnMouseClicked(e -> {
					Label la = (Label)e.getSource();
					SingerMenuController.menuCount = 1;
					SingerMenuController.albumNo = la.getId();
					SingerMainController.singerNo = la.getAccessibleText();
					  try {
					         Parent pane = FXMLLoader.load(getClass().getResource("../../singer/main/SingerMenu.fxml")); 
					         
					         String temp_path = (getClass().getResource("../../singer/main/SingerMenu.fxml")).getPath();
							 String path = temp_path.substring(1, temp_path.length()); // 현재화면 절대경로

							 GobackStack.goURL(path);
					         
					         MusicMainController.secondPane.getChildren().removeAll();
					         MusicMainController.secondPane.getChildren().setAll(pane);
					      } catch (IOException e1) {
					         e1.printStackTrace();
					      }
				});
				
				Label label_singerName = new Label(list.get(i*2).get("SING_NAME").toString());
				label_singerName.setFont(Font.font("-윤고딕330", 15));
				label_singerName.setMaxWidth(200);
				label_singerName.setId(list.get(i*2).get("ALB_NO").toString());
				label_singerName.setAccessibleText(list.get(i*2).get("SING_NO").toString());
				label_singerName.setOnMouseClicked(e -> {
					Label la = (Label)e.getSource();
					SingerMenuController.menuCount = 0;
					SingerMenuController.albumNo = la.getId();
					SingerMainController.singerNo = la.getAccessibleText();
					  try {
					         Parent pane = FXMLLoader.load(getClass().getResource("../../singer/main/SingerMenu.fxml")); 
					         
					         String temp_path = (getClass().getResource("../../singer/main/SingerMenu.fxml")).getPath();
							 String path = temp_path.substring(1, temp_path.length()); // 현재화면 절대경로
							 GobackStack.goURL(path);
					         
					         MusicMainController.secondPane.getChildren().removeAll();
					         MusicMainController.secondPane.getChildren().setAll(pane);
					      } catch (IOException e1) {
					         e1.printStackTrace();
					      }
				});
				
				
				Label label_indate = new Label();
				String date = list.get(i*2).get("ALB_SALEDATE").toString().substring(0,10);
				label_indate.setText(date);
				label_indate.setFont(Font.font("-윤고딕310", 15));
				
				HBox h_albumDetail = new HBox();
				h_albumDetail.setAlignment(Pos.CENTER_LEFT);
				h_albumDetail.setMaxWidth(200);
				h_albumDetail.setSpacing(10);
				
					Label label_musCount = new Label();
					label_musCount.setText(list.get(i*2).get("MUS_COUNT").toString() + "곡");
					label_musCount.setFont(Font.font("-윤고딕310", 15));
				
					
					// 추가 버튼
					JFXButton btn_Add = new JFXButton();
					btn_Add.setRipplerFill(Color.valueOf("#9c0000"));
					btn_Add.setAlignment(Pos.CENTER_LEFT);
					btn_Add.setPrefWidth(20);
					btn_Add.setPrefHeight(20);
					btn_Add.setAccessibleText(list.get(i*2).get("ALB_NO").toString());
					
					// 추가 버튼
					FontAwesomeIcon icon_Add = new FontAwesomeIcon();
					icon_Add.setIconName("PLUS");
					icon_Add.setFill(Color.valueOf("#9c0000"));
					icon_Add.setSize("20");
					btn_Add.setGraphic(icon_Add);
					btnAddList.add(btn_Add);
					
					h_albumDetail.getChildren().addAll(label_musCount,btn_Add);
					v_album.getChildren().addAll(label_albumName, label_singerName, label_indate, h_albumDetail);	
				hbox.getChildren().addAll(iv_Album, v_album);
				
				if (i*2+1 < list.size()) {
					// 앨범이미지를 표시하는 ImageView
					ImageView iv_Album2 = new ImageView();
					Image img_Path2 = new Image(list.get(i*2+1).get("ALB_IMAGE").toString());
					iv_Album2.setImage(img_Path2);
					iv_Album2.setFitWidth(130);
					iv_Album2.setFitHeight(130);
					iv_Album2.setId(list.get(i*2+1).get("ALB_NO").toString());
					iv_Album2.setAccessibleText(list.get(i*2+1).get("SING_NO").toString());
					iv_Album2.setOnMouseClicked(e -> {
						ImageView iv = (ImageView)e.getSource();
						SingerMenuController.menuCount = 1;
						SingerMenuController.albumNo = iv.getId();
						SingerMainController.singerNo = iv.getAccessibleText();
						  try {
						         Parent pane = FXMLLoader.load(getClass().getResource("../../singer/main/SingerMenu.fxml")); 
						         
						         String temp_path = (getClass().getResource("../../singer/main/SingerMenu.fxml")).getPath();
								 String path = temp_path.substring(1, temp_path.length()); // 현재화면 절대경로
								 GobackStack.goURL(path);
						         
						         MusicMainController.secondPane.getChildren().removeAll();
						         MusicMainController.secondPane.getChildren().setAll(pane);
						      } catch (IOException e1) {
						         e1.printStackTrace();
						      }
					});
					
					// Vbox (앨범 상세 정보)
					VBox v_album2 = new VBox();
					v_album2.setMinWidth(200);
					v_album2.setMinHeight(130);
					v_album2.setSpacing(5);
						
						Label label_albumName2 = new Label(list.get(i*2+1).get("ALB_NAME").toString());
						label_albumName2.setFont(Font.font("-윤고딕350", 18));
						label_albumName2.setMaxWidth(200);
						label_albumName2.setId(list.get(i*2+1).get("ALB_NO").toString());
						label_albumName2.setAccessibleText(list.get(i*2+1).get("SING_NO").toString());
						label_albumName2.setOnMouseClicked(e -> {
							Label la = (Label)e.getSource();
							SingerMenuController.menuCount = 1;
							SingerMenuController.albumNo = la.getId();
							SingerMainController.singerNo = la.getAccessibleText();
							  try {
							         Parent pane = FXMLLoader.load(getClass().getResource("../../singer/main/SingerMenu.fxml")); 
							         
							         String temp_path = (getClass().getResource("../../singer/main/SingerMenu.fxml")).getPath();
									 String path = temp_path.substring(1, temp_path.length()); // 현재화면 절대경로
									 GobackStack.goURL(path);
							         
							         MusicMainController.secondPane.getChildren().removeAll();
							         MusicMainController.secondPane.getChildren().setAll(pane);
							      } catch (IOException e1) {
							         e1.printStackTrace();
							      }
						});
						
						Label label_singerName2 = new Label(list.get(i*2+1).get("SING_NAME").toString());
						label_singerName2.setFont(Font.font("-윤고딕330", 15));
						label_singerName2.setMaxWidth(200);
						label_singerName2.setId(list.get(i*2+1).get("ALB_NO").toString());
						label_singerName2.setAccessibleText(list.get(i*2+1).get("SING_NO").toString());
						label_singerName2.setOnMouseClicked(e -> {
							Label la = (Label)e.getSource();
							SingerMenuController.menuCount = 0;
							SingerMenuController.albumNo = la.getId();
							SingerMainController.singerNo = la.getAccessibleText();
							  try {
							         Parent pane = FXMLLoader.load(getClass().getResource("../../singer/main/SingerMenu.fxml")); 
							         
							         String temp_path = (getClass().getResource("../../singer/main/SingerMenu.fxml")).getPath();
									 String path = temp_path.substring(1, temp_path.length()); // 현재화면 절대경로
									 GobackStack.goURL(path);
							         
							         MusicMainController.secondPane.getChildren().removeAll();
							         MusicMainController.secondPane.getChildren().setAll(pane);
							      } catch (IOException e1) {
							         e1.printStackTrace();
							      }
						});
						
						
						
						Label label_indate2 = new Label();
						String date2 = list.get(i*2+1).get("ALB_SALEDATE").toString().substring(0,10);
						label_indate2.setText(date);
						label_indate2.setFont(Font.font("-윤고딕310", 15));
							
							HBox h_albumDetail2 = new HBox();
							h_albumDetail2.setAlignment(Pos.CENTER_LEFT);
							h_albumDetail2.setMaxWidth(200);
							h_albumDetail2.setSpacing(10);
							
							Label label_musCount2 = new Label();
							label_musCount2.setText(list.get(i*2+1).get("MUS_COUNT").toString() + "곡");
							label_musCount2.setFont(Font.font("-윤고딕310", 15));
							
								
							// 추가 버튼
							JFXButton btn_Add2 = new JFXButton();
							btn_Add2.setRipplerFill(Color.valueOf("#9c0000"));
							btn_Add2.setAlignment(Pos.CENTER_LEFT);
							btn_Add2.setPrefWidth(20);
							btn_Add2.setPrefHeight(20);
							btn_Add2.setAccessibleText(list.get(i*2+1).get("ALB_NO").toString());
								
								// 추가 버튼
								FontAwesomeIcon icon_Add2 = new FontAwesomeIcon();
								icon_Add2.setIconName("PLUS");
								icon_Add2.setFill(Color.valueOf("#9c0000"));
								icon_Add2.setSize("20");
								btn_Add2.setGraphic(icon_Add2);
								btnAddList.add(btn_Add2);
						h_albumDetail2.getChildren().addAll(label_musCount2, btn_Add2);
						v_album2.getChildren().addAll(label_albumName2, label_singerName2, label_indate2, h_albumDetail2);
						hbox.getChildren().addAll(iv_Album2, v_album2);
					}
				
			vbox.getChildren().addAll(h_Line, hbox);
		}
		btnAddClick();
		return vbox;
	}
}
