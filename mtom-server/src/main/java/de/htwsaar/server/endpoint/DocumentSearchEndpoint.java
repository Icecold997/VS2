package de.htwsaar.server.endpoint;

import java.io.IOException;
import java.util.List;
import java.util.Optional;


import de.htwsaar.SearchDocumentRequest;
import de.htwsaar.SearchDocumentResponse;
import de.htwsaar.server.config.ServerInformationTransmitter;
import de.htwsaar.server.persistence.FileArrangementConfig;
import de.htwsaar.server.persistence.FileArrangementDAO;
import de.htwsaar.server.persistence.ForwardingConfig;
import de.htwsaar.server.persistence.ForwardingDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;



@Endpoint
public class DocumentSearchEndpoint {

    @Autowired
    ServerInformationTransmitter transmitter;

    @Autowired
    FileArrangementDAO fileArrangementDao;
    @Autowired
    ForwardingDAO forwardingDAO;

    private static final String NAMESPACE_URI = "http://htwsaar.de/";

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "searchDocumentRequest")
    @ResponsePayload
    public SearchDocumentResponse searchDocument(@RequestPayload SearchDocumentRequest request) throws IOException {

 //TODO usecase: datei in filearrangement suchen ,wen local vorhanden gib found  zurück ,wen nicht suche in kindern weiter

        Optional<FileArrangementConfig> files = fileArrangementDao.findByfilename(request.getDocumentName());
        SearchDocumentResponse response = new SearchDocumentResponse();
        response.setFound(false);

        if(files.isPresent()){
            response.setFound(true);
            return response;
        }else{
            Iterable<ForwardingConfig> childs = forwardingDAO.findAll();
                for (ForwardingConfig f: childs) {
                    if(transmitter.sendSearchRequestToChild(f.getUrl(),request.getDocumentName())){
                        response.setFound(true);
                    }
                }
        }


       return  response;
    }

}
