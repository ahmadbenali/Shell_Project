import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import static java.lang.System.*;
import java.io.PrintStream;

public class EchoCommand extends BaseBuiltIn{
    @Override
    public void execute(List<String> args, ShellContext context) {


        ShellUtils.CommandData data=ShellUtils.extractRedirection(args);

        boolean isRedirect = data.isRedirect;
        List<String> commandPart=data.CommandParts;
        String WriteOnFile= data.WriteOnFile;

        if(args.size()>1) {
            if (isRedirect) {
                try {
                    File file = new File(context.getCurrentPath(), data.WriteOnFile);

                    // Ensure parent directories exist (fixes /tmp/owl/ cases)
                    if (file.getParentFile() != null) file.getParentFile().mkdirs();

                    // Use try-with-resources to ensure the file closes
                    try (PrintStream fileOut = new PrintStream(new FileOutputStream(file))) {

                        fileOut.println(String.join("",data.CommandParts.subList(1,data.CommandParts.size())));
                    }
                } catch (IOException e) {
                    System.err.println("echo: redirection failed: " + e.getMessage());
                }
            }
            else {
                out.println(HandleEcho(args));
            }
        }


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

    private static String HandleEcho(List<String> parts)
    {

        List<String> echoArgs = parts.subList(1, parts.size());
        //System.out.println(String.join(" ", echoArgs));
        return String.join(" ",echoArgs);
        // return the output

    }
    //process quotes after ECHO
    private static String processQuotes(String Input) {
        StringBuilder result = new StringBuilder();
        boolean insideSingleQuote = false;
        boolean insideDoubleQuote = false;
        boolean escapeNext = false;
        boolean lastWasSpace = false;

        for (int i = 0; i < Input.length(); i++) {
            char c = Input.charAt(i);

            if (escapeNext) {
                // Logic for handling the character AFTER a backslash
                if (insideDoubleQuote) {
                    if (c == '"' || c == '\\' || c == '$' || c == '`') {
                        result.append(c);
                    } else {
                        result.append('\\').append(c);
                    }
                } else {
                    result.append(c);
                }
                escapeNext = false;
                lastWasSpace = false; // Reset space tracking
                continue; // Skip further checks for this literal character
            }

            // 1. Backslash detection
            if (c == '\\' && !insideSingleQuote) {
                escapeNext = true;
                continue;
            }

            // 2. Single Quote handling
            if (c == '\'' && !insideDoubleQuote) {
                insideSingleQuote = !insideSingleQuote;
                continue;
            }

            // 3. Double Quote handling
            if (c == '"' && !insideSingleQuote) {
                insideDoubleQuote = !insideDoubleQuote;
                continue;
            }

            // 4. Space handling
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

        // Safety for trailing backslash
        if (escapeNext) result.append('\\');

        return result.toString();
    }
}
