package stages.login;

import Entity.Student;
import javafx.animation.*;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.image.ImageView;
import javafx.event.ActionEvent;
import javafx.animation.Interpolator;

import java.io.IOException;

import Function.*;

import java.sql.*;


import static Function.globalVariable.globalDate;
import static Function.globalVariable.loginStudent;

public class LoginController {

    Connection conn;
    PreparedStatement pstmt;
    ResultSet rs;

    Function fnc = new Function();
    dbFunction dbFunc = new dbFunction();

    private Stage stage;
    private Scene scene;
    private Parent root;


    //@FXML=======================================================================================================================================================================================
    @FXML
    private AnchorPane adminRoot, staffRoot, userRoot, loginRoot, setFrame;
    @FXML
    private Button passButton, staffLog, userLog, signInStudent, exit;
    @FXML
    private PasswordField passwordtextfield, passwordtextfield2;
    @FXML
    private TextField passwordTextVisible, passwordTextVisible2, tf_staffid, tfAdminName, studentIDField;
    @FXML
    private ImageView passwordIcon, passwordIcon2;
    @FXML
    private Label dateHere;
    @FXML
    private TextField studentIDFld, fNameFld, lNameFld, sectionFld, emailFld, passwordTextVisible1;
    @FXML
    private Label errorText;
//EXIT=======================================================================================================================================================================================


//PASSWORD_TEXT=======================================================================================================================================================================================

    private boolean isPasswordVisible = false;

    @FXML
    public void togglePasswordVisibility() {
        if (isPasswordVisible) {
            // Hide plain text and show PasswordField
            passwordTextVisible.setVisible(false);
            passwordtextfield.setText(passwordTextVisible.getText());
            passwordtextfield.setVisible(true);

            // Update the icon
            passwordIcon.setImage(new Image(getClass().getResource("/icons/passIconeyeshow.png").toExternalForm()));
        } else {
            // Show plain text and hide PasswordField
            passwordtextfield.setVisible(false);
            passwordTextVisible.setText(passwordtextfield.getText());
            passwordTextVisible.setVisible(true);

            // Update the icon
            passwordIcon.setImage(new Image(getClass().getResource("/icons/passIconeyehide.png").toExternalForm()));
        }
        isPasswordVisible = !isPasswordVisible;
    }

    boolean isPasswordVisible2 = false;

    @FXML
    public void togglePasswordVisibility2() {
        if (isPasswordVisible2) {
            // Hide plain text and show PasswordField
            passwordTextVisible2.setVisible(false);
            passwordtextfield2.setText(passwordTextVisible2.getText());
            passwordtextfield2.setVisible(true);

            // Update the icon
            passwordIcon2.setImage(new Image(getClass().getResource("/icons/passIconeyeshow.png").toExternalForm()));
        } else {
            // Show plain text and hide PasswordField
            passwordtextfield2.setVisible(false);
            passwordTextVisible2.setText(passwordtextfield2.getText());
            passwordTextVisible2.setVisible(true);

            // Update the icon
            passwordIcon2.setImage(new Image(getClass().getResource("/icons/passIconeyehide.png").toExternalForm()));
        }
        isPasswordVisible2 = !isPasswordVisible2;
    }

//Login_Main=======================================================================================================================================================================================

    @FXML
    private void switchLogin(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("logFXML/login_view.fxml"));

        // Get the current scene and its width
        Scene scene = exit.getScene();
        double sceneWidth = scene.getWidth();

        // Set the initial position of the new root outside the left edge
        root.translateXProperty().set(-sceneWidth);

        // Get the parent container (assuming AnchorPane is the root)
        AnchorPane loginRoot = (AnchorPane) scene.getRoot();

        // Add the new root to the parent container
        loginRoot.getChildren().add(root);

        // Create a timeline animation for sliding the new scene into view
        Timeline timeline = new Timeline();

        // Animate the new root sliding in from the left to the center
        KeyValue kvIn = new KeyValue(root.translateXProperty(), 0, Interpolator.EASE_BOTH);
        KeyFrame kfIn = new KeyFrame(Duration.seconds(0.6), kvIn);
        timeline.getKeyFrames().add(kfIn);

