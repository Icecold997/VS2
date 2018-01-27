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
import de.htwsaar.server.config.ServerInformationTransmitter;
import de.htwsaar.server.gui.FileViewList;
import de.htwsaar.server.gui.MainController;
import de.htwsaar.server.persistence.FileArrangementDAO;
import de.htwsaar.server.persistence.FileArrangementConfig;
import javafx.application.Platform;
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

	@Autowired
	MainController mainController;

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
			FileArrangementConfig fileArrangementConfig = new FileArrangementConfig();
			Document document = request.getDocument();

			fileArrangementConfig.setFilename(document.getName());
			fileArrangementConfig.setFileLocation(request.getDocument().getPath());
			fileArrangementConfig.setLocal(true);
			fileArrangementConfig.setSourceIp(document.getSourceUri());
	    	  if(!fileExist(request.getDocument().getPath(),request.getDocument().getName())) {
			    fileArrangementDao.save(fileArrangementConfig);
				  System.out.println("Datei in Datenbank aufgenommen");
		      }else{
		      	 fileArrangementDao.deleteByfilenameAndFileLocation(fileArrangementConfig.getFilename(),request.getDocument().getPath());
				 fileArrangementDao.save(fileArrangementConfig);
				  System.out.println("Datei schon vorhanden wird überschrieben");
			  }


			byte[] demBytes = document.getContent();  // datei in byteform aus der soap nachricht holen

			File outputFile = new File(request.getDocument().getPath() + "/" + document.getName()); //  ort an dem datei gespeichert wird

			try (FileOutputStream outputStream = new FileOutputStream(outputFile);) { // bytes aus nachricht in datei zurückschreiben
				outputStream.write(demBytes);
				outputStream.close();
				response.setSuccess(true);
				response.setFileInformation(fileArragementConfigToFileView(fileArrangementConfig));
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            mainController.addItem(response.getFileInformation());
                        }
                    });

			} catch (Exception e) {
				//e.printStackTrace();
			}

		floodingTransmitter.floodReceivedFile(request);

		return response;
	}

	private boolean fileExist(String path,String name){
	  return fileArrangementDao.findByFileLocationAndFilename(path,name).isPresent();
	}

	private FileView fileArragementConfigToFileView(FileArrangementConfig fileArrangementConfig) {
		FileView fileInformation = new FileView();
		fileInformation.setSourceIp(fileArrangementConfig.getSourceIp());
		fileInformation.setFileOrDirectoryName(fileArrangementConfig.getFilename());
		fileInformation.setPath(fileArrangementConfig.getFileLocation());
		fileInformation.setDate(fileArrangementConfig.getUpdated_at().toString());
		if (fileArrangementConfig.isDirectory()) {
			fileInformation.setType("Directory");
		}else{
			fileInformation.setType("File");
		}
		return fileInformation;
	}
}
