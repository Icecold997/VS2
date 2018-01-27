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

    /**
     * Endpoint für die Verarbeitung der Directory-Informationen
     *
     * @param request Request
     * @return response
     * @throws IOException Exception für Dateioperationen
     */
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "directoryInformationRequest")
    @ResponsePayload
    public DirectoryInformationResponse getInformation(@RequestPayload DirectoryInformationRequest request) throws IOException {

        DirectoryInformationResponse respone = new DirectoryInformationResponse();
        Optional<List<FileArrangementConfig>> fileList = fileArrangementDAO.findAllByFileLocation(request.getPath());
        System.out.println("Directory information Endpoint erreicht");
        System.out.println("Directory pfad" + request.getPath());
        if (fileList.isPresent()){
            for (FileArrangementConfig fileConfig : fileList.get()) {
                FileView fileView = new FileView();
                if (fileConfig.isDirectory()) {
                    fileView.setType("Directory");
                } else {
                    fileView.setType("File");
                }
                if (fileConfig.isLocal()) {
                    fileView.setSourceIp("localhost"); //TODO lokale ip adresse eingeben
                } else {
                    fileView.setSourceIp(fileConfig.getSourceIp());
                }
                fileView.setPath(request.getPath());
                fileView.setDate(fileConfig.getUpdated_at().toString());
                fileView.setFileOrDirectoryName(fileConfig.getFilename());
                respone.getFileConfig().add(fileView);
            }
    }
        respone.setSuccess(true);
        return respone;
    }


}
