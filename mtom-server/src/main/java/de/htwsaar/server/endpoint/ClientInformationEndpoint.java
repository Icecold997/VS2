package de.htwsaar.server.endpoint;


import de.htwsaar.*;
import de.htwsaar.server.config.ServerConfig;
import de.htwsaar.server.persistence.FileArrangementConfig;
import de.htwsaar.server.persistence.FileArrangementDAO;
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
public class ClientInformationEndpoint {

    @Autowired
    FileArrangementDAO fileArrangementDAO;
    @Autowired
    ServerConfig serverConfig;

    private static final String NAMESPACE_URI = "http://htwsaar.de/";

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "directoryInformationRequest")
    @ResponsePayload
    public DirectoryInformationResponse getInformation(@RequestPayload DirectoryInformationRequest request) throws IOException {

        DirectoryInformationResponse respone = new DirectoryInformationResponse();

        List<FileArrangementConfig> fileList = new ArrayList<FileArrangementConfig>();
        fileList = fileArrangementDAO.findAll();
        for(FileArrangementConfig fileConfig:fileList){
            FileView fileView = new FileView();
            fileView.setType("");//TODO type in datenbank einfügen
            fileView.setDate(fileConfig.getUpdated_at().toString());
            fileView.setFileOrDirectoryName(fileConfig.getFilename());
            respone.getFileConfig().add(fileView);
        }
        return respone;
    }
}
