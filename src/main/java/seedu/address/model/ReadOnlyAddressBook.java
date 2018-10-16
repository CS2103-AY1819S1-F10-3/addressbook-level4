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
    ObservableList<Contact> getContactList();

    /**
     * Returns an unmodifiable view of the client list.
     * This list will not contain any duplicate clients.
     */
    ObservableList<Contact> getClientList();

    /**
     * Returns an unmodifiable view of the service provider list.
     * This list will not contain any duplicate service providers.
     */
    ObservableList<Contact> getServiceProviderList();

}
