/**
 *가수 리스트 상세보기를 출력하는 화면 controller
 * 
 * 
 * @author Hansoo
 *
 */
package kr.or.ddit.clap.view.album.album;

import java.io.IOException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import kr.or.ddit.clap.service.album.IAlbumService;
import kr.or.ddit.clap.vo.album.AlbumVO;

public class AlbumDetailController implements Initializable {

	public static String albumNo;// 파라미터로 받은 선택한 가수의 PK
	private Registry reg;
	private IAlbumService ias;
	private String temp_img_path = "";

	// 파라미터로 넘기기 위해 전역으로 선언
	public AlbumVO aVO = null;
	public String str_like_cnt;
	public static AnchorPane contents;

	// @FXML Label label_singNo;
	@FXML
	Label label_albumName1;
//	@FXML
//	Label label_albumName2;
	@FXML
	Label label_singerName;
	@FXML
	Label label_saledate;
	@FXML
	Label label_saleEnter;
	@FXML
	Label label_entertain;
	@FXML
	Label label_LikeCnt;
	@FXML
	ImageView imgview_albumImg;
	@FXML
	Label txt_intro;
	@FXML
	AnchorPane main;

	// ShowSingerList.fxml는 VBOX를 포함한 전부이기 때문에
	// 현재 씬의 VBox까지 모두 제거 후 ShowSingerList를 불러야함.
	public void givePane(AnchorPane contents) {
		this.contents = contents;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		try {
			// reg로 ISingerService객체를 받아옴
			reg = LocateRegistry.getRegistry("localhost", 8888);
			ias = (IAlbumService) reg.lookup("album");
			aVO = ias.albumDetailInfo(albumNo);
			// 파라미터로 받은 정보를 PK로 상세정보를 가져옴
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}

		label_albumName1.setText(aVO.getAlb_name());
//		label_albumName2.setText(aVO.getAlb_name());
		label_singerName.setText(aVO.getSing_name());
		String temp_date = aVO.getAlb_saledate().substring(0, 10);
		label_saledate.setText(temp_date);
		label_saleEnter.setText(aVO.getAlb_sale_enter());

		label_entertain.setText(aVO.getAlb_entertain());
		txt_intro.setText(aVO.getAlb_intro());

		Image img = new Image(aVO.getAlb_image());
		temp_img_path = aVO.getAlb_image(); // aVO.getSing_image()를 전역으로 쓰기위해
		imgview_albumImg.setImage(img);

		// 좋아요 수를 가져오는 쿼리
		int like_cnt = 0;
		try {
			like_cnt = ias.selectAlbumLikeCnt(albumNo);
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		// 좋아요는 다른 VO에서 가져와야함...
		str_like_cnt = like_cnt + "";
		label_LikeCnt.setText(str_like_cnt);

	}

	@FXML
	public void wideView() {
		// img_wideimg
		try {
			AnchorPane pane = FXMLLoader.load(getClass().getResource("AlbummgWiderDialog.fxml"));
			Stage stage = new Stage(StageStyle.UTILITY);
			Scene scene = new Scene(pane);
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			Stage primaryStage = (Stage) label_albumName1.getScene().getWindow();
			stage.initOwner(primaryStage);
			stage.setResizable(false);

			ImageView img_wideimg = (ImageView) pane.lookup("#img_wideimg");
			Image temp_img = new Image(temp_img_path);
			img_wideimg.setImage(temp_img);

			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 수정화면으로 이동
	@FXML
	public void updateAlbum() {

		try {
			// 바뀔 화면(FXML)을 가져옴
			UpdateAlbumController.albumNo = albumNo;// 가수번호를 변수로 넘겨줌

			FXMLLoader loader = new FXMLLoader(getClass().getResource("UpdateAlbum.fxml"));// initialize실행됨
			Parent UpdateAlbum = loader.load();
			UpdateAlbumController cotroller = loader.getController();
			cotroller.initData(aVO, str_like_cnt);
			main.getChildren().removeAll();
			main.getChildren().setAll(UpdateAlbum);

		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

	@FXML
	public void deleteAlbum() {

		// Alert창을 출력해 정말 삭제할 지 물어봄
		try { if(0>alertConfrimDelete()) 
		{ return; }

		int cnt = ias.deleteAlbum(albumNo);
		if (cnt >= 1) {
		}

		else {

		}
	}
	catch(RemoteException e)
	{
		e.printStackTrace();
	}

	// 화면전환 
		FXMLLoader loader = new FXMLLoader(getClass().getResource("ShowAlbumLIst.fxml")); 
	Parent albumList;
		  try {
		  
			  albumList = loader.load(); //ShowSingerList.fxml는 VBOX를 포함한 전부이기 때문에 //현재 씬의
		//  VBox까지 모두 제거 후 ShowSingerList를 불러야함.
		  
		  contents.getChildren().removeAll(); // main.getChildren().removeAll();
		  contents.getChildren().setAll(albumList);
		  
		  } catch (IOException e) { e.printStackTrace(); }
		  
		  
		 
	}

	// 사용자가 확인을 누르면 1을 리턴 이외는 -1
	public int alertConfrimDelete() {
		Alert alertConfirm = new Alert(AlertType.CONFIRMATION);

		alertConfirm.setTitle("CONFIRMATION");
		alertConfirm.setContentText("정말로 삭제하시겠습니까?(해당 앨범 및 곡이 모두 삭제됩니다)");

		// Alert창을 보여주고 사용자가 누른 버튼 값 읽어오기
		ButtonType confirmResult = alertConfirm.showAndWait().get();

		if (confirmResult == ButtonType.OK) {
			return 1;
		} else if (confirmResult == ButtonType.CANCEL) {
			return -1;
		}
		return -1;
	}
}
