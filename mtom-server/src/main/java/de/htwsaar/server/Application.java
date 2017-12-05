package de.htwsaar.server;


import de.htwsaar.server.config.ServerConfig;
import javafx.application.Preloader;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Lazy;
import org.springframework.ws.WebServiceException;



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
	ServerConfig serverConfig;



	/**
	 * Startet die JavaFX Stage.
	 *
	 * @param stage Die MainStage der Anwendung.
	 * @throws Exception Exceptions während dem Starten der JavaFX Stage.
	 */
	@Override
	public void start(Stage stage) throws Exception {



		notifyPreloader(new Preloader.StateChangeNotification(Preloader.StateChangeNotification.Type.BEFORE_START));


		serverConfig.startServer();
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