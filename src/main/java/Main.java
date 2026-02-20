import java.util.*;
import static java.lang.System.*;

public class Main {

    private static String InitializeInput()
    {
        out.print("$ ");
        Scanner scanner=new Scanner(in);//System.in
        return scanner.nextLine();

    }
    public static Map<String,Command> BuiltIn =new HashMap<>();

    public static void main(String[] args) throws Exception {


        ShellContext context =new ShellContext();



        BuiltIn.put("echo",new EchoCommand());
        BuiltIn.put("cd",new CdCommand());
        BuiltIn.put("pwd",new PwdCommand());
        BuiltIn.put("type",new TypeCommand());
        BuiltIn.put("exit",new ExitCommand());

        while(true) {

            String input =InitializeInput();

            // return an obj that have a parts and bunch of flags, one of these for detect redirect
            List<String> CommandLine = CommandParser.parse(input);

            String cmdName = CommandLine.getFirst();
            Command cmd = BuiltIn.get(cmdName);


            if (cmd != null) {
                cmd.execute(CommandLine, context);
            }
            else
            {
                String path = ShellContext.getPath(cmdName);
                if(path != null)
                {
                    Command External =new ExternalCommand();
                    External.execute(CommandLine,context);
                }
                else System.out.println(cmdName+": command not found");
            }

        }
    }
}
