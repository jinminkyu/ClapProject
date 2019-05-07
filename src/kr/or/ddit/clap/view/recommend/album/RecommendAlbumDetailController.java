package kr.or.ddit.clap.view.recommend.album;

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

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import kr.or.ddit.clap.service.recommend.IRecommendService;
import kr.or.ddit.clap.view.singer.singer.UpdateSingerController;
import kr.or.ddit.clap.vo.music.MusicVO;
import kr.or.ddit.clap.vo.recommend.RecommendAlbumListVO;
import kr.or.ddit.clap.vo.recommend.RecommendAlbumVO;

public class RecommendAlbumDetailController implements Initializable {


	@FXML AnchorPane main;
	@FXML Label labelrcmName1;
	@FXML Label labelrcmName2;
	@FXML ImageView imgview_img;
	@FXML VBox main_vbox;
	@FXML Label label_rcmContents;
	@FXML JFXTreeTableView<MusicVO> tbl_music;
	@FXML TreeTableColumn<MusicVO, String> col_MusicName;
	@FXML TreeTableColumn<MusicVO, String> col_AlbName;
	@FXML TreeTableColumn<MusicVO, String> col_SingerName;
	@FXML TreeTableColumn<MusicVO, JFXButton> col_Deletebtn;
	@FXML TreeTableColumn<MusicVO, String> col_MusicNo;
	@FXML Label lable_cntMusic;
	@FXML Label label_LikeCnt;
	@FXML AnchorPane contents;
	@FXML FontAwesomeIcon icon;
	private Registry reg;
	private IRecommendService irs;
	public static String rcmAlbNo; //추천앨범번호 pk값 
	private String temp_img_path = "";
	public  ObservableList<MusicVO> musicList;
	public RecommendAlbumVO rVO;
	
	public void givePane(AnchorPane contents) {
		this.contents = contents;
		System.out.println("contents 적용완료");
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		System.out.println("추천앨범번호:" + rcmAlbNo);
		try {
			reg = LocateRegistry.getRegistry("localhost", 8888);
			irs = (IRecommendService) reg.lookup("recommend");
			
			//추천앨범 상세정보 가져오는 쿼리
			 rVO=irs.selectRecommendAlbumDetail(rcmAlbNo);
			
			//값 세팅 
			labelrcmName1.setText(rVO.getRcm_alb_name());
			labelrcmName2.setText(rVO.getRcm_alb_name());
			
			label_rcmContents.setText(rVO.getRcm_content());
			icon.setIconName(rVO.getRcm_icon());
			
			//이미지 세팅
			temp_img_path = rVO.getRcm_alb_image();
			Image img = new Image(temp_img_path);
			imgview_img.setImage(img);
		
			
			//좋아요 카운트 쿼리
			int likeCnt =  irs.selectAlbumLikeCnt(rcmAlbNo);
			label_LikeCnt.setText(likeCnt+"");
			
			
			//추천앨범no를 통해서 해당 추천앨법 곡을 가져오는 쿼리
			musicList = FXCollections.observableArrayList(irs.SelectRcmMusicList(rcmAlbNo));
			System.out.println("해당 추천앨범 곡 개수 :"+musicList.size());

			lable_cntMusic.setText(musicList.size()+"곡");
			
			 TreeItem<MusicVO> root = new RecursiveTreeItem<>(musicList, RecursiveTreeObject::getChildren);
			 tbl_music.setRoot(root);
			 tbl_music.setShowRoot(false);
		
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
		
		
	
		
		
		//테이블 컬럼 값 매핑
		tbl_music.setPlaceholder(new Label("선택된 곡이 없습니다."));
		
		musicList = FXCollections.observableArrayList();

		col_MusicName
		.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getMus_title()));
		
		col_AlbName
				.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getAlb_name()));

		col_SingerName
				.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getSing_name()));

		col_MusicNo
				.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getMus_no()));

		
		col_Deletebtn.setCellValueFactory(
				param -> new SimpleObjectProperty<JFXButton>(param.getValue().getValue().getBtn()));
		
	}

	
	//수정화면
	@FXML public void updateRcmAlbum() {
		try {
			System.out.println("업데이트");
			// 바뀔 화면(FXML)을 가져옴
			UpdateRecommendAlbumController.rcmAlbNo = rcmAlbNo;// 가수번호를 변수로 넘겨줌

			FXMLLoader loader = new FXMLLoader(getClass().getResource("UpdateRecommendAlbum.fxml"));// initialize실행됨
			Parent updateRecommendAlbum = loader.load();
			UpdateRecommendAlbumController cotroller = loader.getController();
			String str_like_cnt = label_LikeCnt.getText();
			cotroller.initData(rVO, str_like_cnt);
			main.getChildren().removeAll();
			main.getChildren().setAll(updateRecommendAlbum);

		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
	}

	
		//삭제버튼입니다.
	@FXML public void cancel() {
		// Alert창을 출력해 정말 삭제할 지 물어봄
				try {
					if (0 > alertConfrimDelete()) {
						return;
					}
					System.out.println("dddd" + rcmAlbNo);
					int cnt = irs.deleteRecommendAlbum(rcmAlbNo);
					System.out.println("삭제곡 앨범" + rcmAlbNo);
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
				FXMLLoader loader = new FXMLLoader(getClass().getResource("RecommendAlbumList.fxml"));
				Parent recommendAlbumList;
				try {

					recommendAlbumList = loader.load();

					contents.getChildren().removeAll();
					contents.getChildren().setAll(recommendAlbumList);

				} catch (IOException e) {
					e.printStackTrace();
				}
		
	}
	
	//크게보기 버튼 
	@FXML public void wideView() {
		// img_wideimg
				System.out.println("크게보기 버튼클릭");
				try {
					AnchorPane pane = FXMLLoader.load(getClass().getResource("AlbummgWiderDialog.fxml"));
					Stage stage = new Stage();
					Scene scene = new Scene(pane);
					stage.setScene(scene);
					stage.initModality(Modality.APPLICATION_MODAL);
					Stage primaryStage = (Stage) label_rcmContents.getScene().getWindow();
					stage.initOwner(primaryStage);
					stage.setWidth(500);
					stage.setHeight(600);

					ImageView img_wideimg = (ImageView) pane.lookup("#img_wideimg");
					Image temp_img = new Image(temp_img_path);
					img_wideimg.setImage(temp_img);

					stage.show();
				} catch (IOException e) {
					e.printStackTrace();
				}
	}
	
	// 사용자가 확인을 누르면 1을 리턴 이외는 -1
		public int alertConfrimDelete() {
			Alert alertConfirm = new Alert(AlertType.CONFIRMATION);

			alertConfirm.setTitle("CONFIRMATION");
			alertConfirm.setContentText("정말로 삭제하시겠습니까?(추천앨범이 삭제됩니다)");

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
