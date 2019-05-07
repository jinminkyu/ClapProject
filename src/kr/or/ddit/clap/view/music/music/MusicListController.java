/**
 *가수 리스트를 출력하는 화면 controller
 * 
 * 
 * @author Hansoo
 *
 */
package kr.or.ddit.clap.view.music.music;

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
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import kr.or.ddit.clap.main.LoginSession;
import kr.or.ddit.clap.service.music.IMusicService;
import kr.or.ddit.clap.vo.music.MusicVO;

public class MusicListController implements Initializable {

	@FXML
	Pagination p_paging;
	@FXML
	JFXTreeTableView<MusicVO> tbl_Music;
	@FXML
	TreeTableColumn<MusicVO, ImageView> col_musicImg;
	@FXML
	TreeTableColumn<MusicVO, String> col_musicTitle;
	@FXML
	TreeTableColumn<MusicVO, String> col_albumName;
	@FXML
	TreeTableColumn<MusicVO, String> col_singerName;
	@FXML
	TreeTableColumn<MusicVO, String> col_genreDetail;
	@FXML
	TreeTableColumn<MusicVO, String> col_musicNo;
	
	@FXML JFXComboBox<String> combo_search;
	@FXML TextField text_search;
	@FXML Button btn_search;	
	@FXML ImageView imgeview;
	@FXML Label session_id;
	
	private Registry reg;
	private IMusicService ims;
	private ObservableList<MusicVO> musicList, currentmusicList;
	private int from, to, itemsForPage, totalPageCnt;
	@FXML AnchorPane main;
	@FXML AnchorPane contents;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			reg = LocateRegistry.getRegistry("localhost", 8888);
			ims =  (IMusicService) reg.lookup("music");
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}

		// 관리자 이미지 넣기
		LoginSession ls = new LoginSession();
		Image img = null;
		if (ls.session.getMem_image() == null) {
			img = new Image("file:\\\\Sem-pc\\공유폴더\\Clap\\img\\userimg\\icons8-person-64.png");
		} else {
			img = new Image(ls.session.getMem_image());
		}
		imgeview.setImage(img);
		session_id.setText(ls.session.getMem_id());

		col_musicImg
				.setCellValueFactory(param -> new SimpleObjectProperty<ImageView>(param.getValue().getValue().getImgView()));
		
		col_musicTitle
				.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getMus_title()));

		col_albumName
				.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getAlb_name()));

		col_singerName
				.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getSing_name()));

		col_genreDetail.setCellValueFactory(
				param -> new SimpleStringProperty(param.getValue().getValue().getGen_detail_name()));

		col_musicNo.setCellValueFactory(
				param -> new SimpleStringProperty(param.getValue().getValue().getMus_no()));

	
		
		try {
			musicList = FXCollections.observableArrayList(ims.selectListAll());
		} catch (RemoteException e) {
			System.out.println("에러");
			e.getMessage();
			e.printStackTrace();
		}
		System.out.println(musicList.size());
		
		//데이터 삽입
		
		TreeItem<MusicVO> root = new RecursiveTreeItem<>(musicList, RecursiveTreeObject::getChildren);
		
		tbl_Music.setRoot(root);
		tbl_Music.setShowRoot(false);
		
		
		itemsForPage=9; // 한페이지 보여줄 항목 수 설정
		
		paging();
		
		combo_search.getItems().addAll("곡","앨범","아티스트");
		combo_search.setValue(combo_search.getItems().get(0));
		
		
		//검색버튼 클릭
		btn_search.setOnAction(e ->{
			search();
		});
		
		//더블클릭
		tbl_Music.setOnMouseClicked(e ->{
			System.out.println("더블클릭");
			if (e.getClickCount()  > 1) {
				
				
				
				String musicNo = tbl_Music.getSelectionModel().getSelectedItem().getValue().getMus_no();
				System.out.println("곡 번호:" + musicNo);
				
				try {
					//바뀔 화면(FXML)을 가져옴

					MusicDetailController.musicNo = musicNo;//곡 번호를 변수로 넘겨줌
					
					FXMLLoader loader = new FXMLLoader(getClass().getResource("MusicDetail.fxml"));// init실행됨
					Parent musicDetail= loader.load(); 
					
					MusicDetailController cotroller = loader.getController();
					cotroller.givePane(contents); 
					
					main.getChildren().removeAll();
					main.getChildren().setAll(musicDetail);
					
					
				
				} catch (IOException e1) {
					e1.printStackTrace();
				} 
			}
		});
		}
		
	
	//페이징  메서드
	private void paging() {
		totalPageCnt = musicList.size() % itemsForPage == 0 ? musicList.size() / itemsForPage
				: musicList.size() / itemsForPage + 1;
		
		p_paging.setPageCount(totalPageCnt); // 전체 페이지 수 설정
		
		p_paging.setPageFactory((Integer pageIndex) -> {
			
			from = pageIndex * itemsForPage;
			to = from + itemsForPage - 1;
			
			
			TreeItem<MusicVO> root = new RecursiveTreeItem<>(getTableViewData(from, to), RecursiveTreeObject::getChildren);
			tbl_Music.setRoot(root);
			tbl_Music.setShowRoot(false);
			return tbl_Music;
		});
	}
	
	//페이징에 맞는 데이터를 가져옴
private ObservableList<MusicVO> getTableViewData(int from, int to) {
		
	currentmusicList = FXCollections.observableArrayList(); //
		int totSize = musicList.size();
		for (int i = from; i <= to && i < totSize; i++) {
			
			currentmusicList.add(musicList.get(i));
		}
		
		return currentmusicList;
	}
//검색 메서드
private void search() {
	try {
		MusicVO vo = new MusicVO();
		ObservableList<MusicVO> searchlist = FXCollections.observableArrayList();
		switch (combo_search.getValue()) {
		
		case "곡":
			vo.setMus_title(text_search.getText());
			searchlist = FXCollections.observableArrayList(ims.searchList(vo));
			break;
		case "앨범":
			vo.setAlb_name(text_search.getText());
			searchlist = FXCollections.observableArrayList(ims.searchList(vo));
			break;
		case "아티스트":
			vo.setSing_name(text_search.getText());
			searchlist = FXCollections.observableArrayList(ims.searchList(vo));
			break;
			
		default :
			break;
		}
		
		musicList = FXCollections.observableArrayList(searchlist); //검색조건에 맞는 리스트를 저장
		paging();
	}
	catch (Exception e) {
		e.printStackTrace();
	}
}

public void InsertMusic() {
	
	try {
		//바뀔 화면(FXML)을 가져옴
		
	FXMLLoader loader = new FXMLLoader(getClass().getResource("InsertMusic.fxml"));// init실행됨
		Parent InsertMusic= loader.load(); 
		
		InsertMusicController cotroller = loader.getController();
		cotroller.givePane(contents); 
		
		main.getChildren().removeAll();
		main.getChildren().setAll(InsertMusic);
		
		
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
		FXMLLoader loader = new FXMLLoader(getClass().getResource("../../album/album/ShowAlbumLIst.fxml"));// init실행됨
		Parent album= loader.load(); 
		contents.getChildren().removeAll();
		contents.getChildren().setAll(album);
	} catch (IOException e1) {
		e1.printStackTrace();
	}
}



@FXML public void MusManag() {
	try {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("MusicList.fxml"));// init실행됨
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
