package kr.or.ddit.clap.service.like;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import kr.or.ddit.clap.vo.member.LikeVO;

public interface ILikeService extends Remote{
	public List<LikeVO> selectMusLike(LikeVO vo) 		throws RemoteException;
	public int deleteMusLike(LikeVO vo)					throws RemoteException;
	
	public List<LikeVO> selectAlbLike(LikeVO vo)		throws RemoteException;
	public int deleteAlbLike(LikeVO vo)					throws RemoteException;
	
	public List<LikeVO> selectRcmLike(LikeVO vo)		throws RemoteException;
	public int deleteRcmLike(LikeVO vo)					throws RemoteException;
	
	public List<LikeVO> selectSingLike(LikeVO vo)		throws RemoteException;
	public int deleteSingLike(LikeVO vo)					throws RemoteException;

}

