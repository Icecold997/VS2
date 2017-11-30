package de.htwsaar.server.endpoint;

import java.io.IOException;


import de.htwsaar.RenameDocumentRequest;
import de.htwsaar.RenameDocumentResponse;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;



@Endpoint
public class DocumentRenameEndpoint {

    private static final String NAMESPACE_URI = "http://htwsaar.de/";

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "renameDocumentRequest")
    @ResponsePayload
    public RenameDocumentResponse renameDocument(@RequestPayload RenameDocumentRequest request) throws IOException {


        RenameDocumentResponse response = new RenameDocumentResponse();
        response.setSuccess(true);
        return response;
    }

}
