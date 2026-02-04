import java.util.List;

import static java.lang.System.*;

public class EchoCommand extends BaseBuiltIn{
    @Override
    public void execute(List<String> args, ShellContext context) {

        if(args.size()>1) HandleEcho(String.join(" ", args.subList(1, args.size())));
        else out.println();
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

        out.println(output);
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
