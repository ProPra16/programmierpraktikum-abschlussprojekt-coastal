package de.hhu.propra16.coastal.tddt;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;

import java.net.URL;
import java.util.ResourceBundle;


public class TDDTMenu {

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
    }

    @FXML
    public void save(ActionEvent event) {
        FileChooser dialog = new FileChooser();
        dialog.setTitle("Wähle eine Datei aus");
        dialog.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML Files", "*.xml"));
    }
}
