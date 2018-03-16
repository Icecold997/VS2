package de.htwsaar.server.config;

import de.htwsaar.*;
import de.htwsaar.server.persistence.ForwardingConfig;
import de.htwsaar.server.persistence.ForwardingDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;


/**
 * Klasse die das Flooding im peer to peer netzwerk übernimmt
 */

public class FloodingTransmitter  extends WebServiceGatewaySupport{

 @Autowired
 ForwardingDAO forwardingDAO;

 @Autowired
 ServerConfig serverConfig;

 @Autowired
  FloodingCheck floodingCheck;
 public FloodingTransmitter(){}

    /**
     * Verteile & speichere erhaltenes Document
     *
     * @param storeDocumentRequest Request
     */
    public void floodReceivedFile(StoreDocumentRequest storeDocumentRequest){
        String sourceIp = storeDocumentRequest.getDocument().getSourceUri();
        Iterable<ForwardingConfig> forwardingConfigs;
        forwardingConfigs = forwardingDAO.findAll();
        for(ForwardingConfig forwardingConfig : forwardingConfigs){
            if(!forwardingConfig.getUrl().equals(serverConfig.getServerIp()) && !forwardingConfig.getUrl().equals(sourceIp) && !floodingCheck.isPresent(storeDocumentRequest.getGuid())){
                System.out.println("Floode empfangene datei an :" + forwardingConfig.getUrl());
                System.out.println("füge request in liste ein : " );

                try {
                    StoreDocumentResponse response = (StoreDocumentResponse) getWebServiceTemplate()
                            .marshalSendAndReceive("http://" + forwardingConfig.getUrl()+ ":9090/ws/documents", storeDocumentRequest);
                }catch (Exception e){
                }
            }
        }
        floodingCheck.addGui(storeDocumentRequest.getGuid());
    }

    /**
     * Verteile Löschung des File
     *
     * @param deleteDocumentRequest request
     */
    public void floodDeleteFileRequest(DeleteDocumentRequest deleteDocumentRequest) {
        String sourceIp = deleteDocumentRequest.getSourceIp();
        Iterable<ForwardingConfig> forwardingConfigs;
        forwardingConfigs = forwardingDAO.findAll();
        for (ForwardingConfig forwardingConfig : forwardingConfigs) {
            if(!forwardingConfig.getUrl().equals(serverConfig.getServerIp()) && !forwardingConfig.getUrl().equals(sourceIp) && !floodingCheck.isPresent(deleteDocumentRequest.getGuid())){
                System.out.println("Floode gelöschte datei an :" + forwardingConfig.getUrl());
                try {
                    DeleteDocumentResponse response = (DeleteDocumentResponse) getWebServiceTemplate()
                            .marshalSendAndReceive("http://" + forwardingConfig.getUrl() + ":9090/ws/documents", deleteDocumentRequest);
                } catch (Exception e) {

                }
            }
        }
        floodingCheck.addGui(deleteDocumentRequest.getGuid());
    }

    /**
     * Verteile und speichere Rename Request
     *
     * @param renameDocumentRequest request
     */
    public void floodRenameRequest(RenameDocumentRequest renameDocumentRequest){
        String sourceIp = renameDocumentRequest.getSourceIp();
        Iterable<ForwardingConfig> forwardingConfigs;
        forwardingConfigs = forwardingDAO.findAll();
        for(ForwardingConfig forwardingConfig : forwardingConfigs) {
            if(!forwardingConfig.getUrl().equals(serverConfig.getServerIp()) && !forwardingConfig.getUrl().equals(sourceIp) && !floodingCheck.isPresent(renameDocumentRequest.getGuid())){
                System.out.println("Floode rename an: " + forwardingConfig.getUrl());
                RenameDocumentResponse response = (RenameDocumentResponse) getWebServiceTemplate()
                        .marshalSendAndReceive("http://" + forwardingConfig.getUrl() + ":9090/ws/documents", renameDocumentRequest);
            }
        }
        floodingCheck.addGui(renameDocumentRequest.getGuid());
    }





}
