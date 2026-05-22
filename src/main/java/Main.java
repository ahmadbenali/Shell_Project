import java.util.*;
import java.awt.Toolkit;

public class Main {

    public static Map<String,Command> BuiltIn =new HashMap<>();

    public static void main(String[] args) throws Exception {
        //In future make a record instead of this
        //why built-in ?They are integrated for performance (no need to start a new process)
        //or because they must change the shell's own internal state (like cd changing the current directory).
        BuiltIn.put("echo",new EchoCommand());
        BuiltIn.put("cd",new CdCommand());
        BuiltIn.put("pwd",new PwdCommand());
        BuiltIn.put("type",new TypeCommand());
        //BuiltIn.put("exit",new )



        ShellContext context =new ShellContext();
        //To solve the JLine native library issue
        //System.setProperty("org.jline.terminal.provider", "exec");
        InitializeInput read = new InitializeInput(BuiltIn);


        while(true) {

            String input = read.ReadInputWithAutoComplete();

            // return an obj that have a parts and bunch of flags, one of these for detect redirect
            List<String> CommandLine = CommandParser.parse(input);

            String cmdName = CommandLine.get(0);
            Command cmd = BuiltIn.get(cmdName);


            //This is wrong because the IDE tell you to put return of break inside while to exit
            //You can delete class exit
            if(cmdName.equals("exit")) {
                System.exit(0);
            }

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
                else {
                    System.out.println(cmdName+": command not found");
                    Toolkit.getDefaultToolkit().beep();
                }
            }
        }
    }
}
