package client.gui;

import client.ws.DocumentsClient;
import de.htwsaar.FileView;
import de.htwsaar.Document;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.apache.derby.impl.tools.sysinfo.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


import javax.swing.*;
import javax.swing.plaf.FileChooserUI;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author cedosw
 */
public class MainController implements Initializable {


    private static final Logger logger = LoggerFactory.getLogger(MainController.class);


    @Autowired
    private DocumentsClient documentsClient;

    @Autowired
    private Router router;

    @Autowired
    FileViewList fileViewList;

    @FXML
    TableView<FileView> table_view;
    @FXML
    TableColumn<FileView, String> table_name;
    @FXML
    TableColumn<FileView, String> table_date;
    @FXML
    TableColumn<FileView, String> table_type;

    @FXML
    Label downloadDirectoryLabelDynamicText;


   @Override
    public void initialize(URL url, ResourceBundle bundle) {

       table_view.setOnMousePressed(new EventHandler<javafx.scene.input.MouseEvent>() {
           @Override
           public void handle(javafx.scene.input.MouseEvent event) {
               if(event.isPrimaryButtonDown() && event.getClickCount()== 2){
                   handleDoubleClickOnTableItem(table_view.getSelectionModel().getSelectedItem());
               }
           }
       });
       table_name.setCellValueFactory(new PropertyValueFactory<FileView, String>("fileOrDirectoryName"));
       table_date.setCellValueFactory(new PropertyValueFactory<FileView, String>("date"));
       table_type.setCellValueFactory(new PropertyValueFactory<FileView, String>("type"));

       table_view.setItems(fileViewList.getFileViewList());
    }

    @FXML
    private void chanceDownloadDirectory(){
        downloadDirectoryLabelDynamicText.setText(router.startDirectoryChooser());
    }
    @FXML
    private void uploadChoosenFile(){
        try {
            this.addItem(documentsClient.storeDocument(router.startFileChooser().getAbsolutePath()));
        }catch(IOException e){

        }
    }
   public void addItem(FileView fileView){
       fileViewList.addFileView(fileView);
       table_view.setItems(fileViewList.getFileViewList());
   }

    private void handleDoubleClickOnTableItem(FileView tableItem){

        if(tableItem.getType().equals("File")){
            try{
                Document document = documentsClient.downloadFileFromServer(tableItem.getFileOrDirectoryName(),tableItem.getSourceIp());
                byte[] demBytes = document.getContent();
                File outputFile = new File(downloadDirectoryLabelDynamicText.getText() + "/" + document.getName());
                FileOutputStream outputStream = new FileOutputStream(outputFile);
                outputStream.write(demBytes);
                outputStream.close();
                System.out.println("Datei erfolgreich gedownloaded");

           }catch (Exception e){
               e.printStackTrace();
           }
        }
    }
    @FXML
    protected void exit() {
        System.exit(0); //TODO Ordentlicher Exit.
    }
}
