package kr.or.ddit.clap.vo.member;

import java.io.Serializable;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public class LikeVO  extends RecursiveTreeObject<LikeVO> implements Serializable{

	
	private String mus_no;
	private String alb_no;
	private String sing_no;
	private String rcm_alb_no;
	
	private String like_date;
	private String mus_title;
	private String sing_name;
	private String alb_name;
	private String sing_image;
	private String mem_id;
	private ImageView imgView; 
	private JFXCheckBox chBox;
	private JFXButton mubtnLike;
	private JFXButton albbtnLike;
	private JFXButton itsbtnLike;
	private JFXButton rcmbtnLike;
	
	
	public void createButtonImg1() {
		if(mubtnLike ==null) {
		this.mubtnLike = new JFXButton();
		FontAwesomeIcon remove = new FontAwesomeIcon();
		remove.setIconName("HEART");
		remove.setFill(Color.valueOf("#9c0000"));
		remove.setSize("20");
		mubtnLike.setId(mus_no);
		mubtnLike.setGraphic(remove);
		}
	}
	public JFXButton getMubtnLike() {
		createButtonImg1();
		return mubtnLike;
	}
	public void setMubtnLike(JFXButton mubtnLike) {
		this.mubtnLike = mubtnLike;
	}

	
	
	public String getSing_no() {
		return sing_no;
	}
	public void setSing_no(String sing_no) {
		this.sing_no = sing_no;
	}
	
	
	public void createButtonImg3() {
		if(rcmbtnLike ==null) {
		this.rcmbtnLike = new JFXButton();
		FontAwesomeIcon remove = new FontAwesomeIcon();
		remove.setIconName("HEART");
		remove.setFill(Color.valueOf("#9c0000"));
		remove.setSize("20");
		rcmbtnLike.setId(rcm_alb_no);
		rcmbtnLike.setGraphic(remove);
		}
	}
	public JFXButton getRcmbtnLike() {
		createButtonImg3();
		return rcmbtnLike;
		}
	public void setRcmbtnLike(JFXButton rcmbtnLike) {
		this.rcmbtnLike = rcmbtnLike;
	}
	
	
	
	public String getRcm_alb_no() {
		return rcm_alb_no;
	}
	public void setRcm_alb_no(String rcm_alb_no) {
		this.rcm_alb_no = rcm_alb_no;
	}
	public String getAlb_no() {
		return alb_no;
	}
	public void setAlb_no(String alb_no) {
		this.alb_no = alb_no;
	}


	public void createButtonImg() {
		if(albbtnLike ==null) {
		this.albbtnLike = new JFXButton();
		FontAwesomeIcon remove = new FontAwesomeIcon();
		remove.setIconName("HEART");
		remove.setFill(Color.valueOf("#9c0000"));
		remove.setSize("20");
		albbtnLike.setId(alb_no);
	//	btn.setId(id+"");
		//id++;
		albbtnLike.setGraphic(remove);
		}
	}
	public JFXButton getAlbbtnLike() {
		createButtonImg();
		return albbtnLike;
	}
	public void setAlbbtnLike(JFXButton albbtnLike) {
		this.albbtnLike = albbtnLike;
	}
	
	
	public void createButtonImg2() {
		if(itsbtnLike ==null) {
		this.itsbtnLike = new JFXButton();
		FontAwesomeIcon remove = new FontAwesomeIcon();
		remove.setIconName("HEART");
		remove.setFill(Color.valueOf("#9c0000"));
		remove.setSize("20");
		itsbtnLike.setId(sing_no);
		itsbtnLike.setGraphic(remove);
		}
	}
	public JFXButton getItsbtnLike() {
		createButtonImg2();
		return itsbtnLike;
	}
	public void setItsbtnLike(JFXButton itsbtnLike) {
		this.itsbtnLike = itsbtnLike;
	}
	
	public String getMus_no() {
		return mus_no;
	}
	public void setMus_no(String mus_no) {
		this.mus_no = mus_no;
	}
	public String getlike_date() {
		return like_date;
	}
	public void setlike_date(String like_date) {
		this.like_date = like_date;
	}
	public String getMus_title() {
		return mus_title;
	}
	public void setMus_title(String mus_title) {
		this.mus_title = mus_title;
		
	}
	public String getSing_name() {
		return sing_name;
	}
	public void setSing_name(String sing_name) {
		this.sing_name = sing_name;
	}
	public String getAlb_name() {
		return alb_name;
	}
	public void setAlb_name(String alb_name) {
		this.alb_name = alb_name;
	}
	public String getSing_image() {
		return sing_image;
	}
	public void setSing_image(String sing_image) {
		this.sing_image = sing_image;
	}
	public String getMem_id() {
		return mem_id;
	}
	public void setMem_id(String mem_id) {
		this.mem_id = mem_id;
	}
	public ImageView getImgView() {
		this.imgView = new ImageView();
		//Image img = new Image("file:\\\\Sem-pc\\공유폴더\\Clap\\img\\noImg.png");
		Image img = new Image(sing_image);
		imgView.setImage(img);
		imgView.setFitWidth(100);
		imgView.setFitHeight(70);
		
		return imgView;
	}
	public void setImgView(ImageView imgView) {
		this.imgView = imgView;
	}
	
	
	
	public void createChbox() {
		if(chBox ==null) {
			
		this.chBox = new JFXCheckBox();
		chBox.setCheckedColor(Color.valueOf("#9c0000"));
		chBox.setId(mus_no);
		}
	}
	public JFXCheckBox getChBox() {
		createChbox();
		return this.chBox;
	}
	public JFXCheckBox getchBox1() {
		return this.chBox;
	}
	public void setChBox(JFXCheckBox chBox) {
		this.chBox = chBox;
	}
	
	
	
}