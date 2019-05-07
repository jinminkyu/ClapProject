/**
 *가수 리스트를 출력하는 화면 controller
 * 
 * 
 * @author Hansoo
 *
 */
package kr.or.ddit.clap.view.album.album;

import java.io.IOException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Pagination;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import kr.or.ddit.clap.main.LoginSession;
import kr.or.ddit.clap.service.album.IAlbumService;
import kr.or.ddit.clap.vo.album.AlbumVO;
import javafx.scene.control.Label;

public class ShowAlbumLIstController implements Initializable {

	@FXML
	Pagination p_paging;
	@FXML
	JFXTreeTableView<AlbumVO> tbl_album;
	@FXML
	TreeTableColumn<AlbumVO, ImageView> col_albumImg;
	@FXML
	TreeTableColumn<AlbumVO, String> col_albumName;
	@FXML
	TreeTableColumn<AlbumVO, String> col_singerName;
	@FXML
	TreeTableColumn<AlbumVO, String> col_saleDate;
	@FXML
	TreeTableColumn<AlbumVO, String> col_saleEnter;
	@FXML
	TreeTableColumn<AlbumVO, String> col_albumNo;
	
	@FXML JFXComboBox<String> combo_search;
	@FXML TextField text_search;
	@FXML Button btn_search;	
	
	private Registry reg;
	private IAlbumService ias;
	private ObservableList<AlbumVO> albumList, currentalbumList;
	private int from, to, itemsForPage, totalPageCnt;
	@FXML AnchorPane main;
	@FXML AnchorPane contents;
	@FXML ImageView imgeview;
	@FXML Label session_id;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			reg = LocateRegistry.getRegistry("localhost", 8888);
			ias =  (IAlbumService) reg.lookup("album");
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
		
		//관리자 이미지 넣기
		LoginSession ls = new LoginSession();
		Image img = null;
		if(ls.session.getMem_image() == null) {
			img = new Image("file:\\\\Sem-pc\\공유폴더\\Clap\\img\\userimg\\icons8-person-64.png");
		}else {
			img = new Image(ls.session.getMem_image());
		}
		imgeview.setImage(img);
		session_id.setText(ls.session.getMem_id());
		
		
		col_albumImg
				.setCellValueFactory(param -> new SimpleObjectProperty<ImageView>(param.getValue().getValue().getImgView()));

		col_albumName
				.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getAlb_name()));

		col_singerName
				.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getSing_name()));

		col_saleDate
				.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getAlb_saledate()));

		col_saleEnter.setCellValueFactory(
				param -> new SimpleStringProperty(param.getValue().getValue().getAlb_sale_enter()));

		col_albumNo.setCellValueFactory(
				param -> new SimpleStringProperty(param.getValue().getValue().getAlb_no()));

	
		
		try {
			albumList = FXCollections.observableArrayList(ias.selectListAll());
		} catch (RemoteException e) {
			e.getMessage();
			e.printStackTrace();
		}
		
		//데이터 삽입
		
		TreeItem<AlbumVO> root = new RecursiveTreeItem<>(albumList, RecursiveTreeObject::getChildren);
		
		tbl_album.setRoot(root);
		tbl_album.setShowRoot(false);
		
		
		itemsForPage=9; // 한페이지 보여줄 항목 수 설정
		
		paging();
		
		combo_search.getItems().addAll("앨범명","가수이름");
		combo_search.setValue(combo_search.getItems().get(0));
		
		
		//검색버튼 클릭
		btn_search.setOnAction(e ->{
			search();
		});
		
		//더블클릭
		tbl_album.setOnMouseClicked(e ->{
			if (e.getClickCount()  > 1) {
				
				
				String albumNo = tbl_album.getSelectionModel().getSelectedItem().getValue().getAlb_no();
				
				try {
					//바뀔 화면(FXML)을 가져옴

					AlbumDetailController.albumNo = albumNo; // 가수번호를 변수로 넘겨줌
					
					FXMLLoader loader = new FXMLLoader(getClass().getResource("AlbumDetail.fxml"));// init실행됨
					Parent albumDetail= loader.load(); 
					
					AlbumDetailController cotroller = loader.getController();
					cotroller.givePane(contents); 
					
					albumDetail.setLayoutY(50);
					main.getChildren().removeAll();
					main.getChildren().setAll(albumDetail);
					
					
				} catch (IOException e1) {
					e1.printStackTrace();
				} 
			}
		});
		}
		
	
	//페이징  메서드
	private void paging() {
		totalPageCnt = albumList.size() % itemsForPage == 0 ? albumList.size() / itemsForPage
				: albumList.size() / itemsForPage + 1;
		
		p_paging.setPageCount(totalPageCnt); // 전체 페이지 수 설정
		
		p_paging.setPageFactory((Integer pageIndex) -> {
			
			from = pageIndex * itemsForPage;
			to = from + itemsForPage - 1;
			
			
			TreeItem<AlbumVO> root = new RecursiveTreeItem<>(getTableViewData(from, to), RecursiveTreeObject::getChildren);
			tbl_album.setRoot(root);
			tbl_album.setShowRoot(false);
			return tbl_album;
		});
	}
	
	//페이징에 맞는 데이터를 가져옴
