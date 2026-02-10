import java.util.ArrayList;
import java.util.List;

public class CommandParser {
    public static List<String> parse(String Input )
    {
        List<String> FinalString = new ArrayList<>();
        StringBuilder CurrentString = new StringBuilder();

        boolean inSingle = false;//inside Single
        boolean inDouble = false;//inside double
        boolean escaped = false;

        for (int i = 0; i < Input.length(); i++) {
            char c = Input.charAt(i);

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

                // Check if the previous character was '1' to support '1>'
                if (CurrentString.length() == 1 && CurrentString.charAt(0) == '1') {
                    // It's '1>', we clear the '1' so it doesn't stay in the arguments
                    CurrentString.setLength(0);
                } else if (!CurrentString.isEmpty()) {
                    // If it was a word like "echo", finish it
                    FinalString.add(CurrentString.toString());
                    CurrentString.setLength(0);
                }
                // Add the ">" as the redirect marker
                FinalString.add(">");
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
}
