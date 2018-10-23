package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.HistoryCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.LoginCommand;
import seedu.address.logic.commands.MatchMakeCommand;
import seedu.address.logic.commands.RedoCommand;
import seedu.address.logic.commands.RegisterAccountCommand;
import seedu.address.logic.commands.SelectCommand;
import seedu.address.logic.commands.UndoCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses user input.
 */
public class AddressBookParser {

    /**
     * Used for initial separation of command word and args.
     */
    private static final Pattern BASIC_COMMAND_FORMAT = Pattern.compile("(?<commandWord>\\S+)(?<arguments>.*)");
    private static final Pattern SECONDARY_COMMAND_FORMAT =
            Pattern.compile("(?<commandWord>[a-zA-Z]+)(?<identifier>[#\\d]+)?[\\s]?(?<helperCommandWord>[a-zA-Z]+)?"
                    + "(?<arguments>.*)");
    private static final Pattern COMMAND_FORMAT =
            Pattern.compile("(?<commandWord>[a-zA-Z]+)#?(?<identifier>[\\d]+)?[\\s]*(?<helperCommandWord>(?!./)"
                    + "[a-zA-Z]+)?(?<arguments>.*)");

    /**
     * Parses user input into command for execution. This method is use before user has successfully logged in.
     * @param userInput full user input string.
     * @return the command based on the user input.
     * @throws ParseException if the user input does not conform the expected format.
     */
    public Command parseCommandBeforeLoggedIn(String userInput) throws ParseException {
        final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(userInput.trim());
        if (!matcher.matches()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }

        final String commandWord = matcher.group("commandWord");
        final String arguments = matcher.group("arguments");
        switch (commandWord) {

        case LoginCommand.COMMAND_WORD:
            return new LoginCommandParser().parse(arguments);

        case HelpCommand.COMMAND_WORD:
            return new HelpCommand();

        default:
            throw new ParseException(MESSAGE_UNKNOWN_COMMAND);
        }
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

        final String firstWord = matcher.group("commandWord");
        final String identifier = matcher.group("identifier");
        final String secondWord = matcher.group("helperCommandWord");
        final String arguments = matcher.group("arguments");

        final String commandWord;

        System.out.println(firstWord);
        System.out.println(secondWord);
        System.out.println(identifier);
        System.out.println(arguments);

        if (secondWord == null) {
            commandWord = firstWord;
        } else {
            commandWord =  String.format("%s %s", firstWord, secondWord);
        }

        switch (commandWord) {
        case "login":
            return new LoginCommandParser().parse(arguments);

        case "help":
            return new HelpCommand();

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

        case "client add":
            return new AddClientCommandParser().parse(arguments);

        case "client delete":
            return new DeleteCommandParser().parse(identifier);

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

//        switch (firstWord) {
//        //TODO: abstract out "client" and "serviceprovider" string here and tidy up code here
//        case "client":
//            switch (secondWord) {
//
//            case AddCommand.COMMAND_WORD:
//                return new AddClientCommandParser().parse(arguments);
//
//            case ListCommand.COMMAND_WORD:
//                return new ListClientCommandParser().parse(arguments);
//
//            case DeleteCommand.COMMAND_WORD:
//                return new DeleteCommandParser().parse(arguments);
//
//            default:
//                throw new ParseException(MESSAGE_UNKNOWN_COMMAND);
//            }
//
//        case "serviceprovider":
//            switch (secondWord) {
//
//            case AddCommand.COMMAND_WORD:
//                return new AddServiceProviderCommandParser().parse(arguments);
//
//            case ListCommand.COMMAND_WORD:
//                return new ListServiceProviderCommandParser().parse(arguments);
//
//            case DeleteCommand.COMMAND_WORD:
//                return new DeleteCommandParser().parse(arguments);
//
//            default:
//                throw new ParseException(MESSAGE_UNKNOWN_COMMAND);
//            }
//
//        case RegisterAccountCommand.COMMAND_WORD:
//            return new RegisterAccountCommandParser().parse(arguments);
//
//        case LoginCommand.COMMAND_WORD:
//            throw new ParseException("Already logged in.");
//
//        case EditCommand.COMMAND_WORD:
//            //TODO: edit command right now does edits to current list showing.
//            //TODO: syntax for command should be client#xx/serviceprovider#xx update ...
//            return new EditCommandParser().parse(arguments);
//
//        case SelectCommand.COMMAND_WORD:
//            return new SelectCommandParser().parse(arguments);
//
//        case MatchMakeCommand.COMMAND_WORD:
//            //TODO: make this a sub-command of client and serviceprovider
//            return new MatchMakeCommandParser().parse(arguments);
//
//        case ClearCommand.COMMAND_WORD:
//            return new ClearCommand();
//
//        case HistoryCommand.COMMAND_WORD:
//            return new HistoryCommand();
//
//        case ExitCommand.COMMAND_WORD:
//            return new ExitCommand();
//
//        case HelpCommand.COMMAND_WORD:
//            return new HelpCommand();
//
//        case UndoCommand.COMMAND_WORD:
//            return new UndoCommand();
//
//        case RedoCommand.COMMAND_WORD:
//            return new RedoCommand();
//
//        default:
//            throw new ParseException(MESSAGE_UNKNOWN_COMMAND);
//        }
    }
}
