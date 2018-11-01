package systemtests;

import static org.junit.Assert.assertTrue;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX;
import static seedu.address.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.logic.commands.SelectCommand.MESSAGE_SELECT_CONTACT_SUCCESS;
import static seedu.address.testutil.TestUtil.getLastIndex;
import static seedu.address.testutil.TypicalContacts.KEYWORD_MATCHING_MEIER;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.Ignore;
import org.junit.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.RedoCommand;
import seedu.address.logic.commands.SelectCommand;
import seedu.address.logic.commands.UndoCommand;
import seedu.address.model.ContactType;
import seedu.address.model.Model;

// import static seedu.address.testutil.TestUtil.getMidIndex;

public class SelectCommandSystemTest extends AddressBookSystemTest {
    @Test
    public void select() {
        /* ------------------------ Perform select operations on the shown unfiltered list -------------------------- */

        /* Case: select the first card in the client list, command with leading spaces and trailing spaces
         * -> selected
         */
        String command = "          " + String.format(SelectCommand.COMMAND_WORD_GENERAL,
                ContactType.CLIENT, "#" + INDEX_FIRST_PERSON.getOneBased()) + "        ";
        assertCommandSuccess(command, INDEX_FIRST_PERSON);

        /* Case: select the last card in the client list -> selected */
        Index personCount = getLastIndex(getModel());
        command = String.format(SelectCommand.COMMAND_WORD_GENERAL,
                ContactType.CLIENT, "#" + personCount.getOneBased());
        assertCommandSuccess(command, personCount);

        /* Case: undo previous selection -> rejected */
        command = UndoCommand.COMMAND_WORD;
        String expectedResultMessage = UndoCommand.MESSAGE_FAILURE;
        assertCommandFailure(command, expectedResultMessage);

        /* Case: redo selecting last card in the list -> rejected */
        command = RedoCommand.COMMAND_WORD;
        expectedResultMessage = RedoCommand.MESSAGE_FAILURE;
        assertCommandFailure(command, expectedResultMessage);

        // TODO Resolve selection of middle card issue
        /* Case: select the middle card in the client list -> selected */
        // Index middleIndex = getMidIndex(getModel());
        // command = SelectCommand.COMMAND_WORD + " " + middleIndex.getOneBased();
        // assertCommandSuccess(command, middleIndex);

        /* Case: select the current selected card -> selected */
        // assertCommandSuccess(command, middleIndex);

        /* ------------------------ Perform select operations on the shown filtered list ---------------------------- */

        /* Case: filtered client list, select index within bounds of address book but out of bounds of client list
         * -> rejected
         */
        showPersonsWithName(KEYWORD_MATCHING_MEIER);
        int invalidIndex = getModel().getAddressBook().getContactList().size();
        // Select does selecting by unique ID, not relative to the shown list
        // assertCommandFailure(String.format(SelectCommand.COMMAND_WORD_GENERAL,
        // ContactType.CLIENT, "#" + invalidIndex), String.format(MESSAGE_INVALID_PERSON_DISPLAYED_INDEX,
        //         ContactType.CLIENT));

        /* Case: filtered client list, select index within bounds of address book and client list -> selected */
        Index validIndex = Index.fromOneBased(1);
        assertTrue(validIndex.getZeroBased() < getModel().getFilteredContactList().size());
        command = String.format(SelectCommand.COMMAND_WORD_GENERAL,
                ContactType.CLIENT, "#" + validIndex.getOneBased());
        // Select does selecting by unique ID, not relative to the shown list
        // assertCommandSuccess(command, validIndex);

        /* ----------------------------------- Perform invalid select operations ------------------------------------ */

        /* Case: invalid index (0) -> rejected */
        assertCommandFailure(String.format(SelectCommand.COMMAND_WORD_GENERAL,
                ContactType.CLIENT, "#0"), String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                String.format(SelectCommand.MESSAGE_USAGE, ContactType.CLIENT, "#<ID>")));

