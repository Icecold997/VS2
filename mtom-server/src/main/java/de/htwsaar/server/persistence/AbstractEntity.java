package de.htwsaar.server.persistence;

/*
  Created by Timo on 22.11.2017.
 */
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.xml.bind.annotation.XmlElement;
import java.util.Date;

/**
 * @author timo
 */
@MappedSuperclass
public abstract class AbstractEntity {

    @Id @GeneratedValue
    private int id;

    @CreationTimestamp
    private Date created_at;

    @UpdateTimestamp
    private Date updated_at;

    /**
     * Erhalte ID
     *
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * Setze ID
     *
     * @param id Document ID
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Erhalte Erstellungsdatum
     *
     * @return Erstellungsdatum
     */
    public Date getCreated_at() {
        return created_at;
    }

    /**
     * Setze Erstellungsdatum
     *
     * @param created_at Erstellungsdatum
     */
    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    /**
     * Erhalte Aktualisierungsdatum
     *
     * @return Aktualisierungsdatum
     */
    public Date getUpdated_at() {
        return updated_at;
    }

    /**
     * Setze Aktualisierungsdatum
     *
     * @param updated_at Aktualisierungsdatum
     */
    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }
}
