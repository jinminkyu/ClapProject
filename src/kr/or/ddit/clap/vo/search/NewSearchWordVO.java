package kr.or.ddit.clap.vo.search;

import java.io.Serializable;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

public class NewSearchWordVO extends RecursiveTreeObject<NewSearchWordVO> implements Serializable{
	
	private String new_word_no;
	private String new_word_date;
	private String new_word;
	private String mem_id;
	
	public String getNew_word_no() {
		return new_word_no;
	}
	public void setNew_word_no(String new_word_no) {
		this.new_word_no = new_word_no;
	}
	public String getNew_word_date() {
		return new_word_date;
	}
	public void setNew_word_date(String new_word_date) {
		this.new_word_date = new_word_date;
	}
	public String getNew_word() {
		return new_word;
	}
	public void setNew_word(String new_word) {
		this.new_word = new_word;
	}
	public String getMem_id() {
		return mem_id;
	}
	public void setMem_id(String mem_id) {
		this.mem_id = mem_id;
	}

}
