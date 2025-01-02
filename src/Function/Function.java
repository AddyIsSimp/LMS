package Function;

import LinkedList.DoublyLinkList;
import LinkedList.Link;

import javax.swing.*;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import Entity.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritablePixelFormat;

import static Function.globalVariable.bookList;
import static Function.globalVariable.fnc;

public class Function {

    public boolean digitChecker(String s) {
        for(int i = 0; i<s.length(); i++) {
            if(Character.isDigit(s.charAt(i))==false) {
                return false;
            }
        }
        return true;
    }

    public int countBkQuantity(DoublyLinkList list) {
        int bookQuantity = 0;
        Link current = list.getFirst();
        while(current!=null) {
            Book book = current.getElement();
            bookQuantity += book.getQuantity();
            current=current.getNext();
        }
        return bookQuantity;
    }

    public String retrieveStudentID(String ID)  {       //2024-00001
        String studentID = null;
        if (!ID.isEmpty()) {
            if (ID.matches("\\d{4}-\\d{5}")) {
                studentID = ID.replace("-", "");

            } else {
                
                return null;
            }
        } else {
            System.out.println("Empty ID provided");
            return null;
        }
        return studentID;
    }

    public byte[] convertImageToByteArray(Image image) {
        try {
            int width = (int) image.getWidth();
            int height = (int) image.getHeight();

            PixelReader pixelReader = image.getPixelReader();

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            byte[] buffer = new byte[width * height * 4]; // Assuming ARGB format (4 bytes per pixel)
            WritablePixelFormat<ByteBuffer> pixelFormat = WritablePixelFormat.getByteBgraInstance();

            pixelReader.getPixels(0, 0, width, height, pixelFormat, buffer, 0, width * 4);

            outputStream.write(buffer);

            // Return the byte array
            return outputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String[] arrayListToStringArray(ArrayList<String> strs) {
        String[] strings = new String[strs.size()];
        for(int i = 0; i<strs.size(); i++) {
            strings[i] = strs.get(i);
        }
        return strings;
    }

    public int countUniBkQuantity(DoublyLinkList list) {
        int uniqueBook = 0;
        Link current = list.getFirst();
        while(current!=null) {
            uniqueBook++;
            current = current.getNext();
        }
        return uniqueBook;
    }


    public int countBookPresent(DoublyLinkList list) {
        int uniqueBook = 0;
        Link current = list.getFirst();
        while(current!=null) {
            Book book = current.getElement();
            if (book.getQuantity() <= 0) {
                current = current.getNext(); // Ensure pointer moves forward.
                continue;
        }
            uniqueBook++;
            current = current.getNext();
        }
        return uniqueBook;
    }


    public String getDateNowStr() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }

    public java.sql.Date getDateNow() {
        return globalVariable.globalDate;
    }


    public int countBkBorrow(DoublyLinkList list) {
        int bookBorrow = 0;
        Link current = list.getFirst();
        while(current!=null) {
            Book book = current.getElement();
            bookBorrow += book.getBorrowed();
            current = current.getNext();
        }
        return bookBorrow;
    }

    public boolean staffIDChecker(String s) {   //Correct format lName+staffID
        String pattern = "^[a-zA-Z]+\\d{3}$";
        return s.matches(pattern);
    }

    public boolean passwordChecker(String s) { //password should have letter and a number or special character greater than 8 chars
        if (s.length() < 8) {
            return false;
        }
        String pattern = "^(?=.*[a-zA-Z])(?=.*[\\d\\W]).+$";
        return s.matches(pattern);
    }

    public DoublyLinkList selectCategoryBooks(Category ctgry) {
        DoublyLinkList bookList = globalVariable.bookList;
        DoublyLinkList categoryList = new DoublyLinkList();

        if(bookList.isEmpty())  return null;

        Link current = bookList.getFirst();
        String ctgryName = ctgry.getName();
        while (current != null) {
            Book bk = current.getElement();
            if (bk.getCategory().equals(ctgryName)) {
                categoryList.insertNOrder(bk);
            }
            current = current.getNext(); // Advance to the next link
        }

        return categoryList;
    }

    public java.sql.Date convertToSqlDate(LocalDate localDate) {
        if (localDate == null) {
            throw new IllegalArgumentException("LocalDate cannot be null");
        }

        return java.sql.Date.valueOf(localDate);
    }

    public Image getImage(String imgName) {
        Image img = null;
        try {
            var resource = getClass().getResource("/bookImages/" + imgName);
            if (resource == null) {
                throw new IllegalArgumentException("Image not found: " + imgName);
            }
            String imagePath = resource.toExternalForm();
            img = new Image(imagePath);
        } catch (Exception e) {
            showAlert("Get Image Error", e.getMessage());
        }
        return img;
    }

    public void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.setTitle(title);
        alert.show();
    }

