package kr.or.ddit.clap.vo.support;

import java.io.Serializable;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class EventBoardVO extends RecursiveTreeObject<EventBoardVO> implements Serializable{
	
	private String event_no;
	private String event_title;
	private String event_sdate;
	private String event_edate;
	private String event_view_cnt;
	private String event_image;
	private String event_content;
	private String mem_id;
	private ImageView imgView;
	
	public ImageView getImgView() {
		this.imgView = new ImageView();
		//Image img = new Image("file:\\\\Sem-pc\\공유폴더\\Clap\\img\\noImg.png");
		Image img = new Image(event_image);
		imgView.setImage(img);
		imgView.setFitWidth(120);
		imgView.setFitHeight(50);
		return imgView;
	}
	public void setImgView(ImageView imgView) {
		this.imgView = imgView;
	}
	public String getEvent_no() {
		return event_no;
	}
	public void setEvent_no(String event_no) {
		this.event_no = event_no;
	}
	public String getEvent_title() {
		return event_title;
	}
	public void setEvent_title(String event_title) {
		this.event_title = event_title;
	}
	public String getEvent_sdate() {
		return event_sdate;
	}
	public void setEvent_sdate(String event_sdate) {
		this.event_sdate = event_sdate;
	}
	public String getEvent_edate() {
		return event_edate;
	}
	public void setEvent_edate(String event_edate) {
		this.event_edate = event_edate;
	}
	public String getEvent_view_cnt() {
		return event_view_cnt;
	}
	public void setEvent_view_cnt(String event_view_cnt) {
		this.event_view_cnt = event_view_cnt;
	}
	public String getEvent_image() {
		return event_image;
	}
	public void setEvent_image(String event_image) {
		this.event_image = event_image;
	}
	public String getEvent_content() {
		return event_content;
	}
	public void setEvent_content(String event_content) {
		this.event_content = event_content;
	}
	public String getMem_id() {
		return mem_id;
	}
	public void setMem_id(String mem_id) {
		this.mem_id = mem_id;
	}
	
}
