
package client.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import client.ws.DocumentsClient;

@Configuration
public class WebServiceConfig {

	@Bean
	public Jaxb2Marshaller marshaller() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setContextPath("de.htwsaar");
		marshaller.setMtomEnabled(true);
		return marshaller;
	}

	@Bean
	public DocumentsClient documentsClient(Jaxb2Marshaller marshaller) {
		DocumentsClient client = new DocumentsClient();
		client.setDefaultUri("http://localhost:9090/ws/documents");
		client.setMarshaller(marshaller);
		client.setUnmarshaller(marshaller);
		return client;
	}

}