    public ObservableList<Book> inventoryBookView() {
        try {
            DoublyLinkList books = globalVariable.bookList; // Assuming this is correctly defined.
            ObservableList<Book> inventoryBook = FXCollections.observableArrayList();

            Link current = books.getFirst();
            while (current != null) {
                Book book = current.getElement(); // Get the current Book.

                String title = book.getTitle();
                String author = book.getAuthor();
                String category = book.getCategory();
                String ISBN = book.getISBN();
                int quantity = book.getQuantity();
                Image img = book.getImageSrc();
                int borrowed = book.getBorrowed();

                // Create a new book object and add it to the list
                Book newBook = new Book(title, author, category, img, ISBN, quantity, borrowed);

                inventoryBook.add(newBook); // Add it to the ObservableList.
                current = current.getNext(); // Move to the next node.
            }

            return inventoryBook;
        } catch (NullPointerException e) {
            showAlert("Inventory Book View Error", "A null pointer was encountered: " + e.getMessage());
        } catch (Exception e) {
            showAlert("Inventory Book View Error", "An unexpected error occurred: " + e.getMessage());
        }

        return null; // Return null if an exception occurs.
    }

    public Student getStudentWithName(ArrayList<Student> studentList, int id) {
        for (Student student : studentList) {
            if (student.getSchoolID() == id) {
                return student;
            }
        }
        return null;
    }

    public ObservableList<Transact> retrievePendingTransact(ArrayList<Transact> transactsList) {
        ObservableList<Transact> pendingTransact = FXCollections.observableArrayList();
        for (Transact transact : transactsList) {
            transact.setBorrowButton();
            if ("PENDING".equalsIgnoreCase(transact.getStatus())) {
                pendingTransact.add(transact);
            }
        }
        return pendingTransact;
    }

    public ObservableList<Transact> retrieveAllTransact(ArrayList<Transact> transactsList) {
        ObservableList<Transact> pendingTransact = FXCollections.observableArrayList();
        for (Transact transact : transactsList) {
            pendingTransact.add(transact);
        }
        return pendingTransact;
    }

    public ObservableList<Transact> retrieveStudentTransact(ArrayList<Transact> transactsList, int studentID) {
        ObservableList<Transact> studentTransact = FXCollections.observableArrayList();
        for (Transact transact : transactsList) {
            if (transact.getBorrowerID()==studentID) {
                studentTransact.add(transact);
            }
        }
        return studentTransact;
    }

    public ObservableList<Student> retrieveStudent(ArrayList<Student> studentList) {
        ObservableList<Student> studentAcc = FXCollections.observableArrayList();
        for (Student student : studentList) {
            studentAcc.add(student);
        }
        return studentAcc;
    }

    public ObservableList<Staff> retrieveStaff(ArrayList<Staff> staffList) {
        ObservableList<Staff> StaffAcc = FXCollections.observableArrayList();
        for (Staff staff : staffList) {
            StaffAcc.add(staff);
        }
        return StaffAcc;
    }


    public ObservableList<Book> retrieveBook(DoublyLinkList bookList) {
        ObservableList<Book> bookListObv = FXCollections.observableArrayList();
        if(bookList.isEmpty()) return null;

        Link current = bookList.getFirst();
        while (current != null) {
            Book book = current.getElement();
            bookListObv.add(book);
            current = current.getNext();
        }
        return bookListObv;
    }

    public ObservableList<Transact> retrieveOngoingTransact(ArrayList<Transact> transactsList) {
        ObservableList<Transact> ongoingTransact = FXCollections.observableArrayList();
        for (Transact transact : transactsList) {
            transact.setReturnButton();
            if ("ONGOING".equalsIgnoreCase(transact.getStatus())) {
                ongoingTransact.add(transact);
            }
            if(transact.getBorrowDate()!=null) {
                transact.setDayLeft();
            }
        }
        return ongoingTransact;
    }

