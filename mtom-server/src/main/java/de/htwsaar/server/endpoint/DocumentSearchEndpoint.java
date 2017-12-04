package de.htwsaar.server.endpoint;

import java.io.IOException;


import de.htwsaar.SearchDocumentRequest;
import de.htwsaar.SearchDocumentResponse;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;



@Endpoint
public class DocumentSearchEndpoint {

    private static final String NAMESPACE_URI = "http://htwsaar.de/";

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "searchDocumentRequest")
    @ResponsePayload
    public SearchDocumentResponse searchDocument(@RequestPayload SearchDocumentRequest request) throws IOException {

 //TODO usecase: datei in filearrangement suchen ,wen local vorhanden gib found  zurück ,wen nicht suche in kindern weiter

        SearchDocumentResponse response = new SearchDocumentResponse();
        response.setFound(true);
        return response;
    }

}
