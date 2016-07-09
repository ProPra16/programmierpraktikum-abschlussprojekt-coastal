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
    protected String oldEditorText;
    protected String oldTestText;
    protected Exercise exercise;
    protected Timer t = new Timer();
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
            oldTestText = test.getText();
        }
        if (status.getText().equals("GREEN")) {
            oldEditorText = editor.getText();
        }
        if (timer > 0) {
            t.schedule(new TimerTask() {
                public void run() {
                    Platform.runLater(() -> {
                        if (!status.getText().equals("REFACTOR CODE") && !status.getText().equals("REFACTOR TEST")) {
                            int minutes = timer / 60;
                            int seconds = timer % 60;
                            time.setText(Integer.toString(minutes) + ":" + Integer.toString(seconds));
                            if (timer == 0) {
                                timer = oldTimer;
                                if (status.getText().equals("RED")) {
                                    test.setText(oldTestText);
                                } else {
                                    editor.setText(oldEditorText);
                                }
                            }
                            timer--;
                        }
                        else {
                            TDDTMenu.baby.time.setText("-:-");
                        }
                    });
                }
            }, 0, 1000);
        }
    }
}