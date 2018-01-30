package de.htwsaar.server.endpoint;

import de.htwsaar.*;
import de.htwsaar.server.config.FloodingTransmitter;
import de.htwsaar.server.config.ServerConfig;
import de.htwsaar.server.gui.MainController;
import de.htwsaar.server.persistence.FileArrangementConfig;
import de.htwsaar.server.persistence.FileArrangementDAO;
import de.htwsaar.server.persistence.ForwardingDAO;
import javafx.application.Platform;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.io.File;
import java.io.IOException;
import java.sql.Time;
import java.util.Date;

@Endpoint
public class CreateDirEndpoint {
    @Autowired
    FileArrangementDAO fileArrangementDAO;
    @Autowired
    ServerConfig serverConfig;
    @Autowired
    ForwardingDAO forwardingDAO;

    @Autowired
    FloodingTransmitter floodingTransmitter;

    @Autowired
    MainController mainController;

    private static final String NAMESPACE_URI = "http://htwsaar.de/";

    /**
     * Endpoint zum Erstellen von directorys
     *
     * @param request request
     */
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "createDirectoryRequest")
    @ResponsePayload
    public CreateDirectoryResponse getInfo(@RequestPayload CreateDirectoryRequest request) throws IOException {

        String workPath  ;
        String newPath1 = request.getPath().substring(request.getPath().indexOf(request.getRequestRootDirName())+request.getRequestRootDirName().length(),request.getPath().length());

        if(newPath1.isEmpty()){  //root directory
            workPath = serverConfig.fileDirectory;
        }else{  //sub dir
            workPath   = serverConfig.fileDirectory;
            workPath   = workPath + newPath1;
        }
        System.out.println("Create Dir Enpoint : " + workPath);
        System.out.println(request.getDirectoryName());
        File dir = new File(workPath+"/"+request.getDirectoryName());
        if(dir.mkdir()) {
            FileArrangementConfig fileArrangementConfig = new FileArrangementConfig();
            fileArrangementConfig.setFilename(request.getDirectoryName());
            fileArrangementConfig.setFileLocation(workPath);
            fileArrangementConfig.setLocal(true);
            fileArrangementConfig.setDirectory(true);
            fileArrangementConfig.setSourceIp(request.getSourceIp());
            fileArrangementDAO.save(fileArrangementConfig);
            String finalWorkPath = workPath;
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println("füge dir gui hinzu : " + dir.getName());
                        FileView fileView = new FileView();
                        fileView.setFileOrDirectoryName(dir.getName());
                        fileView.setType("Directory");
                        fileView.setRequestRootDirName(request.getRequestRootDirName());
                        fileView.setPath(finalWorkPath);
                        fileView.setSourceIp(request.getSourceIp());
                        fileView.setDate(new Date().toString());
                        mainController.addItem(fileView);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            floodingTransmitter.floodCreateDirRequest(request);
        }
        CreateDirectoryResponse response = new CreateDirectoryResponse();
        return  response;
    }
}
