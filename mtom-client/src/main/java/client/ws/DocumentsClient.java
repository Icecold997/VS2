package client.ws;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import de.htwsaar.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;



public class DocumentsClient extends WebServiceGatewaySupport {

    @Autowired
    public UrlList urlList;

	public DocumentsClient() {}

	/**
	 *
	 * @param pathToFile im format C:/../myfile.t
	 * @return Dateiinformation
	 * @throws IOException Exception für den Reader
     */
	public FileView storeDocument(String pathToFile) throws IOException {
		Document document = new Document();
		Path inputPath  = new File(pathToFile).toPath();
		byte[] array = Files.readAllBytes(inputPath);
		document.setContent(array);
		document.setName(inputPath.getFileName().toString());
		StoreDocumentRequest request = new StoreDocumentRequest();
		request.setDocument(document);


		StoreDocumentResponse response = (StoreDocumentResponse) getWebServiceTemplate()
				.marshalSendAndReceive(urlList.getUrl(),request);

		if(response.isSuccess()){
			System.out.println("Datei erfolgreich versendet");
		}
		return response.getFileInformation();
	}

	/**
	 * Dateiumbenennung
	 *
	 * @param oldFileName alter Dateiname
	 * @param newFileName neuer Dateiname
	 * @return response Neue Datei
	 */
	public FileView renameDocument(String oldFileName,String newFileName){
	   RenameDocumentRequest request = new RenameDocumentRequest();
	   request.setCurrentDocumentName(oldFileName);
	   request.setNewDocumentName(newFileName);
	   RenameDocumentResponse response =(RenameDocumentResponse) getWebServiceTemplate().marshalSendAndReceive(urlList.getUrl(),request);
	   boolean success = response.isSuccess();
	   if(success){
	   	System.out.println("Datei erfolgreich umbenannt");
	   }

	   return response.getNewFile();
	}

	/**
	 * Dateilöschung
	 *
	 * @param fileName Zieldatei
	 * @return success
	 */
	public boolean deleteDocument(String fileName){
   	  DeleteDocumentRequest request = new DeleteDocumentRequest();
	   request.setDocumentName(fileName);
	   DeleteDocumentResponse response = (DeleteDocumentResponse) getWebServiceTemplate().marshalSendAndReceive(urlList.getUrl(),request);
	   boolean success = response.isSuccess();
	   if(success){
	   	System.out.println("Datei erfolgreich gelöscht");
	   }
   	   return success;
	}

	/**
	 * Senden der Directory-Informationen
	 *
	 * @param url Ziel-URL
	 * @return response
	 * @throws IOException Exception für sendandrecieve
	 */
	public DirectoryInformationResponse sendDirectoryInformationRequest(String url) throws IOException {
		DirectoryInformationRequest request = new DirectoryInformationRequest();
		DirectoryInformationResponse response = (DirectoryInformationResponse) getWebServiceTemplate()
				.marshalSendAndReceive(url,request);
		return response;
	}

	/**
	 * Datei-Download
	 *
	 * @param fileName Dateiname
	 * @return response Download
	 */
	public Document downloadFileFromServer(String fileName){
	    DownloadDocumentRequest request = new DownloadDocumentRequest();
        request.setFileName(fileName);
        DownloadDocumentResponse response = (DownloadDocumentResponse) getWebServiceTemplate()
                .marshalSendAndReceive(urlList.getUrl(),request);
         return response.getDocument();
    }

	/**
	 * Datei-Suche
	 *
	 * @param fileName Datei-Name
	 * @return response zum Fund
	 */
	public List<FileView> searchFile(String fileName){
		SearchDocumentRequest request = new SearchDocumentRequest();
		request.setDocumentName(fileName);
		SearchDocumentResponse response =(SearchDocumentResponse) getWebServiceTemplate().marshalSendAndReceive(urlList.getUrl(),request);
		return response.getFile();
	}
}
