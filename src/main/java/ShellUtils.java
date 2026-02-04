import java.util.ArrayList;
import java.util.List;

// File: ShellUtils.java
public class ShellUtils {

    // The data container (Inner Class)
    public static class CommandData {
        public List<String> args;
        public String redirectFile;
        public boolean isRedirected;
    }

    // The logic method
    public static CommandData extractRedirection(List<String> parts) {
        CommandData data = new CommandData();
        int index = parts.indexOf(">");

        if (index != -1 && index < parts.size() - 1) {
            data.isRedirected = true;
            data.redirectFile = parts.get(index + 1);
            data.args = new ArrayList<>(parts.subList(0, index));
        } else {
            data.isRedirected = false;
            data.args = new ArrayList<>(parts); // Safely copy the list
        }
        return data;
    }
}