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
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import stages.admin.bkManageController;
import stages.admin.library.BookSelectionService;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;
import java.util.ResourceBundle;
import Function.*;

public class brrowBooksController implements Initializable {

    PreparedStatement pstmt;
    ResultSet rs;

    Function fnc = new Function();
    dbFunction dbFunc = new dbFunction();

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

    private Student studentLogin;
    private Connection conn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            // Load the library view
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(bkManageController.class.getResource("/stages/admin/library/libraryView.fxml"));
            VBox libraryView = fxmlLoader.load();
            libraryBox.getChildren().add(libraryView);

            // Listen for changes in the selected book
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
            bkQtyField.setText(Integer.toString(book.getQuantity()));
        }
    }

    @FXML
    private void goBorrowBook(ActionEvent event) {
        try {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Borrow Book Request", ButtonType.NO, ButtonType.YES);
            alert.setTitle("Borrow Book");
            alert.setHeaderText("Confirm Borrow Book Request");
            alert.setContentText("Borrower Info\nBorrower ID:  " + studentLogin.getSchoolID() +
                    "\nBorrower Name:  " + studentLogin.getfName() +
                    "\n\nBook Info \nTitle:  " + bkTitleField.getText() +
                    "\nISBN:  " + bkISBNField.getText()
            );
            alert.showAndWait();

            conn = dbFunc.connectToDB();
            int transactID = dbFunc.resetAutoIncrement(conn, "transact", "trans_id");
            if (bkTitleField.getText() == null) {
                Alert error = new Alert(Alert.AlertType.NONE, "No selected book", ButtonType.OK);
                error.setTitle("Borrow Book");
                error.showAndWait();
                return;
            }

            String title = bkTitleField.getText();
            String bookISBN = bkISBNField.getText();
            int bkISBN = Integer.parseInt(bookISBN);
            LocalDate return_date = LocalDate.now().plusDays(7);
            java.sql.Date dateNow = fnc.convertToSqlDate(return_date);

            if (alert.showAndWait().get() == ButtonType.YES) {
                Transact newTransact = new Transact(transactID, studentLogin.getSchoolID(), bkISBN, dateNow, "PENDING");

            } else {
                alert.close();
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
