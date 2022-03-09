package controllers;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import library.Library;
import utilities.Validator;

public class MainController {
    private Parent root;

    @FXML
    private AnchorPane ap;

    @FXML
    private Button loginBtn, homeBtn, submitBtn;

    @FXML
    private Label errMsg;

    @FXML
    private PasswordField tfPassword;

    @FXML
    private TextField tfUsername;

    @FXML
    void displayHome(ActionEvent event) {
        render("../scenes/MainScene");
    }

    @FXML
    void displayLogin(ActionEvent event) {
        render("../scenes/Login");
    }

    @FXML
    void authenticate(ActionEvent event) {
        errMsg.setText("");
        String username = tfUsername.getText();
        String pass = tfPassword.getText();

        if (username.isBlank() || pass.isBlank()) {
            errMsg.setText("Fields cannot be empty");
            return;
        }

        if (!Validator.validateDigits(username)) {
            errMsg.setText("Invalid username. Only digits are accepted");
            return;
        }

        char c = username.charAt(0);
        int ret = 0;
        switch (c) {
            case '1':
                ret = Library.authenticate(Integer.parseInt(username), pass,
                        Library.AccessLevel.admin);
                if (ret == 1) {
                    render("../scenes/admin/AdminHomePage");
                } else if (ret == 0)
                    errMsg.setText("Credentials don't match");
                break;
            case '2':
                ret = Library.authenticate(Integer.parseInt(username), pass,
                        Library.AccessLevel.librarian);
                if (ret == 2)
                    render("../scenes/librarian/LibrarianHomePage");
                else if (ret == 0)
                    errMsg.setText("Credentials don't match");
                break;
            case '3':
                ret = Library.authenticate(Integer.parseInt(username), pass,
                        Library.AccessLevel.student);
                if (ret == 3)
                    render("../scenes/student/Search");
                else if (ret == 0)
                    errMsg.setText("Credentials don't match");
                break;
            default:
                errMsg.setText("Username not found");
        }
    }

    private void render(String scenePath) {
        try {
            root = FXMLLoader.load(getClass().getResource(scenePath + ".fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) ap.getScene().getWindow();
            stage.setTitle(scenePath);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
