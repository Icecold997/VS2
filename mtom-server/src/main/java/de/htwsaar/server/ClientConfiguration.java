package de.htwsaar.server;

import de.htwsaar.server.config.FloodingTransmitter;
import de.htwsaar.server.gui.MainController;
import de.htwsaar.server.ws.DocumentsClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * @author tsander
 */
@Configuration
public class ClientConfiguration {

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
