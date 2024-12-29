package stages.staff;

import Entity.Book;
import Entity.Student;
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
import java.util.ResourceBundle;

import static Function.globalVariable.*;
import static Function.globalVariable.bookList;

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

    //Student Table
    @FXML
    private TableView<Student> studentTableView;
    @FXML
    private TableColumn<Student, String> schoolIDCol, nameCol, sectionCol;


    //Book Table
    @FXML
    private TableView<Book> bookTableView;
    @FXML
    private TableColumn<Book, String> bkIsbnCol, bkTitleCol, bkAuthorCol, bkQtyCol;
    @FXML
    private ChoiceBox<String> studentSortCB, bookSortCB;
    @FXML
    private Label bookQty, studentQty;

    private String[] sortType = {"A-Z", "Z-A"};
    private ObservableList<Student> StudentList = FXCollections.observableArrayList();
    private ObservableList<Book> BookList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // For student sort Function
        studentSortCB.getItems().addAll(sortType);
        studentSortCB.setValue(sortType[0]);

        // For book sort Function
        bookSortCB.getItems().addAll(sortType);
        bookSortCB.setValue(sortType[0]);

        // Set total quantity
        bookQty.setText(Integer.toString(globalVariable.fnc.countBkQuantity(bookList)));
        studentQty.setText(Integer.toString(globalVariable.sortedStudentListASC.size()));

        schoolIDCol.setCellValueFactory(new PropertyValueFactory<>("schoolID"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        sectionCol.setCellValueFactory(new PropertyValueFactory<>("section"));

        bkTitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        bkIsbnCol.setCellValueFactory(new PropertyValueFactory<>("ISBN"));
        bkAuthorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
        bkQtyCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        if (studentList != null) {
            StudentList = fnc.retrieveStudent(globalVariable.sortedStudentListASC);
        } else {
            showErrorAlert("Error", "Student list is empty or not initialized.");
        }

        if (bookList != null) {
            BookList = fnc.retrieveBook(bookList);
        } else {
            showErrorAlert("Error", "Book list is empty or not initialized.");
        }

        studentTableView.setItems(StudentList);
        bookTableView.setItems(BookList);

        // Add sorting functionality to ChoiceBox
        studentSortCB.setOnAction(e -> {
            String sortOption = studentSortCB.getValue();
            if ("A-Z".equals(sortOption)) {
                StudentList.sort((s1, s2) -> s1.getFullName().compareToIgnoreCase(s2.getFullName()));
            } else if ("Z-A".equals(sortOption)) {
                StudentList.sort((s1, s2) -> s2.getFullName().compareToIgnoreCase(s1.getFullName()));
            }
        });

        bookSortCB.setOnAction(e -> {
            String sortOption = bookSortCB.getValue();
            if ("A-Z".equals(sortOption)) {
                BookList.sort((b1, b2) -> b1.getTitle().compareToIgnoreCase(b2.getTitle()));
            } else if ("Z-A".equals(sortOption)) {
                BookList.sort((b1, b2) -> b2.getTitle().compareToIgnoreCase(b1.getTitle()));
            }
        });
    }


    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
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
        stage.show();    }

    @FXML
    void goReports(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/stages/staff/staffFXML/reports/staff_reports.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
