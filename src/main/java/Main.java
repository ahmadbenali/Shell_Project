import java.io.File;
import java.io.IOException;
import java.util.*;
import static java.lang.System.*;


public class Main {

    private static String currentPath=System.getProperty("user.dir");

    private static final String home=System.getProperty("user.home");

    private static final Set<String> Shell_BuiltIn = Set.of("type","exit","echo","pwd","cd");

    private static String InitializeInput()
    {
        System.out.print("$ ");
        Scanner scanner=new Scanner(System.in);
        String input=scanner.nextLine();

        return input;

    }

    //@Nullable indicate that the variable can be Null
    //because getPath  it returns null if the command isn't found in the PATH.
    private static  String getPath(String inputPath)
    {
        //path is  Env variable, your program will ask os to check every file inside PATH
        //specifying a set of directories where executable programs are located.
        String pathEnv=System.getenv("PATH");

        if(pathEnv==null) return null;

        // because the input path will be "/usr/local/bin:/usr/bin:/bin:/usr/games"
        String []directories=pathEnv.split(":");

        for(String dir:directories)
        {
            //It doesn't actually create a file on your hard drive yet;
            // it simply creates a Java object that represents a potential file location.
            //Java intelligently combines these into a single path,
            //handling the slashes for you so you don't have to worry if dir ends with a / or not.
            File target=new File(dir,inputPath);
            if(target.exists() && target.canExecute())
                return target.getAbsolutePath();
        }
        return null;
    }

    private static void HandleBasicEcho(String AfterEcho)
    {
            if(AfterEcho.startsWith("'") && AfterEcho.endsWith("'"))
            {
                System.out.println(AfterEcho.replaceAll("'",""));
            }
            else
            {
                String NewAfterEcho = AfterEcho.replaceAll("\\s+", " ");
                System.out.println(NewAfterEcho.replaceAll("'", ""));
            }

    }

    private static void HandleEcho(String parts)
    {
        //String output = String.join(" ",parseInput(parts));
        String output=processQuotes(parts);

        System.out.println(output);
    }

    //@NotNull indicate that the variable can't be Null
    private static  String processQuotes( String Input)
    {
        StringBuilder result = new StringBuilder();
        boolean insideSingleQuote = false;
        boolean insideDoubleQuote = false;
        boolean escapeNext = false;//should next chat be escaped ?
        boolean lastWasSpace = false;


        for (int i = 0; i < Input.length(); i++) {
            char c = Input.charAt(i);


            if (escapeNext) {
                // Inside double quotes, only specific chars are escaped
                if (insideDoubleQuote) {
                    if (c == '"' || c == '\\' || c == '$' || c == '`') {
                        result.append(c); // Remove \ and add char
                    } else {
                        result.append('\\').append(c); // Keep \ and add char
                    }
                } else {
                    // Outside quotes, \ always escapes the next char
                    result.append(c);
                }
                escapeNext = false;
            }

            // Backslash detection

            if (c == '\\' && !insideSingleQuote) {
                escapeNext = true;
                continue;
            }


            if (c == '\'' && !insideDoubleQuote) {
                insideSingleQuote = !insideSingleQuote;
                continue; // Don't add the quote itself
            }

            if (c == '"' && !insideSingleQuote) {
                insideDoubleQuote = !insideDoubleQuote;
                continue; // Don't add the quote itself
            }

            if (c == ' ' && !insideSingleQuote && !insideDoubleQuote) {
                if (!lastWasSpace && result.length() > 0) {
                    result.append(c);
                    lastWasSpace = true;
                }
                continue;
            }
            result.append(c);
            lastWasSpace = false;
        }

        return result.toString();

    }

    private static List<String> parseInput(String input)
    {
        List<String> args = new ArrayList<>();
        StringBuilder current = new StringBuilder();

        boolean inSingle = false;//inside Single
        boolean inDouble = false;//inside double
        boolean escaped = false;//space

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);

