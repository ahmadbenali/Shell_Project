import java.util.List;

public class PwdCommand extends BaseBuiltIn{
    @Override
    public void execute(List<String> args, ShellContext context) {
        System.out.println(context.getCurrentPath());
    }
}
