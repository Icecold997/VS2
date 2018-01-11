package de.htwsaar.server.config;

import de.htwsaar.*;
import de.htwsaar.server.persistence.*;
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


    //durchsuche dir und füge alle directories und files in datenbank
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
            Optional<ForwardingConfig> forwardingConfig = forwardingDAO.findByUrl(serverIp); // sich selbst in der datenbank finden
            ConnectionConfig connectionConfig = new ConnectionConfig();
            connectionConfig.setIp(forwardingConfig.get().getUrl());
            connectionConfig.setConnections(forwardingConfig.get().getConnections());
            for(ServerInfo serverInfo : superNodes){
                System.out.println("supernode url: "+serverInfo.getServerIp());
                List<ConnectionConfig> connectionConfigList =transmitter.sendConnectionRequest(serverInfo.getServerIp());
                if(!connectionConfigList.isEmpty()){
                    //TODO zwei peers mit wenigstens verbindungen auswählen
                    System.out.println("verbinde mit peer : "+ connectionConfigList.get(0).getIp());
                    transmitter.connectWithPeer(connectionConfigList.get(0).getIp(),connectionConfig);
                    break;
                }
            }
        }

    }

    public String getServerIp(){
        return this.serverIp;
    }

}
