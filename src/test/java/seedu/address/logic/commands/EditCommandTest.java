package seedu.address.logic.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.typicalContacts.getTypicalAddressBook;

import org.junit.Ignore;
import org.junit.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.EditCommand.EditContactDescriptor;
import seedu.address.model.AddressBook;
import seedu.address.model.ContactType;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.contact.Contact;
import seedu.address.testutil.ClientBuilder;
import seedu.address.testutil.EditContactDescriptorBuilder;
import seedu.address.testutil.ServiceProviderBuilder;
import seedu.address.testutil.TypicalAccount;

/**
 * Contains integration tests (interaction with the Model, UndoCommand and RedoCommand) and unit tests for EditCommand.
 */
public class EditCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs(), TypicalAccount.ROOTACCOUNT);
    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void execute_allFieldsSpecifiedClientList_success() {
        model.updateFilteredContactList(ContactType.CLIENT.getFilter());
        Contact editedContact = new ClientBuilder().build();
        EditCommand.EditContactDescriptor descriptor = new EditContactDescriptorBuilder(editedContact).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON, descriptor, ContactType.CLIENT);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, editedContact);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs(),
                TypicalAccount.ROOTACCOUNT);
        expectedModel.updateFilteredContactList(ContactType.CLIENT.getFilter());
        expectedModel.updateContact(model.getFilteredContactList().get(0), editedContact);
        expectedModel.commitAddressBook();

        assertCommandSuccess(editCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_allFieldsSpecifiedServiceProviderList_success() {
        model.updateFilteredContactList(ContactType.SERVICE_PROVIDER.getFilter());
        Contact editedContact = new ServiceProviderBuilder().build();
        EditCommand.EditContactDescriptor descriptor = new EditContactDescriptorBuilder(editedContact).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON, descriptor, ContactType.SERVICE_PROVIDER);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, editedContact);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs(),
                TypicalAccount.ROOTACCOUNT);
        expectedModel.updateFilteredContactList(ContactType.SERVICE_PROVIDER.getFilter());
        expectedModel.updateContact(model.getFilteredContactList().get(0), editedContact);
        expectedModel.commitAddressBook();

        assertCommandSuccess(editCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_someFieldsSpecifiedClientList_success() {
        model.updateFilteredContactList(ContactType.CLIENT.getFilter());
        Index indexLastPerson = Index.fromOneBased(model.getFilteredContactList().size());
        Contact lastContact = model.getFilteredContactList().get(indexLastPerson.getZeroBased());

        ClientBuilder personInList = new ClientBuilder(lastContact);
        Contact editedContact = personInList.withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB)
                .withTags(VALID_TAG_HUSBAND).build();

        EditCommand.EditContactDescriptor descriptor = new EditContactDescriptorBuilder().withName(VALID_NAME_BOB)
                .withPhone(VALID_PHONE_BOB).withTags(VALID_TAG_HUSBAND).build();
        EditCommand editCommand = new EditCommand(indexLastPerson, descriptor, ContactType.CLIENT);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, editedContact);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs(),
                TypicalAccount.ROOTACCOUNT);
        expectedModel.updateFilteredContactList(ContactType.CLIENT.getFilter());
        expectedModel.updateContact(lastContact, editedContact);
        expectedModel.commitAddressBook();

        assertCommandSuccess(editCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_someFieldsSpecifiedServiceProviderList_success() {
        model.updateFilteredContactList(ContactType.SERVICE_PROVIDER.getFilter());
        Index indexLastPerson = Index.fromOneBased(model.getFilteredContactList().size());
        Contact lastContact = model.getFilteredContactList().get(indexLastPerson.getZeroBased());

        ServiceProviderBuilder personInList = new ServiceProviderBuilder(lastContact);
        Contact editedContact = personInList.withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB)
                .withTags(VALID_TAG_HUSBAND).build();

        EditCommand.EditContactDescriptor descriptor = new EditContactDescriptorBuilder().withName(VALID_NAME_BOB)
                .withPhone(VALID_PHONE_BOB).withTags(VALID_TAG_HUSBAND).build();
        EditCommand editCommand = new EditCommand(indexLastPerson, descriptor, ContactType.SERVICE_PROVIDER);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, editedContact);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs(),
                TypicalAccount.ROOTACCOUNT);
        expectedModel.updateFilteredContactList(ContactType.SERVICE_PROVIDER.getFilter());
        expectedModel.updateContact(lastContact, editedContact);
        expectedModel.commitAddressBook();

        assertCommandSuccess(editCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noFieldSpecifiedClientList_success() {
        model.updateFilteredContactList(ContactType.CLIENT.getFilter());
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON, new EditContactDescriptor(), ContactType.CLIENT);
        Contact editedContact = model.getFilteredContactList().get(INDEX_FIRST_PERSON.getZeroBased());

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, editedContact);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs(),
                TypicalAccount.ROOTACCOUNT);
        expectedModel.updateFilteredContactList(ContactType.CLIENT.getFilter());
        expectedModel.commitAddressBook();

        assertCommandSuccess(editCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noFieldSpecifiedServiceProviderList_success() {
        model.updateFilteredContactList(ContactType.SERVICE_PROVIDER.getFilter());
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON, new EditContactDescriptor(),
                ContactType.SERVICE_PROVIDER);
        Contact editedContact = model.getFilteredContactList().get(INDEX_FIRST_PERSON.getZeroBased());

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, editedContact);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs(),
                TypicalAccount.ROOTACCOUNT);
        expectedModel.updateFilteredContactList(ContactType.SERVICE_PROVIDER.getFilter());
        expectedModel.commitAddressBook();

        assertCommandSuccess(editCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_clientList_success() {
        model.updateFilteredContactList(ContactType.CLIENT.getFilter());
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Contact contactInFilteredList = model.getFilteredContactList().get(INDEX_FIRST_PERSON.getZeroBased());
        Contact editedContact = new ClientBuilder(contactInFilteredList).withName(VALID_NAME_BOB).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON,
                new EditContactDescriptorBuilder().withName(VALID_NAME_BOB).build(), ContactType.CLIENT);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, editedContact);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs(),
                TypicalAccount.ROOTACCOUNT);
        expectedModel.updateContact(model.getFilteredContactList().get(0), editedContact);
        expectedModel.updateFilteredContactList(ContactType.CLIENT.getFilter());
        expectedModel.commitAddressBook();

        assertCommandSuccess(editCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_serviceProviderList_success() {
        model.updateFilteredContactList(ContactType.SERVICE_PROVIDER.getFilter());
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Contact contactInFilteredList = model.getFilteredContactList().get(INDEX_FIRST_PERSON.getZeroBased());
        Contact editedContact = new ServiceProviderBuilder(contactInFilteredList).withName(VALID_NAME_BOB).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON,
                new EditContactDescriptorBuilder().withName(VALID_NAME_BOB).build(), ContactType.SERVICE_PROVIDER);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, editedContact);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs(),
                TypicalAccount.ROOTACCOUNT);
        expectedModel.updateContact(model.getFilteredContactList().get(0), editedContact);
        expectedModel.updateFilteredContactList(ContactType.SERVICE_PROVIDER.getFilter());
        expectedModel.commitAddressBook();

        assertCommandSuccess(editCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicateContactClientList_failure() {
        model.updateFilteredContactList(ContactType.CLIENT.getFilter());
        Contact firstContact = model.getFilteredContactList().get(INDEX_FIRST_PERSON.getZeroBased());
        EditCommand.EditContactDescriptor descriptor = new EditContactDescriptorBuilder(firstContact).build();
        EditCommand editCommand = new EditCommand(INDEX_SECOND_PERSON, descriptor, ContactType.CLIENT);

        assertCommandFailure(editCommand, model, commandHistory, EditCommand.MESSAGE_DUPLICATE_PERSON);
    }

    @Test
    public void execute_duplicateContactServiceProviderList_failure() {
        model.updateFilteredContactList(ContactType.SERVICE_PROVIDER.getFilter());
        Contact firstContact = model.getFilteredContactList().get(INDEX_FIRST_PERSON.getZeroBased());
        EditCommand.EditContactDescriptor descriptor = new EditContactDescriptorBuilder(firstContact).build();
        EditCommand editCommand = new EditCommand(INDEX_SECOND_PERSON, descriptor, ContactType.SERVICE_PROVIDER);

        assertCommandFailure(editCommand, model, commandHistory, EditCommand.MESSAGE_DUPLICATE_PERSON);
    }

    // Create a client that is a duplicate of a serviceprovider --> command failure
    @Test
    public void execute_duplicateClientAndServiceProviderList_failure() {
        model.updateFilteredContactList(ContactType.SERVICE_PROVIDER.getFilter());
        Contact firstContact = model.getFilteredContactList().get(INDEX_FIRST_PERSON.getZeroBased());
        EditCommand.EditContactDescriptor descriptor = new EditContactDescriptorBuilder(firstContact).build();
        model.updateFilteredContactList(ContactType.CLIENT.getFilter());
        EditCommand editCommand = new EditCommand(INDEX_SECOND_PERSON, descriptor, ContactType.CLIENT);

        assertCommandFailure(editCommand, model, commandHistory, EditCommand.MESSAGE_DUPLICATE_PERSON);
    }

    @Test
    public void execute_duplicateClientFilteredList_failure() {
        model.updateFilteredContactList(ContactType.CLIENT.getFilter());
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        // edit client in filtered list into a duplicate in address book
        Contact contactInList = model.getAddressBook().getContactList().get(INDEX_SECOND_PERSON.getZeroBased());
        // TODO: how update works now is to filter the list by client/serviceprovider, then update. To change implmtion.
        model.updateFilteredContactList(ContactType.CLIENT.getFilter());
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON,
                new EditContactDescriptorBuilder(contactInList).build(), ContactType.CLIENT);

        assertCommandFailure(editCommand, model, commandHistory, EditCommand.MESSAGE_DUPLICATE_PERSON);
    }

    @Test
    public void execute_duplicateServiceProviderFilteredList_failure() {
        model.updateFilteredContactList(ContactType.SERVICE_PROVIDER.getFilter());
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        // edit client in filtered list into a duplicate in address book
        Contact contactInList = model.getAddressBook().getContactList().get(INDEX_SECOND_PERSON.getZeroBased());
        model.updateFilteredContactList(ContactType.SERVICE_PROVIDER.getFilter());
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON,
                new EditContactDescriptorBuilder(contactInList).build(), ContactType.SERVICE_PROVIDER);

        assertCommandFailure(editCommand, model, commandHistory, EditCommand.MESSAGE_DUPLICATE_PERSON);
    }

    @Test
    public void execute_invalidClientIndexClientList_failure() {
        model.updateFilteredContactList(ContactType.CLIENT.getFilter());
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredContactList().size() + 1);
        EditContactDescriptor descriptor = new EditContactDescriptorBuilder().withName(VALID_NAME_BOB).build();
        EditCommand editCommand = new EditCommand(outOfBoundIndex, descriptor, ContactType.CLIENT);

        assertCommandFailure(editCommand, model, commandHistory, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidServiceProviderIndexServiceProviderList_failure() {
        model.updateFilteredContactList(ContactType.SERVICE_PROVIDER.getFilter());
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredContactList().size() + 1);
        EditContactDescriptor descriptor = new EditContactDescriptorBuilder().withName(VALID_NAME_BOB).build();
        EditCommand editCommand = new EditCommand(outOfBoundIndex, descriptor, ContactType.SERVICE_PROVIDER);

        assertCommandFailure(editCommand, model, commandHistory, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    /**
     * Edit filtered list where index is larger than size of filtered list,
     * but smaller than size of address book
     */
    @Test
    public void execute_clientOutsideFilteredList_success() {
        model.updateFilteredContactList(ContactType.CLIENT.getFilter());
        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        Contact contactOutsideFilteredList = model.getFilteredContactList().get(outOfBoundIndex.getZeroBased());
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getContactList().size());

        Contact editedContact = new ClientBuilder(contactOutsideFilteredList).withName(VALID_NAME_BOB).build();
        model.updateFilteredContactList(ContactType.CLIENT.getFilter());
        EditCommand editCommand = new EditCommand(outOfBoundIndex,
                new EditContactDescriptorBuilder().withName(VALID_NAME_BOB).build(), ContactType.CLIENT);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, editedContact);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs(),
                TypicalAccount.ROOTACCOUNT);
        model.updateFilteredContactList(ContactType.CLIENT.getFilter());
        expectedModel.updateContact(expectedModel.getFilteredContactList().get(outOfBoundIndex.getZeroBased()), editedContact);
        expectedModel.updateFilteredContactList(ContactType.CLIENT.getFilter());
        expectedModel.commitAddressBook();

        assertCommandSuccess(editCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void executeUndoRedo_validIndexUnfilteredList_success() throws Exception {
        Contact editedContact = new ClientBuilder().build();
        Contact contactToEdit = model.getFilteredContactList().get(INDEX_FIRST_PERSON.getZeroBased());
        EditContactDescriptor descriptor = new EditContactDescriptorBuilder(editedContact).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON, descriptor, ContactType.CLIENT);
        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs(),
                TypicalAccount.ROOTACCOUNT);
        expectedModel.updateContact(contactToEdit, editedContact);
        expectedModel.commitAddressBook();

        // edit -> first client edited
        editCommand.execute(model, commandHistory);

        // undo -> reverts addressbook back to previous state and filtered client list to show all persons
        expectedModel.undoAddressBook();
        expectedModel.updateFilteredContactList(ContactType.CLIENT.getFilter());
        assertCommandSuccess(new UndoCommand(), model, commandHistory, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        // redo -> same first client edited again
        expectedModel.redoAddressBook();
        assertCommandSuccess(new RedoCommand(), model, commandHistory, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void executeUndoRedo_invalidIndexUnfilteredList_failure() {
        model.updateFilteredContactList(ContactType.CLIENT.getFilter());
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredContactList().size() + 1);
        EditContactDescriptor descriptor = new EditContactDescriptorBuilder().withName(VALID_NAME_BOB).build();
        EditCommand editCommand = new EditCommand(outOfBoundIndex, descriptor, ContactType.CLIENT);

        // execution failed -> address book state not added into model
        assertCommandFailure(editCommand, model, commandHistory, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);

        // single address book state in model -> undoCommand and redoCommand fail
        assertCommandFailure(new UndoCommand(), model, commandHistory, UndoCommand.MESSAGE_FAILURE);
        assertCommandFailure(new RedoCommand(), model, commandHistory, RedoCommand.MESSAGE_FAILURE);
    }

    /**
     * 1. Edits a {@code Client} from a filtered list.
     * 2. Undo the edit.
     * 3. The unfiltered list should be shown now. Verify that the index of the previously edited client in the
     * unfiltered list is different from the index at the filtered list.
     * 4. Redo the edit. This ensures {@code RedoCommand} edits the client object regardless of indexing.
     */
    @Test
    @Ignore
    public void executeUndoRedo_validIndexFilteredList_samePersonEdited() throws Exception {
        Contact editedContact = new ClientBuilder().build();
        EditContactDescriptor descriptor = new EditContactDescriptorBuilder(editedContact).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON, descriptor, ContactType.CLIENT);
        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs(),
                TypicalAccount.ROOTACCOUNT);

        showPersonAtIndex(model, INDEX_SECOND_PERSON);
        Contact contactToEdit = model.getFilteredContactList().get(INDEX_FIRST_PERSON.getZeroBased());
        expectedModel.updateContact(contactToEdit, editedContact);
        expectedModel.commitAddressBook();

        // edit -> edits second client in unfiltered client list / first client in filtered client list
        editCommand.execute(model, commandHistory);

        // undo -> reverts addressbook back to previous state and filtered client list to show all persons
        expectedModel.undoAddressBook();
        assertCommandSuccess(new UndoCommand(), model, commandHistory, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        assertNotEquals(model.getFilteredContactList().get(INDEX_FIRST_PERSON.getZeroBased()), contactToEdit);
        // redo -> edits same second client in unfiltered client list
        expectedModel.redoAddressBook();
        assertCommandSuccess(new RedoCommand(), model, commandHistory, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void equals() {
        final EditCommand standardCommand = new EditCommand(INDEX_FIRST_PERSON, DESC_AMY, ContactType.CLIENT);

        // same values -> returns true
        EditContactDescriptor copyDescriptor = new EditCommand.EditContactDescriptor(DESC_AMY);
        EditCommand commandWithSameValues = new EditCommand(INDEX_FIRST_PERSON, copyDescriptor, ContactType.CLIENT);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new EditCommand(INDEX_SECOND_PERSON, DESC_AMY, ContactType.CLIENT)));

        // different descriptor -> returns false
        assertFalse(standardCommand.equals(new EditCommand(INDEX_FIRST_PERSON, DESC_BOB, ContactType.CLIENT)));
    }

}
