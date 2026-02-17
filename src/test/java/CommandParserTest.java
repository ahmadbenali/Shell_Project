import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CommandParserTest {
    @Test
    void testParser()
    {
        // This just checks if the test can "see" your main code
        List<String> result = CommandParser.parse("echo hello");
        assertNotNull(result);

        //The result to pass the test OR not
        assertEquals("echo", result.getFirst());
    }
}
