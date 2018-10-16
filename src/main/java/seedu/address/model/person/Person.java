package seedu.address.model.person;

import java.util.Set;

import seedu.address.model.AddressBook;
import seedu.address.model.contact.Address;
import seedu.address.model.contact.Contact;
import seedu.address.model.contact.Email;
import seedu.address.model.contact.Name;
import seedu.address.model.contact.Phone;
import seedu.address.model.tag.Tag;

/**
 * Represents a Person in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Person extends Contact {

    /**
     * Every field must be present and not null.
     */
    public Person(Name name, Phone phone, Email email, Address address, Set<Tag> tags) {
        super(name, phone, email, address, tags);
    }

    @Override
    public boolean presentIn(AddressBook addressBook) {
        return addressBook.hasClient(this);
    }

    @Override
    public void addTo(AddressBook addressBook) {
        addressBook.addClient(this);
    }

    @Override
    public void removeFrom(AddressBook addressBook) {
        addressBook.removeClient(this);
    }
}
