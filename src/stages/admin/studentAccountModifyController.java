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

public class studentAccountModifyController implements Initializable {

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
    private TextField searchField;
    @FXML
    private RadioButton emailRB, idRB;
    @FXML
    private Button saveBttn;

    @FXML
    private TableView<Student> studentTableView;
    @FXML
    private TableColumn<Student, String> schoolIDCol,firstNameCol, lastNameCol, sectionCol, emailCol;

    @FXML
    private Label lblError, lblError2;

    Student searchStudent;
    dbFunction dbFunc = new dbFunction();
    Function fnc = new Function();

    @FXML
    private ChoiceBox<String> sortCB;

    private String[] sortType = {"A-Z", "Z-A"};
    private ObservableList<Student> retrieveStudent = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize sort options
        sortCB.getItems().addAll(sortType);
        sortCB.setValue(sortType[0]);
        sortCB.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            applySorting(fnc.retrieveStudent(sortedStudentListASC));
        });

        if(globalVariable.searchStudent!=null) {
            searchStudent=globalVariable.searchStudent;
            loadSearch(searchStudent);
        }

        // Set up TableView columns
        schoolIDCol.setCellValueFactory(new PropertyValueFactory<>("schoolID"));
        firstNameCol.setCellValueFactory(new PropertyValueFactory<>("fName"));
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lName"));
        sectionCol.setCellValueFactory(new PropertyValueFactory<>("section"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));

        if (!sortedStudentListASC.isEmpty()) {
            retrieveStudent = fnc.retrieveStudent(globalVariable.sortedStudentListASC);
            studentTableView.setItems(retrieveStudent);
            studentTableView.refresh();
        }
    }

    private void applySorting(ObservableList<Student> studentList) {
        if (sortCB.getValue().equals("A-Z")) {
            globalVariable.sortedStudentListASC.clear();
            studentList.sorted((s1, s2) -> s1.getFName().compareToIgnoreCase(s2.getFName()))
                    .forEach(globalVariable.sortedStudentListASC::add);
            studentTableView.setItems(FXCollections.observableArrayList(globalVariable.sortedStudentListASC));
        } else {
            sortedStudentListASC.clear();
            studentList.sorted((s1, s2) -> s2.getFName().compareToIgnoreCase(s1.getFName()))
                    .forEach(sortedStudentListASC::add);
            studentTableView.setItems(FXCollections.observableArrayList(sortedStudentListASC));
        }
    }

    private void refreshTable() {
        retrieveStudent = fnc.retrieveStudent(globalVariable.sortedStudentListASC);
        applySorting(retrieveStudent);
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

    private void loadSearch(Student student) {
        if(searchStudent==null) {
            lblError.setText("Student not found"); return;
        }else {
            schoolIDField.setText(Integer.toString(searchStudent.getSchoolID()));
            sectionIDField.setText(searchStudent.getSection());
            confirmPassField.setText(searchStudent.getPass());
            fNameField.setText(searchStudent.getFName());
            lNameField.setText(searchStudent.getLName());
            emailField.setText(searchStudent.getEmail());
            passwordField.setText(searchStudent.getPass());

            schoolIDField.setDisable(false);
            sectionIDField.setDisable(false);
            confirmPassField.setDisable(false);
            fNameField.setDisable(false);
            lNameField.setDisable(false);
            emailField.setDisable(false);
            passwordField.setDisable(false);
            saveBttn.setDisable(false);
            globalVariable.searchStudent = null;
        }
    }

    @FXML
    private void doSearch(MouseEvent event) {
        String selected = "Email";
        String searchFld;

        if(emailRB.isSelected()) { //find which button selected
            selected = "Email";
        }else {
            selected = "ID";
        }

        if(searchField.getText().isEmpty()) {  //if searchfield is empty
            lblError.setText("Search text is blank"); return;
        }
        searchFld = searchField.getText();

        if(selected.equals("ID")) {
            searchStudent = globalVariable.fnc.findStudentID(sortedStudentListASC, searchFld);
        }else {
            searchStudent = globalVariable.fnc.findStudentEmail(sortedStudentListASC, searchFld);
        }

        if(searchStudent==null) {
            lblError.setText("Student not found"); return;
        }else {
            schoolIDField.setText(Integer.toString(searchStudent.getSchoolID()));
            sectionIDField.setText(searchStudent.getSection());
            confirmPassField.setText(searchStudent.getPass());
            fNameField.setText(searchStudent.getFName());
            lNameField.setText(searchStudent.getLName());
            emailField.setText(searchStudent.getEmail());
            passwordField.setText(searchStudent.getPass());

            schoolIDField.setDisable(false);
            sectionIDField.setDisable(false);
            confirmPassField.setDisable(false);
            fNameField.setDisable(false);
            lNameField.setDisable(false);
            emailField.setDisable(false);
            passwordField.setDisable(false);
            saveBttn.setDisable(false);
        }
    }

    @FXML
    private void doModifyStudent(ActionEvent event) {
        try {
            String schoolID = schoolIDField.getText();
            String section = sectionIDField.getText();
            String fName = fNameField.getText();
            String lName = lNameField.getText();
            String email = emailField.getText();
            String pass = passwordField.getText();
            String confirmPass = confirmPassField.getText();

            if (schoolID == null || schoolID.trim().isEmpty()) {
                lblError2.setText("School ID is empty");
                return;
            } else if (section == null || section.trim().isEmpty()) {
                lblError2.setText("Section is empty");
                return;
            } else if (fName == null || fName.trim().isEmpty()) {
                lblError2.setText("First name is empty");
                return;
            } else if (lName == null || lName.trim().isEmpty()) {
                lblError2.setText("Last name is empty");
                return;
            } else if (email == null || email.trim().isEmpty()) {
                lblError2.setText("Email is empty");
                return;
            } else if (pass == null || pass.trim().isEmpty()) {
                lblError2.setText("Password is empty");
                return;
            } else if (confirmPass == null || confirmPass.trim().isEmpty()) {
                lblError2.setText("Confirm password is missing");
                return;
            } else if (!fnc.emailChecker(email)) {
                lblError2.setText("Wrong email format! e.g.(john.doe@example.com)");
                return;
            } else if (!fnc.passwordChecker(pass)) {
                lblError2.setText("Password is atleast 8 characters" +
                        "\n with letter and number or special character");
            } else if (!pass.equals(confirmPass)) {
                lblError2.setText("Password does not match");
                return;
            }
            int schoolIDInt = 0;
            if(fnc.retrieveStudentID(schoolID)!=null) {
                schoolIDInt = Integer.parseInt(fnc.retrieveStudentID(schoolID));
            }

            if(schoolIDInt!=0) {
                Student modifiedStudent = new Student(schoolIDInt, fName, lName, section, email, pass);
                boolean isModify = dbFunc.modifyStudentDB(modifiedStudent, schoolIDInt);
                if (isModify == true) {
                    sortedStudentListASC.remove(searchStudent);
                    sortedStudentListASC.add(modifiedStudent);
                    Alert error = new Alert(Alert.AlertType.NONE, "Student account updated successfully", ButtonType.OK);
                    error.setTitle("Student Account Update");
                    error.showAndWait();

                    schoolIDField.setText(null);
                    sectionIDField.setText(null);
                    fNameField.setText(null);
                    lNameField.setText(null);
                    emailField.setText(null);
                    passwordField.setText(null);
                    confirmPassField.setText(null);
                    schoolIDField.setDisable(true);
                    sectionIDField.setDisable(true);
                    confirmPassField.setDisable(true);
                    fNameField.setDisable(true);
                    lNameField.setDisable(true);
                    emailField.setDisable(true);
                    passwordField.setDisable(true);
                    saveBttn.setDisable(true);
                    refreshTable();
                }
            }else {
                lblError2.setText("Invalid ID format e.g (NNNN-NNNNN)");
            }
        }catch(Exception e) {
            fnc.showAlert("Modify Student Error", e.getMessage());
            e.printStackTrace();
        }
    }

}
