package kr.or.ddit.clap.main;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;
import kr.or.ddit.clap.service.login.ILoginService;
import kr.or.ddit.clap.vo.member.MemberVO;

/**
 * 프로그램을 실행시킬 Main Class
 * 
 * 
 * @author Hansoo
 *
 */

public class ClientMain extends Application {
   
   private ILoginService ils;
   private Registry reg;
   List<MemberVO> list = new ArrayList<MemberVO>();

   @Override
   public void start(Stage primaryStage) throws Exception {
      LoginSession ls = new LoginSession();

      // 임시로그인 부분
      try {
         reg = LocateRegistry.getRegistry("localhost", 8888);
         ils = (ILoginService) reg.lookup("login");
      /*   
        list = ils.select("user1");
         ls.session = list.get(0);*/
      } catch (RemoteException e) {
         e.printStackTrace();
      } catch (NotBoundException e) {	
         e.printStackTrace();
      }

      System.out.println("Start Clap!");
      FXMLLoader loader = new FXMLLoader(getClass().getResource("MusicMain.fxml"));
      ScrollPane root = loader.load();
      
      Scene scene = new Scene(root);
      primaryStage.setTitle("Clap:음악, 그리고 설레임");
      primaryStage.setScene(scene);
      primaryStage.show();
      
   }
   public static void main(String[] args) {
      launch(args);
   }
   
}