/**
 * MusicHistory의 service Interface
 * @author 진민규
 *
 */

package kr.or.ddit.clap.service.musichistory;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;
import kr.or.ddit.clap.vo.music.MusicHistoryVO;

/**
 * 
 * @author 진민규
 *
 */
public interface IMusicHistoryService extends Remote {
	public List<Map> toDaySelect() throws RemoteException;
	public List<Map> top10Select() throws RemoteException;
	public List<Map> genreSelect(String genre) throws RemoteException;
	public List<Map> periodSelect(Map<String, String> day) throws RemoteException;
	public List<Map> interiorSelect(String year) throws RemoteException;
	public List<Map> foreignSelect(String year) throws RemoteException;
	public List<MusicHistoryVO> selectMayIts(MusicHistoryVO vo) throws RemoteException;
	public List<MusicHistoryVO> selectMayIndate(MusicHistoryVO vo) throws RemoteException;
	public List<MusicHistoryVO> selectNewLisMus(String id) throws RemoteException;
	public List<MusicHistoryVO> selectManyLisMus(String id) throws RemoteException;
	public List<MusicHistoryVO> selectManyIts(String id) throws RemoteException;
	public int historyInsert (MusicHistoryVO vo) throws RemoteException;
}
