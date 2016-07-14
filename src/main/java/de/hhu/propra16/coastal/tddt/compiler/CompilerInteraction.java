package de.hhu.propra16.coastal.tddt.compiler;

import de.hhu.propra16.coastal.tddt.tracking.*;
import de.hhu.propra16.coastal.tddt.catalog.Exercise;
import de.hhu.propra16.coastal.tddt.gui.Babysteps;
import de.hhu.propra16.coastal.tddt.gui.ITDDLabel;
import de.hhu.propra16.coastal.tddt.gui.ITDDListView;
import de.hhu.propra16.coastal.tddt.gui.ITDDTextArea;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import vk.core.api.*;

import java.util.Collection;
import java.util.List;

/**
 * Created by student on 02/07/16.
 */

public class CompilerInteraction {


    public static void compile(ITDDTextArea taeditor, ITDDTextArea tatest, TextArea taterminal, TextArea tatestterminal, ITDDLabel lbstatus, ITDDLabel lbtime, Exercise currentExercise, ITDDListView<Exercise> lvexercises, Button btback, Babysteps baby, Tracking tracker, List<ErrorObject> errors) {
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
            CompilerReport.changeReport(taeditor, tatest, lbstatus, btback, currentExercise, baby, tracker);
        } else {
            CompilerReport.showErrors(compiler, errorsProgram, errorsTest, taterminal, tatestterminal, currentExercise, lbstatus, errors);
        }
    }

    private static boolean continueable(JavaStringCompiler compiler, Collection<CompileError> compileProgramErrors, Collection<CompileError> compileTestsErrors, Exercise currentExercise, ITDDLabel lbstatus) {
        if (CompilerReport.error(compiler,  compileProgramErrors, compileTestsErrors, currentExercise, lbstatus) == ErrorType.NOERROR) {
            return true;
        }
        return false;
    }
}
