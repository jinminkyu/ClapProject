package kr.or.ddit.clap.service.myalbum;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

import kr.or.ddit.clap.vo.myalbum.MyAlbumVO;

/**
 * 
 * @author 진민규
 *
 */

public interface IMyAlbumService  extends Remote {
	
	public List<MyAlbumVO> myAlbumSelect(String id) throws RemoteException;
	public int myAlbumInsert(Map<String,String> myAlbum) throws RemoteException;
	
	public int deleteMyalb(MyAlbumVO vo)	throws RemoteException;
	public int updateMyalb(MyAlbumVO vo) 	throws RemoteException;
}
