package kr.or.ddit.clap.vo.recommend;

import java.io.Serializable;

public class RecommendAlbumListVO implements Serializable{
	
	private String mus_no;
	private String rcm_alb_no;
	
	public String getMus_no() {
		return mus_no;
	}
	public void setMus_no(String mus_no) {
		this.mus_no = mus_no;
	}
	public String getRcm_alb_no() {
		return rcm_alb_no;
	}
	public void setRcm_alb_no(String rcm_alb_no) {
		this.rcm_alb_no = rcm_alb_no;
	}

}
