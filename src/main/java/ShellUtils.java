import java.util.ArrayList;
import java.util.List;

public class ShellUtils {

    public static class CommandData
    {
        public List<String> ClearCommand;
        public boolean isRedirect;
        public String WriteOnFile;
    }

    public static CommandData extractRedirection(List<String> CommandLine) {
        CommandData data = new CommandData();
        data.ClearCommand = CommandLine;
        data.WriteOnFile = null;
        data.isRedirect = false;

        int index = CommandLine.indexOf(">");//index or -1

        // If ">" is found and there is a filename after it
        if (index != -1 ) {
            data.isRedirect = true;
            data.WriteOnFile = CommandLine.get(index + 1);

            // Get everything BEFORE the ">" as the command arguments
            data.ClearCommand = new ArrayList<>(CommandLine.subList(0, index));
        }

        return data;
    }
}
