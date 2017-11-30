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

    private String url;  //ip addresse der kinder/des parent
    private boolean isParent;



    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isParent() {
        return isParent;
    }

    public void setParent(boolean parent) {
        isParent = parent;
    }






}
