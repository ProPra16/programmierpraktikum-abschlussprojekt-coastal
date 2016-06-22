package de.hhu.propra16.coastal.tddt;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;

import java.net.URL;
import java.util.ResourceBundle;


public class TDDTMenu implements Initializable{

    @FXML
    MenuItem miopen;

    @FXML
    MenuItem misave;

    @FXML
    MenuItem miclose;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        miopen.setOnAction(e -> {
            FileChooser dialog = new FileChooser();
            dialog.setTitle("Wähle eine Datei aus");
            dialog.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML Files", "*.xml"));

        });

        misave.setOnAction(e -> {
            FileChooser dialog = new FileChooser();
            dialog.setTitle("Wähle eine Datei aus");
            dialog.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML Files", "*.xml"));

        });



    }
}
