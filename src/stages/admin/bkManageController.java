package stages.admin;

import Entity.Book;
import Entity.Category;
import LinkedList.DoublyLinkList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import stages.admin.library.BookSelectionService;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import Function.*;
import stages.admin.library.libraryController;

public class bkManageController implements Initializable {

    @FXML
    private VBox libraryBox;

    @FXML
    private TextField bkTitleField;

    @FXML
    private TextField bkAuthorField;

    @FXML
    private TextField bkISBNField;

    @FXML
    private TextField bkCtgryField;

    @FXML
    private TextField bkQtyField;

    @FXML
    private ImageView bkImage;

    @FXML private TextField searchField;
    @FXML private Label lblError;

    // Reference to the libraryController
    private Book searchBook;
    private DoublyLinkList bookList;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            bookList = globalVariable.bookList;

            // Load the library view and get the controller instance
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(bkManageController.class.getResource("/stages/admin/library/libraryView.fxml"));
            VBox libraryView = fxmlLoader.load();
            libraryBox.getChildren().add(libraryView);

            BookSelectionService.getInstance().selectedBookProperty().addListener((observable, oldBook, newBook) -> {
                if (newBook != null) {
                    // Call setBookData to update the fields with the selected book's data
                    setBookData(newBook);
                }
            });

        } catch (IOException e) {
            Alert error = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            error.setTitle("Book Manage Page Error");
            error.showAndWait();
        }
    }

    @FXML
    private void doSearch(MouseEvent event) {
        String selected = "isbn";
        String searchFld;

        if(searchField==null || searchField.getText().isEmpty()) {  //if searchfield is empty
            lblError.setText("Search text is blank"); return;
        }
        searchFld = searchField.getText();
        searchBook = bookList.findTitle(searchFld);

        if(searchBook==null) {
            lblError.setText("Found no book"); return;
        }else {
            Image img = searchBook.getImageSrc();
            bkImage.setImage(img);
            bkTitleField.setText(searchBook.getTitle());
            bkAuthorField.setText(searchBook.getAuthor());
            bkISBNField.setText(searchBook.getISBN());
            bkCtgryField.setText(searchBook.getCategory());
            bkQtyField.setText(Integer.toString(searchBook.getQuantity()));
        }
    }

    public void setBookData(Book book) {
        if (book != null) {
            if(bkQtyField == null) {
                lblError.setText("Book not available");
            }
            bkImage.setImage(book.getImageSrc());
            bkTitleField.setText(book.getTitle());
            bkAuthorField.setText(book.getAuthor());
            bkISBNField.setText(book.getISBN());
            bkCtgryField.setText(book.getCategory());
            bkQtyField.setText(Integer.toString(book.getQuantity()));
            searchBook = book;
        }
    }

    @FXML
    private void goDashboard(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/stages/admin/adminFXML/admin_dashboard.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    public void goAccountStaff(MouseEvent event) throws IOException {
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

//    @FXML
//    private void goManageBooks(MouseEvent event) throws IOException {
//        Parent root = FXMLLoader.load(getClass().getResource("/stages/admin/adminFXML/admin_bkManage.fxml"));
//        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//        stage.setScene(new Scene(root));
//        stage.show();
//    }

    @FXML
    private void goReports(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/stages/admin/adminFXML/reports/admin_acc_reports.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void doModify(ActionEvent event) throws IOException {
        if(bkTitleField==null || bkTitleField.getText()==null) {
            lblError.setText("No book selected");
        }
        globalVariable.modifyBook = searchBook;
        Parent root = FXMLLoader.load(getClass().getResource("/stages/admin/adminFXML/inventory/admin_inventoryModify.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

}
