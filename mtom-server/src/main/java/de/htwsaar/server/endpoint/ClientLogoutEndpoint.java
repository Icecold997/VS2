package de.htwsaar.server.endpoint;

import de.htwsaar.LogoutClientRequest;
import de.htwsaar.LogoutClientResponse;
import de.htwsaar.server.config.ServerConfig;
import de.htwsaar.server.config.ServerInformationTransmitter;
import de.htwsaar.server.persistence.ForwardingConfig;
import de.htwsaar.server.persistence.ForwardingDAO;
import de.htwsaar.server.persistence.ServerDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.io.IOException;
import java.util.Optional;

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
        Optional<ForwardingConfig> forwardingConfig = forwardingDAO.findByUrl(request.getSourceIp());
        if(forwardingConfig.isPresent()){
            System.out.println("Diese IP wurde gefunden.");
        }
        return response;
    }
}
