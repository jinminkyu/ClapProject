package kr.or.ddit.clap.vo.music;

import java.io.Serializable;

public class LyricsVO implements Serializable{

	private String mus_no;
	private String lyrics_txt;
	
	public String getMus_no() {
		return mus_no;
	}
	public void setMus_no(String mus_no) {
		this.mus_no = mus_no;
	}
	public String getLyrics_txt() {
		return lyrics_txt;
	}
	public void setLyrics_txt(String lyrics_txt) {
		this.lyrics_txt = lyrics_txt;
	}
	
}
