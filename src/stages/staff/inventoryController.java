package stages.staff;

import Entity.Book;
import LinkedList.DoublyLinkList;
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
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import Function.*;
import stages.admin.bkManageController;

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
    private Label bookBrrwQty;
    @FXML
    private Label bookQty;
    @FXML
    private Label bookUniqueQty;

    @FXML
    private ChoiceBox<String> sortCB;
    @FXML
    private ChoiceBox<String> changeViewCB;
    @FXML
    private VBox libraryBox;
    @FXML
    private HBox sortBox;
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
    Function fnc = new Function();
    private ObservableList<Book> retrieveBook = FXCollections.observableArrayList();
    private final String[] sortType = {"A-Z", "Z-A"};
    private final String[] changeViewType = {"Table View", "Library View"};


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

        titleCol.setCellValueFactory(new PropertyValueFactory<Book, String>("title"));
        authorCol.setCellValueFactory(new PropertyValueFactory<Book, String>("author"));
        isbnCol.setCellValueFactory(new PropertyValueFactory<Book, String>("ISBN"));
        categoryCol.setCellValueFactory(new PropertyValueFactory<Book, String>("category"));
        qtyCol.setCellValueFactory(new PropertyValueFactory<Book, String>("quantity"));

        retrieveBook = fnc.retrieveBook(globalVariable.bookList);
        changeViewCB.getItems().addAll(changeViewType);
        if (globalVariable.isLibraryView == true) {
            changeViewCB.setValue(changeViewType[1]);
            refreshLibraryView();
        } else {
            changeViewCB.setValue(changeViewType[0]);
            refreshTableView();
        }
        changeViewCB.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            refreshTable();
        });

    }

    private void sortBookData() {
        if(retrieveBook!=null) {
            if (sortCB.getValue().equals("A-Z")) {
                retrieveBook.sort((t1, t2) -> t1.getTitle().compareToIgnoreCase(t2.getTitle()));
            } else if (sortCB.getValue().equals("Z-A")) {
                retrieveBook.sort((t1, t2) -> t2.getTitle().compareToIgnoreCase(t1.getTitle()));
            }
            BookTableView.refresh();
            BookTableView.setItems(retrieveBook);
        }
    }

    public void refreshTable() {
        if (changeViewCB.getValue().equals("Table View")) {
            refreshTableView();
        } else if (changeViewCB.getValue().equals("Library View")) {
            refreshLibraryView();
        }
    }

    public void refreshTableView() {
        ObservableList<Book> bookList = fnc.inventoryBookView();
        BookTableView.setItems(bookList);
        libraryBox.getChildren().clear();
        libraryBox.getChildren().addAll(sortBox, BookTableView);
    }

    public void refreshLibraryView() {
        try {
            libraryBox.getChildren().clear();
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(bkManageController.class.getResource("/stages/admin/library/libraryView.fxml"));
            VBox libraryView = fxmlLoader.load();
            libraryBox.getChildren().add(libraryView);
        } catch (IOException e) {
            Alert error = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            error.setTitle("Refresh Error");
            error.showAndWait();
        } catch (Exception e) {
            Alert error = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            error.setTitle("Refresh Error");
            error.showAndWait();
        }
    }

    //SWITCHING MENU
    @FXML
    private void goDashboard(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/stages/staff/staffFXML/staff_dashboard.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void goBorrowTransact(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/stages/staff/staffFXML/transact/staff_brrowtrans.fxml"));
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

        if (alert.showAndWait().get() == ButtonType.OK) {
            System.out.println("You successfully logged out!");
            Parent root = FXMLLoader.load(getClass().getResource("/stages/login/logFXML/login_view.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        }
    }

    @FXML
    private void goManageBooks(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/stages/staff/staffFXML/staff_managebooks.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void goProfileAdmin(MouseEvent event) {

    }

    @FXML
    private void goReports(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/stages/staff/staffFXML/reports/staff_reports.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    //PROCEED TO SUB MENU
    @FXML
    private void doInsert(ActionEvent event) throws IOException {
        if (changeViewCB.getValue().equals("Library View")) {
            globalVariable.isLibraryView = true;
        } else {
            globalVariable.isLibraryView = false;
        }
        Parent root = FXMLLoader.load(getClass().getResource("/stages/staff/staffFXML/inventory/staff_inventoryInsert.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }


    public void doModify(ActionEvent event) throws IOException {
        if (changeViewCB.getValue().equals("Library View")) {
            globalVariable.isLibraryView = true;
        } else {
            globalVariable.isLibraryView = false;
        }
        Parent root = FXMLLoader.load(getClass().getResource("/stages/staff/staffFXML/inventory/staff_inventoryModify.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void doRemove(ActionEvent event) throws IOException {
        if (changeViewCB.getValue().equals("Library View")) {
            globalVariable.isLibraryView = true;
        } else {
            globalVariable.isLibraryView = false;
        }
        Parent root = FXMLLoader.load(getClass().getResource("/stages/staff/staffFXML/inventory/staff_inventoryDelete.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
