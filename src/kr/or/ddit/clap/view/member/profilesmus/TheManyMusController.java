package kr.or.ddit.clap.view.member.profilesmus;

import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import javafx.scene.control.TreeTableColumn;
import javafx.scene.image.ImageView;
import kr.or.ddit.clap.main.LoginSession;
import kr.or.ddit.clap.service.musichistory.IMusicHistoryService;
import kr.or.ddit.clap.vo.music.MusicHistoryVO;
import javafx.scene.control.Pagination;
import javafx.scene.control.TreeItem;

public class TheManyMusController implements Initializable{
	private Registry reg;
	private IMusicHistoryService imhs;
	
	@FXML JFXCheckBox chbox_main;
	@FXML JFXTreeTableView<MusicHistoryVO> tbl_Manymus;
	@FXML TreeTableColumn<MusicHistoryVO, JFXCheckBox> col_Checks;
	@FXML TreeTableColumn<MusicHistoryVO, ImageView> col_Img;
	@FXML TreeTableColumn<MusicHistoryVO, String> col_Mus;
	@FXML TreeTableColumn<MusicHistoryVO, String> col_Singer;
	@FXML TreeTableColumn<MusicHistoryVO, String> col_MusCount;
	@FXML Pagination p_Paging;

	private ObservableList<MusicHistoryVO> manyMusList, currentsingerList;
	private int from, to, itemsForPage, totalPageCnt;

	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			reg = LocateRegistry.getRegistry("localhost", 8888);
			imhs = (IMusicHistoryService) reg.lookup("history");
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
		
		col_Img.setCellValueFactory(param -> new SimpleObjectProperty<ImageView>(param.getValue().getValue().getImageView()));
		col_Mus.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getTitle()));
		col_Singer.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getName()));
		col_MusCount.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getHisto_no()));
		col_Checks.setCellValueFactory(param -> new SimpleObjectProperty<JFXCheckBox>(param.getValue().getValue().getChBox()));

		String user_id = LoginSession.session.getMem_id();
		try {
			manyMusList = FXCollections.observableArrayList(imhs.selectManyLisMus(user_id));
			
		} catch (RemoteException e) {
			System.out.println("에러");
			e.printStackTrace();
		}
		
		TreeItem<MusicHistoryVO> root = new RecursiveTreeItem<>(manyMusList, RecursiveTreeObject::getChildren);
		tbl_Manymus.setRoot(root);
		tbl_Manymus.setShowRoot(false);

		itemsForPage = 10; // 한페이지 보여줄 항목 수 설정
		
		


		paging();
	}
	private void paging() {
		totalPageCnt = manyMusList.size() % itemsForPage == 0 ? manyMusList.size() / itemsForPage
				: manyMusList.size() / itemsForPage + 1;

		p_Paging.setPageCount(totalPageCnt); // 전체 페이지 수 설정

		p_Paging.setPageFactory((Integer pageIndex) -> {

			from = pageIndex * itemsForPage;
			to = from + itemsForPage - 1;

			TreeItem<MusicHistoryVO> root = new RecursiveTreeItem<>(getTableViewData(from, to),
					RecursiveTreeObject::getChildren);
			tbl_Manymus.setRoot(root);
			tbl_Manymus.setShowRoot(false);
			return tbl_Manymus;
		});

	}

	
	private ObservableList<MusicHistoryVO> getTableViewData(int from, int to) {

		currentsingerList = FXCollections.observableArrayList(); //
		int totSize = manyMusList.size();
		for (int i = from; i <= to && i < totSize; i++) {

			currentsingerList.add(manyMusList.get(i));
		}

		return currentsingerList;
	}

	
	// 전체 선택 및 해제 메서드
		@FXML
		public void mainCheck() {
			

			if (chbox_main.isSelected()) {
				for (int i = 0; i < manyMusList.size(); i++) {
					manyMusList.get(i).getChBox().setSelected(true);
				}

			} else {
				for (int i = 0; i < manyMusList.size(); i++) {
					manyMusList.get(i).getChBox().setSelected(false);
				}
			}
		}
		
	
		
}
