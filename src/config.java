import javax.swing.*;
import java.sql.*;

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
                    "stud_name VARCHAR(64) NOT NULL, " +
                    "book_isbn VARCHAR(24) NOT NULL, " +
                    "book_title VARCHAR(64) NOT NULL, " +
                    "borrow_date DATE, " +
                    "status VARCHAR(64) NOT NULL, " +
                    "penalty DOUBLE," +
                    "return_date DATE, " +
                    "PRIMARY KEY(trans_id))";
            stmt.executeUpdate(sqlTableTransact);
            System.out.println("Table 'transact' created succesfully");

            String sqlTableStaff = "CREATE TABLE IF NOT EXISTS librarydb.staff (" +
                    "staff_id INT NOT NULL AUTO_INCREMENT, " +
                    "fName VARCHAR(64) NOT NULL, " +
                    "lName VARCHAR(64) NOT NULL, " +
                    "staff_UN VARCHAR(64) NOT NULL," +
                    "password VARCHAR(64) NOT NULL, " +
                    "PRIMARY KEY(staff_id))";
            stmt.executeUpdate(sqlTableStaff);
            System.out.println("Table 'staff' created successfully");

            insertAdmin();
            insertCategoriesIfNotExist();
//            insertSampleBook();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Create Table Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Create Table Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void insertAdmin() {
        // Add admin account in staff
        try {
            conn = dbFunct.connectToDB();
            if (conn == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "You have not yet opened the server", ButtonType.OK);
                alert.setTitle("Server Error");
                alert.show();
                return;
            }
            stmt = conn.createStatement();

            // Check if admin account already exists
            String sqlCheckAdmin = "SELECT COUNT(*) AS count FROM staff WHERE staff_UN = 'Admin123'";
            ResultSet rs = stmt.executeQuery(sqlCheckAdmin);
            if (rs.next() && rs.getInt("count") > 0) {
                System.out.println("Admin is setup already");
                return;
            }

            // Insert admin account
            String sqlAddAdmin = "INSERT INTO staff(staff_id, fName, lName, staff_UN, password) VALUES (0, 'Admin', 'N/A', 'Admin123', 'admin@123')";
            stmt.executeUpdate(sqlAddAdmin);
            System.out.println("Admin successfully setup");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "ADMIN SETUP ERROR", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "ADMIN SETUP ERROR", JOptionPane.ERROR_MESSAGE);
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

    public void insertCategoriesIfNotExist() {
        Connection conn = null;
        PreparedStatement checkStmt = null;
        Statement insertStmt = null;

        try {
            conn = dbFunct.connectToDB();

            if (conn == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "You have not yet opened the server", ButtonType.OK);
                alert.setTitle("Server Error");
                alert.show();
                return;
            }

            String checkQuery = "SELECT COUNT(*) FROM bkcategory WHERE ctgry_name IN ('Fiction', 'Non-Fiction', 'Academic', 'Childrens', 'Philosophy', 'Comics')";
            checkStmt = conn.prepareStatement(checkQuery);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next() && rs.getInt(1) == 0) {
                String sqlInsertCategory = "INSERT INTO bkcategory(ctgry_name) " +
                        "VALUES ('Fiction'), ('Non-Fiction'), ('Academic'), ('Childrens'), ('Philosophy'), ('Comics')";
                insertStmt = conn.createStatement();
                insertStmt.executeUpdate(sqlInsertCategory);
                System.out.println("Categories successfully inserted.");
            } else {
                System.out.println("Categories already exist");
            }

            rs.close();
            checkStmt.close();
            if (insertStmt != null) insertStmt.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(config::new);
    }
}


