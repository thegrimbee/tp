package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_MISSING_FIELD;
import static seedu.address.logic.parser.CliSyntax.PREFIX_AMOUNT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CCA_NAME;

import java.util.stream.Stream;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.RecordAttendanceCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.cca.Amount;
import seedu.address.model.cca.CcaName;


/**
 * Parses input arguments and creates a new RecordAttendanceCommand object.
 */
public class RecordAttendanceCommandParser implements Parser<RecordAttendanceCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the RecordAttendanceCommand
     * and returns a RecordAttendanceCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public RecordAttendanceCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_CCA_NAME, PREFIX_AMOUNT);

        if (!arePrefixesPresent(argMultimap, PREFIX_CCA_NAME, PREFIX_AMOUNT)
                || argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    MESSAGE_MISSING_FIELD,
                    RecordAttendanceCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_CCA_NAME);
        try {
            Index studentIndex = ParserUtil.parseIndex(argMultimap.getPreamble());
            CcaName ccaName = ParserUtil.parseCcaName(argMultimap.getValue(PREFIX_CCA_NAME).get());
            Amount amount = ParserUtil.parseAmount(argMultimap.getValue(PREFIX_AMOUNT).get());

            return new RecordAttendanceCommand(studentIndex, ccaName, amount);
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                            pe.getMessage(),
                            RecordAttendanceCommand.MESSAGE_USAGE), pe);
        }
    }

    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
