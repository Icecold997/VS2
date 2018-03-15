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
    private int department; //gruppenId
    private int rang;  // rang in der hierarchie

    /**
     * Erhalte URL
     *
     * @return URL
     */
    public String getUrl() {
        return url;
    }

    /**
     * Setze URL
     *
     * @param url URL
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Ist Parent?
     *
     * @return true/false
     */
    public boolean isParent() {
        return isParent;
    }

    /**
     * Setze Parent
     *
     * @param parent parent
     */
    public void setParent(boolean parent) {
        isParent = parent;
    }

    public int getDepartment() { return department; }

    public void setDepartment(int department) {
        this.department = department;
    }

    public int getRang() {
        return rang;
    }

    public void setRang(int rang) {
        this.rang = rang;
    }




}
