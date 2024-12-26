package Function;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.UUID;

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
        }catch(Exception e) {
            JOptionPane.showMessageDialog(null, "Unable to find and load driver", "Driver Error", 0);
        }
    }

    public Connection connectToDB() {
        try{
            loadDriver();
            String url = "jdbc:mysql://localhost:3306/librarydb";
            String user = "root";
            String password = "";
            Connection connect = DriverManager.getConnection(url, user, password);
            return connect;
        }catch(SQLException e) {
            System.out.println(e.getMessage());
        }catch(Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public ArrayList<Category> retrieveCategories() {
        ArrayList<Category> categories = new ArrayList<Category>();
        try{
            conn = connectToDB();
            if(conn==null) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "You have not yet open the server", ButtonType.OK);
                alert.setTitle("Server Error");
                alert.show();
                return null;
            }

            String sqlGetCategory = "SELECT ctgry_id, ctgry_name FROM bkcategory";
            pstmt = conn.prepareStatement(sqlGetCategory);

            //Retrieve the category starting from id 1
            rs = pstmt.executeQuery();
            while(rs.next()) {
                int id = rs.getInt("ctgry_id");
                String ctgryName = rs.getString("ctgry_name");
                categories.add(new Category(id, ctgryName));
            }
        }catch(SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.setTitle("RetrieveCategoryError");
            alert.show();
        }catch(Exception e) {
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
            if(conn==null) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "You have not yet open the server", ButtonType.OK);
                alert.setTitle("Server Error");
                alert.show();
                return null;
            }

            String sqlGetBook = "SELECT * FROM book";
            pstmt = conn.prepareStatement(sqlGetBook);
            rs = pstmt.executeQuery();
            while(rs.next()) {
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
        }catch(SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.setTitle("RetrieveBookError");
            alert.show();
            e.printStackTrace();
        }catch(Exception e) {
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
            if(conn==null) {
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
            return id+1;
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.setTitle("ResetIncrementError");
            alert.show();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.setTitle("ResetIncrementError");
            alert.show();
        }
        return id+1;
    }

    public boolean insertBookDB(Book book, String imgName) {
        try{
            conn = connectToDB();
            if(conn==null) {
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
        }catch(SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.setTitle("InsertBookError");
            alert.show();
        }catch(Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.setTitle("InsertBookError");
            alert.show();
        }
        return false;
    }

    public boolean removeBookDB(String title, String isbn) {
        try{
            conn = connectToDB();
            if(conn==null) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "You have not yet open the server", ButtonType.OK);
                alert.setTitle("Server Error");
                alert.show();
                return false;
            }

            String sqlDeleteBook = "DELETE FROM librarydb.book WHERE title = ? AND isbn = ?";
            pstmt = conn.prepareStatement(sqlDeleteBook);
            pstmt.setString(1, title);
            pstmt.setString(2, isbn);
            pstmt.executeUpdate();
            return true;
        }catch(SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.setTitle("DeleteBookError");
            alert.show();
        }catch(Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.setTitle("DeleteBookError");
            alert.show();
        }
        return false;
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
        try{
            conn = connectToDB();
            if(conn==null) {
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
            pstmt.setString(2, student.getfName());
            pstmt.setString(3, student.getlName());
            pstmt.setString(4, student.getSection());
            pstmt.setString(5, student.getEmail());
            pstmt.setString(6, student.getPass());
            pstmt.setDouble(7, student.getPenalty());

            pstmt.execute();
            return staffId+1;
        }catch(SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.setTitle("Insert Student Error");
            alert.show();
        }catch(Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.setTitle("Insert Student Error");
            alert.show();
        }
        return staffId;
    }

    public ObservableList<Book> inventoryBookView() {
        try {
            conn = connectToDB();
            if(conn==null) {
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
        int transId=0;
        try {
            conn = connectToDB();
            if(conn==null) {
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
                staff.setfName(rs.getString("fName"));
                staff.setlName(rs.getString("lName"));
                staff.setEmail(rs.getString("email"));
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
                student.setfName(rs.getString("fName"));
                student.setlName(rs.getString("lName"));
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
            pstmt.setDate(2, globalVariable.fnc.getDateNow());
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


}
