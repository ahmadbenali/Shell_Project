import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


public class AutoCompletionTest {

    @Test
    public void autoCompletion() {

        String cmdName="xyz";
        String path = ShellContext.getPath(cmdName);

        assertNull(path);
    }

}
