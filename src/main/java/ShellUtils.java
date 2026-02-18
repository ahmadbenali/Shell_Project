import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ShellUtils {

    public static class CommandData
    {
        public List<String> ClearCommand;
        public boolean isStdout;
        public boolean isStderr;
        public boolean isAppend;
        public String WriteOnFile;
    }

    public static CommandData extractRedirection(List<String> CommandLine) {
        //data is all info about the input command with flags
        CommandData data = new CommandData();
        data.ClearCommand = CommandLine;
        data.WriteOnFile = null;
        data.isStdout = false;
        data.isStderr = false;
        data.isAppend = false;

        int stdoutIndex = CommandLine.indexOf(">");//index or -1
        int stderrIndex = CommandLine.indexOf("2>");
        int appendIndex = CommandLine.indexOf(">>");


        // If ">" is found and there is a filename after it
        if (stdoutIndex != -1 ) {
            data.isStdout = true;
            data.WriteOnFile = CommandLine.get(stdoutIndex + 1);
            // Get everything BEFORE the ">" as the command arguments
            data.ClearCommand = new ArrayList<>(CommandLine.subList(0, stdoutIndex));
        }
        else if(stderrIndex != -1)
        {
            data.isStderr=true;
            data.WriteOnFile = CommandLine.get(stderrIndex + 1);
            data.ClearCommand = new ArrayList<>(CommandLine.subList(0, stderrIndex));
        }
        else if(appendIndex != -1)
        {
            data.isAppend=true;
            data.WriteOnFile = CommandLine.get(appendIndex + 1);
            data.ClearCommand = new ArrayList<>(CommandLine.subList(0, appendIndex));
        }
        return data;
    }

    public static File prepareOutputFile(String fileName, String currentPath, String errorPrefix) {
        File file = new File(fileName);

        // Resolve relative vs absolute paths
        if (!file.isAbsolute()) {
            file = new File(currentPath, fileName);
        }

        // Handle parent directory creation
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            if (!parent.mkdirs()) {
                // Use the prefix to differentiate between 'echo' and external commands
                System.err.println(errorPrefix + ": " + parent.getPath() + ": Permission denied or directory creation failed");
                return null;
            }
        }
        return file;
    }


}
