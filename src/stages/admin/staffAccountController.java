package stages.admin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import javax.naming.spi.InitialContextFactory;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class staffAccountController implements Initializable {


    @FXML
    private Label acctStaffBtn;

    @FXML
    private Label acctStudentBtn;

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
    private Label staffQty;
    @FXML
    private Label studentQty;
    @FXML
    private ChoiceBox<String> sortCB;

    private String[] sortType = {"A-Z", "Z-A"};

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sortCB.getItems().addAll(sortType);
        sortCB.setValue(sortType[0]);


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

    @FXML
    private void goReports(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/stages/admin/adminFXML/reports/admin_acc_reports.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void goAcctStudent(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/stages/admin/adminFXML/students/admin_acctStudents.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    //doInsert para sa staff
    @FXML
    private void doAdd(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/stages/admin/adminFXML/staff/admin_acctStaffsAdd.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
    //doModify para sa staff
    @FXML
    private void doModify(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/stages/admin/adminFXML/staff/admin_acctStaffsModify.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
     }

    @FXML
    private void doRemove(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/stages/admin/adminFXML/staff/admin_acctStaffsDelete.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void returnAcctStaff(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/stages/admin/adminFXML/staff/admin_acctStaffs.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    public void addStaff(ActionEvent actionEvent) {

    }

    @FXML
    public void editStaff(ActionEvent actionEvent) {

    }

    @FXML
    public void deleteStaff(ActionEvent actionEvent) {

    }

    @FXML
    private void doSearch(ActionEvent event) {

    }


}
