package kr.or.ddit.clap.vo.support;

import java.io.Serializable;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import javafx.scene.image.ImageView;

public class QnaVO extends RecursiveTreeObject<QnaVO> implements Serializable{
	
	private String qna_no;
	private String qna_indate;
	private String qna_title;
	private String mem_tel;
	private String mem_email;
	private String qna_type;
	private String qna_view_cnt;
	private String parent_no;
	private String attach_file;
	private String qna_content;
	private String mem_id;
	private ImageView imageview;
	
	
	
	public ImageView getImageview() {
		return imageview;
	}
	public void setImageview(ImageView imageview) {
		this.imageview = imageview;
	}
	public String getQna_no() {
		return qna_no;
	}
	public void setQna_no(String qna_no) {
		this.qna_no = qna_no;
	}
	public String getQna_indate() {
		return qna_indate;
	}
	public void setQna_indate(String qna_indate) {
		this.qna_indate = qna_indate;
	}
	public String getQna_title() {
		return qna_title;
	}
	public void setQna_title(String qna_title) {
		this.qna_title = qna_title;
	}
	public String getMem_tel() {
		return mem_tel;
	}
	public void setMem_tel(String mem_tel) {
		this.mem_tel = mem_tel;
	}
	public String getMem_email() {
		return mem_email;
	}
	public void setMem_email(String mem_email) {
		this.mem_email = mem_email;
	}
	public String getQna_type() {
		return qna_type;
	}
	public void setQna_type(String qna_type) {
		this.qna_type = qna_type;
	}
	public String getQna_view_cnt() {
		return qna_view_cnt;
	}
	public void setQna_view_cnt(String qna_view_cnt) {
		this.qna_view_cnt = qna_view_cnt;
	}
	public String getParent_no() {
		return parent_no;
	}
	public void setParent_no(String parent_no) {
		this.parent_no = parent_no;
	}
	public String getAttach_file() {
		return attach_file;
	}
	public void setAttach_file(String attach_file) {
		this.attach_file = attach_file;
	}
	public String getQna_content() {
		return qna_content;
	}
	public void setQna_content(String qna_content) {
		this.qna_content = qna_content;
	}
	public String getMem_id() {
		return mem_id;
	}
	public void setMem_id(String mem_id) {
		this.mem_id = mem_id;
	}
	
}
