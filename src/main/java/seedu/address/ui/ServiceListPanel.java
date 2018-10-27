package seedu.address.ui;

import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.ui.JumpToListRequestEvent;
import seedu.address.commons.events.ui.PersonPanelSelectionChangedEvent;
import seedu.address.model.contact.Contact;

/**
 * Panel containing the list of persons.
 */
public class ServiceListPanel extends UiPart<Region> {
    private static final String FXML = "ServiceListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(ServiceListPanel.class);

    @FXML
    private ListView<Contact> serviceListView;

    public ServiceListPanel(ObservableList<Contact> contactList) {
        super(FXML);
        setConnections(contactList);
        registerAsAnEventHandler(this);
    }

    private void setConnections(ObservableList<Contact> contactList) {
        serviceListView.setItems(contactList);
        serviceListView.setCellFactory(listView -> new ServiceListViewCell());
        setEventHandlerForSelectionChangeEvent();
    }

    private void setEventHandlerForSelectionChangeEvent() {
        serviceListView.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        logger.fine("Selection in client list panel changed to : '" + newValue + "'");
                        raise(new PersonPanelSelectionChangedEvent(newValue));
                    }
                });
    }

    /**
     * Scrolls to the {@code PersonCard} at the {@code index} and selects it.
     */
    private void scrollTo(int index) {
        Platform.runLater(() -> {
            serviceListView.scrollTo(index);
            serviceListView.getSelectionModel().clearAndSelect(index);
        });
    }

    @Subscribe
    private void handleJumpToListRequestEvent(JumpToListRequestEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        scrollTo(event.targetIndex);
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code Client} using a {@code PersonCard}.
     */
    class ServiceListViewCell extends ListCell<Contact> {
        @Override
        protected void updateItem(Contact contact, boolean empty) {
            super.updateItem(contact, empty);

            if (empty || contact == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new PersonCard(contact, getIndex() + 1).getRoot());
            }
        }
    }

}
