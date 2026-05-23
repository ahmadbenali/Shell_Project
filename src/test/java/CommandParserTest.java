import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class CommandParserTest {


    void AppendTest()
    {
        List<String> result = CommandParser.parse("echo \"test\"insidequotes\"script\"");

        String expectedOutput = "\"test\"insidequotesscript\"\"";

        //the message appear on failure
        assertEquals(expectedOutput, result.get(1), "should be the same string: ");

    }
}
