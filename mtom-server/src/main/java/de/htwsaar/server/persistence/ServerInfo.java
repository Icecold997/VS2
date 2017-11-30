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


    public ServerInfo() {

    }


    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }


}