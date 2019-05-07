package kr.or.ddit.clap.view.musicplayer;

import java.io.File;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import com.jfoenix.controls.JFXSlider;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.scene.control.Label;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.util.Duration;
import kr.or.ddit.clap.main.LoginSession;
import kr.or.ddit.clap.service.ticket.ITicketService;
import kr.or.ddit.clap.vo.ticket.TicketBuyListVO;

    public class   MusicPlayer {
	public static MediaPlayer mediaPlayer;
	private Media media;
	private Status status;
	private Registry reg;
	private ITicketService its;
	private List<TicketBuyListVO> buyticket;
	
	public MusicPlayer() {
		try {
			reg = LocateRegistry.getRegistry("localhost", 8888);
			its = (ITicketService) reg.lookup("ticket");
			buyticket = its.buyfind(LoginSession.session.getMem_id());
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
	}
	
	public void play(FontAwesomeIcon icon_play) {
		status = mediaPlayer.getStatus();
		
		if (status != status.PLAYING) {
			icon_play.setIconName("PAUSE");
			mediaPlayer.play();
		}else  {
			icon_play.setIconName("PLAY");
			mediaPlayer.pause();
		}
	}
	
	public void stop() {
		mediaPlayer.stop();
		mediaPlayer = null;
	}
	
	public Status getStatus() {
		return mediaPlayer.getStatus();
	}
	
	public void musicSync(double value) {
		double time = (mediaPlayer.getTotalDuration().toSeconds() * 1000 / 100) * value;
		mediaPlayer.seek(new Duration(time));
	}
	
	public void Ready(Label Label_nowTime, Label Label_finalTime, JFXSlider slider_time) {
		
		mediaPlayer.setOnReady(() -> {
			if(buyticket.size() == 0) {
				mediaPlayer.setStopTime(new Duration(60000));
			}
		
			int totalSeconds = (int) Math.floor(mediaPlayer.getTotalDuration().toSeconds());
			int tMinutes = totalSeconds / 60;
			int tSeconds = totalSeconds - tMinutes * 60;
			
			Label_finalTime.setText(String.format("%02d:%02d",tMinutes,tSeconds));
				
			mediaPlayer.currentTimeProperty().addListener((observable, oldValue, newValue) -> {
				if (mediaPlayer != null) {
					int nowSeconds = (int) Math.floor(mediaPlayer.getCurrentTime().toSeconds());
					int minutes = nowSeconds / 60;
					int seconds = nowSeconds - minutes * 60;
					
					Label_nowTime.setText(String.format("%02d:%02d",minutes,seconds));
					double nowTime = mediaPlayer.getCurrentTime().toSeconds() / 
					                 mediaPlayer.getTotalDuration().toSeconds() * 100;
					
					if (!slider_time.isValueChanging()) {
						slider_time.setValue(nowTime);
					}
				}
			});
		});
	}
	
	public void setMedia(String path) {
		if (mediaPlayer != null) {
			mediaPlayer.stop();
			
		}
		media = new Media(new File(path).toURI().toString());
		mediaPlayer = new MediaPlayer(media);
	}
}
