/**
 * Album의 service Interface
 * @author Hansoo
 *
 */

package kr.or.ddit.clap.service.album;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

import kr.or.ddit.clap.vo.album.AlbumVO;

public interface IAlbumService extends Remote {
	public List<AlbumVO> selectListAll() throws RemoteException;

	public List<AlbumVO> searchList(AlbumVO vo) throws RemoteException;

	public int insertAlbum(AlbumVO vo) throws RemoteException;

	public AlbumVO albumDetailInfo(String albumNo) throws RemoteException;

	public int selectAlbumLikeCnt(String albumNo) throws RemoteException;

	public int updateAlbumInfo(AlbumVO vo) throws RemoteException;

	public int deleteAlbum(String albumNo) throws RemoteException;

	public List<Map> newAlbumSelect() throws RemoteException;

	public List<Map> singerAlbumSelect(AlbumVO vo) throws RemoteException;

	// 앨범 좋아요 여부 채크
	public int checkHeartYN(Map<String, String> map) throws RemoteException;

	// 앨범 라이크 취소
	public int deleteAlbLike(Map<String, String> map) throws RemoteException;

	// 앨범 라이크 등록
	public int insertAlbLike(Map<String, String> map) throws RemoteException;

	// 앨범 댓글 조회
	public List<Map<String, String>> selectAlbReply(String singerNo) throws RemoteException;

	// 앨범 댓글 등록
	public int insertAlbReply(Map<String, String> map) throws RemoteException;

	// 앨범 댓글 삭제
	public int deleteAlbReply(String id) throws RemoteException;

}
