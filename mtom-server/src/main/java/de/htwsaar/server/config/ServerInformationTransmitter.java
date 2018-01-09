package de.htwsaar.server.config;

import de.htwsaar.*;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

import java.util.List;

/**
 * Created by Timo on 08.12.2017.
 */
public class ServerInformationTransmitter extends WebServiceGatewaySupport {

 //TODO testen

    public boolean sendRequestToParent(String targetUrl,Directory directory,String ownUrl){

        SendDirectoryInformationToParentRequest request = new SendDirectoryInformationToParentRequest();
        request.setDirectory(directory);
        request.setIp(ownUrl);
        String finalUrl = "http://"+targetUrl+":9090/ws/documents";
        SendDirectoryInformationToParentResponse response = (SendDirectoryInformationToParentResponse) getWebServiceTemplate().marshalSendAndReceive(finalUrl,request);
        return true;
    }

    public boolean sendSearchRequestToChild(String targetUrl,String fileName){
        SearchDocumentRequest request = new SearchDocumentRequest();
        request.setDocumentName(fileName);
        String finalUrl = "http://"+targetUrl+":9090/ws/documents";
        SearchDocumentResponse response=(SearchDocumentResponse) getWebServiceTemplate().marshalSendAndReceive(finalUrl,request);
        return response.isFound();
    }
}
