package seedu.wife.logic.parser;

import static seedu.wife.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.wife.commons.core.HelpMenu;
import seedu.wife.logic.commands.EditCommand;
import seedu.wife.logic.commands.HelpCommand;
import seedu.wife.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new HelpCommand object
 */
public class HelpCommandParser implements Parser<HelpCommand>{

    /**
     * Parses the given {@code String} of arguments in the context of the HelpCommand
     * and returns a HelpCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public HelpCommand parse(String args) throws ParseException {
        try {
            assert !args.equals(null);
            String trimmedArgs = args.trim();
            if (trimmedArgs.isEmpty()) {
                return new HelpCommand(HelpMenu.getGeneralHelp());
            }

            HelpMenu helpCommand = HelpMenu.parseCommand(trimmedArgs);
            return new HelpCommand(HelpMenu.getCommandHelp(helpCommand));
        } catch (Exception e) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }
    }
}
