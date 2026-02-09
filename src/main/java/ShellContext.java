import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


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
        List<String> FinalString = new ArrayList<>();
        StringBuilder CurrentString = new StringBuilder();

        boolean inSingle = false;//inside Single
        boolean inDouble = false;//inside double
        boolean escaped = false;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);

            if (escaped) {
                // Check double quote rules: \ only escapes " \ $ ` and newline
                if (inDouble) {
                    if (c == '"' || c == '\\' || c == '$' || c == '`') {
                        CurrentString.append(c);
                    } else {
                        CurrentString.append('\\').append(c); // Keep literal backslash
                    }
                } else {
                    CurrentString.append(c); // Outside quotes, \ always escapes
                }
                escaped = false;
            }else if (c == '>' && !inSingle && !inDouble) {
                // Check if previous character was '1' or '2' (file descriptor)
                String lastToken = null;
                if (!FinalString.isEmpty()) {
                    lastToken = FinalString.get(FinalString.size() - 1);
                }

                // Check if we have a pending digit in CurrentString (like building "1")
                String currentContent = CurrentString.toString();

                // Case 1: "1>" or "2>" as separate tokens (e.g., "echo hello 1 > file")
                if (lastToken != null && (lastToken.equals("1") || lastToken.equals("2"))) {
                    // Remove the last token and combine it with >
                    FinalString.remove(FinalString.size() - 1);
                    FinalString.add(lastToken + ">");
                }
                // Case 2: "1>" or "2>" being built in current string (e.g., "echo hello1>file")
                else if (currentContent.endsWith("1") || currentContent.endsWith("2")) {
                    // Extract everything except the last digit
                    String beforeDigit = currentContent.substring(0, currentContent.length() - 1);
                    String digit = currentContent.substring(currentContent.length() - 1);

                    // Add the part before digit if exists
                    if (!beforeDigit.isEmpty()) {
                        FinalString.add(beforeDigit);
                    }

                    // Add "1>" or "2>"
                    FinalString.add(digit + ">");
                    CurrentString.setLength(0);
                }
                // Case 3: Regular ">" redirection
                else {
                    // Finish any pending word first
                    if (!CurrentString.isEmpty()) {
                        FinalString.add(CurrentString.toString());
                        CurrentString.setLength(0);
                    }
                    // Add ">" or treat as "1>" (stdout is default)
                    FinalString.add(">");
                }
            }
            else if (c == '\\' && !inSingle) {
                escaped = true; // Trigger escape mode for next char
            } else if (c == '\'' && !inDouble) {
                inSingle = !inSingle; // Toggle single quotes
            } else if (c == '"' && !inSingle) {
                inDouble = !inDouble; // Toggle double quotes
            } else if (c == ' ' && !inSingle && !inDouble) {
                // Split into new argument on unquoted space
                if (!CurrentString.isEmpty()) {
                    FinalString.add(CurrentString.toString());
                    CurrentString.setLength(0);
                }
            } else {
                CurrentString.append(c);
            }
        }
        if (escaped) CurrentString.append('\\'); // Handle trailing backslash
        if (!CurrentString.isEmpty()) FinalString.add(CurrentString.toString());

        return FinalString;
        //return args,flag
    }

    public String getCurrentPath() {
        return this.currentPath;
    }


}
