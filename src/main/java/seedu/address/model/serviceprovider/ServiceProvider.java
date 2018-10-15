package seedu.address.model.serviceprovider;

import java.util.Collections;
import java.util.Set;

import seedu.address.model.AddressBook;
import seedu.address.model.contact.Address;
import seedu.address.model.contact.Contact;
import seedu.address.model.contact.Email;
import seedu.address.model.contact.Name;
import seedu.address.model.contact.Phone;
import seedu.address.model.tag.Tag;

/**
 * Represents a ServiceProvider in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class ServiceProvider extends Contact {

    /**
     * Every field must be present and not null.
     */
    public ServiceProvider(Name name, Phone phone, Email email, Address address, Set<Tag> tags) {
        super(name, phone, email, address, tags);
    }

    @Override
    public boolean presentIn(AddressBook addressBook) {
        return addressBook.hasServiceProvider(this);
    }

    @Override
    public void addTo(AddressBook addressBook) {
        addressBook.addServiceProvider(this);
    }

    @Override
    public void removeFrom(AddressBook addressBook) {
        addressBook.removeServiceProvider(this);
    }
}
