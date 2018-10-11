package seedu.address.model;

import javafx.collections.ObservableList;
import seedu.address.model.contact.Contact;

/**
 * Unmodifiable view of an address book
 */
public interface ReadOnlyAddressBook {

    /**
     * Returns an unmodifiable view of the persons list.
     * This list will not contain any duplicate persons.
     */
    ObservableList<Contact> getPersonList();

}
