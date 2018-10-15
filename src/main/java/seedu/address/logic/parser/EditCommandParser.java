package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EditCommand.EditContactDescriptor;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Model;
import seedu.address.model.contact.Contact;
import seedu.address.model.tag.Tag;

/**
 * Parses input arguments and creates a new EditCommand object
 */
public abstract class EditCommandParser implements Parser<EditCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the EditCommand
     * and returns an EditCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public abstract EditCommand parse(String args) throws ParseException;

    /**
     * Gets the {@code Index} given the {@code ArgumentMultimap}
     * @param argMultimap The ArgumentMultimap object to get {@code Index} from
     * @return The {@code Index} from parsing the ArgumentMultimap object
     * @throws ParseException if the user input does not conform the expected format
     */
    protected Index getIndex(ArgumentMultimap argMultimap) throws ParseException {
        Index index;

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE), pe);
        }
        return index;
    }

    /**
     * Gets the {@code EditContactDescriptor} given the {@code ArgumentMultimap}
     * @param argMultimap The ArgumentMultimap object to create the {@code EditContactDescriptor}
     * @return The {@code EditContactDescriptor} from parsing the ArgumentMultimap object
     * @throws ParseException if the user input does not conform the expected format
     */
    protected EditContactDescriptor getEditContactDescriptor(ArgumentMultimap argMultimap) throws ParseException {
        EditContactDescriptor editContactDescriptor = new EditContactDescriptor();
        if (argMultimap.getValue(PREFIX_NAME).isPresent()) {
            editContactDescriptor.setName(ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get()));
        }
        if (argMultimap.getValue(PREFIX_PHONE).isPresent()) {
            editContactDescriptor.setPhone(ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE).get()));
        }
        if (argMultimap.getValue(PREFIX_EMAIL).isPresent()) {
            editContactDescriptor.setEmail(ParserUtil.parseEmail(argMultimap.getValue(PREFIX_EMAIL).get()));
        }
        if (argMultimap.getValue(PREFIX_ADDRESS).isPresent()) {
            editContactDescriptor.setAddress(ParserUtil.parseAddress(argMultimap.getValue(PREFIX_ADDRESS).get()));
        }
        parseTagsForEdit(argMultimap.getAllValues(PREFIX_TAG)).ifPresent(editContactDescriptor::setTags);

        if (!editContactDescriptor.isAnyFieldEdited()) {
            throw new ParseException(EditCommand.MESSAGE_NOT_EDITED);
        }
        return editContactDescriptor;
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>} if {@code tags} is non-empty.
     * If {@code tags} contain only one element which is an empty string, it will be parsed into a
     * {@code Set<Tag>} containing zero tags.
     */
    private Optional<Set<Tag>> parseTagsForEdit(Collection<String> tags) throws ParseException {
        assert tags != null;

        if (tags.isEmpty()) {
            return Optional.empty();
        }
        Collection<String> tagSet = tags.size() == 1 && tags.contains("") ? Collections.emptySet() : tags;
        return Optional.of(ParserUtil.parseTags(tagSet));
    }

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
