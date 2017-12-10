package client;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
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


}
