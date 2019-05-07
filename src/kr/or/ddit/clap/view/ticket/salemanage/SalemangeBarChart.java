package kr.or.ddit.clap.view.ticket.salemanage;

import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Arrays;
import java.util.Random;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXComboBox;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.AnchorPane;
import kr.or.ddit.clap.service.ticket.ITicketService;
import kr.or.ddit.clap.vo.ticket.TicketBuyListVO;

public class SalemangeBarChart implements Initializable{
	
	private Registry reg;
	private ITicketService its;
	private AnchorPane contents;
	 String[] cates = {"판매량"};
	    // 시리즈를 담을 자료구조 
	    XYChart.Series<String, Number> series = null;
	    private ObservableList<String> xLabels = FXCollections.observableArrayList();
		@FXML CategoryAxis xAxis;
		@FXML NumberAxis yAxis;
		@FXML BarChart chart;
		private int no1_total=0; 
		private int no2_total=0;
		private int no3_total=0;
		private ObservableList<TicketBuyListVO> tiList;
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
	    	
	        xLabels.addAll(Arrays.asList(cates));
	        xAxis.setCategories(xLabels);
	        style2();
	    }
	    
	    public void style1() { // 바 하나짜리 차트
	    	
	    	try {
				tiList = FXCollections.observableArrayList(its.selectTickChart());
			} catch (RemoteException e) {
				e.printStackTrace();
			}
	    	
	    	for (int i = 0; i < tiList.size(); i++) {
				
				if (tiList.get(i).getTicket_no().equals("1") || tiList.get(i).getTicket_no().equals("2")) {
					no1_total += Integer.parseInt(tiList.get(i).getPrice());
				}
				if(tiList.get(i).getTicket_no().equals("3") || tiList.get(i).getTicket_no().equals("4")) {
					no2_total += Integer.parseInt(tiList.get(i).getPrice());
				}
				if(tiList.get(i).getTicket_no().equals("5") || tiList.get(i).getTicket_no().equals("6")) {
					no3_total += Integer.parseInt(tiList.get(i).getPrice());
				
			}
		}
	    	
	        series = new XYChart.Series<String,Number>();
	        chart.getData().clear();
	        series.setName("판매량");
	        // 데이터 생성
	        series.getData().add(new XYChart.Data<String, Number>(xLabels.get(0), no1_total));
	        chart.getData().add(series);
	        
	        series.getData().add(new XYChart.Data<String, Number>(xLabels.get(2), no2_total));
	        chart.getData().add(series);
	    }
	    
	    
	    
	    public void style2() { 
	        
	        chart.getData().clear();
	        
	    	
	    	try {
				tiList = FXCollections.observableArrayList(its.selectTickChart());
			} catch (RemoteException e) {
				e.printStackTrace();
			}
	        
	        for (int i = 0; i < tiList.size(); i++) {
				
				if (tiList.get(i).getTicket_no().equals("1") || tiList.get(i).getTicket_no().equals("2")) {
					no1_total += Integer.parseInt(tiList.get(i).getPrice());
				}
				if(tiList.get(i).getTicket_no().equals("3") || tiList.get(i).getTicket_no().equals("4")) {
					no2_total += Integer.parseInt(tiList.get(i).getPrice());
				}
				if(tiList.get(i).getTicket_no().equals("5") || tiList.get(i).getTicket_no().equals("6")) {
					no3_total += Integer.parseInt(tiList.get(i).getPrice());
				
			}
		}
	        
	        String[] names = {"1개월권","3개월권","1년권"};
	        
	            series = new XYChart.Series<String,Number>(); // 시리즈를 4개 생성 
	            series.setName(names[0]); // 시리즈의 이름 4개 생성 []
	            series.getData().add(new XYChart.Data<String, Number>(xLabels.get(0), no1_total));
	            chart.getData().add(series);
	            
	            series = new XYChart.Series<String,Number>(); // 시리즈를 4개 생성 
	            series.setName(names[1]); // 시리즈의 이름 4개 생성 []
	            series.getData().add(new XYChart.Data<String, Number>(xLabels.get(0), no2_total));
	            chart.getData().add(series);
	            
	            series = new XYChart.Series<String,Number>(); // 시리즈를 4개 생성 
	            series.setName(names[2]); // 시리즈의 이름 4개 생성 []
	            series.getData().add(new XYChart.Data<String, Number>(xLabels.get(0), no3_total));
	            chart.getData().add(series);
	        
	        
	    }
	    
	}
