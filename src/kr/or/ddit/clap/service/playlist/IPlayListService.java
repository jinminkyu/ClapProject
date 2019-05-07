package kr.or.ddit.clap.service.playlist;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

import kr.or.ddit.clap.vo.music.PlayListVO;

/**
 * 
 * @author 진민규
 *
 */

public interface IPlayListService  extends Remote {
	public List<PlayListVO> playlistSelect(String mem_id) throws RemoteException;
	public int playlistInsert(PlayListVO vo) throws RemoteException;
	public int playlistDelete(PlayListVO vo) throws RemoteException;
}
