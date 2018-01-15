package de.htwsaar.server.endpoint;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import de.htwsaar.DeleteDocumentRequest;
import de.htwsaar.DeleteDocumentResponse;


import de.htwsaar.server.persistence.FileArrangementConfig;
import de.htwsaar.server.persistence.FileArrangementDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;



@Endpoint
public class DocumentDeleteEndpoint {

    @Autowired
    FileArrangementDAO fileArrangementDao;

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

        Optional<FileArrangementConfig> fileArrangementConfig =  fileArrangementDao.findByfilename(request.getDocumentName());
        if(fileArrangementConfig.isPresent()) {
            File file = new File(fileArrangementConfig.get().getFileLocation()+"/"+fileArrangementConfig.get().getFilename());
            if(file.delete()){
                System.out.println(file.getName()+" is deleted");
                fileArrangementDao.deleteByfilename(request.getDocumentName());
            }
        }
        DeleteDocumentResponse response = new DeleteDocumentResponse();
        response.setSuccess(true);
        return response;
    }

}
