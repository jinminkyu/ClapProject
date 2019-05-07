package kr.or.ddit.clap.vo.support;

import java.io.Serializable;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

public class QnaReviewVO extends RecursiveTreeObject<QnaReviewVO> implements Serializable {
	
	private String qna_re_no;
	private String qna_re_content;
	private String qna_re_indate;
	private String qna_no;
	private String mem_id;
	
	
	public String getQna_re_no() {
		return qna_re_no;
	}
	public void setQna_re_no(String qna_re_no) {
		this.qna_re_no = qna_re_no;
	}
	public String getQna_re_content() {
		return qna_re_content;
	}
	public void setQna_re_content(String qna_re_content) {
		this.qna_re_content = qna_re_content;
	}
	public String getQna_re_indate() {
		return qna_re_indate;
	}
	public void setQna_re_indate(String qna_re_indate) {
		this.qna_re_indate = qna_re_indate;
	}
	public String getQna_no() {
		return qna_no;
	}
	public void setQna_no(String qna_no) {
		this.qna_no = qna_no;
	}
	public String getMem_id() {
		return mem_id;
	}
	public void setMem_id(String mem_id) {
		this.mem_id = mem_id;
	}
	

}