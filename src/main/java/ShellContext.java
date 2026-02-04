import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.getenv;

//Encapsulation: Each shell object keeps track of its own location.
//Flexibility: You can have two shells open in different folders at the same time without them crashing into each other.
public class ShellContext {

    //Must not be static because it's can be different from window shell to other
    private String currentPath=System.getProperty("user.dir");
    private static final String home=System.getProperty("user.home");

    //Must not be static because it's can be different from window shell to other
    public  void ChangeDirectory(List<String> parts) throws IOException
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
            currentPath=home;
        }
        else{
            newDir=new File(currentPath,targetPath);
            if(newDir.exists() && newDir.isDirectory())
                currentPath= newDir.getCanonicalPath();
            else System.out.println("cd: " + parts.get(1) + ": No such file or directory");

        }
    }
    public void setCurrentPath(String currentPath) {
        this.currentPath = currentPath;
    }

    //Must be static because it's not an object instance, will not be diff from shell to shell
    public static String getPath(String inputPath)
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

    //parseInput for command and after command
    public static List<String> parseInput(String input)
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

    public String getCurrentPath() {
        return this.currentPath;
    }

    public static String getHome() {
        return home;
    }
}
