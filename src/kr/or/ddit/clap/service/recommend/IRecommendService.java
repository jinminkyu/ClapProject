package kr.or.ddit.clap.service.recommend;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

import kr.or.ddit.clap.vo.music.MusicVO;
import kr.or.ddit.clap.vo.recommend.RecommendAlbumListVO;
import kr.or.ddit.clap.vo.recommend.RecommendAlbumVO;

public interface IRecommendService extends Remote {

	
	//앨범 전체리스트 조회
	public List<RecommendAlbumVO> selectAllRecommendAlbum() throws RemoteException;

	//앨범 인기순 리스트 조회
	public List<RecommendAlbumVO> selectBestRecommendAlbum() throws RemoteException;
	
	//좋아요 수
	public int selectAlbumLikeCnt(String RcmAlbNo) throws RemoteException;
	
	//리스트 수
	public int selectAlbumListCnt(String RcmAlbNo) throws RemoteException; 
	
	
	//추천앨범 생성쿼리 
	public int insertRecommendAlbum(RecommendAlbumVO rVO) throws RemoteException;
	
	
	//추천앨범 상세조회
	public RecommendAlbumVO selectRecommendAlbumDetail(String rcmAlbNo) throws RemoteException;
	
	//추천앨범에 들어있는 곡 조회 
	public List<MusicVO> SelectRcmMusicList(String rcmAlbNo) throws RemoteException;
	
	//현재 시퀀스를 조회하는 쿼리 추가
	public String selectSequence() throws RemoteException;
			
	//추천앨범곡을 추가
	public int insertRecommendAlbumMusic(Map<String, String> map) throws RemoteException;
	
	//추천앨범 수정
	public int updateRcmAlbum(RecommendAlbumVO vo) throws RemoteException;
	
	//추천앨범에 속해있는 곡 전체삭제
	public int deleteRcmMusic(String rcmAlbNo) throws RemoteException;
	
	//추천앨범 삭제
	public int deleteRecommendAlbum(String rcmAlbNo) throws RemoteException;
	
	
	//추천앨범  좋아요 여부 채크
	public int checkHeartYN(Map<String, String> map) throws RemoteException;
	
	//추천앨범 라이크 취소
	public int deleteRcmLike(Map<String, String> map) throws RemoteException;
	
	//추천앨범 라이크 등록
	public int insertRcmLike(Map<String, String> map) throws RemoteException;
	
	
	
	
	
	

}
