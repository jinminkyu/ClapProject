package kr.or.ddit.clap.view.chartmenu.dialog;

import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import kr.or.ddit.clap.main.LoginSession;
import kr.or.ddit.clap.main.MusicMainController;
import kr.or.ddit.clap.service.myalbum.IMyAlbumService;
import kr.or.ddit.clap.service.myalbumlist.IMyAlbumListService;
import kr.or.ddit.clap.view.musicplayer.MusicPlayerController;
import kr.or.ddit.clap.vo.myalbum.MyAlbumVO;

/**
 * 
 * @author 진민규
 *
 */

public class MyAlbumDialogController implements Initializable{
	
	@FXML JFXTextField tf_albumName;
	@FXML JFXTreeTableView<MyAlbumVO> t_table;
	@FXML TreeTableColumn<MyAlbumVO, String> tcol_album;
	@FXML TreeTableColumn<MyAlbumVO, String> tcol_no;
	
	private ObservableList<MyAlbumVO> albumList;
	private Registry reg;
	private IMyAlbumService imas;
	private IMyAlbumListService imals;
	private String id;
	private TreeItem<MyAlbumVO> root;
	public static ArrayList<String> mus_no = new ArrayList<>();
	
	private MusicPlayerController mpc;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		mpc = MusicMainController.playerLoad.getController();
		try {
			reg = LocateRegistry.getRegistry("localhost", 8888);
			imas = (IMyAlbumService) reg.lookup("myalbum");
			imals = (IMyAlbumListService) reg.lookup("myalbumlist");
			id = LoginSession.session.getMem_id();
			albumList = FXCollections.observableArrayList(imas.myAlbumSelect(id));
			
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		} 
		
		tcol_album.setCellValueFactory( param -> 
			new SimpleStringProperty(param.getValue().getValue().getMyalb_name())
		);
		
		tcol_no.setCellValueFactory( param -> 
			new SimpleStringProperty(param.getValue().getValue().getMyalb_no())
		);
	
		root = new RecursiveTreeItem<>(albumList, RecursiveTreeObject::getChildren);
		t_table.setRoot(root);
		t_table.setShowRoot(false);
		
		// 더블 클릭 시
		t_table.setOnMouseClicked(e ->{
			if (e.getClickCount()  > 1) {
				try {
					for (int i = 0; i < mus_no.size(); i++) {
						Map<String, String> myAlbumList = new HashMap<>();
						String albumNo = t_table.getSelectionModel().getSelectedItem().getValue().getMyalb_no();
						myAlbumList.put("myalb_no", albumNo);
						myAlbumList.put("mus_no", mus_no.get(i));
						imals.myAlbumListInsert(myAlbumList);
					}
					albumList = FXCollections.observableArrayList(imas.myAlbumSelect(id));
					root = new RecursiveTreeItem<>(albumList, RecursiveTreeObject::getChildren);
					t_table.setRoot(root);
					
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
			}
		});
	}
	
	@FXML public void myAlbumInsert() {
		if (tf_albumName.getText().length() != 0) {
			try {
				Map<String, String> myAlbum = new HashMap<>();
				myAlbum.put("name", tf_albumName.getText());
				myAlbum.put("id", id);
				int result = imas.myAlbumInsert(myAlbum);
				
				albumList = FXCollections.observableArrayList(imas.myAlbumSelect(id));
				root = new RecursiveTreeItem<>(albumList, RecursiveTreeObject::getChildren);
				t_table.setRoot(root);
			} catch (RemoteException e) {
				e.printStackTrace();
			}		
		}
	}
}
