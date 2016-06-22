package de.hhu.propra16.coastal.tddt;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


public class TDDTMenu {

    private static Stage primaryStage;

    @FXML
    MenuItem miopen;

    @FXML
    MenuItem misave;

    @FXML
    MenuItem miclose;

    @FXML
    public void open(ActionEvent event) {
        FileChooser dialog = new FileChooser();
        dialog.setTitle("Wähle eine Datei aus");
        dialog.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML Files", "*.xml"));
        dialog.showOpenDialog(primaryStage);
    }

    @FXML
    public void save(ActionEvent event) {
        FileChooser dialog = new FileChooser();
        dialog.setTitle("Wähle eine Datei aus");
        dialog.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML Files", "*.xml"));
        dialog.showOpenDialog(primaryStage);
    }

    @FXML
    public void close(ActionEvent event) {
        primaryStage.close();
    }

    public static void setStage (Stage stage) {
        primaryStage = stage;
    }
}
