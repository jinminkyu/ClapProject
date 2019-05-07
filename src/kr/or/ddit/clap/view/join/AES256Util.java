package kr.or.ddit.clap.view.join;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;

/**
 * AES256 암/복호화를 지원하는 클래스.
 * @author Kyunghun
 *
 */
public class AES256Util {
	private String iv;
	private Key keySpec;

	final static String key = "1st-is-clap-1234"; // 16자 비밀키 입력

	public static void main(String[] args) throws UnsupportedEncodingException, NoSuchAlgorithmException, GeneralSecurityException {
		AES256Util aes = new AES256Util();
		System.out.println("암호화 하기.");
		String pw1 = "1111"; // 입력 값
		String pw2 = "ddit206";
		
		String encryptedPw1 = aes.encrypt(pw1); // 암호화된 값.
		String encryptedPw2 = aes.encrypt(pw2);
		System.out.println(encryptedPw1);
		System.out.println(encryptedPw2);
		
		System.out.println();
		System.out.println("복호화 하기.");
		String decryptedPw1 = aes.decrypt(encryptedPw1); // 복호화된 값.
		String decryptedPw2 = aes.decrypt("23");
		System.out.println(decryptedPw1);
		System.out.println(decryptedPw2);
		
		// 비밀번호 일치여부 확인시
		// -> 암호화시킨 입력값과 vo의 값 비교.
		
		// 비밀번호 업데이트 시
		// -> 입력값을 암호화시키고 DB에 저장.
	}
	
	
	public AES256Util() throws UnsupportedEncodingException {
		this.iv = key.substring(0, 16);
		byte[] keyBytes = new byte[16];
		byte[] b = key.getBytes("UTF-8");
		int len = b.length;
		if (len > keyBytes.length) {
			len = keyBytes.length;
		}
		System.arraycopy(b, 0, keyBytes, 0, len);
		SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");

		this.keySpec = keySpec;
	}

	/**
	 * AES256 으로 암호화 한다.
	 * 
	 * @param str
	 *            암호화할 문자열
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws GeneralSecurityException
	 * @throws UnsupportedEncodingException
	 */
	public String encrypt(String str) throws NoSuchAlgorithmException,
			GeneralSecurityException, UnsupportedEncodingException {
		Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
		c.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(iv.getBytes()));
		byte[] encrypted = c.doFinal(str.getBytes("UTF-8"));
		String enStr = new String(Base64.encodeBase64(encrypted));
		return enStr;
	}

	/**
	 * AES256으로 암호화된 txt 를 복호화한다.
	 * 
	 * @param str
	 *            복호화할 문자열
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws GeneralSecurityException
	 * @throws UnsupportedEncodingException
	 */
	public String decrypt(String str) throws NoSuchAlgorithmException,
			GeneralSecurityException, UnsupportedEncodingException {
		Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
		c.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(iv.getBytes()));
		byte[] byteStr = Base64.decodeBase64(str.getBytes());
		return new String(c.doFinal(byteStr), "UTF-8");
	}

}