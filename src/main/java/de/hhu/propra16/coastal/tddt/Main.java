package de.hhu.propra16.coastal.tddt;

import de.hhu.propra16.coastal.tddt.gui.TDDTMenu;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        /*Empty/create chart.txt*/
        try{
            FileWriter chartFileWriter = new FileWriter("src/test/chart.txt");
            chartFileWriter.write("0");
            chartFileWriter.flush();
        }
        catch(IOException ex){
            System.out.println(ex.toString());
        }

        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        } catch (Exception e) {
            System.err.println("Sample nicht gefunden");
        }
        primaryStage.setTitle("Test Driven Development Trainer");
        Scene scene = new Scene(root);
        URL url = getClass().getResource("tddt.css");
        scene.getStylesheets().add(url.toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.setOnCloseRequest(event -> Platform.exit());
        TDDTMenu.setStage(primaryStage);
        primaryStage.show();
    }


    public static void main(String[] args) {
        Platform.setImplicitExit(true); // allows Platform.exit() to close all windows related to the App
        launch(args);
    }
}
