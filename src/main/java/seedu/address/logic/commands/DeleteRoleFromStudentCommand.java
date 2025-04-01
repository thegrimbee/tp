package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CCA;

import java.util.List;
import java.util.Optional;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.cca.Cca;
import seedu.address.model.cca.CcaName;
import seedu.address.model.person.Person;

/**
 * Deletes a role and sets to a default role of a specified cca from a student identified
 * using it's displayed index from the address book.
 */
public class DeleteRoleFromStudentCommand extends Command {

    public static final String COMMAND_WORD = "delete_r";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Deletes a role from the student identified "
            + "by the index number used in the displayed student list. "
            + "Parameters: INDEX (must be a positive integer) "
            + PREFIX_CCA + "CCA_NAME\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_CCA + "Basketball";

    public static final String MESSAGE_DELETE_ROLE_FROM_STUDENT_SUCCESS = "Deleted role from student: %1$s";
    public static final String MESSAGE_ROLE_NOT_ASSIGNED = "This student does not have a role in this CCA.";

    private final Index studentIndex;
    private final CcaName ccaName;

    /**
     * @param studentIndex of the student in the filtered student list to delete role
     * @param ccaName of the CCA to delete role
     */
    public DeleteRoleFromStudentCommand(Index studentIndex, CcaName ccaName) {
        requireAllNonNull(studentIndex, ccaName);
        this.studentIndex = studentIndex;
        this.ccaName = ccaName;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownPersonList = model.getFilteredPersonList();
        List<Cca> lastCcaList = model.getCcaList();

        if (studentIndex.getZeroBased() >= lastShownPersonList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }
        Person personToDeleteRole = lastShownPersonList.get(studentIndex.getZeroBased());

        Optional<Cca> searchedCca = findCcaWithCcaName(lastCcaList, ccaName);
        if (searchedCca.isEmpty()) {
            throw new CommandException(Messages.MESSAGE_CCA_NOT_FOUND);
        }
        Cca targetCca = searchedCca.get();

        if (!personToDeleteRole.hasCca(targetCca)) {
            throw new CommandException(Messages.MESSAGE_CCA_NOT_IN_PERSON);
        }

        if (personToDeleteRole.isDefaultRoleInCca(targetCca)) {
            throw new CommandException(MESSAGE_ROLE_NOT_ASSIGNED);
        }

        Person personWithDeletedRole = personToDeleteRole.deleteRole(targetCca);

        try {

            model.setPerson(personToDeleteRole, personWithDeletedRole);

        } catch (IllegalArgumentException e) {
            throw new CommandException(Messages.MESSAGE_CCA_NOT_FOUND);
        }

        model.updateFilteredPersonList(Model.PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(
                String.format(MESSAGE_DELETE_ROLE_FROM_STUDENT_SUCCESS, personWithDeletedRole));
    }

    /**
     * Finds a {@code Cca} from a list of {@code Cca} with the given {@code CcaName}.
     *
     * @param ccaList list of {@code Cca} to search from
     * @param ccaName {@code CcaName} to search for
     * @return {@code Optional<Cca>} containing the {@code Cca} if found, otherwise {@code Optional.empty()}
     */
    public Optional<Cca> findCcaWithCcaName(List<Cca> ccaList, CcaName ccaName) {
        for (Cca cca : ccaList) {
            if (cca.getCcaName().equals(ccaName)) {
                return Optional.of(cca);
            }
        }
        return Optional.empty();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof DeleteRoleFromStudentCommand)) {
            return false;
        }

        DeleteRoleFromStudentCommand otherCommand = (DeleteRoleFromStudentCommand) other;
        return studentIndex.equals(otherCommand.studentIndex)
                && ccaName.equals(otherCommand.ccaName);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("studentIndex", studentIndex)
                .add("ccaName", ccaName)
                .toString();
    }
}
