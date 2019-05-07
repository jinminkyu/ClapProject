package kr.or.ddit.clap.view.musicplayer;

import java.io.IOException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ResourceBundle;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.fxml.Initializable;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TreeTableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import kr.or.ddit.clap.main.LoginSession;
import kr.or.ddit.clap.main.MusicMainController;
import kr.or.ddit.clap.service.myalbumlist.IMyAlbumListService;
import kr.or.ddit.clap.vo.myalbum.MyAlbumListVO;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.Label;
import com.jfoenix.controls.JFXButton;



public class MyAlbumListController implements Initializable{
	
	@FXML TreeTableView<MyAlbumListVO> t_table;
	@FXML TreeTableColumn<MyAlbumListVO, JFXCheckBox> tcol_check;
	@FXML TreeTableColumn<MyAlbumListVO, VBox> tcol_musicVbox;
	@FXML Label label_name;
	@FXML JFXButton btn_back;
	@FXML AnchorPane anchorpane_myalbum;
	
	public static String album_no;
	public static String album_name;
	private Registry reg;
	private IMyAlbumListService imals;
	public ObservableList<MyAlbumListVO> myalbumList;
	private MusicPlayerController mpc;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			reg = LocateRegistry.getRegistry("localhost", 8888);
			imals = (IMyAlbumListService) reg.lookup("myalbumlist");
			MyAlbumListVO vo = new MyAlbumListVO();
			vo.setMyalb_no(album_no);
			vo.setMem_id(LoginSession.session.getMem_id());
			myalbumList = FXCollections.observableArrayList(imals.selectMyAlbList(vo));
			t_table.setPlaceholder(new Label(" MyAlbum에 등록하신 곡이 없습니다."));
		} catch (RemoteException e2) {
			e2.printStackTrace();
		} catch (NotBoundException e1) {
			e1.printStackTrace();
		}
		
		label_name.setText(album_name);
		
		tcol_check.setCellValueFactory( param -> 
		new SimpleObjectProperty<JFXCheckBox>(param.getValue().getValue().getChBox())
		);
		
		tcol_musicVbox.setCellValueFactory( param ->
			new SimpleObjectProperty<VBox>(param.getValue().getValue().getMusicVbox())
		);
		
		TreeItem<MyAlbumListVO> root = new RecursiveTreeItem<>(myalbumList, RecursiveTreeObject::getChildren);
		t_table.setRoot(root);
		t_table.setShowRoot(false);
		
		mpc = MusicMainController.playerLoad.getController();
		mpc.btn_add.setVisible(true);
		
	}

	@FXML public void backClick() {
		try {
			mpc.btn_add.setVisible(false);
			AnchorPane pane = FXMLLoader.load(getClass().getResource("MyAlbum.fxml"));
			anchorpane_myalbum.getChildren().removeAll();
			anchorpane_myalbum.getChildren().setAll(pane);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
