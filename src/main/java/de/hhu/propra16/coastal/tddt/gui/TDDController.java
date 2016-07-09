package de.hhu.propra16.coastal.tddt.gui;


/**
 * Created by student on 29/06/16.
 */
public class TDDController {

    public static void toEditor(ITDDTextArea taeditor, ITDDTextArea tatest) {
        taeditor.setEditable(true);
        tatest.setEditable(false);
    }

    public static void toTestEditor(ITDDTextArea taeditor, ITDDTextArea tatest) {
        taeditor.setEditable(false);
        tatest.setEditable(true);
    }

}
