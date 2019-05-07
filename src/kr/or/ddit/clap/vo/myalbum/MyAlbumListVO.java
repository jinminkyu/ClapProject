package kr.or.ddit.clap.vo.myalbum;

import java.io.Serializable;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class MyAlbumListVO  extends RecursiveTreeObject<MyAlbumListVO> implements Serializable{
	
	private String myalb_list_no;
	private String myalb_no;
	private String mus_no;
	private String myalb_list_indate;
	private String mus_title;
	private String alb_image;
	private String sing_name;
	private String myalb_name;
	private String mem_id;
	private JFXCheckBox chBox;
	private ImageView imgView;
	private VBox musicVbox;

	
	public VBox getMusicVbox() {
		this.musicVbox = new VBox();
		Label sigerName = new Label(sing_name);
		sigerName.setFont(Font.font("-윤고딕330", 12));
		sigerName.setTextFill(Color.valueOf("#dcdcdc"));
		
		Label musicName = new Label(mus_title);
		musicName.setFont(Font.font("-윤고딕350", 14));
		musicVbox.getChildren().addAll(musicName,sigerName);
		return this.musicVbox;
	}
	public void setMusicVbox(VBox musicVbox) {
		this.musicVbox = musicVbox;
	}
	public String getMem_id() {
		return mem_id;
	}
	public void setMem_id(String mem_id) {
		this.mem_id = mem_id;
	}
	
	public void createChbox() {
		if(chBox ==null) {
		this.chBox = new JFXCheckBox();
		chBox.setCheckedColor(Color.valueOf("#9c0000"));
		}
	}
	public JFXCheckBox getChBox() {
		createChbox();
		return this.chBox;
	}
	public JFXCheckBox getChBox1() {
		return chBox;
	}
	public void setChBox(JFXCheckBox chBox) {
		this.chBox = chBox;
	}
	public String getMyalb_name() {
		return myalb_name;
	}
	public void setMyalb_name(String myalb_name) {
		this.myalb_name = myalb_name;
	}
	public String getMus_title() {
		return mus_title;
	}
	public void setMus_title(String mus_title) {
		this.mus_title = mus_title;
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
		Image img = new Image(alb_image);
		imgView.setImage(img);
		imgView.setFitWidth(120);
		imgView.setFitHeight(50);
		return imgView;
	}
	public void setImgView(ImageView imgView) {
		this.imgView = imgView;
	}
	public String getMyalb_list_no() {
		return myalb_list_no;
	}
	public void setMyalb_list_no(String myalb_list_no) {
		this.myalb_list_no = myalb_list_no;
	}
	public String getMyalb_no() {
		return myalb_no;
	}
	public void setMyalb_no(String myalb_no) {
		this.myalb_no = myalb_no;
	}
	public String getMus_no() {
		return mus_no;
	}
	public void setMus_no(String mus_no) {
		this.mus_no = mus_no;
	}
	public String getMyalb_list_indate() {
		return myalb_list_indate;
	}
	public void setMyalb_list_indate(String myalb_list_indate) {
		this.myalb_list_indate = myalb_list_indate;
	}

}
