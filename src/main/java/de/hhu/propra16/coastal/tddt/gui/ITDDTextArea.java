package de.hhu.propra16.coastal.tddt.gui;

public interface ITDDTextArea {
    void setEditable(boolean editable);
    void setText(String text);

    String getText();
    /*
    public static String getText(){
        return "";
    }*/

    boolean isEditable();
}
