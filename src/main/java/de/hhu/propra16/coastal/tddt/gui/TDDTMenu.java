package de.hhu.propra16.coastal.tddt.gui;

import de.hhu.propra16.coastal.tddt.catalog.Catalog;
import de.hhu.propra16.coastal.tddt.catalog.CatalogParser;
import de.hhu.propra16.coastal.tddt.catalog.Exercise;
import de.hhu.propra16.coastal.tddt.compiler.CompilerInteraction;
import de.hhu.propra16.coastal.tddt.compiler.CompilerReport;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.*;
import javafx.scene.Group;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;


import java.io.File;

import java.io.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;


public class TDDTMenu   {

    private static Stage primaryStage;

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
    protected TDDLabel lbbabysteps;

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

    public static Babysteps baby;


    @FXML
    protected void open(ActionEvent event) {
        primaryStage.setOnCloseRequest(e-> {
            speichernAbfrage(TriggerSaveOption.Close);
        });
        FileChooser dialog = new FileChooser();
        dialog.setTitle("Wähle eine Datei aus");
        dialog.setInitialDirectory(Paths.get("src/test").toFile()); //TODO
        dialog.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML Files", "*.xml"));
        File file = dialog.showOpenDialog(primaryStage);
        if(file == null) {
            return;
        }
        directory = file.getParentFile();
        currentExercise = null;
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
    protected void saveClick(ActionEvent event) {
        save();
    }

    private void save() {
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
        speichernAbfrage(TriggerSaveOption.Close);
        primaryStage.close();
    }

    @FXML
    protected void next(ActionEvent event) {
        CompilerInteraction.compile(taeditor, tatest, taterminal, tatestterminal, lbstatus, lbtime, currentExercise, lvexercises, btback, baby);
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
        if(!lvexercises.getSelectionModel().getSelectedItem().equals(currentExercise)) {
            speichernAbfrage(TriggerSaveOption.ChangeExercise);
        }

        currentExercise = lvexercises.getSelectionModel().getSelectedItem();
        catalog.loadExercise(taeditor, tatest, lbdescription, currentExercise);
        CompilerReport.setPreviousCode(taeditor.getText());
        CompilerReport.setPreviousTest(tatest.getText());
        if (currentExercise.isBabysteps()) {
            baby = new Babysteps(currentExercise, lbstatus, lbtime, taeditor, tatest);
            lbbabysteps.setText("AKTIVIERT");
            lbbabysteps.setId("green");
        } else {
            lbbabysteps.setText("DEAKTIVIERT");
            lbbabysteps.setId("red");
        }
    }

    public void speichernAbfrage(TriggerSaveOption option) {
        if(currentExercise != null ) {
            ButtonType ja = new ButtonType("Ja");
            ButtonType nein = new ButtonType("Nein");
            String abfrage = "bevor zu einer anderen Aufgabe gewechselt wird";
            if (option == TriggerSaveOption.Close) {
                abfrage = "bevor das Programm geschlossen wird?";
            }
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Möchtest du speichern, " + abfrage, ja, nein);

            alert.setHeaderText("");
            alert.setTitle("");
            alert.showAndWait();
            if (alert.getResult() == ja) {
                save();
            }
        }
    }


    @FXML
    @Ignore
    protected void showChart(ActionEvent event){
        int[] chartNumber = new int[4];
        /*init declaration*/
        File fileChart = new File("src/main/resources/de/hhu/propra16/coastal/tddt/chart.txt");
        int sum = 1;
        /*load file*/
        if(fileChart.exists()){
            try{
                FileReader chartFileReader = new FileReader("src/main/resources/de/hhu/propra16/coastal/tddt/chart.txt");
                for(int i=0; i<4; i++){
                    chartNumber[i] = chartFileReader.read();
                }
            }
            catch(IOException ex){
                System.out.println("User hat noch keine Aktionen betaetigt.");
            }
        }
        else{
            System.out.println("User hat noch keine Aktionen betaetigt.");
        }
        for(int i=0; i<chartNumber.length; i++){
            sum+=chartNumber[i];
        }

        /*Chart Darstellung, oeffnet ein neues Fenster*/
        Stage benutzeranalyse = new Stage();
        Scene scene = new Scene(new Group(), 500, 500);
        benutzeranalyse.setTitle("Benutzeranalyse");


        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                        new PieChart.Data("Status: RED", (int) (chartNumber[0]*100/sum)),
                        new PieChart.Data("Refactor Code", (int) (chartNumber[1]*100/sum)),
                        new PieChart.Data("Status: GREEN", (int) (chartNumber[2]*100/sum)),
                        new PieChart.Data("Refactor Test", (int) (chartNumber[3]*100/sum)));
        final PieChart chart = new PieChart(pieChartData);
        chart.setTitle("Verbrachte Zeit von Nutzer:");

        ((Group) scene.getRoot()).getChildren().add(chart);
        benutzeranalyse.setScene(scene);
        benutzeranalyse.show();


    }

    public static void setStage (Stage stage) {
        primaryStage = stage;
    }
}
