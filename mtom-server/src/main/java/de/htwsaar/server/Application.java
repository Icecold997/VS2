package de.htwsaar.server;


import de.htwsaar.AbstractJavaFxApp;
import de.htwsaar.server.config.ServerConfig;
import de.htwsaar.server.config.ServerInformationTransmitter;
import de.htwsaar.server.gui.Router;
import de.htwsaar.server.persistence.ForwardingConfig;
import de.htwsaar.server.persistence.ForwardingDAO;
import de.htwsaar.server.persistence.ServerDAO;
import de.htwsaar.server.persistence.ServerInfo;
import javafx.application.Platform;
import javafx.application.Preloader;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Lazy;

import java.beans.EventHandler;


/**
 * Die Main Klasse der Applikation. Erbt von der abstrakten JavaFx-Oberklasse um Spring-Support
 * auch in JavaFX-Klassen zu gewährleisten.
 *
 * @author cedosw
 */
@Lazy
@SpringBootApplication
@ComponentScan({"de.htwsaar","de.htwsaar" })
public class Application extends AbstractJavaFxApp {

	private static Logger logger = LoggerFactory.getLogger(Application.class);


	@Autowired
	ForwardingDAO forwardingDAO;
	@Autowired
	ServerConfig serverConfig;
	@Autowired
	ServerDAO serverDAO;
	@Autowired
	ServerInformationTransmitter transmitter;

	@Autowired
	Router router;

	@Value("${ui.client.title}")
	private String windowClientTitle;

	/**
	 * Startet die JavaFX Stage.
	 *
	 * @param stage Die MainStage der Anwendung.
	 * @throws Exception Exceptions während dem Starten der JavaFX Stage.
	 */
	@Override
	public void start(Stage stage) throws Exception {



		notifyPreloader(new Preloader.StateChangeNotification(Preloader.StateChangeNotification.Type.BEFORE_START));

		router.setStage(stage);
		router.setSceneContent("/main.fxml");

		stage.setTitle(windowClientTitle);
		stage.setResizable(true);
		stage.centerOnScreen();
		stage.show();
		stage.getIcons().add(new Image("/soap.png"));

		// Logout bei schliessen der Stage
		stage.setOnCloseRequest(new javafx.event.EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent windowEvent) {
				logout();
			}
		});
		router.setStage(stage);

		serverConfig.startServer();
	}

     private void logout(){
		 Platform.runLater( new Runnable(){
		 	@Override
			 public void run(){
				Iterable<ForwardingConfig> ipList = forwardingDAO.findAll();
				Iterable<ServerInfo> superNodes = serverDAO.findAll() ;
				for(ForwardingConfig connections : ipList){  // verbundene server
					transmitter.sendLogoutRequest(connections.getUrl(),serverConfig.getServerIp());
				}
				for(ServerInfo superNode : superNodes){
					transmitter.sendLogoutRequest(superNode.getServerIp(),serverConfig.getServerIp());
				}
			}
		 }
		 );

	 }



	/**
	 * Die Main Methode des Programms.
	 *
	 * @param args Kommandozeilenparameter.
	 */
	public static void main(String args[]) {
		launchApp(Application.class, args);
	}

}