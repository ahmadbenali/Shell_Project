import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class CommandParserTest {

    @Test
    void AppendTest()
    {
        List<String> result = CommandParser.parse("echo hello >> output.txt");
        // The parser should split "echo" and "first" into separate tokens
        List<String> expected = List.of("echo", "hello", ">>", "output.txt");

        assertEquals(expected, result, "The parser should split arguments and identify '>>'");

    }
}
