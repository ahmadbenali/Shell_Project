import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class CommandParserTest {

    @Test
    void AppendTest()
    {
        //Triple quotes to write exact terminal commands
        String shellCommand = """
            echo "test\"insidequotes"hello\"
            """;

        List<String> result = CommandParser.parse(shellCommand.trim());

        String expectedOutput = "test\"insidequoteshello\"";

        assertEquals(expectedOutput, result.get(1), "NOOOOOOOOOOOOOOOOOOOOOOOO");
    }
}
