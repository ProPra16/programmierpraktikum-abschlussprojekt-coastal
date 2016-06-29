package de.hhu.propra16.coastal.tddt;


import javafx.scene.control.TextArea;

/**
 * Created by student on 29/06/16.
 */
public class TDDController {

    public static void toEditor(TextArea taeditor, TextArea tatest) {
        taeditor.setEditable(true);
        tatest.setEditable(false);
    }

    public static void toTestEditor(TextArea taeditor, TextArea tatest) {
        taeditor.setEditable(false);
        tatest.setEditable(true);
    }

}
