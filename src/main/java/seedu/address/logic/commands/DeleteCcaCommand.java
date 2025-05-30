package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.cca.Cca;

/**
 * Deletes a CCA identified using it's displayed index from the address book.
 */
public class DeleteCcaCommand extends Command {

    public static final String COMMAND_WORD = "delete_c";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the CCA identified by the index number used in the displayed CCA list.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_DELETE_CCA_SUCCESS = "Deleted CCA: %1$s";

    private final Index targetIndex;

    /**
     * Creates a DeleteCcaCommand to delete the {@code Cca} specified by the index.
     */
    public DeleteCcaCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Cca> lastShownList = model.getCcaList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_CCA_DISPLAYED_INDEX);
        }

        Cca ccaToDelete = lastShownList.get(targetIndex.getZeroBased());
        model.deleteCca(ccaToDelete);
        return new CommandResult(String.format(MESSAGE_DELETE_CCA_SUCCESS, Messages.format(ccaToDelete)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DeleteCcaCommand)) {
            return false;
        }

        DeleteCcaCommand otherDeleteCommand = (DeleteCcaCommand) other;
        return targetIndex.equals(otherDeleteCommand.targetIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .toString();
    }
}
