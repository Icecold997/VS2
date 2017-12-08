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
    public boolean sendRequestToParent(String targetUrl,List<Directory> directoryList){

        SendDirectoryInformationToParentRequest request = new SendDirectoryInformationToParentRequest();
        for(Directory directory :directoryList){
            request.getDirectory().add(directory);
        }
        SendDirectoryInformationToParentResponse response = (SendDirectoryInformationToParentResponse) getWebServiceTemplate().marshalSendAndReceive("http://localhost:9090/ws/documents",request);
        return true;
    }
}
