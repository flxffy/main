package seedu.address.ui;

import java.util.logging.Logger;

import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.medicine.Medicine;

/**
 * The Information Panel of the App.
 */
public class InformationPanel extends UiPart<Region> {

    private static final String FXML = "InformationPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(getClass());

    @FXML
    private StackPane informationPanel;

    public InformationPanel(ObservableValue<Medicine> selectedMedicine) {
        super(FXML);

        // Load medicine information page when selected medicine changes.
        selectedMedicine.addListener((observable, oldSelectMedicine, newSelectedMedicine) -> {
            emptyInformationPanel();
            if (newSelectedMedicine != null) {
                showSelectedInformation(newSelectedMedicine);
            }
        });
    }

    private void showSelectedInformation(Medicine medicine) {
        informationPanel.getChildren().add(new BatchTable(medicine).getRoot());
    }

    private void emptyInformationPanel() {
        if (informationPanel.getChildren() != null) {
            informationPanel.getChildren().clear();
        }
    }

}
