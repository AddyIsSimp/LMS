package stages.admin;



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
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

import Entity.Staff;
import Function.Function;
import Function.dbFunction;
import javafx.event.ActionEvent;

import Function.globalVariable;

import static Function.globalVariable.*;

public class staffAccountController implements Initializable {

    Connection conn;
    Statement stmt;
    ResultSet rs;

    Staff searchStaff2;
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
    private TextField searchField;

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
    private TableColumn<Staff, String> firstNameCol, lastNameCol, staffIDCol;
    @FXML
    private ChoiceBox<String> sortCB;

    @FXML
    private RadioButton titleRB, isbnRB; // Declare these in your controller class

    @FXML
    private ToggleGroup searchToggleGroup; // Add this toggle group


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
        staffQty.setText(Integer.toString(sortedStaffListASC.size()));

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
    private void doSearch(MouseEvent event) throws IOException {
        if (searchField.getText().isEmpty()) {
            lblError.setText("Search text is blank");
            return;
        }

        String searchFld = searchField.getText();
        searchStaff2 = globalVariable.fnc.findStaffID(sortedStaffListASC, searchFld);
        if (searchStaff2 != null) {
            globalVariable.searchStaff = searchStaff2;

            Parent root = FXMLLoader.load(getClass().getResource("/stages/admin/adminFXML/staff/admin_acctStaffsModify.fxml"));

            if (event.getSource() instanceof Node) {
                Node sourceNode = (Node) event.getSource();
                if (sourceNode.getScene() != null) {
                    Stage stage = (Stage) sourceNode.getScene().getWindow();
                    if (stage != null) {
                        stage.setScene(new Scene(root));
                        stage.show();
                    }
                } else {
                    System.out.println("Source Node is not in a scene.");
                }
            } else {
                System.out.println("Event source is not a Node.");
            }
        } else {
            lblError.setText("Staff not found with the specified ID");
        }
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
            staffTableView.refresh();
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
