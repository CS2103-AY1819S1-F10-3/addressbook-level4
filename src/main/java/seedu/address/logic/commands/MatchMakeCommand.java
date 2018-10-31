package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.core.Messages;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.client.Client;
import seedu.address.model.contact.Contact;
import seedu.address.model.serviceprovider.ServiceProvider;

/**
 * Finds and lists all persons in address book whose name contains any of the argument keywords.
 * Keyword matching is case insensitive.
 */
public class MatchMakeCommand extends Command {

    public static final String COMMAND_WORD = "matchmake";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all persons whose names contain any of "
            + "the specified keywords (case-insensitive) and displays them as a list with index numbers.\n"
            + "Parameters: KEYWORD [MORE_KEYWORDS]...\n"
            + "Example: " + COMMAND_WORD + " alice bob charlie";

    private final String contactType;
    private final String contactId;

    public MatchMakeCommand(String contactType, String contactId) {
        this.contactType = contactType;
        this.contactId = contactId;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);

        // Update filtered contact list to contain all contacts and get the list
        model.updateFilteredContactList(x -> true);
        model.getFilteredContactList();

        // Find the contact for which we are going to match make
        int contactId = Integer.parseInt(this.contactId);
        Contact contact;
        try {
            contact = model.getAddressBook().getContactList().get(contactId);
        } catch (IndexOutOfBoundsException exception) {
            throw new CommandException("Invalid entity id: " + contactId);
        }

        if (contact instanceof Client) {
            model.updateFilteredContactList(c -> c instanceof ServiceProvider);
            if (!contactType.equals("client")) {
                throw new CommandException("Invalid service provider id: " + contactId);
            }
        } else if (contact instanceof ServiceProvider) {
            model.updateFilteredContactList(c -> c instanceof Client);
            if (!contactType.equals("serviceprovider")) {
                throw new CommandException("Invalid client id: " + contactId);
            }
        } else {
            // We should never arrive here.
            throw new CommandException("Unknown entity (neither client nor service provider found in database.");
        }

        return new CommandResult(
                String.format(Messages.MESSAGE_CLIENTS_LISTED_OVERVIEW, model.getFilteredContactList().size()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof MatchMakeCommand // instanceof handles nulls
                && contactType.equals(((MatchMakeCommand) other).contactType)
                && contactId.equals(((MatchMakeCommand) other).contactId)); // state check
    }
}
