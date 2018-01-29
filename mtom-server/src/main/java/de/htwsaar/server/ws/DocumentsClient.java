package de.htwsaar.server.ws;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import de.htwsaar.*;
import de.htwsaar.server.config.GUID;
import de.htwsaar.server.config.ServerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;



public class DocumentsClient extends WebServiceGatewaySupport {

	@Autowired
	ServerConfig serverConfig;

	private GUID guid = new GUID();
	public DocumentsClient() {}


	/**
	 *
	 * @param pathToFile im format C:/../myfile.t
	 * @return
	 * @throws IOException
     */
	public FileView storeDocument(String pathToFile,String path) throws IOException {
		Document document = new Document();
		Path inputPath  = new File(pathToFile).toPath();
		byte[] array = Files.readAllBytes(inputPath);
		document.setContent(array);
		document.setPath(path);
		document.setRequestRootDirName(serverConfig.getRootDirectory());
		document.setName(inputPath.getFileName().toString());
		document.setSourceUri(serverConfig.getServerIp());
		StoreDocumentRequest request = new StoreDocumentRequest();
		request.setDocument(document);
        request.setGuid(guid.generateGUID());
		StoreDocumentResponse response = (StoreDocumentResponse) getWebServiceTemplate()
				.marshalSendAndReceive("http://"+serverConfig.getServerIp()+":9090/ws/documents",request);

		if(response.isSuccess()){
			System.out.println("Datei erfolgreich versendet");
		}
		return response.getFileInformation();
	}


   public FileView renameDocument(String oldFileName,String newFileName,String path){
	   RenameDocumentRequest request = new RenameDocumentRequest();
	   request.setCurrentDocumentName(oldFileName);
	   request.setSourceIp(serverConfig.getServerIp());
	   request.setRequestRootDirName(serverConfig.getRootDirectory());
	   request.setNewDocumentName(newFileName);
	   request.setPath(path);
	   request.setGuid(guid.generateGUID());
	   RenameDocumentResponse response =(RenameDocumentResponse) getWebServiceTemplate().marshalSendAndReceive("http://"+serverConfig.getServerIp()+":9090/ws/documents",request);
	   boolean success = response.isSuccess();
	   if(success){
	   	System.out.println("Datei erfolgreich umbenannt");
	   }

	   return response.getNewFile();
   }

   public boolean deleteDocument(String fileName,String path){
   	  DeleteDocumentRequest request = new DeleteDocumentRequest();
	   request.setDocumentName(fileName);
	   request.setSourceIp(serverConfig.getServerIp());
	   request.setRequestRootDirName(serverConfig.getRootDirectory());
	   request.setPath(path);
	   request.setGuid(guid.generateGUID());
	   DeleteDocumentResponse response = (DeleteDocumentResponse) getWebServiceTemplate().marshalSendAndReceive("http://"+serverConfig.getServerIp()+":9090/ws/documents",request);
	   boolean success = response.isSuccess();
	   if(success){
	   	System.out.println("Datei erfolgreich gelöscht");
	   }else{
		   System.out.println("Datei konnte nicht gelöscht werden");
	   }
   	   return success;

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
		request.setGuid(guid.generateGUID());
		SearchDocumentResponse response =(SearchDocumentResponse) getWebServiceTemplate().marshalSendAndReceive("http://"+serverConfig.getServerIp()+":9090/ws/documents",request);
		return response.getFile();
	}

	public void createDir(String dirName,String dirPath){
		CreateDirectoryRequest request = new CreateDirectoryRequest();
		request.setDirectoryName(dirName);
		request.setPath(dirPath);
		request.setGuid(guid.generateGUID());
		request.setRequestRootDirName(serverConfig.getRootDirectory());
		request.setSourceIp(serverConfig.getServerIp());
		CreateDirectoryResponse response = (CreateDirectoryResponse) getWebServiceTemplate().marshalSendAndReceive("http://"+serverConfig.getServerIp()+":9090/ws/documents",request);

	}

	public DirectoryInformationResponse sendDirectoryInformationRequest(String url,String path) throws IOException {
		DirectoryInformationRequest request = new DirectoryInformationRequest();
		request.setPath(path);
		request.setGuid(guid.generateGUID());
		request.setRequestRootDirName(serverConfig.getRootDirectory());
		DirectoryInformationResponse response = (DirectoryInformationResponse) getWebServiceTemplate()
				.marshalSendAndReceive(url,request);
		return response;
	}

	public Document downloadFileFromServer(String fileName ,String url,String path){
		DownloadDocumentRequest request = new DownloadDocumentRequest();
		request.setFileName(fileName);
		request.setPath(path);
		request.setRequestRootDirName(serverConfig.getRootDirectory());
		DownloadDocumentResponse response = (DownloadDocumentResponse) getWebServiceTemplate()
				.marshalSendAndReceive(url,request);
		return response.getDocument();
	}
}
