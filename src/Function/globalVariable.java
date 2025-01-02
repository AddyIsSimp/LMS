package Function;

import Entity.*;
import LinkedList.DoublyLinkList;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.TableView;

import java.sql.Date;
import java.util.ArrayList;

public class globalVariable {
    //GLOBAL
    public static Function fnc = new Function();
    public static Date globalDate = fnc.getDateNow();
    public static dbFunction dbFnc = new dbFunction();
    public static ArrayList<Transact> transactList = new ArrayList<Transact>();;
    public static ArrayList<Category> categoryList = new ArrayList<Category>();

    //LIBRARY
    public static Group cabinets = new Group();
    public static DoublyLinkList bookList = new DoublyLinkList();
    public static boolean isLibraryView = false;       //this is for inventory if false it should be tabular

    //ADMIN
    public static ArrayList<Staff> sortedStaffListASC = new ArrayList<>();
    public static ArrayList<Student> sortedStudentListASC = new ArrayList<>();
    public static Staff searchStaff;
    public static Student searchStudent;

    //ADMIN & STAFF
    public static Book modifyBook;

    //STUDENT
    public static Student loginStudent;
    public static Transact studentBookBorrowed;
}
