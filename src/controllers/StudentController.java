package controllers;

import java.io.IOException;
import java.util.ArrayList;

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
import javafx.stage.Stage;
import library.CatalogueItem;
import library.Library;
import library.Ticket;

public class StudentController {
    private Parent root;
    private int idx = -1;
    ArrayList<CatalogueItem> result = new ArrayList<>();

    @FXML
    private AnchorPane ap;

    @FXML
    private TableView<CatalogueItem> catTable, borrowedTbl;

    @FXML
    private TableColumn<CatalogueItem, String> catCol, genreCol, titleCol, availCol;

    @FXML
    private TableColumn<CatalogueItem, Integer> idCol;

    @FXML
    private Button logoutBtn, searchBtn, requestBtn, borrowedBtn, homeBtn, returnBtn;

    @FXML
    private TextField tfSearch;

    @FXML
    private Label welcomeTxt, errMsg;

    @FXML
    void displayHome(ActionEvent event) {
        render("../scenes/student/Search");
    }

    @FXML
    void logout(ActionEvent event) {
        Library.logout();
        render("../scenes/Login");
    }

    @FXML
    void displayBorrowedItems(ActionEvent event) {
        render("../scenes/student/BorrowedItems");
    }

    @FXML
    void searchByTitle(ActionEvent event) {
        errMsg.setVisible(false);
        catTable.setVisible(false);
        requestBtn.setVisible(false);
        idx = Library.searchCatalogue(tfSearch.getText());
        if (tfSearch.getText().isBlank()) {
            errMsg.setText("Field cannot be empty");
            errMsg.setVisible(true);
            return;
        }
        if (idx == -1) {
            errMsg.setText("No item was found");
            errMsg.setVisible(true);
            return;
        }

        result.clear();
        result.add(Library.getCatalogue().get(idx));
        errMsg.setText("A ticket has been issued. See a librarian to retrieve your item");
        catTable.setVisible(true);
        requestBtn.setVisible(true);
        this.initialize();
    }

    @FXML
    void createTicket(ActionEvent event) {
        errMsg.setVisible(true);
        errMsg.setText("Request ticket created.");
        Library.createTicket(new Ticket(Ticket.generateId(), Library.getLoggedIn().getId(),
                Library.getCatalogue().get(idx).getId(), Library.getCatalogue().get(idx).isAvailable()));
        Library.save(Library.AccessLevel.student);
    }

    @FXML
    void returnItem(ActionEvent event) {
        String input = tfSearch.getText();
        if (input.isBlank()) {
            errMsg.setText("Field cannot be empty");
            return;
        }

        if (Library.returnItem(tfSearch.getText(), Library.getLoggedIn().getId())) {
            errMsg.setText("Item has been returned");
            this.initialize();
            return;
        }

        errMsg.setText("Invalid input");

    }

    @FXML
    public void initialize() {
        if (this.welcomeTxt != null) {
            welcomeTxt.setText("Welcome, " + Library.getLoggedIn().getFirstName() + "!");
        }

        if (this.catTable != null) {

            ObservableList<CatalogueItem> items = FXCollections.observableArrayList();
            for (CatalogueItem item : result) {
                items.add(item);
            }
            catTable.setItems(items);
            idCol.setCellValueFactory(new PropertyValueFactory<CatalogueItem, Integer>("id"));
            titleCol.setCellValueFactory(new PropertyValueFactory<CatalogueItem, String>("title"));
            genreCol.setCellValueFactory(new PropertyValueFactory<CatalogueItem, String>("genre"));
            catCol.setCellValueFactory(new PropertyValueFactory<CatalogueItem, String>("category"));
            availCol.setCellValueFactory(new PropertyValueFactory<CatalogueItem, String>("available"));
        }

        if (this.borrowedTbl != null) {
            int loggedId = Library.getLoggedIn().getId();
            int idx = Library.search(loggedId, Library.AccessLevel.student);
            ArrayList<CatalogueItem> borrowedList = Library.getStudents().get(idx).getBorrowed();

            ObservableList<CatalogueItem> items = FXCollections.observableArrayList();
            for (CatalogueItem item : borrowedList) {
                items.add(item);
            }
            borrowedTbl.setItems(items);
            titleCol.setCellValueFactory(new PropertyValueFactory<CatalogueItem, String>("title"));
            genreCol.setCellValueFactory(new PropertyValueFactory<CatalogueItem, String>("genre"));
            catCol.setCellValueFactory(new PropertyValueFactory<CatalogueItem, String>("category"));
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
