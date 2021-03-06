package kr.or.ddit.clap.view.member.profiles;

import java.io.IOException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.scene.layout.StackPane;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Pagination;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.image.ImageView;
import kr.or.ddit.clap.main.LoginSession;
import kr.or.ddit.clap.main.MusicMainController;
import kr.or.ddit.clap.service.like.ILikeService;
import kr.or.ddit.clap.service.playlist.IPlayListService;
import kr.or.ddit.clap.view.chartmenu.dialog.MyAlbumDialogController;
import kr.or.ddit.clap.view.chartmenu.musiclist.MusicList;
import kr.or.ddit.clap.view.musicplayer.MusicPlayerController;
import kr.or.ddit.clap.vo.member.LikeVO;
import kr.or.ddit.clap.vo.music.PlayListVO;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class LikeController implements Initializable {

	private Registry reg;
	private ILikeService ilks;

	@FXML
	JFXTreeTableView<LikeVO> tbl_like;
	@FXML
	TreeTableColumn<LikeVO, JFXCheckBox> col_Checks;
	@FXML
	TreeTableColumn<LikeVO, ImageView> col_Img;
	@FXML
	TreeTableColumn<LikeVO, String> col_MusInfo;
	@FXML
	TreeTableColumn<LikeVO, String> col_Its;
	@FXML
	TreeTableColumn<LikeVO, String> col_Alb;
	@FXML
	TreeTableColumn<LikeVO, String> col_LikeIndate;
	@FXML
	TreeTableColumn<LikeVO, JFXButton> col_Like;
	private ObservableList<LikeVO> likeList, currentsingerList;
	private int from, to, itemsForPage, totalPageCnt;
	private IPlayListService ipls;
	@FXML
	Pagination p_Paging;
	@FXML
	JFXCheckBox chbox_main;
	@FXML AnchorPane Head;
	@FXML AnchorPane contents;
	private MusicPlayerController mpc;
	private ObservableList<JFXCheckBox> cbnList = FXCollections.observableArrayList();
	private ObservableList<JFXButton> btnPlayList = FXCollections.observableArrayList();
	private ObservableList<JFXButton> btnAddList = FXCollections.observableArrayList();
	private ObservableList<JFXButton> btnPutList = FXCollections.observableArrayList();
	private ObservableList<JFXButton> btnMovieList = FXCollections.observableArrayList();
	@FXML VBox mainBox;
	@FXML StackPane stackpane;
	@FXML Button btn_mus;
	@FXML Button btn_Rcm;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			reg = LocateRegistry.getRegistry("localhost", 8888);
			ilks = (ILikeService) reg.lookup("like");
			ipls = (IPlayListService) reg.lookup("playlist");
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
		
	/*	musicList = new MusicList(cbnList, btnPlayList, btnAddList, btnPutList,
				  btnMovieList, mainBox, stackpane);
		*/
		col_Img.setCellValueFactory(param -> new SimpleObjectProperty<ImageView>(param.getValue().getValue().getImgView()));
		col_Its.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getSing_name()));
		col_MusInfo.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getMus_title()));
		col_Alb.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getAlb_name()));
		col_LikeIndate.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getlike_date().substring(0, 10)));
		col_Checks.setCellValueFactory(param -> new SimpleObjectProperty<JFXCheckBox>(param.getValue().getValue().getChBox()));
		col_Like.setCellValueFactory(param -> new SimpleObjectProperty<JFXButton>(param.getValue().getValue().getMubtnLike()));

		String user_id = LoginSession.session.getMem_id();
		LikeVO vo = new LikeVO();
		vo.setMem_id(user_id);
		try {
			likeList = FXCollections.observableArrayList(ilks.selectMusLike(vo));
			
		} catch (RemoteException e) {
			System.out.println("에러");
			e.printStackTrace();
		}
		
		
		// 데이터 삽입

		TreeItem<LikeVO> root = new RecursiveTreeItem<>(likeList, RecursiveTreeObject::getChildren);
		tbl_like.setRoot(root);
		tbl_like.setShowRoot(false);
		
		if(likeList.size()>0) {
		 for(int i=0; i<likeList.size(); i++) {

			 tbl_like.getTreeItem(i).getValue().getMubtnLike().setOnAction(e->{
			
				 JFXButton temp_btn = (JFXButton) e.getSource();
				 
				 for(int j =0; j<likeList.size(); j++) {
					 if(temp_btn.getId().equals(tbl_like.getTreeItem(j).getValue().getMubtnLike().getId())) {
					
						 
							LikeVO vo1 = new LikeVO();
							vo1.setMem_id(user_id);
							vo1.setMus_no(temp_btn.getId());
							try {
							int liset = ilks.deleteMusLike(vo1);
							} catch (RemoteException e2) {
								System.out.println("에러");
								e2.printStackTrace();
							}
							 likeList.remove(j);
						 //다시 설정
						 TreeItem<LikeVO> root1 = new RecursiveTreeItem<>(likeList, RecursiveTreeObject::getChildren);
						 tbl_like.setRoot(root1);
						 tbl_like.setShowRoot(false);
						 
						 return;
					 }
				 }
			 });
		 }
		}

		itemsForPage = 10; // 한페이지 보여줄 항목 수 설정

		paging();

	}

	private void paging() {
		totalPageCnt = likeList.size() % itemsForPage == 0 ? likeList.size() / itemsForPage
				: likeList.size() / itemsForPage + 1;

		p_Paging.setPageCount(totalPageCnt); // 전체 페이지 수 설정

		p_Paging.setPageFactory((Integer pageIndex) -> {

			from = pageIndex * itemsForPage;
			to = from + itemsForPage - 1;

			TreeItem<LikeVO> root = new RecursiveTreeItem<>(getTableViewData(from, to),
					RecursiveTreeObject::getChildren);
			tbl_like.setRoot(root);
			tbl_like.setShowRoot(false);
			return tbl_like;
		});

	}

	private ObservableList<LikeVO> getTableViewData(int from, int to) {

		currentsingerList = FXCollections.observableArrayList(); //
		int totSize = likeList.size();
		for (int i = from; i <= to && i < totSize; i++) {

			currentsingerList.add(likeList.get(i));
		}

		return currentsingerList;
	}

	// 전체 선택 및 해제 메서드
	@FXML
	public void mainCheck() {

		if (chbox_main.isSelected()) {
			for (int i = 0; i < likeList.size(); i++) {
				likeList.get(i).getChBox().setSelected(true);
			}

		} else {
			for (int i = 0; i < likeList.size(); i++) {
				likeList.get(i).getChBox().setSelected(false);
			}
		}
	}
	
	@FXML 
	public void btn_mus() {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("like.fxml"));
			Head.getChildren().removeAll();
			Head.getChildren().setAll(root);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@FXML 
	public void btn_alb() {
		try {
			Parent root1 = FXMLLoader.load(getClass().getResource("alblike.fxml"));
			contents.getChildren().removeAll();
			contents.getChildren().setAll(root1);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@FXML 
	public void btn_Rcm() {
		try {
			Parent root1 = FXMLLoader.load(getClass().getResource("rcmlike.fxml"));
			contents.getChildren().removeAll();
			contents.getChildren().setAll(root1);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@FXML 
	public void btn_Its() {
		try {
			Parent root1 = FXMLLoader.load(getClass().getResource("itslike.fxml"));
			contents.getChildren().removeAll();
			contents.getChildren().setAll(root1);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
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
		chbox_main.setSelected(false);
		mainCheck();
	}
	
	// 체크 박스 선택한 곡넘버 보내기
		private ArrayList<String> musicCheckList() {
			ArrayList<String> list = new ArrayList<>();
			for (int i = 0; i < likeList.size(); i++) {
				if (likeList.get(i).getChBox().isSelected()) {
					list.add(likeList.get(i).getChBox().getId());
					System.out.println(list.get(i));
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
		
		@FXML public void btnMainPut() {/*
			if (LoginSession.session == null) {
				return;
			}
			
			ArrayList<String> list = musicCheckList();
			myAlbumdialog1();
			chbox_main.setSelected(false);
			mainCheck();
		}
		public void myAlbumdialog1(){
			ArrayList<String> list = musicCheckList();
		StackPane content;
		try {
			content = FXMLLoader.load(getClass().getResource("../../chartmenu/dialog/MyAlbumDialog.fxml"));
			JFXDialog dialog = new JFXDialog(stackpane, content, JFXDialog.DialogTransition.CENTER);
			dialog.setBackground(Background.EMPTY);
			dialog.show();
			
		} catch (IOException e1) {
			e1.printStackTrace();
		}*/System.out.println("담기");}

		@FXML public void btnMainAdd() {}
}
