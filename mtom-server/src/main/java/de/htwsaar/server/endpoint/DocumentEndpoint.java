package de.htwsaar.server.endpoint;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;


import de.htwsaar.Document;
import de.htwsaar.StoreDocumentRequest;
import de.htwsaar.StoreDocumentResponse;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;



@Endpoint
public class DocumentEndpoint {

	private static final String NAMESPACE_URI = "http://htwsaar.de/";

	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "storeDocumentRequest")
	@ResponsePayload
	public StoreDocumentResponse storeDocument(
			@RequestPayload StoreDocumentRequest request) throws IOException {
		Document document = request.getDocument();
		System.out.println("DateiName: "+ document.getName());
		byte[] demBytes = document.getContent();

		File outputFile = new File("C:/input/"+document.getName());

		try (FileOutputStream outputStream = new FileOutputStream(outputFile); ) {

			outputStream.write(demBytes);

		} catch (Exception e) {
			e.printStackTrace();
		}

		StoreDocumentResponse response = new StoreDocumentResponse();
		response.setSuccess(true);
		return response;
	}

}
