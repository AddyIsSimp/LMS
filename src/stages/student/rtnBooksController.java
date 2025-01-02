package stages.student;

import Entity.Transact;
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
import Function.globalVariable;
public class rtnBooksController implements Initializable {

    @FXML
    private HBox acctBtn;

    @FXML
    private HBox brrowBooks;

    @FXML
    private HBox borrowTransBtn;

    @FXML
    private HBox dashboardBtn;

    @FXML
    private HBox rtnBooks;

    @FXML
    private HBox logoutBtn;

    @FXML
    private HBox reportsBtn;

    @FXML
    private TableView<Transact> brrwTransTblView;
    @FXML
    private TableColumn<Transact, String> brrwDateCol, daysLeftCol, isbnCol, titleCol, statusCol;
//    @FXML
//    private TableColumn<Transact, ?> daysLeftCol;
//    @FXML
//    private TableColumn<?, ?> isbnCol;
//    @FXML
//    private TableColumn<?, ?> titleCol;

    @FXML
    private ChoiceBox<String> sortCB;
    private String[] sortType = {"A-Z", "Z-A"};
    private ObservableList<Transact> transacts = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize ChoiceBox for sorting
        sortCB.getItems().addAll(sortType);
        sortCB.setValue(sortType[0]);

        sortCB.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            sortTransactData(); // Call sorting method when the value changes
        });

        titleCol.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
        isbnCol.setCellValueFactory(new PropertyValueFactory<>("bkIsbn"));
        brrwDateCol.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));
        daysLeftCol.setCellValueFactory(new PropertyValueFactory<>("dayLeft"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        transacts = globalVariable.fnc.retrieveStudentTransact(globalVariable.transactList, globalVariable.loginStudent.getSchoolID());
        if (globalVariable.transactList != null) {

        } else {
            globalVariable.fnc.showAlert("Error", "Transaction list is empty or not initialized.");
            return;
        }

        brrwTransTblView.setItems(transacts);
    }

    private void sortTransactData() {
        if (transacts != null && !transacts.isEmpty()) {
            if (sortCB.getValue().equals("A-Z")) {
                transacts.sort((t1, t2) -> t1.getBookTitle().compareToIgnoreCase(t2.getBookTitle()));
            } else if (sortCB.getValue().equals("Z-A")) {
                transacts.sort((t1, t2) -> t2.getBookTitle().compareToIgnoreCase(t1.getBookTitle()));
            }
            brrwTransTblView.refresh();
        }
    }

    @FXML
    private void goDashboard(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/stages/student/studentFXML/student_dashboard.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
    @FXML
    void gobrrowBooks(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/stages/student/studentFXML/student_borrowBooks.fxml"));
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
    void gortnBooks(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/stages/student/studentFXML/student_returnBooks.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
