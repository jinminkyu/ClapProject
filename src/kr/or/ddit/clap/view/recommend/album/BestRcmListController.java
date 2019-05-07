package kr.or.ddit.clap.view.recommend.album;

import java.io.IOException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ResourceBundle;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import kr.or.ddit.clap.service.recommend.IRecommendService;
import kr.or.ddit.clap.vo.recommend.RecommendAlbumVO;

public class BestRcmListController implements Initializable {

	private Registry reg;
	private IRecommendService irs;
	private ObservableList<RecommendAlbumVO> recommendList;
	@FXML
	AnchorPane main;
	@FXML
	AnchorPane contents;
	@FXML
	VBox main_vbox;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		try {
			reg = LocateRegistry.getRegistry("localhost", 8888);
			irs = (IRecommendService) reg.lookup("recommend");
			recommendList = FXCollections.observableArrayList(irs.selectBestRecommendAlbum());
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
		
		// 높이 조절

		int contentsHeight = 900;
		if (recommendList.size() <= 4) {
			contents.setPrefHeight(contentsHeight);
		}

		else if (recommendList.size() > 4) {
			int temp_h = ((recommendList.size() - 4) / 2 + (recommendList.size() - 4) % 2);

			contentsHeight = contentsHeight + (500 * temp_h);
			contents.setPrefHeight(contentsHeight);
		}
			HBox hbox = null;

			for (int i = 0; i < recommendList.size(); i++) {
				int likeCnt = 0;
				int listCnt = 0;

				// PK
				String RcmAlbNo = recommendList.get(i).getRcm_alb_no();
				try {
					// 좋아요 수 구하는 쿼리
					likeCnt = irs.selectAlbumLikeCnt(RcmAlbNo);
					// 리스트의 개수구하는 쿼리
					listCnt = irs.selectAlbumListCnt(RcmAlbNo);
				} catch (RemoteException e) {
					e.printStackTrace();
				}

				if (i % 2 == 0) {
					// 큰 HBOX 생성
					hbox = new HBox();
					hbox.setPrefWidth(716);
					hbox.setPrefHeight(500);
					main_vbox.setMargin(hbox, new Insets(20, 0, 0, 30));
				}

				VBox vbox = new VBox();
				vbox.setPrefWidth(358);
				vbox.setPrefHeight(300);
				vbox.setStyle("-fx-background-color:#9c0000;");
				vbox.setStyle("-fx-border-color:#d3d3d3");
				hbox.setMargin(vbox, new Insets(0, 20, 0, 0));

				// 앨범이미지를 표시하는 ImageView
				ImageView iv_Album = new ImageView();
				Image img_Path = new Image(recommendList.get(i).getRcm_alb_image());
				iv_Album.setImage(img_Path);
				iv_Album.setFitWidth(358);
				iv_Album.setFitHeight(250);
				iv_Album.setOpacity(0.75);

				// Title 라벨
				Label title = new Label();
				title.setFont(Font.font("-윤고딕340", 18));
				title.setTextFill(Color.valueOf("#090948"));
				title.setPrefWidth(348);
				title.setPrefHeight(20);
				title.setWrapText(true);
				title.setPadding(new Insets(0, 0, 0, 15));
				title.setText(recommendList.get(i).getRcm_alb_name());

				title.setOnMouseClicked(e -> {
					// 화면전환 코드 작성

					if (e.getClickCount() > 1) {
						Label obj_label = (Label) e.getSource();

						for (int j = 0; j < recommendList.size(); j++) {

							// 앨범 이름이 같을 경우 잘못된 값이 들어갈 가능성이 있음 -> 앨범이름도 유효성을 걸어줘?
							if (obj_label.getText().equals(recommendList.get(j).getRcm_alb_name())) {
								String rcmAlbNo = recommendList.get(j).getRcm_alb_no();

								// 화면전환
								UserRcmDetailController.rcmAlbNo = rcmAlbNo;// 곡 번호를 변수로 넘겨줌
								FXMLLoader loader = new FXMLLoader(getClass().getResource("UserRcmDetail.fxml"));// init실행됨
								Parent userRcmDetailController;

								try {
									userRcmDetailController = loader.load();

									UserRcmDetailController cotroller = loader.getController();

									contents.getChildren().removeAll();
									contents.getChildren().setAll(userRcmDetailController);
								} catch (IOException e1) {
									e1.printStackTrace();
								}

							}
						}

					}
				});
				//랭킹
				Label rank = new Label();
				rank.setFont(Font.font("-윤고딕350", 18));
				rank.setTextFill(Color.valueOf("#9c0000"));
				rank.setPrefWidth(308);
				rank.setPrefHeight(20);
				rank.setWrapText(true);
				rank.setPadding(new Insets(5, 0, 5, 15));
				rank.setText("인기 TOP" + (i+1));
				
				// title 위의 아이콘담는 박스
				VBox icon_box = new VBox();
				icon_box.setPrefWidth(50);
				icon_box.setPrefHeight(50);
				icon_box.setAlignment(Pos.CENTER_LEFT);
				icon_box.setPadding(new Insets(10, 0, 10, 15));

				// icon_box 안의 아이콘
				FontAwesomeIcon icon = new FontAwesomeIcon();
				icon.setIconName(recommendList.get(i).getRcm_icon());
				icon.setSize("42");
				icon.setFill(Color.valueOf("#090948"));


				// title과 like를 담을 hbox
				HBox temp_hbox = new HBox();
				temp_hbox.setPrefWidth(308);
				temp_hbox.setPrefHeight(20);
				temp_hbox.setPadding(new Insets(5, 5, 5, 15));
				temp_hbox.setStyle("-fx-border-color:#d3d3d3;");
//				temp_hbox.setStyle("-fx-border-style : solid hidden hidden hidden;");
				vbox.setMargin(temp_hbox, new Insets(30, 0, 0, 0));

				// 좋아요 라벨
				Label like = new Label();
				like.setFont(Font.font("-윤고딕340", 14));
				like.setTextFill(Color.valueOf("#090948"));
				like.setPrefWidth(308);
				like.setPrefHeight(40);
				// like.setPadding(new Insets(20,0,0,30));
				String strLikeCnt = likeCnt + "";
				like.setText(strLikeCnt);

				// 좋아요 아이콘
				FontAwesomeIcon icon_Like = new FontAwesomeIcon();
				icon_Like.setIconName("HEART");
				icon_Like.setFill(Color.RED);
				icon_Like.setSize("20");
				like.setGraphic(icon_Like);

				// Title 곡 라벨 갯수
				Label cntMusic = new Label();
				cntMusic.setFont(Font.font("-윤고딕340", 14));
				cntMusic.setTextFill(Color.valueOf("#090948"));
				cntMusic.setPrefWidth(308);
				cntMusic.setPrefHeight(40);
				// cntMusic.setPadding(new Insets(20,0,0,30));
				cntMusic.setText(" " +listCnt + "곡");

				// Title 좋아요 아이콘
				FontAwesomeIcon icon_cntMusic = new FontAwesomeIcon();
				icon_cntMusic.setIconName("MUSIC");
				icon_cntMusic.setFill(Color.valueOf("#30cc00"));
				icon_cntMusic.setSize("20");
				cntMusic.setGraphic(icon_cntMusic);

				icon_box.getChildren().add(icon);
				
				temp_hbox.getChildren().addAll(like, cntMusic);
				vbox.getChildren().addAll(iv_Album, rank, icon_box, title, temp_hbox);
				hbox.getChildren().add(vbox);

				if (i % 2 == 0) {
					main_vbox.getChildren().add(hbox);

				} else if (i == recommendList.size()) { 
					main_vbox.getChildren().add(hbox);
				}

			}

		}
	

	@FXML
	public void InsertRecommendAlbum() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("InsertRecommendAlbum.fxml"));// init실행됨
		Parent insertRecommend;
		try {
			insertRecommend = loader.load();

			InsertRecommendAlbumController cotroller = loader.getController();
			cotroller.givePane(contents);

			main.getChildren().removeAll();
			main.getChildren().setAll(insertRecommend);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
