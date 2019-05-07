/**
 *가수 리스트를 출력하는 화면 controller
 * 
 * 
 * @author Hansoo
 *
 */
package kr.or.ddit.clap.view.recommend.album;

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
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Pagination;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import kr.or.ddit.clap.service.music.IMusicService;
import kr.or.ddit.clap.vo.music.MusicVO;

public class SelectMusicController implements Initializable {

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
	
	private Registry reg;
	private IMusicService ims;
	private ObservableList<MusicVO> musicList, currentmusicList;
	private int from, to, itemsForPage, totalPageCnt;
	@FXML AnchorPane main;
	@FXML AnchorPane contents;
	public InsertRecommendAlbumController irac;
	
	public void setcontroller(InsertRecommendAlbumController irac) {
		this.irac = irac;
	}

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
		
		
		itemsForPage=10; // 한페이지 보여줄 항목 수 설정
		
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
				
				MusicVO pVO =new MusicVO();
				pVO.setMus_title(tbl_Music.getSelectionModel().getSelectedItem().getValue().getMus_title());
				pVO.setAlb_name(tbl_Music.getSelectionModel().getSelectedItem().getValue().getAlb_name());
				pVO.setSing_name(tbl_Music.getSelectionModel().getSelectedItem().getValue().getSing_name());
				pVO.setMus_no(tbl_Music.getSelectionModel().getSelectedItem().getValue().getMus_no());
				pVO.setBtn(tbl_Music.getSelectionModel().getSelectedItem().getValue().createButtonImg());
				
				irac.addMusic(pVO);
				
				
				String musicNo = tbl_Music.getSelectionModel().getSelectedItem().getValue().getMus_no();
				System.out.println("곡 번호:" + musicNo);
			
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

//확인버튼 클릭시 창닫힘
	public void btn_close() {
		
		Stage dialogStage = (Stage) btn_search.getScene().getWindow();
		dialogStage.close();
		
	}


}
