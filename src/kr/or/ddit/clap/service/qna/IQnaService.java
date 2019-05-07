package kr.or.ddit.clap.service.qna;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import kr.or.ddit.clap.vo.support.QnaReviewVO;
import kr.or.ddit.clap.vo.support.QnaVO;

public interface IQnaService extends Remote {
	

	public List<QnaVO> selectListAll() throws RemoteException;

	public QnaVO qnaDetailContent(String ContentNo) throws RemoteException;

	public int insertQna(QnaVO vo) throws RemoteException;
	
	public int deleteQnaContent(String qna_no) throws RemoteException;
	
	public int updateQna(QnaVO vo) throws RemoteException;
	
	public int updateCount(String qna_no) throws RemoteException; //조회수
	
	public int insertQnaReview(QnaReviewVO vo) throws RemoteException;
	
	public List<QnaReviewVO> selectListReviewAll(String qna_no) throws RemoteException;

	public List<QnaVO> selectQna(String id) throws RemoteException;
}
