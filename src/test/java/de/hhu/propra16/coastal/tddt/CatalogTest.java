package de.hhu.propra16.coastal.tddt;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class CatalogTest {

    @Test
    public void addExercise1() throws Exception {
        Catalog catalog = new Catalog("testName");
        Exercise mockercise = mock(Exercise.class);
        catalog.addExercise(mockercise);
        assertEquals(mockercise, catalog.getExercises().get(0));
    }

    @Test
    public void loadExercise() throws Exception {
        String testClassContent = "testClassContent";
        String testTestContent = "testTestContent";
        String testDescription = "testDescription";
        Catalog catalog = new Catalog("testName");
        ITDDTextArea mockAreaClass = mock(ITDDTextArea.class);
        ITDDTextArea mockAreaTest = mock(ITDDTextArea.class);
        ITDDLabel mockLabel = mock(ITDDLabel.class);
        Exercise mockercise = mock(Exercise.class);

        when(mockercise.getClassContent()).thenReturn(testClassContent);
        when(mockercise.getTestContent()).thenReturn(testTestContent);
        when(mockercise.getDescription()).thenReturn(testDescription);

        catalog.addExercise(mockercise);
        catalog.loadExercise(mockAreaClass, mockAreaTest, mockLabel, mockercise);

        verify(mockAreaClass).setText(testClassContent);
        verify(mockAreaTest).setText(testTestContent);
        verify(mockLabel).setText(testDescription);
    }

    @Test
    public void loadInListView() throws Exception {
        Catalog catalog = new Catalog("testName");
        ObservableList<Exercise> list = FXCollections.observableArrayList();
        ITDDListView mockListView = mock(ITDDListView.class);
        when(mockListView.getItems()).thenReturn(list);
        Exercise mockercise = mock(Exercise.class);
        catalog.addExercise(mockercise);
        catalog.loadInListView(mockListView);
        assertEquals(mockercise, catalog.getExercises().get(0));
        assertEquals(mockercise, mockListView.getItems().get(0));
    }
}