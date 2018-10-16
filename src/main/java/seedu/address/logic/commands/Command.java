package seedu.address.logic.commands;

import java.util.List;
import java.util.function.Function;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.commands.exceptions.LackOfPrivilegeException;
import seedu.address.model.Model;
import seedu.address.model.contact.Contact;

/**
 * Represents a command with hidden internal logic and the ability to be executed.
 */
public abstract class Command {

    /**
     * Executes the command and returns the result message.
     *
     * @param model {@code Model} which the command should operate on.
     * @param history {@code CommandHistory} which the command should operate on.
     * @return feedback message of the operation result for display
     * @throws CommandException If an error occurs during command execution.
     * @throws LackOfPrivilegeException If user account does not possess the required privilege
     * to execute that command.
     */
    public abstract CommandResult execute(Model model, CommandHistory history) throws CommandException,
            LackOfPrivilegeException;

    /**
     * Enum of the types of Contacts, namely the client and the service provider. In this enum it encapsulates the logic
     * for EditCommand to retrieve the correct list to make edits in.
     */
    public enum ContactType {
        CLIENT {
            public Function<Model, List<Contact>> getCorrectListFunction() {
                return Model::getFilteredContactList;
            }
        },
        SERVICE_PROVIDER {
            public Function<Model, List<Contact>> getCorrectListFunction() {
                return Model::getFilteredContactList;
            }
        };

        /**
         * This method returns a function that encapsulates the correct logic for obtaining the contact list for the
         * correct type.
         * @return A function that takes in a Model as a parameter and returns the correct list based on the
         *     ContactType enum
         */
        public abstract Function<Model, List<Contact>> getCorrectListFunction();
    }
}
