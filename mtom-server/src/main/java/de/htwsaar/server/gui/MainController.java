package de.htwsaar.server.gui;

import de.htwsaar.server.config.ServerConfig;
import de.htwsaar.server.persistence.FileArrangementConfig;
import de.htwsaar.server.persistence.FileArrangementDAO;
import de.htwsaar.server.persistence.ServerDAO;
import de.htwsaar.server.persistence.ServerInfo;
import de.htwsaar.server.ws.DocumentsClient;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


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

    private String workDirectoryPath;

    @Autowired
    private DocumentsClient documentsClient;

    @Autowired
    private Router router;

    @Autowired
    private ServerConfig serverConfig;

    @Autowired
    FileViewList fileViewList;

    @Autowired
    private ServerDAO serverDAO;

    @Autowired
    private FileArrangementDAO fileArrangementDAO;

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

       workDirectoryPath = serverConfig.fileDirectory;
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

    @FXML
    private void createDirectory(){
       documentsClient.createDir("Neuer Ordner",workDirectoryPath);

    }
    /**
     * Erhalte Dateiinformatoonen
     */
    private void getFileInformation(){
       try {
           Iterable<ServerInfo> superNodes = serverDAO.findAll();
           for(ServerInfo superNode : superNodes) {
               DirectoryInformationResponse respone = documentsClient.sendDirectoryInformationRequest("http://" + superNode.getServerIp() + ":9090/ws/documents",serverConfig.fileDirectory);

               if (respone.isSuccess()) {
                   if (!respone.getFileConfig().isEmpty()) {
                       fileViewList.setList(respone.getFileConfig());

                       this.downloadFile(respone.getFileConfig(),"http://"+superNode.getServerIp()+":9090/ws/documents" ,serverConfig.fileDirectory);

                   }
               }
           }
       }catch (Exception e){
           e.printStackTrace();
       }
    }

    private void downloadFile(List<FileView> files, String url,String currentPath){
        System.out.println("downloade file von: "+ url);
        System.out.println("current path:  "+ currentPath);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    String workPath  = serverConfig.fileDirectory ;
                    for (FileView file : files) {    //datei liste
                        if (file.getType().equals("File")) {   // wen datei
                            Document document = documentsClient.downloadFileFromServer(file.getFileOrDirectoryName(), url,file.getPath()); //downloade datei von url
                            byte[] demBytes = document.getContent();      //speichern der datei



                            String newPath1 = document.getPath().substring(document.getPath().indexOf(document.getRequestRootDirName())+document.getRequestRootDirName().length(),document.getPath().length());

                            if(newPath1.isEmpty()){  //root directory
                                workPath = serverConfig.fileDirectory;
                            }else{  //sub dir
                                workPath   = serverConfig.fileDirectory;
                                workPath   = workPath + newPath1;
                            }
                            File outputFile = new File(workPath);
                            FileOutputStream outputStream = new FileOutputStream(outputFile);
                            outputStream.write(demBytes);
                            outputStream.close();
                            FileArrangementConfig fileArrangementConfig = new FileArrangementConfig();
                            fileArrangementConfig.setDirectory(false);
                            fileArrangementConfig.setFilename(outputFile.getName());
                            fileArrangementConfig.setFileLocation(workPath.substring(0,workPath.lastIndexOf("/")));
                            fileArrangementConfig.setLocal(true);

                            if(!fileArrangementDAO.findByFileLocationAndFilename(fileArrangementConfig.getFileLocation(),fileArrangementConfig.getFilename()).isPresent()){
                                fileArrangementDAO.save(fileArrangementConfig);
                                System.out.println("Datei: "+fileArrangementConfig.getFilename());
                                System.out.println("Dateipfad: "+fileArrangementConfig.getFileLocation());
                                System.out.println("Datei in Datenbank aufgenommen");
                            }

                        }else{
                            String newPath1 = file.getPath().substring(file.getPath().indexOf(file.getRequestRootDirName())+file.getRequestRootDirName().length(),file.getPath().length());
                            if(newPath1.isEmpty()){  //root directory
                                workPath = serverConfig.fileDirectory  ;
                                newPath1 =  "/"+file.getFileOrDirectoryName();
                            }else{  //sub dir
                                workPath += newPath1;
                            }

                            File directory = new File(workPath+newPath1);
                            if(!directory.exists()) {
                                FileArrangementConfig fileArrangementConfig = new FileArrangementConfig();
                                fileArrangementConfig.setDirectory(true);
                                fileArrangementConfig.setFilename(directory.getName());
                                fileArrangementConfig.setFileLocation(workPath);
                                fileArrangementConfig.setLocal(true);
                                fileArrangementDAO.save(fileArrangementConfig);
                                directory.mkdir();


                                DirectoryInformationResponse response = documentsClient.sendDirectoryInformationRequest(url,file.getPath()+"/"+file.getFileOrDirectoryName());
                                downloadFile(response.getFileConfig(),url,file.getPath());
                            }else{
                                DirectoryInformationResponse response = documentsClient.sendDirectoryInformationRequest(url,file.getPath()+"/"+file.getFileOrDirectoryName());
                                downloadFile(response.getFileConfig(),url,file.getPath());
                            }


                        }

                    }
                }catch (IOException e){e.printStackTrace();}
            }
            }

        );
    }

    /**
     * Dateiumbenennung
     *
     * @param newName neuer Name
     * @param oldName alter Name
     * @param fileView Darstellung
     */
    private void renameDocument(String newName,String oldName,FileView fileView){
        documentsClient.renameDocument(oldName,newName,fileView.getPath());

    }

    /**
     * Dateilöschung
     *
     * @param fileView Darstellung
     */
    private void deleteDocument(FileView fileView){
        System.out.println("t"+fileView.getPath());
         documentsClient.deleteDocument(fileView.getFileOrDirectoryName(),fileView.getPath());
    }

    /**
     * Dateiupload
     */
    @FXML
    private void uploadChoosenFile(){
        try {
         documentsClient.storeDocument(router.startFileChooser().getAbsolutePath(),workDirectoryPath);
        }catch(Exception e){

        }
    }

    /**
     * Element zur Darstellung hinzufügen
     *
     * @param fileView Darstellung
     */
    public void addItem(FileView fileView){
        System.out.println("test1 : " +fileView.getPath());
        System.out.println("test2 : " +workDirectoryPath);
        if(fileView.getPath().equals(workDirectoryPath) || fileView.getPath().endsWith(serverConfig.getRootDirectory())) {

            fileViewList.addFileView(fileView);
        }
//       table_view.setItems(fileViewList.getFileViewList());
    }

    public void deleteFile(FileView fileView){
        fileViewList.deleteFileView(fileView);
    }

    /**
     * Wechsel zwischen directorys
     *
     * @param tableItem --
     */
    private void handleDoubleClickOnTableItem(FileView tableItem) {
        if (tableItem != null) {
            if (tableItem.getType().equals("Directory")) {
                try {
                    System.out.println("test: "+tableItem.getPath());
                    DirectoryInformationResponse respone = documentsClient.sendDirectoryInformationRequest("http://" + serverConfig.getServerIp() + ":9090/ws/documents", tableItem.getPath()+"/"+tableItem.getFileOrDirectoryName());

                    if (respone.isSuccess()) {
                            workDirectoryPath += "/" + tableItem.getFileOrDirectoryName();
                            table_view.getItems().clear();
                            fileViewList.getFileViewList().clear();
                            fileViewList.setList(respone.getFileConfig());
                            table_view.setItems(fileViewList.getFileViewList());

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * deprecated --
     */
    @FXML
    private void goBack() {
        if (!workDirectoryPath.equals(serverConfig.fileDirectory)) {
            try {
                DirectoryInformationResponse respone = documentsClient.sendDirectoryInformationRequest("http://" + serverConfig.getServerIp() + ":9090/ws/documents", workDirectoryPath.substring(0, workDirectoryPath.lastIndexOf("/")));

                if (respone.isSuccess()) {
                    workDirectoryPath = workDirectoryPath.substring(0, workDirectoryPath.lastIndexOf("/"));

                    table_view.getItems().clear();
                    fileViewList.getFileViewList().clear();
                    fileViewList.setList(respone.getFileConfig());
                    table_view.setItems(fileViewList.getFileViewList());

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
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
