package kr.or.ddit.clap.service.noticeboard;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import kr.or.ddit.clap.vo.support.NoticeBoardVO;

public interface INoticeBoardService extends Remote {
	
	public List<NoticeBoardVO> selectListAll() throws RemoteException;
	
	public NoticeBoardVO NoticeBoardDetailContent(String NoticeNo) throws RemoteException;
	
	public int insertNotice(NoticeBoardVO vo) throws RemoteException;
	
	public int deleteNoticeContent(String notice_no) throws RemoteException;
	
	public int updateNotice(NoticeBoardVO vo) throws RemoteException;
	
	public int updateCount(String notice_no) throws RemoteException;

}
