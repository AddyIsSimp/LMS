package stages.admin;

import Entity.Book;
import Entity.Student;
import Entity.Transact;
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
import java.util.ResourceBundle;
import Function.*;

import static Function.globalVariable.*;
import static Function.globalVariable.fnc;

public class dashboardController implements Initializable {

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
    private Label bookQty, staffQty, studentQty;

    @FXML
    private TableView<Student> studentAccTableView;

    @FXML
    private TableView<Book> bookDetailsTableView;

    @FXML
    private TableColumn<Student, String> studentIDCol, firstNameCol, lastNameCol, sectionCol;

    @FXML
    private TableColumn<Book, String> isbnCol, titleCol, bkAuthorCol, bkqtyCol;

    @FXML
    private ChoiceBox<String> studentCB, bookCB;

    private final String[] sortType = {"A-Z", "Z-A"};
    private ObservableList<Student> retrieveStudentlist = FXCollections.observableArrayList();
    private ObservableList<Book> retrieveBooklist = FXCollections.observableArrayList();
    private Function fnc = new Function();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize sort options
        studentCB.getItems().addAll(sortType);
        studentCB.setValue(sortType[0]);

        bookCB.getItems().addAll(sortType);
        bookCB.setValue(sortType[0]);

        // Display quantities
        bookQty.setText(Integer.toString(globalVariable.fnc.countBkQuantity(bookList)));
        studentQty.setText(Integer.toString(globalVariable.sortedStudentListASC.size()));
        staffQty.setText(Integer.toString(globalVariable.sortedStaffListASC.size()));

        // Set up TableView columns for Students
        studentIDCol.setCellValueFactory(new PropertyValueFactory<>("schoolID"));
        firstNameCol.setCellValueFactory(new PropertyValueFactory<>("fName"));
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lName"));
        sectionCol.setCellValueFactory(new PropertyValueFactory<>("section"));

        // Set up TableView columns for Books
        isbnCol.setCellValueFactory(new PropertyValueFactory<>("ISBN"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        bkAuthorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
        bkqtyCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        // Retrieve lists
        if (sortedStudentListASC != null) {
            retrieveStudentlist = fnc.retrieveStudent(globalVariable.sortedStudentListASC);
        } else {
            showErrorAlert("Error", "Student list is empty or not initialized.");
        }

        if (bookList != null) {
            retrieveBooklist = fnc.retrieveBook(bookList);
        } else {
            showErrorAlert("Error", "Book list is empty or not initialized.");
        }

        // Set lists to TableView
        studentAccTableView.setItems(retrieveStudentlist);
        bookDetailsTableView.setItems(retrieveBooklist);

        // Sorting for students
        studentCB.setOnAction(e -> {
            String sortOption = studentCB.getValue();
            if ("A-Z".equals(sortOption)) {
                retrieveStudentlist.sort((s1, s2) -> s1.getFName().compareToIgnoreCase(s2.getFName()));
            } else if ("Z-A".equals(sortOption)) {
                retrieveStudentlist.sort((s1, s2) -> s2.getFName().compareToIgnoreCase(s1.getFName()));
            }
        });

        // Sorting for books
        bookCB.setOnAction(e -> {
            String sortOption = bookCB.getValue();
            if ("A-Z".equals(sortOption)) {
                retrieveBooklist.sort((b1, b2) -> b1.getTitle().compareToIgnoreCase(b2.getTitle()));
            } else if ("Z-A".equals(sortOption)) {
                retrieveBooklist.sort((b1, b2) -> b2.getTitle().compareToIgnoreCase(b1.getTitle()));
            }
        });
    }

    // Helper method for showing error alerts
    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }



    //SWAP MENU
    @FXML
    void goAccountStaff(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/stages/admin/adminFXML/staff/admin_acctStaffs.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    void goBorrowTransact(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/stages/admin/adminFXML/transact/admin_transact.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    void goInventory(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/stages/admin/adminFXML/inventory/admin_inventory.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    void goLogout(MouseEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("You're about to logout! Do you want to continue?");

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
        Parent root = FXMLLoader.load(getClass().getResource("/stages/admin/adminFXML/admin_bkManage.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    void goReports(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/stages/admin/adminFXML/reports/admin_acc_reports.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

}
