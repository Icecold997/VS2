package de.htwsaar.server.endpoint;

import de.htwsaar.LogoutClientRequest;
import de.htwsaar.LogoutClientResponse;
import de.htwsaar.server.config.ServerConfig;
import de.htwsaar.server.config.ServerInformationTransmitter;
import de.htwsaar.server.persistence.ForwardingConfig;
import de.htwsaar.server.persistence.ForwardingDAO;
import de.htwsaar.server.persistence.ServerDAO;
import de.htwsaar.server.persistence.ServerInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.io.IOException;

@Endpoint
public class ClientLogoutEndpoint {

    @Autowired
    ForwardingDAO forwardingDAO;
    @Autowired
    ServerConfig serverConfig;
    @Autowired
    ServerDAO serverDAO;
    @Autowired
    ServerInformationTransmitter transmitter;

    private static final String NAMESPACE_URI = "http://htwsaar.de/";


    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "logoutClientRequest")
    @ResponsePayload
    public LogoutClientResponse getResponse(@RequestPayload LogoutClientRequest request) throws IOException {

        LogoutClientResponse response = new LogoutClientResponse();

          System.out.println("test");


        return response;
    }
}
