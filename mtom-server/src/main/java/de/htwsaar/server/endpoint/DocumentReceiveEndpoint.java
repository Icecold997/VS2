package de.htwsaar.server.endpoint;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


import de.htwsaar.Document;
import de.htwsaar.FileView;
import de.htwsaar.StoreDocumentRequest;
import de.htwsaar.StoreDocumentResponse;
import de.htwsaar.server.config.FloodingTransmitter;
import de.htwsaar.server.config.ServerConfig;
import de.htwsaar.server.persistence.FileArrangementDAO;
import de.htwsaar.server.persistence.FileArrangementConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;



@Endpoint
public class DocumentReceiveEndpoint {

	@Autowired
	FileArrangementDAO fileArrangementDao;

	@Autowired
	ServerConfig serverConfig;

	@Autowired
	FloodingTransmitter floodingTransmitter;

	private static final String NAMESPACE_URI = "http://htwsaar.de/";

	/**
	 * Endpoint für den Dateiempfang
	 *
	 * @param request request
	 * @return response
	 * @throws IOException Exception für Dateioperationen
	 */
	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "storeDocumentRequest")
	@ResponsePayload
	public StoreDocumentResponse storeDocument(@RequestPayload StoreDocumentRequest request) throws IOException {

		StoreDocumentResponse response = new StoreDocumentResponse();

	     	System.out.println("Datei empfangen : DateiName: " + request.getDocument().getName());
	     	System.out.println("ServerGruppe :" + serverConfig.getServerGroup());
	     	System.out.println("ServerRang : "  + serverConfig.getServerRang());
	     	FileArrangementConfig fileArrangementConfig = new FileArrangementConfig();
			Document document = request.getDocument();

			fileArrangementConfig.setFilename(document.getName());
			fileArrangementConfig.setFileLocation(serverConfig.fileDirectory + "/");
			fileArrangementConfig.setLocal(true);
			fileArrangementConfig.setSourceIp(document.getSourceUri());
			fileArrangementConfig.setFileRang(serverConfig.getServerRang());
			fileArrangementConfig.setFileDepartment(serverConfig.getServerGroup());
	    	  if(!fileExist(request.getDocument().getName())) {
			      fileArrangementDao.save(fileArrangementConfig);
				  floodingTransmitter.floodReceivedFile(request);
				  System.out.println("Datei in Datenbank aufgenommen");
		      }else{
		      	 fileArrangementDao.deleteByfilename(fileArrangementConfig.getFilename());
				 fileArrangementDao.save(fileArrangementConfig);
				  System.out.println("Datei schon vorhanden wird überschrieben");
			  }

			byte[] demBytes = document.getContent();  // datei in byteform aus der soap nachricht holen

			File outputFile = new File(serverConfig.fileDirectory + "/" + document.getName()); //  ort an dem datei gespeichert wird

			try (FileOutputStream outputStream = new FileOutputStream(outputFile);) { // bytes aus nachricht in datei zurückschreiben
				outputStream.write(demBytes);
				outputStream.close();
				response.setSuccess(true);
				response.setFileInformation(fileArragementConfigToFileView(fileArrangementConfig));
			} catch (Exception e) {
				e.printStackTrace();
			}



		return response;
	}

	private boolean fileExist(String fileName){
	  return fileArrangementDao.findByfilename(fileName).isPresent();
	}

	private FileView fileArragementConfigToFileView(FileArrangementConfig fileArrangementConfig) {
		FileView fileInformation = new FileView();
		fileInformation.setSourceIp(fileArrangementConfig.getSourceIp());
		fileInformation.setFileOrDirectoryName(fileArrangementConfig.getFilename());
		fileInformation.setDate(fileArrangementConfig.getUpdated_at().toString());
		if (fileArrangementConfig.isDirectory()) {
			fileInformation.setType("Directory");
		}else{
			fileInformation.setType("File");
			fileInformation.setRequestRootDirName(serverConfig.getRootDirectory());
		}
		return fileInformation;
	}
}
