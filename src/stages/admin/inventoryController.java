package stages.admin;

import Entity.Book;
import LinkedList.DoublyLinkList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import Function.*;
import stages.admin.library.libraryController;

import static Function.globalVariable.dbFnc;

public class inventoryController implements Initializable {


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
    private VBox libraryBox;

    @FXML
    private Label bookBrrwQty;

    @FXML
    private Label bookQty;

    @FXML
    private Label bookUniqueQty;

    Function fnc = new Function();
    @FXML
    private TableView<Book> BookTableView;
    @FXML
    private TableColumn<Book, String> authorCol;
    @FXML
    private TableColumn<Book, String> categoryCol;
    @FXML
    private TableColumn<Book, String> isbnCol;
    @FXML
    private TableColumn<Book, String> qtyCol;
    @FXML
    private TableColumn<Book, String> titleCol;

    @FXML
    private ChoiceBox<String> changeViewCB;
    private ObservableList<Book> bookList = FXCollections.observableArrayList();

    private Group tableDisplay;

    //To be inserted in the Group tableDisplay
    @FXML
    private Label sortLabel;
    private final String[] sortType = {"A-Z", "Z-A"};
    private final String[] changeViewType = {"Table View", "Library View"};
    @FXML ChoiceBox<String> sortCB;
    //end of to be inserted

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        DoublyLinkList books = globalVariable.bookList;
        bookQty.setText(Integer.toString(fnc.countBkQuantity(books)));
        bookUniqueQty.setText(Integer.toString(fnc.countUniBkQuantity(books)));
        bookBrrwQty.setText(Integer.toString(fnc.countBkBorrow(books)));

        sortCB.getItems().addAll(sortType);
        sortCB.setValue(sortType[0]);
        sortCB.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            sortBookData();
        });

        changeViewCB.getItems().addAll(changeViewType);
        changeViewCB.setValue(changeViewType[0]);
        changeViewCB.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selectViewType();
        });

        titleCol.setCellValueFactory(new PropertyValueFactory<Book, String>("title"));
        authorCol.setCellValueFactory(new PropertyValueFactory<Book, String>("author"));
        isbnCol.setCellValueFactory(new PropertyValueFactory<Book, String>("ISBN"));
        categoryCol.setCellValueFactory(new PropertyValueFactory<Book, String>("category"));
        qtyCol.setCellValueFactory(new PropertyValueFactory<Book, String>("quantity"));

        //Code here to add the node into the tableDisplay

        if(!bookList.isEmpty()) {
            bookList = dbFnc.inventoryBookView();
            bookList.sort((t1, t2) -> t1.getTitle().compareToIgnoreCase(t2.getTitle()));
            BookTableView.setItems(bookList);
        }
    }

    private void sortBookData() {
        if (bookList != null && !bookList.isEmpty()) {
            if (sortCB.getValue().equals("A-Z")) {
                bookList.sort((t1, t2) -> t1.getTitle().compareToIgnoreCase(t2.getTitle()));
            } else if (sortCB.getValue().equals("Z-A")) {
                bookList.sort((t1, t2) -> t2.getTitle().compareToIgnoreCase(t1.getTitle()));
            }
            BookTableView.refresh();
        }
    }

    private void selectViewType() {
        try {
            if (bookList != null && !bookList.isEmpty()) {
                if (changeViewCB.getValue().equals("Library View")) {
                    libraryBox.getChildren().clear();
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(bkManageController.class.getResource("/stages/admin/library/libraryView.fxml"));
                    VBox libraryView = fxmlLoader.load();
                    libraryBox.getChildren().add(libraryView);
                } else if (changeViewCB.getValue().equals("Table View")) {
                    libraryBox.getChildren().clear();
                    libraryBox.getChildren().add(BookTableView);
                    bookList.sort((t1, t2) -> t1.getTitle().compareToIgnoreCase(t2.getTitle()));
                    BookTableView.refresh();
                }
            }
        }catch(IOException e) {
            Alert error = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            error.setTitle("Change View Error");
            error.showAndWait();
        }catch(Exception e) {
            Alert error = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            error.setTitle("Change View Error");
            error.showAndWait();
        }
    }



//SWITCHING MENU
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

//    @FXML
//    private void goInventory(MouseEvent event) throws IOException {
//        Parent root = FXMLLoader.load(getClass().getResource("/stages/admin/adminFXML/admin_inventory.fxml"));
//        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//        stage.setScene(new Scene(root));
//        stage.show();
//    }

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

    //PROCEED TO SUB MENU
    @FXML
    private void doInsert(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/stages/admin/adminFXML/inventory/admin_inventoryInsert.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void doModify(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/stages/admin/adminFXML/inventory/admin_inventoryModify.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void doRemove(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/stages/admin/adminFXML/inventory/admin_inventoryDelete.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

}