            if (escaped) {
                // Check double quote rules: \ only escapes " \ $ ` and newline
                if (inDouble) {
                    if (c == '"' || c == '\\' || c == '$' || c == '`') {
                        current.append(c);
                    } else {
                        current.append('\\').append(c); // Keep literal backslash
                    }
                } else {
                    current.append(c); // Outside quotes, \ always escapes
                }
                escaped = false;
            } else if (c == '\\' && !inSingle) {
                escaped = true; // Trigger escape mode for next char
            } else if (c == '\'' && !inDouble) {
                inSingle = !inSingle; // Toggle single quotes
            } else if (c == '"' && !inSingle) {
                inDouble = !inDouble; // Toggle double quotes
            } else if (c == ' ' && !inSingle && !inDouble) {
                // Split into new argument on unquoted space
                if (current.length() > 0) {
                    args.add(current.toString());
                    current.setLength(0);
                }
            } else {
                current.append(c);
            }
        }
        if (escaped) current.append('\\'); // Handle trailing backslash
        if (current.length() > 0) args.add(current.toString());

        return args;
    }

    private static void TypeCommand(String Input)
    {
        if (Shell_BuiltIn.contains(Input))
            System.out.println(Input + " is a shell builtin");
        else
        {
            String path=getPath(Input);
            if(path !=null) System.out.println(Input +" is "+path);
            else System.out.println(Input +" not found");
        }
    }

    private static void ChangeDirectory(List<String>  parts) throws IOException
    {
        String targetPath = parts.get(1);
        File newDir ;

        if(targetPath.startsWith("/"))
        {
            newDir=new File(targetPath);
            if(newDir.exists() && newDir.isDirectory())
                // To convert java.io.file to java.lang.String
                currentPath=newDir.getAbsolutePath();
            else System.out.println("cd: " + parts.get(1) + ": No such file or directory");
        }
        else if(targetPath.equals("~"))
        {
            currentPath=getenv("HOME");
        }
        else{
            newDir=new File(currentPath,targetPath);
            if(newDir.exists() && newDir.isDirectory())
                currentPath= newDir.getCanonicalPath();
            else System.out.println("cd: " + parts.get(1) + ": No such file or directory");

        }
    }

    private static void ExecuteExternalCommand(String command,List<String>  parts)
    {
        String path = getPath(command);

       // List<String> fullCommand = new ArrayList<>();
        //fullCommand.add(command);
        if (parts.size() > 1) {
           // fullCommand.addAll(parseInput(parts.get(1)));
        }
        if(path != null) {
            try {
                //It takes your array of strings (e.g., ["custom_exe_1234", "Alice"]) and tells Java,
                //"I want to run the program named in index 0 and pass the rest of the strings as arguments to it".
                ProcessBuilder pb = new ProcessBuilder(parts);

                pb.directory(new File(currentPath));
                //Without this, the program (like custom_exe_1234) would run in the background,
                // but you wouldn't see its output on your screen.
                // By using inheritIO, when the program prints "Hello Alice",
                // that message appears in your shell.
                pb.inheritIO();

                //It tells the Operating System to actually
                //find the executable and begin running it as a separate process.
                pb.start().waitFor();

                //It forces your while(true) loop to stop and wait
                //until the external program finishes running.

            }
            catch (Exception e){
                System.exit(0);
            }
        }
        else System.out.println(command+": command not found");
    }

    public static void main(String[] args) throws Exception {


        while(true)
        {
            String input =InitializeInput();

            //List<String> parts=new ArrayList<>(List.of(input.split(" ", 2)));

            // will be 0 1 and 2
            //problem in ECHO
            List<String> parts=parseInput(input);
            //String []parts=input.split(" ",2);

            String command= parts.get(0);

            // This "exit".equals(command) for NULL safe
            switch (command) {
                case "exit" ->exit(0);

                case "echo" ->{
                    if(parts.size()>1)
                    {
                        String []AfterEcho=input.split(" ",2);
                        HandleEcho(AfterEcho[1]);
                        //HandleEcho(String.join(" ", parts.subList(1, parts.size())));
                    }
                    else System.out.println();
                }

                case "type" ->{
                    if(parts.size()>1) {
                       TypeCommand(parts.get(1));
                    }
                }

                case "pwd" -> {
                    //It returns the Current Working Directory
                    //(the folder where the user was when they ran your Java program).
                    //System.getProperty("user.name") return the name of user currently logged in os
                    System.out.println(currentPath);
                }

                case "cd" -> {
                    if(parts.size()>1)
                    {
                        //Absolute path
                        ChangeDirectory(parts);
                    }

                }

                default -> {
//                    out.println(command);
//                    out.println(parts.get(1));
                    ExecuteExternalCommand(command,parts);
                }
            }


        }


    }
}
