package client;


import de.htwsaar.SpringFxmlLoader;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.Modality;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Der Router für die ClientApplication. Kann injiziert werden um auf eine andere Szene zu navigieren.
 *
 * @author cedosw mgoebel
 */
@Component
public class Router {

    /** Die Stage der Anwendung */
    private Stage stage;

    @Autowired
    private SpringFxmlLoader fxmlLoader;


    /**
     * Ändert die Stage auf eine andere Szene.
     *
     * @param scene Die Scene die geladen werden soll.
     */
    public void changeScene(Scene scene) {
        stage.setScene(scene);
        stage.sizeToScene();
    }

    public Parent setSceneContent(String fxml, String stylesheet, int width, int height) {
        Parent page = (Parent) fxmlLoader.load(App.class.getResource(fxml), null);
        Scene scene = new Scene(page, width, height);
        scene.getStylesheets().add(App.class.getResource(stylesheet).toExternalForm());
        stage.hide();
        stage.getIcons().add(new Image("/soap.png"));
        stage.setScene(scene);
        stage.sizeToScene();
        stage.centerOnScreen();
        stage.show();
        return page;
    }



    public Parent setSceneContent(String fxml, String stylesheet) {
        return setSceneContent(fxml, stylesheet, 1280, 720);
    }

    public Parent setSceneContent(String fxml) {
        return setSceneContent(fxml, "/theme.css", 1152, 648);
    }

    public void showModal(String fxml, String stylesheet, int width, int height, String title) {
        Stage dialog = new Stage();
        dialog.getIcons().add(new Image("/soap.png"));
        //dialog.initStyle(StageStyle.UNDECORATED);
        dialog.setTitle(title);
        Scene scene = new Scene((Parent) fxmlLoader.load(App.class.getResource(fxml), null));
        dialog.setHeight(height);
        dialog.setWidth(width);
        scene.getStylesheets().add(App.class.getResource(stylesheet).toExternalForm());
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setScene(scene);
        dialog.setResizable(false);
        dialog.centerOnScreen();
        dialog.show();

    }

    public void closeModal(Parent scene){
        Stage stage = (Stage) scene.getScene().getWindow();
        stage.close();
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;

    }
}
