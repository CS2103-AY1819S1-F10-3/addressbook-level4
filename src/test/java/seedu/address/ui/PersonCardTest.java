package seedu.address.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.ui.testutil.GuiTestAssert.assertCardDisplaysPerson;

import org.junit.Test;

import guitests.guihandles.PersonCardHandle;
import seedu.address.model.contact.Contact;
import seedu.address.testutil.PersonBuilder;

public class PersonCardTest extends GuiUnitTest {

    @Test
    public void display() {
        // no tags
        Contact contactWithNoTags = new PersonBuilder().withTags(new String[0]).build();
        PersonCard personCard = new PersonCard(contactWithNoTags, 1);
        uiPartRule.setUiPart(personCard);
        assertCardDisplay(personCard, contactWithNoTags, 1);

        // with tags
        Contact contactWithTags = new PersonBuilder().build();
        personCard = new PersonCard(contactWithTags, 2);
        uiPartRule.setUiPart(personCard);
        assertCardDisplay(personCard, contactWithTags, 2);
    }

    @Test
    public void equals() {
        Contact contact = new PersonBuilder().build();
        PersonCard personCard = new PersonCard(contact, 0);

        // same person, same index -> returns true
        PersonCard copy = new PersonCard(contact, 0);
        assertTrue(personCard.equals(copy));

        // same object -> returns true
        assertTrue(personCard.equals(personCard));

        // null -> returns false
        assertFalse(personCard.equals(null));

        // different types -> returns false
        assertFalse(personCard.equals(0));

        // different person, same index -> returns false
        Contact differentContact = new PersonBuilder().withName("differentName").build();
        assertFalse(personCard.equals(new PersonCard(differentContact, 0)));

        // same person, different index -> returns false
        assertFalse(personCard.equals(new PersonCard(contact, 1)));
    }

    /**
     * Asserts that {@code personCard} displays the details of {@code expectedPerson} correctly and matches
     * {@code expectedId}.
     */
    private void assertCardDisplay(PersonCard personCard, Contact expectedContact, int expectedId) {
        guiRobot.pauseForHuman();

        PersonCardHandle personCardHandle = new PersonCardHandle(personCard.getRoot());

        // verify id is displayed correctly
        assertEquals(Integer.toString(expectedId) + ". ", personCardHandle.getId());

        // verify person details are displayed correctly
        assertCardDisplaysPerson(expectedContact, personCardHandle);
    }
}
