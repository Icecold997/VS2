package de.htwsaar.server.config;

import java.io.File;

public class ServerConfig {

    public static String fileDirectory;
    public static void startServer(){
        createFileDirectory();
    }

    private static void createFileDirectory() {
        String path = System.getProperty("user.dir") + "/";
        String dirName = "fileSystem";

        File dir = new File(path + dirName);

        if (dir.mkdir() || dir.exists()) {
            fileDirectory = path + dirName;
            System.out.println("Directory erstellt");
        } else {
            System.out.println("Directory konnte nicht erstellt werden");
        }
    }

}
