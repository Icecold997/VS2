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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Endpoint
public class DownloadDocumentEndpoint {

    @Autowired
    FileArrangementDAO fileArrangementDAO;
    @Autowired
    ServerConfig serverConfig;

    private static final String NAMESPACE_URI = "http://htwsaar.de/";

    /**
     * Endpoint für den Download von Dateien.
     * @param request request
     * @return response
     * @throws IOException Exception für Dateioperationen
     */
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "downloadDocumentRequest")
    @ResponsePayload
    public DownloadDocumentResponse downloadDocument(@RequestPayload DownloadDocumentRequest request) throws IOException {
        DownloadDocumentResponse respone = new DownloadDocumentResponse();
        System.out.println("Download Enpoint :" +request.getFileName());
        System.out.println("Download Enpoint :" +request.getPath());
        Optional<FileArrangementConfig> config = fileArrangementDAO.findByFileLocationAndFilename(request.getPath(),request.getFileName());
       if( config.isPresent()){
           System.out.println("datei gefunden");
           Document document = new Document();
           Path inputPath  = new File(config.get().getFileLocation()+"/"+config.get().getFilename()).toPath();
           byte[] array = Files.readAllBytes(inputPath);
           document.setContent(array);
           document.setPath(inputPath.toString());
           document.setName(inputPath.getFileName().toString());
           document.setSourceUri(config.get().getSourceIp());
           respone.setDocument(document);
       }

        return respone;
    }


}
