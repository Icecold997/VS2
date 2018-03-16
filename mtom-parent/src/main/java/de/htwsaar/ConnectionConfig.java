//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.7 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2018.03.16 um 08:54:02 AM CET 
//


package de.htwsaar;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für connectionConfig complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="connectionConfig">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ip" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="connections" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="rang" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="group" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "connectionConfig", propOrder = {
    "ip",
    "connections",
    "rang",
    "group"
})
public class ConnectionConfig {

    @XmlElement(required = true)
    protected String ip;
    protected int connections;
    protected int rang;
    protected int group;

    /**
     * Ruft den Wert der ip-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIp() {
        return ip;
    }

    /**
     * Legt den Wert der ip-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIp(String value) {
        this.ip = value;
    }

    /**
     * Ruft den Wert der connections-Eigenschaft ab.
     * 
     */
    public int getConnections() {
        return connections;
    }

    /**
     * Legt den Wert der connections-Eigenschaft fest.
     * 
     */
    public void setConnections(int value) {
        this.connections = value;
    }

    /**
     * Ruft den Wert der rang-Eigenschaft ab.
     * 
     */
    public int getRang() {
        return rang;
    }

    /**
     * Legt den Wert der rang-Eigenschaft fest.
     * 
     */
    public void setRang(int value) {
        this.rang = value;
    }

    /**
     * Ruft den Wert der group-Eigenschaft ab.
     * 
     */
    public int getGroup() {
        return group;
    }

    /**
     * Legt den Wert der group-Eigenschaft fest.
     * 
     */
    public void setGroup(int value) {
        this.group = value;
    }

}
