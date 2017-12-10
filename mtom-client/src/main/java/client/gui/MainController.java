package client.gui;

import de.htwsaar.FileView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author cedosw
 */
public class MainController implements Initializable {


    private static final Logger logger = LoggerFactory.getLogger(MainController.class);




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

   private ObservableList<FileView> testList = FXCollections.observableArrayList();

   @Override
    public void initialize(URL url, ResourceBundle bundle) {

       table_name.setCellValueFactory(new PropertyValueFactory<FileView, String>("fileOrDirectoryName"));
       table_date.setCellValueFactory(new PropertyValueFactory<FileView, String>("date"));
       table_type.setCellValueFactory(new PropertyValueFactory<FileView, String>("type"));
       testList.addAll(fileViewList.getFileViewList());
       table_view.setItems(testList);
    }

   public void addItem(FileView fileView){
       testList.add(fileView);
       table_view.setItems(testList);
   }

    @FXML
    protected void exit() {
        System.exit(0); //TODO Ordentlicher Exit.
    }
}
