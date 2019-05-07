/**
 * Singer의 service Interface
 * @author Hansoo
 *
 */

package kr.or.ddit.clap.service.singer;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

import kr.or.ddit.clap.vo.singer.SingerVO;

public interface ISingerService extends Remote {
	public List<SingerVO> selectListAll() throws RemoteException;

	public List<SingerVO> searchList(SingerVO vo) throws RemoteException;

	public SingerVO singerDetailInfo(String singerNo) throws RemoteException;

	public int selectSingerLikeCnt(String singerNo) throws RemoteException;

	public int updateSingerInfo(SingerVO vo) throws RemoteException;

	public int insertSinger(SingerVO vo) throws RemoteException;

	public int deleteSinger(String singerNo) throws RemoteException;

	// 가수앨범 좋아요 여부 채크
	public int checkHeartYN(Map<String, String> map) throws RemoteException;

	// 가수 라이크 취소
	public int deleteSingerLike(Map<String, String> map) throws RemoteException;

	// 가수 라이크 등록
	public int insertSingerLike(Map<String, String> map) throws RemoteException;

	// 가수 댓글 조회
	public List<Map<String, String>> selectReply(String singerNo) throws RemoteException;

	// 가수 댓글 등록
	public int insertReply(Map<String, String> map) throws RemoteException;

	// 가수 댓글 삭제
	public int deleteSigerReply(String id) throws RemoteException;

	// 멤버 신고횟수 1 증가
	public int insertBlackCnt(String id) throws RemoteException;

}
