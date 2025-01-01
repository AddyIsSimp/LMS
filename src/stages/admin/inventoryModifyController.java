package stages.admin;

import Entity.Book;
import Entity.Category;
import Entity.Staff;
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
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static Function.globalVariable.*;

public class inventoryModifyController implements Initializable {

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
    private Button saveBttn, changeImgBttn;
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

        categories = categoryList;

        if (categories != null && !categories.isEmpty()) {
            tfCategory.getItems().addAll(categories);
            tfCategory.setValue(categories.get(0)); // Optional: Set a default value
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "No categories");
            alert.showAndWait();
        }

        //If search
        if (modifyBook != null) {
            searchBook = modifyBook;
            Image img = searchBook.getImageSrc();
            bkImage.setImage(img);
            titleField.setText(searchBook.getTitle());
            authorField.setText(searchBook.getAuthor());
            isbnField.setText(searchBook.getISBN());
            tfCategory.setValue(categories.getFirst());
            qtyField.setText(Integer.toString(searchBook.getQuantity()));
            changeImgBttn.setDisable(false);
            titleField.setDisable(false);
            authorField.setDisable(false);
            isbnField.setDisable(false);
            tfCategory.setDisable(false);
            qtyField.setDisable(false);
            saveBttn.setDisable(false);
            modifyBook = null;
        }

        sortCB.getItems().addAll(sortType);
        sortCB.setValue(sortType[0]);
        sortCB.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            sortBookData();
        });

        retrieveBook = fnc.retrieveBook(bookList);
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

        if (searchField.getText().isEmpty()) {  //if searchfield is empty
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
            changeImgBttn.setDisable(false);
            titleField.setDisable(false);
            authorField.setDisable(false);
            isbnField.setDisable(true);
            tfCategory.setDisable(false);
            qtyField.setDisable(false);
            saveBttn.setDisable(false);
        }
    }

    @FXML
    private void browseImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select an Image");

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            String filePath = file.toURI().toString();

            newImage = new Image(filePath);

            bkImage.setImage(newImage);
            bkImage.setFitWidth(120);
            bkImage.setFitHeight(144);
            bkImage.setPreserveRatio(false);
        }
    }

    @FXML
    private void editBook(ActionEvent event) {
        if (titleField.getText() == null || titleField.getText().trim().isEmpty()) {
            lblError2.setText("Title is blank");
            return;
        } else if (authorField.getText() == null || authorField.getText().trim().isEmpty()) {
            lblError2.setText("Author is blank");
            return;
        } else if (isbnField.getText() == null || isbnField.getText().trim().isEmpty()) {
            lblError2.setText("ISBN is blank");
            return;
        } else if (fnc.digitChecker(isbnField.getText()) == false || isbnField.getText().trim().isEmpty()) {
            lblError2.setText("ISBN should be all digits");
            return;
        } else if (tfCategory.getSelectionModel() == null) {
            lblError2.setText("No category selected");
            return;
        } else if (qtyField.getText() == null || qtyField.getText().trim().isEmpty()) {
            lblError2.setText("Quantity is blank");
            return;
        } else if (fnc.digitChecker(qtyField.getText()) == false) {
            lblError2.setText("Quantity should be digits");
            return;
        } else if (fnc.checkISBNExempt(globalVariable.bookList, isbnField.getText(), isbnField.getText()) == false) {
            lblError2.setText("ISBN is already used");
            return;
        }

        //Upload the image to database
        String bkTitle = titleField.getText();
        String bkAuthor = authorField.getText();
        String bkISBN = isbnField.getText();
        Category ctgryObj = tfCategory.getSelectionModel().getSelectedItem();
        String category = ctgryObj.getName();
        int quantity = Integer.parseInt(qtyField.getText());

        String imgName = globalVariable.dbFnc.insertBookImageDB(newImage, bkTitle + bkAuthor);
        Book newBook = new Book(bkTitle, bkAuthor, category, newImage, bkISBN, quantity);
        boolean success=dbFnc.modifyBookDB(newBook,imgName);
        globalVariable.bookList.deleteBook(searchBook.getTitle());
        globalVariable.bookList.insertNOrder(newBook);
        refreshTable();

        if (!success) {
            lblError.setText("Database update failed.");
            return;
        }else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Book modify successfully", ButtonType.OK);
            alert.setTitle("Book Modify");
            alert.show();
        }

        imgName = globalVariable.dbFnc.insertBookImageDB(newImage, bkTitle + bkAuthor);
        if (imgName == null) {
            lblError.setText("Image upload failed.");
            return;
        }

        searchField.setText(null);
        bkImage.setImage(null);
        titleField.setText(null);
        authorField.setText(null);
        isbnField.setText(null);
        tfCategory.setValue(categories.getFirst());
        qtyField.setText(null);
        changeImgBttn.setDisable(true);
        titleField.setDisable(true);
        authorField.setDisable(true);
        isbnField.setDisable(true);
        tfCategory.setDisable(true);
        qtyField.setDisable(true);
        saveBttn.setDisable(true);
    }

}
