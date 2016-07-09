package de.hhu.propra16.coastal.tddt.catalog;

import de.hhu.propra16.coastal.tddt.gui.ITDDLabel;
import de.hhu.propra16.coastal.tddt.gui.ITDDListView;
import de.hhu.propra16.coastal.tddt.gui.ITDDTextArea;

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
