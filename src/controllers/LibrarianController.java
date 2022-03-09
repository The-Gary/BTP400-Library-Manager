package controllers;

import java.io.IOException;
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
import library.CatalogueItem;
import library.Library;
import library.Ticket;
import utilities.Validator;

public class LibrarianController {
    private Parent root;
    private int idx;

    @FXML
    private Text confirmationMsg;

    @FXML
    private Button addItemBtn, homeBtn, delItemBtn, logoutBtn, newItemBtnm, reportBtn, cancelBtn, confirmBtn, searchBtn,
            ticketsBtn, resolveBtn;

    @FXML
    private TextField tfGenre, tfCategory, tfTitle, tfId;

    @FXML
    private AnchorPane ap;

    @FXML
    private Label welcomeTxt, errMsg;

    @FXML
    private TableView<CatalogueItem> catTable;

    @FXML
    private TableView<Ticket> ticketTbl;

    @FXML
    private TableColumn<Ticket, Integer> ticketIdCol, studentIdCol, itemIdCol;

    @FXML
    private TableColumn<Ticket, String> ticketAvailCol;

    @FXML
    private TableColumn<CatalogueItem, String> catCol, genreCol, titleCol, availCol;

    @FXML
    private TableColumn<CatalogueItem, Integer> idCol;

    @FXML
    void displayHome(ActionEvent event) {
        render("../scenes/librarian/LibrarianHomePage");
    }

    @FXML
    void displayAddItem(ActionEvent event) {
        render("../scenes/librarian/AddNewItem");
    }

    @FXML
    void displayDeleteItem(ActionEvent event) {
        render("../scenes/librarian/DeleteItem");
    }

    @FXML
    void displayTickets(ActionEvent event) {
        render("../scenes/librarian/ViewTickets");
    }

    @FXML
    void generateReport(ActionEvent event) {
        Library.genReport(Library.AccessLevel.librarian);
        render("../scenes/librarian/ViewCatalogue");
    }

    @FXML
    void logout(ActionEvent event) {
        Library.logout();
        render("../scenes/Login");
    }

    @FXML
    void deleteLibrarian(ActionEvent event) {
        int itemId = Library.getCatalogue().get(idx).getId();
        Library.removeCatalogueItem(itemId);
        displayHome(event);
    }

    @FXML
    void cancelDelete(ActionEvent event) {
        errMsg.setText("");
        displayDeleteItem(event);
    }

    @FXML
    void promptConfirmation(ActionEvent event) {
        errMsg.setText("");
        confirmationMsg.setVisible(false);
        confirmBtn.setVisible(false);
        cancelBtn.setVisible(false);

        String input = tfId.getText();
        if (input.isEmpty()) {
            errMsg.setText("Field cannot be empty");
            return;
        }

        if (!Validator.validateDigits(input)) {
            errMsg.setText("Invalid item id");
            return;
        }

        idx = Library.searchCatalogue(Integer.parseInt(input));
        if (idx == -1) {
            errMsg.setText("Item id not found");
            return;
        }

        if (idx == -1) {
            confirmationMsg.setText("Item id not found");
            confirmationMsg.setVisible(true);
        } else {
            String confirm = "Confirm deleting item: " + Library.getCatalogue().get(idx).getTitle() + " - "
                    + Library.getCatalogue().get(idx).getId();

            confirmationMsg.setText(confirm);
            confirmationMsg.setVisible(true);
            confirmBtn.setVisible(true);
            cancelBtn.setVisible(true);
        }
    }

    @FXML
    void addNewItem(ActionEvent event) {
        errMsg.setText("");
        int newId;
        boolean added = false;
        String sCategory = tfCategory.getText();
        String genre = tfGenre.getText();
        String title = tfTitle.getText();

        if (title.isEmpty() || sCategory.isEmpty() || genre.isBlank()) {
            errMsg.setText("Inputs cannot be empty");
            return;
        }

        if (!Validator.validateGenre(genre)) {
            errMsg.setText("Invalid genre");
            return;
        }

        if (!Validator.validateCategory(sCategory)) {
            errMsg.setText("Invalid category");
            return;
        }

        CatalogueItem.Category category = CatalogueItem.Category.unavailable;
        switch (sCategory) {
            case "book":
                category = CatalogueItem.Category.book;
                break;
            case "magazine":
                category = CatalogueItem.Category.magazine;
                break;
            case "movie":
                category = CatalogueItem.Category.movie;
        }
        do {
            newId = new Random().nextInt(10000, 99999);
            if (Library.searchCatalogue(newId) == -1) {
                Library.addCatalogueItem(
                        new CatalogueItem(newId, title, genre, category, true));
                added = true;
            }
        } while (!added);
        displayAddItem(event);
    }

    @FXML
    void resolveTicket(ActionEvent event) {
        errMsg.setText("");
        if (tfId.getText().isBlank()) {
            errMsg.setText("Field cannot be empty");
            return;
        }

        if (!Validator.validateDigits(tfId.getText())) {
            errMsg.setText("Invalid id");
            return;
        }

        if (!Library.resolveTicket(Integer.parseInt(tfId.getText()), errMsg)) {
            return;
        }

        this.initialize();
    }

    @FXML
    public void initialize() {
        if (this.welcomeTxt != null) {
            welcomeTxt.setText("Welcome, " + Library.getLoggedIn().getFirstName() + "!");
        }

        if (this.catTable != null) {
            ObservableList<CatalogueItem> items = FXCollections.observableArrayList();
            for (CatalogueItem item : Library.getCatalogue()) {
                items.add(item);
            }
            catTable.setItems(items);
            idCol.setCellValueFactory(new PropertyValueFactory<CatalogueItem, Integer>("id"));
            titleCol.setCellValueFactory(new PropertyValueFactory<CatalogueItem, String>("title"));
            genreCol.setCellValueFactory(new PropertyValueFactory<CatalogueItem, String>("genre"));
            catCol.setCellValueFactory(new PropertyValueFactory<CatalogueItem, String>("category"));
            availCol.setCellValueFactory(new PropertyValueFactory<CatalogueItem, String>("available"));
        }

        if (this.ticketTbl != null) {
            ObservableList<Ticket> items = FXCollections.observableArrayList();
            for (Ticket ticket : Library.getActiveTickets()) {
                items.add(ticket);
            }
            ticketTbl.setItems(items);
            ticketIdCol.setCellValueFactory(new PropertyValueFactory<Ticket, Integer>("id"));
            studentIdCol.setCellValueFactory(new PropertyValueFactory<Ticket, Integer>("studentId"));
            itemIdCol.setCellValueFactory(new PropertyValueFactory<Ticket, Integer>("itemId"));
            ticketAvailCol.setCellValueFactory(new PropertyValueFactory<Ticket, String>("available"));
        }

    }

    private void render(String sceneName) {
        try {
            root = FXMLLoader.load(getClass().getResource(sceneName + ".fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) ap.getScene().getWindow();
            stage.setTitle(sceneName);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
