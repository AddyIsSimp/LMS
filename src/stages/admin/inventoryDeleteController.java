package stages.admin;

import Entity.Book;
import Entity.Category;
import Function.globalVariable;
import LinkedList.DoublyLinkList;
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
    private ChoiceBox<String> sortCB;
    @FXML
    private ChoiceBox<Category> tfCategory;

    @FXML
    private ImageView bkImage;

    @FXML
    private Label lblError, lblError2;

    @FXML private Image newImage;
    @FXML private Button deleteBttn, changeImgBttn;
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
    DoublyLinkList bookList;
    ArrayList<Category> categories;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        bookList = globalVariable.bookList;
        categories = globalVariable.dbFnc.retrieveCategories();

        if (categories != null && !categories.isEmpty()) {
            tfCategory.getItems().addAll(categories);
            tfCategory.setValue(categories.get(0)); // Optional: Set a default value
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "No categories");
            alert.showAndWait();
        }

        //Insert book in table
        titleCol.setCellValueFactory(new PropertyValueFactory<Book, String>("title"));
        authorCol.setCellValueFactory(new PropertyValueFactory<Book, String>("author"));
        isbnCol.setCellValueFactory(new PropertyValueFactory<Book, String>("ISBN"));
        categoryCol.setCellValueFactory(new PropertyValueFactory<Book, String>("category"));
        qtyCol.setCellValueFactory(new PropertyValueFactory<Book, String>("quantity"));

        ObservableList<Book> bookList = fnc.inventoryBookView();
        System.out.println(bookList.size());
        BookTableView.setItems(bookList);

        sortCB.getItems().addAll("A-Z", "Z-A");
    }

    public void refreshTable() {
        titleCol.setCellValueFactory(new PropertyValueFactory<Book, String>("title"));
        authorCol.setCellValueFactory(new PropertyValueFactory<Book, String>("author"));
        isbnCol.setCellValueFactory(new PropertyValueFactory<Book, String>("ISBN"));
        categoryCol.setCellValueFactory(new PropertyValueFactory<Book, String>("category"));
        qtyCol.setCellValueFactory(new PropertyValueFactory<Book, String>("quantity"));

        ObservableList<Book> bookList = fnc.inventoryBookView();
        System.out.println(bookList.size());
        BookTableView.setItems(bookList);
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
        Parent root = FXMLLoader.load(getClass().getResource("/stages/admin/adminFXML/admin_reports.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    //METHODS HERE
    @FXML
    private void doSearch(MouseEvent event) {
        String selected = "isbn";
        String searchFld;

        if(isbnRB.isSelected()) { //find which button selected
            selected = "isbn";
        }else {
            selected = "title";
        }

        if(searchField==null || searchField.getText().isEmpty()) {  //if searchfield is empty
            lblError.setText("Search text is blank"); return;
        }
        searchFld = searchField.getText();

        //REtrieve the book data
        if(selected.equals("title")) {
            searchBook = bookList.findTitle(searchFld);
        }else {
            searchBook = bookList.findISBN(searchFld);
        }

        if(searchBook==null) {
            lblError.setText("Found no book"); return;
        }else {
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

        boolean deletedInDB = dbFnc.removeBookDB(bkTitle, bkISBN);
        bookList.deleteBook(searchBook.getTitle());

        searchField.setText(null);
        bkImage.setImage(null);
        titleField.setText(null);
        authorField.setText(null);
        isbnField.setText(null);
        tfCategory.setValue(categories.getFirst());
        qtyField.setText(null);
        refreshTable();
        deleteBttn.setDisable(true);
    }

}
