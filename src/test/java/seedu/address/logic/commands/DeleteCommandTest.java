package seedu.address.logic.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.typicalContacts.getTypicalAddressBook;

import org.junit.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.model.ContactType;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.contact.Contact;
import seedu.address.testutil.TypicalAccount;

/**
 * Contains integration tests (interaction with the Model, UndoCommand and RedoCommand) and unit tests for
 * {@code DeleteCommand}.
 */
public class DeleteCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs(), TypicalAccount.ROOTACCOUNT);
    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void execute_validIndexClientList_success() {
        model.updateFilteredContactList(ContactType.CLIENT.getFilter());
        Contact contactToDelete = model.getFilteredContactList().get(INDEX_FIRST_PERSON.getZeroBased());
        DeleteCommand deleteCommand = new DeleteCommand(INDEX_FIRST_PERSON, ContactType.CLIENT);

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_PERSON_SUCCESS, contactToDelete);

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs(),
                TypicalAccount.ROOTACCOUNT);
        expectedModel.updateFilteredContactList(ContactType.CLIENT.getFilter());
        expectedModel.deleteContact(contactToDelete);
        expectedModel.commitAddressBook();

        assertCommandSuccess(deleteCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_validIndexServiceProviderList_success() {
        model.updateFilteredContactList(ContactType.SERVICE_PROVIDER.getFilter());
        Contact contactToDelete = model.getFilteredContactList().get(INDEX_FIRST_PERSON.getZeroBased());
        DeleteCommand deleteCommand = new DeleteCommand(INDEX_FIRST_PERSON, ContactType.SERVICE_PROVIDER);

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_PERSON_SUCCESS, contactToDelete);

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs(),
                TypicalAccount.ROOTACCOUNT);
        expectedModel.updateFilteredContactList(ContactType.SERVICE_PROVIDER.getFilter());
        expectedModel.deleteContact(contactToDelete);
        expectedModel.commitAddressBook();

        assertCommandSuccess(deleteCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexClientList_throwsCommandException() {
        model.updateFilteredContactList(ContactType.CLIENT.getFilter());
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredContactList().size() + 1);
        DeleteCommand deleteCommand = new DeleteCommand(outOfBoundIndex, ContactType.CLIENT);

        assertCommandFailure(deleteCommand, model, commandHistory, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidIndexServiceProviderList_throwsCommandException() {
        model.updateFilteredContactList(ContactType.SERVICE_PROVIDER.getFilter());
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredContactList().size() + 1);
        DeleteCommand deleteCommand = new DeleteCommand(outOfBoundIndex, ContactType.SERVICE_PROVIDER);

        assertCommandFailure(deleteCommand, model, commandHistory, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    /**
     * deleting a person from heartsquare shows the client/serviceprovider list depending on client#ID delete or
     * serviceprovider#ID delete. Heartsquare's deletion of contacts are not relative to the list shown on the UI but
     * instead specifically client/serviceprovider ID
     */
    @Test
    public void execute_clientShownInClientFilteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        model.updateFilteredContactList(ContactType.CLIENT.getFilter());
        Contact contactToDelete = model.getFilteredContactList().get(INDEX_FIRST_PERSON.getZeroBased());
        DeleteCommand deleteCommand = new DeleteCommand(INDEX_FIRST_PERSON, ContactType.CLIENT);

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_PERSON_SUCCESS, contactToDelete);

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs(), TypicalAccount.ROOTACCOUNT);
        expectedModel.deleteContact(contactToDelete);
        expectedModel.commitAddressBook();
        expectedModel.updateFilteredContactList(ContactType.CLIENT.getFilter());

        assertCommandSuccess(deleteCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    /**
     * deleting a person from heartsquare shows the client/serviceprovider list depending on client#ID delete or
     * serviceprovider#ID delete. Heartsquare's deletion of contacts are not relative to the list shown on the UI but
     * instead specifically client/serviceprovider ID
     */
    @Test
    public void execute_serviceProviderShownInServiceProviderFilteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        model.updateFilteredContactList(ContactType.SERVICE_PROVIDER.getFilter());
        Contact contactToDelete = model.getFilteredContactList().get(INDEX_FIRST_PERSON.getZeroBased());
        DeleteCommand deleteCommand = new DeleteCommand(INDEX_FIRST_PERSON, ContactType.SERVICE_PROVIDER);

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_PERSON_SUCCESS, contactToDelete);

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs(), TypicalAccount.ROOTACCOUNT);
        expectedModel.deleteContact(contactToDelete);
        expectedModel.commitAddressBook();
        expectedModel.updateFilteredContactList(ContactType.SERVICE_PROVIDER.getFilter());

        assertCommandSuccess(deleteCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_deleteClientNotShownInClientFilteredList_success() {
        model.updateFilteredContactList(ContactType.CLIENT.getFilter());
        showPersonAtIndex(model, INDEX_SECOND_PERSON);
        Contact contactToDelete = model.getFilteredContactList().get(0);

        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getContactList().size());

        DeleteCommand deleteCommand = new DeleteCommand(outOfBoundIndex, ContactType.CLIENT);

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_PERSON_SUCCESS, contactToDelete);

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs(), TypicalAccount.ROOTACCOUNT);
        expectedModel.deleteContact(contactToDelete);
        expectedModel.commitAddressBook();
        expectedModel.updateFilteredContactList(ContactType.CLIENT.getFilter());

        assertCommandSuccess(deleteCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_deleteServiceProviderNotShownInServiceProviderFilteredList_success() {
        model.updateFilteredContactList(ContactType.SERVICE_PROVIDER.getFilter());
        showPersonAtIndex(model, INDEX_SECOND_PERSON);
        Contact contactToDelete = model.getFilteredContactList().get(0);

        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getContactList().size());

        DeleteCommand deleteCommand = new DeleteCommand(outOfBoundIndex, ContactType.SERVICE_PROVIDER);

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_PERSON_SUCCESS, contactToDelete);

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs(), TypicalAccount.ROOTACCOUNT);
        expectedModel.deleteContact(contactToDelete);
        expectedModel.commitAddressBook();
        expectedModel.updateFilteredContactList(ContactType.SERVICE_PROVIDER.getFilter());

        assertCommandSuccess(deleteCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_deleteServiceProviderWhileShownClientFilteredList_success() {
        model.updateFilteredContactList(ContactType.SERVICE_PROVIDER.getFilter());
        Contact contactToDelete = model.getFilteredContactList().get(INDEX_FIRST_PERSON.getZeroBased());

        model.updateFilteredContactList(ContactType.CLIENT.getFilter());
        assertTrue(!model.getFilteredContactList().contains(contactToDelete));

        DeleteCommand deleteCommand = new DeleteCommand(Index.fromOneBased(contactToDelete.getID()),
                ContactType.SERVICE_PROVIDER);

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_PERSON_SUCCESS, contactToDelete);

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs(), TypicalAccount.ROOTACCOUNT);

        expectedModel.deleteContact(contactToDelete);
        expectedModel.commitAddressBook();
        expectedModel.updateFilteredContactList(ContactType.SERVICE_PROVIDER.getFilter());

        assertCommandSuccess(deleteCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_deleteClientWhileShownServiceProviderFilteredList_success() {
        model.updateFilteredContactList(ContactType.CLIENT.getFilter());
        Contact contactToDelete = model.getFilteredContactList().get(INDEX_FIRST_PERSON.getZeroBased());

        model.updateFilteredContactList(ContactType.SERVICE_PROVIDER.getFilter());
        assertTrue(!model.getFilteredContactList().contains(contactToDelete));

        DeleteCommand deleteCommand = new DeleteCommand(Index.fromOneBased(contactToDelete.getID()),
                ContactType.CLIENT);

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_PERSON_SUCCESS, contactToDelete);

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs(), TypicalAccount.ROOTACCOUNT);

        expectedModel.deleteContact(contactToDelete);
        expectedModel.commitAddressBook();
        expectedModel.updateFilteredContactList(ContactType.CLIENT.getFilter());

        assertCommandSuccess(deleteCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void executeUndoRedo_validIndexFilteredClientList_success() throws Exception {
        model.updateFilteredContactList(ContactType.CLIENT.getFilter());
        Contact contactToDelete = model.getFilteredContactList().get(INDEX_FIRST_PERSON.getZeroBased());
        DeleteCommand deleteCommand = new DeleteCommand(INDEX_FIRST_PERSON, ContactType.CLIENT);

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs(), TypicalAccount.ROOTACCOUNT);
        expectedModel.updateFilteredContactList(ContactType.CLIENT.getFilter());
        expectedModel.deleteContact(contactToDelete);
        expectedModel.commitAddressBook();

        // delete -> first client deleted
        deleteCommand.execute(model, commandHistory);

        // undo -> reverts addressbook back to previous state and filtered client list to show all persons
        expectedModel.undoAddressBook();
        assertCommandSuccess(new UndoCommand(), model, commandHistory, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        // redo -> same first client deleted again
        expectedModel.redoAddressBook();
        assertCommandSuccess(new RedoCommand(), model, commandHistory, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void executeUndoRedo_validIndexFilteredServiceProviderList_success() throws Exception {
        model.updateFilteredContactList(ContactType.SERVICE_PROVIDER.getFilter());
        Contact contactToDelete = model.getFilteredContactList().get(INDEX_FIRST_PERSON.getZeroBased());
        DeleteCommand deleteCommand = new DeleteCommand(INDEX_FIRST_PERSON, ContactType.SERVICE_PROVIDER);

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs(), TypicalAccount.ROOTACCOUNT);
        expectedModel.updateFilteredContactList(ContactType.SERVICE_PROVIDER.getFilter());
        expectedModel.deleteContact(contactToDelete);
        expectedModel.commitAddressBook();

        // delete -> first client deleted
        deleteCommand.execute(model, commandHistory);

        // undo -> reverts addressbook back to previous state and filtered client list to show all persons
        expectedModel.undoAddressBook();
        assertCommandSuccess(new UndoCommand(), model, commandHistory, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        // redo -> same first client deleted again
        expectedModel.redoAddressBook();
        assertCommandSuccess(new RedoCommand(), model, commandHistory, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void executeUndoRedo_invalidIndexClientList_failure() {
        model.updateFilteredContactList(ContactType.CLIENT.getFilter());
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredContactList().size() + 1);
        DeleteCommand deleteCommand = new DeleteCommand(outOfBoundIndex, ContactType.CLIENT);

        // execution failed -> address book state not added into model
        assertCommandFailure(deleteCommand, model, commandHistory, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);

        // single address book state in model -> undoCommand and redoCommand fail
        assertCommandFailure(new UndoCommand(), model, commandHistory, UndoCommand.MESSAGE_FAILURE);
        assertCommandFailure(new RedoCommand(), model, commandHistory, RedoCommand.MESSAGE_FAILURE);
    }

    @Test
    public void executeUndoRedo_invalidIndexServiceProviderList_failure() {
        model.updateFilteredContactList(ContactType.SERVICE_PROVIDER.getFilter());
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredContactList().size() + 1);
        DeleteCommand deleteCommand = new DeleteCommand(outOfBoundIndex, ContactType.SERVICE_PROVIDER);

        // execution failed -> address book state not added into model
        assertCommandFailure(deleteCommand, model, commandHistory, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);

        // single address book state in model -> undoCommand and redoCommand fail
        assertCommandFailure(new UndoCommand(), model, commandHistory, UndoCommand.MESSAGE_FAILURE);
        assertCommandFailure(new RedoCommand(), model, commandHistory, RedoCommand.MESSAGE_FAILURE);
    }

    /**
     * 1. Deletes a {@code Client} from a filtered list.
     * 2. Undo the deletion.
     * 3. The unfiltered list should be shown now. Verify that the index of the previously deleted client in the
     * unfiltered list is different from the index at the filtered list.
     * 4. Redo the deletion. This ensures {@code RedoCommand} deletes the client object regardless of indexing.
     */
    @Test
    public void executeUndoRedo_validIndexFilteredList_samePersonDeleted() throws Exception {
        DeleteCommand deleteCommand = new DeleteCommand(INDEX_FIRST_PERSON, ContactType.CLIENT);
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs(), TypicalAccount.ROOTACCOUNT);

        showPersonAtIndex(model, INDEX_SECOND_PERSON);
        Contact contactToDelete = model.getFilteredContactList().get(INDEX_FIRST_PERSON.getZeroBased());
        expectedModel.deleteContact(contactToDelete);
        expectedModel.commitAddressBook();

        // delete -> deletes second client in unfiltered client list / first client in filtered client list
        deleteCommand.execute(model, commandHistory);

        // undo -> reverts addressbook back to previous state and filtered client list to show all persons
        expectedModel.undoAddressBook();
        assertCommandSuccess(new UndoCommand(), model, commandHistory, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        assertNotEquals(contactToDelete, model.getFilteredContactList().get(INDEX_FIRST_PERSON.getZeroBased()));
        // redo -> deletes same second client in unfiltered client list
        expectedModel.redoAddressBook();
        assertCommandSuccess(new RedoCommand(), model, commandHistory, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void equals() {
        DeleteCommand deleteFirstCommand = new DeleteCommand(INDEX_FIRST_PERSON, ContactType.CLIENT);
        DeleteCommand deleteSecondCommand = new DeleteCommand(INDEX_SECOND_PERSON, ContactType.CLIENT);

        // same object -> returns true
        assertTrue(deleteFirstCommand.equals(deleteFirstCommand));

        // same values -> returns true
        DeleteCommand deleteFirstCommandCopy = new DeleteCommand(INDEX_FIRST_PERSON, ContactType.CLIENT);
        assertTrue(deleteFirstCommand.equals(deleteFirstCommandCopy));

        // different types -> returns false
        assertFalse(deleteFirstCommand.equals(1));

        // null -> returns false
        assertFalse(deleteFirstCommand.equals(null));

        // different client -> returns false
        assertFalse(deleteFirstCommand.equals(deleteSecondCommand));
    }

    /**
     * Updates {@code model}'s filtered list to show no one.
     */
    private void showNoPerson(Model model) {
        model.updateFilteredContactList(p -> false);

        assertTrue(model.getFilteredContactList().isEmpty());
    }
}
