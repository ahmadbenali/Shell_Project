import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class CommandParserTest {

    @Test
    void AppendTest()
    {
        List<String> result = CommandParser.parse("ls /tmp/baz >> /tmp/bar/bar.md");
        // The parser should split "echo" and "first" into separate tokens
        List<String> expected = List.of("ls", "/tmp/baz", ">>", "/tmp/bar/bar.md");

        assertEquals(expected, result, "The parser should split arguments and identify '>>'");

    }
}
