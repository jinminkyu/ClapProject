package kr.or.ddit.clap.view.ticket.buylist;

import java.io.IOException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ResourceBundle;

import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import javafx.fxml.Initializable;
import kr.or.ddit.clap.main.LoginSession;
import kr.or.ddit.clap.service.ticket.ITicketService;
import kr.or.ddit.clap.view.member.mypage.MypageController;
import kr.or.ddit.clap.view.ticket.ticket.TicketController;
import kr.or.ddit.clap.vo.ticket.TicketBuyListVO;
import kr.or.ddit.clap.vo.ticket.TicketVO;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.TreeTableColumn;

public class TicketBuyListController implements Initializable{
	
	private static String user_id = LoginSession.session.getMem_id();
	private Registry reg;
	private ITicketService its;

	public MypageController mypc;
	private ObservableList<TicketBuyListVO> tickeylist1, currenttickeylist1;
	private ObservableList<TicketBuyListVO> tickeylist2, currenttickeylist2;
	private int from, to, itemsForPage, totalPageCnt;
	@FXML Label la_Date;
	@FXML Label la_Date2;
	@FXML AnchorPane contents;
	@FXML TreeTableView<TicketBuyListVO> tbl1_ticket;
	@FXML TreeTableColumn<TicketBuyListVO,String> col1_ticketname;
	@FXML TreeTableColumn<TicketBuyListVO,String> col1_tickettime;
	@FXML TreeTableView<TicketBuyListVO> tbl2_ticket;
	@FXML TreeTableColumn<TicketBuyListVO,String> col2_ticketname;
	@FXML TreeTableColumn<TicketBuyListVO,String> col2_tickettime;
	@FXML TreeTableColumn<TicketBuyListVO,String> col2_ticketbuydate;
	@FXML TreeTableColumn<TicketBuyListVO,String> col2_buyType;
	@FXML Pagination p_paging1;
	@FXML Pagination p_paging2;
	
	public void setController(MypageController mypc) {
		this.mypc=mypc;
	}
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			reg = LocateRegistry.getRegistry("localhost", 8888);
			its = (ITicketService) reg.lookup("ticket");

			// 파라미터로 받은 정보를 PK로 상세정보를 가져옴
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
	
		
	      TicketController tc = new TicketController();
	      String[] arr = tc.ticketCheck(user_id);
	      System.out.println("0"+arr[0]);
	      System.out.println("1"+arr[1]);
	      System.out.println(arr[2]);
	      
	      la_Date.setText(arr[1]);
	      if(arr[2]==null) {
	    	  la_Date2.setText("0 일");
	      }else {
	    	  la_Date2.setText(arr[2]+"일");
	      }
	    
	      
	      
