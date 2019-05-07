package kr.or.ddit.clap.service.singer;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import kr.or.ddit.clap.vo.singer.SingerReviewVO;

public interface ISingerReviewService extends Remote {
	public List<SingerReviewVO> selectItsReview(SingerReviewVO vo)		throws RemoteException;
	public int deleteItsReview(SingerReviewVO vo)					throws RemoteException;
}
