package Entity;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;

import javax.swing.*;
import java.io.File;

public class Book {
    private File f = null;
    private String path = null;
    private ImageIcon format = null;
    private byte[] pImage = null;

    private int bookID;
    private String title;
    private String author;
    private String category;
    private Image imageSrc;
    private String ISBN;
    private int quantity;
    private int borrowed;

    public Book(String title, String author, String category, Image imageSrc, String isbn, int qty, int borrowed) {
        this.title = title;
        this.author = author;
        this.category = category;
        this.ISBN = isbn;
        this.quantity = qty;
        this.borrowed = borrowed;
        this.imageSrc = imageSrc;
    }

    public Book(String title, String author, String category, String isbn, int qty, int borrowed) {
        this.title = title;
        this.author = author;
        this.category = category;
        this.ISBN = isbn;
        this.quantity = qty;
        this.borrowed = borrowed;
    }

    public Book(String title, String author, String category, Image imageSrc, String isbn, int qty) {
        this.title = title;
        this.author = author;
        this.category = category;
        this.ISBN = isbn;
        this.quantity = qty;
        this.imageSrc = imageSrc;
    }

    public Book(String title, String author, String category, Image imageSrc, int quantity) {
        this.title = title;
        this.author = author;
        this.quantity = quantity;
        this.category = category;
        this.imageSrc = imageSrc;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getGenre() {
        return category;
    }

    public void setGenre(String genre) {
        this.category = genre;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getBorrowed() {
        return borrowed;
    }

    public void setBorrowed(int borrowed) {
        this.borrowed = borrowed;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(Object category) {
        this.category = category.toString();
    }

    public Image getImageSrc() {
        return imageSrc;
    }

    public void setImageSrc(Image imageSrc) {
        this.imageSrc = imageSrc;
    }

    public void bookDataView() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Book Request", ButtonType.NO, ButtonType.YES);
        alert.setTitle("Return Book");
        alert.setHeaderText("Accept Return Request");
        alert.showAndWait();
    }

    public void borrowBookView() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Book Request", ButtonType.NO, ButtonType.YES);
        alert.setTitle("Return Book");
        alert.setHeaderText("Accept Return Request");
        alert.showAndWait();
    }

}
