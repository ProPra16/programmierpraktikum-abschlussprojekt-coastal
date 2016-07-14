package de.hhu.propra16.coastal.tddt.tracking;

import de.hhu.propra16.coastal.tddt.compiler.ErrorObject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.ComboBoxListCell;

import java.net.URL;
import java.util.List;


public class UserFailsAnalyzer extends SplitPane {

    @FXML
    private TextArea taerror;

    @FXML
    private ListView<ErrorObject> lverrors;

    private ObservableList<ErrorObject> errors = FXCollections.observableArrayList();

    public UserFailsAnalyzer(FXMLLoader loader) {
        loader.setController(this);
        loader.setRoot(this);
        try {
            loader.load();

        } catch (Exception e) {
            System.out.println("ERROR");
        }
        lverrors.setOnMouseClicked(e -> {
            if(lverrors.getSelectionModel().getSelectedItem() != null) {
                taerror.setText(lverrors.getSelectionModel().getSelectedItem().getMessage());
            }
        });
    }

    public void refresh() {
        lverrors.setItems(errors);
    }


    public void loadErrors(List<ErrorObject> userErrors) {
        errors.clear();
        errors.addAll(userErrors);
        refresh();
    }


}
