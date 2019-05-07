package kr.or.ddit.clap.view.recommend.album;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import kr.or.ddit.clap.main.LoginSession;
import kr.or.ddit.clap.service.recommend.IRecommendService;
import kr.or.ddit.clap.vo.music.MusicVO;
import kr.or.ddit.clap.vo.recommend.RecommendAlbumVO;

public class InsertRecommendAlbumController implements Initializable {

	@FXML
	ImageView imgview_img;
	@FXML
	AnchorPane main;
	@FXML
	JFXTreeTableView<MusicVO> tbl_music;
	@FXML
	TextArea txt_rcmContents;
	@FXML
	JFXTextField txt_rcmName;
	@FXML
	Label lable_cntMusic;

	@FXML
	TreeTableColumn<MusicVO, String> col_MusicName;
	@FXML
	TreeTableColumn<MusicVO, String> col_AlbName;
	@FXML
	TreeTableColumn<MusicVO, String> col_SingerName;
	@FXML
	TreeTableColumn<MusicVO, String> col_MusicNo;
	@FXML
	TreeTableColumn<MusicVO, JFXButton> col_Deletebtn;
	@FXML
	FontAwesomeIcon icon;
	@FXML
	VBox icon1, icon2, icon3, icon4, icon5, icon6, icon7, icon8, icon9, icon10;
	@FXML
	FontAwesomeIcon iicon1, iicon2, iicon3, iicon4, iicon5, iicon6, iicon7, iicon8, iicon9, iicon10;
	@FXML
	JFXButton btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btn10;
	@FXML
	JFXButton btn_icon;
	@FXML
	VBox icon_box, btn_box;
	
	private FileChooser fileChooser;
	private File filePath;
	private String img_path;
	private Registry reg;
	private IRecommendService irs;
	public  AnchorPane contents;
	
	public  ObservableList<MusicVO> musicList;
	@FXML VBox main_vbox;
	
	public void givePane(AnchorPane contents) {
		this.contents = contents;
		System.out.println("contents 적용완료");
	}
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {
			reg = LocateRegistry.getRegistry("localhost", 8888);
			irs = (IRecommendService) reg.lookup("recommend");
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
		
		
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
		
		
		btn_icon.setOnAction(e->{
			icon_box.setVisible(true);
			btn_box.setVisible(true);
		});
		
		btn1.setOnAction(e->{
			icon.setIconName(iicon1.getIconName());
			icon_box.setVisible(false);
			btn_box.setVisible(false);
		});
		btn2.setOnAction(e->{
			icon.setIconName(iicon2.getIconName());
			icon_box.setVisible(false);
			btn_box.setVisible(false);
		});
		btn3.setOnAction(e->{
			icon.setIconName(iicon3.getIconName());
			icon_box.setVisible(false);
			btn_box.setVisible(false);
		});
		btn4.setOnAction(e->{
			icon.setIconName(iicon4.getIconName());
			icon_box.setVisible(false);
			btn_box.setVisible(false);
		});
		btn5.setOnAction(e->{
			icon.setIconName(iicon5.getIconName());
			icon_box.setVisible(false);
			btn_box.setVisible(false);
		});
		btn6.setOnAction(e->{
			icon.setIconName(iicon6.getIconName());
			icon_box.setVisible(false);
			btn_box.setVisible(false);
		});
		btn7.setOnAction(e->{
			icon.setIconName(iicon7.getIconName());
			icon_box.setVisible(false);
			btn_box.setVisible(false);
		});
		btn8.setOnAction(e->{
			icon.setIconName(iicon8.getIconName());
			icon_box.setVisible(false);
			btn_box.setVisible(false);
		});
		btn9.setOnAction(e->{
			icon.setIconName(iicon9.getIconName());
			icon_box.setVisible(false);
			btn_box.setVisible(false);
		});
		btn10.setOnAction(e->{
			icon.setIconName(iicon10.getIconName());
			icon_box.setVisible(false);
			btn_box.setVisible(false);
		});
	}

