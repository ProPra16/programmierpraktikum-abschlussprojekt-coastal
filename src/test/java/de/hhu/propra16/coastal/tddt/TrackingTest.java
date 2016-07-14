package de.hhu.propra16.coastal.tddt;

import de.hhu.propra16.coastal.tddt.tracking.Tracking;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;

@Ignore
public class TrackingTest {

    private Tracking t;
    private Thread thread;


    @Before
    public void setUp() throws Exception {
        t = new Tracking();
        thread = new Thread();

    }

    @Test
    public void firsTimer() throws Exception{
        t.startTimer(0);
        thread.sleep(1000);
        t.stopTimer(0);
        assertEquals(1, t.getTime(0));
    }

    @Test
    public void firstTimer2Seconds() throws Exception{
        t.startTimer(0);
        thread.sleep(2000);
        t.stopTimer(0);
        assertEquals(2, t.getTime(0));
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
