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
import vk.core.api.*;


import java.io.File;

import java.net.URL;


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
    private TextArea taeditor;

    @FXML
    private TextArea tatest;

    @FXML
    private TextArea taterminal;

    @FXML
    private TextArea tatestterminal;

    @FXML
    private Button btnextstep;

    @FXML
    private Label lbstatus;

    @FXML
    private MenuItem mihelp;

    @FXML
    private ListView<Exercise> lvexercises;

    @FXML
    private Label lbdescription;

    private Catalog catalog;

    private CompileTarget target = CompileTarget.TEST;

    private Exercise currentExercise;

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



        if(file == null) {
            return;
        }


        try {
            catalog = new CatalogParser().parse(file);
        } catch (Exception e) {
            System.err.println("Fehler beim Parsen des Katalogs aufgetreten");
            return;
        }

        catalog.loadExercises(taeditor, tatest, lbdescription, lvexercises);
        if(lvexercises.getItems().get(0) == null) {
            return;
        }
        currentExercise = lvexercises.getItems().get(0);

    }

    @FXML
    protected void save(ActionEvent event) {
        DirectoryChooser dialog = new DirectoryChooser();
        dialog.setTitle("Wähle einen Ordner aus");
        File directory = dialog.showDialog(primaryStage);


        if(directory == null) {
            return;
        }

        try {

            Path p = Paths.get(directory.toPath().toString() + "/" + lvexercises.getItems().get(0).getClassName() + ".java");
            StandardOpenOption option = StandardOpenOption.TRUNCATE_EXISTING;
            if (!Files.exists(p)) {
                option = StandardOpenOption.CREATE;
            }
            Files.write(p, taeditor.getText().getBytes(), option);
        } catch (Exception e) {
            System.err.println("Write to data failed");
        }


        try {
            Path p2 = Paths.get(directory.toPath().toString() + "/" + lvexercises.getItems().get(0).getTestName() + ".java");
            StandardOpenOption option = StandardOpenOption.TRUNCATE_EXISTING;
            if (!Files.exists(p2)) {
                option = StandardOpenOption.CREATE;
            }
            Files.write(p2, taeditor.getText().getBytes(), option);
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
        compile(target);
    }

    private void compile(CompileTarget target) {
        taterminal.clear();
        tatestterminal.clear();
        if(lvexercises.getItems().isEmpty()) {
            return;
        }
        CompilationUnit compilationUnitProgram = new CompilationUnit(currentExercise.getClassName(), taeditor.getText(), false);
        String errorMessagesProgram = "Compiler Error in Program:" + "\n" + "\n";
        CompilationUnit compilationUnitTest = new CompilationUnit(currentExercise.getTestName(), tatest.getText(), true);

        String errorMessagesTest = "Compiler Error in Test:" + "\n" + "\n";

        JavaStringCompiler compiler = CompilerFactory.getCompiler(compilationUnitTest, compilationUnitProgram);


        compiler.compileAndRunTests();
        Collection<CompileError> errorsProgram = compiler.getCompilerResult().getCompilerErrorsForCompilationUnit(compilationUnitProgram);
        Collection<CompileError> errorsTest = compiler.getCompilerResult().getCompilerErrorsForCompilationUnit(compilationUnitTest);


        for(CompileError error : errorsProgram) {
            String currentTerminal = taterminal.getText();
            taterminal.setText(currentTerminal + " " + error.getLineNumber() + ": " + error.getMessage() + "\n" + "\n");
        }
        for(CompileError error : errorsTest) {
            String currentTerminal = tatestterminal.getText();
            tatestterminal.setText(currentTerminal + " " + error.getLineNumber() + ": " + error.getMessage() + "\n" + "\n");
        }
        if(!compiler.getCompilerResult().hasCompileErrors()) {
            changeReport();

        } else {
            if (target == CompileTarget.TEST) {
                // TODO Bei Syxtaxfehlern sollte nicht changeReport aufrufen
                tatestterminal.setText(errorMessagesTest + tatestterminal.getText());

            } else {
                taterminal.setText(errorMessagesProgram + taterminal.getText());
            }
        }
    }

    @Deprecated
    private void runTests() {
        /*tatestterminal.clear();
        //Collection<TestFailure> failures = compiler.getTestResult().getTestFailures();
        for(TestFailure failure: failures) {
            String currentTerminal = tatestterminal.getText();
            tatestterminal.setText(currentTerminal + failure.getMessage() +"\n" +"\n");
        }*/
    }

    private void changeReport() {
        switch (lbstatus.getText()) {
            case "RED":
                lbstatus.setText("GREEN");
                lbstatus.setId("green");
                TDDController.toEditor(taeditor, tatest);
                target = CompileTarget.EDITOR;
                break;
            case "GREEN":
                lbstatus.setText("REFACTOR");
                lbstatus.setId("black");
                target = CompileTarget.EDITOR;
                break;
            case "REFACTOR":
                lbstatus.setText("RED");
                lbstatus.setId("red");
                TDDController.toTestEditor(taeditor, tatest);
                target = CompileTarget.TEST;
                break;
        }
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
        Babysteps baby = new Babysteps(currentExercise, lbstatus);
    }

    public static void setStage (Stage stage) {
        primaryStage = stage;
    }
}
