package kr.or.ddit.clap.view.member.mypage;

import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import kr.or.ddit.clap.main.LoginSession;
import kr.or.ddit.clap.service.myalbumlist.IMyAlbumListService;
import kr.or.ddit.clap.vo.myalbum.MyAlbumListVO;
import kr.or.ddit.clap.vo.myalbum.MyAlbumVO;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

public class MypageMyAlbListController implements Initializable{

	private Registry reg;
	private IMyAlbumListService imals;
	
	public static  String myAlbNo;
	public static String myAlbName;
	
	@FXML Label la_MyAlbName;
	@FXML JFXCheckBox chbox_main;
	
	@FXML JFXTreeTableView<MyAlbumListVO> tbl_MyAlbLists;
	@FXML TreeTableColumn<MyAlbumListVO,JFXCheckBox> col_Checks;
	@FXML TreeTableColumn<MyAlbumListVO,ImageView> col_Img;
	@FXML TreeTableColumn<MyAlbumListVO,String> col_Mus;
	@FXML TreeTableColumn<MyAlbumListVO,String> col_Its;
	@FXML Pagination p_Paging;
	
	private static String user_id = LoginSession.session.getMem_id();
	private ObservableList<MyAlbumListVO> myAlbList, currentsingerList;
	private int from, to, itemsForPage, totalPageCnt;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			reg = LocateRegistry.getRegistry("localhost", 8888);
			imals = (IMyAlbumListService) reg.lookup("myalbumlist");
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
		la_MyAlbName.setText(myAlbName);
		myAlblist();
		
	}
	
	public void myAlblist() {
		// 마이앨범
	
		MyAlbumListVO vo = new MyAlbumListVO();
		vo.setMem_id(user_id);
		vo.setMyalb_no(myAlbNo);
		try {
			myAlbList = FXCollections.observableArrayList(imals.selectMyAlbList(vo));
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		//col_No.setCellValueFactory(param -> new SimpleStringProperty(""+number++));
		col_Checks.setCellValueFactory(param -> new SimpleObjectProperty<JFXCheckBox>(param.getValue().getValue().getChBox()));
		col_Img.setCellValueFactory(param -> new SimpleObjectProperty<ImageView>(param.getValue().getValue().getImgView()));
		col_Mus.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getValue().getMus_title()));
		col_Its.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getValue().getSing_name()));


		TreeItem<MyAlbumListVO> root3 = new RecursiveTreeItem<>(myAlbList, RecursiveTreeObject::getChildren);
		tbl_MyAlbLists.setRoot(root3);
		tbl_MyAlbLists.setShowRoot(false);
		
		itemsForPage = 10; // 한페이지 보여줄 항목 수 설정

		paging();
	}

	private void paging() {
		totalPageCnt = myAlbList.size() % itemsForPage == 0 ? myAlbList.size() / itemsForPage
				: myAlbList.size() / itemsForPage + 1;

		p_Paging.setPageCount(totalPageCnt); // 전체 페이지 수 설정

		p_Paging.setPageFactory((Integer pageIndex) -> {

			from = pageIndex * itemsForPage;
			to = from + itemsForPage - 1;

			TreeItem<MyAlbumListVO> root = new RecursiveTreeItem<>(getTableViewData(from, to),
					RecursiveTreeObject::getChildren);
			tbl_MyAlbLists.setRoot(root);
			tbl_MyAlbLists.setShowRoot(false);
			return tbl_MyAlbLists;
		});

	}

	private ObservableList<MyAlbumListVO> getTableViewData(int from, int to) {

		currentsingerList = FXCollections.observableArrayList(); //
		int totSize = myAlbList.size();
		for (int i = from; i <= to && i < totSize; i++) {

			currentsingerList.add(myAlbList.get(i));
		}

		return currentsingerList;
	}

	// 전체 선택 및 해제 메서드
	@FXML
	public void mainCheck() {
		if (chbox_main.isSelected()) {
			for (int i = 0; i < myAlbList.size(); i++) {
				myAlbList.get(i).getChBox().setSelected(true);
			}

		} else {
			for (int i = 0; i < myAlbList.size(); i++) {
				myAlbList.get(i).getChBox().setSelected(false);
			}
		}
	}

	@FXML
	public void btn_Cl() {
		for (int i = 0; i < myAlbList.size(); i++) {
			if (myAlbList.get(i).getChBox().isSelected()) {
				MyAlbumListVO vo = new MyAlbumListVO();
				vo.setMus_no(myAlbList.get(i).getMus_no());
				vo.setMyalb_no(myAlbNo);
				try {
					int ok = imals.deleteMyAlbList(vo);
					if (ok > 0) {
						infoMsg("삭제 완료", "");
					}
					myAlblist();
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
			;
		}
	}

	public void infoMsg(String headerText, String msg) {
		Alert infoAlert = new Alert(AlertType.INFORMATION);
		infoAlert.setTitle("정보 확인");
		infoAlert.setHeaderText(headerText);
		infoAlert.setContentText(msg);
		infoAlert.showAndWait();
	}

	
	

}
