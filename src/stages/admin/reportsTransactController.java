package stages.admin;


import Entity.Book;
import Entity.Category;
import Entity.Transact;
import Function.Function;
import Function.dbFunction;
import Function.globalVariable;
import LinkedList.DoublyLinkList;
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

import static Function.globalVariable.bookList;

public class reportsTransactController implements Initializable {
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
    private TableView<Transact> transactTableView;
    @FXML
    private TableColumn<Transact, String> bkTitleCol, bkIsbnCol, schoolIdCol, studentNameCol, brrwDateCol, statusCol;
    @FXML
    private ChoiceBox<String> sortCB;
    @FXML
    private ChoiceBox<String> transactTypeCB;
    @FXML
    private Label pendingQty, ongoingQty, successQty, transactQty;

    private String[] sortType = {"A-Z", "Z-A"};
    private String[] transactType = {"All","Pending","Ongoing", "Finish"};
    private String[] reportOptions = {"Account", "Book", "Transaction"};
    private ObservableList<Transact> retrieveTransact = FXCollections.observableArrayList();
    private ArrayList<Transact> transactionList;

    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            //Set the Quanity in the account reports
            transactionList = globalVariable.transactList;
            pendingQty.setText(Integer.toString(fnc.retrievePendingTransact(transactionList).size()));
            ongoingQty.setText(Integer.toString(fnc.retrieveOngoingTransact(transactionList).size()));
            successQty.setText(Integer.toString(fnc.retrieveFinishTransact(transactionList).size()));
            transactQty.setText(Integer.toString(fnc.retrievePendingTransact(transactionList).size()));

                transactTypeCB.getItems().addAll(transactType);
                transactTypeCB.setValue(transactType[0]);

            transactTypeCB.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                filterTransactType();
            });

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
    
//            // Set up TableView columns
//            titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
//            authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
//            ctgryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
//            isbnCol.setCellValueFactory(new PropertyValueFactory<>("ISBN"));
//            qtyCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
//            brrwCol.setCellValueFactory(new PropertyValueFactory<>("borrowed"));
//
//            if (bookList != null) {
//                retrieveBook = fnc.retrieveBook(bookList);
//                applySorting();
//            }

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
            retrieveTransact.sort((t1, t2) -> t1.getBookTitle().compareToIgnoreCase(t2.getBookTitle()));
        } else {
            retrieveTransact.sort((t1, t2) -> t2.getBookTitle().compareToIgnoreCase(t1.getBookTitle()));
        }
        transactTableView.refresh();
        transactTableView.setItems(retrieveTransact);
    }

    //INCOMPLETE
    private void filterTransactType() {
        if(transactTypeCB.getValue().equals("All")) {

        }
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
}
