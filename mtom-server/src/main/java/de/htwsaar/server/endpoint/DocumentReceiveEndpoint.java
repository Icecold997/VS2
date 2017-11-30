package de.htwsaar.server.endpoint;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


import de.htwsaar.Document;
import de.htwsaar.StoreDocumentRequest;
import de.htwsaar.StoreDocumentResponse;
import de.htwsaar.server.persistence.ServerDAO;
import de.htwsaar.server.persistence.ServerInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;



@Endpoint
public class DocumentReceiveEndpoint {

	@Autowired
	ServerDAO serverDao;

	private static final String NAMESPACE_URI = "http://htwsaar.de/";

	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "storeDocumentRequest")
	@ResponsePayload
	public StoreDocumentResponse storeDocument(@RequestPayload StoreDocumentRequest request) throws IOException {

		ServerInfo serverInfo = new ServerInfo();       //Datenbank beispiel
		serverInfo.setServerIp("192.168.0.1");
		serverInfo = serverDao.save(serverInfo);

		Document document = request.getDocument();
		System.out.println("Datei empfangen : DateiName: "+ document.getName());
		byte[] demBytes = document.getContent();  // datei in byteform aus der soap nachricht holen

		File outputFile = new File("C:/input/"+document.getName()); //  ort an dem datei gespeichert wird

		try (FileOutputStream outputStream = new FileOutputStream(outputFile); ) { // bytes aus nachricht in datei zurückschreiben

			outputStream.write(demBytes);

		} catch (Exception e) {
			e.printStackTrace();
		}

		StoreDocumentResponse response = new StoreDocumentResponse();
		response.setSuccess(true);
		return response;
	}

}
