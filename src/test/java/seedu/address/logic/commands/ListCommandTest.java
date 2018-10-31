package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.commons.core.Messages.MESSAGE_CLIENTS_LISTED_OVERVIEW;
import static seedu.address.commons.core.Messages.MESSAGE_LIST_ALL_CLIENTS;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalContacts.ALICE;
import static seedu.address.testutil.TypicalContacts.BENSON;
import static seedu.address.testutil.TypicalContacts.CARL;
import static seedu.address.testutil.TypicalContacts.DANIEL;
import static seedu.address.testutil.TypicalContacts.ELLE;
import static seedu.address.testutil.TypicalContacts.FIONA;
import static seedu.address.testutil.TypicalContacts.GEORGE;
import static seedu.address.testutil.TypicalContacts.getTypicalAddressBook;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import org.junit.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.model.ContactType;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.contact.ContactContainsKeywordsPredicate;
import seedu.address.model.contact.ContactInformation;
import seedu.address.testutil.ClientBuilder;

/**
 * Contains integration tests (interaction with the Model) for {@code ListCommand}.
 */
public class ListCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void equals() {
        ContactContainsKeywordsPredicate firstPredicate =
                new ContactContainsKeywordsPredicate(new ClientBuilder().withName("first").build());
        ContactContainsKeywordsPredicate secondPredicate =
                new ContactContainsKeywordsPredicate(new ClientBuilder().withName("second").build());

        ListCommand findFirstCommand = new ListCommand(firstPredicate, ContactType.CLIENT.getFilter());
        ListCommand findSecondCommand = new ListCommand(secondPredicate, ContactType.CLIENT.getFilter());

        // same object -> returns true
        assertTrue(findFirstCommand.equals(findFirstCommand));

        // same values -> returns true
        ListCommand findFirstCommandCopy = new ListCommand(firstPredicate, ContactType.CLIENT.getFilter());
        assertTrue(findFirstCommand.equals(findFirstCommandCopy));

        // different types -> returns false
        assertFalse(findFirstCommand.equals(1));

        // null -> returns false
        assertFalse(findFirstCommand.equals(null));

        // different client -> returns false
        assertFalse(findFirstCommand.equals(findSecondCommand));
    }

    @Test
    public void execute_zeroKeywords_everyPersonFound() {
        String expectedMessage = MESSAGE_LIST_ALL_CLIENTS;
        ContactContainsKeywordsPredicate predicate = preparePredicate();
        ListCommand command = new ListCommand(predicate, ContactType.CLIENT.getFilter());
        expectedModel.updateFilteredContactList(predicate.and(ContactType.CLIENT.getFilter()));
        assertCommandSuccess(command, model, commandHistory, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(ALICE, BENSON, CARL, DANIEL, ELLE, FIONA, GEORGE), model.getFilteredContactList());
    }

    @Test
    public void execute_multipleKeywords_zeroOrOnePersonFound() {

        /* Case: List with 3 person's name -> 0 persons found */
        String expectedMessage = String.format(MESSAGE_CLIENTS_LISTED_OVERVIEW, 0);
        ContactContainsKeywordsPredicate predicate = preparePredicate("Kurz Elle Kunz");
        ListCommand command = new ListCommand(predicate, ContactType.CLIENT.getFilter());
        expectedModel.updateFilteredContactList(predicate);
        assertCommandSuccess(command, model, commandHistory, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(), model.getFilteredContactList());

        /* Case: List with 1 person's name -> 1 persons found */
        expectedMessage = String.format(MESSAGE_CLIENTS_LISTED_OVERVIEW, 1);
        predicate = preparePredicate("Benson Meier");
        command = new ListCommand(predicate, ContactType.CLIENT.getFilter());
        expectedModel.updateFilteredContactList(predicate);
        assertCommandSuccess(command, model, commandHistory, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(BENSON), model.getFilteredContactList());
    }

    /**
     * Parses {@code userInput} into a {@code NameContainsKeywordsPredicate}.
     */
    private ContactContainsKeywordsPredicate preparePredicate(String userName) {
        return new ContactContainsKeywordsPredicate(new ContactInformation(Optional.of(userName), Optional.empty(),
                Optional.empty(), Optional.empty(), new ArrayList<>()));
    }

    private ContactContainsKeywordsPredicate preparePredicate() {
        return new ContactContainsKeywordsPredicate();
    }
}
