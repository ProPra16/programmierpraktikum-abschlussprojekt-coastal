package de.hhu.propra16.coastal.tddt.compiler;

import de.hhu.propra16.coastal.tddt.tracking.*;
import de.hhu.propra16.coastal.tddt.catalog.Exercise;
import de.hhu.propra16.coastal.tddt.gui.*;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import vk.core.api.CompileError;
import vk.core.api.JavaStringCompiler;
import vk.core.api.TestFailure;
import vk.core.api.TestResult;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by student on 06/07/16.
 */
public class CompilerReport {

    private static String previousCode;

    private static String previousTest;

    private static CompileTarget target = CompileTarget.TEST;

    /*chart File into String[]*/
    public static int[] readAll(String filePath){
        /*init declar*/
        ArrayList<String> outputArrayList = new ArrayList<>();
        int cnt=0;
        String tmp;
        /*into String*/
        try{
            BufferedReader loadChart = new BufferedReader(new FileReader(filePath));
            tmp = loadChart.readLine();
            while(tmp!=null){
                //if(tmp.isEmpty()) break;
                outputArrayList.add(tmp);
                tmp = loadChart.readLine();
                cnt++;
            }
        }
        catch(IOException ex){
            System.out.println(ex.toString());
        }
        /*into String array*/
        int[] outputArray = new int[cnt];
        for(int i=0; i<cnt; i++){
            outputArray[i] = Integer.parseInt(outputArrayList.get(i));
        }
        return outputArray;
    }

    /*saving int[] to file*/
    public static void save(int[] saveTo){
        try{
            String saveToString ="";
            BufferedWriter writer = new BufferedWriter(new FileWriter("src/test/chart.txt"));
            for(int i=0; i<saveTo.length; i++){
                saveToString +=saveTo[i]+"\n";
            }
            writer.write(saveToString);
            writer.flush();
        }
        catch(IOException ex){
            System.out.println(ex.toString());
        }
    }

    static void changeReport(ITDDTextArea taeditor, ITDDTextArea tatest, ITDDLabel lbstatus, Button btback, Exercise currentExercise, Babysteps baby, Tracking tracker) {
        /*save chart*/
        int[] chartArray = {tracker.getTime(0), tracker.getTime(1), tracker.getTime(2), tracker.getTime(3)};
        save(chartArray);
        baby.refreshTimer();
        switch (lbstatus.getText()) {
            case "RED":
                /*Tracking*/
                tracker.stopTimer(0);
                tracker.startTimer(1);
                chartArray[0] = tracker.getTime(0);
                save(chartArray);
                /**/
                btback.setDisable(false);
                lbstatus.setText("GREEN");
                lbstatus.setId("green");
                TDDController.toEditor(taeditor, tatest);
                target = CompileTarget.EDITOR;
                if(currentExercise.isBabysteps()) {
                    baby.getOldEditor(taeditor);
                }
                break;
            case "GREEN":
                /*Tracking*/
                tracker.stopTimer(1);
                tracker.startTimer(2);
                chartArray[1] = tracker.getTime(1);
                save(chartArray);
                /**/
                btback.setDisable(true);
                lbstatus.setText("REFACTOR CODE");
                lbstatus.setId("black");
                target = CompileTarget.EDITOR;
                if(currentExercise.isBabysteps()) {
                    baby.getOldTest(tatest);
                }
                break;
            case "REFACTOR CODE":
                /*Tracking*/
                tracker.stopTimer(2);
                tracker.startTimer(3);
                chartArray[2] = tracker.getTime(2);
                save(chartArray);
                /**/
                lbstatus.setText("REFACTOR TEST");
                TDDController.toTestEditor(taeditor, tatest);
                target = CompileTarget.TEST;
                break;
            case "REFACTOR TEST":
                /*Tracking*/
                tracker.stopTimer(3);
                tracker.startTimer(0);
                chartArray[3] = tracker.getTime(3);
                save(chartArray);
                /**/
                previousCode = taeditor.getText();
                previousTest = tatest.getText();
                lbstatus.setText("RED");
                lbstatus.setId("red");
                break;
        }
    }

