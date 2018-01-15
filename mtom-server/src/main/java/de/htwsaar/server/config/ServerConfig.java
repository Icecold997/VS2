package de.htwsaar.server.config;

import de.htwsaar.*;
import de.htwsaar.server.persistence.*;
import de.htwsaar.server.ws.ConnectionConfigComperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Configurable
public class ServerConfig {

    @Autowired
    FileArrangementDAO fileArrangementDAO;

    @Autowired
    ForwardingDAO forwardingDAO;

    @Autowired
    private ServerDAO serverDAO;

    @Autowired
    ServerInformationTransmitter transmitter;

    @Value("${server.status}")
    private String serverStatus;

    @Value("${server.rootDir}")
    private String rootDirectory;

    @Value("${server.address}")
    private String serverIp;

    public  String fileDirectory;

    /**
     * Startet den Server
     */
    public  void startServer(){
        try {
            createFileDirectory();
            if (!forwardingDAO.findByUrl(this.serverIp).isPresent()) {
                ForwardingConfig forwardingConfig = new ForwardingConfig();
                forwardingConfig.setConnections(0);
                forwardingConfig.setUrl(this.serverIp);
                forwardingDAO.save(forwardingConfig);
            }
            connnectWithNetwork();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * Erstelle Directory
     */
    private  void createFileDirectory(){
        String path = System.getProperty("user.dir") + "/";


        File dir = new File(path + rootDirectory);

        if (dir.exists()) {
            fileDirectory = path + rootDirectory;
            System.out.println("Directory vorhanden");
            checkDirecotory(dir);
        } else if(dir.mkdir()) {
            fileDirectory = path + rootDirectory;
            System.out.println("Directory erstellt");
        }
        else{
            System.out.println("Directory konnte nicht erstellt werden");
        }
    }

    /**
     * Durchsuchen des Directories und Ablegen der Files in der Datenbank
     *
     * @param dir Directory
     */
    private void checkDirecotory(File dir)  {
        File[] files = dir.listFiles();

        if(files != null){
            for(int i= 0 ; i< files.length;i++){
                if(fileArrangementDAO.findByfilename(files[i].getName()).isPresent()){
                    //file gefunden
                }else if(files[i].isDirectory()){

                  FileArrangementConfig fileArrangementConfig = new FileArrangementConfig();
                  fileArrangementConfig.setFilename(files[i].getName());
                  fileArrangementConfig.setFileLocation(fileDirectory);
                  fileArrangementConfig.setDirectory(true);
                    try{
                        fileArrangementConfig.setSourceIp(LocalIpAddress.getExternalIpAddress());
                    }catch(IOException e){
                        fileArrangementConfig.setSourceIp("localhost");
                    }

                  fileArrangementConfig.setLocal(true);
                  fileArrangementDAO.save(fileArrangementConfig);
                }else {
                    FileArrangementConfig fileArrangementConfig = new FileArrangementConfig();
                    if(!files[i].getName().startsWith(".")) {
                        fileArrangementConfig.setFilename(files[i].getName());
                        fileArrangementConfig.setFileLocation(fileDirectory);
                        fileArrangementConfig.setDirectory(false);
                        fileArrangementConfig.setLocal(true);
                        fileArrangementDAO.save(fileArrangementConfig);
                    }
                }

            }
        }
    }

    /**
     * Stellt verbindung mit dem peer to peer netzwerk her
     */
    public void connnectWithNetwork(){
        if(!serverStatus.equals("supernode")){
            System.out.println("kein supernode verbinde mit netzwerk");
            Iterable<ServerInfo> superNodes;
            superNodes = serverDAO.findAll();     //finde supernodes in db
            Optional<ForwardingConfig> forwardingConfigFromMe = forwardingDAO.findByUrl(serverIp); // sich selbst in der datenbank finden
            ConnectionConfig connectionConfigFromMe = new ConnectionConfig();
            connectionConfigFromMe.setIp(forwardingConfigFromMe.get().getUrl());
            connectionConfigFromMe.setConnections(forwardingConfigFromMe.get().getConnections());
            for(ServerInfo serverInfo : superNodes){
                System.out.println("supernode url: "+serverInfo.getServerIp());
                List<ConnectionConfig> connectionConfigList =transmitter.sendConnectionRequest(serverInfo.getServerIp());
                if(!connectionConfigList.isEmpty()){
                    Collections.sort(connectionConfigList,new ConnectionConfigComperator());
                   for(ConnectionConfig connection : connectionConfigList){
                       if((!connection.getIp().equals(serverIp)) && (forwardingConfigFromMe.get().getConnections() < 2) ){ //wen nicht eigene ip und eigene connections < 2
                           System.out.println("Verbinde mit peer: "+connection.getIp());
                           connectWithPeer(connection.getIp(),connectionConfigFromMe);
                           System.out.println("Erhöhe eigene Connections um 1 ");
                           forwardingConfigFromMe.get().setConnections(forwardingConfigFromMe.get().getConnections()+1);

                           if(connectionConfigFromMe.getConnections() == 2){
                               break;
                           }
                       }
                   }
                    System.out.println("Speicher eigene neue Verbindungsdetails in Datenbank");
                    forwardingDAO.save(forwardingConfigFromMe.get());
                    break;
                }
            }
        }
    }

    /**
     * Verbinde zum Peer
     *
     * @param peerIp IP des Peer
     * @param connectionConfig  Verbindungskonfiguration
     */
    private void connectWithPeer(String peerIp ,ConnectionConfig connectionConfig){
        ConnectionConfig connectionConfigFromTarget = transmitter.connectWithPeer(peerIp,connectionConfig);
        Optional<ForwardingConfig> forwardingConfig = forwardingDAO.findByUrl(connectionConfigFromTarget.getIp());
        if(forwardingConfig.isPresent()){
            forwardingConfig.get().setConnections(connectionConfigFromTarget.getConnections());
            forwardingDAO.save(forwardingConfig.get());
        }else{
            ForwardingConfig forwardingConfigFromTarget = new ForwardingConfig();
            forwardingConfigFromTarget.setConnections(connectionConfigFromTarget.getConnections());
            forwardingConfigFromTarget.setUrl(connectionConfigFromTarget.getIp());
            forwardingDAO.save(forwardingConfigFromTarget);
        }
    }

    /**
     * Erhalte Server IP
     *
     * @return IP
     */
    public String getServerIp(){
        return this.serverIp;
    }

}
