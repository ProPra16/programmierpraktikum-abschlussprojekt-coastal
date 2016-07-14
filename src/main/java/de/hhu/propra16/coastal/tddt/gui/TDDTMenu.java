package de.hhu.propra16.coastal.tddt.gui;

import de.hhu.propra16.coastal.tddt.catalog.Catalog;
import de.hhu.propra16.coastal.tddt.catalog.CatalogParser;
import de.hhu.propra16.coastal.tddt.catalog.Exercise;
import de.hhu.propra16.coastal.tddt.compiler.CompilerInteraction;
import de.hhu.propra16.coastal.tddt.compiler.CompilerReport;
import de.hhu.propra16.coastal.tddt.tracking.Tracking;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.scene.Group;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.fxmisc.richtext.LineNumberFactory;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;


import java.io.*;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ResourceBundle;


public class TDDTMenu implements Initializable {

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
    protected TDDLabel lbtracking;

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

    public static Tracking tracker = new Tracking();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        taeditor.setParagraphGraphicFactory(LineNumberFactory.get(taeditor));
        taeditor.richChanges()
                .filter(ch -> !ch.getInserted().equals(ch.getRemoved()))
                .subscribe(change ->{
                    taeditor.setStyleSpans(0, taeditor.computeHighlighting());
                });
        tatest.setParagraphGraphicFactory(LineNumberFactory.get(tatest));

        tatest.richChanges()
                .filter(ch -> !ch.getInserted().equals(ch.getRemoved()))
                .subscribe(change ->{
                    tatest.setStyleSpans(0, tatest.computeHighlighting());
                });
    }

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
    }

    @FXML
    protected void next(ActionEvent event) {
        CompilerInteraction.compile(taeditor, tatest, taterminal, tatestterminal, lbstatus, lbtime, currentExercise, lvexercises, btback, baby, tracker);
    }

    @FXML
    protected void previous(ActionEvent event) {
        CompilerReport.back(taeditor, tatest, lbstatus, btback, baby, tracker);
    }


    @FXML
    protected void help(ActionEvent event) {
        Stage stage = new Stage();
        stage.setTitle("Benutzerhandbuch");
        StackPane pane = new StackPane();
        WebView browser = new WebView();
        URL url = getClass().getResource("help.html");
        try {
            browser.getEngine().load(url.toExternalForm());
        }catch(Exception e) {
            System.out.println("Laden der HTML nicht möglich");
        }
        pane.getChildren().add(browser);
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    protected void chooseExercise(MouseEvent event) {
        if(lvexercises.getSelectionModel().getSelectedItem() == null) {
            return;
        }
        if(!lvexercises.getSelectionModel().getSelectedItem().equals(currentExercise)) {
            speichernAbfrage(TriggerSaveOption.ChangeExercise);
        } else {
            return;
        }

        currentExercise = lvexercises.getSelectionModel().getSelectedItem();
        File program = new File(directory.toPath().toString() + "/" + catalog +"/" + currentExercise.getClassName() + ".java");
        File test = new File(directory.toPath().toString() + "/" + catalog +"/" + currentExercise.getTestName() + ".java");
        if(Files.exists(program.toPath()) && Files.exists(test.toPath()) && continueAbfrage()) {
            continueExercise(program,test);
        } else {
            catalog.loadExercise(taeditor, tatest, lbdescription, currentExercise);
        }
        CompilerReport.setPreviousCode(taeditor.getText());
        CompilerReport.setPreviousTest(tatest.getText());
        if(baby != null ) {
            baby.stopTimer();
        }
        if (currentExercise.isBabysteps()) {
            baby = new Babysteps(currentExercise, tatest);
            baby.babystep(lbstatus, lbtime, taeditor, tatest);
            lbbabysteps.setText("AKTIVIERT");
            lbbabysteps.setId("green");
        } else {
            lbtime.setText("-:-");
            lbbabysteps.setText("DEAKTIVIERT");
            lbbabysteps.setId("red");
        }

        if(currentExercise.isTracking()) {
            lbtracking.setText("AKTIVIERT");
            lbtracking.setId("green");
        } else {
            lbtracking.setText("DEAKTIVIERT");
            lbtracking.setId("red");
        }
        tracker.startTimer(0);
    }

    private void speichernAbfrage(TriggerSaveOption option) {
        if(currentExercise != null) {
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

    private void continueExercise(File program, File test) {
        catalog.loadExercise(taeditor, tatest, lbdescription, currentExercise, program, test);
    }

    private boolean continueAbfrage() {
        ButtonType ja = new ButtonType("Ja");
        ButtonType nein = new ButtonType("Nein");
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Diese Aufgabe hast du bereits bearbeitet. Möchtest du daran weiterarbeiten?", ja, nein);
        alert.setHeaderText("");
        alert.setTitle("");
        alert.showAndWait();
        if(alert.getResult() == ja) {
            return true;
        }
        return false;
    }


    @FXML
    protected void showChart(ActionEvent event){
        /*init declaration*/
        int[] chartNumber = CompilerReport.readAll("src/test/chart.txt");
        int sum = 0;
        for (int i = 0; i < chartNumber.length; i++) {
            sum += chartNumber[i];
        }
        /*load file*/
        if(chartNumber.length==4 && sum!=0) {


            /*Chart Darstellung, oeffnet ein neues Fenster*/
            Stage benutzeranalyse = new Stage();
            Scene scene = new Scene(new Group(), 500, 500);
            benutzeranalyse.setTitle("Benutzeranalyse");

            ObservableList<PieChart.Data> pieChartData =
                    FXCollections.observableArrayList(
                            new PieChart.Data("Red", chartNumber[0] * 100 / sum),
                            new PieChart.Data("Refactor Code", chartNumber[2] * 100 / sum),
                            new PieChart.Data("Green", chartNumber[1] * 100 / sum),
                            new PieChart.Data("Refactor Test", chartNumber[3] * 100 / sum));
            final PieChart chart = new PieChart(pieChartData);
            chart.setTitle("Verbrachte Zeit vom Nutzer:");
            ((Group) scene.getRoot()).getChildren().add(chart);
            benutzeranalyse.setScene(scene);
            benutzeranalyse.show();
        }
        else{
            /*Opens Game Win window*/
            Label msg1 = new Label("Es wurden nicht genug Daten gesammelt.");
            Stage benutzeranalyse = new Stage();
            StackPane sp = new StackPane();
            sp.getChildren().addAll(msg1);

            Scene sc = new Scene(sp, 300, 200);

            benutzeranalyse.setTitle("Benutzeranalyse");
            benutzeranalyse.setScene(sc);

            benutzeranalyse.show();
        }

    }

    public static void setStage (Stage stage) {
        primaryStage = stage;
    }


}
