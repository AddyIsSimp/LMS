package stages.student;

import Entity.Book;
import Entity.Student;
import Entity.Transact;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import stages.admin.bkManageController;
import stages.admin.library.BookSelectionService;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import Function.*;
import Function.globalVariable;

import static Function.globalVariable.bookList;

public class brrowBooksController implements Initializable {

    PreparedStatement pstmt;
    ResultSet rs;
    Function fnc = new Function();
    dbFunction dbFunc = new dbFunction();

    @FXML
    private TextField searchField;
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

    private Book searchBook;
    private Student studentLogin;
    private Connection conn;

    @FXML
    private Label lblError;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            studentLogin = globalVariable.loginStudent;
            // Load the library view
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(bkManageController.class.getResource("/stages/admin/library/libraryView.fxml"));
            VBox libraryView = fxmlLoader.load();
            libraryBox.getChildren().add(libraryView);

            BookSelectionService.getInstance().selectedBookProperty().addListener((observable, oldBook, newBook) -> {
                if (newBook != null) {
                    setBookData(newBook);
                }
            });

        } catch (Exception e) {
            Alert error = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            error.showAndWait();
        }
    }

    public void setBookData(Book book) {
        if (book != null) {
            bkImage.setImage(book.getImageSrc());
            bkTitleField.setText(book.getTitle());
            bkAuthorField.setText(book.getAuthor());
            bkISBNField.setText(book.getISBN());
            bkCtgryField.setText(book.getCategory());
            int quantity = book.getQuantity();
            if (quantity == 0) { // If quantity is 0
                bkQtyField.setText("Book not available");
            } else {
                bkQtyField.setText(Integer.toString(quantity)); // Show quantity
            }
        }
    }

    @FXML
    private void goBorrowBook(ActionEvent event) {
        try {
            if (bkTitleField.getText() == null) {
                Alert error = new Alert(Alert.AlertType.NONE, "No selected book", ButtonType.OK);
                error.setTitle("Borrow Book");
                error.show();
                return;
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Borrow Book Request", ButtonType.NO, ButtonType.YES);
            alert.setTitle("Borrow Book");
            alert.setHeaderText("Confirm Borrow Book Request");
            alert.setContentText("Borrower Info\nBorrower ID:  " + studentLogin.getSchoolID() +
                    "\nBorrower Name:  " + studentLogin.getFName() +
                    "\n\nBook Info \nTitle:  " + bkTitleField.getText() +
                    "\nISBN:  " + bkISBNField.getText()
            );

            if (alert.showAndWait().get() == ButtonType.YES) {
                conn = dbFunc.connectToDB();
                int transactID = dbFunc.resetAutoIncrement(conn, "transact", "trans_id");

                String bktitle = bkTitleField.getText();
                String bookISBN = bkISBNField.getText();

                Transact newTransact = new Transact(transactID, bktitle, bookISBN, studentLogin.getSchoolID(), studentLogin.getLName(), "PENDING");
                globalVariable.dbFnc.insertPendingTransact(newTransact);
                globalVariable.transactList.add(newTransact);

                alert = new Alert(Alert.AlertType.INFORMATION, "Book Borrow Pending. Kindly proceed to the library within this week.", ButtonType.OK);
                alert.setTitle("Book Borrowing");
                alert.show();
            }
        }catch(Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.setTitle("Book Borrow Request Error");
            alert.show();
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

    @FXML
    void gortnBooks(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/stages/student/studentFXML/student_returnBooks.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();    }

//    @FXML
//    void goReports(MouseEvent event) throws IOException {
//        Parent root = FXMLLoader.load(getClass().getResource("/stages/admin/adminFXML/admin_reports.fxml"));
//        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//        stage.setScene(new Scene(root));
//        stage.show();
//    }




}
