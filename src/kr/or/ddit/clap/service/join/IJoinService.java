package kr.or.ddit.clap.service.join;

import java.rmi.Remote;
import java.rmi.RemoteException;

import kr.or.ddit.clap.vo.member.MemberVO;

public interface IJoinService extends Remote{
	public int insert(MemberVO vo) throws RemoteException;
}
