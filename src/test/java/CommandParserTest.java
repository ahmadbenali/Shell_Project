import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class CommandParserTest {
// ahmad
    @Test
    void AppendTest()
    {
        List<String> result = CommandParser.parse("echo hello >> output.txt");
        ShellContext context= new ShellContext();

        Command cmd = new EchoCommand();
        cmd.execute(result, context);
        // The parser should split "echo" and "first" into separate tokens
        //List<String> expected = List.of("echo", "hello", ">>", "output.txt");

        assertEquals("hello", result, "The parser should split arguments and identify '>>'");

    }
}
