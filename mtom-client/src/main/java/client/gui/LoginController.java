package client.gui;

import client.ws.DocumentsClient;
import client.ws.UrlList;
import com.jfoenix.controls.JFXTextField;
import de.htwsaar.DirectoryInformationResponse;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author wirth
 */
public class LoginController implements Initializable {


    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);



   @Autowired
    DocumentsClient documentsClient;

    @Autowired
    private Router router;

    @Autowired
    FileViewList externFileViewList;


    @FXML
    private JFXTextField hostInput;

    /**
     * Initialisierung
     *
     * @param url URL
     * @param bundle Bundle
     */
    public void initialize(URL url, ResourceBundle bundle) {

    }

    /**
     * Melden am Zielserver
     */
    @FXML
    protected void login() {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    try {
                        DirectoryInformationResponse respone = documentsClient.sendDirectoryInformationRequest("http://" + hostInput.getText() + ":9090/ws/documents");

                        if (respone.isSuccess()) {
                            if (!respone.getFileConfig().isEmpty()) {
                                externFileViewList.setList(respone.getFileConfig());
                            }
                            documentsClient.urlList.addUrl("http://" + hostInput.getText() + ":9090/ws/documents");
                            router.getStage().setResizable(false);
                            router.setSceneContent("/main.fxml");
                        }
                    }
                    catch(IOException e){

                    }
                }
            });
    }


    /**
     * exit
     */
    @FXML
    protected void exit() {
        System.exit(0); //TODO Ordentlicher Exit.
    }
}
