import java.io.File;
import java.io.IOException;
import java.util.List;

import static java.lang.System.*;

public class CdCommand extends BaseBuiltIn{

    //This class will ineharate from baseclass the command interface
    @Override
    public void execute(List<String> args, ShellContext context) {
        if(hasMinimumArgs(args,2))
            return;
        try{
            context.ChangeDirectory(args);
        } catch (IOException e) {
            reportError("cd",args.get(1)+": No such directory");
        }
    }


}
