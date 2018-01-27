package de.htwsaar.server.endpoint;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import de.htwsaar.DeleteDocumentRequest;
import de.htwsaar.DeleteDocumentResponse;


import de.htwsaar.FileView;
import de.htwsaar.server.config.FloodingTransmitter;
import de.htwsaar.server.config.ServerConfig;
import de.htwsaar.server.gui.FileViewList;
import de.htwsaar.server.gui.MainController;
import de.htwsaar.server.persistence.FileArrangementConfig;
import de.htwsaar.server.persistence.FileArrangementDAO;
import javafx.application.Platform;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;



@Endpoint
public class DocumentDeleteEndpoint {

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
     * Endpoint für die Dateilöschung
     *
     * @param request Anfrage
     * @return response
     * @throws IOException Exception für Dateioperationen
     */
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "deleteDocumentRequest")
    @ResponsePayload
    public DeleteDocumentResponse deleteDocument(@RequestPayload DeleteDocumentRequest request) throws IOException {
        DeleteDocumentResponse response = new DeleteDocumentResponse();
        response.setSuccess(true);
        String workPath  ;
        String newPath1 = request.getPath().substring(request.getPath().indexOf(request.getRequestRootDirName())+request.getRequestRootDirName().length(),request.getPath().length());

        if(newPath1.isEmpty()){  //root directory
            workPath = serverConfig.fileDirectory;
        }else{  //sub dir
            workPath   = serverConfig.fileDirectory;
            workPath   = workPath + newPath1;
        }
        System.out.println("delte endpoint: "+workPath);
        System.out.println("delte endpoint: "+request.getDocumentName());
        Optional<FileArrangementConfig> fileArrangementConfig =  fileArrangementDao.findByFileLocationAndFilename(workPath,request.getDocumentName());
        if(fileArrangementConfig.isPresent()) {
            System.out.println("delete endpoint: "+fileArrangementConfig.get().getFileLocation());
            System.out.println("delete endpoint: "+fileArrangementConfig.get().getFilename());
            File file = new File(fileArrangementConfig.get().getFileLocation()+"/"+fileArrangementConfig.get().getFilename());

            if(file.delete()){
                System.out.println(file.getName()+" is deleted update gui");
                FileView oldfile = fileArragementConfigToFileView(fileArrangementConfig.get());
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        mainController.deleteFile(oldfile);
                    }
                });

                fileArrangementDao.deleteByfilenameAndFileLocation(request.getDocumentName(),workPath);
                floodingTransmitter.floodDeleteFileRequest(request);
            }else{
                response.setSuccess(false);
            }
        }


        return response;
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
        }
        return fileInformation;
    }
}