	      //이용중인 이용권
			try {
				tickeylist1 = FXCollections.observableArrayList(its.selectBuyAllist(user_id));
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			
			for(int i=0; i< tickeylist1.size(); i++) {
				if(tickeylist1.get(i).getTicket_no().equals("1") || tickeylist1.get(i).getTicket_no().equals("2")) {
					tickeylist1.get(i).setTicket_no("무제한 음악감상 31일권");
					tickeylist1.get(i).setTicket_buydate1("1개월");
				}else if(tickeylist1.get(i).getTicket_no().equals("3") || tickeylist1.get(i).getTicket_no().equals("4")) {
					tickeylist1.get(i).setTicket_no("무제한 음악감상 183일권");
					tickeylist1.get(i).setTicket_buydate1("6개월");
				}else if(tickeylist1.get(i).getTicket_no().equals("5") || tickeylist1.get(i).getTicket_no().equals("6")) {
					tickeylist1.get(i).setTicket_no("무제한 음악감상 + VIP할인혜택 365일권");
					tickeylist1.get(i).setTicket_buydate1("1년권");
				}
			}
			col1_ticketname.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getValue().getTicket_no()));
			col1_tickettime.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getValue().getTicket_buydate1()));
			
			TreeItem<TicketBuyListVO> root = new RecursiveTreeItem<>(tickeylist1, RecursiveTreeObject::getChildren);
			tbl1_ticket.setRoot(root);
			tbl1_ticket.setShowRoot(false);

			itemsForPage = 10; // 한페이지 보여줄 항목 수 설정
			paging1();
			
			
			
			
			//이용권 구매내역
			
		
			try {
				tickeylist2 = FXCollections.observableArrayList(its.selectBuyAllist(user_id));
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			
			for(int i=0; i< tickeylist2.size(); i++) {
				if(tickeylist2.get(i).getTicket_no().equals("1") || tickeylist1.get(i).getTicket_no().equals("2")) {
					tickeylist2.get(i).setTicket_no("무제한 음악감상 31일권");
					tickeylist2.get(i).setTicket_buydate1("1개월");
				}else if(tickeylist2.get(i).getTicket_no().equals("3") || tickeylist1.get(i).getTicket_no().equals("4")) {
					tickeylist2.get(i).setTicket_no("무제한 음악감상 183일권");
					tickeylist2.get(i).setTicket_buydate1("6개월");
				}else if(tickeylist2.get(i).getTicket_no().equals("5") || tickeylist1.get(i).getTicket_no().equals("6")) {
					tickeylist2.get(i).setTicket_no("무제한 음악감상 + VIP할인혜택 365일권");
					tickeylist2.get(i).setTicket_buydate1("1년권");
				}
			}
			col2_ticketname.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getValue().getTicket_no()));
			col2_tickettime.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getValue().getTicket_buydate1()));
			col2_ticketbuydate.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getValue().getTicket_buydate().substring(0, 10)));
			col2_buyType.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getValue().getTicket_buy_type()));
			
			TreeItem<TicketBuyListVO> root2 = new RecursiveTreeItem<>(tickeylist2, RecursiveTreeObject::getChildren);
			tbl2_ticket.setRoot(root2);
			tbl2_ticket.setShowRoot(false);

			itemsForPage = 10; // 한페이지 보여줄 항목 수 설정
			paging2();
	      
	}
	
	private void paging1() {
		totalPageCnt = tickeylist1.size() % itemsForPage == 0 ? tickeylist1.size() / itemsForPage
				: tickeylist1.size() / itemsForPage + 1;

		p_paging1.setPageCount(totalPageCnt); // 전체 페이지 수 설정

		p_paging1.setPageFactory((Integer pageIndex) -> {

			from = pageIndex * itemsForPage;
			to = from + itemsForPage - 1;

			TreeItem<TicketBuyListVO> root = new RecursiveTreeItem<>(getTableViewData(from, to),
					RecursiveTreeObject::getChildren);
			tbl1_ticket.setRoot(root);
			tbl1_ticket.setShowRoot(false);
			return tbl1_ticket;
		});
	}

	// 페이징에 맞는 데이터를 가져옴
	private ObservableList<TicketBuyListVO> getTableViewData(int from, int to) {

		currenttickeylist1 = FXCollections.observableArrayList(); //
		int totSize = tickeylist1.size();
		for (int i = from; i <= to && i < totSize; i++) {

			currenttickeylist1.add(tickeylist1.get(i));
		}

		return currenttickeylist1;
	}

	
	private void paging2() {
		totalPageCnt = tickeylist2.size() % itemsForPage == 0 ? tickeylist2.size() / itemsForPage
				: tickeylist2.size() / itemsForPage + 1;

		p_paging2.setPageCount(totalPageCnt); // 전체 페이지 수 설정

		p_paging2.setPageFactory((Integer pageIndex) -> {

			from = pageIndex * itemsForPage;
			to = from + itemsForPage - 1;

			TreeItem<TicketBuyListVO> root = new RecursiveTreeItem<>(getTableViewData2(from, to),
					RecursiveTreeObject::getChildren);
			tbl2_ticket.setRoot(root);
			tbl2_ticket.setShowRoot(false);
			return tbl2_ticket;
		});
	}

	// 페이징에 맞는 데이터를 가져옴
	private ObservableList<TicketBuyListVO> getTableViewData2(int from, int to) {

		currenttickeylist2 = FXCollections.observableArrayList(); 
		int totSize = tickeylist2.size();
		for (int i = from; i <= to && i < totSize; i++) {

			currenttickeylist2.add(tickeylist2.get(i));
		}

		return currenttickeylist2;
	}
	
	
	
	@FXML public void buyTicket(ActionEvent event) {
		mypc.chTicket();
	}
	

}
