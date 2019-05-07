package kr.or.ddit.clap.service.login;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

import kr.or.ddit.clap.vo.member.MemberVO;
import kr.or.ddit.clap.vo.search.NewSearchWordVO;

public interface ILoginService extends Remote{
	public Boolean idCheck(String id) throws RemoteException;

	public List<MemberVO> select(String id) throws RemoteException;

	public List<MemberVO> idSearch(MemberVO vo) throws RemoteException;
	
	public Boolean emailCheck(MemberVO vo) throws RemoteException;
	
	public List<NewSearchWordVO> selecthotkeyword() throws RemoteException;
	
	public List<NewSearchWordVO> selectHistorykeyword(String id) throws RemoteException;
	
	public List<String> gameMember(String mem_id) throws RemoteException;
	
	public int gameUpdate(Map map) throws RemoteException;
	
	public int insertSearchWord(Map<String,String> map) throws RemoteException;
	
	public String selectSearchPK(String word) throws RemoteException;
	
	
}


