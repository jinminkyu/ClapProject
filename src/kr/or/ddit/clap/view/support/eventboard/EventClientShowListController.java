/**
 *  이벤트 페이지를 출력하는 화면 controller
 *  
 *  @author hanhwa
 */
package kr.or.ddit.clap.view.support.eventboard;

import java.io.IOException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ResourceBundle;

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
import javafx.scene.control.Pagination;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import kr.or.ddit.clap.service.eventboard.IEventBoardService;
import kr.or.ddit.clap.vo.support.EventBoardVO;

public class EventClientShowListController implements Initializable  {

	@FXML
	AnchorPane e_main;
	@FXML
	JFXTreeTableView<EventBoardVO> tbl_Event;
	@FXML
	TreeTableColumn<EventBoardVO, String> col_EventNo;
	@FXML
	TreeTableColumn<EventBoardVO, ImageView> col_EventImg;
	@FXML
	TreeTableColumn<EventBoardVO, String> col_EventTitle;
	@FXML
	TreeTableColumn<EventBoardVO, String> col_EventSDate;
	@FXML
	TreeTableColumn<EventBoardVO, String> col_EventEDate;
	@FXML
	TreeTableColumn<EventBoardVO, String> col_EventCnt;
	@FXML
	Pagination e_paging;
	
	private Registry reg;
	private IEventBoardService ies;
	private ObservableList<EventBoardVO> eventList, currenteventList;
	private int from, to, itemsForPage, totalPageCnt;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		try {
			reg = LocateRegistry.getRegistry("localhost", 8888);
			ies = (IEventBoardService) reg.lookup("eventboard");
		} catch(RemoteException e) {
			e.printStackTrace();
		} catch(NotBoundException e) {
			e.printStackTrace();
		}
		
		col_EventNo.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getEvent_no()));
		col_EventImg.setCellValueFactory(param -> new SimpleObjectProperty<ImageView>(param.getValue().getValue().getImgView()));
		col_EventTitle.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getEvent_title()));
		col_EventSDate.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getEvent_sdate().substring(0, 10)));
		col_EventEDate.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getEvent_edate()));
		col_EventCnt.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getEvent_view_cnt()));
		
		try {
			eventList = FXCollections.observableArrayList(ies.selectListAll());
			
		} catch(RemoteException e) {
			e.printStackTrace();
			System.out.println("에러");
		}
		
		//데이터 삽입
		TreeItem<EventBoardVO> root = new RecursiveTreeItem<>(eventList, RecursiveTreeObject::getChildren);
		tbl_Event.setRoot(root);
		tbl_Event.setShowRoot(false);
		
		itemsForPage=10; // 한페이지 보여줄 항목 수 설정
		
		paging();
		
		// 더블클릭
		tbl_Event.setOnMouseClicked(e -> {
			if (e.getClickCount() > 1) {
				String ContentNo = tbl_Event.getSelectionModel().getSelectedItem().getValue().getEvent_no();
				System.out.println(ContentNo);
				
				try {
					// 바뀔 화면(FXML)을 가져옴
					EventContentDetailController.ContentNo = ContentNo; // 번호을 변수로 넘겨줌
					ies.updateCount(ContentNo);
					System.out.println("이벤트 조회수 : " + ContentNo);
					
					FXMLLoader loader = new FXMLLoader(getClass().getResource("EventContentDetail.fxml"));// init실행됨
					Parent eventDetail = loader.load();
					e_main.getChildren().removeAll();
					e_main.getChildren().setAll(eventDetail);
					
				} catch(IOException e1) {
					e1.printStackTrace();
				}
				
			}
				
		});
		
		
		
		
		
		
		
	}
	
	//페이징  메서드
		private void paging() {
			totalPageCnt = eventList.size() % itemsForPage == 0 ? eventList.size() / itemsForPage
					: eventList.size() / itemsForPage + 1;
			
			e_paging.setPageCount(totalPageCnt); // 전체 페이지 수 설정
			
			e_paging.setPageFactory((Integer pageIndex) -> {
				
				from = pageIndex * itemsForPage;
				to = from + itemsForPage - 1;
				
				
				TreeItem<EventBoardVO> root = new RecursiveTreeItem<>(getTableViewData(from, to), RecursiveTreeObject::getChildren);
				tbl_Event.setRoot(root);
				tbl_Event.setShowRoot(false);
				return tbl_Event;
			});
		}
		
		// 페이징에 맞는 데이터를 가져옴
		private ObservableList<EventBoardVO> getTableViewData(int from, int to) {

			currenteventList = FXCollections.observableArrayList(); //
			int totSize = eventList.size();
			for (int i = from; i <= to && i < totSize; i++) {

				currenteventList.add(eventList.get(i));
			}

			return currenteventList;
		}

}
