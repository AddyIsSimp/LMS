package stages.admin;

import Entity.Book;
import Entity.Category;
import Function.Function;
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

public class inventoryInsertController implements Initializable {

    @FXML
    private TextField tfAuthor;

    @FXML
    private ChoiceBox<Category> tfCategory;

    @FXML
    private TextField tfISBN;

    @FXML
    private TextField tfQuantity;

    @FXML
    private TextField tfTitle;

    @FXML
    private TextField pathField;

    @FXML
    private ImageView imgView;

    @FXML
    private Label lblError;

    private Image newImage;

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
        ArrayList<Category> categories = globalVariable.categoryList;
        if (categories != null && !categories.isEmpty()) {
            tfCategory.getItems().addAll(categories);
            tfCategory.setValue(categories.get(0));
        } else {
            System.out.println("No categories retrieved.");
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
        Parent root = FXMLLoader.load(getClass().getResource("/stages/admin/adminFXML/admin_reports.fxml"));
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

    //INSERT FUNCTIONS
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
            pathField.setText(filePath);

            newImage = new Image(filePath);

            imgView.setImage(newImage);
            imgView.setFitWidth(120);
            imgView.setFitHeight(144);
            imgView.setPreserveRatio(false);
        }
    }

    @FXML
    private void createBook(ActionEvent event) {
        if (newImage == null) {
            lblError.setText("No image selected");
            return;
        } else if (tfTitle.getText() == null || tfTitle.getText().trim().isEmpty()) {
            lblError.setText("Title is blank");
            return;
        } else if (tfAuthor.getText() == null || tfAuthor.getText().trim().isEmpty()) {
            lblError.setText("Author is blank");
            return;
        } else if (tfISBN.getText() == null || tfISBN.getText().trim().isEmpty()) {
            lblError.setText("ISBN is blank");
            return;
        } else if (fnc.digitChecker(tfISBN.getText()) == false || tfISBN.getText().trim().isEmpty()) {
            lblError.setText("ISBN should be all digits");
            return;
        } else if (tfCategory.getSelectionModel() == null) {
            lblError.setText("No category selected");
            return;
        } else if (tfQuantity.getText() == null || tfQuantity.getText().trim().isEmpty()) {
            lblError.setText("Quantity is blank");
            return;
        } else if (fnc.digitChecker(tfQuantity.getText()) == false) {
            lblError.setText("Quantity should be digits");
            return;
        }

        //Upload the image to database
        String bkTitle = tfTitle.getText();
        String bkAuthor = tfAuthor.getText();
        String bkISBN = tfISBN.getText();
        Category ctgryObj = tfCategory.getSelectionModel().getSelectedItem();
        String category = ctgryObj.getName();
        int quantity = Integer.parseInt(tfQuantity.getText());

        String imgName = globalVariable.dbFnc.insertBookImageDB(newImage, bkTitle + bkAuthor);

        Book newBook = new Book(bkTitle, bkAuthor, category, newImage, bkISBN, quantity);
        Function fc = new Function();
        boolean uniqueISBN = fc.checkISBN(globalVariable.bookList, bkISBN);

        if (uniqueISBN) {
            //Upload the book to database
            boolean ifSuccess = globalVariable.dbFnc.insertBookDB(newBook, imgName);
            if (ifSuccess) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Book inserted successfully", ButtonType.OK);
                alert.setTitle("Book Insert");
                alert.show();
                globalVariable.bookList.insertNOrder(newBook);
                imgView.setImage(null);
                pathField.setText(null);
                tfTitle.setText(null);
                tfAuthor.setText(null);
                tfISBN.setText(null);
                tfQuantity.setText(null);
                lblError.setText(null);
                refreshTable();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Book is not inserted", ButtonType.OK);
                alert.setTitle("Book Insert");
                alert.show();
            }
        }else{
            lblError.setText("ISBN is is already use");
            return;
        }
    }

}
