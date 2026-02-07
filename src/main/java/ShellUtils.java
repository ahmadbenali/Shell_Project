import java.util.ArrayList;
import java.util.List;

public class ShellUtils {

    public static class CommandData
    {
        public List<String> CommandParts;
        public boolean isRedirect;
        public String WriteOnFile;
    }

    public static CommandData extractRedirection(List<String> parts) {
        CommandData data = new CommandData();
        data.CommandParts = parts;
        data.WriteOnFile = null;
        data.isRedirect = false;

        int index = parts.indexOf(">");//index or -1

        // If ">" is found and there is a filename after it
        if (index != -1 ) {
            data.isRedirect = true;
            data.WriteOnFile = parts.get(index + 1);

            // Get everything BEFORE the ">" as the command arguments
            data.CommandParts = new ArrayList<>(parts.subList(0, index));
        }

        return data;
    }
}
