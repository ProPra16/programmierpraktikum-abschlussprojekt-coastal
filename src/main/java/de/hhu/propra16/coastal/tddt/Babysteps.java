package de.hhu.propra16.coastal.tddt;

import java.lang.Thread;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;

public class Babysteps {
    private int timer;
    private ITDDLabel status;
    private ITDDLabel time;
    public Babysteps(Exercise currentExercise, ITDDLabel lbstatus, ITDDLabel lbtime) {
        timer = currentExercise.getBabystepTime();
        status = lbstatus;
        time = lbtime;
    }

    public void babystep() {
        //ActionEvent event = new ActionEvent();
        if (!status.getText().equals("REFACTOR CODE") && !status.getText().equals("REFACTOR TEST")) {
            // String oldText = irgendwas;
            /*while (time > 0) {
                try {
                    Thread.sleep(1000);
                }
                catch (InterruptedException e) { }
                time--;
                if (time == 0) {
                    time = exercise.getBabystepTime();
                }*/
                int minutes = timer/60;
                int seconds = timer%60;
                time.setText(Integer.toString(minutes) + ":" + Integer.toString(seconds));
            //}
        }
        else {
            time.setText("00:00");
        }
    }
    // public void blabla(Button "Nächster Schritt" wird angeklickt) {
        // tddt.next(event); }
}