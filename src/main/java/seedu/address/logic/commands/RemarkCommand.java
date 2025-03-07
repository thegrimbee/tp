package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

public class RemarkCommand extends Command {
    public static final String COMMAND_WORD = "remark";
    
    public static final String MESSAGE_USAGE = COMMAND_WORD
    + ": Edits the remark of the person identified "
    + "by the index number used in the last person listing. "
    + "Existing remark will be overwritten by the input.\n"
    + "Parameters: INDEX (must be a positive integer) "
    + "r/ [REMARK]\n"
    + "Example: " + COMMAND_WORD + " 1 "
    + "r/ Likes to swim.";

    public static final String MESSAGE_NOT_IMPLEMENTED_YET =
        "Remark command not implemented yet";

    @Override
    public CommandResult execute(Model model) throws CommandException {
        throw new CommandException(MESSAGE_NOT_IMPLEMENTED_YET);
    }
}
