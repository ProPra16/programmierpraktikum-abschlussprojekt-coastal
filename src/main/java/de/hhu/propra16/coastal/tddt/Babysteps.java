package de.hhu.propra16.coastal.tddt;

import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;

public class Babysteps {
    protected int timer;
    protected int oldTimer;
    protected ITDDLabel status;
    protected ITDDLabel time;
    protected ITDDTextArea editor;
    protected ITDDTextArea test;
    protected String oldCode;
    protected String olderCode;
    protected Exercise exercise;
    public Babysteps(Exercise currentExercise, ITDDLabel lbstatus, ITDDLabel lbtime, ITDDTextArea taeditor, ITDDTextArea tatest) {
        timer = currentExercise.getBabystepTime();
        oldTimer = timer;
        status = lbstatus;
        time = lbtime;
        editor = taeditor;
        test = tatest;
        exercise = currentExercise;
    }

    public void babystep() {
        if (status.getText().equals("RED")) {
            oldCode = test.getText();
        }
        if (status.getText().equals("GREEN")) {
            olderCode = test.getText();
            oldCode = editor.getText();
        }
        if (timer > 0) {
            Timer t = new Timer();
            t.schedule(new TimerTask() {
                public void run() {
                    Platform.runLater(() -> {
                        timer--;
                        int minutes = timer / 60;
                        int seconds = timer % 60;
                        time.setText(Integer.toString(minutes) + ":" + Integer.toString(seconds));
                        if (timer == 0) {
                            timer = oldTimer;
                            if (status.getText().equals("RED")) {
                                test.setText(oldCode);
                            }
                            else {
                                editor.setText(oldCode);
                            }
                        }
                    });
                }
            }, 1000, 1000);
        }
    }
}