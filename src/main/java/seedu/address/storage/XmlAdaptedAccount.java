package seedu.address.storage;

import java.util.Objects;

import javax.xml.bind.annotation.XmlElement;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.account.Account;
import seedu.address.model.account.Role;

/**
 * JAXB-friendly version of the Person.
 */
public class XmlAdaptedAccount {
    @XmlElement(required = true)
    private String username;
    @XmlElement(required = true)
    private String password;
    @XmlElement(required = true)
    private Role role;

    /**
     * Constructs an XmlAdaptedAccount.
     * This is the no-arg constructor that is required by JAXB.
     */
    public XmlAdaptedAccount() {}

    /**
     * Constructs an {@code XmlAdaptedAccount} with the given account details.
     */
    public XmlAdaptedAccount(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    /**
     * Converts a given Person into this class for JAXB use.
     *
     * @param source future changes to this will not affect the created XmlAdaptedPerson
     */
    public XmlAdaptedAccount(Account source) {
        username = source.getUserName();
        password = source.getPassword();
        role = source.getRole();
    }

    /**
     * Converts this jaxb-friendly adapted account object into the model's Account object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted person
     */
    public Account toModelType() throws IllegalValueException {

        if (username == null) {
            throw new IllegalValueException("Username is null.");
        }
        if (password == null) {
            throw new IllegalValueException("Password is null.");
        }

        if (role == null) {
            throw new IllegalValueException("Role is null.");
        }

        return new Account(username, password, role);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof XmlAdaptedPerson)) {
            return false;
        }

        XmlAdaptedAccount otherAccount = (XmlAdaptedAccount) other;
        return Objects.equals(username, otherAccount.username)
                && Objects.equals(password, otherAccount.password)
                && Objects.equals(role, otherAccount.role);
    }
}