	public void select1() {
		icon1.setStyle("-fx-background-color: #8e8ef1");
	}
	public void select2() {
		icon2.setStyle("-fx-background-color: #8e8ef1");
	}
	public void select3() {
		icon3.setStyle("-fx-background-color: #8e8ef1");
	}
	public void select4() {
		icon4.setStyle("-fx-background-color: #8e8ef1");
	}
	public void select5() {
		icon5.setStyle("-fx-background-color: #8e8ef1");
	}
	
	
	public void select6() {
		icon6.setStyle("-fx-background-color: #8e8ef1");
	}
	public void select7() {
		icon7.setStyle("-fx-background-color: #8e8ef1");
	}
	public void select8() {
		icon8.setStyle("-fx-background-color: #8e8ef1");
	}
	public void select9() {
		icon9.setStyle("-fx-background-color: #8e8ef1");
	}
	public void select10() {
		icon10.setStyle("-fx-background-color: #8e8ef1");
	}
	
	
	public void exit1() {
		icon1.setStyle("-fx-background-color: white");
	}
	public void exit2() {
		icon2.setStyle("-fx-background-color: white");
	}
	public void exit3() {
		icon3.setStyle("-fx-background-color: white");
	}
	public void exit4() {
		icon4.setStyle("-fx-background-color: white");
	}
	public void exit5() {
		icon5.setStyle("-fx-background-color: white");
	}
	
	public void exit6() {
		icon6.setStyle("-fx-background-color: white");
	}
	public void exit7() {
		icon7.setStyle("-fx-background-color: white");
	}
	public void exit8() {
		icon8.setStyle("-fx-background-color: white");
	}
	public void exit9() {
		icon9.setStyle("-fx-background-color: white");
	}
	public void exit10() {
		icon10.setStyle("-fx-background-color: white");
	}
	
	
	
	// 인서트하는 메서드
	public void insertRcmAlbum() {
		//유효성
		if(txt_rcmName.getText().isEmpty()) {
			errMsg("추천앨범 명은 필수 입력 사항입니다.");
			return;
		}
		img_path = (img_path==null)?"":img_path;
		if(img_path.isEmpty()) {
			errMsg("추천앨범 이미지는 필수 입력 사항입니다.");
			return;
		}

		if(musicList.size() < 0) {
			errMsg("1개 이상 곡을 추가해야 합니다.");
			return;
		}
		
		//인서트
		RecommendAlbumVO rVO = new RecommendAlbumVO();
		
		rVO.setRcm_alb_name(txt_rcmName.getText());
		rVO.setRcm_content(txt_rcmContents.getText());
		rVO.setMem_id(LoginSession.session.getMem_id());
		rVO.setRcm_alb_image(img_path);
		rVO.setRcm_icon(icon.getIconName());
		
		
		 
		try {
			irs.insertRecommendAlbum(rVO);				//추천앨범 생성쿼리 
			String sequence = irs.selectSequence();     //현재 시퀀스를 조회하는 쿼리 추가
			System.out.println("현재 시퀀스:"+sequence);
	
			//추천앨범곡을 추가  (시퀀스와 곡 넘버)
			for(int i  = 0; i<musicList.size(); i++) {
				Map<String, String> RcmMusicMap = new HashMap<String, String>();
				RcmMusicMap.put( "rcmAlbNo", sequence);
				RcmMusicMap.put( "musicNo",musicList.get(i).getMus_no());
				System.out.println("rcmAlbNo:"+sequence + "musicNo"+ musicList.get(i).getMus_no() );
				irs.insertRecommendAlbumMusic(RcmMusicMap);
				System.out.println("추천앨범리스트 생성 성공");
				
			}
			//인서트
			FXMLLoader loader = new FXMLLoader(getClass().getResource("RecommendAlbumList.fxml"));// init실행됨
			Parent recommendAlbumList;
			recommendAlbumList = loader.load();
			contents.getChildren().removeAll();
			contents.getChildren().setAll(recommendAlbumList);
		
		
		
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void cancel() {

		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("RecommendAlbumList.fxml"));// init실행됨
			Parent recommendAlbumList;
			recommendAlbumList = loader.load();
			contents.getChildren().removeAll();
			contents.getChildren().setAll(recommendAlbumList);
		}catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	// 추천앨범 목록에 곡을 추가하는 모달창 띄우는 메서드
	public void btn_insertMusic() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("SelectMusic.fxml"));
			Parent selectMusic = loader.load();

