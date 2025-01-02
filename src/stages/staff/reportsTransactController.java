package stages.staff;


import Entity.Transact;
import Function.Function;
import Function.dbFunction;
import Function.globalVariable;
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
    private TableColumn<Transact, String> bkTitleCol, bkIsbnCol, schoolIdCol, studentNameCol, brrwDateCol, returnDateCol, statusCol, penaltyCol;
    @FXML
    private ChoiceBox<String> sortCB;
    @FXML
    private ChoiceBox<String> transactTypeCB;
    @FXML
    private Label pendingQty, ongoingQty, successQty, transactQty;

    private String[] sortType = {"A-Z", "Z-A"};
    private String[] transactType = {"All","Pending","Ongoing", "Finish"};
    private String[] reportOptions = {"Book", "Transaction"};
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
            transactQty.setText(Integer.toString(transactionList.size()));

            transactTypeCB.getItems().addAll(transactType);
            transactTypeCB.setValue(transactType[0]);

            transactTypeCB.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                filterTransactType();
            });

            reportbox.getItems().addAll(reportOptions);
            reportbox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    switch (newValue) {
                        case "Book":
                            loadFXMLForChoice("/stages/staff/staffFXML/reports/staff_reports.fxml");
                            break;
                        case "Transaction":
                            loadFXMLForChoice("/stages/staff/staffFXML/reports/staff_Transreports.fxml");
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
    
            // Set up TableView columns
            bkTitleCol.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
            bkIsbnCol.setCellValueFactory(new PropertyValueFactory<>("bkIsbn"));
            schoolIdCol.setCellValueFactory(new PropertyValueFactory<>("borrowerID"));
            studentNameCol.setCellValueFactory(new PropertyValueFactory<>("borrowerName"));
            statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
            brrwDateCol.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));
            returnDateCol.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
            penaltyCol.setCellValueFactory(new PropertyValueFactory<>("penalty"));

            if (globalVariable.transactList != null) {
                retrieveTransact = fnc.retrieveAllTransact(globalVariable.transactList);
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
            retrieveTransact.sort((t1, t2) -> t1.getBookTitle().compareToIgnoreCase(t2.getBookTitle()));
        } else {
            retrieveTransact.sort((t1, t2) -> t2.getBookTitle().compareToIgnoreCase(t1.getBookTitle()));
        }
        transactTableView.refresh();
        transactTableView.setItems(retrieveTransact);
    }

    private void filterTransactType() {
        if(transactTypeCB.getValue().equals("All")) {
            retrieveTransact = fnc.retrieveAllTransact(globalVariable.transactList);
            applySorting();
        }else if(transactTypeCB.getValue().equals("Pending")) {
            retrieveTransact = fnc.retrievePendingTransact(globalVariable.transactList);
            applySorting();
        }else if(transactTypeCB.getValue().equals("Ongoing")) {
            retrieveTransact = fnc.retrieveOngoingTransact(globalVariable.transactList);
            applySorting();
        }else if(transactTypeCB.getValue().equals("Finish")) {
            retrieveTransact = fnc.retrieveFinishTransact(globalVariable.transactList);
            applySorting();
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
}
