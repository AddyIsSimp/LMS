package stages.admin;


import Entity.Staff;
import Function.Function;
import Function.dbFunction;
import Function.globalVariable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
import java.util.ResourceBundle;

import static Function.globalVariable.sortedStaffListASC;

public class staffAccountDeleteController implements Initializable {

    Connection conn;
    Statement stmt;
    ResultSet rs;

    dbFunction dbFnc = new dbFunction();
    Function fnc = new Function();


    @FXML
    private Label acctStaffBtn;

    @FXML
    private Label acctStudentBtn;
    @FXML
    private HBox inventoryBtn, logoutBtn, reportsBtn;

    @FXML
    private Label staffQty, studentQty;

    @FXML
    private Button saveBttn;
    @FXML
    private TextField IDField, confirmPassField, fNameField, lNameField,  passwordField, searchField;

    @FXML
    private Label lblError, lblError2;

    @FXML
    private TableView<Staff> staffTableView;

    @FXML
    private HBox dashboardBtn;
    @FXML
    private HBox  bkManageBtn;
    @FXML private HBox borrowTransBtn;

    @FXML
    private TableColumn<Staff, String> firstNameCol, lastNameCol, staffIDCol;
    @FXML
    private ChoiceBox<String> sortCB;

    @FXML
    private RadioButton titleRB, isbnRB; // Declare these in your controller class

    @FXML
    private ToggleGroup searchToggleGroup; // Add this toggle group

    private Staff searchStaff;
    private String[] sortType = {"A-Z", "Z-A"};
    private ObservableList<Staff> retrieveStaff = FXCollections.observableArrayList();

    @Override

    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize sort options
        sortCB.getItems().addAll(sortType);
        sortCB.setValue(sortType[0]);
        sortCB.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            applySorting(fnc.retrieveStaff(sortedStaffListASC));
        });

        // Set up TableView columns
        firstNameCol.setCellValueFactory(new PropertyValueFactory<>("fName"));
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lName"));
        staffIDCol.setCellValueFactory(new PropertyValueFactory<>("username"));

        if (sortedStaffListASC != null) {
            retrieveStaff = fnc.retrieveStaff(globalVariable.sortedStaffListASC);
            staffTableView.setItems(retrieveStaff);
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

    private void refreshTable() {
        retrieveStaff = fnc.retrieveStaff(globalVariable.sortedStaffListASC);
        applySorting(retrieveStaff);
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
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
    private void goReports(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/stages/admin/adminFXML/reports/admin_acc_reports.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void goAcctStudent(MouseEvent actionevent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/stages/admin/adminFXML/students/admin_acctStudents.fxml"));
        Stage stage = (Stage) ((Node) actionevent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
    @FXML
    private void returnAcctStaff(MouseEvent actionevent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/stages/admin/adminFXML/staff/admin_acctStaffs.fxml"));
        Stage stage = (Stage) ((Node) actionevent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void doSearch(MouseEvent event) {
        String searchFld = searchField.getText();
        if(searchFld.isEmpty() || searchFld.equals(null)) {
            lblError.setText("Search text is blank"); return;
        }

        searchStaff = fnc.findStaffID(sortedStaffListASC, searchFld);
        if(searchStaff!=null) {
            IDField.setText(searchStaff.getUsername());
            fNameField.setText(searchStaff.getFName());
            lNameField.setText(searchStaff.getLName());
            passwordField.setText(searchStaff.getPassword());
            confirmPassField.setText(searchStaff.getPassword());
            saveBttn.setDisable(false);
        }else {
            lblError.setText("Staff not found with the ID");
        }
    }

    @FXML
    private void doDeleteStaff(ActionEvent event) {
        try {
            if (fNameField.getText() == null || fNameField.getText().trim().isEmpty()) {
                lblError.setText("First name is empty");
                return;
            }

            String fName = fNameField.getText();
            String lName = lNameField.getText();
            String password = passwordField.getText();
            String staffID = IDField.getText();

            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Delete Staff", ButtonType.NO, ButtonType.YES);
            alert.setTitle("Delete Staff");
            alert.setHeaderText("Do you really want to delete the staff account?");

            if (alert.showAndWait().get() == ButtonType.YES) {
                dbFnc.deleteStaffDB(staffID);
                fnc.deleteStaff(sortedStaffListASC, staffID);
                refreshTable();

                alert = new Alert(Alert.AlertType.NONE, "Delete Staff Successfully", ButtonType.OK);
                alert.setTitle("Staff Account Delete");
                alert.show();

                fNameField.setText(null);
                lNameField.setText(null);
                passwordField.setText(null);
                confirmPassField.setText(null);
                IDField.setText(null);
                lblError.setText(null);
                saveBttn.setDisable(true);
            }else{
                fNameField.setText(null);
                lNameField.setText(null);
                passwordField.setText(null);
                confirmPassField.setText(null);
                IDField.setText(null);
                lblError.setText(null);
                saveBttn.setDisable(true);
                alert.close();
            }
            }catch(Exception e){
                Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
                alert.setTitle("Delete Staff Error");
                alert.show();
            }


    }
}
