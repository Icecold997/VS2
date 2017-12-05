package de.htwsaar.server.config;

import de.htwsaar.server.persistence.FileArrangementConfig;
import de.htwsaar.server.persistence.FileArrangementDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.File;

@Service
@Configurable
public class ServerConfig {

    @Autowired
    FileArrangementDAO fileArrangementDAO;

    public  String fileDirectory;
    public  void startServer(){
        createFileDirectory();
    }

    private  void createFileDirectory() {
        String path = System.getProperty("user.dir") + "/";
        String dirName = "fileSystem";

        File dir = new File(path + dirName);

        if (dir.mkdir() || dir.exists()) {
            fileDirectory = path + dirName;
            System.out.println("Directory erstellt");

            checkDirecotory(dir);
        } else {
            System.out.println("Directory konnte nicht erstellt werden");
        }
    }

    //TODO initialisierung der datenbankeinträge
    //durchsuche dir und füge alle directories und files in datenbank
    private void checkDirecotory(File dir) {
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
}
