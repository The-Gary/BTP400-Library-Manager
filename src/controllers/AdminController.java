package controllers;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Random;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import library.Library;
import library.users.Librarian;
import utilities.Validator;

public class AdminController {
    private Parent root;
    private int idx;

    @FXML
    private AnchorPane ap;

    @FXML
    private Label welcomeTxt, errMsg;

    @FXML
    private Text confirmationMsg;

    @FXML
    private TableView<Librarian> libTable;

    @FXML
    private TableColumn<Librarian, String> emailCol, fNameCol, lNameCol;

    @FXML
    private TableColumn<Librarian, LocalDate> dateCol;

    @FXML
    private TableColumn<Librarian, Integer> idCol;

    @FXML
    private Button homeBtn, submitBtn, searchBtn, confirmBtn, cancelBtn, reportBtn, newLibBtn, logoutBtn, delLibBtn;

    @FXML
    private TextField tfEmail, tfFirstName, tfHireDate, tfLastName, tfPassword, tfId;

    @FXML
    void displayHome(ActionEvent event) {
        render("../scenes/admin/AdminHomePage");
    }

    @FXML
    void generateReport(ActionEvent event) {
        Library.genReport(Library.AccessLevel.admin);
        render("../scenes/admin/ViewLibrarians");
    }

    @FXML
    void displayRegisterLibrarian(ActionEvent event) {
        render("../scenes/admin/RegisterLibrarian");
    }

    @FXML
    void displayDeleteLibrarian(ActionEvent event) {
        render("../scenes/admin/DeleteLibrarian");
    }

    @FXML
    void logout(ActionEvent event) {
        Library.logout();
        render("../scenes/Login");
    }

    @FXML
    void registerLibrarian(ActionEvent event) {
        errMsg.setText("");
        String password = tfPassword.getText();
        String fName = tfFirstName.getText();
        String lName = tfLastName.getText();
        String email = tfEmail.getText();
        String date = tfHireDate.getText();
        if (password.isEmpty() || fName.isEmpty() || lName.isEmpty() || email.isEmpty() || date.isEmpty()) {
            errMsg.setText("Fields cannot be left empty.");
            return;
        }

        if (!Validator.validateEmail(email)) {
            errMsg.setText("Email address is not the correct format.");
            return;
        }

        if (!Validator.validateDate(date)) {
            errMsg.setText("Date is not the correct format.");
            return;
        }

        int newId;
        boolean added = false;
        do {
            newId = new Random().nextInt(200, 299);
            if (Library.search(newId, Library.AccessLevel.librarian) == -1) {
                Library.addLibrarian(new Librarian(newId, tfPassword.getText(), tfFirstName.getText(),
                        tfLastName.getText(), tfEmail.getText(), LocalDate.parse(tfHireDate.getText())));
                System.out.println("new librarian added"); // DEBUG
                added = true;
            }
        } while (!added);
        render("../scenes/admin/RegisterLibrarian");
    }

    @FXML
    void promptConfirmation(ActionEvent event) {
        errMsg.setText("");
        String input = tfId.getText();
        if (input.isEmpty()) {
            errMsg.setText("Field cannot be empty");
            return;
        }

        if (!Validator.validateDigits(input)) {
            errMsg.setText("Invalid username");
            return;
        }

        idx = Library.search(Integer.parseInt(input), Library.AccessLevel.librarian);
        if (idx == -1) {
            errMsg.setText("Librarian id not found");
            return;
        }

        String name = Library.getLibrarians().get(idx).getFirstName() + " "
                + Library.getLibrarians().get(idx).getLastName();

        String confirm = "Confirm deleting librarian: " + name + " - "
                + Library.getLibrarians().get(idx).getId();

        confirmationMsg.setText(confirm);
        confirmationMsg.setVisible(true);
        confirmBtn.setVisible(true);
        cancelBtn.setVisible(true);
    }

    @FXML
    void deleteLibrarian(ActionEvent event) {
        int librarianId = Library.getLibrarians().get(idx).getId();
        Library.removeLibrarian(librarianId);
        displayHome(event);
    }

    @FXML
    void cancelDelete(ActionEvent event) {
        errMsg.setText("");
        displayDeleteLibrarian(event);
    }

    @FXML
    public void initialize() {
        if (this.welcomeTxt != null) {
            welcomeTxt.setText("Welcome, " + Library.getLoggedIn().getFirstName() + "!");
        }

        if (this.libTable != null) {
            ObservableList<Librarian> items = FXCollections.observableArrayList();
            for (Librarian lib : Library.getLibrarians()) {
                items.add(lib);
            }
            libTable.setItems(items);
            idCol.setCellValueFactory(new PropertyValueFactory<Librarian, Integer>("id"));
            fNameCol.setCellValueFactory(new PropertyValueFactory<Librarian, String>("firstName"));
            lNameCol.setCellValueFactory(new PropertyValueFactory<Librarian, String>("lastName"));
            emailCol.setCellValueFactory(new PropertyValueFactory<Librarian, String>("email"));
            dateCol.setCellValueFactory(new PropertyValueFactory<Librarian, LocalDate>("hireDate"));
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
