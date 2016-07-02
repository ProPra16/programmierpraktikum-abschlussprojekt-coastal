package de.hhu.propra16.coastal.tddt;

import java.lang.Thread;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;

public class Babysteps {
    private long time;
    private TDDTMenu tddt;
    private Exercise exercise;
    public Babysteps(Exercise currentExercise, TDDTMenu tddtmenu) {
        tddt = tddtmenu;
        exercise = currentExercise;
        time = exercise.getBabystepTime();
    }

    public void babystep() {
        ActionEvent event = new ActionEvent();
        if (!tddt.lbstatus.getText().equals("REFACTOR")) {
            // String oldText = irgendwas;
            while (time > 0) {
                try {
                    Thread.sleep(1000);
                }
                catch (InterruptedException e) { }
                time--;
                if (time == 0) {
                    time = exercise.getBabystepTime();
                }
                long minutes = time/60;
                long seconds = time%60;
                tddt.lbtime.setText(String.valueOf(minutes) + ":" + String.valueOf(seconds));
            }
        }
    }
    // public void blabla(Button "Nächster Schritt" wird angeklickt) {
        // tddt.next(event); }
}