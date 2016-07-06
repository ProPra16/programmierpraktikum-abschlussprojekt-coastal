package de.hhu.propra16.coastal.tddt;

import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import vk.core.api.CompilationUnit;
import vk.core.api.CompileError;
import vk.core.api.CompilerFactory;
import vk.core.api.JavaStringCompiler;

import java.util.Collection;

/**
 * Created by student on 02/07/16.
 */
public class CompilerInteraction {

    private static CompileTarget target = CompileTarget.TEST;

    private static String previousCode;

    public static void compile(ITDDTextArea taeditor, ITDDTextArea tatest, TextArea taterminal, TextArea tatestterminal, ITDDLabel lbstatus, Exercise currentExercise, ITDDListView<Exercise> lvexercises, Button btback) {
        taterminal.clear();
        tatestterminal.clear();
        if(lvexercises.getItems().isEmpty() || currentExercise == null) {
            return;
        }
        CompilationUnit compilationUnitProgram = new CompilationUnit(currentExercise.getClassName(), taeditor.getText(), false);
        CompilationUnit compilationUnitTest = new CompilationUnit(currentExercise.getTestName(), tatest.getText(), true);
        JavaStringCompiler compiler = CompilerFactory.getCompiler(compilationUnitProgram, compilationUnitTest);

        compiler.compileAndRunTests();
        Collection<CompileError> errorsProgram = compiler.getCompilerResult().getCompilerErrorsForCompilationUnit(compilationUnitProgram);
        Collection<CompileError> errorsTest = compiler.getCompilerResult().getCompilerErrorsForCompilationUnit(compilationUnitTest);

        for(CompileError error : errorsTest) {
            String currentTerminal = tatestterminal.getText();
            tatestterminal.setText(currentTerminal + " " + error.getLineNumber() + ": " + error.getMessage() + "\n" + "\n");
        }
        if(continueable(compiler, errorsProgram, errorsTest, currentExercise, lbstatus)) {
            changeReport(taeditor, tatest, lbstatus, btback);

        } else {
            showErrors(compiler, errorsProgram, errorsTest, taterminal, tatestterminal, currentExercise, lbstatus);
        }
    }

    private static void showErrors(JavaStringCompiler compiler, Collection<CompileError> errorsProgram, Collection<CompileError> errorsTest, TextArea taterminal, TextArea tatestterminal, Exercise currentExercise, ITDDLabel lbstatus) {
        for(CompileError error : errorsProgram) {
            String currentTerminal = taterminal.getText();
            taterminal.setText(currentTerminal + " " + error.getLineNumber() + ": " + error.getMessage() + "\n" + "\n");
        }

        String errorMessagesProgram = "Compiler Error in Program:" + "\n" + "\n";
        String errorMessagesTest = "Compiler Error in Test:" + "\n" + "\n";
        ErrorType error = error(compiler, errorsProgram, errorsTest, currentExercise, lbstatus);
        System.out.println(error);
        if (target == CompileTarget.TEST) {
            if(error == ErrorType.compilerErrorTest) {
                tatestterminal.setText(errorMessagesTest + tatestterminal.getText());

            } else if (error == ErrorType.TestsNotSucceeded) {
                tatestterminal.setText("Alle Tests müssen erfüllt werden" +"\n" + "\n" + tatestterminal.getText());
            } else {
                tatestterminal.setText("Alle Tests müssen fehlschlagen!" +"\n" + "\n" + tatestterminal.getText());
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

    private static boolean continueable(JavaStringCompiler compiler, Collection<CompileError> compileProgramErrors, Collection<CompileError> compileTestsErrors, Exercise currentExercise, ITDDLabel lbstatus) {
        if(error(compiler,  compileProgramErrors, compileTestsErrors, currentExercise, lbstatus) == ErrorType.NOERROR) {
            return true;
        }
        return false;
    }

    private static ErrorType error(JavaStringCompiler compiler, Collection<CompileError> compilerProgramErrors, Collection<CompileError> compilerTestsErrors, Exercise curentExercise, ITDDLabel lbstatus) {

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

    private static void changeReport(ITDDTextArea taeditor, ITDDTextArea tatest, ITDDLabel lbstatus, Button btback) {
        switch (lbstatus.getText()) {
            case "RED":
                btback.setDisable(false);
                lbstatus.setText("GREEN");
                lbstatus.setId("green");
                TDDController.toEditor(taeditor, tatest);
                target = CompileTarget.EDITOR;
                break;
            case "GREEN":
                btback.setDisable(true);
                lbstatus.setText("REFACTOR CODE");
                lbstatus.setId("black");
                target = CompileTarget.EDITOR;
                break;
            case "REFACTOR CODE":
                lbstatus.setText("REFACTOR TEST");
                TDDController.toTestEditor(taeditor, tatest);
                target = CompileTarget.TEST;
                break;
            case "REFACTOR TEST":
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





}
