package client.ws;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import de.htwsaar.*;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;



public class DocumentsClient extends WebServiceGatewaySupport {


	public DocumentsClient() {}

	/**
	 *
	 * @param pathToFile im format C:/../myfile.t
	 * @return
	 * @throws IOException
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
				.marshalSendAndReceive("http://localhost:9090/ws/documents",request);

		if(response.isSuccess()){
			System.out.println("Datei erfolgreich versendet");
		}
		return response.getFileInformation();
	}

   public boolean renameDocument(String oldFileName,String newFileName){
	   RenameDocumentRequest request = new RenameDocumentRequest();
	   request.setCurrentDocumentName(oldFileName);
	   request.setNewDocumentName(newFileName);
	   RenameDocumentResponse response =(RenameDocumentResponse) getWebServiceTemplate().marshalSendAndReceive(request);
	   boolean success = response.isSuccess();
	   if(success){
	   	System.out.println("Datei erfolgreich umbenannt");
	   }
	   return success;
   }

   public boolean deleteDocument(String fileName){
   	  DeleteDocumentRequest request = new DeleteDocumentRequest();
	   request.setDocumentName(fileName);
	   DeleteDocumentResponse response = (DeleteDocumentResponse) getWebServiceTemplate().marshalSendAndReceive(request);
	   boolean success = response.isSuccess();
	   if(success){
	   	System.out.println("Datei erfolgreich gelöscht");
	   }
   	   return success;

   }

	public DirectoryInformationResponse sendDirectoryInformationRequest(String url) throws IOException {
		DirectoryInformationRequest request = new DirectoryInformationRequest();
		DirectoryInformationResponse response = (DirectoryInformationResponse) getWebServiceTemplate()
				.marshalSendAndReceive(request);
		return response;
	}

	public Document downloadFileFromServer(String fileName,String url){
	    DownloadDocumentRequest request = new DownloadDocumentRequest();
        request.setFileName(fileName);
        DownloadDocumentResponse response = (DownloadDocumentResponse) getWebServiceTemplate()
                .marshalSendAndReceive("http://localhost:9090/ws/documents",request);
         return response.getDocument();
    }
}
