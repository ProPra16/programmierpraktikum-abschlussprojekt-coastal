package de.hhu.propra16.coastal.tddt;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import vk.core.api.CompileError;
import vk.core.api.JavaStringCompiler;
import vk.core.api.TestFailure;
import vk.core.api.TestResult;

import java.net.URL;
import java.util.Collection;
import java.util.ResourceBundle;

/**
 * Created by student on 06/07/16.
 */
public class CompilerReport {

    private static Tracking tracker;

    private static String previousCode;

    private static CompileTarget target = CompileTarget.TEST;



    static void changeReport(ITDDTextArea taeditor, ITDDTextArea tatest, ITDDLabel lbstatus, Button btback) {
        tracker = new Tracking();
        /*implementing Tracking*/
        for(int i=0; i<3; i++){
            tracker.addTimer();
        }

        switch (lbstatus.getText()) {
            case "RED":
                tracker.stopTimer(3);
                tracker.startTimer();
                btback.setDisable(false);
                lbstatus.setText("GREEN");
                lbstatus.setId("green");
                TDDController.toEditor(taeditor, tatest);
                target = CompileTarget.EDITOR;
                break;
            case "GREEN":
                tracker.stopTimer();
                tracker.startTimer(1);
                btback.setDisable(true);
                lbstatus.setText("REFACTOR CODE");
                lbstatus.setId("black");
                target = CompileTarget.EDITOR;
                break;
            case "REFACTOR CODE":
                tracker.stopTimer(1);
                tracker.startTimer(2);
                lbstatus.setText("REFACTOR TEST");
                TDDController.toTestEditor(taeditor, tatest);
                target = CompileTarget.TEST;
                break;
            case "REFACTOR TEST":
                tracker.stopTimer(2);
                tracker.startTimer(3);
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
        if(compiler.getTestResult() == null) {
            return;
        }
        TestResult result = compiler.getTestResult();
        String output = "";

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
