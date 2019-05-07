package kr.or.ddit.clap.vo.music;

import java.io.Serializable;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class PlayListVO extends RecursiveTreeObject<PlayListVO> implements Serializable{
	
	private String play_no;
	private String play_indate;
	private String mus_no;
	private String mem_id;
	private String mus_title;
	private String mus_lyrics;
	private String mus_file;
	private String alb_image;
	private String sing_name;
	private VBox vbox;
	private JFXCheckBox checkbox;
	
	public VBox getVbox() {
		this.vbox = new VBox();

		checkbox.setCheckedColor(Color.valueOf("#9c0000"));
		Label sigerName = new Label(sing_name);
		sigerName.setFont(Font.font("-윤고딕330", 12));
		sigerName.setTextFill(Color.valueOf("#dcdcdc"));
		
		Label musicName = new Label(mus_title);
		musicName.setFont(Font.font("-윤고딕350", 15));
		vbox.getChildren().addAll(musicName,sigerName);
		return this.vbox;
	}
	public void setVbox(VBox vbox) {
		this.vbox = vbox;
	}
	public JFXCheckBox getCheckbox() {
		this.checkbox = new JFXCheckBox();
		return this.checkbox;
	}
	public JFXCheckBox getCheckbox1() {
		return this.checkbox;
	}
	public void setCheckbox(JFXCheckBox checkbox) {
		this.checkbox = checkbox;
	}
	public String getMus_title() {
		return mus_title;
	}
	public void setMus_title(String mus_title) {
		this.mus_title = mus_title;
	}
	public String getMus_lyrics() {
		return mus_lyrics;
	}
	public void setMus_lyrics(String mus_lyrics) {
		this.mus_lyrics = mus_lyrics;
	}
	public String getMus_file() {
		return mus_file;
	}
	public void setMus_file(String mus_file) {
		this.mus_file = mus_file;
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
	public String getPlay_no() {
		return play_no;
	}
	public void setPlay_no(String play_no) {
		this.play_no = play_no;
	}
	public String getPlay_indate() {
		return play_indate;
	}
	public void setPlay_indate(String play_indate) {
		this.play_indate = play_indate;
	}
	public String getMus_no() {
		return mus_no;
	}
	public void setMus_no(String mus_no) {
		this.mus_no = mus_no;
	}
	public String getMem_id() {
		return mem_id;
	}
	public void setMem_id(String mem_id) {
		this.mem_id = mem_id;
	}
}
