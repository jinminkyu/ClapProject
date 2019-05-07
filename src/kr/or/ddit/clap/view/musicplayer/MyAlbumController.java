package kr.or.ddit.clap.view.musicplayer;

import java.io.IOException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.layout.AnchorPane;
import kr.or.ddit.clap.main.LoginSession;
import kr.or.ddit.clap.service.myalbum.IMyAlbumService;
import kr.or.ddit.clap.vo.myalbum.MyAlbumVO;

public class MyAlbumController implements Initializable{

	@FXML JFXTreeTableView<MyAlbumVO> t_myAlbumTable;
	@FXML TreeTableColumn<MyAlbumVO, String> tcol_myAlbum;
	@FXML AnchorPane anchorpane_myalbum;
	
	private Registry reg;
	private IMyAlbumService imas;
	private TreeItem<MyAlbumVO> myAlbumListRoot;
	private ObservableList<MyAlbumVO> myAlbumList;
	public static FXMLLoader loader;
	

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			reg = LocateRegistry.getRegistry("localhost", 8888);
			imas = (IMyAlbumService) reg.lookup("myalbum");
			myAlbumList = FXCollections.observableArrayList(imas.myAlbumSelect(LoginSession.session.getMem_id()));
			t_myAlbumTable.setPlaceholder(new Label("등록 하신 MyAlbum이 없습니다."));
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
		
		tcol_myAlbum.setCellValueFactory( param ->
		new SimpleStringProperty(param.getValue().getValue().getMyalb_name())
		);
		
		myAlbumListRoot = new RecursiveTreeItem<>(myAlbumList, RecursiveTreeObject::getChildren);
		t_myAlbumTable.setRoot(myAlbumListRoot);
		t_myAlbumTable.setShowRoot(false);
		
		myAlbumTableSelet();
	}
	
	public void myAlbumTableSelet() {
		t_myAlbumTable.getSelectionModel().selectedIndexProperty().addListener(observable-> {
			int index = t_myAlbumTable.getSelectionModel().getSelectedIndex();
			try {
				String album_name = t_myAlbumTable.getSelectionModel().getSelectedItem().getValue().getMyalb_name();
				String album_no = t_myAlbumTable.getSelectionModel().getSelectedItem().getValue().getMyalb_no();
				MyAlbumListController.album_no = album_no;
				MyAlbumListController.album_name = album_name;
				loader = new FXMLLoader(getClass().getResource("MyAlbumList.fxml"));
				AnchorPane pane = loader.load();
				anchorpane_myalbum.getChildren().removeAll();
				anchorpane_myalbum.getChildren().setAll(pane);
				
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		
		});
	}

}
