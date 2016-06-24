package de.hhu.propra16.coastal.tddt;

import javafx.scene.control.TextArea;

import java.util.ArrayList;

public class Catalog {

    private ArrayList<Exercise> mExercises;

    public Catalog() {
        mExercises = new ArrayList<>();
    }

    public void addExercise(Exercise exercise) {
        mExercises.add(exercise);
    }

    public void loadExercise(TextArea taeditor, TextArea tatest, int exercise) {
        loadClassContent(taeditor, exercise);
        loadTestContent(tatest, exercise);
    }

    private void loadClassContent(TextArea taeditor, int exercise) {
        taeditor.setText(mExercises.get(exercise).getClassContent());
    }

    private void loadTestContent(TextArea tatest, int exercise) {
        tatest.setText(mExercises.get(exercise).getTestContent());
    }
}
