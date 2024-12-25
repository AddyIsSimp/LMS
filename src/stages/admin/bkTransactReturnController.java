package stages.admin;

import Entity.Transact;
import Function.Function;
import Function.dbFunction;
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
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;
import Function.globalVariable;
public class bkTransactReturnController implements Initializable {

    Connection conn;
    Statement stmt;
    ResultSet rs;

    dbFunction dbFnc = new dbFunction();
    Function fnc = new Function();

    @FXML
    private HBox acctBtn, bkManageBtn, borrowTransBtn, dashboardBtn, inventoryBtn, logoutBtn, reportsBtn;
    @FXML
    private TableView<Transact> brrwTransTblView;
    @FXML
    private TableColumn<Transact, Date> borrowDateCol;
    @FXML
    private TableColumn<Transact, Integer> daysCol;
    @FXML
    private TableColumn<Transact, Button> returnBtnCol;
    @FXML
    private TableColumn<Transact, Integer> studentIDCol;
    @FXML
    private TableColumn<Transact, String> studentNameCol;
    @FXML
    private TableColumn<Transact, String> titleCol;

    @FXML
    private ChoiceBox<String> sortBy;
    private final String[] sortType = {"A-Z", "Z-A"};

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize sort options
        sortBy.getItems().addAll(sortType);
        sortBy.setValue(sortType[0]);

        // Set up TableView columns
        titleCol.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
        borrowDateCol.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));
        studentIDCol.setCellValueFactory(new PropertyValueFactory<>("borrowerID"));
        studentNameCol.setCellValueFactory(new PropertyValueFactory<>("borrowerName"));
        returnBtnCol.setCellValueFactory(new PropertyValueFactory<>("returnBtn"));
        daysCol.setCellValueFactory(new PropertyValueFactory<>("dayLeft"));

        // Load data into TableView
        ObservableList<Transact> transacts = FXCollections.observableArrayList();

        if (globalVariable.transactList != null) {
            transacts = fnc.retrieveOngoingTransact(globalVariable.transactList);
        } else {
            globalVariable.fnc.showAlert("Error", "Transaction list is empty or not initialized.");
        }

        brrwTransTblView.setItems(transacts);
    }

//SWITCHING MENU

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

    @FXML
    private void goReports(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/stages/admin/adminFXML/admin_reports.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

}
