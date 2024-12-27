import LinkedList.DoublyLinkList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import stages.login.LoginController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import Function.*;

import java.io.IOException;
import java.sql.Connection;

import Function.globalVariable;
public class index extends Application {
    @Override
    public void start(Stage stage) throws Exception {

        try {
            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("stages/login/logFXML/login_view.fxml"));
            Parent root = loader.load();

            // Set stage title and icon
            stage.setTitle("LMS!");
            Image icon = new Image(getClass().getResource("icons/icon_LMS.png").toExternalForm());
            stage.getIcons().add(icon);

            // Get the controller instance
            LoginController controller = loader.getController();

            // Create and configure the scene
            Scene scene = new Scene(root);
            scene.setOnKeyPressed(event -> {
                if (event.isControlDown() && event.isShiftDown() && event.getCode() == KeyCode.S) {
                    try {
                        // Trigger admin login
                        controller.adminLogin(event);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });

            // Configure the stage
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();

            Connection conn = globalVariable.dbFnc.connectToDB();
            if(conn==null) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "You have not yet open the server", ButtonType.OK);
                alert.setTitle("Server Error");
                alert.show();
                return;
            }

            //Copy the book in database
            globalVariable.bookList = globalVariable.dbFnc.retrieveBooksnOrder();
            System.out.println("Book collected: " + globalVariable.bookList.getSize());
            globalVariable.transactList = globalVariable.dbFnc.retrieveAllTransacts();
            System.out.println("Transact made: " + globalVariable.transactList.size());
            globalVariable.sortedStaffListASC = globalVariable.dbFnc.retrieveStaffAccount();
            System.out.println("Staff number: " + globalVariable.sortedStaffListASC.size());
            globalVariable.sortedStudentListASC = globalVariable.dbFnc.retrieveStudentAccount();
            System.out.println("Student number: " +globalVariable.sortedStudentListASC.size());
            globalVariable.categoryList = globalVariable.dbFnc.retrieveCategories();
            System.out.println("Category number: " +globalVariable.categoryList.size());
        }catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.setTitle("Indexing Error");
            alert.show();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
