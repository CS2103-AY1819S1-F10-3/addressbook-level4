package seedu.address.model.contact;

import java.util.Set;

import seedu.address.model.tag.Tag;

/**
 * An interface to be implemented by the client and the service provider. The Addressbook will use this interface
 *     to retrieve details of the contact.
 */
public interface Contact {

    // Get the name of the contact
    Name getName();

    // Get the address of the contact
    Address getAddress();

    // Get the email of the contact
    Email getEmail();

    // get the phone number of the contact
    Phone getPhone();

    // Get the tag set
    Set<Tag> getTags();

    /**
     * Returns true if both persons of the same name have at least one other identity field that is the same.
     * This defines a weaker notion of equality between two persons.
     */
    default boolean isSameContact(Contact otherContact) {
        if (otherContact == this) {
            return true;
        }

        return otherContact != null
                && otherContact.getName().equals(getName())
                && (otherContact.getPhone().equals(getPhone()) || otherContact.getEmail().equals(getEmail()));
    }
}
