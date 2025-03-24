package com.example.rubank;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * Demo TableView.
 * The data source is the Enum class Location. The GUI object is a TableView with
 * 2 columns: zip and county.
 * @author Lily Chang
 */
public class Controller {
    @FXML
    TableView tbl_location;

    @FXML
    private TableColumn<Branch, String> col_zip, col_county; //TableColumn<S, T>

    /**
     * This method will be performed automatically after the fxml is loaded.
     * Write code to set the initial data for the GUI objects.
     * Typically, set a list of objects from the backend to the frontend objects,
     * such as ComboBox (dropdown), or ListView.
     */
    public void initialize() {
        ObservableList<Branch> branches =
                FXCollections.observableArrayList(Branch.values());
        tbl_location.setItems(branches);
        col_zip.setCellValueFactory(new PropertyValueFactory<>("zip"));
        col_county.setCellValueFactory(new PropertyValueFactory<>("county"));
    }
}