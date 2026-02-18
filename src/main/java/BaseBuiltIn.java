import java.util.List;

import static java.lang.System.*;

//Common things between commands BuiltIN
public abstract class BaseBuiltIn implements Command{

    public record BuiltIn(String cmd, Command command){}

    protected void reportError(String cmd , String message)
    {
        err.println(cmd + " : " + message );
    }

    protected boolean hasMinimumArgs(List<String> args , int min)
    {
        if(args.size()<min)
        {
            reportError(args.get(0),"Missing Operands.");
            return true;
        }
        return false;
    }

}
