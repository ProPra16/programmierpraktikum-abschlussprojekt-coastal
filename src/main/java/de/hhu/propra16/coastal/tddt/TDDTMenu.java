package de.hhu.propra16.coastal.tddt;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;


import java.awt.*;
import java.io.File;

import java.net.URL;

import java.io.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;



public class TDDTMenu implements Initializable {

    private static Stage primaryStage;

    @FXML
    private MenuItem miopen;

    @FXML
    private MenuItem misave;

    @FXML
    private MenuItem miclose;

    @FXML
    private MenuItem userTracking;

    @FXML
    private TDDTextArea taeditor;

    @FXML
    private TDDTextArea tatest;

    @FXML
    private TextArea taterminal;

    @FXML
    private TextArea tatestterminal;

    @FXML
    private Button btnextstep;

    @FXML
    private Button btback;

    @FXML
    protected TDDLabel lbstatus;

    @FXML
    protected TDDLabel lbtime;

    @FXML
    private MenuItem mihelp;

    @FXML
    private TDDListView<Exercise> lvexercises;

    @FXML
    private TDDLabel lbdescription;

    private Catalog catalog;

    private Exercise currentExercise;

    private File directory;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    protected void open(ActionEvent event) {
        FileChooser dialog = new FileChooser();
        dialog.setTitle("Wähle eine Datei aus");
        dialog.setInitialDirectory(Paths.get("src/test").toFile()); //TODO
        dialog.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML Files", "*.xml"));
        File file = dialog.showOpenDialog(primaryStage);
        directory = file.getParentFile();

        if(file == null) {
            return;
        }

        try {
            catalog = new CatalogParser().parse(file);
        } catch (Exception e) {
            System.err.println("Fehler beim Parsen des Katalogs aufgetreten");
            if (!(e instanceof SAXException)) {
                e.printStackTrace();
            }
            else if (e instanceof SAXParseException) {
                SAXParseException saxParseE = (SAXParseException) e;
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Fehler beim Parsen des Katalogs");
                alert.setHeaderText(saxParseE.getMessage() + " (Zeile " + saxParseE.getLineNumber() + ", Zeichen " + saxParseE.getColumnNumber() + ")");
                alert.show();
            }
            return;
        }
        catalog.loadInListView(lvexercises);
    }

    @FXML
    protected void save(ActionEvent event) {
        if(currentExercise == null) {
            return;
        }
        try {
            File dir = new File(directory.toPath().toString() + "/" + catalog +"/");
            if(!dir.exists()) {
                dir.mkdir();
            }
            Path p = Paths.get(directory.toPath().toString() + "/" + catalog + "/" + currentExercise.getClassName() + ".java");
            StandardOpenOption option = StandardOpenOption.TRUNCATE_EXISTING;
            if (!Files.exists(p)) {
                option = StandardOpenOption.CREATE;
            }
            Files.write(p, taeditor.getText().getBytes(), option);
        } catch (Exception e) {
            System.err.println("Write to data failed");
        }


        try {
            Path p2 = Paths.get(directory.toPath().toString() + "/" + catalog +"/" + currentExercise.getTestName() + ".java");
            StandardOpenOption option = StandardOpenOption.TRUNCATE_EXISTING;
            if (!Files.exists(p2)) {
                option = StandardOpenOption.CREATE;
            }
            Files.write(p2, tatest.getText().getBytes(), option);
        } catch (Exception e) {
            System.err.println("Write to data failed");
        }
    }

    @FXML
    protected void close(ActionEvent event) {
        primaryStage.close();
    }

    @FXML
    protected void next(ActionEvent event) {
        CompilerInteraction.compile(taeditor, tatest, taterminal, tatestterminal, lbstatus, lbtime, currentExercise, lvexercises, btback);
    }

    @FXML
    protected void previous(ActionEvent event) {
        CompilerReport.back(taeditor, tatest, lbstatus, btback);
    }


    @FXML
    protected void help(ActionEvent event) {

    }

    @FXML
    protected void chooseExercise(MouseEvent event) {
        if(lvexercises.getSelectionModel().getSelectedItem() == null) {
            return;
        }
        currentExercise = lvexercises.getSelectionModel().getSelectedItem();
        catalog.loadExercise(taeditor, tatest, lbdescription, currentExercise);
        CompilerReport.setPreviousCode(taeditor.getText());
    }


    @FXML
    protected void showChart(ActionEvent event){
        int[] chart = new int[4];
        /*init declar*/
        File fileChart = new File("chart.txt");

        /*load file*/
        if(fileChart.exists()){
            try{
                FileReader chartReader = new FileReader("chart.txt");
                for(int i=0; i<4; i++){
                    chart[i] = chartReader.read();
                }
            }
            catch(IOException ex){
                System.out.println(ex.toString());
            }
        }
        /*Chart Darstellung*/
    }

    public static void setStage (Stage stage) {
        primaryStage = stage;
    }
}
