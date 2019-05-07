package kr.or.ddit.clap.vo.album;

import java.io.Serializable;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class AlbumVO extends RecursiveTreeObject<AlbumVO> implements Serializable{
	
	private String alb_no;
	private String alb_name;
	private String alb_saledate;
	private String alb_indate;
	private String alb_image;
	private String alb_intro;
	private String alb_sale_enter;
	private String alb_entertain;
	private String sing_no;
	private String sing_name;
	private ImageView imgView; 
	
	public ImageView getImgView() {
		this.imgView = new ImageView();
		Image img = new Image(alb_image);
		imgView.setImage(img);
		imgView.setFitWidth(120);
		imgView.setFitHeight(50);
		return imgView;
	}
	public void setImgView(ImageView imgView) {
		this.imgView = imgView;
	}
	public String getSing_name() {
		return sing_name;
	}
	public void setSing_name(String sing_name) {
		this.sing_name = sing_name;
	}
	public String getAlb_no() {
		return alb_no;
	}
	public void setAlb_no(String alb_no) {
		this.alb_no = alb_no;
	}
	public String getAlb_name() {
		return alb_name;
	}
	public void setAlb_name(String alb_name) {
		this.alb_name = alb_name;
	}
	public String getAlb_saledate() {
		return alb_saledate;
	}
	public void setAlb_saledate(String alb_saledate) {
		this.alb_saledate = alb_saledate;
	}
	public String getAlb_indate() {
		return alb_indate;
	}
	public void setAlb_indate(String alb_indate) {
		this.alb_indate = alb_indate;
	}
	public String getAlb_image() {
		return alb_image;
	}
	public void setAlb_image(String alb_image) {
		this.alb_image = alb_image;
	}
	public String getAlb_intro() {
		return alb_intro;
	}
	public void setAlb_intro(String alb_intro) {
		this.alb_intro = alb_intro;
	}
	public String getAlb_sale_enter() {
		return alb_sale_enter;
	}
	public void setAlb_sale_enter(String alb_sale_enter) {
		this.alb_sale_enter = alb_sale_enter;
	}
	public String getAlb_entertain() {
		return alb_entertain;
	}
	public void setAlb_entertain(String alb_entertain) {
		this.alb_entertain = alb_entertain;
	}
	public String getSing_no() {
		return sing_no;
	}
	public void setSing_no(String sing_no) {
		this.sing_no = sing_no;
	}

}	
