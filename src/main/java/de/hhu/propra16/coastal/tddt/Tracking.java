package de.hhu.propra16.coastal.tddt;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

import java.util.ArrayList;

public class Tracking {
    ArrayList<Timer> timerList;
    /*A Tracking datatype automatically contains one Timer object in its ArrayList*/
    public Tracking(){
        timerList = new ArrayList<Timer>();
        timerList.add(new Timer());
    }

    public void addTimer(){
        timerList.add(new Timer());
    }

    /*Operating with the standard Timer*/
    public void startTimer(){
        timerList.get(0).start();
    }

    public void stopTimer(){
        timerList.get(0).end();
    }

    public int getTime() {
        return timerList.get(0).returnTimeSpentInSeconds();
    }

    public void setLabel(String s){
        timerList.get(0).setLabel(s);
    }

    public String getLabel(){
        return timerList.get(0).getLabel();
    }

    public boolean started(){
        return timerList.get(0).started();
    }
    /*Operating with any timer in the Arraylist*/
    public void startTimer(int x){
        if(x<timerList.size()) {
            timerList.get(x).start();
        }
    }

    public void stopTimer(int x){
        if(x<timerList.size()) {
            timerList.get(x).end();
        }
    }

    public int getTime(int x) {
        if (x>=timerList.size()) return -1;
        return timerList.get(x).returnTimeSpentInSeconds();
    }

    public void setLabel(String s, int x){
        timerList.get(x).setLabel(s);
    }

    public String getLabel(int x){
        return timerList.get(x).getLabel();
    }

    public boolean started(int x){
        return timerList.get(x).started();
    }
}