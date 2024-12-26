package stages.admin;



import Entity.Transact;
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
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import Entity.Staff;
import Entity.Transact;
import Function.Function;
import Function.dbFunction;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import Function.globalVariable;

public class staffAccountController implements Initializable {

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
    private TextField IDField, confirmPassField, fNameField, lNameField,  passwordField;
    @FXML
    private Label lblError;

    @FXML
    private TableView<Staff> staffTableView;

    @FXML
    private HBox dashboardBtn;
    @FXML
    private HBox  bkManageBtn;
    @FXML private HBox borrowTransBtn;

    @FXML
    private TableColumn<Staff, String> firstNameCol, lastNameCol, emailCol, staffIDCol;
    @FXML
    private ChoiceBox<String> sortCB;

    private String[] sortType = {"A-Z", "Z-A"};
    ObservableList<Staff> staffList = FXCollections.observableArrayList();

    @Override

    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize sort options
        sortCB.getItems().addAll(sortType);
        sortCB.setValue(sortType[0]);

        // Set up TableView columns
        firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        staffIDCol.setCellValueFactory(new PropertyValueFactory<>("username"));

        // Load data into TableView
        ObservableList<Staff> staffList = FXCollections.observableArrayList(globalVariable.stafflist);

        if (staffList.isEmpty()) {
            showErrorAlert("Error", "Staff list is empty or not initialized.");
        }

        staffTableView.setItems(staffList);

        // Add sorting functionality
        sortCB.setOnAction(event -> applySorting(staffList));
    }

    private void applySorting(ObservableList<Staff> staffList) {
        if (sortCB.getValue().equals("A-Z")) {
            globalVariable.sortedStaffListASC.clear();
            staffList.sorted((s1, s2) -> s1.getfName().compareToIgnoreCase(s2.getfName()))
                    .forEach(globalVariable.sortedStaffListASC::add);
            staffTableView.setItems(FXCollections.observableArrayList(globalVariable.sortedStaffListASC));
        } else {
            globalVariable.sortedStaffListDESC.clear();
            staffList.sorted((s1, s2) -> s2.getfName().compareToIgnoreCase(s1.getfName()))
                    .forEach(globalVariable.sortedStaffListDESC::add);
            staffTableView.setItems(FXCollections.observableArrayList(globalVariable.sortedStaffListDESC));
        }
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
    private void goAcctStudent(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/stages/admin/adminFXML/students/admin_acctStudents.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
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
    public void addStaff(ActionEvent actionevent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/stages/admin/adminFXML/staff/admin_acctStaffsAdd.fxml"));
        Stage stage = (Stage) ((Node) actionevent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    public void editStaff(ActionEvent actionevent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/stages/admin/adminFXML/staff/admin_acctStaffsModify.fxml"));
        Stage stage = (Stage) ((Node) actionevent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    public void deleteStaff(ActionEvent actionevent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/stages/admin/adminFXML/staff/admin_acctStaffsDelete.fxml"));
        Stage stage = (Stage) ((Node) actionevent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void doSearch(ActionEvent event) {

    }

    @FXML
    private void generateID(ActionEvent event) {
        try {
            if (lNameField.getText() == null || lNameField.getText().trim().isEmpty()) {
                lblError.setText("Last name is empty"); return;
            }

            conn = dbFnc.connectToDB();
            int staffID = globalVariable.dbFnc.resetAutoIncrement(conn, "staff", "staff_id");
            String staffIDTemp = fnc.getUserId(lNameField.getText(), staffID);
            IDField.setText(staffIDTemp);
        }catch(Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.setTitle("Generate ID Error");
            alert.show();
        }
    }

    @FXML
    private void doAddStaff(ActionEvent event) {
        try{
            if(fNameField.getText() == null || fNameField.getText().trim().isEmpty()) {
                lblError.setText("First name is empty"); return;
            }else if(lNameField.getText() == null || lNameField.getText().trim().isEmpty()) {
                lblError.setText("Last name is empty"); return;
            }else if(passwordField.getText() == null || passwordField.getText().trim().isEmpty()) {
                lblError.setText("Password is empty"); return;
            }else if(confirmPassField.getText() == null || confirmPassField.getText().trim().isEmpty()) {
                lblError.setText("Confirm Password is empty");
                return;
            }else if(passwordField.getText().equals(confirmPassField.getText())==false) {
                lblError.setText("Password does not match"); return;
            } else if(IDField.getText() == null || IDField.getText().trim().isEmpty()) {
                lblError.setText("Staff ID is empty"); return;
            }

            String fName = fNameField.getText();
            String lName = lNameField.getText();
            String password = passwordField.getText();
            String staffID = IDField.getText();

            Staff newStaff = new Staff(fName, lName, staffID, password);
            dbFnc.insertStaffDB(newStaff);
            globalVariable.sortedStaffListASC.add(newStaff);
            Alert alert = new Alert(Alert.AlertType.NONE, "Staff Registered Successfully", ButtonType.OK);
            alert.setTitle("Staff Register");
            alert.show();

            fNameField.setText(null);
            lNameField.setText(null);
            passwordField.setText(null);
            confirmPassField.setText(null);
            IDField.setText(null);
            lblError.setText(null);
        }catch(Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.setTitle("Register Staff Error");
            alert.show();
        }
    }


}
