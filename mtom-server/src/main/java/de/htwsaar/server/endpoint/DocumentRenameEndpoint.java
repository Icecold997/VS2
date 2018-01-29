package de.htwsaar.server.endpoint;

import java.io.File;
import java.io.IOException;
import java.util.Optional;


import de.htwsaar.FileView;
import de.htwsaar.RenameDocumentRequest;
import de.htwsaar.RenameDocumentResponse;
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
public class DocumentRenameEndpoint {

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
     * Endpoint für die Dateiumbenennung.
     *
     * @param request request
     * @return response
     * @throws IOException Exception für Dateioperationen
     */
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "renameDocumentRequest")
    @ResponsePayload
    public RenameDocumentResponse renameDocument(@RequestPayload RenameDocumentRequest request) throws IOException {

        String workPath  ;
        System.out.println("rename request path: " + request.getPath());
        System.out.println("rename request root: " + request.getRequestRootDirName());
        String newPath1 = request.getPath().substring(request.getPath().indexOf(request.getRequestRootDirName())+request.getRequestRootDirName().length(),request.getPath().length());

        if(newPath1.isEmpty()){  //root directory
            System.out.println("root");
            workPath = serverConfig.fileDirectory;
        }else{  //sub dir
            System.out.println("sub dir: "+newPath1);
            workPath   = serverConfig.fileDirectory;
            workPath   = workPath + newPath1;
        }
        System.out.println("Rename Endpoint: " + workPath);

       Optional<FileArrangementConfig> fileArrangementConfig =  fileArrangementDao.findByFileLocationAndFilename(workPath,request.getCurrentDocumentName());
       RenameDocumentResponse response = new RenameDocumentResponse();
        if(fileArrangementConfig.isPresent()){
            File oldFile = new File(fileArrangementConfig.get().getFileLocation()+"/"+fileArrangementConfig.get().getFilename());
            File newFile = new File(fileArrangementConfig.get().getFileLocation()+"/"+request.getNewDocumentName());



            if(oldFile.renameTo(newFile)){

                System.out.println("File name changed succesful");
                FileView oldFileView = new FileView();
                oldFileView.setFileOrDirectoryName(request.getCurrentDocumentName());
                FileView fileView = new FileView();
                fileView.setFileOrDirectoryName(request.getNewDocumentName());
                fileView.setDate(fileArrangementConfig.get().getUpdated_at().toString());
                fileView.setPath(workPath);
                if(fileArrangementConfig.get().isDirectory()){
                    fileView.setType("Directory");
                    oldFileView.setType("Directory");
                }else{
                    oldFileView.setType("File");
                    fileView.setType("File");
                }

                fileView.setFileOrDirectoryName(request.getNewDocumentName());


                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            System.out.println("lösche datei aus gui : " + oldFileView.getFileOrDirectoryName());
                            mainController.deleteFile(oldFileView);
                            Thread.currentThread().sleep(1000);
                            System.out.println("füge neue datei in gui hinzu" + fileView.getFileOrDirectoryName());
                            mainController.addItem(fileView);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });

                response.setNewFile(fileView);
            }
            fileArrangementConfig.get().setFilename(request.getNewDocumentName());
            fileArrangementDao.save(fileArrangementConfig.get());

            floodingTransmitter.floodRenameRequest(request);
       }



        response.setSuccess(true);
        return response;
    }

}
