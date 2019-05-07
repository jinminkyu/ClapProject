package kr.or.ddit.clap.vo.music;

import java.io.Serializable;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public class MusicHistoryVO  extends RecursiveTreeObject<MusicHistoryVO> implements Serializable{
	
	private String histo_no;
	private String histo_indate;
	private String mus_no;
	private String mem_id;
	private String name;
	private String title;
	private String image;
	private ImageView imageView;
	private JFXCheckBox chBox;
	
	
	
	
	
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public ImageView getImageView() {
		this.imageView = new ImageView();
		//Image img = new Image("file:\\\\Sem-pc\\공유폴더\\Clap\\img\\noImg.png");
		Image img = new Image(image);
		imageView.setImage(img);
		imageView.setFitWidth(120);
		imageView.setFitHeight(50);
		return imageView;
	}
	public void setImageView(ImageView imageView) {
		this.imageView = imageView;
	}
	
	
	public void createChbox() {
		if(chBox ==null) {
		this.chBox = new JFXCheckBox();
		chBox.setCheckedColor(Color.valueOf("#9c0000"));
		chBox.setId(mus_no);
		}
	}
	public JFXCheckBox getChBox() {
		createChbox();
		return this.chBox;
	}
	
	public JFXCheckBox getChBox1() {
		return this.chBox;
	}
	public void setChBox(JFXCheckBox chBox) {
		this.chBox = chBox;
	}
	public String getHisto_no() {
		return histo_no;
	}
	public void setHisto_no(String histo_no) {
		this.histo_no = histo_no;
	}
	public String getHisto_indate() {
		return histo_indate;
	}
	public void setHisto_indate(String histo_indate) {
		this.histo_indate = histo_indate;
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

}
