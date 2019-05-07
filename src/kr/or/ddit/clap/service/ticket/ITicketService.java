package kr.or.ddit.clap.service.ticket;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import kr.or.ddit.clap.vo.ticket.TicketBuyListVO;
import kr.or.ddit.clap.vo.ticket.TicketVO;

public interface ITicketService extends Remote{
	public List<TicketBuyListVO> selectList(String id) throws RemoteException;
	public List<TicketBuyListVO> selectTickBuyAllList(TicketBuyListVO vo) throws RemoteException;
	public List<TicketVO> selectTicket() throws RemoteException;
	public List<TicketBuyListVO> selectTickChart() throws RemoteException;
	
	public int insertTicketBuy(TicketBuyListVO vo) throws RemoteException;
	
	public List<TicketBuyListVO> buyfind(String id) throws RemoteException;
	public List<TicketBuyListVO> selectBuyAllist(String id) throws RemoteException;
}
