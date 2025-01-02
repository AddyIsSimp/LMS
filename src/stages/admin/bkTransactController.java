package stages.admin;

import Entity.Transact;
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
import java.util.ArrayList;
import java.util.ResourceBundle;

import Function.*;

public class bkTransactController implements Initializable {

    Connection conn;
    Statement stmt;
    ResultSet rs;

    dbFunction dbFnc = new dbFunction();
    Function fnc = new Function();

    @FXML
    private HBox acctBtn, bkManageBtn, borrowTransBtn, dashboardBtn, inventoryBtn, logoutBtn, reportsBtn;

    // TABLE AND COLUMNS
    @FXML
    private TableView<Transact> brrwTransTblView;
    @FXML
    private TableColumn<Transact, String> titleCol;
    @FXML
    private TableColumn<Transact, String> isbnCol;
    @FXML
    private TableColumn<Transact, Integer> studentIDCol;
    @FXML
    private TableColumn<Transact, String> studentNameCol;
    @FXML
    private TableColumn<Transact, Button> acceptBtnCol;
    @FXML
    private TableColumn<Transact, Button> declineBtnCol;

    @FXML
    private ChoiceBox<String> sortBy;

    private final String[] sortType = {"A-Z", "Z-A"};
    private ObservableList<Transact> transacts = FXCollections.observableArrayList();

    // INITIALIZE
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            // Initialize ChoiceBox for sorting
            sortBy.getItems().addAll(sortType);
            sortBy.setValue(sortType[0]);

            sortBy.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                sortTransactData(); // Call sorting method when the value changes
            });

            // Set up TableView columns
            titleCol.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
            isbnCol.setCellValueFactory(new PropertyValueFactory<>("bkIsbn"));
            studentIDCol.setCellValueFactory(new PropertyValueFactory<>("borrowerID"));
            studentNameCol.setCellValueFactory(new PropertyValueFactory<>("borrowerName"));
            acceptBtnCol.setCellValueFactory(new PropertyValueFactory<>("acceptBtn"));
            declineBtnCol.setCellValueFactory(new PropertyValueFactory<>("declineBtn"));

            if (globalVariable.transactList != null) {
                transacts = fnc.retrievePendingTransact(globalVariable.transactList);
                transacts.sort((t1, t2) -> t1.getBookTitle().compareToIgnoreCase(t2.getBookTitle()));
                brrwTransTblView.setItems(transacts);
            }
        } catch (Exception e) {
            showErrorAlert("Initialization Error", e.getMessage());
        }
    }

    public void refreshTable() {
        try {
            if (globalVariable.transactList != null) {
                transacts = fnc.retrievePendingTransact(globalVariable.transactList);
                brrwTransTblView.setItems(transacts);
            } else {
                showErrorAlert("Data Error", "Transaction list is empty or not initialized.");
            }
        } catch (Exception e) {
            showErrorAlert("Refresh Error", e.getMessage());
        }
    }

    private void showErrorAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void sortTransactData() {
        if (transacts != null && !transacts.isEmpty()) {
            if (sortBy.getValue().equals("A-Z")) {
                transacts.sort((t1, t2) -> t1.getBookTitle().compareToIgnoreCase(t2.getBookTitle()));
            } else if (sortBy.getValue().equals("Z-A")) {
                transacts.sort((t1, t2) -> t2.getBookTitle().compareToIgnoreCase(t1.getBookTitle()));
            }
            brrwTransTblView.refresh();
        }
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

//    @FXML
//    private void goBorrowTransact(MouseEvent event) throws IOException {
//        Parent root = FXMLLoader.load(getClass().getResource("/stages/admin/adminFXML/admin_transact.fxml"));
//        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//        stage.setScene(new Scene(root));
//        stage.show();
//    }

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
    private void goReports(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/stages/admin/adminFXML/reports/admin_acc_reports.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));

        stage.show();
    }

    @FXML
    private void goReturnTransact(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/stages/admin/adminFXML/transact/admin_transactReturn.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void goTransactHistory(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/stages/admin/adminFXML/transact/admin_transactHistory.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void refresh(MouseEvent event) {
        refreshTable();
    }

}
