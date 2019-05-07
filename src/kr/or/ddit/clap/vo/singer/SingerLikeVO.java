package kr.or.ddit.clap.vo.singer;

import java.io.Serializable;

public class SingerLikeVO implements Serializable{
	
	private String mem_id;
	private String sing_no;
	private String sing_like_date;
	
	public String getMem_id() {
		return mem_id;
	}
	public void setMem_id(String mem_id) {
		this.mem_id = mem_id;
	}
	public String getSing_no() {
		return sing_no;
	}
	public void setSing_no(String sing_no) {
		this.sing_no = sing_no;
	}
	public String getSing_like_date() {
		return sing_like_date;
	}
	public void setSing_like_date(String sing_like_date) {
		this.sing_like_date = sing_like_date;
	}
	
}
