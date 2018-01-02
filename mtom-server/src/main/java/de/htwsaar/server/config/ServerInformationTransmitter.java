package de.htwsaar.server.config;

import de.htwsaar.Directory;
import de.htwsaar.SendDirectoryInformationToParentRequest;
import de.htwsaar.SendDirectoryInformationToParentResponse;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

import java.util.List;

/**
 * Created by Timo on 08.12.2017.
 */
public class ServerInformationTransmitter extends WebServiceGatewaySupport {

 //TODO testen

    public boolean sendRequestToParent(String targetUrl,Directory directory){

        SendDirectoryInformationToParentRequest request = new SendDirectoryInformationToParentRequest();
        request.setDirectory(directory);
        String finalUrl = "http://"+targetUrl+":9090/ws/documents";
        SendDirectoryInformationToParentResponse response = (SendDirectoryInformationToParentResponse) getWebServiceTemplate().marshalSendAndReceive(finalUrl,request);
        return true;
    }
}
