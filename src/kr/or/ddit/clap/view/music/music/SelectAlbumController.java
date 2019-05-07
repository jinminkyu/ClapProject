/**
 *가수 리스트를 출력하는 화면 controller
 * 
 * 
 * @author Hansoo
 *
 */
package kr.or.ddit.clap.view.music.music;

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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import kr.or.ddit.clap.service.album.IAlbumService;
import kr.or.ddit.clap.vo.album.AlbumVO;

public class SelectAlbumController implements Initializable {

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
	
	//부모창의 InsertMusicController를 자신의 멤버변수로 선언한다.
	private InsertMusicController iMC;
	
	// 매개변수로 받은 InsertMusicController를 this연산자로  자신의 멤버변수에 set한다.
	public void setcontroller(InsertMusicController iMC){
		this.iMC = iMC;
	}
	
	
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
			System.out.println("에러");
			e.getMessage();
			e.printStackTrace();
		}
		System.out.println(albumList.size());
		
		//데이터 삽입
		
		TreeItem<AlbumVO> root = new RecursiveTreeItem<>(albumList, RecursiveTreeObject::getChildren);
		
		tbl_album.setRoot(root);
		tbl_album.setShowRoot(false);
		
		
		itemsForPage=10; // 한페이지 보여줄 항목 수 설정
		
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
				String albName = tbl_album.getSelectionModel().getSelectedItem().getValue().getAlb_name();
				String singname = tbl_album.getSelectionModel().getSelectedItem().getValue().getSing_name();
				String img_path = tbl_album.getSelectionModel().getSelectedItem().getValue().getAlb_image();
				System.out.println("선택한 앨범번호: "+albumNo);
				
				//InsertMusicController를 참조하는 멤버변수를 가지고 있기때문에 자식창에서 부모창의 멤버변수와 메서드를 사용할 수  있다.
				iMC.label_albNO.setText(albumNo);
				iMC.txt_albName.setText(albName);
				iMC.txt_singerName.setText(singname);

				//이미지를 바꿔준다.
				Image img = new Image(img_path);
				iMC.imgview_albumImg.setImage(img);
				
				//자식창 닫음
				Stage dialogStage = (Stage) btn_search.getScene().getWindow();
				dialogStage.close();
				
				
				
				
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



}
