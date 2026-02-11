import java.io.File;
import java.io.IOException;
import java.util.List;
import static java.lang.System.*;

public class ExternalCommand implements Command {

    @Override
    public void execute(List<String> CommandLine, ShellContext context)
    {
        // Separate the command from the redirection info
        ShellUtils.CommandData data = ShellUtils.extractRedirection(CommandLine);

        try {
            ProcessBuilder processBuilder = new ProcessBuilder(data.ClearCommand);
            processBuilder.directory(new File(context.getCurrentPath()));

            if (data.isStdout)
            {
                Stdout(data, context, processBuilder);
            }
            else if(data.isStderr)
            {
                Stderr(data, context, processBuilder);
            }
            else {
                processBuilder.inheritIO();
            }

            Process process = processBuilder.start();
            process.waitFor();

        } catch (IOException e) {
            err.println("Error executing command: " + e.getMessage());
        } catch (InterruptedException e) {
            err.println("Command interrupted: " + e.getMessage());
            Thread.currentThread().interrupt(); // Restore interrupt status
        }
    }

    private void Stderr(ShellUtils.CommandData data , ShellContext context ,ProcessBuilder processBuilder)
    {
        File stderr = ShellUtils.prepareOutputFile(data.WriteOnFile,
                context.getCurrentPath(), data.ClearCommand.getFirst());

        assert stderr != null;
        processBuilder.redirectError(stderr);
        // Normal output (if any) should still go to the console
        processBuilder.redirectOutput(ProcessBuilder.Redirect.INHERIT); // Keep stderr separate
    }

    private void Stdout(ShellUtils.CommandData data , ShellContext context ,ProcessBuilder processBuilder)
    {
        // Handle the output file path
        File stdout = ShellUtils.prepareOutputFile(data.WriteOnFile,
                context.getCurrentPath(), data.ClearCommand.getFirst());

        // Redirect stdout to file
        assert stdout != null;
        processBuilder.redirectOutput(ProcessBuilder.Redirect.to(stdout));
        // Normal output (if any) should still go to the console
        processBuilder.redirectError(ProcessBuilder.Redirect.INHERIT);
    }

}