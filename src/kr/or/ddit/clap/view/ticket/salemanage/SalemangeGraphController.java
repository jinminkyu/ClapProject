package kr.or.ddit.clap.view.ticket.salemanage;

import java.io.IOException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Random;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXComboBox;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.AnchorPane;
import kr.or.ddit.clap.service.ticket.ITicketService;
import kr.or.ddit.clap.view.album.album.AlbumDetailController;
import kr.or.ddit.clap.vo.ticket.TicketBuyListVO;

public class SalemangeGraphController implements Initializable{
	private Registry reg;
	private ITicketService its;

	private int no1_total=0; //20대
	private int no2_total=0;
	private int no3_total=0;
	private int no11_total=0;//30~50대
	private int no21_total=0;
	private int no31_total=0;
	
	private int no12_total=0;//50~70세
	private int no22_total=0;
	private int no32_total=0;
	
	private int no14_total=0;//70세~
	private int no24_total=0;
	private int no34_total=0;
	@FXML private LineChart<String, Integer> lineChart;
	XYChart.Series<String, Integer> series = null;
	private ObservableList<TicketBuyListVO> tiList;
	
	 private BarChart<String, Number> chart;
	 private CategoryAxis xAxis; // X축
	 private NumberAxis yAxis; // Y축
	@FXML JFXComboBox<String> combo_Chart;
	private ObservableList<String> xLabels = FXCollections.observableArrayList();
	@FXML AnchorPane main;
	@FXML AnchorPane contents;

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

		combo_Chart.getItems().addAll("총 매출","연령별 매출");
		combo_Chart.setValue(combo_Chart.getItems().get(1));
		multichart();
	}

	public void multichart() {
		lineChart.getData().clear(); // 기존 라인 제거
		try {
			tiList = FXCollections.observableArrayList(its.selectTickChart());
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		for (int i = 0; i < tiList.size(); i++) { //20대
			if (Integer.parseInt(tiList.get(i).getTicket_buy_no()) > 0
					&& Integer.parseInt(tiList.get(i).getTicket_buy_no()) < 30) {
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
		}
		
		
		for (int i = 0; i < tiList.size(); i++) { //30대
			if (Integer.parseInt(tiList.get(i).getTicket_buy_no()) > 29
					&& Integer.parseInt(tiList.get(i).getTicket_buy_no()) < 60) {
				if (tiList.get(i).getTicket_no().equals("1") || tiList.get(i).getTicket_no().equals("2")) {
					no11_total += Integer.parseInt(tiList.get(i).getPrice());
				}
				if(tiList.get(i).getTicket_no().equals("3") || tiList.get(i).getTicket_no().equals("4")) {
					no21_total += Integer.parseInt(tiList.get(i).getPrice());
				}
				if(tiList.get(i).getTicket_no().equals("5") || tiList.get(i).getTicket_no().equals("6")) {
					no31_total += Integer.parseInt(tiList.get(i).getPrice());
				}
			}
		}
		
		
		for (int i = 0; i < tiList.size(); i++) { ///60~80세
			if (Integer.parseInt(tiList.get(i).getTicket_buy_no()) >59
					&& Integer.parseInt(tiList.get(i).getTicket_buy_no()) < 80) {
				if (tiList.get(i).getTicket_no().equals("1") || tiList.get(i).getTicket_no().equals("2")) {
					no12_total += Integer.parseInt(tiList.get(i).getPrice());
				}
				if(tiList.get(i).getTicket_no().equals("3") || tiList.get(i).getTicket_no().equals("4")) {
					no22_total += Integer.parseInt(tiList.get(i).getPrice());
				}
				if(tiList.get(i).getTicket_no().equals("5") || tiList.get(i).getTicket_no().equals("6")) {
					no32_total += Integer.parseInt(tiList.get(i).getPrice());
				}
			}
		}
		
		System.out.println(no11_total);
		
		series = null;
		series = new XYChart.Series<String, Integer>();
		series.getData().add(new XYChart.Data<String, Integer>("20대", no1_total));
		series.getData().add(new XYChart.Data<String, Integer>("30~50대", no11_total));
		series.getData().add(new XYChart.Data<String, Integer>("60~80대", no21_total));
		series.setName("1개월권");
		lineChart.getData().add(series);
		
		
		
		series = null;
		series = new XYChart.Series<String, Integer>();
		series.getData().add(new XYChart.Data<String, Integer>("20대", no2_total));
		series.getData().add(new XYChart.Data<String, Integer>("30~50대", no21_total));
		series.getData().add(new XYChart.Data<String, Integer>("60~80대", no22_total));
		series.setName("6개월권");
            lineChart.getData().add(series);
            

    		series = null;
    		series = new XYChart.Series<String, Integer>();
    		series.getData().add(new XYChart.Data<String, Integer>("20대", no3_total));
    		series.getData().add(new XYChart.Data<String, Integer>("30~50대", no31_total));
    		series.getData().add(new XYChart.Data<String, Integer>("60~80대", no32_total));
    		series.setName("1년권");
                lineChart.getData().add(series);
	
	}
	
	public void chartProd() throws IOException {//파이차트로 
		//lineChart.getData().clear(); // 기존 라인 제거
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("bar.fxml"));// init실행됨
		Parent charta= loader.load(); 
		contents.getChildren().removeAll();
		contents.getChildren().setAll(charta);
	
		  /*String[] cates = {"1개월권", "6개월권", "1년권"};
		XYChart.Series<String, Number>  series = new XYChart.Series<String,Number>();
		  for(int i = 0; i < cates.length; i++) {
		series.setName(cates[i]);
		for(int j = 0; j < cates.length; j ++) { // 시리즈 별 데이터 생성
            series.getData().add(new XYChart.Data<String, Number>(xLabels.get(j), random()));
        }
        // 차트에 추가
        chart.getData().add(series);*/
    
    
}
	
	@FXML public void combo_ch() {
		try {
			switch (combo_Chart.getValue()) {

			case "총 매출":
				FXMLLoader loader = new FXMLLoader(getClass().getResource("bar.fxml"));// init실행됨
				Parent barcharta= loader.load(); 
				contents.getChildren().removeAll();
				contents.getChildren().setAll(barcharta);
			
				break;
			case "연령별 매출":
				FXMLLoader loader1 = new FXMLLoader(getClass().getResource("graph.fxml"));// init실행됨
				Parent charts= loader1.load(); 
				main.getChildren().removeAll();
				main.getChildren().setAll(charts);
				break;
			default:
			
				break;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
