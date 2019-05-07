package kr.or.ddit.clap.service.album;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import kr.or.ddit.clap.vo.album.AlbumReviewVO;

public interface IAlbumReviewService extends Remote{
	public List<AlbumReviewVO> selectAlbReview(AlbumReviewVO vo)		throws RemoteException;
	public int deleteAlbReview(AlbumReviewVO vo)					throws RemoteException;
	
}