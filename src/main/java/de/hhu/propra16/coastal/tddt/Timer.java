package de.hhu.propra16.coastal.tddt;
public class Timer {
    int timeSpend;
    long start;
    long end;
    public Timer(){
    }

    public void startTracking() {
        start = System.currentTimeMillis();
    }

    public void endTracking(){
        end = System.currentTimeMillis();
    }

    public int returnTimeSpentInSeconds() {
        timeSpend = (int) (end-start)/1000;
        return timeSpend;
    }

}
