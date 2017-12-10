package client;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.WebServiceException;
import org.springframework.ws.client.WebServiceIOException;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author cedosw
 */
public class LoginController implements Initializable {


    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);




    @Autowired
    private Router router;



    @FXML
    private JFXTextField usernameInput;
    @FXML
    private JFXPasswordField passwordInput;
    @FXML
    private JFXTextField hostInput;

    public void initialize(URL url, ResourceBundle bundle) {
    }

    @FXML
    protected void login() {
        router.getStage().setResizable(false);
        router.setSceneContent("/main.fxml");
    }

    @FXML
    protected void exit() {
        System.exit(0); //TODO Ordentlicher Exit.
    }
}
