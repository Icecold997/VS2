package de.htwsaar.server.gui;

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
    FileViewList fileViewList;

    @FXML
    private JFXTextField searchInput;

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

       table_view.setEditable(true);

       table_name.setCellFactory(TextFieldTableCell.<FileView>forTableColumn());
       table_name.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<FileView, String>>() {
           @Override
           public void handle(TableColumn.CellEditEvent<FileView, String> event) {
               renameDocument(event.getNewValue(),event.getOldValue(),event.getRowValue());
           }
       });
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

    private void renameDocument(String newName,String oldName,FileView fileView){
       FileView newFileView = documentsClient.renameDocument(oldName,newName);
       if(newFileView != null){
            table_view.getItems().remove(fileView);
            addItem(newFileView);
       }
    }

    private void deleteDocument(FileView fileView){
        if( documentsClient.deleteDocument(fileView.getFileOrDirectoryName())) {
            table_view.getItems().remove(fileView);
        }
    }
    
    @FXML
    private void chanceDownloadDirectory(){
       try {
           downloadDirectoryLabelDynamicText.setText(router.startDirectoryChooser());
       }
        catch(Exception e){

       }
    }
    @FXML
    private void uploadChoosenFile(){
        try {
            this.addItem(documentsClient.storeDocument(router.startFileChooser().getAbsolutePath()));
        }catch(Exception e){

        }
    }


   public void addItem(FileView fileView){
       fileViewList.addFileView(fileView);
       table_view.setItems(fileViewList.getFileViewList());
   }

    private void handleDoubleClickOnTableItem(FileView tableItem){
        try{
        if(tableItem.getType().equals("File")){

                Document document = documentsClient.downloadFileFromServer(tableItem.getFileOrDirectoryName());
                byte[] demBytes = document.getContent();
                File outputFile = new File(downloadDirectoryLabelDynamicText.getText() + "/" + document.getName());
                FileOutputStream outputStream = new FileOutputStream(outputFile);
                outputStream.write(demBytes);
                outputStream.close();
                System.out.println("Datei erfolgreich gedownloaded");


        }else if(tableItem.getType().equals("Directory")){
            DirectoryInformationResponse respone = documentsClient.sendDirectoryInformationRequest("http://"+tableItem.getSourceIp()+":9090/ws/documents");
            if(respone.isSuccess()) {
                if(!respone.getFileConfig().isEmpty()) {
                    table_view.getItems().clear();
                    fileViewList.getFileViewList().clear();
                    fileViewList.setList(respone.getFileConfig());
                    table_view.setItems(fileViewList.getFileViewList());
                    documentsClient.urlList.addUrl("http://"+tableItem.getSourceIp()+":9090/ws/documents") ;

                }
            }

         }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    private void goBack(){
        if(documentsClient.urlList.getSize() > 1) {
            documentsClient.urlList.remove();
            try {
                DirectoryInformationResponse respone = documentsClient.sendDirectoryInformationRequest(documentsClient.urlList.getUrl());
                if (respone.isSuccess()) {
                    table_view.getItems().clear();
                    fileViewList.getFileViewList().clear();
                    fileViewList.setList(respone.getFileConfig());
                    table_view.setItems(fileViewList.getFileViewList());
                }
            } catch (IOException e) {

            }
        }
    }

    @FXML
    private void search(){
        System.out.println(documentsClient.searchFile(searchInput.getText()));
    }

    @FXML
    protected void exit() {
        System.exit(0); //TODO Ordentlicher Exit.
    }
}