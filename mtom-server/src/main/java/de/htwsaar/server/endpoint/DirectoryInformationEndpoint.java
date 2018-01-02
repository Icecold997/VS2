package de.htwsaar.server.endpoint;




import java.io.IOException;
import java.util.List;
import java.util.Optional;


import de.htwsaar.*;

import de.htwsaar.server.config.ServerConfig;
import de.htwsaar.server.persistence.FileArrangementConfig;
import de.htwsaar.server.persistence.FileArrangementDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;



@Endpoint
public class DirectoryInformationEndpoint {

    @Autowired
    FileArrangementDAO fileArrangementDAO;
    @Autowired
    ServerConfig serverConfig;

    private static final String NAMESPACE_URI = "http://htwsaar.de/";

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "sendDirectoryInformationToParentRequest")
    @ResponsePayload
    public SendDirectoryInformationToParentResponse getInfo(@RequestPayload SendDirectoryInformationToParentRequest request) throws IOException {

        //TODO informationen vom kind in datenbank aufnehmen
        Directory directory = request.getDirectory();

            Optional<FileArrangementConfig> fileArrangementConfig = fileArrangementDAO.findByfilenameAndIsDirectory(directory.getDirectoryName(),true);
            if(fileArrangementConfig.isPresent()){
                //verzeichnis schon vorhanden
            }else{
                FileArrangementConfig fileArrangementConfig1 = new FileArrangementConfig();
                fileArrangementConfig1.setSourceIp(directory.getSourceIp());
                fileArrangementConfig1.setLocal(false);
                fileArrangementConfig1.setDirectory(true);
                fileArrangementConfig1.setFilename(directory.getDirectoryName());
                fileArrangementConfig1.setFileLocation(serverConfig.fileDirectory);
                fileArrangementDAO.save(fileArrangementConfig1);
            }

        SendDirectoryInformationToParentResponse response = new SendDirectoryInformationToParentResponse();
        return  response;
    }

}
