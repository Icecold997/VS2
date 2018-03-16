package de.htwsaar.server.config;

import de.htwsaar.*;
import de.htwsaar.server.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.*;

@Service
@Configurable
public class ServerConfig {

    @Autowired
    FileArrangementDAO fileArrangementDAO;

    @Autowired
    ForwardingDAO forwardingDAO;

    @Autowired
    ServerInformationTransmitter transmitter;

    @Autowired
    private ServerDAO serverDAO;

    @Value("${server.rootDir}")
    private String rootDirectory;

    @Value("${allowServerAddress}")
    private boolean allowServerAddress;

    @Value("${server.address}")
    public String serverIp;

    public  String fileDirectory;

     @Value("${serverRang}")
     private int serverRang;

    @Value("${serverGroup}")
    private int serverGroup;

    @Value("${server.status}")
    private String serverStatus;


    /**
     * Startet den Server
     */
    public  void startServer(){
        try {
            createFileDirectory();
            if (!forwardingDAO.findByUrl(this.serverIp).isPresent()) {
                ForwardingConfig forwardingConfig = new ForwardingConfig();
                forwardingConfig.setConnections(0);
                forwardingConfig.setRang(serverRang);
                forwardingConfig.setDepartment(serverGroup);
                forwardingConfig.setUrl(this.serverIp);
                forwardingDAO.save(forwardingConfig);
            }
            connectWithGroup();

        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println(serverIp);
    }

    /**
     * Erstellung eines Directories
     */
    private  void createFileDirectory(){

        String path = System.getProperty("user.dir") + "/";


        File dir = new File(path + rootDirectory);

        if (dir.exists()) {
            fileDirectory = path + rootDirectory;
            System.out.println("Directory vorhanden");
            checkDirecotory(dir);
            if(serverStatus.equals("supernode") && serverRang > 0) {
                sendInformationToParent(dir);
            }
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
                  fileArrangementConfig.setSourceIp(this.serverIp);
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
     * Übersenden der IP- und Directory-Informationen ans Parent
     *
     * @param dir Directory
     */
  //TODO beim server start vater informationen schicken (
   private void sendInformationToParent(File dir){
       Optional<List<ForwardingConfig>> forwardingConfigs;
       forwardingConfigs = forwardingDAO.findAllByisParent(true);
       Directory directory = new Directory();
       directory.setSourceIp(serverIp);
       directory.setDirectoryRang(serverRang);
       directory.setDirectoryDepartment(serverGroup);
       directory.setDirectoryName(dir.getName());
       if(forwardingConfigs.isPresent()){
           for (ForwardingConfig parent:forwardingConfigs.get()){
                transmitter.sendRequestToParent(parent.getUrl(),directory,serverIp);
           }
       }
   }

    /**
     * Stellt verbindung mit dem peer to peer netzwerk der gruppe her
     */
   private void connectWithGroup(){
       if(!serverStatus.equals("supernode")) {
           System.out.println("Keine supernode, Verbinde mit Gruppen Netzwerk");
           List<ConnectionConfig> savedConnectionsToPeers = new ArrayList<ConnectionConfig>();
           Optional<ServerInfo> superNodes = serverDAO.findByServerGroupAndServerRang(serverGroup,serverRang);
           Optional<ForwardingConfig> forwardingConfigFromMe = forwardingDAO.findByUrl(serverIp); // sich selbst in der datenbank finden
           ConnectionConfig connectionConfigFromMe = new ConnectionConfig();
           connectionConfigFromMe.setIp(forwardingConfigFromMe.get().getUrl());
           connectionConfigFromMe.setRang(serverRang);
           connectionConfigFromMe.setGroup(serverGroup);
           connectionConfigFromMe.setConnections(forwardingConfigFromMe.get().getConnections());

           if(superNodes.isPresent()) {
               System.out.println("SuperNodeUrl :" + superNodes.get().getServerIp());
               System.out.println("SuperNodeRang :" + superNodes.get().getServerRang());
               System.out.println("SuperNodeGruppe :" + superNodes.get().getServerGroup());
               System.out.println("Zustand des Netzwerkes holen");
               List<ConnectionConfig> connectionConfigList = transmitter.sendConnectionRequest(superNodes.get().getServerIp()); //zustand des netzwerkes von supernode holen
               if (!connectionConfigList.isEmpty()) {
                   Collections.sort(connectionConfigList, new ConnectionConfigComperator()); // netzwerkzustandsliste nach anzahl verbindungen sortieren
                   for (ConnectionConfig connection : connectionConfigList) {
                       if ((!connection.getIp().equals(serverIp)) && (forwardingConfigFromMe.get().getConnections() < 2)) { //wen nicht eigene ip und eigene connections < 2
                           System.out.println("Verbinde mit peer: " + connection.getIp());
                           savedConnectionsToPeers.add(connectWithPeer(connection.getIp(), connectionConfigFromMe));
                           System.out.println("Erhöhe eigene Connections um 1 ");
                           forwardingConfigFromMe.get().setConnections(forwardingConfigFromMe.get().getConnections() + 1);

                           if (connectionConfigFromMe.getConnections() == 2) {
                               break;
                           }
                       }
                   }
                   System.out.println("Speicher eigene neue Verbindungsdetails in Datenbank");
                   forwardingDAO.save(forwardingConfigFromMe.get());
                   System.out.println("Sende Supernodes meine neuen Verbindungen");
                   this.sendNetworkInformationToSupernode(savedConnectionsToPeers);
               }
           }
       }else{
           System.out.println("Server ist SuperNode der Gruppe :" + getServerGroup() +"  auf Rang: " + getServerRang());
       }

   }


    /**
     * Sendet die neuen netzwerkinformationen zu den Supernodes
     *
     *@param connectionConfigs Liste der neuen Verbindungen die im SuperNode geupdatet werden sollen
     */
    private void sendNetworkInformationToSupernode(List<ConnectionConfig> connectionConfigs){
        Iterable<ServerInfo> superNodes;
        superNodes = serverDAO.findAll();     //finde supernodes in db

        for(ServerInfo serverInfo : superNodes) {
            transmitter.sendNetworkInformationToSuperNode(serverInfo.getServerIp(),connectionConfigs);
        }
    }


    /**
     * Verbinde zum Peer
     *
     * @param peerIp IP des Peer
     * @param connectionConfig  Verbindungskonfiguration
     */
    private ConnectionConfig connectWithPeer(String peerIp ,ConnectionConfig connectionConfig){
        ConnectionConfig connectionConfigFromTarget = transmitter.connectWithPeer(peerIp,connectionConfig);
        Optional<ForwardingConfig> forwardingConfig = forwardingDAO.findByUrl(connectionConfigFromTarget.getIp());
        if(forwardingConfig.isPresent()){
            forwardingConfig.get().setConnections(connectionConfigFromTarget.getConnections());
            forwardingDAO.save(forwardingConfig.get());
        }else{
            ForwardingConfig forwardingConfigFromTarget = new ForwardingConfig();
            forwardingConfigFromTarget.setConnections(connectionConfigFromTarget.getConnections());
            forwardingConfigFromTarget.setUrl(connectionConfigFromTarget.getIp());
            forwardingConfigFromTarget.setRang(connectionConfigFromTarget.getRang());
            forwardingConfigFromTarget.setDepartment(connectionConfigFromTarget.getGroup());
            forwardingDAO.save(forwardingConfigFromTarget);
        }
        return connectionConfigFromTarget;
    }


   public String getServerIp(){return this.serverIp;}

   public String getRootDirectory() {return  this.rootDirectory;}

   public int getServerRang() { return serverRang; }

   public int getServerGroup() { return serverGroup; }



}
