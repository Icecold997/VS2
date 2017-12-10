package client;

import client.gui.LoginController;
import client.gui.MainController;
import client.ws.DocumentsClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * @author cedosw
 */
@Configuration
public class ClientConfiguration {

    @Bean
    @Scope("prototype")
    LoginController loginController() {
        return new LoginController();
    }

    @Bean
    @Scope("prototype")
    MainController mainController() {
            return new MainController();
            }

    @Bean
    @Scope("prototype")
    DocumentsClient documentsClient() {
        return new DocumentsClient();
    }
}
