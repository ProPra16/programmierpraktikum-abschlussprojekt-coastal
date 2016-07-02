package de.hhu.propra16.coastal.tddt;

import javafx.scene.control.ListView;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CatalogTest {
    @Test
    public void addExercise1() throws Exception {
        Catalog catalog = new Catalog();
        Exercise mockercise = mock(Exercise.class);
        catalog.addExercise(mockercise);
        assertEquals(mockercise, catalog.getExercises().get(0));
    }

    @Test
    public void loadExercises() throws Exception {

    }

    @Test
    public void loadExercise1() throws Exception {

        Catalog catalog = new Catalog();
        ITDDTextArea mockArea = mock(ITDDTextArea.class);
        Exercise mockercise = mock(Exercise.class);
        when(mockercise.getClassContent()).thenReturn("testClassContent");
        catalog.addExercise(mockercise);
        try {
            catalog.loadExercise(mockArea, null, null, mockercise);
        }
        catch (NullPointerException ignored) {}
        verify(mockArea).setText("testClassContent");
    }

    @Ignore
    @Test
    public void loadInListView() throws Exception {
        Catalog catalog = new Catalog();
        ListView<Exercise> mockListView = mock(ListView.class);
        Exercise mockercise = mock(Exercise.class);
        catalog.addExercise(mockercise);
        catalog.loadInListView(mockListView);
        assertEquals(mockercise, mockListView.getItems().get(0));
    }

    @Test
    public void getExercises() throws Exception {

    }

    @Test
    public void addExercise() throws Exception {

    }

    @Test
    public void loadExercise() throws Exception {

    }

}