package stages.student.library;

import Entity.Category;
import Function.globalVariable;
import LinkedList.DoublyLinkList;
import LinkedList.Link;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class libraryController implements Initializable {

    @FXML
    private HBox bookLayout;
    @FXML
    private HBox bookLayout2;
    @FXML
    private HBox bookLayout3;
    @FXML
    private VBox cabinet;
    @FXML
    private Label bookNum;
    @FXML
    private Button moveLeftBtn;
    @FXML
    private Button moveRightBtn;
    @FXML
    private Label cabinetPos;
    @FXML
    private ChoiceBox<String> sortCB;

    private DoublyLinkList books;
    private int cabinetIndex = 0;
    private int cabinetCount;

    @FXML
    private ChoiceBox<Category> categoryCB;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            bookLayout.getChildren().clear();
            bookLayout2.getChildren().clear();
            bookLayout3.getChildren().clear();
            initializeLibraryView(books = globalVariable.bookList);

            //SORT CHOICEBOX
            String[] sortType = {"A-Z", "Z-A"};
            sortCB.getItems().addAll(sortType);
            sortCB.setValue(sortType[0]);

            sortCB.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    // Call the handleChoice method when category is selected
                    if(newValue.equals("A-Z")) {
                        initializeLibraryView(books);
                    }else if(newValue.equals("Z-A")) {
                        initializeLibraryViewReverse(books);
                    }
                }
            });

            //CATEGORY CHOICEBOX
            ArrayList<Category> categories = globalVariable.categoryList;
            Category allCtgry = new Category(0, "All");
            if (categories.size() != 0) {
                categoryCB.getItems().addAll(categories);
                categoryCB.getItems().addFirst(allCtgry);
                categoryCB.setValue(allCtgry);
            }

            categoryCB.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    // Call the handleChoice method when category is selected
                    if (newValue.getName().equals("All")) {
                        initializeLibraryView(books = globalVariable.bookList);
                    }else {
                        books = globalVariable.fnc.selectCategoryBooks(newValue);
                        initializeLibraryView(books);
                    }
                }
            });
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    void moveLeftEvt(ActionEvent event) {
        try {
            if(cabinetIndex>=0) {
                moveRightBtn.setDisable(false); //Update the moving btn cause he can now move right
                cabinetIndex--;
                int start = cabinetIndex * 42;
                int end = start + 42;
                DoublyLinkList bookList = copyValueList(books, start, end);

                Link current = bookList.getFirst();

                bookLayout.getChildren().clear();
                bookLayout2.getChildren().clear();
                bookLayout3.getChildren().clear();

                int count = 0;
                while (current != null) {               //Load to display book in cabinet
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("/stages/admin/library/Book.fxml"));

                    HBox bookBox = fxmlLoader.load();
                    BookController bookController = fxmlLoader.getController();
                    bookController.setData(current.getElement());       //Set the data of the book
                    if (count < 14) {
                        bookLayout.getChildren().add(bookBox);
                    } else if (count < 28) {
                        bookLayout2.getChildren().add(bookBox);
                    } else if (count < 42) {
                        bookLayout3.getChildren().add(bookBox);
                    } else break;
                    count++;
                    current = current.getNext();
                }

                //Update the cabinet index
                cabinetPos.setText(Integer.toString(cabinetIndex+1));

                moveLeftBtn.setDisable(cabinetIndex == 0);
            }
        }catch(Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
        }
    }

    @FXML
    void moveRightEvt(ActionEvent event) {
        try {
            if(cabinetIndex<cabinetCount) {
                moveLeftBtn.setDisable(false); //Update the moving btn cause he can now move right
                cabinetIndex++;
                cabinetPos.setText(Integer.toString(cabinetIndex));
                int start = cabinetIndex * 42;
                int end = start + 42;
                DoublyLinkList bookList = copyValueList(books, start, end);

                int bookCount = bookList.getSize();      //Indicates the number of books
                Link current = bookList.getFirst();

                bookLayout.getChildren().clear();
                bookLayout2.getChildren().clear();
                bookLayout3.getChildren().clear();
                int count = 0;
                while (current != null) {               //Load to display book in cabinet
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("/stages/admin/library/Book.fxml"));

                    HBox bookBox = fxmlLoader.load();
                    BookController bookController = fxmlLoader.getController();
                    bookController.setData(current.getElement());       //Set the data of the book
                    if (count < 14) {
                        bookLayout.getChildren().add(bookBox);
                    } else if (count < 28) {
                        bookLayout2.getChildren().add(bookBox);
                    } else if (count < 42) {
                        bookLayout3.getChildren().add(bookBox);
                    } else break;
                    count++;
                    current = current.getNext();
                }

                //Update the cabinet index
                cabinetPos.setText(Integer.toString(cabinetIndex+1));

                moveRightBtn.setDisable(cabinetIndex == cabinetCount-1);
            }
        }catch(Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
        }
    }

    //TO BE ERASED
    private DoublyLinkList libraryBooks() throws IOException { //Create a list of books
        DoublyLinkList books = globalVariable.bookList;
        return books;
    }

    //To retrieve the range of books
    public DoublyLinkList copyValueList(DoublyLinkList books, int atStart, int atEnd) {
        DoublyLinkList list = new DoublyLinkList(); //Store the copy here

        if(atStart < 0) { //Invalid value of start and end
            Alert alert = new Alert(Alert.AlertType.WARNING, "Library View Copy Error");
            return list;
        }else {
            //Go to the desired start of list
            Link current = books.getFirst();
            int i; //index

            for(i = 0; i<atStart; i++) {
                current = current.getNext();
            }


            //Add the range until reaches the end
            for(; current!=null && i < atEnd; i++) {        //Copy the value to list
                list.insertNOrder(current.getElement());
                current = current.getNext();
            }
        }
        return list;
    }

    public void initializeLibraryView(DoublyLinkList books) {
        try {
            // Check if the list of books is empty
            if (books == null || books.isEmpty()) {
                bookLayout.getChildren().clear();
                bookLayout2.getChildren().clear();
                bookLayout3.getChildren().clear();
                moveRightBtn.setDisable(true);
                moveLeftBtn.setDisable(true);
                return;
            }

            bookLayout.getChildren().clear();
            bookLayout2.getChildren().clear();
            bookLayout3.getChildren().clear();

            moveLeftBtn.setDisable(true);

            int bookCount = books.getSize(); // Indicates the number of books
            bookNum.setText(Integer.toString(bookCount));
            cabinetPos.setText(Integer.toString(cabinetIndex + 1));
            cabinetCount = bookCount / 42;
            if (bookCount % 42 != 0 && bookCount > 42) { // Add another cabinet
                cabinetCount++;
            }
            if (bookCount <= 42) {
                moveRightBtn.setDisable(true);
            }

            int count = 0;
            Link current = books.getFirst();

            while (current != null) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/stages/admin/library/Book.fxml"));

                HBox bookBox = fxmlLoader.load();
                BookController bookController = fxmlLoader.getController();
                bookController.setData(current.getElement());

                if (count < 14) {
                    bookLayout.getChildren().add(bookBox);
                } else if (count < 28) {
                    bookLayout2.getChildren().add(bookBox);
                } else if (count < 42) {
                    bookLayout3.getChildren().add(bookBox);
                }
                count++;
                current = current.getNext();
            }
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
            alert.showAndWait();
        }
    }

    public void initializeLibraryViewReverse(DoublyLinkList books) {
        try {
            // Check if the list of books is empty
            if (books == null || books.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Book list is empty", ButtonType.OK);
                alert.setTitle("Book Display");
                alert.show();
                return;
            }

            moveLeftBtn.setDisable(true);

            bookLayout.getChildren().clear();
            bookLayout2.getChildren().clear();
            bookLayout3.getChildren().clear();

            int bookCount = books.getSize(); // Indicates the number of books
            bookNum.setText(Integer.toString(bookCount));
            cabinetPos.setText(Integer.toString(cabinetIndex + 1));
            cabinetCount = bookCount / 42;
            if (bookCount % 42 != 0 && bookCount > 42) { // Add another cabinet
                cabinetCount++;
            }
            if (bookCount <= 42) {
                moveRightBtn.setDisable(true);
            }

            int count = 0;
            Link current = books.getLast();

            while (current != null) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/stages/admin/library/Book.fxml"));

                HBox bookBox = fxmlLoader.load();
                BookController bookController = fxmlLoader.getController();
                bookController.setData(current.getElement());

                if (count < 14) {
                    bookLayout.getChildren().add(bookBox);
                } else if (count < 28) {
                    bookLayout2.getChildren().add(bookBox);
                } else if (count < 42) {
                    bookLayout3.getChildren().add(bookBox);
                }
                count++;
                current = current.getPrev();
            }
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
            alert.showAndWait();
        }
    }

    public void clearLibraryBox() {
        try {
            Platform.runLater(() -> {
                if (bookLayout != null) bookLayout.getChildren().clear();
                if (bookLayout2 != null) bookLayout2.getChildren().clear();
                if (bookLayout3 != null) bookLayout3.getChildren().clear();
            });
        } catch (Exception e) {
            e.printStackTrace(); // Log the full error for debugging
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error clearing library box: " + e.getMessage());
            alert.showAndWait();
        }
    }

//    private void handleChoice(Category category) {
//        if (category.getName().equals("All")) {
//            DoublyLinkList bookList = globalVariable.bookList;
//            if (libraryCtrl != null) {
//                libraryCtrl.initializeLibraryView(bookList);
//
//        } else {
//            DoublyLinkList categoryList = globalVariable.fnc.selectCategoryBooks(category);
//            if (libraryCtrl != null) {
//                libraryCtrl.initializeLibraryView(categoryList);
//            }
//        }
//    }

}
