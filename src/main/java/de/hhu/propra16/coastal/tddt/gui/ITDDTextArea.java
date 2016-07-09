package de.hhu.propra16.coastal.tddt.gui;

public interface ITDDTextArea {
    void setEditable(boolean editable);
    void setText(String text);
    String getText();

    boolean isEditable();
}
