package de.hhu.propra16.coastal.tddt.gui;

import java.util.Timer;
import java.util.TimerTask;

import de.hhu.propra16.coastal.tddt.catalog.Exercise;
import javafx.application.Platform;

public class Babysteps {
    private int timer;
    private int oldTimer;
    private String oldEditorText;
    private String oldTestText;
    private Timer t;


    public Babysteps(Exercise currentExercise, ITDDTextArea taeditor, ITDDTextArea tatest) {
        timer = currentExercise.getBabystepTime();
        oldTimer = timer;
        oldTestText = tatest.getText();
        oldEditorText = taeditor.getText();
        t = new Timer();

    }

    public void babystep(ITDDLabel lbstatus, ITDDLabel lbtime, ITDDTextArea taeditor, ITDDTextArea tatest) {
        if (timer > 0) {
            t.schedule(new TimerTask() {
                public void run() {
                    Platform.runLater(() -> {
                        if (!lbstatus.getText().equals("REFACTOR CODE") && !lbstatus.getText().equals("REFACTOR TEST")) {
                            int minutes = timer / 60;
                            int seconds = timer % 60;
                            lbtime.setText(Integer.toString(minutes) + ":" + Integer.toString(seconds));
                            if (timer == 0) {
                                timer = oldTimer;
                                if (lbstatus.getText().equals("RED")) {
                                    tatest.setText(oldTestText);
                                } else {
                                    taeditor.setText(oldEditorText);
                                }
                            }
                            timer--;
                        }
                        else {
                            lbtime.setText("-:-");
                        }
                    });
                }
            }, 0, 1000);
        }
    }

    public void refreshTimer() {
        timer = oldTimer;
    }
    
    public void stopTimer() {
        t.cancel();
    }

    public void getOldTest(ITDDTextArea tatest) {
        oldTestText = tatest.getText();
    }

    public void getOldEditor(ITDDTextArea taeditor) {
        oldEditorText = taeditor.getText();
    }
}