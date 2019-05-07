/**
 *이벤트 리스트를 출력하는 화면 controller
 * 
 * 
 * @author Hanhwa
 *
 */
package kr.or.ddit.clap.view.support.eventboard;

import java.io.IOException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
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
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import kr.or.ddit.clap.main.LoginSession;
import kr.or.ddit.clap.service.eventboard.IEventBoardService;
import kr.or.ddit.clap.vo.support.EventBoardVO;

public class EventShowListController implements Initializable {
	//관리자 페이지

	@FXML
	AnchorPane contents; // 왼쪽 메뉴바
	@FXML
	AnchorPane main; // 오른쪽
	@FXML
	Pagination e_paging;
	@FXML
	JFXTreeTableView<EventBoardVO> tbl_Event;
	@FXML
	TreeTableColumn<EventBoardVO, String> col_EventNo;
	@FXML
	TreeTableColumn<EventBoardVO, String> col_EventTitle;
	@FXML
	TreeTableColumn<EventBoardVO, String> col_EventSDate;
	@FXML
	TreeTableColumn<EventBoardVO, String> col_EventEDate;
	@FXML
	TreeTableColumn<EventBoardVO, ImageView> col_EventImage;
	@FXML
	TreeTableColumn<EventBoardVO, String> col_Content;
	@FXML
	JFXComboBox<String> combo_Search;
	@FXML
	JFXButton btn_Add;
	@FXML
	JFXButton btn_search;
	@FXML
	JFXTextField text_Search;
	@FXML ImageView imgeview;
	@FXML Label session_id;
	
	private Registry reg;
	private IEventBoardService ies;
	private ObservableList<EventBoardVO> eventList, currenteventList;
	private int from, to, itemsForPage, totalPageCnt;
	/*public String str_EventTitle;
	public String str_EventSDate;
	public String str_EventEDate;*/
	//public static String eventNo;
	public EventBoardVO eVO = null;
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		try {
			reg = LocateRegistry.getRegistry("localhost", 8888);
			ies = (IEventBoardService) reg.lookup("eventboard");
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
		
		col_EventImage.setCellValueFactory(param -> new SimpleObjectProperty<ImageView>(param.getValue().getValue().getImgView()));
		col_EventNo.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getEvent_no()));
		col_EventTitle.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getEvent_title()));
		col_EventSDate.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getEvent_sdate()));
		col_EventEDate.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getEvent_edate()));
		col_Content.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getEvent_content()));
		
		try {
			
			eventList = FXCollections.observableArrayList(ies.selectListAll());
		} catch(RemoteException e) {
			System.out.println("에러");
			e.printStackTrace();
		}
		
		//데이터 삽입
		TreeItem<EventBoardVO> root = new RecursiveTreeItem<>(eventList,RecursiveTreeObject::getChildren);
		tbl_Event.setRoot(root);
		tbl_Event.setShowRoot(false);
		
		itemsForPage=10; // 한페이지 보여줄 항목 수 설정
		
		paging();
		
		combo_Search.getItems().addAll("제목명");
		combo_Search.setValue(combo_Search.getItems().get(0));
		
		
		//검색버튼 클릭
		btn_search.setOnAction(e ->{
			search();
		});
		
		
		// 더블클릭
		btn_Add.setOnMouseClicked(e -> {
			
			try {
				// 바뀔 화면(FXML)을 가져옴	
				FXMLLoader loader = new FXMLLoader(getClass().getResource("EventContentInsert.fxml"));// init실행됨
				Parent EventInsert = loader.load();
				
				EventContentInsertController cotroller = loader.getController();
				cotroller.givePane(contents); 
				
				main.getChildren().removeAll();
				main.getChildren().setAll(EventInsert);
				
				
			} catch(IOException ee) {
				ee.printStackTrace();
			}
			
			
		});
		
		// 더블클릭
		tbl_Event.setOnMouseClicked(e -> {
			if (e.getClickCount()  > 1) {
				String eventNo    = tbl_Event.getSelectionModel().getSelectedItem().getValue().getEvent_no();
				String eventImg   = tbl_Event.getSelectionModel().getSelectedItem().getValue().getEvent_image();
				String eventTitle = tbl_Event.getSelectionModel().getSelectedItem().getValue().getEvent_title();
				String eventCont  = tbl_Event.getSelectionModel().getSelectedItem().getValue().getEvent_content();
				String eventSdate = tbl_Event.getSelectionModel().getSelectedItem().getValue().getEvent_sdate();
				String eventEdate = tbl_Event.getSelectionModel().getSelectedItem().getValue().getEvent_edate();
				System.out.println("넘겨줄 정보 : "+eventNo + eventImg + eventTitle + eventCont + eventSdate + eventEdate);
					
				try {
					
					
					//바뀔 화면(FXML)을 가져옴 eventNo, 
					EventContentUpdateController.eventNo = eventNo; //글 번호를 변수로 넘겨줌.
					
					FXMLLoader loader = new FXMLLoader(getClass().getResource("EventContentUpdate.fxml"));
					Parent eventUpdate = loader.load();
					
					EventContentUpdateController controller = loader.getController();
					controller.initData(eventNo, eventImg, eventTitle, eventCont, eventSdate, eventEdate); //eVO
					controller.givePane(contents); 
					
					main.getChildren().removeAll();
					main.getChildren().setAll(eventUpdate);
					
					
				} catch(IOException ee) {
					ee.printStackTrace();
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
		
	// 검색 메서드
	private void search() {
		try {
			EventBoardVO vo = new EventBoardVO();
			ObservableList<EventBoardVO> searchlist = FXCollections.observableArrayList();

			switch (combo_Search.getValue()) {

			case "제목명":
				vo.setEvent_title(text_Search.getText());
				searchlist = FXCollections.observableArrayList(ies.searchList(vo));
				break;

			default:
				break;

			}

			eventList = FXCollections.observableArrayList(searchlist); // 검색조건에 맞는 리스트를 저장
			paging();

		} catch (Exception e) {
			e.printStackTrace();
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
			FXMLLoader loader = new FXMLLoader(getClass().getResource("EventShowList.fxml"));// init실행됨
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