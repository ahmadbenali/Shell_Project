import java.io.*;
import java.util.List;

public class EchoCommand extends BaseBuiltIn {
    @Override
    public void execute(List<String> CommandLine, ShellContext context) {

        ShellUtils.CommandData data = ShellUtils.extractRedirection(CommandLine);

        boolean isStdout = data.isStdout;
        boolean isStderr = data.isStderr;

        if (CommandLine.size() > 1) {
            //write the output command on file
            if (isStdout) {
                ExecuteOnFile(data, context.getCurrentPath());
            }
            //Content of the command appear on console and any message will go to stderr
            else if (isStderr) {
                System.out.println(HandleEcho(data.ClearCommand));
                prepareEmptyErrorFile(data.WriteOnFile, context.getCurrentPath(), "echo");

            } else {
                // Standard behavior (writing to console)
                System.out.println(HandleEcho(CommandLine));
            }
        }
    }

    private static String HandleEcho(List<String> CommandLine) {


        List<String> echoArgs = CommandLine.subList(1, CommandLine.size());
        //System.out.println(String.join(" ", echoArgs));
        return String.join(" ", echoArgs);
        // return the output

    }

    private static void ExecuteOnFile(ShellUtils.CommandData data, String currentPath) {
        List<String> commandPart = data.ClearCommand;
        String writeOnFile = data.WriteOnFile;

        try {
            File Stdout = ShellUtils.prepareOutputFile(writeOnFile,
                    currentPath, "echo");

            // Use try-with-resources to ensure the file closes
            try (PrintStream fileOut = new PrintStream(new FileOutputStream(Stdout))) {
                // Join the parts and write to the file
                fileOut.println(String.join(" ", commandPart.subList(1, commandPart.size())));
            }
        } catch (IOException e) {
            System.err.println("echo: redirection failed: " + e.getMessage());
        }
    }

    private void prepareEmptyErrorFile(String WriteOnFile, String currentPath, String errorMessage) {
        // 2> creates the file even if no error occurs
        File StderrFile = ShellUtils.prepareOutputFile(WriteOnFile, currentPath, errorMessage);


        try (FileOutputStream fos = new FileOutputStream(StderrFile)) {
        } catch (IOException e) {
        }
    }
}
