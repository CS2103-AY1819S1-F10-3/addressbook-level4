package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.HistoryCommand;
import seedu.address.logic.commands.LoginCommand;
import seedu.address.logic.commands.MatchMakeCommand;
import seedu.address.logic.commands.RedoCommand;
import seedu.address.logic.commands.UndoCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses user input.
 */
public class AddressBookParser {

    /**
     * Used for initial separation of command word and args.
     */
    private static final Pattern COMMAND_FORMAT =
            Pattern.compile("(?<firstWord>[a-zA-Z]+)(?<identifier>#[\\d\\w-]+)?(?<secondWord>[\\s]+(?!./)"
                    + "[a-zA-Z]+)?(?<arguments>.*)");

    /**
     * Parses user input into command for execution. This method is use before user has successfully logged in.
     * @param userInput full user input string.
     * @return the command based on the user input.
     * @throws ParseException if the user input does not conform the expected format.
     */
    public Command parseCommandBeforeLoggedIn(String userInput) throws ParseException {
        final Matcher matcher = COMMAND_FORMAT.matcher(userInput.trim());
        if (!matcher.matches()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }

        final String commandWord = getCommandWord(matcher.group("firstWord"), matcher.group("secondWord"));
        final String arguments = matcher.group("arguments");
        switch (commandWord) {

        case LoginCommand.COMMAND_WORD:
            return new LoginCommandParser().parse(arguments);

        case HelpCommand.COMMAND_WORD:
            return new HelpCommand();

        case ExitCommand.COMMAND_WORD:
            return new ExitCommand();

        default:
            throw new ParseException(MESSAGE_UNKNOWN_COMMAND);
        }
    }

    private String getCommandWord(String firstWord, String secondWord) {
        String commandWord;

        if (secondWord == null) {
            commandWord = firstWord;
        } else {
            commandWord =  String.format("%s %s", firstWord, secondWord.trim());
        }

        return commandWord;

    }

    /**
     * Parses user input into command for execution. This method will only be called after
     * user has successfully log in.
     *
     * @param userInput full user input string
     * @return the command based on the user input
     * @throws ParseException if the user input does not conform the expected format
     */
    public Command parseCommand(String userInput) throws ParseException {
        final Matcher matcher = COMMAND_FORMAT.matcher(userInput.trim());
        if (!matcher.matches()) {
            //TODO: update HelpCommand.MESSAGE_USAGE
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }

        final String firstWord = matcher.group("firstWord");
        final String identifier = matcher.group("identifier");
        final String secondWord = matcher.group("secondWord");
        final String arguments = matcher.group("arguments");

        final String commandWord = getCommandWord(firstWord, secondWord);

        //TODO: do away with new String cases
        switch (commandWord) {

        case "registeraccount":
            return new RegisterAccountCommandParser().parse(arguments);

        case "select":
            return new SelectCommandParser().parse(arguments);

        case MatchMakeCommand.COMMAND_WORD:
            //TODO: make this a sub-command of client and serviceprovider
            return new MatchMakeCommandParser().parse(arguments);

        case ClearCommand.COMMAND_WORD:
            return new ClearCommand();

        case HistoryCommand.COMMAND_WORD:
            return new HistoryCommand();

        case ExitCommand.COMMAND_WORD:
            return new ExitCommand();

        case UndoCommand.COMMAND_WORD:
            return new UndoCommand();

        case RedoCommand.COMMAND_WORD:
            return new RedoCommand();
            
        case HelpCommand.COMMAND_WORD:
            return new HelpCommand();

        case "client add":
            return new AddClientCommandParser().parse(arguments);

        case "client delete":
            return new DeleteCommandParser().parse(identifier.substring(1));

        case "client list":
            return new ListClientCommandParser().parse(arguments);

        case "serviceprovider add":
            return new AddServiceProviderCommandParser().parse(arguments);

        case "serviceprovider delete":
            return new DeleteCommandParser().parse(identifier);

        case "serviceprovider list":
            return new ListServiceProviderCommandParser().parse(arguments);
        default:
            throw new ParseException(MESSAGE_UNKNOWN_COMMAND);
        }
    }
}
