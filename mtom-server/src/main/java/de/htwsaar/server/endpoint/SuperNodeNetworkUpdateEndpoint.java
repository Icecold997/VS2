package de.htwsaar.server.endpoint;


import de.htwsaar.*;
import de.htwsaar.server.config.ServerConfig;
import de.htwsaar.server.persistence.ForwardingConfig;
import de.htwsaar.server.persistence.ForwardingDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Endpoint
public class SuperNodeNetworkUpdateEndpoint {

    @Autowired
    ForwardingDAO forwardingDAO;

    @Autowired
    ServerConfig serverConfig;

    private static final String NAMESPACE_URI = "http://htwsaar.de/";

    /**
     * Endpoint für SuperNodes zum updaten des Netzwerkstatus
     *
     * @param request request
     * @return response
     * @throws IOException Exception für Schreiboperationen
     */
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "superNodeInformationRequest")
    @ResponsePayload
    public SuperNodeInformationResponse getNetworkInformation(@RequestPayload SuperNodeInformationRequest request) throws IOException {

        System.out.println("SuperNode erreicht.Update des Netzwerkstatus in der DB");
        for(ConnectionConfig connectionConfigToUpdate : request.getConnectionConfig() ){
            this.updateNetworkInformation(connectionConfigToUpdate);
        }
        return  new SuperNodeInformationResponse();

    }

    private void updateNetworkInformation(ConnectionConfig connectionConfig){
        Iterable<ForwardingConfig> networkInformationList = forwardingDAO.findAll();
        for(ForwardingConfig forwardingConfig : networkInformationList){
             if(forwardingConfig.getUrl().equals(connectionConfig.getIp())){ // eintrag in datenbank anhand von Ip finden
                 forwardingConfig.setConnections(connectionConfig.getConnections());
                 System.out.println("Eintrag: "+forwardingConfig.getUrl()+ "  in datenbank geUpdatet");
                 forwardingDAO.save(forwardingConfig);
             }
        }
    }


}
