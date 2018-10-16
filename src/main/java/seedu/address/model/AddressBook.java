package seedu.address.model;

import static java.util.Objects.requireNonNull;

import java.util.List;

import javafx.collections.ObservableList;
import seedu.address.model.contact.Contact;
import seedu.address.model.contact.UniqueContactList;
import seedu.address.model.person.Person;
import seedu.address.model.serviceprovider.ServiceProvider;

/**
 * Wraps all data at the address-book level
 * Duplicates are not allowed (by .isSameContact comparison)
 */
public class AddressBook implements ReadOnlyAddressBook {

    private final UniqueContactList<Contact> contacts;
    private final UniqueContactList<Contact> clients;
    private final UniqueContactList<Contact> serviceProviders;

    /*
     * The 'unusual' code block below is an non-static initialization block, sometimes used to avoid duplication
     * between constructors. See https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
     *
     * Note that non-static init blocks are not recommended to use. There are other ways to avoid duplication
     *   among constructors.
     */
    {
        clients = new UniqueContactList<>();
        contacts = clients;
        serviceProviders = new UniqueContactList<>();
    }

    public AddressBook() {}

    /**
     * Creates an AddressBook using the Contacts in the {@code toBeCopied}
     */
    public AddressBook(ReadOnlyAddressBook toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    //// list overwrite operations

    /**
     * Replaces the contents of the contact list with {@code contacts}.
     * {@code contacts} must not contain duplicate contacts.
     */
    private void setContacts(List<Contact> clients, List<Contact> serviceProviders) {
        this.clients.setContacts(clients);
        this.serviceProviders.setContacts(serviceProviders);
    }

    /**
     * Resets the existing data of this {@code AddressBook} with {@code newData}.
     */
    public void resetData(ReadOnlyAddressBook newData) {
        requireNonNull(newData);

        setContacts(newData.getClientList(), newData.getServiceProviderList());
    }

    //// contact-level operations

    /**
     * Returns true if a contact with the same identity as {@code client} exists in the address book.
     * @param client The client to compare to
     * @return true if the address book contains the client, false otherwise
     */
    public boolean hasClient(Person client) {
        requireNonNull(client);
        return clients.contains(client);
    }

    /**
     * Returns true if a contact with the same identity as {@code serviceProvider} exists in the address book.
     * @param serviceProvider The service provider to compare to
     * @return true if the address book contains the client, false otherwise
     */
    public boolean hasServiceProvider(ServiceProvider serviceProvider) {
        requireNonNull(serviceProvider);
        return serviceProviders.contains(serviceProvider);
    }

    /**
     * Adds a service provider to the address book.
     * The service provider must not already exist in the address book.
     * @param serviceProvider The service provider to be added to the address book
     */
    public void addServiceProvider(ServiceProvider serviceProvider) {
        serviceProviders.add(serviceProvider);
    }

    /**
     * Adds a client to the address book.
     * The client must not already exist in the address book.
     * @param client The client to be added to the address book
     */
    public void addClient(Person client) {
        clients.add(client);
    }

    /**
     * Replaces the given contact {@code target} in the list with {@code editedContact}.
     * {@code target} must exist in the address book.
     * The contact identity of {@code editedContact} must not be the same as another existing contact in the address
     * book.
     */
    public void updateContact(Contact target, Contact editedContact) {
        requireNonNull(editedContact);

        if (target instanceof Person) {
            clients.setContact(target, editedContact);
        } else {
            // target is instance of ServiceProvider
            serviceProviders.setContact(target, editedContact);
        }
    }

    /**
     * Removes the client {@code key} from this AddressBook.
     * {@code key} must exist in the address book.
     * @param key The client to be removed
     */
    public void removeClient(Person key) {
        clients.remove(key);
    }

    /**
     * Removes the service provider {@code key} from this AddressBook.
     * {@code key} must exist in the address book.
     * @param key The service provider to be removed
     */
    public void removeServiceProvider(ServiceProvider key) {
        serviceProviders.remove(key);
    }

    //// util methods

    @Override
    public String toString() {
        //TODO: NOTE!!!!
        return contacts.asUnmodifiableObservableList().size() + " contacts";
        // TODO: refine later
    }

    @Override
    public ObservableList<Contact> getContactList() {
        //TODO: NOTE!!!!
        return contacts.asUnmodifiableObservableList();
    }

    @Override
    public ObservableList<Contact> getClientList() {
        return clients.asUnmodifiableObservableList();
    }

    @Override
    public ObservableList<Contact> getServiceProviderList() {
        return serviceProviders.asUnmodifiableObservableList();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AddressBook // instanceof handles nulls
                && contacts.equals(((AddressBook) other).contacts)
                && clients.equals(((AddressBook) other).clients)
                && serviceProviders.equals(((AddressBook) other).serviceProviders));
    }

    @Override
    public int hashCode() {
        // TODO: a better hashcode?
        return clients.hashCode() + serviceProviders.hashCode();
    }

}
