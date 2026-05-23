import java.util.ArrayList;
import java.util.List;

public class CommandParser {
    public static List<String> parse(String Input) {
        List<String> FinalString = new ArrayList<>();
        StringBuilder CurrentString = new StringBuilder();
        boolean inSingle = false;
        boolean inDouble = false;
        boolean escaped = false;

        for (int i = 0; i < Input.length(); i++) {
            char c = Input.charAt(i);

            if (escaped) {
                // Handle escaped character
                if (inDouble) {
                    if (c == '"' || c == '\\' || c == '$' || c == '`') {
                        CurrentString.append(c);
                    } else if (c == 'n') {
                        CurrentString.append('\n');
                    } else if (c == 'r') {
                        CurrentString.append('\r');
                    } else if (c == 't') {
                        CurrentString.append('\t');
                    } else {
                        CurrentString.append('\\').append(c);
                    }
                } else {
                    CurrentString.append(c);
                }
                escaped = false;
            }
            else if (c == '\\' && !inSingle) {
                escaped = true;
            }
            else if (c == '\'' && !inDouble) {
                inSingle = !inSingle;
            }
            else if (c == '"' && !inSingle) {
                if (inDouble) {
                    // Inside double quotes: this " ends the quote
                    inDouble = false;
                } else {
                    // Outside double quotes: this " starts a quote
                    inDouble = true;
                }
            }
            else if (c == '>' && !inSingle && !inDouble) {
                String op = ">";
                if (i + 1 < Input.length() && Input.charAt(i + 1) == '>') {
                    op = ">>";
                    i++;
                }
                if (CurrentString.length() == 1 && (CurrentString.charAt(0) == '1' || CurrentString.charAt(0) == '2')) {
                    char prefix = CurrentString.charAt(0);
                    CurrentString.setLength(0);
                    if (prefix == '1') {
                        FinalString.add(op);
                    } else {
                        FinalString.add("2" + op);
                    }
                } else {
                    if (!CurrentString.isEmpty()) {
                        FinalString.add(CurrentString.toString());
                        CurrentString.setLength(0);
                    }
                    FinalString.add(op);
                }
            }
            else if (c == ' ' && !inSingle && !inDouble) {
                if (!CurrentString.isEmpty()) {
                    FinalString.add(CurrentString.toString());
                    CurrentString.setLength(0);
                }
            }
            else {
                // Regular character: add to current string
                CurrentString.append(c);
            }
        }

        if (escaped) CurrentString.append('\\');
        if (!CurrentString.isEmpty()) FinalString.add(CurrentString.toString());
        return FinalString;
    }
}