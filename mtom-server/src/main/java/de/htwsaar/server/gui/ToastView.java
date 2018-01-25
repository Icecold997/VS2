package de.htwsaar.server.gui;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 * Diese Klasse ist dazugedacht den Anwender auf einen Fehler mittels Toasts aufmerksam zu machen.
 */
public final class ToastView {

    /**
     * Erstellt einen ToastView relativ zur angegebenen Owner Stage.
     * Dieser ToastView wird für Fehlermeldungen benutzt. Indiziert durch rote Schrift
     * @param ownerStage Stage von der aus der ToastView angezeigt werden soll
     * @param toastMsg Nachricht die angezeigt werden soll
     * @param toastDelay Zeit wie lange der toast angezeigt werden soll (z.B. 3500 = 3,5 s)
     * @param fadeInDelay Dauer der FadeIn Animation
     * @param fadeOutDelay Dauer der FadeOut Animation
     * @author Mike Goebel
     */
    public static void makeErrorMessage(Stage ownerStage, String toastMsg, int toastDelay, int fadeInDelay, int fadeOutDelay) {

        Stage toastStage = new Stage();
        toastStage.initOwner(ownerStage);

        toastStage.setResizable(false);
        toastStage.initStyle(StageStyle.TRANSPARENT);

        Text text = new Text(toastMsg);
        text.setFont(Font.font("Calibri", 25));
        text.setFill(Color.ORANGERED);
        text.setWrappingWidth(400);
        text.setTextAlignment(TextAlignment.CENTER);


        StackPane root = new StackPane(text);

        root.setStyle("-fx-background-radius: 20;" +
                " -fx-background-color: rgba(0, 0, 0, 0.6);" +
                "-fx-padding: 50px;"

        );
        root.setOpacity(0);

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        toastStage.setWidth(500);
        toastStage.setHeight(100);
        toastStage.setX(ownerStage.getX() + ownerStage.getWidth() / 2 - toastStage.getWidth() / 2);
        toastStage.setY(ownerStage.getY() + ownerStage.getHeight() / 2 - toastStage.getHeight() / 2);
        toastStage.setScene(scene);
        toastStage.alwaysOnTopProperty();
        toastStage.show();

        Timeline fadeInTimeline = new Timeline();
        KeyFrame fadeInKey1 = new KeyFrame(Duration.millis(fadeInDelay), new KeyValue(toastStage.getScene().getRoot().opacityProperty(), 1));
        fadeInTimeline.getKeyFrames().add(fadeInKey1);
        fadeInTimeline.setOnFinished((ae) ->
        {
            new Thread(() -> {
                try {
                    Thread.sleep(toastDelay);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                Timeline fadeOutTimeline = new Timeline();
                KeyFrame fadeOutKey1 = new KeyFrame(Duration.millis(fadeOutDelay), new KeyValue(toastStage.getScene().getRoot().opacityProperty(), 0));
                fadeOutTimeline.getKeyFrames().add(fadeOutKey1);
                fadeOutTimeline.setOnFinished((aeb) -> toastStage.close());
                fadeOutTimeline.play();
            }).start();
        });
        fadeInTimeline.play();
    }

    /**
     * Erstellt einen ToastView relativ zur angegebenen Owner Stage.
     * Dieser ToastView wird für Infomeldungen benutzt. Indiziert durch blaue Schrift
     * @param ownerStage Stage von der aus der ToastView angezeigt werden soll
     * @param toastMsg Nachricht die angezeigt werden soll
     * @param toastDelay Zeit wie lange der toast angezeigt werden soll (z.B. 3500 = 3,5 s)
     * @param fadeInDelay Dauer der FadeIn Animation
     * @param fadeOutDelay Dauer der FadeOut Animation
     * @author Mike Goebel
     */
    public static void makeMessage(Stage ownerStage, String toastMsg, int toastDelay, int fadeInDelay, int fadeOutDelay) {

        Stage toastStage = new Stage();
        toastStage.initOwner(ownerStage);

        toastStage.setResizable(false);
        toastStage.initStyle(StageStyle.TRANSPARENT);

        Text text = new Text(toastMsg);
        text.setFont(Font.font("Calibri", 25));
        text.setFill(Color.LIGHTBLUE);
        text.setWrappingWidth(100);


        StackPane root = new StackPane(text);

        root.setStyle("-fx-background-radius: 20;" +
                " -fx-background-color: rgba(0, 0, 0, 0.6);" +
                "-fx-padding: 50px;"

        );
        root.setOpacity(0);

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        toastStage.setWidth(250);
        toastStage.setHeight(100);
        toastStage.setX(ownerStage.getX() + ownerStage.getWidth() / 2 - toastStage.getWidth() / 2);
        toastStage.setY(ownerStage.getY() + ownerStage.getHeight() / 2 - toastStage.getHeight() / 2);
        toastStage.setScene(scene);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                }catch (InterruptedException e){

                }
                toastStage.show();
                Timeline fadeInTimeline = new Timeline();
                KeyFrame fadeInKey1 = new KeyFrame(Duration.millis(fadeInDelay), new KeyValue(toastStage.getScene().getRoot().opacityProperty(), 1));
                fadeInTimeline.getKeyFrames().add(fadeInKey1);
                fadeInTimeline.setOnFinished((ae) ->
                {
                    new Thread(() -> {
                        try {
                            Thread.sleep(toastDelay);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        Timeline fadeOutTimeline = new Timeline();
                        KeyFrame fadeOutKey1 = new KeyFrame(Duration.millis(fadeOutDelay), new KeyValue(toastStage.getScene().getRoot().opacityProperty(), 0));
                        fadeOutTimeline.getKeyFrames().add(fadeOutKey1);
                        fadeOutTimeline.setOnFinished((aeb) -> toastStage.close());
                        fadeOutTimeline.play();
                    }).start();
                });
                fadeInTimeline.play();
            }
        });



    }
}