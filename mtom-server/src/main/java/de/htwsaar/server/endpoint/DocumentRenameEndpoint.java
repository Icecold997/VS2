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
import de.htwsaar.server.persistence.FileArrangementConfig;
import de.htwsaar.server.persistence.FileArrangementDAO;
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
    FileViewList fileViewList;

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

       Optional<FileArrangementConfig> fileArrangementConfig =  fileArrangementDao.findByfilename(request.getCurrentDocumentName());
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
                if(fileArrangementConfig.get().isDirectory()){
                    fileView.setType("Directory");
                    oldFileView.setType("Directory");
                }else{
                    oldFileView.setType("File");
                    fileView.setType("File");
                }
                System.out.println("lösche datei aus gui : " + oldFileView.getFileOrDirectoryName());
                fileViewList.deleteFileView(oldFileView);
                fileView.setFileOrDirectoryName(request.getNewDocumentName());
                System.out.println("füge neue datei in gui hinzu" + fileView.getFileOrDirectoryName());
                fileViewList.addFileView(fileView);
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
