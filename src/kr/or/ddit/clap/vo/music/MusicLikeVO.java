package kr.or.ddit.clap.vo.music;

import java.io.Serializable;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class MusicLikeVO extends RecursiveTreeObject<MusicLikeVO> implements Serializable{
	
	private String mem_id;
	private String mus_no;
	private String mus_like_date;
	private String title;
	private String sname;
	private String aname; 
	private ImageView imgView;
	
	
	public ImageView getImgView() {
		this.imgView = new ImageView();
		Image img = new Image(sing_image);
		imgView.setImage(img);
		imgView.setFitWidth(120);
		imgView.setFitHeight(50);
		return imgView;
	}
	public void setImgView(ImageView imgView) {
		this.imgView = imgView;
	}
	public String getSing_image() {
		return sing_image;
	}
	public void setSing_image(String sing_image) {
		this.sing_image = sing_image;
	}
	private String sing_image;
	

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSname() {
		return sname;
	}
	public void setSname(String sname) {
		this.sname = sname;
	}
	public String getAname() {
		return aname;
	}
	public void setAname(String aname) {
		this.aname = aname;
	}
	public String getMem_id() {
		return mem_id;
	}
	public void setMem_id(String mem_id) {
		this.mem_id = mem_id;
	}
	public String getMus_no() {
		return mus_no;
	}
	public void setMus_no(String mus_no) {
		this.mus_no = mus_no;
	}
	public String getMus_like_date() {
		return mus_like_date;
	}
	public void setMus_like_date(String mus_like_date) {
		this.mus_like_date = mus_like_date;
	}
	
}
