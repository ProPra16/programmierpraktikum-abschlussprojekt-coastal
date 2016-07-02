package de.hhu.propra16.coastal.tddt;

import org.junit.Test;
import static org.junit.Assert.*;

public class TrackingTest {
    Tracking t = new Tracking();
    Thread thread = new Thread();
    @Test
    public void firsTimer() throws Exception{
        t.startTimer();
        thread.sleep(1000);
        t.stopTimer();
        assertEquals(1, t.getTime());
    }

    @Test
    public void firstTimer2Seconds() throws Exception{
        t.startTimer();
        thread.sleep(2000);
        t.stopTimer();
        assertEquals(2, t.getTime());
    }

    @Test
    public void secondTimer3seconds() throws Exception{
        t.addTimer();
        t.startTimer(1);
        thread.sleep(3000);
        t.stopTimer(1);
        assertEquals(3, t.getTime(1));
    }

    @Test
    public void errorTimerOutOfBounds() throws Exception{
        t.startTimer(2);
        thread.sleep(500);
        t.stopTimer(2);
        assertEquals(-1, t.getTime(2));
    }
}
