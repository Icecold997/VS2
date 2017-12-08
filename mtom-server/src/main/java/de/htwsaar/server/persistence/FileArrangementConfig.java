package de.htwsaar.server.persistence;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

/**
 * Created by Timo on 30.11.2017.
 */

@Entity
@Table(name = "FileArrangement")
@Inheritance(strategy= InheritanceType.TABLE_PER_CLASS)
public class FileArrangementConfig extends AbstractEntity{

    private String filename;
    private String fileLocation;
    private boolean isDirectory;
    private boolean isLocal;
    private String sourceIp;

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFileLocation() {
        return fileLocation;
    }

    public void setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    public void setDirectory(boolean directory) {
        isDirectory = directory;
    }

    public boolean isLocal() {
        return isLocal;
    }

    public void setLocal(boolean local) {
        isLocal = local;
    }

    public String getSourceIp() {
        return sourceIp;
    }

    public void setSourceIp(String sourceIp) {
        this.sourceIp = sourceIp;
    }


}
