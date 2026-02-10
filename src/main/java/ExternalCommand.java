import java.io.File;
import java.io.IOException;
import java.util.List;
import static java.lang.System.*;

public class ExternalCommand implements Command {

    @Override
    public void execute(List<String> args, ShellContext context) {
        // Separate the command from the redirection info
        ShellUtils.CommandData data = ShellUtils.extractRedirection(args);

        try {
            ProcessBuilder processBuilder = new ProcessBuilder(data.CommandParts);
            processBuilder.directory(new File(context.getCurrentPath()));

            if (data.isRedirect) {
                // Handle the output file path
                File outputFile = resolveOutputFile(data.WriteOnFile, context.getCurrentPath());

                // Create parent directories if needed
                File parent = outputFile.getParentFile();
                if (parent != null && !parent.exists()) {
                    if (!parent.mkdirs()) {
                        err.println("Error: Could not create directory " + parent.getPath());
                        return;
                    }
                }

                // Redirect stdout to file
                processBuilder.redirectOutput(ProcessBuilder.Redirect.to(outputFile));

                // Still show errors on stderr (optional: redirect errors too)
                processBuilder.redirectError(ProcessBuilder.Redirect.INHERIT); // Keep stderr separate

            } else {
                processBuilder.inheritIO();
            }

            Process process = processBuilder.start();
            int exitCode = process.waitFor();

            // Optional: handle non-zero exit codes
            if (exitCode != 0 && !data.isRedirect) {
                // Error already shown via inheritIO
            }

        } catch (IOException e) {
            err.println("Error executing command: " + e.getMessage());
        } catch (InterruptedException e) {
            err.println("Command interrupted: " + e.getMessage());
            Thread.currentThread().interrupt(); // Restore interrupt status
        }
    }

    /**
     * Resolves the output file path relative to the current directory
     */
    private File resolveOutputFile(String filePath, String currentPath) {
        File file = new File(filePath);

        // If absolute path, use as-is
        if (file.isAbsolute()) {
            return file;
        }

        // If relative path, resolve against current directory
        return new File(currentPath, filePath);
    }
}