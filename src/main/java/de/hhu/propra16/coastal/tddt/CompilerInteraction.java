package de.hhu.propra16.coastal.tddt;

import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import vk.core.api.*;

import java.util.Collection;

/**
 * Created by student on 02/07/16.
 */

public class CompilerInteraction {


    public static void compile(ITDDTextArea taeditor, ITDDTextArea tatest, TextArea taterminal, TextArea tatestterminal, ITDDLabel lbstatus, ITDDLabel lbtime, Exercise currentExercise, ITDDListView<Exercise> lvexercises, Button btback) {
        taterminal.clear();
        tatestterminal.clear();
        boolean compileError = false;
        if (lvexercises.getItems().isEmpty() || currentExercise == null) {
            return;
        }

        CompilationUnit compilationUnitProgram = new CompilationUnit(currentExercise.getClassName(), taeditor.getText(), false);
        CompilationUnit compilationUnitTest = new CompilationUnit(currentExercise.getTestName(), tatest.getText(), true);
        JavaStringCompiler compiler = CompilerFactory.getCompiler(compilationUnitProgram, compilationUnitTest);

        compiler.compileAndRunTests();
        Collection<CompileError> errorsProgram = compiler.getCompilerResult().getCompilerErrorsForCompilationUnit(compilationUnitProgram);
        Collection<CompileError> errorsTest = compiler.getCompilerResult().getCompilerErrorsForCompilationUnit(compilationUnitTest);

        for (CompileError error : errorsTest) {
            compileError = true;
            String currentTerminal = tatestterminal.getText();
            tatestterminal.setText(currentTerminal + " " + error.getLineNumber() + ": " + error.getMessage() + "\n" + "\n");
        }

        if (!compileError) {
            CompilerReport.showTestResults(compiler, tatestterminal);
        }

        if (continueable(compiler, errorsProgram, errorsTest, currentExercise, lbstatus)) {
            CompilerReport.changeReport(taeditor, tatest, lbstatus, btback, currentExercise);
        } else {
            CompilerReport.showErrors(compiler, errorsProgram, errorsTest, taterminal, tatestterminal, currentExercise, lbstatus);
        }
    }

    private static boolean continueable(JavaStringCompiler compiler, Collection<CompileError> compileProgramErrors, Collection<CompileError> compileTestsErrors, Exercise currentExercise, ITDDLabel lbstatus) {
        if (CompilerReport.error(compiler,  compileProgramErrors, compileTestsErrors, currentExercise, lbstatus) == ErrorType.NOERROR) {
            return true;
        }
        return false;
    }
}
