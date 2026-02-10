import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import java.io.PrintStream;

public class EchoCommand extends BaseBuiltIn{
    @Override
    public void execute(List<String> CommandLine, ShellContext context) {


        ShellUtils.CommandData data = ShellUtils.extractRedirection(CommandLine);

        boolean isRedirect = data.isRedirect;
        List<String> commandPart = data.ClearCommand;
        String writeOnFile = data.WriteOnFile;

        if (CommandLine.size() > 1) {
            if (isRedirect) {
                try {
                    File Stdout = resolveOutputFile(data.WriteOnFile, context.getCurrentPath());

                    // --- Robust Directory Creation ---
                    File parent = Stdout.getParentFile();
                    if (parent != null && !parent.exists()) {
                        // Check the return value to satisfy the IDE and handle errors
                        if (!parent.mkdirs()) {
                            System.err.println("echo: " + parent.getPath() + ": Permission denied or directory creation failed");
                            return; // Stop execution if we can't create the path
                        }
                    }

                    // Use try-with-resources to ensure the file closes
                    try (PrintStream fileOut = new PrintStream(new FileOutputStream(Stdout))) {
                        // Join the parts and write to the file
                        fileOut.println(String.join(" ", commandPart.subList(1, commandPart.size())));
                    }
                } catch (IOException e) {
                    System.err.println("echo: redirection failed: " + e.getMessage());
                }
            } else {
                // Standard behavior (writing to console)
                System.out.println(HandleEcho(CommandLine));
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

    private static String HandleEcho(List<String> CommandLine)
    {

        List<String> echoArgs = CommandLine.subList(1, CommandLine.size());
        //System.out.println(String.join(" ", echoArgs));
        return String.join(" ",echoArgs);
        // return the output

    }

    private File resolveOutputFile(String filePath, String currentPath) {
        File file = new File(filePath);

        // If absolute path, use as-is
        if (file.isAbsolute()) {
            return file;
        }

        // If relative path, resolve against current directory
        return new File(currentPath, filePath);
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
