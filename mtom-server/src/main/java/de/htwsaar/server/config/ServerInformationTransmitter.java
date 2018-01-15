package de.htwsaar.server.config;

import com.mysql.fabric.Server;
import de.htwsaar.*;
import de.htwsaar.server.persistence.ForwardingConfig;
import de.htwsaar.server.persistence.ForwardingDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

import java.util.List;

/**
 * Created by Timo on 08.12.2017.
 */
public class ServerInformationTransmitter extends WebServiceGatewaySupport {

 //TODO testen

    /**
     * Handler für die Requests an den Parent.
     *
     * @param targetUrl Parent URL
     * @param directory Directory
     * @param ownUrl    eigene URL
     * @return  true
     */
    public boolean sendRequestToParent(String targetUrl,Directory directory,String ownUrl){

        SendDirectoryInformationToParentRequest request = new SendDirectoryInformationToParentRequest();
        request.setDirectory(directory);
        request.setIp(ownUrl);
        String finalUrl = "http://"+targetUrl+":9090/ws/documents";
        SendDirectoryInformationToParentResponse response = (SendDirectoryInformationToParentResponse) getWebServiceTemplate().marshalSendAndReceive(finalUrl,request);
        return true;
    }

    /**
     * Suchanfrage an das Child
     *
     * @param targetUrl Child-URL
     * @param fileName  Dateiname
     * @return          Dateifund true/false
     */
    public boolean sendSearchRequestToChild(String targetUrl,String fileName){
        SearchDocumentRequest request = new SearchDocumentRequest();
        request.setDocumentName(fileName);
        String finalUrl = "http://"+targetUrl+":9090/ws/documents";
        SearchDocumentResponse response=(SearchDocumentResponse) getWebServiceTemplate().marshalSendAndReceive(finalUrl,request);
        return response.isFound();
    }

    /**
     * Connection-Request senden
     *
     * @param targetUrl Ziel URL
     * @return null
     */
    public List<ConnectionConfig> sendConnectionRequest(String targetUrl){
        try {
            NetworkInformationRequest request = new NetworkInformationRequest();
            String finalUrl = "http://" + targetUrl + ":9090/ws/documents";
            NetworkInformationResponse response = (NetworkInformationResponse) getWebServiceTemplate().marshalSendAndReceive(finalUrl, request);
            return response.getConnectionConfig();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Mit Peer verbinden
     *
     * @param targetUrl        Ziel URL
     * @param connectionConfig Verbindungskonfiguration
     * @return null
     */
    public ConnectionConfig connectWithPeer(String targetUrl ,ConnectionConfig connectionConfig){
        try{
            NetworkConnectionRequest request = new NetworkConnectionRequest();
            request.setConnectionConfig(connectionConfig);
            String finalUrl = "http://" + targetUrl + ":9090/ws/documents";
            NetworkConnectionResponse response =(NetworkConnectionResponse) getWebServiceTemplate().marshalSendAndReceive(finalUrl,request);
            return response.getConnectionConfig();
        }catch(Exception e){

        }
        return null;
    }


}
