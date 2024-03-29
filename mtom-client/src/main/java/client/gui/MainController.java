package client.gui;

import client.ws.DocumentsClient;
import com.jfoenix.controls.JFXTextField;
import de.htwsaar.DirectoryInformationResponse;
import de.htwsaar.FileView;
import de.htwsaar.Document;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
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
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author cedosw
 */
public class MainController implements Initializable {


    private static final Logger logger = LoggerFactory.getLogger(MainController.class);
    private boolean isSearchOn = false;


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


                       row.setOnMouseEntered((new EventHandler<javafx.scene.input.MouseEvent>
                               () {

                           @Override
                           public void handle(javafx.scene.input.MouseEvent t) {

                               TableRow<FileView> currentMouseTarget = (TableRow)t.getSource();
                               handleMouseOver(currentMouseTarget.getIndex());
                           }
                       }));
                       return row;
                   }

               });
    }

    /**
     * Dateiumbenennung
     *
     * @param newName neuer Name
     * @param oldName alter Name
     * @param fileView Darstellung
     */
    private void renameDocument(String newName,String oldName,FileView fileView){
       FileView newFileView = documentsClient.renameDocument(oldName,newName);
       if(newFileView != null){
            table_view.getItems().remove(fileView);
            addItem(newFileView);
       }
    }

    private void handleMouseOver(int index){
      if(isSearchOn && table_view.getItems().size() > index)  {
          ToastView.makeMessage(router.getStage(),"Source Ordner: "+table_view.getItems().get(index).getRequestRootDirName(),1000,100,250);
       }
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
     * Setzen des Download-Ziel
     */
    @FXML
    private void chanceDownloadDirectory(){
       try {
           downloadDirectoryLabelDynamicText.setText(router.startDirectoryChooser());
       }
        catch(Exception e){

       }
    }

    /**
     * Dateiupload
     */
    @FXML
    private void uploadChoosenFile(){
        try {
            this.addItem(documentsClient.storeDocument(router.startFileChooser().getAbsolutePath()));
        }catch(Exception e){

        }
    }

    @FXML
    private void reset(){
        try{
            this.refresh();
            this.isSearchOn=false;
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
       table_view.setItems(fileViewList.getFileViewList());
    }

    /**
     * Doppelklick Handler für den Download
     *
     * @param tableItem Tabellen-Objekt
     */
    private void handleDoubleClickOnTableItem(FileView tableItem){
        try{
            if(tableItem != null) {
                    if (tableItem.getType().equals("File")) {
                        if (!downloadDirectoryLabelDynamicText.getText().isEmpty()) {
                            Document document = documentsClient.downloadFileFromServer(tableItem.getFileOrDirectoryName());
                            byte[] demBytes = document.getContent();
                            File outputFile = new File(downloadDirectoryLabelDynamicText.getText() + "/" + document.getName());
                            FileOutputStream outputStream = new FileOutputStream(outputFile);
                            outputStream.write(demBytes);
                            outputStream.close();
                            System.out.println("Datei erfolgreich gedownloaded");
                        }else{
                            ToastView.makeErrorMessage(router.getStage(),"Bitte Downloadverzeichnis auswählen",2500,250,250);
                        }

                    } else if (tableItem.getType().equals("Directory")) {
                        DirectoryInformationResponse respone = documentsClient.sendDirectoryInformationRequest("http://" + tableItem.getSourceIp() + ":9090/ws/documents");
                        if (respone.isSuccess()) {

                            table_view.getItems().clear();
                            fileViewList.getFileViewList().clear();
                            fileViewList.setList(respone.getFileConfig());
                            table_view.setItems(fileViewList.getFileViewList());
                            documentsClient.urlList.addUrl("http://" + tableItem.getSourceIp() + ":9090/ws/documents");


                        }

                    }
                }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Navigationsfunktion
     */
    @FXML
    private void goBack(){
        if(documentsClient.urlList.getSize() > 1) {
            documentsClient.urlList.remove();
            try {
               this.refresh();
            } catch (IOException e) {

            }
        }
    }

    private void refresh() throws IOException{

        DirectoryInformationResponse respone = documentsClient.sendDirectoryInformationRequest(documentsClient.urlList.getUrl());
        if (respone.isSuccess()) {
            table_view.getItems().clear();
            fileViewList.getFileViewList().clear();
            fileViewList.setList(respone.getFileConfig());
            table_view.setItems(fileViewList.getFileViewList());
        }
    }

    /**
     * Dateisuche
     */
    @FXML
    private void search(){
        if(searchInput.getText().isEmpty()) {
              try{
                  isSearchOn = false;
                  this.refresh();
              }catch (IOException e){}
        }else {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    try {
                        List<FileView> foundFiles = documentsClient.searchFile(searchInput.getText());
                        table_view.getItems().clear();
                        fileViewList.getFileViewList().clear();
                        if (!foundFiles.isEmpty()) {
                            fileViewList.setList(foundFiles);
                            table_view.setItems(fileViewList.getFileViewList());
                            isSearchOn = true;
                        }
                    } catch (Exception e) {}
                }
            });

        }
    }

    /**
     * exit
     */
    @FXML
    protected void exit() {
        System.exit(0); //TODO Ordentlicher Exit.
    }
}
