package de.htwsaar.server.endpoint;

import java.io.IOException;

import de.htwsaar.DeleteDocumentRequest;
import de.htwsaar.DeleteDocumentResponse;


import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;



@Endpoint
public class DocumentDeleteEndpoint {

    private static final String NAMESPACE_URI = "http://htwsaar.de/";

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "deleteDocumentRequest")
    @ResponsePayload
    public DeleteDocumentResponse deleteDocument(@RequestPayload DeleteDocumentRequest request) throws IOException {


        DeleteDocumentResponse response = new DeleteDocumentResponse();
        response.setSuccess(true);
        return response;
    }

}
