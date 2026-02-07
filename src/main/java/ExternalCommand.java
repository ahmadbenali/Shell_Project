import java.io.File;
import java.util.List;

public class ExternalCommand implements Command{

    @Override
    public void execute(List<String> args, ShellContext context) {
        // Separate the command from the redirection info
        ShellUtils.CommandData data = ShellUtils.extractRedirection(args);

        try {
            ProcessBuilder pb = new ProcessBuilder(data.CommandParts);
            pb.directory(new File(context.getCurrentPath()));

            if (data.isRedirect) {
                // Ensure the file is created in the current directory
                File outputFile = new File(context.getCurrentPath(), data.WriteOnFile);

                // This is the missing piece!
                // It creates any missing folders like '/tmp/rat/' automatically.
                if (outputFile.getParentFile() == null) outputFile.getParentFile().mkdirs();
                pb.redirectOutput(outputFile);

            } else {
                pb.inheritIO();
            }

            Process process = pb.start();
            process.waitFor();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
