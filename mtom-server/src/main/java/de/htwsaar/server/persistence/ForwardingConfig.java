package de.htwsaar.server.persistence;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;

/**
 * @author Timo
 */

@Entity
@Table(name = "Forwarding")
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
public class ForwardingConfig extends AbstractEntity{

    private String url;  //ip addresse des verbundenenen peers
    private int connections; // anzahl der verbindungen


    public int getConnections() { return connections; }

    public void setConnections(int connections) { this.connections = connections; }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }




}
