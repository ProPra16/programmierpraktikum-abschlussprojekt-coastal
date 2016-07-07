package de.hhu.propra16.coastal.tddt;

import java.lang.Thread;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;

public class Babysteps {
    private int timer;
    private int oldtimer;
    private ITDDLabel status;
    private ITDDLabel time;
    private static Timer t;
    public Babysteps(Exercise currentExercise, ITDDLabel lbstatus, ITDDLabel lbtime) {
        timer = currentExercise.getBabystepTime();
        oldtimer = timer;
        status = lbstatus;
        time = lbtime;
    }

    public void babystep() {
        if (!status.getText().equals("REFACTOR CODE") && !status.getText().equals("REFACTOR TEST")) {
            if (timer > 0) {
                t = new Timer();
                t.schedule(new TimerTask() {
                    public void run() {
                        Platform.runLater(() -> {
                            timer--;
                            int minutes = timer / 60;
                            int seconds = timer % 60;
                            time.setText(Integer.toString(minutes) + ":" + Integer.toString(seconds));
                            if (timer == 0) {
                                timer = oldtimer;
                            }
                        });
                    }
                }, 1000, 1000);
            }
        }
        else {
            time.setText("-:-");
        }
    }
}