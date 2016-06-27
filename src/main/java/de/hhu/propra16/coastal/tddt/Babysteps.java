package de.hhu.propra16.coastal.tddt;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.lang.Thread;
import javafx.scene.control.Label;

public class Babysteps {
    private Date time;
    public Babysteps(Exercise exercise, Label lbstatus) {
        time = exercise.getBabystepTime();
    }

    public static void babystep() {
        /*while (exercise.isBabysteps()) {
            while (!lbstatus.getText().equals("REFACTOR")) { // && time != 0
                // time = time - 1 Sekunde
            }
            switch (lbstatus.getText()) {
                case "RED":
                    lbstatus.setText("GREEN");
                    lbstatus.setId("green");
                    break;
                case "GREEN":
                    lbstatus.setText("REFACTOR");
                    lbstatus.setId("black");
                    break;
                case "REFACTOR":
                    lbstatus.setText("RED");
                    lbstatus.setId("red");
                    break;
            }
            // time = old time
        }*/
    }
}