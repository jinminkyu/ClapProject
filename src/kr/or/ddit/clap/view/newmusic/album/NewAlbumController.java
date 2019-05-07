package kr.or.ddit.clap.view.newmusic.album;

import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import com.jfoenix.controls.JFXButton;
import javafx.scene.Node;
import javafx.scene.control.Pagination;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import kr.or.ddit.clap.service.album.IAlbumService;
import kr.or.ddit.clap.view.chartmenu.musiclist.MusicList;

public class NewAlbumController implements Initializable{

	@FXML JFXButton btn_Song;
	@FXML JFXButton btn_Pop;
	@FXML JFXButton btn_Ost;
	@FXML JFXButton btn_Other;
	@FXML StackPane stackpane;
	@FXML VBox mainBox;
	
	private Registry reg;
	private IAlbumService ias;
	private MusicList musicList;
	private int itemsForPage;
	private ObservableList<JFXButton> btnAddList = FXCollections.observableArrayList();
	private ObservableList<Map> list;
	private Pagination p_page;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			reg = LocateRegistry.getRegistry("localhost", 8888);
			ias = (IAlbumService) reg.lookup("album");
			list = FXCollections.observableArrayList(ias.newAlbumSelect());
			System.out.println(list.size());
			itemsForPage = 5;
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
		
		musicList = new MusicList( btnAddList,mainBox,stackpane);
		pageing(list);
		
		
	}
	
	public VBox createPage(int pageIndex, ObservableList<Map> list, int itemsForPage) {
        int page = pageIndex * itemsForPage;
        return musicList.albumList(list, itemsForPage, page);
    }

	private void pageing(ObservableList<Map> list) {
		
		if (mainBox.getChildren().size() == 4) {
			mainBox.getChildren().remove(3);
		}
		
		if (list.size() == 0) return;
		int size = (list.size() / 2) + (list.size() % 2 > 0 ? 1 : 0);
		int totalPage = size / itemsForPage + (size % itemsForPage > 0 ? 1 : 0);
		
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
