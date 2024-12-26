package stages.staff;

import Entity.Category;
import Function.globalVariable;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class reportsController implements Initializable {

    @FXML
    private HBox acctBtn;

    @FXML
    private HBox bkManageBtn;

    @FXML
    private HBox borrowTransBtn;

    @FXML
    private HBox dashboardBtn;

    @FXML
    private HBox inventoryBtn;

    @FXML
    private HBox logoutBtn;

    @FXML
    private HBox reportsBtn;

    @FXML
    private ChoiceBox<String> reportbox;

    @FXML
    private Label reportname;

//SWITCHING MENU
@FXML
@Override
public void initialize(URL location, ResourceBundle resources) {
    try {
        // Create a list of report options
        ArrayList<String> reportOptions = new ArrayList<>();
        reportOptions.add("Books");
        reportOptions.add("Penalty");
        reportOptions.add("Transaction");

        // Add options to the ChoiceBox and set the default value
        if (!reportOptions.isEmpty()) {
            reportbox.getItems().addAll(reportOptions);
            reportbox.getValue(); // Set the default value

        }

        // Add a listener for selection changes
        reportbox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // Load the corresponding FXML file based on the selected option
                switch (newValue) {
                    case "Books":
                        loadFXMLForChoice("/stages/staff/staffFXML/reports/staff_reports.fxml");
                        break;
                    case "Penalty":
                        loadFXMLForChoice("/stages/staff/staffFXML/reports/staff_Penaltyreports.fxml");
                        break;
                    case "Transaction":
                        loadFXMLForChoice("/stages/staff/staffFXML/reports/staff_Transreports.fxml");
                        break;
                    default:
                        break;
                }
            }
        });
    } catch (Exception e) {
        // Handle errors and show an alert
        Alert alert = new Alert(Alert.AlertType.ERROR, "An error occurred: " + e.getMessage());
        alert.showAndWait();
    }
}

    private void updateChoiceBoxName(String name) {
        // Dynamically update the name of the ChoiceBox
        reportbox.setAccessibleText(name); // Optional, for accessibility purposes
        reportbox.setRotate(Double.parseDouble(name));     // Updates the prompt text to show the selected value
    }
    private void loadFXMLForChoice(String fxmlPath) {
        try {
            // Load the FXML file
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));

            // Get the current stage
            Stage stage = (Stage) reportbox.getScene().getWindow();

            // Update the scene
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            // Handle exceptions and show an alert
            Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to load the selected report: " + e.getMessage());
            alert.showAndWait();
        }
    }


    @FXML
    private void goDashboard(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/stages/staff/staffFXML/staff_dashboard.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void goBorrowTransact(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/stages/staff/staffFXML/transact/staff_brrowtrans.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void goInventory(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/stages/staff/staffFXML/inventory/staff_inventory.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    void goLogout(MouseEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("You're about to logout!");
        alert.setContentText("Do you want to continue?");

        if(alert.showAndWait().get() == ButtonType.OK) {
            System.out.println("You successfully logged out!");
            Parent root = FXMLLoader.load(getClass().getResource("/stages/login/logFXML/login_view.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        }
    }

    @FXML
    void goManageBooks(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/stages/staff/staffFXML/staff_managebooks.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }





//    @FXML
//    void goReports(MouseEvent event) throws IOException {
//        Parent root = FXMLLoader.load(getClass().getResource("/stages/staff/staffFXML/staff_reports.fxml"));
//        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//        stage.setScene(new Scene(root));
//        stage.show();
//    }

}
