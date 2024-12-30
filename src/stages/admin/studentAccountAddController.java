package stages.admin;

import Entity.Student;
import Function.Function;
import Function.dbFunction;
import Function.globalVariable;
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
import java.util.ResourceBundle;

import static Function.globalVariable.sortedStudentListASC;


public class studentAccountAddController implements Initializable {

    @FXML
    private Label acctStaffBtn;

    @FXML
    private Label acctStudentBtn;
    @FXML
    private Label studentQty;

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
    private TextField schoolIDField, sectionIDField, confirmPassField, fNameField, lNameField, emailField,  passwordField;

    @FXML
    private TableView<Student> studentTableView;
    @FXML
    private TableColumn<Student, String> schoolIDCol,firstNameCol, lastNameCol, sectionCol, emailCol;

    @FXML
    private Label lblError;

    Student searchStudent;
    dbFunction dbFunc = new dbFunction();
    Function fnc = new Function();

    @FXML
    private ChoiceBox<String> sortCB;

    private String[] sortType = {"A-Z", "Z-A"};
    private ObservableList<Student> retrieveStudentlist = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize sort options
        sortCB.getItems().addAll(sortType);
        sortCB.setValue(sortType[0]);

        // Set up TableView columns
        schoolIDCol.setCellValueFactory(new PropertyValueFactory<>("schoolID"));
        firstNameCol.setCellValueFactory(new PropertyValueFactory<>("fName"));
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lName"));
        sectionCol.setCellValueFactory(new PropertyValueFactory<>("section"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));

        if (!sortedStudentListASC.isEmpty()) {
            retrieveStudentlist = fnc.retrieveStudent(globalVariable.sortedStudentListASC);
            studentTableView.setItems(retrieveStudentlist);
            studentTableView.refresh();
        }
    }

    private void refreshTable() {
        if (!sortedStudentListASC.isEmpty()) {
            retrieveStudentlist = fnc.retrieveStudent(globalVariable.sortedStudentListASC);
            studentTableView.setItems(retrieveStudentlist);
            studentTableView.refresh();
        }
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void goDashboard(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/stages/admin/adminFXML/admin_dashboard.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void goAcctStaff(MouseEvent event) throws IOException {
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
    private void goReports(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/stages/admin/adminFXML/reports/admin_acc_reports.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void returnAcctStudent(MouseEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/stages/admin/adminFXML/students/admin_acctStudents.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void doAddStudent(ActionEvent event) {
        String schoolID = schoolIDField.getText();
        String fName = fNameField.getText();
        String lName = lNameField.getText();
        String email = emailField.getText();
        String section = sectionIDField.getText();
        String pass = passwordField.getText();
        String confirmPass = confirmPassField.getText();
        int studentID = 0;

        if (schoolID.isEmpty()) {
            lblError.setText("Student ID is missing");
            return;
        }
        if (fnc.retrieveStudentID(schoolID) != null) {
            studentID = Integer.parseInt(fnc.retrieveStudentID(schoolID));
        } else {
            lblError.setText("Incorrect ID format (NNNN-NNNNN)");
            return;
        }
        if(section.isEmpty()) {
            lblError.setText("Section is missing");
            return;
        }

        if (fName.isEmpty()) {
            lblError.setText("First name is missing");
            return;
        } else if (lName.isEmpty()) {
            lblError.setText("Last name is missing");
            return;
        } else if (email.isEmpty()) {
            lblError.setText("Email is missing");
            return;
        } else if (pass.isEmpty()) {
            lblError.setText("Password is missing");
            return;
        } else if (confirmPass.isEmpty()) {
            lblError.setText("Confirm password is missing");
            return;
        }

        if(fnc.emailChecker(email)==false) {
            lblError.setText("Wrong email format! e.g.(john.doe@example.com)");
            return;
        }
        if (fnc.passwordChecker(pass) == false) {
            lblError.setText("Password is atleast 8 characters with\nletter and number or special character");
            return;
        }
        if (pass.equals(confirmPass) == false) {
            lblError.setText("Password does not match");
            return;
        }

        Student newStudent = new Student(studentID, fName, lName, section, email, pass);
        studentID = dbFunc.insertStudentDB(newStudent);

        if (studentID != 0) {
            sortedStudentListASC.add(newStudent);
            refreshTable();
            schoolIDField.setText(null);
            fNameField.setText(null);
            lNameField.setText(null);
            emailField.setText(null);
            sectionIDField.setText(null);
            passwordField.setText(null);
            confirmPassField.setText(null);

            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Student registered successfully");
            alert.setTitle("Student Registration");
            alert.showAndWait();
        } else {
            lblError.setText("Student Registration Fail");
        }

    }

}
