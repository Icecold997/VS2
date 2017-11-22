package client.ws;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import de.htwsaar.Document;
import de.htwsaar.StoreDocumentRequest;
import de.htwsaar.StoreDocumentResponse;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;



public class DocumentsClient extends WebServiceGatewaySupport {


	public DocumentsClient() {}


	public boolean storeDocument(int size) throws IOException {
		Document document = new Document();
		document.setName(Integer.toString(size));
		Path inputPath  = new File("C:/Keil/myfile.txt").toPath();  //datei auswählen
		byte[] array = Files.readAllBytes(inputPath);
		document.setContent(array);
		document.setName(inputPath.getFileName().toString());
		StoreDocumentRequest request = new StoreDocumentRequest();
		request.setDocument(document);

		System.out.println();
		System.out.println("Storing document of size " + size);

		StoreDocumentResponse response = (StoreDocumentResponse) getWebServiceTemplate()
				.marshalSendAndReceive(request);
		boolean success = response.isSuccess();

		return success;
	}


}
