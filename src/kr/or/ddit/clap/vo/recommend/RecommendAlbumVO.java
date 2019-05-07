package kr.or.ddit.clap.vo.recommend;

import java.io.Serializable;

public class RecommendAlbumVO implements Serializable{
	
	private String rcm_alb_no;
	private String rcm_alb_name;
	private String rcm_indate;
	private String rcm_content;
	private String mem_id;
	private String rcm_alb_image;
	private String cnt;
	private String rcm_icon;
	
	
	
	public String getCnt() {
		return cnt;
	}
	public void setCnt(String cnt) {
		this.cnt = cnt;
	}
	public String getRcm_alb_image() {
		return rcm_alb_image;
	}
	public void setRcm_alb_image(String rcm_alb_image) {
		this.rcm_alb_image = rcm_alb_image;
	}
	public String getRcm_alb_no() {
		return rcm_alb_no;
	}
	public void setRcm_alb_no(String rcm_alb_no) {
		this.rcm_alb_no = rcm_alb_no;
	}
	public String getRcm_alb_name() {
		return rcm_alb_name;
	}
	public void setRcm_alb_name(String rcm_alb_name) {
		this.rcm_alb_name = rcm_alb_name;
	}
	public String getRcm_indate() {
		return rcm_indate;
	}
	public void setRcm_indate(String rcm_indate) {
		this.rcm_indate = rcm_indate;
	}
	public String getRcm_content() {
		return rcm_content;
	}
	public void setRcm_content(String rcm_content) {
		this.rcm_content = rcm_content;
	}
	public String getMem_id() {
		return mem_id;
	}
	public void setMem_id(String mem_id) {
		this.mem_id = mem_id;
	}
	public String getRcm_icon() {
		return rcm_icon;
	}
	public void setRcm_icon(String rcm_icon) {
		this.rcm_icon = rcm_icon;
	}

}
