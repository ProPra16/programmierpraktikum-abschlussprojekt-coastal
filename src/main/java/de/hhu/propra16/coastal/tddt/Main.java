package de.hhu.propra16.coastal.tddt;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        }catch (Exception e) {
            System.err.println("Sample nicht gefunden");
        }
        primaryStage.setTitle("Test Driven Development Trainer");
        primaryStage.setScene(new Scene(root, 1080, 720));
        TDDTMenu.setStage(primaryStage);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
