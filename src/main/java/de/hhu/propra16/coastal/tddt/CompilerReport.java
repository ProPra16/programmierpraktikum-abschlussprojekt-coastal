package de.hhu.propra16.coastal.tddt;

import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import vk.core.api.CompileError;
import vk.core.api.JavaStringCompiler;
import vk.core.api.TestFailure;
import vk.core.api.TestResult;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by student on 06/07/16.
 */
public class CompilerReport {

    private static File fileChart = new File("src/main/resources/de/hhu/propra16/coastal/tddt/chart.txt");

    private static String previousCode;

    private static CompileTarget target = CompileTarget.TEST;

    /*chart File into String[]*/
    @Deprecated
    static int[] readAll(String filePath){
        /*init declar*/
        ArrayList<String> outputArrayList = new ArrayList<>();
        int cnt=0;
        String tmp;
        /*into String*/
        try{
            BufferedReader loadChart = new BufferedReader(new FileReader(filePath));
            tmp = loadChart.readLine();
            while(tmp!=null){
                if(tmp.isEmpty()) break;
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
    @Deprecated
    static void save(int[] saveTo){
        try{
            String saveToString ="";
            BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/resources/de/hhu/propra16/coastal/tddt/chart.txt"));
            for(int i=0; i<saveTo.length; i++){
                saveToString +=saveTo[i]+"\n";
            }
            writer.write(saveToString);
        }
        catch(IOException ex){
            System.out.println(ex.toString());
        }
    }

    static void changeReport(ITDDTextArea taeditor, ITDDTextArea tatest, ITDDLabel lbstatus, Button btback, Exercise currentExercise) {
        /*load chart*/
        //int[] chartArray = readAll("src/main/resources/de/hhu/propra16/coastal/tddt/chart.txt");

        /*implementing Tracking*/
        /*Tracking tracker;
        tracker = new Tracking();
        for(int i=0; i<3; i++){
            tracker.addTimer();
        }*/
        TDDTMenu.baby.timer = TDDTMenu.baby.oldTimer;
        switch (lbstatus.getText()) {
            case "RED":
                /*Tracking*/
                /*tracker.stopTimer(3);
                tracker.startTimer();
                if(chartArray.length==4){
                    chartArray[3]+=tracker.getTime(3);
                    save(chartArray);
                }
                else{
                    int[] first = new int[4];
                    for(int i=0; i<first.length; i++){
                        if(i<chartArray.length){
                            first[i] = chartArray[i];
                        }
                        else{
                            first[i] = 0;
                        }
                    }
                    //if(tracker.started(3)) first[3] = tracker.getTime(3);
                    save(first);
                }*/
                /**/
                btback.setDisable(false);
                lbstatus.setText("GREEN");
                lbstatus.setId("green");
                TDDController.toEditor(taeditor, tatest);
                target = CompileTarget.EDITOR;
                if(currentExercise.isBabysteps()) {
                    TDDTMenu.baby.oldTestText = TDDTMenu.baby.test.getText();
                }
                break;
            case "GREEN":
                /*Tracking*/
                /*tracker.stopTimer();
                tracker.startTimer(1);
                chartArray[0]+=tracker.getTime();
                save(chartArray);*/
                /**/
                btback.setDisable(true);
                lbstatus.setText("REFACTOR CODE");
                lbstatus.setId("black");
                target = CompileTarget.EDITOR;
                if(currentExercise.isBabysteps()) {
                    TDDTMenu.baby.oldEditorText = TDDTMenu.baby.editor.getText();
                }
                break;
            case "REFACTOR CODE":
                /*Tracking*/
                /*tracker.stopTimer(1);
                tracker.startTimer(2);
                chartArray[1]+=tracker.getTime(1);
                save(chartArray);*/
                /**/
                lbstatus.setText("REFACTOR TEST");
                TDDController.toTestEditor(taeditor, tatest);
                target = CompileTarget.TEST;
                break;
            case "REFACTOR TEST":
                /*Tracking*/
                /*tracker.stopTimer(2);
                tracker.startTimer(3);
                chartArray[2]+=tracker.getTime(2);
                save(chartArray);*/
                /**/
                previousCode = taeditor.getText();
                lbstatus.setText("RED");
                lbstatus.setId("red");
                break;
        }
    }

    public static void back(ITDDTextArea taeditor, ITDDTextArea tatest, ITDDLabel lbstatus, Button btback) {
        btback.setDisable(true);
        taeditor.setText(previousCode);
        lbstatus.setText("RED");
        lbstatus.setId("red");
        TDDController.toTestEditor(taeditor, tatest);
        target = CompileTarget.TEST;
        TDDTMenu.baby.editor.setText(TDDTMenu.baby.oldEditorText);
    }

    public static void setPreviousCode(String previous) {
        previousCode = previous;
    }

    static void showErrors(JavaStringCompiler compiler, Collection<CompileError> errorsProgram, Collection<CompileError> errorsTest, TextArea taterminal, TextArea tatestterminal, Exercise currentExercise, ITDDLabel lbstatus) {
        for(CompileError error : errorsProgram) {
            String currentTerminal = taterminal.getText();
            taterminal.setText(currentTerminal + " " + error.getLineNumber() + ": " + error.getMessage() + "\n" + "\n");
        }

        String errorMessagesProgram = "Compiler Error in Program:" + "\n" + "\n";
        String errorMessagesTest = "Compiler Error in Test:" + "\n" + "\n";
        ErrorType error = error(compiler, errorsProgram, errorsTest, currentExercise, lbstatus);
        if (target == CompileTarget.TEST) {
            if(error == ErrorType.compilerErrorTest) {
                tatestterminal.setText(errorMessagesTest + tatestterminal.getText());

            } else if (error == ErrorType.TestsNotSucceeded) {
                tatestterminal.setText("Alle Tests müssen erfüllt werden" +"\n" + "\n" + tatestterminal.getText());
            } else {
                tatestterminal.setText("Ein Test muss fehlschlagen!" +"\n" + "\n" + tatestterminal.getText());
            }

        } else {
            if(error == ErrorType.compilerErrorProgram) {
                taterminal.setText(errorMessagesProgram + taterminal.getText());
            } else if (error == ErrorType.compilerErrorTest) {
                tatestterminal.setText(errorMessagesTest + tatestterminal.getText());
            } else {
                taterminal.setText("Alle Tests müssen erfüllt werden" + "\n" + "\n" + taterminal.getText());
            }
        }
    }

    static void showTestResults(JavaStringCompiler compiler, TextArea tatestterminal) {
        TestResult result = compiler.getTestResult();
        String output = "";
        if(result == null) {
            return;
        }
        output = output + "Numbers of failed Tests: " + result.getNumberOfFailedTests() +"\n" +"\n";
        output = output + "Numbers of ignored Tests: " + result.getNumberOfIgnoredTests() +"\n" +"\n";
        output = output + "Numbers of successfull Tests: " + result.getNumberOfSuccessfulTests() +"\n" +"\n";

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
