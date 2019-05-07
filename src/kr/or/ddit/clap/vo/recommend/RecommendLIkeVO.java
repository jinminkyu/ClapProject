package kr.or.ddit.clap.vo.recommend;

import java.io.Serializable;

public class RecommendLIkeVO implements Serializable{
	
	private String mem_id;
	private String rcm_alb_no;
	private String rcm_like_date;
	
	public String getMem_id() {
		return mem_id;
	}
	public void setMem_id(String mem_id) {
		this.mem_id = mem_id;
	}
	public String getRcm_alb_no() {
		return rcm_alb_no;
	}
	public void setRcm_alb_no(String rcm_alb_no) {
		this.rcm_alb_no = rcm_alb_no;
	}
	public String getRcm_like_date() {
		return rcm_like_date;
	}
	public void setRcm_like_date(String rcm_like_date) {
		this.rcm_like_date = rcm_like_date;
	}

}
