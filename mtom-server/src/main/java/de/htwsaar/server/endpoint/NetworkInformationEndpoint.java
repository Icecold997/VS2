package de.htwsaar.server.endpoint;


import de.htwsaar.*;
import de.htwsaar.server.config.ServerConfig;
import de.htwsaar.server.persistence.FileArrangementConfig;
import de.htwsaar.server.persistence.FileArrangementDAO;
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
public class NetworkInformationEndpoint {

    @Autowired
    ForwardingDAO forwardingDAO;

    @Autowired
    ServerConfig serverConfig;

    private static final String NAMESPACE_URI = "http://htwsaar.de/";

    /**
     * Endpoint für die Netzwerkinformationen
     *
     * @param request request
     * @return response
     * @throws IOException Exception für Schreiboperationen
     */
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "networkInformationRequest")
    @ResponsePayload
    public NetworkInformationResponse getNetworkInformation(@RequestPayload NetworkInformationRequest request) throws IOException {

        System.out.println("NetzwerkInformation Endpunkt erreicht.Leite Netzwerk Informationen weiter: ");
        NetworkInformationResponse response = new NetworkInformationResponse();
        Iterable<ForwardingConfig> forwardingConfigList = forwardingDAO.findAll();
        response.getConnectionConfig().addAll(forwardingConfigListToConnectionConfig(forwardingConfigList));
        return response;
    }


    private List<ConnectionConfig> forwardingConfigListToConnectionConfig(Iterable<ForwardingConfig> forwardingConfigList){
        List<ConnectionConfig> connectionConfigList = new ArrayList<ConnectionConfig>();

        for(ForwardingConfig forwardingConfig : forwardingConfigList){
            ConnectionConfig connectionConfig = new ConnectionConfig();
            connectionConfig.setConnections(forwardingConfig.getConnections());
            connectionConfig.setIp(forwardingConfig.getUrl());
            connectionConfigList.add(connectionConfig);
        }
        return connectionConfigList;
    }
}
