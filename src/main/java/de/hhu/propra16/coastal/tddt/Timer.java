package de.hhu.propra16.coastal.tddt;
public class Timer {
    String name;
    int timeSpend = 0;
    long start = 0;
    long end = 0;
    public Timer(){
    }

    public void start() {
        start = System.currentTimeMillis();
    }

    public void end(){
        end = System.currentTimeMillis();
        if(started()){
            timeSpend += (int) (end-start)/1000;
        }

    }

    public int returnTimeSpentInSeconds(){
        return timeSpend;
    }

    public void setLabel(String s){
        name = s;
    }

    public String getLabel(){
        return name;
    }

    /*check whether timer has been started*/
    public boolean started(){
        if(start==0) return false;
        return true;
    }

}