        // When the animation finishes, remove the old root (previous scene)
        timeline.setOnFinished(t -> {
            if (!loginRoot.getChildren().isEmpty()) {
                Node oldScene = loginRoot.getChildren().get(0); // First child is the old scene
                loginRoot.getChildren().remove(oldScene);      // Remove the old scene
            }
        });

        // Play the animation
        timeline.play();
    }

//Login_STAFF=======================================================================================================================================================================================

    public void switchStaff(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("logFXML/staffLogin_view.fxml"));
        Scene currentScene = ((Node) event.getSource()).getScene();
        double sceneWidth = currentScene.getWidth();

        root.translateXProperty().set(sceneWidth);
        loginRoot.getChildren().add(root);

        Timeline timeline = new Timeline();
        KeyValue kvIn = new KeyValue(root.translateXProperty(), 0, Interpolator.EASE_BOTH);
        KeyFrame kfIn = new KeyFrame(Duration.seconds(0.6), kvIn);
        timeline.getKeyFrames().add(kfIn);

        timeline.setOnFinished(t -> {
            Node container = loginRoot.getChildren().get(0);
            loginRoot.getChildren().remove(container);
        });

        timeline.play();
    }

    //STAFF LOGIN
    public void staffLoginEvt(ActionEvent event) throws Exception {
        try {
            conn = dbFunc.connectToDB();
            if (conn == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "You have not yet open the server", ButtonType.OK);
                alert.setTitle("Server Error");
                alert.show();
                return;
            }

            String id = tf_staffid.getText();
            String password = passwordtextfield.getText();
            String passwordVisible = passwordTextVisible.getText();
            if (id.isEmpty()) {
                errorText.setText("Staff ID is empty");
                return;
            } else if (fnc.staffIDChecker(id) == false) {
                errorText.setText("Staff ID format is wrong");
                return;
            } else if (password.isEmpty()) {
                errorText.setText("Password is empty");
                return;
            }

            String sqlFindStaff = "SELECT * FROM staff WHERE staff_UN = ? AND password = ? AND staff_id != 1";
            pstmt = conn.prepareStatement(sqlFindStaff);
            pstmt.setString(1, id);
            if (isPasswordVisible) {
                pstmt.setString(2, passwordVisible);
            }else{
                pstmt.setString(2, password);
            }

            rs = pstmt.executeQuery();
            if (rs.next()) {
                Parent root = FXMLLoader.load(getClass().getResource("/stages/staff/staffFXML/staff_dashboard.fxml"));
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            } else {
                Alert alert = new Alert(Alert.AlertType.NONE, "Staff login not successful", ButtonType.OK);
                alert.setTitle("Login");
                alert.show();
            }

        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.NONE, e.getMessage(), ButtonType.OK);
            alert.setTitle("Login Fail");
            alert.show();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.NONE, e.getMessage(), ButtonType.OK);
            alert.setTitle("Login Fail");
            alert.show();
        }
    }

    //ADMIN LOGIN
    public void adminLoginEvt(ActionEvent event) throws Exception {
        try {
            conn = dbFunc.connectToDB();
            if (conn == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "You have not yet open the server", ButtonType.OK);
                alert.setTitle("Server Error");
                alert.show();
                return;
            }

            String username = tfAdminName.getText();
            String password = passwordtextfield.getText();
            String passwordVisible = passwordTextVisible.getText();


            //Use the fName and password for admin login
            String sqlFindStaff = "SELECT * FROM staff WHERE staff_UN = ? AND password = ? AND staff_id = 1";
            pstmt = conn.prepareStatement(sqlFindStaff);
            pstmt.setString(1, username);
            if (isPasswordVisible) {
                pstmt.setString(2, passwordVisible);
            }else{
                pstmt.setString(2, password);
            }
            rs = pstmt.executeQuery();
            if (rs.next()) {
                Parent root = FXMLLoader.load(getClass().getResource("/stages/admin/adminFXML/admin_dashboard.fxml"));
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            } else {
                Alert alert = new Alert(Alert.AlertType.NONE, "Wrong ID or password", ButtonType.OK);
                alert.setTitle("Login Failed");
                alert.show();
            }

        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.NONE, e.getMessage(), ButtonType.OK);
            alert.setTitle("Login Error");
            alert.show();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.NONE, e.getMessage(), ButtonType.OK);
            alert.setTitle("Login Error");
            alert.show();
        }
    }

    @FXML
    private void loginStudent(ActionEvent event) {
        try {
            conn = dbFunc.connectToDB();
            if (conn == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "You have not yet open the server", ButtonType.OK);
                alert.setTitle("Server Error");
                alert.show();
                return;
            }

            String studentID = studentIDField.getText();
            String password = passwordtextfield.getText();
            String passwordVisible = passwordTextVisible.getText();

            //Use the fName and password for admin login
            if (studentID.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.NONE, "Missing ID", ButtonType.OK);
                alert.setTitle("Login");
                alert.show();
                return;
            }
            if (password.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.NONE, "Missing password", ButtonType.OK);
                alert.setTitle("Login");
                alert.show();
                return;
            }

            studentID = fnc.retrieveStudentID(studentID);   //Retrieve the ID in correct formatt
            if (studentID == null) {//If wrong format
                Alert alert = new Alert(Alert.AlertType.NONE, "Invalid ID format", ButtonType.OK);
                alert.setTitle("Login");
                alert.show();
                return;
            }

            String sqlFindStaff = "SELECT * FROM student WHERE school_id = ? AND password = ?";
            pstmt = conn.prepareStatement(sqlFindStaff);
            pstmt.setString(1, studentID);
            if (isPasswordVisible) {
                pstmt.setString(2, passwordVisible);
            }else{
                pstmt.setString(2, password);
            }
            pstmt.setString(2, password);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                int school_id = rs.getInt("school_id");
                String fName = rs.getString("fName");
                String lName = rs.getString("lName");
                String section = rs.getString("section");
                String email = rs.getString("email");
                String pass = rs.getString("password");
                Double penalty = rs.getDouble("penalty");

                globalVariable.loginStudent = fnc.getStudentWithName(globalVariable.sortedStudentListASC, school_id);
                if(loginStudent==null) {
                    Alert alert = new Alert(Alert.AlertType.NONE, "Student data not retrieve", ButtonType.OK);
                    alert.setTitle("Login Failed");
                    alert.show();
                }

                Parent root = FXMLLoader.load(getClass().getResource("/stages/student/studentFXML/student_borrowBooks.fxml"));
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            } else {
                Alert alert = new Alert(Alert.AlertType.NONE, "Student not found", ButtonType.OK);
                alert.setTitle("Login Failed");
                alert.show();
            }
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.NONE, e.getMessage(), ButtonType.OK);
            alert.setTitle("Login Failed");
            alert.show();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.NONE, e.getMessage(), ButtonType.OK);
            alert.setTitle("Login Failed");
            alert.show();
        }
    }

    @FXML
    private void registerStudent(ActionEvent event) {
        String studentIDStr = studentIDFld.getText();
        String fName = fNameFld.getText();
        String lName = lNameFld.getText();
        String email = emailFld.getText();
        String section = sectionFld.getText();
        String pass = passwordtextfield.getText();
        String confirmPass = passwordtextfield2.getText();
        int studentID = 0;

        if (studentIDStr.isEmpty()) {
            errorText.setText("Student ID is missing");
            return;
        }
        if (fnc.retrieveStudentID(studentIDStr) != null) {
            studentID = Integer.parseInt(fnc.retrieveStudentID(studentIDStr));
        } else {
            errorText.setText("Incorrect ID format (NNNN-NNNNN)");
            return;
        }

        if (fName.isEmpty()) {
            errorText.setText("First name is missing");
            return;
        } else if (lName.isEmpty()) {
            errorText.setText("Last name is missing");
            return;
        } else if (email.isEmpty()) {
            errorText.setText("Email is missing");
            return;
        } else if (section.isEmpty()) {
            errorText.setText("Email is missing");
            return;
        } else if (pass.isEmpty()) {
            errorText.setText("Password is missing");
            return;
        } else if (confirmPass.isEmpty()) {
            errorText.setText("Confirm password is missing");
            return;
        }

        if (fnc.passwordChecker(pass) == false) {
            errorText.setText("Password is atleast 8 characters with letter and number or special character");
            return;
        }
        if (pass.equals(confirmPass) == false) {
            errorText.setText("Password does not match");
            return;
        }

        Student newStudent = new Student(studentID, fName, lName, section, email, pass);
        studentID = dbFunc.insertStudentDB(newStudent);

        if (studentID != 0) {
            globalVariable.sortedStudentListASC.add(newStudent);
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Student registered successfully");
            alert.setTitle("Student Registration");
            alert.showAndWait();
            try {
                switchUsers(event);
            } catch (IOException evt) {
                Alert alertError = new Alert(Alert.AlertType.ERROR, "Fail to back in Login");
                alert.setContentText(evt.getMessage());
                alert.showAndWait();
            }
        } else {
            errorText.setText("Student Registration Fail");
            return;
        }
    }

