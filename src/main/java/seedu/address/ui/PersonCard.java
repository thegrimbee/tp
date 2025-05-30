package seedu.address.ui;

import java.util.Comparator;
import java.util.stream.Collectors;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.person.Person;

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

    public final Person person;

    @FXML
    private HBox personCardPane;
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
    private FlowPane roles;
    @FXML
    private Label ccas;

    /**
     * Creates a {@code PersonCode} with the given {@code Person} and index to display.
     */
    public PersonCard(Person person, int displayedIndex) {
        super(FXML);
        this.person = person;
        id.setText(displayedIndex + ". ");
        name.setText(person.getName().fullName);
        phone.setText(person.getPhone().value);
        address.setText(person.getAddress().value);
        email.setText(person.getEmail().value);

        ccas.setWrapText(true);
        person.getCcaInformations().stream()
                .map(ccaInfo -> ccaInfo.getCca().getCcaName().fullCcaName + " "
                        + ccaInfo.getRole())
                .sorted()
                .forEach(role -> roles.getChildren().add(new Label(role)));

        // Display CCA info or "No CCA" if none
        if (person.getCcaInformations().isEmpty()) {
            ccas.setText("No CCA");
        } else {
            String ccaText = person.getCcaInformations().stream()
                    .sorted(Comparator.comparing(ccaInfo -> ccaInfo.getCca().getCcaName().fullCcaName))
                    .map(ccaInfo -> String.format("%s (%d/%d)",
                            ccaInfo.getCca().getCcaName().fullCcaName,
                            ccaInfo.getAttendance().getSessionsAttended().getSessionCount(),
                            ccaInfo.getAttendance().getTotalSessions().getSessionCount()))
                    .collect(Collectors.joining(", "));

            ccas.setText("CCAs: " + ccaText);
        }
    }
}
