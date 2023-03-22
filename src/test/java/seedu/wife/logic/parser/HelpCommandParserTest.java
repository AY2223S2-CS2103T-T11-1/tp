package seedu.wife.logic.parser;

import static seedu.wife.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.wife.commons.core.HelpMenu;
import seedu.wife.logic.commands.HelpCommand;

public class HelpCommandParserTest {

    private HelpCommandParser parser = new HelpCommandParser();

    @Test
    public void parse_emptyArgs_returnsGeneralHelpCommand() {
        //empty string
        HelpCommand expectedCommand = new HelpCommand(HelpMenu.getGeneralHelp());
        assertParseSuccess(parser, "", expectedCommand);

        //string with whitespace
        assertParseSuccess(parser, "        ", expectedCommand);
    }

    @Test
    public void parse_validCommand_returnsCommandSpecificHelpCommand() {
        //normal input
        HelpCommand expectedCommand = new HelpCommand(HelpMenu.ADD.getCommandUsage());
        assertParseSuccess(parser, "add", expectedCommand);

        //input with whitespaces
        assertParseSuccess(parser, "    add     ", expectedCommand);
    }

    @Test
    public void parse_invalidArg_returnsInvalidArgMessage() {
        //invalid string
        HelpCommand expectedCommand = new HelpCommand(HelpMenu.INVALID.getCommandUsage());
        assertParseSuccess(parser, "abcdef", expectedCommand);
    }
}
