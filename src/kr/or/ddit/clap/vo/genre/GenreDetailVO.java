package kr.or.ddit.clap.vo.genre;

import java.io.Serializable;

public class GenreDetailVO implements Serializable{
	
	private String gen_detail_no;
	private String gen_detail_name;
	private String gen_no;
	
	public String getGen_detail_no() {
		return gen_detail_no;
	}
	public void setGen_detail_no(String gen_detail_no) {
		this.gen_detail_no = gen_detail_no;
	}
	
	public String getGen_detail_name() {
		return gen_detail_name;
	}
	public void setGen_detail_name(String gen_detail_name) {
		this.gen_detail_name = gen_detail_name;
	}
	public String getGen_no() {
		return gen_no;
	}
	public void setGen_no(String gen_no) {
		this.gen_no = gen_no;
	}
	
}
