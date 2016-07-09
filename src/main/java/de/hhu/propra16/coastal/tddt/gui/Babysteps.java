package de.hhu.propra16.coastal.tddt.gui;

import java.util.Timer;
import java.util.TimerTask;

import de.hhu.propra16.coastal.tddt.catalog.Exercise;
import javafx.application.Platform;

public class Babysteps {
    public int timer;
    public int oldTimer;
    public ITDDLabel status;
    public ITDDLabel time;
    public ITDDTextArea editor;
    public ITDDTextArea test;
    public String oldEditorText;
    public String oldTestText;
    public Exercise exercise;
    public Timer t = new Timer();
    
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