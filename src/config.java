import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import Function.*;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class config {
    Connection conn;
    Statement stmt;

    dbFunction dbFunct = new dbFunction();
    Function funct = new Function();

    public config() {
        createDB();
    }

    public void createDB() {
        try {

            //Establish connection
            String url = "jdbc:mysql://localhost:3306/";
            String user = "root";
            String password = "";
            conn = DriverManager.getConnection(url, user, password);
            stmt = conn.createStatement();

            //Create database
            String sqlCreateDB = "CREATE DATABASE IF NOT EXISTS librarydb";
            stmt.executeUpdate(sqlCreateDB);
            System.out.println("Database created successfully");

            //Switched to library db
            String sqlUseDb = "USE librarydb";
            stmt.executeUpdate(sqlUseDb);
            System.out.println("Switched to database librarydb");

            //Create tables
            createTable();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Create Database Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Create Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void createTable() {
        try {
            //Establish connection
            String url = "jdbc:mysql://localhost:3306/librarydb";
            String user = "root";
            String password = "";
            conn = DriverManager.getConnection(url, user, password);
            stmt = conn.createStatement();

            String sqlTableCategory = "CREATE TABLE IF NOT EXISTS librarydb.bkcategory (" +
                    "ctgry_id INT NOT NULL AUTO_INCREMENT, " +
                    "ctgry_name VARCHAR(64) NOT NULL," +
                    "PRIMARY KEY(ctgry_id))";
            stmt.executeUpdate(sqlTableCategory);
            System.out.println("Table 'category' created successfully");

            //Create Book Table
            String sqlTableBook = "CREATE TABLE IF NOT EXISTS librarydb.book (" +
                    "book_id INT NOT NULL AUTO_INCREMENT," +
                    "title VARCHAR(64) NOT NULL," +
                    "author VARCHAR(64) NOT NULL," +
                    "isbn VARCHAR(24) NOT NULL," +
                    "quantity INT(24) NOT NULL," +
                    "borrowed INT NOT NULL," +
                    "ctgry VARCHAR(24) NOT NULL," +
                    "imgID VARCHAR(64) NOT NULL, " +
                    "PRIMARY KEY(book_id));";
            stmt.executeUpdate(sqlTableBook);
            System.out.println("Table 'book' created succesfully");

            String sqlTableStudent = "CREATE TABLE IF NOT EXISTS librarydb.student (" +
                    "school_id INT NOT NULL AUTO_INCREMENT, " +
                    "fName VARCHAR(64) NOT NULL, " +
                    "lName VARCHAR(64) NOT NULL, " +
                    "section VARCHAR(64) NOT NULL, " +
                    "email VARCHAR(64) NOT NULL, " +
                    "password VARCHAR(64) NOT NULL, " +
                    "penalty DOUBLE NOT NULL, " +
                    "PRIMARY KEY(school_id))";
            stmt.executeUpdate(sqlTableStudent);
            System.out.println("Table 'student' created successfully");

            String setAutoIncrement = "ALTER TABLE student AUTO_INCREMENT=202400000;";

            String sqlTableTransact = "CREATE TABLE IF NOT EXISTS librarydb.transact (" +
                    "trans_id INT NOT NULL AUTO_INCREMENT, " +
                    "stud_id INT NOT NULL, " +
                    "book_id INT , " +
                    "borrow_date DATE NOT NULL, " +
                    "penalty DOUBLE," +
                    "return_date DATE, " +
                    "status VARCHAR(64) NOT NULL, " +
                    "FOREIGN KEY(book_id) REFERENCES book(book_id)," +
                    "PRIMARY KEY(trans_id))";
            stmt.executeUpdate(sqlTableTransact);
            System.out.println("Table 'transact' created succesfully");

            String sqlTableStaff = "CREATE TABLE IF NOT EXISTS librarydb.staff (" +
                    "staff_id VARCHAR(64) NOT NULL, " +
                    "fName VARCHAR(64) NOT NULL, " +
                    "lName VARCHAR(64) NOT NULL, " +
                    "email VARCHAR(64) NOT NULL, " +
                    "password VARCHAR(64) NOT NULL, " +
                    "PRIMARY KEY(staff_id))";
            stmt.executeUpdate(sqlTableStaff);
            System.out.println("Table 'staff' created successfully");

            String sqlInsertCategory = "INSERT INTO bkcategory(ctgry_name) values (\"Fiction\"),(\"Non-Fiction\"),(\"Academic\"),(\"Childrens\"),(\"Philosophy\"),(\"Comics\");";
            stmt.executeUpdate(sqlInsertCategory);
            System.out.println("Category created");

            insertAdmin();
//            insertSampleBook();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Create Table Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Create Table Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void insertAdmin() { //Add admin account in staff
        try {
            conn = dbFunct.connectToDB();
            stmt = conn.createStatement();

            String sqlAddAdmin = "INSERT INTO staff(staff_id, fName, lName, email, password) VALUES (0, 'Admin123', 'N/A', 'N/A', 'admin@123')";
            stmt.executeUpdate(sqlAddAdmin);
            System.out.println("Admin successfully setup");
        }catch(SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "INSERT BOOK ERROR", JOptionPane.ERROR_MESSAGE);
        }catch(Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "INSERT BOOK ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void retrieveBooks() {
        try {
            globalVariable.bookList = globalVariable.dbFnc.retrieveBooksnOrder();
        }catch(Exception e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, e.getMessage(), ButtonType.NO, ButtonType.YES);
            alert.setTitle("Retrieve Books Error");
            alert.show();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(config::new);
    }
}


