package stages.staff;

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
    private TableView<Student> studentTableView;

    @FXML
    private TableColumn<Student, String> schoolIDCol,firstNameCol, lastNameCol, sectionCol, emailCol, penaltyCol;

    @FXML
    private ChoiceBox<String> sortCB;

    private String[] sortType = {"A-Z", "Z-A"};
    ObservableList<Student> studentList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize sort options
        sortCB.getItems().addAll(sortType);
        sortCB.setValue(sortType[0]);

        // Set up TableView columns
        schoolIDCol.setCellValueFactory(new PropertyValueFactory<>("schoolID"));
        firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        sectionCol.setCellValueFactory(new PropertyValueFactory<>("section"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));

        // Load data into TableView
        ObservableList<Student> studentList = FXCollections.observableArrayList(globalVariable.studentList);

        if (studentList.isEmpty()) {
            showErrorAlert("Error", "Staff list is empty or not initialized.");
        }

        studentTableView.setItems(studentList);

        // Add sorting functionality
        sortCB.setOnAction(event -> applySorting(studentList));
    }

    private void applySorting(ObservableList<Student> staffList) {
        if (sortCB.getValue().equals("A-Z")) {
            globalVariable.sortedStudentListASC.clear();
            staffList.sorted((s1, s2) -> s1.getfName().compareToIgnoreCase(s2.getfName()))
                    .forEach(globalVariable.sortedStudentListASC::add);
            studentTableView.setItems(FXCollections.observableArrayList(globalVariable.sortedStudentListASC));
        } else {
            globalVariable.sortedStaffListDESC.clear();
            staffList.sorted((s1, s2) -> s2.getfName().compareToIgnoreCase(s1.getfName()))
                    .forEach(globalVariable.sortedStudentListDESC::add);
            studentTableView.setItems(FXCollections.observableArrayList(globalVariable.sortedStudentListDESC));
        }
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
