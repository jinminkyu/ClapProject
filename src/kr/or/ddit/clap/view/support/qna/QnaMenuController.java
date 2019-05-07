/**
 *  문의사항 페이지를 출력하는 화면 controller
 *  
 *  @author hanhwa
 */
package kr.or.ddit.clap.view.support.qna;

import java.io.IOException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Pagination;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import kr.or.ddit.clap.main.LoginSession;
import kr.or.ddit.clap.service.qna.IQnaService;
import kr.or.ddit.clap.vo.support.QnaReviewVO;
import kr.or.ddit.clap.vo.support.QnaVO;

public class QnaMenuController implements Initializable {

	@FXML
	Pagination p_paging;
	@FXML
	JFXTreeTableView<QnaVO> tbl_qna;
	@FXML
	TreeTableColumn<QnaVO, ImageView> col_qnaNumber;
	@FXML
	TreeTableColumn<QnaVO, String> col_qnaTitle;
	@FXML
	TreeTableColumn<QnaVO, String> col_qnaDate;
	@FXML
	TreeTableColumn<QnaVO, String> col_qnaViewCnt;
	@FXML
	TreeTableColumn<QnaVO, String> col_qnaId;
	@FXML
	AnchorPane main;
	@FXML
	JFXButton btn_ins;

	private Registry reg;
	LoginSession ls = new LoginSession();
	private IQnaService iqs;
	private ObservableList<QnaVO> qnaList,currentqnaList;
	List<QnaReviewVO> revList;
	private int from, to, itemsForPage, totalPageCnt;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		try {
			reg = LocateRegistry.getRegistry("localhost", 8888);
			iqs = (IQnaService) reg.lookup("qna");
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
		
		
		
		col_qnaDate.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getQna_indate().substring(0, 10)));
		//col_qnaNumber.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getQna_no()));
		col_qnaTitle.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getQna_title()));
		col_qnaViewCnt.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getQna_view_cnt()));
		col_qnaId.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getMem_id()));
		col_qnaNumber.setCellValueFactory(
				param -> new SimpleObjectProperty<ImageView>(param.getValue().getValue().getImageview()));

		//자기가 쓴 글 만 가져오기
		if(ls.session==null) {
			errMsg("로그인 후 이용해 주세요 ", "");
			return;
		}
		if(ls.session.getMem_auth().equals("f")||ls.session.getMem_auth().equals("f  ")) {
		try {
			qnaList = FXCollections.observableArrayList(iqs.selectQna(ls.session.getMem_id()));
			
		} catch (RemoteException e) {
			System.out.println("에러");
			e.printStackTrace();
		}
		}else {
			try {
				qnaList = FXCollections.observableArrayList(iqs.selectQna(null));
				
			} catch (RemoteException e) {
				System.out.println("에러");
				e.printStackTrace();
			}
		}
		

		//댓글 여부에 따른 이미지 설정
		for(int i=0; i<qnaList.size(); i++) {
			try {
				revList = iqs.selectListReviewAll(qnaList.get(i).getQna_no());
				
			} catch (RemoteException e) {
				System.out.println("에러");
				e.printStackTrace();
			}
			if(revList.size()>0) {
				ImageView img = new ImageView("file:\\\\Sem-pc\\공유폴더\\Clap\\img\\check.png");
				qnaList.get(i).setImageview(img);
				qnaList.get(i).getImageview().setFitWidth(40);
				qnaList.get(i).getImageview().setFitHeight(40);
			}else{
				ImageView img = new ImageView("file:\\\\Sem-pc\\공유폴더\\Clap\\img\\uncheck.png");
				qnaList.get(i).setImageview(img);
				qnaList.get(i).getImageview().setFitWidth(40);
				qnaList.get(i).getImageview().setFitHeight(40);
			}
		}
		// 데이터 삽입
		TreeItem<QnaVO> root = new RecursiveTreeItem<>(qnaList, RecursiveTreeObject::getChildren);
		tbl_qna.setRoot(root);
		tbl_qna.setShowRoot(false);

		itemsForPage = 10; // 한페이지 보여줄 항목 수 설정

		paging();
		
		
		// 더블클릭
		tbl_qna.setOnMouseClicked(e -> {
			if (e.getClickCount() > 1) {
				String ContentNo = tbl_qna.getSelectionModel().getSelectedItem().getValue().getQna_no();

				try {
					// 바뀔 화면(FXML)을 가져옴
					QnaDetailContentController.ContentNo = ContentNo;// 번호을 변수로 넘겨줌
					
					iqs.updateCount(ContentNo);

					FXMLLoader loader = new FXMLLoader(getClass().getResource("QnaDetailContent.fxml"));// init실행됨
					Parent qnaDetail = loader.load();
					main.getChildren().removeAll();
					main.getChildren().setAll(qnaDetail);

				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		btn_ins.setOnAction(e ->{
			
			try {
				// 바뀔 화면(FXML)을 가져옴	

				FXMLLoader loader = new FXMLLoader(getClass().getResource("QnaMenuInsert.fxml"));// init실행됨
				Parent QnaInsert = loader.load();
				main.getChildren().removeAll();
				main.getChildren().setAll(QnaInsert);

			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			
		});
		

	}

	// 페이징 메서드
	private void paging() {
		totalPageCnt = qnaList.size() % itemsForPage == 0 ? qnaList.size() / itemsForPage
				: qnaList.size() / itemsForPage + 1;

		p_paging.setPageCount(totalPageCnt); // 전체 페이지 수 설정

		p_paging.setPageFactory((Integer pageIndex) -> {

			from = pageIndex * itemsForPage;
			to = from + itemsForPage - 1;

			TreeItem<QnaVO> root = new RecursiveTreeItem<>(getTableViewData(from, to),
					RecursiveTreeObject::getChildren);
			tbl_qna.setRoot(root);
			tbl_qna.setShowRoot(false);
			return tbl_qna;
		});
	}

	// 페이징에 맞는 데이터를 가져옴
	private ObservableList<QnaVO> getTableViewData(int from, int to) {

		currentqnaList = FXCollections.observableArrayList(); //
		int totSize = qnaList.size();
		for (int i = from; i <= to && i < totSize; i++) {

			currentqnaList.add(qnaList.get(i));
		}

		return currentqnaList;
	}
	public void errMsg(String headerText, String msg) {
		Alert errAlert = new Alert(AlertType.ERROR);
		errAlert.setTitle("로그인");
		errAlert.setHeaderText(headerText);
		errAlert.setContentText(msg);
		errAlert.showAndWait();
	}
}
