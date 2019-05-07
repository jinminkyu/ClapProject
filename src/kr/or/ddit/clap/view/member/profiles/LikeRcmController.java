package kr.or.ddit.clap.view.member.profiles;

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
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.image.ImageView;
import kr.or.ddit.clap.main.LoginSession;
import kr.or.ddit.clap.service.like.ILikeService;
import kr.or.ddit.clap.vo.member.LikeVO;
import javafx.scene.control.Pagination;
import javafx.scene.control.TreeItem;

public class LikeRcmController implements Initializable{

	private Registry reg;
	private ILikeService ilks;
	
	@FXML JFXCheckBox chbox_main;
	
	@FXML JFXTreeTableView<LikeVO> tbl_like;

	@FXML TreeTableColumn<LikeVO, JFXCheckBox> col_Checks;
	@FXML TreeTableColumn<LikeVO, ImageView> col_Img;
	@FXML TreeTableColumn<LikeVO, String> col_MusInfo;
	@FXML TreeTableColumn<LikeVO, String> col_Its;
	@FXML TreeTableColumn<LikeVO, String> col_Alb;
	@FXML TreeTableColumn<LikeVO, String> col_LikeIndate;
	@FXML TreeTableColumn<LikeVO, JFXButton> col_Like;
	private ObservableList<LikeVO> likeList, currentsingerList;
	private int from, to, itemsForPage, totalPageCnt;
	
	@FXML Pagination p_Paging;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			reg = LocateRegistry.getRegistry("localhost", 8888);
			ilks = (ILikeService) reg.lookup("like");
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
		
		col_Img.setCellValueFactory(param -> new SimpleObjectProperty<ImageView>(param.getValue().getValue().getImgView()));
		col_Its.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getSing_name()));
		col_MusInfo.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getMus_title()));
		col_LikeIndate.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getlike_date().substring(0, 10)));
		col_Checks.setCellValueFactory(param -> new SimpleObjectProperty<JFXCheckBox>(param.getValue().getValue().getChBox()));
		col_Like.setCellValueFactory(param -> new SimpleObjectProperty<JFXButton>(param.getValue().getValue().getRcmbtnLike()));

		String user_id = LoginSession.session.getMem_id();
		LikeVO vo = new LikeVO();
		vo.setMem_id(user_id);
		try {
			likeList = FXCollections.observableArrayList(ilks.selectRcmLike(vo));
			
		} catch (RemoteException e) {
			System.out.println("에러");
			e.printStackTrace();
		}
		
		TreeItem<LikeVO> root = new RecursiveTreeItem<>(likeList, RecursiveTreeObject::getChildren);
		tbl_like.setRoot(root);
		tbl_like.setShowRoot(false);
		if(likeList.size()>0) {
		 for(int i=0; i<likeList.size(); i++) {
			 System.out.println(likeList.size());

			 // tbl_music.getTreeItem(i).getValue().getBtn().setOnAction(e->{
			 tbl_like.getTreeItem(i).getValue().getRcmbtnLike().setOnAction(e->{
			
				 JFXButton temp_btn = (JFXButton) e.getSource();
				 if(likeList.size()>0) {
				 for(int j =0; j<likeList.size(); j++) {
					 if(temp_btn.getId().equals(tbl_like.getTreeItem(j).getValue().getRcmbtnLike().getId())) {
						 likeList.remove(j);
						 
							LikeVO vo1 = new LikeVO();
							vo1.setMem_id(user_id);
							vo1.setRcm_alb_no(temp_btn.getId());
							try {
							int liset = ilks.deleteRcmLike(vo1);
							} catch (RemoteException e2) {
								System.out.println("에러");
								e2.printStackTrace();
							}
						 //다시 설정
						 TreeItem<LikeVO> root1 = new RecursiveTreeItem<>(likeList, RecursiveTreeObject::getChildren);
						 tbl_like.setRoot(root1);
						 tbl_like.setShowRoot(false);
						 
						 return;
					 }
				 }		 }
			 });
			 }
		 
		 }
		
		itemsForPage = 10; // 한페이지 보여줄 항목 수 설정

		paging();

		
	}

	private void paging() {
		totalPageCnt = likeList.size() % itemsForPage == 0 ? likeList.size() / itemsForPage
				: likeList.size() / itemsForPage + 1;

		p_Paging.setPageCount(totalPageCnt); // 전체 페이지 수 설정

		p_Paging.setPageFactory((Integer pageIndex) -> {

			from = pageIndex * itemsForPage;
			to = from + itemsForPage - 1;

			TreeItem<LikeVO> root = new RecursiveTreeItem<>(getTableViewData(from, to),
					RecursiveTreeObject::getChildren);
			tbl_like.setRoot(root);
			tbl_like.setShowRoot(false);
			return tbl_like;
		});

	}

	
	private ObservableList<LikeVO> getTableViewData(int from, int to) {

		currentsingerList = FXCollections.observableArrayList(); //
		int totSize = likeList.size();
		for (int i = from; i <= to && i < totSize; i++) {

			currentsingerList.add(likeList.get(i));
		}

		return currentsingerList;
	}

	
	// 전체 선택 및 해제 메서드
		@FXML
		public void mainCheck() {
			if (chbox_main.isSelected()) {
				for (int i = 0; i < likeList.size(); i++) {
					likeList.get(i).getChBox().setSelected(true);
				}

			} else {
				for (int i = 0; i < likeList.size(); i++) {
					likeList.get(i).getChBox().setSelected(false);
				}
			}
		}

}
