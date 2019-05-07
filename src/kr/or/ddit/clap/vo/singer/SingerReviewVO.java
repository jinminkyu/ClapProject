package kr.or.ddit.clap.vo.singer;

import java.io.Serializable;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public class SingerReviewVO extends RecursiveTreeObject<SingerReviewVO>  implements Serializable{

	
	private String sing_re_no;
	private String sing_re_content;
	private String sing_re_indate;
	private String mem_id;
	private String sing_no;
	
	/**
	 * 현지 마이페이지 리뷰보기 위해 추가
	 * @return
	 */
	private String sing_debut_era;
	private String sing_image;
	private String sing_name;
	private ImageView imgView;
	private JFXButton btnDel;

	
	
	public String getSing_debut_era() {
		return sing_debut_era;
	}
	public void setSing_debut_era(String sing_debut_era) {
		this.sing_debut_era = sing_debut_era;
	}
	public String getSing_image() {
		return sing_image;
	}
	public void setSing_image(String sing_image) {
		this.sing_image = sing_image;
	}
	public String getSing_name() {
		return sing_name;
	}
	public void setSing_name(String sing_name) {
		this.sing_name = sing_name;
	}
	public ImageView getImgView() {
		this.imgView = new ImageView();
		//Image img = new Image("file:\\\\Sem-pc\\공유폴더\\Clap\\img\\noImg.png");
		Image img = new Image(sing_image);
		imgView.setImage(img);
		imgView.setFitWidth(100);
		imgView.setFitHeight(70);
	
		return imgView;
	}
	public void setImgView(ImageView imgView) {
		this.imgView = imgView;
	}
	
	
	public void createButtonImg() {
		if(btnDel ==null) {
		this.btnDel = new JFXButton();
		FontAwesomeIcon remove = new FontAwesomeIcon();
		remove.setIconName("REMOVE");
		remove.setFill(Color.valueOf("#9c0000"));
		remove.setSize("20");
		btnDel.setId(sing_re_no);
		btnDel.setGraphic(remove);
		}
	}
	public JFXButton getBtnDel() {
		createButtonImg();
		return this.btnDel;
	}
	public void setBtnDel(JFXButton btnDel) {
		this.btnDel = btnDel;
	}
	public String getSing_re_no() {
		return sing_re_no;
	}
	public void setSing_re_no(String sing_re_no) {
		this.sing_re_no = sing_re_no;
	}
	public String getsing_re_content() {
		return sing_re_content;
	}
	public void setsing_re_content(String sing_re_content) {
		this.sing_re_content = sing_re_content;
	}
	public String getSing_re_indate() {
		return sing_re_indate;
	}
	public void setSing_re_indate(String sing_re_indate) {
		this.sing_re_indate = sing_re_indate;
	}
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

}
