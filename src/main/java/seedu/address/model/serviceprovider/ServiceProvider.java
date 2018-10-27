package seedu.address.model.serviceprovider;

import java.util.Set;

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
    public static int SPID = 1;

    private final int ID;

    /**
     * Every field must be present and not null.
     *
     */
    public ServiceProvider(Name name, Phone phone, Email email, Address address, Set<Tag> tags) {
        super(name, phone, email, address, tags);
        this.ID = SPID ++;
    }

    public int getID() {
        return ID;
    }

    /**
     * Returns true if both service providers have the same identity and data fields.
     * This defines a stronger notion of equality between two service providers.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof ServiceProvider)) {
            return false;
        }

        ServiceProvider otherServiceProvider = (ServiceProvider) other;
        return otherServiceProvider.getName().equals(getName())
                && otherServiceProvider.getPhone().equals(getPhone())
                && otherServiceProvider.getEmail().equals(getEmail())
                && otherServiceProvider.getAddress().equals(getAddress())
                && otherServiceProvider.getTags().equals(getTags());
    }
}
