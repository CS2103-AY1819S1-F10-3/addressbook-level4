package seedu.address.model;

import java.util.function.Predicate;

import seedu.address.model.client.Client;
import seedu.address.model.contact.Contact;
import seedu.address.model.serviceprovider.ServiceProvider;

public enum ContactType {
    CLIENT {
        @Override
        public Predicate<Contact> getFilter() {
            return contact -> contact instanceof Client;
        }
    },
    SERVICE_PROVIDER {
        @Override
        public Predicate<Contact> getFilter() {
            return contact -> contact instanceof ServiceProvider;
        }
    };

    public abstract Predicate<Contact> getFilter();
}
