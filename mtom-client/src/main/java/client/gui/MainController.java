package client.gui;

import client.ws.DocumentsClient;
import de.htwsaar.FileView;
import de.htwsaar.Document;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
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
       table_view.setRowFactory(
               new Callback<TableView<FileView>, TableRow<FileView>>() {
                   @Override
                   public TableRow<FileView> call(final TableView<FileView> tableView) {
                       final TableRow<FileView> row = new TableRow<>();
                       final ContextMenu rowMenu = new ContextMenu();
                       MenuItem editItem = new MenuItem("Umbennen");
                       editItem.setOnAction(new EventHandler<ActionEvent>() {

                           @Override
                           public void handle(ActionEvent event) {
                               renameDocument(row.getItem());
                           }
                       });
                       MenuItem removeItem = new MenuItem("Löschen");
                       removeItem.setOnAction(new EventHandler<ActionEvent>() {

                           @Override
                           public void handle(ActionEvent event) {
                               deleteDocument(row.getItem());
                           }
                       });
                       rowMenu.getItems().addAll(editItem, removeItem);

// only display context menu for non-null items:

                       row.contextMenuProperty().bind(
                               Bindings.when(Bindings.isNotNull(row.itemProperty()) )
                                       .then(rowMenu)
                                       .otherwise((ContextMenu)null));
                       return row;
                   }

               });
    }
    private void renameDocument(FileView fileView){
        //documentsClient.renameDocument()
        System.out.println("testclick");
    }

    private void deleteDocument(FileView fileView){
        if( documentsClient.deleteDocument(fileView.getFileOrDirectoryName())) {
            table_view.getItems().remove(fileView);
        }
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
