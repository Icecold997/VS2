package client;

import client.gui.Router;
import de.htwsaar.AbstractJavaFxApp;
import javafx.application.Preloader;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
@EnableAutoConfiguration
public class App  extends AbstractJavaFxApp {

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
	public void start(Stage stage) {
		notifyPreloader(new Preloader.StateChangeNotification(Preloader.StateChangeNotification.Type.BEFORE_START));

		router.setStage(stage);
		router.setSceneContent("/login.fxml", "/theme.css", 500, 300);

		stage.setTitle(windowClientTitle);
		stage.setResizable(true);
		stage.centerOnScreen();
		stage.show();
		stage.getIcons().add(new Image("/soap.png"));
		router.setStage(stage);
	}

	/**
	 * Die Main Methode des Programms.
	 *
	 * @param args Kommandozeilenparameter.
	 */
	public static void main(String args[]) {
		launchApp(App.class, args);
	}


/*	@Override
	public void start(Stage stage) throws Exception {

	}

	public static void main(final String[] args) throws Exception {
		final SpringApplication springApplication = new SpringApplication(
				App.class);
		ApplicationContext ctx = springApplication.run();
		DocumentsClient documentsClient = ctx.getBean(DocumentsClient.class);

		String result = null;

		while(result == null){
			System.out.println("Geben Sie eine der Zahlen ein :\n1: Neue Datei anlegen\n2:Datei umbenennen\n3:Datei löschen\n");
			String line = readLine();

			switch (line){
				case "1" : documentsClient.storeDocument(readPathToFile());
					break;

				case "2" :
					System.out.println("Alten DateiNamen eingeben:\n");
					String oldFileName =readLine();
					System.out.println("Neuen DateiNamen eingeben:\n");
					String newFileName =readLine();
					documentsClient.renameDocument(oldFileName,newFileName);
					break;

				case "3" :
					System.out.println("DateiNamen der zu löschenden Datei eingeben:\n");
					String fileName =readLine();
					documentsClient.deleteDocument(fileName);
					break;
				default: break;
			}
		}


		System.out.println("exit");
	}


	private static String readPathToFile() throws IOException {
		String result = null;
		while (result == null) {
			System.out.printf("\nDateipfad eingeben im Format C:/../myfile.endung: ");
			System.out.printf("\nnichts eingeben um programm zu verlassen\n");
			String line = readLine();
			if (line == null || line.trim().isEmpty()) {
				return null;
			}else{
				result = line;
			}

		}
		return result;
	}

	private static String readLine() throws IOException {
		return new BufferedReader(new InputStreamReader(System.in)).readLine();
	}*/

}