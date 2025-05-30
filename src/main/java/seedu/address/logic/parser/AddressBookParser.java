package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_UNKNOWN_COMMAND;

import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.commands.AddCcaToStudentCommand;
import seedu.address.logic.commands.AddRoleToStudentCommand;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CreateCcaCommand;
import seedu.address.logic.commands.CreateStudentCommand;
import seedu.address.logic.commands.DeleteCcaCommand;
import seedu.address.logic.commands.DeleteStudentCommand;
import seedu.address.logic.commands.EditCcaCommand;
import seedu.address.logic.commands.EditStudentCommand;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.RecordAttendanceCommand;
import seedu.address.logic.commands.RemoveCcaFromStudentCommand;
import seedu.address.logic.commands.RemoveRoleFromStudentCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses user input.
 */
public class AddressBookParser {

    /**
     * Used for initial separation of command word and args.
     */
    private static final Pattern BASIC_COMMAND_FORMAT = Pattern.compile("(?<commandWord>\\S+)(?<arguments>.*)");
    private static final Logger logger = LogsCenter.getLogger(AddressBookParser.class);

    /**
     * Parses user input into command for execution.
     *
     * @param userInput full user input string
     * @return the command based on the user input
     * @throws ParseException if the user input does not conform the expected format
     */
    public Command parseCommand(String userInput) throws ParseException {
        final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(userInput.trim());
        if (!matcher.matches()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }

        final String commandWord = matcher.group("commandWord");
        final String arguments = matcher.group("arguments");

        // Note to developers: Change the log level in config.json to enable lower level (i.e., FINE, FINER and lower)
        // log messages such as the one below.
        // Lower level log messages are used sparingly to minimize noise in the code.
        logger.fine("Command word: " + commandWord + "; Arguments: " + arguments);

        switch (commandWord) {

        case CreateStudentCommand.COMMAND_WORD:
            return new CreateStudentCommandParser().parse(arguments);

        case CreateCcaCommand.COMMAND_WORD:
            return new CreateCcaCommandParser().parse(arguments);

        case EditStudentCommand.COMMAND_WORD:
            return new EditStudentCommandParser().parse(arguments);

        case DeleteStudentCommand.COMMAND_WORD:
            return new DeleteStudentCommandParser().parse(arguments);

        case EditCcaCommand.COMMAND_WORD:
            return new EditCcaCommandParser().parse(arguments);

        case DeleteCcaCommand.COMMAND_WORD:
            return new DeleteCcaCommandParser().parse(arguments);

        case RecordAttendanceCommand.COMMAND_WORD:
            return new RecordAttendanceCommandParser().parse(arguments);

        case AddCcaToStudentCommand.COMMAND_WORD: // New case
            return new AddCcaToStudentCommandParser().parse(arguments);

        case RemoveCcaFromStudentCommand.COMMAND_WORD: // New case
            return new RemoveCcaFromStudentCommandParser().parse(arguments);

        case AddRoleToStudentCommand.COMMAND_WORD:
            return new AddRoleToStudentCommandParser().parse(arguments);

        case RemoveRoleFromStudentCommand.COMMAND_WORD:
            return new RemoveRoleFromStudentCommandParser().parse(arguments);

        case ClearCommand.COMMAND_WORD:
            return new ClearCommand();

        case FindCommand.COMMAND_WORD:
            return new FindCommandParser().parse(arguments);

        case ListCommand.COMMAND_WORD:
            return new ListCommand();

        case ExitCommand.COMMAND_WORD:
            return new ExitCommand();

        case HelpCommand.COMMAND_WORD:
            return new HelpCommand();

        default:
            logger.finer("This user input caused a ParseException: " + userInput);
            throw new ParseException(MESSAGE_UNKNOWN_COMMAND);
        }
    }

}
