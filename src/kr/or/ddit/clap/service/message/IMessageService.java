package kr.or.ddit.clap.service.message;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import kr.or.ddit.clap.vo.support.MessageVO;

public interface IMessageService extends Remote{
	public List<MessageVO> selectMessage(MessageVO vo) throws RemoteException;
	public String selectMessFCnt(MessageVO vo) throws RemoteException;
	public int updateMessage(MessageVO vo) throws RemoteException;
	public int insertMessage(MessageVO vo) throws RemoteException;
	public int deleteMessage(String no) throws RemoteException;
}
