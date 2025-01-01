package stages.staff;

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

    //TABLE AND COLUMNS
    @FXML
    private TableView<Transact> brrwTransTblView;
    @FXML
    private TableColumn<Transact, String> titleCol;
    @FXML
    private TableColumn<Transact, String> isbnCol;
    @FXML
    private TableColumn<Transact, String> studentIDCol;
    @FXML
    private TableColumn<Transact, String> studentNameCol;
    @FXML
    private TableColumn<Transact, String> acceptBtnCol;
    @FXML
    private TableColumn<Transact, String> declineBtnCol;

    @FXML
    private ChoiceBox<String> sortBy;

    private String[] sortType = {"A-Z", "Z-A"};

    //INITIALIZE
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            sortBy.getItems().addAll(sortType);
            sortBy.setValue(sortType[0]);

            //SET BORROW TRANSACTION TABLE
            titleCol.setCellValueFactory(new PropertyValueFactory<Transact, String>("bookTitle"));
            isbnCol.setCellValueFactory(new PropertyValueFactory<Transact, String>("bkIsbn"));
            studentIDCol.setCellValueFactory(new PropertyValueFactory<Transact, String>("borrowerID"));
            studentNameCol.setCellValueFactory(new PropertyValueFactory<Transact, String>("borrowerName"));
            acceptBtnCol.setCellValueFactory(new PropertyValueFactory<Transact, String>("acceptBtn"));
            declineBtnCol.setCellValueFactory(new PropertyValueFactory<Transact, String>("declineBtn"));

            //Get the data in the database
            ObservableList<Transact> transacts = FXCollections.observableArrayList();
//            conn = dbFnc.connectToDB();
//            stmt = conn.createStatement();

            //DBFUNCTION get the transact from db to here

            //Sample data
//            transacts.add(new Transact(1, "Nano", "120383013457", 20230015, "Santos, A", 00215, "PENDING"));
//            transacts.add(new Transact(1, "Oano", "120383013457", 20230015, "Santos, A", 00215, "PENDING"));
//            transacts.add(new Transact(1, "Pano", "120383013457", 20230015, "Santos, A", 00215, "PENDING"));
//            transacts.add(new Transact(1, "Qano", "120383013457", 20230015, "Santos, A", 00215, "PENDING"));

            brrwTransTblView.setItems(transacts);
//        }catch(SQLException e) {
//            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
//            alert.show();
        }catch(Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.setResizable(true);
            alert.show();
        }
    }



//SWITCHING MENU

    @FXML
    private void goDashboard(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/stages/staff/staffFXML/staff_dashboard.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

//    @FXML
//    private void goAccountStaff(MouseEvent event) throws IOException {
//        Parent root = FXMLLoader.load(getClass().getResource("/stages/admin/adminFXML/staff/admin_acctStaffs.fxml"));
//        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//        stage.setScene(new Scene(root));
//        stage.show();
//    }

//    @FXML
//    private void goBorrowTransact(MouseEvent event) throws IOException {
//        Parent root = FXMLLoader.load(getClass().getResource("/stages/admin/adminFXML/admin_transact.fxml"));
//        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//        stage.setScene(new Scene(root));
//        stage.show();
//    }

    @FXML
    private void goInventory(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/stages/staff/staffFXML/inventory/staff_inventory.fxml"));
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
        Parent root = FXMLLoader.load(getClass().getResource("/stages/staff/staffFXML/staff_managebooks.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void goProfileAdmin(MouseEvent event) {

    }

    @FXML
    private void goReports(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/stages/staff/staffFXML/reports/staff_reports.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));

        stage.show();
    }

    @FXML
    private void goReturnTransact(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/stages/staff/staffFXML/transact/staff_brrowtransReturn.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }


    @FXML
    private void goTransactHistory(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/stages/staff/staffFXML/transact/staff_transactHistory.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

}
