package de.htwsaar.server.endpoint;




import java.io.IOException;


import de.htwsaar.DeleteDocumentRequest;
import de.htwsaar.DeleteDocumentResponse;

import de.htwsaar.SendDirectoryInformationToParentRequest;
import de.htwsaar.SendDirectoryInformationToParentResponse;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;



@Endpoint
public class DirectoryInformationEndpoint {


    private static final String NAMESPACE_URI = "http://htwsaar.de/";

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "sendDirectoryInformationToParentRequest")
    @ResponsePayload
    public SendDirectoryInformationToParentResponse getInfo(@RequestPayload SendDirectoryInformationToParentRequest request) throws IOException {

        //TODO informationen vom kind in datenbank aufnehmen
        SendDirectoryInformationToParentResponse response = new SendDirectoryInformationToParentResponse();
        return  response;
    }

}
