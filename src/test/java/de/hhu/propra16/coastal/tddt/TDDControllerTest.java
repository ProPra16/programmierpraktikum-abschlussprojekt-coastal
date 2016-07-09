package de.hhu.propra16.coastal.tddt;

import de.hhu.propra16.coastal.tddt.gui.ITDDTextArea;
import de.hhu.propra16.coastal.tddt.gui.TDDController;
import javafx.scene.control.TextArea;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by student on 02/07/16.
 */
@Deprecated
public class TDDControllerTest {

    private ITDDTextArea editor;
    private ITDDTextArea testeditor;

    @Ignore
    @Before
    public void createTextAreas() {
        TextArea editor = new TextArea();
        TextArea testeditor = new TextArea();
    }

    @Ignore
    @Test
    public void testSwitchToEditor() {
        TDDController.toEditor(editor, testeditor);
        assertEquals(editor.isEditable(), true);
        assertEquals(testeditor.isEditable(), false);

    }

    @Ignore
    @Test
    public void testSwitchToTestEditor() {
        TDDController.toEditor(editor, testeditor);
        assertEquals(editor.isEditable(), false);
        assertEquals(testeditor.isEditable(), true);

    }

}
