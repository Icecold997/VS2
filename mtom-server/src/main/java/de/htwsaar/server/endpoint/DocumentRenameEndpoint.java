package de.htwsaar.server.endpoint;

import java.io.File;
import java.io.IOException;
import java.util.Optional;


import de.htwsaar.RenameDocumentRequest;
import de.htwsaar.RenameDocumentResponse;
import de.htwsaar.server.config.ServerConfig;
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

    private static final String NAMESPACE_URI = "http://htwsaar.de/";

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "renameDocumentRequest")
    @ResponsePayload
    public RenameDocumentResponse renameDocument(@RequestPayload RenameDocumentRequest request) throws IOException {

       Optional<FileArrangementConfig> fileArrangementConfig =  fileArrangementDao.findByfilename(request.getCurrentDocumentName());

        if(fileArrangementConfig.isPresent()){
            File oldFile = new File(fileArrangementConfig.get().getFileLocation()+"/"+fileArrangementConfig.get().getFilename());
            File newFile = new File(fileArrangementConfig.get().getFileLocation()+"/"+request.getNewDocumentName());
            if(oldFile.renameTo(newFile)){
                System.out.println("File name changed succesful");
            }
            fileArrangementConfig.get().setFilename(request.getNewDocumentName());
            fileArrangementDao.save(fileArrangementConfig.get());
       }

        RenameDocumentResponse response = new RenameDocumentResponse();
        response.setSuccess(true);
        return response;
    }

}
