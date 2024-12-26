package stages.student.library;

import Entity.Book;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class BookSelectionService {
    private static final BookSelectionService instance = new BookSelectionService();
    private final ObjectProperty<Book> selectedBook = new SimpleObjectProperty<>();

    private BookSelectionService() {}

    public static BookSelectionService getInstance() {
        return instance;
    }

    public ObjectProperty<Book> selectedBookProperty() {
        return selectedBook;
    }

    public Book getSelectedBook() {
        return selectedBook.get();
    }

    public void setSelectedBook(Book book) {
        selectedBook.set(book);
    }
}
