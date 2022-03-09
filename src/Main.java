import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import library.Library;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {

        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("scenes/MainScene.fxml"));

            Scene scene = new Scene(root);
            primaryStage.setTitle("Welcome");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Library("data/books.txt", "data/admins.txt", "data/librarians.txt", "data/students.txt",
                "data/tickets.txt");
        launch(args);
    }
}