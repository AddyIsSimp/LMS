package stages.admin;


import Entity.Book;
import Entity.Category;
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

import static Function.globalVariable.*;

public class reportsBookController implements Initializable {
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
    private TableView<Book> bookTableView;
    @FXML
    private TableColumn<Book, String> titleCol, authorCol, ctgryCol, isbnCol, qtyCol, brrwCol;
    @FXML
    private ChoiceBox<String> sortCB;
    @FXML
    private ChoiceBox<Category> categoryCB;
    @FXML
    private Label bookQty, titleQty, borrowQty;

    private String[] sortType = {"A-Z", "Z-A"};
    private String[] reportOptions = {"Account", "Book", "Transaction"};
    private ObservableList<Book> retrieveBook = FXCollections.observableArrayList();

    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            //Set the Quanity in the account reports
            titleQty.setText(Integer.toString(fnc.countUniBkQuantity(globalVariable.bookList)));
            borrowQty.setText(Integer.toString(fnc.countBkBorrow(globalVariable.bookList)));
            bookQty.setText(Integer.toString(globalVariable.bookList.getSize()));

            ArrayList<Category> categories = globalVariable.categoryList;
            Category ctgryAll = new Category(000, "All");
            if (categories != null && !categories.isEmpty()) {
                categoryCB.getItems().add(ctgryAll);
                categoryCB.getItems().addAll(categories);
                categoryCB.setValue(ctgryAll);
            } else {
                System.out.println("No categories retrieved.");
            }

            // Set up TableView columns
            titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
            authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
            ctgryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
            isbnCol.setCellValueFactory(new PropertyValueFactory<>("ISBN"));
            qtyCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
            brrwCol.setCellValueFactory(new PropertyValueFactory<>("borrowed"));

            categoryCB.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    if (newValue.getName().equals("All")) {
                        retrieveBook = fnc.retrieveBook(bookList);
                        bookTableView.setItems(retrieveBook);
                    }else {
                        DoublyLinkList bookCtgry = fnc.selectCategoryBooks(newValue);
                        filterTable(bookCtgry);
                    }
                }
            });

            reportbox.getItems().addAll(reportOptions);
            reportbox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    switch (newValue) {
                        case "Account":
                            loadFXMLForChoice("/stages/admin/adminFXML/reports/admin_acc_reports.fxml");
                            break;
                        case "Book":
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
    
            sortCB.getItems().addAll(sortType);
            sortCB.setValue(sortType[0]);
            sortCB.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                applySorting();
            });

            if (bookList != null) {
                retrieveBook = fnc.retrieveBook(bookList);
                applySorting();
            }

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "An error occurred: " + e.getMessage());
            alert.setTitle("Reports - Book Initialization Error");
            alert.showAndWait();
            e.printStackTrace();
        }
    }

    private void applySorting() {
        if(retrieveBook!=null) {
            if (sortCB.getValue().equals("A-Z")) {
                retrieveBook.sort((t1, t2) -> t1.getTitle().compareToIgnoreCase(t2.getTitle()));
            } else {
                retrieveBook.sort((t1, t2) -> t2.getTitle().compareToIgnoreCase(t1.getTitle()));
            }
            bookTableView.refresh();
            bookTableView.setItems(retrieveBook);
        }
    }

    private void filterTable(DoublyLinkList bookListCtgry) {
        retrieveBook = fnc.retrieveBook(bookListCtgry);
        bookTableView.setItems(retrieveBook);
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
