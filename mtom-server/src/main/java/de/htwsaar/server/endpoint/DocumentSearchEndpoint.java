package de.htwsaar.server.endpoint;

import java.io.IOException;
import java.util.List;
import java.util.Optional;


import de.htwsaar.FileView;
import de.htwsaar.SearchDocumentRequest;
import de.htwsaar.SearchDocumentResponse;
import de.htwsaar.server.config.ServerConfig;
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
    ServerConfig serverConfig;
    @Autowired
    ServerInformationTransmitter transmitter;

    @Autowired
    FileArrangementDAO fileArrangementDao;
    @Autowired
    ForwardingDAO forwardingDAO;

    private static final String NAMESPACE_URI = "http://htwsaar.de/";

    /**
     * Endpoint für die Dateisuche
     *
     * @param request request
     * @return response
     * @throws IOException Exception für Dateioperationen
     */
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "searchDocumentRequest")
    @ResponsePayload
    public SearchDocumentResponse searchDocument(@RequestPayload SearchDocumentRequest request) throws IOException {

 //TODO usecase: datei in filearrangement suchen ,wen local vorhanden gib found  zurück ,wen nicht suche in kindern weiter

        Optional<FileArrangementConfig> files = fileArrangementDao.findByfilename(request.getDocumentName());
        SearchDocumentResponse response = new SearchDocumentResponse();
        response.setFound(false);

        if(files.isPresent()){
            FileView foundData = new FileView();
            foundData.setDate(files.get().getUpdated_at().toString());
            foundData.setFileOrDirectoryName(files.get().getFilename());
            if(files.get().isDirectory()){
                foundData.setType("Directory");
            }else{
                foundData.setType("File");
            }
            foundData.setSourceIp(serverConfig.getServerIp());
            response.setFound(true);
            response.getFile().add(foundData);
        }
            Optional<List<ForwardingConfig>> childs = forwardingDAO.findAllByisParent(false);
            if(childs.isPresent()){
                for (ForwardingConfig f: childs.get()) {
                    SearchDocumentResponse responseFromChild =transmitter.sendSearchRequestToChild(f.getUrl(),request.getDocumentName());
                    if(responseFromChild.isFound()){
                        response.getFile().addAll(responseFromChild.getFile());
                    }

                }
            }

       return  response;
    }

}