//Login_USER=======================================================================================================================================================================================

    @FXML
    public void switchUser(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("logFXML/userLogin_view.fxml"));

        // Get the current scene and its width
        Scene currentScene = ((Node) event.getSource()).getScene();
        double sceneWidth = currentScene.getWidth();

        // Set initial position of the new root off-screen to the right
        root.translateXProperty().set(sceneWidth);

        // Add the new root to the container
        loginRoot.getChildren().add(root);

        // Create the animation timeline
        Timeline timeline = new Timeline();
        KeyValue kvIn = new KeyValue(root.translateXProperty(), 0, Interpolator.EASE_BOTH);
        KeyFrame kfIn = new KeyFrame(Duration.seconds(0.6), kvIn);
        timeline.getKeyFrames().add(kfIn);

        // When the animation finishes
        timeline.setOnFinished(t -> {
            Node container = loginRoot.getChildren().get(0); // Get the old scene
            loginRoot.getChildren().remove(container);      // Remove the old scene
        });

        // Play the animation
        timeline.play();

    }

    public void switchSignInUser(ActionEvent event) throws IOException {
        // Load the new FXML file for the student sign-in view
        FXMLLoader loader = new FXMLLoader(getClass().getResource("logFXML/signInStudent.fxml"));
        Parent root = loader.load();
        AnchorPane loginRoot = (AnchorPane) ((Node) event.getSource()).getScene().getRoot();
        double sceneWidth = loginRoot.getWidth();
        root.translateXProperty().set(sceneWidth);

        loginRoot.getChildren().add(root);

        // Create the animation timeline for sliding in the new view
        Timeline timeline = new Timeline();

        // KeyFrame for sliding the new scene into view
        KeyValue kvIn = new KeyValue(root.translateXProperty(), 0, Interpolator.EASE_BOTH);
        KeyFrame kfIn = new KeyFrame(Duration.seconds(0.6), kvIn);
        timeline.getKeyFrames().add(kfIn);

        // Remove the old view when the animation completes
        timeline.setOnFinished(t -> {
            Node oldView = loginRoot.getChildren().get(0); // The old view is always at index 0
            loginRoot.getChildren().remove(oldView);      // Remove it from the container
        });

        // Play the animation
        timeline.play();
    }

    @FXML
    private void switchUsers(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("logFXML/userLogin_view.fxml"));

        // Get the current scene and its width
        Scene scene = exit.getScene();
        double sceneWidth = scene.getWidth();

        root.translateXProperty().set(-sceneWidth);
        AnchorPane loginRoot = (AnchorPane) scene.getRoot();

        // Add the new root to the parent container
        loginRoot.getChildren().add(root);

        // Create a timeline animation for sliding the new scene into view
        Timeline timeline = new Timeline();

        // Animate the new root sliding in from the left to the center
        KeyValue kvIn = new KeyValue(root.translateXProperty(), 0, Interpolator.EASE_BOTH);
        KeyFrame kfIn = new KeyFrame(Duration.seconds(0.6), kvIn);
        timeline.getKeyFrames().add(kfIn);

        // When the animation finishes, remove the old root (previous scene)
        timeline.setOnFinished(t -> {
            if (!loginRoot.getChildren().isEmpty()) {
                Node oldScene = loginRoot.getChildren().get(0); // First child is the old scene
                loginRoot.getChildren().remove(oldScene);      // Remove the old scene
            }
        });

        // Play the animation
        timeline.play();
    }

