package kr.or.ddit.clap.service.myalbumlist;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

import kr.or.ddit.clap.vo.myalbum.MyAlbumListVO;

/**
 * 
 * @author 진민규
 *
 */

public interface IMyAlbumListService  extends Remote {
	
	public int myAlbumListInsert(Map<String,String> myAlbumList) throws RemoteException;
	public List<MyAlbumListVO> selectMyAlbList(MyAlbumListVO vo) throws RemoteException;
	public int deleteMyAlbList(MyAlbumListVO vo)  throws RemoteException;
}
