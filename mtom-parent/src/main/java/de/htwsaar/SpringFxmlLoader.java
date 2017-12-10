package de.htwsaar;

import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;

/**
 * @author cedosw
 */
@Component
public class SpringFxmlLoader {
    @Autowired
    private ApplicationContext applicationContext;

    public Object load(URL url, String resources) {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setControllerFactory(clazz -> applicationContext.getBean(clazz));
        fxmlLoader.setLocation(url);
//        fxmlLoader.setResources(ResourceBundle.getBundle(resources));
        fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
        try {
            return fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
