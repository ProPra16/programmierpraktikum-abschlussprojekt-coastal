package de.hhu.propra16.coastal.tddt;

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

    public static void compile(ITDDTextArea taeditor, ITDDTextArea tatest, TextArea taterminal, TextArea tatestterminal, ITDDLabel lbstatus, Exercise currentExercise, ITDDListView<Exercise> lvexercises) {
        taterminal.clear();
        tatestterminal.clear();
        if(lvexercises.getItems().isEmpty() || currentExercise == null) {
            return;
        }
        CompilationUnit compilationUnitProgram = new CompilationUnit(currentExercise.getClassName(), taeditor.getText(), false);
        CompilationUnit compilationUnitTest = new CompilationUnit(currentExercise.getTestName(), tatest.getText(), true);

        JavaStringCompiler compiler = CompilerFactory.getCompiler(compilationUnitTest, compilationUnitProgram);

        compiler.compileAndRunTests();
        Collection<CompileError> errorsProgram = compiler.getCompilerResult().getCompilerErrorsForCompilationUnit(compilationUnitProgram);
        Collection<CompileError> errorsTest = compiler.getCompilerResult().getCompilerErrorsForCompilationUnit(compilationUnitTest);


        for(CompileError error : errorsTest) {
            String currentTerminal = tatestterminal.getText();
            tatestterminal.setText(currentTerminal + " " + error.getLineNumber() + ": " + error.getMessage() + "\n" + "\n");
        }
        if(continueable(compiler, errorsTest, lbstatus)) {
            changeReport(taeditor, tatest, lbstatus);

        } else {
            showErrors(compiler, errorsProgram, errorsTest, taterminal, tatestterminal, lbstatus);
        }
    }

    private static void showErrors(JavaStringCompiler compiler, Collection<CompileError> errorsProgram, Collection<CompileError> errorsTest, TextArea taterminal, TextArea tatestterminal, ITDDLabel lbstatus) {
        for(CompileError error : errorsProgram) {
            String currentTerminal = taterminal.getText();
            taterminal.setText(currentTerminal + " " + error.getLineNumber() + ": " + error.getMessage() + "\n" + "\n");
        }

        String errorMessagesProgram = "Compiler Error in Program:" + "\n" + "\n";
        String errorMessagesTest = "Compiler Error in Test:" + "\n" + "\n";
        ErrorType error = error(compiler, errorsTest, lbstatus);
        if (target == CompileTarget.TEST) {
            if(error == ErrorType.compilerErrorTest) {
                tatestterminal.setText(errorMessagesTest + tatestterminal.getText());

            } else {
                tatestterminal.setText("Alle Tests müssen fehlschlagen!" +"\n" + "\n" + tatestterminal.getText());
            }

        } else {
            if(error == ErrorType.compilerErrorProgram) {
                taterminal.setText(errorMessagesProgram + taterminal.getText());
            } else {
                taterminal.setText("Alle Tests müssen erfüllt werden" + "\n" + "\n" + taterminal.getText());
            }
        }
    }

    private static boolean continueable(JavaStringCompiler compiler, Collection<CompileError> compileTestsErrors, ITDDLabel lbstatus) {
        switch (lbstatus.getText()) {
            case "RED":
                if(error(compiler, compileTestsErrors, lbstatus) == ErrorType.compilerErrorTest) {
                    return false;
                }
                if (compileTestsErrors.size() > 0) {
                    return true;
                }
                return false;
            default:
                if(!compiler.getCompilerResult().hasCompileErrors() && compiler.getTestResult().getNumberOfFailedTests() == 0) {
                    return true;
                }
                return false;
        }
    }

    private static ErrorType error(JavaStringCompiler compiler, Collection<CompileError> compilerTestsErrors, ITDDLabel lbstatus) {
        switch (lbstatus.getText()) {
            case "RED":
                if(compiler.getCompilerResult().hasCompileErrors()) {
                    for(CompileError e: compilerTestsErrors) {
                        if(e.getMessage().indexOf("cannot find symbol") == -1) {
                            return ErrorType.compilerErrorTest;
                        }
                    }
                    return ErrorType.NOERROR;

                }
                if(compiler.getTestResult().getNumberOfFailedTests() == 0) {
                    return ErrorType.TestsNotFailed;
                }
            default:
                if(compiler.getCompilerResult().hasCompileErrors()) {
                    return ErrorType.compilerErrorProgram;
                }
                if(compiler.getTestResult().getNumberOfFailedTests() > 0) {
                    return ErrorType.TestsNotSucceeded;
                }
        }
        return ErrorType.NOERROR;
    }

    private static void changeReport(ITDDTextArea taeditor, ITDDTextArea tatest, ITDDLabel lbstatus) {
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

}
