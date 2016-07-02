package de.hhu.propra16.coastal.tddt;

import javafx.scene.control.TextArea;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by student on 02/07/16.
 */
@Deprecated
public class TDDControllerTest {

    private TextArea editor;
    private TextArea testeditor;

    @Before
    public void createTextAreas() {
        TextArea editor = new TextArea();
        TextArea testeditor = new TextArea();
    }
    @Test
    public void testSwitchToEditor() {
        TDDController.toEditor(editor, testeditor);
        assertEquals(editor.isEditable(), true);
        assertEquals(testeditor.isEditable(), false);

    }

    @Test
    public void testSwitchToTestEditor() {
        TDDController.toEditor(editor, testeditor);
        assertEquals(editor.isEditable(), false);
        assertEquals(testeditor.isEditable(), true);

    }

}
