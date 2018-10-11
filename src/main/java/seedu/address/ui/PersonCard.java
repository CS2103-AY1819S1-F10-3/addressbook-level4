package seedu.address.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.contact.Contact;

/**
 * An UI component that displays information of a {@code Person}.
 */
public class PersonCard extends UiPart<Region> {

    private static final String FXML = "PersonListCard.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final Contact contact;

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label phone;
    @FXML
    private Label address;
    @FXML
    private Label email;
    @FXML
    private FlowPane tags;

    public PersonCard(Contact contact, int displayedIndex) {
        super(FXML);
        this.contact = contact;
        id.setText(displayedIndex + ". ");
        name.setText(contact.getName().fullName);
        phone.setText(contact.getPhone().value);
        address.setText(contact.getAddress().value);
        email.setText(contact.getEmail().value);
        assignTags(contact);
    }

    /**
     * Assigns all tags for the person with a label.
     *
     * @param contact Current person to assign tags to
     */
    private void assignTags(Contact contact) {
        contact.getTags().forEach(tag -> {
            Label tagLabel = createLabelforTag(tag.tagName);
            tags.getChildren().add(tagLabel);
        });
    }

    /**
     * Creates a label for the tag. Label is set to grey if the tag indicates price.
     * Otherwise, it would be set to pink as default.
     *
     * @param tagName Name of the tag
     * @return new Label for the tag
     */
    private Label createLabelforTag(String tagName) {
        Label tagLabel = new Label(tagName);

        if (tagName.toLowerCase().contains("price")) {
            tagLabel.getStyleClass().add("grey");
        } else {
            tagLabel.getStyleClass().add("pink");
        }

        return tagLabel;
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof PersonCard)) {
            return false;
        }

        // state check
        PersonCard card = (PersonCard) other;
        return id.getText().equals(card.id.getText())
                && contact.equals(card.contact);
    }
}
