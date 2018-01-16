package de.htwsaar.server.gui;

import de.htwsaar.Directory;
import de.htwsaar.FileView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class FileViewList {

    private ObservableList<FileView> fileViewList = FXCollections.observableArrayList();

    public ObservableList<FileView> getFileViewList(){
        return this.fileViewList;
    }

    public void addFileView(FileView fileView) {

            ArrayList<FileView> delFileView = new ArrayList<FileView>();
            for (FileView f : fileViewList) {
                if (f.getFileOrDirectoryName().equals(fileView.getFileOrDirectoryName())) {
                    delFileView.add(f);
                }
            }
            this.fileViewList.removeAll(delFileView);
            this.fileViewList.add(fileView);
    }

    public void deleteFileView(FileView fileView){
        for(FileView f: fileViewList){
            if(f.getFileOrDirectoryName().equals(fileView.getFileOrDirectoryName())){
                fileViewList.remove(f);
            }
        }
    }

    public void setList(List<FileView> fileViews){
        this.fileViewList.addAll(fileViews);
    }
}
