/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javalab2.javafxapplication;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.stage.DirectoryChooser;
import javafx.util.Callback;

/**
 *
 * @author Damian Darczuk
 */
public class FXMLDocumentController implements Initializable {
     
    @FXML
    private TreeView<File> treeView;
    
    private final TreeItem<File> rootFile = new TreeItem<>();  
            
    @FXML
    private void addDirectoryAction(ActionEvent event) {
        TreeItem<File> selectedItem = treeView.getSelectionModel().getSelectedItem();
        File selectedFile = selectedItem.getValue();
        File newFile = new File(selectedFile, "new_file_name");
        newFile.mkdir();
        selectedItem.getChildren().add(new TreeItem<>(newFile));
    } 
    
    @FXML
    private void addFileAction(ActionEvent event) {
        try {
            TreeItem<File> selectedItem = treeView.getSelectionModel().getSelectedItem();
            File selectedFile = selectedItem.getValue();
            
            File newFile = new File(selectedFile, "new_file_name");
            newFile.createNewFile();
            
            selectedItem.getChildren().add(new TreeItem<>(newFile));
            
        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    } 
    
    private void removeFiles(TreeItem<File> parent) {
        
        if(parent.getChildren() != null)
            for(TreeItem<File> p: parent.getChildren())
                removeFiles(parent);
        
        parent.getParent().getChildren().remove(parent);
        parent.getValue().delete();
        
    }
    
    @FXML
    private void removeFileAction(ActionEvent event) {
        TreeItem<File> selectedItem = treeView.getSelectionModel().getSelectedItem();
        if(selectedItem != null && selectedItem.getChildren() != null && !selectedItem.equals(rootFile)) {
            selectedItem.getParent().getChildren().remove(selectedItem);
            selectedItem.getValue().delete();
        } else if(selectedItem.getChildren() != null && !selectedItem.equals(rootFile)) {
            removeFiles(selectedItem);        
        }
    }    
    
    @FXML 
    private void openDirectoryChooser (ActionEvent e) {                       
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File file = directoryChooser.showDialog(null);
        
        rootFile.getChildren().clear();
        rootFile.setValue(file);
        if(file.isDirectory())
            loadFilesTree(file, rootFile);
     
    }
    
    private void loadFilesTree(File file, TreeItem<File> root) {       
        if (file.listFiles() != null) {
            for(File f: file.listFiles()) {
                TreeItem<File> tree = new TreeItem<>(f);
                root.getChildren().add(tree);
                if (f.isDirectory())
                    loadFilesTree(f, tree);
            }            
        }
            
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        treeView.setRoot(rootFile);
        treeView.setCellFactory(new FileCellFactory());       
    }
    
    public class FileCell extends TreeCell<File> {  
        @Override
        protected void updateItem(File file, boolean empty) {
            super.updateItem(file, empty);
            if (file != null) {
                setText(file.getName());
            } else {
                setText(null);
            }
        }
    }
    
    public class FileCellFactory implements Callback<TreeView<File>, TreeCell<File>> {
        @Override
        public TreeCell<File> call(TreeView<File> param) {
            return new FileCell();
        }
    }
    
}