			// Controller를 받아온다.
			SelectMusicController cotroller = loader.getController();

			// Controller에 setcontroller메소드를 정의한다.
			// 이 메소드는 this로 받은 자기자신객체를 Controller객체의 멤버변수로 set한다.
			cotroller.setcontroller(this);

			Stage stage = new Stage();
			Scene scene = new Scene(selectMusic);
			stage.setScene(scene);
			//stage.initModality(Modality.APPLICATION_MODAL);
			//Stage primaryStage = (Stage) txt_rcmName.getScene().getWindow();
			//stage.initOwner(primaryStage);
			stage.setWidth(600);
			stage.setHeight(600);
			stage.show();
		} catch (Exception e) {
		}

	}

	// 앨범 이미지 선택
	public void btn_insertImg(ActionEvent event) {
		 Stage stage =  (Stage) ((Node)event.getSource()).getScene().getWindow();
		 fileChooser = new FileChooser();
		 fileChooser.setTitle("Open AlbumImage");
		 
		 String userDirectoryString = "\\\\Sem-pc\\공유폴더\\Clap\\img\\album";
		 
		 System.out.println("userDirectoryString:" + userDirectoryString);
		 File userDirectory = new File(userDirectoryString); 
		 
		 if(!userDirectory.canRead()) { //예외시?
			 userDirectory = new File("c:/");
		 }
		 
		 fileChooser.setInitialDirectory(userDirectory); 
		 
		 this.filePath = fileChooser.showOpenDialog(stage);
		 
		
		 //이미지를 새로운 이미지로 바꿈
		 try {
			 BufferedImage bufferedImage = ImageIO.read(filePath);
			 Image image =  SwingFXUtils.toFXImage(bufferedImage, null);
			 imgview_img.setImage(image);
			 System.out.println("파일경로:" + filePath);
			 String str_filePath = "file:"+filePath;
			 img_path = str_filePath;
			 System.out.println(img_path);
			 
		 }catch (Exception e) {
			 System.out.println("이미지를 선택하지 않았습니다.");
		 }
	}
	
	 //자식창에서 더블클릭 시 곡 테이블에 해당 곡이 추가되어야하는 메서드를 작성
	 public void addMusic(MusicVO vo) {
		 
		 for(int i=0; i<musicList.size(); i++) {
			 if(vo.getMus_no().equals(musicList.get(i).getMus_no())) {
				 errMsg("해당 곡은 리스트에 존재합니다.");
				System.out.println("해당 곡은 리스트에 존재합니다.");
				 return;
			 }
		 }
		 musicList.add(vo);
		 TreeItem<MusicVO> root = new RecursiveTreeItem<>(musicList, RecursiveTreeObject::getChildren);
		 tbl_music.setRoot(root);
		 tbl_music.setShowRoot(false);
		 String cnt_add= musicList.size()+"곡";
		 lable_cntMusic.setText(cnt_add);
		 
		 for(int i=0; i<musicList.size(); i++) {
			 System.out.println("포문시작");
			 
			 tbl_music.getTreeItem(i).getValue().getBtn().setOnAction(e->{
					
				 	
				 Button temp_btn = (Button) e.getSource();
				 
				 for(int j =0; j<musicList.size(); j++) {
					 
					 if(temp_btn.getId().equals(tbl_music.getTreeItem(j).getValue().getBtn().getId())) {
						 musicList.remove(j);
						 System.out.println("남은 개수:"+musicList.size());
						 
						 //다시 설정
						 String cnt_remove = musicList.size()+"곡";
						 lable_cntMusic.setText(cnt_remove);
						 TreeItem<MusicVO> root1 = new RecursiveTreeItem<>(musicList, RecursiveTreeObject::getChildren);
						 tbl_music.setRoot(root1);
						 tbl_music.setShowRoot(false);
						 
						 return;
					 }
					
					 
					 
				 }
				 
			 });
		 }
	 }
	 //유효성 체크 메서드
	 public void errMsg(String msg) {
			Alert errAlert = new Alert(AlertType.ERROR);
			errAlert.setTitle("유효성 검사");
			errAlert.setHeaderText("유효성 검사");
			errAlert.setContentText(msg);
			errAlert.showAndWait();
		}
		
	 
	 
	 
	
}
