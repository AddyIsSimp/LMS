package stages.login;

import javafx.animation.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.image.ImageView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.animation.Interpolator;
import java.io.IOException;
import Function.*;
import javax.swing.*;
import java.sql.*;

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
    private AnchorPane adminRoot;
    @FXML
    private AnchorPane staffRoot;
    @FXML
    private AnchorPane userRoot;
    @FXML
    private AnchorPane loginRoot;
    @FXML
    private AnchorPane setFrame;

    @FXML
    private Button passButton;
    @FXML
    private Button staffLog;
    @FXML
    private Button userLog;
    @FXML
    private Button exit;

    @FXML
    private PasswordField passwordtextfield;
    @FXML
    private TextField passwordTextVisible;
    @FXML
    private TextField tf_staffid;

    @FXML
    private ImageView passwordIcon;

//EXIT=======================================================================================================================================================================================

    public void close() {
        System.exit(0);
    }

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
        System.out.println("Exit Node: " + exit);
        System.out.println("Login Root: " + loginRoot);
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

    public void staffLoginEvt(ActionEvent event) throws Exception {
        try {
            conn = dbFunc.connectToDB();

            String id = tf_staffid.getText();
            String password = passwordtextfield.getText();

            if(fnc.staffIDChecker(id)) {
                int staffId = Integer.parseInt(id);
                String sqlFindStaff = "SELECT * FROM staff WHERE staff_id = ? AND password = ?";
                pstmt = conn.prepareStatement(sqlFindStaff);
                pstmt.setInt(1, staffId);
                pstmt.setString(2, password);
                rs = pstmt.executeQuery();
                if (rs.next()) {
                    //INSERT THE STAFF MENU HERE
                    JOptionPane.showMessageDialog(null,
                            "Wrong id or password",
                            "Login Failed",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null,
                            "Wrong id or password",
                            "Login Failed",
                            JOptionPane.ERROR_MESSAGE);
                }
            }else {
                JOptionPane.showMessageDialog(null,
                        "Wrong id or password",
                        "Login Failed",
                        JOptionPane.ERROR_MESSAGE);
            }

        }catch(SQLException e){
            System.out.println(e.getMessage());
        }catch(Exception e) {
            System.out.println(e.getMessage());
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
}