//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.7 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2018.01.28 um 11:11:04 PM CET 
//


package de.htwsaar;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für anonymous complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="connectionConfig" type="{http://htwsaar.de/}connectionConfig"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "connectionConfig"
})
@XmlRootElement(name = "networkInformationRequest")
public class NetworkInformationRequest {

    @XmlElement(required = true)
    protected ConnectionConfig connectionConfig;

    /**
     * Ruft den Wert der connectionConfig-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link ConnectionConfig }
     *     
     */
    public ConnectionConfig getConnectionConfig() {
        return connectionConfig;
    }

    /**
     * Legt den Wert der connectionConfig-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link ConnectionConfig }
     *     
     */
    public void setConnectionConfig(ConnectionConfig value) {
        this.connectionConfig = value;
    }

}
