package stages.admin;

import Entity.Book;
import Entity.Category;
import Function.globalVariable;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static Function.globalVariable.dbFnc;
import static Function.globalVariable.fnc;

public class inventoryDeleteController implements Initializable {

    @FXML
    private TextField searchField, titleField, authorField, isbnField, qtyField;

    @FXML
    private ChoiceBox<Category> tfCategory;

    @FXML
    private ImageView bkImage;

    @FXML
    private Label lblError, lblError2;

    @FXML
    private Image newImage;
    @FXML
    private Button deleteBttn, changeImgBttn;
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
    private RadioButton titleRB, isbnRB;
    Book searchBook;
    ArrayList<Category> categories;

    @FXML
    private VBox libraryBox;
    @FXML
    private HBox sortBox;
    @FXML
    private ChoiceBox<String> sortCB;
    @FXML
    private ChoiceBox<String> changeViewCB;
    private ObservableList<Book> retrieveBook = FXCollections.observableArrayList();
    private final String[] sortType = {"A-Z", "Z-A"};
    private final String[] changeViewType = {"Table View", "Library View"};

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        categories = globalVariable.categoryList;

        if (categories != null && !categories.isEmpty()) {
            tfCategory.getItems().addAll(categories);
            tfCategory.setValue(categories.get(0)); // Optional: Set a default value
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "No categories");
            alert.showAndWait();
        }

        sortCB.getItems().addAll(sortType);
        sortCB.setValue(sortType[0]);
        sortCB.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            sortBookData();
        });

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
        titleCol.setCellValueFactory(new PropertyValueFactory<Book, String>("title"));
        authorCol.setCellValueFactory(new PropertyValueFactory<Book, String>("author"));
        isbnCol.setCellValueFactory(new PropertyValueFactory<Book, String>("ISBN"));
        categoryCol.setCellValueFactory(new PropertyValueFactory<Book, String>("category"));
        qtyCol.setCellValueFactory(new PropertyValueFactory<Book, String>("quantity"));

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
        if (changeViewCB.getValue().equals("Library View")) {
            globalVariable.isLibraryView = true;
        } else {
            globalVariable.isLibraryView = false;
        }
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

    //METHODS HERE
    @FXML
    private void doSearch(MouseEvent event) {
        String selected = "isbn";
        String searchFld;

        if (isbnRB.isSelected()) { //find which button selected
            selected = "isbn";
        } else {
            selected = "title";
        }

        if (searchField == null || searchField.getText().isEmpty()) {  //if searchfield is empty
            lblError.setText("Search text is blank");
            return;
        }
        searchFld = searchField.getText();

        //REtrieve the book data
        if (selected.equals("title")) {
            searchBook = globalVariable.bookList.findTitle(searchFld);
        } else {
            searchBook = globalVariable.bookList.findISBN(searchFld);
        }

        if (searchBook == null) {
            lblError.setText("Found no book");
            return;
        } else {
            Image img = searchBook.getImageSrc();
            bkImage.setImage(img);
            titleField.setText(searchBook.getTitle());
            authorField.setText(searchBook.getAuthor());
            isbnField.setText(searchBook.getISBN());
            tfCategory.setValue(categories.getFirst());
            qtyField.setText(Integer.toString(searchBook.getQuantity()));
            deleteBttn.setDisable(false);
        }
    }

    @FXML
    private void deleteBook(ActionEvent event) {
        //Upload the image to database
        String bkTitle = titleField.getText();
        String bkAuthor = authorField.getText();
        String bkISBN = isbnField.getText();
        Category ctgryObj = tfCategory.getSelectionModel().getSelectedItem();
        String category = ctgryObj.getName();
        int quantity = Integer.parseInt(qtyField.getText());

        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Delete Book", ButtonType.NO, ButtonType.YES);
        alert.setTitle("Delete Book");
        alert.setHeaderText("Do you really want to delete the book?");

        if(alert.showAndWait().get() == ButtonType.YES) {
            boolean deletedInDB = dbFnc.removeBookDB(bkTitle, bkISBN);
            globalVariable.bookList.deleteBook(searchBook.getTitle());

            searchField.setText(null);
            bkImage.setImage(null);
            titleField.setText(null);
            authorField.setText(null);
            isbnField.setText(null);
            tfCategory.setValue(categories.getFirst());
            qtyField.setText(null);
            refreshTable();
            deleteBttn.setDisable(true);
        }else {
            searchField.setText(null);
            bkImage.setImage(null);
            titleField.setText(null);
            authorField.setText(null);
            isbnField.setText(null);
            tfCategory.setValue(categories.getFirst());
            qtyField.setText(null);
            refreshTable();
            deleteBttn.setDisable(true);
            alert.close();
        }
    }

}
