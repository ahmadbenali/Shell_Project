import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class CommandParserTest {

    @Test
    void AppendTest()
    {
        List<String> result = CommandParser.parse("echo hello 2>> output.txt");

        List<String> expected = List.of("echo", "hello", "2>>", "output.txt");

        //the message appear on failure
        assertEquals(expected, result, "The parser should split arguments and identify '>>'");

    }
}
