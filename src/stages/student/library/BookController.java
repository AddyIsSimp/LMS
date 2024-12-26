package stages.student.library;

import Entity.Book;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class BookController {

    @FXML
    private VBox box;

    @FXML
    private ImageView bookImage;

    @FXML
    private VBox imageContainer;

    @FXML
    private Label bookTitle;

    @FXML
    private Label bookInitial;

    private Book book;

    public void setData(Book book) {
        Image image = book.getImageSrc();
        bookImage.setImage(image);
        bookTitle.setText(book.getTitle());
        this.book = book;
    }

    @FXML
    private void bookSelected(MouseEvent event) {
        if (book != null) {
            BookSelectionService.getInstance().setSelectedBook(book);
        }
    }

}
