package view_controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Inhouse;
import model.Inventory;
import model.Outsourced;
import model.Part;
import model.Part.*;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class AddPartController implements Initializable {

    @FXML
    private RadioButton addPartInHouseRadio;
    @FXML
    private RadioButton addPartOutsourcedRadio;
    @FXML
    private TextField addPartIdField;
    @FXML
    private TextField addPartNameField;
    @FXML
    private TextField addPartInvField;
    @FXML
    private TextField addPartPriceField;
    @FXML
    private TextField addPartMaxField;
    @FXML
    private TextField addPartMinField;
    @FXML
    private Label switchAddPartLabel;
    @FXML
    private TextField switchAddPartField;

    private boolean inHouse;
    private String exception = new String();
    private int partID;

    // RADIO BUTTONS
    @FXML
    void radioAddPartInHouse(ActionEvent actionEvent) {
        inHouse = true;
        switchAddPartLabel.setText("Machine ID");
        switchAddPartField.setPromptText("Mach ID");
        addPartOutsourcedRadio.setSelected(false);
    }

    @FXML
    void radioAddPartOutsourced(ActionEvent actionEvent) {
        inHouse = false;
        switchAddPartLabel.setText("Company Name");
        switchAddPartField.setPromptText("Comp Name");
        addPartInHouseRadio.setSelected(false);
    }


    // SAVE BUTTON
    @FXML
    void addPartSaveBtn(ActionEvent actionEvent) throws IOException {
        String partName = addPartNameField.getText();
        String partInv = addPartInvField.getText();
        String partPrice = addPartPriceField.getText();
        String partMax = addPartMaxField.getText();
        String partMin = addPartMinField.getText();
        String partSwitch = switchAddPartField.getText();

        boolean isValid = true;
        StringBuilder err = new StringBuilder();

        String nameVal = Part.valName(partName);
        if (!nameVal.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error Adding Part");
            alert.setContentText(nameVal);
            alert.showAndWait();

            isValid = false;
        }

        String maxMinVal = Part.valMaxMin(partMax, partMin);
        if (!maxMinVal.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error Adding Part");
            alert.setContentText(maxMinVal);
            alert.showAndWait();

            isValid = false;
        }

        String invVal = Part.valInv(partInv, partMax, partMin);
        errorMessage(invVal);

        String priceVal = Part.valPrice(partPrice);
        errorMessage(priceVal);

        if (isValid) {
            if (inHouse == true) {
                Inhouse inPart = new Inhouse();
                inPart.setPartID(partID);
                inPart.setPartName(partName);
                inPart.setPartPrice(Double.parseDouble(partPrice));
                inPart.setPartInStock(Integer.parseInt(partInv));
                inPart.setPartMax(Integer.parseInt(partMax));
                inPart.setPartMin(Integer.parseInt(partMin));
                inPart.setMachineID(Integer.parseInt(partSwitch));
                Inventory.addPart(inPart);
            } else {
                Outsourced outPart = new Outsourced();
                outPart.setPartID(partID);
                outPart.setPartName(partName);
                outPart.setPartPrice(Double.parseDouble(partPrice));
                outPart.setPartInStock(Integer.parseInt(partInv));
                outPart.setPartMax(Integer.parseInt(partMax));
                outPart.setPartMin(Integer.parseInt(partMin));
                outPart.setCompanyName(partSwitch);
                Inventory.addPart(outPart);
            }
            Parent partSave = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
                Scene s = new Scene(partSave);
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

    // CANCEL BUTTON
    @FXML
    void addPartCancelBtn(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancel");
        alert.setHeaderText("Confirm Cancellation");
        alert.setContentText("Are you sure you want to cancel adding a new part?");
        Optional<ButtonType> confirm = alert.showAndWait();

        if (confirm.get() == ButtonType.OK) {
            Parent partCancel = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
            Scene s = new Scene(partCancel);
            Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            window.setScene(s);
            window.show();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        partID = Inventory.getPartIdCount();
        addPartIdField.setText("Auto-Gen: " + partID);
    }
}
