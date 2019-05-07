/**
 * Author Hansoo 
 * 
 */
package kr.or.ddit.clap.main;

import java.util.Stack;


//앞으로 가기 뒤로가기를 위한 클래스
public class GobackStack {
	public static Stack<String> back  = new Stack<String>();
	public static Stack<String> forward  = new Stack<String>();
	
	public static String printPage() {
	return back.peek();
	}	
	
	public static void goURL(String url) {
		back.push(url);
		if(!forward.empty())
			forward.clear();
	}
	public static void goForward() {
		if(!forward.empty()) {
			 back.push(forward.pop());
		}
	}

	public static void goBack() {
		if(!back.empty()) {
		 forward.push(back.pop());
		}
	}
	
	
}
