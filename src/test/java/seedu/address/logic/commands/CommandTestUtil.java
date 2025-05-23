package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_AMOUNT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CCA_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ROLE;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.Person;
import seedu.address.testutil.EditPersonDescriptorBuilder;

/**
 * Contains helper methods for testing commands.
 */
public class CommandTestUtil {

    public static final String VALID_CCA_NAME_BASKETBALL = "Basketball";
    public static final String VALID_CCA_NAME_BADMINTON = "Badminton";
    public static final String VALID_CCA_NAME_SWIMMING = "Swimming";
    public static final String VALID_CCA_NAME_TABLE_TENNIS = "Table Tennis";
    public static final String VALID_CCA_NAME_TENNIS = "Tennis";
    public static final String VALID_CCA_NAME_VOLLEYBALL = "Volleyball";
    public static final String VALID_CCA_NAME_TRACK_AND_FIELD = "Track and Field";
    public static final String VALID_CCA_NAME_GARDENING = "Gardening";
    public static final String VALID_CCA_NAME_ACTING = "Acting";

    public static final String CCA_NAME_DESC_BASKETBALL = " " + PREFIX_CCA_NAME + VALID_CCA_NAME_BASKETBALL;
    public static final String CCA_NAME_DESC_BADMINTON = " " + PREFIX_CCA_NAME + VALID_CCA_NAME_BADMINTON;
    public static final String CCA_NAME_DESC_SWIMMING = " " + PREFIX_CCA_NAME + VALID_CCA_NAME_SWIMMING;
    public static final String CCA_NAME_DESC_TABLE_TENNIS = " " + PREFIX_CCA_NAME + VALID_CCA_NAME_TABLE_TENNIS;
    public static final String CCA_NAME_DESC_TENNIS = " " + PREFIX_CCA_NAME + VALID_CCA_NAME_TENNIS;
    public static final String CCA_NAME_DESC_VOLLEYBALL = " " + PREFIX_CCA_NAME + VALID_CCA_NAME_VOLLEYBALL;
    public static final String CCA_NAME_DESC_TRACK_AND_FIELD = " " + PREFIX_CCA_NAME + VALID_CCA_NAME_TRACK_AND_FIELD;
    public static final String CCA_NAME_DESC_GARDENING = " " + PREFIX_CCA_NAME + VALID_CCA_NAME_GARDENING;
    public static final String CCA_NAME_DESC_ACTING = " " + PREFIX_CCA_NAME + VALID_CCA_NAME_ACTING;

    // '*' not allowed in ccas
    public static final String INVALID_CCA_NAME_DESC = " " + PREFIX_CCA_NAME + "Basketball*";

    public static final String VALID_NAME_AMY = "Amy Bee";
    public static final String VALID_NAME_BOB = "Bob Choo";
    public static final String VALID_PHONE_AMY = "11111111";
    public static final String VALID_PHONE_BOB = "22222222";
    public static final String VALID_EMAIL_AMY = "amy@example.com";
    public static final String VALID_EMAIL_BOB = "bob@example.com";
    public static final String VALID_ADDRESS_AMY = "Block 312, Amy Street 1";
    public static final String VALID_ADDRESS_BOB = "Block 123, Bobby Street 3";
    public static final String VALID_ROLE_PRESIDENT = "President";
    public static final String VALID_ROLE_VICE_PRESIDENT = "Vice-President";
    public static final String VALID_ROLE_CAPTAIN = "Captain";
    public static final String VALID_ROLE_VICE_CAPTAIN = "Vice-Captain";
    public static final String VALID_ROLE_MEMBER = "Member";

    public static final String NAME_DESC_AMY = " " + PREFIX_NAME + VALID_NAME_AMY;
    public static final String NAME_DESC_BOB = " " + PREFIX_NAME + VALID_NAME_BOB;
    public static final String PHONE_DESC_AMY = " " + PREFIX_PHONE + VALID_PHONE_AMY;
    public static final String PHONE_DESC_BOB = " " + PREFIX_PHONE + VALID_PHONE_BOB;
    public static final String EMAIL_DESC_AMY = " " + PREFIX_EMAIL + VALID_EMAIL_AMY;
    public static final String EMAIL_DESC_BOB = " " + PREFIX_EMAIL + VALID_EMAIL_BOB;
    public static final String ADDRESS_DESC_AMY = " " + PREFIX_ADDRESS + VALID_ADDRESS_AMY;
    public static final String ADDRESS_DESC_BOB = " " + PREFIX_ADDRESS + VALID_ADDRESS_BOB;
    public static final String ROLE_DESC_MEMBER = " " + PREFIX_ROLE + VALID_ROLE_MEMBER;
    public static final String ROLE_DESC_PRESIDENT = " " + PREFIX_ROLE + VALID_ROLE_PRESIDENT;
    public static final String AMOUNT_DESC_ONE = " " + PREFIX_AMOUNT + "1";
    public static final String AMOUNT_DESC_FOUR = " " + PREFIX_AMOUNT + "4";

