package de.hhu.propra16.coastal.tddt;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javax.xml.soap.Text;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;


public class TDDTMenu {

    private static Stage primaryStage;

    @FXML
    MenuItem miopen;

    @FXML
    MenuItem misave;

    @FXML
    MenuItem miclose;

    @FXML
    TextArea taeditor;

    @FXML
    TextArea tatest;

    @FXML
    Button btnextstep;

    @FXML
    Label lbstatus;

    @FXML
    public void open(ActionEvent event) {
        FileChooser dialog = new FileChooser();
        dialog.setTitle("Wähle eine Datei aus");
        dialog.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML Files", "*.xml"));
        dialog.showOpenDialog(primaryStage);
        Path p = Paths.get("../Software.java");
        try {
            if (!Files.exists(p)) {
                Files.write(p, new byte[0], StandardOpenOption.CREATE);
            } else {
                taeditor.setText(new String(Files.readAllBytes(p)));
            }
        } catch (Exception e) {
            System.err.println("Write to data failed");
        }

        Path p2 = Paths.get("../SoftwareTest.java");
        try {
            if(!Files.exists(p2)) {
                Files.write(p2, new byte[0], StandardOpenOption.CREATE);
            } else {
                tatest.setText(new String(Files.readAllBytes(p2)));
            }
        } catch (Exception e) {
            System.err.println("Write to data failed");
        }
    }

    @FXML
    public void save(ActionEvent event) {
        /*FileChooser dialog = new FileChooser();
        dialog.setTitle("Wähle eine Datei aus");
        dialog.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML Files", "*.xml"));
        dialog.showSaveDialog(primaryStage);*/

        Path p = Paths.get("../Software.java");
        try {
            Files.write(p, taeditor.getText().getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
        } catch (Exception e) {
            System.err.println("Write to data failed");
        }

        Path p2 = Paths.get("../SoftwareTest.java");
        try {
            Files.write(p2, tatest.getText().getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
        } catch (Exception e) {
            System.err.println("Write to data failed");
        }
    }

    @FXML
    public void close(ActionEvent event) {
        primaryStage.close();
    }

    @FXML
    public void next(ActionEvent event) {
        switch (lbstatus.getText()) {
            case "RED":
                lbstatus.setText("GREEN");
                lbstatus.setId("green");
                break;
            case "GREEN":
                lbstatus.setText("REFACTOR");
                lbstatus.setId("black");
                break;
            case "REFACTOR":
                lbstatus.setText("RED");
                lbstatus.setId("red");
                break;
        }
    }

    public static void setStage (Stage stage) {
        primaryStage = stage;
    }
}
