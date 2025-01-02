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
import Function.*;
import Function.globalVariable;

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
    private int dayLeft;

    private Button acceptBtn;
    private Button declineBtn;
    private Button returnBtn;

    public Transact() {
    }

    //for Pending transact
    public Transact(int transID, String title, String ISBN, int borrowerID, String borrowerName, String status) {
        setBorrowButton();
        setReturnButton();
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
        returnBtn = new Button("Return");
        returnBtn.setOnAction(event -> {
            String contentText = null;

            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Book Request", ButtonType.NO, ButtonType.YES);
            alert.setTitle("Return Book");
            alert.setHeaderText("Accept Return Request");

            if(dayLeft<0) {
                int numDay = Math.abs(dayLeft);
                int penaltyCalculate = numDay*5;
                contentText = "Borrower Info\nBorrower ID:  " + borrowerID +
                        "\nBorrower Name:  " + borrowerName +
                        "\n\nBook Info \nTitle:  " + bookTitle +
                        "\nISBN:  " + bkIsbn +
                        "\n\nPENALTY: " + numDay + "(day)  x  Php5 = " + penaltyCalculate + "Php"
                ;
                this.setPenalty(penaltyCalculate);
                globalVariable.dbFnc.updatePenaltyDB(this, getPenalty());
            }else {
                contentText = "Borrower Info\nBorrower ID:  " + borrowerID +
                        "\nBorrower Name:  " + borrowerName +
                        "\n\nBook Info \nTitle:  " + bookTitle +
                        "\nISBN:  " + bkIsbn;
            }

            alert.setContentText(contentText);

            if(alert.showAndWait().get() == ButtonType.YES) {
                this.returnDate = globalVariable.fnc.getDateNow();
                this.status = "FINISH";
                Book book = globalVariable.fnc.getBook(globalVariable.bookList, getBkIsbn());
                book.setBorrowed(book.getBorrowed()-1);
                book.setQuantity(book.getQuantity()+1);
                globalVariable.dbFnc.updateTransactStatus(this, "FINISH");
                this.setReturnDate(globalVariable.fnc.getDateNow());
                globalVariable.dbFnc.updateReturnDateDB(this, globalVariable.fnc.getDateNow());
                globalVariable.dbFnc.addBookOneDB(getBookTitle(), getBkIsbn());
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
                Book brrwBook = globalVariable.fnc.getBook(globalVariable.bookList, bkIsbn);
                if(brrwBook!=null) {
                    if(brrwBook.getQuantity()>0) {
                        brrwBook.setQuantity(brrwBook.getQuantity()-1);
                        brrwBook.setBorrowed(brrwBook.getBorrowed() + 1);
                        this.status = "ONGOING";
                        this.borrowDate = globalVariable.fnc.getDateNow();
                        if(this.borrowDate!=null) {
                            setDayLeft();
                        }
                        globalVariable.dbFnc.removeBookOneDB(brrwBook.getTitle(), brrwBook.getISBN());
                        globalVariable.dbFnc.updateTransactStatus(this, "ONGOING");
                    }else {
                        globalVariable.fnc.showAlert("Borrow Request Fail", "Book is not available");
                    }
                }else{
                    globalVariable.fnc.showAlert("Borrow Book Error", "Book not found in the list");
                }
            }else {
                alert.close();
            }
        });

        declineBtn = new Button("Decline");
        declineBtn.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.NONE, "Do you want to decline \nthe request?", ButtonType.YES, ButtonType.NO);
            alert.setTitle("Borrow Request");

            if(alert.showAndWait().get() == ButtonType.YES) {
                this.status = "DECLINE";
                globalVariable.dbFnc.updateTransactStatus(this, "DECLINE");
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

    public int getDayLeft() {
        if(this.borrowDate!=null) {
            this.dayLeft = globalVariable.fnc.computeDayLeft(borrowDate);
        }
        return dayLeft;
    }

    public void setDayLeft() {
        if(this.borrowDate!=null) {
            this.dayLeft = globalVariable.fnc.computeDayLeft(borrowDate);
        }
    }

    public Button getReturnBtn() {
        return returnBtn;
    }

    public void setReturnBtn(Button returnBtn) {
        this.returnBtn = returnBtn;
    }

}
