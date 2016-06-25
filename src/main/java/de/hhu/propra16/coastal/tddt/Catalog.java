package de.hhu.propra16.coastal.tddt;

import javafx.scene.control.ListView;
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

    public void loadExercise(TextArea taeditor, TextArea tatest, ListView lvexercises, int exercise) {
        loadClassContent(taeditor, exercise);
        loadTestContent(tatest, exercise);
        loadInListView(lvexercises);
    }

    private void loadClassContent(TextArea taeditor, int exercise) {
        taeditor.setText(mExercises.get(exercise).getClassContent());
    }

    private void loadTestContent(TextArea tatest, int exercise) {
        tatest.setText(mExercises.get(exercise).getTestContent());
    }

    private void loadInListView(ListView lvexercises) {
        for(Exercise exercise: mExercises) {
            lvexercises.getItems().add(exercise);
        }
    }

    public ArrayList<Exercise> getExercises() {
        return mExercises;
    }
}
