package de.htwsaar.server.config;

import de.htwsaar.*;
import de.htwsaar.server.persistence.FileArrangementConfig;
import de.htwsaar.server.persistence.FileArrangementDAO;
import de.htwsaar.server.persistence.ForwardingConfig;
import de.htwsaar.server.persistence.ForwardingDAO;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Configurable
public class ServerConfig implements EmbeddedServletContainerCustomizer {

    @Autowired
    FileArrangementDAO fileArrangementDAO;

    @Autowired
    ForwardingDAO forwardingDAO;

    @Autowired
    ServerInformationTransmitter transmitter;

    @Value("${server.rootDir}")
    private String rootDirectory;

    @Value("${server.address}")
    private String serverIp;

    public  String fileDirectory;

    /**
     * Feststellung und Ausgabe der Server-IP
     *
     * @param container Container
     */
    @Override
    public void customize(ConfigurableEmbeddedServletContainer container) {
        try {
            String fullIP = java.net.InetAddress.getLocalHost().toString();
            container.setAddress(java.net.InetAddress.getLocalHost());

            if (fullIP.length() > 12) {
                serverIp = fullIP.substring(fullIP.indexOf('/') +1 );
                System.out.println("##### Server is running on: " + serverIp + ":9090 #####");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Startet den Server
     */
    public  void startServer(){
        //String ip = request.getRemoteAddr();
        createFileDirectory();

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
            sendInformationToParent(dir);
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
       directory.setDirectoryName(dir.getName());
       if(forwardingConfigs.isPresent()){
           for (ForwardingConfig parent:forwardingConfigs.get()){
                transmitter.sendRequestToParent(parent.getUrl(),directory,serverIp);
           }
       }
   }




}
