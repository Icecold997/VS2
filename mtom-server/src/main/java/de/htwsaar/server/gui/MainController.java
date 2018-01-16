package de.htwsaar.server.gui;

import de.htwsaar.server.config.ServerConfig;
import de.htwsaar.server.persistence.ServerInfo;
import de.htwsaar.server.ws.DocumentsClient;
import com.jfoenix.controls.JFXTextField;
import de.htwsaar.DirectoryInformationResponse;
import de.htwsaar.FileView;
import de.htwsaar.Document;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


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
    private ServerConfig serverConfig;

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



    /**
     * Initialisierung der Darstellung
     *
     * @param url URL
     * @param bundle Bundle
     */
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

       table_view.setEditable(true);

       table_name.setCellFactory(TextFieldTableCell.<FileView>forTableColumn());
       table_name.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<FileView, String>>() {
           @Override
           public void handle(TableColumn.CellEditEvent<FileView, String> event) {
               renameDocument(event.getNewValue(),event.getOldValue(),event.getRowValue());
           }
       });

       this.getFileInformation();
       table_view.setItems(fileViewList.getFileViewList());
       table_view.setRowFactory(
               new Callback<TableView<FileView>, TableRow<FileView>>() {
                   @Override
                   public TableRow<FileView> call(final TableView<FileView> tableView) {
                       final TableRow<FileView> row = new TableRow<>();
                       final ContextMenu rowMenu = new ContextMenu();

                       MenuItem removeItem = new MenuItem("Löschen");
                       removeItem.setOnAction(new EventHandler<ActionEvent>() {

                           @Override
                           public void handle(ActionEvent event) {
                               deleteDocument(row.getItem());
                           }
                       });
                       rowMenu.getItems().addAll(removeItem);

// only display context menu for non-null items:

                       row.contextMenuProperty().bind(
                               Bindings.when(Bindings.isNotNull(row.itemProperty()) )
                                       .then(rowMenu)
                                       .otherwise((ContextMenu)null));
                       return row;
                   }

               });


    }

    /**
     * Erhalte Dateiinformatoonen
     */
    private void getFileInformation(){
       try {
           DirectoryInformationResponse respone = documentsClient.sendDirectoryInformationRequest("http://" + serverConfig.getServerIp() + ":9090/ws/documents");

           if (respone.isSuccess()) {
               if (!respone.getFileConfig().isEmpty()) {
                   fileViewList.setList(respone.getFileConfig());
               }
           }
       }catch (Exception e){
           e.printStackTrace();
       }
    }

    /**
     * Dateiumbenennung
     *
     * @param newName neuer Name
     * @param oldName alter Name
     * @param fileView Darstellung
     */
    private void renameDocument(String newName,String oldName,FileView fileView){
        documentsClient.renameDocument(oldName,newName);

    }

    /**
     * Dateilöschung
     *
     * @param fileView Darstellung
     */
    private void deleteDocument(FileView fileView){
        if( documentsClient.deleteDocument(fileView.getFileOrDirectoryName())) {
            table_view.getItems().remove(fileView);
        }
    }

    /**
     * Dateiupload
     */
    @FXML
    private void uploadChoosenFile(){
        try {
         documentsClient.storeDocument(router.startFileChooser().getAbsolutePath());
        }catch(Exception e){

        }
    }

    /**
     * Element zur Darstellung hinzufügen
     *
     * @param fileView Darstellung
     */
    public void addItem(FileView fileView){
       fileViewList.addFileView(fileView);
       //table_view.setItems(fileViewList.getFileViewList());
    }

    public void deleteFile(FileView fileView){
        fileViewList.deleteFileView(fileView);
    }

    /**
     * deprecated
     *
     * @param tableItem --
     */
    private void handleDoubleClickOnTableItem(FileView tableItem){

    }

    /**
     * deprecated --
     */
    @FXML
    private void goBack(){

    }


    /**
     * exit
     */
    @FXML
    protected void exit() {
        System.exit(0); //TODO Ordentlicher Exit.
    }
}
