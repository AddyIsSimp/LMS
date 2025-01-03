package Function;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

import Entity.*;
import LinkedList.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;

import javax.swing.*;

import static Function.globalVariable.fnc;

public class dbFunction {
    Connection conn;
    Statement stmt;
    PreparedStatement pstmt;
    ResultSet rs;

    public void loadDriver() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Unable to find and load driver", "Driver Error", 0);
        }
    }

    public Connection connectToDB() {
        try {
            loadDriver();
            String url = "jdbc:mysql://localhost:3306/librarydb";
            String user = "root";
            String password = "";
            Connection connect = DriverManager.getConnection(url, user, password);
            return connect;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public ArrayList<Category> retrieveCategories() {
        ArrayList<Category> categories = new ArrayList<Category>();
        try {
            conn = connectToDB();
            if (conn == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "You have not yet open the server", ButtonType.OK);
                alert.setTitle("Server Error");
                alert.show();
                return null;
            }

            String sqlGetCategory = "SELECT ctgry_id, ctgry_name FROM bkcategory";
            pstmt = conn.prepareStatement(sqlGetCategory);

            //Retrieve the category starting from id 1
            rs = pstmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("ctgry_id");
                String ctgryName = rs.getString("ctgry_name");
                categories.add(new Category(id, ctgryName));
            }
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.setTitle("RetrieveCategoryError");
            alert.show();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.setTitle("RetrieveCategoryError");
            alert.show();
        }
        return categories;
    }

    //LINKED LIST FUNCTION
    public DoublyLinkList retrieveBooksnOrder() {
        try {
            DoublyLinkList books = new DoublyLinkList();

            conn = connectToDB();
            if (conn == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "You have not yet open the server", ButtonType.OK);
                alert.setTitle("Server Error");
                alert.show();
                return null;
            }

            String sqlGetBook = "SELECT * FROM book";
            pstmt = conn.prepareStatement(sqlGetBook);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                //Retrieve all
                String title = rs.getString("title");
                String author = rs.getString("author");
                String imageSrc = rs.getString("imgID");
                String isbn = rs.getString("isbn");
                String category = rs.getString("ctgry");
                int quantity = rs.getInt("quantity");
                int borrowed = rs.getInt("borrowed");   //0 if available, signify the number of books borrowed

                Image img = fnc.getImage(imageSrc);

                Book nBook = new Book(title, author, category, img, isbn, quantity, borrowed);

                //Insert books in order
                books.insertNOrder(nBook);
            }
            return books;
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.setTitle("RetrieveBookError");
            alert.show();
            e.printStackTrace();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.setTitle("RetrieveBookError");
            alert.show();
            e.printStackTrace();
        }
        return null;
    }

    public int resetAutoIncrement(Connection conn, String table, String column) {
        int id = 0;
        try {
            conn = connectToDB();
            if (conn == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "You have not yet open the server", ButtonType.OK);
                alert.setTitle("Server Error");
                alert.show();
                return 0;
            }
            Statement stmt = conn.createStatement();
            String sqlChecker = "SELECT MAX(" + column + ") FROM " + table + " ;";
            ResultSet rs = stmt.executeQuery(sqlChecker);

            if (rs.next() && rs.getString("MAX(" + column + ")") != null) {
                String maxID = rs.getString("MAX(" + column + ")");
                id = Integer.parseInt(maxID);
            } else {
                id = 0;
            }
            String sqlAlterInc = "ALTER TABLE " + table + " AUTO_INCREMENT=" + (id + 1) + ";";
            stmt.executeUpdate(sqlAlterInc);
            return id + 1;
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.setTitle("ResetIncrementError");
            alert.show();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.setTitle("ResetIncrementError");
            alert.show();
        }
        return id + 1;
    }

    public boolean insertBookDB(Book book, String imgName) {
        try {
            conn = connectToDB();
            if (conn == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "You have not yet open the server", ButtonType.OK);
                alert.setTitle("Server Error");
                alert.show();
                return false;
            }
            String sqlInsertBook = "INSERT INTO librarydb.book" +
                    "(title, author, isbn, ctgry, quantity, borrowed, imgID)" +
                    "VALUES (?, ?, ?, ?, ?, 0, ?)";
            pstmt = conn.prepareStatement(sqlInsertBook);
            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getAuthor());
            pstmt.setString(3, book.getISBN());
            pstmt.setString(4, book.getCategory());
            pstmt.setInt(5, book.getQuantity());
            pstmt.setString(6, imgName);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.setTitle("InsertBookError");
            alert.show();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.setTitle("InsertBookError");
            alert.show();
        }
        return false;
    }

    public boolean modifyBookDB(Book book, String imgName) {
        try {
            conn = connectToDB();
            if (conn == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "You have not yet open the server", ButtonType.OK);
                alert.setTitle("Server Error");
                alert.show();
                return false;
            }
            String sqlModifyBook = "UPDATE librarydb.book SET title = ?, author = ?, isbn = ? , quantity = ?," +
                    " ctgry = ?, imgID = ? WHERE isbn = ?";
            pstmt = conn.prepareStatement(sqlModifyBook);
            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getAuthor());
            pstmt.setString(3, book.getISBN());
            pstmt.setInt(4, book.getQuantity());
            pstmt.setString(5, book.getCategory());
            pstmt.setString(6, imgName);
            pstmt.setString(7, book.getISBN());
            pstmt.executeUpdate();

            return true;
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.setTitle("ModiyBookError");
            alert.show();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.setTitle("ModifyBookError");
            alert.show();
        }
        return false;
    }

    public boolean removeBookDB(Book book, String isbn) {
        try {
            if(book.getBorrowed()>0) {
                conn = connectToDB();
                if (conn == null) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "You have not yet open the server", ButtonType.OK);
                    alert.setTitle("Server Error");
                    alert.show();
                    return false;
                }

                String sqlDeleteBook = "DELETE FROM librarydb.book WHERE isbn = ?";
                pstmt = conn.prepareStatement(sqlDeleteBook);
                pstmt.setString(1, isbn);
                pstmt.executeUpdate();
                return true;
            }else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Book is borrowed and cant be deleted", ButtonType.OK);
                alert.setTitle("DeleteBookError");
                alert.show();
            }

        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.setTitle("DeleteBookError");
            alert.show();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.setTitle("DeleteBookError");
            alert.show();
        }
        return false;
    }

    public boolean removeBookOneDB(String title, String isbn) {
        try {
            conn = connectToDB();
            if (conn == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Database connection not established.", ButtonType.OK);
                alert.setTitle("Connection Error");
                alert.show();
                return false;
            }

            String sqlUpdateBook = "UPDATE librarydb.book " +
                    "SET quantity = quantity - 1, borrowed = borrowed + 1 " +
                    "WHERE title = ? AND isbn = ? AND quantity > 0";

            pstmt = conn.prepareStatement(sqlUpdateBook);
            pstmt.setString(1, title);
            pstmt.setString(2, isbn);

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                return true;
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Book not available or insufficient quantity.", ButtonType.OK);
                alert.setTitle("Update Error");
                alert.show();
            }
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.setTitle("Database Error");
            alert.show();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.setTitle("Error");
            alert.show();
        }
        return false; // If any exception occurs, return false
    }

    public boolean addBookOneDB(String title, String isbn) {
        try {
            conn = connectToDB();
            if (conn == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Database connection not established.", ButtonType.OK);
                alert.setTitle("Connection Error");
                alert.show();
                return false;
            }

            String sqlUpdateBook = "UPDATE librarydb.book " +
                    "SET quantity = quantity + 1, borrowed = borrowed - 1 " +
                    "WHERE title = ? AND isbn = ? AND quantity > 0";

            pstmt = conn.prepareStatement(sqlUpdateBook);
            pstmt.setString(1, title);
            pstmt.setString(2, isbn);

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                return true;
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Book not available or insufficient quantity.", ButtonType.OK);
                alert.setTitle("Update Error");
                alert.show();
            }
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.setTitle("Database Error");
            alert.show();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.setTitle("Error");
            alert.show();
        }
        return false; // If any exception occurs, return false
    }


    public int getBookID(String bookTitle) {
        int bookID = 0;
        try {
            conn = connectToDB();
            if (conn == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "You have not yet opened the server", ButtonType.OK);
                alert.setTitle("Server Error");
                alert.show();
                return bookID;
            }

            String searchBookID = "SELECT book_id FROM librarydb.book WHERE title = ?";
            PreparedStatement pstmt = conn.prepareStatement(searchBookID);
            pstmt.setString(1, bookTitle);

            if (rs.next()) {
                bookID = rs.getInt("book_id");
            } else {
                System.err.println("No book found with title: " + bookTitle);
            }
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.setTitle("FindBookError");
            alert.show();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.setTitle("FindBookError");
            alert.show();
        }
        return bookID;
    }

    public String insertBookImageDB(Image img, String imgTitle) {
        String imgName = null;
        try {
            File directory = new File("src/bookImages");
            if (!directory.exists()) {
                directory.mkdirs();
            }

            imgName = imgTitle.replaceAll("\\s+", "_") + ".png";

            String imagePath = "src/bookImages/" + imgName;

            byte[] imgBytes = fnc.convertImageToByteArray(img);

            File imageFile = new File(imagePath);
            try (FileOutputStream fos = new FileOutputStream(imageFile)) {
                fos.write(imgBytes);
            }

            return imgName; // Return the name of the image file
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.setTitle("InsertBkImgError");
            alert.show();
            System.out.println(e.getMessage());
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.setTitle("InsertBkImgError");
            alert.show();
            System.out.println(e.getMessage());
        }
        return imgName;
    }

    public int insertStudentDB(Student student) {
        int staffId = 0;
        try {
            conn = connectToDB();
            if (conn == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "You have not yet open the server", ButtonType.OK);
                alert.setTitle("Server Error");
                alert.show();
                return 0;
            }
            String sqlInsertStudent = "INSERT INTO librarydb.student" +
                    "(school_id, fName, lName, section, email, password, penalty)" +
                    " VALUES (?, ?, ?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sqlInsertStudent);
            pstmt.setInt(1, student.getSchoolID());
            pstmt.setString(2, student.getFName());
            pstmt.setString(3, student.getLName());
            pstmt.setString(4, student.getSection());
            pstmt.setString(5, student.getEmail());
            pstmt.setString(6, student.getPass());
            pstmt.setDouble(7, student.getPenalty());

            pstmt.execute();
            return staffId + 1;
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.setTitle("Insert Student Error");
            alert.show();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.setTitle("Insert Student Error");
            alert.show();
        }
        return staffId;
    }

    public boolean modifyStudentDB(Student student, int id) {
        try {
            conn = connectToDB(); // Establish database connection
            if (conn == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "You have not yet opened the server", ButtonType.OK);
                alert.setTitle("Server Error");
                alert.show();
                return false; // Return false if the connection fails
            }

            // SQL statement for updating a student record
            String sqlUpdateStudent = "UPDATE librarydb.student " +
                    "SET school_id = ?, fName = ?, lName = ?, section = ?, email = ?, password = ?, penalty = ? " +
                    "WHERE school_id = ?";

            pstmt = conn.prepareStatement(sqlUpdateStudent); // Prepare the SQL statement
            pstmt.setInt(1, student.getSchoolID());          // Set school_id
            pstmt.setString(2, student.getFName());          // Set fName
            pstmt.setString(3, student.getLName());          // Set lName
            pstmt.setString(4, student.getSection());        // Set section
            pstmt.setString(5, student.getEmail());          // Set email
            pstmt.setString(6, student.getPass());           // Set password
            pstmt.setDouble(7, student.getPenalty());        // Set penalty
            pstmt.setInt(8, id);                             // Set the student ID for the WHERE clause

            int rowsUpdated = pstmt.executeUpdate(); // Execute the update

            if (rowsUpdated > 0) {
                return true; // Return true if the update was successful
            }
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.setTitle("Modify Student Error");
            alert.show();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.setTitle("Modify Student Error");
            alert.show();
        }
        return false; // Return false if the update fails
    }

    public boolean deleteStudentDB(int id) {
        try {
            conn = connectToDB(); // Establish database connection
            if (conn == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "You have not yet opened the server", ButtonType.OK);
                alert.setTitle("Server Error");
                alert.show();
                return false; // Return false if the connection fails
            }

            // SQL statement for updating a student record
            String sqlUpdateStudent = "DELETE FROM librarydb.student " +
                    "WHERE school_id = ?";

            pstmt = conn.prepareStatement(sqlUpdateStudent); // Prepare the SQL statement
            pstmt.setInt(1, id);          // Set school_id

            int rowsUpdated = pstmt.executeUpdate(); // Execute the update

            if (rowsUpdated > 0) {
                return true; // Return true if the update was successful
            }
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.setTitle("Delete Student DB Error");
            alert.show();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.setTitle("Delete Student DB Error");
            alert.show();
        }
        return false; // Return false if the update fails
    }

    public int insertStaffDB(Staff staff) {
        int staffId = 0;
        try {
            conn = connectToDB();
            if (conn == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "You have not yet open the server", ButtonType.OK);
                alert.setTitle("Server Error");
                alert.show();
                return 0;
            }
            String sqlInsertStudent = "INSERT INTO librarydb.staff" +
                    "(fName, lName, staff_UN, password)" +
                    " VALUES (?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sqlInsertStudent);
            pstmt.setString(1, staff.getFName());
            pstmt.setString(2, staff.getLName());
            pstmt.setString(3, staff.getUsername());
            pstmt.setString(4, staff.getPassword());

            pstmt.execute();
            return staffId + 1;
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.setTitle("Insert Student Error");
            alert.show();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.setTitle("Insert Student Error");
            alert.show();
        }
        return staffId;
    }

    public ObservableList<Book> inventoryBookView() {
        try {
            conn = connectToDB();
            if (conn == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "You have not yet open the server", ButtonType.OK);
                alert.setTitle("Server Error");
                alert.show();
                return null;
            }
            stmt = conn.createStatement();
            ObservableList<Book> inventoryBook = FXCollections.observableArrayList();
            String query = "SELECT * FROM book";
            // Execute the query
            rs = stmt.executeQuery(query);

            while (rs.next()) {
                String title = rs.getString("title");
                String author = rs.getString("author");
                String category = rs.getString("ctgry");
                String ISBN = rs.getString("isbn");
                int quantity = rs.getInt("quantity");
                int borrowed = rs.getInt("borrowed");

                // Create a new book object and add it to the list
                Book book = new Book(title, author, category, ISBN, quantity, borrowed);
                inventoryBook.add(book);
            }
            return inventoryBook;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.setTitle(title);
        alert.show();
    }

    public ArrayList<Transact> retrieveAllTransacts() {
        ArrayList<Transact> finishTrans = new ArrayList<>();
        int transId = 0;
        try {
            conn = connectToDB();
            if (conn == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "You have not yet open the server", ButtonType.OK);
                alert.setTitle("Server Error");
                alert.show();
                return null;
            }
            String sqlSortStaffDesc = "SELECT * FROM librarydb.transact";
            PreparedStatement pstmt = conn.prepareStatement(sqlSortStaffDesc);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Transact trans = new Transact();
                trans.setBorrowButton();
                trans.setReturnButton();
                trans.setTransID(rs.getInt("trans_id"));
                trans.setBorrowerID(rs.getInt("stud_id"));
                trans.setBorrowerName(rs.getString("stud_name"));
                trans.setBkIsbn(rs.getString("book_isbn"));
                trans.setBookTitle(rs.getString("book_title")); // Corrected setter name
                trans.setBorrowDate(rs.getDate("borrow_date"));
                trans.setStatus(rs.getString("status"));
                trans.setPenalty(rs.getDouble("penalty"));
                trans.setReturnDate(rs.getDate("return_date"));
                finishTrans.add(trans);
            }
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Transact Finish Error", e.getMessage());
        }
        return finishTrans;
    }

    public ArrayList<Staff> retrieveStaffAccount() {
        ArrayList<Staff> staffList = new ArrayList<>();
        try {
            conn = connectToDB();
            if (conn == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "You have not yet opened the server", ButtonType.OK);
                alert.setTitle("Server Error");
                alert.show();
                return null;
            }
            String sqlRetrieveStaff = "SELECT * FROM librarydb.staff WHERE staff_id != 1";
            PreparedStatement pstmt = conn.prepareStatement(sqlRetrieveStaff);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Staff staff = new Staff();
                staff.setStaffId(rs.getInt("staff_id"));
                staff.setFName(rs.getString("fName"));
                staff.setLName(rs.getString("lName"));
                staff.setUsername(rs.getString("staff_UN"));
                staff.setPassword(rs.getString("password"));
                staffList.add(staff);
            }
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Retrieve Staff Error", e.getMessage());
        }
        return staffList;
    }

    public boolean insertPendingTransact(Transact transact) {
        try {
            conn = connectToDB();
            if (conn == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "You have not yet opened the server", ButtonType.OK);
                alert.setTitle("Server Error");
                alert.show();
                return false;
            }

            String sqlInsertTransact = "INSERT INTO librarydb.transact (trans_id, stud_id, stud_name, book_title, book_isbn, status) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sqlInsertTransact);
            pstmt.setInt(1, transact.getTransID());
            pstmt.setInt(2, transact.getBorrowerID());
            pstmt.setString(3, transact.getBorrowerName());
            pstmt.setString(4, transact.getBookTitle());
            pstmt.setString(5, transact.getBkIsbn());
            pstmt.setString(6, "PENDING"); // Set the status to "PENDING"

            pstmt.executeUpdate();
            pstmt.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Insert Transact Error", e.getMessage());
        }
        return false;
    }

    public ArrayList<Student> retrieveStudentAccount() {
        ArrayList<Student> studentList = new ArrayList<>();
        try {
            conn = connectToDB();
            if (conn == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "You have not yet opened the server", ButtonType.OK);
                alert.setTitle("Server Error");
                alert.show();
                return null;
            }
            String sqlRetrieveStudent = "SELECT * FROM librarydb.student";
            PreparedStatement pstmt = conn.prepareStatement(sqlRetrieveStudent);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Student student = new Student();
                student.setSchoolID(rs.getInt("school_id"));
                student.setFName(rs.getString("fName"));
                student.setLName(rs.getString("lName"));
                student.setSection(rs.getString("section"));
                student.setEmail(rs.getString("email"));
                student.setPass(rs.getString("password"));
                student.setPenalty(rs.getDouble("penalty"));
                studentList.add(student);
            }
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Retrieve Student Error", e.getMessage());
        }
        return studentList;
    }

    public boolean updateTransactStatus(Transact transact, String newStatus) {
        try {
            conn = connectToDB();

            if (conn == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "You have not yet opened the server", ButtonType.OK);
                alert.setTitle("Server Error");
                alert.show();
                return false;
            }

            String sql = "UPDATE librarydb.transact SET status = ?, borrow_date = ? WHERE trans_id = ?";

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, newStatus);
            pstmt.setDate(2, fnc.getDateNow());
            pstmt.setInt(3, transact.getTransID());

            int rowsUpdated = pstmt.executeUpdate();

            if (rowsUpdated > 0) {
                pstmt.close();
                conn.close();
                return true;
            }
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Update Transaction Error", e.getMessage());
        }

        return false; // Return false if update was unsuccessful
    }

    public Staff searchStaffByLastName(String lastName) {
        Staff staff = null;
        try {
            conn = connectToDB();
            if (conn == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "You have not yet open the server", ButtonType.OK);
                alert.setTitle("Server Error");
                alert.show();
                return null;
            }
            String sqlSearch = "SELECT * FROM librarydb.staff WHERE lName = ?";
            pstmt = conn.prepareStatement(sqlSearch);
            pstmt.setString(1, lastName);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                String staffID = rs.getString("staff_UN");
                String fName = rs.getString("fName");
                String lName = rs.getString("lName");
                String password = rs.getString("password");
                staff = new Staff(fName, lName, staffID, password);
            }
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.setTitle("Search Staff by Last Name Error");
            alert.show();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.setTitle("Search Staff by Last Name Error");
            alert.show();
        }
        return staff;
    }

    public Staff searchStaffByID(String staffID) {
        Staff staff = null;
        try {
            conn = connectToDB();
            if (conn == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "You have not yet open the server", ButtonType.OK);
                alert.setTitle("Server Error");
                alert.show();
                return null;
            }
            String sqlSearch = "SELECT * FROM librarydb.staff WHERE staff_UN = ?";
            pstmt = conn.prepareStatement(sqlSearch);
            pstmt.setString(1, staffID);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                String fName = rs.getString("fName");
                String lName = rs.getString("lName");
                String password = rs.getString("password");
                staff = new Staff(fName, lName, staffID, password);
            }
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.setTitle("Search Staff by ID Error");
            alert.show();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.setTitle("Search Staff by ID Error");
            alert.show();
        }
        return staff;
    }

    public boolean updateStaffDB(Staff staff) {
        try {
            conn = connectToDB();
            if (conn == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "You have not yet open the server", ButtonType.OK);
                alert.setTitle("Server Error");
                alert.show();
                return false;
            }
            String sqlUpdate = "UPDATE librarydb.staff SET fName = ?, lName = ?, password = ? WHERE staff_UN = ?";
            pstmt = conn.prepareStatement(sqlUpdate);
            pstmt.setString(1, staff.getFName());
            pstmt.setString(2, staff.getLName());
            pstmt.setString(3, staff.getPassword());
            pstmt.setString(4, staff.getUsername());

            int rowsUpdated = pstmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.setTitle("Update Staff Error");
            alert.show();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.setTitle("Update Staff Error");
            alert.show();
        }
        return false;
    }

    public int deleteStaffDB(String staffID) {
        int result = 0;

        try {
            conn = connectToDB(); // Establish database connection
            if (conn == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "You have not yet opened the server", ButtonType.OK);
                alert.setTitle("Server Error");
                alert.show();
                return 0;
            }

            String sqlDeleteStaff = "DELETE FROM librarydb.staff WHERE staff_UN = ?";
            pstmt = conn.prepareStatement(sqlDeleteStaff);
            pstmt.setString(1, staffID);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                result = rowsAffected; // Number of rows deleted
            } else {
                throw new SQLException("No staff found with the provided ID or last name.");
            }

        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.setTitle("Delete Staff Error");
            alert.show();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.setTitle("Delete Staff Error");
            alert.show();
        }

        return result;
    }


}
