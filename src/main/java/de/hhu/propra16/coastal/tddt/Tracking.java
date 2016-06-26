package de.hhu.propra16.coastal.tddt;
import java.util.ArrayList;

public class Tracking {
    ArrayList<Timer> timerList;
    /*A Tracking datatype automatically contains one Timer object in its ArrayList*/
    public Tracking(){
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

    public int getTimer() {
        return timerList.get(0).returnTimeSpentInSeconds();
    }

    /*Operating with any timer in the Arraylist*/
    public void startTimerX(int x){
        if(x>=timerList.size()) return;
        timerList.get(x).start();
    }

    public void stopTimerX(int x){
        if(x>=timerList.size()) return;
        timerList.get(x).end();
    }

    public int getTimerX(int x) {
        if (x >=timerList.size()) return 0;
        return timerList.get(x).returnTimeSpentInSeconds();
    }
}
