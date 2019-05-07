package kr.or.ddit.clap.view.member.mypage;

import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import kr.or.ddit.clap.service.musichistory.IMusicHistoryService;
import kr.or.ddit.clap.service.musicreview.IMusicReviewService;
import kr.or.ddit.clap.service.myalbum.IMyAlbumService;
import kr.or.ddit.clap.service.mypage.IMypageService;
import kr.or.ddit.clap.vo.member.MemberVO;
import kr.or.ddit.clap.vo.music.MusicHistoryVO;
import kr.or.ddit.clap.vo.music.MusicReviewVO;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;

import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import javafx.scene.control.TreeTableColumn;
import javafx.scene.text.Text;
import javafx.scene.layout.AnchorPane;

public class OtherMypageController implements Initializable{

	private Registry reg;
	
	public static String othermemid;
	
	private IMypageService ims;
	private IMusicReviewService imrs;
	private IMusicHistoryService imhs;

	private String temp_img_path = "";
	
	@FXML ImageView img_UserImg;
	@FXML Image img_User;
	@FXML Label label_Id;
	@FXML Text text_UserInfo;
	
	private int no1,no2,no3 =0;
	
	
	private ObservableList<MusicReviewVO> revList;
	@FXML JFXTreeTableView<MusicReviewVO> tbl_Review;
	@FXML TreeTableColumn<MusicReviewVO,String> col_ReviewCont;
	@FXML TreeTableColumn<MusicReviewVO,String> col_ReviewDate;
	
	private ObservableList<MusicHistoryVO> singList; // 최근음악 담기
	@FXML JFXTreeTableView<MusicHistoryVO> tbl_ManySigner;
	@FXML TreeTableColumn<MusicHistoryVO,String> col_MSno;
	@FXML TreeTableColumn<MusicHistoryVO,String> col_MSits;
	
	@FXML JFXTreeTableView<MusicHistoryVO> tbl_ManyMusic;
	@FXML TreeTableColumn<MusicHistoryVO,String> col_MMno;
	@FXML TreeTableColumn<MusicHistoryVO,String> col_MMits;
	@FXML TreeTableColumn<MusicHistoryVO,String> col_MMtitle;

	private ObservableList<MusicHistoryVO> newList;
	@FXML JFXTreeTableView<MusicHistoryVO> tbl_NewMusic;
	@FXML TreeTableColumn<MusicHistoryVO,String> col_NMno;
	@FXML TreeTableColumn<MusicHistoryVO,String> col_NMits;
	@FXML TreeTableColumn<MusicHistoryVO,String> col_NMtitle;
	@FXML TreeTableColumn<MusicHistoryVO,String> col_NMdate;

	@FXML AnchorPane main;


	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			reg = LocateRegistry.getRegistry("localhost", 8888);
			ims = (IMypageService) reg.lookup("mypage");
			imrs = (IMusicReviewService) reg.lookup("musicreview");
			imhs = (IMusicHistoryService) reg.lookup("history");
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
		
		
		label_Id.setText(othermemid);	//아이디 넣기
		MemberVO vo =  new MemberVO();
		vo.setMem_id(othermemid);
		MemberVO mvo = new MemberVO();
		try {
			mvo = ims.select(vo);
			text_UserInfo.setText(mvo.getMem_intro());//자기소개 넣기
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		if(mvo.getMem_image()==null) {
			mvo.setMem_image("file:\\\\Sem-pc\\공유폴더\\Clap\\img\\userimg\\icons8-person-64.png");
	   }
		//이미지 설정
		Image img = new Image(mvo.getMem_image());
		temp_img_path = mvo.getMem_image(); // sVO.getSing_image()를 전역으로 쓰기위해
		img_UserImg.setImage(img);

		
		// 최근댓글테이블
				col_ReviewCont.setCellValueFactory(
						param -> new SimpleStringProperty(param.getValue().getValue().getMus_re_content()));
				col_ReviewDate.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getIndate().substring(0, 10)));

				// 최근많이 들은 아티스트이름넣기
				//col_MSno.setCellValueFactory(param -> new SimpleStringProperty("" + (no1++)));
				col_MSits.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getName()));

				// 최근많이 들은 곡
			//	col_MMno.setCellValueFactory(param -> new SimpleStringProperty("" + (no2++)));
				col_MMits.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getName()));
				col_MMtitle.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getTitle()));

				// 최근 감상곡
			//	col_NMno.setCellValueFactory(param -> new SimpleStringProperty("" + (no3++)));
				col_NMits.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getName()));
				col_NMtitle.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getTitle()));
				col_NMdate.setCellValueFactory(
						param -> new SimpleStringProperty(param.getValue().getValue().getHisto_indate().substring(0, 10)));

				// 데이터 삽입
				MusicReviewVO muvo = new MusicReviewVO();
				muvo.setMem_id(othermemid);
				MusicHistoryVO muh = new MusicHistoryVO();
				muh.setMem_id(othermemid);
				try {
					revList = FXCollections.observableArrayList(imrs.selectReview(muvo));
					singList = FXCollections.observableArrayList(imhs.selectMayIts(muh));
					newList = FXCollections.observableArrayList(imhs.selectMayIndate(muh));
				} catch (RemoteException e) {
					System.out.println("에러");
					e.printStackTrace();
				}

				TreeItem<MusicReviewVO> root = new RecursiveTreeItem<>(revList, RecursiveTreeObject::getChildren);
				tbl_Review.setRoot(root);
				tbl_Review.setShowRoot(false);

				TreeItem<MusicHistoryVO> root1 = new RecursiveTreeItem<>(singList, RecursiveTreeObject::getChildren);
				tbl_ManySigner.setRoot(root1);
				tbl_ManySigner.setShowRoot(false);
				tbl_ManyMusic.setRoot(root1);
				tbl_ManyMusic.setShowRoot(false);

				TreeItem<MusicHistoryVO> root2 = new RecursiveTreeItem<>(newList, RecursiveTreeObject::getChildren);
				tbl_NewMusic.setRoot(root2);
				tbl_NewMusic.setShowRoot(false);
		
		
	}

}
