package de.hhu.propra16.coastal.tddt.catalog;

import de.hhu.propra16.coastal.tddt.gui.ITDDLabel;
import de.hhu.propra16.coastal.tddt.gui.ITDDListView;
import de.hhu.propra16.coastal.tddt.gui.ITDDTextArea;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;

public class Catalog {

    private ArrayList<Exercise> mExercises;

    private String name;

    public Catalog(String name) {
        mExercises = new ArrayList<>();
        this.name = name;
    }

    public void addExercise(Exercise exercise) {
        mExercises.add(exercise);
    }


    public void loadExercise(ITDDTextArea taeditor, ITDDTextArea tatest, ITDDLabel lbdescription, Exercise exercise) {
        loadClassContent(taeditor, exercise);
        loadTestContent(tatest, exercise);
        loadDescription(lbdescription, exercise);
    }

    public void loadExercise(ITDDTextArea taeditor, ITDDTextArea tatest, ITDDLabel lbdescription, Exercise exercise, File program, File test) {
        loadClassContent(taeditor, program);
        loadTestContent(tatest, test);
        loadDescription(lbdescription, exercise);
    }

    public void loadInListView(ITDDListView<Exercise> lvexercises) {
        lvexercises.getItems().clear();
        for(Exercise exercise : mExercises) {
            lvexercises.getItems().add(exercise);
        }
    }

    private void loadClassContent(ITDDTextArea taeditor, Exercise exercise) {
        taeditor.setText(exercise.getClassContent());
    }

    private void loadTestContent(ITDDTextArea tatest, Exercise exercise) {
        tatest.setText(exercise.getTestContent());
    }

    private void loadClassContent(ITDDTextArea taeditor, File program) {
        try {
            taeditor.setText(new String(Files.readAllBytes(program.toPath())));
        } catch (Exception e) {
            System.err.println("Fehler des Programmcodes");
        }
    }

    private void loadTestContent(ITDDTextArea tatest, File test) {
        try {
            tatest.setText(new String(Files.readAllBytes(test.toPath())));
        } catch (Exception e) {
            System.err.println("Fehler des Testcodes");
        }
    }

    private void loadDescription(ITDDLabel lbdescription, Exercise exercise) {
        lbdescription.setText(exercise.getDescription());
    }

    public ArrayList<Exercise> getExercises() {
        return mExercises;
    }

    public String toString() {
        return name;
    }
}
