package kr.or.ddit.clap.view.ticket.ticket;

import java.io.IOException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import kr.or.ddit.clap.main.LoginSession;
import kr.or.ddit.clap.service.join.IJoinService;
import kr.or.ddit.clap.service.login.ILoginService;
import kr.or.ddit.clap.service.ticket.ITicketService;
import kr.or.ddit.clap.vo.member.MemberVO;
import kr.or.ddit.clap.vo.ticket.TicketBuyListVO;
import kr.or.ddit.clap.vo.ticket.TicketVO;

public class TicketController implements Initializable{
	
	private ITicketService its;
	private ILoginService ils;
	private Registry reg;
	
	@FXML BorderPane pane;
	@FXML Label lb_date1;
	@FXML Label lb_date2;
	@FXML Label lb_date3; // 이용권 없는 회원
	@FXML Label lb1;
	@FXML Label lb2;
	@FXML Label lb3;
	@FXML Label lb4;
	@FXML Label lb5;
	@FXML Label lb6;
	@FXML Label lb_no1;
	@FXML Label lb_no2;
	@FXML Label lb_no3;
	@FXML Button btn1;
	@FXML Button btn2;
	@FXML Button btn3;
	@FXML Button btn_no1;
	@FXML Button btn_no2;
	@FXML Button btn_no3;
	
	static String[] ticketDate = new String[2]; // 이용권 기한. [0]은 만료일. [1]은 남은 일수.
	static Object[] ticketInfo = new Object[5]; 
	// [0]은 이용권 정보. [1]은 금액. [2]는 액수String. [3]은 이용권 t/f. [4]는 ticket_no (123456)
	// 이용권 정보. 1개월 1, 6개월 2, 1년은 3.
	
	LoginSession ls = new LoginSession();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		List<MemberVO> list2 = new ArrayList<MemberVO>();
		
