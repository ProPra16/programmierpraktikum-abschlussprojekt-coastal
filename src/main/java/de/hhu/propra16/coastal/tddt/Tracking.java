package de.hhu.propra16.coastal.tddt;
/**
 * Created by Jimmy on 26.06.2016.
 */
public class Tracking {
    int timeSpend;
    long start;
    long end;
    public Tracking(){
    }

    public void startTracking() {
        start = System.currentTimeMillis();
    }

    public void endTracking(){
        end = System.currentTimeMillis();
    }

    public int returnTimeSpentinSeconds() {
        timeSpend = (int) (end-start)/1000;
        return timeSpend;
    }




}
