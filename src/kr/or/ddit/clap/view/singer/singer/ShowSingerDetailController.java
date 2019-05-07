/**
 *가수 리스트 상세보기를 출력하는 화면 controller
 * 
 * 
 * @author Hansoo
 *
 */
package kr.or.ddit.clap.view.singer.singer;

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
import kr.or.ddit.clap.service.singer.ISingerService;
import kr.or.ddit.clap.vo.singer.SingerVO;

public class ShowSingerDetailController implements Initializable {

	public static String singerNo;// 파라미터로 받은 선택한 가수의 PK
	private Registry reg;
	private ISingerService iss;
	private String temp_img_path = "";

	// 파라미터로 넘기기 위해 전역으로 선언
	public SingerVO sVO = null;
	public String str_like_cnt;
	public static AnchorPane contents;

	@FXML
	Label label_singNo;
	@FXML
	Label label_singerName1;
//	@FXML
//	Label label_singerName2;
	@FXML
	Label label_ActType;
	@FXML
	Label label_ActEra;
	@FXML
	Label label_DebutEra;
	@FXML
	Label label_DebutMus;
	@FXML
	Label label_Nation;
	@FXML
	Label label_LikeCnt;
	@FXML
	ImageView imgview_singImg;
	@FXML
	Label txt_intro;
	@FXML
	AnchorPane main;

	// ShowSingerList.fxml는 VBOX를 포함한 전부이기 때문에
	// 현재 씬의 VBox까지 모두 제거 후 ShowSingerList를 불러야함.
	public void givePane(AnchorPane contents) {
		this.contents = contents;
		System.out.println("contents 적용완료");
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		System.out.println("가수번호:" + singerNo);

		try {
			// reg로 ISingerService객체를 받아옴
			reg = LocateRegistry.getRegistry("localhost", 8888);
			iss = (ISingerService) reg.lookup("singer");
			sVO = iss.singerDetailInfo(singerNo);
			System.out.println(sVO.getSing_no());
			// 파라미터로 받은 정보를 PK로 상세정보를 가져옴
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}

		label_singerName1.setText(sVO.getSing_name());
//		label_singerName2.setText(sVO.getSing_name());
		label_ActType.setText(sVO.getSing_act_type());
		label_ActEra.setText(sVO.getSing_act_era());
		label_DebutEra.setText(sVO.getSing_debut_era());

		label_DebutMus.setText(sVO.getSing_debut_mus());
		label_Nation.setText(sVO.getSing_nation());
		txt_intro.setText(sVO.getSing_intro());

		Image img = new Image(sVO.getSing_image());
		System.out.println("이미지경로:" + sVO.getSing_image());

		temp_img_path = sVO.getSing_image(); // sVO.getSing_image()를 전역으로 쓰기위해
		imgview_singImg.setImage(img);

		// 좋아요 수를 가져오는 쿼리
		int like_cnt = 0;
		try {
			like_cnt = iss.selectSingerLikeCnt(singerNo);
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
		System.out.println("크게보기 버튼클릭");
		try {
			AnchorPane pane = FXMLLoader.load(getClass().getResource("SingerImgWiderDialog.fxml"));
			Stage stage = new Stage(StageStyle.UTILITY);
			Scene scene = new Scene(pane);
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			Stage primaryStage = (Stage) label_singerName1.getScene().getWindow();
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
	public void updateSinger() {
		try {
			System.out.println("업데이트");
			// 바뀔 화면(FXML)을 가져옴
			UpdateSingerController.singerNo = singerNo;// 가수번호를 변수로 넘겨줌

			FXMLLoader loader = new FXMLLoader(getClass().getResource("UpdateSinger.fxml"));// initialize실행됨
			Parent UpdateSinger = loader.load();
			UpdateSingerController cotroller = loader.getController();
			cotroller.initData(sVO, str_like_cnt);
			main.getChildren().removeAll();
			main.getChildren().setAll(UpdateSinger);

		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

	@FXML
	public void deleteSinger() {
		// Alert창을 출력해 정말 삭제할 지 물어봄
		try {
			if (0 > alertConfrimDelete()) {
				return;
			}

			int cnt = iss.deleteSinger(singerNo);
			System.out.println("삭제곡 번호" + singerNo);
			System.out.println("삭제 여부:" + cnt);
			if (cnt >= 1) {
				System.out.println("삭제성공");
			} else {
				System.out.println("삭제실패");

			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		// 화면전환
		FXMLLoader loader = new FXMLLoader(getClass().getResource("ShowSingerList.fxml"));
		Parent singerList;
		try {

			singerList = loader.load();
			// ShowSingerList.fxml는 VBOX를 포함한 전부이기 때문에
			// 현재 씬의 VBox까지 모두 제거 후 ShowSingerList를 불러야함.

			contents.getChildren().removeAll();
			// main.getChildren().removeAll();
			contents.getChildren().setAll(singerList);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	// 사용자가 확인을 누르면 1을 리턴 이외는 -1
	public int alertConfrimDelete() {
		Alert alertConfirm = new Alert(AlertType.CONFIRMATION);

		alertConfirm.setTitle("CONFIRMATION");
		alertConfirm.setContentText("정말로 삭제하시겠습니까?(해당 가수의 앨범 및 곡이 모두 삭제됩니다)");

		// Alert창을 보여주고 사용자가 누른 버튼 값 읽어오기
		ButtonType confirmResult = alertConfirm.showAndWait().get();

		if (confirmResult == ButtonType.OK) {
			System.out.println("OK 버튼을 눌렀습니다.");
			return 1;
		} else if (confirmResult == ButtonType.CANCEL) {
			System.out.println("취소 버튼을 눌렀습니다.");
			return -1;
		}
		return -1;
	}
}
