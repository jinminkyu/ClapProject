package kr.or.ddit.clap.vo.music;

import java.io.Serializable;

public class MusicVideoVO implements Serializable{
	
	private String mus_no;
	private String mv_file;
	private String mv_indate;
	private String mv_image;
	
	public String getMus_no() {
		return mus_no;
	}
	public void setMus_no(String mus_no) {
		this.mus_no = mus_no;
	}
	public String getMv_file() {
		return mv_file;
	}
	public void setMv_file(String mv_file) {
		this.mv_file = mv_file;
	}
	public String getMv_indate() {
		return mv_indate;
	}
	public void setMv_indate(String mv_indate) {
		this.mv_indate = mv_indate;
	}
	public String getMv_image() {
		return mv_image;
	}
	public void setMv_image(String mv_image) {
		this.mv_image = mv_image;
	}

}	
