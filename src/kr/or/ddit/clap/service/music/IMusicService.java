/**
 * Music의 service Interface
 * @author Hansoo
 *
 */

package kr.or.ddit.clap.service.music;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

import kr.or.ddit.clap.vo.genre.GenreDetailVO;
import kr.or.ddit.clap.vo.genre.GenreVO;
import kr.or.ddit.clap.vo.music.MusicVO;

public interface IMusicService extends Remote {
	public List<MusicVO> selectListAll() throws RemoteException;

	public List<MusicVO> searchList(MusicVO vo) throws RemoteException;

	public List<GenreVO> showGenre() throws RemoteException;

	public String selectGenreNO(String genreName) throws RemoteException;

	public String selectGenreDetailNO(String genreName) throws RemoteException;

	public List<GenreDetailVO> showGenreDetail(String genreNo) throws RemoteException;

	public MusicVO selectMusicDetailInfo(String musicNo) throws RemoteException;

	public int insertMusic(MusicVO vo) throws RemoteException;

	public int selectMusicLikeCnt(String musicNo) throws RemoteException;

	public int updateMusicInfo(MusicVO vo) throws RemoteException;

	public int deleteMusic(String musicNo) throws RemoteException;

	public List<Map> newMusicSelete(String genre_no) throws RemoteException;

	public List<Map> selectSinger(String sing_no) throws RemoteException;

	public List<Map> selectAlbum(String alb_no) throws RemoteException;

	public List<String> albumMusNoSelect(String alb_no) throws RemoteException;

	public List<Map> genreMusicSelete(String gen_detail_no) throws RemoteException;
	
	public List<Map> gameSelect() throws RemoteException;

	// 가수 좋아요 여부 채크
	public int checkHeartYN(Map<String, String> map) throws RemoteException;

	// 가수 라이크 취소
	public int deleteMusicLike(Map<String, String> map) throws RemoteException;

	// 가수 라이크 등록
	public int insertMusicLike(Map<String, String> map) throws RemoteException;

	// 가수 댓글 조회
	public List<Map<String, String>> selectMusReply(String singerNo) throws RemoteException;

	// 가수 댓글 등록
	public int insertMusReply(Map<String, String> map) throws RemoteException;

	// 가수 댓글 삭제
	public int deleteMusReply(String id) throws RemoteException;

}
