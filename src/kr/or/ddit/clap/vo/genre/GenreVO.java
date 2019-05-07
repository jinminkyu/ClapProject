package kr.or.ddit.clap.vo.genre;

import java.io.Serializable;

public class GenreVO implements Serializable{
	
	private String gen_no;
	private String gen_name;
	
	public String getGen_no() {
		return gen_no;
	}
	public void setGen_no(String gen_no) {
		this.gen_no = gen_no;
	}
	public String getGen_name() {
		return gen_name;
	}
	public void setGen_name(String gen_name) {
		this.gen_name = gen_name;
	}

}