    public static void back(ITDDTextArea taeditor, ITDDTextArea tatest, ITDDLabel lbstatus, Button btback, Babysteps baby, Tracking tracker) {
        /*Tracking*/
        tracker.startTimer(0);
        /**/
        btback.setDisable(true);
        taeditor.setText(previousCode);
        tatest.setText(previousTest);
        lbstatus.setText("RED");
        lbstatus.setId("red");
        TDDController.toTestEditor(taeditor, tatest);
        target = CompileTarget.TEST;
        baby.refreshTimer();
    }

    public static void setPreviousCode(String oldCode) {
        previousCode = oldCode;
    }

    public static void setPreviousTest(String oldTest) {
        previousTest = oldTest;
    }

    static void showErrors(JavaStringCompiler compiler, Collection<CompileError> errorsProgram, Collection<CompileError> errorsTest, TextArea taterminal, TextArea tatestterminal, Exercise currentExercise, ITDDLabel lbstatus, List<ErrorObject> errors) {
        for(CompileError error : errorsProgram) {
            String currentTerminal = taterminal.getText();
            taterminal.setText(currentTerminal + " " + error.getLineNumber() + ": " + error.getMessage() + "\n" + "\n");
        }

        String errorMessagesProgram = "Compiler error in program:" + "\n" + "\n";
        String errorMessagesTest = "Compiler error in test:" + "\n" + "\n";
        ErrorType error = error(compiler, errorsProgram, errorsTest, currentExercise, lbstatus);
        if (target == CompileTarget.TEST) {
            if(error == ErrorType.compilerErrorTest) {
                tatestterminal.setText(errorMessagesTest + tatestterminal.getText());

            } else if (error == ErrorType.TestsNotSucceeded) {
                tatestterminal.setText("Alle Tests müssen erfüllt werden!" +"\n" + "\n" + tatestterminal.getText());
            } else {
                tatestterminal.setText("Ein Test muss fehlschlagen!" +"\n" + "\n" + tatestterminal.getText());
            }

        } else {
            if(error == ErrorType.compilerErrorProgram) {
                taterminal.setText(errorMessagesProgram + taterminal.getText());
            } else if (error == ErrorType.compilerErrorTest) {
                tatestterminal.setText(errorMessagesTest + tatestterminal.getText());
            } else {
                taterminal.setText("Alle Tests müssen erfüllt werden!" + "\n" + "\n" + taterminal.getText());
            }
        }
        errors.add(new ErrorObject(error, taterminal.getText() + tatestterminal.getText(), lbstatus.getText()));
    }

    static void showTestResults(JavaStringCompiler compiler, TextArea tatestterminal) {
        TestResult result = compiler.getTestResult();
        String output = "";
        if(result == null) {
            return;
        }
        output = output + "Number of failed tests: " + result.getNumberOfFailedTests() +"\n" +"\n";
        output = output + "Number of ignored tests: " + result.getNumberOfIgnoredTests() +"\n" +"\n";
        output = output + "Number of successful tests: " + result.getNumberOfSuccessfulTests() +"\n" +"\n";

        for(TestFailure failure: result.getTestFailures()) {
            output = output + failure.getMethodName() +": " + failure.getMessage() +"\n" +"\n";
        }
        tatestterminal.setText(tatestterminal.getText() + output);

    }

    static ErrorType error(JavaStringCompiler compiler, Collection<CompileError> compilerProgramErrors, Collection<CompileError> compilerTestsErrors, Exercise curentExercise, ITDDLabel lbstatus) {

        switch (lbstatus.getText()) {
            case "RED":
                if(compiler.getCompilerResult().hasCompileErrors()) {
                    for(CompileError e: compilerTestsErrors) {
                        if(e.getMessage().indexOf("cannot find symbol") == -1 || e.getMessage().indexOf(curentExercise.getClassName()) == -1 || e.getMessage().indexOf(curentExercise.getTestName()) != -1) {
                            return ErrorType.compilerErrorTest;
                        }
                    }
                } else if(compiler.getTestResult().getNumberOfFailedTests() == 0) {
                    return ErrorType.TestsNotFailed;
                }
                return ErrorType.NOERROR;
            default:
                if(!compilerProgramErrors.isEmpty()) {
                    return ErrorType.compilerErrorProgram;
                }
                if(!compilerTestsErrors.isEmpty() || compiler.getTestResult().getNumberOfFailedTests() > 0) {
                    return ErrorType.TestsNotSucceeded;
                }
        }
        return ErrorType.NOERROR;
    }

}
