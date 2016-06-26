package de.hhu.propra16.coastal.tddt;
public class Timer {
    int timeSpend;
    long start = 0;
    long end = 0;
    public Timer(){
    }

    public void start() {
        start = System.currentTimeMillis();
    }

    public void end(){
        end = System.currentTimeMillis();
    }

    public int returnTimeSpentInSeconds() {
        timeSpend = (int) (end-start)/1000;
        return timeSpend;
    }

}
