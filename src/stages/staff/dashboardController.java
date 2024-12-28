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

import static Function.globalVariable.fnc;
import static Function.globalVariable.sortedStudentListASC;

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
    private ObservableList<Student> studentList = FXCollections.observableArrayList();
    private ObservableList<Book> bookList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //For student sort Funciton
        studentSortCB.getItems().addAll(sortType);
        studentSortCB.setValue(sortType[0]);
        studentSortCB.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            sortStudentData(); // Call sorting method when the value changes
        });
        //For book sort Function
        bookSortCB.getItems().addAll(sortType);
        bookSortCB.setValue(sortType[0]);
        bookSortCB.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            sortBookData(); // Call sorting method when the value changes
        });

        //Set total quantity
        bookQty.setText(Integer.toString(fnc.countBkQuantity(globalVariable.bookList)));
        studentQty.setText(Integer.toString(sortedStudentListASC.size()));

        schoolIDCol.setCellValueFactory(new PropertyValueFactory<>("schoolID"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        sectionCol.setCellValueFactory(new PropertyValueFactory<>("section"));

        bkTitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        bkIsbnCol.setCellValueFactory(new PropertyValueFactory<>("ISBN"));
        bkAuthorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
        bkQtyCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        if(!studentList.isEmpty()) {
            studentList = fnc.retrieveStudent(globalVariable.sortedStudentListASC);
            studentList.sort((t1, t2) -> t1.getlName().compareToIgnoreCase(t2.getlName()));
            studentTableView.setItems(studentList);
        }

        if(!bookList.isEmpty()) {
            bookList = fnc.retrieveBook(globalVariable.bookList);
            bookList.sort((t1, t2) -> t1.getTitle().compareToIgnoreCase(t2.getTitle()));
            bookTableView.setItems(bookList);
        }
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void sortStudentData() {
        if (studentList != null && !studentList.isEmpty()) {
            if (studentSortCB.getValue().equals("A-Z")) {
                studentList.sort((t1, t2) -> t1.getlName().compareToIgnoreCase(t2.getlName()));
            } else if (studentSortCB.getValue().equals("Z-A")) {
                studentList.sort((t1, t2) -> t2.getlName().compareToIgnoreCase(t1.getlName()));
            }
            studentTableView.refresh();
        }
    }

    private void sortBookData() {
        if (bookList != null && !bookList.isEmpty()) {
            if (bookSortCB.getValue().equals("A-Z")) {
                bookList.sort((t1, t2) -> t1.getTitle().compareToIgnoreCase(t2.getTitle()));
            } else if (bookSortCB.getValue().equals("Z-A")) {
                bookList.sort((t1, t2) -> t2.getTitle().compareToIgnoreCase(t1.getTitle()));
            }
            bookTableView.refresh();
        }
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
