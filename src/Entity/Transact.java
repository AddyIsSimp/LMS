package Entity;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;

import javax.swing.event.DocumentEvent;
import java.awt.*;
import java.sql.Date;

import Function.globalVariable;
public class Transact {
    private int transID;
    private int borrowerID; //Borrower
    private String borrowerName;
    private String bookTitle;   //Borrowed
    private String bkIsbn;
    private String status; //Pending, Borrowed, Returned, Paid
    private double penalty;
    private Date borrowDate;
    private Date returnDate;

    private Button acceptBtn;
    private Button declineBtn;
    private Button returnBtn;

    public Transact() {

    }

    //for Pending transact
    public Transact(int transID, String title, String ISBN, int borrowerID, String borrowerName, String status) {
        setBorrowButton();
        this.transID = transID;
        this.borrowerID = borrowerID;
        this.borrowerName = borrowerName;
        this.bookTitle = title;
        this.bkIsbn = ISBN;
        this.status = status;
    }

    //TOACCEPT TRANSACT

    //ONGOING TRANSACT

    public int getTransID() {
        return transID;
    }

    public void setTransID(int transID) {
        this.transID = transID;
    }

    public void setReturnButton() {
        acceptBtn = new Button("Return");
        acceptBtn.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Book Request", ButtonType.NO, ButtonType.YES);
            alert.setTitle("Return Book");
            alert.setHeaderText("Accept Return Request");
            alert.setContentText("Borrower Info\nBorrower ID:  " + borrowerID +
                    "\nBorrower Name:  " + borrowerName +
                    "\n\nBook Info \nTitle:  " + bookTitle +
                    "\nISBN:  " + bkIsbn
            );

            if(alert.showAndWait().get() == ButtonType.YES) {
                this.status = "FINISH";
            }else {
                alert.close();
            }
        });
    }

    public void setBorrowButton() {
        acceptBtn = new Button("Accept");
        acceptBtn.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Book Request", ButtonType.NO, ButtonType.YES);
            alert.setTitle("Borrow Request");
            alert.setHeaderText("Accept Borrow Request");
            alert.setContentText("Borrower Info\nBorrower ID:  " + borrowerID +
                    "\nBorrower Name:  " + borrowerName +
                    "\n\nBook Info \nTitle:  " + bookTitle +
                    "\nISBN:  " + bkIsbn
                    );

            if(alert.showAndWait().get() == ButtonType.YES) {
                this.status = "ONGOING";
                this.borrowDate = globalVariable.fnc.getDateNow();
            }else {
                alert.close();
            }
        });

        declineBtn = new Button("Decline");
        declineBtn.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.NONE, "Do you want to decline \nthe request?", ButtonType.YES, ButtonType.NO);
            alert.setTitle("Borrow Request");

            if(alert.showAndWait().get() == ButtonType.YES) {
                this.status = "DECLINED";
            }else {
                alert.close();
            }
        });
    }

    public int getBorrowerID() {
        return borrowerID;
    }

    public void setBorrowerID(int borrowerID) {
        this.borrowerID = borrowerID;
    }

    public double getPenalty() {
        return penalty;
    }

    public void setPenalty(double penalty) {
        this.penalty = penalty;
    }

    public Date getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(Date borrowDate) {
        this.borrowDate = borrowDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public String getBorrowerName() {
        return borrowerName;
    }

    public void setBorrowerName(String borrowerName) {
        this.borrowerName = borrowerName;
    }

    public Button getAcceptBtn() {
        return acceptBtn;
    }

    public void setAcceptBtn(Button acceptBtn) {
        this.acceptBtn = acceptBtn;
    }

    public Button getDeclineBtn() {
        return declineBtn;
    }

    public void setDeclineBtn(Button declineBtn) {
        this.declineBtn = declineBtn;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getBkIsbn() {
        return bkIsbn;
    }

    public void setBkIsbn(String bkIsbn) {
        this.bkIsbn = bkIsbn;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
