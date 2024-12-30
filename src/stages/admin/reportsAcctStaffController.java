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


import Function.*;

import static Function.globalVariable.sortedStaffListASC;

public class reportsAcctStaffController implements Initializable {
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
    private ChoiceBox<String> sortCB, acctUserCB;
    @FXML
    private Label studentQty, staffQty, accountQty;
    @FXML
    private ToggleGroup searchToggleGroup; // Add this toggle group

    private String[] sortType = {"A-Z", "Z-A"};
    private String[] userType = {"Staff", "Student"};
    private String[] reportOptions = {"Accounts", "Books", "Transactions"};
    private ObservableList<Staff> retrieveStaff = FXCollections.observableArrayList();

    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            //Set the Quanity in the account reports
            studentQty.setText(Integer.toString(globalVariable.sortedStudentListASC.size()));
            staffQty.setText(Integer.toString(sortedStaffListASC.size()));
            accountQty.setText(Integer.toString(globalVariable.sortedStudentListASC.size() + sortedStaffListASC.size()));

            reportbox.getItems().addAll(reportOptions);
            reportbox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    switch (newValue) {
                        case "Accounts":
                            loadFXMLForChoice("/stages/admin/adminFXML/reports/admin_acc_reports.fxml");
                            break;
                        case "Books":
                            loadFXMLForChoice("/stages/admin/adminFXML/reports/admin_book_reports.fxml");
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
            sortCB.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                applySorting();
            });

            acctUserCB.getItems().addAll(userType);
            acctUserCB.setValue(userType[0]);
            acctUserCB.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                handleSwitch();
            });

            // Set up TableView columns
            firstNameCol.setCellValueFactory(new PropertyValueFactory<>("fName"));
            lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lName"));
            staffIDCol.setCellValueFactory(new PropertyValueFactory<>("username"));

            if (sortedStaffListASC != null) {
                retrieveStaff = fnc.retrieveStaff(globalVariable.sortedStaffListASC);
                applySorting();
            }
        } catch (Exception e) {
            // Handle errors and show an alert
            Alert alert = new Alert(Alert.AlertType.ERROR, "An error occurred: " + e.getMessage());
            alert.setTitle("Reports - Account Initialization Error");
            alert.showAndWait();
            e.printStackTrace();
        }
    }

    private void applySorting() {
        if (sortCB.getValue().equals("A-Z")) {
            retrieveStaff.sort((t1, t2) -> t1.getLName().compareToIgnoreCase(t2.getLName()));
        } else {
            retrieveStaff.sort((t1, t2) -> t2.getLName().compareToIgnoreCase(t1.getLName()));
        }
        staffTableView.refresh();
        staffTableView.setItems(retrieveStaff);
    }

    private void handleSwitch() {
        try {
            if (acctUserCB.getValue().equals("Staff")) {
                loadFXMLForChoice("/stages/admin/adminFXML/reports/admin_acc_reports.fxml");
            } else if (acctUserCB.getValue().equals("Student")) {
                loadFXMLForChoice("/stages/admin/adminFXML/reports/admin_acc_reportsStudent.fxml");
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
            alert.setTitle("Reports - Staff Account Error");
            alert.showAndWait();
        }
    }

    private void loadFXMLForChoice(String fxmlPath) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = (Stage) reportbox.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            // Handle exceptions and show an alert
            Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to load the selected report: " + e.getMessage());
            alert.showAndWait();
        }
    }

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

        if (alert.showAndWait().get() == ButtonType.OK) {
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
}
