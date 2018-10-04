package seedu.address.model.Contact;

import java.util.Set;

import seedu.address.model.tag.Tag;

/**
 * Contacts of the address book. The address book will store contacts within it, and use this interface to retrieve
 *     information on the contacts.
 */
public interface Contact {

    // Get the name of the Contact
    Name getName();

    // Get the phone number of the Contact
    Phone getPhone();

    // Get the email of the Contact
    Email getEmail();

    // Get the address of the Contact
    Address getAddress();

    // Get the set of tags of the contact
    Set<Tag> getTags();

}
