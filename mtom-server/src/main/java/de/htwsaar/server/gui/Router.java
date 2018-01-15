package de.htwsaar.server.gui;

import de.htwsaar.SpringFxmlLoader;
import java.io.File;

import de.htwsaar.server.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Modality;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Der Router für die ClientApplication. Kann injiziert werden um auf eine andere Szene zu navigieren.
 *
 * @author wirth
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

    /**
     * Setzen des Szenen-Inhalts
     *
     * @param fxml          fxml
     * @param stylesheet    Stylesheet
     * @param width         Breite
     * @param height        Höhe
     * @return Seite
     */
    public Parent setSceneContent(String fxml, String stylesheet, int width, int height) {
        Parent page = (Parent) fxmlLoader.load(Application.class.getResource(fxml), null);
        Scene scene = new Scene(page, width, height);
        scene.getStylesheets().add(Application.class.getResource(stylesheet).toExternalForm());
        stage.hide();
        stage.getIcons().add(new Image("/soap.png"));
        stage.setScene(scene);
        stage.sizeToScene();
        stage.centerOnScreen();
        stage.show();
        return page;
    }

    /**
     * Directory-Chooser
     *
     * @return Directory c:/.../mydir/
     */
    public String startDirectoryChooser(){
        DirectoryChooser directoryChooser = new DirectoryChooser();
        return directoryChooser.showDialog(stage).toString();
    }

    /**
     * File-Chooser
     *
     * @return c:/.../myfile
     */
    public File startFileChooser(){
        FileChooser fileChooser = new FileChooser();
        return fileChooser.showOpenDialog(stage);
    }

    /**
     * Scene Content für Parents
     *
     * @param fxml          fxml
     * @param stylesheet    stylesheet
     * @return page (scene content)
     */
    public Parent setSceneContent(String fxml, String stylesheet) {
        return setSceneContent(fxml, stylesheet, 900, 900);
    }

    /**
     * Inhalt für Parents
     * @param fxml fxml
     * @return Scene Content
     */
    public Parent setSceneContent(String fxml) {
        return setSceneContent(fxml, "/theme.css", 700, 400);
    }

    /**
     * Zeige Parent
     * @param fxml  fxml
     * @param stylesheet stylesheet
     * @param width Breite
     * @param height Höhe
     * @param title Titel
     */
    public void showModal(String fxml, String stylesheet, int width, int height, String title) {
        Stage dialog = new Stage();
        dialog.getIcons().add(new Image("/soap.png"));
        //dialog.initStyle(StageStyle.UNDECORATED);
        dialog.setTitle(title);
        Scene scene = new Scene((Parent) fxmlLoader.load(Application.class.getResource(fxml), null));
        dialog.setHeight(height);
        dialog.setWidth(width);
        scene.getStylesheets().add(Application.class.getResource(stylesheet).toExternalForm());
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setScene(scene);
        dialog.setResizable(false);
        dialog.centerOnScreen();
        dialog.show();

    }

    /**
     * Schließen des Parents
     *
     * @param scene Szene
     */
    public void closeModal(Parent scene){
        Stage stage = (Stage) scene.getScene().getWindow();
        stage.close();
    }

    /**
     * Erhalte Stage
     *
     * @return Stage
     */
    public Stage getStage() {
        return stage;
    }

    /**
     * Setze Stage
     *
     * @param stage Stage
     */
    public void setStage(Stage stage) {
        this.stage = stage;

    }
}
