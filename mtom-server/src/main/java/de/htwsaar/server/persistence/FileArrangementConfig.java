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

    /**
     * Dateinamen erhalten
     *
     * @return Dateiname
     */
    public String getFilename() {
        return filename;
    }

    /**
     * Dateinamen setzen
     *
     * @param filename Dateiname
     */
    public void setFilename(String filename) {
        this.filename = filename;
    }

    /**
     * Dateiort erhalten
     *
     * @return c:/.../myfile
     */
    public String getFileLocation() {
        return fileLocation;
    }

    /**
     * Dateiort setzen
     *
     * @param fileLocation c:/.../myfile
     */
    public void setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
    }

    /**
     * Auf Directory prüfen
     *
     * @return true/false
     */
    public boolean isDirectory() {
        return isDirectory;
    }

    /**
     * Als  Directory setzen
     *
     * @param directory true/false
     */
    public void setDirectory(boolean directory) {
        isDirectory = directory;
    }

    /**
     * Auf die lokale Ablage prüfen
     *
     * @return true / false
     */
    public boolean isLocal() {
        return isLocal;
    }

    /**
     * Auf die lokale Ablage setzen
     *
     * @param local true / false
     */
    public void setLocal(boolean local) {
        isLocal = local;
    }

    /**
     * Herkunfts-IP auslesen
     *
     * @return ip
     */
    public String getSourceIp() {
        return sourceIp;
    }

    /**
     * Herkunfts-IP setzen
     *
     * @param sourceIp ip
     */
    public void setSourceIp(String sourceIp) {
        this.sourceIp = sourceIp;
    }



}
