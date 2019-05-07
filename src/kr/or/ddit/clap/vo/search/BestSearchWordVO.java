package kr.or.ddit.clap.vo.search;

import java.io.Serializable;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

public class BestSearchWordVO extends RecursiveTreeObject<BestSearchWordVO> implements Serializable{
	
	private String best_word_no;
	private String best_word_date;
	private String best_word;
	
	public String getBest_word_no() {
		return best_word_no;
	}
	public void setBest_word_no(String best_word_no) {
		this.best_word_no = best_word_no;
	}
	public String getBest_word_date() {
		return best_word_date;
	}
	public void setBest_word_date(String best_word_date) {
		this.best_word_date = best_word_date;
	}
	public String getBest_word() {
		return best_word;
	}
	public void setBest_word(String best_word) {
		this.best_word = best_word;
	}
	
}
    