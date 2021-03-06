package view_controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Inventory;
import static model.Inventory.getPartInv;
import model.Product;
import model.Part;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class AddProductController implements Initializable {


    private ObservableList<Part> presentParts = FXCollections.observableArrayList();
    private ObservableList<Part> tempParts = FXCollections.observableArrayList();
    private String exception = new String();
    private int productID;

    @FXML
    private TextField addProdIdNumField;
    @FXML
    private TextField addProdNameField;
    @FXML
    private TextField addProdInvField;
    @FXML
    private TextField addProdPriceField;
    @FXML
    private TextField addProdMaxField;
    @FXML
    private TextField addProdMinField;
    @FXML
    private TableView<Part> addProdAddTV;
    @FXML
    private TableColumn<Part, Integer> addProdPartIDCol;
    @FXML
    private TableColumn<Part, String> addProdPartNameCol;
    @FXML
    private TableColumn<Part, Integer> addProdInvCol;
    @FXML
    private TableColumn<Part, Double> addProdPriceCol;
    @FXML
    private TableView<Part> delProdAddTV;
    @FXML
    private TableColumn<Part, Integer> delProdPartIDCol;
    @FXML
    private TableColumn<Part, String> delProdPartNameCol;
    @FXML
    private TableColumn<Part, Integer> delProdInvCol;
    @FXML
    private TableColumn<Part, Double> delProdPriceCol;
    @FXML
    private TextField addProdSearchField;

    // SEARCH BUTTON
    @FXML
    void addProdSearch(ActionEvent actionEvent) {
        String searchPart = addProdSearchField.getText();
        ModifyProductController.prodSearchHelper(searchPart, tempParts, addProdAddTV);
    }

    // ADD BUTTON
    @FXML
    void addProdAdd(ActionEvent actionEvent) {
        Part part = addProdAddTV.getSelectionModel().getSelectedItem();
        if (part == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Part Addition Error");
            alert.setContentText("No part was selected.");
        } else {
            presentParts.add(part);
            updateDelProdAddTV();
        }
    }

    public void updateDelProdAddTV() {
        delProdAddTV.setItems(presentParts);
    }

    // DELETE BUTTON
    @FXML
    void addProdDel(ActionEvent actionEvent) {
        Part part = delProdAddTV.getSelectionModel().getSelectedItem();
        prodDelAlert(part, presentParts);
    }

    static void prodDelAlert(Part part, ObservableList<Part> currentParts) {
        if (part == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Part Deletion Error");
            alert.setContentText("No part was selected.");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.initModality(Modality.NONE);
            alert.setTitle("DELETE");
            alert.setHeaderText("Confirm Deletion");
            alert.setContentText("Are you sure you want to delete " + part.getPartName() + "?");
            Optional<ButtonType> confirm = alert.showAndWait();
            if (confirm.get() == ButtonType.OK) {
                currentParts.remove(part);
            }
        }
    }

    // CANCEL BUTTON
    @FXML
    void addProdCancel(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("CANCEL");
        alert.setHeaderText("Confirm Cancellation");
        alert.setContentText("Are you sure you want to cancel adding a new product?");
        Optional<ButtonType> confirm = alert.showAndWait();
        if (confirm.get() == ButtonType.OK) {
            Parent cancel = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
            Scene s = new Scene(cancel);
            Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            window.setScene(s);
            window.show();
        }
    }

    // SAVE BUTTON
    @FXML
    void addProdSave(ActionEvent actionEvent) throws IOException {
        String prodName = addProdNameField.getText();
        String prodInv = addProdInvField.getText();
        String prodPrice = addProdPriceField.getText();
        String prodMax = addProdMaxField.getText();
        String prodMin = addProdMinField.getText();

        boolean isValid = true;
        StringBuilder err = new StringBuilder();

        String nameVal = Product.valName(prodName);
        if (!nameVal.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error Adding Part");
            alert.setContentText(nameVal);
            alert.showAndWait();

            isValid = false;
        }

        String maxMinVal = Product.valMaxMin(prodMax, prodMin);
        if (!maxMinVal.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error Adding Part");
            alert.setContentText(maxMinVal);
            alert.showAndWait();

            isValid = false;
        }

        String invVal = Product.valInv(prodInv, prodMax, prodMin);
        errorMessage(invVal);

        String priceVal = Product.valPrice(prodPrice);
        errorMessage(priceVal);

        if (isValid) {
            Product newProd = new Product();
            newProd.setProductID(productID);
            newProd.setProductName(prodName);
            newProd.setProductInStock(Integer.parseInt(prodInv));
            newProd.setProductPrice(Double.parseDouble(prodPrice));
            newProd.setProductMax(Integer.parseInt(prodMax));
            newProd.setProductMin(Integer.parseInt(prodMin));
            newProd.setProdParts(presentParts);
            Inventory.addProduct(newProd);

            Parent save = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
            Scene s = new Scene(save);
            Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            window.setScene(s);
            window.show();
        }

    }

    private void errorMessage(String priceVal) {
        boolean isValid;
        if (!priceVal.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error Adding Part");
            alert.setContentText(priceVal);

            isValid = false;
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        updateDelProdAddTV();
        productID = Inventory.getProductIdCount();
        addProdIdNumField.setText("Auto-gen: " + productID);

        updateAddPartTV();
        updateDeletePartTV();

        addProdPartIDCol.setCellValueFactory(cellData -> cellData.getValue().partIDProperty().asObject());
        addProdPartNameCol.setCellValueFactory(cellData -> cellData.getValue().partNameProperty());
        addProdInvCol.setCellValueFactory(cellData -> cellData.getValue().partInStockProperty().asObject());
        addProdPriceCol.setCellValueFactory(cellData -> cellData.getValue().partPriceProperty().asObject());

        delProdPartIDCol.setCellValueFactory(cellData -> cellData.getValue().partIDProperty().asObject());
        delProdPartNameCol.setCellValueFactory(cellData -> cellData.getValue().partNameProperty());
        delProdInvCol.setCellValueFactory(cellData -> cellData.getValue().partInStockProperty().asObject());
        delProdPriceCol.setCellValueFactory(cellData -> cellData.getValue().partPriceProperty().asObject());
    }

    public void updateAddPartTV() {
        addProdAddTV.setItems(getPartInv());
    }

    public void updateDeletePartTV() {
        delProdAddTV.setItems(presentParts);
    }
}
