import java.util.*;


public class Main {

    private static String InitializeInput()
    {
        System.out.print("$ ");
        Scanner scanner=new Scanner(System.in);
        return scanner.nextLine();

    }

    public static Map<String,Command> BuiltIn =new HashMap<>();

    public static void main(String[] args) throws Exception {


        ShellContext context =new ShellContext();

        //In future make a record instted of this
        BuiltIn.put("echo",new EchoCommand());
        BuiltIn.put("cd",new CdCommand());
        BuiltIn.put("pwd",new PwdCommand());
        BuiltIn.put("type",new TypeCommand());
        BuiltIn.put("exit",new ExitCommand());

        while(true) {

            String input =InitializeInput();
            List<String> parts = ShellContext.parseInput(input);
            String cmdName = parts.getFirst();
            Command cmd = BuiltIn.get(cmdName);


            if (cmd != null) {
                cmd.execute(parts, context);
            }
            else
            {
                String path = ShellContext.getPath(cmdName);
                if(path != null)
                {
                    Command External =new ExternalCommand();
                    External.execute(parts,context);
                }
                else System.out.println(cmdName+": command not found");
            }//l

        }
    }
}