		try {
			reg = LocateRegistry.getRegistry("localhost", 8888);
			its = (ITicketService) reg.lookup("ticket");
			
			ils = (ILoginService) reg.lookup("login");
			if (ls.session != null) {
				list2 = ils.select(ls.session.getMem_id());
				ls.session = list2.get(0);
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
		
		btn_no1.setDisable(false);
		btn_no2.setDisable(false);
		btn_no3.setDisable(false);
		
		if(ls.session==null) {
			lb_date3.setVisible(true);			
			lb_date3.setText("로그인이 필요합니다.");	
			btn_no1.setDisable(true);
			btn_no2.setDisable(true);
			btn_no3.setDisable(true);
		}else {
			List<TicketBuyListVO> list = new ArrayList<TicketBuyListVO>();
			try {
				list = its.selectList(ls.session.getMem_id());
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			
			
			if(list.size()==0) {
				lb_date1.setVisible(false);			
				lb_date2.setVisible(false);			
				lb_date3.setVisible(true);			
				lb_date3.setText("사용중인 이용권이 없습니다.");	
				ticketInfo[3] = false;
			}else {
				ticketDate = dateCheck(list);
				
				if(ticketDate[0].equals("no")) {
					lb_date3.setVisible(true);			
					lb_date3.setText("사용중인 이용권이 없습니다.");
					ticketInfo[3] = false;
				}else {
					lb_date3.setVisible(false);			
					lb_date1.setVisible(true);			
					lb_date2.setVisible(true);			
					lb_date1.setText("이용기간 : "+ticketDate[0]);	
					lb_date1.setTextFill(Color.BLACK);
					lb_date2.setText("( "+ticketDate[1]+"일 남았습니다. )");					
					lb_date2.setTextFill(Color.BLACK);
					ticketInfo[3] = true;
				}
			}
					
			if(!ls.session.getMem_grade().equalsIgnoreCase("vip")) {
				// vip 아닐때.
				
			}else if(ls.session.getMem_grade().equalsIgnoreCase("vip")) {
				lb_no1.setTextFill(Color.valueOf("#9d9d9d"));
				lb_no2.setTextFill(Color.valueOf("#9d9d9d"));
				lb_no3.setTextFill(Color.valueOf("#9d9d9d"));
				lb1.setTextFill(Color.BLACK);
				lb2.setTextFill(Color.BLACK);
				lb3.setTextFill(Color.BLACK);
				lb4.setTextFill(Color.BLACK);
				lb5.setTextFill(Color.BLACK);
				lb6.setTextFill(Color.BLACK);
				btn1.setDisable(false);
				btn2.setDisable(false);
				btn3.setDisable(false);
				btn_no1.setDisable(true);
				btn_no2.setDisable(true);
				btn_no3.setDisable(true);
			}
			
//			lb_date1.setTextFill(Color.valueOf("#00cc00"));
		}
		
		// 신빌더에서 onaction을 없애주니까 되네.
		btn_no1.setOnAction(e->{
			ticketInfo[0] = "1개월";
			ticketInfo[1] = 2500;
			ticketInfo[2] = "2,500원";
			ticketInfo[4] = 1;
			buyTicket();
		});
		btn_no2.setOnAction(e->{
			ticketInfo[0] = "6개월";
			ticketInfo[1] = 5000;
			ticketInfo[2] = "5,000원";
			ticketInfo[4] = 3;
			buyTicket();
		});
		btn_no3.setOnAction(e->{
			ticketInfo[0] = "1년";
			ticketInfo[1] = 8000;
			ticketInfo[2] = "8,000원";
			ticketInfo[4] = 5;
			buyTicket();
		});
		btn1.setOnAction(e->{
			ticketInfo[0] = "1개월";
			ticketInfo[1] = 2000;
			ticketInfo[2] = "2,000원";
			ticketInfo[4] = 2;
			buyTicket();
		});
		btn2.setOnAction(e->{
			ticketInfo[0] = "6개월";
			ticketInfo[1] = 4000;
			ticketInfo[2] = "4,000원";
			ticketInfo[4] = 4;
			buyTicket();
		});
		btn3.setOnAction(e->{
			ticketInfo[0] = "1년";
			ticketInfo[1] = 6500;
			ticketInfo[2] = "6,500원";
			ticketInfo[4] = 6;
			buyTicket();
		});
		
	}

	private String[] dateCheck(List<TicketBuyListVO> list) {
		// 이용권 기간 계산.
		Date now = new Date();
		long nowTime = now.getTime();
		System.out.println("nowTime  "+nowTime);
		long finalEndTime = 0;
		String finalEndDate = null; // 끝나는 날.
		String checkDay = null; // 남은 일 수.
		String[] result = new String[2];
		
		SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd");
		SimpleDateFormat sdf2 = new SimpleDateFormat("~ yyyy-MM-dd");
		
		List<TicketBuyListVO> checkList = new ArrayList<TicketBuyListVO>();
		for(int i=0; i<list.size(); i++) {
			// 기간이 유효한 이용권을 checkList에 넣는다.
			// list -> ticket_no, ticket_buydate(예-String 18/12/25)
			// ticket_no : 1,2는 31일. 3,4는 183일. 5,6은 365일.
			// times : 2678400000, 15811200000, 31536000000
			
			// 이용권 만료날짜 계산.
			Date parseDate = null;
			try {
				parseDate = sdf.parse(list.get(i).getTicket_buydate().substring(0, 10));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			long parseDateTime = parseDate.getTime();
			System.out.println("parseDateTime  " + parseDateTime);
			long endDateTime = 0;
			
			// int 범위를 넘어가면 l(long)을 붙여줘야되네.
			if(list.get(i).getTicket_no().equals("1") || list.get(i).getTicket_no().equals("2")) {
				endDateTime = parseDateTime + 2678400000l;
			}else if(list.get(i).getTicket_no().equals("3") || list.get(i).getTicket_no().equals("4")) {
				endDateTime = parseDateTime + 15811200000l;
			}else if(list.get(i).getTicket_no().equals("5") || list.get(i).getTicket_no().equals("6")) {
				endDateTime = parseDateTime + 31536000000l;
			}

			if(endDateTime > nowTime) {
				System.out.println(endDateTime - nowTime);
				finalEndTime += (endDateTime - nowTime);
			}
			
		}
		
		// game 맞춘경우.
		if(ls.session.getMem_game().equals("4")) {
			// 하루는 몇초? -> 86400000
			finalEndTime += 86400000l;
		}
		
		finalEndTime += nowTime;
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(finalEndTime);
		finalEndDate = sdf2.format(cal.getTime());
		
		if(finalEndTime<nowTime) {
			finalEndDate = "no";
			result[1] = "no";
		}else {
			checkDay = String.valueOf((finalEndTime - nowTime)/(1000*60*60*24));
			result[1] = checkDay;
		}
		
		result[0] = finalEndDate;
		
		return result;
	}
	
	/**
	 * 회원 아이디를 넣으면 이용권 사용 여부를 알려주는 메서드.
	 * @param id
	 * @return 이용권 여부 T/F
	 */
	public String[] ticketCheck(String id) {
		ITicketService its = null;
		Registry reg;
		String[] arr_return = new String[3];
		
		try {
			reg = LocateRegistry.getRegistry("localhost", 8888);
			its = (ITicketService) reg.lookup("ticket");
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
		
		List<TicketBuyListVO> list = new ArrayList<TicketBuyListVO>();
		try {
			list = its.selectList(id);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
		if(list.size()==0) {
			arr_return[0] = "f";
		}else {
			ticketDate = dateCheck(list);
			arr_return[1] = ticketDate[0].substring(2, ticketDate[0].length());
			arr_return[2] = ticketDate[1];
			
			if(ticketDate[0].equals("no")) {
				arr_return[0] = "f";
			}else {
				arr_return[0] = "t";
			}
		}
		return arr_return;
	}

	public void buyTicket() {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("BuyTicket2.fxml"));
			pane.getChildren().removeAll();
			pane.getChildren().setAll(root);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
