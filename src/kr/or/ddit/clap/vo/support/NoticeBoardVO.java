package kr.or.ddit.clap.vo.support;

import java.io.Serializable;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

public class NoticeBoardVO extends RecursiveTreeObject<NoticeBoardVO> implements Serializable{
	
	private String notice_no;
	private String notice_indate;
	private String notice_title;
	private String notice_content;
	private String notice_view_cnt;
	private String mem_id;
	
	public String getNotice_no() {
		return notice_no;
	}
	public void setNotice_no(String notice_no) {
		this.notice_no = notice_no;
	}
	public String getNotice_indate() {
		return notice_indate;
	}
	public void setNotice_indate(String notice_indate) {
		this.notice_indate = notice_indate;
	}
	public String getNotice_title() {
		return notice_title;
	}
	public void setNotice_title(String notice_title) {
		this.notice_title = notice_title;
	}
	public String getNotice_content() {
		return notice_content;
	}
	public void setNotice_content(String notice_content) {
		this.notice_content = notice_content;
	}
	public String getNotice_view_cnt() {
		return notice_view_cnt;
	}
	public void setNotice_view_cnt(String notice_view_cnt) {
		this.notice_view_cnt = notice_view_cnt;
	}
	public String getMem_id() {
		return mem_id;
	}
	public void setMem_id(String mem_id) {
		this.mem_id = mem_id;
	}
	
}