    public static final String INVALID_NAME_DESC = " " + PREFIX_NAME + "James&"; // '&' not allowed in names
    public static final String INVALID_PHONE_DESC = " " + PREFIX_PHONE + "911a"; // 'a' not allowed in phones
    public static final String INVALID_EMAIL_DESC = " " + PREFIX_EMAIL + "bob!yahoo"; // missing '@' symbol
    public static final String INVALID_ADDRESS_DESC = " " + PREFIX_ADDRESS; // empty string not allowed for addresses
    public static final String INVALID_ROLE_DESC = " " + PREFIX_ROLE + "member*"; // '*' not allowed in roles
    public static final String INVALID_CCA_DESC = " " + PREFIX_CCA_NAME + "Badminton*"; // '*' not allowed in ccas
    public static final String INVALID_AMOUNT_DESC = " " + PREFIX_AMOUNT + "-1"; // amount not allowed in ccas

    public static final String PREAMBLE_WHITESPACE = "\t  \r  \n";
    public static final String PREAMBLE_NON_EMPTY = "NonEmptyPreamble";

    public static final EditStudentCommand.EditPersonDescriptor DESC_AMY;
    public static final EditStudentCommand.EditPersonDescriptor DESC_BOB;

    static {
        DESC_AMY = new EditPersonDescriptorBuilder().withName(VALID_NAME_AMY)
                .withPhone(VALID_PHONE_AMY).withEmail(VALID_EMAIL_AMY).withAddress(VALID_ADDRESS_AMY).build();
        DESC_BOB = new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB)
                .withPhone(VALID_PHONE_BOB).withEmail(VALID_EMAIL_BOB).withAddress(VALID_ADDRESS_BOB).build();
    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - the returned {@link CommandResult} matches {@code expectedCommandResult} <br>
     * - the {@code actualModel} matches {@code expectedModel}
     */
    public static void assertCommandSuccess(Command command, Model actualModel, CommandResult expectedCommandResult,
            Model expectedModel) {
        try {
            CommandResult result = command.execute(actualModel);
            assertEquals(expectedCommandResult, result);
            assertEquals(expectedModel, actualModel);
        } catch (CommandException ce) {
            throw new AssertionError("Execution of command should not fail.", ce);
        }
    }

    /**
     * Convenience wrapper to {@link #assertCommandSuccess(Command, Model, CommandResult, Model)}
     * that takes a string {@code expectedMessage}.
     */
    public static void assertCommandSuccess(Command command, Model actualModel, String expectedMessage,
            Model expectedModel) {
        CommandResult expectedCommandResult = new CommandResult(expectedMessage);
        assertCommandSuccess(command, actualModel, expectedCommandResult, expectedModel);
    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - a {@code CommandException} is thrown <br>
     * - the CommandException message matches {@code expectedMessage} <br>
     * - the address book, filtered person list and selected person in {@code actualModel} remain unchanged
     */
    public static void assertCommandFailure(Command command, Model actualModel, String expectedMessage) {
        // we are unable to defensively copy the model for comparison later, so we can
        // only do so by copying its components.
        AddressBook expectedAddressBook = new AddressBook(actualModel.getAddressBook());
        List<Person> expectedFilteredList = new ArrayList<>(actualModel.getFilteredPersonList());

        assertThrows(CommandException.class, expectedMessage, () -> command.execute(actualModel));
        assertEquals(expectedAddressBook, actualModel.getAddressBook());
        assertEquals(expectedFilteredList, actualModel.getFilteredPersonList());
    }
    /**
     * Updates {@code model}'s filtered list to show only the person at the given {@code targetIndex} in the
     * {@code model}'s address book.
     */
    public static void showPersonAtIndex(Model model, Index targetIndex) {
        assertTrue(targetIndex.getZeroBased() < model.getFilteredPersonList().size());

        Person person = model.getFilteredPersonList().get(targetIndex.getZeroBased());
        final String[] splitName = person.getName().fullName.split("\\s+");
        model.updateFilteredPersonList(new NameContainsKeywordsPredicate(Arrays.asList(splitName[0])));

        assertEquals(1, model.getFilteredPersonList().size());
    }

}