        /* Case: invalid index (-1) -> rejected */
        assertCommandFailure(String.format(SelectCommand.COMMAND_WORD_GENERAL,
                ContactType.CLIENT, "#-1"), String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                String.format(SelectCommand.MESSAGE_USAGE, ContactType.CLIENT, "#<ID>")));

        // TODO: rewrite this test case: different behaviour now
        /* Case: invalid index (size + 1) -> rejected */
        // assertCommandFailure(String.format(SelectCommand.COMMAND_WORD_GENERAL,
        //         ContactType.CLIENT, "#" + invalidIndex),
        //         String.format(MESSAGE_INVALID_PERSON_DISPLAYED_INDEX, ContactType.CLIENT));

        /* Case: invalid arguments (alphabets) -> rejected */
        assertCommandFailure(String.format(SelectCommand.COMMAND_WORD_GENERAL,
                ContactType.CLIENT, "#abc"), String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                String.format(SelectCommand.MESSAGE_USAGE, ContactType.CLIENT, "#<ID>")));

        assertCommandFailure(String.format(SelectCommand.COMMAND_WORD_GENERAL,
                ContactType.CLIENT, "#1abc1"), String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                String.format(SelectCommand.MESSAGE_USAGE, ContactType.CLIENT, "#<ID>")));

        /* Case: mixed case command word -> rejected */
        assertCommandFailure("SeLeCt 1", MESSAGE_UNKNOWN_COMMAND);

        /* Case: select from empty address book -> rejected */
        deleteAllPersons();
        assertCommandFailure(String.format(SelectCommand.COMMAND_WORD_GENERAL,
                ContactType.CLIENT, "#" + INDEX_FIRST_PERSON.getOneBased()),
                String.format(MESSAGE_INVALID_PERSON_DISPLAYED_INDEX, ContactType.CLIENT));
    }

    /**
     * Executes {@code command} and asserts that the,<br>
     * 1. Command box displays an empty string.<br>
     * 2. Command box has the default style class.<br>
     * 3. Result display box displays the success message of executing select command with the
     * {@code expectedSelectedCardIndex} of the selected client.<br>
     * 4. {@code Storage} and {@code PersonListPanel} remain unchanged.<br>
     * 5. Selected card is at {@code expectedSelectedCardIndex} and the browser url is updated accordingly.<br>
     * 6. Status bar remains unchanged.<br>
     * Verifications 1, 3 and 4 are performed by
     * {@code AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * @see AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     * @see AddressBookSystemTest#assertSelectedCardChanged(Index)
     */
    private void assertCommandSuccess(String command, Index expectedSelectedCardIndex) {
        Model expectedModel = getModel();
        String expectedResultMessage = String.format(MESSAGE_SELECT_CONTACT_SUCCESS, ContactType.CLIENT,
                expectedSelectedCardIndex.getOneBased());
        int preExecutionSelectedCardIndex = getPersonListPanel().getSelectedCardIndex();

        executeCommand(command);
        assertApplicationDisplaysExpected("", expectedResultMessage, expectedModel);

        if (preExecutionSelectedCardIndex == expectedSelectedCardIndex.getZeroBased()) {
            assertSelectedCardUnchanged();
        } else {
            assertSelectedCardChanged(expectedSelectedCardIndex);
        }

        assertCommandBoxShowsDefaultStyle();
        assertStatusBarUnchanged();
    }

    /**
     * Executes {@code command} and asserts that the,<br>
     * 1. Command box displays {@code command}.<br>
     * 2. Command box has the error style class.<br>
     * 3. Result display box displays {@code expectedResultMessage}.<br>
     * 4. {@code Storage} and {@code PersonListPanel} remain unchanged.<br>
     * 5. Browser url, selected card and status bar remain unchanged.<br>
     * Verifications 1, 3 and 4 are performed by
     * {@code AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * @see AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandFailure(String command, String expectedResultMessage) {
        Model expectedModel = getModel();

        executeCommand(command);
        assertApplicationDisplaysExpected(command, expectedResultMessage, expectedModel);
        assertSelectedCardUnchanged();
        assertCommandBoxShowsErrorStyle();
        assertStatusBarUnchanged();
    }
}
