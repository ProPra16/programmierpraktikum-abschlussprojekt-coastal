package de.hhu.propra16.coastal.tddt;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static org.junit.Assert.assertEquals;

public class ExerciseTest {

    private Exercise exercise;

    @Before
    public void setUp() throws Exception {
        exercise = new Exercise("test");
    }

    @Test
    public void testBabysteps() throws Exception {
        assertEquals(false, exercise.isBabysteps());
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("H:m:s");
        LocalTime time = LocalTime.parse("00:" + "02:00", timeFormatter);
        exercise.addBabystepTime(time.getMinute()*60 + time.getSecond());
        assertEquals(true, exercise.isBabysteps());
        assertEquals(120, exercise.getBabystepTime());
    }

    @Test
    public void testDescription() throws Exception {
        final String TEST_DESCRIPTION = "testDescription";
        assertEquals(null, exercise.getDescription());
        exercise.addDescription(TEST_DESCRIPTION);
        assertEquals(TEST_DESCRIPTION, exercise.getDescription());
    }

    @Test
    public void testClassContent() throws Exception {
        final String TEST_CLASS_CONTENT = "testClassContent";
        assertEquals(null, exercise.getClassContent());
        exercise.addClassContent(TEST_CLASS_CONTENT);
        assertEquals(TEST_CLASS_CONTENT, exercise.getClassContent());
    }

    @Test
    public void testTestContent() throws Exception {
        final String TEST_TEST_CONTENT = "testTestContent";
        assertEquals(null, exercise.getTestContent());
        exercise.addTestContent(TEST_TEST_CONTENT);
        assertEquals(TEST_TEST_CONTENT, exercise.getTestContent());
    }

    @Test
    public void testClassName() throws Exception {
        final String TEST_CLASS_NAME = "testClassName";
        assertEquals(null, exercise.getClassName());
        exercise.addClassName(TEST_CLASS_NAME);
        assertEquals(TEST_CLASS_NAME, exercise.getClassName());
    }

    @Test
    public void testTestName() throws Exception {
        final String TEST_TEST_NAME = "testTestName";
        assertEquals(null, exercise.getTestName());
        exercise.addTestName(TEST_TEST_NAME);
        assertEquals(TEST_TEST_NAME, exercise.getTestName());
    }

    @Test
    public void testTracking() throws Exception {
        assertEquals(false, exercise.isTracking());
        exercise.setTracking(true);
        assertEquals(true, exercise.isTracking());
    }

    @Test
    public void testToString() throws Exception {
        assertEquals("test", exercise.toString());
    }
}