//Login_ADMIN=======================================================================================================================================================================================

    @FXML
    public void adminLogin(KeyEvent event) throws IOException {


        // Load the new scene (admin login view)
        Parent newRoot = FXMLLoader.load(getClass().getResource("logFXML/adminLogin_view.fxml"));

        // Set the initial position of the new root off-screen (to the left)
        newRoot.translateXProperty().set(-loginRoot.getWidth());

        // Add the new root to the parent container
        loginRoot.getChildren().add(newRoot);

        // Create timeline animations for both scenes
        Timeline timeline = new Timeline();

        // Slide out the current scene (move it to the right)
        Node currentScene = loginRoot.getChildren().get(0); // Assume the first child is the current scene
        KeyValue slideOutCurrent = new KeyValue(currentScene.translateXProperty(), loginRoot.getWidth(), Interpolator.EASE_BOTH);

        // Slide in the new scene (move it to the center)
        KeyValue slideInNew = new KeyValue(newRoot.translateXProperty(), 0, Interpolator.EASE_BOTH);

        // Combine animations into a single keyframe
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.6), slideOutCurrent, slideInNew);
        timeline.getKeyFrames().add(keyFrame);

        // Remove the old scene after the animation completes
        timeline.setOnFinished(t -> loginRoot.getChildren().remove(currentScene));

        // Play the animation
        timeline.play();

    }

    @FXML
    public void switchLogins(ActionEvent event) throws IOException {


        Parent root = FXMLLoader.load(getClass().getResource("logFXML/login_view.fxml"));


        // Get the current scene and its width
        Scene scene = exit.getScene();
        double sceneWidth = scene.getWidth();

        // Set the initial position of the new root off-screen to the right
        root.translateXProperty().set(sceneWidth);

        // Get the parent container (AnchorPane)
        AnchorPane loginRoot = (AnchorPane) exit.getScene().getRoot();

        // Add the new root to the parent container
        loginRoot.getChildren().add(root);

        // Create the animation timeline for the new scene sliding in from the right
        Timeline timeline = new Timeline();

        // Animate the new scene sliding in from the right to the center
        KeyValue kvIn = new KeyValue(root.translateXProperty(), 0, Interpolator.EASE_BOTH);
        KeyFrame kfIn = new KeyFrame(Duration.seconds(0.6), kvIn); // Slide in faster and smoother
        timeline.getKeyFrames().add(kfIn);

        // Animate the old scene sliding out to the left
        KeyValue kvOut = new KeyValue(loginRoot.getChildren().get(0).translateXProperty(), -sceneWidth, Interpolator.EASE_BOTH);
        KeyFrame kfOut = new KeyFrame(Duration.seconds(0.6), kvOut); // Slide out smoothly
        timeline.getKeyFrames().add(kfOut);

        // When the animation finishes, remove the old scene from the container
        timeline.setOnFinished(t -> {
            // Get the old scene (first child) and remove it
            Node oldRoot = loginRoot.getChildren().get(0); // Assuming the old root is the first child
            loginRoot.getChildren().remove(oldRoot); // Remove the old root
        });

        // Play the animation
        timeline.play();

    }

    @FXML
    private void goAdminLogin(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/stages/login/logFXML/adminLogin_view.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void setDatePicker(MouseEvent event) {
        try {
            DatePicker datePicker = new DatePicker();
            Popup popup = new Popup();
            popup.setAutoHide(true);
            popup.getContent().add(datePicker);

            Window window = ((Node) event.getSource()).getScene().getWindow();

            double popupX = event.getScreenX() - 200;
            double popupY = event.getScreenY();
            popup.show(window, popupX, popupY);

            datePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    Date selectedDate = Date.valueOf(newValue);

                    if (globalDate != null && selectedDate.before(globalDate)) {
                        // Show an alert if the selected date is invalid
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Invalid Date Selection");
                        alert.setHeaderText("Invalid Date");
                        alert.setContentText("The selected date cannot be earlier than the current date.");
                        alert.showAndWait();
                    } else {
                        // Update the globalDate if the date is valid
                        globalDate = selectedDate;
                        System.out.println("Date is set: " + globalDate);
                        dateHere.setText(String.valueOf(globalDate));
                        System.out.println(globalDate);
                        popup.hide();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to open the DatePicker: " + e.getMessage());
            alert.setTitle("Date Picker Error");
            alert.showAndWait();
        }
    }



}