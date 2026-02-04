import java.util.List;

public class TypeCommand extends BaseBuiltIn{
    @Override
    public void execute(List<String> args, ShellContext context) {

        String cmd=args.get(1);

        if (Main.BuiltIn.containsKey(cmd))
            System.out.println(cmd+ " is a shell builtin");
        else
        {
            String path=ShellContext.getPath(cmd);
            if(path !=null) System.out.println(cmd +" is "+path);
            else System.out.println(cmd +" not found");
        }
    }

}
