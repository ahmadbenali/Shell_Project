import java.util.*;


public class Main {

    public static Map<String,Command> BuiltIn =new HashMap<>();

    public static void main(String[] args) throws Exception {
        //In future make a record instead of this
        BuiltIn.put("echo",new EchoCommand());
        BuiltIn.put("cd",new CdCommand());
        BuiltIn.put("pwd",new PwdCommand());
        BuiltIn.put("type",new TypeCommand());
        BuiltIn.put("exit",new ExitCommand());


        ShellContext context =new ShellContext();
        InitializeInput read = new InitializeInput(BuiltIn);


        while(true) {

            String input = read.ReadInputWithAutoComplete();

            // return an obj that have a parts and bunch of flags, one of these for detect redirect
            List<String> CommandLine = CommandParser.parse(input);

            String cmdName = CommandLine.get(0);
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
