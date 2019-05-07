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
import javafx.scene.layout.AnchorPane;
import kr.or.ddit.clap.main.LoginSession;
import kr.or.ddit.clap.service.musicreview.IMusicReviewService;
import kr.or.ddit.clap.service.singer.ISingerReviewService;
import kr.or.ddit.clap.vo.album.AlbumReviewVO;
import kr.or.ddit.clap.vo.music.MusicReviewVO;
import kr.or.ddit.clap.vo.singer.SingerReviewVO;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import javafx.scene.control.TreeTableColumn;
import javafx.scene.image.ImageView;
import javafx.scene.control.Pagination;
import javafx.scene.control.TreeItem;

public class ReviewItsController implements Initializable{
	private Registry reg;
	private ISingerReviewService isrs;

	@FXML AnchorPane Head;
	@FXML JFXTreeTableView<SingerReviewVO> tbl_Review;
	@FXML TreeTableColumn<SingerReviewVO,ImageView> col_imge;
	@FXML TreeTableColumn<SingerReviewVO,String> col_Reviewcon;
	@FXML TreeTableColumn<SingerReviewVO,String> col_Its;
	@FXML TreeTableColumn<SingerReviewVO,String> col_Debu;
	@FXML TreeTableColumn<SingerReviewVO,String> col_ReviwIndate;
	@FXML TreeTableColumn<SingerReviewVO,JFXButton> col_del;
	@FXML Pagination p_Paging;
	
	private ObservableList<SingerReviewVO> reviewList, currentsingerList;
	private int from, to, itemsForPage, totalPageCnt;




	@Override
	public void initialize(URL location, ResourceBundle resources) {
	
		try {
			reg = LocateRegistry.getRegistry("localhost", 8888);
			isrs = (ISingerReviewService) reg.lookup("singreview");
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
		

		col_imge.setCellValueFactory(param -> new SimpleObjectProperty<ImageView>(param.getValue().getValue().getImgView()));
		col_Its.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getSing_name()));
		col_Debu.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getSing_debut_era()));
		col_Reviewcon.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getsing_re_content()));
		col_ReviwIndate.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getSing_re_indate().substring(0, 10)));
		col_del.setCellValueFactory(param -> new SimpleObjectProperty<JFXButton>(param.getValue().getValue().getBtnDel()));
	
		String user_id = LoginSession.session.getMem_id();
		SingerReviewVO vo = new SingerReviewVO();
		vo.setMem_id(user_id);
		try {
			reviewList = FXCollections.observableArrayList(isrs.selectItsReview(vo));
		} catch (RemoteException e) {
			System.out.println("에러");
			e.printStackTrace();
		}
		
		
		// 데이터 삽입

		TreeItem<SingerReviewVO> root = new RecursiveTreeItem<>(reviewList, RecursiveTreeObject::getChildren);
		tbl_Review.setRoot(root);
		tbl_Review.setShowRoot(false);

		if (reviewList.size() > 0) {
			for (int i = 0; i < reviewList.size(); i++) {
				System.out.println(reviewList.size());

				// tbl_music.getTreeItem(i).getValue().getBtn().setOnAction(e->{
				tbl_Review.getTreeItem(i).getValue().getBtnDel().setOnAction(e -> {

					System.out.println("남은 개수:" + reviewList.size());
					JFXButton temp_btn = (JFXButton) e.getSource();
					if (reviewList.size() > 0) {
						for (int j = 0; j < reviewList.size(); j++) {
							System.out.println(temp_btn.getId());
							if (temp_btn.getId().equals(tbl_Review.getTreeItem(j).getValue().getBtnDel().getId())) {
								reviewList.remove(j);

								SingerReviewVO vo1 = new SingerReviewVO();
								vo1.setMem_id(user_id);
								vo1.setSing_re_no(temp_btn.getId());
								try {
									isrs.deleteItsReview(vo1);
								} catch (RemoteException e2) {
									System.out.println("에러");
									e2.printStackTrace();
								}

								// 다시 설정
								TreeItem<SingerReviewVO> root1 = new RecursiveTreeItem<>(reviewList,RecursiveTreeObject::getChildren);
								tbl_Review.setRoot(root1);
								tbl_Review.setShowRoot(false);

								return;
							}
						}
					}
				});
			}

		}

		
		
		itemsForPage = 10; // 한페이지 보여줄 항목 수 설정

		paging();

	}

	private void paging() {
		totalPageCnt = reviewList.size() % itemsForPage == 0 ? reviewList.size() / itemsForPage
				: reviewList.size() / itemsForPage + 1;

		p_Paging.setPageCount(totalPageCnt); // 전체 페이지 수 설정

		p_Paging.setPageFactory((Integer pageIndex) -> {

			from = pageIndex * itemsForPage;
			to = from + itemsForPage - 1;

			TreeItem<SingerReviewVO> root = new RecursiveTreeItem<>(getTableViewData(from, to),
					RecursiveTreeObject::getChildren);
			tbl_Review.setRoot(root);
			tbl_Review.setShowRoot(false);
			return tbl_Review;
		});

	}

	private ObservableList<SingerReviewVO> getTableViewData(int from, int to) {

		currentsingerList = FXCollections.observableArrayList(); //
		int totSize = reviewList.size();
		for (int i = from; i <= to && i < totSize; i++) {

			currentsingerList.add(reviewList.get(i));
		}

		return currentsingerList;
	}
}
