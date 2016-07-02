package de.hhu.propra16.coastal.tddt;

import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.util.ArrayList;

public class Catalog {

    private ArrayList<Exercise> mExercises;

    public Catalog() {
        mExercises = new ArrayList<>();
    }

    public void addExercise(Exercise exercise) {
        mExercises.add(exercise);
    }

    public void loadExercises(ITDDTextArea taeditor, ITDDTextArea tatest, Label lbdescription, ListView<Exercise> lvexercises) {
        Exercise exercise = mExercises.get(0);
        loadClassContent(taeditor, exercise);
        loadTestContent(tatest, exercise);
        loadDescription(lbdescription, exercise);
        loadInListView(lvexercises);
        TDDController.toTestEditor(taeditor, tatest);
    }

    public void loadExercise(ITDDTextArea taeditor, TDDTextArea tatest, Label lbdescription, Exercise exercise) {
        loadClassContent(taeditor, exercise);
        loadTestContent(tatest, exercise);
        loadDescription(lbdescription, exercise);
    }

    public void loadInListView(ListView<Exercise> lvexercises) {
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

    private void loadDescription(Label lbdescription, Exercise exercise) {
        lbdescription.setText(exercise.getDescription());
    }

    public ArrayList<Exercise> getExercises() {
        return mExercises;
    }
}