    public ObservableList<Transact> retrieveFinishTransact(ArrayList<Transact> transactsList) {
        ObservableList<Transact> finishTransact = FXCollections.observableArrayList();
        for (Transact transact : transactsList) {
            if ("FINISH".equalsIgnoreCase(transact.getStatus())) {
                finishTransact.add(transact);
            }
        }
        return finishTransact;
    }

    public String getUserId(String lName, int ID) {
        String userID = null;
        String formattedID = String.format("%03d", ID);
        userID = lName + formattedID;
        return userID;
    }

    public Book getBook(DoublyLinkList list, String isbn) {
        Book bkFind = list.findISBN(isbn);
        return bkFind;
    }


    public int computeDayLeft(java.sql.Date dateBorrow) {
        if (dateBorrow == null) {
            throw new IllegalArgumentException("dateBorrow must not be null");
        }
        int dayLeft;
        Date dateNow = fnc.getDateNow();

        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.setTime(dateBorrow);
        calendar.add(java.util.Calendar.DAY_OF_MONTH, 7);
        Date dueDate = new Date(calendar.getTimeInMillis());

        long differenceInMillis = dueDate.getTime() - dateNow.getTime();
        dayLeft = (int) (differenceInMillis / (1000 * 60 * 60 * 24));

        return dayLeft;
    }


    public boolean checkISBN(DoublyLinkList bookList, String isbn) {    //Returns true if there no duplicate otherwise false
        if(bookList.isEmpty()) return true;

        Link current = bookList.getFirst();
        while(current!=null) {
            Book book = current.getElement();
            if(book.getISBN().equals(isbn)) {
                return false;
            }
            if(current.getNext()==null) return true;
            current = current.getNext();
        }

        return true;
    }

    public boolean checkISBNExempt(DoublyLinkList bookList, String isbn, String exempt) {
        //Revise this method that it will not checkISB
        if(bookList.isEmpty()) return true;

        Link current = bookList.getFirst();
        while(current!=null) {
            Book book = current.getElement();
            if(book.getISBN().equals(exempt)==false && book.getISBN().equals(isbn)) {
                return false;
            }
            if(current.getNext()==null) return true;
            current = current.getNext();
        }

        return true;
    }

    public String getFullName(String lName, String fName) {
        if (lName == null || fName == null || lName.isEmpty() || fName.isEmpty()) {
            throw new IllegalArgumentException("Last name and first name cannot be null or empty");
        }

        fName = fName.substring(0, 1).toUpperCase() + fName.substring(1).toLowerCase();

        return lName + " " + fName.charAt(0) + ".";
    }

    public Student findStudentID(ArrayList<Student> studentList, String id) {
        String studID = retrieveStudentID(id);
        int studentID = 0;
        if(studID!=null) {
            studentID = Integer.parseInt(studID);
        }else showAlert("Find Student Error", "Wrong ID Format");

        for(Student student: studentList) {
            if(student.getSchoolID()==studentID) {
                return student;
            }
        }
        return null;
    }

    public Book findBookISBN(DoublyLinkList bookList, String isbn) {
        Book book = bookList.findISBN(isbn);
        if(book!=null) {
            return book;
        }
        return null;
    }

    public Student findStudentEmail(ArrayList<Student> studentList, String email) {
        for(Student student: studentList) {
            if(student.getEmail().equals(email)) {
                return student;
            }
        }
        return null;
    }

    public Staff findStaffID(ArrayList<Staff> staffList, String id) {
        for(Staff staff: staffList) {
            if(staff.getUsername().equals(id)) {
                return staff;
            }
        }
        return null;
    }

    public boolean emailChecker(String email) {
        String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]$";

        if (email == null || !email.matches(emailRegex)) {
            return true;
        }
        return false;
    }

    public boolean deleteStudent(ArrayList<Student> studentList, int schoolID) {
        boolean isDeleted = false;
        for(Student student: studentList) {
            if(student.getSchoolID()==schoolID) {
                studentList.remove(student);
                return true;
            }
        }
        return false;
    }

    public boolean deleteStaff(ArrayList<Staff> staffList, String staffID) {
        boolean isDeleted = false;
        for(Staff staff :  staffList) {
            if(staff.getUsername().equals(staffID)) {
                staffList.remove(staff);
                return true;
            }
        }
        return false;
    }

}
