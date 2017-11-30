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

		String pathToFile = readPathToFile();
		while (pathToFile != null) {
			documentsClient.storeDocument(pathToFile);
			pathToFile = readPathToFile();
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