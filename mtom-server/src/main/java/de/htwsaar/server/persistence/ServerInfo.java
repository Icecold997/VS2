package de.htwsaar.server.persistence;

import de.htwsaar.Document;
import org.hibernate.validator.constraints.NotEmpty;
import javax.persistence.*;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import java.util.GregorianCalendar;


/**
 * @author timo
 */

@Entity
@Table(name ="Server")
public class ServerInfo extends AbstractEntity {


    @NotEmpty
    private String serverIp;

    private int serverRang;

    private int serverGroup;


    public ServerInfo() {

    }

    /**
     * Erhalte Server IP
     *
     * @return IP
     */
    public String getServerIp() {
        return serverIp;
    }

    /**
     * Setze Server IP
     *
     * @param serverIp IP
     */
    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public int getServerRang() { return serverRang; }

    public void setServerRang(int serverRang) { this.serverRang = serverRang; }

    public int getServerGroup() { return serverGroup; }

    public void setServerGroup(int serverGroup) { this.serverGroup = serverGroup; }


}