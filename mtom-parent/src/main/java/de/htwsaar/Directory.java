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
 * <p>Java-Klasse für directory complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="directory">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="directoryName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="directoryRang" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="directoryDepartment" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="sourceIp" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "directory", propOrder = {
    "directoryName",
    "directoryRang",
    "directoryDepartment",
    "sourceIp"
})
public class Directory {

    @XmlElement(required = true)
    protected String directoryName;
    protected int directoryRang;
    protected int directoryDepartment;
    @XmlElement(required = true)
    protected String sourceIp;

    /**
     * Ruft den Wert der directoryName-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDirectoryName() {
        return directoryName;
    }

    /**
     * Legt den Wert der directoryName-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDirectoryName(String value) {
        this.directoryName = value;
    }

    /**
     * Ruft den Wert der directoryRang-Eigenschaft ab.
     * 
     */
    public int getDirectoryRang() {
        return directoryRang;
    }

    /**
     * Legt den Wert der directoryRang-Eigenschaft fest.
     * 
     */
    public void setDirectoryRang(int value) {
        this.directoryRang = value;
    }

    /**
     * Ruft den Wert der directoryDepartment-Eigenschaft ab.
     * 
     */
    public int getDirectoryDepartment() {
        return directoryDepartment;
    }

    /**
     * Legt den Wert der directoryDepartment-Eigenschaft fest.
     * 
     */
    public void setDirectoryDepartment(int value) {
        this.directoryDepartment = value;
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

}
