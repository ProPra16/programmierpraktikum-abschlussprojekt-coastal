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
    

    public Babysteps(Exercise currentExercise) {
        timer = currentExercise.getBabystepTime();
        oldTimer = timer;
    }

    public void babystep(ITDDLabel lbstatus, ITDDLabel lbtime, ITDDTextArea taeditor, ITDDTextArea tatest) {
        if (lbstatus.getText().equals("RED")) {
            oldTestText = tatest.getText();
        }
        if (lbstatus.getText().equals("GREEN")) {
            oldEditorText = taeditor.getText();
        }
        if (timer > 0) {
            Timer t = new Timer();
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

    public void setOldTest(ITDDTextArea tatest) {
        oldTestText = tatest.getText();
    }

    public void setOldEditor(ITDDTextArea taeditor) {
        oldEditorText = taeditor.getText();
    }
}