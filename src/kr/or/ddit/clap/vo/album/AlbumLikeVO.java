package kr.or.ddit.clap.vo.album;

import java.io.Serializable;

public class AlbumLikeVO implements Serializable{
	
	private String mem_id;
	private String alb_no;
	private String alb_like_date;
	
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
	public String getAlb_like_date() {
		return alb_like_date;
	}
	public void setAlb_like_date(String alb_like_date) {
		this.alb_like_date = alb_like_date;
	}
	
}
