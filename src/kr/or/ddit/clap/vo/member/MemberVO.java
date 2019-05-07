package kr.or.ddit.clap.vo.member;

import java.io.Serializable;
import java.util.Map;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class MemberVO extends RecursiveTreeObject<MemberVO> implements Serializable{
   private String mem_id;
   private String mem_pw;
   private String mem_name;
   private String mem_email;
   private String mem_bir;
   private String mem_gender;
   private String mem_tel;
   private String mem_grade;
   private String mem_auth;
   private String mem_indate;
   private String mem_blacklist_tf;
   private String mem_del_tf;
   private String mem_black_cnt;
   private String mem_image;
   private String mem_intro;
   private String mem_game;
   
   
   private ImageView imgView;
   
        
   public ImageView getImgView() {   //try {
      this.imgView = new ImageView();
      //Image img = new Image("file:\\\\Sem-pc\\공유폴더\\Clap\\img\\noImg.png");
      Image img = new Image(mem_image);
      imgView.setImage(img);
      imgView.setFitWidth(80);
      imgView.setFitHeight(100);
      return imgView;
   }
   public void setImgView(ImageView imgView) {
      this.imgView = imgView;
   }
   public String getMem_id() {
      return mem_id;
   }
   public void setMem_id(String mem_id) {
      this.mem_id = mem_id;
   }
   public String getMem_pw() {
      return mem_pw;
   }
   public void setMem_pw(String mem_pw) {
      this.mem_pw = mem_pw;
   }
   public String getMem_name() {
      return mem_name;
   }
   public void setMem_name(String mem_name) {
      this.mem_name = mem_name;
   }
   public String getMem_email() {
      return mem_email;
   }
   public void setMem_email(String mem_email) {
      this.mem_email = mem_email;
   }
   public String getMem_bir() {
      return mem_bir;
   }
   public void setMem_bir(String mem_bir) {
      this.mem_bir = mem_bir;
   }
   public String getMem_gender() {
      return mem_gender;
   }
   public void setMem_gender(String mem_gender) {
      this.mem_gender = mem_gender;
   }
   public String getMem_tel() {
      return mem_tel;
   }
   public void setMem_tel(String mem_tel) {
      this.mem_tel = mem_tel;
   }
   public String getMem_grade() {
      return mem_grade;
   }
   public void setMem_grade(String mem_grade) {
      this.mem_grade = mem_grade;
   }
   public String getMem_auth() {
      return mem_auth;
   }
   public void setMem_auth(String mem_auth) {
      this.mem_auth = mem_auth;
   }
   public String getMem_indate() {
      return mem_indate;
   }
   public void setMem_indate(String mem_indate) {
      this.mem_indate = mem_indate;
   }
   public String getMem_blacklist_tf() {
      return mem_blacklist_tf;
   }
   public void setMem_blacklist_tf(String mem_blacklist_tf) {
      this.mem_blacklist_tf = mem_blacklist_tf;
   }
   public String getMem_del_tf() {
	   
      return mem_del_tf;
   }
   public void setMem_del_tf(String mem_del_tf) {

      this.mem_del_tf = mem_del_tf;
   }
   public String getMem_black_cnt() {
      return mem_black_cnt;
   }
   public void setMem_black_cnt(String mem_black_cnt) {
      this.mem_black_cnt = mem_black_cnt;
   }
   public String getMem_image() {
      return mem_image;
   }
   public void setMem_image(String mem_image) {
      this.mem_image = mem_image;
   }
   public String getMem_intro() {
      return mem_intro;
   }
   public void setMem_intro(String mem_intro) {
      this.mem_intro = mem_intro;
   }
	public String getMem_game() {
		return mem_game;
	}
	public void setMem_game(String mem_game) {
		this.mem_game = mem_game;
	}
   
}