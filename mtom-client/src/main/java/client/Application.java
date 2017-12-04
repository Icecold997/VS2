package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import client.ws.DocumentsClient;

@Configuration
@ComponentScan
@EnableAutoConfiguration
public class Application {

	public static void main(final String[] args) throws Exception {
		final SpringApplication springApplication = new SpringApplication(
				Application.class);
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
	}

}