import java.util.List;

import static java.lang.System.exit;

public class ExitCommand implements Command{
    @Override
    public void execute(List<String> args, ShellContext context) {
        exit(0);
    }
}
