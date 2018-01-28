package de.htwsaar.server.gui;

import de.htwsaar.server.config.ServerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@Service
public class WorkDirHandler {
    private String workDir ;

    @Value("${server.rootDir}")
    private String rootDirectory;


    @Autowired
    ServerConfig serverConfig;

    public WorkDirHandler(){
    }

    public void setWorkDir(String workDir){
        this.workDir = workDir;
    }

    public String getWorkDir(){
        return this.workDir;
    }
}
