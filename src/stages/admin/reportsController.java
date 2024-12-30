package stages.admin;


import Entity.Staff;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.scene.control.*;


import Function.*;

import static Function.globalVariable.sortedStaffListASC;
import static Function.globalVariable.staffList;

public class reportsController implements Initializable {
    Connection conn;
    Statement stmt;
    ResultSet rs;

    dbFunction dbFnc = new dbFunction();
    Function fnc = new Function();

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
    private TableView<Staff> staffTableView;
    @FXML
    private TableColumn<Staff, String> firstNameCol, lastNameCol, staffIDCol;
    @FXML
    private ChoiceBox<String> sortCB;



    @FXML
    private ToggleGroup searchToggleGroup; // Add this toggle group


    private String[] sortType = {"A-Z", "Z-A"};
    private ObservableList<Staff> retrieveStaff = FXCollections.observableArrayList();


    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            // Create a list of report options
            ArrayList<String> reportOptions = new ArrayList<>();
            reportOptions.add("Accounts");
            reportOptions.add("Books");
            reportOptions.add("Penalty");
            reportOptions.add("Transaction");
    
            // Add options to the ChoiceBox and set the default value
            reportbox.getItems().addAll(reportOptions);
    
            // Add a listener for selection changes
            reportbox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    // Load the corresponding FXML file based on the selected option
                    switch (newValue) {
                        case "Accounts":
                            loadFXMLForChoice("/stages/admin/adminFXML/reports/admin_acc_reports.fxml");
                            break;
                        case "Books":
                            loadFXMLForChoice("/stages/admin/adminFXML/reports/admin_book_reports.fxml");
                            break;
                        case "Penalty":
                            loadFXMLForChoice("/stages/admin/adminFXML/reports/admin_penalty_reports.fxml");
                            break;
                        case "Transaction":
                            loadFXMLForChoice("/stages/admin/adminFXML/reports/admin_trans_reports.fxml");
                            break;
                        default:
                            break;
                    }
                }
            });
    
            // Initialize sort options
            sortCB.getItems().addAll(sortType);
            sortCB.setValue(sortType[0]);
    
            // Set up TableView columns
            firstNameCol.setCellValueFactory(new PropertyValueFactory<>("fName"));
            lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lName"));
            staffIDCol.setCellValueFactory(new PropertyValueFactory<>("username"));
    
            if (staffList != null) {
                retrieveStaff = fnc.retrieveStaff(globalVariable.sortedStaffListASC);
            } else {
                showErrorAlert("Error", "Staff list is empty or not initialized.");
            }
    
            staffTableView.setItems(retrieveStaff);
        } catch (Exception e) {
            // Handle errors and show an alert
            Alert alert = new Alert(Alert.AlertType.ERROR, "An error occurred: " + e.getMessage());
            alert.showAndWait();
        }
    }

    private void applySorting(ObservableList<Staff> staffList) {
        if (sortCB.getValue().equals("A-Z")) {
            globalVariable.sortedStaffListASC.clear();
            staffList.sorted((s1, s2) -> s1.getFName().compareToIgnoreCase(s2.getFName()))
                    .forEach(globalVariable.sortedStaffListASC::add);
            staffTableView.setItems(FXCollections.observableArrayList(globalVariable.sortedStaffListASC));
        } else {
            sortedStaffListASC.clear();
            staffList.sorted((s1, s2) -> s2.getFName().compareToIgnoreCase(s1.getFName()))
                    .forEach(sortedStaffListASC::add);
            staffTableView.setItems(FXCollections.observableArrayList(sortedStaffListASC));
        }
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
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





       /* switch (report) {
            case "Accounts":
                return "/stages/admin/adminFXML/reports/admin_acc_reports.fxml";
            case "Book":
                return "/stages/admin/adminFXML/reports/admin_book_reports.fxml";
            case "Transaction":
                return "/stages/admin/adminFXML/reports/admin_trans_reports.fxml";
            case "Penalty":
                return "/stages/admin/adminFXML/reports/admin_penalty_reports.fxml";
            default:
                return null;
    */





    @FXML
    private void goDashboard(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/stages/admin/adminFXML/admin_dashboard.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void goAccountStaff(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/stages/admin/adminFXML/staff/admin_acctStaffs.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void goBorrowTransact(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/stages/admin/adminFXML/transact/admin_transact.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void goInventory(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/stages/admin/adminFXML/inventory/admin_inventory.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void goLogout(MouseEvent event) throws IOException {
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
    private void goManageBooks(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/stages/admin/adminFXML/admin_bkManage.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void goProfileAdmin(MouseEvent event) {

    }



//    @FXML
//    private void goReports(MouseEvent event) throws IOException {
//        Parent root = FXMLLoader.load(getClass().getResource("/stages/admin/adminFXML/admin_reports.fxml"));
//        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//        stage.setScene(new Scene(root));
//        stage.show();
//    }
}
