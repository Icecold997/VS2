//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.7 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2018.03.16 um 01:06:36 AM CET 
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
 *         &lt;element name="guid" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="path" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="currentDocumentName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="newDocumentName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="sourceIp" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="requestRootDirName" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
    "guid",
    "path",
    "currentDocumentName",
    "newDocumentName",
    "sourceIp",
    "requestRootDirName"
})
@XmlRootElement(name = "renameDocumentRequest")
public class RenameDocumentRequest {

    @XmlElement(required = true)
    protected String guid;
    @XmlElement(required = true)
    protected String path;
    @XmlElement(required = true)
    protected String currentDocumentName;
    @XmlElement(required = true)
    protected String newDocumentName;
    @XmlElement(required = true)
    protected String sourceIp;
    @XmlElement(required = true)
    protected String requestRootDirName;

    /**
     * Ruft den Wert der guid-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGuid() {
        return guid;
    }

    /**
     * Legt den Wert der guid-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGuid(String value) {
        this.guid = value;
    }

    /**
     * Ruft den Wert der path-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPath() {
        return path;
    }

    /**
     * Legt den Wert der path-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPath(String value) {
        this.path = value;
    }

    /**
     * Ruft den Wert der currentDocumentName-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCurrentDocumentName() {
        return currentDocumentName;
    }

    /**
     * Legt den Wert der currentDocumentName-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCurrentDocumentName(String value) {
        this.currentDocumentName = value;
    }

    /**
     * Ruft den Wert der newDocumentName-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNewDocumentName() {
        return newDocumentName;
    }

    /**
     * Legt den Wert der newDocumentName-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNewDocumentName(String value) {
        this.newDocumentName = value;
    }

    /**
     * Ruft den Wert der sourceIp-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSourceIp() {
        return sourceIp;
    }

    /**
     * Legt den Wert der sourceIp-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSourceIp(String value) {
        this.sourceIp = value;
    }

    /**
     * Ruft den Wert der requestRootDirName-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRequestRootDirName() {
        return requestRootDirName;
    }

    /**
     * Legt den Wert der requestRootDirName-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRequestRootDirName(String value) {
        this.requestRootDirName = value;
    }

}
