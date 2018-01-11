package de.htwsaar.server.endpoint;


import de.htwsaar.ConnectionConfig;
import de.htwsaar.NetworkConnectionRequest;
import de.htwsaar.NetworkConnectionResponse;
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
import java.util.Optional;


@Endpoint
public class ConnectToPeerEndpoint {

    @Autowired
    ForwardingDAO forwardingDAO;

    @Autowired
    ServerConfig serverConfig;

    private static final String NAMESPACE_URI = "http://htwsaar.de/";

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "networkConnectionRequest")
    @ResponsePayload
    public NetworkConnectionResponse connectWithNetwork(@RequestPayload NetworkConnectionRequest request) throws IOException {

        System.out.println("NetzwerkVerbindungsEndpunkt erreicht verbinde mit: " + request.getConnectionConfig().getIp() );
        NetworkConnectionResponse response = new NetworkConnectionResponse();
        this.setUpConnection(request.getConnectionConfig());
        return response;
    }




    private void setUpConnection(ConnectionConfig connectionConfig){
       Optional<ForwardingConfig> forwardingConfig =  forwardingDAO.findByUrl(serverConfig.getServerIp());

       if(forwardingConfig.isPresent()){
           System.out.println("eigene connections um eins erhöhen");
         forwardingConfig.get().setConnections(forwardingConfig.get().getConnections()+1);
         forwardingDAO.save(forwardingConfig.get());
       }


       System.out.println("source ip: "+ connectionConfig.getIp());
       ForwardingConfig sourceForwardingConfig = new ForwardingConfig();

        System.out.println("source in datenbank aufnehmen und connections ums eins erhöhen");
        sourceForwardingConfig.setConnections(connectionConfig.getConnections()+1);
        sourceForwardingConfig.setUrl(connectionConfig.getIp());
        forwardingDAO.save(sourceForwardingConfig);


    }


}