private ObservableList<AlbumVO> getTableViewData(int from, int to) {
		
	currentalbumList = FXCollections.observableArrayList(); //
		int totSize = albumList.size();
		for (int i = from; i <= to && i < totSize; i++) {
			
			currentalbumList.add(albumList.get(i));
		}
		
		return currentalbumList;
	}
//검색 메서드
private void search() {
	try {
		AlbumVO vo = new AlbumVO();
		ObservableList<AlbumVO> searchlist = FXCollections.observableArrayList();
		switch (combo_search.getValue()) {
		
		case "앨범명":
			vo.setAlb_name(text_search.getText());
			searchlist = FXCollections.observableArrayList(ias.searchList(vo));
			break;
		case "가수이름":
			vo.setSing_name(text_search.getText());
			searchlist = FXCollections.observableArrayList(ias.searchList(vo));
			break;
			
		default :
			break;
		}
		
		albumList = FXCollections.observableArrayList(searchlist); //검색조건에 맞는 리스트를 저장
		paging();
	}
	catch (Exception e) {
		e.printStackTrace();
	}
}

public void InsertAlbum() {
	
	try {
		//바뀔 화면(FXML)을 가져옴
		
	FXMLLoader loader = new FXMLLoader(getClass().getResource("InsertAlbum.fxml"));// init실행됨
		Parent InsertSinger= loader.load(); 
		
		InsertAlbumController cotroller = loader.getController();
		cotroller.givePane(contents); 
		
		main.getChildren().removeAll();
		main.getChildren().setAll(InsertSinger);
		
		
	} catch (IOException e1) {
		e1.printStackTrace();
	} 
	
	
}





//화면이동
@FXML public void MemManag() {
	try {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("../../member/manage/memmanage.fxml"));// init실행됨
		Parent member= loader.load(); 
		contents.getChildren().removeAll();
		contents.getChildren().setAll(member);
	} catch (IOException e1) {
		e1.printStackTrace();
	}
}


@FXML public void SingManag() {
	try {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("../../singer/singer/ShowSingerList.fxml"));// init실행됨
		Parent singer= loader.load(); 
		contents.getChildren().removeAll();
		contents.getChildren().setAll(singer);
	} catch (IOException e1) {
		e1.printStackTrace();
	}
}


@FXML public void AlbManag() {
	try {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("ShowAlbumLIst.fxml"));// init실행됨
		Parent album= loader.load(); 
		contents.getChildren().removeAll();
		contents.getChildren().setAll(album);
	} catch (IOException e1) {
		e1.printStackTrace();
	}
}



@FXML public void MusManag() {
	try {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("../../music/music/MusicList.fxml"));// init실행됨
		Parent music= loader.load(); 
		contents.getChildren().removeAll();
		contents.getChildren().setAll(music);
	} catch (IOException e1) {
		e1.printStackTrace();
	}
}


@FXML public void Recommen() {
	try {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("../../recommend/album/RecommendAlbumList.fxml"));// init실행됨
		Parent recommend= loader.load(); 
		contents.getChildren().removeAll();
		contents.getChildren().setAll(recommend);
	} catch (IOException e1) {
		e1.printStackTrace();
	}
}


@FXML public void Event() {
	try {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("../../support/eventboard/EventShowList.fxml"));// init실행됨
		Parent event= loader.load(); 
		contents.getChildren().removeAll();
		contents.getChildren().setAll(event);
	} catch (IOException e1) {
		e1.printStackTrace();
	}
}



@FXML public void Sales() {
	try {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("../../ticket/salemanage/salesmanage.fxml"));// init실행됨
		Parent sales= loader.load(); 
		contents.getChildren().removeAll();
		contents.getChildren().setAll(sales);
	} catch (IOException e1) {
		e1.printStackTrace();
	}
}










}
