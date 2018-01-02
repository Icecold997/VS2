package de.htwsaar.server.config;

import de.htwsaar.*;
import de.htwsaar.server.persistence.FileArrangementConfig;
import de.htwsaar.server.persistence.FileArrangementDAO;
import de.htwsaar.server.persistence.ForwardingConfig;
import de.htwsaar.server.persistence.ForwardingDAO;
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
    ServerInformationTransmitter transmitter;

    @Value("${server.rootDir}")
    private String rootDirectory;

    @Value("${server.address}")
    private String serverIp;

    public  String fileDirectory;
    public  void startServer(){
        createFileDirectory();

    }



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

    private List<Directory> getAllDirectorys(){
        Optional<List<FileArrangementConfig>> directorysInDatabase = fileArrangementDAO.findAllByisDirectoryAndIsLocal(true,true);
        List<Directory> directorys = new ArrayList<Directory>();
        Directory directory;
        if(directorysInDatabase.isPresent()){
            for (FileArrangementConfig dir:directorysInDatabase.get()){
                directory = new Directory();
                directory.setDirectoryName(dir.getFilename());
                directory.setSourceIp(dir.getSourceIp());
                directorys.add(directory);
            }
        }
      return directorys;
    }


}
