package de.htwsaar.server.endpoint;


import de.htwsaar.Directory;
import de.htwsaar.SendDirectoryInformationToParentRequest;
import de.htwsaar.SendDirectoryInformationToParentResponse;
import de.htwsaar.server.config.ServerConfig;
import de.htwsaar.server.persistence.FileArrangementConfig;
import de.htwsaar.server.persistence.FileArrangementDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.io.IOException;
import java.util.List;
import java.util.Optional;


@Endpoint
public class ClientInformationEndpoint {

    @Autowired
    FileArrangementDAO fileArrangementDAO;
    @Autowired
    ServerConfig serverConfig;

    private static final String NAMESPACE_URI = "http://htwsaar.de/";

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "")
    @ResponsePayload
    public SendDirectoryInformationToParentResponse getInfo(@RequestPayload SendDirectoryInformationToParentRequest request) throws IOException {

        return new SendDirectoryInformationToParentResponse();
    }
}
