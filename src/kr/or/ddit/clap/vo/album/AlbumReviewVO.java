package kr.or.ddit.clap.vo.album;

import java.io.Serializable;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public class AlbumReviewVO extends RecursiveTreeObject<AlbumReviewVO>  implements Serializable{

	
	
	private String alb_re_no;
	private String alb_re_content;
	private String alb_re_indate;
	private String mem_id;
	private String alb_no;
	
	/**
	 * 현지 마이페이지 리뷰를 위해 추가
	 */
	private String alb_name;
	private String alb_image;
	private String sing_name;
	private ImageView imgView;
	private JFXButton btnDel;
	
	
	
	public String getAlb_name() {
		return alb_name;
	}
	public void setAlb_name(String alb_name) {
		this.alb_name = alb_name;
	}
	public String getAlb_image() {
		return alb_image;
	}
	public void setAlb_image(String alb_image) {
		this.alb_image = alb_image;
	}
	public String getSing_name() {
		return sing_name;
	}
	public void setSing_name(String sing_name) {
		this.sing_name = sing_name;
	}
	public ImageView getImgView() {
		this.imgView = new ImageView();
		//Image img = new Image("file:\\\\Sem-pc\\공유폴더\\Clap\\img\\noImg.png");
		Image img = new Image(alb_image);
		imgView.setImage(img);
		imgView.setFitWidth(100);
		imgView.setFitHeight(70);
	
		return imgView;
	}
	public void setImgView(ImageView imgView) {
		this.imgView = imgView;
	}
	
	
	
	
	public void createButtonImg() {
		if(btnDel ==null) {
		this.btnDel = new JFXButton();
		FontAwesomeIcon remove = new FontAwesomeIcon();
		remove.setIconName("REMOVE");
		remove.setFill(Color.valueOf("#9c0000"));
		remove.setSize("20");
		btnDel.setId(alb_re_no);
		btnDel.setGraphic(remove);
		}
	}
	public JFXButton getBtnDel() {
		createButtonImg();
		return this.btnDel;
	}
	public void setBtnDel(JFXButton btnDel) {
		this.btnDel = btnDel;
	}
	public String getAlb_re_no() {
		return alb_re_no;
	}
	public void setAlb_re_no(String alb_re_no) {
		this.alb_re_no = alb_re_no;
	}
	public String getAlb_re_content() {
		return alb_re_content;
	}
	public void setAlb_re_content(String alb_re_content) {
		this.alb_re_content = alb_re_content;
	}
	public String getAlb_re_indate() {
		return alb_re_indate;
	}
	public void setAlb_re_indate(String alb_re_indate) {
		this.alb_re_indate = alb_re_indate;
	}
	public String getMem_id() {
		return mem_id;
	}
	public void setMem_id(String mem_id) {
		this.mem_id = mem_id;
	}
	public String getAlb_no() {
		return alb_no;
	}
	public void setAlb_no(String alb_no) {
		this.alb_no = alb_no;
	}
	
}
