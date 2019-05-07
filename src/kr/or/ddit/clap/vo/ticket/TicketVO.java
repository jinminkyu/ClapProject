package kr.or.ddit.clap.vo.ticket;

import java.io.Serializable;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

public class TicketVO  extends RecursiveTreeObject<TicketVO>  implements Serializable{
	
	private String ticket_no;
	private String ticket_price;
	private String ticket_indate;
	private String ticket_image;
	private String ticket_content;
	
	public String getTicket_no() {
		return ticket_no;
	}
	public void setTicket_no(String ticket_no) {
		this.ticket_no = ticket_no;
	}
	public String getTicket_price() {
		return ticket_price;
	}
	public void setTicket_price(String ticket_price) {
		this.ticket_price = ticket_price;
	}
	public String getTicket_indate() {
		return ticket_indate;
	}
	public void setTicket_indate(String ticket_indate) {
		this.ticket_indate = ticket_indate;
	}
	public String getTicket_image() {
		return ticket_image;
	}
	public void setTicket_image(String ticket_image) {
		this.ticket_image = ticket_image;
	}
	public String getTicket_content() {
		return ticket_content;
	}
	public void setTicket_content(String ticket_content) {
		this.ticket_content = ticket_content;
	}

}
