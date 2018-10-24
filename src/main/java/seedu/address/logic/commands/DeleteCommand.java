package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.commands.exceptions.LackOfPrivilegeException;
import seedu.address.model.ContactType;
import seedu.address.model.Model;
import seedu.address.model.contact.Contact;

/**
 * Deletes a client identified using it's displayed index from the address book.
 */
public class DeleteCommand extends Command {

    public static final String COMMAND_WORD = "delete";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the client identified by the index number used in the displayed client list.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_DELETE_PERSON_SUCCESS = "Deleted Client: %1$s";

    private final Index targetIndex;
    private final ContactType contactType;

    public DeleteCommand(Index targetIndex, ContactType contactType) {
        this.targetIndex = targetIndex;
        this.contactType = contactType;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException,
            LackOfPrivilegeException {
        requireNonNull(model);

        if (!model.getUserAccount().hasDeletePrivilege()) {
            throw new LackOfPrivilegeException(COMMAND_WORD);
        }

        model.updateFilteredContactList(contactType.getFilter()
                .and(contact -> contact.getID() == targetIndex.getOneBased()));

        List<Contact> filteredList = model.getFilteredContactList();

        if (filteredList.size() == 0) {
            // filtered list size is 0, meaning there is no such contact
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        // filtered list size is 1 (unique ID for client/serviceprovider)
        Contact contactToDelete = filteredList.get(0);
        model.deleteContact(contactToDelete);
        model.updateFilteredContactList(contactType.getFilter());
        model.commitAddressBook();
        return new CommandResult(String.format(MESSAGE_DELETE_PERSON_SUCCESS, contactToDelete));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof DeleteCommand // instanceof handles nulls
                && targetIndex.equals(((DeleteCommand) other).targetIndex)); // state check
    }
}
