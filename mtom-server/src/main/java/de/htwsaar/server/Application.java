package de.htwsaar.server;


import de.htwsaar.AbstractJavaFxApp;
import de.htwsaar.server.config.ServerConfig;
import javafx.application.Preloader;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Lazy;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;


/**
 * Die Main Klasse der Applikation. Erbt von der abstrakten JavaFx-Oberklasse um Spring-Support
 * auch in JavaFX-Klassen zu gewährleisten.
 *
 * @author cedosw
 */
@Lazy
@SpringBootApplication
@ComponentScan({"de.htwsaar","de.htwsaar" })
public class Application extends AbstractJavaFxApp implements EmbeddedServletContainerCustomizer {

	private static Logger logger = LoggerFactory.getLogger(Application.class);

	@Value("${allowServerAddress}")
	private  boolean allowServerAddress;

	@Value("${server.address}")
	private String serverIp;

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

		launchApp(Application.class,args);
	}


	static InetAddress solveIP() {
		Enumeration<NetworkInterface> n = null;
		try {
			n = NetworkInterface.getNetworkInterfaces();
		} catch (SocketException e) {
			e.printStackTrace();
		}
		for (; n != null && n.hasMoreElements(); ) {
			NetworkInterface e = n.nextElement();

			Enumeration<InetAddress> a = e.getInetAddresses();
			for (; a.hasMoreElements(); ) {
				InetAddress addr = a.nextElement();
				if (addr.getHostAddress().length() <= 16) {
					if (!addr.getHostAddress().contains("127") && !addr.getHostAddress().contains("25.92")){
						return addr;
					}

				}
			}
		}
		return null;
	}

	/**
	 * Feststellung und Ausgabe der Server-IP
	 *
	 * @param container Container
	 */
	@Override
	public void customize(ConfigurableEmbeddedServletContainer container) {
		if (!allowServerAddress) {
			if (solveIP() != null) {
				InetAddress address = solveIP();
				serverIp = address.getHostAddress();
				serverConfig.serverIp = serverIp;
				container.setAddress(address);
				System.out.println("##### Server is running on: " + serverIp + ":9090 #####");
			}
		} else {
			System.out.println("##### Server is running on: " + serverIp + ":9090 #####");
		}
	}


}