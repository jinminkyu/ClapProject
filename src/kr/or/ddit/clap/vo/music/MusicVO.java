package kr.or.ddit.clap.vo.music;

import java.io.Serializable;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public class MusicVO extends RecursiveTreeObject<MusicVO> implements Serializable {

	private String mus_no;
	private String mus_title;
	private String mus_time;
	private String mus_indate;
	private String mus_file;
	private String mus_mvfile;
	

	private String mus_muswrite_son;
	private String mus_edit_son;
	private String mus_write_son;
	private String mus_lyrics;
	private String mus_befor_rank;
	private String alb_no;
	private String gen_detail_no;

	private String mus_runtime;
	private String alb_name;
	private String alb_image;
	private String sing_name;
	private String gen_name;
	private String rcm_alb_list_no;
	public String getRcm_alb_list_no() {
		return rcm_alb_list_no;
	}


	public void setRcm_alb_list_no(String rcm_alb_list_no) {
		this.rcm_alb_list_no = rcm_alb_list_no;
	}

	private ImageView imgView;
	private String gen_detail_name;
	private JFXButton btn;
	
	
		
	
	public JFXButton createButtonImg() {
		this.btn = new JFXButton();
		FontAwesomeIcon remove = new FontAwesomeIcon();
		remove.setIconName("REMOVE");
		remove.setFill(Color.valueOf("#9c0000"));
		remove.setSize("20");
		btn.setId(mus_no);
		btn.setGraphic(remove);
		return btn;
	}
	
	
	public JFXButton getBtn() {
		
		return btn;
	}

	public void setBtn(JFXButton btn) {
		
		this.btn = btn;
	}

	public String getMus_mvfile() {
		return mus_mvfile;
	}

	public void setMus_mvfile(String mus_mvfile) {
		this.mus_mvfile = mus_mvfile;
	}
	
	public ImageView getImgView() {
		this.imgView = new ImageView();
		Image img = new Image(alb_image);
		imgView.setImage(img);
		imgView.setFitWidth(120);
		imgView.setFitHeight(50);
		return imgView;
	}

	public void setImgView(ImageView imgView) {
		this.imgView = imgView;
	}

	public String getMus_runtime() {
		return mus_runtime;
	}

	public void setMus_runtime(String mus_runtime) {
		this.mus_runtime = mus_runtime;
	}

	public String getAlb_name() {
		return alb_name;
	}

	public void setAlb_name(String alb_name) {
		this.alb_name = alb_name;
	}

	public String getAlb_image() {
		return alb_image;
	}

	public void setAlb_image(String alb_image) {
		this.alb_image = alb_image;
	}

	public String getSing_name() {
		return sing_name;
	}

	public void setSing_name(String sing_name) {
		this.sing_name = sing_name;
	}

	public String getGen_name() {
		return gen_name;
	}

	public void setGen_name(String gen_name) {
		this.gen_name = gen_name;
	}

	public String getGen_detail_name() {
		return gen_detail_name;
	}

	public void setGen_detail_name(String gen_detail_name) {
		this.gen_detail_name = gen_detail_name;
	}


	public String getMus_no() {
		return mus_no;
	}

	public void setMus_no(String mus_no) {
		this.mus_no = mus_no;
	}

	public String getMus_title() {
		return mus_title;
	}

	public void setMus_title(String mus_title) {
		this.mus_title = mus_title;
	}

	public String getMus_time() {
		return mus_time;
	}

	public void setMus_time(String mus_time) {
		this.mus_time = mus_time;
	}

	public String getMus_indate() {
		return mus_indate;
	}

	public void setMus_indate(String mus_indate) {
		this.mus_indate = mus_indate;
	}

	public String getMus_file() {
		return mus_file;
	}

	public void setMus_file(String mus_file) {
		this.mus_file = mus_file;
	}

	public String getMus_muswrite_son() {
		return mus_muswrite_son;
	}

	public void setMus_muswrite_son(String mus_muswrite_son) {
		this.mus_muswrite_son = mus_muswrite_son;
	}

	public String getMus_edit_son() {
		return mus_edit_son;
	}

	public void setMus_edit_son(String mus_edit_son) {
		this.mus_edit_son = mus_edit_son;
	}

	public String getMus_write_son() {
		return mus_write_son;
	}

	public void setMus_write_son(String mus_write_son) {
		this.mus_write_son = mus_write_son;
	}

	public String getMus_lyrics() {
		return mus_lyrics;
	}

	public void setMus_lyrics(String mus_lyrics) {
		this.mus_lyrics = mus_lyrics;
	}

	public String getAlb_no() {
		return alb_no;
	}

	public void setAlb_no(String alb_no) {
		this.alb_no = alb_no;
	}

	public String getGen_detail_no() {
		return gen_detail_no;
	}

	public void setGen_detail_no(String gen_detail_no) {
		this.gen_detail_no = gen_detail_no;
	}

	public String getMus_befor_rank() {
		return mus_befor_rank;
	}

	public void setMus_befor_rank(String mus_befor_rank) {
		this.mus_befor_rank = mus_befor_rank;
	}

}
